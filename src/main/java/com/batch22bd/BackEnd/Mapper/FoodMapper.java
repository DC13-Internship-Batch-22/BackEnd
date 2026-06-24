package com.batch22bd.BackEnd.Mapper;

import com.batch22bd.BackEnd.DTO.request.CreateFoodRequest;
import com.batch22bd.BackEnd.DTO.request.UpdateFoodRequest;
import com.batch22bd.BackEnd.DTO.response.FoodResponse;
import com.batch22bd.BackEnd.Entity.Category;
import com.batch22bd.BackEnd.Entity.Food;
import com.batch22bd.BackEnd.Enum.FoodStatus;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public Food toEntity(CreateFoodRequest request, Category category, String imageUrl) {
        Food food = new Food();
        food.setName(request.name());
        food.setPrice(request.price());
        food.setCategory(category);
        food.setImageUrl(imageUrl);
        food.setStatus(request.status() == null ? FoodStatus.AVAILABLE : request.status());
        return food;
    }

    public FoodResponse toResponse(Food food) {
        Category category = food.getCategory();
        return new FoodResponse(
                food.getId(),
                food.getName(),
                food.getPrice(),
                food.getImageUrl(),
                food.getStatus(),
                category.getId(),
                category.getName(),
                food.getCreatedAt(),
                food.getUpdatedAt()
        );
    }

    public void update(Food food, UpdateFoodRequest request, Category category, String imageUrl) {
        if (request.name() != null) {
            food.setName(request.name());
        }
        if (request.price() != null) {
            food.setPrice(request.price());
        }
        if (category != null) {
            food.setCategory(category);
        }
        if (imageUrl != null) {
            food.setImageUrl(imageUrl);
        }
        if (request.status() != null) {
            food.setStatus(request.status());
        }
    }
}
