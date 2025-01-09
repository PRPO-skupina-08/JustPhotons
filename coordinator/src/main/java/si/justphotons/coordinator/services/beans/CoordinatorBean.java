package si.justphotons.coordinator.services.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import si.justphotons.coordinator.entities.external.Organisation;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;

@Service
public class CoordinatorBean {

    // private static final int ORGANISATIONS_PORT = 8082;

    @Value("${ORGANISATIONS_URL}")
    private String ORGANISATIONS_URL;

    @Value("${USERS_URL}")
    private String USERS_URL;


    public List<OrganisationEssentials> getOrganisations(Long userId) {
        List<OrganisationEssentials> essentials = new ArrayList<>();
        /* tmp !!!
        * fetch permission check instead
        */
        Map<Long, List<Long>> user2organisations = new HashMap<>() {{
            put(1L, Arrays.asList(1L, 2L));
        }};

        RestTemplate restTemplate = new RestTemplate();

        for (Long orgId: user2organisations.get(userId)) {
            ResponseEntity<OrganisationEssentials> response =  restTemplate.getForEntity(
                String.format("%s/essentials/%d", ORGANISATIONS_URL, orgId),
                OrganisationEssentials.class
            );
            OrganisationEssentials essential = response.getBody();
            essentials.add(essential);
        }
        
        return essentials;
    }

    public Organisation getOrganisation(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Organisation response =  restTemplate.getForObject(
            String.format("%s/%d", ORGANISATIONS_URL, id),
            Organisation.class);
        return response;
    }
}
