package com.heb.pm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Hold information for a product setup work request
 * @author s753601
 * @version 2.13.0
 */
@Entity
@Table(name = "ps_work_rqst")
public class ProductSetupWorkRequest implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ps_work_id")
	Integer id;

	@Column(name = "prod_id")
	Integer productId;

	/**
	 * Return the unique identifier for the ProductSetupWorkRequest.
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Updates id
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Returns the product ID for the product setup work request.
	 * @return product Id
	 */
	public Integer getProductId() {
		return productId;
	}

	/**
	 * Updates productId
	 * @param productId the new productId
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
	 * Compares another object to this one. This is a deep compare.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductSetupWorkRequest that = (ProductSetupWorkRequest) o;

		return id != null ? !id.equals(that.id) : that.id != null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "ShipperAuditKey{" +
				"id=" + id +
				", productId=" + productId +
				'}';
	}
}
