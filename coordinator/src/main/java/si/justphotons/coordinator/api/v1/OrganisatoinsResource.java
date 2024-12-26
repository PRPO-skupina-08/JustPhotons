package si.justphotons.coordinator.api.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import si.justphotons.coordinator.entities.external.EssentialsList;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;
import si.justphotons.coordinator.services.beans.CoordinatorBean;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/organisations")
public class OrganisatoinsResource {

	private static final int ORGANISATIONS_PORT = 8082;

    private final CoordinatorBean coordinatorBean;


	public OrganisatoinsResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}

	@GetMapping
	public ResponseEntity<OrganisationEssentials[]> getAll() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<OrganisationEssentials[]> response =  restTemplate.getForEntity(
			String.format("http://localhost:%d/v1/organisations", ORGANISATIONS_PORT),
			OrganisationEssentials[].class);
		OrganisationEssentials[] essentials = response.getBody();
		return new ResponseEntity<>(essentials, HttpStatus.OK);
	}

}
