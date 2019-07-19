/*
 *  MatAttributeValuesUploadService
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.matAttributeValues;

import com.heb.pm.batchUpload.*;
import com.heb.pm.batchUpload.earley.FileProcessingException;
import com.heb.pm.entity.TransactionTracker;
import com.heb.pm.repository.TransactionTrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Service class that kicks off Mat Attribute Values job.
 *
 * @author s573181
 * @since 2.28.0
 */
@Service
public class MatAttributeValuesUploadService {

	private static final Logger logger = LoggerFactory.getLogger(MatAttributeValuesUploadService.class);
	private static final String JOB_PARAMETER_TRANSACTION_ID = "transactionId";
	private static final String JOB_PARAMETER_USER_ID = "userId";
	private static final String LOG_TRACKING_ID = "Generated tracking-id:%d for batch-type:%s and batch-description:%s";
	private static final String LOG_JOB_SUBMIT_SUCCESS = "Successfully submitted job for processing with tracking-id:%d";
	private static final String LOG_JOB_EXECUTION_ERROR_MESSAGE = "Unable to execute job %s: %s";

	@Value("${app.mat.sourceSystemId}")
	private int sourceSystemId;

	@Value("${app.mat.MatAttributeValuesUploadJob}")
	private String jobName;

	@Autowired
	private BatchUploadParameterMap parameterMap;

	@Autowired
	private BatchUploadDataMap batchUploadDataMap;

	@Autowired
	BatchUploadProductAttributeMap batchUploadProductAttributeMap;

	@Autowired
	private TransactionTrackingRepository transactionTrackingRepository;

	@Autowired
	@Qualifier("asyncJobLauncher")
	private JobLauncher jobLauncher;

	@Autowired
	private JobLocator jobLocator;


	/**
	 * Handles the upload of an mat attribute values file.
	 *
	 * @return The transaction ID of the job kicked off.
	 * @throws IOException
	 */
	public long submitMatAttributeValuesUpload(BatchUploadRequest batchUploadRequest)
			throws IOException, FileProcessingException {


		long trackingId = this.generateTrackingId(batchUploadRequest.getUserId(),
				batchUploadRequest.getUploadFileName(), batchUploadRequest.getUploadDescription());
		logger.info(String.format(LOG_TRACKING_ID, trackingId,
				batchUploadRequest.getUploadFileName(), batchUploadRequest.getUploadDescription()));
		//2. prepare job parameters and submit the job.
		prepareAndSubmitJob(trackingId, batchUploadRequest);
		return trackingId;
	}
	/**
	 * create tracking id with file upload info
	 * @param userId user upload the file
	 * @param attr the atrribute
	 * @param description the desc for tracking
	 * @return tracking id
	 */
	private long generateTrackingId(String userId, String attr, String description) {
		TransactionTracker tracking = new TransactionTracker();
		tracking.setSource(Integer.toString(this.sourceSystemId));
		tracking.setCreateDate(LocalDateTime.now());
		tracking.setUserId(userId);
		tracking.setUserRole(BatchUploadService.USR_ROLE_CD_USER);
		tracking.setFileDes(description);
		tracking.setFileNm(attr);
		tracking.setTrxStatCd(BatchUploadService.TRX_STAT_CD);
		return transactionTrackingRepository.save(tracking).getTrackingId();
	}

	/**
	 * Prepares necessary job parameters and submits the job.
	 * @param trackingId generated tracking id for batch request.
	 * @param batchUploadRequest the uploaded batch request.
	 */
	private void prepareAndSubmitJob(long trackingId, BatchUploadRequest batchUploadRequest) {
		if (trackingId > 0) {
			//1. Get reference to batch upload Job.
			Job job = this.getBatchUploadJob();
			//2. prepare job parameters.
			this.parameterMap.add(trackingId,batchUploadRequest);
			this.batchUploadDataMap.add(trackingId,batchUploadRequest.getBatchUploadType());
			this.batchUploadProductAttributeMap.add(trackingId,batchUploadRequest.getProductAttributes());
			JobParametersBuilder parametersBuilder = new JobParametersBuilder();
			parametersBuilder.addLong(JOB_PARAMETER_TRANSACTION_ID, trackingId);
			parametersBuilder.addString(JOB_PARAMETER_USER_ID, batchUploadRequest.getUserId());
			//3. submit or run the job with the prepared parameters.
			try {
				// Kick off the job. This is asynchronous, so we won't have the status of the job after the call.
				this.jobLauncher.run(job, parametersBuilder.toJobParameters());
				logger.info(LOG_JOB_SUBMIT_SUCCESS, trackingId);
			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
					JobParametersInvalidException e) {
				logger.error(e.getMessage());
				throw new JobExecutionException(String.format(LOG_JOB_EXECUTION_ERROR_MESSAGE,
						this.jobName, e.getMessage()), e.getCause());
			}
		}
	}

	/**
	 * Returns a reference to the Batch Upload job.
	 *
	 * @return A reference to the Batch Upload job.
	 */
	private Job getBatchUploadJob() {
		try {
			return this.jobLocator.getJob(this.jobName);
		} catch (NoSuchJobException e) {
			com.heb.pm.batchUpload.JobNotDefinedException je = new com.heb.pm.batchUpload.JobNotDefinedException(this.jobName);
			logger.error(je.getMessage());
			throw je;
		}
	}
}
