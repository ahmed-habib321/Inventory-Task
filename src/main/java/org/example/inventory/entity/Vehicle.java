package org.example.inventory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "vehicles",
        indexes = {
                @Index(name = "idx_vehicle_tenant", columnList = "tenant_id"),
                @Index(name = "idx_vehicle_dealer", columnList = "dealer_id"),
                @Index(name = "idx_vehicle_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Vehicle {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealer_id", nullable = false)
    private Dealer dealer;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

}
