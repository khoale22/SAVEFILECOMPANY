/*
 *  CandidateItemMasterRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;


import com.heb.pm.entity.CandidateItemMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * This is the repository for a control table.
 *
 * @author vn73545
 * @since 2.8.0
 */
public interface CandidateItemMasterRepository extends JpaRepository<CandidateItemMaster, Integer> {

	/**
	 * Returns a candidate item master based on the candidate work request id and new data.
	 *
	 * @param candidateWorkRequestId The candidate work request id.
	 * @param newData The new data.
	 * @return a candidate item master.
	 */
	CandidateItemMaster findByCandidateWorkRequestIdAndNewData(Long candidateWorkRequestId, boolean newData);

	/**
	 * Returns a list of candidate item master based on the candidate item id.
	 *
	 * @param candidateItemId The candidate item id.
	 * @return a list of candidate item master.
	 */
	List<CandidateItemMaster> findByCandidateItemId(Integer candidateItemId);

	/**
	 * Returns a candidate item master based on the candidate work request id.
	 *
	 * @param candidateWorkRequestId The candidate work request id.
	 * @param newDataVendor The new data.
	 * @param newDataWarehouse The new data.
	 * @return a candidate item master.
	 */
	CandidateItemMaster findFirstByCandidateWorkRequestIdAndCandidateVendorLocationItemsNewDataAndCandidateWarehouseLocationItemsNewData(Long candidateWorkRequestId, boolean newDataVendor, boolean newDataWarehouse);

	@Query(value = "select candidateItemMaster.caseUpc from CandidateItemMaster candidateItemMaster where candidateItemMaster.caseUpc in :upcsGenLst")
	List<Long> findCaseUpcsByUpcGenList(@Param("upcsGenLst") List<Long> upcsGenLst);
}
