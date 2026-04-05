package org.example.inventory.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.inventory.dto.VehicleFilterRequest;


public class PriceRangeValidator implements ConstraintValidator<ValidPriceRange, VehicleFilterRequest> {

    @Override
    public boolean isValid(VehicleFilterRequest request, ConstraintValidatorContext context) {
        if (request == null) return true;
        if (request.priceMin() == null || request.priceMax() == null) return true;
        return request.priceMin().compareTo(request.priceMax()) <= 0;
    }
}
