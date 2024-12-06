package si.justphotons.organisations.api.v1;

import si.justphotons.organisations.entities.Organisation;
import si.justphotons.organisations.services.beans.OrganisationsBean;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
@RequestMapping("/organisations")
public class OrganisationsResource {

	private final OrganisationsBean organisationsBean;

	public OrganisationsResource(OrganisationsBean organisationsBean) {
		this.organisationsBean = organisationsBean;
	}

	@GetMapping("/")
	public ResponseEntity<List<Organisation>> getAll() {
		List<Organisation> orgs = organisationsBean.getAll();
		return ResponseEntity.ok(orgs);
	}

	@PostMapping("/")
	public ResponseEntity<Organisation> postOne(@RequestBody Organisation organisation) {
		Organisation org = organisationsBean.insertOne(organisation);
		return new ResponseEntity<>(org, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Organisation> getMethodName(@PathVariable Long id) {
		Organisation org = organisationsBean.getById(id);
		if (org != null) {
			return new ResponseEntity<>(org, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

}
