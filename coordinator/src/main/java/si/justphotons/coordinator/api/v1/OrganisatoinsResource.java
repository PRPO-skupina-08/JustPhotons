package si.justphotons.coordinator.api.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpServletRequest;
import si.justphotons.coordinator.entities.external.Organisation;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;
import si.justphotons.coordinator.services.beans.CoordinatorBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/organisations")
public class OrganisatoinsResource {

	private final CoordinatorBean coordinatorBean;


	public OrganisatoinsResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}

	@GetMapping
	public ResponseEntity<List<OrganisationEssentials>> getAll(HttpServletRequest request) {
		// Tle se bo klicalo prej še permission check
		// jwt here!!
		Long userId = coordinatorBean.getIdFromJWT(request);
		List<OrganisationEssentials> essentials = coordinatorBean.getOrganisations(userId); 
		// System.out.printf("User ID: %d\n", userId);
		return new ResponseEntity<>(essentials, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Organisation> getOne(@PathVariable Long id) {
		Organisation org = null;
		try {
			org = coordinatorBean.getOrganisation(id);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(e.getStatusCode());
		}
		return new ResponseEntity<>(org, HttpStatus.OK);
	}
}
