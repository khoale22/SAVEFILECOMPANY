package com.heb.pm.repository;

import com.heb.pm.entity.TierPricingAudit;
import com.heb.pm.entity.TierPricingAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface attribute repository.
 * @author  vn70633
 * @since 2.19.0
 */
public interface TierPricingAuditRepository extends JpaRepository<TierPricingAudit, TierPricingAuditKey> {

    /**
     * Find tier pricing audit by product id.
     *
     * @param prodId the product id
     * @return the list
     */
    List<TierPricingAudit> findByKeyProdIdOrderByKeyChangedOn(Long prodId);
}
