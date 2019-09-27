/*
 * WicCategoryWriter
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.writer;
import com.heb.batch.wic.index.WicCategoryDocument;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Persists WicCategoryDocument to an index.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicCategoryWriter implements ItemWriter<WicCategoryDocument> {

	private static final Logger LOGGER = LogManager.getLogger(WicCategoryWriter.class);

	private static final String LOG_MESSAGE = "Writing %d WicCategoryDocument starting with upc %s";

	@Autowired
	private WicCategoryIndexRepository wicCategoryIndexRepository;

	/**
	 * Called by the Spring Batch framework to save wicCategoryDocuments to the index.
	 *
	 * @param wicCategoryDocuments The list of wicCategoryDocuments to save.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends WicCategoryDocument> wicCategoryDocuments) throws Exception {
		if(wicCategoryDocuments !=null && !wicCategoryDocuments.isEmpty()) {
			WicCategoryWriter.LOGGER.debug(String.format(WicCategoryWriter.LOG_MESSAGE, wicCategoryDocuments.size(),
					wicCategoryDocuments.get(0).getWicCatId()));
			this.wicCategoryIndexRepository.saveAll(wicCategoryDocuments);
		}
	}

}
