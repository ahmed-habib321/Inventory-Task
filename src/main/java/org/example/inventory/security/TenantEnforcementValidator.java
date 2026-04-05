package org.example.inventory.security;

import org.example.inventory.exception.TenantMismatchException;
import org.springframework.stereotype.Component;

@Component
public class TenantEnforcementValidator {
    public void validate(String entityTenantId, String resourceType, Object resourceId) {
        String current = TenantContext.get();
        if (!entityTenantId.equals(current)) {
            throw new TenantMismatchException(resourceType, resourceId);
        }
    }
}