/*
 *  AlertUserResponseService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.alert;

import com.heb.pm.entity.AlertUserResponse;
import com.heb.pm.entity.AlertUserResponseKey;
import com.heb.pm.repository.AlertUserResponseRepository;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serves queries and updates related to Alert Comments (Notes).
 *
 * @author vn40486
 * @since 2.16.0
 */
@Service
public class AlertUserResponseService {
    private static final Logger logger = LoggerFactory.getLogger(AlertUserResponseService.class);

    @Autowired
    private AlertUserResponseRepository alertUserResponseRepository;
    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    /**
     * Fetches the Alert user response record pertaining to a specific user id and task/alert id.
     * @param alertID alert or task id.
     * @param userId user id.
     * @return alert user response.
     */
    @Transactional("jpaTransactionManager")
    public AlertUserResponse findOne(Integer alertID, String userId) {
        AlertUserResponseKey key = new AlertUserResponseKey(alertID, userId.toUpperCase());
        return this.alertUserResponseRepository.findOne(key);
    }
}
