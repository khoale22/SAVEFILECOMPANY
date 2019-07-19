/*
 * IngredientNutrientStatementWork
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.io.Serializable;

/**
 * Represents a dynamic attribute of a Ingredient and Nutrient Statement Work Department.
 *
 * @author l730832
 * @since 2.3.0
 */
@Entity
@Table(name = "wrk_ingrd_ntrnt")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
//dB2Oracle changes vn00907
public class IngredientNutrientStatementWork implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private IngredientNutrientStatementWorkKey key;

	@Column(name = "dept_nbr")
	private long deptNumber;

	@Column(name = "lst_updt_uid")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")   
	private String userId;

	/**
	 * Gets key.
	 *
	 * @return the key
	 */
	public IngredientNutrientStatementWorkKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the key
	 */
	public void setKey(IngredientNutrientStatementWorkKey key) {
		this.key = key;
	}

	/**
	 * Gets dept number.
	 *
	 * @return the dept number
	 */
	public long getDeptNumber() {
		return deptNumber;
	}

	/**
	 * Sets dept number.
	 *
	 * @param deptNumber the dept number
	 */
	public void setDeptNumber(long deptNumber) {
		this.deptNumber = deptNumber;
	}

	/**
	 * Gets user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets user id.
	 *
	 * @param userId the user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Compares this object with another for equality. Equality is based on the key.
	 *
	 * @param o The other object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IngredientNutrientStatementWork)) return false;

		IngredientNutrientStatementWork that = (IngredientNutrientStatementWork) o;

		return key.equals(that.key);
	}

	/**
	 * Returns a hash code for the object. Equal objects return the same hash code. Unequal objects (probably) return
	 * different hash codes.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "IngredientNutrientStatementWork{" +
				"key=" + key +
				", deptNumber=" + deptNumber +
				", userId='" + userId + '\'' +
				'}';
	}
}
