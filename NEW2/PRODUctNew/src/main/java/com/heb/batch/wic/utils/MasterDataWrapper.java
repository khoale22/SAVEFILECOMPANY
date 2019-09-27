/*
 * MasterDataWrapper
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.utils;

import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import com.heb.batch.wic.index.TexasStateDocument;
import com.heb.batch.wic.webservice.vo.ProductScanCodeWicVO;
import com.heb.batch.wic.webservice.vo.WicCategoryVO;
import com.heb.batch.wic.webservice.vo.WicSubCategoryVO;
import org.apache.commons.lang3.math.NumberUtils;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Represents a wrapper contains other objects.
 *
 * @author vn03512
 * @since 1.0.0
 */
public class MasterDataWrapper {
    private ProductScanCodeWicVO productScanCodeWic;
    private WicCategoryVO wicCategory;
    private WicSubCategoryVO wicSubCategory;

    /**
     * Create MasterDataWrapper
     */
    public MasterDataWrapper() {
    }

    /**
     * Create MasterDataWrapper and set ProductScanCodeWicVO, WicCategoryVO, WicSubCategoryVO
     *
     * @param productScanCodeWic the ProductScanCodeWicVO
     * @param wicCategory        the WicCategoryVO
     * @param wicSubCategory     the WicSubCategoryVO
     */
    public MasterDataWrapper(ProductScanCodeWicVO productScanCodeWic,
                             WicCategoryVO wicCategory, WicSubCategoryVO wicSubCategory) {
        this.productScanCodeWic = productScanCodeWic;
        this.wicCategory = wicCategory;
        this.wicSubCategory = wicSubCategory;
    }

    /**
     * Get the ProductScanCodeWic
     *
     * @return the ProductScanCodeWic
     */
    public ProductScanCodeWicVO getProductScanCodeWic() {
        return productScanCodeWic;
    }

    /**
     * Set the ProductScanCodeWic
     *
     * @param productScanCodeWic the ProductScanCodeWic
     */
    public void setProductScanCodeWic(ProductScanCodeWicVO productScanCodeWic) {
        this.productScanCodeWic = productScanCodeWic;
    }

    /**
     * Get the WicCategory
     *
     * @return the WicCategory
     */
    public WicCategoryVO getWicCategory() {
        return wicCategory;
    }

    /**
     * Set the WicCategory
     *
     * @param wicCategory the WicCategory
     */
    public void setWicCategory(WicCategoryVO wicCategory) {
        this.wicCategory = wicCategory;
    }

    /**
     * Get the WicSubCategory
     *
     * @return wicSubCategory
     */
    public WicSubCategoryVO getWicSubCategory() {
        return wicSubCategory;
    }

    /**
     * Set the WicSubCategory
     *
     * @param wicSubCategory the WicSubCategory
     */
    public void setWicSubCategory(WicSubCategoryVO wicSubCategory) {
        this.wicSubCategory = wicSubCategory;
    }

    
    /**
     * Create and set ProductScanCodeWic by TexasStateDocument
     *
     * @param texasStateDocument the ProductScanCodeWicDocument
     */
    public void setProductScanCodeWicVOTexasStateDocument(TexasStateDocument texasStateDocument, String action) {
        ProductScanCodeWicVO result = new ProductScanCodeWicVO();
        result.setScnCdId(Long.valueOf(texasStateDocument.getScnCdId()));
        result.setWicAplId(Long.valueOf(texasStateDocument.getWicAplId()));
        result.setWicCatId(Long.valueOf(texasStateDocument.getWicCatId()));
        result.setWicSubCatId(Long.valueOf(texasStateDocument.getWicSubCatId()));
        result.setEffDt(WicUtil.convertDateFromString(texasStateDocument.getEffDt()));
        result.setEndDt(WicUtil.convertDateFromString(WicUtil.getDateOrDefault(texasStateDocument.getEndDt())));
        result.setWicUntTxt(texasStateDocument.getWicUntTxt());
        result.setWicBnftQty(WicUtil.parseStringToDouble(texasStateDocument.getWicBnFtQty(), 3));
        result.setWicBnftUntTxt(texasStateDocument.getWicBnftUntTxt());
        result.setWicPrcAmt(WicUtil.parseStringToDouble(texasStateDocument.getWicPrcAmt(), 3));
        result.setWicPrcCd(texasStateDocument.getWicPrcCd());
        result.setWicCrdAcptId(texasStateDocument.getWicCrdAcptId());
        result.setWicProdDes(texasStateDocument.getWicProdDes());
        result.setWicPkgSzQty(WicUtil.parseStringToDouble(texasStateDocument.getWicPkgSzQty(), 3));
        Timestamp current = new Timestamp(System.currentTimeMillis());
        result.setLebSw(WicConstants.NO);
        result.setCre8Ts(current);
        result.setCre8Uid(WicConstants.TXSTATE_USER);
        result.setLstUpdtTs(current);
        result.setLstUpdtUid(WicConstants.TXSTATE_USER);
        WicUtil.setDefaultValueProductScanCodeWic(result);
        result.setActionCode(action);
        result.setSystemEnvironment(WicConstants.BATCH_ENVIRONMENT);
        if(WicUtil.checkStageEventTrigger(texasStateDocument)){
            result.setStageEvent(WicConstants.YES);
        }
        result.setUpcPluLength(Integer.valueOf(texasStateDocument.getUpcPluLength()));
        result.setPurchaseIndicator(Integer.valueOf(texasStateDocument.getPurchaseIndicator()));
        result.setManualVoucherIndicator(texasStateDocument.getManualVoucherIndicator());
        this.productScanCodeWic = result;
    }

	/**
     * Check if the ProductScanCodeWic record has changed data.
     * 
	 * @param item the new data
	 * @param pscwDocument the old data
	 * @return True if the ProductScanCodeWic record has changed data.
	 */
	public boolean setProductScanCodeWicChange(TexasStateDocument item, ProductScanCodeWicDocument pscwDocument) {
        boolean flagChange = false;
        this.productScanCodeWic = new ProductScanCodeWicVO();
        double priceValue = 0;
        if(!pscwDocument.getWicDescription().trim().equals(item.getWicProdDes().trim())){
            this.productScanCodeWic.setWicProdDes(item.getWicProdDes().trim());
            flagChange = true;
        }
        priceValue = WicUtil.parseStringToDouble(item.getWicPkgSzQty(), 3);
        if(!pscwDocument.getWicPackageSize().equals(priceValue)){
            this.productScanCodeWic.setWicPkgSzQty(priceValue);
            flagChange = true;
        }
        if(!pscwDocument.getEffDt().equals(WicUtil.convertDateFromString(item.getEffDt()))){
            this.productScanCodeWic.setEffDt(WicUtil.convertDateFromString(item.getEffDt()));
            flagChange = true;
        }
        Date endDate = WicUtil.convertDateFromString(WicUtil.getDateOrDefault(item.getEndDt()));
        if(!pscwDocument.getEndDt().equals(endDate)) {
            this.productScanCodeWic.setEndDt(endDate);
            flagChange = true;
        }
        if(!pscwDocument.getWicUntTxt().trim().equals(item.getWicUntTxt().trim())){
            this.productScanCodeWic.setWicUntTxt(item.getWicUntTxt());
            flagChange = true;
        }
        priceValue = WicUtil.parseStringToDouble(item.getWicBnFtQty(), 3);
        if(!pscwDocument.getWicBnFtQty().equals(priceValue)){
            this.productScanCodeWic.setWicBnftQty(priceValue);
            flagChange = true;
        }
        if(!pscwDocument.getWicBnftUntTxt().trim().equals(item.getWicBnftUntTxt().trim())){
            this.productScanCodeWic.setWicBnftUntTxt(item.getWicBnftUntTxt());
            flagChange = true;
        }
        priceValue = WicUtil.parseStringToDouble(item.getWicPrcAmt(), 3);
        if(!pscwDocument.getWicPrcAmt().equals(priceValue)){
            this.productScanCodeWic.setWicPrcAmt(priceValue);
            flagChange = true;
        }
        if(!pscwDocument.getWicPrcCd().trim().equals(item.getWicPrcCd().trim())){
            this.productScanCodeWic.setWicPrcCd(item.getWicPrcCd());
            flagChange = true;
        }
        if(!pscwDocument.getWicCrdAcptId().trim().equals(item.getWicCrdAcptId().trim())){
            this.productScanCodeWic.setWicCrdAcptId(item.getWicCrdAcptId());
            flagChange = true;
        }
        if(!pscwDocument.getUpcPluLength().equals(NumberUtils.toInt(item.getUpcPluLength()))){
            this.productScanCodeWic.setUpcPluLength(NumberUtils.toInt(item.getUpcPluLength()));
            flagChange = true;
        }
        if(!pscwDocument.getPurchaseIndicator().equals(NumberUtils.toInt(item.getPurchaseIndicator()))){
        	this.productScanCodeWic.setPurchaseIndicator(NumberUtils.toInt(item.getPurchaseIndicator()));
        	flagChange = true;
        }
        if(!pscwDocument.getManualVoucherIndicator().trim().equals(item.getManualVoucherIndicator().trim())){
            this.productScanCodeWic.setManualVoucherIndicator(item.getManualVoucherIndicator());
            flagChange = true;
        }
        if(flagChange) {
            boolean flagStageEvent = WicUtil.checkStageEventTrigger(item);
            this.setFlagStageEvent(flagStageEvent, pscwDocument);
        }
	    return flagChange;

	}

	private void setFlagStageEvent(boolean flagStageEvent,ProductScanCodeWicDocument pscwDocument){
	    // set key and last user update
        this.productScanCodeWic.setActionCode(WicConstants.UPDATE);
        this.productScanCodeWic.setScnCdId(pscwDocument.getUpc());
        this.productScanCodeWic.setWicAplId(pscwDocument.getWicAplId());
        this.productScanCodeWic.setWicCatId(pscwDocument.getWicCategoryId());
        this.productScanCodeWic.setWicSubCatId(pscwDocument.getWicSubCategoryId());
        this.productScanCodeWic.setLstUpdtUid(WicConstants.TXSTATE_USER);
        this.productScanCodeWic.setLstUpdtTs(WicUtil.getCurrentTimeStamp());
        this.productScanCodeWic.setSystemEnvironment(WicConstants.BATCH_ENVIRONMENT);
        if(flagStageEvent){
            this.productScanCodeWic.setStageEvent(WicConstants.YES);
        }
    }

	/**
     * Create and set WicCategory by TexasStateDocument
     *
     * @param item the TexasStateDocument
     */
    public void newWicCategory(TexasStateDocument item) {
        WicCategoryVO result = new WicCategoryVO();
        if(item.getWicCatId() != null) {
            result.setWicCatId(Integer.valueOf(item.getWicCatId()));
            result.setWicCatDes(item.getWicCategoryDesc());
            result.setActionCode(WicConstants.ADD);
            result.setSystemEnvironment(WicConstants.BATCH_ENVIRONMENT);
        }
        this.wicCategory = result;
    }

    /**
     * Create and set WicSubCategory by TexasStateDocument
     *
     * @param item the TexasStateDocument
     */
    public void newWicSubCategory(TexasStateDocument item, String ActionCode) {
        WicSubCategoryVO result = new WicSubCategoryVO();
        result.setWicCatId(Integer.valueOf(item.getWicCatId()));
        result.setWicSubCatId(Integer.valueOf(item.getWicSubCatId()));
        result.setWicSubCatDes(item.getWicSubCategoryDesc());
        result.setLebSw(WicConstants.NO);
        result.setActionCode(ActionCode);
        result.setSystemEnvironment(WicConstants.BATCH_ENVIRONMENT);
        this.wicSubCategory = result;
    }

}