package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.request.CreateFoodRequest;
import com.batch22bd.BackEnd.DTO.request.UpdateFoodRequest;
import com.batch22bd.BackEnd.DTO.response.FoodResponse;
import com.batch22bd.BackEnd.Entity.Category;
import com.batch22bd.BackEnd.Entity.Food;
import com.batch22bd.BackEnd.Exception.ConflictException;
import com.batch22bd.BackEnd.Exception.ResourceNotFoundException;
import com.batch22bd.BackEnd.Mapper.FoodMapper;
import com.batch22bd.BackEnd.Repository.CategoryRepository;
import com.batch22bd.BackEnd.Repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final FoodMapper foodMapper;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public FoodResponse createFood(CreateFoodRequest request, MultipartFile image) {
        if (foodRepository.existsByNameAndIsDeletedFalse(request.name())) {
            throw new ConflictException("Food", "name");
        }

        Category category = getActiveCategory(request.categoryId());
        String imageUrl = resolveImageUrl(request.imageUrl(), image);
        Food food = foodMapper.toEntity(request, category, imageUrl);
        Food savedFood = foodRepository.save(food);

        return foodMapper.toResponse(savedFood);
    }

    @Transactional(readOnly = true)
    public FoodResponse getActiveFoodById(Long id) {
        return foodMapper.toResponse(getActiveFood(id));
    }

    @Transactional(readOnly = true)
    public List<FoodResponse> getAllFoods(Long categoryId) {
        if (categoryId != null) {
            getActiveCategory(categoryId);
        }

        List<Food> foods = categoryId == null
                ? foodRepository.findAllByIsDeletedFalse()
                : foodRepository.findAllByCategoryIdAndIsDeletedFalse(categoryId);

        return foods.stream().map(foodMapper::toResponse).toList();
    }

    @Transactional
    public FoodResponse updateFoodById(Long id, UpdateFoodRequest request, MultipartFile image) {
        Food food = getActiveFood(id);

        if (request.name() != null
                && foodRepository.existsByNameAndIsDeletedFalseAndIdNot(request.name(), id)) {
            throw new ConflictException("Food", "name");
        }

        Category category = request.categoryId() == null ? null : getActiveCategory(request.categoryId());
        String imageUrl = resolveImageUrl(request.imageUrl(), image);
        foodMapper.update(food, request, category, imageUrl);

        return foodMapper.toResponse(food);
    }

    @Transactional
    public void deleteFoodById(Long id) {
        Food food = getActiveFood(id);
        food.setDeleted(true);
    }

    private Food getActiveFood(Long id) {
        return foodRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", String.valueOf(id)));
    }

    private Category getActiveCategory(Long id) {
        return categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", String.valueOf(id)));
    }

    private String resolveImageUrl(String imageUrl, MultipartFile image) {
        String uploadedImageUrl = cloudinaryService.uploadImage(image);
        return uploadedImageUrl == null ? imageUrl : uploadedImageUrl;
    }
}
