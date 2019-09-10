package auto.ausiot.ausiotrest.tasks;

import auto.ausiot.ausiotrest.model.*;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import auto.ausiot.ausiotrest.repository.ScheduleRuntimeRepository;
import auto.ausiot.ausiotrest.repository.UnitDetailsRepository;
import auto.ausiot.ausiotrest.util.Util;
import mqtt.Constants;
import mqtt.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ScheduleMaster  implements Job {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleMaster.class);

    @Autowired
    ScheduleTask st;
    static ScheduleRepository srepo;
    @Autowired
    ScheduleRepository scheduleRepository;

    static ScheduleRuntimeRepository sruntimerepo;

    @Autowired
    ScheduleRuntimeRepository scheduleRuntimeRepository;


    static UnitDetailsRepository unitdetailrepo;

    @Autowired
    UnitDetailsRepository unitDetailsRepository;


    public ScheduleMaster() throws SchedulerException {
        ScheduleTask st = new ScheduleTask();
        //st.start();
    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        List lst = srepo.findAll();
        for (int i = 0; i < lst.size(); i++) {
            logger.debug(lst.get(i).toString());
            Schedule s =  (Schedule)lst.get(i);
            runSchedules(s);
        }
    }

    public void runSchedules(Schedule s){
        Schedule snew = new Schedule(s.getId(), s.getName(),
                        s.getUserID(), s.getUnitID() , s.getLineID(),
                        s.getMapSchedule(),s.isEnabled(), ScheduleType.Daily
                        ,s.getStartDate(), s.getEndDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        ScheduleItem si = null;
        try {
            if (isSensorOpen(s.getId())){
                //Has triggered check for time to close
                logger.debug("Sensor Is open check whether it needs to be clossed "  + s.getId());
                closeSensor(s.getId());
            }else{
                si = s.getcheduleItem(Days.get(calendar.get(Calendar.DAY_OF_WEEK) - 1));
                if (si.isEnabled() && si.getDuration() > 0) {
                    logger.debug("Inside Open Action SI time ------- " + si.getTime().toString());
                    logger.debug("Comapring two dates  ------- " + si.getTime().toString() + "  :: " + new Date().toString() + " : " + si.getDuration());
                    if (compareDates(si.getTime(), new Date(), si.getDuration())) {
                        if (isTriggeredForTheDay(s.getId()) == false)
                            openSensor(s.getId(), si);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void addScheduleItemRuntimetoDB(String sensorID, ScheduleItem scitm, String status){
        Date start = new Date();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, scitm.getDuration());
        Date end = now.getTime();
        ScheduleItemRuntime scitmruntime = new ScheduleItemRuntime(sensorID , start, end , status);
        sruntimerepo.deleteById(sensorID);
        sruntimerepo.save(scitmruntime);
   }

    public void updateScheduleItemRuntimetoDB(String sensorID, String status){
        Optional<ScheduleItemRuntime> scri_op = sruntimerepo.findById(sensorID);

        if (scri_op.isPresent()){
            ScheduleItemRuntime scri =scri_op.get();
            scri.setStatus(status);
            sruntimerepo.save(scri);
        }
    }
    public  boolean isSensorOpen(String schduleID) {
        boolean ret = false;
        Optional<ScheduleItemRuntime> sit = sruntimerepo.findById(schduleID);
        if (sit.isPresent() == true) {
            if (sit.get().getStarttime().getDay() == new Date().getDay()) {
                if (sit.get().getStatus().compareTo(Constants.RUN_STATUS_RUNNING) == 0) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    public  boolean isTriggeredForTheDay(String schduleID) {
        boolean ret = false;
        Optional<ScheduleItemRuntime> sit = sruntimerepo.findById(schduleID);
        if (sit.isPresent() == true) {
            if (sit.get().getStarttime().getDay() == new Date().getDay()) {
                    ret = true;
            }
        }
        return ret;
    }
    public  void closeSensor(String schduleID) throws MqttException, URISyntaxException {
        Optional<ScheduleItemRuntime> sito = sruntimerepo.findById(schduleID);
        Schedule sch = srepo.findById(schduleID).get();
        Optional<UnitDetails> udo = unitdetailrepo.findById(sch.getUnitID());
        UnitDetails ud = null;
        if (udo.isPresent() == true){
            ud = udo.get();
        }

        String topic = sch.getUnitID();//Util.getTopic(schduleID);
        String sensorNumber = sch.getLineID();//Util.getSensorID(schduleID);


        if (sito.isPresent() == true) {
            ScheduleItemRuntime sit = sito.get();
            try {
                if (compareDates(sit.getEndtime(), new Date())){
                    // Remove entry
                    Subscriber mqttsub = new Subscriber();
                    mqttsub.createConnection(ud.getMqqttUrl(),ud.getMqqttUserID(),ud.getMqqttPassword());
                    mqttsub.sendMsg(topic, "R" + sensorNumber + "OFF");
                    //sruntimerepo.deleteById(schduleID);
                    updateScheduleItemRuntimetoDB(schduleID,Constants.RUN_STATUS_CLOSED);
                    logger.debug(". " + topic );
                    mqttsub.disconnect();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public  void openSensor(String schduleID, ScheduleItem si) throws MqttException, URISyntaxException {
        Optional<ScheduleItemRuntime> sito = sruntimerepo.findById(schduleID);
        Schedule sch = srepo.findById(schduleID).get();

        Optional<UnitDetails> udo = unitdetailrepo.findById(sch.getUnitID());
        UnitDetails ud = null;
        if (udo.isPresent() == true){
            ud = udo.get();
        }
        String topic = sch.getUnitID();
        String sensorNumber = sch.getLineID();

        Subscriber mqttsub = new Subscriber();
        mqttsub.createConnection(ud.getMqqttUrl(),ud.getMqqttUserID(),ud.getMqqttPassword());
        mqttsub.sendMsg(topic, "R" + sensorNumber + "ON");
        logger.debug("Open Action ------- " + topic );
        addScheduleItemRuntimetoDB(schduleID,si,Constants.RUN_STATUS_RUNNING);
        mqttsub.disconnect();
    }

//    public String getTopic(String schduleID){
//        String topic = schduleID.substring(0,schduleID.lastIndexOf("_"));
//        return topic;
//    }
//
//    public String getSensorID(String schduleID){
//        String ID = schduleID.substring(schduleID.lastIndexOf("_") + 1,schduleID.length());
//        return ID;
//    }

    public boolean compareDates(Date endTime , Date nowTime) throws ParseException {
        boolean ret = false;
        if (endTime.getHours() == nowTime.getHours()){
            long diffmints = (endTime.getMinutes() - nowTime.getMinutes());
            if ( diffmints <= 0)
                ret = true;


        }


        return ret;
    }

    public boolean compareDates(Date startTime , Date nowTime, int gapInMinutes) throws ParseException {
        boolean ret = false;
        if (startTime.getHours() == nowTime.getHours()){
            long diffmints = (nowTime.getMinutes() - startTime.getMinutes());
            if ((diffmints >= 0) && (diffmints < gapInMinutes)) {
                ret = true;
            }

        }
        return ret;
    }



    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws SchedulerException {
       srepo = scheduleRepository;
       sruntimerepo = scheduleRuntimeRepository;
       unitdetailrepo = unitDetailsRepository;
       st.start();
    }
}
