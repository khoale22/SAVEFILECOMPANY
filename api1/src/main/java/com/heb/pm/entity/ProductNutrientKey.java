/*
 * ProductNutrientKey
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents the key of the ap_location table.
 *
 * @author vn73545
 * @since 2.5.0
 */
@Embeddable
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductNutrientKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "scn_cd_id")
	private Long upc;

	@Column(name = "ntrnt_mst_id")
	private Integer masterId;

	@Column(name = "src_system_id")
	private Integer sourceSystem;

	@Column(name = "val_preprd_typ_cd")
	private Integer valPreprdTypCd;

	/**
	 * @return the upc
	 */
	public Long getUpc() {
		return upc;
	}

	/**
	 * @param upc the upc to set
	 */
	public void setUpc(Long upc) {
		this.upc = upc;
	}

	/**
	 * @return the masterId
	 */
	public Integer getMasterId() {
		return masterId;
	}

	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
	}

	/**
	 * @return the sourceSystem
	 */
	public Integer getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * @param sourceSystem the sourceSystem to set
	 */
	public void setSourceSystem(Integer sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * @return the valPreprdTypCd
	 */
	public Integer getValPreprdTypCd() {
		return valPreprdTypCd;
	}

	/**
	 * @param valPreprdTypCd the valPreprdTypCd to set
	 */
	public void setValPreprdTypCd(Integer valPreprdTypCd) {
		this.valPreprdTypCd = valPreprdTypCd;
	}

	/**
	 * Compares another object to this one for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductNutrientKey)) return false;

		ProductNutrientKey that = (ProductNutrientKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		if (masterId != null ? !masterId.equals(that.masterId) : that.masterId != null) return false;
		if (sourceSystem != null ? !sourceSystem.equals(that.sourceSystem) : that.sourceSystem != null) return false;
		return valPreprdTypCd != null ? valPreprdTypCd.equals(that.valPreprdTypCd) : that.valPreprdTypCd == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = 31 * result + (masterId != null ? masterId.hashCode() : 0);
		result = 31 * result + (sourceSystem != null ? sourceSystem.hashCode() : 0);
		result = 31 * result + (valPreprdTypCd != null ? valPreprdTypCd.hashCode() : 0);
		return result;
	}
}
