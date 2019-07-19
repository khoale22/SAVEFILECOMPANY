/*
 *  CostOwnerRepositoryWithCount
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CostOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository to retrieve information about query with count.
 *
 * @author vn70529
 * @since 2.41.0
 */
public interface CostOwnerRepositoryWithCount extends CostOwnerCommon, JpaRepository<CostOwner, Integer> {

	/**
	 * find by cost owner id and name contain
	 *
	 * @param costOwnerId   the cost owner id
	 * @param costOwnerName the cost owner name
	 * @param pageable      paging data
	 * @return page data
	 */
	@Query(FIND_COST_OWNER_BY_ID_AND_NAME_SQL)
	Page<CostOwner> findAllByCostOwnerIdAndCostOwnerName(@Param("costOwnerId") String costOwnerId,
	                                                     @Param("costOwnerName") String costOwnerName,
	                                                     Pageable pageable);

	/**
	 * Find by cost owner id.
	 *
	 * @param costOwnerId The cost owner id.
	 * @param pageable    The page request for pagination.
	 * @return the page of cost owners.
	 */
	@Query(FIND_COST_OWNER_BY_ID)
	Page<CostOwner> findByCostOwnerId(@Param("costOwnerId") String costOwnerId,
	                                  Pageable pageable);

	/**
	 * Find all cost owners filter by cost owner name.
	 *
	 * @param name        The name to search.
	 * @param pageRequest The page request for pagination.
	 * @return the page of cost owners.
	 */
	Page<CostOwner> findByCostOwnerNameIgnoreCaseContaining(String name, Pageable pageRequest);
}
