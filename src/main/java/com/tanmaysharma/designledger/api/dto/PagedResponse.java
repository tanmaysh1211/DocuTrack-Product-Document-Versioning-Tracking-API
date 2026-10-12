package com.tanmaysharma.designledger.api.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PagedResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
    public static <T> PagedResponse<T> from(Page<T> result) {
        return new PagedResponse<>(result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }
}
