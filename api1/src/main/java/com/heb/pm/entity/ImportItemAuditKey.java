package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *@author s753601
 *@version 2.7.0
 */
@Embeddable
// dB2Oracle changes vn00907 starts
@TypeDefs({
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
// dB2Oracle changes vn00907
public class ImportItemAuditKey implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final int FOUR_BYTES = 32;
	private static final int PRIME_NUMBER = 31;

	@Column(name = "AUD_REC_CRE8_TS")
	private LocalDateTime changedOn;

	@Column(name = "ITM_ID")
	private Long itemCode;

	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	@Column(name = "ITM_KEY_TYP_CD")
	private String itemType;

	@Column(name = "VEND_LOC_NBR")
	private Integer vendorNumber;

	//db2o changes  vn00907
	@Type(type="fixedLengthCharPK")
	@Column(name = "VEND_LOC_TYP_CD")
	private String vendorType;

	/**
	 * Returns the timestamp for when the record was made
	 * @return changedOn
	 */
	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	/**
	 * Updates the changedOn timestamp
	 * @param changedOn the new changedOn
	 */
	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	/**
	 * Returns the item code component to uniquely identify the item
	 * @return itemCode
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Updates the item code
	 * @param itemCode the new item code
	 */
	public void setItemCode(Long itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * Returns the item type code to uniquely identify the import item.
	 * @return itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Updates the itemType
	 * @param itemType the new itemType
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * Returns the Number assigned by accounting to a vendor that supplies items to a warehouse
	 * @return vendorNumber
	 */
	public Integer getVendorNumber() {
		return vendorNumber;
	}

	/**
	 * Updates the vendorNumber
	 * @param vendorNumber the new vendorNumber
	 */
	public void setVendorNumber(Integer vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	/**
	 * Returns the string identifier for the classification of the vendor
	 * @return vendorType
	 */
	public String getVendorType() {
		return vendorType;
	}

	/**
	 *Updates the VendorType
	 * @param vendorType the new vendorType
	 */
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImportItemAuditKey that = (ImportItemAuditKey) o;

		return itemCode == that.itemCode && vendorNumber == that.vendorNumber && (itemType != null ?
				itemType.equals(that.itemType) : that.itemType == null) && (vendorType != null ?
				vendorType.equals(that.vendorType) : that.vendorType == null) && (changedOn != null ?
				changedOn.equals(that.changedOn) : that.changedOn == null);
	}

	/**
	 * Returns a hash code for this object. Equal objects always return the same value. Unequals objects (probably)
	 * return different values.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = itemType != null ? itemType.hashCode() : 0;
		result = PRIME_NUMBER * result + (int) (itemCode ^ (itemCode >>> FOUR_BYTES));
		result = PRIME_NUMBER * result + (vendorType != null ? vendorType.hashCode() : 0);
		result = PRIME_NUMBER * result + vendorNumber;
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ImportItemAuditKey{" +
				"itemType='" + itemType + '\'' +
				", itemCode=" + itemCode + '\'' +
				", changeOn=" + changedOn + '\'' +
				", vzendorType=" + vendorType + + '\'' +
				", vendorNumber=" + vendorNumber +
				'}';
	}
}
