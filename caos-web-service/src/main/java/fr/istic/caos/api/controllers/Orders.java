package fr.istic.caos.api.controllers;

import fr.istic.caos.api.dto.OrderTO;
import fr.istic.caos.api.errors.BadRequestException;
import fr.istic.caos.api.errors.WebServiceException;
import fr.istic.caos.api.managers.impl.ManagerFactory;
import fr.istic.caos.api.model.Customer;
import fr.istic.caos.api.model.Order;
import fr.istic.caos.api.model.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Le contrôleur REST pour les commandes.
 */
@Path("orders")
public class Orders extends AbstractController<Order> {

    /**
     * Constructeur du contrôleur pour /orders
     */
    public Orders() {
        super(ManagerFactory.getOrderManager());
    }

    /**
     * Récupère l'intégralité des commandes enregistrés dans le Web service.
     * @return Une liste de OrderTO
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderTO> getAllOrders() {
        List<OrderTO> orders = new ArrayList<>();
        for (Order o : manager.getAll()) {
            orders.add(createOrderTo(o));
        }
        return orders;
    }

    /**
     * Récupère une commande à partir de l'identifiant fourni dans la requête.
     * @param oId L'identifiant de la commande (paramètre de la requête)
     * @return Une réponse encapsulant un OrderTO, ou une erreur si un problème survient.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getOrderById(@PathParam("id") int oId){
        try{
            Order o = manager.get(oId);
            return getResponse(200, createOrderTo(o));
        }
        catch(WebServiceException ex){
            return ex.toResponse();
        }
    }

    /**
     * Gestion d'une requête POST d'une commande (création d'une commande).
     * @param oTO L'objet OrderTO fourni
     * @return Une Response
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(OrderTO oTO) {

        try {
            checkOrderTo(oTO);
            Product p = ManagerFactory.getProductManager().get(oTO.product_id);
            Customer c = ManagerFactory.getCustomerManager().get(oTO.customer_id);

            Order o =  new Order(oTO.id, new Date(), false, p, c);
            OrderTO to = createOrderTo(manager.create(o));

            return getResponse(201, to);
        } catch (WebServiceException ex) {
            return ex.toResponse();
        }
    }

    /**
     * Créer l'objet DTO associé à l'objet interne Order.
     * @param o L'objet Order
     * @return Une instance de OrderTO
     */
    private OrderTO createOrderTo(Order o) {
        OrderTO to = new OrderTO();
        to.customer_id = o.getClient().getId();
        to.product_id = o.getProduit().getId();
        to.id = o.getId();
        to.datetime = o.getDatetime().toInstant();
        return to;
    }

    /**
     * Annulation d'une commande
     * @param id identifiant de la commande à annuler.
     * @return Retourne une réponse avec un code de statut 200 si la commande a bien été supprimée,
     * et un code d'erreur sinon.
     */
    @DELETE
    @Path("{id}")
    public Response removeOrder(@PathParam("id") int id) {
        try {
            manager.delete(id);
            return Response.noContent().build();
        } catch (WebServiceException ex) {
            return ex.toResponse();
        }
    }

    /**
     * Vérifie que la commande fournie par le client est correcte.
     * @param dto L'objet DTO à contrôler
     * @throws WebServiceException Des exceptions de ce type sont levées lorsque certains attributs de la requête
     * sont manquants ou incorrects.
     */
    private void checkOrderTo(OrderTO dto) throws WebServiceException {

        if(dto.product_id < 0){
            throw new BadRequestException("Veuillez fournir un identifiant de produit valide");
        }

        if(dto.customer_id < 0){
            throw new BadRequestException("Veuillez fournir un identifiant de client valide");
        }
    }
}