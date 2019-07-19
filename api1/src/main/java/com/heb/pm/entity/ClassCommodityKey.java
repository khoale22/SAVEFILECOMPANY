/*
 * ClassCommodityKey
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the pd_class_commodity table.
 *
 * @author d116773
 * @since 2.0.2
 */
@Embeddable
public class ClassCommodityKey implements Serializable {// default constructor

	// default constructor
	public ClassCommodityKey(){super();}

	// copy constructor
	public ClassCommodityKey(ClassCommodityKey key){
		super();
		this.setClassCode(key.getClassCode());
		this.setCommodityCode(key.getCommodityCode());
	}

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name="pd_omi_com_cls_cd")
	private Integer classCode;

	@Column(name="pd_omi_com_cd")
	private Integer commodityCode;

	/**
	 * Returns the OMI Class for this object.
	 *
	 * @return The OMI Class for this object.
	 */
	public Integer getClassCode() {
		return classCode;
	}

	/**
	 * Sets the OMI Class for this object.
	 *
	 * @param classCode The Class for this object.
	 */
	public void setClassCode(Integer classCode) {
		this.classCode = classCode;
	}

	/**
	 * Returns the Commodity for this object.
	 *
	 * @return The Commodity for this object.
	 */
	public Integer getCommodityCode() {
		return commodityCode;
	}

	/**
	 * Sets the Commodity for this object.
	 *
	 * @param commodityCode The Commodity for this object.
	 */
	public void setCommodityCode(Integer commodityCode) {
		this.commodityCode = commodityCode;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ClassCommodityKey that = (ClassCommodityKey) o;

		if (classCode != null ? !classCode.equals(that.classCode) : that.classCode != null) return false;
		return commodityCode != null ? commodityCode.equals(that.commodityCode) : that.commodityCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = classCode != null ? classCode.hashCode() : 0;
		result = ClassCommodityKey.PRIME_NUMBER * result + (commodityCode != null ? commodityCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ClassCommodityKey{" +
				"classCode=" + classCode +
				", commodityCode=" + commodityCode +
				'}';
	}
}

