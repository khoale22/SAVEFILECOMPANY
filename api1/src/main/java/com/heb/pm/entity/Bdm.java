/*
 * BDM
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents a bdm.
 *
 * @author m314029
 * @since 2.0.6
 */
@Entity
@Table(name = "bdm")
//dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
// dB2Oracle changes vn00907
})
public class Bdm implements Serializable{

	// default constructor
	public Bdm(){super();}

	// copy constructor
	public Bdm(Bdm bdm){
		super();
		this.setBdmCode(bdm.getBdmCode());
		this.setFirstName(bdm.getFirstName());
		this.setLastName(bdm.getBdmCode());
		this.setFullName(bdm.getFullName());
		this.setBdmImsCode(bdm.getBdmImsCode());
		this.setPrimeFunctionCodeOne(bdm.getPrimeFunctionCodeOne());
		this.setPrimeGroupNumberOne(bdm.getPrimeGroupNumberOne());
		this.setBdmOmiNumber(bdm.getBdmOmiNumber());
	}

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s[%s]";

	@Id
	@Column(name="bdm_cd")
	// dB2Oracle changes vn00907
	@Type(type="fixedLengthCharPK") 
	private String bdmCode;

	@Column(name="bdm_frst_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String firstName;

	@Column(name="dbm_lst_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String lastName;

	@Column(name="bdm_full_nm")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String fullName;

	@Column(name="bdm_ims_cd")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String bdmImsCode;

	@Column(name="prim_func_cd_1")
	//db2o changes  vn00907
	@Type(type="fixedLengthChar")    
	private String primeFunctionCodeOne;

	@Column(name="prim_grp_nbr_1")
	private Integer primeGroupNumberOne;

	@Column(name="usr_id")
	private String userId;

	@Column(name = "bdm_omi_nbr", nullable = false)
	private Integer bdmOmiNumber;

	/**
	 * Returns the bdm code of the bdm.
	 *
	 * @return The bdm code of the bdm.
	 */
	public String getBdmCode() {
		return bdmCode;
	}

	/**
	 * Sets the bdm code of the bdm.
	 *
	 * @param bdmCode The bdm code of the bdm.
	 */
	public void setBdmCode(String bdmCode) {
		this.bdmCode = bdmCode;
	}

	/**
	 * Returns the first name of the bdm.
	 *
	 * @return The first name of the bdm.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the bdm.
	 *
	 * @param firstName The first name of the bdm.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the last name of the bdm.
	 *
	 * @return The last name of the bdm.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the bdm.
	 *
	 * @param lastName The last name of the bdm.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the full name of the bdm.
	 *
	 * @return The full name of the bdm.
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the full name of the bdm.
	 *
	 * @param fullName The full name of the bdm.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Returns a description bdm to display on the GUI.
	 *
	 * @return A description bdm to display on the GUI.
	 */
	public String getDisplayName() {
		return this.bdmCode == null ? "" :
				String.format(Bdm.DISPLAY_NAME_FORMAT, this.fullName.trim(), this.bdmCode.trim());
	}

	/**
	 * Returns a unique ID for this bdm that can be used where a distinct number is needed like in a list.
	 * It relies on the fact that, thought the table does not enforce it, bdm IDs are unique to the
	 * bdm. If the key is not set for this object, it returns 0.
	 *
	 * @return A unique ID for this bdm.
	 */
	public String getNormalizedId() {

		return this.bdmCode == null ? "0" : this.bdmCode;
	}

	/**
	 * Returns BdmImsCode.
	 *
	 * @return The BdmImsCode.
	 **/
	public String getBdmImsCode() {
		return bdmImsCode;
	}

	/**
	 * Sets the BdmImsCode.
	 *
	 * @param bdmImsCode The BdmImsCode.
	 **/
	public void setBdmImsCode(String bdmImsCode) {
		this.bdmImsCode = bdmImsCode;
	}

	/**
	 * Returns PrimeFunctionCodeOne.
	 *
	 * @return The PrimeFunctionCodeOne.
	 **/
	public String getPrimeFunctionCodeOne() {
		return primeFunctionCodeOne;
	}

	/**
	 * Sets the PrimeFunctionCodeOne.
	 *
	 * @param primeFunctionCodeOne The PrimeFunctionCodeOne.
	 **/
	public void setPrimeFunctionCodeOne(String primeFunctionCodeOne) {
		this.primeFunctionCodeOne = primeFunctionCodeOne;
	}

	/**
	 * Returns PrimeGroupNumberOne.
	 *
	 * @return The PrimeGroupNumberOne.
	 **/
	public Integer getPrimeGroupNumberOne() {
		return primeGroupNumberOne;
	}

	/**
	 * Sets the PrimeGroupNumberOne.
	 *
	 * @param primeGroupNumberOne The PrimeGroupNumberOne.
	 **/
	public void setPrimeGroupNumberOne(Integer primeGroupNumberOne) {
		this.primeGroupNumberOne = primeGroupNumberOne;
	}
	/**
	 * Returns tuser id.
	 * @return the user id.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * @param userId the user id.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the BDM OMI Number.
	 *
	 * @return the BDM OMI Number.
	 */
	public Integer getBdmOmiNumber() {
		return bdmOmiNumber;
	}

	/**
	 * Sets the BDM OMI Number.
	 *
	 * @param bdmOmiNumber the BDM OMI Number.
	 */
	public void setBdmOmiNumber(Integer bdmOmiNumber) {
		this.bdmOmiNumber = bdmOmiNumber;
	}

	/**
	 * Gets the BDM OMI Number displayed with padded 0s.
	 *
	 * @return the BDM OMI Number displayed with padded 0s.
	 */
	public String getBdmOmiNumberDisplayText() {
		return StringUtils.leftPad(this.bdmOmiNumber.toString(), 3, '0');
	}
	
	/**
	 * Tests for equality with another object. Equality is based on the key.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Bdm bdm = (Bdm) o;

		return !(bdmCode != null ? !bdmCode.equals(bdm.bdmCode) : bdm.bdmCode != null);

	}

	/**
	 * Returns a hash code for the object. Equal objects return the same falue. Unequal objects (probably) return
	 * different values.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return bdmCode != null ? bdmCode.hashCode() : 0;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "BDM{" +
				"bdmCode='" + bdmCode + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", fullName='" + fullName + '\'' +
				", userId='" + userId + '\'' +
				'}';
	}
}
