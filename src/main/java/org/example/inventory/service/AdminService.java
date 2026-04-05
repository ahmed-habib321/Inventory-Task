package org.example.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventory.repository.AdminRepository;
import org.example.inventory.entity.SubscriptionType;
import org.example.inventory.security.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;


    public Map<SubscriptionType, Long> countBySubscription() {
        log.info("Global count requested by admin in tenant {}", TenantContext.get());
        return toMap(adminRepository.countBySubscriptionType());
    }


    public Map<SubscriptionType, Long> countBySubscriptionForTenant(String tenantId) {
        return toMap(adminRepository.countBySubscriptionTypeForTenant(tenantId));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<SubscriptionType, Long> toMap(java.util.List<Object[]> rows) {
        // Pre-populate all enum keys with zero so both BASIC and PREMIUM always appear
        Map<SubscriptionType, Long> result = new EnumMap<>(SubscriptionType.class);
        Arrays.stream(SubscriptionType.values()).forEach(t -> result.put(t, 0L));

        for (Object[] row : rows) {
            SubscriptionType type = (SubscriptionType) row[0];
            Long count = (Long) row[1];
            result.put(type, count);
        }
        return result;
    }
}
