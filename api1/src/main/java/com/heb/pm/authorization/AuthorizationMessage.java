/*
 *  AuthorizeItem
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.authorization;

import java.io.Serializable;

/**
 * Holds the data read from the authorization item related to DB.
 *
 * @author vn70529
 * @since 2.23.0
 */
public class AuthorizationMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * the function name.
	 */
	private String function;
	/**
	 * version authorize item.
	 */
	private String version;
	/**
	 * item id.
	 */
	private Long item;
	/**
	 * item description.
	 */
	private String itemDescription;
	/**
	 * store number.
	 */
	private String storeNumber;
	/**
	 * unit cost
	 */
	private Double unitCost;
	/**
	 * unit retail.
	 */
	private Double unitRetail;
	/**
	 * userId of user authorize item.
	 */
	private String userId;
	/**
	 * username of user authorize item.
	 */
	private String userName;


	/**
	 * Return a String representation of this object.
	 *
	 * @return A String representation of this object.
	 */
	@Override
	public String toString() {
		return "AuthorizationMessage{" +
				", function='" + function + '\'' +
				", version='" + version + '\'' +
				", item=" + item +
				", itemDescription='" + itemDescription + '\'' +
				", storeNumber='" + storeNumber + '\'' +
				", unitCost='" + unitCost + '\'' +
				", unitRetail='" + unitRetail + '\'' +
				", userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				'}';
	}

	/**
	 * Returns the function that was performed.
	 *
	 * @return The function that was performe
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * Sets the function that was performed.
	 *
	 * @param function The function that was performed.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setFunction(String function) {
		this.function = function;
		return this;
	}

	/**
	 * Returns the version of the JSON message.
	 *
	 * @return The version of the JSON message.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version of the JSON message.
	 *
	 * @param version The version of the JSON message.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setVersion(String version) {
		this.version = version;
		return this;
	}

	/**
	 * Returns the item code of the message.
	 *
	 * @return The item code of the message.
	 */
	public Long getItem() {
		return item;
	}

	/**
	 * Sets the item code of the message.
	 *
	 * @param item The item code of the message.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setItem(Long item) {
		this.item = item;
		return this;
	}

	/**
	 * Returns the item description of the message.
	 *
	 * @return The item description of the message.
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * Sets the item description of the message.
	 *
	 * @param itemDescription The item description of the message.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
		return this;
	}

	/**
	 * Returns the store number of the message.
	 *
	 * @return The store number of the message.
	 */
	public String getStoreNumber() {
		return storeNumber;
	}

	/**
	 * Sets the store number of the message.
	 *
	 * @param storeNumber The store number of the message.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
		return this;
	}

	/**
	 * Returns the unit cost of the message.
	 *
	 * @return The unit cost of the message.
	 */
	public Double getUnitCost() {
		return unitCost;
	}

	/**
	 * Sets the unit cost of the message.
	 *
	 * @param unitCost The unit cost of the message.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
		return this;
	}

	/**
	 * Returns the unit retail of the message.
	 *
	 * @return The unit retail of the message.
	 */
	public Double getUnitRetail() {
		return unitRetail;
	}

	/**
	 * Sets the unit retail of the message.
	 *
	 * @param unitRetail The unit retail of the message.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setUnitRetail(Double unitRetail) {
		this.unitRetail = unitRetail;
		return this;
	}

	/**
	 * Returns the ID of the user who made the authorization.
	 *
	 * @return The ID of the user who made the authorization.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the ID of the user who made the authorization.
	 *
	 * @param userId The ID of the user who made the authorization.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	/**
	 * Returns the name of the user who made the authorization.
	 *
	 * @return The name of the user who made the authorization.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the name of the user who made the authorization.
	 *
	 * @param userName The name of the user who made the authorization.
	 * @return This object for further configuration.
	 */
	public AuthorizationMessage setUserName(String userName) {
		this.userName = userName;
		return this;
	}
}
