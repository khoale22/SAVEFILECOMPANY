/*
 * VendorReader
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Vendor;
import com.heb.pm.repository.VendorRepository;
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
 * Reads vendors from VendorService.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorReader implements ItemReader<Vendor>, StepExecutionListener {

	@Autowired
	private VendorRepository vendorRepository;

	private Iterator<Vendor> vendorIterator;

	/**
	 * Reads a Vendor from Vendor Service.
	 *
	 * @return A vendor read from Vendor Service.
	 *
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	@Override
	public Vendor read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (this.vendorIterator.hasNext()) {
			return this.vendorIterator.next();
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
		// Call the vendor service as this step starts so that read can just go through the already
		// created list.
		this.vendorIterator = this.vendorRepository.findAll().iterator();
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
	 * Sets the VendorRepository for this class to read from. This is used for testing.
	 *
	 * @param vendorRepository The VendorRepository for this class to read from.
	 */
	public void setVendorRepository(VendorRepository vendorRepository) {
		this.vendorRepository = vendorRepository;
	}
}
