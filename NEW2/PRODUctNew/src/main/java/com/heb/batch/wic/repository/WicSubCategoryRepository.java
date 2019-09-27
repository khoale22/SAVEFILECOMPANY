/*
 *  WicSubCategoryRepository
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of H-E-B.
 */

package com.heb.batch.wic.repository;

import com.heb.batch.wic.entity.WicSubCategory;
import com.heb.batch.wic.entity.WicSubCategoryKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is the repository for the Wic Sub Category Repository.
 *
 * @author vn55306
 * @since 1.0.0
 */
public interface WicSubCategoryRepository extends JpaRepository<WicSubCategory, WicSubCategoryKey> {

	}
