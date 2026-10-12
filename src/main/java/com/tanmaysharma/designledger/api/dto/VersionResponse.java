package com.tanmaysharma.designledger.api.dto;

import java.time.Instant;
import java.util.UUID;

public record VersionResponse(
        UUID id,
        int versionNumber,
        String storageKey,
        String checksum,
        String contentType,
        long sizeBytes,
        String createdBy,
        Instant createdAt) {
}
