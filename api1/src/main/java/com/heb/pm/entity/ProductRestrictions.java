package com.heb.pm.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This entity ties products and selling restrictions together
 * @author s753601
 * @version 2.12.0
 */
@Entity
@Table(name="prod_sals_rstr")
@TypeDefs({
		@TypeDef(name = "fixedLengthChar", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharType.class),
		@TypeDef(name = "fixedLengthCharPK", typeClass = com.heb.pm.util.oracle.OracleFixedLengthCharTypePK.class)
})
public class ProductRestrictions implements Serializable{

	@EmbeddedId
	private ProductRestrictionsKey key;

	@Transient
	private String actionCode;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SALS_RSTR_CD", referencedColumnName = "SALS_RSTR_CD", insertable = false, updatable = false, nullable = false)
	private SellingRestrictionCode restriction;

	/**
	 * Gets action code.
	 *
	 * @return the action code
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Sets action code.
	 *
	 * @param actionCode the action code
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Returns the unique identifier for a product's restriction
	 * @return key
	 */
	public ProductRestrictionsKey getKey() {
		return key;
	}

	/**
	 * Updates the key
	 * @param key the new key
	 */
	public void setKey(ProductRestrictionsKey key) {
		this.key = key;
	}

	/**
	 * Returns a detailed restriction
	 * @return restriction
	 */
	public SellingRestrictionCode getRestriction() {
		return restriction;
	}

	/**
	 * Updates the restriction
	 * @param restriction the new restriction
	 */
	public void setRestriction(SellingRestrictionCode restriction) {
		this.restriction = restriction;
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
		if (!(o instanceof ProductRestrictions)) {
			return false;
		}

		ProductRestrictions that = (ProductRestrictions) o;
		if (this.key != null ? !this.key.equals(that.key) : that.key != null) return false;

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
		return this.key == null ? 0 : this.key.hashCode();
	}

	public String toString(){
		return "ProductRestrictions={" +
				"key=" + key +
				", restriction=" + restriction+
				"}";
	}
}
