package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.Schedule;
import auto.ausiot.ausiotrest.model.Unit;
import auto.ausiot.ausiotrest.model.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UnitRepository extends MongoRepository<Unit, String> {
    List<Unit> findByUserID(String id);

}