/*
 *
 *  DiscontinueRulesObject
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.productDiscontinue;

import java.io.Serializable;

/**
 * Represents the product discontinue rules flattened out in a way that more directly represents the business
 * understanding of the business rules. This object will make it easier to send data back and forth with the
 * front end.
 *
 * @author s573181
 * @since 2.0.2
 */
public class DiscontinueRules implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int PRIME_NUMBER = 31;

	// These are set for both default and exception delete rules.
	private boolean salesSwitch;
	private String storeSales;
	private boolean newProductSetupSwitch;
	private String newItemPeriod;
	private boolean warehouseUnitSwitch;
	private String warehouseUnits;
	private boolean storeUnitSwitch;
	private String storeUnits;
	private boolean receiptsSwitch;
	private String storeReceipts;
	private boolean purchaseOrderSwitch;
	private String purchaseOrders;

	// The remaining are set only for discontinue exception rules.
	private boolean neverDiscontinueSwitch;
	private String exceptionTypeId;
	private String exceptionName;
	private String exceptionType;
	private int exceptionNumber;
	private int priorityNumber;

	/**
	 * Returns boolean neverDiscontinueSwitch for the DiscontinueRules.
	 *
	 * @return The boolean neverDiscontinueSwitch for the DiscontinueRules object.
	 */
	public boolean isNeverDiscontinueSwitch() {
		return neverDiscontinueSwitch;
	}

	/**
	 * Sets the neverDiscontinueSwitch for the DiscontinueRules object.
	 *
	 * @param neverDiscontinueSwitch The neverDiscontinueSwitch for the DiscontinueRules object.
	 */
	public void setNeverDiscontinueSwitch(boolean neverDiscontinueSwitch) {
		this.neverDiscontinueSwitch = neverDiscontinueSwitch;
	}

	/**
	 * Returns boolean isSalesSwitch for the DiscontinueRules.
	 *
	 * @return The boolean isSalesSwitch for the DiscontinueRules object.
	 */
	public boolean isSalesSwitch() {
		return salesSwitch;
	}

	/**
	 * Sets the isSalesSwitch for the DiscontinueRules object.
	 *
	 * @param salesSwitch The salesSwitch for the DiscontinueRules object.
	 */
	public void setSalesSwitch(boolean salesSwitch) {
		this.salesSwitch = salesSwitch;
	}

	/**
	 * Returns storeSales for the DiscontinueRules.
	 *
	 * @return The storeSales for the DiscontinueRules object.
	 */
	public String getStoreSales() {
		return storeSales;
	}

	/**
	 * Sets the storeSales for the DiscontinueRules object.
	 *
	 * @param storeSales The storeSales for the DiscontinueRules object.
	 */
	public void setStoreSales(String storeSales) {
		this.storeSales = storeSales;
	}

	/**
	 * Returns boolean receiptsSwitch for the DiscontinueRules.
	 *
	 * @return The boolean receiptsSwitch for the DiscontinueRules object.
	 */
	public boolean isReceiptsSwitch() {
		return receiptsSwitch;
	}

	/**
	 * Sets the receiptsSwitch for the DiscontinueRules object.
	 *
	 * @param receiptsSwitch The receiptsSwitch for the DiscontinueRules object.
	 */
	public void setReceiptsSwitch(boolean receiptsSwitch) {
		this.receiptsSwitch = receiptsSwitch;
	}

	/**
	 * Returns storeReceipts for the DiscontinueRules.
	 *
	 * @return The storeReceipts for the DiscontinueRules object.
	 */
	public String getStoreReceipts() {
		return storeReceipts;
	}

	/**
	 * Sets the storeReceipts for the DiscontinueRules object.
	 *
	 * @param storeReceipts The storeReceipts for the DiscontinueRules object.
	 */
	public void setStoreReceipts(String storeReceipts) {
		this.storeReceipts = storeReceipts;
	}

	/**
	 * Returns boolean storeUnitSwitch for the DiscontinueRules.
	 *
	 * @return The boolean storeUnitSwitch for the DiscontinueRules object.
	 */
	public boolean isStoreUnitSwitch() {
		return storeUnitSwitch;
	}

	/**
	 * Sets the storeUnitSwitch for the DiscontinueRules object.
	 *
	 * @param storeUnitSwitch The storeUnitSwitch for the DiscontinueRules object.
	 */
	public void setStoreUnitSwitch(boolean storeUnitSwitch) {
		this.storeUnitSwitch = storeUnitSwitch;
	}

	/**
	 * Returns storeUnits for the DiscontinueRules.
	 *
	 * @return The storeUnits for the DiscontinueRules object.
	 */
	public String getStoreUnits() {
		return storeUnits;
	}

	/**
	 * Sets the storeUnits for the DiscontinueRules object.
	 *
	 * @param storeUnits The storeUnits for the DiscontinueRules object.
	 */
	public void setStoreUnits(String storeUnits) {
		this.storeUnits = storeUnits;
	}

	/**
	 * Returns boolean warehouseUnitSwitch for the DiscontinueRules.
	 *
	 * @return The boolean warehouseUnitSwitch for the DiscontinueRules object.
	 */
	public boolean isWarehouseUnitSwitch() {
		return warehouseUnitSwitch;
	}

	/**
	 * Sets the warehouseUnitSwitch for the DiscontinueRules object.
	 *
	 * @param warehouseUnitSwitch The warehouseUnitSwitch for the DiscontinueRules object.
	 */
	public void setWarehouseUnitSwitch(boolean warehouseUnitSwitch) {
		this.warehouseUnitSwitch = warehouseUnitSwitch;
	}

	/**
	 * Returns warehouseUnits for the DiscontinueRules.
	 *
	 * @return The warehouseUnits for the DiscontinueRules object.
	 */
	public String getWarehouseUnits() {
		return warehouseUnits;
	}

	/**
	 * Sets the warehouseUnits for the DiscontinueRules object.
	 *
	 * @param warehouseUnits The warehouseUnits for the DiscontinueRules object.
	 */
	public void setWarehouseUnits(String warehouseUnits) {
		this.warehouseUnits = warehouseUnits;
	}

	/**
	 * Returns boolean newProductSetupSwitch for the DiscontinueRules.
	 *
	 * @return The boolean newProductSetupSwitch for the DiscontinueRules object.
	 */
	public boolean isNewProductSetupSwitch() {
		return newProductSetupSwitch;
	}

	/**
	 * Sets the newProductSetupSwitch for the DiscontinueRules object.
	 *
	 * @param newProductSetupSwitch The newProductSetupSwitch for the DiscontinueRules object.
	 */
	public void setNewProductSetupSwitch(boolean newProductSetupSwitch) {
		this.newProductSetupSwitch = newProductSetupSwitch;
	}

	/**
	 * Returns newItemPeriod for the DiscontinueRules.
	 *
	 * @return The newItemPeriod for the DiscontinueRules object.
	 */
	public String getNewItemPeriod() {
		return newItemPeriod;
	}

	/**
	 * Sets the newItemPeriod for the DiscontinueRules object.
	 *
	 * @param newItemPeriod The newItemPeriod for the DiscontinueRules object.
	 */
	public void setNewItemPeriod(String newItemPeriod) {
		this.newItemPeriod = newItemPeriod;
	}

	/**
	 * Returns boolean purchaseOrderSwitch for the DiscontinueRules.
	 *
	 * @return The boolean purchaseOrderSwitch for the DiscontinueRules object.
	 */
	public boolean isPurchaseOrderSwitch() {
		return purchaseOrderSwitch;
	}

	/**
	 * Sets the purchaseOrderSwitch for the DiscontinueRules object.
	 *
	 * @param purchaseOrderSwitch The purchaseOrderSwitch for the DiscontinueRules object.
	 */
	public void setPurchaseOrderSwitch(boolean purchaseOrderSwitch) {
		this.purchaseOrderSwitch = purchaseOrderSwitch;
	}

	/**
	 * Returns purchaseOrderSwitch for the DiscontinueRules.
	 *
	 * @return The purchaseOrderSwitch for the DiscontinueRules object.
	 */
	public String getPurchaseOrders() {
		return purchaseOrders;
	}

	/**
	 * Sets the purchaseOrders for the DiscontinueRules object.
	 *
	 * @param purchaseOrders The purchaseOrders for the DiscontinueRules object.
	 */
	public void setPurchaseOrders(String purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}

	/**
	 * Returns the ID of what this exception applies to. For example, if this is a Vendor exception,
	 * this holds the vendor number.
	 *
	 * @return The ID of the exception.
	 */
	public String getExceptionTypeId() {
		return exceptionTypeId;
	}

	/**
	 * Sets the ID of what this exception applies to.
	 *
	 * @param exceptionTypeId The ID of the exception.
	 */
	public void setExceptionTypeId(String exceptionTypeId) {
		this.exceptionTypeId = exceptionTypeId;
	}

	/**
	 * Returns the exception's name.
	 *
	 * @return The exceptions' name.
	 */
	public String getExceptionName() {
		return exceptionName;
	}

	/**
	 * Sets the exception's name.
	 *
	 * @param exceptionName The exception's name.
	 */
	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	/**
	 * Returns the type of exception this is (eg. Vendor).
	 *
	 * @return The type of exception this is.
	 */
	public String getExceptionType() {
		return exceptionType;
	}

	/**
	 * Sets the type of exception this is.
	 *
	 * @param exceptionType The type of exception this is.
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	/**
	 * Returns the exception number for this object. An exception rule is grouped together by these numbers. For
	 * example, if there is a rule set up for a particular vendor, all the components of the rule share this ID.
	 *
	 * @return The exception number for this object.
	 */
	public int getExceptionNumber() {
		return exceptionNumber;
	}

	/**
	 * Sets the exception number for this object.
	 *
	 * @param exceptionNumber The exception number for this object.
	 */
	public void setExceptionNumber(int exceptionNumber) {
		this.exceptionNumber = exceptionNumber;
	}

	/**
	 * Returns the exception's priority number.
	 *
	 * @return The exception's priority number.
	 */
	public int getPriorityNumber() {
		return priorityNumber;
	}

	/**
	 * Sets the exception's priority number.
	 *
	 * @param priorityNumber The exception's priority number.
	 */
	public void setPriorityNumber(int priorityNumber) {
		this.priorityNumber = priorityNumber;
	}

	/**
	 * Tests for equality against an object. Equality is based on all fields.
	 *
	 * @param o The object to test against.
	 * @return True if the objects are equal and false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DiscontinueRules)) return false;

		DiscontinueRules that = (DiscontinueRules) o;

		if (neverDiscontinueSwitch != that.neverDiscontinueSwitch) return false;
		if (salesSwitch != that.salesSwitch) return false;
		if (receiptsSwitch != that.receiptsSwitch) return false;
		if (storeUnitSwitch != that.storeUnitSwitch) return false;
		if (warehouseUnitSwitch != that.warehouseUnitSwitch) return false;
		if (newProductSetupSwitch != that.newProductSetupSwitch) return false;
		if (purchaseOrderSwitch != that.purchaseOrderSwitch) return false;
		if (storeSales != null ? !storeSales.equals(that.storeSales) : that.storeSales != null) return false;
		if (storeReceipts != null ? !storeReceipts.equals(that.storeReceipts) : that.storeReceipts != null)
			return false;
		if (storeUnits != null ? !storeUnits.equals(that.storeUnits) : that.storeUnits != null) return false;
		if (warehouseUnits != null ? !warehouseUnits.equals(that.warehouseUnits) : that.warehouseUnits != null)
			return false;
		if (newItemPeriod != null ? !newItemPeriod.equals(that.newItemPeriod) : that.newItemPeriod != null)
			return false;
		return !(purchaseOrders != null ? !purchaseOrders.equals(that.purchaseOrders) : that.purchaseOrders != null);

	}

	/**
	 * Returns a hash code for this object. Equal objects have the same hash code. Unequal objects (probably) have
	 * different hash codes.
	 *
	 * @return A hash code for this object.
	 */
	@Override
	public int hashCode() {
		int result = (neverDiscontinueSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (salesSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (storeSales != null ? storeSales.hashCode() : 0);
		result = PRIME_NUMBER * result + (receiptsSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (storeReceipts != null ? storeReceipts.hashCode() : 0);
		result = PRIME_NUMBER * result + (storeUnitSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (storeUnits != null ? storeUnits.hashCode() : 0);
		result = PRIME_NUMBER * result + (warehouseUnitSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (warehouseUnits != null ? warehouseUnits.hashCode() : 0);
		result = PRIME_NUMBER * result + (newProductSetupSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (newItemPeriod != null ? newItemPeriod.hashCode() : 0);
		result = PRIME_NUMBER * result + (purchaseOrderSwitch ? 1 : 0);
		result = PRIME_NUMBER * result + (purchaseOrders != null ? purchaseOrders.hashCode() : 0);
		return result;
	}

	/**
	 * Returns a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "DiscontinueRules{" +
				"neverDiscontinueSwitch=" + neverDiscontinueSwitch +
				", salesSwitch=" + salesSwitch +
				", storeSales='" + storeSales + '\'' +
				", receiptsSwitch=" + receiptsSwitch +
				", storeReceipts='" + storeReceipts + '\'' +
				", storeUnitSwitch=" + storeUnitSwitch +
				", storeUnits='" + storeUnits + '\'' +
				", warehouseUnitSwitch=" + warehouseUnitSwitch +
				", warehouseUnits='" + warehouseUnits + '\'' +
				", newProductSetupSwitch=" + newProductSetupSwitch +
				", newItemPeriod='" + newItemPeriod + '\'' +
				", purchaseOrderSwitch=" + purchaseOrderSwitch +
				", purchaseOrders='" + purchaseOrders + '\'' +
				", exceptionTypeId='" + exceptionTypeId + '\'' +
				", exceptionName='" + exceptionName + '\'' +
				", exceptionType='" + exceptionType + '\'' +
				", exceptionNumber=" + exceptionNumber +
				", priorityNumber=" + priorityNumber +
				'}';
	}
}
