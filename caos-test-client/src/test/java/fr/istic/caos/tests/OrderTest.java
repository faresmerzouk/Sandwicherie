package fr.istic.caos.tests;

import fr.istic.caos.dto.CustomerTO;
import fr.istic.caos.dto.OrderTO;
import fr.istic.caos.dto.ProductTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderTest extends AbstractTest {

    @Test
    @DisplayName("POST /orders")
    public void testPOSTOrders() {
        OrderTO order = new OrderTO();
        order.customer_id = 1;
        order.product_id = 5;

        RequestSpecification request = createRequest();
        Response response = request.body(order).post("/orders");
        assertEquals(201, response.statusCode());

        order = response.as(OrderTO.class);
        assertEquals(1, order.customer_id);
        assertEquals(5, order.product_id);
    }

    @Test
    @DisplayName("DELETE /order{id}")
    public void testDELETEOrders() {
        Response response = RestAssured.delete("/orders/1");
        assertEquals(200, response.statusCode());

        response = RestAssured.delete("/orders/1");
        assertEquals(404, response.statusCode());

        response = RestAssured.get("/orders/1");
        assertEquals(404, response.statusCode());

        response = RestAssured.get("/orders/3");
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("GET /orders")
    public void testGETOrders() {
        Response response = RestAssured.get("/orders");
        assertEquals(200, response.statusCode());

        List<OrderTO> orders = Arrays.asList(response.as(OrderTO[].class));

        assertNotNull(orders);
        assertEquals(4, orders.size());

        OrderTO o = orders.get(0);
        assertEquals(1, o.id);
        assertEquals("2019-05-01T11:25:01Z", o.datetime);
        assertEquals(1, o.customer_id);
        assertEquals(2, o.product_id);

        o = orders.get(1);
        assertEquals(2, o.id);
        assertEquals("2018-05-02T12:25:02Z", o.datetime);
        assertEquals(2, o.customer_id);
        assertEquals(4, o.product_id);

        o = orders.get(2);
        assertEquals(3, o.id);
        assertEquals("2017-04-04T12:11:11Z", o.datetime);
        assertEquals(4, o.customer_id);
        assertEquals(1, o.product_id);

        o = orders.get(3);
        assertEquals(4, o.id);
        assertEquals("2014-05-01T11:01:10Z", o.datetime);
        assertEquals(3, o.customer_id);
        assertEquals(6, o.product_id);
    }

    @Test
    @DisplayName("GET /orders/{id}")
    public void testGETOrderById() {
        Response response = RestAssured.get("/orders/4");
        assertEquals(200, response.statusCode());

        OrderTO order = response.as(OrderTO.class);
        assertNotNull(order);
        assertEquals(4, order.id);
        assertEquals("2014-05-01T11:01:10Z", order.datetime);
        assertEquals(3, order.customer_id);
        assertEquals(6, order.product_id);

        response = RestAssured.get("/orders/20");
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Achat Sandwich OK - Les quantités et le stock sont correctement mis à jour")
    public void testAchatSandwich() {

        RequestSpecification request = createRequest();
        OrderTO order = new OrderTO();
        order.customer_id = 1;
        order.product_id = 5;

        Response responseGet1 = request.get("/products/5");
        ProductTO productInit = responseGet1.as(ProductTO.class);
        int stock = productInit.stock;
        float price = productInit.price;

        Response responseGet2 = request.get("/customers/1");
        CustomerTO customerInit = responseGet2.as(CustomerTO.class);
        float credit = customerInit.credit;

        Response responsePost = request.body(order).post("/orders");

        Response responseGet3 = request.get("/products/5");
        ProductTO productGet = responseGet3.as(ProductTO.class);
        Response responseGet4 = request.get("/customers/1");
        CustomerTO customerGet = responseGet4.as(CustomerTO.class);

        assertEquals(201, responsePost.statusCode());
        assertEquals((credit - price), customerGet.credit);
        assertEquals((stock - 1), productGet.stock);

    }

    @Test
    @DisplayName("Achat sandwich : pas assez de crédit pour acheter")
    public void testAchatSandwichDonne400() {

        RequestSpecification request = createRequest();
        OrderTO order = new OrderTO();
        order.customer_id = 1;
        order.product_id = 5;

        Response responseGet1 = request.get("/products/5");
        ProductTO productInit = responseGet1.as(ProductTO.class);
        productInit.price = 12000.0f;
        request.body(productInit).put("/products/5");
        Response responsePost = request.body(order).post("/orders");
        assertEquals(400, responsePost.statusCode());

    }

    @Test
    @DisplayName("Annuler une commande de Sandwich")
    public void testAnnuleOrder() {
        RequestSpecification request = createRequest();

        Response responseGetOrder = request.get("/orders/1");
        OrderTO orderGet = responseGetOrder.as(OrderTO.class);
        int idCust = orderGet.customer_id;
        int idProd = orderGet.product_id;

        Response responseGetProduct = request.get("/products/" + idProd);
        ProductTO productInit = responseGetProduct.as(ProductTO.class);
        int stock = productInit.stock;
        float price = productInit.price;

        Response responseGetCust = request.get("/customers/" + idCust);
        CustomerTO customerInit = responseGetCust.as(CustomerTO.class);
        float credit = customerInit.credit;

        Response responseDel1 = request.delete("/orders/1");

        assertEquals(200, responseDel1.statusCode());

        Response responseGet3 = request.get("/products/" + idProd);
        ProductTO productGet = responseGet3.as(ProductTO.class);
        Response responseGet4 = request.get("/customers/" + idCust);
        CustomerTO customerGet = responseGet4.as(CustomerTO.class);

        assertEquals((credit + price), customerGet.credit);
        assertEquals((stock + 1), productGet.stock);

    }

    @Test
    @DisplayName("Check post doesn't work when stock is 0")
    public void testPostStockNull() {
        RequestSpecification request = createRequest();

        Response responseGetProduct = request.get("/products/1");
        ProductTO productInit = responseGetProduct.as(ProductTO.class);
        Response responseGetOrder = request.get("/orders/1");
        OrderTO orderInit = responseGetOrder.as(OrderTO.class);
        productInit.stock=0;
        orderInit.customer_id = 1;

        Response responsePostPro = request.body(productInit).post("/products/");
        ProductTO productPost = responsePostPro.as(ProductTO.class);
        orderInit.product_id = productPost.id;

        Response responsePost = request.body(orderInit).post("/orders/");

        assertEquals(400, responsePost.statusCode());
    }
}