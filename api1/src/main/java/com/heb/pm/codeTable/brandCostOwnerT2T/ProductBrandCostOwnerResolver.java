/*
 * ProductBrandCostOwnerResolver
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.codeTable.brandCostOwnerT2T;

import com.heb.pm.entity.ProductBrandCostOwner;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves lazy loaded objects for a ProductBrandCostOwner.
 *
 * @author vn70529
 * @since 2.12.0
 */
public class ProductBrandCostOwnerResolver implements LazyObjectResolver<ProductBrandCostOwner> {
	/**
	 * Resolves the ProductBrandCostOwner, then fetches the lazy loaded objects:
	 *
	 * @param productBrandCostOwner The object to resolve.
	 */
	@Override
	public void fetch(ProductBrandCostOwner productBrandCostOwner){
		productBrandCostOwner.getCostOwner().getTopToTop().getTopToTopName();
		productBrandCostOwner.getProductBrand().getProductBrandDescription();
	}
}
