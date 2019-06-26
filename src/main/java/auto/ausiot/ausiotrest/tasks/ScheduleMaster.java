package auto.ausiot.ausiotrest.tasks;

import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.model.ScheduleItem;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class ScheduleMaster  implements Job {
    @Autowired
    ScheduleTask st;
    static ScheduleRepository sr1;
    @Autowired
    ScheduleRepository scheduleRepository;

    public ScheduleMaster() throws SchedulerException {
        ScheduleTask st = new ScheduleTask();
        //st.start();
    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        Optional<Schedule> optionalSchedule =  sr1.findById("1");
        if (optionalSchedule.isPresent()) {
            Schedule s = optionalSchedule.get();
            Schedule snew = new Schedule(s.getId(),s.getMapSchedule(),s.isEnabled());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

            ScheduleItem si = null;
            try {
                si = snew.hasScheduleItem(calendar.get(Calendar.DAY_OF_WEEK) - 1, new Date());
                int x = 0;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws SchedulerException {
       sr1 = scheduleRepository;
       st.start();
    }
}
