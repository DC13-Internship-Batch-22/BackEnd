package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import com.batch22bd.BackEnd.Enum.TableStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByIsDeletedFalse(Pageable pageable);
    Optional<Order> findByIdAndIsDeletedFalse(Long id);
    Optional<Order> findByTableIdAndStatus(
            Long tableId,
            OrderStatus status
    );

    List<Order> findByTable_IdInAndStatusInAndIsDeletedFalseOrderByCreatedAtDesc(
            Collection<Long> tableIds,
            Collection<OrderStatus> statuses
    );
    Optional<Order> findFirstByTable_IdAndStatusInAndIsDeletedFalseOrderByCreatedAtDesc(
            Long tableId,
            Collection<OrderStatus> statuses
    );

    @Query("""
        SELECT o
        FROM Order o
        WHERE o.isDeleted = false
            AND (
                :orderId IS NULL
                OR :orderId = ''
                OR CAST(o.id AS string) LIKE CONCAT('%', :orderId, '%')
            )
            AND (
                :status IS NULL
                OR :status = ''
                OR LOWER(CAST(o.status AS string)) LIKE LOWER(CONCAT('%', :status, '%'))
            )
    """)
    Page<Order> search(
            @Param("orderId") String orderId,
            @Param("status") String status,
            Pageable pageable
    );
}
