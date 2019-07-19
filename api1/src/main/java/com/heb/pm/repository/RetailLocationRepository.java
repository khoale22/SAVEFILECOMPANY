/*
 * RetailLocationRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.RetailLocation;
import com.heb.pm.entity.RetailLocationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about RetailLocation.
 *
 * @author vn70529
 * @since 2.23.0
 */
public interface RetailLocationRepository extends JpaRepository<RetailLocation, RetailLocationKey> {

	/**
	 * Find all retail location.
	 */
	static final String FIND_RETAIL_LOCATION = "SELECT retailLocation " +
			"FROM RetailLocation retailLocation " +
			"WHERE (retailLocation.key.locationTypeCode = :locationTypeCode) " +
			"AND (retailLocation.financialDivisionId = :financialDivisionId) " +
			"AND (retailLocation.retailLocationStatusCode = :retailLocationStatusCode)";

	/**
	 * Find retail location by user store Id.
	 */
	static final String FIND_RETAIL_LOCATION_BY_USER_STORE_ID = "SELECT retailLocation " +
			"FROM RetailLocation retailLocation " +
			"WHERE (retailLocation.key.locationTypeCode = :locationTypeCode) " +
			"AND (retailLocation.financialDivisionId = :financialDivisionId) " +
			"AND (retailLocation.retailLocationStatusCode = :retailLocationStatusCode) " +
			"AND (retailLocation.key.locationNumber = :locationNumber)";

	/**
	 * Returns the list retail locations.
	 * @param linOfBusId the lin of bus id.
	 * @return the list retail locations.
	 */
	List<RetailLocation> findByLinOfBusId(Integer linOfBusId);

	/**
	 * Returns the list of retail locations by lin of bus id and location number.
	 * @param linOfBusId the lin of bus id.
	 * @param locationNumber the location number.
	 * @return the list of retail locations.
	 */
	List<RetailLocation> findByLinOfBusIdAndKeyLocationNumber(Integer linOfBusId, Integer locationNumber);

	/**
	 * Returns the list of retail locations by location type code and financial division id and retail location status code and location number.
	 * @param locationTypeCode the location type code.
	 * @param financialDivisionId the financial division id.
	 * @param retailLocationStatusCode the retail location status code.
	 * @param locationNumber the location number.
	 * @return the list of retail locations.
	 */
	@Query(value = FIND_RETAIL_LOCATION_BY_USER_STORE_ID)
	RetailLocation findRetailLocationByUserStoreId(
			@Param("locationTypeCode") String locationTypeCode,
			@Param("financialDivisionId") Integer financialDivisionId,
			@Param("retailLocationStatusCode") String retailLocationStatusCode,
			@Param("locationNumber") Integer locationNumber);

	/**
	 * Returns the list of retail locations by location type code and financial division id and retail location status code.
	 * @param locationTypeCode the location type code.
	 * @param financialDivisionId the financial division id.
	 * @param retailLocationStatusCode the retail location status code.
	 * @return the list of retail locations.
	 */
	@Query(value = FIND_RETAIL_LOCATION)
	List<RetailLocation> findRetailLocation(
			@Param("locationTypeCode") String locationTypeCode,
			@Param("financialDivisionId") Integer financialDivisionId,
			@Param("retailLocationStatusCode") String retailLocationStatusCode);
}
