/*
 * NutrientStatementPanelHeaderRepositoryCommon
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

/**
 * Common constants for the count and non-count NutrientStatementPanelHeader JPA repository implementations.
 *
 * @author vn70529
 * @since 2.22.0
 */
public interface NutrientStatementPanelHeaderRepositoryCommon {
    /**
     * The query search nutrient statement panel header by source system id and statement maintenance switch order by source system reference id.
     */
    String SEARCH_NUTRIENT_STATEMENT_PANEL_HEADER = "SELECT ntrnPanHdr FROM NutrientStatementPanelHeader ntrnPanHdr" +
            " WHERE ntrnPanHdr.sourceSystemId = (:sourceSystemId) AND ntrnPanHdr.statementMaintenanceSwitch = (:statementMaintenanceSwitch)" +
            " ORDER BY CAST(ntrnPanHdr.sourceSystemReferenceId AS int) ASC";

    /**
     * The query search nutrient statement panel header by source system reference id, source system id and statement maintenance switch order by source system reference id.
     */
    String SEARCH_NUTRIENT_STATEMENT_PANEL_HEADER_BY_SOURCE_SYSTEM_REFERENCE_ID = "SELECT ntrnPanHdr FROM NutrientStatementPanelHeader ntrnPanHdr" +
            " WHERE ntrnPanHdr.sourceSystemReferenceId IN (:statementIds) AND ntrnPanHdr.sourceSystemId = (:sourceSystemId)" +
            " AND ntrnPanHdr.statementMaintenanceSwitch = (:statementMaintenanceSwitch)" +
            " ORDER BY CAST(ntrnPanHdr.sourceSystemReferenceId AS int) ASC";
}
