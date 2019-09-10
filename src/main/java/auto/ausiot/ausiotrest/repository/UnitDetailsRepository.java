package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.Unit;
import auto.ausiot.ausiotrest.model.UnitDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface UnitDetailsRepository extends MongoRepository<UnitDetails, String> {
    //UnitDetails findByUnitID(String id);
}