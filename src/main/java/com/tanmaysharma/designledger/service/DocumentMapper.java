package com.tanmaysharma.designledger.service;

import com.tanmaysharma.designledger.api.dto.DocumentResponse;
import com.tanmaysharma.designledger.api.dto.VersionResponse;
import com.tanmaysharma.designledger.domain.DocumentVersion;
import com.tanmaysharma.designledger.domain.ProductDocument;

final class DocumentMapper {
    private DocumentMapper() {}

    static DocumentResponse toResponse(ProductDocument document) {
        return new DocumentResponse(document.getId(), document.getProductCode(), document.getName(),
                document.getDescription(), document.getStatus(), document.getRevision(),
                document.getCreatedAt(), document.getUpdatedAt());
    }

    static VersionResponse toResponse(DocumentVersion version) {
        return new VersionResponse(version.getId(), version.getVersionNumber(), version.getStorageKey(),
                version.getChecksum(), version.getContentType(), version.getSizeBytes(),
                version.getCreatedBy(), version.getCreatedAt());
    }
}
