/*
 * CommodityWriter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.CoreTransactional;
import com.heb.pm.productHierarchy.CommodityDocument;
import com.heb.pm.repository.CommodityIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Persists CommodityDocuments to an index.
 *
 * @author d116773
 * @since 2.0.2
 */
public class CommodityWriter implements ItemWriter<CommodityDocument> {

	private static final Logger logger = LoggerFactory.getLogger(CommodityWriter.class);

	private static final String LOG_MESSAGE = "Writing %d CommodityDocuments starting with commodity %s";

	@Autowired
	private CommodityIndexRepository indexRepository;

	/**
	 * Called by the Spring Batch framework to save CommodityDocuments to the index.
	 *
	 * @param commodityDocuments The list of CommodityDocuments to save.
	 * @throws Exception
	 */
	@Override
	@CoreTransactional
	public void write(List<? extends CommodityDocument> commodityDocuments) throws Exception {

		CommodityWriter.logger.debug(String.format(CommodityWriter.LOG_MESSAGE, commodityDocuments.size(),
				commodityDocuments.get(0).getData().getKey()));

		this.indexRepository.save(commodityDocuments);
	}

	/**
	 * Sets the CommodityIndexRepository for this object to use. This is mainly for testing.
	 *
	 * @param indexRepository The CommodityIndexRepository for this object to use.
	 */
	public void setIndexRepository(CommodityIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}
}
