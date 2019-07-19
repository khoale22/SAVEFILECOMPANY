package com.heb.pm.entity;

/**
 * Represents a record in the dstrb_fltr table.
 *
 * @author d116773
 * @since 2.13.0
 */

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="dstrb_fltr")
public class DistributionFilter implements Serializable {

	private static final long serialVersionUID = -405060624862460787L;
	public static final String THIRD_PARTY_YES = "Y";
	public static final String THIRD_PARTY_NO = "N";

	@EmbeddedId
	private DistributionFilterKey key;

	@Column(name="lst_updt_uid")
	private String lastUpdateUserId;

	/**
	 * Returns the key for this record.
	 *
	 * @return The key for this record.
	 */
	public DistributionFilterKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this record.
	 *
	 * @param key The key for this record.
	 */
	public void setKey(DistributionFilterKey key) {
		this.key = key;
	}

	/**
	 * Returns the one-pass ID of the user who last updatd this record.
	 *
	 * @return The one-pass ID of the user who last updatd this record.
	 */
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	/**
	 * Sets the one-pass ID of the user who last updatd this record.
	 *
	 * @param lastUpdateUserId The one-pass ID of the user who last updatd this record.
	 */
	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	/**
	 * Returns whether or not this record represents a "third-party-sellable" project. Basically, is this a product
	 * record with a source system for Instacart.
	 *
	 * @return True if this record represents third-party-sellable and false otherwise.
	 */
	public boolean isThirdPartySellable() {
		boolean b1 =this.key.getKeyType().equals(DistributionFilterKey.PRODUCT_KEY_TYPE);
		boolean b2 = this.key.getTargetSystemId().equals(SourceSystem.SOURCE_SYSTEM_INSTACART);
		return  b1 && b2;
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
		if (!(o instanceof DistributionFilter)) return false;

		DistributionFilter that = (DistributionFilter) o;

		return !(key != null ? !key.equals(that.key) : that.key != null);

	}

	/**
	 * Returns a hash code for this object.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "DistributionFilter{" +
				"key=" + key +
				", lastUpdateUserId='" + lastUpdateUserId + '\'' +
				'}';
	}
}
