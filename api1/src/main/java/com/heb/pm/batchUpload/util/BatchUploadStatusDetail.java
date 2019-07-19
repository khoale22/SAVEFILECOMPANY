/*
 * BatchUploadStatusDetail
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.batchUpload.util;

import java.io.Serializable;

/**
 * @author vn87351
 * @since 2.12.0
 */
public class BatchUploadStatusDetail implements Serializable {

	public static final String ERROR_MESSAGE_DEFAULT = "";
	private static final long serialVersionUID = 1L;
	private Long productId;
	private String productDescription;
	private String size;
	private Long primaryUpc;
	private String updateResult;
	private String errorMessage;
	private String errorGetData;
	private Long upc;

	/**
	 * @return the size
	 */
	public String getSize() {
		return this.size;
	}
	/**
	 * @param sizeVal the size to set
	 */
	public void setSize(String sizeVal) {
		this.size = sizeVal;
	}
	/**
	 * @return the primaryUpc
	 */
	public Long getPrimaryUpc() {
		return this.primaryUpc;
	}
	/**
	 * @param primaryUpcVal the primaryUpc to set
	 */
	public void setPrimaryUpc(Long primaryUpcVal) {
		this.primaryUpc = primaryUpcVal;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}
	/**
	 * @param errorMessageVal the errorMessage to set
	 */
	public void setErrorMessage(String errorMessageVal) {
		this.errorMessage = errorMessageVal;
	}
	/**
	 * @param errorGetDataVal the errorGetData to set
	 */
	public void setErrorGetData(String errorGetDataVal) {
		this.errorGetData = errorGetDataVal;
	}
	/**
	 * @return the errorGetData
	 */
	public String getErrorGetData() {
		return this.errorGetData;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getUpdateResult() {
		return updateResult;
	}

	public void setUpdateResult(String updateResult) {
		this.updateResult = updateResult;
	}

	public Long getUpc() {
		return upc;
	}

	public void setUpc(Long upc) {
		this.upc = upc;
	}
	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "BatchUploadStatus{" +
				"product Id=" + productId +
				", upc='" + upc + '\'' +
				", product Description='" + productDescription + '\'' +
				", errorMessage='" + errorMessage + '\'' +
				'}';
	}
}
