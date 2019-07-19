/*
 * BatchUploadStatus
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.batchUpload.util;

import java.io.Serializable;
import java.util.List;

/**
 * The batch upload status
 * @author vn87351
 * since 2.12.0
 *
 */
public class BatchUploadStatus implements Serializable {
	public static final String STATUS_COMPLETED = "Completed";
	public static final String STATUS_IN_PROGRESS = "In Progress";
	public static final String DATE_DEFAULT = "";
	private static final long serialVersionUID = 1L;
	private long requestId;
	private String attributeSelected;
	private String updateDescription;
	private String dateTime;
	private String status;
	private String result;
	private String userId;
	private List<String> statusCode;
	private boolean isImageUpload;
	/**
	 * @return the requestId
	 */
	public long getRequestId() {
		return this.requestId;
	}
	/**
	 * @param requestIdValue the requestId to set
	 */
	public void setRequestId(long requestIdValue) {
		this.requestId = requestIdValue;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return this.dateTime;
	}
	/**
	 * @param dateTimeValue the dateTime to set
	 */
	public void setDateTime(String dateTimeValue) {
		this.dateTime = dateTimeValue;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.status;
	}
	/**
	 * @param statusValue the status to set
	 */
	public void setStatus(String statusValue) {
		this.status = statusValue;
	}
	/**
	 * @return the result
	 */
	public String getResult() {

		return this.result;
	}
	/**
	 * @param resultValue the result to set
	 */
	public void setResult(String resultValue) {
		this.result = resultValue;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return this.userId;
	}
	/**
	 * @param userIdValue the userId to set
	 */
	public void setUserId(String userIdValue) {
		this.userId = userIdValue;
	}

	/**
	 * @return the isImageUpload
	 */
	public boolean isImageUpload() {
	    return this.isImageUpload;
	}
	/**
	 * @param isImageUploadVal the isImageUpload to set
	 */
	public void setImageUpload(boolean isImageUploadVal) {
	    this.isImageUpload = isImageUploadVal;
	}
	/**
	 * @return the attributeSelected
	 */
	public String getAttributeSelected() {
		return attributeSelected;
	}
	/**
	 * @param attributeSelected the attributeSelected to set
	 */
	public void setAttributeSelected(String attributeSelected) {
		this.attributeSelected = attributeSelected;
	}
	/**
	 * @return the updateDescription
	 */
	public String getUpdateDescription() {
		return updateDescription;
	}
	/**
	 * @param updateDescription the updateDescription to set
	 */
	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}
	/**
	 * @return the statusCode
	 */
	public List<String> getStatusCode() {
		return statusCode;
	}
	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(List<String> statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "BatchUploadStatus{" +
				"request Id=" + requestId +
				", status='" + status + '\'' +
				", result='" + result + '\'' +
				", userId='" + userId + '\'' +
				", dateTime='" + dateTime + '\'' +
				'}';
	}
}
