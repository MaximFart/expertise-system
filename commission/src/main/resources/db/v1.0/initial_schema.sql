-- db/changelog/v1.0/initial_schema.sql
CREATE TABLE IF NOT EXISTS applications (
    id UUID PRIMARY KEY,
    number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS outbox_events (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    published BOOLEAN DEFAULT FALSE
    );

CREATE INDEX idx_outbox_unpublished ON outbox_events (created_at) WHERE published = FALSE