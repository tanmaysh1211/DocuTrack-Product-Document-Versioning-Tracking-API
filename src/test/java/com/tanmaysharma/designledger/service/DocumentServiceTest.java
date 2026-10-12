package com.tanmaysharma.designledger.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tanmaysharma.designledger.api.dto.CreateDocumentRequest;
import com.tanmaysharma.designledger.api.dto.UpdateDocumentRequest;
import com.tanmaysharma.designledger.domain.DocumentStatus;
import com.tanmaysharma.designledger.domain.ProductDocument;
import com.tanmaysharma.designledger.exception.DuplicateResourceException;
import com.tanmaysharma.designledger.exception.RevisionConflictException;
import com.tanmaysharma.designledger.repository.DocumentVersionRepository;
import com.tanmaysharma.designledger.repository.ProductDocumentRepository;
import com.tanmaysharma.designledger.service.DocumentService;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @Mock ProductDocumentRepository documentRepository;
    @Mock DocumentVersionRepository versionRepository;
    private DocumentService service;

    @BeforeEach
    void setUp() { service = new DocumentService(documentRepository, versionRepository); }

    @Test
    void createsDocumentWithNormalizedProductCode() {
        when(documentRepository.existsByProductCodeIgnoreCase("ROUTER-42")).thenReturn(false);
        when(documentRepository.save(any(ProductDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(new CreateDocumentRequest(" router-42 ", "Core Router", "Edge product"));

        assertThat(response.productCode()).isEqualTo("ROUTER-42");
        assertThat(response.status()).isEqualTo(DocumentStatus.DRAFT);
        verify(documentRepository).save(any(ProductDocument.class));
    }

    @Test
    void rejectsDuplicateProductCode() {
        when(documentRepository.existsByProductCodeIgnoreCase("ROUTER-42")).thenReturn(true);

        assertThatThrownBy(() -> service.create(
                new CreateDocumentRequest("router-42", "Core Router", null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void rejectsStaleRevisionBeforeUpdate() {
        UUID id = UUID.randomUUID();
        ProductDocument stored = new ProductDocument("SW-1", "Switch", null);
        when(documentRepository.findById(id)).thenReturn(Optional.of(stored));

        assertThatThrownBy(() -> service.update(id,
                new UpdateDocumentRequest("Switch 2", null, DocumentStatus.ACTIVE, 3)))
                .isInstanceOf(RevisionConflictException.class)
                .hasMessageContaining("current revision is 0");
    }
}
