/*
 * WicCategoryVO
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.vo;
/**
 * This is WicCategoryVO class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicCategoryVO extends BaseVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/* The Constructor WicCategoryVO */
	public WicCategoryVO() {
	}

	/**
	 * Create a new DspWrkQueue with the given parameters.
	 *
	 * @param pWicCatId
	 *            wicCatId
	 * @param pWicCatDes
	 *            wicCatDes
	 */
	public WicCategoryVO(int pWicCatId, String pWicCatDes) {
		this.wicCatId = pWicCatId;
		this.wicCatDes = pWicCatDes;
	}

	/* The private wicCatId. */
	private int wicCatId=-1;
	/* The private wicCatDes. */
	private String wicCatDes;

	/*
	 * Sets the wicCatId.
	 * 
	 * @param wicCatId type {@link int}
	 */
	public void setWicCatId(int wicCatId) {
		this.wicCatId = wicCatId;
	}

	/*
	 * Gets the wicCatId.
	 * 
	 * @return wicCatId
	 */
	public int getWicCatId() {
		return this.wicCatId;
	}

	/*
	 * Sets the wicCatDes.
	 * 
	 * @param wicCatDes type {@link String}
	 */
	public void setWicCatDes(String wicCatDes) {
		this.wicCatDes = wicCatDes;
	}

	/*
	 * Gets the wicCatDes.
	 * 
	 * @return wicCatDes
	 */
	public String getWicCatDes() {
		return this.wicCatDes;
	}

	@Override
	public String toString() {
		return "WicCategoryVO{" + "wicCatId='" + this.wicCatId + '\'' +

		",wicCatDes='" + this.wicCatDes + '\'' +

		'}';
	}

}