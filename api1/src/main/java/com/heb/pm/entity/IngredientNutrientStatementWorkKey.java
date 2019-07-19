/*
 * IngredientNutrientStatementWorkKey
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
 * Represents a key for the dynamic attribute of a Ingredient and Nutrient Statement Work Department.
 *
 * @author l730832
 * @since 2.3.0
 */
@Embeddable
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class IngredientNutrientStatementWorkKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "stmt_no")
	private long statementNumber;

	@Column(name = "rec_typ_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")  
	private String recTypCode;

	@Column(name = "seq_nbr")
	private long sequenceNumber;

	/**
	 * Gets statement number.
	 *
	 * @return the statement number
	 */
	public long getStatementNumber() {
		return statementNumber;
	}

	/**
	 * Sets statement number.
	 *
	 * @param statementNumber the statement number
	 */
	public void setStatementNumber(long statementNumber) {
		this.statementNumber = statementNumber;
	}

	/**
	 * Gets sequence number.
	 *
	 * @return the sequence number
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets sequence number.
	 *
	 * @param sequenceNumber the sequence number
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Gets rec typ code.
	 *
	 * @return the rec typ code
	 */
	public String getRecTypCode() {
		return recTypCode;
	}

	/**
	 * Sets rec typ code.
	 *
	 * @param recTypCode the rec typ code
	 */
	public void setRecTypCode(String recTypCode) {
		this.recTypCode = recTypCode;
	}

	/**
	 * Tests this object to another for equality.
	 *
	 * @param o The object to test against.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		IngredientNutrientStatementWorkKey that = (IngredientNutrientStatementWorkKey) o;

		if (statementNumber != that.statementNumber) return false;
		if (sequenceNumber != that.sequenceNumber) return false;
		return !(recTypCode != null ? !recTypCode.equals(that.recTypCode) : that.recTypCode != null);

	}

	/**
	 * Returns a hash code for this object. Equal objects return the same value. Unequal objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (int) (statementNumber ^ (statementNumber >>> 32));
		result = 31 * result + (recTypCode != null ? recTypCode.hashCode() : 0);
		result = 31 * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "IngredientNutrientStatementWorkKey{" +
				"statementNumber=" + statementNumber +
				", recTypCode='" + recTypCode + '\'' +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}
