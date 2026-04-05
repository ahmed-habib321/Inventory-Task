package org.example.inventory.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventory.repository.DealerRepository;
import org.example.inventory.dto.CreateDealerRequest;
import org.example.inventory.dto.DealerResponse;
import org.example.inventory.dto.PagedResponse;
import org.example.inventory.dto.UpdateDealerRequest;
import org.example.inventory.entity.Dealer;
import org.example.inventory.mapper.DealerMapper;
import org.example.inventory.security.TenantContext;
import org.example.inventory.security.TenantEnforcementValidator;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealerService {

    private final DealerRepository dealerRepository;
    private final DealerMapper dealerMapper;
    private final TenantEnforcementValidator tenantValidator;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public DealerResponse create(CreateDealerRequest request) {
        String tenantId = TenantContext.get();

        if (dealerRepository.existsByEmailAndTenantId(request.email(), tenantId)) {
            throw new IllegalArgumentException(
                    "A dealer with email '%s' already exists in this tenant.".formatted(request.email()));
        }

        Dealer dealer = dealerMapper.toEntity(request);
        dealer.setTenantId(tenantId);

        Dealer saved = dealerRepository.save(dealer);
        log.debug("Created dealer {} for tenant {}", saved.getId(), tenantId);
        return dealerMapper.toResponse(saved);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    public DealerResponse findById(UUID id) {
        String tenantId = TenantContext.get();
        Dealer dealer = loadAndValidate(id, tenantId);
        return dealerMapper.toResponse(dealer);
    }

    public PagedResponse<DealerResponse> findAll(Pageable pageable) {
        String tenantId = TenantContext.get();
        return PagedResponse.from(
                dealerRepository.findAllByTenantId(tenantId, pageable)
                        .map(dealerMapper::toResponse));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public DealerResponse update(UUID id, UpdateDealerRequest request) {
        String tenantId = TenantContext.get();
        Dealer dealer = loadAndValidate(id, tenantId);

        if (request.email() != null
                && !request.email().equals(dealer.getEmail())
                && dealerRepository.existsByEmailAndTenantId(request.email(), tenantId)) {
            throw new IllegalArgumentException(
                    "A dealer with email '%s' already exists in this tenant.".formatted(request.email()));
        }

        dealerMapper.updateEntity(request, dealer);
        Dealer saved = dealerRepository.save(dealer);
        log.debug("Updated dealer {} for tenant {}", id, tenantId);
        return dealerMapper.toResponse(saved);
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(UUID id) {
        String tenantId = TenantContext.get();
        Dealer dealer = loadAndValidate(id, tenantId);
        dealerRepository.delete(dealer);
        log.debug("Deleted dealer {} for tenant {}", id, tenantId);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Dealer loadAndValidate(UUID id, String tenantId) {
        Dealer dealer = dealerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dealer not found with id: " + id));
        tenantValidator.validate(tenantId, "Dealer", id);
        return dealer;
    }
}
