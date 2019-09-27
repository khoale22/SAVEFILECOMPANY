/*
 *  WIC
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.batch.wic.entity;

import com.heb.batch.wic.utils.oracle.OracleFixedLengthCharType;
import com.heb.batch.wic.utils.oracle.OracleFixedLengthCharTypePK;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
/**
 * This is the wic entity. This represents a wic (woman infant child) product which is a govt sponsored program where you can
 * buy wic products using wic cards.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Entity
@Table(name = "prod_scn_cd_wic")
@TypeDef(name = "fixedLengthChar", typeClass = OracleFixedLengthCharType.class)
@TypeDef(name = "fixedLengthCharPK", typeClass = OracleFixedLengthCharTypePK.class)
public class ProductScanCodeWic implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private ProductScanCodeWicKey key;

    @Column(name = "eff_dt")
	private Date effDt;

    @Column(name = "end_dt")
    private Date endDt;

    @Column(name = "wic_unt_txt", length = 10)
	@Type(type="fixedLengthChar")
    private String wicUntTxt;

    @Column(name = "wic_bnft_qty", precision = 7, scale = 2)
    private Double wicBnFtQty;

    @Column(name = "wic_bnft_unt_txt", length = 50)
	@Type(type="fixedLengthChar")
    private String wicBnftUntTxt;

    @Column(name = "wic_prc_amt", precision = 7, scale = 2)
    private Double wicPrcAmt;

    @Column(name = "wic_prc_cd", length = 2)
	@Type(type="fixedLengthCharPK")
    private String wicPrcCd;

    @Column(name = "wic_crd_acpt_id", length = 15)
	@Type(type="fixedLengthChar")
    private String wicCrdAcptId;

	@Column(name = "leb_sw")
	@Type(type="fixedLengthChar")
	private String lebSwitch;

	@Column(name = "wic_prod_des")
	@Type(type="fixedLengthChar")
	private String wicDescription;

	@Column(name = "wic_pkg_sz_qty", precision = 7, scale = 2)
	private Double wicPackageSize;

    @Column(name = "cre8_ts")
	private Timestamp cre8Ts;

    @Column(name = "cre8_uid", length = 20)
	@Type(type="fixedLengthChar")
    private String cre8UId;

    @Column(name = "lst_updt_ts")
    private Timestamp lstUpdtTs;

    @Column(name = "lst_updt_uid", length = 20)
    private String lstUpdtUId;

    @Column(name = "scn_cd_ln_nbr")
	private Integer upcPluLength;

    @Column(name = "scn_cd_rdmbl_typ_cd")
	private Integer purchaseIndicator;

	@Column(name = "man_vchr_sw")
	private String manualVoucherIndicator;

	@Transient
	private String action;
    /**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public ProductScanCodeWicKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(ProductScanCodeWicKey key) {
		this.key = key;
	}

	/**
	 * @return the ENT_DT
	 */
	public Date getEndDt() {
		return endDt;
	}

	/**
	 * Sets the END_DT
	 *
	 * @param endDt The END_DT
	 */
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}


	/**
     * @return the EFF_DT
     */
    public Date getEffDt() {
        return effDt;
    }

    /**
     * Sets the EFF_DT
     *
     * @param effDt The EFF_DT
     */
    public void setEffDt(Date effDt) {
        this.effDt = effDt;
    }


    /**
     * @return the WIC_BNFT_QTY
     */
    public Double getWicBnFtQty() {
        return wicBnFtQty;
    }

    /**
     * Sets the WIC_BNFT_QTY
     *
     * @param wicBnFtQty the WIC_BNFT_QTY
     */
    public void setWicBnFtQty(Double wicBnFtQty) {
        this.wicBnFtQty = wicBnFtQty;
    }

	/**
	 * @return the WIC_UNT_TXT
	 */
	public String getWicUntTxt() {
		return wicUntTxt;
	}

	/**
	 * Sets the WIC_UNT_TXT
	 *
	 * @param wicUntTxt the WIC_UNT_TXT
	 */
	public void setWicUntTxt(String wicUntTxt) {
		this.wicUntTxt = wicUntTxt;
	}


    /**
     * @return the WIC_PRC_AMT
     */
    public Double getWicPrcAmt() {
        return wicPrcAmt;
    }

    /**
     * Sets the WIC_PRC_AMT
     *
     * @param wicPrcAmt the WIC_PRC_AMT
     */
    public void setWicPrcAmt(Double wicPrcAmt) {
        this.wicPrcAmt = wicPrcAmt;
    }

	/**
	 * @return the WIC_BNFT_UNT_QTY
	 */
	public String getWicBnftUntTxt() {
		return wicBnftUntTxt;
	}

	/**
	 * Sets the WIC_BNFT_UNT_QTY
	 *
	 * @param wicBnftUntTxt the WIC_BNFT_UNT_QTY
	 */
	public void setWicBnftUntTxt(String wicBnftUntTxt) {
		this.wicBnftUntTxt = wicBnftUntTxt;
	}

    /**
     * @return the WIC_CRD_ACPT_ID
     */
    public String getWicCrdAcptId() {
        return wicCrdAcptId;
    }

    /**
     * Sets the WIC_CRD_ACPT_ID
     *
     * @param wicCrdAcptId the WIC_CRD_ACPT_ID
     */
    public void setWicCrdAcptId(String wicCrdAcptId) {
        this.wicCrdAcptId = wicCrdAcptId;
    }

	/**
	 * @return the WIC_PRC_CD
	 */
	public String getWicPrcCd() {
		return wicPrcCd;
	}

	/**
	 * Sets the WIC_PRC_CD
	 *
	 * @param wicPrcCd the WIC_PRC_CD
	 */
	public void setWicPrcCd(String wicPrcCd) {
		this.wicPrcCd = wicPrcCd;
	}

    /**
     * @return the CRE8_TS
     */
    public Timestamp getCre8Ts() {
        return cre8Ts;
    }

    /**
     * Sets the CRE8_TS
     *
     * @param cre8Ts the CRE8_TS
     */
    public void setCre8Ts(Timestamp cre8Ts) {
        this.cre8Ts = cre8Ts;
    }


    /**
     * @return the LST_UPDT_TS
     */
    public Timestamp getLstUpdtTs() {
        return lstUpdtTs;
    }

    /**
     * Sets the LST_UPDT_TS
     *
     * @param lstUpdtTs the LST_UPDT_TS
     */
    public void setLstUpdtTs(Timestamp lstUpdtTs) {
        this.lstUpdtTs = lstUpdtTs;
    }

    /**
     * @return the LST_UPDT_UID
     */
    public String getLstUpdtUId() {
        return lstUpdtUId;
    }

    /**
     * Sets the LST_UPDT_UID
     *
     * @param lstUpdtUId the LST_UPDT_UID
     */
    public void setLstUpdtUId(String lstUpdtUId) {
        this.lstUpdtUId = lstUpdtUId;
    }

	/**
	 * Returns the LebSwitch. The LEB switch determines whether or not it is LEB.
	 *
	 * @return LebSwitch
	 */
	public String getLebSwitch() {
		return lebSwitch;
	}

	/**
	 * Sets the LebSwitch
	 *
	 * @param lebSwitch The LebSwitch
	 */
	public void setLebSwitch(String lebSwitch) {
		this.lebSwitch = lebSwitch;
	}

	/**
	 * @return the CRE8_UID
	 */
	public String getCre8UId() {
		return cre8UId;
	}

	/**
	 * Sets the CRE8_UID
	 *
	 * @param cre8UId the CRE8_UID
	 */
	public void setCre8UId(String cre8UId) {
		this.cre8UId = cre8UId;
	}

	/**
	 * Returns the WicDescription. The wic description of the current wic.
	 *
	 * @return WicDescription
	 */
	public String getWicDescription() {
		return wicDescription;
	}

	/**
	 * Sets the WicDescription
	 *
	 * @param wicDescription The WicDescription
	 */
	public void setWicDescription(String wicDescription) {
		this.wicDescription = wicDescription;
	}

	/**
	 * Returns the WicPackageSize. The Size of the WIC UPC
	 *
	 * @return WicPackageSize
	 */
	public Double getWicPackageSize() {
		return wicPackageSize;
	}

	/**
	 * Sets the WicPackageSize
	 *
	 * @param wicPackageSize The WicPackageSize
	 */
	public void setWicPackageSize(Double wicPackageSize) {
		this.wicPackageSize = wicPackageSize;
	}

	/**
	 * Sets the action.
	 *
	 * @param action The action
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the upcPluLength
	 */
	public Integer getUpcPluLength() { return upcPluLength; }

	/**
	 * Sets the upcPluLength.
	 *
	 * @param upcPluLength The upcPluLength
	 */
	public void setUpcPluLength(int upcPluLength) { this.upcPluLength = upcPluLength; }

	/**
	 * @return the purchaseIndicator
	 */
	public Integer getPurchaseIndicator() { return purchaseIndicator; }

	/**
	 * Sets the purchaseIndicator.
	 *
	 * @param purchaseIndicator The purchaseIndicator
	 */
	public void setPurchaseIndicator(int purchaseIndicator) { this.purchaseIndicator = purchaseIndicator; }

	/**
	 * @return the manualVoucherIndicator
	 */
	public String getManualVoucherIndicator() { return manualVoucherIndicator; }

	/**
	 * Sets the manualVoucherIndicator.
	 *
	 * @param manualVoucherIndicator The manualVoucherIndicator
	 */
	public void setManualVoucherIndicator(String manualVoucherIndicator) { this.manualVoucherIndicator = manualVoucherIndicator; }


	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductScanCodeWic sellingUnitWic = (ProductScanCodeWic) o;

		return key != null ? !key.equals(sellingUnitWic.key) : sellingUnitWic.key != null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ProductScanCodeWic{" +
				"key=" + key +
				", lebSwitch=" + lebSwitch +
				'}';
	}
}
