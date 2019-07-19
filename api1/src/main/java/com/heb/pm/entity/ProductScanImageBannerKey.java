package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Entity representing the product scan image banner key
 * @author s753601
 * @version 2.13.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductScanImageBannerKey implements Serializable{

	private static final int FOUR_BYTES = 32;

	@Column(name = "SCN_CD_ID")
	private long id;

	@Column(name = "seq_nbr")
	private long sequenceNumber;

	@Column(name = "sals_chnl_cd")
	@Type(type = "fixedLengthCharPK")
	private String salesChannelCode;

	/**
	 * Returns the numeric identifier for the productScanImageBanner
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Updates the id
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the sequence number for the productScanImageBanner
	 * @return sequenceNumber
	 */
	public long getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Updates the sequenceNumber
	 * @param sequenceNumber the new sequenceNumber
	 */
	public void setSequenceNumber(long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns the unique identifier for the sales channel attached to the productScanImageBanner
	 * @return the salesChannelCode
	 */
	public String getSalesChannelCode() {
		return salesChannelCode;
	}

	/**
	 * Updates the salesChannelCode
	 * @param salesChannelCode the new salesChannelCode
	 */
	public void setSalesChannelCode(String salesChannelCode) {
		this.salesChannelCode = salesChannelCode;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductScanImageBannerKey{" +
				"id='" + id + '\'' +
				", sequenceNumber='" + sequenceNumber + '\'' +
				", salesChannelCode='" + salesChannelCode + '\'' +
				'}';
	}

	/**
	 * Compares another object to this one. If that object is a WarehouseLocationItem, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ProductScanImageBannerKey)) {
			return false;
		}

		ProductScanImageBannerKey that = (ProductScanImageBannerKey) o;
		if (this.id != that.id) return false;
		if (this.sequenceNumber != that.sequenceNumber) return false;
		if (!this.salesChannelCode.equals(that.salesChannelCode)) return false;
		return true;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		int result = (int) (this.id ^ (this.id >>> FOUR_BYTES));
		result = result * (int) (this.sequenceNumber ^ (this.sequenceNumber >>> FOUR_BYTES));
		result = result * this.salesChannelCode.hashCode();
		return result;
	}
}
