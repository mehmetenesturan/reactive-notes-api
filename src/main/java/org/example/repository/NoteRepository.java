package org.example.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.example.model.Note;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NoteRepository {

    private final PgPool client;

    public NoteRepository(PgPool client) {
        this.client = client;
    }

    public Uni<Void> create(Note note) {
        return client.preparedQuery("INSERT INTO notes (id, title, metadata) VALUES ($1, $2, $3)")
                .execute(Tuple.of(note.getId(), note.getTitle(), new JsonObject(note.getMetadata())))
                .replaceWithVoid();
    }

    public Uni<Note> findById(UUID id) {
        return client.preparedQuery("SELECT id, title, metadata FROM notes WHERE id = $1")
                .execute(Tuple.of(id))
                .onItem().transform(rows -> {
                    Row row = rows.iterator().hasNext() ? rows.iterator().next() : null;
                    return row == null ? null : fromRow(row);
                });
    }

    public Uni<List<Note>> findAll() {
        return client.preparedQuery("SELECT id, title, metadata FROM notes")
                .execute()
                .onItem().transform(rows -> {
                    List<Note> notes = new ArrayList<>();
                    for (Row row : rows) {
                        notes.add(fromRow(row));
                    }
                    return notes;
                });
    }

    public Uni<Void> deleteById(UUID id) {
        return client.preparedQuery("DELETE FROM notes WHERE id = $1")
                .execute(Tuple.of(id))
                .replaceWithVoid();
    }

    private Note fromRow(Row row) {
        JsonObject json = row.getJsonObject("metadata");
        return new Note(
                row.getUUID("id"),
                row.getString("title"),
                json == null ? null : json.getMap()
        );
    }
}
