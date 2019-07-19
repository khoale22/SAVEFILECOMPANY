/*
 *  ProductScanCodeWicResolver
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.codeTable.wicLeb;

import com.heb.pm.entity.SellingUnitWic;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves a ProductScanCodeWic object returned by the ProductScanCodeWic REST endpoint.
 *
 *  @author vn70529
 * @since 2.12.0
 */
public class ProductScanCodeWicResolver implements LazyObjectResolver<SellingUnitWic> {

	@Override
	public void fetch(SellingUnitWic productScanCodeWic){
		productScanCodeWic.getWicCategory().getDisplayName();
		productScanCodeWic.getWicSubCategory().getDisplayName();
		productScanCodeWic.getSellingUnit().getRetailUnitOfMeasure().getStandardUnitOfMeasureCode();
	}
}
