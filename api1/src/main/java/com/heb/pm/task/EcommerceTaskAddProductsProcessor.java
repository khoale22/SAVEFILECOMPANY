/*
 * EcommerceTaskAddProductsProcessor
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * The processor part of the mass update job. It will convert a product ID to a candidate work request.
 *
 * @author vn40486
 * @since 2.16.0
 */
public class EcommerceTaskAddProductsProcessor implements ItemProcessor<Long, CandidateWorkRequest>, StepExecutionListener{

	private static final Logger logger = LoggerFactory.getLogger(EcommerceTaskAddProductsProcessor.class);

	private static final String PRODUCT_LOG_MESSAGE = "Processing Product ID %d";

	@Value("#{jobParameters['transactionId']}")
	private long transactionId;

	@Value("#{jobParameters['userId']}")
	private String userId;

	@Value("${app.sourceSystemId}")
	private int sourceSystemId;

	@Autowired
	private EcommerceTaskService ecommerceTaskService;

	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;

	/**
	 * Called by the SpringBatch framework. It takes a product ID and creates a CandidateWorkRequest.
	 *
	 * @param item The product ID to create a work request for.
	 * @return A candidate work request for the product.
	 * @throws Exception
	 */
	@Override
	public CandidateWorkRequest process(Long item) throws Exception {
		EcommerceTaskAddProductsProcessor.logger.info (
				String.format(EcommerceTaskAddProductsProcessor.PRODUCT_LOG_MESSAGE, item));
		long candidateExistsCount = this.candidateWorkRequestRepository.countByTrackingIdAndProductIdAndStatus(
				transactionId, item, CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD);
		if(!(candidateExistsCount > 0)) {
			return this.ecommerceTaskService.createAddProductsCandidate(
					item, this.transactionId, this.userId, this.sourceSystemId);
		}
		return null;
	}

	/**
	 * Called by Spring Batch before the step starts.
	 *
	 * @param stepExecution The context the step is executing in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
