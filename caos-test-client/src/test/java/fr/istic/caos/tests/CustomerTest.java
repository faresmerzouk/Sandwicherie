package fr.istic.caos.tests;

import fr.istic.caos.dto.CustomerTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest extends AbstractTest {

    @Test
    @DisplayName("Get all customers")
    public void testGetCustomers() {
        Response response = RestAssured.get("/customers");
        assertEquals(200, response.statusCode());

        List<CustomerTO> client = Arrays.asList(response.getBody().as(CustomerTO[].class));
        assertNotNull(client);

        assertTrue(client.size() > 0);

        CustomerTO customer = client.get(0);
        assertEquals("Dupond", customer.c_name);
        assertEquals(50, customer.credit);

        customer = client.get(1);
        assertEquals("Haddock", customer.c_name);
        assertEquals(40, customer.credit);

        customer = client.get(2);
        assertEquals("Tournesol", customer.c_name);
        assertEquals(30, customer.credit);

        customer = client.get(3);
        assertEquals("Milou", customer.c_name);
        assertEquals(200, customer.credit);
    }


    @Test
    @DisplayName("Get a customer by id")
    public void testGetCustomerById() {
        Response response = RestAssured.get("/customers/2");
        assertEquals(200, response.statusCode());

        CustomerTO customer = response.as(CustomerTO.class);
        assertNotNull(customer);

        assertEquals("Haddock", customer.c_name);
        assertEquals(40, customer.credit);
    }

    @Test
    @DisplayName("Get a customer who is not int the customers'list")
    public void testErrorGetCustomerById() {
        Response response = RestAssured.get("/customers/20");
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Post a customer")
    public void testPOSTCustomers() {

        // Création d'une requête (on la stocke pour pouvoir y incorporer le DTO à envoyer)
        RequestSpecification request = createRequest();

        // Création du DTO de test
        CustomerTO customer = new CustomerTO();
        customer.c_name = "Tintin";
        customer.credit = 40;
        customer.password = "Fusée";

        // On envoie la requête et on désérialise le résultat en CustomerTO
        Response response = request.body(customer).post("/customers");
        customer = response.as(CustomerTO.class);

        // L'objet a bien été créé
        assertEquals(201, response.statusCode());

        // On récupère l'id du DTO renvoyé par le serveur
        int id = customer.id;

        // On lance une requête GET avec cet id pour s'assurer qu'il est présent dans le Web service
        response = RestAssured.get("/customers/" + id);
        assertEquals(200, response.statusCode());
        customer = response.as(CustomerTO.class);

        // Je vérifie que le contenu de l'objet créé correspond au DTO que j'ai envoyé.
        assertEquals("Tintin", customer.c_name);
        assertEquals(40.0, customer.credit);
    }

    @Test
    @DisplayName("PUT /customers error 400-404")
    public void testErrorPutCustomers() {
        RequestSpecification request = createRequest();
        CustomerTO customer = new CustomerTO();
        customer.c_name = "Alcazar";
        customer.credit = -50;
        customer.id=1;


        Response response = request.body(customer).put("/customers/1");
        assertEquals(400, response.statusCode());

        Response response4 = request.body(new CustomerTO()).put("/customers/160");
        assertEquals(404, response4.statusCode());
    }

    @Test
    @DisplayName("Delete a customer by id ")
    public void testDELETECustomers() {
        Response response = RestAssured.delete("/customers/2");
        assertEquals(200, response.statusCode());

    }
}
