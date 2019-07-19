/*
 * SubCommodityStateWarningAuditRepositoryWithCounts
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.SubCommodityStateWarningAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA repository for sub-commodity state warning audit entity with count.
 * @author vn70529
 * @since 2.18.4
 */
public interface SubCommodityStateWarningAuditRepositoryWithCounts extends SubCommodityStateWarningAuditRepository{
    /**
     * Find all SubCommodityStateWarningAudit by subCommodityCode
     * @param subCommodityCode the code of SubCommodity.
     * @return List<SubCommodityStateWarningAudit>
     */
    Page<SubCommodityStateWarningAudit> findByKeySubCommodityCodeOrderByKeyChangedOnAsc(Integer subCommodityCode, Pageable pageRequest);

    /**
     * Count all SubCommodityStateWarningAudit by sub commodity code.
     *
     * @param subCommodityCode the code of SubCommodity
     * @return the int
     */
    int countAllByKeySubCommodityCode(Integer subCommodityCode);
}
