/*
 * SubDepartmentWriter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.productHierarchy.SubDepartmentDocument;
import com.heb.pm.repository.SubDepartmentIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for the SubDepartment index batch job.
 *
 * @author d116773
 * @since 2.0.2
 */
public class SubDepartmentWriter implements ItemWriter<SubDepartmentDocument> {

	public static final Logger logger = LoggerFactory.getLogger(SubDepartmentWriter.class);

	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing %s sub-departments starting with %s.";

	@Autowired
	private SubDepartmentIndexRepository indexRepository;

	/**
	 * Writes SubDepartmentDocuments to the index.
	 *
	 * @param items The items to write.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends SubDepartmentDocument> items) throws Exception {

		if (items == null || items.isEmpty()) {
			SubDepartmentWriter.logger.debug(SubDepartmentWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		if (SubDepartmentWriter.logger.isDebugEnabled()) {
			SubDepartmentWriter.logger.debug(String.format(SubDepartmentWriter.LOG_MESSAGE,
					items.size(), items.get(0)));
		}

		this.indexRepository.save(items);
	}

	/**
	 * Sets the SubDepartmentIndexRepository for this object. THis is mainly for testing.
	 *
	 * @param indexRepository The SubDepartmentIndexRepository for this object.
	 */
	public void setIndexRepository(SubDepartmentIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
