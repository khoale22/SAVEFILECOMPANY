/*
 *  CustomerProductGroupRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about customer product group.
 *
 * @author vn87351
 * @since 2.14.0
 */
public interface CustomerProductGroupRepository extends JpaRepository<CustomerProductGroup, Long> {

	/**
	 * Finds a list of customer product groups that do not contain the given ids.
	 *
	 * @param ids List of customer product group ids to exclude.
	 * @return List of customer product groups matching the search criteria.
	 */
	List<CustomerProductGroup> findByCustProductGroupIdNotInOrderByCustProductGroupId(List<Long> ids);
}
