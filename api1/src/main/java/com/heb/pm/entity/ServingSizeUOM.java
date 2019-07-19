/*
 *  ServingSizeUOM
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * the entity for serving size uom table
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name = "srvng_sz_uom")
public class ServingSizeUOM  implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "srvng_sz_uom_cd")
	private String servingSizeUomCode;

	@Column(name = "srvng_sz_uom_abb")
	private String servingSizeUomAbbriviation;

	@Column(name = "srvng_sz_uom_des")
	private String servingSizeUomDescription;

	@Column(name = "src_system_id")
	private int sourceSystem;

	@Column(name = "xtrnl_id")
	private String xtrnlId;

	/**
	 * Returns the serving Size Uom Code
	 *
	 * @return servingSizeUomCode
	 */
	public String getServingSizeUomCode() {
		return servingSizeUomCode;
	}
	/**
	 * Sets the servingSizeUomCode
	 *
	 * @param servingSizeUomCode The servingSizeUomCode
	 */
	public ServingSizeUOM setServingSizeUomCode(String servingSizeUomCode) {
		this.servingSizeUomCode = servingSizeUomCode;
		return this;
	}
	/**
	 * Returns the serving Size Uom Abbriviation
	 *
	 * @return servingSizeUomAbbriviation
	 */
	public String getServingSizeUomAbbriviation() {
		return servingSizeUomAbbriviation;
	}
	/**
	 * Sets the servingSizeUomAbbriviation
	 *
	 * @param servingSizeUomAbbriviation The servingSizeUomAbbriviation
	 */
	public ServingSizeUOM setServingSizeUomAbbriviation(String servingSizeUomAbbriviation) {
		this.servingSizeUomAbbriviation = servingSizeUomAbbriviation;
		return this;
	}
	/**
	 * Returns the serving Size Uom Description
	 *
	 * @return servingSizeUomDescription
	 */
	public String getServingSizeUomDescription() {
		return servingSizeUomDescription;
	}
	/**
	 * Sets the servingSizeUomDescription
	 *
	 * @param servingSizeUomDescription The servingSizeUomDescription
	 */
	public ServingSizeUOM setServingSizeUomDescription(String servingSizeUomDescription) {
		this.servingSizeUomDescription = servingSizeUomDescription;
		return this;
	}
	/**
	 * Returns the source System
	 *
	 * @return sourceSystem
	 */
	public int getSourceSystem() {
		return sourceSystem;
	}
	/**
	 * Sets the sourceSystem
	 *
	 * @param sourceSystem The sourceSystem
	 */
	public ServingSizeUOM setSourceSystem(int sourceSystem) {
		this.sourceSystem = sourceSystem;
		return this;
	}
	/**
	 * Returns the xtrnlId
	 *
	 * @return xtrnlId
	 */
	public String getXtrnlId() {
		return xtrnlId;
	}
	/**
	 * Sets the xtrnlId
	 *
	 * @param xtrnlId The xtrnlId
	 */
	public ServingSizeUOM setXtrnlId(String xtrnlId) {
		this.xtrnlId = xtrnlId;
		return this;
	}
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

		ServingSizeUOM servingSizeUOM = (ServingSizeUOM) o;

		return servingSizeUomCode != null ?
				servingSizeUomCode.equals(servingSizeUOM.servingSizeUomCode):false;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = servingSizeUomCode != null ? servingSizeUomCode.hashCode() : 0;
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ServingSizeUOM{" +
				"servingSizeUomCode=" + servingSizeUomCode +
				", servingSizeUomAbbriviation=" + servingSizeUomAbbriviation +
				", servingSizeUomDescription=" + servingSizeUomDescription +
				", sourceSystem=" + sourceSystem +
				'}';
	}
}
