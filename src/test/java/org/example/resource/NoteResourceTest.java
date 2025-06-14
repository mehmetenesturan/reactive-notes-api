package org.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import io.restassured.internal.http.HttpResponseException;
import io.vertx.core.json.JsonObject;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.HttpClientConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;

import java.util.UUID;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.inject.Inject;
import io.vertx.mutiny.pgclient.PgPool;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

@QuarkusTest
public class NoteResourceTest {

    @Inject
    PgPool client;

    @BeforeEach
    public void cleanTable() {
        // Her test öncesi tabloyu temizle
        client.query("TRUNCATE TABLE notes").execute().await().indefinitely();
    }

    @Test
    public void testCreateAndGetNote() {
        UUID id = UUID.randomUUID();
        JsonObject metadata = new JsonObject()
                .put("tags", Arrays.asList("test", "urgent"))
                .put("color", "red");

        String jsonBody = """
        {
            "id": "%s",
            "title": "Test Note",
            "metadata": %s
        }
        """.formatted(id, metadata.encode());

        // Not oluştur
        given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/notes")
                .then()
                .statusCode(201)
                .body("id", is(id.toString()))
                .body("title", is("Test Note"))
                .body("metadata.color", is("red"));

        // Notu getir
        given()
                .when()
                .get("/notes/" + id)
                .then()
                .statusCode(200)
                .body("id", is(id.toString()))
                .body("title", is("Test Note"))
                .body("metadata.color", is("red"));
    }

    @Test
    public void testDeleteNote() throws Exception {
        UUID id = UUID.randomUUID();
        JsonObject metadata = new JsonObject()
                .put("tags", Arrays.asList("delete"))
                .put("color", "black");

        String jsonBody = """
        {
            "id": "%s",
            "title": "Note to delete",
            "metadata": %s
        }
        """.formatted(id, metadata.encode());

        // Not oluştur
        given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/notes")
                .then()
                .statusCode(201);

        // Notu sil
        given()
                .when()
                .delete("/notes/" + id)
                .then()
                .statusCode(204);

        // Var olmayan notu silmeyi dene - 404 bekleniyor
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/notes/" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGetAllNotes() {
        // İlk notu oluştur
        UUID id1 = UUID.randomUUID();
        JsonObject metadata1 = new JsonObject()
                .put("tags", Arrays.asList("test1"))
                .put("color", "red");

        String jsonBody1 = """
        {
            "id": "%s",
            "title": "Test Note 1",
            "metadata": %s
        }
        """.formatted(id1, metadata1.encode());

        given()
                .contentType("application/json")
                .body(jsonBody1)
                .when()
                .post("/notes")
                .then()
                .statusCode(201);

        // İkinci notu oluştur
        UUID id2 = UUID.randomUUID();
        JsonObject metadata2 = new JsonObject()
                .put("tags", Arrays.asList("test2"))
                .put("color", "blue");

        String jsonBody2 = """
        {
            "id": "%s",
            "title": "Test Note 2",
            "metadata": %s
        }
        """.formatted(id2, metadata2.encode());

        given()
                .contentType("application/json")
                .body(jsonBody2)
                .when()
                .post("/notes")
                .then()
                .statusCode(201);

        // Tüm notları getir
        given()
                .when()
                .get("/notes")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(id1.toString()))
                .body("[1].id", is(id2.toString()));
    }
}
