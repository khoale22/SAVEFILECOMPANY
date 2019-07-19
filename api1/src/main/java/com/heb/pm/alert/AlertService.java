/*
 *  AlertService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.alert;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.repository.AlertStagingRepository;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * Holds all business logic related for alert.
 *
 * @author vn70633
 * @since 2.7.0
 */
@Service
public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Autowired
    private AlertStagingRepository alertStagingRepository;

    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    /**
     * Insert new alert when update nutrient statement.
     * @param alertKey alertKey.
     * @param eBM eBM User Id.
     */
    public void createProductUpdateAlertForUpdateNutrient(String alertKey, String eBM){
        long alertID =  this.applicationAlertStagingServiceClient.insertAlertForUpdateNutrient(
                alertKey,eBM);
        this.applicationAlertStagingServiceClient.insertAlertRecipientForUpdateNutrient(BigInteger.valueOf(alertID), eBM);
    }

    /**
     * Update alert when update nutrient statement.
     * @param alertID alert id.
     * @param alertDataTxt product ID.
     */
    public void updateProductUpdateAlertForUpdateNutrient(Integer alertID, String alertDataTxt){
        this.applicationAlertStagingServiceClient.updateAlertForUpdateNutrient(alertID, alertDataTxt);
    }

    /**
     * Find alert staging by key alert key and status active.
     *
     * @param alertTypeCd the alert type.
     * @param alertStatus the alert status.
     * @param alertKey the alert key.
     * @return the alert staging.
     */
    @Transactional("jpaTransactionManager")
    public AlertStaging findByAlertTypeCDAndAlertStatusCDAndAlertKey(String alertTypeCd, String alertStatus, String alertKey) {
        return this.alertStagingRepository.findByAlertTypeCDAndAlertStatusCDAndAlertKey(alertTypeCd, alertStatus, alertKey);
    }

}
