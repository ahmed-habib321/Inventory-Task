package org.example.inventory.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.inventory.exception.WrappedFilterException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Component
@Order(10)
public class TenantResolutionFilter implements Filter {

    public static final String TENANT_HEADER = "X-Tenant-Id";

    private static final java.util.List<String> EXCLUDED_PREFIXES = java.util.List.of(
            "/swagger-ui",
            "/api-docs",
            "/h2-console"
    );


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        if (isExcluded(path)) {
            chain.doFilter(request, response);
            return;
        }


        String tenantId = httpRequest.getHeader(TENANT_HEADER);
        if (!StringUtils.hasText(tenantId)) {
            httpResponse.setStatus(400);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("""
            {"status":400,"error":"Bad Request","message":"Required header 'X-Tenant-Id' is missing."}
            """);
            return;
        }

        log.debug("Tenant resolved: {}", tenantId);

        try {
            ScopedValue.where(TenantContext.CURRENT_TENANT, tenantId.trim())
                    .run(() -> {
                        try {
                            chain.doFilter(request, response);
                        } catch (IOException | ServletException e) {
                            throw new WrappedFilterException(e);
                        }
                    });
        } catch (WrappedFilterException e) {
            e.rethrow();
        }
    }

    private boolean isExcluded(String path) {
        return EXCLUDED_PREFIXES.stream().anyMatch(path::startsWith);
    }
}