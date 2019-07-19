/*
 * ItemClassRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ItemClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for ItemClass.
 *
 * @author m314029
 * @since 2.4.0
 */
public interface ItemClassRepository extends JpaRepository<ItemClass, Integer> {

	/**
	 * Find itemClass by department id and sub department id.
	 *
	 * @param departmentId the department id
	 * @param subDepartmentId the sub department id
	 * @return the list
	 */
	List<ItemClass> findByDepartmentIdAndSubDepartmentId(Integer departmentId, String subDepartmentId);

	/**
	 * Returns the list of classes that exist that are part of a user's search criteria in their search session.
	 *
	 * @param sessionId The ID of the user's session.
	 * @return A list of classes that match the list the user has provided.
	 */
	@Query(value = "select itemClass.itemClassCode from ItemClass itemClass " +
			"where exists (select searchCriteria.classCode " +
			"from SearchCriteria searchCriteria " +
			"where itemClass.itemClassCode = searchCriteria.classCode and searchCriteria.sessionId = :sessionId)")
	List<Integer> findAllForSearch(@Param("sessionId") String sessionId);

	/**
	 * Find itemClass by sub commodity.
	 *
	 * @param subCom The Subcommodity.
	 * @return Item Class.
	 */
	@Query(value = "select itemClass from ItemClass itemClass " +
			"inner join itemClass.commodityList coms " +
			"inner join coms.subCommodityList subComs " +
			"where subComs.subCommodityCode = :subCom")
	ItemClass findBySubCommodity(@Param("subCom") Integer subCom);


}
