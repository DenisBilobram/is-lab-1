package is.lab1.is_lab1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import is.lab1.is_lab1.config.Sha256PasswordEncoder;

@SpringBootApplication
public class IsLab1Application {

	public static void main(String[] args) {
		SpringApplication.run(IsLab1Application.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Sha256PasswordEncoder();
	}

}
