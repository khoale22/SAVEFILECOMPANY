package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The key for the CandidateDistributionFilter class.
 *
 * @author d116773
 * @since 2.12.0
 */
@Embeddable
public class CandidateDistributionFilterKey implements Serializable {

	public static final String PRODUCT_ATTRIBUTE_TYPE = "PROD";

	@Column(name="ps_work_id")
	private Long workRequestId;

	@Column(name="trg_sys_id")
	private Long targetSystemId;

	@Column(name="dstrb_key_typ_cd")
	private String keyType;

	@Column(name="attr_val_nbr")
	private String attributeId;

	/**
	 * Returns the work ID to tie this request to.
	 *
	 * @return The work ID to tie this request to.
	 */
	public Long getWorkRequestId() {
		return workRequestId;
	}

	/**
	 * Sets the work ID to tie this request to.
	 *
	 * @param workRequestId The work ID to tie this request to.
	 */
	public void setWorkRequestId(Long workRequestId) {
		this.workRequestId = workRequestId;
	}

	/**
	 * Returns the target system this filter applies to.
	 *
	 * @return The target system this filter applies to.
	 */
	public Long getTargetSystemId() {
		return targetSystemId;
	}

	/**
	 * Sets the target system this filter applies to.
	 *
	 * @param targetSystemId The target system this filter applies to.
	 */
	public void setTargetSystemId(Long targetSystemId) {
		this.targetSystemId = targetSystemId;
	}

	/**
	 * Returns the type of data in the attribute ID field. Constants are defined above.
	 *
	 * @return The type of data in the attribute ID field.
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * Sets the type of data that is in the attribute ID field.
	 *
	 * @param keyType The type of data in the attribute ID field.
	 */
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	/**
	 * The ID attribute to set.
	 *
	 * @return The ID of the attribute to set.
	 */
	public String getAttributeId() {
		return attributeId;
	}

	/**
	 * Sets the ID of the attribute to set.
	 *
	 * @param attributeId The ID of the attribute to set.
	 */
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * Compares this object to another for equality. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CandidateDistributionFilterKey)) return false;

		CandidateDistributionFilterKey that = (CandidateDistributionFilterKey) o;

		if (workRequestId != null ? !workRequestId.equals(that.workRequestId) : that.workRequestId != null)
			return false;
		if (targetSystemId != null ? !targetSystemId.equals(that.targetSystemId) : that.targetSystemId != null)
			return false;
		if (keyType != null ? !keyType.equals(that.keyType) : that.keyType != null) return false;
		return !(attributeId != null ? !attributeId.equals(that.attributeId) : that.attributeId != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = workRequestId != null ? workRequestId.hashCode() : 0;
		result = 31 * result + (targetSystemId != null ? targetSystemId.hashCode() : 0);
		result = 31 * result + (keyType != null ? keyType.hashCode() : 0);
		result = 31 * result + (attributeId != null ? attributeId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return A string representation of the object.
	 */
	@Override
	public String toString() {
		return "CandidateDistributionFilterKey{" +
				"workRequestId=" + workRequestId +
				", targetSystemId=" + targetSystemId +
				", keyType='" + keyType + '\'' +
				", attributeId='" + attributeId + '\'' +
				'}';
	}
}
