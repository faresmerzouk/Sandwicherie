package fr.istic.caos.api.errors;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Structure de base d'une exception représentant une erreur du Web Service.
 * Une erreur possède un message et un code de retour
 */
public abstract class WebServiceException extends RuntimeException {

    /**
     * Le code de retour
     */
    private final int statusCode;

    /**
     * Créer une erreur à partir d'un message et d'un code
     * @param statusCode Le code de retour
     * @param message Le messge d'erreur
     */
    protected WebServiceException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public final Response toResponse() {
        return Response
                .status(statusCode)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(this.getMessage())
                .build();
    }
}
