package si.justphotons.coordinator.api.v1;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.servlet.http.HttpServletRequest;
import si.justphotons.coordinator.entities.external.LoginEssentials;
import si.justphotons.coordinator.entities.external.Organisation;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;
import si.justphotons.coordinator.services.beans.CoordinatorBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api/v1")
public class AuthResource {

	private final CoordinatorBean coordinatorBean;


	public AuthResource(CoordinatorBean coordinatorBean) {
		this.coordinatorBean =  coordinatorBean;
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody LoginEssentials entity) {
        String jwt;
        try {
			jwt = coordinatorBean.login(entity);
		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>(e.getStatusCode());
		}
		
		Map<String, String> json = new HashMap<>();
		json.put("token", jwt);
		return new ResponseEntity<>(json, HttpStatus.OK);
	}
	
}
