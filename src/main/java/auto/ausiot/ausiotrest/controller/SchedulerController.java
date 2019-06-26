package auto.ausiot.ausiotrest.controller;

import auto.ausiot.ausiotrest.model.Days;
import auto.ausiot.ausiotrest.model.Employee;
import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.model.ScheduleItem;
import auto.ausiot.ausiotrest.repository.EmployeeRepository;
import auto.ausiot.ausiotrest.repository.ScheduleRepository;
import auto.ausiot.ausiotrest.tasks.ScheduleMaster;
import auto.ausiot.ausiotrest.tasks.ScheduleTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
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
//        try {
//            tk.scheduletasks();
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        return "{ \"isWorking\" : true }";

    }
//    @GetMapping("/employees")
//    public List<Employee> getEmployees()
//    {
//        List<Employee> employeesList = employeeRepository.findAll();
//        return employeesList;
//    }
//    @GetMapping("/employee/{id}")
//    public Optional<Employee> getEmployee(@PathVariable String id)
//    {
//        Optional<Employee> emp = employeeRepository.findById(id);
//        return emp;
//    }
//    @PutMapping("/employee/{id}")
//    public Optional<Employee> updateEmployee(@RequestBody Employee newEmployee, @PathVariable String id)
//    {
//        Optional<Employee> optionalEmp = employeeRepository.findById(id);
//        if (optionalEmp.isPresent()) {
//            Employee emp = optionalEmp.get();
//            emp.setFirstName(newEmployee.getFirstName());
//            emp.setLastName(newEmployee.getLastName());
//            emp.setEmail(newEmployee.getEmail());
//            employeeRepository.save(emp);
//        }
//        return optionalEmp;
//    }
//    @DeleteMapping(value = "/employee/{id}", produces = "application/json; charset=utf-8")
//    public String deleteEmployee(@PathVariable String id) {
//        Boolean result = employeeRepository.existsById(id);
//        employeeRepository.deleteById(id);
//        return "{ \"success\" : "+ (result ? "true" : "false") +" }";
//    }
//    @PostMapping("/employee")
//    public Employee addEmployee(@RequestBody Employee newEmployee)
//    {
//        String id = String.valueOf(new Random().nextInt());
//        Employee emp = new Employee(id, newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getEmail());
//        employeeRepository.insert(emp);
//        return emp;
//    }

    //Save
    @PostMapping("/schedule")
    public Schedule addSchedule(@RequestBody Schedule s)
    {
        String id = String.valueOf(new Random().nextInt());
        //Employee emp = new Employee(id, newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getEmail());
        Schedule snew = new Schedule(s.getId(),s.getMapSchedule(),s.isEnabled());
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
        Optional<Schedule> s = scheduleRepository.findById(id);
        return s;
    }
    @PutMapping("/schedule/{id}")
    public Optional<Schedule> updateEmployee(@RequestBody Schedule newSchedule, @PathVariable String id)
    {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(id);
        if (optionalSchedule.isPresent()) {
            Schedule s = optionalSchedule.get();
            Schedule snew = new Schedule(s.getId(),s.getMapSchedule(),s.isEnabled());
            scheduleRepository.save(snew);
        }
        return optionalSchedule;
    }
    @DeleteMapping(value = "/schedule/{id}", produces = "application/json; charset=utf-8")
    public String deleteSchedule(@PathVariable String id) {
        Boolean result =scheduleRepository.existsById(id);
        scheduleRepository.deleteById(id);
        return "{ \"success\" : "+ (result ? "true" : "false") +" }";
    }

    void x(){
       Employee e1 = new Employee("1","sss","ddd","dddd");

        ObjectMapper mapper = new ObjectMapper();

        // Java object to JSON file
        try {
            mapper.writeValue(new File("/tmp/emp.json"), e1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Java object to JSON string
        try {
            String jsonString = mapper.writeValueAsString(e1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



//        ScheduleItem si = new ScheduleItem("1",new Date(),2,true);
//        Map<Days, ScheduleItem> mapSchedule = new HashMap<Days, ScheduleItem>();
//        mapSchedule.put(Days.Friday,si);
//        Schedule s = new Schedule("1",mapSchedule,true);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        // Java object to JSON file
//        try {
//            mapper.writeValue(new File("/tmp/staff.json"), s);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Java object to JSON string
//        try {
//            String jsonString = mapper.writeValueAsString(s);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


    }
}