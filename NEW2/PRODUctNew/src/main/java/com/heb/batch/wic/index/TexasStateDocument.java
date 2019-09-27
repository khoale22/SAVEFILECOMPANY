/*
 * BdmDocument
 *
 *  Copyright (c) 2016 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */
/*
 *  TexasStateDocument
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.index;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Wraps a TexasStateWicVO for storage in an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Document(indexName = "texas-state",type = "TexasState")
public class TexasStateDocument implements Cloneable{
	public static String PURCHASE_INDICATOR_ONE = "1";
	public static String BROADBAND_SUB_CAT_ID = "0";
	@Id
	private String id;
	@Field(type = FieldType.Text)
	private String wicAplId;
	@Field(type = FieldType.Text)
	private String aplPreFix;
    @Field(type = FieldType.Text)
	private String scnCdId;
	@Field(type = FieldType.Text)
	private String wicProdDes;
	@Field(type = FieldType.Text)
	private String wicCatId;
	@Field(type = FieldType.Text)
	private String wicCategoryDesc;
	@Field(type = FieldType.Text)
	private String wicSubCatId;
	@Field(type = FieldType.Text)
	private String wicSubCategoryDesc;
	@Field(type = FieldType.Text)
	private String wicUntTxt;
	@Field(type = FieldType.Text)
	private String wicPkgSzQty;
	@Field(type = FieldType.Text)
	private String wicBnFtQty;
	@Field(type = FieldType.Text)
	private String wicBnftUntTxt;
	@Field(type = FieldType.Text)
	private String wicPrcAmt;
	@Field(type = FieldType.Text)
	private String wicPrcCd;
	@Field(type = FieldType.Text)
	private String wicCrdAcptId;
	@Field(type = FieldType.Text)
	private String effDt;
	@Field(type = FieldType.Text)
	private String endDt;
	@Field(type = FieldType.Text)
	private String purchaseIndicator;
	@Field(type = FieldType.Text)
	private String upcPluLength;
	@Field(type = FieldType.Text)
	private String manualVoucherIndicator;
	private String upcCheckDigit;
	private String idCode;
	private String sequenceNumber;
	private String messageId;
	private String errorMessage = StringUtils.EMPTY;
	private String dataRaw;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getWicAplId() {
		return wicAplId;
	}

	public void setWicAplId(String wicAplId) {
		this.wicAplId = wicAplId;
	}

	public String getScnCdId() {
		return scnCdId;
	}

	public void setScnCdId(String scnCdId) {
		this.scnCdId = scnCdId;
	}

	public String getWicProdDes() {
		return wicProdDes;
	}

	public void setWicProdDes(String wicProdDes) {
		this.wicProdDes = wicProdDes;
	}

	public String getWicCatId() {
		return wicCatId;
	}

	public void setWicCatId(String wicCatId) {
		this.wicCatId = wicCatId;
	}

	public String getWicCategoryDesc() {
		return wicCategoryDesc;
	}

	public void setWicCategoryDesc(String wicCategoryDesc) {
		this.wicCategoryDesc = wicCategoryDesc;
	}

	public String getWicSubCatId() {
		return wicSubCatId;
	}

	public void setWicSubCatId(String wicSubCatId) {
		this.wicSubCatId = wicSubCatId;
	}

	public String getWicSubCategoryDesc() {
		return wicSubCategoryDesc;
	}

	public void setWicSubCategoryDesc(String wicSubCategoryDesc) {
		this.wicSubCategoryDesc = wicSubCategoryDesc;
	}

	public String getWicUntTxt() {
		return wicUntTxt;
	}

	public void setWicUntTxt(String wicUntTxt) {
		this.wicUntTxt = wicUntTxt;
	}

	public String getWicPkgSzQty() {
		return wicPkgSzQty;
	}

	public void setWicPkgSzQty(String wicPkgSzQty) {
		this.wicPkgSzQty = wicPkgSzQty;
	}

	public String getWicBnFtQty() {
		return wicBnFtQty;
	}

	public void setWicBnFtQty(String wicBnFtQty) {
		this.wicBnFtQty = wicBnFtQty;
	}

	public String getWicBnftUntTxt() {
		return wicBnftUntTxt;
	}

	public void setWicBnftUntTxt(String wicBnftUntTxt) {
		this.wicBnftUntTxt = wicBnftUntTxt;
	}

	public String getWicPrcAmt() {
		return wicPrcAmt;
	}

	public void setWicPrcAmt(String wicPrcAmt) {
		this.wicPrcAmt = wicPrcAmt;
	}

	public String getWicPrcCd() {
		return wicPrcCd;
	}

	public void setWicPrcCd(String wicPrcCd) {
		this.wicPrcCd = wicPrcCd;
	}

	public String getWicCrdAcptId() {
		return wicCrdAcptId;
	}

	public void setWicCrdAcptId(String wicCrdAcptId) {
		this.wicCrdAcptId = wicCrdAcptId;
	}

	public String getEffDt() {
		return effDt;
	}

	public void setEffDt(String effDt) {
		this.effDt = effDt;
	}

	public String getEndDt() {
		return endDt;
	}

	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}

	public String getUpcCheckDigit() {
		return upcCheckDigit;
	}

	public String getPurchaseIndicator() { return purchaseIndicator; }

	public void setPurchaseIndicator(String purchaseIndicator) { this.purchaseIndicator = purchaseIndicator; }

	public String getUpcPluLength() { return upcPluLength; }

	public void setUpcPluLength(String upcPluLength) { this.upcPluLength = upcPluLength; }

	public String getManualVoucherIndicator() { return manualVoucherIndicator; }

	public void setManualVoucherIndicator(String manualVoucherIndicator) { this.manualVoucherIndicator = manualVoucherIndicator; }

	public void setUpcCheckDigit(String upcCheckDigit) {
		this.upcCheckDigit = upcCheckDigit;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getDataRaw() {
		return this.dataRaw;
	}

	public void setDataRaw(String dataRaw) {
		this.dataRaw = dataRaw;
	}

	public String getAplPreFix() {
		return aplPreFix;
	}

	public void setAplPreFix(String aplPreFix) {
		this.aplPreFix = aplPreFix;
	}

	/**
	 * Generate Id by WicApprovedProductListId, UPC, WicCategoryId and WicSubCategoryId
	 *
	 * @param wicAplId the WicApprovedProductListId
	 * @param upc the UPC
	 * @param catId the WicCategoryId
	 * @param subCatId the WicSubCategoryId
	 * @return the ID
	 */
	public static String generateId(String wicAplId, String upc, String catId, String subCatId) {
		return String.format("%s%s%s%s", wicAplId, upc, catId, subCatId);
	}
	@Override
	public String toString() {
		return "TexasStateDocument{" +
				"id='" + id + '\'' +
				", wicAplId='" + wicAplId + '\'' +
				", aplPreFix='" + aplPreFix + '\'' +
				", scnCdId='" + scnCdId + '\'' +
				", wicProdDes='" + wicProdDes + '\'' +
				", wicCatId='" + wicCatId + '\'' +
				", wicCategoryDesc='" + wicCategoryDesc + '\'' +
				", wicSubCatId='" + wicSubCatId + '\'' +
				", wicSubCategoryDesc='" + wicSubCategoryDesc + '\'' +
				", wicUntTxt='" + wicUntTxt + '\'' +
				", wicPkgSzQty='" + wicPkgSzQty + '\'' +
				", wicBnFtQty='" + wicBnFtQty + '\'' +
				", wicBnftUntTxt='" + wicBnftUntTxt + '\'' +
				", wicPrcAmt='" + wicPrcAmt + '\'' +
				", wicPrcCd='" + wicPrcCd + '\'' +
				", wicCrdAcptId='" + wicCrdAcptId + '\'' +
				", effDt='" + effDt + '\'' +
				", endDt='" + endDt + '\'' +
				", purchaseIndicator='" + purchaseIndicator + '\'' +
				", upcPluLength='" + upcPluLength + '\'' +
				", manualVoucherIndicator='" + manualVoucherIndicator + '\'' +
				", upcCheckDigit='" + upcCheckDigit + '\'' +
				", idCode='" + idCode + '\'' +
				", sequenceNumber='" + sequenceNumber + '\'' +
				", messageId='" + messageId + '\'' +
				", errorMessage='" + errorMessage + '\'' +
				", dataRaw='" + dataRaw + '\'' +
				'}';
	}

	@Override
	public TexasStateDocument clone() throws CloneNotSupportedException {
		return (TexasStateDocument)super.clone();
	}
}
