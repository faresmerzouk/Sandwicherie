package fr.istic.caos.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Classe abstraite définissant la structure de base d'une classe de test
 * @author Fabien
 */
public abstract class AbstractTest {

    /**
     * L'URI de base du Webservice
     */
    private static final String BASE_URI = "http://localhost:8080/caos-ws/api";

    /**
     * Classe de journalisation
     */
    protected final Logger logger = getLogger("Client de test sandwich [" + getClass().getSimpleName() + "]");

    @BeforeAll
    static void initializeClient() {
        // Spécification des requêtes
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();

        // Analyseur par défaut
        defaultParser = Parser.JSON;
    }

    @BeforeEach
    void setUp() {
        logger.info("Réinitialisation du Web Service");
        RestAssured.delete("/");
    }

    public static RequestSpecification createRequest() {
        return given(RestAssured.requestSpecification);
    }

}
