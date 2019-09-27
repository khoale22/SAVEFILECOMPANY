/*
 * WicSubCategoryVO
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.webservice.vo;
/**
 * This is WicSubCategoryVO class.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicSubCategoryVO extends BaseVO {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/* The Constructor WicSubCategoryVO */
	public WicSubCategoryVO() {
	}

	/**
	 * Create a new DspWrkQueue with the given parameters.
	 *
	 * @param pWicCatId
	 *            wicCatId
	 * @param pWicSubCatId
	 *            wicSubCatId
	 * @param pWicSubCatDes
	 *            wicSubCatDes
	 * @param pLebSw
	 *            lebSw
	 */
	public WicSubCategoryVO(int pWicCatId, int pWicSubCatId, String pWicSubCatDes, String pLebSw) {
		this.wicCatId = pWicCatId;
		this.wicSubCatId = pWicSubCatId;
		this.wicSubCatDes = pWicSubCatDes;
		this.lebSw = pLebSw;
	}

	/* The private wicCatId. */
	private int wicCatId = -1;
	/* The private wicSubCatId. */
	private int wicSubCatId = -1;
	/* The private wicSubCatDes. */
	private String wicSubCatDes;
	/* The private lebSw. */
	private String lebSw;

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
	 * Sets the wicSubCatId.
	 * 
	 * @param wicSubCatId type {@link int}
	 */
	public void setWicSubCatId(int wicSubCatId) {
		this.wicSubCatId = wicSubCatId;
	}

	/*
	 * Gets the wicSubCatId.
	 * 
	 * @return wicSubCatId
	 */
	public int getWicSubCatId() {
		return this.wicSubCatId;
	}

	/*
	 * Sets the wicSubCatDes.
	 * 
	 * @param wicSubCatDes type {@link String}
	 */
	public void setWicSubCatDes(String wicSubCatDes) {
		this.wicSubCatDes = wicSubCatDes;
	}

	/*
	 * Gets the wicSubCatDes.
	 * 
	 * @return wicSubCatDes
	 */
	public String getWicSubCatDes() {
		return this.wicSubCatDes;
	}

	/*
	 * Sets the lebSw.
	 * 
	 * @param lebSw type {@link String}
	 */
	public void setLebSw(String lebSw) {
		this.lebSw = lebSw;
	}

	/*
	 * Gets the lebSw.
	 * 
	 * @return lebSw
	 */
	public String getLebSw() {
		return this.lebSw;
	}

	@Override
	public String toString() {
		return "WicSubCategoryVO{" + "wicCatId='" + this.wicCatId + '\'' +

		",wicSubCatId='" + this.wicSubCatId + '\'' + ",wicSubCatDes='"
				+ this.wicSubCatDes + '\'' + ",lebSw='" + this.lebSw + '\'' +

				'}';
	}

}