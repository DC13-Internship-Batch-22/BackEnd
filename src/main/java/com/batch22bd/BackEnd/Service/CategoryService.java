package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.request.CreateCategoryRequest;
import com.batch22bd.BackEnd.DTO.response.CategoryResponse;
import com.batch22bd.BackEnd.Entity.Category;
import com.batch22bd.BackEnd.Exception.ConflictException;
import com.batch22bd.BackEnd.Exception.ResourceNotFoundException;
import com.batch22bd.BackEnd.Mapper.CategoryMapper;
import com.batch22bd.BackEnd.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByNameAndIsDeletedFalse(request.name())) {
            throw new ConflictException("Category", "name");
        }
        Category category = categoryMapper.toEntity(request);
        Category categorySaved = categoryRepository.save(category);

        return categoryMapper.toResponse(categorySaved);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));
        return categoryMapper.toResponse(category);
    }
}
