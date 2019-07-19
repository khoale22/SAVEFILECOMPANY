/*
 *  AlertCommentService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.alert;

import com.heb.pm.entity.AlertComment;
import com.heb.pm.repository.AlertCommentRepository;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serves queries and updates related to Alert Comments (Notes).
 *
 * @author vn40486
 * @since 2.16.0
 */
@Service
public class AlertCommentService {
    private static final Logger logger = LoggerFactory.getLogger(AlertCommentService.class);

    @Autowired
    private AlertCommentRepository alertCommentRepository;
    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    /**
     * Insert new alert when update nutrient statement.
     * @param comment alert comment to be saved.
     */
    public AlertComment insertComment(AlertComment comment){
        return this.applicationAlertStagingServiceClient.insertAlertComment(comment);
    }

    /**
     * Update alert when update nutrient statement.
     * @param comment alert comment to be updated.
     */
    public AlertComment updateComment(AlertComment comment) {
        return this.applicationAlertStagingServiceClient.updateAlertComment(comment);
    }

    /**
     * Delete alert when delete nutrient statement.
     * @param comment alert comment to be deleted.
     */
    public void deleteComment(AlertComment comment) {
        this.applicationAlertStagingServiceClient.deleteAlertComment(comment);
    }

    /**
     * Fetches list of user notes or comments tagged to an alert / task id.
     * @param alertID alert or task id.
     * @return list of alert comments or notes.
     */
    @Transactional("jpaTransactionManager")
    public List<AlertComment> findByAlertId(Integer alertID) {
        return this.alertCommentRepository.findByKeyAlertIDOrderByCreateTimeDesc(alertID);
    }



}
