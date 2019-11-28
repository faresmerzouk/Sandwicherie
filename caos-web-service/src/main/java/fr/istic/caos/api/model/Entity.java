package fr.istic.caos.api.model;

/**
 * Représente une entité.
 * Une entité possède forcément un identifiant unique.
 */
public interface Entity {
    /**
     * L'identifiant unique de l'entité
     * @return L'identifiant
     */
    int getId();

    /**
     * Modifie l'identifiant de l'entité
     * @param id L'identifiant
     */
    void setId(int id);
}
