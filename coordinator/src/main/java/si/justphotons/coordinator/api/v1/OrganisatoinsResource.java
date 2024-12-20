package si.justphotons.coordinator.api.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import si.justphotons.coordinator.entities.OrganisationEssentials;
import si.justphotons.coordinator.services.beans.CoordinatorBean;

@RestController
@RequestMapping("/api/v1/organisations")
public class OrganisatoinsResource {


    private final CoordinatorBean coordinatorBean;


	public OrganisatoinsResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}

	public ResponseEntity<List<OrganisationEssentials>> getAll() {
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
