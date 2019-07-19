package com.heb.pm.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents the preferred unit of measure for a product attached to a product hierarchy -> sub commodity.
 *
 * @author m314029
 * @since 2.7.0
 */
@Entity
@Table(name = "prod_pref_uom")
public class ProductPreferredUnitOfMeasure implements Serializable {

	// default constructor
	public ProductPreferredUnitOfMeasure(){super();}

	// copy constructor
	public ProductPreferredUnitOfMeasure(ProductPreferredUnitOfMeasure preferredUnitOfMeasure) {
		super();
		this.setKey(new ProductPreferredUnitOfMeasureKey(preferredUnitOfMeasure.getKey()));
		this.setSequenceNumber(preferredUnitOfMeasure.getSequenceNumber());
		this.setRetailUnitOfMeasure(new RetailUnitOfMeasure(preferredUnitOfMeasure.getRetailUnitOfMeasure()));
	}

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductPreferredUnitOfMeasureKey key;

	@Column(name = "pref_uom_seq_nbr")
	private Integer sequenceNumber;

	@Transient
	private String action;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "retl_sell_sz_cd", referencedColumnName = "retl_sell_sz_cd", insertable = false, updatable = false)
	private RetailUnitOfMeasure retailUnitOfMeasure;

	/**
	 * Returns the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
	 *
	 * @return the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
	 **/
	public ProductPreferredUnitOfMeasureKey getKey() {
		return key;
	}

	/**
	 * Sets the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
	 *
	 * @param key the composite key which includes subCommodityCode and retailUnitOfMeasureCode.
	 **/
	public void setKey(ProductPreferredUnitOfMeasureKey key) {
		this.key = key;
	}

	/**
	 * Returns sequence number of this preferred unit of measure. There can be up to two.
	 *
	 * @return The SequenceNumber which is used to determine which row to display.  Higher seq number is displayed.
	 **/
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * Sets the SequenceNumber which is used to determine which row to display.  Higher seq number is displayed.
	 *
	 * @param sequenceNumber The SequenceNumber which is used to determine which row to display.  Higher seq number is displayed.
	 **/
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * Returns retail unit of measure matching retailUnitOfMeasureCode for code table specifics.
	 *
	 * @return The RetailUOM object that is tied to a productPreferredUnitOfMeasure.
	 **/
	public RetailUnitOfMeasure getRetailUnitOfMeasure() {
		return retailUnitOfMeasure;
	}

	/**
	 * Sets the RetailUOM object that is tied to a productPreferredUnitOfMeasure.
	 *
	 * @param retailUnitOfMeasure The RetailUOM object that is tied to a productPreferredUnitOfMeasure.
	 **/
	public void setRetailUnitOfMeasure(RetailUnitOfMeasure retailUnitOfMeasure) {
		this.retailUnitOfMeasure = retailUnitOfMeasure;
	}

	/**
	 * This is the action for this productPreferredUnitOfMeasure to send to the webservice. It can be 'A' for add, 'U'
	 * for update, or 'D' for delete.
	 *
	 * @return The action for the webservice to take for this productPreferredUnitOfMeasure.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Set the action for this productPreferredUnitOfMeasure.
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

		ProductPreferredUnitOfMeasure that = (ProductPreferredUnitOfMeasure) o;

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
		return "ProductPreferredUOM{" +
				"key=" + key +
				", sequenceNumber=" + sequenceNumber +
				'}';
	}
}
