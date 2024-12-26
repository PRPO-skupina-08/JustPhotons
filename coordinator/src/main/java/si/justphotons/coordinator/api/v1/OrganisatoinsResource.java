package si.justphotons.coordinator.api.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import si.justphotons.coordinator.entities.external.OrganisationEssentials;
import si.justphotons.coordinator.services.beans.CoordinatorBean;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/organisations")
public class OrganisatoinsResource {

    private final CoordinatorBean coordinatorBean;


	public OrganisatoinsResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}

	@GetMapping
	public ResponseEntity<List<OrganisationEssentials>> getAll() {
		List<OrganisationEssentials> essentials = coordinatorBean.getOrganisations(); 
		return new ResponseEntity<>(essentials, HttpStatus.OK);
	}

}
