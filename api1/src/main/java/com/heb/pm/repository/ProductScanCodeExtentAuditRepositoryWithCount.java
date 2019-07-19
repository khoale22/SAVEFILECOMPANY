/*
 *  ProductScanCodeExtentAuditRepositoryWithCount
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanCodeExtentAudit;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Repository for Product Scan Code Extent Audit with count.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface ProductScanCodeExtentAuditRepositoryWithCount extends ProductScanCodeExtentAuditRepository {

    /**
     * Get the product scan code extent by scan code id and key product extent data code.
     *
     * @param scanCodeId      the scan code id to find.
     * @param prodExtDataCode the product extent data code to find.
     * @return the list of ProductScanCodeExtentAudit.
     */
    List<ProductScanCodeExtentAudit> findByKey_ScanCodeIdAndProdExtDataCodeOrderByProdExtDataCodeDescKey_ChangedOnAsc(Long scanCodeId, String prodExtDataCode, Pageable page);

    /**
     * Count by scan code id and key product extent data code.
     *
     * @param scanCodeId      the scan code id.
     * @param prodExtDataCode the product extent data code.
     * @return the int
     */
    int countAllByKey_ScanCodeIdAndProdExtDataCode(Long scanCodeId, String prodExtDataCode);

}
