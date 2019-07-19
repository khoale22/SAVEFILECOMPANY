package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents product cubiscan measurements from the cubiscan measuring device.
 *
 * @author m594201
 * @since 2.7.0
 */
@Entity
@Table(name = "prod_cubiscan")
public class ProductCubiscan implements Serializable{

	private static final int PRIME_NUMBER = 31;
	private static final int FOUR_BYTES = 32;

	@EmbeddedId
	private ProductCubiscanKey key;

	@Column(name = "prod_inch_ln")
	private Double retailLength;

	@Column(name = "prod_inch_wd")
	private Double retailWidth;

	@Column(name = "prod_inch_ht")
	private Double retailHeight;

	@Column(name = "prod_lb_wt")
	private Double retailWeight;

	@Column(name = "lst_updt_ts")
	private Date lastUpdatedTimestamp;

	@Column(name = "lst_updt_uid")
	private String lastUpdatedBy;

	@Column(name = "seq_nbr", updatable = false, insertable = false)
	private Long seqNumber;

	/**
	 * Gets key.
	 *
	 * @return the Composite key for cubiscan entity.
	 */
	public ProductCubiscanKey getKey() {
		return key;
	}

	/**
	 * Sets key.
	 *
	 * @param key the Composite key for cubiscan entity.
	 */
	public void setKey(ProductCubiscanKey key) {
		this.key = key;
	}

	/**
	 * Gets retail length that is given by the cubiscan measuring device.
	 *
	 * @return the retail length that is given by the cubiscan measuring device.
	 */
	public Double getRetailLength() {
		return retailLength;
	}

	/**
	 * Sets retail length that is given by the cubiscan measuring device.
	 *
	 * @param retailLength the retail length that is given by the cubiscan measuring device.
	 */
	public void setRetailLength(Double retailLength) {
		this.retailLength = retailLength;
	}

	/**
	 * Gets retail width that is given by the cubiscan measuring device.
	 *
	 * @return the retail width that is given by the cubiscan measuring device.
	 */
	public Double getRetailWidth() {
		return retailWidth;
	}

	/**
	 * Sets retail width that is given by the cubiscan measuring device.
	 *
	 * @param retailWidth the retail width that is given by the cubiscan measuring device.
	 */
	public void setRetailWidth(Double retailWidth) {
		this.retailWidth = retailWidth;
	}

	/**
	 * Gets retail height that is given by the cubiscan measuring device.
	 *
	 * @return the retail height that is given by the cubiscan measuring device.
	 */
	public Double getRetailHeight() {
		return retailHeight;
	}

	/**
	 * Sets retail height that is given by the cubiscan measuring device.
	 *
	 * @param retailHeight the retail height that is given by the cubiscan measuring device.
	 */
	public void setRetailHeight(Double retailHeight) {
		this.retailHeight = retailHeight;
	}

	/**
	 * Gets retail weight that is given by the cubiscan measuring device.
	 *
	 * @return the retail weight that is given by the cubiscan measuring device.
	 */
	public Double getRetailWeight() {
		return retailWeight;
	}

	/**
	 * Sets retail weight that is given by the cubiscan measuring device.
	 *
	 * @param retailWeight the retail weight that is given by the cubiscan measuring device.
	 */
	public void setRetailWeight(Double retailWeight) {
		this.retailWeight = retailWeight;
	}

	/**
	 * Gets last updated timestamp that the measurements provided by the cubiscan measuring device was updated.
	 *
	 * @return the last updated timestamp that the measurements provided by the cubiscan measuring device was updated.
	 */
	public Date getLastUpdatedTimestamp() {
		return lastUpdatedTimestamp;
	}

	/**
	 * Sets last updated timestamp that the measurements provided by the cubiscan measuring device was updated.
	 *
	 * @param lastUpdatedTimestamp the last updated timestamp that the measurements provided by the cubiscan measuring device was updated.
	 */
	public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
		this.lastUpdatedTimestamp = lastUpdatedTimestamp;
	}

	/**
	 * Gets the user ID of the last user to update ProductCubiscan measuring data.
	 *
	 * @return The userID of the current user making the edit to the ProductCubiscan measuring data.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * Sets the user ID of the last user to update ProductCubiscan measuring data.
	 *
	 * @param lastUpdatedBy the last updated by user ID of the current user making the edit to the ProductCubiscan measuring data.
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * Compares another object to this one. If that object is an ProductCubiscan, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductCubiscan)) return false;
		ProductCubiscan that = (ProductCubiscan) o;

		return key == that.key;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return this.key != null ? this.key.hashCode() : 0;
	}

}
