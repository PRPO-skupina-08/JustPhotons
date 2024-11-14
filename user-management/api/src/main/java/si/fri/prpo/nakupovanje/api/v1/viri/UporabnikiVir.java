package si.fri.prpo.nakupovanje.api.v1.viri;

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

import si.fri.prpo.nakupovanje.entitete.Uporabnik;
import si.fri.prpo.nakupovanje.zrna.UporabnikiZrno;

import java.util.List;

//@Secure
@ApplicationScoped
@Path("uporabniki")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UporabnikiVir {

    @Context
    private UriInfo uriInfo;

    @Inject
    private UporabnikiZrno uporabnikiZrno;

    @Operation(description = "Vrne seznam uporabnikov.", summary = "Seznam uporabnikov")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Seznam uporabnikov",
                    content = @Content(schema = @Schema(implementation = Uporabnik.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Število vrnjenih uporabnikov")}
            )})
    @RolesAllowed("user")
    @GET
    public Response pridobiUporabnike() {
        List<Uporabnik> allUsers = uporabnikiZrno.pridobiUporabnike();
        Long userCount = uporabnikiZrno.pridobiUporabnikeCount(null);
        return Response.ok(allUsers).header("X-Total_count", userCount).build();
    }

    @POST
    public Response dodajUporabnika(Uporabnik uporabnik) {
        uporabnikiZrno.dodajUporabnika(uporabnik);
        return Response.noContent().build();
    }

    @GET
    @Path("{id_uporabnika}")
    public Response pridobiUporabnika(@PathParam("id_uporabnika") Integer id_uporabnika) {
        Uporabnik uporabnik = uporabnikiZrno.pridobiUporabnika(id_uporabnika);
        return uporabnik != null
                ? Response.ok(uporabnik).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id_uporabnika}")
    public Response odstraniUporabnika(@PathParam("id_uporabnika") Integer id_uporabnika) {
        boolean succ = uporabnikiZrno.odstraniUporanbika(id_uporabnika);
        return succ
            ? Response.ok(id_uporabnika).build()
            : Response.status(Response.Status.NOT_FOUND).build();
    }
}
