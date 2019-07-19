/*
 *  CostOwnerCommon
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

/**
 * Repository to retrieve information about CostOwnerCommon.
 *
 * @author vn87351
 * @since 2.17.0
 */
public interface CostOwnerCommon {

	/**
	 * SQL statement that filter cost owner by its description and id.
	 */
	String FIND_COST_OWNER_BY_ID_AND_NAME_SQL = "from CostOwner costOwner " +
			"where costOwner.costOwnerId like concat('%', :costOwnerId, '%') " +
			"and upper(costOwner.costOwnerName) like concat('%', upper(:costOwnerName), '%')";

	/**
	 * SQL statement that filter cost owner by its id.
	 */
	String FIND_COST_OWNER_BY_ID = "from CostOwner costOwner " +
			"where costOwner.costOwnerId like concat('%', :costOwnerId, '%')";
}
