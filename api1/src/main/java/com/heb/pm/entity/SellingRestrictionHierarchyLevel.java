package com.heb.pm.entity;

import com.heb.pm.productHierarchy.ProductHierarchyController;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
/**
 * Represents a record in the sals_rstr_grp_dflt table.
 *
 * @author m314029
 * @since 2.6.0
 */
@Entity
@Table(name="sals_rstr_grp_dflt")
public class SellingRestrictionHierarchyLevel implements Serializable, Comparable<SellingRestrictionHierarchyLevel> {

	private static final long serialVersionUID = 1L;
	public static final String ADD_ACTION_CODE = "A";
	public static final String DELETE_ACTION_CODE = "D";

	// default constructor
	public SellingRestrictionHierarchyLevel(){super();}

	// copy constructor
	public SellingRestrictionHierarchyLevel(SellingRestrictionHierarchyLevel sellingRestrictionHierarchyLevel){
		super();
		this.setActionCode(sellingRestrictionHierarchyLevel.getActionCode());
		this.setInherited(sellingRestrictionHierarchyLevel.getInherited());
		this.setOverrideLowerLevels(sellingRestrictionHierarchyLevel.getOverrideLowerLevels());
		this.setKey(new SellingRestrictionHierarchyLevelKey(sellingRestrictionHierarchyLevel.getKey()));
		this.setSellingRestriction(new SellingRestriction(sellingRestrictionHierarchyLevel.getSellingRestriction()));
	}

	@EmbeddedId
	private SellingRestrictionHierarchyLevelKey key;

	@Transient
	private Boolean inherited = false;

	@Transient
	private String actionCode;

	@Transient
	private Boolean overrideLowerLevels;

	@Transient
	private ProductHierarchyController.ProductHierarchyLevel productHierarchyLevel;

	@ManyToOne
	@JoinColumn(name = "sals_rstr_grp_cd", referencedColumnName = "sals_rstr_grp_cd", insertable = false, updatable = false, nullable = false)
	private SellingRestriction sellingRestriction;

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
	 * Returns Key.
	 *
	 * @return The Key.
	 **/
	public SellingRestrictionHierarchyLevelKey getKey() {
		return key;
	}

	/**
	 * Sets the Key.
	 *
	 * @param key The Key.
	 **/
	public void setKey(SellingRestrictionHierarchyLevelKey key) {
		this.key = key;
	}

	/**
	 * Returns SellingRestriction.
	 *
	 * @return The SellingRestriction.
	 **/
	public SellingRestriction getSellingRestriction() {
		return sellingRestriction;
	}

	/**
	 * Sets the SellingRestriction.
	 *
	 * @param sellingRestriction The SellingRestriction.
	 **/
	public void setSellingRestriction(SellingRestriction sellingRestriction) {
		this.sellingRestriction = sellingRestriction;
	}

	/**
	 * Returns whether this selling restriction is inherited from higher level in the hierarchy or not.
	 *
	 * @return True if inherited, false or null otherwise.
	 **/
	public Boolean getInherited() {
		return inherited != null ? inherited : false;
	}

	/**
	 * Sets whether this selling restriction is inherited from higher level in the hierarchy or not.
	 *
	 * @param inherited The inherited value.
	 **/
	public void setInherited(Boolean inherited) {
		this.inherited = inherited;
	}

	/**
	 * Gets the action code for a selling restriction.
	 *
	 * @return the action code.
	 */
	public String getActionCode() {
		return actionCode;
	}

	/**
	 * Sets the action code for this selling restriction. The is the action to be done for the selling restriction
	 * (i.e. "A" for add, "D" for delete).
	 *
	 * @param actionCode The action code to set on this selling restriction.
	 */
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	/**
	 * Gets whether or not to override lower level selling restrictions.
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
	 * Required to be able to use Collections.sort to sort a list of SellingRestrictionHierarchyLevel based on their
	 * restriction's description.
	 *
	 * @param o The object to compare to
	 * @return a negative value if this.description < o.description, zero if this.description = o.description
	 * and a positive value if this.description > o.description
	 */
	@Override
	public int compareTo(@Nonnull SellingRestrictionHierarchyLevel o) {
		if(this.getSellingRestriction() != null && o.getSellingRestriction() != null) {
			return this.getSellingRestriction().getRestrictionDescription()
					.compareTo(o.getSellingRestriction().getRestrictionDescription());
		} else {
			return 0;
		}
	}

	/**
	 * Compares another object to this one. If that object is an SellingRestrictionHierarchyLevel, it uses they keys
	 * to determine if they are equal and ignores non-key values for the comparison.
	 *
	 * @param o The object to compare to.
	 * @return True if they are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SellingRestrictionHierarchyLevel that = (SellingRestrictionHierarchyLevel) o;

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
		return "SellingRestrictionHierarchyLevel{" +
				"key=" + key +
				'}';
	}
}
