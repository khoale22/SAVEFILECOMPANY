/*
 *  SellingUnitAuditRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.SellingUnitAudit;
import com.heb.pm.entity.SellingUnitAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This repository for a selling unit audit.
 *
 * @author l730832
 * @since 2.12.0
 */
public interface SellingUnitAuditRepository extends JpaRepository<SellingUnitAudit, SellingUnitAuditKey> {

	/**
	 * Retrieves all records of changes on a selling units attributes
	 * @param itemCode numeric component to identify the selling unit
	 * @return all records of changes on an item master's attributes
	 */
	List<SellingUnitAudit> findByKeyUpcOrderByKeyChangedOn(Long itemCode);
}
