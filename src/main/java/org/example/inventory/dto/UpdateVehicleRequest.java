package org.example.inventory.dto;

import jakarta.validation.constraints.Positive;
import org.example.inventory.entity.VehicleStatus;

import java.math.BigDecimal;

public record UpdateVehicleRequest
        (
                String model,
                @Positive(message = "price must be positive")
                BigDecimal price,
                VehicleStatus status
        ) {
}
