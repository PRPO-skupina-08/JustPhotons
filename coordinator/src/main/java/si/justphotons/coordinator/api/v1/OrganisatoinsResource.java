package si.justphotons.coordinator.api.v1;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(summary = "Get all organisations from particular user (JWT)")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "list of organisations", 
			content = { @Content(mediaType = "application/json", 
			schema = @Schema(implementation = OrganisationEssentials.class)) 
		}),
		@ApiResponse(responseCode = "403", description = "No JWT token provided", 
			content = @Content)
	})
	@GetMapping
	public ResponseEntity<List<OrganisationEssentials>> getAll(HttpServletRequest request) {
		Long userId = coordinatorBean.getIdFromJWT(request);
		if (userId == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		List<OrganisationEssentials> essentials = coordinatorBean.getOrganisations(userId); 
		// System.out.printf("User ID: %d\n", userId);
		return new ResponseEntity<>(essentials, HttpStatus.OK);
	}

	@Operation(summary = "Get organisations details")
	@ApiResponses(value = { 
		@ApiResponse(responseCode = "200", description = "organisations details", 
			content = { @Content(mediaType = "application/json", 
			schema = @Schema(implementation = Organisation.class)) 
		}),
		@ApiResponse(responseCode = "403", description = "No JWT token provided", 
			content = @Content),
		@ApiResponse(responseCode = "404", description = "Organisatoin with :id not found", 
			content = @Content)
	})
	@GetMapping("/{id}")
	public ResponseEntity<Organisation> getOne(@PathVariable Long id, HttpServletRequest request) {
		Long userId = coordinatorBean.getIdFromJWT(request);
		if (userId == null) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}

		Organisation org = null;
		try {
			org = coordinatorBean.getOrganisation(id);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(e.getStatusCode());
		}
		return new ResponseEntity<>(org, HttpStatus.OK);
	}
	
}
