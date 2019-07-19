/*
 * ProductTrashCanRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductTrashCan;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository for ProductTrashCan.
 *
 * @author vn70529
 * @since 2.12.0
 */
public interface ProductTrashCanRepository extends JpaRepository<ProductTrashCan, Long> {
}
