/*
 * AssortmentCandidateWorkRequestCreator
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.assortment;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This is template for AssortmentCandidateWorkRequestCreator.
 *
 * @author vn55306
 * @since 2.8.0
 */
@Service
public class AssortmentCandidateWorkRequestCreator extends CandidateWorkRequestCreator {
    private static final Logger logger = LoggerFactory.getLogger(AssortmentCandidateWorkRequestCreator.class);
    public static final String DATE_MM_DD_YYYY = "MM/dd/yyyy";
    public  static final int MIN_ORDER_QTY_DISCOUNT_TYPE_LENGTH = 1;
    public  static final int FULFILLMENT_CHANNEL_CODE_LENGTH = 2;
    @Autowired
    AbstractBatchUploadValidator assortmentValidator;
    @Override
    public CandidateWorkRequest createRequest(List<String> cellValues, List<ProductAttribute> productAttributes,long transactionId, String userId){
        AssortmentBatchUpload assortmentBatchUpload = this.createAssortmentBatchUpload(cellValues);
        //format data (any speciall data like code-value)
        this.formatData(assortmentBatchUpload);
        try {
            this.assortmentValidator.validateRow(assortmentBatchUpload);
        }catch (UnexpectedInputException e){
             //set product id null in case user input invalid value for Product Id
            assortmentBatchUpload.setProductId(null);
        }
        CandidateWorkRequest candidateWorkRequest = WorkRequestCreatorUtils.getEmptyWorkRequest(
                    assortmentBatchUpload.getProductId(),null,
                    userId,
                    transactionId,
                    CandidateWorkRequest.SRC_SYSTEM_ID_DEFAULT,
                    this.getBatchStatus(assortmentBatchUpload).getName());
            if (!assortmentBatchUpload.hasErrors()) {
                this.setCandidateProductMaster(candidateWorkRequest, assortmentBatchUpload);

                this.setFulfillmentChannel(candidateWorkRequest,candidateWorkRequest.getCandidateProductMaster().get(0), assortmentBatchUpload);
                this.setMinCustomerOrderQty(candidateWorkRequest.getCandidateProductMaster().get(0), assortmentBatchUpload);
                this.setMinOrderQtyDiscount(candidateWorkRequest.getCandidateProductMaster().get(0), assortmentBatchUpload);
                this.setCandidateStatus(candidateWorkRequest, assortmentBatchUpload);
            } else {
                this.setCandidateStatus(candidateWorkRequest, assortmentBatchUpload);
            }
            return candidateWorkRequest;
    }

    /**
     * set Candidate ProductMaster.
     * @param assortmentBatchUpload
     *            AssortmentBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     * @author vn55306
     */
    private void setCandidateProductMaster(CandidateWorkRequest candidateWorkRequest, AssortmentBatchUpload assortmentBatchUpload){
             CandidateProductMaster candidateProductMaster = new CandidateProductMaster();
            candidateProductMaster.setProductId(StringUtils.isNotEmpty(assortmentBatchUpload.getProductId()) ? Long.valueOf(assortmentBatchUpload.getProductId()):null);
            candidateProductMaster.setNewDataSw(true);
            candidateProductMaster.setPackagingText(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setPrprcForNbr(1);
            candidateProductMaster.setLstUpdtUsrId(candidateWorkRequest.getUserId());
            candidateProductMaster.setLastUpdateTs(LocalDateTime.now());
            candidateProductMaster.setCriticalItem(CandidateProductMaster.STRING_DEFAULT_BLANK);
            candidateProductMaster.setMinCustOrdQty(StringUtils.isNotEmpty(assortmentBatchUpload.getMinCustomerOrderQuantity())?BigDecimal.valueOf(Long.valueOf(assortmentBatchUpload.getMinCustomerOrderQuantity())):BigDecimal.ZERO);
            candidateProductMaster.setCandidateWorkRequest(candidateWorkRequest);
            candidateWorkRequest.setCandidateProductMaster(new ArrayList<CandidateProductMaster>());
            candidateWorkRequest.getCandidateProductMaster().add(candidateProductMaster);
    }
    /**
     * set CandidateStatus.
     * @param assortmentBatchUpload
     *            AssortmentBatchUpload
     * @param candidateWorkRequest
     *           CandidateWorkRequest
     * @author vn55306
     */
    private void setCandidateStatus(CandidateWorkRequest candidateWorkRequest, AssortmentBatchUpload assortmentBatchUpload){
        candidateWorkRequest.setCandidateStatuses(new ArrayList<CandidateStatus>());
        CandidateStatus candidateStatus = new CandidateStatus();
        CandidateStatusKey candidateStatKey =new CandidateStatusKey();
        candidateStatKey.setStatus(this.getBatchStatus(assortmentBatchUpload).getName());
        candidateStatKey.setLastUpdateDate(LocalDateTime.now());
        candidateStatus.setKey(candidateStatKey);
        candidateStatus.setUpdateUserId(candidateWorkRequest.getUserId());
        candidateStatus.setStatusChangeReason(CandidateStatus.STAT_CHG_RSN_ID_WRKG);
        candidateStatus.setCommentText(assortmentBatchUpload.hasErrors()?assortmentBatchUpload.getErrors().get(0):StringUtils.EMPTY);
        candidateStatus.setCandidateWorkRequest(candidateWorkRequest);
        candidateWorkRequest.getCandidateStatuses().add(candidateStatus);
    }

    /**
     * set FulfillmentChannel.
     * @param candidateWorkRequest
     *            The CandidateWorkRequest
     * @param candidateProductMaster
     *           The CandidateProductMaster
     * @param assortmentBatchUpload
     *           The AssortmentBatchUpload
     * @author vn55306
     */
    private  void setFulfillmentChannel(CandidateWorkRequest candidateWorkRequest, CandidateProductMaster candidateProductMaster, AssortmentBatchUpload assortmentBatchUpload){
        if (StringUtils.isNotBlank(assortmentBatchUpload.getFulfillmentProgram())) {
            CandidateFulfillmentChannel candidateProductFullfillChanel = new CandidateFulfillmentChannel();
            CandidateFulfillmentChannelKey key = new CandidateFulfillmentChannelKey();
            key.setSalesChannelCode(assortmentBatchUpload.getSalesChanelCode());
            key.setFulfillmentChannelCode(assortmentBatchUpload.getFulfillmentProgram());
             candidateProductFullfillChanel.setKey(key);
            candidateProductFullfillChanel.setEffectiveDate(DateUtils.getLocalDate(assortmentBatchUpload.getEffectiveStartDate(),DATE_MM_DD_YYYY));
            candidateProductFullfillChanel.setExpirationDate(DateUtils.getLocalDate(assortmentBatchUpload.getEffectiveEndDate(),DATE_MM_DD_YYYY));
            candidateProductFullfillChanel.setNewData("Y");
            candidateProductFullfillChanel.setCandidateProductMaster(candidateProductMaster);
            candidateProductFullfillChanel.setCandidateWorkRequest(candidateWorkRequest);
            candidateProductMaster.setCandidateFulfillmentChannels(new ArrayList<CandidateFulfillmentChannel>());
            candidateProductMaster.getCandidateFulfillmentChannels().add(candidateProductFullfillChanel);
        }
    }
    /**
     * set MinCustomerOrderQty.
     * @param candidateProductMaster
     *           The CandidateProductMaster
     * @param assortmentBatchUpload
     *           The AssortmentBatchUpload
     * @author vn55306
     */
    private void setMinCustomerOrderQty(CandidateProductMaster candidateProductMaster, AssortmentBatchUpload assortmentBatchUpload) {
        BigDecimal minCustomerOrderQty = BigDecimal.ZERO; //Is it required to insert this even if user didnt enter anything ?
        if (StringUtils.isNotEmpty(assortmentBatchUpload.getMinCustomerOrderQuantity())) {
            minCustomerOrderQty = org.springframework.util.NumberUtils.parseNumber(assortmentBatchUpload.getMinCustomerOrderQuantity(), BigDecimal.class);
            candidateProductMaster.setMinCustOrdQty(minCustomerOrderQty);
        }
    }
    /**
     * set MinOrderQtyDiscount.
     * @param candidateProductMaster
     *           The CandidateProductMaster
     * @param assortmentBatchUpload
     *           The AssortmentBatchUpload
     * @author vn55306
     */
    private void setMinOrderQtyDiscount(CandidateProductMaster candidateProductMaster, AssortmentBatchUpload assortmentBatchUpload) {
         if (StringUtils.isNotBlank(assortmentBatchUpload.getMinOrderQuantityForDiscount())) {
            CandidateDiscountThreshold candidateProductDiscThrh = new CandidateDiscountThreshold();
            CandidateDiscountThresholdKey key = new CandidateDiscountThresholdKey();
            key.setEffectiveDate(DateUtils.getLocalDate(assortmentBatchUpload.getEffectiveStartDate(),DATE_MM_DD_YYYY));
            key.setMinDiscountThresholdQuantity(Long.valueOf(assortmentBatchUpload.getMinOrderQuantityForDiscount()));
            candidateProductDiscThrh.setKey(key);
            candidateProductDiscThrh.setThresholdDiscountType(assortmentBatchUpload.getMinOrderQuantityDiscountType());
            candidateProductDiscThrh.setThresholdDiscountAmount(Double.valueOf(assortmentBatchUpload.getMinOrderQuantityDiscountValue()));
            candidateProductDiscThrh.setCandidateProductMaster(candidateProductMaster);
            candidateProductMaster.setCandidateDiscountThresholds(new ArrayList<CandidateDiscountThreshold>());
            candidateProductMaster.getCandidateDiscountThresholds().add(candidateProductDiscThrh);
        }
    }
    /**
     * convert data from row to AssortmentBatchUpload.
     * @param cellValues
     *            List<String>
     * @return AssortmentBatchUpload
     * @author vn55306
     */
    private AssortmentBatchUpload createAssortmentBatchUpload(List<String> cellValues) {
        AssortmentBatchUpload ret = new AssortmentBatchUpload();
        String value;
        for (int columnCounter = 0; columnCounter < cellValues.size(); columnCounter++) {
            value = cellValues.get(columnCounter);
            switch (columnCounter) {
                case AssortmentBatchUpload.POSITION_COLUMN_PRODUCT_ID: {
                    ret.setProductId(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_FULFILLMENT_PROGRAM: {
                    ret.setFulfillmentProgram(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_EFFECTIVE_START_DATE: {
                    ret.setEffectiveStartDate(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_EFFECTIVE_END_DATE: {
                    ret.setEffectiveEndDate(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_MIN_CUSTOMER_ORDER_QUANTITY: {
                    ret.setMinCustomerOrderQuantity(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_MIN_ORDER_QUANTITY_FOR_DISCOUNT: {
                    ret.setMinOrderQuantityForDiscount(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_MIN_ORDER_QUANTITY_DISCOUNT_TYPE: {
                    ret.setMinOrderQuantityDiscountType(value);
                    break;
                }
                case AssortmentBatchUpload.POSITION_COLUMN_MIN_ORDER_QUANTITY_DISCOUNT_VALUE: {
                    ret.setMinOrderQuantityDiscountValue(value);
                    break;
                }
            }
        }
        ret.setSalesChanelCode(AssortmentBatchUpload.SALES_CHANEL_CODE_DEFAULT_BATCH_UPLOAD);
        return ret;
    }
    /**
     * formatData.
     * @param assortmentBatchUpload
     *            The AssortmentBatchUpload
     * @author vn55306
     */
    private void formatData(AssortmentBatchUpload assortmentBatchUpload) {
        if (StringUtils.isNotBlank(assortmentBatchUpload.getFulfillmentProgram())) {
            assortmentBatchUpload.setFulfillmentProgram(
                    this.getCode(assortmentBatchUpload.getFulfillmentProgram(), FULFILLMENT_CHANNEL_CODE_LENGTH));
        }
        if (StringUtils.isNotBlank(assortmentBatchUpload.getMinOrderQuantityDiscountType())) {
            assortmentBatchUpload.setMinOrderQuantityDiscountType(
                    this.getCode(assortmentBatchUpload.getMinOrderQuantityDiscountType(), MIN_ORDER_QTY_DISCOUNT_TYPE_LENGTH));

        }
    }
    /**
     * get Code from cell.
     * @param codeValue
     *            String
     * @param endIndex
     *            int
     * @author vn55306
     */
    private String getCode(String codeValue, int endIndex) {
        return codeValue.substring(0, endIndex);
    }


}
