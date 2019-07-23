package auto.ausiot.ausiotrest.controller;

import auto.ausiot.ausiotrest.model.*;
import auto.ausiot.ausiotrest.repository.EmployeeRepository;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import auto.ausiot.ausiotrest.tasks.ScheduleMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.util.*;


@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
@RestController
public class SchedulerController
{

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    ScheduleMaster sm;
    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck()
    {
        return "{ \"isWorking\" : true }";
    }



    @PostMapping("/sensor")
    public Schedule addSensor(@RequestBody String sensorID)
    {
        Map<Days, ScheduleItem> si = new HashMap<>();
        Schedule defaultschedule = new Schedule(sensorID,si,true,ScheduleType.Weekly);
        //@TODO Move to constant file
        String schedule = "WEEKLY::1,13:00,9,TRUE;2,13:00,9,TRUE;3,13:00,9,TRUE;4,13:00,9,TRUE;5,13:00,9,TRUE;6,13:00,9,TRUE;0,13:00,9,TRUE";
        try {
            defaultschedule.createSheduleFromString(schedule);
            scheduleRepository.save(defaultschedule);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return defaultschedule;
    }

    //Save
    @PostMapping("/schedule")
    public Schedule addSchedule(@RequestBody Schedule s)
    {
        String id = String.valueOf(new Random().nextInt());
        Schedule snew = new Schedule(s.getId(),s.getMapSchedule(),s.isEnabled(),s.getType());
        if (scheduleRepository.findById(s.getId())!=null) {
            scheduleRepository.deleteById(s.getId());
        }
        scheduleRepository.insert(snew);
        return snew;
    }


    @GetMapping("/schedules")
    public List<Schedule> getSchedules()
    {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        return scheduleList;
    }
    // Get ID
    @GetMapping("/schedule/{id}")
    public Optional<Schedule> getSchedule(@PathVariable String id)
    {
         Optional<Schedule> sc = scheduleRepository.findById(id);
        // If user does not have a schedule give him the default
        if (sc.isPresent() == false){
            Map<Days, ScheduleItem> si = new HashMap<>();
            Schedule defaultschedule = new Schedule(id,si,true,ScheduleType.Weekly);
            //@TODO Move to constant file
            String schedule = "WEEKLY::1,13:00,9,TRUE;2,13:00,9,TRUE;3,13:00,9,TRUE;4,13:00,9,TRUE;5,13:00,9,TRUE;6,13:00,9,TRUE;0,13:00,9,TRUE";
            try {
                defaultschedule.createSheduleFromString(schedule);
                defaultschedule.setId(id);
                sc = Optional.of(defaultschedule);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return sc;
    }
    @PutMapping("/schedule/{id}")
    public Optional<Schedule> updateEmployee(@RequestBody Schedule newSchedule, @PathVariable String id)
    {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule s = optionalSchedule.get();
            Schedule snew = new Schedule(s.getId(),s.getMapSchedule(),s.isEnabled(),s.getType());
            scheduleRepository.save(snew);
        }
        return optionalSchedule;
    }

    @RequestMapping(value = "/schedule/{id}",method = RequestMethod.DELETE)
    public String deleteSchedule(@PathVariable String id) {
        Boolean result =scheduleRepository.existsById(id);
        scheduleRepository.deleteById(id);
        String res=  "{ \"success\" : "+ (result ? "true" : "false") +" }";
        return res;
    }

}