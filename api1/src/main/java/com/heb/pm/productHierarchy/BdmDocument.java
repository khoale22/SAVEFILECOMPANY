/*
 * BdmDocument
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.Bdm;
import com.heb.pm.index.DocumentWrapper;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Wraps a Bdm for storage in an index.
 *
 * @author m314029
 * @since 2.0.6
 */
@Document(indexName = "bdm", type = "bdm")
public class BdmDocument extends DocumentWrapper<Bdm, String> {

	/**
	 * Constructs a new BdmDocument.
	 */
	public BdmDocument() {}

	/**
	 * Constructs a new BdmDocument.
	 *
	 * @param bdm The BDM for this document to wrap.
	 */
	public BdmDocument(Bdm bdm) {
		super(bdm);
	}

	@Override
	protected String toKey(Bdm data) {
		return data == null ? "" :  data.getNormalizedId();
	}
}
