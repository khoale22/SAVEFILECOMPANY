/*
 * UpcSearchService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;
import com.heb.pm.repository.ProductDiscontinueRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.LongPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class can be delegated to for all searches based on UPC.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
class UpcSearchService {

	// The default number of UPCs to search for. This number
	// and fewer will have the maximum performance.
	private static final int DEFAULT_UPC_COUNT = 100;

	// Used to get consistent size lists to query runners.
	private LongPopulator longPopulator = new LongPopulator();

	// The DAO to get all prod_del data from.
	@Autowired
	private ProductDiscontinueRepositoryWithCount productDiscontinueRepositoryWithCount;

	// The DAO to get all prod_del data from.
	@Autowired
	private com.heb.pm.repository.ProductDiscontinueRepository productDiscontinueRepository;


	/**
	 * Searches for product discontinue records based on multiple UPCs.
	 *
	 * @param upcs A list of UPCs to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByUpcs(List<Long> upcs, Pageable request,
															  StatusFilter statusFilter, boolean includeCount) {

		if (upcs == null || upcs.isEmpty()) {
			throw new IllegalArgumentException("UPCs cannot be null or empty.");
		}
		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		this.longPopulator.populate(upcs, UpcSearchService.DEFAULT_UPC_COUNT);

		return includeCount ? this.searchWithCounts(upcs, request, statusFilter) :
				this.searchWithoutCounts(upcs, request, statusFilter);
	}

	/**
	 * Handles the search when the user wants counts.
	 *
	 * @param upcs A list of UPCs to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchWithCounts(List<Long> upcs, Pageable request,
													  StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByUpcsWithCount(upcs, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByUpcsWithCount(upcs, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findDistinctByKeyUpcIn(upcs, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Used to get the list of UPCs listed in the Prod_Del table
	 * @param upcs the UPCs
	 * @return	matching UPCs
	 */
	List<Long> findAllUPCs(List<Long> upcs) {
		this.longPopulator.populate(upcs, UpcSearchService.DEFAULT_UPC_COUNT);
		return this.convertProductDiscontinueListToUpcList(
				this.productDiscontinueRepository.findAllDistinctByKeyUpcIn(upcs));
	}

	/**
	 * Handles the search when the user does not want counts.
	 *
	 * @param upcs A list of UPCs to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchWithoutCounts(List<Long> upcs, Pageable request,
																StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByUpcs(upcs, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByUpcs(upcs, request);
				break;
			default:
				data = this.productDiscontinueRepository.findDistinctByKeyUpcIn(upcs, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Converts list of Product Discontinues into a list of their upcs.
	 *
	 * @param productDiscontinueList List of Product Discontinues to convert.
	 * @return A List of upc.
	 */
	private List<Long> convertProductDiscontinueListToUpcList(List<ProductDiscontinue> productDiscontinueList){
		return productDiscontinueList.stream().map(pd -> pd.getKey().getUpc()).collect(Collectors.toList());
	}

	/**
	 * Sets the ProductDiscontinueRepository for this object to use. This is mainly here for testing.
	 *
	 * @param repository The ProductDiscontinueRepository for this object to use.
	 */
	public void setRepositoryWithCount(ProductDiscontinueRepositoryWithCount repository) {
		this.productDiscontinueRepositoryWithCount = repository;
	}
	/**
	 * Sets the ProductDiscontinueRepository for this object to use. This is mainly here for testing.
	 *
	 * @param repository The ProductDiscontinueRepository for this object to use.
	 */
	public void setRepository(com.heb.pm.repository.ProductDiscontinueRepository repository) {
		this.productDiscontinueRepository = repository;
	}
}
