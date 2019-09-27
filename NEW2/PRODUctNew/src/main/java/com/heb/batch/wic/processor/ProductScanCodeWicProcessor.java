/*
 * ProductScanCodeWicProcessor
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.processor;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.index.ProductScanCodeWicDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Converts ProductScanCodeWic to ProductScanCodeWicDocument.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class ProductScanCodeWicProcessor implements ItemProcessor<ProductScanCodeWic, ProductScanCodeWicDocument> {
	/**
	 * Called by the Spring Batch framework. It will wrap a ProductScanCodeWic in a ProductScanCodeWicDocument and return it.
	 *
	 * @param cc The ProductScanCodeWic to wrap.
	 * @return The wrapped ProductScanCodeWic.
	 * @throws Exception
	 */
	@Override
	public ProductScanCodeWicDocument process(ProductScanCodeWic cc) throws Exception {
		ProductScanCodeWicDocument productScanCodeWicDocument = new ProductScanCodeWicDocument();
		productScanCodeWicDocument.setFields(cc);
		return productScanCodeWicDocument;
	}
}