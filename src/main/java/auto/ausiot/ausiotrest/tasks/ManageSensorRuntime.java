package auto.ausiot.ausiotrest.tasks;

import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.model.UnitDetails;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import auto.ausiot.ausiotrest.repository.ScheduleRuntimeRepository;
import auto.ausiot.ausiotrest.repository.UnitDetailsRepository;
import auto.ausiot.ausiotrest.util.Util;
import mqtt.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Component
public class ManageSensorRuntime {
    @Autowired
    ScheduleRuntimeRepository scheduleRuntimeRepository;

    static ScheduleRuntimeRepository sruntimerepo;

    static UnitDetailsRepository unitdetailrepo;

    @Autowired
    UnitDetailsRepository unitDetailsRepository;


    public static void  removeSensorRecords(List<Schedule> schedules)  {
        Subscriber mqttsub = null;
        if(schedules.size() > 0){
            Schedule schedule = schedules.get(0);

            Optional<UnitDetails> udo = unitdetailrepo.findById(schedule.getUnitID());
            UnitDetails ud = null;
            if (udo.isPresent() == true){
                ud = udo.get();
            }
            try {
                mqttsub = new Subscriber();
                mqttsub.createConnection(ud.getMqqttUrl(), ud.getMqqttUserID(), ud.getMqqttPassword());
            }catch (MqttException e) {
                e.printStackTrace();
            }
        }

        for (int i =0; i < schedules.size();i++) {
            String schduleID = schedules.get(i).getId();
            String topic = schedules.get(i).getUnitID();
            String sensorNumber = schedules.get(i).getLineID();
            try {
                mqttsub.sendMsg(topic, "R" + sensorNumber + "OFF");
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            sruntimerepo.deleteById(schduleID);
        }
    }

    public static void  removeSensorRecord(Schedule schedule)  {
        Optional<UnitDetails> udo = unitdetailrepo.findById(schedule.getUnitID());
        UnitDetails ud = null;
        if (udo.isPresent() == true){
            ud = udo.get();
        }
        String topic = schedule.getUnitID();//Util.getTopic(schduleID);
        String sensorNumber = schedule.getLineID();//Util.getSensorID(schduleID);
        try {
            Subscriber mqttsub = new Subscriber();
            mqttsub.createConnection(ud.getMqqttUrl(),ud.getMqqttUserID(),ud.getMqqttPassword());
            mqttsub.sendMsg(topic, "R" + sensorNumber + "OFF");
            mqttsub.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        sruntimerepo.deleteById(schedule.getId());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws SchedulerException {
        sruntimerepo = scheduleRuntimeRepository;
        unitdetailrepo = unitDetailsRepository;
    }

}
