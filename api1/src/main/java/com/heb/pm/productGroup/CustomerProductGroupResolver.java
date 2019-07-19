/*
 *  ProductGroupSearchController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup;

import com.heb.pm.entity.CustomerProductGroup;
import com.heb.util.jpa.LazyObjectResolver;
import java.util.List;

public class CustomerProductGroupResolver implements LazyObjectResolver<List<CustomerProductGroup>> {
	/**
	 * Performs the actual resolution of lazily loaded parameters.
	 * @param customerProductGroups The object to resolve.
	 */
	@Override
	public void fetch(List<CustomerProductGroup> customerProductGroups){
		for (CustomerProductGroup customerProductGroup:customerProductGroups) {
			customerProductGroup.getProductGroupType().getProductGroupTypeCode();
			if (customerProductGroup.getLowestEntity()!=null) {
				customerProductGroup.getLowestEntity().getDisplayText();
			}
		}
	}
}
