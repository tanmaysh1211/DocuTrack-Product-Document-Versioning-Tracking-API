package com.tanmaysharma.designledger.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tanmaysharma.designledger.domain.ProductDocument;

public interface ProductDocumentRepository extends JpaRepository<ProductDocument, UUID> {
    boolean existsByProductCodeIgnoreCase(String productCode);

    @Query("""
            select d from ProductDocument d
            where (:query is null or lower(d.productCode) like lower(concat('%', :query, '%'))
                   or lower(d.name) like lower(concat('%', :query, '%'))
                   or lower(coalesce(d.description, '')) like lower(concat('%', :query, '%')))
            """)
    Page<ProductDocument> search(@Param("query") String query, Pageable pageable);

    Optional<ProductDocument> findByProductCodeIgnoreCase(String productCode);
}
