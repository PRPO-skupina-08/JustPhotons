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
import si.justphotons.api.v1.dtos.UserCreateDTO;
import si.justphotons.servicedtos.UserSendDTO;

import javax.validation.Valid; // To trigger validation for objects or collections

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

    // tale openAPI dela na /openapi (raw yaml) in na /api-specs/ui (za lep prikaz)
    @Operation(description = "Vrne seznam uporabnikov.", summary = "Seznam uporabnikov")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam uporabnikov",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vseh uporabnikov")}
            )
    })
    @GET
    public Response getUsers() {
        List<UserSendDTO> allUsers = uporabnikiZrno.getUsers();
        Long userCount = uporabnikiZrno.getUsersCount(null);
        return Response.ok(allUsers).header("X-Total_count", userCount).build();
    }

    @Operation(description = "Vrne seznam uporabnikov glede na podani filter. Možne filtre lahko najdemo tukaj: https://github.com/kumuluz/kumuluzee-rest", summary = "Seznam uporabnikov glede na filter")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam uporabnikov",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vseh uporabnikov glede na filter")}
            )
    })
    @GET
    @Path("/filtered")
    public Response getUsersFiltered() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<UserSendDTO> users = uporabnikiZrno.getUsers(query);
        Long userCount = uporabnikiZrno.getUsersCount(query);
        return Response.ok(users).header("X-Total_count", userCount).build();
    }



    @Operation(description = "Omogoča dodajanje uporabnika.", summary = "Dodajanje uporabnika.")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Uporabnik ustvarjen",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.OBJECT))
            ),
            @APIResponse(responseCode = "400",
                    description = "Podatki v telesu zahtevka niso ustrezne oblike"
            )
    })
//    @RequestBody(
//            description = "User creation payload",
//            required = true,
//            content = @Content(
//                    mediaType = "application/json",
//                    schema = @Schema(implementation = UserCreateDTO.class, type = SchemaType=OBJECT)
//            )
//    )
    @POST
    public Response addUser(@Valid UserCreateDTO userCreateDTO) {

        User user = convertToEntity(userCreateDTO);
        UserSendDTO dto = uporabnikiZrno.addUser(user);
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    @GET
    @Path("{id_uporabnika}")
    public Response getUser(@PathParam("id_uporabnika") Integer id_uporabnika) {
        UserSendDTO dto;
        try {
            dto = uporabnikiZrno.getUser(id_uporabnika);
        } catch (Error err) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(dto).build();
    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") Integer userId) {
        boolean succ = uporabnikiZrno.deleteUser(userId);
        return succ
            ? Response.ok(userId).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{userId}")
    public Response updateUser(@PathParam("userId") Integer userId, @Valid UserCreateDTO userCreateDTO) {
        User user = convertToEntity(userCreateDTO);
        UserSendDTO dto;
        try {
            dto = uporabnikiZrno.updateUser(userId, user);
        } catch (Error err) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (dto != null) {
            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }


    }


    /*
        DTO conversion methods:
    */

    public User convertToEntity(UserCreateDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        return user;
    }
    public UserCreateDTO convertToDTO(User user) {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
