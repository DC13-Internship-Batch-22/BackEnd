package com.batch22bd.BackEnd.Mapper;

import com.batch22bd.BackEnd.DTO.request.TableRequest;
import com.batch22bd.BackEnd.DTO.response.TableResponse;
import com.batch22bd.BackEnd.Entity.TableEntity;

public class TableMapper {

    private TableMapper() {
    }

    public static TableEntity toEntity(TableRequest dto) {
        return TableEntity.builder()
                .tableNumber(dto.tableNumber())
                .capacity(dto.capacity())
                .status(dto.status())
                .build();
    }

    public static TableResponse toResponse(TableEntity table) {
        return toResponse(table, null);
    }

    public static TableResponse toResponse(TableEntity table, Long orderId) {
        return new TableResponse(
                table.getId(),
                table.getTableNumber(),
                table.getCapacity(),
                table.getStatus(),
                orderId,
                table.getCreatedAt(),
                table.getUpdatedAt()
        );
    }

    public static void updateEntity(TableEntity table, TableRequest dto) {
        table.setTableNumber(dto.tableNumber());
        table.setCapacity(dto.capacity());
        table.setStatus(dto.status());
    }
}
