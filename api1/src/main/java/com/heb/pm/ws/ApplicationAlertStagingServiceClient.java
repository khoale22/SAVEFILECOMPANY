/*
 * ApplicationAlertStagingServiceClient.java
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.ws;

import com.heb.pm.entity.AlertComment;
import com.heb.pm.entity.AlertCommentKey;
import com.heb.util.DateUtils;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.applicationalertstagingservice.ApplicationAlertStagingServicePortType;
import com.heb.xmlns.ei.applicationalertstagingservice.ApplicationAlertStagingServiceServiceagent;
import com.heb.xmlns.ei.applicationalertstagingservice.Fault;
import com.heb.xmlns.ei.applicationalertstagingservice.alertrecipient_reply.AlertRecipientReply;
import com.heb.xmlns.ei.applicationalertstagingservice.deletealertcomment_reply.DeleteAlertCommentReply;
import com.heb.xmlns.ei.applicationalertstagingservice.deletealertcomment_request.DeleteAlertCommentRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.deletealertrecipient_reply.DeleteAlertRecipientReply;
import com.heb.xmlns.ei.applicationalertstagingservice.deletealertrecipient_request.DeleteAlertRecipientRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalert_reply.InsertAlertReply;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalert_request.InsertAlertRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertcomment_reply.InsertAlertCommentReply;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertcomment_request.InsertAlertCommentRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertrecipient_request.InsertAlertRecipientRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertuserresp_reply.InsertAlertUserRespReply;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertuserresp_request.InsertAlertUserRespRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealert_reply.UpdateAlertReply;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealert_request.UpdateAlertRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealertcomment_reply.UpdateAlertCommentReply;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealertcomment_request.UpdateAlertCommentRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealertuserresp_reply.UpdateAlertUserRespReply;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealertuserresp_request.UpdateAlertUserRespRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Provides access to service endpoint for application alert staging service.
 *
 * @author vn70633
 * @since 2.7.0
 */
@Service
public class ApplicationAlertStagingServiceClient extends BaseWebServiceClient
        <ApplicationAlertStagingServiceServiceagent, ApplicationAlertStagingServicePortType>{

    private static final Logger logger = LoggerFactory.getLogger(ApplicationAlertStagingServiceClient.class);

    @Value("${applicationAlertStagingService.uri}")
    private String uri;

    // error messages
    private static final String ERROR_ALERT_RECIPIENT_ADD = "Error while updating Alert Recipient Response: %s.";
    private static final String ERROR_WEB_SERVICE_UPDATE_RESPONSE = "Error while updating Alert: %s.";
    private static final String ERROR_ALERT_COMMENT_INSERT = "Error while inserting Alert Comment: %s.";
    private static final String ERROR_ALERT_COMMENT_UPDATE = "Error while updating Alert Comment: %s.";
    private static final String ERROR_ALERT_COMMENT_DELETE = "Error while deleting Alert Comment: %s.";
    private static final String ERROR_ALERT_USER_RESP_INSERT = "Error while inserting Alert User Response: %s.";
    private static final String ERROR_ALERT_USER_RESP_UPDATE = "Error while updating Alert User Response: %s.";
    private static final String ERROR_ALERT_USER_RESP_DELETE = "Error while deleting Alert User Response: %s.";

    @Autowired
    private UserInfo userInfo;
    /**
     * Return the service agent for this client.
     *
     * @return ApplicationAlertStagingServiceServiceagent associated with this client.
     */
    @Override
    protected ApplicationAlertStagingServiceServiceagent getServiceAgent() {
        try {
            URL url = new URL(this.getWebServiceUri());
            return new ApplicationAlertStagingServiceServiceagent(url);
        } catch (MalformedURLException e) {
            ApplicationAlertStagingServiceClient.logger.error(e.getMessage());
        }
        return new ApplicationAlertStagingServiceServiceagent();
    }

    /**
     * Return the port type for this client.
     *
     * @param agent The agent to use to create the port.
     * @return ApplicationAlertStagingServicePortType associated with this client.
     */
    @Override
    protected ApplicationAlertStagingServicePortType getServicePort(ApplicationAlertStagingServiceServiceagent agent) {
        return agent.getApplicationAlertStagingService();
    }

    /**
     * Return the url for this client.
     *
     * @return String url for this client.
     */
    @Override
    protected String getWebServiceUri() {
        return this.uri;
    }

    /**
     * Sets the URI to access application alert staging service. This is primarily used for testing.
     *
     * @param uri The URI to access application alert staging service.
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Sets the UserInfo for this object to use. This is for testing.
     *
     * @param userInfo The UserInfo for this object to use.
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Submits an updateAlertRequest.
     * @param request Update Alert Request.
     * @throws SoapException
     */
    public UpdateAlertReply updateAlert(UpdateAlertRequest request) throws CheckedSoapException {
        request.setAuthentication(this.getAuthentication());
        try {
            UpdateAlertReply reply = this.getPort().updateAlert(request);
            return reply;
        } catch (Fault f) {
            throw new CheckedSoapException(formatErrorMessage(f.getFaultInfo(), ERROR_WEB_SERVICE_UPDATE_RESPONSE, f.getMessage()));
        } catch (Exception e) {
            throw new CheckedSoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
        }
    }

    /**
     * Submits an insertAlertRequest.
     * @param request Insert Alert Request.
     * @throws SoapException
     */
    public InsertAlertReply insertAlert(InsertAlertRequest request) {
        request.setAuthentication(this.getAuthentication());
        try{
            InsertAlertReply reply = this.getPort().insertAlert(request);
            return reply;
        } catch (Exception e) {
            throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
        }
    }

    /**
     * Submits an insertAlertRecipientRequest.
     * @param request Insert Alert Recipient Request.
     * @throws SoapException
     */
    public AlertRecipientReply insertAlertRecipient(InsertAlertRecipientRequest request){
        request.setAuthentication(this.getAuthentication());
        try {
            AlertRecipientReply reply = this.getPort().insertAlertRecipient(request);
            return reply;
        } catch (Exception e) {
            throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
        }
    }

    /**
     * Create new alert when change nutrition statement.
     * @param alertKey alert key.
     * @param eBM assignedUserId.
     * @return BigInteger Alert ID.
     */
    public long insertAlertForUpdateNutrient(String alertKey, String eBM){
        return this.insertAlert(AlertStagingServiceHelper.createAlertRequestForUpdateNutrient(alertKey, eBM,
                userInfo.getUserId())).getALERTID().longValue();
    }

    /**
     * Create new alert recipient when change nutrition statement.
     * @param alertID alert ID.
     * @param recipientId RecipientID.
     */
    public void insertAlertRecipientForUpdateNutrient(BigInteger alertID, String recipientId){
        this.insertAlertRecipient(AlertStagingServiceHelper.createAlertRecipientRequestForUpdateNutrient(
                alertID,recipientId));
    }

    /**
     * Used to create a new alert / task with the given tracking id, task name and user id.
     * @param trackingId tracking id(ps_work_rqst) of the task.
     * @param taskName task description.
     * @param userId user info.
     * @return created task/alert id.
     */
    public BigInteger insertAlertForEcommerceTask(Long trackingId, String taskName, String userId){
        //Create and insert Alert Request
        InsertAlertRequest insertAlertRequest
                = AlertStagingServiceHelper.createAlertRequestForEcommerceTask(trackingId, taskName,userId);
        InsertAlertReply alertReply = this.insertAlert(insertAlertRequest);
        //Create and insert Alert Recipient
        InsertAlertRecipientRequest alertRecipientRequest
                = AlertStagingServiceHelper.createInsertAlertRecipientRequestForEcommerceTask(
                        alertReply.getALERTID(), userId);
        this.insertAlertRecipient(alertRecipientRequest);
        return alertReply.getALERTID();
    }

    /**
     * Update alert when change nutrition statement.
     * @param alertID
     * @param alertDataTxt
     */
    public void updateAlertForUpdateNutrient(Integer alertID, String alertDataTxt){
        try {
            this.updateAlert(AlertStagingServiceHelper.createProductUpdateAlertRequestForUpdateNutrient(
                    alertID,alertDataTxt, userInfo.getUserId()));
        } catch (CheckedSoapException e) {
            throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
        }
    }

    /**
     * Handles updating alert status.
     * @param alertID alert or task id.
     * @param alertStatusCode alert status. Active/Closed.
     * @throws CheckedSoapException throws exception in case of issues while executing the update request.
     */
    public void updateAlert(Integer alertID, String alertStatusCode) throws CheckedSoapException {
        UpdateAlertRequest updateAlertRequest = new UpdateAlertRequest();
        updateAlertRequest.setALERTID(BigInteger.valueOf(alertID));
        updateAlertRequest.setALERTSTATUSCODE(alertStatusCode);
        this.updateAlert(updateAlertRequest);
    }

    /**
     * Handles preparing and inserting an alert comment of a task/alert.
     * @param comment comment to be saved.
     * @return saved alert comment.
     */
    public AlertComment insertAlertComment(AlertComment comment){
        //Create and insert Alert Comment Request
        InsertAlertCommentRequest alertCommentRequest = new InsertAlertCommentRequest();
        alertCommentRequest.setALRTID(BigInteger.valueOf(comment.getKey().getAlertID()));
        alertCommentRequest.setSEQNBR(BigDecimal.ZERO);
        alertCommentRequest.setCMTTXT(comment.getComment());
        alertCommentRequest.setCRE8ID(comment.getCreateUserID());
        alertCommentRequest.setAuthentication(this.getAuthentication());
        AlertComment savedComment = null;
        try {
            InsertAlertCommentReply reply = this.getPort().insertAlertComment(alertCommentRequest);
            if (reply.getSEQNBR() != null && reply.getSEQNBR().intValue() > 0) {
                AlertCommentKey key = new AlertCommentKey(reply.getALRTID().intValue(), reply.getSEQNBR().intValue());
                savedComment = new AlertComment();
                savedComment.setComment(reply.getCMTTXT());
                savedComment.setCreateUserID(reply.getCRE8ID());
                savedComment.setCreateTime(DateUtils.toLocalDateTime(reply.getCRE8TS(), DateUtils.DATE_TIME_FORMAT_01));
            }
        } catch (Exception e) {
            throw new SoapException(String.format(ERROR_ALERT_COMMENT_INSERT, e.getMessage()));
        }
        return savedComment;
    }

    /**
     * Handles preparing and deleting an alert comment of a task/alert.
     * @param comment comment to be deleted.
     * @return deleted alert comment.
     */
    public AlertComment deleteAlertComment(AlertComment comment){
        DeleteAlertCommentRequest alertCommentRequest = new DeleteAlertCommentRequest();
        alertCommentRequest.setALRTID(BigInteger.valueOf(comment.getKey().getAlertID()));
        alertCommentRequest.setSEQNBR(BigDecimal.valueOf(comment.getKey().getSequenceNumber()));
        alertCommentRequest.setAuthentication(this.getAuthentication());
        try {
            DeleteAlertCommentReply reply = this.getPort().deleteAlertComment(alertCommentRequest);
        } catch (Exception e) {
            throw new SoapException(String.format(ERROR_ALERT_COMMENT_DELETE, e.getMessage()));
        }
        return comment;
    }

    /**
     * Handles preparing and updating an alert comment of a task/alert.
     * @param comment comment to be updated.
     * @return updated alert comment.
     */
    public AlertComment updateAlertComment(AlertComment comment){
        //Update Alert Comment Request
        UpdateAlertCommentRequest alertCommentRequest = new UpdateAlertCommentRequest();
        alertCommentRequest.setALRTID(BigInteger.valueOf(comment.getKey().getAlertID()));
        alertCommentRequest.setSEQNBR(BigDecimal.valueOf(comment.getKey().getSequenceNumber()));
        alertCommentRequest.setCMTTXT(comment.getComment());
        alertCommentRequest.setCRE8ID(comment.getCreateUserID());
        alertCommentRequest.setCRE8TS(LocalDateTime.now().toString());
        alertCommentRequest.setAuthentication(this.getAuthentication());
        AlertComment updatedComment = null;
        try {
            UpdateAlertCommentReply reply = this.getPort().updateAlertComment(alertCommentRequest);
            if (reply.getSEQNBR() != null && reply.getSEQNBR().intValue() > 0) {
                updatedComment = new AlertComment();
                updatedComment.setComment(reply.getCMTTXT());
                updatedComment.setCreateUserID(reply.getCRE8ID());
                updatedComment.setCreateTime(DateUtils.toLocalDateTime(reply.getCRE8TS(), DateUtils.DATE_TIME_FORMAT_01));
            }
        } catch (Exception e) {
            throw new SoapException(String.format(ERROR_ALERT_COMMENT_UPDATE, e.getMessage()));
        }
        return updatedComment;
    }

    /**
     * Submits an updateAlertRequest.
     * @param request Update Alert Request.
     * @throws SoapException
     */
    public UpdateAlertUserRespReply updateAlertUserResponse(UpdateAlertUserRespRequest request) throws CheckedSoapException {
        request.setAuthentication(this.getAuthentication());
        try {
            UpdateAlertUserRespReply reply = this.getPort().updateAlertUserResp(request);
            return reply;
        } catch (Fault f) {
            throw new CheckedSoapException(formatErrorMessage(f.getFaultInfo(), ERROR_ALERT_USER_RESP_UPDATE, f.getMessage()));
        } catch (Exception e) {
            throw new CheckedSoapException(String.format(ERROR_ALERT_USER_RESP_UPDATE, e.getMessage()));
        }
    }

    /**
     * Submits an update to the alert user response.
     * @param request Update Alert user response Request.
     * @throws SoapException
     */
    public InsertAlertUserRespReply insertAlertUserResponse(InsertAlertUserRespRequest request) throws CheckedSoapException {
        request.setAuthentication(this.getAuthentication());
        try {
            InsertAlertUserRespReply reply = this.getPort().insertAlertUserResp(request);
            return reply;
        } catch (Fault f) {
            logger.error(String.format(ERROR_ALERT_USER_RESP_INSERT,f.getFaultInfo().getFaultString()),f.getMessage());
            throw new CheckedSoapException(formatErrorMessage(f.getFaultInfo(), ERROR_ALERT_USER_RESP_INSERT, f.getMessage()));
        } catch (Exception e) {
            logger.error(String.format(ERROR_ALERT_USER_RESP_INSERT,e.getMessage()));
            throw new CheckedSoapException(String.format(ERROR_ALERT_USER_RESP_INSERT, e.getMessage()));
        }
    }
	/**
	 * Submits an deleteAlertRecipientRequest.
	 * @throws SoapException
	 */
	public DeleteAlertRecipientReply deleteAlertRecipient(int alertId,String userId)throws CheckedSoapException{
		return deleteAlertRecipient(AlertStagingServiceHelper.createDeleteAlertRecipientRequest(alertId,userId));
	}
    /**
     * Submits an deleteAlertRecipientRequest.
     * @param request delete Alert Recipient Request.
     * @throws SoapException
     */
    public DeleteAlertRecipientReply deleteAlertRecipient(DeleteAlertRecipientRequest request){
        request.setAuthentication(this.getAuthentication());
        try {
            DeleteAlertRecipientReply reply = this.getPort().deleteAlertRecipient(request);
            return reply;
        } catch (Exception e) {
            logger.error(String.format(ERROR_ALERT_USER_RESP_DELETE,e.getMessage()));
            throw new SoapException(String.format(ERROR_WEB_SERVICE_UPDATE_RESPONSE, e.getMessage()));
        }
    }
}