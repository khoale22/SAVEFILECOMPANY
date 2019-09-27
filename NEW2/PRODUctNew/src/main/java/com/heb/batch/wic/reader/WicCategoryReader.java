/*
 * WicCategoryReader
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.reader;

import com.heb.batch.wic.entity.WicCategory;
import com.heb.batch.wic.index.repository.WicCategoryIndexRepository;
import com.heb.batch.wic.repository.WicCategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Iterator;

/**
 * Reads WicCategory from the database.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicCategoryReader implements ItemReader<WicCategory>, StepExecutionListener {
	private static final Logger LOGGER = LogManager.getLogger(WicCategoryReader.class);
	@Autowired
	private WicCategoryRepository wicCategoryRepository;
	private Iterator<WicCategory> data;
	private int pageSize = 500;
	private int currentPage = 0;
	@Autowired
	private WicCategoryIndexRepository wicCategoryIndexRepository;
	/**
	 * Called by Spring Batch to return the next ProductScanCodes in the list.
	 *
	 * @return The next ProductScanCodes in the list. Null when there is no more data.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public WicCategory read() throws Exception {
		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}
		// If not, see if you can fetch another set.
		Page<WicCategory> page =
				this.wicCategoryRepository.findAll(PageRequest.of(this.currentPage++, this.pageSize));
		// If there was results, return the next one.
		if (page.hasContent()) {
			this.data = page.iterator();
			return data.next();
		}
		// If not, we're at the end of the data.
		return null;
	}

	/**
	 * Sets up the data to be returned.
	 *
	 * @param stepExecution The environment this step is going to run in.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Start J50X100D-SYS-WIC-CATEGORY-3");
		this.currentPage = 0;
		try {
			this.wicCategoryIndexRepository.refresh();
			this.wicCategoryIndexRepository.deleteAll();
			this.wicCategoryIndexRepository.count();
		}catch (Exception e){
			LOGGER.error("TexasStateReader Exception ="+e.getMessage());
		}
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return Always reutrns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}


	/**
	 * Sets the size of the page to data in.
	 *
	 * @param pageSize The size of the page to read data in.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
