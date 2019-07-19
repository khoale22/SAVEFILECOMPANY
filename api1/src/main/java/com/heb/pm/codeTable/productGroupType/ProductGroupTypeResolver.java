/*
 *  ProductGroupTypeResolver
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.codeTable.productGroupType;

import com.heb.pm.entity.ProductGroupChoiceOption;
import com.heb.pm.entity.ProductGroupChoiceType;
import com.heb.pm.entity.ProductGroupType;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves a ProductGroupType object returned by the ProductGroupType REST endpoint.
 *
 *  @author vn70529
 * @since 2.12
 */
public class ProductGroupTypeResolver implements LazyObjectResolver<ProductGroupType> {

	@Override
	public void fetch(ProductGroupType productGroupType){
		productGroupType.getCustomerProductGroups().size();
		for (ProductGroupChoiceType productGroupChoiceType:productGroupType.getProductGroupChoiceTypes()) {
			productGroupChoiceType.getChoiceType().getDisplayName();
			for (ProductGroupChoiceOption productGroupChoiceOption:productGroupChoiceType.getProductGroupChoiceOptions()) {
				productGroupChoiceOption.getChoiceOption().getProductChoiceText();
			}
		}
	}
}
