/*
 * DeleteScanCodesWICAndUpdateScanCodesReader class.
 *
 * Copyright (c) 2018 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of H-E-B.
 */
package com.heb.batch.wic.reader;

import com.heb.batch.wic.entity.ProductScanCodeWic;
import com.heb.batch.wic.repository.ProductScanCodeWicRepository;
import com.heb.batch.wic.utils.WicConstants;
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
 * Returns ProductScanCodeDocument in ProductScanCodesIndexRepository
 *
 * @author vn03500
 * @since 1.0.0
 */
public class DeleteScanCodesWICAndUpdateScanCodesReader implements ItemReader<ProductScanCodeWic>, StepExecutionListener {
	private static final Logger LOGGER = LogManager.getLogger(DeleteScanCodesWICAndUpdateScanCodesReader.class);
	@Autowired
	private ProductScanCodeWicRepository productScanCodeWicRepository;
	private Iterator<ProductScanCodeWic> data;
	private int pageSize = 500;
	private int currentPage = 0;

	/**
	 * Called by Spring Batch to return the next ProductScanCodeWicDocument in the list.
	 *
	 * @return The next ProductScanCodeWicDocument in the list. Null when there is no more data.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public ProductScanCodeWic read() throws Exception {
		// Read the data until we have next
		if (this.data != null && this.data.hasNext()) {
			return this.data.next();
		}
		try {
			// If not, see if you can fetch another set.
			Page<ProductScanCodeWic> page = this.productScanCodeWicRepository
					.findByCre8UId(WicConstants.TXSTATE_USER, PageRequest.of(this.currentPage++, this.pageSize));
			// If there was results, return the next one.
			if (page.hasContent()) {
				this.data = page.iterator();
				return data.next();
			}
		}catch (Exception e){
			LOGGER.error("DeleteScanCodesWICAndUpdateScanCodesReader Exception ="+e.getMessage());
			return null;
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
		LOGGER.info("Start J50X100D-SYS-PROD-SCAN-CODE-STEP-8");
		this.currentPage = 0;
		this.data = null;
	}

	/**
	 * Unimplemented.
	 *
	 * @param stepExecution Ignored.
	 * @return null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	/**o
	 * Sets the size of the page to data in.
	 *
	 * @param pageSize The size of the page to read data in.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
