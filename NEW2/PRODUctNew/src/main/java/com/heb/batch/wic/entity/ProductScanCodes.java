/*
 * ProductScanCodes
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */

package com.heb.batch.wic.entity;

import com.heb.batch.wic.utils.oracle.OracleFixedLengthCharType;
import com.heb.batch.wic.utils.oracle.OracleFixedLengthCharTypePK;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Represents Product Scan Codes.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Entity
@Table(name="prod_scn_codes")
@TypeDef(name = "fixedLengthChar", typeClass = OracleFixedLengthCharType.class)
@TypeDef(name = "fixedLengthCharPK", typeClass = OracleFixedLengthCharTypePK.class)
public class ProductScanCodes implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;

	@Id
	@Column(name="scn_cd_id")
	private long upc;

	@Column(name="prim_scn_cd_sw")
	private String primaryUpc;

	@Column(name="tag_sz_des")
	@Type(type="fixedLengthChar")
	private String tagSize;

	@Column(name="bns_scn_cd_sw")
	private String bonusSwitch;

	@Column(name="PROD_ID")
	private long prodId;

	@Column(name="PROC_SCN_MAINT_SW")
	private String processedByScanMaintenance;

	@Column(name = "dscon_dt")
	private LocalDate discontinueDate;

	@Column(name = "lst_scn_dt")
	private LocalDate lastScanDate;

	@Column(name = "frst_scn_dt")
	private LocalDate firstScanDate;

	@Column(name = "retl_unt_sell_sz_1")
	private Double quantity;

	@Column(name = "retl_unt_sell_sz_2")
	private Double quantity2;

	@Column(name = "retl_unt_wt")
	private Double retailWeight;

	@Column(name = "tst_scn_prfmd_sw")
	private String testScanned;

	@Column(name = "dsd_deld_sw")
	private String dsdDeleteSwitch;

	@Column(name="wic_sw")
	@Type(type="fixedLengthChar")
	private String wicSwitch;

	@Column(name = "dsd_dept_ovrd_sw")
	private String dsdDeptOverideSwitch;

	@Column(name = "retl_unt_ln")
	private Double retailLength;

	@Column(name = "retl_unt_wd")
	private Double retailWidth;

	@Column(name = "retl_unt_ht")
	private Double retailHeight;

	@Column(name = "lst_updt_ts")
	private Date lastUpdatedOn;

	@Column(name = "lst_updt_uid")
	private String lastUpdatedBy;

	@Column(name = "pse_grams_wt")
	private Double pseGramWeight;

	@Column(name = "wic_apl_id")
	private Long wicApprovedProductListId;

	@Column(name = "retl_sell_sz_cd_1")
	private String retailUnitOfMeasureCode;

	@Column(name="SCN_TYP_CD")
	private String scanTypeCode;

	@Column(name="cre8_uid")
	@Type(type="fixedLengthChar")
	private String createId;

	@Column(name="cre8_ts")
	private LocalDateTime createTime;

	/**
	 * Returns the Wic Switch.
	 *
	 * @return WicSwitch
	 */
	public String getWicSwitch() {
		return wicSwitch;
	}

	/**
	 * Returns the selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
	 *
	 * @return The selling unit's UPC. UPC is used generically here. This could be a UPC, PLU, EAN, etc.
	 */
	public long getUpc() {
		return upc;
	}

	/**
	 * Sets the selling unit's UPC.
	 *
	 * @param upc The selling unit's UPC.
	 */
	public void setUpc(long upc) {
		this.upc = upc;
	}

	/**
	 * Compares another object to this one. This only compares the keys.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductScanCodes)) return false;

		ProductScanCodes that = (ProductScanCodes) o;

		return upc == that.upc;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int) (upc ^ (upc >>> ProductScanCodes.FOUR_BYTES));
	}


	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductScanCodes{" +
				"upc=" + upc +
				", primaryUpc=" + primaryUpc +
				", tagSizeDescription='" + tagSize + '\'' +
				", discontinueDate=" + discontinueDate +
				", processedByScanMaintenance =" + processedByScanMaintenance +
				", prodId=" + prodId +
				", bonusSwitch=" + bonusSwitch +
				", prodId=" + prodId +
				", lastScanDate=" + lastScanDate +
				", productMaster=" + prodId +
				", pseGramWeight=" + pseGramWeight +
				", tagSizeDescription=" + tagSize +
				'}';
	}
}
