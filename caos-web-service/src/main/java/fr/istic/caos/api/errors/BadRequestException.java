package fr.istic.caos.api.errors;

/**
 * Représente une erreur HTTP 400.
 */
public class BadRequestException extends WebServiceException {
    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException() {
        super(400, "La requête n'est pas correcte.");
    }
}
