package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the VEND_ITM_STR database table.
 * 
 */
@Embeddable
public class VendorItemStoreKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="VEND_LOC_NBR", insertable=false, updatable=false)
	private long vendLocNbr;

	@Column(name="VEND_LOC_TYP_CD", insertable=false, updatable=false)
	private String vendLocTypCd;

	@Column(name="ITM_ID", insertable=false, updatable=false)
	private long itmId;

	@Column(name="ITM_KEY_TYP_CD", insertable=false, updatable=false)
	private String itmKeyTypCd;

	@Column(name="LOC_NBR", insertable=false, updatable=false)
	private long locNbr;

	@Column(name="LOC_TYP_CD", insertable=false, updatable=false)
	private String locTypCd;

	public VendorItemStoreKey() {
	}
	public long getVendLocNbr() {
		return this.vendLocNbr;
	}
	public void setVendLocNbr(long vendLocNbr) {
		this.vendLocNbr = vendLocNbr;
	}
	public String getVendLocTypCd() {
		return this.vendLocTypCd;
	}
	public void setVendLocTypCd(String vendLocTypCd) {
		this.vendLocTypCd = vendLocTypCd;
	}
	public long getItmId() {
		return this.itmId;
	}
	public void setItmId(long itmId) {
		this.itmId = itmId;
	}
	public String getItmKeyTypCd() {
		return this.itmKeyTypCd;
	}
	public void setItmKeyTypCd(String itmKeyTypCd) {
		this.itmKeyTypCd = itmKeyTypCd;
	}
	public long getLocNbr() {
		return this.locNbr;
	}
	public void setLocNbr(long locNbr) {
		this.locNbr = locNbr;
	}
	public String getLocTypCd() {
		return this.locTypCd;
	}
	public void setLocTypCd(String locTypCd) {
		this.locTypCd = locTypCd;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof VendorItemStoreKey)) {
			return false;
		}
		VendorItemStoreKey castOther = (VendorItemStoreKey)other;
		return 
			(this.vendLocNbr == castOther.vendLocNbr)
			&& this.vendLocTypCd.equals(castOther.vendLocTypCd)
			&& (this.itmId == castOther.itmId)
			&& this.itmKeyTypCd.equals(castOther.itmKeyTypCd)
			&& (this.locNbr == castOther.locNbr)
			&& this.locTypCd.equals(castOther.locTypCd);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.vendLocNbr ^ (this.vendLocNbr >>> 32)));
		hash = hash * prime + this.vendLocTypCd.hashCode();
		hash = hash * prime + ((int) (this.itmId ^ (this.itmId >>> 32)));
		hash = hash * prime + this.itmKeyTypCd.hashCode();
		hash = hash * prime + ((int) (this.locNbr ^ (this.locNbr >>> 32)));
		hash = hash * prime + this.locTypCd.hashCode();
		
		return hash;
	}
}