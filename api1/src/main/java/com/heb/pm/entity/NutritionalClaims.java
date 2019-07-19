/*
 *  Nutritional
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This holds all of the nutritional information that is attached to a upc.
 *
 * @author l730832
 * @since 2.11.0
 */
@Entity
@Table(name = "prod_scn_ntrntl")
public class NutritionalClaims implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NutritionalClaimsKey key;

	@ManyToOne
	@JoinColumn(name = "prod_ntrntl_cd", referencedColumnName = "prod_ntrntl_cd", insertable = false, updatable = false)
	private NutritionalClaimsAttribute nutritionalClaimsAttribute;

	@Column(name = "src_system_id")
	private Long sourceSystemId;

	@Transient
	private boolean selected;

	/**
	 * Returns the NutritionalClaimsAttribute. The nutritional Attribute holds the definition and the abbreviation according
	 * to the nutritional claims code. The code table for nutritional.
	 *
	 * @return NutritionalClaimsAttribute
	 */
	public NutritionalClaimsAttribute getNutritionalClaimsAttribute() {
		return nutritionalClaimsAttribute;
	}

	/**
	 * Sets the NutritionalClaimsAttribute
	 *
	 * @param nutritionalClaimsAttribute The NutritionalClaimsAttribute
	 */
	public void setNutritionalClaimsAttribute(NutritionalClaimsAttribute nutritionalClaimsAttribute) {
		this.nutritionalClaimsAttribute = nutritionalClaimsAttribute;
	}

	/**
	 * Returns the Key
	 *
	 * @return Key
	 */
	public NutritionalClaimsKey getKey() {
		return key;
	}

	/**
	 * Sets the Key
	 *
	 * @param key The Key
	 */
	public void setKey(NutritionalClaimsKey key) {
		this.key = key;
	}

	/**
	 * Returns the SourceSystemId. The source system id tells whether it has come from vestcom(8) or product maintenance(4).
	 *
	 * @return SourceSystemId
	 */
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Sets the SourceSystemId
	 *
	 * @param sourceSystemId The SourceSystemId
	 */
	public void setSourceSystemId(Long sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	/**
	 * @return Gets the value of selected and returns selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Sets the selected
	 */
	public boolean isSelected() {
		return selected;
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

		NutritionalClaims that = (NutritionalClaims) o;

		return key != null ? !key.equals(that.key) : that.key != null;
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
}
