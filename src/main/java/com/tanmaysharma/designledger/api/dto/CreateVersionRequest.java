package com.tanmaysharma.designledger.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateVersionRequest(
        @NotBlank @Size(max = 512) String storageKey,
        @NotBlank @Pattern(regexp = "^[a-fA-F0-9]{64}$", message = "must be a SHA-256 hex digest") String checksum,
        @NotBlank @Size(max = 120) String contentType,
        @Positive long sizeBytes,
        @NotBlank @Size(max = 120) String createdBy) {
}
