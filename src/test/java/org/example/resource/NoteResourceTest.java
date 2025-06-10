package org.example.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import io.restassured.internal.http.HttpResponseException;
import io.vertx.core.json.JsonObject;

import java.util.UUID;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.inject.Inject;
import io.vertx.mutiny.pgclient.PgPool;

@QuarkusTest
public class NoteResourceTest {

    @Inject
    PgPool client;

    @BeforeEach
    public void cleanTable() {
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

        // Create note
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

        // Get note
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
    public void testDeleteNote() {
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

        // Create note
        given()
                .contentType("application/json")
                .body(jsonBody)
                .when()
                .post("/notes")
                .then()
                .statusCode(201);

        // Delete note
        given()
                .when()
                .delete("/notes/" + id)
                .then()
                .statusCode(204);

        // Get deleted note - expect 404
        assertThrows(HttpResponseException.class, () -> {
            given()
                .when()
                .get("/notes/" + id);
        });
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
