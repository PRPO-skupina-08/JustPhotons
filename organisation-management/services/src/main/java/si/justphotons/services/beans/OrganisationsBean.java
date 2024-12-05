package si.justphotons.services.beans;

import si.justphotons.entities.Organisation;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class OrganisationsBean {
  private List<Organisation> organisations = new ArrayList<>();

  public List<Organisation> getAll() {
    return organisations;
  }

  public Organisation insertOne(Organisation organisation) {
    organisations.add(organisation);
    return organisation;
  }

}