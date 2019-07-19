package com.heb.pm.repository;

import com.heb.pm.entity.ProductRestrictionsAudit;
import com.heb.pm.entity.ProductRestrictionsAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Restful client for Product Restrictions Audits
 */
public interface ProductRestrictionsAuditRepository extends JpaRepository<ProductRestrictionsAudit, ProductRestrictionsAuditKey> {
	List<ProductRestrictionsAudit> findByKeyProdIdAndRestrictionRestrictionGroupCode(Long prodId, String restrictionGroupCode);

	List<ProductRestrictionsAudit> findByKeyProdIdAndRestrictionRestrictionGroupCodeNot(Long prodId, String restrictionGroupCode);
}
