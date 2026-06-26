package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByIsDeletedFalse(Pageable pageable);
    Optional<Order> findByIdAndIsDeletedFalse(Long id);
    Optional<Order> findByTableIdAndStatus(
            Long tableId,
            OrderStatus status
    );

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.isDeleted = false
        AND (:orderId IS NULL OR CAST(o.id AS string) LIKE %:orderId%)
        AND (:table IS NULL OR LOWER(o.table.tableNumber) LIKE LOWER(CONCAT('%', :table, '%')))
    """)
    Page<Order> search(
            @Param("orderId") String orderId,
            @Param("table") String table,
            Pageable pageable
    );
}
