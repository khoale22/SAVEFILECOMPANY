package com.heb.pm.entity;

import java.util.List;

public class ProductRestrictionUpdates {
	private static final int FOUR_BYTES = 32;
	private Long prodId;
	private List<ProductRestrictions> restrictionsAdded;
	private List<ProductRestrictions> restrictionsRemoved;

	/**
	 * The unique identifier for the product to be updated
	 * @return
	 */
	public Long getProdId() {
		return prodId;
	}

	/**
	 * updates unique identifier for the product to be updated
	 * @param prodId
	 */
	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	/**
	 * The list of tier pricings to added to a product
	 * @return
	 */
	public List<ProductRestrictions> getRestrictionsAdded() {
		return restrictionsAdded;
	}

	/**
	 * Updates the list of tier pricings to be added
	 * @param tierPricingsAdded
	 */
	public void setRestrictionsAdded(List<ProductRestrictions> tierPricingsAdded) {
		this.restrictionsAdded = tierPricingsAdded;
	}

	/**
	 * The list of tier pricings to be removed from a product
	 * @return
	 */
	public List<ProductRestrictions> getRestrictionsRemoved() {
		return restrictionsRemoved;
	}

	/**
	 * updates the list of tier pricings to be removed
	 * @param tierPricingsRemoved
	 */
	public void setRestrictionsRemoved(List<ProductRestrictions> tierPricingsRemoved) {
		this.restrictionsRemoved = tierPricingsRemoved;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "ProductRestrictionUpdates{" +
				"prodId=" + prodId +
				", restrictionsAdded=" + restrictionsAdded +
				", restrictionsRemoved=" + restrictionsRemoved +
				'}';
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
		if (!(o instanceof ProductRestrictionUpdates)) return false;

		ProductRestrictionUpdates that = (ProductRestrictionUpdates) o;

		return prodId == that.prodId;

	}

	/**
	 * Returns a hash code for this object. If two objects are equal, they have the same hash. If they are not equal,
	 * they have different hash codes.
	 *
	 * @return The hash code for this obejct.
	 */
	@Override
	public int hashCode() {
		return (int) (prodId ^ (prodId >>> FOUR_BYTES));
	}
}
