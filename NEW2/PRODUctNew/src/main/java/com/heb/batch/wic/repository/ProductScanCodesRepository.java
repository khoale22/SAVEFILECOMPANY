/*
 * ProductScanCodesRepository
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.repository;
import com.heb.batch.wic.entity.ProductScanCodes;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository for product scan codes (UPC).
 *
 * @author vn55306
 * @since 1.0.0
 */
public interface ProductScanCodesRepository extends JpaRepository<ProductScanCodes, Long> {
}
