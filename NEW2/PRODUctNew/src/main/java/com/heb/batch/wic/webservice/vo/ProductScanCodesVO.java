/*
 * ProductScanCodesVO
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.vo;

import java.sql.Date;
import java.sql.Timestamp;
/**
 * This is ProductScanCodesVO class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class ProductScanCodesVO extends BaseVO {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/* The Constructor ProdScnCodes */
	public ProductScanCodesVO() {
		// no param
	}

	/* The private scnCdId. */
	private long scnCdId;
	/* The private prodId. */
	private int prodId = -1;
	/* The private scnTypCd. */
	private String scnTypCd;
	/* The private primScnCdSw. */
	private String primScnCdSw;
	/* The private bnsScnCdSw. */
	private String bnsScnCdSw;
	/* The private scnCdCmt. */
	private String scnCdCmt;
	/* The private retlUntLn. */
	private double retlUntLn = -1;
	/* The private retlUntWd. */
	private double retlUntWd = -1;
	/* The private retlUntHt. */
	private double retlUntHt = -1;
	/* The private retlUntWt. */
	private double retlUntWt = -1;
	/* The private retlSellSzCd1. */
	private String retlSellSzCd1;
	/* The private retlUntSellSz1. */
	private double retlUntSellSz1 = -1;
	/* The private retlSellSzCd2. */
	private String retlSellSzCd2;
	/* The private retlUntSellSz2. */
	private double retlUntSellSz2 = -1;
	/* The private sampProvdSw. */
	private String sampProvdSw;
	/* The private prprcOffPct. */
	private int prprcOffPct = -1;
	/* The private prodSubBrndId. */
	private int prodSubBrndId = -1;
	/* The private frstScnDt. */
	private Date frstScnDt;
	/* The private lstScnDt. */
	private Date lstScnDt;
	/* The private consmUntId. */
	private int consmUntId = -1;
	/* The private tagItmId. */
	private double tagItmId = -1;
	/* The private tagItmKeyTypCd. */
	private String tagItmKeyTypCd;
	/* The private tagSzDes. */
	private String tagSzDes;
	/* The private wicSw. */
	private String wicSw;
	/* The private lebSw. */
	private String lebSw;
	/* The private wicAplId. */
	private long wicAplId = -1;
	/* The private fam3Cd. */
	private double fam3Cd = -1;
	/* The private fam4Cd. */
	private double fam4Cd = -1;
	/* The private dsdDeldSw. */
	private String dsdDeldSw;
	/* The private dsdDeptOvrdSw. */
	private String dsdDeptOvrdSw;
	/* The private upcActvSw. */
	private String upcActvSw;
	/* The private scaleSw. */
	private String scaleSw;
	/* The private wicUpdtUsrId. */
	private String wicUpdtUsrId;
	/* The private wicLstUpdtTs. */
	private Timestamp wicLstUpdtTs;
	/* The private lebUpdtUsrId. */
	private String lebUpdtUsrId;
	/* The private lebLstUpdtTs. */
	private Timestamp lebLstUpdtTs;
	/* The private cre8Ts. */
	private Timestamp cre8Ts;
	/* The private cre8Uid. */
	private String cre8Uid;
	/* The private lstUpdtTs. */
	private Timestamp lstUpdtTs;
	/* The private lstUpdtUid. */
	private String lstUpdtUid;
	/* The private lstSysUpdtId. */
	private int lstSysUpdtId = -1;
	/* The private pseGramsWt. */
	private int pseGramsWt = -1;
	/* The private tstScnPrfmdSw. */
	private String tstScnPrfmdSw;
	/* The private dsconDt. */
	private Date dsconDt;
	/* The private procScnMaintSw. */
	private String procScnMaintSw;
	/*The private vcUserId.*/
	private String vcUpdtUsrId;
	private boolean stageEvent = false;
	private boolean productUpdateEvent = false;
	private boolean familyCodesEvent = false;
	private boolean stageEventWic = false;
	private boolean stageEventIdeal = false;
	private int upcPluLength;
	private int purchaseIndicator;
	private String manualVoucherIndicator;

	/*
	 * Sets the scnCdId.
	 * 
	 * @param scnCdId type {@link }
	 */
	public void setScnCdId(long scnCdId) {
		this.scnCdId = scnCdId;
	}

	/*
	 * Gets the scnCdId.
	 * 
	 * @return scnCdId
	 */
	public long getScnCdId() {
		return this.scnCdId;
	}

	/*
	 * Sets the prodId.
	 * 
	 * @param prodId type {@link int}
	 */
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}

	/*
	 * Gets the prodId.
	 * 
	 * @return prodId
	 */
	public int getProdId() {
		return this.prodId;
	}

	/*
	 * Sets the scnTypCd.
	 * 
	 * @param scnTypCd type {@link String}
	 */
	public void setScnTypCd(String scnTypCd) {
		this.scnTypCd = scnTypCd;
	}

	/*
	 * Gets the scnTypCd.
	 * 
	 * @return scnTypCd
	 */
	public String getScnTypCd() {
		return this.scnTypCd;
	}

	/*
	 * Sets the primScnCdSw.
	 * 
	 * @param primScnCdSw type {@link String}
	 */
	public void setPrimScnCdSw(String primScnCdSw) {
		this.primScnCdSw = primScnCdSw;
	}

	/*
	 * Gets the primScnCdSw.
	 * 
	 * @return primScnCdSw
	 */
	public String getPrimScnCdSw() {
		return this.primScnCdSw;
	}

	/*
	 * Sets the bnsScnCdSw.
	 * 
	 * @param bnsScnCdSw type {@link String}
	 */
	public void setBnsScnCdSw(String bnsScnCdSw) {
		this.bnsScnCdSw = bnsScnCdSw;
	}

	/*
	 * Gets the bnsScnCdSw.
	 * 
	 * @return bnsScnCdSw
	 */
	public String getBnsScnCdSw() {
		return this.bnsScnCdSw;
	}

	/*
	 * Sets the scnCdCmt.
	 * 
	 * @param scnCdCmt type {@link String}
	 */
	public void setScnCdCmt(String scnCdCmt) {
		this.scnCdCmt = scnCdCmt;
	}

	/*
	 * Gets the scnCdCmt.
	 * 
	 * @return scnCdCmt
	 */
	public String getScnCdCmt() {
		return this.scnCdCmt;
	}

	/*
	 * Sets the retlUntLn.
	 * 
	 * @param retlUntLn type {@link }
	 */
	public void setRetlUntLn(double retlUntLn) {
		this.retlUntLn = retlUntLn;
	}

	/*
	 * Gets the retlUntLn.
	 * 
	 * @return retlUntLn
	 */
	public double getRetlUntLn() {
		return this.retlUntLn;
	}

	/*
	 * Sets the retlUntWd.
	 * 
	 * @param retlUntWd type {@link }
	 */
	public void setRetlUntWd(double retlUntWd) {
		this.retlUntWd = retlUntWd;
	}

	/*
	 * Gets the retlUntWd.
	 * 
	 * @return retlUntWd
	 */
	public double getRetlUntWd() {
		return this.retlUntWd;
	}

	/*
	 * Sets the retlUntHt.
	 * 
	 * @param retlUntHt type {@link }
	 */
	public void setRetlUntHt(double retlUntHt) {
		this.retlUntHt = retlUntHt;
	}

	/*
	 * Gets the retlUntHt.
	 * 
	 * @return retlUntHt
	 */
	public double getRetlUntHt() {
		return this.retlUntHt;
	}

	/*
	 * Sets the retlUntWt.
	 * 
	 * @param retlUntWt type {@link }
	 */
	public void setRetlUntWt(double retlUntWt) {
		this.retlUntWt = retlUntWt;
	}

	/*
	 * Gets the retlUntWt.
	 * 
	 * @return retlUntWt
	 */
	public double getRetlUntWt() {
		return this.retlUntWt;
	}

	/*
	 * Sets the retlSellSzCd1.
	 * 
	 * @param retlSellSzCd1 type {@link String}
	 */

	/*
	 * Sets the sampProvdSw.
	 * 
	 * @param sampProvdSw type {@link String}
	 */
	public void setSampProvdSw(String sampProvdSw) {
		this.sampProvdSw = sampProvdSw;
	}

	public String getRetlSellSzCd1() {
		return retlSellSzCd1;
	}

	public void setRetlSellSzCd1(String retlSellSzCd1) {
		this.retlSellSzCd1 = retlSellSzCd1;
	}

	public double getRetlUntSellSz1() {
		return retlUntSellSz1;
	}

	public void setRetlUntSellSz1(double retlUntSellSz1) {
		this.retlUntSellSz1 = retlUntSellSz1;
	}

	public String getRetlSellSzCd2() {
		return retlSellSzCd2;
	}

	public void setRetlSellSzCd2(String retlSellSzCd2) {
		this.retlSellSzCd2 = retlSellSzCd2;
	}

	public double getRetlUntSellSz2() {
		return retlUntSellSz2;
	}

	public void setRetlUntSellSz2(double retlUntSellSz2) {
		this.retlUntSellSz2 = retlUntSellSz2;
	}

	/*
	 * Gets the sampProvdSw.
	 * 
	 * @return sampProvdSw
	 */
	public String getSampProvdSw() {
		return this.sampProvdSw;
	}

	/*
	 * Sets the prprcOffPct.
	 * 
	 * @param prprcOffPct type {@link }
	 */
	public void setPrprcOffPct(int prprcOffPct) {
		this.prprcOffPct = prprcOffPct;
	}

	/*
	 * Gets the prprcOffPct.
	 * 
	 * @return prprcOffPct
	 */
	public int getPrprcOffPct() {
		return this.prprcOffPct;
	}

	/*
	 * Sets the prodSubBrndId.
	 * 
	 * @param prodSubBrndId type {@link int}
	 */
	public void setProdSubBrndId(int prodSubBrndId) {
		this.prodSubBrndId = prodSubBrndId;
	}

	/*
	 * Gets the prodSubBrndId.
	 * 
	 * @return prodSubBrndId
	 */
	public int getProdSubBrndId() {
		return this.prodSubBrndId;
	}

	/*
	 * Sets the frstScnDt.
	 * 
	 * @param frstScnDt type {@link Date}
	 */
	public void setFrstScnDt(Date frstScnDt) {
		this.frstScnDt = frstScnDt;
	}

	/*
	 * Gets the frstScnDt.
	 * 
	 * @return frstScnDt
	 */
	public Date getFrstScnDt() {
		return this.frstScnDt;
	}

	/*
	 * Sets the lstScnDt.
	 * 
	 * @param lstScnDt type {@link Date}
	 */
	public void setLstScnDt(Date lstScnDt) {
		this.lstScnDt = lstScnDt;
	}

	/*
	 * Gets the lstScnDt.
	 * 
	 * @return lstScnDt
	 */
	public Date getLstScnDt() {
		return this.lstScnDt;
	}

	/*
	 * Sets the consmUntId.
	 * 
	 * @param consmUntId type {@link int}
	 */
	public void setConsmUntId(int consmUntId) {
		this.consmUntId = consmUntId;
	}

	/*
	 * Gets the consmUntId.
	 * 
	 * @return consmUntId
	 */
	public int getConsmUntId() {
		return this.consmUntId;
	}

	/*
	 * Sets the tagItmId.
	 * 
	 * @param tagItmId type {@link double}
	 */
	public void setTagItmId(double tagItmId) {
		this.tagItmId = tagItmId;
	}

	/*
	 * Gets the tagItmId.
	 * 
	 * @return tagItmId
	 */
	public double getTagItmId() {
		return this.tagItmId;
	}

	/*
	 * Sets the tagItmKeyTypCd.
	 * 
	 * @param tagItmKeyTypCd type {@link String}
	 */
	public void setTagItmKeyTypCd(String tagItmKeyTypCd) {
		this.tagItmKeyTypCd = tagItmKeyTypCd;
	}

	/*
	 * Gets the tagItmKeyTypCd.
	 * 
	 * @return tagItmKeyTypCd
	 */
	public String getTagItmKeyTypCd() {
		return this.tagItmKeyTypCd;
	}

	/*
	 * Sets the tagSzDes.
	 * 
	 * @param tagSzDes type {@link String}
	 */
	public void setTagSzDes(String tagSzDes) {
		this.tagSzDes = tagSzDes;
	}

	/*
	 * Gets the tagSzDes.
	 * 
	 * @return tagSzDes
	 */
	public String getTagSzDes() {
		return this.tagSzDes;
	}

	/*
	 * Sets the wicSw.
	 * 
	 * @param wicSw type {@link String}
	 */
	public void setWicSw(String wicSw) {
		this.wicSw = wicSw;
	}

	/*
	 * Gets the wicSw.
	 * 
	 * @return wicSw
	 */
	public String getWicSw() {
		return this.wicSw;
	}

	/*
	 * Sets the lebSw.
	 * 
	 * @param lebSw type {@link String}
	 */
	public void setLebSw(String lebSw) {
		this.lebSw = lebSw;
	}

	/*
	 * Gets the lebSw.
	 * 
	 * @return lebSw
	 */
	public String getLebSw() {
		return this.lebSw;
	}

	/*
	 * Sets the wicAplId.
	 * 
	 * @param wicAplId type {@link double}
	 */
	public void setWicAplId(long wicAplId) {
		this.wicAplId = wicAplId;
	}

	/*
	 * Gets the wicAplId.
	 * 
	 * @return wicAplId
	 */
	public long getWicAplId() {
		return this.wicAplId;
	}

	public double getFam3Cd() {
		return fam3Cd;
	}

	public void setFam3Cd(double fam3Cd) {
		this.fam3Cd = fam3Cd;
	}

	public double getFam4Cd() {
		return fam4Cd;
	}

	public void setFam4Cd(double fam4Cd) {
		this.fam4Cd = fam4Cd;
	}

	/*
	 * Sets the dsdDeldSw.
	 * 
	 * @param dsdDeldSw type {@link String}
	 */
	public void setDsdDeldSw(String dsdDeldSw) {
		this.dsdDeldSw = dsdDeldSw;
	}

	/*
	 * Gets the dsdDeldSw.
	 * 
	 * @return dsdDeldSw
	 */
	public String getDsdDeldSw() {
		return this.dsdDeldSw;
	}

	/*
	 * Sets the dsdDeptOvrdSw.
	 * 
	 * @param dsdDeptOvrdSw type {@link String}
	 */
	public void setDsdDeptOvrdSw(String dsdDeptOvrdSw) {
		this.dsdDeptOvrdSw = dsdDeptOvrdSw;
	}

	/*
	 * Gets the dsdDeptOvrdSw.
	 * 
	 * @return dsdDeptOvrdSw
	 */
	public String getDsdDeptOvrdSw() {
		return this.dsdDeptOvrdSw;
	}

	/*
	 * Sets the upcActvSw.
	 * 
	 * @param upcActvSw type {@link String}
	 */
	public void setUpcActvSw(String upcActvSw) {
		this.upcActvSw = upcActvSw;
	}

	/*
	 * Gets the upcActvSw.
	 * 
	 * @return upcActvSw
	 */
	public String getUpcActvSw() {
		return this.upcActvSw;
	}

	/*
	 * Sets the scaleSw.
	 * 
	 * @param scaleSw type {@link String}
	 */
	public void setScaleSw(String scaleSw) {
		this.scaleSw = scaleSw;
	}

	/*
	 * Gets the scaleSw.
	 * 
	 * @return scaleSw
	 */
	public String getScaleSw() {
		return this.scaleSw;
	}

	/*
	 * Sets the wicUpdtUsrId.
	 * 
	 * @param wicUpdtUsrId type {@link String}
	 */
	public void setWicUpdtUsrId(String wicUpdtUsrId) {
		this.wicUpdtUsrId = wicUpdtUsrId;
	}

	/*
	 * Gets the wicUpdtUsrId.
	 * 
	 * @return wicUpdtUsrId
	 */
	public String getWicUpdtUsrId() {
		return this.wicUpdtUsrId;
	}

	/*
	 * Sets the wicLstUpdtTs.
	 * 
	 * @param wicLstUpdtTs type {@link }
	 */
	public void setWicLstUpdtTs(Timestamp wicLstUpdtTs) {
		this.wicLstUpdtTs = wicLstUpdtTs;
	}

	/*
	 * Gets the wicLstUpdtTs.
	 * 
	 * @return wicLstUpdtTs
	 */
	public Timestamp getWicLstUpdtTs() {
		return this.wicLstUpdtTs;
	}

	/*
	 * Sets the lebUpdtUsrId.
	 * 
	 * @param lebUpdtUsrId type {@link String}
	 */
	public void setLebUpdtUsrId(String lebUpdtUsrId) {
		this.lebUpdtUsrId = lebUpdtUsrId;
	}

	/*
	 * Gets the lebUpdtUsrId.
	 * 
	 * @return lebUpdtUsrId
	 */
	public String getLebUpdtUsrId() {
		return this.lebUpdtUsrId;
	}

	/*
	 * Sets the lebLstUpdtTs.
	 * 
	 * @param lebLstUpdtTs type {@link }
	 */
	public void setLebLstUpdtTs(Timestamp lebLstUpdtTs) {
		this.lebLstUpdtTs = lebLstUpdtTs;
	}

	/*
	 * Gets the lebLstUpdtTs.
	 * 
	 * @return lebLstUpdtTs
	 */
	public Timestamp getLebLstUpdtTs() {
		return this.lebLstUpdtTs;
	}

	/*
	 * Sets the cre8Ts.
	 * 
	 * @param cre8Ts type {@link }
	 */
	public void setCre8Ts(Timestamp cre8Ts) {
		this.cre8Ts = cre8Ts;
	}

	/*
	 * Gets the cre8Ts.
	 * 
	 * @return cre8Ts
	 */
	public Timestamp getCre8Ts() {
		return this.cre8Ts;
	}

	/*
	 * Sets the cre8Uid.
	 * 
	 * @param cre8Uid type {@link String}
	 */
	public void setCre8Uid(String cre8Uid) {
		this.cre8Uid = cre8Uid;
	}

	/*
	 * Gets the cre8Uid.
	 * 
	 * @return cre8Uid
	 */
	public String getCre8Uid() {
		return this.cre8Uid;
	}

	/*
	 * Sets the lstUpdtTs.
	 * 
	 * @param lstUpdtTs type {@link }
	 */
	public void setLstUpdtTs(Timestamp lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}

	/*
	 * Gets the lstUpdtTs.
	 * 
	 * @return lstUpdtTs
	 */
	public Timestamp getLstUpdtTs() {
		return this.lstUpdtTs;
	}

	/*
	 * Sets the lstUpdtUid.
	 * 
	 * @param lstUpdtUid type {@link String}
	 */
	public void setLstUpdtUid(String lstUpdtUid) {
		this.lstUpdtUid = lstUpdtUid;
	}

	/*
	 * Gets the lstUpdtUid.
	 * 
	 * @return lstUpdtUid
	 */
	public String getLstUpdtUid() {
		return this.lstUpdtUid;
	}

	/*
	 * Sets the lstSysUpdtId.
	 * 
	 * @param lstSysUpdtId type {@link int}
	 */
	public void setLstSysUpdtId(int lstSysUpdtId) {
		this.lstSysUpdtId = lstSysUpdtId;
	}

	/*
	 * Gets the lstSysUpdtId.
	 * 
	 * @return lstSysUpdtId
	 */
	public int getLstSysUpdtId() {
		return this.lstSysUpdtId;
	}

	/*
	 * Sets the pseGramsWt.
	 * 
	 * @param pseGramsWt type {@link }
	 */
	public void setPseGramsWt(int pseGramsWt) {
		this.pseGramsWt = pseGramsWt;
	}

	/*
	 * Gets the pseGramsWt.
	 * 
	 * @return pseGramsWt
	 */
	public int getPseGramsWt() {
		return this.pseGramsWt;
	}

	/*
	 * Sets the tstScnPrfmdSw.
	 * 
	 * @param tstScnPrfmdSw type {@link String}
	 */
	public void setTstScnPrfmdSw(String tstScnPrfmdSw) {
		this.tstScnPrfmdSw = tstScnPrfmdSw;
	}

	/*
	 * Gets the tstScnPrfmdSw.
	 * 
	 * @return tstScnPrfmdSw
	 */
	public String getTstScnPrfmdSw() {
		return this.tstScnPrfmdSw;
	}

	/*
	 * Sets the dsconDt.
	 * 
	 * @param dsconDt type {@link Date}
	 */
	public void setDsconDt(Date dsconDt) {
		this.dsconDt = dsconDt;
	}

	/*
	 * Gets the dsconDt.
	 * 
	 * @return dsconDt
	 */
	public Date getDsconDt() {
		return this.dsconDt;
	}
	/*
	 * Gets the procScnMaintSw.
	 *
	 * @return procScnMaintSw
	 */
	public String getProcScnMaintSw() {
		return this.procScnMaintSw;
	}
	/*
	 * Sets the procScnMaintSw.
	 * 
	 * @param procScnMaintSw type {@link String}
	 */
	public void setProcScnMaintSw(String procScnMaintSw) {
		this.procScnMaintSw = procScnMaintSw;
	}

	/**
	 * Get the upcPluLength.
	 *
	 * @return the upcPluLength
	 */
	public int getUpcPluLength() {
		return upcPluLength;
	}

	/**
	 * Set the upcPluLength.
	 *
	 * @param upcPluLength the upcPluLength to set
	 */
	public void setUpcPluLength(int upcPluLength) {
		this.upcPluLength = upcPluLength;
	}

	/**
	 * Get the purchaseIndicator.
	 *
	 * @return the purchaseIndicator
	 */
	public int getPurchaseIndicator() {
		return purchaseIndicator;
	}

	/**
	 * Set the purchaseIndicator.
	 *
	 * @param purchaseIndicator the purchaseIndicator to set
	 */
	public void setPurchaseIndicator(int purchaseIndicator) {
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

	public boolean isStageEventIdeal() {
		return stageEventIdeal;
	}

	public void setStageEventIdeal(boolean stageEventIdeal) {
		this.stageEventIdeal = stageEventIdeal;
	}

	public boolean isStageEventWic() {
		return stageEventWic;
	}

	public void setStageEventWic(boolean stageEventWic) {
		this.stageEventWic = stageEventWic;
	}

	public boolean isFamilyCodesEvent() {
		return familyCodesEvent;
	}

	public void setFamilyCodesEvent(boolean familyCodesEvent) {
		this.familyCodesEvent = familyCodesEvent;
	}

	public boolean isStageEvent() {
		return stageEvent;
	}

	public void setStageEvent(boolean stageEvent) {
		this.stageEvent = stageEvent;
	}

	public boolean isProductUpdateEvent() {
		return productUpdateEvent;
	}

	public void setProductUpdateEvent(boolean productUpdateEvent) {
		this.productUpdateEvent = productUpdateEvent;
	}

	public String getVcUpdtUsrId() {
		return vcUpdtUsrId;
	}

	public void setVcUpdtUsrId(String vcUpdtUsrId) {
		this.vcUpdtUsrId = vcUpdtUsrId;
	}

	@Override
	public String toString() {
		return "ProductScanCodesVO{" +
				"scnCdId=" + scnCdId +
				", prodId=" + prodId +
				", scnTypCd='" + scnTypCd + '\'' +
				", primScnCdSw='" + primScnCdSw + '\'' +
				", bnsScnCdSw='" + bnsScnCdSw + '\'' +
				", scnCdCmt='" + scnCdCmt + '\'' +
				", retlUntLn=" + retlUntLn +
				", retlUntWd=" + retlUntWd +
				", retlUntHt=" + retlUntHt +
				", retlUntWt=" + retlUntWt +
				", retlSellSzCd1='" + retlSellSzCd1 + '\'' +
				", retlUntSellSz1=" + retlUntSellSz1 +
				", retlSellSzCd2='" + retlSellSzCd2 + '\'' +
				", retlUntSellSz2=" + retlUntSellSz2 +
				", sampProvdSw='" + sampProvdSw + '\'' +
				", prprcOffPct=" + prprcOffPct +
				", prodSubBrndId=" + prodSubBrndId +
				", frstScnDt=" + frstScnDt +
				", lstScnDt=" + lstScnDt +
				", consmUntId=" + consmUntId +
				", tagItmId=" + tagItmId +
				", tagItmKeyTypCd='" + tagItmKeyTypCd + '\'' +
				", tagSzDes='" + tagSzDes + '\'' +
				", wicSw='" + wicSw + '\'' +
				", lebSw='" + lebSw + '\'' +
				", wicAplId=" + wicAplId +
				", fam3Cd=" + fam3Cd +
				", fam4Cd=" + fam4Cd +
				", dsdDeldSw='" + dsdDeldSw + '\'' +
				", dsdDeptOvrdSw='" + dsdDeptOvrdSw + '\'' +
				", upcActvSw='" + upcActvSw + '\'' +
				", scaleSw='" + scaleSw + '\'' +
				", wicUpdtUsrId='" + wicUpdtUsrId + '\'' +
				", wicLstUpdtTs=" + wicLstUpdtTs +
				", lebUpdtUsrId='" + lebUpdtUsrId + '\'' +
				", lebLstUpdtTs=" + lebLstUpdtTs +
				", cre8Ts=" + cre8Ts +
				", cre8Uid='" + cre8Uid + '\'' +
				", lstUpdtTs=" + lstUpdtTs +
				", lstUpdtUid='" + lstUpdtUid + '\'' +
				", lstSysUpdtId=" + lstSysUpdtId +
				", pseGramsWt=" + pseGramsWt +
				", tstScnPrfmdSw='" + tstScnPrfmdSw + '\'' +
				", dsconDt=" + dsconDt +
				", procScnMaintSw='" + procScnMaintSw + '\'' +
				", upcPluLength='" + upcPluLength + '\'' +
				", purchaseIndicator='" + purchaseIndicator + '\'' +
				", manualVoucherIndicator='" + manualVoucherIndicator + '\'' +
				", vcUpdtUsrId='" + vcUpdtUsrId + '\'' +
				", stageEvent=" + stageEvent +
				", productUpdateEvent=" + productUpdateEvent +
				", familyCodesEvent=" + familyCodesEvent +
				", stageEventWic=" + stageEventWic +
				", stageEventIdeal=" + stageEventIdeal +
				'}';
	}

}