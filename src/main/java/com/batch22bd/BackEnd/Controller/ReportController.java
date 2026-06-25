package com.batch22bd.BackEnd.Controller;

import com.batch22bd.BackEnd.DTO.request.DateRangeRequest;
import com.batch22bd.BackEnd.DTO.request.RevenueTrendRequest;
import com.batch22bd.BackEnd.DTO.request.TopSellingDishesRequest;
import com.batch22bd.BackEnd.DTO.request.RecentLargeTransactionsRequest;
import com.batch22bd.BackEnd.DTO.response.*;
import com.batch22bd.BackEnd.Service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<ReportSummaryResponse> getSummary(
            @Valid @ModelAttribute DateRangeRequest request
    ) {
        return ResponseEntity.ok(reportService.getSummary(request));
    }

    @GetMapping("/revenue-trend")
    public ResponseEntity<List<RevenueTrendResponse>> getRevenueTrend(
            @Valid @ModelAttribute RevenueTrendRequest request
    ) {
        return ResponseEntity.ok(reportService.getRevenueTrend(request));
    }

    @GetMapping("/top-selling-dishes")
    public ResponseEntity<List<TopSellingDishResponse>> getTopSellingDishes(
            @Valid @ModelAttribute TopSellingDishesRequest request
    ) {
        return ResponseEntity.ok(reportService.getTopSellingDishes(request));
    }

    @GetMapping("/recent-large-transactions")
    public ResponseEntity<List<RecentLargeTransactionResponse>> getRecentLargeTransactions(
            @Valid @ModelAttribute RecentLargeTransactionsRequest request
    ) {
        return ResponseEntity.ok(reportService.getRecentLargeTransactions(request));
    }

    @GetMapping("/orders-by-status")
    public ResponseEntity<List<OrderStatusReportResponse>> getOrdersByStatus(
            @Valid @ModelAttribute DateRangeRequest request
    ) {
        return ResponseEntity.ok(reportService.getOrdersByStatus(request));
    }

    @GetMapping("/category-sales")
    public ResponseEntity<List<CategorySalesResponse>> getCategorySales(
            @Valid @ModelAttribute DateRangeRequest request
    ) {
        return ResponseEntity.ok(reportService.getCategorySales(request));
    }
}
