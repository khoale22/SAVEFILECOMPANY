package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a product scan code extent data key that is extended attributes for UPCs.
 *
 * @author m594201
 * @since 2.7.0
 */
@Embeddable
public class ProductScanCodeExtentKey implements Serializable {

	private static final int PRIME_NUMBER = 31;
	private static final int FOUR_BYTES = 32;

	@Column(name = "scn_cd_id")
	private Long scanCodeId;

	@Column(name = "prod_ext_dta_cd")
	private String prodExtDataCode;

	/**
	 * Gets scan code id that is attached to the measurements given by the gladson measuring data.
	 *
	 * @return the scan code id that is attached to the measurements given by the gladson measuring data.
	 */
	public Long getScanCodeId() {
		return scanCodeId;
	}

	/**
	 * Sets scan code id that is attached to the measurements given by the gladson measuring data.
	 *
	 * @param scanCodeId the scan code id that is attached to the measurements given by the gladson measuring data.
	 */
	public void setScanCodeId(Long scanCodeId) {
		this.scanCodeId = scanCodeId;
	}

	/**
	 * Gets prod ext data code.
	 *
	 * @return the prod ext data code that is tied to a description that then provides the measurement for that code.
	 */
	public String getProdExtDataCode() {
		return prodExtDataCode;
	}

	/**
	 * Sets prod ext data code.
	 *
	 * @param prodExtDataCode the prod ext data code that is tied to a description that then provides the measurement for that code.
	 */
	public void setProdExtDataCode(String prodExtDataCode) {
		this.prodExtDataCode = prodExtDataCode;
	}

	/**
	 * Compares another object to this one. If that object is an ProductScanCodeExtentKey, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ProductScanCodeExtentKey)) return false;

		ProductScanCodeExtentKey that = (ProductScanCodeExtentKey) o;

		return Objects.equals(scanCodeId, that.scanCodeId);

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return (int) (scanCodeId ^ (scanCodeId >>> FOUR_BYTES));
	}
}
