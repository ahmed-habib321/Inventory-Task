package org.example.inventory.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.inventory.repository.DealerRepository;
import org.example.inventory.repository.VehicleRepository;
import org.example.inventory.dto.*;
import org.example.inventory.entity.Dealer;
import org.example.inventory.entity.Vehicle;
import org.example.inventory.mapper.VehicleMapper;
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
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;
    private final VehicleMapper vehicleMapper;
    private final TenantEnforcementValidator tenantValidator;

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public VehicleResponse create(CreateVehicleRequest request) {
        String tenantId = TenantContext.get();

        UUID dealerId = UUID.fromString(request.dealerId());
        Dealer dealer = dealerRepository.findByIdAndTenantId(dealerId, tenantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dealer not found with id: " + dealerId));

        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setTenantId(tenantId);
        vehicle.setDealer(dealer);

        Vehicle saved = vehicleRepository.save(vehicle);
        log.debug("Created vehicle {} under dealer {} for tenant {}", saved.getId(), dealerId, tenantId);
        return vehicleMapper.toResponse(saved);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    public VehicleResponse findById(UUID id) {
        String tenantId = TenantContext.get();
        Vehicle vehicle = loadAndValidate(id, tenantId);
        return vehicleMapper.toResponse(vehicle);
    }

    public PagedResponse<VehicleResponse> findAll(VehicleFilterRequest filter, Pageable pageable) {
        String tenantId = TenantContext.get();

        return PagedResponse.from(
                vehicleRepository.findAllWithFilters(
                        tenantId,
                        filter.model(),
                        filter.status(),
                        filter.priceMin(),
                        filter.priceMax(),
                        filter.subscription(),
                        pageable
                ).map(vehicleMapper::toResponse)
        );
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public VehicleResponse update(UUID id, UpdateVehicleRequest request) {
        String tenantId = TenantContext.get();
        Vehicle vehicle = loadAndValidate(id, tenantId);
        vehicleMapper.updateEntity(request, vehicle);
        Vehicle saved = vehicleRepository.save(vehicle);
        log.debug("Updated vehicle {} for tenant {}", id, tenantId);
        return vehicleMapper.toResponse(saved);
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void delete(UUID id) {
        String tenantId = TenantContext.get();
        Vehicle vehicle = loadAndValidate(id, tenantId);
        vehicleRepository.delete(vehicle);
        log.debug("Deleted vehicle {} for tenant {}", id, tenantId);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Vehicle loadAndValidate(UUID id, String tenantId) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Vehicle not found with id: " + id));
        tenantValidator.validate(tenantId, "Vehicle", id);
        return vehicle;
    }
}
