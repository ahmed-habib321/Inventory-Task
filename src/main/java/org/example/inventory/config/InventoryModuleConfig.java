package org.example.inventory.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryModuleConfig {

    @Bean
    public OpenAPI inventoryOpenAPI() {
        final String basicSchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Module API")
                        .description("""
                                Multi-tenant Dealer & Vehicle Inventory module.
                                
                                **Authentication:** HTTP Basic (user/user123 or admin/admin123)
                                
                                **Tenant header:** Every request must include `X-Tenant-Id` (e.g. `tenant-A`).
                                Missing header → 400. Cross-tenant access → 403.
                                
                                **Roles:**
                                - `USER` — access to /dealers and /vehicles
                                - `GLOBAL_ADMIN` — additionally access to /admin/**
                                """)
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(basicSchemeName))
                .components(new Components()
                        .addSecuritySchemes(basicSchemeName,
                                new SecurityScheme()
                                        .name(basicSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }
}
