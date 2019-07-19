/*
 * AlertStagingRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.AlertStaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * JPA Repository for AlertStagingRepository.
 *
 * @author vn70633
 * @since 2.7.0
 */
public interface AlertStagingRepositoryWithCount extends JpaRepository<AlertStaging, Integer>{

    final String QUERY_ALL_ALERTS_BY_ASSIGNEE = "SELECT DISTINCT a FROM AlertStaging a  LEFT JOIN a.alertRecipients r" +
            " WHERE a.alertTypeCD = :alertTypeCd AND a.alertStatusCD <> 'DELTE'" +
            " AND (" +
            "   UPPER(TRIM(a.assignedUserID)) = UPPER(:assignedUserID) OR UPPER(TRIM(a.delegatedByUserID)) = UPPER(:assignedUserID) " +
            "   OR (UPPER(r.key.recipientId) = UPPER(:assignedUserID) AND r.key.alertRecipientTypeCode = 'USRID')" +
            " )  ORDER BY a.responseByDate desc";

    final String QUERY_ALERTS_BY_ALERT_TYPE_AND_STATUS_AND_ASSIGNEE = "SELECT DISTINCT a FROM AlertStaging a LEFT JOIN a.alertRecipients r  " +
            " WHERE a.alertTypeCD = :alertTypeCd AND a.alertStatusCD = :alertStatusCD " +
            " AND (" +
            "   UPPER(TRIM(a.assignedUserID)) = UPPER(:assignedUserID) OR UPPER(TRIM(a.delegatedByUserID)) = UPPER(:assignedUserID) " +
            "   OR (UPPER(r.key.recipientId) = UPPER(:assignedUserID) AND r.key.alertRecipientTypeCode = 'USRID')" +
            " )  ORDER BY a.responseByDate desc";

    /**
     * Find alert staging by alert type and status active.
     *
     * @param alertTypeCd the alert type.
     * @param alertStatus the alert status.
     * @return list of alert staging with pagination info.
     */
    Page<AlertStaging> findByAlertTypeCDAndAlertStatusCDOrderByResponseByDateAsc(
            String alertTypeCd, String alertStatus, Pageable pageRequest);

    /**
     * Find all alerts (active and closed) by alert type and assignee.
     * @param alertTypeCd alert type code.
     * @param assignedUserID assigned user id.
     * @param pageRequest pagination info.
     * @return list of alerts with pagination info.
     */
    @Query(value = QUERY_ALL_ALERTS_BY_ASSIGNEE)
    Page<AlertStaging> findAllAlertsByAssignee(@Param("alertTypeCd") String alertTypeCd,
                                               @Param("assignedUserID") String assignedUserID, Pageable pageRequest);

    /**
     * Find alerts (active or closed) by alert type, status and assignee.
     * @param alertTypeCd alert type code.
     * @param alertStatusCD alert status code.
     * @param pageRequest pagination info.
     * @return list of alerts with pagination info.
     */
    @Query(value = QUERY_ALERTS_BY_ALERT_TYPE_AND_STATUS_AND_ASSIGNEE)
    Page<AlertStaging> findAlertsByAlertTypeAndStatusAndAssignee(@Param("alertTypeCd") String alertTypeCd,
                                                                 @Param("alertStatusCD") String alertStatusCD,
                                                                 @Param("assignedUserID") String assignedUserID, Pageable pageRequest);


}
