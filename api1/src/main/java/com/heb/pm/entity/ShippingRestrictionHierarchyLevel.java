package com.heb.pm.entity;

import com.heb.pm.productHierarchy.ProductHierarchyController;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a record in the sals_rstr_prod_dfl table.
 *
 * @author m314029
 * @since 2.6.0
 */
@Entity
@Table(name="sals_rstr_prod_dfl")
public class ShippingRestrictionHierarchyLevel implements Serializable, Comparable<ShippingRestrictionHierarchyLevel> {

	private static final long serialVersionUID = 1L;

	public static final String ADD_ACTION_CODE = "A";
	public static final String DELETE_ACTION_CODE = "D";
	public static final String UPDATE_ACTION_CODE = "U";

	// default constructor
	public ShippingRestrictionHierarchyLevel(){super();}

	// copy constructor
	public ShippingRestrictionHierarchyLevel(ShippingRestrictionHierarchyLevel shippingRestrictionHierarchyLevel){
		super();
		this.setActionCode(shippingRestrictionHierarchyLevel.getActionCode());
		this.setApplicableAtThisLevel(shippingRestrictionHierarchyLevel.getApplicableAtThisLevel());
		this.setInherited(shippingRestrictionHierarchyLevel.getInherited());
		this.setOverrideLowerLevels(shippingRestrictionHierarchyLevel.getOverrideLowerLevels());
		this.setKey(new ShippingRestrictionHierarchyLevelKey(shippingRestrictionHierarchyLevel.getKey()));
		this.setSellingRestrictionCode(new SellingRestrictionCode(shippingRestrictionHierarchyLevel.getSellingRestrictionCode()));
	}

	@EmbeddedId
	private ShippingRestrictionHierarchyLevelKey key;

	@Column(name = "excsn_sw")
	private Boolean applicableAtThisLevel;

	@Transient
	private Boolean inherited = false;

	@Transient
	private String actionCode;

	@Transient
	private Boolean overrideLowerLevels;

	@Transient
	private ProductHierarchyController.ProductHierarchyLevel productHierarchyLevel;

	@ManyToOne
	@JoinColumn(name = "sals_rstr_cd", referencedColumnName = "sals_rstr_cd", insertable = false, updatable = false, nullable = false)
	private SellingRestrictionCode sellingRestrictionCode;

	/**
	 * Gets the product hierarchy level for this shipping restriction.
	 *
	 * @return The productHierarchyLevel.
	 */
	public ProductHierarchyController.ProductHierarchyLevel getProductHierarchyLevel() {
		return productHierarchyLevel;
	}

	/**
	 * Sets the product hierarchy level for this shipping restriction. This will be managed on the back end, and
	 * retrieved on the front end, since the shipping restrictions for all levels at or above a particular hierarchy
	 * level will be returned in api call.
	 *
	 * @param productHierarchyLevel The productHierarchyLevel set for this shipping restriction.
	 */
	public void setProductHierarchyLevel(ProductHierarchyController.ProductHierarchyLevel productHierarchyLevel) {
		this.productHierarchyLevel = productHierarchyLevel;
	}

	/**
	 * Returns ApplicableAtThisLevel.
	 *
	 * @return The ApplicableAtThisLevel.
	 **/
	public Boolean getApplicableAtThisLevel() {
		return applicableAtThisLevel;
	}

	/**
	 * Sets the ApplicableAtThisLevel.
	 *
	 * @param applicableAtThisLevel The ApplicableAtThisLevel.
	 **/
	public void setApplicableAtThisLevel(Boolean applicableAtThisLevel) {
		this.applicableAtThisLevel = applicableAtThisLevel;
	}

	/**
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public ShippingRestrictionHierarchyLevelKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public void setKey(ShippingRestrictionHierarchyLevelKey key) {
		this.key = key;
	}

	/**
	 * Returns SellingRestrictionCode.
	 *
	 * @return The SellingRestrictionCode.
	 **/
	public SellingRestrictionCode getSellingRestrictionCode() {
		return sellingRestrictionCode;
	}

	/**
	 * Sets the SellingRestrictionCode.
	 *
	 * @param sellingRestrictionCode The SellingRestrictionCode.
	 **/
	public void setSellingRestrictionCode(SellingRestrictionCode sellingRestrictionCode) {
		this.sellingRestrictionCode = sellingRestrictionCode;
	}

	/**
	 * Returns whether this shipping restriction is inherited from higher level in the hierarchy or not.
	 *
	 * @return True if inherited, false or null otherwise.
	 **/
	public Boolean getInherited() {
		return inherited;
	}

	/**
	 * Sets whether this shipping restriction is inherited from higher level in the hierarchy or not.
	 *
	 * @param inherited The inherited value.
	 **/
	public void setInherited(Boolean inherited) {
		this.inherited = inherited;
	}

	/**
	 * Gets the action code for a shipping restriction.
	 *
	 * @return the action code.
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the action code for this shipping restriction. The is the action to be done for the shipping restriction
	 * (i.e. "A" for add, "D" for delete).
	 *
	 * @param actionCode The action code to set on this shipping restriction.
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Gets whether or not to override lower level shipping restrictions.
	 *
	 * @return the overrideLowerLevels switch.
	 */
	public Boolean getOverrideLowerLevels() {
		return overrideLowerLevels;
	}

	/**
	 * Sets whether or not to override (delete) any lower level restrictions.
	 *
	 * @param overrideLowerLevels Whether or not to override lower level restrictions.
	 */
	public void setOverrideLowerLevels(Boolean overrideLowerLevels) {
		this.overrideLowerLevels = overrideLowerLevels;
	}
	/**
	 * Required to be able to use Collections.sort to sort a list of ShippingRestrictionHierarchyLevel based on their
	 * restriction's description.
	 *
	 * @param o The object to compare to
	 * @return a negative value if this.description < o.description, zero if this.description = o.description
	 * and a positive value if this.description > o.description
	 */
	@Override
	public int compareTo(@Nonnull ShippingRestrictionHierarchyLevel o) {
		if(this.getSellingRestrictionCode() != null && o.getSellingRestrictionCode() != null) {
			return this.getSellingRestrictionCode().getRestrictionDescription()
					.compareTo(o.getSellingRestrictionCode().getRestrictionDescription());
		} else {
			return 0;
		}
	}

	/**
	 * Compares another object to this one. If that object is an ShippingRestrictionHierarchyLevel, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ShippingRestrictionHierarchyLevel that = (ShippingRestrictionHierarchyLevel) o;

		return key != null ? key.equals(that.key) : that.key == null;
	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}

	/**
	 * Returns a printable representation of the object.
	 *
	 * @return A printable representation of the object.
	 */
	@Override
	public String toString() {
		return "ShippingRestrictionHierarchyLevel{" +
				"applicableAtThisLevel=" + applicableAtThisLevel +
				", key=" + key +
				'}';
	}
}
