/*
 * com.heb.jaf.security.HebUserDetails
 *
 * Copyright (c) 2010 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Information about a specific user.
 *
 * @author r511759
 */
public class HebUserDetails extends User {

	private static final long serialVersionUID = 1L;
	private static final String FAKE_PASSWORD =  "!@#$%^";

	private String displayName;
	private String hebJobCode;
	private String hebJobDesc;
	private String departmentNumber;
	private String mail;
	private String mobile;

	//Valid only for vendors
	private String vendorOrgId;
	private String vendorOrgName;
	private String hebGLlocation;
	private String hebLocType;
	/**
	 * Creates a new HebUserDetails object.
	 *
	 * @param username The one-pass ID of the user.
	 * @param authorities What resources does this user have access to?
	 */
	public HebUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {
		super(username, HebUserDetails.FAKE_PASSWORD, false, false, false, false, authorities);
	}

	/**
	 * Sets the user's display name. This will be their full name.
	 *
	 * @param displayName The user's full name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Retuns the user's display name. This is their full name.
	 *
	 * @return The user's full name.
	 */
	public String getDisplayName() {
		return this.displayName;
	}


	/**
	 * Sets the user's job code.
	 *
	 * @param hebJobCode The user's job code.
	 */
	public void setHebJobCode(String hebJobCode) {
		this.hebJobCode = hebJobCode;
	}


	/**
	 * If the user is a partner, returns the user's job code.
	 *
	 * @return The user's job code. Null if the user is a vendor.
	 */
	public String getHebJobCode() {
		return this.hebJobCode;
	}


	/**
	 * Set's the user's job description.
	 *
	 * @param hebJobDesc The user's job description.
	 */
	public void setHebJobDesc(String hebJobDesc) {
		this.hebJobDesc = hebJobDesc;
	}


	/**
	 * If the user is a partner, return's the user's job description.
	 *
	 * @return The user's job description. Null if the user is a vendor.
	 */
	public String getHebJobDesc() {
		return this.hebJobDesc;
	}


	/**
	 * Sets the user's email address.
	 *
	 * @param mail The user's email address.
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}


	/**
	 * Returns the user's email address.
	 *
	 * @return The user's email address.
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * Sets the user's organization ID.
	 *
	 * @param vendorOrgId The vendor's organization ID.
	 */
	// TODO: what is a vendor's org ID?
	public void setVendorOrgId(String vendorOrgId) {
		this.vendorOrgId = vendorOrgId;
	}


	/**
	 * If the user is a vendor, returns the user's organization ID.
	 *
	 * @return The vendor's organization ID. Null if the user is a partner.
	 */
	public String getVendorOrgId() {
		return this.vendorOrgId;
	}


	/**
	 * Sets the vendor's organization name.
	 *
	 * @param vendorOrgName The vendor's organization name.
	 */
	// TODO: what is the organization name?
	public void setVendorOrgName(String vendorOrgName) {
		this.vendorOrgName = vendorOrgName;
	}


	/**
	 *  If the user is a vendor, returns the vendor's organization name.
	 *
	 * @return The vendor's organization name. Null if the user is a partner.
	 */
	public String getVendorOrgName() {
		return this.vendorOrgName;
	}


	/**
	 * Sets the user's department number.
	 *
	 * @param departmentNumber The user's department number.
	 */
	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}


	/**
	 * If the user is a partner, returns the user's department number.
	 *
	 * @return The user's department number. Null if the user is a vendor.
	 */
	public String getDepartmentNumber() {
		return this.departmentNumber;
	}


	/**
	 * Sets the user's mobile number.
	 *
	 * @param mobile The users's mobile number.
	 */
	// TODO: confirm that's really what this is.
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	/**
	 * Returns the user's mobile number.
	 *
	 * @return The user's mobile number.
	 */
	// TODO: confirm for vendor.
	public String getMobile() {
		return mobile;
	}


	/**
	 * Set's the users GL location. For store partners, this will be the store they work for.
	 *
	 * @param hebGLlocation The user's GL location.
	 */
	public void setHebGLlocation(String hebGLlocation) {
		this.hebGLlocation = hebGLlocation;
	}


	/**
	 * If the user is a partner, returns the user's GL location.
	 *
	 * @return The user's GL location. Null if the user is a vendor.
	 */
	public String getHebGLlocation() {
		return hebGLlocation;
	}

	/**
	 * If the user is a partner, returns the user's location type.
	 * @return The user's location type.
	 */
	public String getHebLocType() {
		return hebLocType;
	}

	/**
	 * Set's the users location type.
	 * @param hebLocType the location type
	 */
	public void setHebLocType(String hebLocType) {
		this.hebLocType = hebLocType;
	}
}
