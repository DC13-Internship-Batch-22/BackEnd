package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import com.batch22bd.BackEnd.DTO.request.DateRangeRequest;
import com.batch22bd.BackEnd.DTO.request.RevenueTrendRequest;
import com.batch22bd.BackEnd.DTO.request.TopSellingDishesRequest;
import com.batch22bd.BackEnd.DTO.request.RecentLargeTransactionsRequest;
import com.batch22bd.BackEnd.DTO.response.*;
import com.batch22bd.BackEnd.Repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportSummaryResponse getSummary(DateRangeRequest request) {
        DateRange range = toDateRange(request);

        ReportRepository.ReportSummaryProjection projection = reportRepository.getSummary(range.start(),
                range.end());

        return new ReportSummaryResponse(
                projection.getRevenue(),
                projection.getTotalOrders(),
                projection.getAverageOrderValue(),
                projection.getTotalItemsSold(),
                projection.getLargestOrderAmount());
    }

    public List<RevenueTrendResponse> getRevenueTrend(RevenueTrendRequest request) {
        DateRange range = toDateRange(request);

        return reportRepository.getRevenueTrend(
                range.start(),
                range.end(),
                request.getGroupBy().name())
                .stream()
                .map(item -> new RevenueTrendResponse(
                        item.getPeriod(),
                        item.getRevenue(),
                        item.getOrderCount()))
                .toList();
    }

    public List<TopSellingDishResponse> getTopSellingDishes(TopSellingDishesRequest request) {
        DateRange range = toDateRange(request);

        return reportRepository.getTopSellingDishes(
                range.start(),
                range.end(),
                request.getLimit(),
                request.getSortBy().name())
                .stream()
                .map(item -> new TopSellingDishResponse(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantitySold(),
                        item.getRevenue()))
                .toList();
    }

    public List<RecentLargeTransactionResponse> getRecentLargeTransactions(
            RecentLargeTransactionsRequest request) {
        DateRange range = toDateRange(request);

        List<Order> orders = reportRepository.findRecentLargeTransactions(
                range.start(),
                range.end(),
                request.getMinAmount(),
                request.getLimit());

        return orders.stream()
                .map(order -> new RecentLargeTransactionResponse(
                        order.getId(),
                        order.getTable() == null ? null : order.getTable().getTableNumber(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getCreatedAt(),
                        order.getItems()))
                .toList();
    }

    public List<OrderStatusReportResponse> getOrdersByStatus(DateRangeRequest request) {
        DateRange range = toDateRange(request);

        Map<OrderStatus, Long> countMap = reportRepository.getOrdersByStatus(
                range.start(),
                range.end())
                .stream()
                .filter(item -> item.getStatus() != null)
                .collect(Collectors.toMap(
                        item -> OrderStatus.valueOf(item.getStatus()),
                        ReportRepository.OrderStatusProjection::getCount));

        return Arrays.stream(OrderStatus.values())
                .map(status -> new OrderStatusReportResponse(
                        status,
                        countMap.getOrDefault(status, 0L)))
                .toList();
    }

    public List<CategorySalesResponse> getCategorySales(DateRangeRequest request) {
        DateRange range = toDateRange(request);

        return reportRepository.getCategorySales(range.start(), range.end())
                .stream()
                .map(item -> new CategorySalesResponse(
                        item.getCategoryId(),
                        item.getCategoryName(),
                        item.getQuantitySold(),
                        item.getRevenue()))
                .toList();
    }

    private DateRange toDateRange(DateRangeRequest request) {
        LocalDate fromDate = request.getFrom();
        LocalDate toDate = request.getTo();

        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(30);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }

        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("from date must be before or equal to to date");
        }

        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.plusDays(1).atStartOfDay();

        return new DateRange(start, end);
    }

    private record DateRange(
            LocalDateTime start,
            LocalDateTime end) {
    }
}
