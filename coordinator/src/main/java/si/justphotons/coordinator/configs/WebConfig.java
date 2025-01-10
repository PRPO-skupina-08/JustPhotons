package si.justphotons.coordinator.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import si.justphotons.coordinator.interceptors.RequestInterceptor;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    RequestInterceptor interceptor;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**");
    // }

    // Register an interceptor with the registry, Interceptor name : RequestInterceptor
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
        .excludePathPatterns("/api/v1/register", "/api/v1/login");     // Exclude specific URL patterns
        // .addPathPatterns("/demo/**", "/api/**")  // Add specific URL patterns
  }
}
