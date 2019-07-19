package com.heb.pm.repository;

import com.heb.pm.entity.ProductRelationshipAudit;
import com.heb.pm.entity.ProductRelationshipAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This repository is intended to get all change records on the product relationship table based on the search criteria
 * @version 2.15.0
 */
public interface ProductRelationshipAuditRepository extends JpaRepository<ProductRelationshipAudit, ProductRelationshipAuditKey> {

	/**
	 * Retrieves all records of changes on an product relationship's attributes
	 * @param prodId product id
	 * @return all records of changes on an product relationship's attributes
	 */
	@Query( "select a from ProductRelationshipAudit a where a.key.productId = :id and a.key.productRelationshipCode in ('FPROD', 'ADDON', 'USELL') order by a.lastUpdateTs desc" )
	List<ProductRelationshipAudit> findByKeyProductIdOrderByKeyChangedOn(@Param("id")Long prodId);
	/**
	 * Retrieves all records of changes on an product relationship's attributes
	 * @param relatedProductId product id
	 * @return all records of changes on an product relationship's attributes
	 */
	List<ProductRelationshipAudit> findByKeyProductRelationshipCodeAndKeyRelatedProductIdOrderByKeyChangedOnDesc(String productRelationshipCode, Long relatedProductId);
}
