/*
 * ProductDescriptionAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductDescriptionAudit;
import com.heb.pm.entity.ProductDescriptionAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA Repository for ProductDescriptionRepository.
 *
 * @author a786878
 * @since 2.16.0
 */
public interface ProductDescriptionAuditRepository extends JpaRepository<ProductDescriptionAudit, ProductDescriptionAuditKey>{

	/**
	 * Retrieve proposed product description audit records
	 * @param productId to search for
	 * @param languageType to search for
	 * @return selected audit records
	 */
	@Query(value = "select d from ProductDescriptionAudit d where d.key.descriptionType = :descriptionType and d.key.languageType = :languageType and d.key.productId = :productId")
	List<ProductDescriptionAudit> getProductDescriptionAudits(@Param("descriptionType") String descriptionType, @Param("languageType")String languageType, @Param("productId")Long productId);

	/**
	 * Retrieves all records of changes on an product description attributes
	 * @param productId to search for
	 * @return selected audit records
	 */
	List<ProductDescriptionAudit> findByKeyProductIdOrderByKeyChangedOn(@Param("productId")Long productId);
}
