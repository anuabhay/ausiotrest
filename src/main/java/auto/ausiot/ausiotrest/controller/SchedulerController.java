package auto.ausiot.ausiotrest.controller;

import auto.ausiot.ausiotrest.model.*;
import auto.ausiot.ausiotrest.model.security.User;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import auto.ausiot.ausiotrest.repository.UnitRepository;
import auto.ausiot.ausiotrest.tasks.ManageSensorRuntime;
import auto.ausiot.ausiotrest.tasks.ScheduleMaster;
import mqtt.Constants;
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
    ScheduleRepository scheduleRepository;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    ScheduleMaster sm;
    @GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
    public String getHealthCheck()
    {
        return "{ \"isWorking\" : true }";
    }


    @PostMapping("/unit")
    public Unit addSensor(@RequestBody Unit unit)
    {
        unitRepository.save(unit);
        return unit;
    }


    @GetMapping("/units/{id}")
    public List<Unit> getUnits(@PathVariable String id)
    {
        List<Unit> unitList = unitRepository.findByUserID(id);
        return unitList;
    }

    @RequestMapping(value = "/units/{id}",method = RequestMethod.DELETE)
    public String deleteUnit(@PathVariable String id) {
        Boolean result =unitRepository.existsById(id);
        unitRepository.deleteById(id);
        List<Schedule> lst = scheduleRepository.findByUnitID(id);
        scheduleRepository.deleteAll(lst);
        ManageSensorRuntime.removeSensorRecords(lst);
        String res=  "{ \"success\" : "+ (result ? "true" : "false") +" }";
        return res;
    }


//    @PostMapping("/sensor")
//    public Schedule addSensor(@RequestBody String sensorID)
//    {
//        Map<Days, ScheduleItem> si = new HashMap<>();
//        Schedule defaultschedule = new Schedule(sensorID,
//
//                si,true,ScheduleType.Weekly,
//                new Date(), new Date(Constants.MAX_END_DATE));
//        //@TODO Move to constant file
//        String schedule = "WEEKLY::1,13:00,9,TRUE;2,13:00,9,TRUE;3,13:00,9,TRUE;4,13:00,9,TRUE;5,13:00,9,TRUE;6,13:00,9,TRUE;0,13:00,9,TRUE";
//        try {
//            defaultschedule.createSheduleFromString(schedule);
//            scheduleRepository.save(defaultschedule);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return defaultschedule;
//    }

    //Save
    @PostMapping("/schedule")
    public Schedule addSchedule(@RequestBody Schedule s)
    {
        String id = String.valueOf(new Random().nextInt());
        Schedule snew = new Schedule(s.getId(), s.getName(),
                s.getUserID(),s.getUnitID(),s.getLineID(),
                s.getMapSchedule(),s.isEnabled(),s.getType(),s.getStartDate(),s.getEndDate());

        Optional<Schedule> sc = scheduleRepository.findById(s.getId());
        if (sc.isPresent() == true){
        //if (scheduleRepository.findById(s.getId())!=null) {
            scheduleRepository.deleteById(s.getId());
            ManageSensorRuntime.removeSensorRecord(snew);
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

    @GetMapping("/user/schedules/{id}")
    public List<Schedule> getUserSchedules(@PathVariable String id)
    {
        List<Schedule> scheduleList = scheduleRepository.findByUserID(id);
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
            Schedule defaultschedule = new Schedule(id,"Schedule -",
                    "","","",
                    si,true,ScheduleType.Weekly,
                    new Date(), new Date(Constants.MAX_END_DATE));
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
            Schedule snew = new Schedule(s.getId(), s.getName(),
                    s.getUserID(), s.getUnitID() , s.getLineID(),
                    s.getMapSchedule(),s.isEnabled()
                    ,s.getType(),s.getStartDate(),s.getEndDate());
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

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable String id)
    {
        User userExists = null;
        userExists = userService.findUserByEmail(id);

        return userExists;
    }

    @GetMapping("/schedule1/{id}")
    public Optional<Schedule> getSchedule1(@PathVariable String id)
    {
        Optional<Schedule> sc = scheduleRepository.findById(id);
        // If user does not have a schedule give him the default
        if (sc.isPresent() == false){
            Map<Days, ScheduleItem> si = new HashMap<>();
            Schedule defaultschedule = new Schedule(id, "Schedule 1",
                    "","","",
                    si,true,ScheduleType.Weekly,
                                            new Date(), new Date(Constants.MAX_END_DATE));
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

}