/*
 *  MasterDataExtensionAuditRepositoryWithCount
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.MasterDataExtensionAudit;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * JPA repository for Master Data Extension Audit entity with count.
 *
 * @author vn70529
 * @since 2.15.0
 */
public interface MasterDataExtensionAuditRepositoryWithCount extends MasterDataExtensionAuditRepository{

    /**
     * Get the list of MasterDataExtensionAudit by keyId and dataSourceSystem, attributeId.
     *
     * @param keyId               the keyId is productId or upc to find.
     * @param dataSourceSystem the data source system.
     * @param attributeId      the attributeId to find.
     * @return the list of MasterDataExtensionAudit.
     */
    List<MasterDataExtensionAudit> findAllByKey_KeyIdAndKey_DataSourceSystemAndKey_AttributeIdOrderByKey_AttributeIdDescKey_ChangedOnAsc(Long keyId, Long dataSourceSystem, Long attributeId, Pageable pag);

    /**
     * Count by keyId and dataSourceSystem, attributeId.
     *
     * @param keyId               the keyId is productId or upc to find.
     * @param dataSourceSystem the data source system.
     * @param attributeId      the attributeId to find.
     * @return the int
     */
    int countAllByKey_KeyIdAndKey_DataSourceSystemAndKey_AttributeId(Long keyId, Long dataSourceSystem, Long attributeId);

}
