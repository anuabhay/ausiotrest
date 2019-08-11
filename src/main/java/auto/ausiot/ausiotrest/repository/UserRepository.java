package auto.ausiot.ausiotrest.repository;

import auto.ausiot.ausiotrest.model.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}