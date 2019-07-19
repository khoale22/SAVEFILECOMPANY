/*
 * User
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.entity;

import java.io.Serializable;

/**
 * The type User.
 *
 * @author l730832
 * @since  2.6.0
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NAME_FORMAT = "%s [%s]";
	private String uid;
	private String hebLegacyID;
	private String givenName;
	private String lastName;
	private String fullName;
	private String mail;
	private String telephoneNumber;

	public User() {
	}

	public User(String uid) {
		this.uid = uid;
	}

	public User(String uid, String hebLegacyID, String hebJobCode, String hebJobDesc, String mail, String givenName, String lastName) {
		super();
		this.uid = uid;
		this.hebLegacyID = hebLegacyID;
		this.givenName = givenName;
		this.lastName = lastName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * Returns the Uid
	 *
	 * @return Uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * Sets the Uid
	 *
	 * @param uid The Uid
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Returns the HebLegacyID
	 *
	 * @return HebLegacyID
	 */
	public String getHebLegacyID() {
		return hebLegacyID;
	}

	/**
	 * Sets the HebLegacyID
	 *
	 * @param hebLegacyID The HebLegacyID
	 */
	public void setHebLegacyID(String hebLegacyID) {
		this.hebLegacyID = hebLegacyID;
	}

	/**
	 * Returns the GivenName
	 *
	 * @return GivenName
	 */
	public String getGivenName() {
		return givenName;
	}

	/**
	 * Sets the GivenName
	 *
	 * @param givenName The GivenName
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	/**
	 * Returns the Sn
	 *
	 * @return Sn
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the Sn
	 *
	 * @param lastName The Sn
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the FullName
	 *
	 * @return FullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Sets the FullName
	 *
	 * @param fullName The FullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * Returns display name of user.
	 * @return
	 */
	public String getDisplayName() {
		return String.format(DISPLAY_NAME_FORMAT,
				this.getFullName(), this.getUid());
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

		User user = (User) o;

		if (uid != null ? !uid.equals(user.uid) : user.uid != null) return false;
		if (hebLegacyID != null ? !hebLegacyID.equals(user.hebLegacyID) : user.hebLegacyID != null) return false;
		if (givenName != null ? !givenName.equals(user.givenName) : user.givenName != null) return false;
		if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
		return fullName != null ? fullName.equals(user.fullName) : user.fullName == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = uid != null ? uid.hashCode() : 0;
		result = 31 * result + (hebLegacyID != null ? hebLegacyID.hashCode() : 0);
		result = 31 * result + (givenName != null ? givenName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "User{" +
				"uid='" + uid + '\'' +
				", hebLegacyID='" + hebLegacyID + '\'' +
				", givenName='" + givenName + '\'' +
				", lastName='" + lastName + '\'' +
				", fullName='" + fullName + '\'' +
				'}';
	}
}
