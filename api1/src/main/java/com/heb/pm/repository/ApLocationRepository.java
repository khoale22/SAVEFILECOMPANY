/*
 * ApLocationRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ApLocation;
import com.heb.pm.entity.ApLocationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve information about ApLocation.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface ApLocationRepository extends JpaRepository<ApLocation, ApLocationKey> {
	/**
	 * Query to find ApLocation object by vendorNumber and apTypeCode in 'DS'.
	 */
	String FIND_APLOCATION_QUERY = "SELECT al FROM ApLocation al WHERE al.key.apTypeCode in ('DS')" +
			" and al.key.apVendorNumber = :vendorNumber";

	@Query(FIND_APLOCATION_QUERY)
    ApLocation findOneByVendorNumber(@Param("vendorNumber") Integer vendorNumber);

	ApLocation findFirstByKeyApVendorNumber(Integer apVendorNumber);
}
