package com.tanmaysharma.designledger.api.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.tanmaysharma.designledger.domain.DocumentStatus;

public record DocumentResponse(
        UUID id,
        String productCode,
        String name,
        String description,
        DocumentStatus status,
        long revision,
        Instant createdAt,
        Instant updatedAt) implements Serializable {
}
