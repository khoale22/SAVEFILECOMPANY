/*
 * DynamicAttributeAuditRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.DynamicAttributeAudit;
import com.heb.pm.entity.DynamicAttributeAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for dynamic attributes audit entries.
 *
 */
public interface DynamicAttributeAuditRepository extends JpaRepository<DynamicAttributeAudit, DynamicAttributeAuditKey> {
	/**
	 * This method returns all dynamic attributes audits that are part of the tags and specs attributes for a product
	 * @param key the product id
	 * @param keyType the type of product for the attribute
	 * @param sourceSystem the source of the attribute
	 * @return
	 */
	List<DynamicAttributeAudit> findByKeyKeyAndKeyKeyTypeAndKeySourceSystemOrderByKeyAttributeId(long key, String keyType, int sourceSystem);

	/**
	 * This method returns all dynamic attributes audits that are part of the tags and specs attributes for a product
	 * @param prodId the product id
	 * @param keyType  the itemProductKeyCode
	 * @param attributeId the logic attribute id
	 * @param dataSources list data source of the attributes
	 * @return
	 */
	List<DynamicAttributeAudit> findByKeyKeyAndKeyKeyTypeAndKeyAttributeIdNotAndKeySourceSystemInOrderByKeyChangedOn(Long prodId, String keyType, int  attributeId, List<Integer> dataSources);

}
