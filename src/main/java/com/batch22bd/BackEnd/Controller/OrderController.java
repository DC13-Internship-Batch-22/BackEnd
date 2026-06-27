package com.batch22bd.BackEnd.Controller;

import com.batch22bd.BackEnd.DTO.OrderDto;
import com.batch22bd.BackEnd.DTO.request.AssignOrderRequest;
import com.batch22bd.BackEnd.DTO.response.OrderDetailResponse;
import com.batch22bd.BackEnd.DTO.response.OrderSummaryResponse;
import com.batch22bd.BackEnd.DTO.response.PageResponse;
import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import com.batch22bd.BackEnd.Service.OrderService;
import com.batch22bd.BackEnd.Service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    @GetMapping()
    public ResponseEntity<PageResponse<OrderSummaryResponse>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) OrderStatus status
    ) {
        return ResponseEntity.ok(
                orderService.getOrders(page, size, status, orderId)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.getOrder(id)
        );
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Long> orderDish(
            @PathVariable Long orderId,
            @RequestBody List<OrderDto> order
    ) {
        return ResponseEntity.ok(
                orderService.updateOrder(orderId, order)
        );
    }

    @DeleteMapping("/{orderId}/items")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long orderId,
            @RequestBody OrderDto request
    ) {
        orderService.removeItem(orderId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<Long> addOrder(@RequestBody AssignOrderRequest request) {
        return ResponseEntity.ok(
                orderService.createOrder(request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete successfully");
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id
    ){
        orderService.updateStatus(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/override")
    public ResponseEntity<String> overrideItems (
            @PathVariable Long id,
            @RequestBody List<OrderDto> orderDtos
    ) {
        return ResponseEntity.ok(orderService.overrideItems(id, orderDtos));
    }
}
