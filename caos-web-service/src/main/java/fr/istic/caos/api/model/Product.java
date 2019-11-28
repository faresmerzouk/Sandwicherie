package fr.istic.caos.api.model;

public class Product implements Entity {
    /**
     * L'identifiant unique
     */
    private int id;

    /**
     * Le nom du produit
     */
    private String name;

    /**
     * Le prix
     */
    private float price;

    /**
     * Le stock disponible
     */
    private int stock;

    /**
     * Le stock initial
     */
    private int initStock;

    /**
     * La liste des ingrédients
     */
    private String ingredients;

    /**
     * Créé un produit
     * @param id L'identifiant unique
     * @param name Le nom
     * @param price Le prix
     * @param initStock Le stock initial
     * @param stock Le stock
     * @param ingredients La liste des ingrédients
     */
    public Product(int id, String name, float price, int initStock, int stock, String ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.initStock = initStock;
        this.ingredients = ingredients;
    }

    /**
     * Créé un produit
     */
    public Product() {
    }

    @Override
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    public int getStock() {
        return this.stock;
    }

    public int getInitStock() {
        return this.initStock;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setInitStock(int initStock) {
        this.initStock = initStock;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
