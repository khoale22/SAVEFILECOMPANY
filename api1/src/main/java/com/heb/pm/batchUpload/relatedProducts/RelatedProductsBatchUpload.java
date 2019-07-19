/*
 *  RelatedProductsBatchUpload
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.batchUpload.relatedProducts;

import com.heb.pm.batchUpload.BatchUpload;

import java.util.ArrayList;
import java.util.List;

/**
 * This is RelatedProductsBatchUpload class.
 *
 * @author vn73545
 * @since 2.20.0
 */
public class RelatedProductsBatchUpload extends BatchUpload {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 3884054421364570290L;
    /** The Constant int POSITION_COLUMN_PRODUCT_ID. */
    public static final int POSITION_COLUMN_PRODUCT_ID = 0;
    /** The Constant STRING_PRODUCT_ID. */
    public static final String STRING_PRODUCT_ID = "PDP Product ID";
    /** The Constant int POSITION_COLUMN_RELATED_PRODUCT_ID. */
    public static final int POSITION_COLUMN_RELATED_PRODUCT_ID = 1;
    /** The Constant STRING_RELATED_PRODUCT_ID. */
    public static final String STRING_RELATED_PRODUCT_ID = "Related Product";
    private String productId;
    private String relatedProductId;
	/**
	 * Get the productId.
	 *
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * Set the productId.
	 *
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * Get the relatedProductId.
	 *
	 * @return the relatedProductId
	 */
	public String getRelatedProductId() {
		return relatedProductId;
	}
	/**
	 * Set the relatedProductId.
	 *
	 * @param relatedProductId the relatedProductId to set
	 */
	public void setRelatedProductId(String relatedProductId) {
		this.relatedProductId = relatedProductId;
	}
}
