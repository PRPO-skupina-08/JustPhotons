package si.justphotons.coordinator.services.beans;

import java.lang.reflect.Array;
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
import org.springframework.web.cors.PreFlightRequestHandler;

import ch.qos.logback.core.pattern.color.BoldBlueCompositeConverter;
import jakarta.servlet.http.HttpServletRequest;
import si.justphotons.coordinator.api.v1.dtos.LoginEssentials;
import si.justphotons.coordinator.api.v1.dtos.RegistrationEssentials;
import si.justphotons.coordinator.entities.external.Organisation;
import si.justphotons.coordinator.entities.external.OrganisationEssentials;
import si.justphotons.coordinator.entities.external.Permission;

@Service
public class CoordinatorBean {

    @Value("${ORGANISATIONS_URL:http://localhost:8082/v1/organisations}")
    private String ORGANISATIONS_URL;

    @Value("${USERS_URL:http://localhost:8081/v1}")
    private String USERS_URL;

    @Value("${IMAGES_URL:http://localhost:8083/api/v1/images}")
    private String IMAGES_URL;

    @Value("${METADATA_URL:http://localhost:8084/api/v1/metadata}")
    private String METADATA_URL;

    @Value("${PERMISSIONS_URL:http://localhost:8085/api/v1/permissions}")
    private String PERMISSIONS_URL;


    public List<OrganisationEssentials> getOrganisations(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        List<OrganisationEssentials> essentials = new ArrayList<>();

        ResponseEntity<Permission[]> response = restTemplate.getForEntity(
            String.format("%s?user_id=%d", PERMISSIONS_URL, userId),
             Permission[].class
        );
        List<Permission> organisations = Arrays.asList(response.getBody());
        
        for (Permission perm : organisations) {
            ResponseEntity<OrganisationEssentials> res =  restTemplate.getForEntity(
                String.format("%s/essentials/%d", ORGANISATIONS_URL, perm.getOrgId()),
                OrganisationEssentials.class
            );
            OrganisationEssentials essential = res.getBody();
            essentials.add(essential);
        }

        return essentials;       
    }

    public Organisation getOrganisation(Long orgId, Long userId) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Permission[]> response = restTemplate.getForEntity(
            String.format("%s?user_id=%d&org_id=%d", PERMISSIONS_URL, userId, orgId),
             Permission[].class
        );
        Permission[] permissions = response.getBody();
        if (permissions == null) {
            return null;
        }

        Boolean allowed = permissions.length != 0;
        if (!allowed) {
            return null;
        }

        Organisation res =  restTemplate.getForObject(
            String.format("%s/%d", ORGANISATIONS_URL, orgId),
            Organisation.class);
        return res;
    }


    /* for auth */

    public String login(LoginEssentials loginEssentials) {
        RestTemplate restTemplate = new RestTemplate();
        String response =  restTemplate.postForObject(
            String.format("%s/login", USERS_URL),
            loginEssentials,
            String.class
        );
        return response;
    }

    public String register(RegistrationEssentials registrationEssentials) {
        RestTemplate restTemplate = new RestTemplate();
        String response =  restTemplate.postForObject(
            String.format("%s/register", USERS_URL),
            registrationEssentials,
            String.class
        );
        return response;
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

            String url = String.format("%s/users/id", USERS_URL);
            try {
                ResponseEntity<Long> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                Long.class
                );
                return response.getBody();
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
            
          }
        return null;
    }
}
