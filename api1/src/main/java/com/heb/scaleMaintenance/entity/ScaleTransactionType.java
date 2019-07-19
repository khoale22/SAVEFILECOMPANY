package com.heb.scaleMaintenance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity for scale transaction type.
 *
 * @author m314029
 * @since 2.17.8
 */
@Entity
@Table(name = "SCL_TRX_TYP")
public class ScaleTransactionType implements Serializable{

	private static final long serialVersionUID = -7355680048246897036L;

	@Id
	@Column(name = "SCL_TRANS_TYP_ID", updatable = false)
	private Long id;

	@Column(name = "SCL_TRANS_TYP_DES")
	private String description;


	public enum Code {
		LOAD(0, "LOAD"),
		MAINTENANCE(1, "MAINTENANCE"),
		//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		LOADBYDEPT(2, "LOADBYDEPT");
		//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

		Integer id;
		String description;

		/**
		 * Constructor for a scale transaction type.
		 *
		 * @param id ID of the scale transaction type.
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
	}

	/**
	 * Returns Id.
	 *
	 * @return The Id.
	 **/
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Id.
	 *
	 * @param id The Id.
	 **/
	public ScaleTransactionType setId(Long id) {
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
	public ScaleTransactionType setDescription(String description) {
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

		ScaleTransactionType that = (ScaleTransactionType) o;

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
		return "ScaleTransactionType{" +
				"id=" + id +
				", description='" + description + '\'' +
				'}';
	}
}
