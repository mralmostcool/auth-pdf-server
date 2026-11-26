-- V2: Create pdf_documents table
CREATE TABLE IF NOT EXISTS pdf_documents (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    storage_key VARCHAR(512) NOT NULL UNIQUE,
    bucket_name VARCHAR(128) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(64) NOT NULL,
    page_format VARCHAR(32) NOT NULL DEFAULT 'A4',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_pdf_documents_created_at ON pdf_documents (created_at);
