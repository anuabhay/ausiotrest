package auto.ausiot.ausiotrest.tasks;

import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import auto.ausiot.ausiotrest.repository.ScheduleRuntimeRepository;
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

@Component
public class ManageSensorRuntime {
    @Autowired
    ScheduleRuntimeRepository scheduleRuntimeRepository;

    static ScheduleRuntimeRepository sruntimerepo;


    public static void  removeSensorRecords(List<Schedule> schedules)  {

        for (int i =0; i < schedules.size();i++) {
            String schduleID = schedules.get(i).getId();
            String topic = schedules.get(i).getUnitID();
            String sensorNumber = schedules.get(i).getLineID();
            try {
                Subscriber.connect();
                Subscriber.sendMsg(topic, "R" + sensorNumber + "OFF");
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            sruntimerepo.deleteById(schduleID);
        }
    }

    public static void  removeSensorRecord(Schedule schedule)  {
        String topic = schedule.getUnitID();//Util.getTopic(schduleID);
        String sensorNumber = schedule.getLineID();//Util.getSensorID(schduleID);
        try {
            Subscriber.connect();
            Subscriber.sendMsg(topic, "R" + sensorNumber + "OFF");
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
    }

}
