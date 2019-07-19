/*
 * PrimaryUpcRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.PrimaryUpc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for Primary UPC
 *
 * @author vn75469
 * @since 2.16.0
 */
public interface PrimaryUpcRepository extends JpaRepository<PrimaryUpc, Long> {
	String FIND_ITEM_CODES_BY_UPC_CODE_QUERY = "SELECT DISTINCT pu.itemCode FROM PrimaryUpc pu " +
			"INNER JOIN pu.associateUpcs au WHERE au.upc = :upcCode";

	/**
	 * Find Item Codes by Item Code.
	 *
	 * @param upcCode itemTypeCode.
	 * @return list of Item Codes.
	 */
	@Query(value = FIND_ITEM_CODES_BY_UPC_CODE_QUERY)
	List<Long> findByUpcCode(@Param("upcCode") Long upcCode);
}
