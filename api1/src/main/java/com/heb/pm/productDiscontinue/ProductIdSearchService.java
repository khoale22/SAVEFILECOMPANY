/*
 * ProductIdSearchService
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
 * This class can be delegated to for all searches based on product ID.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
public class ProductIdSearchService {

	// The default number of products to search for. This number
	// and fewer will have the maximum performance.
	private static final int DEFAULT_PRODUCT_ID_COUNT = 100;

	// Used to get consistent size lists to query runners.
	private LongPopulator longPopulator = new LongPopulator();

	// The DAO to get all prod_del data from.
	@Autowired
	private ProductDiscontinueRepositoryWithCount productDiscontinueRepositoryWithCount;

	// The DAO to get all prod_del data without counts from.
	@Autowired
	private com.heb.pm.repository.ProductDiscontinueRepository productDiscontinueRepository;


	/**
	 * Searches for product discontinue records based on product IDs.
	 *
	 * @param productIds A list of product IDs to search for records on.
	 * @param request Holds paging information about the request.
	 * @param statusFilter  The filter to apply to item status.
	 * @param statusFilter  The filter to apply to item status.
	 * @param includeCount Set to true to include page and record counts.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	public PageableResult<ProductDiscontinue> findByProductIds(List<Long> productIds, Pageable request,
															  StatusFilter statusFilter, boolean includeCount) {

		if (productIds == null || productIds.isEmpty()) {
			throw new IllegalArgumentException("Product IDs cannot be null or empty.");
		}
		if (request == null) {
			throw new IllegalArgumentException("Page request cannot be null");
		}

		this.longPopulator.populate(productIds, ProductIdSearchService.DEFAULT_PRODUCT_ID_COUNT);

		return includeCount ? this.searchWithCounts(productIds, request, statusFilter) :
				this.searchWithoutCounts(productIds, request, statusFilter);
	}

	/**
	 * Handles the search when the user wants counts.
	 *
	 * @param productIds A list of product IDs to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchWithCounts(List<Long> productIds, Pageable request,
																StatusFilter statusFilter) {
		Page<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepositoryWithCount.findActiveByProductIdsWithCount(productIds, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepositoryWithCount.findDiscontinuedByProductIdsWithCount(productIds, request);
				break;
			default:
				data = this.productDiscontinueRepositoryWithCount.findDistinctByKeyProductIdIn(productIds, request);
		}
		return new PageableResult<>(request.getPageNumber(),
				data.getTotalPages(),
				data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Used to get the list of products listed in the Prod_Del table
	 * @param productIds the products
	 * @return	matching products
	 */
	List<Long> findAllProducts(List<Long> productIds) {
		this.longPopulator.populate(productIds, ProductIdSearchService.DEFAULT_PRODUCT_ID_COUNT);
		return this.convertProductDiscontinueListToProductIdList(
				this.productDiscontinueRepository.findAllDistinctByKeyProductIdIn(productIds));
	}

	/**
	 * Handles the search when the user does not want counts.
	 *
	 * @param productIds A list of product IDs to search for records on.
	 * @param request The request with page and page size
	 * @param statusFilter  The filter to apply to item status.
	 * @return An object containing the product discontinue records based on search criteria.
	 */
	private PageableResult<ProductDiscontinue> searchWithoutCounts(List<Long> productIds, Pageable request,
																   StatusFilter statusFilter) {
		List<ProductDiscontinue> data;

		switch (statusFilter) {
			case ACTIVE:
				data = this.productDiscontinueRepository.findActiveByProductIds(productIds, request);
				break;
			case DISCONTINUED:
				data = this.productDiscontinueRepository.findDiscontinuedByProductIds(productIds, request);
				break;
			default:
				data = this.productDiscontinueRepository.findDistinctByKeyProductIdIn(productIds, request);
		}
		return new PageableResult<>(request.getPageNumber(), data);
	}

	/**
	 * Converts list of Product Discontinues into a list of their product ids.
	 *
	 * @param productDiscontinueList List of Product Discontinues to convert.
	 * @return A List of upc.
	 */
	private List<Long> convertProductDiscontinueListToProductIdList(List<ProductDiscontinue> productDiscontinueList){
		return productDiscontinueList.stream().map(pd -> pd.getKey().getProductId()).collect(Collectors.toList());
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
