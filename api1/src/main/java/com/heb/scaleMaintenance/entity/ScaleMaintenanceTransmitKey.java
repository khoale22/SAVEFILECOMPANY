package com.heb.scaleMaintenance.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Embedded id for scale maintenance transmit.
 *
 * @author m314029
 * @since 2.17.8
 */
@Embeddable
public class ScaleMaintenanceTransmitKey implements Serializable{
	private static final long serialVersionUID = 5617261511387861552L;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "STR_NBR", updatable = false)
	private Integer store;

	@Column(name = "TRX_ID", updatable = false)
	private Long transactionId;

	/**
	 * Returns Store.
	 *
	 * @return The Store.
	 **/
	public Integer getStore() {
		return store;
	}

	/**
	 * Sets the Store.
	 *
	 * @param store The Store.
	 **/
	public ScaleMaintenanceTransmitKey setStore(Integer store) {
		this.store = store;
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
	public ScaleMaintenanceTransmitKey setTransactionId(Long transactionId) {
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

		ScaleMaintenanceTransmitKey that = (ScaleMaintenanceTransmitKey) o;

		if (store != null ? !store.equals(that.store) : that.store != null) return false;
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
		int result = store != null ? store.hashCode() : 0;
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
		return "ScaleMaintenanceTransmitKey{" +
				"store=" + store +
				", transactionId=" + transactionId +
				'}';
	}
}
