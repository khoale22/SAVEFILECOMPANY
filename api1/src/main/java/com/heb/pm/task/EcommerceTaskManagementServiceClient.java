/*
 * EcommerceTaskManagementServiceClient
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.AlertStaging;
import com.heb.pm.entity.AlertUserResponse;
import com.heb.pm.ws.ApplicationAlertStagingServiceClient;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.xmlns.ei.applicationalertstagingservice.deletealertrecipient_request.DeleteAlertRecipientRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertrecipient_request.InsertAlertRecipientRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertuserresp_request.InsertAlertUserRespRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealert_request.UpdateAlertRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealertuserresp_request.UpdateAlertUserRespRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * EcommerceTaskManagementServiceClient is used to prepare webservices request specific to ecommerce task and invoke
 * the respective management service client for executing the prepared request.
 *
 * @author vn40486
 * @since 2.16.0
 */
@Service
public class EcommerceTaskManagementServiceClient {

    @Autowired
    ApplicationAlertStagingServiceClient applicationAlertStagingServiceClient;

    /**
     * Used to prepares request for updating task(alert) description and status and sent it to the respective webservice
     * client to execute the request.
     * @param alertStaging alert or task object with modified description and status.
     * @throws CheckedSoapException throws CheckedSoapException in case of any error with executing the request.
     */
    protected void updateTask(AlertStaging alertStaging) throws CheckedSoapException {
        UpdateAlertRequest request = new UpdateAlertRequest();
        request.setALERTID(BigInteger.valueOf(alertStaging.getAlertID()));
        request.setALERTSTATUSCODE(alertStaging.getAlertStatusCD());
        request.setALERTDATATEXT(alertStaging.getAlertDataTxt());
        request.setUSERID(alertStaging.getAlertStatusUserId());
        request.setALERTHIDDENSWITCH(alertStaging.getAlertHideSw().toString());
        this.applicationAlertStagingServiceClient.updateAlert(request);
    }

    /**
     * Used to prepares request for updating alert user response and sent it to the respective webservice client to
     * execute the request.
     * @param alertUserResponse alert user response.
     * @throws CheckedSoapException throws CheckedSoapException in case of any error with executing the request.
     */
    protected void updateAlertUserResp(AlertUserResponse alertUserResponse) throws CheckedSoapException {
        UpdateAlertUserRespRequest request = new UpdateAlertUserRespRequest();
        request.setALRTID(BigInteger.valueOf(alertUserResponse.getKey().getAlertID()));
        request.setUSRID(alertUserResponse.getKey().getUserId());
        request.setALRTRESLCD(alertUserResponse.getAlertResolutionCode());
        this.applicationAlertStagingServiceClient.updateAlertUserResponse(request);
    }

    /**
     * Prepares new record for the given alert user response with necessary default values and sents it to the
     * respective webservice client to execute the request. .
     * @param alertStaging alert staging or task info object.
     * @throws CheckedSoapException throws CheckedSoapException in case of any error with executing the request.
     */
    protected void insertAlertUserResp(AlertStaging alertStaging) throws CheckedSoapException {
        InsertAlertUserRespRequest request = new InsertAlertUserRespRequest();
        request.setALRTID(BigInteger.valueOf(alertStaging.getAlertID()));
        request.setUSRID(alertStaging.getAlertStatusUserId());
        if(alertStaging.getAlertStatusCD().equals(AlertStaging.AlertStatusCD.CLOSE)) {
            request.setALRTRESLCD(AlertUserResponse.RESOLUTION_CODE_CLOSE);
        } else {
            request.setALRTRESLCD(AlertUserResponse.RESOLUTION_CODE_DEFAULT);
        }
        request.setALRTREADSW(AlertUserResponse.ALERT_READ_SW_NO);
        request.setHIDEALRTSW(AlertUserResponse.ALERT_HIDE_SW_NO);
        request.setUSRROLECD(AlertUserResponse.ROLE_CD_DEFAULT);
        this.applicationAlertStagingServiceClient.insertAlertUserResponse(request);
    }

    /**
     * Create the alert recipient.
     * @param alertId the alert id.
     * @param recipientId the recipient id.
     */
    public void createAlertRecipient(Integer alertId, String recipientId){
        InsertAlertRecipientRequest request = new InsertAlertRecipientRequest();
        request.setALRTID(BigInteger.valueOf(alertId));
        request.setRECIPIENTID(StringUtils.isNotBlank(recipientId)?recipientId.toUpperCase().trim(): null);
        request.setALRTRECPTYPCD(AlertStaging.ALERT_RECIPIENT_TYPE_USER);
        applicationAlertStagingServiceClient.insertAlertRecipient(request);
    }

    /**
     * Delete alert recipient.
     * @param alertId the alert id.
     * @param recipientId the recipientId.
     * @return response status.
     */
    public boolean deleteAlertRecipient(Integer alertId, String recipientId) {
        DeleteAlertRecipientRequest request = new DeleteAlertRecipientRequest();
        request.setALRTID(BigInteger.valueOf(alertId));
        request.setRECIPIENTID(StringUtils.isNotBlank(recipientId) ? recipientId.toUpperCase().trim() : null);
        request.setALRTRECPTYPCD(AlertStaging.ALERT_RECIPIENT_TYPE_USER);
        return applicationAlertStagingServiceClient.deleteAlertRecipient(request).isRESPONSESTATUS();
    }

}