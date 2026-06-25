package com.batch22bd.BackEnd.Service;

import com.batch22bd.BackEnd.DTO.request.TableRequest;
import com.batch22bd.BackEnd.DTO.response.TableResponse;
import com.batch22bd.BackEnd.Entity.TableEntity;
import com.batch22bd.BackEnd.Mapper.TableMapper;
import com.batch22bd.BackEnd.Repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;

    public List<TableResponse> getAllTables() {
        return tableRepository.findAllByIsDeletedFalseOrderByIdAsc()
                .stream()
                .map(TableMapper::toResponse)
                .toList();
    }

    public TableResponse getTableById(Long id) {
        return TableMapper.toResponse(findActiveTable(id));
    }

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

    public TableResponse updateTable(Long id, TableRequest dto) {
        TableEntity table = findActiveTable(id);
        if (tableRepository.existsByTableNumberAndIdNotAndIsDeletedFalse(dto.tableNumber(), id)) {
            throw new IllegalArgumentException("Table number already exists");
        }

        TableMapper.updateEntity(table, dto);
        table.setUpdatedAt(LocalDateTime.now());

        return TableMapper.toResponse(tableRepository.save(table));
    }

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
}
