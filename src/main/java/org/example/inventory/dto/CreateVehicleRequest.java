package org.example.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.inventory.entity.VehicleStatus;
import org.example.inventory.validation.ValidUUID;

import java.math.BigDecimal;

public record CreateVehicleRequest
        (
                @NotBlank(message = "model must not be blank")
                String model,
                @NotNull(message = "price is required")
                @Positive(message = "price must be positive")
                BigDecimal price,
                @NotNull(message = "dealerId is required")
                @ValidUUID
                String dealerId,
                @NotNull(message = "status is required (AVAILABLE or SOLD)")
                VehicleStatus status
        ) {
}
