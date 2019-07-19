/*
 *  ProductGroupIdListFeeder
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.massUpdate.job;

import java.util.Iterator;

/**
 * This is the product group Id List feeder that will feed a list of product group ids that we already have inside of
 * the search criteria. This is not done by pages. In this case we do not need a setupNext method.
 *
 * @author l730832
 * @since 2.17.0
 */
public class ProductGroupIdListFeeder implements ProductFeeder {

	private Iterator<Long> productGroupIds;

	public ProductGroupIdListFeeder(Iterator<Long> productGroupIds) {
		this.productGroupIds = productGroupIds;
	}

	/**
	 * Nothing needs to be done in the initialization because we already have the list.
	 */
	@Override
	public void init() {
	}

	/**
	 * If the iterator has a next id then return it if not return null.
	 * @return a product group id.
	 */
	@Override
	public Long read() {
			if (!this.productGroupIds.hasNext()) {
				return null;
			}
			return this.productGroupIds.next();
		}

}
