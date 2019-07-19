/*
 * VarietalType
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vn87351
 * @since 2.12.0
 */
@Entity
@Table(name="varietal_type")
public class VarietalType implements Serializable {
	private static final String DEFAULT_SORT_BY_VARIETAL_TYPE_CODE = "varietalTypeCode";
	/**
	 * format to show summary detail varietal type
	 */
	private static final String VARIETAL_TYPE_SUMMARY_FORMAT="%s [%s]";
	private static final long serialVersionUID = 1L;

	@Column(name="vartl_typ_abb")
	private String varietalTypeAbbreviations;

	@Id
	@Column(name="vartl_typ_cd")
	private Integer varietalTypeCode;

	@Column(name="vartl_typ_des")
	private String varietalTypeDescription;

	@JsonIgnoreProperties("varietalType")
	@OneToMany(mappedBy = "varietalType", fetch = FetchType.LAZY)
	private List<Varietal> varietal;
	/**
	 * the get Varietal Type Abbreviations
	 * @return Varietal Type Abbreviations
	 */
	public String getVarietalTypeAbbreviations() {
		return varietalTypeAbbreviations;
	}

	/**
	 * the set Varietal Type Abbreviations
	 * @param varietalTypeAbbreviations
	 * @return Varietal Type
	 */
	public VarietalType setVarietalTypeAbbreviations(String varietalTypeAbbreviations) {
		this.varietalTypeAbbreviations = varietalTypeAbbreviations;
		return this;
	}

	/**
	 * the get Varietal Type Code
	 * @return varietalTypeCode
	 */
	public Integer getVarietalTypeCode() {
		return varietalTypeCode;
	}

	/**
	 * the set Varietal Type Code
	 * @param varietalTypeCode
	 * @return VarietalType
	 */
	public VarietalType setVarietalTypeCode(Integer varietalTypeCode) {
		this.varietalTypeCode = varietalTypeCode;
		return this;
	}

	/**
	 * get Varietal Type Description
	 * @return varietal Type Description
	 */
	public String getVarietalTypeDescription() {
		return varietalTypeDescription;
	}

	/**
	 * set Varietal Type Description
	 * @param varietalTypeDescription
	 * @return Varietal Type
	 */
	public VarietalType setVarietalTypeDescription(String varietalTypeDescription) {
		this.varietalTypeDescription = varietalTypeDescription;
		return this;
	}
	/**
	 * get Varietal List
	 * @return List varietal
	 */
	public List<Varietal> getVarietal() {
		return varietal;
	}
	/**
	 * set Varietal Type
	 * @param varietal
	 * @return Varietal Type
	 */
	public VarietalType setVarietal(List<Varietal> varietal) {
		this.varietal = varietal;
		return this;
	}

	/**
	 * get summary field to show ui
	 * @return summary detail
	 */
	public String getVarietalTypeSummary(){
		return String.format(VARIETAL_TYPE_SUMMARY_FORMAT,StringUtils.trimToEmpty(this.getVarietalTypeDescription()), String.valueOf(this.getVarietalTypeCode()));
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
		VarietalType varietal = (VarietalType) o;
		return varietal.getVarietalTypeCode().equals(getVarietalTypeCode());
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = varietalTypeCode != null ? varietalTypeCode.hashCode() : 0;
		result = 31 * result + (varietalTypeAbbreviations != null ? varietalTypeAbbreviations.hashCode() : 0);
		result = 31 * result + (varietalTypeDescription != null ? varietalTypeDescription.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "VarietalType{" +
				"code='" + varietalTypeCode + '\'' +
				", varietalTypeAbb='" + varietalTypeAbbreviations + '\'' +
				", varietalTypeDesc='" + varietalTypeDescription + '\'' +
				'}';
	}
	/**
	 * Returns a default sort for the table.
	 *
	 * @return A default sort for the table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(VarietalType.DEFAULT_SORT_BY_VARIETAL_TYPE_CODE);
	}
}
