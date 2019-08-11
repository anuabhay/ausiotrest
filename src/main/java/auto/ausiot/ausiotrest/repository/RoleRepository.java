package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.security.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}