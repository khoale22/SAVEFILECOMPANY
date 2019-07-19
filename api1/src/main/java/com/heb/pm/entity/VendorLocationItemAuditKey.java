package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Composite key for VendorLocationItemAudit.
 *
 * @author m314029
 * @since 2.7.0
 */
@Embeddable
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class VendorLocationItemAuditKey implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name = "itm_key_typ_cd")
	@Type(type = "fixedLengthCharPK")
	private String itemType;

	@Column(name = "itm_id")
	private Long itemCode;

	@Column(name = "vend_loc_typ_cd")
	@Type(type = "fixedLengthCharPK")
	private String vendorType;

	@Column(name = "vend_loc_nbr")
	private Integer vendorNumber;

	@Column(name = "aud_rec_cre8_ts")
	private LocalDateTime changedOn;

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Long getItemCode() {
		return itemCode;
	}

	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	public String getVendorType() {
		return vendorType;
	}

	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	public Integer getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(Integer vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	/**
	 * Returns the changed on. The changed on is the timestamp in which the transaction occurred.
	 *
	 * @return Timestamp
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Sets the Timestamp
	 *
	 * @param timestamp The Timestamp
	 */
	public void setChangedOn(LocalDateTime timestamp) {
		this.changedOn = changedOn;
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

		VendorLocationItemAuditKey that = (VendorLocationItemAuditKey) o;

		if (itemType != null ? !itemType.equals(that.itemType) : that.itemType != null) return false;
		if (itemCode != null ? !itemCode.equals(that.itemCode) : that.itemCode != null) return false;
		if (vendorType != null ? !vendorType.equals(that.vendorType) : that.vendorType != null) return false;
		if (vendorNumber != null ? !vendorNumber.equals(that.vendorNumber) : that.vendorNumber != null) return false;
		return changedOn != null ? changedOn.equals(that.changedOn) : that.changedOn == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType != null ? itemType.hashCode() : 0;
		result = VendorLocationItemAuditKey.PRIME_NUMBER * result + (itemCode != null ? itemCode.hashCode() : 0);
		result = VendorLocationItemAuditKey.PRIME_NUMBER * result + (vendorType != null ? vendorType.hashCode() : 0);
		result = VendorLocationItemAuditKey.PRIME_NUMBER * result + (vendorNumber != null ? vendorNumber.hashCode() : 0);
		result = VendorLocationItemAuditKey.PRIME_NUMBER * result + (changedOn != null ? changedOn.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "VendorLocationItemAuditKey{" +
				"itemType='" + itemType + '\'' +
				", itemCode=" + itemCode +
				", vendorType='" + vendorType + '\'' +
				", vendorNumber=" + vendorNumber +
				", changedOn=" + changedOn +
				'}';
	}
}
