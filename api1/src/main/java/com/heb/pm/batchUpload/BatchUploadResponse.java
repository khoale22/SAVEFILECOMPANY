/*
 *  BatchUploadResponse
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;
/**
 * Response class to send back to the front end once the job is started.
 * @author vn87351
 * @since 2.12.0
 */
public class BatchUploadResponse {
	private String message;
	private Long transactionId;

	/**
	 * Constructs a new batch upload response.
	 *
	 * @param transactionId The transaction ID for the job.
	 * @param message Any message for the user.
	 */
	public BatchUploadResponse(Long transactionId, String message) {
		this.transactionId = transactionId;
		this.message = message;
	}

	/**
	 * Returns any messages for the user.
	 *
	 * @return Messages for the user.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the transaction ID for the user's mass update job. This will be
	 *
	 * @return The transaction ID for the user's mass update job.
	 */
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "BatchUpload Response{" +
				"message='" + message + '\'' +
				", transactionId=" + transactionId +
				'}';
	}
}