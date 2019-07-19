package com.heb.pm.entity;

/**
 * Represents Product assortment data returned by ProductAssortmentService then returned to the UI.
 *
 * @author m594201
 * @since 2.12.0
 */
public class ProductAssortment {

	private long storeCount;

	/**
	 * Gets total count SSA Count Assorted in Stores.
	 *
	 * @return the total count SSA Count Assorted in Stores.
	 */
	public long getStoreCount() {
		return storeCount;
	}

	/**
	 * Sets total count SSA Count Assorted in Stores.
	 *
	 * @param storeCount the total count SSA Count Assorted in Stores.
	 */
	public void setStoreCount(long storeCount) {
		this.storeCount = storeCount;
	}
}
