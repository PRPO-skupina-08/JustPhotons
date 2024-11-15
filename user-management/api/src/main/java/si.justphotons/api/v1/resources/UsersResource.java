package si.justphotons.api.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import com.kumuluz.ee.rest.beans.QueryParameters;

import si.justphotons.entities.User;
import si.justphotons.beans.UsersBean;

import java.util.List;

    //@Secure
    @ApplicationScoped
    @Path("users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private UsersBean uporabnikiZrno;

    @Operation(description = "Vrne seznam uporabnikov.", summary = "Seznam uporabnikov")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam uporabnikov",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih uporabnikov")}
            )})
    @RolesAllowed("user")
    @GET
    public Response getUsers() {
        List<User> allUsers = uporabnikiZrno.getUsers();
        Long userCount = uporabnikiZrno.getUsersCount(null);
        return Response.ok(allUsers).header("X-Total_count", userCount).build();
    }

    @POST
    public Response addUser(User uporabnik) {
        uporabnikiZrno.addUser(uporabnik);
        return Response.noContent().build();
    }

    @GET
    @Path("{id_uporabnika}")
    public Response getUser(@PathParam("id_uporabnika") Integer id_uporabnika) {
        User uporabnik = uporabnikiZrno.getUser(id_uporabnika);
        return uporabnik != null
                ? Response.ok(uporabnik).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id_uporabnika}")
    public Response deleteUser(@PathParam("id_uporabnika") Integer id_uporabnika) {
        boolean succ = uporabnikiZrno.deleteUser(id_uporabnika);
        return succ
            ? Response.ok(id_uporabnika).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }
}
