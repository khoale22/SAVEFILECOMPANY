/*
 *  NutrientAuditRepository
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.NutrientAudit;
import com.heb.pm.entity.NutrientAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * JPA repository for Nutrient Audit.
 *
 * @author vn70633
 * @since 2.15.0
 */
public interface NutrientAuditRepository extends JpaRepository<NutrientAudit, NutrientAuditKey> {
    /**
     * Find by nutrient code list.
     *
     * @param scnCdId the scan code
     * @return the list
     */
        @Query("select key.upc, action, changedBy, max(lastUpdateTs) as lastUpdateTs, max(key.changedOn) as changedOn from NutrientAudit where key.sourceSystem = 13 and key.upc = :scnCdId group by key.upc, action, changedBy, lastUpdateTs")
        List<Object[]> getPublishedAttributesNutrientAudit(@Param(value = "scnCdId")Long scnCdId);
}

