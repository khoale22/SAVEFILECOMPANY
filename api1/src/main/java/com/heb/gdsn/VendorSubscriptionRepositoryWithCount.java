/*
 * VendorSubscriptionRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.gdsn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for the VendorSubscription entity when you need counts returned with the queries.
 *
 * @author d116773
 * @since 2.3.0
 */
public interface VendorSubscriptionRepositoryWithCount extends JpaRepository<VendorSubscription, Integer>{

	/**
	 * Searches for vendor subscription record by GLN.
	 *
	 * @param vendorGln The GLN to search by.
	 * @param pageRequest The page request.
	 * @return A page of vendor subscriptions including record counts.
	 */
	Page<VendorSubscription> findByTrimmedVendorGln(String vendorGln, Pageable pageRequest);

	/**
	 * Searches for vendor subscription record by vendor name.
	 *
	 * @param vendorName The vendor name to search by.
	 * @param pageRequest The page request.
	 * @return A page of vendor subscriptions including record counts.
	 */
	@Query("select v from VendorSubscription v where upper(v.vendorName )like upper(:vendorName)")
	Page<VendorSubscription> findByVendorName(@Param("vendorName")String vendorName, Pageable pageRequest);
}
