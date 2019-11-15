package project.wpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import project.wpl.repository.RegistrationRepository;


@EnableJpaRepositories("project/wpl/repository")
@SpringBootApplication
public class Application {

	@Autowired
	RegistrationRepository registrationRepository;

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

}
