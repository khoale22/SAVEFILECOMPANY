package com.heb.scaleMaintenance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity for status code.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "STAT_CD")
public class Status implements Serializable{
	private static final long serialVersionUID = -4780973841502294382L;

	@Id
	@Column(name = "STAT_ID", updatable = false)
	private Integer id;

	@Column(name = "STAT_DES")
	private String description;

	public enum Code {
		// the order of these is the order in which a user expects the flow of statuses to be. If a new 'state' is
		// added, it needs to be added in the respective location in the flow. This order is used in an 'EnumMap' to
		// sort the data based on the order defined here
		READY(2, "Ready"),
		IN_PROGRESS(0, "In Progress"),
		TRANSMITTING(3, "Transmitting"),
		TRANSMITTED(6, "Transmitted"),
		TIMED_OUT(4, "Timed out"),
		COMPLETED(1, "Completed"),
		ERROR(5, "Error");

		Integer id;
		String description;

		/**
		 * Constructor for a status code.
		 *
		 * @param id ID of the status.
		 * @param description Description of the status.
		 */
		Code(Integer id, String description) {
			this.id = id;
			this.description = description;
		}

		/**
		 * Returns the id that represents each value.
		 *
		 * @return The id that represents each value.
		 */
		public Integer getId() {
			return this.id;
		}

		/**
		 * Returns Description.
		 *
		 * @return The Description.
		 **/
		public String getDescription() {
			return description;
		}

		/**
		 * Gets a code given an id.
		 *
		 * @param id ID to look for.
		 * @return Code matching the id, or null if not found.
		 */
		public static Code getById(Integer id){
			if(id != null) {
				for (Code code : Code.values()) {
					if (code.getId().equals(id)){
						return code;
					}
				}
			}
			return null;
		}
	}

	/**
	 * Returns Id.
	 *
	 * @return The Id.
	 **/
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id The Id.
	 **/
	public Status setId(Integer id) {
		this.id = id;
		return this;
	}

	/**
	 * Returns Description.
	 *
	 * @return The Description.
	 **/
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description.
	 *
	 * @param description The Description.
	 **/
	public Status setDescription(String description) {
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

		Status that = (Status) o;

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
		return "StatusCode{" +
				"id=" + id +
				", description='" + description + '\'' +
				'}';
	}
}
