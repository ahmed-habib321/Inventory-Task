package org.example.inventory.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriceRange {

    String message() default "priceMin must be less than or equal to priceMax";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
