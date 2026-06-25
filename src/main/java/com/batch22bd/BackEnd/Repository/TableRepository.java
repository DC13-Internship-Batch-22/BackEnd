package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    List<TableEntity> findAllByIsDeletedFalseOrderByIdAsc();

    Optional<TableEntity> findByIdAndIsDeletedFalse(Long id);

    boolean existsByTableNumberAndIsDeletedFalse(String tableNumber);

    boolean existsByTableNumberAndIdNotAndIsDeletedFalse(String tableNumber, Long id);
}
