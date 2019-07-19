/*
 * TransactionTrackingRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.TransactionTracker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author d116773
 * @since 2.12.0
 */
public interface TransactionTrackingRepository extends JpaRepository<TransactionTracker, Long>,TransactionTrackingRepositoryCommon {

	/**
	 * Get list user created tracking.
	 *
	 * @param lstSource   the resource id
	 * @param lstFileName file name
	 * @return
	 */
	@Query(FIND_USER_CREATED_TRACKING)
	List<String> findAllUserCreatedTracking(@Param("listSource") List<Integer> lstSource,
											@Param("listFileName") List<String> lstFileName);

}
