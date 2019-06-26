package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.Employee;
import auto.ausiot.ausiotrest.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
}