package com.heb.pm.productSearch;

import com.heb.pm.entity.FulfillmentChannel;
import com.heb.pm.entity.TagType;
import com.heb.pm.entity.VertexTaxCategory;

import java.time.LocalDate;

/**
 * Class to hold specifics related to the build you own search function.
 *
 * @author d116773
 * @since 2.14.0
 */
public class CustomSearchEntry {

	public static final int PRIMO_PICK = 0;
	public static final int PRIMO_PICK_STATUS = 1;
	public static final int ITEM_ADD_DATE= 2;
	public static final int SERVICE_CASE_STATUS= 3;
	public static final int DISTINCTIVE = 4;
	public static final int GO_LOCAL = 5;
	public static final int SELECT_INGREDIENTS = 6;
	public static final int PRODUCT_ADD_DATE = 7;
	public static final int RETAIL_LINK = 8;
	public static final int THIRD_PARTY_SELLABLE = 9;
	public static final int DSV = 10;
	public static final int PUBLISH_STATUS = 11;
	public static final int SELF_MANUFACTURED = 12;
	public static final int TOTALLY_TEXAS = 13;
	public static final int PUBLISH_DATE = 14;
	public static final int EBM = 15;
	public static final int ECOMMERCE_BRAND = 16;
	public static final int TAX_CATEGORY = 17;
	public static final int TAX_QUALIFYING_CONDITION = 18;
	public static final int FULFILLMENT_CHANNEL = 19;
	public static final int TAG_TYPE = 20;
	public static final int MAT = 21;
	public static final int IMAGE_CHANGE_ON_DATE = 22;

	public static final int EQUAL = 0;
	public static final int GREATER_THAN = 1;
	public static final int LESS_THAN = 2;
	public static final int ONE_WEEK = 3;
	public static final int BETWEEN = 4;

	private int type;
	private int operator;
	private String stringComparator;
	private Boolean booleanComparator;
	private LocalDate dateComparator;
	private LocalDate endDateComparator;
	private Integer integerComparator;
	private VertexTaxCategory taxCategory;
	private TagType tagType;
	private FulfillmentChannel fulfillmentChannel;

	/**
	 * Holds the list of products as string that client request for image search.
	 */
	private String productIds;
	/**
	 * Holds the list of upcs as string that client request for image search.
	 */
	private String upcs;
	/**
	 * Holds the list of item codes as string that client request for image search.
	 */
	private String itemCodes;
	/**
	 * Returns the type of search this is. Allowable types are defined as constants above.
	 *
	 * @return The type of search this is.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type of search this is.
	 *
	 * @param type The type of search this is
	 */
	public void setType(int type) {
		if (type < CustomSearchEntry.PRIMO_PICK || type > CustomSearchEntry.IMAGE_CHANGE_ON_DATE) {

			throw new IllegalArgumentException(String. format("Type must be between %d and %d",
					CustomSearchEntry.PRIMO_PICK, CustomSearchEntry.IMAGE_CHANGE_ON_DATE));
		}
		this.type = type;
	}

	/**
	 * Returns the operand to use for this comparison. This is not meaningful for every type.
	 *
	 * @return The operand to use for this comparison.
	 */
	public int getOperator() {
		return operator;
	}

	/**
	 * Sets the operand to use for this comparison.
	 *
	 * @param operator The operand to use for this comparison.
	 */
	public void setOperator(int operator) {
		if (operator < CustomSearchEntry.EQUAL || operator > CustomSearchEntry.BETWEEN) {

			throw new IllegalArgumentException(String. format("Operand must be between %d and %d",
					CustomSearchEntry.EQUAL, CustomSearchEntry.BETWEEN));
		}
		this.operator = operator;
	}

	// The remaining may or may not be set based on different types of searches.

	/**
	 * If the value should be compared to a string, returns that string.
	 *
	 * @return The string to compare to.
	 */
	public String getStringComparator() {
		return stringComparator;
	}

	/**
	 * Sets the string to compare to.
	 *
	 * @param stringComparator The string to compare to.
	 */
	public void setStringComparator(String stringComparator) {
		this.stringComparator = stringComparator;
	}

	/**
	 * If the value should be compared to a boolean, returns that boolean.
	 *
	 * @return The boolean to compare to.
	 */
	public Boolean getBooleanComparator() {
		return booleanComparator;
	}

	/**
	 * Sets the boolean to compare to.
	 *
	 * @param booleanComparator The boolean to compare to.
	 */
	public void setBooleanComparator(Boolean booleanComparator) {
		this.booleanComparator = booleanComparator;
	}

	/**
	 * If the value should be compared to a date, returns that date.
	 *
	 * @return The date to compare to.
	 */
	public LocalDate getDateComparator() {
		return dateComparator;
	}

	/**
	 * Sets the date to compare to.
	 *
	 * @param dateComparator The date to compare to.
	 */
	public void setDateComparator(LocalDate dateComparator) {
		this.dateComparator = dateComparator;
	}

	/**
	 * If the value should be compared to a date range, returns the end date.
	 *
	 * @return The end date to compare to.
	 */
	public LocalDate getEndDateComparator() {
		return endDateComparator;
	}

	/**
	 * Sets the end date to compare to.
	 *
	 * @param endDateComparator The end date to compare to.
	 */
	public void setEndDateComparator(LocalDate endDateComparator) {
		this.endDateComparator = endDateComparator;
	}

	/**
	 * If the value should be compared to an integer, returns that integer.
	 *
	 * @return The integer to compare to.
	 */
	public Integer getIntegerComparator() {
		return integerComparator;
	}

	/**
	 * Sets the integer to compare to.
	 *
	 * @param integerComparator The integer to compare to.
	 */
	public void setIntegerComparator(Integer integerComparator) {
		this.integerComparator = integerComparator;
	}

	/**
	 * If the value should be compared to a tax category or qualifying condition, returns that tax category
	 * or qualifying condition.
	 *
	 * @return The tax category or qualifying condition to compare to.
	 */
	public VertexTaxCategory getTaxCategory() {
		return taxCategory;
	}

	/**
	 * Sets the tax category or qualifying condition to compare to.
	 *
	 * @param taxCategory The tax category or qualifying condition to compare to.
	 */
	public void setTaxCategory(VertexTaxCategory taxCategory) {
		this.taxCategory = taxCategory;
	}

	/**
	 * If the value should be compared to a tax category or qualifying condition, returns that tax category
	 * or qualifying condition.
	 *
	 * @return The tax category or qualifying condition to compare to.
	 */
	public TagType getTagType() {
		return tagType;
	}

	/**
	 * Sets the tax category or qualifying condition to compare to.
	 *
	 * @param tagType The tax category or qualifying condition to compare to.
	 */
	public void setTagType(TagType tagType) {
		this.tagType = tagType;
	}

	/**
	 * If the value should be compared to a fulfillment channel, returns that fulfillment channel.
	 * 
	 * @return The fulfillment channel to compare to.
	 */
	public FulfillmentChannel getFulfillmentChannel() {
		return fulfillmentChannel;
	}

	/**
	 * Sets the fulfillment channel to compare to.
	 *
	 * @param fulfillmentChannel The fulfillment channel to compare to.
	 */
	public void setFulfillmentChannel(FulfillmentChannel fulfillmentChannel) {
		this.fulfillmentChannel = fulfillmentChannel;
	}
	/**
	 * Returns the list of product IDs the user wants to search by.
	 *
	 * @return The list of product IDs the user wants to search by.
	 */
	public String getProductIds() {
		return productIds;
	}

	/**
	 * Sets the list of product IDs the user wants to search by.
	 *
	 * @param productIds The list of product IDs the user wants to search by.
	 */
	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}

	/**
	 * Returns the list of UPCs the user wants to search by.
	 *
	 * @return The list of UPCs the user wants to search by.
	 */
	public String getUpcs() {
		return upcs;
	}

	/**
	 * Sets the list of UPCs the user wants to search by.
	 *
	 * @param upcs The list of UPCs the user wants to search by.
	 */
	public void setUpcs(String upcs) {
		this.upcs = upcs;
	}

	/**
	 * Returns the list of item codes the user wants to search by.
	 *
	 * @return The list of item codes the user wants to search by.
	 */
	public String getItemCodes() {
		return itemCodes;
	}

	/**
	 * Sets the list of item codes the user wants to search by.
	 *
	 * @param itemCodes The list of item codes the user wants to search by.
	 */
	public void setItemCodes(String itemCodes) {
		this.itemCodes = itemCodes;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "CustomSearchEntry{" +
				"type=" + type +
				", operator=" + operator +
				", stringComparator='" + stringComparator + '\'' +
				", booleanComparator=" + booleanComparator +
				", dateComparator=" + dateComparator +
				", endDateComparator=" + endDateComparator +
				", integerComparator=" + integerComparator +
				", taxCategory=" + taxCategory +
				", fulfillmentChannel=" + fulfillmentChannel +
				", upcs=" + upcs +
				", productIds=" + productIds +
				", itemCodes=" + itemCodes +
				'}';
	}
}
