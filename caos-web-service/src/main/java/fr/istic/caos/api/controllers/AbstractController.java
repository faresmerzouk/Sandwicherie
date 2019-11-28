package fr.istic.caos.api.controllers;

import fr.istic.caos.api.managers.Manager;
import fr.istic.caos.api.model.Entity;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Représente la structure commune d'un contrôleur.
 * @param <T> Le type de l'objet exposé par le contrôleur
 */
public abstract class AbstractController<T extends Entity> {
    /**
     * Le manager chargé de récupérer les produits depuis la source de données
     */
    protected final Manager<T> manager;

    /**
     * Constructeur de base
     * @param manager Le manager permettant de récupérer les objets à exposer
     */
    protected AbstractController(Manager<T> manager) {
        this.manager = manager;
    }

    /**
     * Créé une réponse HTTP
     * @param statusCode Le code de statut
     * @param entity L'objet à incorporer dans le corps de la réponse
     * @param <R> Le type d'objet de la réponse
     * @return L'instance de Response
     */
    protected final <R> Response getResponse(int statusCode, R entity) {
        return getResponse(statusCode, entity, MediaType.APPLICATION_JSON_TYPE);
    }

    /**
     * Créé une réponse HTTP
     * @param statusCode Le code de statut
     * @param entity L'objet à incorporer dans le corps de la réponse
     * @param contentType Le type de contenu
     * @param <R> Le type d'objet de la réponse
     * @return L'instance de Response
     */
    protected final <R> Response getResponse(int statusCode, R entity, MediaType contentType) {
        return Response
                .status(statusCode)
                .type(contentType)
                .entity(entity)
                .build();
    }
}
