/*
 * CandidateWorkRequestCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.parser;

import com.heb.pm.batchUpload.BatchUpload;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * This is CandidateWorkRequestCreator class.
 *
 * @author vn55306
 * @since 2.8.0
 */
public abstract class CandidateWorkRequestCreator {
	private static final int CANDIDATE_STAT_COMMENT_LIMIT = 255;
	private static final Logger logger = LoggerFactory.getLogger(CandidateWorkRequestCreator.class);
	/**
	 * createRequest.
	 * @param cellValues
	 *           List<String>
	 * @param transactionId
	 *            long
	 * @param userId
	 * @author vn55306
	 */
	public abstract  CandidateWorkRequest createRequest(List<String> cellValues,List<ProductAttribute> productAttributes, long transactionId, String userId);
	protected CandidateStatusKey.StatusCode getBatchStatus(BatchUpload batchUpload) {
		return batchUpload.hasErrors() ? CandidateStatusKey.StatusCode.FAILURE : CandidateStatusKey.StatusCode.BATCH_UPLOAD;
	}
	protected String getLengthOptimizedError(List<String> errors) {
		if(!errors.isEmpty()) {
			String error = String.join("; ", errors);
			logger.info("error =" + error);
			if(error.length()>CANDIDATE_STAT_COMMENT_LIMIT) {
				return error.substring(0, CANDIDATE_STAT_COMMENT_LIMIT);
			} else {
				return error;
			}
		}
		return StringUtils.EMPTY;
	}

}
