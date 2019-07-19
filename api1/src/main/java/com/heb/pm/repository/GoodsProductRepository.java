/*
 * GoodsProductRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.GoodsProduct;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for GoodsProduct.
 *
 * @author vn73545
 * @since 2.0.6
 */
public interface GoodsProductRepository extends JpaRepository<GoodsProduct, Long> {
}
