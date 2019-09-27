/*
 * WicSubCategoryReader
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.reader;

import com.heb.batch.wic.entity.WicSubCategory;
import com.heb.batch.wic.index.repository.WicSubCategoryIndexRepository;
import com.heb.batch.wic.repository.WicSubCategoryRepository;
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
 * Reads WicSubCategory from the database.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class WicSubCategoryReader implements ItemReader<WicSubCategory>, StepExecutionListener {
	private static final Logger LOGGER = LogManager.getLogger(WicSubCategoryReader.class);
	@Autowired
	private WicSubCategoryRepository wicSubCategoryRepository;
	@Autowired
	private WicSubCategoryIndexRepository wicSubCategoryIndexRepository;
	private Iterator<WicSubCategory> data;
	private int pageSize = 500;
	private int currentPage = 0;

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
	public WicSubCategory read() throws Exception {
		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}
		// If not, see if you can fetch another set.
		Page<WicSubCategory> page =
				this.wicSubCategoryRepository.findAll(PageRequest.of(this.currentPage++, this.pageSize));
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
		LOGGER.info("Start J50X100D-SYS-WIC-SUB-CATEGORY-4");
		this.currentPage = 0;
		try{
			this.wicSubCategoryIndexRepository.refresh();
			this.wicSubCategoryIndexRepository.deleteAll();
			this.wicSubCategoryIndexRepository.count();
		}catch (Exception e){
			LOGGER.error("WicSubCategoryReader Exception ="+e.getMessage());
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
