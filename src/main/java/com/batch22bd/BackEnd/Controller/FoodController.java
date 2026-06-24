package com.batch22bd.BackEnd.Controller;

import com.batch22bd.BackEnd.DTO.request.CreateFoodRequest;
import com.batch22bd.BackEnd.DTO.request.UpdateFoodRequest;
import com.batch22bd.BackEnd.DTO.response.FoodResponse;
import com.batch22bd.BackEnd.Enum.FoodStatus;
import com.batch22bd.BackEnd.Service.FoodService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/foods")
@RequiredArgsConstructor
@Validated
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodResponse> createFood(@RequestBody @Valid CreateFoodRequest request) {
        FoodResponse response = foodService.createFood(request, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(
            value = "/with-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )    public ResponseEntity<FoodResponse> createFoodWithImage(
            @RequestParam("name")
            @NotBlank(message = "Food name is required")
            @Size(min = 2, max = 150, message = "Food name must be between {min} and {max} characters")
            String name,

            @RequestParam("price")
            @NotNull(message = "Food price is required")
            @DecimalMin(value = "0.0", inclusive = false, message = "Food price must be greater than 0")
            BigDecimal price,

            @RequestParam("categoryId")
            @NotNull(message = "Category id is required")
            Long categoryId,

            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            @RequestParam(value = "status", required = false) FoodStatus status
    ) {
        CreateFoodRequest request = new CreateFoodRequest(name, price, categoryId, imageUrl, status);
        FoodResponse response = foodService.createFood(request, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getActiveFoodById(id));
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoods(
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(foodService.getAllFoods(categoryId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FoodResponse> updateFoodById(
            @PathVariable Long id,
            @RequestBody @Valid UpdateFoodRequest request
    ) {
        return ResponseEntity.ok(foodService.updateFoodById(id, request, null));
    }

    @PatchMapping(
            value = "/{id}/with-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )    public ResponseEntity<FoodResponse> updateFoodByIdWithImage(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false)
            @Size(min = 2, max = 150, message = "Food name must be between {min} and {max} characters")
            String name,

            @RequestParam(value = "price", required = false)
            @DecimalMin(value = "0.0", inclusive = false, message = "Food price must be greater than 0")
            BigDecimal price,

            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            @RequestParam(value = "status", required = false) FoodStatus status
    ) {
        UpdateFoodRequest request = new UpdateFoodRequest(name, price, categoryId, imageUrl, status);
        return ResponseEntity.ok(foodService.updateFoodById(id, request, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodById(@PathVariable Long id) {
        foodService.deleteFoodById(id);
        return ResponseEntity.noContent().build();
    }
}
