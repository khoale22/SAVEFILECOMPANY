package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Holds the unique identifier for the ProductScanImageUIR object
 * @author s753601
 * @version 2.13.0
 */
@Embeddable
public class ProductScanImageURIKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int FOUR_BYTES = 32;


	@Column(name = "scn_cd_id")
	private long id;

	@Column(name = "seq_nbr")
	private long sequenceNumber;

	/**
	 * Returns the id component to uniquely identify a ProductScanImageUIR object
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Updates id
	 * @param id  the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the sequenceNumber component to uniquely identify a ProductScanImageUIR object
	 * @return sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Updates sequenceNumber
	 * @param sequenceNumber the new sequenceNumber
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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

		ProductScanImageURIKey that = (ProductScanImageURIKey) o;

		if (id != that.id) return false;
		return sequenceNumber == that.sequenceNumber;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> FOUR_BYTES));
		result = FOUR_BYTES * result + (int)(sequenceNumber ^ (sequenceNumber >>> FOUR_BYTES));
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ProductScanImageURIKey{" +
				"id=" + id +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}
