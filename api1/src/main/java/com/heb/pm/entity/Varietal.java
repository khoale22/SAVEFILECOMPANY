/*
 * Varietal
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name="varietal")
public class Varietal  implements Serializable {
	private static final String DEFAULT_SORT_BY_VARIETAL_ID = "varietalId";
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="vartl_id")
	private Integer varietalId;

	@Column(name="vartl_typ_cd")
	private String varietalTypeCode;

	@Column(name="vartl_nm")
	private String varietalName;

	@JsonIgnoreProperties("varietal")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name="vartl_typ_cd", referencedColumnName = "vartl_typ_cd", insertable = false, updatable = false, nullable = false)
	})
	private VarietalType varietalType;
	/**
	 * get  Varietal Type
	 * @return VarietalType
	 */
	public VarietalType getVarietalType() {
		return varietalType;
	}
	/**
	 * set Varietal Type
	 * @param varietalType
	 * @return Varietal
	 */
	public Varietal setVarietalType(VarietalType varietalType) {
		this.varietalType = varietalType;
		return this;
	}

	/**
	 * get  Varietal Id
	 * @return id
	 */
	public Integer getVarietalId() {
		return varietalId;
	}

	/**
	 * set Varietal id
	 * @param varietalId
	 * @return
	 */
	public Varietal setVarietalId(Integer varietalId) {
		this.varietalId = varietalId;
		return this;
	}

	/**
	 * get varietal Type Code
	 * @return String
	 */
	public String getVarietalTypeCode() {
		return varietalTypeCode;
	}

	/**
	 * set varietal type code
	 * @param varietalTypeCode
	 * @return
	 */
	public Varietal setVarietalTypeCode(String varietalTypeCode) {
		this.varietalTypeCode = varietalTypeCode;
		return this;
	}

	/**
	 * get varietal Name
	 * @return
	 */
	public String getVarietalName() {
		return varietalName;
	}

	/**
	 * set varietal name
	 * @param varietalName
	 * @return
	 */
	public Varietal setVarietalName(String varietalName) {
		this.varietalName = varietalName;
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
		Varietal varietal = (Varietal) o;
		return varietal.getVarietalId()==this.getVarietalId();
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = varietalId != null ? varietalId.hashCode() : 0;
		result = 31 * result + (varietalTypeCode != null ? varietalTypeCode.hashCode() : 0);
		result = 31 * result + (varietalName != null ? varietalName.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "Varietal{" +
				"id='" + varietalId + '\'' +
				", varietalName='" + varietalName + '\'' +
				", varietalTypeCode='" + varietalTypeCode + '\'' +
				'}';
	}
	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(Varietal.DEFAULT_SORT_BY_VARIETAL_ID);
	}
}
