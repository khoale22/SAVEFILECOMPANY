/*
 * ProductScanCodeWicVO
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.vo;
import com.heb.batch.wic.utils.WicConstants;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * This is ProductScanCodeWicVO class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class ProductScanCodeWicVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	private Long scnCdId;
	/* The private wicAplId. */
	private Long wicAplId;
	/* The private wicCatId. */
	private Long wicCatId;
	/* The private wicSubCatId. */
	private Long wicSubCatId;
	/* The private effDt. */
	private Date effDt;
	/* The private effDt. */
	private Date endDt;
	/* The private wicProdDes. */
	private String wicProdDes;
	/* The private wicUntTxt. */
	private String wicUntTxt;
	/* The private wicPkgSzQty. */
	private double wicPkgSzQty = -1;
	/* The private wicBnftQty. */
	private double wicBnftQty = -1;
	/* The private wicBnftUntTxt. */
	private String wicBnftUntTxt;
	/* The private wicPrcAmt. */
	private double wicPrcAmt = -1;
	/* The private wicPrcCd. */
	private String wicPrcCd;
	/* The private wicCrdAcptId. */
	private String wicCrdAcptId;
	/* The private lebSw. */
	private String lebSw;
	/* The private cre8Ts. */
	private Timestamp cre8Ts;
	/* The private cre8Uid. */
	private String cre8Uid;
	/* The private lstUpdtTs. */
	private Timestamp lstUpdtTs;
	/* The private lstUpdtUid. */
	private String lstUpdtUid;
	private String stageEvent;
	private String wiccEvent = WicConstants.YES;
	/* The private upcPluLength. */
	private Integer upcPluLength;
	/* The private purchaseIndicator. */
	private Integer purchaseIndicator;
	/* The private manualVoucherIndicator. */
	private String manualVoucherIndicator;
	
	/* The Constructor ProdScnCodes */
	public ProductScanCodeWicVO() {
		// no param
	}

	public Long getScnCdId() {
		return scnCdId;
	}

	public void setScnCdId(Long scnCdId) {
		this.scnCdId = scnCdId;
	}


	public Long getWicAplId() {
		return wicAplId;
	}

	public void setWicAplId(Long wicAplId) {
		this.wicAplId = wicAplId;
	}


	public Long getWicCatId() {
		return wicCatId;
	}

	public void setWicCatId(Long wicCatId) {
		this.wicCatId = wicCatId;
	}


	public Long getWicSubCatId() {
		return wicSubCatId;
	}

	public void setWicSubCatId(Long wicSubCatId) {
		this.wicSubCatId = wicSubCatId;
	}


	public Date getEffDt() {
		return effDt;
	}

	public void setEffDt(Date effDt) {
		this.effDt = effDt;
	}


	public Date getEndDt() {
		return endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}


	public String getWicProdDes() {
		return wicProdDes;
	}

	public void setWicProdDes(String wicProdDes) {
		this.wicProdDes = wicProdDes;
	}


	public String getWicUntTxt() {
		return wicUntTxt;
	}

	public void setWicUntTxt(String wicUntTxt) {
		this.wicUntTxt = wicUntTxt;
	}


	public Double getWicPkgSzQty() {
		return wicPkgSzQty;
	}

	public void setWicPkgSzQty(Double wicPkgSzQty) {
		this.wicPkgSzQty = wicPkgSzQty;
	}


	public Double getWicBnftQty() {
		return wicBnftQty;
	}

	public void setWicBnftQty(Double wicBnftQty) {
		this.wicBnftQty = wicBnftQty;
	}


	public String getWicBnftUntTxt() {
		return wicBnftUntTxt;
	}

	public void setWicBnftUntTxt(String wicBnftUntTxt) {
		this.wicBnftUntTxt = wicBnftUntTxt;
	}


	public Double getWicPrcAmt() {
		return wicPrcAmt;
	}

	public void setWicPrcAmt(Double wicPrcAmt) {
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


	public String getLebSw() {
		return lebSw;
	}

	public void setLebSw(String lebSw) {
		this.lebSw = lebSw;
	}


	public Timestamp getCre8Ts() {
		return cre8Ts;
	}

	public void setCre8Ts(Timestamp cre8Ts) {
		this.cre8Ts = cre8Ts;
	}


	public String getCre8Uid() {
		return cre8Uid;
	}

	public void setCre8Uid(String cre8Uid) {
		this.cre8Uid = cre8Uid;
	}


	public Timestamp getLstUpdtTs() {
		return lstUpdtTs;
	}

	public void setLstUpdtTs(Timestamp lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}


	public String getLstUpdtUid() {
		return lstUpdtUid;
	}

	public void setLstUpdtUid(String lstUpdtUid) {
		this.lstUpdtUid = lstUpdtUid;
	}

	public String getStageEvent() {
		return stageEvent;
	}

	public void setStageEvent(String stageEvent) {
		this.stageEvent = stageEvent;
	}

	public String getWiccEvent() {
		return wiccEvent;
	}

	public void setWiccEvent(String wiccEvent) {
		this.wiccEvent = wiccEvent;
	}

	/**
	 * Get the upcPluLength.
	 *
	 * @return the upcPluLength
	 */
	public Integer getUpcPluLength() {
		return upcPluLength;
	}

	/**
	 * Set the upcPluLength.
	 *
	 * @param upcPluLength the upcPluLength to set
	 */
	public void setUpcPluLength(Integer upcPluLength) {
		this.upcPluLength = upcPluLength;
	}

	/**
	 * Get the purchaseIndicator.
	 *
	 * @return the purchaseIndicator
	 */
	public Integer getPurchaseIndicator() {
		return purchaseIndicator;
	}

	/**
	 * Set the purchaseIndicator.
	 *
	 * @param purchaseIndicator the purchaseIndicator to set
	 */
	public void setPurchaseIndicator(Integer purchaseIndicator) {
		this.purchaseIndicator = purchaseIndicator;
	}

	/**
	 * Get the manualVoucherIndicator.
	 *
	 * @return the manualVoucherIndicator
	 */
	public String getManualVoucherIndicator() {
		return manualVoucherIndicator;
	}

	/**
	 * Set the manualVoucherIndicator.
	 *
	 * @param manualVoucherIndicator the manualVoucherIndicator to set
	 */
	public void setManualVoucherIndicator(String manualVoucherIndicator) {
		this.manualVoucherIndicator = manualVoucherIndicator;
	}

	@Override
	public String toString() {
		return "ProdScnCdWicVO{" +
				"scnCdId=" + scnCdId +
				", wicAplId=" + wicAplId +
				", wicCatId=" + wicCatId +
				", wicSubCatId=" + wicSubCatId +
				", effDt=" + effDt +
				", endDt=" + endDt +
				", wicProdDes='" + wicProdDes + '\'' +
				", wicUntTxt='" + wicUntTxt + '\'' +
				", wicPkgSzQty=" + wicPkgSzQty +
				", wicBnftQty=" + wicBnftQty +
				", wicBnftUntTxt='" + wicBnftUntTxt + '\'' +
				", wicPrcAmt=" + wicPrcAmt +
				", wicPrcCd='" + wicPrcCd + '\'' +
				", wicCrdAcptId='" + wicCrdAcptId + '\'' +
				", lebSw='" + lebSw + '\'' +
				", cre8Ts=" + cre8Ts +
				", upcPluLength=" + upcPluLength +
				", purchaseIndicator=" + purchaseIndicator +
				", manualVoucherIndicator=" + manualVoucherIndicator +
				", cre8Uid='" + cre8Uid + '\'' +
				", lstUpdtTs=" + lstUpdtTs +
				", lstUpdtUid='" + lstUpdtUid + '\'' +
				'}';
	}
}
