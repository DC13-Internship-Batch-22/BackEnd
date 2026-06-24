package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    boolean existsByNameAndIsDeletedFalse(String name);

    boolean existsByNameAndIsDeletedFalseAndIdNot(String name, Long id);

    Optional<Food> findByIdAndIsDeletedFalse(Long id);

    List<Food> findAllByIsDeletedFalse();

    List<Food> findAllByCategoryIdAndIsDeletedFalse(Long categoryId);
}