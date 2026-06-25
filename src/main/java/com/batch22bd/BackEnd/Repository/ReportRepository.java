package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Order, Long> {

    @Query(value = """
            SELECT
                COALESCE(SUM(o.total_amount), 0) AS "revenue",
                COUNT(o.id) AS "totalOrders",
                COALESCE(AVG(o.total_amount), 0) AS "averageOrderValue",
                COALESCE(MAX(o.total_amount), 0) AS "largestOrderAmount",
                COALESCE((
                    SELECT SUM((item ->> 'quantity')::bigint)
                    FROM orders oi
                    CROSS JOIN LATERAL jsonb_array_elements(COALESCE(oi.items, '[]'::jsonb)) AS item
                    WHERE oi.created_at >= :start
                      AND oi.created_at < :end
                      AND oi.status = 'COMPLETED'
                      AND COALESCE(oi.is_deleted, false) = false
                ), 0) AS "totalItemsSold"
            FROM orders o
            WHERE o.created_at >= :start
              AND o.created_at < :end
              AND o.status = 'COMPLETED'
              AND COALESCE(o.is_deleted, false) = false
            """, nativeQuery = true)
    ReportSummaryProjection getSummary(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT
                CASE :groupBy
                    WHEN 'HOUR' THEN to_char(period_start, 'YYYY-MM-DD HH24:00')
                    WHEN 'DAY' THEN to_char(period_start, 'YYYY-MM-DD')
                    WHEN 'WEEK' THEN to_char(period_start, 'IYYY-"W"IW')
                    WHEN 'MONTH' THEN to_char(period_start, 'YYYY-MM')
                END AS "period",
                revenue AS "revenue",
                order_count AS "orderCount"
            FROM (
                SELECT
                    CASE :groupBy
                        WHEN 'HOUR' THEN date_trunc('hour', o.created_at)
                        WHEN 'DAY' THEN date_trunc('day', o.created_at)
                        WHEN 'WEEK' THEN date_trunc('week', o.created_at)
                        WHEN 'MONTH' THEN date_trunc('month', o.created_at)
                    END AS period_start,
                    COALESCE(SUM(o.total_amount), 0) AS revenue,
                    COUNT(o.id) AS order_count
                FROM orders o
                WHERE o.created_at >= :start
                  AND o.created_at < :end
                  AND o.status = 'COMPLETED'
                  AND COALESCE(o.is_deleted, false) = false
                GROUP BY period_start
            ) result
            ORDER BY period_start ASC
            """, nativeQuery = true)
    List<RevenueTrendProjection> getRevenueTrend(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("groupBy") String groupBy
    );

    @Query(value = """
            SELECT
                product_id AS "productId",
                product_name AS "productName",
                quantity_sold AS "quantitySold",
                revenue AS "revenue"
            FROM (
                SELECT
                    (item ->> 'productId')::bigint AS product_id,
                    item ->> 'productName' AS product_name,
                    COALESCE(SUM((item ->> 'quantity')::bigint), 0) AS quantity_sold,
                    COALESCE(SUM((item ->> 'quantity')::numeric * (item ->> 'price')::numeric), 0) AS revenue
                FROM orders o
                CROSS JOIN LATERAL jsonb_array_elements(COALESCE(o.items, '[]'::jsonb)) AS item
                WHERE o.created_at >= :start
                  AND o.created_at < :end
                  AND o.status = 'COMPLETED'
                  AND COALESCE(o.is_deleted, false) = false
                GROUP BY product_id, product_name
            ) result
            ORDER BY
                CASE WHEN :sortBy = 'REVENUE' THEN revenue END DESC,
                CASE WHEN :sortBy = 'QUANTITY' THEN quantity_sold END DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<TopSellingDishProjection> getTopSellingDishes(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("limit") Integer limit,
            @Param("sortBy") String sortBy
    );

    @Query(value = """
            SELECT o.*
            FROM orders o
            WHERE o.created_at >= :start
              AND o.created_at < :end
              AND o.total_amount >= :minAmount
              AND o.status = 'COMPLETED'
              AND COALESCE(o.is_deleted, false) = false
            ORDER BY o.total_amount DESC, o.created_at DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Order> findRecentLargeTransactions(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("minAmount") BigDecimal minAmount,
            @Param("limit") Integer limit
    );

    @Query(value = """
            SELECT
                o.status AS "status",
                COUNT(o.id) AS "count"
            FROM orders o
            WHERE o.created_at >= :start
              AND o.created_at < :end
              AND COALESCE(o.is_deleted, false) = false
            GROUP BY o.status
            """, nativeQuery = true)
    List<OrderStatusProjection> getOrdersByStatus(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
            SELECT
                c.id AS "categoryId",
                c.name AS "categoryName",
                COALESCE(SUM((item ->> 'quantity')::bigint), 0) AS "quantitySold",
                COALESCE(SUM((item ->> 'quantity')::numeric * (item ->> 'price')::numeric), 0) AS "revenue"
            FROM orders o
            CROSS JOIN LATERAL jsonb_array_elements(COALESCE(o.items, '[]'::jsonb)) AS item
            JOIN foods f ON f.id = (item ->> 'productId')::bigint
            JOIN categories c ON c.id = f.category_id
            WHERE o.created_at >= :start
              AND o.created_at < :end
              AND o.status = 'COMPLETED'
              AND COALESCE(o.is_deleted, false) = false
            GROUP BY c.id, c.name
            ORDER BY "revenue" DESC
            """, nativeQuery = true)
    List<CategorySalesProjection> getCategorySales(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    interface ReportSummaryProjection {
        BigDecimal getRevenue();

        Long getTotalOrders();

        BigDecimal getAverageOrderValue();

        Long getTotalItemsSold();

        BigDecimal getLargestOrderAmount();
    }

    interface RevenueTrendProjection {
        String getPeriod();

        BigDecimal getRevenue();

        Long getOrderCount();
    }

    interface TopSellingDishProjection {
        Long getProductId();

        String getProductName();

        Long getQuantitySold();

        BigDecimal getRevenue();
    }

    interface OrderStatusProjection {
        String getStatus();

        Long getCount();
    }

    interface CategorySalesProjection {
        Long getCategoryId();

        String getCategoryName();

        Long getQuantitySold();

        BigDecimal getRevenue();
    }
}
