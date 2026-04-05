package org.example.inventory.dto;

import jakarta.validation.constraints.Email;
import org.example.inventory.entity.SubscriptionType;

public record UpdateDealerRequest
        (
                String name,
                @Email(message = "email must be a valid email address")
                String email,
                SubscriptionType subscriptionType
        ) {
}
