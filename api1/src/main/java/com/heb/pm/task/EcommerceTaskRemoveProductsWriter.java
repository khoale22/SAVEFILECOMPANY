/*
 * EcommerceTaskRemoveProductsWriter.java
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The writer for the mass update batch job. Removes products in a task(tracking-id) using their respective work request Id.
 *
 * @author vn40486
 * @since 2.16.0
 */
public class EcommerceTaskRemoveProductsWriter implements ItemWriter<CandidateWorkRequest> {

	private static final Logger logger = LoggerFactory.getLogger(EcommerceTaskRemoveProductsWriter.class);

	private  static final String EMPTY_LIST_MESSAGE = "List of candidate work requests empty";

	private static final String PRODUCT_LOG_MESSAGE = "Deleting work request IDs [%s]";

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	/**
	 * Called by the Spring framework to write out the CandidateWorkRequests that go into the mass update.
	 *
	 * @param items The list of CandidateWorkRequests to write.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends CandidateWorkRequest> items) throws Exception {
		// Just log an empty list.
		if (items.isEmpty()) {
			EcommerceTaskRemoveProductsWriter.logger.info(EcommerceTaskRemoveProductsWriter.EMPTY_LIST_MESSAGE);
			return;
		}
		List<Long> productIds =  items.stream().map(CandidateWorkRequest::getProductId).collect(Collectors.toList());
		EcommerceTaskRemoveProductsWriter.logger.info(
					String.format(EcommerceTaskRemoveProductsWriter.PRODUCT_LOG_MESSAGE, StringUtils.join(productIds,",")));
		this.candidateWorkRequestRepository.delete(items);
	}
}
