package com.batch22bd.BackEnd.Mapper;

import com.batch22bd.BackEnd.DTO.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {
    public <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<T>(
                page.getContent(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}
