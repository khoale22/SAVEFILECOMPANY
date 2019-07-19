/*
 * WineCandidateWorkRequestCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.serviceCaseSigns;

import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import com.heb.pm.entity.*;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.repository.TagTypeRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This is template for WineCandidateWorkRequestCreator.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class ServiceCaseSignCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
	private static final Logger logger = LoggerFactory.getLogger(ServiceCaseSignCandidateWorkRequestCreator.class);
	/**
	 * NON_ASCII_CHARACTERS.
	 * In ASCII, codes 20 hex to 7E hex, known as the printable characters, represent letters, digits,
	 * punctuation marks, and a few miscellaneous symbols.
	 */
	private static final String NON_ASCII_CHARACTERS = "[^\\x20-\\x7E]";
	
    @Autowired
    AbstractBatchUploadValidator serviceCaseSignValidator;
    @Autowired
    private SellingUnitRepository sellingUnitRepository;
    @Autowired
    private TagTypeRepository tagTypeRepository;

    @Override
    public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes,long transactionId, String userId) {
        ServiceCaseSignBatchUpload serviceCaseSignBatchUpload = this.createServiceCaseSignBatchUpload(cellValues);
        try {
            this.serviceCaseSignValidator.validateRow(serviceCaseSignBatchUpload);
        }catch (UnexpectedInputException e){
            serviceCaseSignBatchUpload.setProductId(null);
            serviceCaseSignBatchUpload.setUpc(null);
        }
        CandidateWorkRequest candidateWorkRequest =
                WorkRequestCreatorUtils.getEmptyWorkRequest(
                        serviceCaseSignBatchUpload.getProductId(),StringUtils.isBlank(serviceCaseSignBatchUpload.getUpc())?null:Long.valueOf(serviceCaseSignBatchUpload.getUpc()),userId,transactionId,
                        CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT,serviceCaseSignBatchUpload.getServiceCaseSignStatusCode());

        if(serviceCaseSignBatchUpload.hasErrors()){
            candidateWorkRequest.setIntent(CandidateWorkRequest.INTENT.EXCEL_UPLOAD.getName());
            this.setDataToCandidateStat(candidateWorkRequest,serviceCaseSignBatchUpload);
        } else {
            candidateWorkRequest.setIntent(CandidateWorkRequest.INTENT.APPROVE_PROCESS.getName());
            // there is no tracking id for work request of approval intent id.
            candidateWorkRequest.setTrackingId(null);
            //set the UploadServeCaseSign to true when there is no error
            candidateWorkRequest.setUploadServeCaseSign(true);
            lookupProductId(serviceCaseSignBatchUpload);
            candidateWorkRequest.setProductId(NumberUtils.toLong(serviceCaseSignBatchUpload.getProductId()));
            this.setDataToCandidateProductMaster(candidateWorkRequest,serviceCaseSignBatchUpload);
            this.setDataToCandidateDescription(candidateWorkRequest.getCandidateProductMaster().get(0),serviceCaseSignBatchUpload);
            this.setDataToCandidateStat(candidateWorkRequest,serviceCaseSignBatchUpload);
        }
        return candidateWorkRequest;
    }
    /**
     * set Data To CandidateStat.
     * @param serviceCaseSignBatchUpload
     *            ServiceCaseSignBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     * @author vn55306
     */
    public void setDataToCandidateStat(CandidateWorkRequest candidateWorkRequest, ServiceCaseSignBatchUpload serviceCaseSignBatchUpload){
        String errorMessage = serviceCaseSignBatchUpload.hasErrors()? serviceCaseSignBatchUpload.getErrors().get(0) : StringUtils.EMPTY;
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey candidateStatKey =new CandidateStatusKey();
        candidateStatKey.setStatus(this.getBatchStatus(serviceCaseSignBatchUpload).getName());
        candidateStatKey.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(candidateStatKey);
        candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(errorMessage);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }
    /**
     * se tData To CandidateProductMaster.
     * @param serviceCaseSignBatchUpload
     *            ServiceCaseSignBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     * @author vn55306
     */
    private void setDataToCandidateProductMaster(CandidateWorkRequest candidateWorkRequest, ServiceCaseSignBatchUpload serviceCaseSignBatchUpload) {
            CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
            candidateProductMaster.setProductId(serviceCaseSignBatchUpload.getProductId()==null?null:Long.valueOf(serviceCaseSignBatchUpload.getProductId()));
            candidateProductMaster.setNewDataSw(true);
            candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setPrprcForNbr(1);
            candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
            candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
            candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
            if (StringUtils.isNotBlank(serviceCaseSignBatchUpload.getTagType())) {
                TagType tagType = tagTypeRepository.findFirstByTagTypeDescription(StringUtils.trim(serviceCaseSignBatchUpload.getTagType()));
                candidateProductMaster.setTagType(tagType == null ? null : tagType.getTagTypeCode());
            }
            candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
            candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
    }
    /**
     * set Data To CandidateDescription.
     * @param batchUploadServiceCaseSign
     *           the ServiceCaseSignBatchUpload
     * @param  candidateProductMaster
     *          the CandidateProductMaster
     * @author vn55306
     */
    private  void setDataToCandidateDescription(CandidateProductMaster candidateProductMaster, ServiceCaseSignBatchUpload batchUploadServiceCaseSign){
        candidateProductMaster.setCandidateDescriptions(new ArrayList<CandidateDescription>()) ;
        CandidateDescription candidateDescription ;
        CandidateDescriptionKey key;
        if (StringUtils.isNotBlank(batchUploadServiceCaseSign.getApprovedServiceCaseSign())) {
              candidateDescription = new CandidateDescription();
              key = new CandidateDescriptionKey();
             key.setLanguageType(CandidateDescriptionKey.LANGUAGE_ENGLISH);
             key.setDescriptionType(CandidateDescriptionKey.DESCRIPTION_TYPE_SGNRC);
             candidateDescription.setKey(key);
             candidateDescription.setLastUpdateUserId(candidateProductMaster.getLstUpdtUsrId());
             candidateDescription.setLastUpdateDate(LocalDateTime.now());
             candidateDescription.setDescription(this.removeNonAsciiCharacters(batchUploadServiceCaseSign.getApprovedServiceCaseSign()));
             candidateDescription.setCandidateProductMaster(candidateProductMaster);
             candidateProductMaster.getCandidateDescriptions().add(candidateDescription);
        }
        if (StringUtils.isNotBlank(batchUploadServiceCaseSign.getProposedServiceCaseSign())) {
             candidateDescription = new CandidateDescription();
             key = new CandidateDescriptionKey();
            key.setLanguageType(CandidateDescriptionKey.LANGUAGE_ENGLISH);
            key.setDescriptionType(CandidateDescriptionKey.DESCRIPTION_TYPE_SGNRP);
            candidateDescription.setKey(key);
            candidateDescription.setLastUpdateUserId(candidateProductMaster.getLstUpdtUsrId());
            candidateDescription.setLastUpdateDate(LocalDateTime.now());
            candidateDescription.setDescription(this.removeNonAsciiCharacters(batchUploadServiceCaseSign.getProposedServiceCaseSign()));
            candidateDescription.setCandidateProductMaster(candidateProductMaster);
            candidateProductMaster.getCandidateDescriptions().add(candidateDescription);
        }
        if (StringUtils.isNotBlank(batchUploadServiceCaseSign.getServiceCaseCallout())) {
            candidateDescription = new CandidateDescription();
            key = new CandidateDescriptionKey();
            key.setLanguageType(CandidateDescriptionKey.LANGUAGE_ENGLISH);
            key.setDescriptionType(CandidateDescriptionKey.DESCRIPTION_TYPE_SRVCC);
            candidateDescription.setKey(key);
            candidateDescription.setLastUpdateUserId(candidateProductMaster.getLstUpdtUsrId());
            candidateDescription.setLastUpdateDate(LocalDateTime.now());
            candidateDescription.setDescription(this.removeNonAsciiCharacters(batchUploadServiceCaseSign.getServiceCaseCallout()));
            candidateDescription.setCandidateProductMaster(candidateProductMaster);
            candidateProductMaster.getCandidateDescriptions().add(candidateDescription);
        }

    }

    /**
     * Remove non-ascii characters.
     *
     * @param text - The string has non-ascii characters.
     * @return The string hasn't non-ascii characters.
     */
    private String removeNonAsciiCharacters(String text) {
    	if(StringUtils.isNotEmpty(text)){
    		text = text.replaceAll(NON_ASCII_CHARACTERS, StringUtils.EMPTY);
    	}
    	return StringUtils.trim(text);
    }

    /**
     * look up product id for upc on HEB. will return error if upc invalid or product does not exist on HEB
     *
     * @param serviceCaseSignBatchUpload
     */
    private void lookupProductId(ServiceCaseSignBatchUpload serviceCaseSignBatchUpload) {
        SellingUnit sellingUnit = sellingUnitRepository.findOne(NumberUtils.toLong(serviceCaseSignBatchUpload.getUpc(), 0L));
        serviceCaseSignBatchUpload.setProductId(String.valueOf(sellingUnit.getProdId()));
    }

    /**
     * create ServiceCaseSignBatchUpload.
     * @param cellValues
     *            List<String>
     * @author vn55306
     */
    private ServiceCaseSignBatchUpload createServiceCaseSignBatchUpload(List<String> cellValues){
        ServiceCaseSignBatchUpload ret = new ServiceCaseSignBatchUpload();
        String value = StringUtils.EMPTY;
        for (int j = 0; j < cellValues.size(); j++) {
            value = cellValues.get(j);
            switch(j){
                case ServiceCaseSignBatchUpload.COL_NM_UPC:
                    ret.setUpc(value);
                    break;
                case ServiceCaseSignBatchUpload.COL_NM_PRODUCT_ID:
                    ret.setProductId(value);
                    break;
                case ServiceCaseSignBatchUpload.COL_NM_PROPOSED_SERVICE_CASE_SIGN_DESCRIPTION:
                    ret.setProposedServiceCaseSign(value);
                    break;
                case ServiceCaseSignBatchUpload.COL_NM_APPROVED_SERVICE_CASE_SIGN_DESCRIPTION:
                    ret.setApprovedServiceCaseSign(value);
                    break;
                case ServiceCaseSignBatchUpload.COL_NM_SERVICE_CASE_STATUS:
                    ret.setServiceCaseSignStatus(value);
                    break;
                case ServiceCaseSignBatchUpload.COL_NM_TAG_TYPE:
                    ret.setTagType(value);
                    break;
                case ServiceCaseSignBatchUpload.COL_NM_SERVICE_CASE_CALLOUT:
                    ret.setServiceCaseCallout(value);
                    break;
            }
        }
        return ret;
    }

    }

