package org.example.inventory.dto;

import lombok.Builder;
import org.example.inventory.entity.VehicleStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record VehicleResponse(
        UUID id,
        String model,
        BigDecimal price,
        VehicleStatus status,
        UUID dealerId
) {
}
