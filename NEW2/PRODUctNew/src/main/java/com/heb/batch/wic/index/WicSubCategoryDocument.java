/*
 * WicSubCategoryDocument
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * Wraps a WicSubCategory for storage in an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Document(indexName = "wic-sub-categories")
public class WicSubCategoryDocument{
	@Id
	private String id;
	@Field(type = FieldType.Long)
	private Long wicSubCategoryId;
	@Field(type = FieldType.Long)
	private Long wicCategoryId;
	@Field(type = FieldType.Text)
	private String description;
	@Field(type = FieldType.Text)
	private String lebSwitch;

	/**
	 * Returns the document's ID
	 * 
	 * @return Id
	 */
	public String getId() {
		if(this.id == null)
			this.id = generateId(this.wicCategoryId, this.wicSubCategoryId);
		return this.id;
	}

	/**
	 * Sets the document's ID
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the document's ID by Wic Category Id and Wic Sub Category Id
	 *
	 * @param id the WicCategoryId
	 * @param subId the WicSubCategoryId
	 */
	public void setId(Long id, Long subId) {
		this.id = generateId(id, subId);
	}

	/**
	 * Sets the document's ID by Wic Category Id and Wic Sub Category Id
	 *
	 * @param id the WicCategoryId
	 * @param subId the WicSubCategoryId
	 */
	public void setId(String id, String subId) {
		this.id = generateId(id, subId);
	}

	/**
	 * Generate Id by WicCategoryId and WicSubCategoryId
	 *
	 * @param id the WicCategoryId
	 * @param subId the WicSubCategoryId
	 * @return the Id
	 */
	public static String generateId(Long id, Long subId) {
		return String.format("%02d%03d", id, subId);
	}

	/**
	 * Generate Id by Wic Category Id and Wic Sub Category Id
	 *
	 * @param id the Wic Category Id
	 * @param subId the Wic Sub Category Id
	 * @return the Id
	 */
	public static String generateId(String id, String subId) {
		return String.format("%02d%03d", Long.valueOf(id), Long.valueOf(subId));
	}

	/**
	 * Returns the WicSubCategoryId
	 *
	 * @return WicSubCategoryId
	 */
	public Long getWicSubCategoryId() {
		return wicSubCategoryId;
	}

	/**
	 * Sets the WicSubCategoryId
	 *
	 * @param wicSubCategoryId The WicSubCategoryId
	 */
	public void setWicSubCategoryId(Long wicSubCategoryId) {
		this.wicSubCategoryId = wicSubCategoryId;
	}


	/**
	 * Returns the WicCategoryId
	 *
	 * @return WicCategoryId
	 */
	public Long getWicCategoryId() {
		return wicCategoryId;
	}

	/**
	 * Sets the WicCategoryId
	 *
	 * @param wicCategoryId The WicCategoryId
	 */
	public void setWicCategoryId(Long wicCategoryId) {
		this.wicCategoryId = wicCategoryId;
	}

	/**
	 * Returns the Description
	 *
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the Description
	 *
	 * @param description The Description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the LebSwitch. The LEB switch determines whether or not it is LEB.
	 *
	 * @return LebSwitch
	 */
	public String getLebSwitch() {
		return lebSwitch;
	}

	/**
	 * Sets the LebSwitch
	 *
	 * @param lebSwitch The LebSwitch
	 */
	public void setLebSwitch(String lebSwitch) {
		this.lebSwitch = lebSwitch;
	}
}