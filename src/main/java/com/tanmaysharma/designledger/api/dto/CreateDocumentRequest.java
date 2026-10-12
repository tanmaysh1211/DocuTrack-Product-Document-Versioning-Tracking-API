package com.tanmaysharma.designledger.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDocumentRequest(
        @NotBlank @Size(max = 80) String productCode,
        @NotBlank @Size(max = 180) String name,
        @Size(max = 2000) String description) {
}
