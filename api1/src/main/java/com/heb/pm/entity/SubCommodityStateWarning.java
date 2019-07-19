package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a warning for a particular state attached to any product under a product hierarchy -> sub commodity.
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name = "st_warn_dflt_scom")
public class SubCommodityStateWarning implements Serializable {

	private static final long serialVersionUID = 1L;

	// default constructor
	public SubCommodityStateWarning(){super();}

	// copy constructor
	public SubCommodityStateWarning(SubCommodityStateWarning subCommodityStateWarning) {
		super();
		this.setKey(new SubCommodityStateWarningKey(subCommodityStateWarning.getKey()));
		this.setAction(subCommodityStateWarning.getAction());
		if(subCommodityStateWarning.getProductStateWarning() != null) {
			this.setProductStateWarning(new ProductStateWarning(subCommodityStateWarning.getProductStateWarning()));
		}
	}

	@EmbeddedId
	private SubCommodityStateWarningKey key;

	@Transient
	private String action;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
			@JoinColumn(name = "st_cd", referencedColumnName = "st_cd", insertable = false, updatable = false),
			@JoinColumn(name = "st_prod_warn_cd", referencedColumnName = "st_prod_warn_cd", insertable = false, updatable = false)
	})
	private ProductStateWarning productStateWarning;

	/**
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public SubCommodityStateWarningKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public void setKey(SubCommodityStateWarningKey key) {
		this.key = key;
	}

	/**
	 * Returns the product state warning matching this.key.stateCode, and this.key.stateProductWarningCode.
	 *
	 * @return The ProductStateWarning that is required by law to inform of issues the ingredients in the product may cause.
	 **/
	public ProductStateWarning getProductStateWarning() {
		return productStateWarning;
	}

	/**
	 * Sets the product state warning for this sub-commodity state warning.
	 *
	 * @param productStateWarning The ProductStateWarning that is required by law to inform of issues the ingredients in the product may cause.
	 **/
	public void setProductStateWarning(ProductStateWarning productStateWarning) {
		this.productStateWarning = productStateWarning;
	}

	/**
	 * This is the action for this sub-commodity state warning to send to the webservice. It can be 'A' for add, or
	 * 'D' for delete.
	 *
	 * @return The action for the webservice to take for this sub-commodity state warning.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Set the action for this sub-commodity state warning.
	 *
	 * @param action The action to set.
	 */
	public void setAction(String action) {
		this.action = action;
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

		SubCommodityStateWarning that = (SubCommodityStateWarning) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "SubCommodityStateWarning{" +
				"key=" + key +
				", action='" + action + '\'' +
				'}';
	}
}
