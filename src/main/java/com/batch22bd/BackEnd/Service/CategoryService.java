package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.request.CreateCategoryRequest;
import com.batch22bd.BackEnd.DTO.request.UpdateCategoryRequest;
import com.batch22bd.BackEnd.DTO.response.CategoryResponse;
import com.batch22bd.BackEnd.Entity.Category;
import com.batch22bd.BackEnd.Exception.ConflictException;
import com.batch22bd.BackEnd.Exception.ResourceNotFoundException;
import com.batch22bd.BackEnd.Mapper.CategoryMapper;
import com.batch22bd.BackEnd.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByNameAndIsDeletedFalse(request.name())) {
            throw new ConflictException("Category", "name");
        }
        Category category = categoryMapper.toEntity(request);
        Category categorySaved = categoryRepository.save(category);

        return categoryMapper.toResponse(categorySaved);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getActiveCategoryById(Long id) {
        Category category = getActiveCategory(id);
        return categoryMapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> list = categoryRepository.findAllByIsDeletedFalse();
        return list.stream().map(categoryMapper::toResponse).toList();
    }

    @Transactional
    public CategoryResponse updateCategoryById(Long id, UpdateCategoryRequest request) {
        Category category = getActiveCategory(id);

        if (request.name() != null
                && categoryRepository.existsByNameAndIsDeletedFalseAndIdNot(request.name(), id))
        {
            throw new ConflictException("Category", "name");
        }

        categoryMapper.update(category, request);

        return categoryMapper.toResponse(category);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = getActiveCategory(id);
        category.setDeleted(true);
    }

    // Helpers method
    private Category getActiveCategory(Long id) {
        return categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", "id", String.valueOf(id))
                );
    }
}
