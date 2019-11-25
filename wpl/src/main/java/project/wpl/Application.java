package project.wpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import project.wpl.repository.BankAccountRepository;
import project.wpl.repository.RegistrationRepository;
import project.wpl.repository.RoleRepository;

@EnableJdbcHttpSession
@EnableJpaRepositories("project/wpl/repository")
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Autowired
	RegistrationRepository registrationRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	BankAccountRepository bankAccountRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
	}

}
