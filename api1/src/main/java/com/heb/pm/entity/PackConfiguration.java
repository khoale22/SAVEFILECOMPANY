package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a pack configuration.
 *
 * @author m314029
 * @since 2.21.0
 */
@Entity
@Table(name = "PK_CFG")
public class PackConfiguration implements CodeTable {

	private static final long serialVersionUID = -6478888954948427550L;

	public PackConfiguration() {}

	public PackConfiguration(CodeTable codeTable) {
		this.id = codeTable.getId();
		this.description = codeTable.getDescription();
	}

	@Id
	@Column(name = "PK_CFG_CD")
	private String id;

	@Column(name = "PK_CFG_DES")
	private String description;

	/**
	 * Returns Id.
	 *
	 * @return The Id.
	 **/
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id The Id.
	 **/
	@Override
	public PackConfiguration setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * Returns Description.
	 *
	 * @return The Description.
	 **/
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description.
	 *
	 * @param description The Description.
	 **/
	@Override
	public PackConfiguration setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * Compares another object to this one. The key is the only thing used to determine equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		PackConfiguration that = (PackConfiguration) o;

		return id != null ? id.equals(that.id) : that.id == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "PackConfiguration{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
