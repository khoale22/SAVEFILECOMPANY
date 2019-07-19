/*
 *  ProductPkVarAuditRepository
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductPkVarAudit;
import com.heb.pm.entity.ProductPkVarAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for Product Pk Var Audit.
 *
 * @author vn70633
 * @since 2.15.0
 */
public interface ProductPkVarAuditRepository extends JpaRepository<ProductPkVarAudit, ProductPkVarAuditKey> {
    /**
     * Find by nutrient code list.
     *
     * @param scnCdId the scan code
     * @return the list
     */
    @Query("select key.upc, action, changedBy, max(lastUpdateTs) as lastUpdateTs, max(key.changedOn) as changedOn from ProductPkVarAudit where key.sourceSystem = 13 and key.upc = :scnCdId group by key.upc, action, changedBy, lastUpdateTs")
    List<Object[]>  getPublishedAttributesProductPkVarAudit(@Param(value = "scnCdId")Long scnCdId);

}

