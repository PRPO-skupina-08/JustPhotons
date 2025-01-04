package si.justphotons.coordinator.services.beans;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import si.justphotons.coordinator.entities.external.Organisation;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;

@Service
public class CoordinatorBean {

    private static final int ORGANISATIONS_PORT = 8082;

    public List<OrganisationEssentials> getOrganisations() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<OrganisationEssentials[]> response =  restTemplate.getForEntity(
            String.format("http://localhost:%d/v1/organisations", ORGANISATIONS_PORT),
            OrganisationEssentials[].class);
        OrganisationEssentials[] essentials = response.getBody();
        return Arrays.asList(essentials);
    }

    public Organisation getOrganisation(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Organisation response =  restTemplate.getForObject(
            String.format("http://localhost:%d/v1/organisations/%d", ORGANISATIONS_PORT, id),
            Organisation.class);
        return response;
    }
}
