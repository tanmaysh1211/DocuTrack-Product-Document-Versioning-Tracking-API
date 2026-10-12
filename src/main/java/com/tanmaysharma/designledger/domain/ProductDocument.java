package com.tanmaysharma.designledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_documents")
public class ProductDocument {
    @Id
    private UUID id;

    @Column(name = "product_code", nullable = false, unique = true, length = 80)
    private String productCode;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DocumentStatus status;

    @Version
    @Column(nullable = false)
    private long revision;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ProductDocument() {}

    public ProductDocument(String productCode, String name, String description) {
        this.id = UUID.randomUUID();
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.status = DocumentStatus.DRAFT;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() { updatedAt = Instant.now(); }

    public void update(String name, String description, DocumentStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public UUID getId() { return id; }
    public String getProductCode() { return productCode; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public DocumentStatus getStatus() { return status; }
    public long getRevision() { return revision; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
