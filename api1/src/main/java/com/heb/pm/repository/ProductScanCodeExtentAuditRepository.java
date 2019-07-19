/*
 * ProductScanCodeExtentAuditRepository
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanCodeExtentAudit;
import com.heb.pm.entity.ProductScanCodeExtentAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Product Scan Code Extent Audit.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface ProductScanCodeExtentAuditRepository extends JpaRepository<ProductScanCodeExtentAudit, ProductScanCodeExtentAuditKey> {
}

