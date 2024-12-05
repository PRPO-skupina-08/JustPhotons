package si.justphotons.api.v1.resources;

import si.justphotons.entities.Organisation;
import si.justphotons.services.beans.OrganisationsBean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/organisations")
public class OrganisationsResource {

	private final OrganisationsBean organisationsBean;

	public OrganisationsResource(OrganisationsBean organisationsBean) {
		this.organisationsBean = organisationsBean;
	}

	@GetMapping("/")
	public List<Organisation> getAll() {
		return organisationsBean.getAll();
	}

	@PostMapping("/")
	public Organisation postOne(@RequestBody Organisation organisation) {
		return organisationsBean.insertOne(organisation);
	}

}
