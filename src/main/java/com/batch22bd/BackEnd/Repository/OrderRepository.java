package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIsDeletedFalse();
}
