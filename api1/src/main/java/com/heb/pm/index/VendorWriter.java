/*
 * VendorWriter
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.index;

import com.heb.pm.entity.Vendor;
import com.heb.pm.repository.VendorIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Writes Vendors to the vendor index.
 *
 * @author d116773
 * @since 2.0.2
 */
public class VendorWriter implements ItemWriter<Vendor> {

	private static final Logger logger = LoggerFactory.getLogger(VendorWriter.class);

	private static final String EMPTY_LIST_LOG_MESSAGE = "Called write with null or empty list.";
	private static final String LOG_MESSAGE = "Writing vendors beginning with number %d";

	@Autowired
	private VendorIndexRepository vendorIndexRepository;

	/**
	 * Called by the Spring Batch framework. It will write vendors to the vendor index.
	 *
	 * @param vendors The list of vendors to write.
	 * @throws Exception
	 */
	@Override
	public void write(List<? extends Vendor> vendors) throws Exception {

		if (vendors == null || vendors.isEmpty()) {
			VendorWriter.logger.debug(VendorWriter.EMPTY_LIST_LOG_MESSAGE);
			return;
		}

		if (VendorWriter.logger.isDebugEnabled()) {
			VendorWriter.logger.debug(String.format(VendorWriter.LOG_MESSAGE, vendors.get(0).getVendorNumber()));
		}

		vendorIndexRepository.save(vendors);
	}

	/**
	 * Sets the object's VendorIndexRepository to write to. This is primarily used for testing.
	 *
	 * @param vendorIndexRepository The object's VendorIndexRepository to write to.
	 */
	public void setVendorIndexRepository(VendorIndexRepository vendorIndexRepository) {
		this.vendorIndexRepository = vendorIndexRepository;
	}
}
