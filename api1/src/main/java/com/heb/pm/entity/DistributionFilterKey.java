package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represents the key of the dstrb_fltr table.
 *
 * @author d116773
 * @since 2.13.0
 */
@Embeddable
public class DistributionFilterKey implements Serializable {

	private static final long serialVersionUID = -405060624862460787L;

	public static final String PRODUCT_KEY_TYPE = "PROD ";

	@Column(name="trg_system_id")
	private Integer targetSystemId;

	@Column(name="dstrb_key_typ_cd")
	private String keyType;

	@Column(name="attr_val_nbr")
	private Long keyId;

	/**
	 * Returns the ID of the system this filter targets. That would be a system that is allowed to consume this product.
	 *
	 * @return The ID of the system this filter targets.
	 */
	public Integer getTargetSystemId() {
		return targetSystemId;
	}

	/**
	 * Sets the ID of the system this filter targets.
	 *
	 * @param targetSystemId The ID of the system this filter targets.
	 */
	public void setTargetSystemId(Integer targetSystemId) {
		this.targetSystemId = targetSystemId;
	}

	/**
	 * Returns the type that keyId represents. There are constants defined for the value.
	 *
	 * @return The type that keyId represents.
	 */
	public String getKeyType() {
		return keyType;
	}

	/**
	 * Sets the type that keyId represents.
	 *
	 * @param keyType The type that keyId represents.
	 */
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	/**
	 * An ID that represents an object in our system. Typically a product ID.
	 *
	 * @return The ID of an object in our system.
	 */
	public Long getKeyId() {
		return keyId;
	}

	/**
	 * Sets the ID of this object in our system.
	 *
	 * @param keyId The ID of this object in our system.
	 */
	public void setKeyId(Long keyId) {
		this.keyId = keyId;
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
		if (!(o instanceof DistributionFilterKey)) return false;

		DistributionFilterKey that = (DistributionFilterKey) o;

		if (targetSystemId != null ? !targetSystemId.equals(that.targetSystemId) : that.targetSystemId != null)
			return false;
		if (keyType != null ? !keyType.equals(that.keyType) : that.keyType != null) return false;
		return !(keyId != null ? !keyId.equals(that.keyId) : that.keyId != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = targetSystemId != null ? targetSystemId.hashCode() : 0;
		result = 31 * result + (keyType != null ? keyType.hashCode() : 0);
		result = 31 * result + (keyId != null ? keyId.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "DistributionFilterKey{" +
				"targetSystemId=" + targetSystemId +
				", keyType='" + keyType + '\'' +
				", keyId=" + keyId +
				'}';
	}
}
