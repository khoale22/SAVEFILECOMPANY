/*
 *  CandidateWorkRequestWriter
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload;

import com.heb.pm.batchUpload.jms.BatchUploadMessageTibcoSender;
import com.heb.pm.entity.CandidateProductMaster;
import com.heb.pm.entity.CandidateStatus;
import com.heb.pm.entity.CandidateStatusKey;
import com.heb.pm.entity.CandidateWorkRequest;
import com.heb.pm.repository.CandidateWorkRequestRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The writer for the Batch Upload batch job.
 *
 * @author vn55306
 * @since 2.12.0
 */
public class CandidateWorkRequestWriter implements ItemWriter<CandidateWorkRequest> , StepExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(CandidateWorkRequestWriter.class);
	private  static final String EMPTY_LIST_MESSAGE = "List of candidate work requests empty";
	private static final String WRITE_MESSAGE = "Writing %d candidate work requests";
	@Value("#{jobParameters['transactionId']}")
	private long transactionId;
	@Value("#{jobParameters['userId']}")
	private String userId;
	@Autowired
	private CandidateWorkRequestRepository candidateWorkRequestRepository;
	@Autowired
	private BatchUploadMessageTibcoSender massUploadMessageTibcoSender;

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
			CandidateWorkRequestWriter.logger.info(CandidateWorkRequestWriter.EMPTY_LIST_MESSAGE);
			return;
		}
		// We don't have a lot of information about items (ps_work_id is not set yet), so just log the counts.
		CandidateWorkRequestWriter.logger.info(String.format(CandidateWorkRequestWriter.WRITE_MESSAGE, items.size()));

		//when the excel upload is service Case sign, the application will create one more work request with intent 27
		if(items.get(0).isUploadServeCaseSign()){
			List<CandidateWorkRequest> candidateWorkRequests = this.createWorkRequestForServiceCaseSignBeforeSave(items);
			this.candidateWorkRequestRepository.save(candidateWorkRequests);
		}else {
			this.candidateWorkRequestRepository.save(items);
		}
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		int countWorkIds = this.candidateWorkRequestRepository.countByTransactionTracking_trackingId(transactionId);
		if (countWorkIds > 0) {
			logger.debug("AssortmentBathUploadJMSQueueListener - receivedMessage, countWorkIds: " + countWorkIds);
			this.massUploadMessageTibcoSender.sendTrkIdToTibcoEMSQueue(transactionId, countWorkIds, userId);
		}
		return null;
	}
	/**
	 * create more work request for  Sevice Case Sign with intent id = 27.
	 * @param items
	 *            List<CandidateWorkRequest>
	 * @author vn55306
	 */
	private List<CandidateWorkRequest> createWorkRequestForServiceCaseSignBeforeSave(List<? extends
			CandidateWorkRequest> items){
		List<CandidateWorkRequest> lstWorkRequest = new ArrayList<CandidateWorkRequest>();
		CandidateWorkRequest candidateWorkRequestNew = null;
		List<Long> productIds = new ArrayList<Long>();
		CandidateStatus candidateStatus;
		CandidateStatusKey candidateStatKey;
		for(CandidateWorkRequest candidateWorkRequest:items){

			// if the intent is approval then create new work request with intent 27
			if(candidateWorkRequest.getIntent()==CandidateWorkRequest.INTENT.APPROVE_PROCESS.getName()){
				if(candidateWorkRequest.getProductId()!=null && candidateWorkRequest.getProductId()>0){
					productIds.add(candidateWorkRequest.getProductId());
				}
				candidateWorkRequestNew = new CandidateWorkRequest();
				candidateWorkRequestNew.setTrackingId(transactionId);
				candidateWorkRequestNew.setIntent(CandidateWorkRequest.INTENT.EXCEL_UPLOAD.getName());
				candidateWorkRequestNew.setStatus(CandidateStatus.PD_SETUP_STAT_CD_BATCH_UPLOAD);
				candidateWorkRequestNew.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
				candidateWorkRequestNew.setUpc(candidateWorkRequest.getUpc());
				candidateWorkRequestNew.setProductId(candidateWorkRequest.getProductId());
				candidateWorkRequestNew.setLastUpdateUserId(candidateWorkRequest.getLastUpdateUserId());
				candidateWorkRequestNew.setSourceSystem(candidateWorkRequest.getSourceSystem());
				candidateWorkRequestNew.setLastUpdateDate(candidateWorkRequest.getLastUpdateDate());
				candidateWorkRequestNew.setCreateDate(candidateWorkRequest.getCreateDate());
				candidateWorkRequestNew.setUserId(candidateWorkRequest.getUserId());

				if(candidateWorkRequest.getCandidateStatuses()!=null) {
					candidateWorkRequestNew.setCandidateStatuses(new ArrayList<CandidateStatus>());
					candidateStatus = new CandidateStatus();
					candidateStatKey = new CandidateStatusKey();
					candidateStatKey.setStatus(candidateWorkRequest.getCandidateStatuses().get(0).getKey().getStatus());
					candidateStatKey.setLastUpdateDate(LocalDateTime.now());
					candidateStatus.setKey(candidateStatKey);
					candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
					candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
					candidateStatus.setCommentText(candidateWorkRequest.getCandidateStatuses().get(0).getCommentText());
					candidateStatus.setCandidateWorkRequest(candidateWorkRequestNew);
					candidateWorkRequestNew.getCandidateStatuses().add(candidateStatus);
				}
				lstWorkRequest.add(candidateWorkRequestNew);
				//remove the status for work request of approval process
				candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
			}
			lstWorkRequest.add(candidateWorkRequest);

		}
		if(!productIds.isEmpty()){
			//find the list of work request that has intent id = 28 for the uploaded product
			List<CandidateWorkRequest> candidateWorkRequestOld = this.candidateWorkRequestRepository.findByIntentAndProductIdIn(CandidateWorkRequest.INTENT.APPROVE_PROCESS.getName(),productIds);
			//change status to delete
			for(CandidateWorkRequest candidateWorkRequest:candidateWorkRequestOld){
				candidateWorkRequest.setStatus(CandidateWorkRequest.StatusCode.DELETED.getName());
			}
			lstWorkRequest.addAll(candidateWorkRequestOld);
		}
		return lstWorkRequest;
	}
}
