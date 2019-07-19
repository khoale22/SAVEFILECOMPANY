/*
 * AlertStagingServiceHelper
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB
 */

package com.heb.pm.ws;

import com.heb.pm.entity.AlertStaging;
import com.heb.xmlns.ei.applicationalertstagingservice.deletealertrecipient_request.DeleteAlertRecipientRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalert_request.InsertAlertRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.insertalertrecipient_request.InsertAlertRecipientRequest;
import com.heb.xmlns.ei.applicationalertstagingservice.updatealert_request.UpdateAlertRequest;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Create all request for Alert Service.
 *
 * @author vn70633
 * @since 2.7.0
 */
class AlertStagingServiceHelper {

	private static final String STRING_NO = "N";
	private static final String STRING_ACTIV = "ACTIV";
	private static final String STRING_PRUPD = "PRUPD";
	private static final String STRING_PMEBM = "PMEBM";
	private static final String STRING_USRID = "USRID";
	private static final String DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT = "01679";
	private static final BigInteger DEFAULT_NEW_ALERT_ALERT_ID = BigInteger.ZERO;
	private static final String TASK_INFO_ALERT_SW_NO = "N";
	private static final String TASK_INFO_ONLY_SW_NO = "N";

	/**
	 * Create alert staging webservice insert request.
	 * @param alertKey alertKey.
     * @param assignedUser assignedUserId.
	 * @param currentUserId currentUserId.
	 * @return alert staging webservice insert request.
	 */
	public static InsertAlertRequest createAlertRequestForUpdateNutrient(String alertKey, String assignedUser,
																   String currentUserId){
		InsertAlertRequest request = new InsertAlertRequest();
		request.setALERTID(DEFAULT_NEW_ALERT_ALERT_ID);
		request.setALERTKEY(alertKey);
		request.setALERTDATATEXT(DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT);
		request.setALERTSTATUSCODE(STRING_ACTIV);
		request.setALERTTYPECODE(STRING_PRUPD);
		request.setALERTHIDDENSWITCH(STRING_NO);
		request.setALERTCRITICALPERCENT(BigDecimal.ZERO);
		request.setASSIGNEDUSERID(assignedUser);
		request.setDELEGATEDBYUSERID(currentUserId);
		request.setUSERID(currentUserId);
		request.setUSERROLEID(STRING_PMEBM);
		return request;
	}
	/**
	 * Create alert staging webservice update request.
	 * @param alertID Alert ID.
	 * @param alertDataTxt alertDataTxt.
	 * @return alert staging webservice update request.
	 */
	public static UpdateAlertRequest createProductUpdateAlertRequestForUpdateNutrient(Integer alertID, String
			alertDataTxt, String currentUserId){
		UpdateAlertRequest request = new UpdateAlertRequest();
		request.setALERTID(BigInteger.valueOf(alertID));
		if(alertDataTxt.indexOf(DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT) == -1) {
			alertDataTxt = new StringBuilder(StringUtils.trim(alertDataTxt)).append(",").append(DEFAULT_ALERT_FOR_NUTRIENT_STATEMENT).toString();
			request.setALERTDATATEXT(alertDataTxt);
		}
		request.setDELEGATEDBYUSERID(currentUserId);
		return request;
	}

	/**
	 * Create alert recipient webservice insert request.
	 * @param alertID Alert ID.
	 * @param recipientId RecipientID.
	 * @return alert recipient webservice insert request.
	 */
	public static InsertAlertRecipientRequest createAlertRecipientRequestForUpdateNutrient(BigInteger alertID, String
            recipientId){
		InsertAlertRecipientRequest request = new InsertAlertRecipientRequest();
		request.setALRTID(alertID);
		request.setRECIPIENTID(recipientId);
		request.setALRTRECPTYPCD(STRING_USRID);
		request.setINFOALRTSW(STRING_NO);
		request.setINFOONLYSW(STRING_NO);
		return request;
	}


	public static InsertAlertRequest createAlertRequestForEcommerceTask(
			Long alertKey, String alertName, String userId){
		InsertAlertRequest request = new InsertAlertRequest();
		request.setALERTID(DEFAULT_NEW_ALERT_ALERT_ID);
		request.setALERTKEY(String.valueOf(alertKey));
		request.setALERTDATATEXT(alertName);
		request.setALERTTYPECODE(AlertStaging.AlertTypeCD.ECOM_TASK.getName());
		request.setALERTSTATUSCODE(AlertStaging.AlertStatusCD.ACTIVE.getName());
		request.setUSERID(userId);
//		request.setRESPONSEBYDATE(new Date()); //must auto insert - verify!!
		request.setUSERROLEID(AlertStaging.ALERT_CATEGORY_ROLE_PM);
		request.setASSIGNEDUSERID(userId.toUpperCase());
		request.setDELEGATEDBYUSERID(userId.toUpperCase());
		return request;
	}

	public static InsertAlertRecipientRequest createInsertAlertRecipientRequestForEcommerceTask(BigInteger alertId,
																								String userId){
		InsertAlertRecipientRequest request = new InsertAlertRecipientRequest();
		request.setALRTID(alertId);
		request.setRECIPIENTID(userId.toUpperCase());
		request.setALRTRECPTYPCD(AlertStaging.ALERT_RECIPIENT_TYPE_USER);
		request.setINFOALRTSW(TASK_INFO_ALERT_SW_NO);
		request.setINFOONLYSW(TASK_INFO_ONLY_SW_NO);
		return request;
	}
	/**
	 * Delete alert recipient.
	 * @param alertId the alert id.
	 * @param recipientId the recipientId.
	 * @return response status.
	 */
	public static DeleteAlertRecipientRequest createDeleteAlertRecipientRequest(Integer alertId, String recipientId){
		DeleteAlertRecipientRequest request = new DeleteAlertRecipientRequest();
		request.setALRTID(BigInteger.valueOf(alertId));
		request.setRECIPIENTID(StringUtils.isNotBlank(recipientId)?recipientId.toUpperCase().trim(): null);
		request.setALRTRECPTYPCD(AlertStaging.ALERT_RECIPIENT_TYPE_USER);
		return request;
	}
}
