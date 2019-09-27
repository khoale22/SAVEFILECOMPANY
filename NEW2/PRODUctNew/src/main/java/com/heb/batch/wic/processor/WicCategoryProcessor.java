/*
 * WicCategoryProcessor
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.processor;

import com.heb.batch.wic.entity.WicCategory;
import com.heb.batch.wic.index.WicCategoryDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Converts a WicCategory to a WicCategoryDocument.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicCategoryProcessor implements ItemProcessor<WicCategory, WicCategoryDocument> {

	/**
	 * Called by the Spring Batch framework. It will wrap WicCategory in a WicCategoryDocument and return it.
	 *
	 * @param cc The WicCategory to wrap.
	 * @return The wrapped WicCategory.
	 * @throws Exception
	 */
	@Override
	public WicCategoryDocument process(WicCategory cc) throws Exception {
		WicCategoryDocument wicCategoryDocument = new WicCategoryDocument();
		wicCategoryDocument.setWicCatId(String.valueOf(cc.getId()));
		wicCategoryDocument.setDescription(cc.getDescription());
		return wicCategoryDocument;
	}
}
