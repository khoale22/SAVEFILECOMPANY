/*
 * ProductPreferredUnitOfMeasureAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductPreferredUnitOfMeasureAudit;
import com.heb.pm.entity.ProductPreferredUnitOfMeasureAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for product preferred unit of measure audit.
 *
 * @author vn70529
 * @since 2.18.4
 */
public interface ProductPreferredUnitOfMeasureAuditRepository extends JpaRepository<ProductPreferredUnitOfMeasureAudit, ProductPreferredUnitOfMeasureAuditKey> {

}

