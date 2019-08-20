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
            String topic = Util.getTopic(schduleID);
            String sensorNumber = Util.getSensorID(schduleID);
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

    public static void  removeSensorRecord(String schduleID)  {
        String topic = Util.getTopic(schduleID);
        String sensorNumber = Util.getSensorID(schduleID);
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

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws SchedulerException {
        sruntimerepo = scheduleRuntimeRepository;
    }

}
