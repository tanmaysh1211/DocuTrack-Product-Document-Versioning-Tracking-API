package com.tanmaysharma.designledger.api.dto;

import com.tanmaysharma.designledger.domain.DocumentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateDocumentRequest(
        @NotBlank @Size(max = 180) String name,
        @Size(max = 2000) String description,
        @NotNull DocumentStatus status,
        @PositiveOrZero long expectedRevision) {
}
