package si.justphotons.organisations.api.v1;

import si.justphotons.organisations.entities.Album;
import si.justphotons.organisations.entities.Organisation;
import si.justphotons.organisations.services.beans.OrganisationsBean;
import si.justphotons.organisations.services.dtos.OrganisationEssentials;
import si.justphotons.organisations.services.beans.AlbumsBean;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/v1/organisations")
public class OrganisationsResource {

	private final OrganisationsBean organisationsBean;
	private final AlbumsBean albumsBean;

	public OrganisationsResource(OrganisationsBean organisationsBean, AlbumsBean albumsBean) {
		this.organisationsBean = organisationsBean;
		this.albumsBean = albumsBean;
	}

	@GetMapping
	public ResponseEntity<List<OrganisationEssentials>> getAll() {
		List<OrganisationEssentials> orgs = organisationsBean.getAll();
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
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{orgId}/albums")
	public ResponseEntity<List<Album>> getAlbums(@PathVariable Long orgId, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "25") Integer pageSize) {
		List<Album> albums = albumsBean.getAll(orgId, page, pageSize);

		if (albums == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(albums, HttpStatus.OK);
	}

	@PostMapping("/{orgId}/albums")
	public ResponseEntity<Album> postOne(@PathVariable Long orgId, @RequestBody Album album) {
		Album al = albumsBean.insertOne(orgId, album);
		if (al == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(al, HttpStatus.CREATED);
	}

	@GetMapping("/{orgId}/albums/{albumId}")
	public ResponseEntity<Album> getOne(@PathVariable Long orgId, @PathVariable Long albumId) {

		Album al = albumsBean.getOne(orgId, albumId);
		if (al == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(al, HttpStatus.OK);
	}

	@PutMapping("/{orgId}/albums/{albumId}")
	public ResponseEntity<Album> putOne(@PathVariable Long orgId, @PathVariable Long albumId, @RequestBody Album album) {

		Album al = albumsBean.updateOne(orgId, albumId, album);
		if (al == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(al, HttpStatus.OK);
	}

	@DeleteMapping("/{orgId}/albums/{albumId}")
	public ResponseEntity<Album> deleteOne(@PathVariable Long orgId, @PathVariable Long albumId) {

		boolean succ = albumsBean.removeOne(orgId, albumId);
		if (succ) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
	}



}
