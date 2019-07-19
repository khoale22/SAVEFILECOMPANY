/*
 * ItemInfo.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
/**
 * Item object. Contains item level information.
 *
 * @author s573181
 * @since 2.23.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	private String itemType;
	private Long itemCode;
	private Integer commodity;
	private Integer subCommodity;
	private ContainedUpc containedUpc;

	/**
	 * Returns the item type (ITMCD or DSD).
	 *
	 * @return the item type (ITMCD or DSD).
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Sets the item type (ITMCD or DSD).
	 *
	 * @param itemType the item type (ITMCD or DSD).
	 * @return the updated Item.
	 */
	public Item setItemType(String itemType) {
		this.itemType = itemType;
		return this;
	}

	/**
	 * Returns the item code.
	 *
	 * @return the item code.
	 */
	public Long getItemCode() {
		return itemCode;
	}

	/**
	 * Sets the item code.
	 *
	 * @param itemCode the item code.
	 * @return the updated Item.
	 */
	public Item setItemCode(Long itemCode) {
		this.itemCode = itemCode;
		return this;
	}
	/**
	 * Returns the commodity.
	 *
	 * @return the commodity.
	 */
	public Integer getCommodity() {
		return commodity;
	}

	/**
	 * Sets the commodity.
	 *
	 * @param commodity the commodity.
	 * @return the updated item info.
	 */
	public Item setCommodity(Integer commodity) {
		this.commodity = commodity;
		return this;
	}

	/**
	 * Returns the sub commodity.
	 *
	 * @return the sub commodity.
	 */
	public Integer getSubCommodity() {
		return subCommodity;
	}

	/**
	 * Sets the sub commodity.
	 * @param subCommodity the sub commodity.
	 * @return the updated item info.
	 */
	public Item setSubCommodity(Integer subCommodity) {
		this.subCommodity = subCommodity;
		return this;
	}

	/**
	 * Returns the contained upc.
	 *
	 * @return the contained upc.
	 */
	public ContainedUpc getContainedUpc() {
		return containedUpc;
	}

	/**
	 * Sets the contained upc.
	 *
	 * @param containedUpc the contained upc.
	 * @return the updated item info.
	 */
	public Item setContainedUpc(ContainedUpc containedUpc) {
		this.containedUpc = containedUpc;
		return this;
	}

}
