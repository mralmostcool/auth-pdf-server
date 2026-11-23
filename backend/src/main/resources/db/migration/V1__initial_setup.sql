-- Initial baseline schema migration
CREATE TABLE IF NOT EXISTS app_info (
    id SERIAL PRIMARY KEY,
    app_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO app_info (app_name) VALUES ('pdfserver')
ON CONFLICT DO NOTHING;

-- Spring Modulith 2.2.x Event Publication Registry table for PostgreSQL
CREATE TABLE IF NOT EXISTS event_publication (
    id UUID NOT NULL,
    listener_id VARCHAR(512) NOT NULL,
    event_type VARCHAR(512) NOT NULL,
    serialized_event TEXT NOT NULL,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20),
    completion_attempts INT DEFAULT 0,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS event_publication_by_completion_date_idx
    ON event_publication (completion_date);

CREATE INDEX IF NOT EXISTS event_publication_by_listener_idx
    ON event_publication (listener_id);
