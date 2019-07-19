/*
 * ItemCodeSearchService
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
import com.heb.util.list.LongPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class can be delegated to for all searches based on item code.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
class ItemCodeSearchService {

	// The default number of item codes to search for. This number
	// and fewer will have the maximum performance.
	private static final int DEFAULT_ITEM_CODE_COUNT = 100;

	private static final String DISCONTINUE_NULL_DATE = "1600-01-01";
	private static final String D_PURCHASING_STATUS = "D    ";

	// Used to get consistent size lists to query runners.
	private LongPopulator longPopulator = new LongPopulator();

	// The DAO to get all prod_del data from.
	@Autowired
	private ProductDiscontinueRepositoryWithCount productDiscontinueRepositoryWithCount;

	// The DAO to get all prod_del data without count from.
	@Autowired
	private com.heb.pm.repository.ProductDiscontinueRepository productDiscontinueRepository;


	/**
	 * Searches for product discontinue records based on multiple item codes.
	 *
	 * @param itemCodes A list of item codes to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByItemCodes(List<Long> itemCodes, Pageable request,
															  StatusFilter statusFilter, boolean includeCount) {

		if (itemCodes == null || itemCodes.isEmpty()) {
			throw new IllegalArgumentException("Item codes cannot be null or empty.");
		}
		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		this.longPopulator.populate(itemCodes, ItemCodeSearchService.DEFAULT_ITEM_CODE_COUNT);

		return includeCount ? this.searchWithCounts(itemCodes, request, statusFilter) :
				this.searchWithoutCounts(itemCodes, request, statusFilter);
	}

	/**
	 * Handles the search when the user wants counts.
	 *
	 * @param itemCodes A list of item codes to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchWithCounts(List<Long> itemCodes, Pageable request,
													  StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByItemCodesWithCount(itemCodes, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByItemCodesWithCount(itemCodes, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.
						findDistinctByKeyItemCodeInAndKeyItemType(itemCodes, ProductDiscontinueKey.WAREHOUSE, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Used to get all matching  item codes
	 * @param itemCodes the UPCs
	 * @return	matching item codes
	 */
	List<Long> findAllItemCodes(List<Long> itemCodes) {
		this.longPopulator.populate(itemCodes, ItemCodeSearchService.DEFAULT_ITEM_CODE_COUNT);
		return this.convertProductDiscontinueListToItemList(
				this.productDiscontinueRepository.findAllDistinctByKeyItemCodeIn(itemCodes));
	}

	/**
	 * Handles the search when the user does not want counts.
	 *
	 * @param itemCodes A list of item codes to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchWithoutCounts(List<Long> itemCodes, Pageable request,
																StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByItemCodes(itemCodes, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByItemCodes(itemCodes, request);
				break;
			default:
				data = this.productDiscontinueRepository.findDistinctByKeyItemCodeInAndKeyItemType(itemCodes,
						ProductDiscontinueKey.WAREHOUSE, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Converts list of Product Discontinues into a list of their upcs.
	 *
	 * @param productDiscontinueList List of Product Discontinues to convert.
	 * @return A List of upc.
	 */
	private List<Long> convertProductDiscontinueListToItemList(List<ProductDiscontinue> productDiscontinueList){
		return productDiscontinueList.stream().map(pd -> pd.getKey().getItemCode()).collect(Collectors.toList());
	}

	/**
	 * Sets the ProductDiscontinueRepository for this object to use. This is for testing.
	 *
	 * @param repository The ProductDiscontinueRepository for this object to use.
	 */
	public void setRepositoryWithCount(ProductDiscontinueRepositoryWithCount repository) {
		this.productDiscontinueRepositoryWithCount = repository;
	}

	/**
	 * Sets the ProductDiscontinueRepository for this object to use. This is for testing.
	 *
	 * @param repository The ProductDiscontinueRepository for this object to use.
	 */
	public void setRepository(com.heb.pm.repository.ProductDiscontinueRepository repository) {
		this.productDiscontinueRepository = repository;
	}
}
