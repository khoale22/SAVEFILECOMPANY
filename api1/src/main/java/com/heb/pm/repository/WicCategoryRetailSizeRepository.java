/*
 *  WicCategoryRetailSizeRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.WicCategoryRetailSize;
import com.heb.pm.entity.WicCategoryRetailSizeKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This is the repository for wic category retail size.
 *
 * @author l730832
 * @since 2.12.0
 */
public interface WicCategoryRetailSizeRepository extends JpaRepository<WicCategoryRetailSize, WicCategoryRetailSizeKey> {

	/**
	 * Finds a wic category retail size by the category id and the sub category id.
	 * @param wicCategoryId
	 * @param wicSubCategoryId
	 * @return a wic category retail size.
	 */
	List<WicCategoryRetailSize> findByKeyWicCategoryIdAndKeyWicSubCategoryId(Long wicCategoryId, Long wicSubCategoryId);
}
