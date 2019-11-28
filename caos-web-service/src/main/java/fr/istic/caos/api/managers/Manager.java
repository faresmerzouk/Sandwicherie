package fr.istic.caos.api.managers;

import fr.istic.caos.api.model.Entity;

import java.util.List;

/**
 * Classe qui s'occupe de gérer les objets de données de l'application
 * @param <T> Le type de l'entité
 */
public interface Manager<T extends Entity> {

    /**
     * Récupérer toutes les instances de données
     * @return Une liste d'instances
     */
    List<T> getAll();

    /**
     * Récupère l'entité portant l'id passé en paramètre
     * @param id Id de l'entité
     * @return L'entité, ou null si non trouvée
     */
    T get(int id);

    /**
     * Créé l'entité
     * @param object L'entité à créer
     * @return L'objet créé
     */
    T create(T object);

    /**
     * Met à jour l'entité portant l'id passé en paramètre
     * @param id L'id de l'entité à modifier
     * @param object L'objet contenant les modifications à apporté à l'objet
     * @return L'objet modifié
     */
    T update(int id, T object);

    /**
     * Supprime l'entité portant l'oid passé en paramètre
     * @param id L'id de l'objet à supprimer
     */
    void delete(int id);

    /**
     * Remise à zéro du manager (test)
     */
    void reset();
}
