package si.justphotons.api.v1;

import si.justphotons.services.beans.OrganisationsBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@SpringBootApplication(scanBasePackages = "si.justphotons")
public class OrganisationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrganisationsApplication.class, args);
	}

}
