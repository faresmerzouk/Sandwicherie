package fr.istic.caos.api.managers.impl;

import fr.istic.caos.api.errors.BadRequestException;
import fr.istic.caos.api.errors.NotFoundException;
import fr.istic.caos.api.managers.AbstractManager;
import fr.istic.caos.api.managers.Manager;
import fr.istic.caos.api.model.Customer;
import fr.istic.caos.api.model.Order;
import fr.istic.caos.api.model.Product;

import java.util.Date;

import static fr.istic.caos.Utils.date;

public class OrderManager extends AbstractManager<Order> {

    private final Manager<Product> productManager = ManagerFactory.getProductManager();
    private final Manager<Customer> customerManager = ManagerFactory.getCustomerManager();

    /**
     * Remise à zéro technique de la "base de données".
     */
    @Override
    public void reset() {
        data.clear();
        addOrder(1, date("2019-05-01T10:25:01-01:00"), false, customerManager.get(1), productManager.get(2));
        addOrder(2, date("2018-05-02T11:25:02-01:00"), true, customerManager.get(2), productManager.get(4));
        addOrder(3, date("2017-04-04T11:11:11-01:00"), true, customerManager.get(4), productManager.get(1));
        addOrder(4, date("2014-05-01T10:01:10-01:00"), true, customerManager.get(3), productManager.get(6));
    }

    /**
     * Constructeur de la "base de données" en dur.
     */
    OrderManager() {
        reset();
    }

    /**
     * Ajouter une commande en dur dans la "base de données".
     * @param id Identifiant de la commande à ajouter.
     * @param dateOrder Date de la commande.
     * @param taken Statut de la commande (retirée ou non).
     * @param customer Nom du client ayant passé la commande.
     * @param product Nom du sandwich commandé.
     */
    public void addOrder(Integer id, Date dateOrder, boolean taken, Customer customer, Product product){
        data.put(id, new Order(id, dateOrder, taken, product, customer));
    }

    /**
     * Créer une commande de sandwich.
     * @param o La commande à créer
     * @return une commande avec mise à jour du stock de sandwiches et du solde du client.
     */
    @Override
    public Order create(Order o) {
        checkOrder(o);
        decrementStock(o);
        decrementSolde(o);
        return super.create(o);
    }

    /**
     * Vérifier la validité d'une commande (ie. stock de sandwiches strictement supérieur à 0
     * et solde du client supérieur ou égal au prix du sandwich).
     * @param o La commande dont on cherche à vérifier la validité.
     */
    private void checkOrder(Order o)
    {
        Product p = productManager.get(o.getProduit().getId());
        if(p.getStock() < 1){
            throw new BadRequestException("Le produit n'est plus en stock.");
        }

        Customer c = customerManager.get(o.getClient().getId());
        if(c.getCredit() < p.getPrice()){
            throw new BadRequestException("Votre crédit n'est pas suffisant.");
        }
    }

    /**
     * Décrémenter le stock en cas de commande.
     * @param o La commande qui provoque la décrémentation du stock.
     */
    private void decrementStock(Order o){
        Product p = productManager.get(o.getProduit().getId());
        p.setStock(p.getStock() - 1);
    }

    /**
     * Décrémenter le solde d'un client qui passe une commande.
     * @param o La commande qui provoque la décrémentation du solde du client.
     */
    private void decrementSolde(Order o){
        Customer c = customerManager.get(o.getClient().getId());
        Product p = productManager.get(o.getProduit().getId());
        c.setCredit(c.getCredit() - p.getPrice());
    }

    /**
     * Annuler une commande.
     * @param id Identifiant de la commande à annuler.
     */
    @Override
    public void delete(int id) {
        if (!data.containsKey(id)) {
            throw new NotFoundException();
        }
        Order o = data.get(id);
        checkTakeAway(o);
        incrementSoldeAndStock(o);
        super.delete(id);
    }

    /**
     * Vérifier qu'une commande n'a pas été retirée.
     * @param o La commande.
     */
    private void checkTakeAway(Order o){
        if(o.isTaken()){
            throw new BadRequestException("Votre commande est déjà retirée : vous ne pouvez plus l'annuler.");
        }
    }

    /**
     * Mettre à jour (incrémenter) le solde d'un client ainsi que le stock de sandwiches (méthode utilisée
     * dans le cas d'une annulation de commande non retirée.
     * @param o La commande.
     */
    private void incrementSoldeAndStock(Order o){
        Product p = o.getProduit();
        Customer c = o.getClient();
        p.setStock(p.getStock() + 1);
        c.setCredit(c.getCredit() + p.getPrice());
    }
}