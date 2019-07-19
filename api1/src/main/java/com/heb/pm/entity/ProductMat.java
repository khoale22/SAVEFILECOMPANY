package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Product level MAT attributes.
 *
 * @author s573181
 * @since 2.29.0
 */
@Entity
@Table(name="PROD_MAT")
public class ProductMat implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductMatKey key;

	@Column(name = "TRX_ID")
	private String transactionId;

	@Column(name = "A102168_PROD_LIN_CD")
	private String productLineCode;

	/**
	 * Returns the ProductMatKey.
	 *
	 * @return the ProductMatKey.
	 */
	public ProductMatKey getKey() {
		return key;
	}

	/**
	 * Sets the ProductMatKey.
	 *
	 * @param key the ProductMatKey.
	 * @return the updated product mat.
	 */
	public ProductMat setKey(ProductMatKey key) {
		this.key = key;
		return this;
	}

	/**
	 * Returns the transaction id.
	 *
	 * @return the transaction id.
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Set the transaction id.
	 *
	 * @param transactionId the transaction id.
	 * @return the updated ProductMat.
	 */
	public ProductMat setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}

	/**
	 * Returns the product line code.
	 *
	 * @return the product line code.
	 */
	public String getProductLineCode() {
		return productLineCode;
	}

	/**
	 * Sets the product line code.
	 * @param productLineCode the product line code.
	 * @return the updated ProductMat.
	 */
	public ProductMat setProductLineCode(String productLineCode) {
		this.productLineCode = productLineCode;
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
		ProductMat that = (ProductMat) o;
		return key.equals(that.key);
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductMat{" +
				"key=" + key +
				", transactionId=" + transactionId +
				", productLineCode='" + productLineCode + '\'' +
				'}';
	}
}
