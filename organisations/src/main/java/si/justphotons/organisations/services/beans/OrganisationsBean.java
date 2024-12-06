package si.justphotons.organisations.services.beans;

import java.util.List;
import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import si.justphotons.organisations.entities.Organisation;
import si.justphotons.organisations.entities.OrganisationsRepository;

@Service
public class OrganisationsBean {

  OrganisationsRepository organisationsRepository;

  public OrganisationsBean(OrganisationsRepository o) {
    this.organisationsRepository = o;
  }

  public List<Organisation> getAll() {
    return organisationsRepository.findAll();
  }

  public Organisation insertOne(Organisation organisation) {
    Organisation org = organisationsRepository.save(organisation);
    return org;
  }

  public Organisation getById(Long id) {
    return organisationsRepository.findById(id).orElse(null);
  }

  @Transactional
  public boolean updateOne(Long id, Organisation org) {
    Optional<Organisation> orgOptional = organisationsRepository.findById(id);
    if (orgOptional.isPresent()) {
      Organisation o = orgOptional.get();
      o.setId(id);
      o.setDescription(org.getDescription());
      o.setName(org.getName());
      o.setAlbums(org.getAlbums());
      organisationsRepository.save(o);
      return true;
    }
    return false;
  }

  @Transactional
  public boolean deleteOne(Long id) {
    Optional<Organisation> org = organisationsRepository.findById(id);
    if (org.isPresent()) {
      organisationsRepository.deleteById(id);
      return true;
    }
    
    return false;
  }

}