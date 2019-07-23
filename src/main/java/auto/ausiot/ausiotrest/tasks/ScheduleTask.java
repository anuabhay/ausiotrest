package auto.ausiot.ausiotrest.tasks;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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