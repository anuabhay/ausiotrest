package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
}