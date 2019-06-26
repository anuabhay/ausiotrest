package auto.ausiot.ausiotrest.tasks;

import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.text.*;
@Component
public class ScheduleTask   {



    public ScheduleTask()  {
        int x = 1;
    }

    public void start() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(ScheduleMaster.class)
                .withIdentity("dummyJobName", "group1").build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(15).repeatForever())
                .build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws SchedulerException {
        //start();
    }


//    public void scheduletasks() throws SchedulerException {
//        JobDetail job = JobBuilder.newJob(ScheduleTask.class)
//                .withIdentity("dummyJobName", "group1").build();
//
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withIdentity("dummyTriggerName", "group1")
//                .withSchedule(
//                        SimpleScheduleBuilder.simpleSchedule()
//                                .withIntervalInSeconds(5).repeatForever())
//                .build();
//
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);
//    }
//    public static void main(String[] args) throws SchedulerException {
//
//        JobDetail job = JobBuilder.newJob(ScheduleTask.class)
//                .withIdentity("dummyJobName", "group1").build();
//
//        Trigger trigger = TriggerBuilder
//                .newTrigger()
//                .withIdentity("dummyTriggerName", "group1")
//                .withSchedule(
//                        SimpleScheduleBuilder.simpleSchedule()
//                                .withIntervalInSeconds(10).repeatForever())
//                .build();
//
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.start();
//        scheduler.scheduleJob(job, trigger);
//    }
}