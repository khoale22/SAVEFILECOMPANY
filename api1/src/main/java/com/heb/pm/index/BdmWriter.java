/*
 * BdmWriter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.productHierarchy.BdmDocument;
import com.heb.pm.repository.BdmIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writer for the Bdm index batch job.
 *
 * @author m314029
 * @since
 */
public class BdmWriter implements ItemWriter<BdmDocument> {

	private static final Logger logger = LoggerFactory.getLogger(BdmWriter.class);

	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing %s bdms starting with %s.";

	@Autowired
	private BdmIndexRepository indexRepository;

	/**
	 * Called by the Spring batch framework to write the bdms to the index.
	 *
	 * @param items The list of bdms to write.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends BdmDocument> items) throws Exception {

		if (items == null || items.isEmpty()) {
			BdmWriter.logger.info(BdmWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		BdmWriter.logger.debug(String.format(BdmWriter.LOG_MESSAGE, items.size(),
				items.get(0).getKey()));

		this.indexRepository.save(items);

	}

	/**
	 * Sets the BdmIndexRepository for this object to use. This is mainly for testing.
	 *
	 * @param indexRepository The BdmIndexRepository for this object to use.
	 */
	public void setIndexRepository(BdmIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
