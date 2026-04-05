package org.example.inventory.exception;

public class MissingTenantException extends RuntimeException {

    public MissingTenantException() {
        super("Required header 'X-Tenant-Id' is missing.");
    }
}