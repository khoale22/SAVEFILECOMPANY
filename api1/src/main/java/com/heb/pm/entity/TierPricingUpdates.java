package com.heb.pm.entity;

import java.util.List;

/**
 * This class holds all of the updates for a products tier pricing list
 * @author s753601
 * @version 2.13.0
 */
public class TierPricingUpdates {
	private static final int FOUR_BYTES = 32;
	private Long prodId;
	private List<TierPricing> tierPricingsAdded;
	private List<TierPricing> tierPricingsRemoved;

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
	public List<TierPricing> getTierPricingsAdded() {
		return tierPricingsAdded;
	}

	/**
	 * Updates the list of tier pricings to be added
	 * @param tierPricingsAdded
	 */
	public void setTierPricingsAdded(List<TierPricing> tierPricingsAdded) {
		this.tierPricingsAdded = tierPricingsAdded;
	}

	/**
	 * The list of tier pricings to be removed from a product
	 * @return
	 */
	public List<TierPricing> getTierPricingsRemoved() {
		return tierPricingsRemoved;
	}

	/**
	 * updates the list of tier pricings to be removed
	 * @param tierPricingsRemoved
	 */
	public void setTierPricingsRemoved(List<TierPricing> tierPricingsRemoved) {
		this.tierPricingsRemoved = tierPricingsRemoved;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "TierPricingUpdates{" +
				"prodId=" + prodId +
				", tierPricingsAdded=" + tierPricingsAdded +
				", tierPricingsRemoved=" + tierPricingsRemoved +
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
		if (!(o instanceof TierPricingUpdates)) return false;

		TierPricingUpdates that = (TierPricingUpdates) o;

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
