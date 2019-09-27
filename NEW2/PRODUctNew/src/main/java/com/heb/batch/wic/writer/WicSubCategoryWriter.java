/*
 * WicSubCategoryWriter
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.writer;
import com.heb.batch.wic.index.WicSubCategoryDocument;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Persists WicSubCategoryDocument to an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicSubCategoryWriter implements ItemWriter<WicSubCategoryDocument> {

	private static final Logger LOGGER = LogManager.getLogger(WicSubCategoryWriter.class);

	private static final String LOG_MESSAGE = "Writing %d WicSubCategoryDocument starting with upc %s";

	@Autowired
	private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;

	/**
	 * Called by the Spring Batch framework to save wicSubCategoryDocuments to the index.
	 *
	 * @param wicSubCategoryDocuments The list of wicSubCategoryDocuments to save.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WicSubCategoryDocument> wicSubCategoryDocuments) throws Exception {
		if(wicSubCategoryDocuments !=null && !wicSubCategoryDocuments.isEmpty()) {
			WicSubCategoryWriter.LOGGER.debug(String.format(WicSubCategoryWriter.LOG_MESSAGE, wicSubCategoryDocuments.size(),
					wicSubCategoryDocuments.get(0).getWicSubCategoryId()));
			this.wicSubCategoryIndexRepository.saveAll(wicSubCategoryDocuments);
		}
	}

}
