/*
 * com.heb.util.jpa.PageableResult
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.util.jpa;

import java.io.Serializable;

/**
 * Class to store page results from window queries. The application may or may not return counts with
 * a result set, so this class allows for controllers to return a consistent object. It may only have
 * the data property set or it may have data, pageCount, and recordCount. The property complete will
 * be true if the counts are set and false if they are not.
 *
 * @param <DataType> The type of data that will be passed back to the client.
 * @author d116773
 */
public class PageableResult<DataType extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page;
	private Integer pageCount;
	private Long recordCount;
	private Iterable<DataType> data;
	private boolean complete;

	/**
	 * Creates a PageableResult with all properties set.
	 *
	 * @param page The current page this dataset represents.
	 * @param pageCount The total number of pages that are available based on the previous query parameters.
	 * @param recordCount The total number of records that are available based on the parameters
	 *                    used to generate this data set.
	 * @param data The data for the current page.
	 */
	public PageableResult(int page, Integer pageCount, Long recordCount, Iterable<DataType> data) {
		this.page = page;
		this.pageCount = pageCount;
		this.recordCount = recordCount;
		this.data = data;
		this.complete = true;
	}

	/**
	 * Creates a PageableResult with only the page and data set.
	 *
	 * @param page The current page this dataset represents.
	 * @param data The data for the current page.
	 */
	public PageableResult(int page, Iterable<DataType> data) {
		this.page = page;
		this.data = data;
		this.complete = false;
	}

	/**
	 * Returns the data for the current page.
	 *
	 * @return The data for the current page.
	 */
	public Iterable<DataType> getData() {
		return data;
	}

	/**
	 * Returns the total number of pages available based on the parameters
	 * used to generate this data set.
	 *
	 * @return The total number of pages available based on the parameters
	 * used to generate this data set.
	 */
	public Integer getPageCount() {
		return this.pageCount;
	}

	/**
	 * Returns the total number of records available based on the parameters
	 * used to generate this data set.
	 *
	 * @return The total number of records available based on the parameters
	 * used to generate this data set.
	 */
	public Long getRecordCount() {
		return this.recordCount;
	}

	/**
	 * Returns true if this object has pageCount and recordCount parameters set.
	 *
	 * @return True if this object has pageCount and recordCount parameters set and false
	 * otherwise.
	 */
	public Boolean isComplete() {
		return this.complete;
	}

	/**
	 * Returns the page number this data set represents.
	 *
	 * @return The page number this data set represents.
	 */
	public int getPage() {
		return this.page;
	}
}
