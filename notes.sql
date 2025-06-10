CREATE DATABASE notesdb;

\c notesdb;

CREATE TABLE IF NOT EXISTS notes (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    metadata JSONB
);
