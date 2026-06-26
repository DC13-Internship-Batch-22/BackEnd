package com.batch22bd.BackEnd.DTO.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> items,
        int totalCount,
        int totalPage,
        int page,
        int pageSize
) {
}
