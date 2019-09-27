/*
 * WicCategoryDocument
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
 * Wraps a WicCategory for storage in an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
@Document(indexName = "wic-categories")
public class WicCategoryDocument {
	@Id
	private String wicCatId;
	@Field(type = FieldType.Text)
	private String description;

	/**
	 * Returns the Wic Category Id
	 *
	 * @return wicCatId
	 */
	public String getWicCatId() {
		return wicCatId;
	}

	/**
	 * Sets the Wic Category Id
	 *
	 * @param wicCatId The Wic Category Id
	 */
	public void setWicCatId(String wicCatId) {
		this.wicCatId = wicCatId;
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
}