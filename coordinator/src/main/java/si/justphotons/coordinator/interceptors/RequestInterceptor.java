package si.justphotons.coordinator.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import si.justphotons.coordinator.services.beans.CoordinatorBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Value("${USERS_URL:http://localhost:8081/v1}")
    private String USERS_URL;

    // Request is intercepted by this method before reaching the Controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //* Business logic just when the request is received and intercepted by this interceptor before reaching the controller
        try {
            Long userId = getIdFromJWT(request);
            if (userId == null) {
                System.out.println("niste prijavljeni");
                response.sendError(401, "Niste prijavljeni");
                return false;
            }
            System.out.printf("User ID: %d\n", userId);
            request.setAttribute("userId", userId);
        }
        //* If the Exception is caught, this method will return false
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Response is intercepted by this method before reaching the client
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //* Business logic just before the response reaches the client and the request is served
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method is called after request & response HTTP communication is done.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //* Business logic after request and response is Completed
        try {
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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