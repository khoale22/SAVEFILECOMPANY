package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the key for the ProductMat entity.
 * @author s573181
 * @since 2.29.0
 */
@Embeddable
public class ProductMatKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "PROD_ID")
	private Long prodId;

	@Column(name = "SRC_SYSTEM_ID")
	private Long sourceSystemId;

	/**
	 * Returns the product id.
	 *
	 * @return the product id.
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * Sets the product id.
	 * @param prodId the product id.
	 *
	 * @return the updated Product Mat Key.
	 */
	public ProductMatKey setProdId(Long prodId) {
		this.prodId = prodId;
		return this;
	}

	/**
	 * Returns the source system id.
	 *
	 * @return the source system id.
	 */
	public Long getSourceSystemId() {
		return sourceSystemId;
	}

	/**
	 * Sets the source system id.
	 *
	 * @param sourceSystemId the source system id.
	 * @return the updated product mat key.
	 */
	public ProductMatKey setSourceSystemId(Long sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
		return this;
	}

	/**
	 * Compares another object to this one. If that object is an ItemMaster, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProductMatKey that = (ProductMatKey) o;
		return prodId.equals(that.prodId) &&
				sourceSystemId.equals(that.sourceSystemId);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(prodId, sourceSystemId);
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductMatKey{" +
				"prodId=" + prodId +
				", sourceSystemId=" + sourceSystemId +
				'}';
	}
}
