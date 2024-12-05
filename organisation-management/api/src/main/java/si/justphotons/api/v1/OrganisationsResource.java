package si.justphotons.api.v1;

import si.justphotons.services.beans.OrganisationsBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication(scanBasePackages = "si.justphotons")
@RestController
public class OrganisationsResource {

	private final OrganisationsBean organisationsBean;

	public OrganisationsResource(OrganisationsBean organisationsBean) {
		this.organisationsBean = organisationsBean;
	}

	@GetMapping("/v1")
	public String home() {
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(OrganisationsResource.class, args);
	}

}
