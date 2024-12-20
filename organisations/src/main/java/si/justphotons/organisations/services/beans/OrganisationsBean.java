package si.justphotons.organisations.services.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import si.justphotons.organisations.entities.Organisation;
import si.justphotons.organisations.entities.OrganisationsRepository;
import si.justphotons.organisations.services.dtos.OrganisationEssentials;

@Service
public class OrganisationsBean {

  OrganisationsRepository organisationsRepository;

  public OrganisationsBean(OrganisationsRepository o) {
    this.organisationsRepository = o;
  }

  public List<OrganisationEssentials> getAll() {
    List<Organisation> orgs = organisationsRepository.findAll();
    return convertToEssentials(orgs);
  }

  public Organisation insertOne(Organisation organisation) {
    Organisation org = organisationsRepository.save(organisation);
    return org;
  }

  public Organisation getById(Long id) {
    return organisationsRepository.findById(id).orElse(null);
  }

  @Transactional
  public Organisation updateOne(Long id, Organisation org) {
    Optional<Organisation> orgOptional = organisationsRepository.findById(id);
    if (orgOptional.isPresent()) {
      Organisation o = orgOptional.get();
      o.setId(id);
      o.setDescription(org.getDescription());
      o.setName(org.getName());
      Organisation res = organisationsRepository.save(o);
      return res;
    }
    return null;
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


  /*
  conversion methods
  */ 

  private List<OrganisationEssentials> convertToEssentials(List<Organisation> organisations) {
    List<OrganisationEssentials> essentials = new ArrayList<>();
    for (Organisation org : organisations) {
      OrganisationEssentials ess = new OrganisationEssentials();
      ess.setId(org.getId());
      ess.setName(org.getName());
      essentials.add(ess);
    }

    return essentials;
  }

}