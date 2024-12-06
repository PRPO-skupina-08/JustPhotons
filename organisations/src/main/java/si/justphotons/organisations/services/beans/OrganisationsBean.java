package si.justphotons.organisations.services.beans;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}