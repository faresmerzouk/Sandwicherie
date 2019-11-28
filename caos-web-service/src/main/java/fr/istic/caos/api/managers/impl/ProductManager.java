package fr.istic.caos.api.managers.impl;

import fr.istic.caos.api.managers.AbstractManager;
import fr.istic.caos.api.model.Product;

public class ProductManager extends AbstractManager<Product> {
    //Constructeur par défaut :
    ProductManager() {
        reset();
    }

    private void addProduct(int id, String name, int initStock, int stock, String ingredients, float price) {
        data.put(id, new Product(id, name, price, initStock, stock, ingredients));
    }



    //Remettre à zéro le contenu de la map :
    @Override
    public void reset() {
        data.clear();
        addProduct(1, "Parisien", 50, 30, "Pain, jambon, beurre", 3);
        addProduct(2, "Elvis", 30, 10, "Pain, beurre de cacahuètes, banane, bacon", 4);
        addProduct(3, "BLT", 30, 10, "Bacon, laitue, tomate", 4);
        addProduct(4, "Jambon beurre", 50, 38, "Pain, jambon, beurre", 2);
        addProduct(5, "Belle-mère", 40, 30, "Pain, viande hachée, maïs, piment", 10);
        addProduct(6, "Jambon emmental", 60, 29, "Pain, jambon, emmental", 3);
    }
}