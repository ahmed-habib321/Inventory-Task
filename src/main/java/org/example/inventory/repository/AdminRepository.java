package org.example.inventory.repository;

import org.example.inventory.entity.Dealer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;


import java.util.List;
import java.util.UUID;

@RepositoryDefinition(domainClass = Dealer.class, idClass = UUID.class)
public interface AdminRepository {
    @Query("SELECT d.subscriptionType, COUNT(d) FROM Dealer d GROUP BY d.subscriptionType")
    List<Object[]> countBySubscriptionType();

    @Query("""
            SELECT d.subscriptionType, COUNT(d)
            FROM Dealer d
            WHERE d.tenantId = :tenantId
            GROUP BY d.subscriptionType
            """)
    List<Object[]> countBySubscriptionTypeForTenant(String tenantId);
}
