/*
 * PLUMaintenanceServiceClient
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.ws;

import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalert_request.InsertAlertRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealert_request.UpdateAlertRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Provides access to service endpoint for PLU maintenance service.
 *
 * @author vn70529
 * @since 2.20.0
 */
@Service
public class PLUMaintenanceServiceClient {

    private static final String ERROR_WEB_SERVICE_UPDATE_RESPONSE = "Error while updating Alert: %s.";

    private static final String STRING_NO = "N";
    private static final String STRING_ACTIV = "ACTIV";
    private static final String STRING_PRUPD = "PRUPD";
    private static final String STRING_PMEBM = "PMEBM";
    private static final String DEFAULT_ALERT_FOR_INGREDIENT_STATEMENT = "01674";
    private static final String DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT = "01679";
    private static final BigInteger DEFAULT_NEW_ALERT_ALERT_ID = BigInteger.ZERO;

    @Autowired
    private ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    /**
     * Create alerts for nutrition and ingredients statement attributes when maintenance Scale Upc.
     * @param alertKey the alert key.
     * @param assignedUser the assigned user.
     * @param currentUserId the user maintenance Scale Upc.
     */
    public void createAlertForPLUMaintenance(String alertKey, String assignedUser,
                                               String currentUserId){
        InsertAlertRequest request = new InsertAlertRequest();
        request.setALERTID(DEFAULT_NEW_ALERT_ALERT_ID);
        request.setALERTKEY(alertKey);
        String alertDataTxt = new StringBuilder(DEFAULT_ALERT_FOR_INGREDIENT_STATEMENT).append(",").append(DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT).toString();
        request.setALERTDATATEXT(alertDataTxt);
        request.setALERTSTATUSCODE(STRING_ACTIV);
        request.setALERTTYPECODE(STRING_PRUPD);
        request.setALERTHIDDENSWITCH(STRING_NO);
        request.setALERTCRITICALPERCENT(BigDecimal.ZERO);
        request.setASSIGNEDUSERID(assignedUser);
        request.setDELEGATEDBYUSERID(currentUserId);
        request.setUSERID(currentUserId);
        request.setUSERROLEID(STRING_PMEBM);
        this.applicationAlertStagingServiceClient.insertAlert(request);
    }

    /**
     * Update alerts for nutrition and ingredients statement attributes when maintenance Scale Upc.
     * @param alertID the alert id.
     * @param alertDataTxt the alert data.
     * @param currentUserId the user maintenance Scale Upc.
     */
    public void updateAlertForPLUMaintenance(Integer alertID, String
            alertDataTxt, String currentUserId){
        UpdateAlertRequest request = new UpdateAlertRequest();
        request.setALERTID(BigInteger.valueOf(alertID));
        if(alertDataTxt.indexOf(DEFAULT_ALERT_FOR_INGREDIENT_STATEMENT) == -1) {
            alertDataTxt = new StringBuilder(StringUtils.trim(alertDataTxt)).append(",").append(DEFAULT_ALERT_FOR_INGREDIENT_STATEMENT).toString();
        }
        if(alertDataTxt.indexOf(DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT) == -1) {
            alertDataTxt = new StringBuilder(StringUtils.trim(alertDataTxt)).append(",").append(DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT).toString();
        }
        request.setALERTDATATEXT(alertDataTxt);
        request.setDELEGATEDBYUSERID(currentUserId);
        try {
            this.applicationAlertStagingServiceClient.updateAlert(request);
        } catch (CheckedSoapException e) {
            throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
        }
    }
}
