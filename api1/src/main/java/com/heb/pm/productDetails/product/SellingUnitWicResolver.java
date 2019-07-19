/*
 *  SellingUnitWicResolver
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product;

import com.heb.pm.entity.SellingUnitWic;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * This is the resolver for the selling unit wic.
 *
 * @author l730832
 * @since 2.12.0
 */
public class SellingUnitWicResolver implements LazyObjectResolver<SellingUnitWic> {

	@Override
	public void fetch(SellingUnitWic sellingUnitWic) {
		sellingUnitWic.getSellingUnit();
		sellingUnitWic.getSellingUnit().getProductMaster().getProdId();
	}
}
