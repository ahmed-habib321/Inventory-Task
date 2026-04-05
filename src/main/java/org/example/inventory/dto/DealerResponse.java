package org.example.inventory.dto;

import lombok.Builder;
import org.example.inventory.entity.SubscriptionType;

import java.util.UUID;

@Builder
public record DealerResponse (
         UUID id,
         String name,
         String email,
         SubscriptionType subscriptionType
){
}
