package org.example.model;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class NoteTest {

    @Test
    public void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        JsonObject metadata = new JsonObject()
                .put("tags", Arrays.asList("model", "test"))
                .put("color", "green");
        Note note = new Note();

        note.setId(id);
        note.setTitle("Model Test");
        note.setMetadata(metadata.getMap());

        assertEquals(id, note.getId());
        assertEquals("Model Test", note.getTitle());
        assertEquals("green", note.getMetadata().get("color"));
    }
}
