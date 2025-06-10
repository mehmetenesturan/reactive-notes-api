package org.example.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.model.Note;
import org.example.repository.NoteRepository;

import java.util.List;
import java.util.UUID;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteResource {

    @Inject
    NoteRepository repository;

    @POST
    public Uni<Response> create(Note note) {
        if (note.getId() == null) {
            note.setId(UUID.randomUUID());
        }
        return repository.create(note)
                .onItem().transform(v -> Response.status(Response.Status.CREATED).entity(note).build());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") UUID id) {
        return repository.findById(id)
                .onItem().transform(note -> {
                    if (note == null) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                    return Response.ok(note).build();
                });
    }

    @GET
    public Uni<List<Note>> getAll() {
        return repository.findAll();
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") UUID id) {
        return repository.deleteById(id)
                .onItem().transform(v -> Response.noContent().build());
    }
}
