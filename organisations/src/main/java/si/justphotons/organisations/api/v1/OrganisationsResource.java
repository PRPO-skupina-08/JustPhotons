package si.justphotons.organisations.api.v1;

import si.justphotons.organisations.entities.Organisation;
import si.justphotons.organisations.services.beans.OrganisationsBean;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/organisations")
public class OrganisationsResource {

	private final OrganisationsBean organisationsBean;

	public OrganisationsResource(OrganisationsBean organisationsBean) {
		this.organisationsBean = organisationsBean;
	}

	@GetMapping
	public ResponseEntity<List<Organisation>> getAll() {
		List<Organisation> orgs = organisationsBean.getAll();
		return ResponseEntity.ok(orgs);
	}

	@PostMapping
	public ResponseEntity<Organisation> postOne(@RequestBody Organisation organisation) {
		Organisation org = organisationsBean.insertOne(organisation);
		return new ResponseEntity<>(org, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Organisation> getOne(@PathVariable Long id) {
		Organisation org = organisationsBean.getById(id);
		if (org != null) {
			return new ResponseEntity<>(org, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Organisation> putOne(@PathVariable Long id, @RequestBody Organisation entity) {
		boolean succ = organisationsBean.updateOne(id, entity);
		if (succ) {
			entity.setId(id);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Organisation> deleteOne(@PathVariable Long id) {
		boolean succ = organisationsBean.deleteOne(id);
		if (succ) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	

}
