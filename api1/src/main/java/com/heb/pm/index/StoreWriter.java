/*
 * StoreWriter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Store;
import com.heb.pm.repository.StoreIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writes StoreDocuments to the store index.
 *
 * @author d116773
 * @since 2.0.2
 */
public class StoreWriter implements ItemWriter<Store> {

	private static final Logger logger = LoggerFactory.getLogger(StoreWriter.class);

	private static final String EMPTY_LIST_LOGGER_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing %s stores starting with %s.";

	@Autowired
	private StoreIndexRepository indexRepository;

	@Override
	public void write(List<? extends Store> stores) throws Exception {

		if (stores == null || stores.isEmpty()) {
			StoreWriter.logger.error(StoreWriter.EMPTY_LIST_LOGGER_MESSAGE);
			return;
		}

		StoreWriter.logger.debug(String.format(StoreWriter.LOG_MESSAGE, stores.size(),
				stores.get(0).getStoreNumber()));

		this.indexRepository.save(stores);
	}

	/**
	 * Sets the StoreIndexRepository for the object. This is used for testing.
	 *
	 * @param indexRepository The StoreIndexRepository for the object.
	 */
	public void setIndexRepository(StoreIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
