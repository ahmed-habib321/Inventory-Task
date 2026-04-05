package org.example.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.inventory.entity.SubscriptionType;

public record CreateDealerRequest (
        @NotBlank(message = "name must not be blank")
        String name,

        @NotBlank(message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        String email,

        @NotNull(message = "subscriptionType is required (BASIC or PREMIUM)")
        SubscriptionType subscriptionType
){
}
