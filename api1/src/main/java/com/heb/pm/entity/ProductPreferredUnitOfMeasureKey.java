package com.heb.pm.entity;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * An embeddable key for a product preferred UOM.
 *
 * @author m314029
 * @since 2.7.0
 */
@Embeddable
@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
public class ProductPreferredUnitOfMeasureKey implements Serializable {

	// default constructor
	public ProductPreferredUnitOfMeasureKey(){super();}

	// copy constructor
	public ProductPreferredUnitOfMeasureKey(ProductPreferredUnitOfMeasureKey key) {
		super();
		key.setRetailUnitOfMeasureCode(key.getRetailUnitOfMeasureCode());
		key.setSubCommodityCode(key.getSubCommodityCode());
	}
	private static final long serialVersionUID = 1L;

	private static final int PRIME_NUMBER = 31;

	@Column(name="pd_omi_sub_com_cd")
	private Integer subCommodityCode;

	@Column(name="retl_sell_sz_cd")
	@Type(type="fixedLengthCharPK")
	private String retailUnitOfMeasureCode;

	/**
	 * Returns SubCommodityCode that the product is labeled under.  Which is the lowest organization of the specific product.
	 *
	 * @return The SubCommodityCode that the product is labeled under. Which is the lowest organization of the specific product.
	 **/
	public Integer getSubCommodityCode() {
		return subCommodityCode;
	}

	/**
	 * Sets the SubCommodityCode that the product is labeled under. Which is the lowest organization of the specific product.
	 *
	 * @param subCommodityCode The SubCommodityCode that the product is labeled under. Which is the lowest organization of the specific product.
	 **/
	public void setSubCommodityCode(Integer subCommodityCode) {
		this.subCommodityCode = subCommodityCode;
	}

	/**
	 * Returns code of the retail unit of measure this preferred unit of measure refers to.
	 *
	 * @return The RetailUOMCode.
	 **/
	public String getRetailUnitOfMeasureCode() {
		return retailUnitOfMeasureCode;
	}

	/**
	 * Sets the Retail Unit of Measure Code that is sent to the scales.
	 *
	 * @param retailUnitOfMeasureCode The Retail Unit of Measure Code that is sent to the scales.
	 **/
	public void setRetailUnitOfMeasureCode(String retailUnitOfMeasureCode) {
		this.retailUnitOfMeasureCode = retailUnitOfMeasureCode;
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

		ProductPreferredUnitOfMeasureKey that = (ProductPreferredUnitOfMeasureKey) o;

		if (subCommodityCode != null ? !subCommodityCode.equals(that.subCommodityCode) : that.subCommodityCode != null)
			return false;
		return retailUnitOfMeasureCode != null ? retailUnitOfMeasureCode.equals(that.retailUnitOfMeasureCode) : that.retailUnitOfMeasureCode == null;
	}

	/**
	 * Returns a hash for this object. If two objects are equal, they will have the same hash. If they are not,
	 * they will (probably) have different hashes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = subCommodityCode != null ? subCommodityCode.hashCode() : 0;
		result = PRIME_NUMBER * result + (retailUnitOfMeasureCode != null ? retailUnitOfMeasureCode.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductPreferredUOMKey{" +
				"subCommodityCode=" + subCommodityCode +
				", retailUnitOfMeasureCode='" + retailUnitOfMeasureCode + '\'' +
				'}';
	}
}
