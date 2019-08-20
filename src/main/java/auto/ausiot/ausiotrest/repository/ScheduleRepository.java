package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
            List <Schedule> findByUserID(String id);
            List <Schedule> findByUnitID(String id);
}