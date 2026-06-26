package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.OrderDto;
import com.batch22bd.BackEnd.DTO.request.AssignOrderRequest;
import com.batch22bd.BackEnd.DTO.response.OrderSummaryResponse;
import com.batch22bd.BackEnd.DTO.response.OrderDetailResponse;
import com.batch22bd.BackEnd.DTO.response.PageResponse;
import com.batch22bd.BackEnd.Entity.Food;
import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Entity.OrderItem;
import com.batch22bd.BackEnd.Entity.TableEntity;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import com.batch22bd.BackEnd.Enum.TableStatus;
import com.batch22bd.BackEnd.Exception.OrderException.DeleteNoItemException;
import com.batch22bd.BackEnd.Exception.ResourceNotFoundException;
import com.batch22bd.BackEnd.Mapper.OrderMapper;
import com.batch22bd.BackEnd.Mapper.PageMapper;
import com.batch22bd.BackEnd.Repository.FoodRepository;
import com.batch22bd.BackEnd.Repository.OrderRepository;
import com.batch22bd.BackEnd.Repository.TableRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final  OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final FoodRepository foodRepository;

    private final OrderMapper orderMapper;
    private final PageMapper pageMapper;

    private BigDecimal calculateTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    BigDecimal subtotal = item.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    return subtotal;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Long createOrder (AssignOrderRequest request) {
        TableEntity tableEntity = tableRepository.findById(request.getTable_id())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Table",
                        "TableId",
                        String.valueOf(request.getTable_id()))
                );


        List<OrderItem> orderItems = request.getItems()
                .stream()
                .map(dto -> {
                    Food food = foodRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Food",
                                    "foodId",
                                    String.valueOf(dto.getProductId())
                            ));

                    return orderMapper.toOrderItem(food, dto.getQuantity());
                })
                .toList();

        tableEntity.setStatus(TableStatus.OCCUPIED);

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


    public PageResponse<OrderSummaryResponse> getOrders(int page, int size, OrderStatus status, String orderId) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pageOrder = orderRepository.search(
                orderId,
                status == null ? null : status.name(),
                pageable
        );

        Page<OrderSummaryResponse> pageOrderSummary = pageOrder.map(orderMapper::toSummaryResponse);
        return pageMapper.toPageResponse(pageOrderSummary);
    }

    public OrderDetailResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Order",
                        "orderId",
                        String.valueOf(id)));

        return orderMapper.toDetailResponse(order);
    }


    @Transactional
    public Long updateOrder (Long orderId, List<OrderDto> orderdto) {

        Order order = orderRepository
                .findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Order",
                        "OrderId",
                        String.valueOf(orderId)));

        List<Long> productIds = orderdto.stream()
                .map(OrderDto::getProductId)
                .toList();

        List<Food> foods = foodRepository.findAllById(productIds);

        Map<Long, Food> foodMap = foods.stream()
                .collect(Collectors.toMap(
                        Food::getId,
                        Function.identity()
                ));

        for (OrderDto dto : orderdto) {

            Food food = foodMap.get(dto.getProductId());

            if (food == null) {
                throw new ResourceNotFoundException(
                        "Food",
                        "FoodId",
                        String.valueOf(dto.getProductId())
                );
            }

            OrderItem existingItem = order.getItems()
                    .stream()
                    .filter(item -> item.getProductId().equals(dto.getProductId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {

                existingItem.setQuantity(
                        existingItem.getQuantity() + dto.getQuantity()
                );

            } else {

                order.getItems().add(
                        new OrderItem(
                                food.getId(),
                                food.getName(),
                                dto.getQuantity(),
                                food.getPrice()
                        )
                );
            }
        }
        order.setTotalAmount(calculateTotal(order.getItems()));
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return order.getId();
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public void removeItem (Long orderId, OrderDto orderdto) {
        Food food = foodRepository.findById(orderdto.getProductId())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Foood",
                        "foodId",
                        String.valueOf(orderdto.getProductId())));

        Order order = orderRepository
                .findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Order",
                        "OrderId",
                        String.valueOf(orderId)));

        OrderItem existingItem = order.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(orderdto.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem!=null) {
            existingItem.setQuantity(
                    existingItem.getQuantity() - orderdto.getQuantity()
            );
            order.setTotalAmount(calculateTotal(order.getItems()));
            order.setUpdatedAt(LocalDateTime.now());
        } else {
            throw new DeleteNoItemException("This Item is not available");
        }

        orderRepository.save(order);
    }

    public void updateStatus (Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Order",
                        "OrderId",
                        String.valueOf(id))
                );
        order.setStatus(status);
    }
}
