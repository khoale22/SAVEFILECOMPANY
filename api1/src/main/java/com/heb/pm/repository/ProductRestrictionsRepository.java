package com.heb.pm.repository;

import com.heb.pm.entity.ProductRestrictions;
import com.heb.pm.entity.ProductRestrictionsKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Restful client for Product Restrictions
 * @author m594201
 * @version 2.14.0
 */
public interface ProductRestrictionsRepository extends JpaRepository<ProductRestrictions, ProductRestrictionsKey> {
	List<ProductRestrictions> findByKeyProdIdAndRestrictionRestrictionGroupCode(Long prodId, String restrictionGroupCode);
}
