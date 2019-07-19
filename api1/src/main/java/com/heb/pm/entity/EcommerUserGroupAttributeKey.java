package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ECOM_USR_GRP_ATTR database table.
 * 
 */
@Embeddable
public class EcommerUserGroupAttributeKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="USR_INRFC_GRP_CD", insertable=false, updatable=false)
	private String usrInrfcGrpCd;

	@Column(name="ATTR_ID", insertable=false, updatable=false)
	private long attributeId;

	public EcommerUserGroupAttributeKey() {
	}
	public String getUsrInrfcGrpCd() {
		return this.usrInrfcGrpCd;
	}
	public void setUsrInrfcGrpCd(String usrInrfcGrpCd) {
		this.usrInrfcGrpCd = usrInrfcGrpCd;
	}

	public long getAttributeId() {
		return this.attributeId;
	}
	public void setAttributeId(long attributeId) {
		this.attributeId = attributeId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EcommerUserGroupAttributeKey)) {
			return false;
		}
		EcommerUserGroupAttributeKey castOther = (EcommerUserGroupAttributeKey)other;
		return 
			this.usrInrfcGrpCd.equals(castOther.usrInrfcGrpCd)
			&& (this.attributeId == castOther.attributeId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.usrInrfcGrpCd.hashCode();
		hash = hash * prime + ((int) (this.attributeId ^ (this.attributeId >>> 32)));
		
		return hash;
	}
}