package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.model.ScheduleItemRuntime;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRuntimeRepository extends MongoRepository<ScheduleItemRuntime, String> {
}