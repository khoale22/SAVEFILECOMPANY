/*
 * PrimoPickBatchUpload
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.batchUpload.primoPick;

import com.heb.pm.batchUpload.BatchUpload;

import java.util.*;

/**
 * The object include primo pick file info.
 * datas is the value on row
 *
 * @author vn87351
 * @since 2.12.0
 */
public class PrimoPickBatchUpload extends BatchUpload{
	/** The column Constant in file upload. */
	public static final int COL_POS_UPC =0;
	public static final String COL_NM_UPC = "UPC";
	public static final int COL_POS_ITEM_CODE =1;
	public static final String COL_NM_ITEM_CODE = "Item Code";
	public static final int COL_POS_CHANNEL =2;
	public static final String COL_NM_CHANNEL = "Channel";
	public static final int COL_POS_PRODUCT_ID =3;
	public static final String COL_NM_PRODUCT_ID = "Product Id";
	public static final int COL_POS_PRODUCT_DESCRIPTION =4;
	public static final String COL_NM_PRODUCT_DESCRIPTION = "Product Description";
	public static final int COL_POS_SIZE_TEXT =5;
	public static final String COL_NM_SIZE_TEXT = "Size Text";
	public static final int COL_POS_SUB_COMMODITY =6;
	public static final String COL_NM_SUB_COMMODITY = "Sub Commodity";
	public static final int COL_POS_BDM =7;
	public static final String COL_NM_BDM = "BDM";
	public static final int COL_POS_SUB_DEPARTMENT =8;
	public static final String COL_NM_SUB_DEPARTMENT = "Sub Department";
	public static final int COL_POS_PRIMO_PICK =9;
	public static final String COL_NM_PRIMO_PICK = "Primo Pick";
	public static final int COL_POS_PRIMO_PICK_LONG_DESCRIPTION =10;
	public static final String COL_NM_PRIMO_PICK_LONG_DESCRIPTION = "Primo Pick Long Description";
	public static final int COL_POS_PRIMO_PICK_SHORT_DESCRIPTION =11;
	public static final String COL_NM_PRIMO_PICK_SHORT_DESCRIPTION = "Primo Pick Short Description";
	public static final int COL_POS_PRIMO_PICK_STATUS =12;
	public static final String COL_NM_PRIMO_PICK_STATUS = "Primo Pick Status";
	public static final int COL_POS_PRIMO_PICK_START_DATE =13;
	public static final String COL_NM_PRIMO_PICK_START_DATE = "Primo Pick Start Date";
	public static final int COL_POS_PRIMO_PICK_END_DATE =14;
	public static final String COL_NM_PRIMO_PICK_END_DATE = "Primo Pick End Date";
	public static final String PRIMO_PICK_YES = "Y";
	public static final String PRIMO_PICK_NO = "N";
	public static final String PRIMO_PICK_STATUS_REJECT  = "R";
	public static final Map<Integer,String> uploadFileHeader;
	static {
		uploadFileHeader = new HashMap<>();
		uploadFileHeader.put(COL_POS_UPC, COL_NM_UPC);
		uploadFileHeader.put(COL_POS_ITEM_CODE, COL_NM_ITEM_CODE);
		uploadFileHeader.put(COL_POS_CHANNEL, COL_NM_CHANNEL);
		uploadFileHeader.put(COL_POS_PRODUCT_ID, COL_NM_PRODUCT_ID);
		uploadFileHeader.put(COL_POS_PRODUCT_DESCRIPTION, COL_NM_PRODUCT_DESCRIPTION);
		uploadFileHeader.put(COL_POS_SIZE_TEXT, COL_NM_SIZE_TEXT);
		uploadFileHeader.put(COL_POS_SUB_COMMODITY, COL_NM_SUB_COMMODITY);
		uploadFileHeader.put(COL_POS_BDM, COL_NM_BDM);
		uploadFileHeader.put(COL_POS_SUB_DEPARTMENT, COL_NM_SUB_DEPARTMENT);
		uploadFileHeader.put(COL_POS_PRIMO_PICK, COL_NM_PRIMO_PICK);
		uploadFileHeader.put(COL_POS_PRIMO_PICK_LONG_DESCRIPTION, COL_NM_PRIMO_PICK_LONG_DESCRIPTION);
		uploadFileHeader.put(COL_POS_PRIMO_PICK_SHORT_DESCRIPTION, COL_NM_PRIMO_PICK_SHORT_DESCRIPTION);
		uploadFileHeader.put(COL_POS_PRIMO_PICK_STATUS, COL_NM_PRIMO_PICK_STATUS);
		uploadFileHeader.put(COL_POS_PRIMO_PICK_START_DATE, COL_NM_PRIMO_PICK_START_DATE);
		uploadFileHeader.put(COL_POS_PRIMO_PICK_END_DATE, COL_NM_PRIMO_PICK_END_DATE);
	}

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private String upc;
	private String productId;
	private String productDescription;
	private String primoPick;
	private String primoPickLongDescription;
	private String primoPickShortDescription;
	private String primoPickStartDate;
	private String primoPickEndDate;
	private String primoPickStatus;
	private int psWorkId;

	/**
	 * @return the upc
	 */
	public String getUpc() {
		return this.upc;
	}

	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return this.productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return this.productDescription;
	}

	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * @return the primoPickLongDescription
	 */
	public String getPrimoPickLongDescription() {
		return this.primoPickLongDescription;
	}

	/**
	 * @param primoPickLongDescription the primoPickLongDescription to set
	 */
	public void setPrimoPickLongDescription(String primoPickLongDescription) {
		this.primoPickLongDescription = primoPickLongDescription;
	}

	/**
	 * @return the primoPickShortDescription
	 */
	public String getPrimoPickShortDescription() {
		return this.primoPickShortDescription;
	}

	/**
	 * @param primoPickShortDescription the primoPickShortDescription to set
	 */
	public void setPrimoPickShortDescription(String primoPickShortDescription) {
		this.primoPickShortDescription = primoPickShortDescription;
	}

	/**
	 * @return the primoPickStartDate
	 */
	public String getPrimoPickStartDate() {
		return primoPickStartDate;
	}

	/**
	 * @param primoPickStartDate the primoPickStartDate to set
	 */
	public void setPrimoPickStartDate(String primoPickStartDate) {
		this.primoPickStartDate = primoPickStartDate;
	}

	/**
	 * @return the primoPickEndDate
	 */
	public String getPrimoPickEndDate() {
		return primoPickEndDate;
	}

	/**
	 * @param primoPickEndDate the primoPickEndDate to set
	 */
	public void setPrimoPickEndDate(String primoPickEndDate) {
		this.primoPickEndDate = primoPickEndDate;
	}

	/**
	 * @return the primoPickStatus
	 */
	public String getPrimoPickStatus() {
		return this.primoPickStatus;
	}

	/**
	 * @param primoPickStatus the primoPickStatus to set
	 */
	public void setPrimoPickStatus(String primoPickStatus) {
		this.primoPickStatus = primoPickStatus;
	}

	/**
	 * @return the primoPick
	 */
	public String getPrimoPick() {
		return this.primoPick;
	}

	/**
	 * @param primoPick the primoPick to set
	 */
	public void setPrimoPick(String primoPick) {
		this.primoPick = primoPick;
	}

	/**
	 * @return the psWorkId
	 */
	public int getPsWorkId() {
		return this.psWorkId;
	}

	/**
	 * @param psWorkId the psWorkId to set
	 */
	public void setPsWorkId(int psWorkId) {
		this.psWorkId = psWorkId;
	}
}

