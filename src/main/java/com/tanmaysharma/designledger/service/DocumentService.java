package com.tanmaysharma.designledger.service;

import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tanmaysharma.designledger.api.dto.CreateDocumentRequest;
import com.tanmaysharma.designledger.api.dto.CreateVersionRequest;
import com.tanmaysharma.designledger.api.dto.DocumentResponse;
import com.tanmaysharma.designledger.api.dto.PagedResponse;
import com.tanmaysharma.designledger.api.dto.UpdateDocumentRequest;
import com.tanmaysharma.designledger.api.dto.VersionResponse;
import com.tanmaysharma.designledger.domain.DocumentVersion;
import com.tanmaysharma.designledger.domain.ProductDocument;
import com.tanmaysharma.designledger.exception.DuplicateResourceException;
import com.tanmaysharma.designledger.exception.ResourceNotFoundException;
import com.tanmaysharma.designledger.exception.RevisionConflictException;
import com.tanmaysharma.designledger.repository.DocumentVersionRepository;
import com.tanmaysharma.designledger.repository.ProductDocumentRepository;

@Service
@Transactional(readOnly = true)
public class DocumentService {
    private final ProductDocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;

    public DocumentService(ProductDocumentRepository documentRepository,
                           DocumentVersionRepository versionRepository) {
        this.documentRepository = documentRepository;
        this.versionRepository = versionRepository;
    }

    @Transactional
    public DocumentResponse create(CreateDocumentRequest request) {
        String code = request.productCode().trim().toUpperCase();
        if (documentRepository.existsByProductCodeIgnoreCase(code)) {
            throw new DuplicateResourceException("Product code already exists: " + code);
        }
        ProductDocument document = new ProductDocument(code, request.name().trim(), request.description());
        return DocumentMapper.toResponse(documentRepository.save(document));
    }

    @Cacheable(cacheNames = "documents", key = "#id")
    public DocumentResponse get(UUID id) {
        return DocumentMapper.toResponse(findDocument(id));
    }

    public PagedResponse<DocumentResponse> search(String query, int page, int size) {
        String normalized = query == null || query.isBlank() ? null : query.trim();
        var pageable = PageRequest.of(page, Math.min(size, 100), Sort.by("updatedAt").descending());
        return PagedResponse.from(documentRepository.search(normalized, pageable).map(DocumentMapper::toResponse));
    }

    @Transactional
    @CacheEvict(cacheNames = "documents", key = "#id")
    public DocumentResponse update(UUID id, UpdateDocumentRequest request) {
        ProductDocument document = findDocument(id);
        if (document.getRevision() != request.expectedRevision()) {
            throw new RevisionConflictException("Document was modified. Expected revision "
                    + request.expectedRevision() + " but current revision is " + document.getRevision());
        }
        document.update(request.name().trim(), request.description(), request.status());
        return DocumentMapper.toResponse(documentRepository.saveAndFlush(document));
    }

    @Transactional
    @CacheEvict(cacheNames = "documents", key = "#documentId")
    public VersionResponse addVersion(UUID documentId, CreateVersionRequest request) {
        ProductDocument document = findDocument(documentId);
        int nextVersion = Math.toIntExact(versionRepository.countByDocumentId(documentId) + 1);
        DocumentVersion version = new DocumentVersion(document, nextVersion, request.storageKey().trim(),
                request.checksum().toLowerCase(), request.contentType().trim(), request.sizeBytes(),
                request.createdBy().trim());
        return DocumentMapper.toResponse(versionRepository.save(version));
    }

    public List<VersionResponse> versions(UUID documentId) {
        findDocument(documentId);
        return versionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId)
                .stream().map(DocumentMapper::toResponse).toList();
    }

    private ProductDocument findDocument(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + id));
    }
}
