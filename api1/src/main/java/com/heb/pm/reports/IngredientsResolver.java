/*
 * IngredientsResolver
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.reports;

import com.heb.pm.entity.DynamicAttribute;
import com.heb.util.jpa.LazyObjectResolver;
import org.springframework.stereotype.Component;

/**
 * Resolves DynamicAttributes for the ingredients report.
 *
 * @author d116773
 * @since 2.0.7
 */
@Component
public class IngredientsResolver implements LazyObjectResolver<DynamicAttribute> {

	/**
	 * Resolve DynamicAttributes for the ingredients report.
	 * @param d The object to resolve.
	 */
	@Override
	public void fetch(DynamicAttribute d) {
		if (d.getProductMaster() != null) {
			d.getProductMaster().getClassCommodity().getBdm().getBdmCode();
			if (d.getProductMaster().getSubCommodity() != null) {
				d.getProductMaster().getSubCommodity().getName();
			}
		}
	}
}
