package org.example.model;

import java.util.Map;
import java.util.UUID;

public class Note {
    private UUID id;
    private String title;
    private Map<String, Object> metadata;

    public Note() {}

    public Note(UUID id, String title, Map<String, Object> metadata) {
        this.id = id;
        this.title = title;
        this.metadata = metadata;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
