package com.tanmaysharma.designledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "document_versions", uniqueConstraints =
        @UniqueConstraint(name = "uk_document_version", columnNames = {"document_id", "version_number"}))
public class DocumentVersion {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false, updatable = false)
    private ProductDocument document;

    @Column(name = "version_number", nullable = false, updatable = false)
    private int versionNumber;

    @Column(name = "storage_key", nullable = false, updatable = false, length = 512)
    private String storageKey;

    @Column(nullable = false, updatable = false, length = 64)
    private String checksum;

    @Column(name = "content_type", nullable = false, updatable = false, length = 120)
    private String contentType;

    @Column(name = "size_bytes", nullable = false, updatable = false)
    private long sizeBytes;

    @Column(name = "created_by", nullable = false, updatable = false, length = 120)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected DocumentVersion() {}

    public DocumentVersion(ProductDocument document, int versionNumber, String storageKey,
                           String checksum, String contentType, long sizeBytes, String createdBy) {
        this.id = UUID.randomUUID();
        this.document = document;
        this.versionNumber = versionNumber;
        this.storageKey = storageKey;
        this.checksum = checksum;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.createdBy = createdBy;
    }

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }

    public UUID getId() { return id; }
    public ProductDocument getDocument() { return document; }
    public int getVersionNumber() { return versionNumber; }
    public String getStorageKey() { return storageKey; }
    public String getChecksum() { return checksum; }
    public String getContentType() { return contentType; }
    public long getSizeBytes() { return sizeBytes; }
    public String getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
}
