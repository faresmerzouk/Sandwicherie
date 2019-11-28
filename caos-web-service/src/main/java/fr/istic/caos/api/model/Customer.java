package fr.istic.caos.api.model;

public class Customer implements Entity {
    /**
     * L'identifiant unique
     */
    private int id;

    /**
     * Le nom du client
     */
    private String name;

    /**
     * Le solde du compte client
     */
    private float credit;

    /**
     * Le mot de passe de connexion du client
     */
    private String password;

    /**
     * Créer un client
     * @param id L'identifiant
     * @param name Le nom
     * @param credit Le solde
     * @param password Le mot de passe
     */
    public Customer(int id, String name, float credit, String password) {
        this.id = id;
        this.name = name;
        this.credit = credit;
        this.password = password;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getCredit() {
        return this.credit;
    }

    public String getPassword() {
        return this.password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
