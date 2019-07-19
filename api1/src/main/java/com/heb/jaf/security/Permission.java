/*
 * com.heb.jaf.security.Permission
 *
 * Copyright (c) 2014 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.jaf.security;

import org.springframework.security.core.GrantedAuthority;


/**
 * Represents a particular access level to a particular resource.
 *
 * @author r511759
 *
 */
public class Permission implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	private static final String AUTHORITY_FORMAT = "%S-%S";

	private String resourceName;
	private String accessType;

	/* (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		return String.format(Permission.AUTHORITY_FORMAT, this.resourceName, this.accessType);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Permission)) return false;

		Permission that = (Permission) o;

	/*		if (resourceName != null ? !resourceName.equals(that.resourceName) : that.resourceName != null) return false;
		return !(accessType != null ? !accessType.equals(that.accessType) : that.accessType != null);
		*/
		return this.checkAccess(that.getResourceName(), that.getAccessType());

	}

	@Override
	public int hashCode() {
		int result = resourceName != null ? resourceName.hashCode() : 0;
		result = 31 * result + (accessType != null ? accessType.hashCode() : 0);
		return result;
	}

	/**
	 * Check by resource name and access type to see if it matches this permission.
	 *
	 * @param resourceName The name of the resource to compare to.
	 * @param accessType The permission to compare to.
	 * @return True if this object is for the requested resourceName and permission. False otherwise.
	 */
	public boolean checkAccess(String resourceName, String accessType) {

		boolean resourceNameMatch = this.resourceName == null ? resourceName == null : this.resourceName.equals(resourceName);

		boolean accessTypeMatch = this.accessType == null ? accessType == null : this.accessType.equals(accessType);

		return resourceNameMatch && accessTypeMatch;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getAuthority();
	}


	/**
	 * Returns the name of the resource this permission is on.
	 *
	 * @return The name of the resource this permission is on.
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * Sets the name of the resource this permission is on.
	 *
	 * @param resourceName The name of the resource this permission is on.
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * Returns the type of access a user has on this resource.
	 *
	 * @return The type of access a user has on this resource.
	 */
	public String getAccessType() {
		return accessType;
	}

	/**
	 * Sets the type of access a user has on this resource.
	 *
	 * @param accessType The type of access a user has on this resource.
	 */
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
}
