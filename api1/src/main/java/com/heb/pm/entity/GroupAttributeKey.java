package com.heb.pm.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the GRP_ATTR database table.
 * 
 */
@Embeddable
public class GroupAttributeKey implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="RLSHP_GRP_ID", insertable=false, updatable=false)
	private long rlshpGrpId;

	@Column(name="ATTR_ID", insertable=false, updatable=false)
	private long attrtibuteId;

	public GroupAttributeKey() {
	}
	public long getRlshpGrpId() {
		return this.rlshpGrpId;
	}
	public void setRlshpGrpId(long rlshpGrpId) {
		this.rlshpGrpId = rlshpGrpId;
	}
	public long getAttrtibuteId() {
		return this.attrtibuteId;
	}
	public void setAttrtibuteId(long attrtibuteId) {
		this.attrtibuteId = attrtibuteId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GroupAttributeKey)) {
			return false;
		}
		GroupAttributeKey castOther = (GroupAttributeKey)other;
		return 
			(this.rlshpGrpId == castOther.rlshpGrpId)
			&& (this.attrtibuteId == castOther.attrtibuteId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.rlshpGrpId ^ (this.rlshpGrpId >>> 32)));
		hash = hash * prime + ((int) (this.attrtibuteId ^ (this.attrtibuteId >>> 32)));
		
		return hash;
	}
}