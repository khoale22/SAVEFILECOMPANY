/*
 *  ProductPkVariationKey
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The primary key class for the prod_pk_variation database table.
 * @author vn73545
 * @since 2.12.0
 */
@Embeddable
public class ProductPkVariationKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="scn_cd_id")
	private Long upc;
	
	@Column(name="seq_nbr")
	private Long sequence;
	
	@Column(name="src_system_id")
	private Integer sourceSystem;

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
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
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
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductPkVariationKey)) return false;

		ProductPkVariationKey that = (ProductPkVariationKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;
		return sourceSystem != null ? sourceSystem.equals(that.sourceSystem) : that.sourceSystem == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
		result = 31 * result + (sourceSystem != null ? sourceSystem.hashCode() : 0);
		return result;
	}
}
