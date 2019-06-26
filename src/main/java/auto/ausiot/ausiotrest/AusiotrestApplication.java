package auto.ausiot.ausiotrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"auto.ausiot"})
public class AusiotrestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AusiotrestApplication.class, args);
	}

}
