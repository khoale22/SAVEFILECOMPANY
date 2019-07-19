/*
 *  CostOwnerRepository
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CostOwner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about query.
 *
 * @author vn87351
 * @since 2.17.0
 */
public interface CostOwnerRepository extends CostOwnerCommon, JpaRepository<CostOwner, Integer> {

	/**
	 * Find by cost owner id and name contain
	 *
	 * @param costOwnerId   The cost owner id
	 * @param costOwnerName The cost owner name
	 * @param pageable      Paging data
	 * @return page data
	 */
	@Query(FIND_COST_OWNER_BY_ID_AND_NAME_SQL)
	List<CostOwner> findAllByCostOwnerIdAndCostOwnerName(@Param("costOwnerId") String costOwnerId,
	                                                     @Param("costOwnerName") String costOwnerName,
	                                                     Pageable pageable);

	/**
	 * Find cost owners filtered by cost owner id.
	 *
	 * @param costOwnerId The cost owner id.
	 * @param pageable    The page request for pagination.
	 * @return the page of cost owners.
	 */
	@Query(FIND_COST_OWNER_BY_ID)
	List<CostOwner> findByCostOwnerId(@Param("costOwnerId") String costOwnerId,
	                                  Pageable pageable);

	/**
	 * Find cost owners filtered by cost owner name.
	 *
	 * @param name        The name to search.
	 * @param pageRequest The page request for pagination.
	 * @return the page of cost owners.
	 */
	List<CostOwner> findByCostOwnerNameIgnoreCaseContaining(String name, Pageable pageRequest);

	/**
	 * Find cost owners filtered by cost owner name.
	 *
	 * @param name The name to search.
	 * @return cost owners found.
	 */
	List<CostOwner> findByTrimmedCostOwnerNameIgnoreCase(String name);

	/**
	 * Find cost owners with pagination.
	 *
	 * @param pageRequest The page request for pagination.
	 * @return the page of cost owners.
	 */
	List<CostOwner> findAllBy(Pageable pageRequest);

	/**
	 * Finds CostOwner with the highest ID.
	 *
	 * @return CostOwner with the highest ID.
	 */
	CostOwner findTop1ByOrderByCostOwnerIdDesc();

	/**
	 * Find by topToTop id.
	 * @param topToTopId the topToTop id.
	 * @return the page of cost owners.
	 */
	CostOwner findFirstByTopToTopId(Integer topToTopId);
}
