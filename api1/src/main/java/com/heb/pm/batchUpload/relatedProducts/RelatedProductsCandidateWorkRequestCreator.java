/*
 * RelatedProductsCandidateWorkRequestCreator
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.relatedProducts;

import com.heb.pm.batchUpload.UnexpectedInputException;
import com.heb.pm.batchUpload.parser.CandidateWorkRequestCreator;
import com.heb.pm.batchUpload.AbstractBatchUploadValidator;
import com.heb.pm.batchUpload.parser.ProductAttribute;
import com.heb.pm.entity.*;
import com.heb.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heb.pm.batchUpload.parser.WorkRequestCreatorUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This is template for RelatedProductsCandidateWorkRequestCreator.
 *
 * @author vn73545
 * @since 2.20.0
 */
@Service
public class RelatedProductsCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
	private static final Logger logger = LoggerFactory.getLogger(RelatedProductsCandidateWorkRequestCreator.class);
	private static final String REQUIRED_PRODUCT_ID_MESSAGE = "Product ID is a mandatory field";
	private static final String INVALID_PRODUCT_ID_MESSAGE = "Product ID is invalid";
	private static final String REQUIRED_RELATED_PRODUCT_MESSAGE = "Related product cannot be null or zero";
	private static final String INVALID_RELATED_PRODUCT_MESSAGE = "Related product is invalid";

    @Override
    public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes,long transactionId, String userId){
    	RelatedProductsBatchUpload relatedProductsBatchUpload = this.createRelatedProductsBatchUpload(cellValues);
    	this.validateRelatedProducts(relatedProductsBatchUpload);
        CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(
                    relatedProductsBatchUpload.getProductId(),
                    null,
                    userId,
                    transactionId,
                    CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT,
                    this.getBatchStatus(relatedProductsBatchUpload).getName());
        if (!relatedProductsBatchUpload.hasErrors()) {
        	this.setCandidateProductMaster(candidateWorkRequest, relatedProductsBatchUpload);
        	this.setCandidateStatus(candidateWorkRequest, relatedProductsBatchUpload);
        } else {
        	this.setCandidateStatus(candidateWorkRequest, relatedProductsBatchUpload);
        }
        return candidateWorkRequest;
    }
    /**
	 * Validate related products.
	 * @param relatedProductsBatchUpload The RelatedProductsBatchUpload.
	 */
    private void validateRelatedProducts(RelatedProductsBatchUpload relatedProductsBatchUpload) {
    	String productId = StringUtils.trim(relatedProductsBatchUpload.getProductId());
    	String relatedProductId = StringUtils.trim(relatedProductsBatchUpload.getRelatedProductId());
    	if(StringUtils.isEmpty(productId)){
    		relatedProductsBatchUpload.getErrors().add(REQUIRED_PRODUCT_ID_MESSAGE);
    		relatedProductsBatchUpload.setProductId(null);
    	}else if(!this.isInteger(productId)) {
    		relatedProductsBatchUpload.getErrors().add(INVALID_PRODUCT_ID_MESSAGE);
    		relatedProductsBatchUpload.setProductId(null);
    	}else if(StringUtils.isEmpty(relatedProductId)){
    		relatedProductsBatchUpload.getErrors().add(REQUIRED_RELATED_PRODUCT_MESSAGE);
    	}else if(!this.isInteger(relatedProductId)) {
    		relatedProductsBatchUpload.getErrors().add(INVALID_RELATED_PRODUCT_MESSAGE);
    	}
    }
    /**
	 * Validate limit integer.
	 * @param value
	 *            the Value
	 */
    private boolean isInteger(String value) {
		boolean flag=true;
		try {
			// check if valid integer number
			if (new BigInteger(value).compareTo(new BigInteger("999999999")) > 0) {
				flag = false;
			} else if (new BigInteger(value).compareTo(BigInteger.ZERO) <= 0) {
				flag = false;
			}
		}catch (NumberFormatException e){
			flag = false;
		}
		return flag;
	}
    /**
     * Set Candidate ProductMaster.
     * @param relatedProductsBatchUpload
     *            RelatedProductsBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     */
    private void setCandidateProductMaster(CandidateWorkRequest candidateWorkRequest, RelatedProductsBatchUpload relatedProductsBatchUpload){
    	CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
    	candidateProductMaster.setProductId(StringUtils.isNotEmpty(relatedProductsBatchUpload.getProductId()) ? Long.valueOf(relatedProductsBatchUpload.getProductId()):null);
    	candidateProductMaster.setSoldSeplySwitch(StringUtils.EMPTY);
    	candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
    	candidateProductMaster.setLastUpdateTs(LocalDateTime.now());

    	String relatedProductId = StringUtils.trim(relatedProductsBatchUpload.getRelatedProductId());
    	CandidateProductRelationshipKey key = new CandidateProductRelationshipKey();
    	key.setCandidateRelatedProductId(Long.valueOf(relatedProductId));
    	key.setProductRelationshipCode(ProductRelationship.ProductRelationshipCode.FIXED_RELATED_PRODUCT.getValue());
    	CandidateProductRelationship candidateProductRelationship = new CandidateProductRelationship();
    	candidateProductRelationship.setKey(key);
    	candidateProductRelationship.setQuantity(0D);
    	candidateProductRelationship.setLstUpdtUsrId(candidateWorkRequest.getUserId());
    	candidateProductRelationship.setLastUpdateTs(LocalDateTime.now());
    	candidateProductRelationship.setCandidateProductMaster(candidateProductMaster);
    	candidateProductMaster.setCandidateProductRelationships(new ArrayList<CandidateProductRelationship>());
    	candidateProductMaster.getCandidateProductRelationships().add(candidateProductRelationship);

    	candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
    	candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
    	candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
    }

    /**
     * Set Candidate Status.
     * @param relatedProductsBatchUpload
     *            RelatedProductsBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     */
    private void setCandidateStatus(CandidateWorkRequest candidateWorkRequest, RelatedProductsBatchUpload relatedProductsBatchUpload){
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey candidateStatKey =new CandidateStatusKey();
        candidateStatKey.setStatus(this.getBatchStatus(relatedProductsBatchUpload).getName());
        candidateStatKey.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(candidateStatKey);
        candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(relatedProductsBatchUpload.hasErrors()?relatedProductsBatchUpload.getErrors().get(0):StringUtils.EMPTY);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }

    /**
     * Convert data from row to RelatedProductsBatchUpload.
     * @param cellValues
     *            List<String>
     * @return RelatedProductsBatchUpload
     */
    private RelatedProductsBatchUpload createRelatedProductsBatchUpload(List<String> cellValues) {
        RelatedProductsBatchUpload ret = new RelatedProductsBatchUpload();
        String value;
        for (int columnCounter = 0; columnCounter < cellValues.size(); columnCounter++) {
            value = cellValues.get(columnCounter);
            switch (columnCounter) {
                case RelatedProductsBatchUpload.POSITION_COLUMN_PRODUCT_ID: {
                    ret.setProductId(value);
                    break;
                }
                case RelatedProductsBatchUpload.POSITION_COLUMN_RELATED_PRODUCT_ID: {
                	ret.setRelatedProductId(value);
                	break;
                }
            }
        }
        return ret;
    }
}
