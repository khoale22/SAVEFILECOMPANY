/*
 * ShippingRestrictionHierarchyLevelAuditRepositoryWithCounts
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ShippingRestrictionHierarchyLevelAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA repository for shipping restrictions for hierarchy levels Audit entity with count.
 * @author vn70529
 * @since 2.18.4
 */
public interface ShippingRestrictionHierarchyLevelAuditRepositoryWithCounts extends ShippingRestrictionHierarchyLevelAuditRepository{
    /**
     *  Find all ShippingRestrictionHierarchyLevelAudit by subCommodityCode
     * @param subCommodityCode the code of SubCommodity.
     * @return List<ShippingRestrictionHierarchyLevelAudit>
     */
    Page<ShippingRestrictionHierarchyLevelAudit> findByKeySubCommodityOrderByKeyRestrictionCodeAscKeyChangedOnAsc(Integer subCommodityCode, Pageable pageRequest);

    /**
     * Count all ShippingRestrictionHierarchyLevelAudit by sub commodity code.
     *
     * @param subCommodityCode the code of SubCommodity
     * @return the int
     */
    int countAllByKeySubCommodity(Integer subCommodityCode);
}
