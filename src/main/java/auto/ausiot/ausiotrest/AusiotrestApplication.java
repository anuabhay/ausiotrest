package auto.ausiot.ausiotrest;

import auto.ausiot.ausiotrest.model.security.Role;
import auto.ausiot.ausiotrest.model.security.User;
import auto.ausiot.ausiotrest.repository.RoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"auto.ausiot"})
public class AusiotrestApplication {

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {

		return args -> {

			Role adminRole = roleRepository.findByRole("ADMIN");
			if (adminRole == null) {
				Role newAdminRole = new Role();
				newAdminRole.setRole("ADMIN");
				roleRepository.save(newAdminRole);
			}
		};

	}

	public static void main(String[] args) {
		SpringApplication.run(AusiotrestApplication.class, args);
	}

}
