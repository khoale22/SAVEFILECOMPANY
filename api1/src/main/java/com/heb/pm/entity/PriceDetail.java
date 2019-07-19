package com.heb.pm.entity;

import java.io.Serializable;

/**
 * Represents Price Detail data returned by PriceService then returned to the UI.
 *
 * @author m594201
 * @since 2.12.0
 */
public class PriceDetail implements Serializable{

	private static final long serialVersionUID = 3479003112993247683L;
	private Integer xFor;

	private double retailPrice;

	private Integer zone;

	private Boolean weight;

	/**
	 * Gets retail price.  Price at which a product is sold to the customer.
	 *
	 * @return the retail price at which a product is sold to the customer.
	 */
	public double getRetailPrice() {
		return retailPrice;
	}

	/**
	 * Sets retail price.
	 *
	 * @param retailPrice the retail price
	 */
	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	/**
	 * Gets regular retails x for. Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 *
	 * @return the regular retails x for Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 */
	public Integer getxFor() {
		return xFor;
	}

	/**
	 * Gets regular retails x for. Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 *
	 * @return the regular retails x for Numeric unit available for retail, but not considered inventory.  1 Bottle, 1 Package X FOR (retails for)
	 */
	public void setxFor(Integer xFor) {
		this.xFor = xFor;
	}

	/**
	 * Gets zone. Group of stores with same retails
	 *
	 * @return the zone group of stores with same retails
	 */
	public Integer getZone() {
		return zone;
	}

	/**
	 * Sets zone. Group of stores with same retails
	 *
	 * @param zone the zone group of stores with same retails
	 */
	public void setZone(Integer zone) {
		this.zone = zone;
	}

	/**
	 * Returns Weight. This is whether or not this item is weighed.
	 *
	 * @return The Weight.
	 **/
	public Boolean getWeight() {
		return weight;
	}

	/**
	 * Sets the Weight.
	 *
	 * @param weight The Weight.
	 **/
	public void setWeight(Boolean weight) {
		this.weight = weight;
	}
}
