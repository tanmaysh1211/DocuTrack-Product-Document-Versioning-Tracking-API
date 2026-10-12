package com.tanmaysharma.designledger.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tanmaysharma.designledger.domain.DocumentVersion;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, UUID> {
    List<DocumentVersion> findByDocumentIdOrderByVersionNumberDesc(UUID documentId);
    long countByDocumentId(UUID documentId);
}
