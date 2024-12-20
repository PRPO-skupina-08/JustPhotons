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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/organisations")
public class OrganisatoinsResource {


    private final CoordinatorBean coordinatorBean;


	public OrganisatoinsResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}

	@GetMapping
	public ResponseEntity<List<OrganisationEssentials>> getAll() {
		RestTemplate restTemplate = new RestTemplate();
		EssentialsList response =  restTemplate.getForObject(
			"localhost:8082/v1/organisations", EssentialsList.class);
		List<OrganisationEssentials> essentials = response.getEssentials();
		return new ResponseEntity<>(essentials, HttpStatus.OK);
	}

}
