/*
 * StoreReader
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Store;
import com.heb.pm.repository.StoreRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;


/**
 * Reads stores from StoreService.
 *
 * @author d116773
 * @since 2.0.2
 */
public class StoreReader implements ItemReader<Store>, StepExecutionListener {

	@Autowired
	private StoreRepository storeRepository;

	private Iterator<Store> storeIterator;

	/**
	 * Called by Spring Batch to return the next Store.
	 *
	 * @return A store read from StoreService.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public Store read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (this.storeIterator.hasNext()) {
			return this.storeIterator.next();
		} else {
			return null;
		}
	}

	/**
	 * Called before the step starts. It will initialize the iterator that read returns data from.
	 *
	 * @param stepExecution SpringBatch information relating to this step.
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Call the store service as this step starts so that read can just go through the already
		// created list.
		this.storeIterator = this.storeRepository.findAll().iterator();
	}

	/**
	 * Unused.
	 *
	 * @param stepExecution Unused.
	 * @return Always returns null.
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}

	/**
	 * Sets the StoreRepository for this class to read from. This is used for testing.
	 *
	 * @param storeRepository The VendorRepository for this class to read from.
	 */
	public void setStoreRepository(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
}
