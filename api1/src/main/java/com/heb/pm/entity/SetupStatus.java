package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a record in the ps_stat code table.
 *
 * @author d116773
 * @since 2.17.0
 */
@Entity
@Table(name = "ps_stat")
public class SetupStatus {

	@Id
	@Column(name="pd_setup_stat_cd")
	private String statusCode;

	@Column(name="pd_setup_stat_des")
	private String statusDescription;

	/**
	 * Returns the ID for this status.
	 *
	 * @return The ID for this status.
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * Sets the ID for this status.
	 *
	 * @param statusCode The ID for this status.
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Returns the description for this status.
	 *
	 * @return The description for this status.
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * Sets the description for this status.]
	 *
	 * @param statusDescription The description for this status.
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/**
	 * Compares this object to another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SetupStatus)) return false;

		SetupStatus that = (SetupStatus) o;

		return statusCode != null ? statusCode.equals(that.statusCode) : that.statusCode == null;
	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return statusCode != null ? statusCode.hashCode() : 0;
	}

	/**
	 * Retuns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "SetupStatus{" +
				"statusCode='" + statusCode + '\'' +
				", statusDescription='" + statusDescription + '\'' +
				'}';
	}
}
