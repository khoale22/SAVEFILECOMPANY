/*
 * SubDepartmentDocument
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.SubDepartment;
import com.heb.pm.index.DocumentWrapper;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * Wrapps a SubDepartment object for storage in an index.
 *
 * @author d116773
 * @since 2.0.2
 */
@Document(indexName="sub-departments", type="sub-department")
public class SubDepartmentDocument extends DocumentWrapper<SubDepartment, String> {

	/**
	 * Constructs a new SubDepartmentDocument.
	 */
	public SubDepartmentDocument() {}

	/**
	 * Constructs a new SubDepartmentDocument.
	 *
	 * @param s The SubDepartment to wrap.
	 */
	public SubDepartmentDocument(SubDepartment s) { super(s); }

	/**
	 * Returns a key for the document.
	 *
	 * @param data The data this object will store.
	 * @return A key for the document based on its data.
	 */
	@Override
	protected String toKey(SubDepartment data) {
		return data == null ? "" : data.getNormalizedId();
	}
}
