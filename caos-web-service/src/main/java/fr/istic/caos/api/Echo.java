package fr.istic.caos.api;

import fr.istic.caos.api.managers.impl.ManagerFactory;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Fabien
 */
@Path("/")
public class Echo {

    /**
     * Une requête pour tester de manière simple la conectivité au Web service.
     */
    @GET
    @Path("echo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response reply() {
        // 200 OK
        return Response.ok("Bip bip ! I'm alive.").build();
    }

    /**
     * Remise à l'état zéro technique de la base de données pour les tests.
     */
    @DELETE
    public Response reset() {
        ManagerFactory.resetManagers();
        return Response.noContent().build();
    }

}
