package fr.istic.caos.api.controllers;

import fr.istic.caos.api.dto.CustomerTO;
import fr.istic.caos.api.errors.BadRequestException;
import fr.istic.caos.api.errors.WebServiceException;
import fr.istic.caos.api.managers.impl.ManagerFactory;
import fr.istic.caos.api.model.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static fr.istic.caos.Utils.isEmpty;

/**
 * Le contrôleur REST pour les clients.
 */
@Path("customers")
public class Customers extends AbstractController<Customer> {

    /**
     * Constructeur du contrôleur pour /customers
     */
    public Customers() {
        super(ManagerFactory.getCustomerManager());
    }

    /**
     * Récupère l'intégralité des clients enregistrés dans le Web service.
     * @return Une liste de CustomerTO
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerTO> getAllCustomers() {
        List<CustomerTO> customers = new ArrayList<>();
        for (Customer c : manager.getAll()) {
            customers.add(createCustomerTo(c));
        }
        return customers;
    }

    /**
     * Récupère un client à partir de l'identifiant fourni dans la requête.
     * @param cId L'identifiant du client (paramètre de la requête)
     * @return Une réponse encapsulant un CustomerTO, ou une erreur si un problème survient.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getCustomerById(@PathParam("id") int cId) {
        try {
            Customer c = manager.get(cId);
            return getResponse(200, createCustomerTo(c));
        } catch (WebServiceException ex) {
            return ex.toResponse();
        }
    }

    /**
     * Créé un client
     * @param cTO L'objet client à créer (DTO issu du corps de la requête en JSON)
     * @return La réponse encapsulant l'objet créé, ou une erreur si un problème survient
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(CustomerTO cTO) {
        try {
            checkCustomerTo(cTO);
            Customer c = new Customer(cTO.id, cTO.c_name, cTO.credit, cTO.password);
            c = manager.create(c);
            return getResponse(201, createCustomerTo(c));
        } catch (WebServiceException ex) {
            return ex.toResponse();
        }
    }

    /**
     * Modifie un client.
     * @param id L'identifiant du client à modifier
     * @param updates Les modifications partielles à apporter au client
     * @return Une réponse encapsulant l'objet modifié, ou une erreur si un problème survient
     */
    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modifyCustomer(@PathParam("id") int id, CustomerTO updates) {
        try {
            Customer c = manager.get(id);

            if (!isEmpty(updates.c_name)) {
                c.setName(updates.c_name);
            }
            if (!isEmpty(updates.password)) {
                c.setPassword(updates.password);
            }
            if (updates.credit != 0) {
                if (updates.credit < 0) {
                    throw new BadRequestException("Le crédit ne peut pas être négatif");
                }
                c.setCredit(updates.credit);
            }
            return getResponse(200, createCustomerTo(c));
        } catch (WebServiceException ex) {
            return ex.toResponse();
        }
    }

    /**
     * Supprime un client.
     * @param id L'identifiant du client à supprimer
     * @return Une réponse portant un code de retour suivant le déroulé de l'opération
     */
    @DELETE
    @Path("{id}")
    public Response removeCustomer(@PathParam("id") int id) {
        try {
            manager.delete(id);
            return Response.noContent().build();
        } catch (WebServiceException ex) {
            return ex.toResponse();
        }
    }

    /**
     * Créer le DTO associé à l'objet interne Customer
     *
     * @param c L'objet Customer
     * @return Une instance de CustomerTO
     */
    private CustomerTO createCustomerTo(Customer c) {
        CustomerTO dto = new CustomerTO();
        dto.id = c.getId();
        dto.c_name = c.getName();
        dto.credit = c.getCredit();
        return dto;
    }

    /**
     * Effectue les vérifications de données sur les objets envoyés par le client.
     * @param dto Le CustomerTO à vérifier
     * @throws WebServiceException Une erreur dans la requête du client a été trouvée.
     */
    private void checkCustomerTo(CustomerTO dto) throws WebServiceException {
        if (isEmpty(dto.c_name)) {
            throw new BadRequestException("Vous devez fournir un nom");
        }

        if (isEmpty(dto.password)) {
            throw new BadRequestException("Vous devez fournir un mot de pass");
        }
    }

}
