package si.justphotons.api.v1;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//@SecurityScheme(name = "openid-connect", type = SecuritySchemeType.OPENIDCONNECT,
//        openIdConnectUrl = "http://auth-server-url/.well-known/openid-configuration")
//@OpenAPIDefinition(info = @Info(title = "Rest API", version = "v1", contact = @Contact(), license = @License(), description = "JavaSI API for managing conference."), security = @SecurityRequirement(name = "openid-connect"), servers = @Server(url ="http://localhost:8080/v1"))
@ApplicationPath("v1")
public class JustPhotonsApplication extends Application {

}
