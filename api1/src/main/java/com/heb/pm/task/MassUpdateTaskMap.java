/*
 * MassUpdateTaskMap
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the list of products the user asked to mass update.
 *
 * @author vn70529
 * @since 2.17.0
 */
@Service
public class MassUpdateTaskMap {

	private Map<Long, MassUpdateTaskRequest> massUpdateTaskRequest;

	/**
	 * Constructs a new MassUpdateProductMap.
	 */
	public MassUpdateTaskMap() {
		this.massUpdateTaskRequest = new HashMap<>();
	}

	/**
	 * Adds a list of products to the map.
	 *
	 * @param transactionId The transaction ID for the job.
	 * @param massUpdateTaskRequest The massUpdateTaskRequest.
	 */
	public void add(Long transactionId, MassUpdateTaskRequest massUpdateTaskRequest) {
		this.massUpdateTaskRequest.put(transactionId, massUpdateTaskRequest);
	}

	/**
	 * Returns a list of products for the job. This is a destructive read and the list will no longer be available
	 * after this call.
	 *
	 * @param transactionId The transaction ID for the job.
	 * @return The MassUpdateTaskRequest for the job to update.
	 */
	public MassUpdateTaskRequest get(Long transactionId) {
		return this.massUpdateTaskRequest.remove(transactionId);
	}

	/**
	 * Returns a list of products for the job and do not remove.
	 *
	 * @param transactionId The transaction ID for the job.
	 * @return The alertStaging for the job to update.
	 */
	public MassUpdateTaskRequest getItem(Long transactionId) {
		return this.massUpdateTaskRequest.get(transactionId);
	}

	/**
	 * remove the data on map
	 * @param transactionId the tracking id
	 * @return
	 */
	public void remove(Long transactionId) {
		this.massUpdateTaskRequest.remove(transactionId);
	}
}
