package org.example.inventory.security;

public final class TenantContext {

    public static final ScopedValue<String> CURRENT_TENANT = ScopedValue.newInstance();

    private TenantContext() {}

    public static String get() {
        return CURRENT_TENANT.get();
    }

    public static boolean isSet() {
        return CURRENT_TENANT.isBound();
    }

}
