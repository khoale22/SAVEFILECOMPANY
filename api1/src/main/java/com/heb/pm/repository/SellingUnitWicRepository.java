/*
 *  SellingUnitWicRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.SellingUnitWic;
import com.heb.pm.entity.SellingUnitWicKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * This is the repository for the selling unit wic repository.
 *
 * @author l730832
 * @since 2.12.0
 */
public interface SellingUnitWicRepository extends JpaRepository<SellingUnitWic, SellingUnitWicKey> {

	/**
	 * Finds a list of selling unit wics by the wic approved product list and then orders them by the upc.
	 *
	 * @param wicAplId The wic apl id to search on.
	 * @return list of selling unit wics.
	 */
	List<SellingUnitWic> findByKeyWicApprovedProductListIdOrderByKeyUpc(Long wicAplId);

	/**
	 * Finds a list of selling unit wics by the upc.
	 *
	 * @param upc the upc to search on.
	 * @return list of selling unit wics
	 */
	List<SellingUnitWic> findByKeyUpc(Long upc);

	/**
	 * Finds a list of selling unit wics by the wic category id and the wic sub category id.
	 *
	 * @param wicCategoryId The wic category Id.
	 * @param wicSubcategoryId The wic sub category Id.
	 * @return list of selling unit wics
	 */
	List<SellingUnitWic> findByKeyWicCategoryIdAndKeyWicSubCategoryIdOrderByKeyUpc(Long wicCategoryId, Long wicSubcategoryId);
}
