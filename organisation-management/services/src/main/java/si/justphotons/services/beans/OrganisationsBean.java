package si.justphotons.services.beans;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class OrganisationsBean {
  private List<String> organisations = new ArrayList<>();

  public List<String> getAll() {
    return organisations;
  }
}