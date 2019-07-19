package com.heb.pm.repository;

import com.heb.pm.entity.ProductWarning;
import com.heb.pm.entity.ProductWarningKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Restful client for Product Warnings.
 * @author m594201
 * @version 2.14.0
 */
public interface ProductWarningsRepository extends JpaRepository<ProductWarning, ProductWarningKey> {
	List<ProductWarning> findByKeyProdId(Long prodId);
}
