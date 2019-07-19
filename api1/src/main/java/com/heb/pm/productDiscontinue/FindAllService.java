/*
 * FindAllService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.entity.ProductDiscontinueKey;
import com.heb.pm.repository.ProductDiscontinueRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * This class can be delegated to for all searches for all records of prod_del.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
public class FindAllService {

	@Autowired
	private ProductDiscontinueRepositoryWithCount productDiscontinueRepositoryWithCount;

	/**
	 * Fetches a window of all the product discontinue records.
	 *
	 * @param page Which page to fetch.
	 * @param pageSize How big of a page to fetch.
	 * @return An iterable list of product discontinue records.
	 */
	public PageableResult<ProductDiscontinue> findAll(int page, int pageSize) {

		Pageable allRecordsRequest = new PageRequest(page, pageSize,
				ProductDiscontinueKey.getDefaultSort());

		// Unfortunately, the built in findAll does not offer an option to return without counts. If we ever need it,
		// we can add it.
		Page<ProductDiscontinue> data = this.productDiscontinueRepositoryWithCount.findAll(allRecordsRequest);
		return new PageableResult<>(page,
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent()
		);
	}

	/**
	 * Sets the ProductDiscontinueRepository for this object to use to fetch data. This is mainly for
	 * testing.
	 *
	 * @param repository The ProductDiscontinueRepository for this object to use.
	 */
	public void setRepository(ProductDiscontinueRepositoryWithCount repository) {
		this.productDiscontinueRepositoryWithCount = repository;
	}
}
