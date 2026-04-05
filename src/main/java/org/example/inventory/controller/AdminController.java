package org.example.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.inventory.entity.SubscriptionType;
import org.example.inventory.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Platform-wide admin operations (GLOBAL_ADMIN only)")
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/dealers/countBySubscription")
    @PreAuthorize("hasRole('GLOBAL_ADMIN')")
    @Operation(
            summary = "Count dealers by subscription type",
            description = """
                    Returns dealer counts grouped by subscription type.
                    - Without tenantId: GLOBAL count across all tenants.
                    - With tenantId: scoped count for that tenant only.
                    Requires GLOBAL_ADMIN role.
                    """)
    public Map<SubscriptionType, Long> countBySubscription(
            @Parameter(description = "Optional: scope the count to a specific tenant")
            @RequestParam(required = false) String tenantId) {

        if (tenantId != null && !tenantId.isBlank()) {
            return adminService.countBySubscriptionForTenant(tenantId);
        }
        return adminService.countBySubscription();
    }
}