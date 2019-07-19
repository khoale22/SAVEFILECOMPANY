/*
 *  Country
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Represents country information for import.
 *
 * @author s573181
 * @since 2.5.0
 */
@Entity
@Table(name = "cntry_cd")
//dB2Oracle changes vn00907 starts
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class Country  implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%d]";
	/**
	 * Holds display name format to show on grid.
	 */
	private static final String DISPLAY_NAME_FORMAT_ON_GRID = "%s - %d";

	/**
	 * The constand FILED_NAME_DEFAULT_SORT.
	 */
	private static final String COUNTRY_SORT_FIELD = "countryName";

	@Id
	@Column(name = "cntry_id")
	private int countryId;

	@Column(name = "cntry_abb")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String countryAbbreviation;

	@Column(name = "cntry_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")
	private String countryName;

	@Column(name = "cntry_iso_a3_cod")
	@Type(type="fixedLengthChar")
	private String countIsoA3Cod;

	@Column(name = "cntry_iso_n3_cd")
	private int countIsoN3Cd;
	/**
	 * Returns country id.
	 *
	 * @return country id.
	 */
	public int getCountryId() {
		return countryId;
	}

	/**
	 * Sets country id.
	 *
	 * @param countryId the country id.
	 */
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	/**
	 * Returns country abbreviation.
	 *
	 * @return country abbreviation.
	 */
	public String getCountryAbbreviation() {
		return countryAbbreviation;
	}

	/**
	 * Sets the countryAbbreviation.
	 *
	 * @param countryAbbreviation The countryAbbreviation.
	 */
	public void setCountryAbbreviation(String countryAbbreviation) {
		this.countryAbbreviation = countryAbbreviation;
	}

	/**
	 * Returns country name.
	 *
	 * @return country name.
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Sets the country name.
	 *
	 * @param countryName the country name.
	 */
	public void setCountryName(String countryName) { this.countryName = countryName; }

	/**
	 * Get count iso a 3 cod.
	 *
	 * @return count iso a 3 cod
	 */
	public String getCountIsoA3Cod() {
		return countIsoA3Cod;
	}

	/**
	 * Sets the count iso a 3 cod.
	 *
	 * @param countIsoA3Cod the count iso a 3 cod.
	 */
	public void setCountIsoA3Cod(String countIsoA3Cod) {
		this.countIsoA3Cod = countIsoA3Cod;
	}

	/**
	 * Get count iso n 3 cd.
	 *
	 * @return the count iso n 3 cd.
	 */
	public int getCountIsoN3Cd() {
		return countIsoN3Cd;
	}

	/**
	 * Sets count iso n 3 cd.
	 *
	 * @param countIsoN3Cd the count iso n 3 cd.
	 */
	public void setCountIsoN3Cd(int countIsoN3Cd) {
		this.countIsoN3Cd = countIsoN3Cd;
	}

	/**
	 * Displays the country name.
	 *
	 * @return The country name and id in the format USA[1]
	 */
	public String getDisplayName(){
		return String.format(Country.DISPLAY_NAME_FORMAT, this.getCountryName().trim(),
				this.getCountryId());
	}

	/**
	 * Displays the country name to show on grid.
	 *
	 * @return The country name and id in the format USA[1]
	 */
	public String getDisplayNameOnGrid(){
		return String.format(Country.DISPLAY_NAME_FORMAT_ON_GRID, this.getCountryName().trim(),
				this.getCountryId());
	}

	/**
	 * Displays the CountIso to show on grid.
	 *
	 * @return The CountIso and id in the format String [1]
	 */
	public String getDisplayCountIsoOnGrid(){
		return String.format(Country.DISPLAY_NAME_FORMAT_ON_GRID, this.getCountIsoA3Cod().trim(),
				this.getCountIsoN3Cd());
	}

	/**
	 * Compares another object to this one. If that object is a ImportItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Country country = (Country) o;

		return countryId == country.countryId;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return countryId;
	}

	/**
	 * Returns the default sort order for the country table.
	 *
	 * @return The default sort order for the country table.
	 */
	public static Sort getDefaultSort() {
		return new Sort(
				new Sort.Order(Sort.Direction.ASC, Country.COUNTRY_SORT_FIELD)
		);
	}
}
