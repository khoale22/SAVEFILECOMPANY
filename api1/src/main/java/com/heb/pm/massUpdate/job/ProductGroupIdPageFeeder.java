/*
 *  ProductGroupIdPageFeeder
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job;

import com.heb.pm.customHierarchy.GenericEntityRelationshipService;
import com.heb.pm.entity.GenericEntityRelationship;
import com.heb.pm.productSearch.ProductSearchCriteria;
import com.heb.util.jpa.PageableResult;

import java.util.Iterator;

/**
 * This is the product group id reader for a page.
 *
 * @author l730832
 * @since 2.17.0
 */
public class ProductGroupIdPageFeeder implements ProductFeeder {

	private int pageSize;
	private int page;
	private ProductSearchCriteria productSearchCriteria;

	private Iterator<GenericEntityRelationship> productGroups;

	private GenericEntityRelationshipService genericEntityRelationshipService;

	public ProductGroupIdPageFeeder(int pageSize, ProductSearchCriteria productSearchCriteria, GenericEntityRelationshipService genericEntityRelationshipService) {
		this.productSearchCriteria = productSearchCriteria;
		this.genericEntityRelationshipService = genericEntityRelationshipService;
		this.pageSize = pageSize;
	}

	/**
	 * This initializes the product feeder.
	 */
	@Override
	public void init() {
		this.page = 0;
		this.setupNextBatch();
	}

	/**
	 * This sets up the next batch for each read. (Each page)
	 */
	private void setupNextBatch() {
		// else this is a product group mass page batch job...get all current hierarchy relationships
		PageableResult<GenericEntityRelationship> productGroupRelationship =
				this.genericEntityRelationshipService.findAllCustomerProductGroupsByRelationship(
						this.productSearchCriteria.getLowestCustomerHierarchyNode().getHierarchyContext(),
						this.productSearchCriteria.getLowestCustomerHierarchyNode().getChildEntityId(),
						this.page, this.pageSize, false);
		if (!productGroupRelationship.getData().iterator().hasNext()) {
			this.productGroups = null;
		} else {
			this.productGroups = productGroupRelationship.getData().iterator();
		}
		this.page++;
	}

	/**
	 * This reads the product group id's by page.
	 * @return
	 */
	@Override
	public Long read() {
		if (this.productGroups == null) {
			return null;
		}

		while(this.productGroups.hasNext()) {
			GenericEntityRelationship customerProductGroupByRelationship = this.productGroups.next();
			if(this.productSearchCriteria.getExcludedProducts() == null ||
					!this.productSearchCriteria.getExcludedProducts().contains(
							customerProductGroupByRelationship.getGenericChildEntity().getDisplayNumber())) {
				return customerProductGroupByRelationship.getGenericChildEntity().getDisplayNumber();
			}
		}
		this.setupNextBatch();
		return this.read();
	}
}
