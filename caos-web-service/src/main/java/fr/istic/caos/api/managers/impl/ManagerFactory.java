package fr.istic.caos.api.managers.impl;

import fr.istic.caos.api.managers.Manager;
import fr.istic.caos.api.model.Customer;
import fr.istic.caos.api.model.Order;
import fr.istic.caos.api.model.Product;

public abstract class ManagerFactory
{
    private static Manager<Product> productManager;

    public static Manager<Product> getProductManager() {
        if (productManager == null) {
            productManager = new ProductManager();
        }
        return productManager;
    }

    private static Manager<Customer> customerManager;

    public static Manager<Customer> getCustomerManager() {
        if (customerManager == null) {
            customerManager = new CustomerManager();
        }
        return customerManager;
    }

    private static Manager<Order> orderManager;

    public static Manager<Order> getOrderManager() {
        if (orderManager == null) {
            orderManager = new OrderManager();
        }
        return orderManager;
    }

    public static void resetManagers() {
        getProductManager().reset();
        getCustomerManager().reset();
        getOrderManager().reset();
    }
}