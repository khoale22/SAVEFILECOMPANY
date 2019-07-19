/*
 * PrimoPickSetter
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.primoPick;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.entity.*;
import com.heb.pm.repository.ProductMarketingClaimRepository;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is setter to entity.
 *
 * @author vn87351
 * @since 2.12.0
 */
@Component
public class PrimoPickSetter {
	private static final Logger LOGGER = LoggerFactory.getLogger(PrimoPickSetter.class);
	public static final String DFLT_MAX_DATE_TIME_MM_DD_YYYY = "12/31/9999";
	/**
	 * DFLT_DATE_FORMAT.
	 */
	public static final String DFLT_DATE_FORMAT = "MM/dd/yyyy";
	@Autowired
	private ProductMarketingClaimRepository productMarketingClaimRepository;

	/**
	 * set data on to CandidateStat entity.
	 *
	 * @param candidateWorkRequest   CandidateWorkRequest
	 * @param statusCode the status code
	 * @param comments the comment
	 */
	public void setCandidateStatus(
			CandidateWorkRequest candidateWorkRequest, CandidateStatusKey.StatusCode statusCode, String comments) {
		candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
		CandidateStatus candidateStatus = new CandidateStatus();
		CandidateStatusKey candidateStatKey = new CandidateStatusKey();
		candidateStatKey.setStatus(statusCode.getName());
		candidateStatKey.setLastUpdateDate(LocalDateTime.now());
		candidateStatus.setKey(candidateStatKey);
		candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
		candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
		candidateStatus.setCommentText(comments);
		candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
		candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
	}

	/**
	 * set data To CandidateProductMaster.
	 *
	 * @param primoPick PrimoPickBatchUpload
	 * @param candidateWorkRequest   CandidateWorkRequest
	 */
	public void setCandidateProductMaster(
			CandidateWorkRequest candidateWorkRequest, PrimoPickBatchUpload primoPick) {
		CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
		candidateProductMaster.setProductId(NumberUtils.toLong(primoPick.getProductId(), 0L));
		candidateProductMaster.setNewDataSw(true);
		candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
		candidateProductMaster.setPrprcForNbr(0);
		candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
		candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
		candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
		candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
		setCandidateDescription(candidateProductMaster, primoPick);
		setCandidateProductMarketingClaim(candidateProductMaster, primoPick);
		candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
		candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);

	}

	/**
	 * set data to candidate product marketing claim
	 *
	 * @param candidateProductMaster CandidateProductMaster entity
	 * @param primoPick excel object
	 */
	private void setCandidateProductMarketingClaim(
			CandidateProductMaster candidateProductMaster, PrimoPickBatchUpload primoPick) {

		candidateProductMaster.setCandidateProductMarketingClaim(new ArrayList<CandidateProductMarketingClaim>());
		CandidateProductMarketingClaim candidateProductMarketingClaim = new CandidateProductMarketingClaim();
		CandidateProductMarketingClaimKey key = new CandidateProductMarketingClaimKey();
		key.setCandidateProductId(candidateProductMaster.getCandidateProductId());
		candidateProductMarketingClaim.setKey(key);
		candidateProductMarketingClaim.setStatus(getMarketingClaimStatusCode(primoPick));

		//set primo pick start date and end date when the status is not reject or is not primo pick
		if (!(PrimoPickBatchUpload.PRIMO_PICK_STATUS_REJECT.equalsIgnoreCase(primoPick
				.getPrimoPickStatus()) || PrimoPickBatchUpload.PRIMO_PICK_NO.equalsIgnoreCase
				(primoPick.getPrimoPick()))) {

			//set default value for start date, end date.
			if(PrimoPickBatchUpload.PRIMO_PICK_YES.equalsIgnoreCase(primoPick.getPrimoPick()) ||
					isPrimoPickEmptyAndDateNotEmpty(primoPick)){
				if(StringUtils.isBlank(primoPick.getPrimoPickStartDate())){
					primoPick.setPrimoPickStartDate(LocalDate.now().plusDays(1L)
							.format(DateTimeFormatter.ofPattern(DFLT_DATE_FORMAT)));
				}
				if(StringUtils.isBlank(primoPick.getPrimoPickEndDate())){
					LocalDate endDateCal = DateUtils.getLocalDate(DFLT_MAX_DATE_TIME_MM_DD_YYYY,
							AbstractBatchUploadValidator.DFLT_DATE_FORMAT);
					primoPick.setPrimoPickEndDate(
							endDateCal.format(DateTimeFormatter.ofPattern(DFLT_DATE_FORMAT)));
				}
			}

			candidateProductMarketingClaim.setEffectiveDate(
					DateUtils.getLocalDate(primoPick.getPrimoPickStartDate(),
							DFLT_DATE_FORMAT));
			candidateProductMarketingClaim.setExpirationDate(
					DateUtils.getLocalDate(primoPick.getPrimoPickEndDate(),
							DFLT_DATE_FORMAT));
		}
		if (primoPick.getPrimoPick().equals(PrimoPickBatchUpload.PRIMO_PICK_YES)) {
			candidateProductMarketingClaim.setNewData(CandidateProductMarketingClaim.TURN_CODE_ON);
			candidateProductMarketingClaim.getKey().setMarketingClaimCode(
					CandidateProductMarketingClaim.Codes.DISTINCTIVE.getCode());
			createNewCandidateProductMarketingClaim(
					candidateProductMaster, CandidateProductMarketingClaim.Codes.PRIMO_PICK.getCode(),
					candidateProductMarketingClaim.getStatus(),
					CandidateProductMarketingClaim.TURN_CODE_ON, candidateProductMarketingClaim.getEffectiveDate(),
					candidateProductMarketingClaim.getExpirationDate());

		} else if (StringUtils.isNotBlank(primoPick.getPrimoPick())) {
			candidateProductMarketingClaim.setNewData(CandidateProductMarketingClaim.TURN_CODE_OFF);
			candidateProductMarketingClaim.getKey().setMarketingClaimCode(
					CandidateProductMarketingClaim.Codes.PRIMO_PICK.getCode());
		} else if (StringUtils.isNotBlank(candidateProductMarketingClaim.getStatus())) {
			candidateProductMarketingClaim.setNewData(null);
			candidateProductMarketingClaim.getKey().setMarketingClaimCode(
					CandidateProductMarketingClaim.Codes.PRIMO_PICK.getCode());
			createNewCandidateProductMarketingClaim(candidateProductMaster, CandidateProductMarketingClaim.Codes.DISTINCTIVE.getCode(),
					candidateProductMarketingClaim.getStatus(), null,
					candidateProductMarketingClaim.getEffectiveDate(), candidateProductMarketingClaim.getExpirationDate());
		}
		candidateProductMarketingClaim.setCandidateProductMaster(candidateProductMaster);
		candidateProductMaster.getCandidateProductMarketingClaim().add(candidateProductMarketingClaim);

	}

	/**
	 * validate date with condition Primo Pick Empty And Date Not Empty
	 * @param primoPick PrimoPickBatchUpload
	 * @return boolean
	 */
	private boolean isPrimoPickEmptyAndDateNotEmpty(PrimoPickBatchUpload primoPick){
		return StringUtils.isBlank(primoPick.getPrimoPick()) && (!StringUtils.isBlank
				(primoPick.getPrimoPickStartDate()) || !StringUtils
				.isBlank(primoPick.getPrimoPickEndDate()));
	}

	/**
	 * create camdidate marketing claim
	 *
	 * @param candidateProductMaster
	 * @param code                   the claim code
	 */
	private void createNewCandidateProductMarketingClaim(CandidateProductMaster candidateProductMaster,
														 String code, String status, String dataSwitch, LocalDate start, LocalDate end) {
		CandidateProductMarketingClaim candidatePRC = new CandidateProductMarketingClaim();
		CandidateProductMarketingClaimKey key = new CandidateProductMarketingClaimKey();
		key.setCandidateProductId(candidateProductMaster.getCandidateProductId());
		key.setMarketingClaimCode(code);
		candidatePRC.setEffectiveDate(start);
		candidatePRC.setExpirationDate(end);
		candidatePRC.setNewData(dataSwitch);
		candidatePRC.setStatus(status);
		candidatePRC.setKey(key);
		candidatePRC.setCandidateProductMaster(candidateProductMaster);
		candidateProductMaster.getCandidateProductMarketingClaim().add(candidatePRC);
	}

	/**
	 * marketing claim status code with logic
	 *
	 * @param primoPick PrimoPickBatchUpload
	 * @return code
	 */
	private String getMarketingClaimStatusCode(PrimoPickBatchUpload primoPick) {
		String codeFinal = StringUtils.EMPTY;
		if (StringUtils.isEmpty(primoPick.getPrimoPickStatus())) {
			ProductMarketingClaim productMarketingClaim =
					productMarketingClaimRepository.findFirstByKeyProdId(Long.valueOf(primoPick.getProductId()));
			if (productMarketingClaim != null) {
				codeFinal = productMarketingClaim.getMarketingClaimStatusCode();
			} else {
				codeFinal = CandidateProductMarketingClaim.MARKETING_CLAIM_STATUS_CODE_SUBMIT;
			}
		} else {
			codeFinal = primoPick.getPrimoPickStatus();
		}
		return codeFinal;
	}

	/**
	 * set excel data to candidate description
	 *
	 * @param candidateProductMaster the candidate product master entity
	 * @param primoPick the excel data
	 */
	private void setCandidateDescription(
			CandidateProductMaster candidateProductMaster, PrimoPickBatchUpload primoPick) {

		candidateProductMaster.setCandidateDescriptions(new ArrayList<>());
		if (StringUtils.isNotEmpty(primoPick.getPrimoPickShortDescription())) {
			CandidateDescription candidateDescription = new CandidateDescription();
			CandidateDescriptionKey key = new CandidateDescriptionKey();
			key.setDescriptionType(DescriptionType.PRIMO_PICK_SHORT_DESCRIPTION);
			key.setLanguageType(CandidateDescriptionKey.LANGUAGE_ENGLISH);
			key.setCandidateProductId(candidateProductMaster.getCandidateProductId());
			candidateDescription.setKey(key);
			candidateDescription.setDescription(primoPick.getPrimoPickShortDescription());
			candidateDescription.setLastUpdateUserId(candidateProductMaster.getLstUpdtUsrId());
			candidateDescription.setLastUpdateDate(LocalDateTime.now());
			candidateDescription.setCandidateProductMaster(candidateProductMaster);
			candidateProductMaster.getCandidateDescriptions().add(candidateDescription);
		}
		if (StringUtils.isNotEmpty(primoPick.getPrimoPickLongDescription())) {
			CandidateDescription candidateDescription = new CandidateDescription();
			CandidateDescriptionKey key = new CandidateDescriptionKey();
			key.setDescriptionType(DescriptionType.PRIMO_PICK_LONG_DESCRIPTION);
			key.setLanguageType(CandidateDescriptionKey.LANGUAGE_ENGLISH);
			key.setCandidateProductId(candidateProductMaster.getCandidateProductId());
			candidateDescription.setKey(key);
			candidateDescription.setDescription(primoPick.getPrimoPickLongDescription());
			candidateDescription.setLastUpdateUserId(candidateProductMaster.getLstUpdtUsrId());
			candidateDescription.setLastUpdateDate(LocalDateTime.now());
			candidateDescription.setCandidateProductMaster(candidateProductMaster);
			candidateProductMaster.getCandidateDescriptions().add(candidateDescription);
		}
	}
}
