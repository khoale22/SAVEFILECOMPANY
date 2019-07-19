/*
 * ClassCommodityRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA Repository for ClassCommodity.
 *
 * @author d116773
 * @since 2.0.2
 */
public interface ClassCommodityRepository extends JpaRepository<ClassCommodity, ClassCommodityKey> {

	/**
	 * Returns a page of requested class commodities where commodity code is not the parameter commodityCodeNot.
	 * This is mainly used where the user wants a list of commodities, not classes, which would have a commodity code
	 * that was not zero.
	 *
	 * @param commodityCodeNot The commodity code to not include in search.
	 * @param pageRequest The page requested.
	 * @return a page of classCommodity based on request.
	 */
	Page<ClassCommodity> findAllByKeyCommodityCodeNot(Integer commodityCodeNot, Pageable pageRequest);

	/**
	 * Returns the list of commodities that exist that are part of a user's search criteria in their search session.
	 *
	 * @param sessionId The ID of the user's session.
	 * @return A list of commodities that match the list the user has provided.
	 */
	@Query(value = "select classCommodity.key.commodityCode from ClassCommodity classCommodity " +
			"where exists (select searchCriteria.commodityCode " +
			"from SearchCriteria searchCriteria " +
			"where classCommodity.key.commodityCode = searchCriteria.commodityCode and searchCriteria.sessionId = :sessionId)")
	List<Integer> findAllForSearch(@Param("sessionId") String sessionId);

	/**
	 * Find all commodities belong to bdm by bdm code.
	 *
	 * @param bdmCode bdm code.
	 * @return List of Commodity belong to bdm.
	 */
	List<ClassCommodity> findByBdmCode (String bdmCode);

	/**
	 * Find class commodity by class code and commodity code.
	 *
	 * @param classCode     the class code
	 * @param commodityCode the commodity code
	 * @return ClassCommodity
	 */
	ClassCommodity findFirstByKeyClassCodeAndKeyCommodityCode(int classCode, int commodityCode);
}
