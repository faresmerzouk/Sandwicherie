package fr.istic.caos.api.managers;

import fr.istic.caos.api.errors.NotFoundException;
import fr.istic.caos.api.model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractManager<T extends Entity> implements Manager<T> {
    /**
     * Le compteur d'incrémentation pour le type de ressource concerné
     */
    private final AtomicInteger autoIncrement = new AtomicInteger(0);

    /**
     * Le tableau associant son identifiant à l'objet métier
     */
    protected final SortedMap<Integer, T> data = new ConcurrentSkipListMap<>();

    /**
     * Récupère tous les objets du type du manager.
     * @return Une liste d'instance de T
     */
    @Override
    public List<T> getAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * Récupère un objet portant l'identifiant spécifié en paramètre
     * @param id identifiant unique de l'entité
     * @return Une instance de l'objet T
     */
    @Override
    public T get(int id) {
        if (!data.containsKey(id))
            throw new NotFoundException();

        return data.get(id);
    }

    /**
     * Créé un objet.
     * @param object L'objet à créer
     * @return L'ojet créé
     */
    @Override
    public T create(T object) {
        int nextId = autoIncrement.incrementAndGet();
        object.setId(nextId);
        data.put(nextId, object);
        return object;
    }

    /**
     * Mettre à jour les attributs d'un objet.
     * @param id L'id de l'entité à modifier
     * @param object L'objet contenant les modifications à apporter à l'objet
     * @return L'objet modifié
     */
    @Override
    public T update(int id, T object) {
        if (!data.containsKey(id))
            throw new NotFoundException();

        return data.put(id, object);
    }

    /**
     * Supprime un objet.
     * @param id L'id de l'objet à supprimer
     */
    @Override
    public void delete(int id) {
        if(!data.containsKey(id))
            throw new NotFoundException();

        data.remove(id);
    }
}