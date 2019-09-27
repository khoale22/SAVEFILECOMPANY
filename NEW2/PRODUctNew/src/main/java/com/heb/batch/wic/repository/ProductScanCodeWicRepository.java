/*
 *  ProductScanCodeWicRepository
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of H-E-B.
 */

package com.heb.batch.wic.repository;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.entity.ProductScanCodeWicKey;
import com.heb.batch.wic.utils.WicConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * This is the repository for the product scan code wic Repository.
 *
 * @author vn55306
 * @since 1.0.0
 */
public interface ProductScanCodeWicRepository extends JpaRepository<ProductScanCodeWic, ProductScanCodeWicKey> {
	@Query(value = WicConstants.FIND_BY_CREATE_USER_SQL)
	Page<ProductScanCodeWic> findByCre8UId(@Param("cre8UId") String cre8UId, Pageable pageable);
}