/*
 * VendorService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.pm.entity.Vendor;
import com.heb.pm.repository.VendorIndexRepository;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Maintains all business logic related to vendors.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
public class VendorService {

	private static final Logger logger = LoggerFactory.getLogger(VendorService.class);

	private static final String VENDOR_NAME_REGULAR_EXPRESSION = ".*%s.*";
	private static final String VENDOR_SEARCH_LOG_MESSAGE = "searching for vendor by the regular expression '%s'";

	@Autowired
	private VendorIndexRepository indexRepository;

	/**
	 * Searches for a list of vendors by name. This is a wildcard search, meaning that anything partially matching
	 * the text passed in will be returned.
	 *
	 * @param searchString The text to search for vendors by.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with vendors matching the search criteria.
	 */
	public PageableResult<Vendor> findByRegularExpression(String searchString, int page, int pageSize) {
		String regexString = String.format(VendorService.VENDOR_NAME_REGULAR_EXPRESSION, searchString.toLowerCase());
		PageRequest request = new PageRequest(page, pageSize, Vendor.getSortByVendorNumber());
		VendorService.logger.debug(String.format(VendorService.VENDOR_SEARCH_LOG_MESSAGE, regexString));
		Page<Vendor> vp =  this.indexRepository.findByRegularExpression(regexString, request);
		return new PageableResult<>(page, vp.getTotalPages(), vp.getTotalElements(), vp.getContent());
	}

	/**
	 * Returns a vendor by AP number.
	 *
	 * @param vendorNumber The AP number to search for.
	 * @return The vendor with that AP number.
	 */
	public Vendor findByVendorNumber(Integer vendorNumber) {
		return this.indexRepository.findOne(vendorNumber);
	}

	/**
	 * Sets the repository the service uses to get data from the vendor index.
	 *
	 * @param indexRepository The repository the service uses to get data from the vendor index.
	 */
	public void setIndexRepository(VendorIndexRepository indexRepository) {
		this.indexRepository = indexRepository;
	}

}
