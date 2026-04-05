package org.example.inventory.exception;

public class TenantMismatchException extends RuntimeException {

    public TenantMismatchException(String resourceType, Object resourceId) {
        super("Access denied: %s with id '%s' does not belong to the current tenant."
                .formatted(resourceType, resourceId));
    }
}