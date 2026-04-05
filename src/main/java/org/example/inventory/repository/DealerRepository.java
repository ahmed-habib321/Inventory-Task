package org.example.inventory.repository;

import org.example.inventory.entity.Dealer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, UUID> {


    Optional<Dealer> findByIdAndTenantId(UUID id, String tenantId);


    Page<Dealer> findAllByTenantId(String tenantId, Pageable pageable);


    boolean existsByEmailAndTenantId(String email, String tenantId);
}
