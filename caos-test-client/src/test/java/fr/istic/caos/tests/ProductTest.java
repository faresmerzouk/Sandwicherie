package fr.istic.caos.tests;

import fr.istic.caos.dto.ProductTO;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest extends AbstractTest {

    @Test
    @DisplayName("GET /products")
    public void testGetProducts() {

        Response response = RestAssured.get("/products");
        assertEquals(200, response.statusCode());

        List<ProductTO> products = Arrays.asList(response.as(ProductTO[].class));

        assertNotNull(products);
        assertTrue(products.size() > 0);

        ProductTO p = products.get(0);
        assertEquals("Parisien", p.p_name);
        assertEquals(50, p.initStock);
        assertEquals(30, p.stock);
        assertEquals("Pain, jambon, beurre", p.ingredients);
        assertEquals(3.0, p.price);

        p = products.get(1);
        assertEquals("Elvis", p.p_name);
        assertEquals(30, p.initStock);
        assertEquals(10, p.stock);
        assertEquals("Pain, beurre de cacahuètes, banane, bacon", p.ingredients);
        assertEquals(4.0, p.price);

        p = products.get(2);
        assertEquals("BLT", p.p_name);
        assertEquals(30, p.initStock);
        assertEquals(10, p.stock);
        assertEquals("Bacon, laitue, tomate", p.ingredients);
        assertEquals(4.0, p.price);

        p = products.get(3);
        assertEquals("Jambon beurre", p.p_name);
        assertEquals(50, p.initStock);
        assertEquals(38, p.stock);
        assertEquals("Pain, jambon, beurre", p.ingredients);
        assertEquals(2.0, p.price);

        p = products.get(4);
        assertEquals("Belle-mère", p.p_name);
        assertEquals(40, p.initStock);
        assertEquals(30, p.stock);
        assertEquals("Pain, viande hachée, maïs, piment", p.ingredients);
        assertEquals(10.0, p.price);
        p = products.get(5);
        assertEquals("Jambon emmental", p.p_name);
        assertEquals(60, p.initStock);
        assertEquals(29, p.stock);
        assertEquals("Pain, jambon, emmental", p.ingredients);
        assertEquals(3.0, p.price);

    }


    @Test
    @DisplayName("GET /products/{id}")
    public void testGetProductsById() {
        Response response = RestAssured.get("/products/2");
        assertEquals(200, response.statusCode());

        ProductTO product = response.as(ProductTO.class);
        assertNotNull(product);
        assertEquals("Elvis", product.p_name);
        assertEquals(30, product.initStock);
        assertEquals(10, product.stock);
        assertEquals("Pain, beurre de cacahuètes, banane, bacon", product.ingredients);
        assertEquals(4.0, product.price);

    }

    @Test
    @DisplayName("GET /products/{id} => 404")
    public void testErrorGetProductsById() {
        Response response = RestAssured.get("/products/20");
        assertEquals(404, response.statusCode());
    }


    @Test
    @DisplayName("POST /products")
    public void testPOSTProducts() {
        RequestSpecification request = createRequest();

        ProductTO product = new ProductTO();
        product.p_name = "Jambon pêche";
        product.price = 10.0f;
        product.ingredients = "Pain, jambon, pêche";
        product.initStock = 3;
        product.stock = 2;

        Response response = request.body(product).post("/products");
        assertEquals(201, response.statusCode());
        product = response.as(ProductTO.class);


        response = RestAssured.get("/products/" + product.id);
        product = response.as(ProductTO.class);

        assertEquals("Jambon pêche", product.p_name);
        assertEquals(3, product.initStock);
        assertEquals(2, product.stock);
        assertEquals("Pain, jambon, pêche", product.ingredients);
        assertEquals(10.0, product.price);

    }


    @Test
    @DisplayName("PUT /products")
    public void testPutProduct() {

        RequestSpecification request = createRequest();

        ProductTO product = new ProductTO();
        product.p_name = "Jambon pêche";
        product.price = 10.0f;
        product.ingredients = "Pain, jambon, pêche";
        product.initStock = 3;
        product.stock = 2;

        Response response = request.body(product).put("/products/1");
        assertEquals(200, response.statusCode());


        response = RestAssured.get("/products/1");
        product = response.as(ProductTO.class);

        assertEquals("Jambon pêche", product.p_name);
        assertEquals(3, product.initStock);
        assertEquals(2, product.stock);
        assertEquals("Pain, jambon, pêche", product.ingredients);
        assertEquals(10.0, product.price);

    }
    @Test
    @DisplayName("PUT /products - negative values errors")
    public void testnegtiveErrorPutProducts() {
        RequestSpecification request = createRequest();
        ProductTO product = new ProductTO();
        product.p_name = "Jambon pêche";
        product.price = -10.0f;
        product.ingredients = "Pain, jambon, pêche";
        product.initStock = 3;
        product.stock = 2;

        Response response2 = request.body(product).put("/products/1");
        assertEquals(400, response2.statusCode());
    }

    @Test
    @DisplayName("PUT /products - not found error")
    public void testErrorPutProducts() {
        RequestSpecification request = createRequest();
        ProductTO product = new ProductTO();
        product.p_name = "Jambon pêche";
        product.price = 10.0f;
        product.ingredients = "Pain, jambon, pêche";
        product.initStock = 3;
        product.stock = 2;

        Response response2 = request.body(product).put("/products/9");
        assertEquals(404, response2.statusCode());
    }


    @Test
    @DisplayName("DELETE /products/pid")
    public void testDeleteProduct() {

        RequestSpecification request = createRequest();
        ProductTO product = new ProductTO();
        product.initStock = 2;

        Response response = request.delete("/products/1");
        assertEquals(200, response.statusCode());
        Response response2 = request.get("/products/1");
        assertEquals(404, response2.statusCode());

    }

    @Test
    @DisplayName("DELETE /products/pid")
    public void testDeleteProduct404() {
        Response response = RestAssured.get("/products/9");
        assertEquals(404, response.statusCode());
    }


}
