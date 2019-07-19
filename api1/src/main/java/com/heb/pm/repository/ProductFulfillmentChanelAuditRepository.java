/*
 * DynamicAttributeAuditRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.heb.pm.entity.ProductFulfillmentChanelAudit;
import com.heb.pm.entity.ProductFulfillmentChanelAuditKey;

/**
 * Repository for dynamic attributes audit entries.
 *
 */
public interface ProductFulfillmentChanelAuditRepository extends JpaRepository<ProductFulfillmentChanelAudit, ProductFulfillmentChanelAuditKey> {
    /**
     * This method returns all fulfillment attributes audits for a product
     * @param productId the product id
     * @return
     */
    List<ProductFulfillmentChanelAudit> findByKeyProductIdOrderByKeyChangedOn(long productId);
}

