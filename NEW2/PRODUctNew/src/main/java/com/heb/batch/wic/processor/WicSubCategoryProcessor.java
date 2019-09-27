/*
 * WicSubCategoryProcessor
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.processor;

import com.heb.batch.wic.entity.WicSubCategory;
import com.heb.batch.wic.entity.WicSubCategoryKey;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Converts a WicSubCategory to a WicSubCategoryDocument.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicSubCategoryProcessor implements ItemProcessor<WicSubCategory, WicSubCategoryDocument> {

	/**
	 * Called by the Spring Batch framework. It will wrap a WicSubCategory in a WicSubCategoryDocument and return it.
	 *
	 * @param cc The WicSubCategory to wrap.
	 * @return The wrapped WicSubCategory.
	 * @throws Exception
	 */
	@Override
	public WicSubCategoryDocument process(WicSubCategory cc) throws Exception {
		WicSubCategoryDocument wicSubCategoryDocument = new WicSubCategoryDocument();
		WicSubCategoryKey key = cc.getKey();
		if (key != null) {
			wicSubCategoryDocument.setId(key.getWicCategoryid(), key.getWicSubCategoryId());
			wicSubCategoryDocument.setWicCategoryId(key.getWicCategoryid());
			wicSubCategoryDocument.setWicSubCategoryId(key.getWicSubCategoryId());
		}
		wicSubCategoryDocument.setDescription(cc.getDescription());
		wicSubCategoryDocument.setLebSwitch(cc.getLebSwitch());
		return wicSubCategoryDocument;
	}
}
