package fr.istic.caos.api.managers.impl;

import fr.istic.caos.api.managers.AbstractManager;
import fr.istic.caos.api.model.Customer;

public class CustomerManager extends AbstractManager<Customer> {

    CustomerManager() {
        reset();
    }

    private void addCustomer(int id, String name, float credit, String password) {
        data.put(id, new Customer(id, name, credit, password));
    }

    @Override
    public void reset() {
        data.clear();
        addCustomer(1, "Dupond", 50, "Melon");
        addCustomer(2, "Haddock", 40, "Licorne");
        addCustomer(3, "Tournesol", 30, "Lune");
        addCustomer(4, "Milou", 200, "Os");
    }
}
