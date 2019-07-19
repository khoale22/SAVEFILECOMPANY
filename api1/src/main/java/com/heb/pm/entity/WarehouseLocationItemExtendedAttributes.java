/*
 *  WarehouseLocationItemExtendedAttributes
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

package com.heb.pm.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Holds the extended attributes (the less commonly used) for ship pack data for a particular warehouse.
 *
 * @author s573181
 * @since 2.0.4
 */
@Entity
@Table(name="whse_loc_itm_extn")
public class WarehouseLocationItemExtendedAttributes implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(WarehouseLocationItemExtendedAttributes.class);

	@EmbeddedId
	private WarehouseLocationItemKey key;

	@Column(name = "hi_on_ord_fwd_buy")
	private int onOrderForForwardBuy;

	@Column(name = "hi_on_ord_promo")
	private int onOrderPromotion;

	@Column(name = "hi_on_ord_total")
	private int totalOnOrder;

	@Column(name = "hi_on_ord_turn")
	private int onOrderForTurn;

	@Column(name = "hi_first_received")
	private Integer firstReceived;

	@Column(name = "HI_LST_CST")
	private Double lastCost;

	@Column(name = "HI_OLD_LAST_CST")
	private Double priorLastCost;

	/**
	 * Returns the key for this record.
	 *
	 * @return The key for this record.
	 */
	public WarehouseLocationItemKey getKey() {
		return key;
	}

	/**
	 * Sets the key for this record.
	 *
	 * @param key The key for this record.
	 */
	public void setKey(WarehouseLocationItemKey key) {
		this.key = key;
	}

	/**
	 * Returns the number of on order forward buys for this object.
	 *
	 * @return The number of on order forward buys for this object.
	 */
	public int getOnOrderForForwardBuy() {
		return onOrderForForwardBuy;
	}

	/**
	 * Sets the number of on order forward buys for this object.
	 *
	 * @param onOrderForForwardBuy The number of on order forward buys for this object.
	 */
	public void setOnOrderForForwardBuy(int onOrderForForwardBuy) {
		this.onOrderForForwardBuy = onOrderForForwardBuy;
	}

	/**
	 * Returns the number of on order promotions for this object.
	 *
	 * @return The number of on order promotions for this object.
	 */
	public int getOnOrderPromotion() {
		return onOrderPromotion;
	}

	/**
	 * Sets the number of on order promotions for this object.
	 *
	 * @param onOrderPromotion The number of on order promotions for this object.
	 */
	public void setOnOrderPromotion(int onOrderPromotion) {
		this.onOrderPromotion = onOrderPromotion;
	}

	/**
	 * Returns the total number on order for this object.
	 *
	 * @return The total number on order for this object.
	 */
	public int getTotalOnOrder() {
		return totalOnOrder;
	}

	/**
	 * Sets the total number on order for this object.
	 *
	 * @param totalOnOrder The total number on order for this object.
	 */
	public void setTotalOnOrder(int totalOnOrder) {
		this.totalOnOrder = totalOnOrder;
	}

	/**
	 * Returns the number of on order for the turn of this object.
	 *
	 * @return The number of on order for the turn of this object.
	 */
	public int getOnOrderForTurn() {
		return onOrderForTurn;
	}

	/**
	 * Sets the number of on order for the turn of this object.
	 *
	 * @param onOrderForTurn The number of on order for the turn of this object.
	 */
	public void setOnOrderForTurn(int onOrderForTurn) {
		this.onOrderForTurn = onOrderForTurn;
	}

	/**
	 * Returns the FirstReceived. Returns the first received date(in mddyy or mmddyy format) from a warehouse.
	 *
	 * @return FirstReceived
	 */
	public Integer getFirstReceived() {
		return firstReceived;
	}

	/**
	 * Sets the FirstReceived
	 *
	 * @param firstReceived The FirstReceived
	 */
	public void setFirstReceived(Integer firstReceived) {
		this.firstReceived = firstReceived;
	}


	/**
	 * This converts a string in the format MMddyy to a local date that converts it to MM-dd-yyyy.
	 *
	 * @return local date or null if there isn't any information on a first received date.
	 */
	public LocalDate getFirstReceivedDate() {
		String paddedNumber = String.format("%06d", this.getFirstReceived());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		try {
			Date date = new SimpleDateFormat("MMddyy").parse(paddedNumber);
			String dateString2 = new SimpleDateFormat("MM/dd/yyyy").format(date);
			LocalDate localDate = LocalDate.parse(dateString2, formatter);
			return localDate;
		} catch (ParseException e) {
			WarehouseLocationItemExtendedAttributes.logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Last cost when the last ordered from warehouse.
	 * @return lastCost
	 */
	public Double getLastCost() {
		return lastCost;
	}

	/**
	 * Updates lastCost
	 * @param lastCost the new lastCost
	 */
	public void setLastCost(Double lastCost) {
		this.lastCost = lastCost;
	}

	/**
	 * What the cost was when first ordered from warehouse
	 * @return priorLastCost
	 */
	public Double getPriorLastCost() {
		return priorLastCost;
	}

	/**
	 * Updates the priorLastCost
	 * @param priorLastCost the new priorLastCost
	 */
	public void setPriorLastCost(Double priorLastCost) {
		this.priorLastCost = priorLastCost;
	}

	/**
	 * Compares another object to this one. If that object is a WarehouseLocationItemExtendedAttributes, it uses they keys
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
		if (!(o instanceof WarehouseLocationItemExtendedAttributes)) {
			return false;
		}

		WarehouseLocationItemExtendedAttributes that = (WarehouseLocationItemExtendedAttributes) o;
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

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "WarehouseLocationItemExtendedAttributes{" +
				"key=" + key +
				", onOrderForForwardBuy=" + onOrderForForwardBuy +
				", onOrderPromotion=" + onOrderPromotion +
				", totalOnOrder=" + totalOnOrder +
				", onOrderForTurn=" + onOrderForTurn +
				", lastCost=" + lastCost +
				", priorLastCost=" + priorLastCost +
				'}';
	}
}
