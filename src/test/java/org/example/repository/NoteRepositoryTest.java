package org.example.repository;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.example.model.Note;
import org.example.repository.NoteRepository;
import io.vertx.core.json.JsonObject;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class NoteRepositoryTest {

    @Inject
    NoteRepository repository;

    @Test
    public void testCreateAndFind() {
        UUID id = UUID.randomUUID();
        JsonObject metadata = new JsonObject()
                .put("tags", Arrays.asList("repo", "test"))
                .put("color", "blue");
        Note note = new Note(id, "Repo Test", metadata.getMap());

        Uni<Void> create = repository.create(note);
        create.await().indefinitely();

        Note found = repository.findById(id).await().indefinitely();
        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Repo Test", found.getTitle());
        assertEquals("blue", found.getMetadata().get("color"));
    }
}
