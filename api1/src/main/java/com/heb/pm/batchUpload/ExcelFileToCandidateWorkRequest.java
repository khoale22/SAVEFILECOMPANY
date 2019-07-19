/*
 *  ExcelFileToCandidateWorkRequest
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreatorFactory;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.entity.CandidateWorkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * This is ExcelFileToCandidateWorkRequest class.
 *
 * @author vn55306
 * @since 2.8.0
 */
public class ExcelFileToCandidateWorkRequest implements ItemProcessor<List<String>, CandidateWorkRequest>, StepExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(ExcelFileToCandidateWorkRequest.class);
	@Value("#{jobParameters['transactionId']}")
	private long transactionId;
	@Value("#{jobParameters['userId']}")
	private String userId;

	@Autowired
	private BatchUploadDataMap batchUploadDataMap;
	@Autowired
	private BatchUploadProductAttributeMap batchUploadProductAttributeMap;
	@Autowired
	private CandidateWorkRequestCreatorFactory factory;

	private CandidateWorkRequestCreator creator;

	private BatchUploadType batchUploadType;

	private List<ProductAttribute> productAttributes;
	@Override
	public CandidateWorkRequest process(List<String> rows) throws Exception {
		if(rows!=null){
			return this.creator.createRequest(rows, productAttributes,transactionId, userId);
		}
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		batchUploadType = batchUploadDataMap.get(transactionId);
		productAttributes = this.batchUploadProductAttributeMap.get(transactionId);
		this.creator =
				this.factory.getCreator(batchUploadType);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
}
