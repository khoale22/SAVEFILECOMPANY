/*
 * ProductScanCodeWicReader
 *
 *  Copyright (c) 2018 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of H-E-B.
 */

package com.heb.batch.wic.reader;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.index.repository.ProductScanCodeWicIndexRepository;
import com.heb.batch.wic.repository.ProductScanCodeWicRepository;
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
 * Reads ProductScanCodeWic from the database.
 *
 * @author vn55306
 * @since 1.0.0
 */
public class ProductScanCodeWicReader implements ItemReader<ProductScanCodeWic>, StepExecutionListener {
	private static final Logger LOGGER = LogManager.getLogger(ProductScanCodeWicReader.class);
	@Autowired
	private ProductScanCodeWicRepository productScanCodeWicRepository;
	@Autowired
	private ProductScanCodeWicIndexRepository productScanCodeWicIndexRepository;
	private Iterator<ProductScanCodeWic> data;
	private int pageSize = 1000;
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
	public ProductScanCodeWic read() throws Exception {
		// If there is still data, return it.
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}
		// If not, see if you can fetch another set.
		Page<ProductScanCodeWic> page =
				this.productScanCodeWicRepository.findAll(PageRequest.of(this.currentPage++, this.pageSize));
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
		LOGGER.info("Start J50X100D-SYS-PROD-SCAN-CODE-WIC-STEP-6");
		this.currentPage = 0;
		try {
			this.productScanCodeWicIndexRepository.refresh();
			this.productScanCodeWicIndexRepository.deleteAll();

		}catch (Exception e){
			LOGGER.error("ProductScanCodeWic Exception ="+e.getMessage());
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
