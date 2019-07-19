package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the VEND_ITM_STR database table.
 *
 */
@Entity
@Table(name="VEND_ITM_STR")
public class VendorItemStore implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ITEM_KEY_TYPE_DSD_CODE = "DSD";
	public static final String LOCATION_TYPE_S_CODE = "S";
	public static final String VENDOR_LOCATION_TYPE_D_CODE = "D";
	@EmbeddedId
	private VendorItemStoreKey id;

	//	@Temporal(TemporalType.DATE)
	@Column(name="AUTH_DT")
	private LocalDate authDt;

	@Column(name="AUTHN_SW")
	private Boolean authnSw;

	@Column(name="INV_SEQ_NBR")
	private BigDecimal invSeqNbr;

	@Column(name="LST_UPDT_TS")
	private LocalDateTime lstUpdtTs;

	@Column(name="LST_UPDT_USR_ID")
	private String lstUpdtUsrId;

	@Column(name="STR_DEPT_NBR")
	private String strDeptNbr;

	@Column(name="STR_SUB_DEPT_ID")
	private String strSubDeptId;

	//	@Temporal(TemporalType.DATE)
	@Column(name="UNATH_DT")
	private LocalDate unathDt;

	//bi-directional many-to-one association to VendLocItm
	/*@JsonIgnoreProperties("vendorItemStores")*/
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="ITM_ID", referencedColumnName = "itm_id", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name="ITM_KEY_TYP_CD", referencedColumnName = "itm_key_typ_cd", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "VEND_LOC_NBR", referencedColumnName = "vend_loc_nbr", insertable = false, updatable = false, nullable = false),
			@JoinColumn(name = "VEND_LOC_TYP_CD", referencedColumnName = "vend_loc_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private VendorLocationItem vendorLocationItem;

	public VendorItemStore() {
	}

	public VendorItemStoreKey getId() {
		return this.id;
	}

	public void setId(VendorItemStoreKey id) {
		this.id = id;
	}

	public LocalDate getAuthDt() {
		return this.authDt;
	}

	public void setAuthDt(LocalDate authDt) {
		this.authDt = authDt;
	}

	public Boolean getAuthnSw() {
		return this.authnSw;
	}

	public void setAuthnSw(Boolean authnSw) {
		this.authnSw = authnSw;
	}

	public BigDecimal getInvSeqNbr() {
		return this.invSeqNbr;
	}

	public void setInvSeqNbr(BigDecimal invSeqNbr) {
		this.invSeqNbr = invSeqNbr;
	}

	public LocalDateTime getLstUpdtTs() {
		return this.lstUpdtTs;
	}

	public void setLstUpdtTs(LocalDateTime lstUpdtTs) {
		this.lstUpdtTs = lstUpdtTs;
	}

	public String getLstUpdtUsrId() {
		return this.lstUpdtUsrId;
	}

	public void setLstUpdtUsrId(String lstUpdtUsrId) {
		this.lstUpdtUsrId = lstUpdtUsrId;
	}

	public String getStrDeptNbr() {
		return this.strDeptNbr;
	}

	public void setStrDeptNbr(String strDeptNbr) {
		this.strDeptNbr = strDeptNbr;
	}

	public String getStrSubDeptId() {
		return this.strSubDeptId;
	}

	public void setStrSubDeptId(String strSubDeptId) {
		this.strSubDeptId = strSubDeptId;
	}

	public LocalDate getUnathDt() {
		return this.unathDt;
	}

	public void setUnathDt(LocalDate unathDt) {
		this.unathDt = unathDt;
	}

	public VendorLocationItem getVendorLocationItem() {
		return this.vendorLocationItem;
	}

	public void setVendorLocationItem(VendorLocationItem vendorLocationItem) {
		this.vendorLocationItem = vendorLocationItem;
	}

}