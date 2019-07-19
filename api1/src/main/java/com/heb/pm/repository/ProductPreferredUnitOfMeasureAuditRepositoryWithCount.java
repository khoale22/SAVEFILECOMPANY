/*
 * ProductPreferredUnitOfMeasureAuditRepositoryWithCount
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductPreferredUnitOfMeasureAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * JPA repository for product preferred unit of measure audit entity with count.
 * @author vn70529
 * @since 2.18.4
 */
public interface ProductPreferredUnitOfMeasureAuditRepositoryWithCount extends ProductPreferredUnitOfMeasureAuditRepository{
    /**
     * Find ProductPreferredUnitOfMeasureAudit by subCommodityCode
     * @param subCommodityCode the code of SubCommodity
     * @return List<ProductPreferredUnitOfMeasureAudit>
     */
    Page<ProductPreferredUnitOfMeasureAudit> findByKeySubCommodityCodeOrderByRetailUnitOfMeasureCodeAscKeyChangedOnAsc(Integer subCommodityCode, Pageable pageRequest);

    /**
     * Count all ProductPreferredUnitOfMeasureAudit by sub commodity code.
     *
     * @param subCommodityCode the code of SubCommodity
     * @return the int
     */
    int countAllByKeySubCommodityCode(Integer subCommodityCode);
}
