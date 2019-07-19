/*
 * ProductUpdateTaskMassUpdateProcessor
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.task;

import com.heb.pm.entity.*;
import com.heb.pm.repository.BdmRepository;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author vn87351
 * @since 2.17.0
 */
public class ProductUpdateTaskProcessor implements ItemProcessor<AlertStaging, CandidateWorkRequest> {

	/**
	 * Reason for rejection of nutrition updates.
	 */
	@Value("#{jobParameters['trackingId']}")
	private Long transactionId;
	/**
	 * Used who submitted the mass update.
	 */
	@Value("#{jobParameters['userId']}")
	private String userId;

	public static final String DATE_YYYY_MM_DD = "yyyy-MM-dd";
	private MassUpdateTaskRequest massUpdateTaskRequest;
	@Autowired
	private MassUpdateTaskMap massUpdateTaskMap;
	@Autowired
	private BdmRepository bdmRepository;
	@Autowired
	private ProductUpdatesTaskService productUpdatesTaskService;

	/**
	 * Process the provided item, returning a potentially modified or new item for continued
	 * processing.  If the returned result is null, it is assumed that processing of the item
	 * should not continue.
	 * Action to process on batch
	 * 		1: save data after mass fill
	 * 		2: assign product to BDM
	 * 	    3: assign product to eBM
	 * 	    4: publish product
	 * 	    5: delete alerts
	 *
	 * @param alertStaging item to be processed
	 * @return potentially modified or new item for continued processing, null if processing of the
	 * provided item should not continue.
	 * @throws Exception
	 */
	@Override
	public CandidateWorkRequest process(AlertStaging alertStaging) {
		if (alertStaging != null) {
			massUpdateTaskRequest=massUpdateTaskMap.getItem(transactionId);
			switch (massUpdateTaskRequest.getActionType()){
				case MassUpdateTaskRequest.ACTION_TYPE_SAVE:
					return processSaveData(alertStaging);
				case MassUpdateTaskRequest.ACTION_TYPE_ASSIGN_BDM:
					return processAssignBDM(alertStaging);
				case MassUpdateTaskRequest.ACTION_TYPE_ASSIGN_eBM:
					return processAssignEBM(alertStaging);
				case MassUpdateTaskRequest.ACTION_TYPE_PUBLISH_PRODUCT:
					return processPublish(alertStaging);
				case MassUpdateTaskRequest.ACTION_TYPE_DELETE_PRODUCT:
					return processDeleteAlert(alertStaging);
			}
		}
		return null;
	}

	/**
	 * process delete alert. do not create work resquest. call webservice to close alert
	 * @param alertStaging
	 * @return
	 */
	private CandidateWorkRequest processDeleteAlert(AlertStaging alertStaging){
		productUpdatesTaskService.removeAlert(alertStaging,massUpdateTaskRequest.getUserId());
		return null;
	}

	/**
	 * process publish product. will create work request and send to mainframe process
	 * @param alertStaging
	 * @return
	 */
	private CandidateWorkRequest processPublish(AlertStaging alertStaging){
		CandidateWorkRequest workRequest =
				getEmptyWorkRequest(massUpdateTaskRequest, alertStaging,
						CandidateWorkRequest.INTNT_ID_PUBLISH,massUpdateTaskRequest.getUserId());
		setDataToCandidateStatus(workRequest, CandidateStatusKey.StatusCode.BATCH_UPLOAD, StringUtils.EMPTY);

		return workRequest;
	}

	/**
	 * process assgin to eBM. will create work request and send to mainframe process
	 * @param alertStaging
	 * @return
	 */
	private CandidateWorkRequest processAssignEBM(AlertStaging alertStaging){
		String userAssign=StringUtils.EMPTY;
		if(alertStaging.getProductMaster().getClassCommodity()!=null)
			userAssign=alertStaging.getProductMaster().getClassCommodity().geteBMid();
		CandidateWorkRequest workRequest =
				getEmptyWorkRequest(massUpdateTaskRequest, alertStaging,
						CandidateWorkRequest.INTNT_ID_ASSIGNED_EBM,userAssign);

		setDataToCandidateStatus(workRequest, CandidateStatusKey.StatusCode.BATCH_UPLOAD, StringUtils.EMPTY);
		return workRequest;
	}

	/**
	 * process assign to BDM. will create work request and send to mainframe process
	 * @param alertStaging
	 * @return
	 */
	private CandidateWorkRequest processAssignBDM(AlertStaging alertStaging){
		String userAssign=StringUtils.EMPTY;
		if(StringUtils.isNotBlank(alertStaging.getProductMaster().getBdmCode())){
			Bdm bdm = bdmRepository.findOne(StringUtils.trim(alertStaging.getProductMaster().getBdmCode()));
			if(bdm!=null)
				userAssign = StringUtils.trim(bdm.getUserId());
		}
		CandidateWorkRequest workRequest =
				getEmptyWorkRequest(massUpdateTaskRequest, alertStaging,
						CandidateWorkRequest.INTNT_ID_ASSIGNED_BDM,userAssign);
		setDataToCandidateStatus(workRequest, CandidateStatusKey.StatusCode.BATCH_UPLOAD, StringUtils.EMPTY);
		return workRequest;
	}

	/**
	 * process save mass fill data. will create work request and send to mainframe process
	 * @param alertStaging
	 * @return
	 */
	private CandidateWorkRequest processSaveData(AlertStaging alertStaging){
		CandidateWorkRequest workRequest =
				getEmptyWorkRequest(massUpdateTaskRequest, alertStaging,
						CandidateWorkRequest.INTNT_ID_DEFAULT,null);
		setDataToCandidateStatus(workRequest, CandidateStatusKey.StatusCode.BATCH_UPLOAD, StringUtils.EMPTY);
		setCandidateProductMaster(workRequest, alertStaging, massUpdateTaskRequest);
		checkPDPTemplateMassFill(workRequest, alertStaging, massUpdateTaskRequest);
		return workRequest;
	}

	/**
	 * Set data to CandidateStatus.
	 *
	 * @param candidateWorkRequest the candidateWorkRequest.
	 * @param statusCode           the status code.
	 */
	private void setDataToCandidateStatus(CandidateWorkRequest candidateWorkRequest, CandidateStatusKey.StatusCode statusCode, String errorMessage) {
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey candidateStatKey = new CandidateStatusKey();
		candidateStatKey.setStatus(statusCode.getName());
		candidateStatKey.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(candidateStatKey);
		candidateStatus.setUpdateUserId(userId);
		candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
		candidateStatus.setCommentText(errorMessage);
		candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
	}

	/**
	 * Get empty CandidateWorkRequest.
	 *
	 * @param massUpdateTaskRequest the MassUpdateTaskRequest.
	 * @param alertStaging the AlertStaging.
	 * @return the CandidateWorkRequest.
	 */
	private CandidateWorkRequest getEmptyWorkRequest(MassUpdateTaskRequest massUpdateTaskRequest,
													 AlertStaging alertStaging,int intent,String userAssigne) {
		CandidateWorkRequest workRequest = new CandidateWorkRequest();
		workRequest.setProductId(alertStaging.getProductMaster().getProdId());
		workRequest.setCreateDate(LocalDateTime.now());
		workRequest.setUserId(userId);
		workRequest.setLastUpdateDate(LocalDateTime.now());
		workRequest.setReadyToActivate(CandidateWorkRequest.RDY_TO_ACTVD_SW_DEFAULT);
		workRequest.setStatus(CandidateWorkRequest.PD_SETUP_STAT_CD_BATCH_UPLOAD);
		workRequest.setIntent(intent);
		workRequest.setSourceSystem(CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT);
		workRequest.setLastUpdateUserId(userId);
		workRequest.setTrackingId(transactionId);
		workRequest.setStatusChangeReason(CandidateWorkRequest.STAT_CHG_RSN_ID_WRKG);
		workRequest.setLastUpdateUserId(userAssigne);
		workRequest.setProductId(alertStaging.getProductMaster().getProdId());
		workRequest.setCandidateProductMaster(new ArrayList<>());
		setCandidateProductMaster(workRequest, alertStaging, massUpdateTaskRequest);
		return workRequest;
	}

	/**
	 * set candidate product master
	 *
	 * @param candidateWorkRequest CandidateWorkRequest
	 * @param alertStaging         AlertStaging
	 * @param massUpdateTaskRequest         MassUpdateTaskRequest
	 */
	private void setCandidateProductMaster(CandidateWorkRequest candidateWorkRequest, AlertStaging alertStaging, MassUpdateTaskRequest massUpdateTaskRequest) {
		CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
		candidateProductMaster.setProductId(alertStaging.getProductMaster().getProdId());
		candidateProductMaster.setNewDataSw(true);
		candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
		candidateProductMaster.setPrprcForNbr(1);
		candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
		candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
		candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
		candidateProductMaster.setCandidateFulfillmentChannels(new ArrayList<>());
		checkFulfillmentMassFill(candidateWorkRequest, candidateProductMaster, alertStaging, massUpdateTaskRequest);
		candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.setCandidateProductMaster(new ArrayList<>());
		candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
	}

	/**
	 * mass fill for fulfillment condition
	 * @param candidateWorkRequest
	 * @param candidateProductMaster
	 * @param alertStaging
	 * @param massFillData
	 */
	private void checkFulfillmentMassFill(CandidateWorkRequest candidateWorkRequest, CandidateProductMaster candidateProductMaster,
										  AlertStaging alertStaging, MassUpdateTaskRequest massFillData){
		if (alertStaging.isAlertStagingsFromDB()) {
			if(!StringUtils.isBlank(massFillData.getEffectiveDate()) && !StringUtils.isBlank(massFillData.getExpirationDate())){
				setFulfillmentChannelFromDB(candidateWorkRequest, candidateProductMaster,alertStaging, massFillData);
			}
		}else{
			candidateProductMaster.setCandidateFulfillmentChannels(new ArrayList<CandidateFulfillmentChannel>());
			for (ProductFullfilmentChanel productFullfilmentChanel : alertStaging.getProductMaster().getProductFullfilmentChanels()) {
				CandidateFulfillmentChannel candidateProductFullfillChanel = new CandidateFulfillmentChannel();
				CandidateFulfillmentChannelKey key = new CandidateFulfillmentChannelKey();
				key.setSalesChannelCode(productFullfilmentChanel.getKey().getSalesChanelCode());
				key.setFulfillmentChannelCode(productFullfilmentChanel.getKey().getFullfillmentChanelCode());
				candidateProductFullfillChanel.setKey(key);
				candidateProductFullfillChanel.setNewData(CandidateFulfillmentChannel.NO_STATUS);
				if(productFullfilmentChanel.getExpirationDate()!=null && productFullfilmentChanel.getEffectDate()!=null){
					candidateProductFullfillChanel.setEffectiveDate(productFullfilmentChanel.getEffectDate());
					candidateProductFullfillChanel.setExpirationDate(productFullfilmentChanel.getExpirationDate());
					candidateProductFullfillChanel.setNewData(CandidateFulfillmentChannel.YES_STATUS);
				}
				candidateProductFullfillChanel.setCandidateProductMaster(candidateProductMaster);
				candidateProductFullfillChanel.setCandidateWorkRequest(candidateWorkRequest);
				candidateProductMaster.getCandidateFulfillmentChannels().add(candidateProductFullfillChanel);
			}
		}
	}
	/**
	 * set fulfillment channel
	 * @param candidateWorkRequest
	 * @param candidateProductMaster
	 * @param alertStaging
	 * @param massFillData
	 */
	private void setFulfillmentChannelFromDB(CandidateWorkRequest candidateWorkRequest, CandidateProductMaster candidateProductMaster,
									   AlertStaging alertStaging, MassUpdateTaskRequest massFillData) {
		candidateProductMaster.setCandidateFulfillmentChannels(new ArrayList<CandidateFulfillmentChannel>());
		for (FulfillmentChannel fulfillmentChannel : massFillData.getLstFulfillmentChannel()) {
			CandidateFulfillmentChannel candidateProductFullfillChanel = new CandidateFulfillmentChannel();
			CandidateFulfillmentChannelKey key = new CandidateFulfillmentChannelKey();
			key.setSalesChannelCode(fulfillmentChannel.getKey().getSalesChannelCode());
			key.setFulfillmentChannelCode(fulfillmentChannel.getKey().getFulfillmentChannelCode());
			candidateProductFullfillChanel.setKey(key);
			candidateProductFullfillChanel.setNewData(CandidateFulfillmentChannel.NO_STATUS);
			if(!StringUtils.isBlank(massFillData.getEffectiveDate()) && !StringUtils.isBlank(massFillData.getExpirationDate())) {
				candidateProductFullfillChanel.setEffectiveDate(DateUtils.getLocalDate(massFillData.getEffectiveDate(), DATE_YYYY_MM_DD));
				candidateProductFullfillChanel.setExpirationDate(DateUtils.getLocalDate(massFillData.getExpirationDate(), DATE_YYYY_MM_DD));
				candidateProductFullfillChanel.setNewData(CandidateFulfillmentChannel.YES_STATUS);
			}

			candidateProductFullfillChanel.setCandidateProductMaster(candidateProductMaster);
			candidateProductFullfillChanel.setCandidateWorkRequest(candidateWorkRequest);
			candidateProductMaster.getCandidateFulfillmentChannels().add(candidateProductFullfillChanel);
		}
	}

	/**
	 * check pdp template mass fill data to insert candidate master data attribute
	 * @param candidateWorkRequest
	 * @param alertStaging
	 * @param massFillData
	 */
	private void checkPDPTemplateMassFill(CandidateWorkRequest candidateWorkRequest, AlertStaging alertStaging, MassUpdateTaskRequest massFillData) {
		if (alertStaging.isAlertStagingsFromDB()) {
			if (massFillData.getPdpTemplate() != null && StringUtils.isNotBlank(massFillData.getPdpTemplate().getDescription())) {
				setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, alertStaging, massFillData, massFillData.getPdpTemplate().getDescription());
			}
		} else if (alertStaging.getProductMaster().getPdpTemplate() != null) {
			setDataToCandidateMasterDataExtensionAttribute(candidateWorkRequest, alertStaging, massFillData, alertStaging.getProductMaster().getPdpTemplate().getAttributeValueText());
		}
	}

	/**
	 * Set Data To CandidateMasterDataExtensionAttribute.
	 *
	 * @param candidateWorkRequest the candidateWorkRequest.
	 */
	private void setDataToCandidateMasterDataExtensionAttribute(CandidateWorkRequest candidateWorkRequest, AlertStaging alertStaging, MassUpdateTaskRequest massFillData, String description) {
		candidateWorkRequest.setCandidateMasterDataExtensionAttributes(new ArrayList<>());
		CandidateMasterDataExtensionAttribute candidateMasterDataExtensionAttribute = new CandidateMasterDataExtensionAttribute();
		CandidateMasterDataExtensionAttributeKey candidateMasterDataExtensionAttributeKey = new CandidateMasterDataExtensionAttributeKey();
		candidateMasterDataExtensionAttributeKey.setAttributeId(CandidateMasterDataExtensionAttribute.PDP_TEMPLATE_ATTRIBUTE_ID);
		candidateMasterDataExtensionAttributeKey.setKeyId(alertStaging.getProductMaster().getProdId());
		candidateMasterDataExtensionAttributeKey.setItemProductKey(CandidateMasterDataExtensionAttributeKey.PRODUCT_KEY);
		candidateMasterDataExtensionAttributeKey.setSequenceNumber(1L);
		candidateMasterDataExtensionAttributeKey.setDataSourceSystem(Long.valueOf(CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT));
		candidateMasterDataExtensionAttribute.setAttributeCode(0);
		candidateMasterDataExtensionAttribute.setKey(candidateMasterDataExtensionAttributeKey);
		candidateMasterDataExtensionAttribute.setAttributeValueText(description);
		candidateMasterDataExtensionAttribute.setNewData(CandidateMasterDataExtensionAttribute.YES_STATUS);
		candidateMasterDataExtensionAttribute.setAttributeValueDate(LocalDate.now());
		candidateMasterDataExtensionAttribute.setCreateDate(LocalDateTime.now());
		candidateMasterDataExtensionAttribute.setLastUpdateDate(LocalDateTime.now());
		candidateMasterDataExtensionAttribute.setCreateUserId(userId);
		candidateMasterDataExtensionAttribute.setLastUpdateUserId(userId);
		candidateMasterDataExtensionAttribute.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.getCandidateMasterDataExtensionAttributes().add(candidateMasterDataExtensionAttribute);
	}
}
