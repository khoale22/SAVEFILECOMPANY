/*
 * SubDepartmentProcessor
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.productHierarchy.SubDepartmentDocument;
import org.springframework.batch.item.ItemProcessor;

/**
 * Processor for the SubDepartment index batch job.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentProcessor implements ItemProcessor<SubDepartment, SubDepartmentDocument> {

	/**
	 * Converts a SubDepartment to a SubDepartmentDocument.
	 *
	 * @param subDepartment The SubDepartment to process.
	 * @return subDepartment converted to a SubDepartmentDocument.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public SubDepartmentDocument process(SubDepartment subDepartment) throws Exception {
		return subDepartment == null ? null : new SubDepartmentDocument(subDepartment);
	}
}
