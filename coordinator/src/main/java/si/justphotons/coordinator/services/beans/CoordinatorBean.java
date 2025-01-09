package si.justphotons.coordinator.services.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import si.justphotons.coordinator.entities.external.Organisation;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;

@Service
public class CoordinatorBean {

    @Value("${ORGANISATIONS_URL:http://localhost:8082/v1/organisations}")
    private String ORGANISATIONS_URL;

    @Value("${USERS_URL:http://localhost:8081/v1/users}")
    private String USERS_URL;


    public List<OrganisationEssentials> getOrganisations(Long userId) {
        List<OrganisationEssentials> essentials = new ArrayList<>();
        /* tmp !!!
        * fetch permission check instead
        */
        Map<Long, List<Long>> user2organisations = new HashMap<>() {{
            put(1L, Arrays.asList(1L, 2L));
            put(2L, Arrays.asList(1L, 3L));
        }};

        RestTemplate restTemplate = new RestTemplate();
        List<Long> organisations = user2organisations.get(userId);

        if (organisations != null) {
            for (Long orgId: organisations) {
                ResponseEntity<OrganisationEssentials> response =  restTemplate.getForEntity(
                    String.format("%s/essentials/%d", ORGANISATIONS_URL, orgId),
                    OrganisationEssentials.class
                );
                OrganisationEssentials essential = response.getBody();
                essentials.add(essential);
            }
        }

        return essentials;       
    }

    public Long getIdFromJWT(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        RestTemplate restTemplate = new RestTemplate();

        if (authHeader != null && !authHeader.isEmpty()) {
            // since the header should be added to each outgoing request,
            // add an interceptor that handles this.
            HttpHeaders headers = new HttpHeaders();
            headers.set("AUTHORIZATION", authHeader);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            String url = String.format("%s/id", USERS_URL);

            ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Long.class
            );
            return response.getBody();
          }
        return null;
    }

    public Organisation getOrganisation(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        Organisation response =  restTemplate.getForObject(
            String.format("%s/%d", ORGANISATIONS_URL, id),
            Organisation.class);
        return response;
    }
}
