/*
 * CommodityProcessor
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.productHierarchy.CommodityDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Converts ClassCommodities to CommodityDocuments.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityProcessor implements ItemProcessor<ClassCommodity, CommodityDocument> {

	/**
	 * Called by the Spring Batch framework. It will wrap a ClassCommodity in a CommodityDocument and return it.
	 *
	 * @param cc The ClassCommodity to wrap.
	 * @return The wrapped ClassCommodity.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public CommodityDocument process(ClassCommodity cc) throws Exception {
		return cc == null ? null : new CommodityDocument(cc);
	}
}
