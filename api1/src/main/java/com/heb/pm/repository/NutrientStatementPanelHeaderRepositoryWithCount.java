/*
 * NutrientStatementPanelHeaderRepositoryWithCount
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementPanelHeader;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for NutrientStatementPanelHeader
 *
 * @author vn7033
 * @since 2.20.0
 */
public interface NutrientStatementPanelHeaderRepositoryWithCount  extends JpaRepository<NutrientStatementPanelHeader, Long>, NutrientStatementPanelHeaderRepositoryCommon {

    /**
     * Find nutrient statement panel header by source system reference id, source system id and statement maintenance switch order by source system reference id.
     *
     * @param statementIds the statement ids
     * @param sourceSystemId the source system id
     * @param statementMaintenanceSwitch the active status
     * @param pageRequest  the page request
     * @return the page
     */
    @Query(value = SEARCH_NUTRIENT_STATEMENT_PANEL_HEADER_BY_SOURCE_SYSTEM_REFERENCE_ID)
    Page<NutrientStatementPanelHeader> findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitch(@Param(value = "statementIds")List<String> statementIds, @Param(value = "sourceSystemId")Long sourceSystemId, @Param(value = "statementMaintenanceSwitch")char statementMaintenanceSwitch, Pageable pageRequest);

    /**
     * Find nutrient statement panel header by source system id and statement maintenance switch order by source system reference id..
     *
     * @param statementMaintenanceSwitch the statement maintenance switch
     * @param sourceSystemId the source system id
     * @param pageRequest  the page request
     * @return the page
     */
    @Query(value = SEARCH_NUTRIENT_STATEMENT_PANEL_HEADER)
    Page<NutrientStatementPanelHeader> findBySourceSystemIdAndStatementMaintenanceSwitch(@Param(value = "sourceSystemId")Long sourceSystemId, @Param(value = "statementMaintenanceSwitch")char statementMaintenanceSwitch, Pageable pageRequest);
}

