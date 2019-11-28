package fr.istic.caos.api.errors;

/**
 * Représente une erreur HTTP 404.
 */
public class NotFoundException extends WebServiceException {

    public NotFoundException(String message){
        super(404, message);
    }

    public NotFoundException(){
        super(404, "La ressource requise n'a pas été trouvée.");
    }
}