/*
 * CommodityDocument
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.index.DocumentWrapper;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Wraps a ClassCommodity object for storage in an index. This class is meant to hold just commodities and
 * not classes. Do not add classes (pd_om_com_cd = 0) to this index as there can be only on entry per commodity number.
 * It will not fail, the results will be unpredictable, but likely bad.
 *
 * @author d116773
 * @since 2.0.2
 */
@Document(indexName="omi-commodity", type="classCommodity")
public class CommodityDocument extends DocumentWrapper<ClassCommodity, String> {

	/**
	 * Constructs a new, empty CommodityDocument.
	 */
	public CommodityDocument() {}

	/**
	 * Constructs a new CommodityDocument.
	 *
	 * @param cc The ClassCommodity for this document to wrap.
	 */
	public CommodityDocument(ClassCommodity cc) {
		super(cc);
	}

	/**
	 * Returns a suitable key for the index for a given Commodity. Since this index is assumed to hold
	 * commodities, it returns the commodities's ID as a string.
	 *
	 * @param data The data this object will store.
	 * @return A key for the ClassCommodity.
	 */
	@Override
	protected String toKey(ClassCommodity data) {
		if (data == null || data.getKey() == null) {
			return "";
		}
		return Integer.toString(data.getKey().getCommodityCode());
	}
}
