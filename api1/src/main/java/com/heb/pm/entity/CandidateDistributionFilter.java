package com.heb.pm.entity;

import javax.persistence.*;

/**
 * Stores products and stores that are available to third parties such as
 * Unata.
 *
 * @author d116773
 * @since 2.12.0
 */
@Entity
@Table(name = "ps_dstrb_fltr")
public class CandidateDistributionFilter {

	public static final Character ADD = 'Y';
	public static final Character DELETE = 'D';

	@EmbeddedId
	private CandidateDistributionFilterKey key;

	@Column(name = "new_dta_sw")
	private Character action;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ps_work_id", insertable = false, updatable = false)
	private CandidateWorkRequest workRequest;

	/**
	 * Returns the key for this object.
	 *
	 * @return The key for this object.
	 */
	public CandidateDistributionFilterKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this object.
	 *
	 * @param key The key for this object.
	 */
	public void setKey(CandidateDistributionFilterKey key) {
		this.key = key;
	}

	/**
	 * Returns the action to take on this record. Constants are defined for the
	 * acceptable values.
	 *
	 * @return The action to take on this record.
	 */
	public Character getAction() {
		return action;
	}

	/**
	 * Sets the action to take on this record.
	 *
	 * @param action The action to take on this record.
	 */
	public void setAction(Character action) {
		this.action = action;
	}

	/**
	 * Returns the work request this record is associated to.
	 *
	 * @return The work request this record is associated to.
	 */
	public CandidateWorkRequest getWorkRequest() {
		return workRequest;
	}

	/**
	 * Sets the work request this record is associated to.
	 *
	 * @param workRequest The work request this record is associated to.
	 */
	public void setWorkRequest(CandidateWorkRequest workRequest) {
		this.workRequest = workRequest;
	}

	/**
	 * Compares this object with another for equality.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateDistributionFilter)) return false;

		CandidateDistributionFilter that = (CandidateDistributionFilter) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);

	}

	/**
	 * Returns a hash code for the object.
	 *
	 * @return A hash code for the object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "CandidateDistributionFilter{" +
				"key=" + key +
				", action=" + action +
				'}';
	}

	/**
	 * Called by hibernate before this object is saved. It sets the work request ID as that is not created until
	 * it is inserted into the work request table.
	 */
	@PrePersist
	public void setWorkRequestId() {
		if (this.getKey().getWorkRequestId() == null) {
			this.getKey().setWorkRequestId(this.workRequest.getWorkRequestId());
		}
	}
}
