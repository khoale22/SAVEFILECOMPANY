package com.heb.scaleMaintenance.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Embedded id for scale maintenance upc.
 *
 * @author m314029
 * @since 2.17.8
 */
@Embeddable
public class ScaleMaintenanceUpcKey implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PRIME_NUMBER = 31;

	public ScaleMaintenanceUpcKey(){
		super();
	}

	public ScaleMaintenanceUpcKey(Long upc, Long transactionId) {
		super();
		this.upc = upc;
		this.transactionId = transactionId;
	}

	@Column(name = "UPC_NBR", updatable = false)
	private Long upc;

	@Column(name = "TRX_ID", updatable = false)
	private Long transactionId;

	/**
	 * Returns Upc.
	 *
	 * @return The Upc.
	 **/
	public Long getUpc() {
		return upc;
	}

	/**
	 * Sets the Upc.
	 *
	 * @param upc The Upc.
	 **/
	public ScaleMaintenanceUpcKey setUpc(Long upc) {
		this.upc = upc;
		return this;
	}

	/**
	 * Returns TransactionId.
	 *
	 * @return The TransactionId.
	 **/
	public Long getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the TransactionId.
	 *
	 * @param transactionId The TransactionId.
	 **/
	public ScaleMaintenanceUpcKey setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
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

		ScaleMaintenanceUpcKey that = (ScaleMaintenanceUpcKey) o;

		if (upc != null ? !upc.equals(that.upc) : that.upc != null) return false;
		return transactionId != null ? transactionId.equals(that.transactionId) : that.transactionId == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = upc != null ? upc.hashCode() : 0;
		result = PRIME_NUMBER * result + (transactionId != null ? transactionId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ScaleMaintenanceUpcKey{" +
				"upc=" + upc +
				", transactionId=" + transactionId +
				'}';
	}
}
