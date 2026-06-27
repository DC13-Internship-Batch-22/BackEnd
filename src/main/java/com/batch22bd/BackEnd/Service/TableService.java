package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.request.TableRequest;
import com.batch22bd.BackEnd.DTO.response.TableResponse;
import com.batch22bd.BackEnd.Entity.Order;
import com.batch22bd.BackEnd.Entity.TableEntity;
import com.batch22bd.BackEnd.Enum.OrderStatus;
import com.batch22bd.BackEnd.Enum.TableStatus;
import com.batch22bd.BackEnd.Mapper.TableMapper;
import com.batch22bd.BackEnd.Repository.OrderRepository;
import com.batch22bd.BackEnd.Repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TableService {

    private static final Set<OrderStatus> ACTIVE_ORDER_STATUSES = EnumSet.of(
            OrderStatus.PENDING,
            OrderStatus.CONFIRMED
    );

    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<TableResponse> getAllTables(int page, int size) {
        Page<TableEntity> tablePage = tableRepository.findByIsDeletedFalse(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
        );
        List<TableEntity> tables = tablePage.getContent();
        Map<Long, Long> activeOrderIdsByTableId = getActiveOrderIdsByTableId(tables);

        return tablePage.map(table -> TableMapper.toResponse(table, getOrderIdIfOccupied(table, activeOrderIdsByTableId)));
    }

    @Transactional(readOnly = true)
    public TableResponse getTableById(Long id) {
        TableEntity table = findActiveTable(id);
        Long orderId = null;

        if (table.getStatus() == TableStatus.OCCUPIED) {
            orderId = orderRepository
                    .findFirstByTable_IdAndStatusInAndIsDeletedFalseOrderByCreatedAtDesc(table.getId(), ACTIVE_ORDER_STATUSES)
                    .map(Order::getId)
                    .orElse(null);
        }

        return TableMapper.toResponse(table, orderId);
    }

    @Transactional
    public TableResponse createTable(TableRequest dto) {
        if (tableRepository.existsByTableNumberAndIsDeletedFalse(dto.tableNumber())) {
            throw new IllegalArgumentException("Table number already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        TableEntity table = TableMapper.toEntity(dto);
        table.setCreatedAt(now);
        table.setUpdatedAt(now);
        table.setIsDeleted(false);

        return TableMapper.toResponse(tableRepository.save(table));
    }

    @Transactional
    public TableResponse updateTable(Long id, TableRequest dto) {
        TableEntity table = findActiveTable(id);
        if (tableRepository.existsByTableNumberAndIdNotAndIsDeletedFalse(dto.tableNumber(), id)) {
            throw new IllegalArgumentException("Table number already exists");
        }

        TableMapper.updateEntity(table, dto);
        table.setUpdatedAt(LocalDateTime.now());

        return TableMapper.toResponse(tableRepository.save(table));
    }

    @Transactional
    public void deleteTable(Long id) {
        TableEntity table = findActiveTable(id);
        table.setIsDeleted(true);
        table.setUpdatedAt(LocalDateTime.now());
        tableRepository.save(table);
    }

    private TableEntity findActiveTable(Long id) {
        return tableRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
    }

    private Map<Long, Long> getActiveOrderIdsByTableId(List<TableEntity> tables) {
        List<Long> occupiedTableIds = tables.stream()
                .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                .map(TableEntity::getId)
                .toList();

        if (occupiedTableIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, Long> orderIdsByTableId = new HashMap<>();
        orderRepository
                .findByTable_IdInAndStatusInAndIsDeletedFalseOrderByCreatedAtDesc(
                        occupiedTableIds,
                        ACTIVE_ORDER_STATUSES
                )
                .forEach(order -> orderIdsByTableId.putIfAbsent(order.getTable().getId(), order.getId()));

        return orderIdsByTableId;
    }

    public Long getOrderIdIfOccupied(TableEntity table, Map<Long, Long> activeOrderIdsByTableId) {
        if (table.getStatus() != TableStatus.OCCUPIED) {
            return null;
        }

        return activeOrderIdsByTableId.get(table.getId());
    }
}
