package org.example.inventory.dto;

import org.example.inventory.entity.SubscriptionType;
import org.example.inventory.entity.VehicleStatus;
import org.example.inventory.validation.ValidPriceRange;

import java.math.BigDecimal;

@ValidPriceRange
public record VehicleFilterRequest(
        String model,
        VehicleStatus status,
        BigDecimal priceMin,
        BigDecimal priceMax,
        SubscriptionType subscription
) {}
