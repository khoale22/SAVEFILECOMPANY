/*
 *  WicSubCategoryRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.WicSubCategory;
import com.heb.pm.entity.WicSubCategoryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about wic sub categories.
 *
 * @author vn70529
 * @since 2.12
 */
public interface WicSubCategoryRepository extends JpaRepository<WicSubCategory, WicSubCategoryKey> {

	/**
	 * Returns a list of all wic categories ordered by id.
	 *
	 * @return a list of all wic categories ordered by id.
	 */
	List<WicSubCategory> findAllByOrderByWicCategoryIdAsc();

}
