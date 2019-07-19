/*
 *  ProductIdFeeder
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.pm.productSearch.ProductSearchService;
import com.heb.util.jpa.PageableResult;

import java.util.Iterator;

/**
 * This is the product ID feeder for products.
 *
 * @author l730832
 * @since 2.17.0
 */
public class ProductIdFeeder implements ProductFeeder {

	private int pageSize;
	private int page;
	private Iterator<ProductMaster> products;
	private ProductSearchCriteria productSearchCriteria;
	private ProductSearchService productSearchService;

	public ProductIdFeeder(int pageSize, ProductSearchCriteria productSearchCriteria, ProductSearchService productSearchService) {
		this.pageSize = pageSize;
		this.productSearchCriteria = productSearchCriteria;
		this.productSearchService = productSearchService;
	}

	@Override
	public Long read() {
		if (this.products == null) {
			return null;
		}

		while (this.products.hasNext()) {
			ProductMaster productMaster = this.products.next();
			if (this.productSearchCriteria.getExcludedProducts() == null ||
					!this.productSearchCriteria.getExcludedProducts().contains(productMaster.getProdId())) {
				return productMaster.getProdId();
			}
		}

		// At this point, we've read through all the products in the batch, so go get the
		// next batch.
		this.setupNextBatch();
		return this.read();
	}

	@Override
	public void init() {
		this.page = 0;
		this.setupNextBatch();
	}

	private void setupNextBatch() {
		PageableResult<ProductMaster> productData =
				this.productSearchService.searchForProducts(this.productSearchCriteria, this.page, this.pageSize, false);
		if (!productData.getData().iterator().hasNext()) {
			this.products = null;
		} else {
			this.products = productData.getData().iterator();
		}
		this.page++;
	}
}
