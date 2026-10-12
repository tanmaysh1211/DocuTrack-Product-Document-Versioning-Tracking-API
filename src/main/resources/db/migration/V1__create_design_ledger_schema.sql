CREATE TABLE product_documents (
    id BINARY(16) NOT NULL,
    product_code VARCHAR(80) NOT NULL,
    name VARCHAR(180) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(20) NOT NULL,
    revision BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_product_documents PRIMARY KEY (id),
    CONSTRAINT uk_product_documents_product_code UNIQUE (product_code)
);

CREATE INDEX idx_product_documents_name ON product_documents (name);
CREATE INDEX idx_product_documents_updated_at ON product_documents (updated_at);

CREATE TABLE document_versions (
    id BINARY(16) NOT NULL,
    document_id BINARY(16) NOT NULL,
    version_number INT NOT NULL,
    storage_key VARCHAR(512) NOT NULL,
    checksum VARCHAR(64) NOT NULL,
    content_type VARCHAR(120) NOT NULL,
    size_bytes BIGINT NOT NULL,
    created_by VARCHAR(120) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT pk_document_versions PRIMARY KEY (id),
    CONSTRAINT fk_document_versions_document
        FOREIGN KEY (document_id) REFERENCES product_documents (id),
    CONSTRAINT uk_document_version UNIQUE (document_id, version_number),
    CONSTRAINT chk_document_versions_size CHECK (size_bytes > 0)
);

CREATE INDEX idx_document_versions_document_created
    ON document_versions (document_id, created_at);
