/*
 *
 * NutrientStatementPanelHeaderRepository.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.repository;

import com.heb.pm.entity.NutrientStatementPanelHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for NutrientStatementPanelHeader.
 *
 * @author vn7033
 * @since 2.20.0
 */
public interface NutrientStatementPanelHeaderRepository extends JpaRepository<NutrientStatementPanelHeader, Long>, NutrientStatementPanelHeaderRepositoryCommon {

    /**
     * Find first nutrient statement panel header by source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id.
     * @param sourceSystemId the source system id.
     * @param statementMaintenanceSwitch the statement maintanance switch.
     * @return NutrientStatementPanelHeader
     */
    NutrientStatementPanelHeader findFirstBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(String sourceSystemReferenceId, Long sourceSystemId, char statementMaintenanceSwitch);

    /**
     * Find all by source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id.
     * @return the  List<NutrientStatementPanelHeader>
     */

    /**
     * Find all by source system reference id.
     *
     * @param sourceSystemReferenceId the source system reference id.
     * @param sourceSystemId the source system id.
     * @param statementMaintenanceSwitch the statementMaintenanceSwitch flag.
     * @return the  List<NutrientStatementPanelHeader>
     */
    List<NutrientStatementPanelHeader> findAllBySourceSystemReferenceIdAndSourceSystemIdAndStatementMaintenanceSwitch(String sourceSystemReferenceId, Long sourceSystemId, char statementMaintenanceSwitch);

    /**
     * Find nutrient statement panel header by source system reference id, source system id and statement maintenance switch order by source system reference id..
     *
     * @param statementIds the statement ids
     * @param sourceSystemId the source system id
     * @param statementMaintenanceSwitch the active status
     * @param pageRequest  the page request
     * @return the list
     */
    @Query(value = SEARCH_NUTRIENT_STATEMENT_PANEL_HEADER_BY_SOURCE_SYSTEM_REFERENCE_ID)
    List<NutrientStatementPanelHeader> findBySourceSystemReferenceIdInAndSourceSystemIdAndStatementMaintenanceSwitch(@Param(value = "statementIds")List<String> statementIds, @Param(value = "sourceSystemId")Long sourceSystemId, @Param(value = "statementMaintenanceSwitch")char statementMaintenanceSwitch, Pageable pageRequest);

    /**
     * Find nutrient statement panel header by source system id and statement maintenance switch order by source system reference id.
     *
     * @param sourceSystemId the source system id
     * @param statementMaintenanceSwitch the statement maintenance switch
     * @param pageRequest the page request
     * @return the list
     */
    @Query(value = SEARCH_NUTRIENT_STATEMENT_PANEL_HEADER)
    List<NutrientStatementPanelHeader> findBySourceSystemIdAndStatementMaintenanceSwitch(@Param(value = "sourceSystemId")Long sourceSystemId, @Param(value = "statementMaintenanceSwitch")char statementMaintenanceSwitch, Pageable pageRequest);

    /**
     * Find by Source System Reference Id, Statement Maintenance Switch Not, Source System Id.
     *
     * @param statementIds Source System Reference Id.
     * @param statementMaintenanceSwitch Statement Maintenance Switch Not.
     * @param sourceSystemId Source System Id.
     * @return List of Nutrient Statement Panel Header.
     */
    List<NutrientStatementPanelHeader> findBySourceSystemReferenceIdAndStatementMaintenanceSwitchNotAndSourceSystemId (String statementIds,char statementMaintenanceSwitch,Long sourceSystemId);

    /**
     * Find newest id in table NTRN_PAN_HDR.
     *
     * @return newest id in table NTRN_PAN_HDR.
     */
    @Query(value = "select max(nsph.nutrientPanelHeaderId) from NutrientStatementPanelHeader nsph")
    Long getLastestId();
}
