package com.batch22bd.BackEnd.Controller;

import com.batch22bd.BackEnd.DTO.request.OrderRequest;
import com.batch22bd.BackEnd.DTO.response.OrderDetailResponse;
import com.batch22bd.BackEnd.DTO.response.OrderSummaryResponse;
import com.batch22bd.BackEnd.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<OrderSummaryResponse>> getOrders() {
        return ResponseEntity.ok(
                orderService.getOrders()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(
                orderService.getOrder(id)
        );
    }

    @PostMapping("/")
    public ResponseEntity<Long> addOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(
                orderService.addOrder(request)
        );
    }
}
