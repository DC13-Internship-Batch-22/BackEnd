package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.request.OrderRequest;
import com.batch22bd.BackEnd.DTO.response.OrderSummaryResponse;
import com.batch22bd.BackEnd.DTO.response.OrderDetailResponse;
import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Entity.OrderItem;
import com.batch22bd.BackEnd.Entity.TableEntity;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import com.batch22bd.BackEnd.Enum.TableStatus;
import com.batch22bd.BackEnd.Exception.ResourceNotFoundException;
import com.batch22bd.BackEnd.Mapper.OrderMapper;
import com.batch22bd.BackEnd.Repository.OrderRepository;
import com.batch22bd.BackEnd.Repository.TableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final  OrderRepository orderRepository;
    private final TableRepository tableRepository;

    private final OrderMapper orderMapper;

    private BigDecimal calculateTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Long addOrder (OrderRequest request) {
        TableEntity tableEntity = tableRepository.findById(request.getTable_id())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Table",
                        "TableId",
                        String.valueOf(request.getTable_id()))
                );

        List<OrderItem> orderItems = request.getItems();

        tableEntity.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(tableEntity);

        Order order = Order.builder()
                                .table(tableEntity)
                                .items(orderItems)
                                .totalAmount(calculateTotal(orderItems))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .status(OrderStatus.PENDING)
                                .isDeleted(false)
                                .build();
        orderRepository.save(order);
        return order.getId();
    }

    public List<OrderSummaryResponse> getOrders() {
        return orderRepository.findByIsDeletedFalse()
                .stream()
                .map(orderMapper::toSummaryResponse)
                .toList();
    }

    public OrderDetailResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Order",
                        "orderId",
                        String.valueOf(id)));

        return orderMapper.toDetailResponse(order);
    }
}
