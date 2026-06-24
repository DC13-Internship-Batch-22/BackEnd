package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameAndIsDeletedFalse(String name);

    Optional<Category> findByIdAndIsDeletedFalse(Long id);
}
