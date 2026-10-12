package com.tanmaysharma.designledger.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tanmaysharma.designledger.api.dto.CreateDocumentRequest;
import com.tanmaysharma.designledger.api.dto.CreateVersionRequest;
import com.tanmaysharma.designledger.api.dto.DocumentResponse;
import com.tanmaysharma.designledger.api.dto.PagedResponse;
import com.tanmaysharma.designledger.api.dto.UpdateDocumentRequest;
import com.tanmaysharma.designledger.api.dto.VersionResponse;
import com.tanmaysharma.designledger.service.DocumentService;

@Validated
@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) { this.documentService = documentService; }

    @PostMapping
    ResponseEntity<DocumentResponse> create(@Valid @RequestBody CreateDocumentRequest request) {
        DocumentResponse created = documentService.create(request);
        return ResponseEntity.created(URI.create("/api/v1/documents/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    DocumentResponse get(@PathVariable UUID id) { return documentService.get(id); }

    @GetMapping
    PagedResponse<DocumentResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return documentService.search(query, page, size);
    }

    @PutMapping("/{id}")
    DocumentResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateDocumentRequest request) {
        return documentService.update(id, request);
    }

    @PostMapping("/{id}/versions")
    ResponseEntity<VersionResponse> addVersion(@PathVariable UUID id,
                                                @Valid @RequestBody CreateVersionRequest request) {
        VersionResponse created = documentService.addVersion(id, request);
        return ResponseEntity.created(URI.create("/api/v1/documents/" + id + "/versions/"
                + created.versionNumber())).body(created);
    }

    @GetMapping("/{id}/versions")
    List<VersionResponse> versions(@PathVariable UUID id) { return documentService.versions(id); }
}
