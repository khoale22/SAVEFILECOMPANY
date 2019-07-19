/*
 *
 * NutrientStatementPanelHeaderResolver.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.entity.NutrientPanelColumnHeader;
import com.heb.pm.entity.NutrientPanelDetail;
import com.heb.pm.entity.NutrientStatementPanelHeader;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolver for NutrientStatementPanelHeader
 *
 * @author vn70529
 * @since 2.20.0
 */
public class NutrientStatementPanelHeaderResolver implements LazyObjectResolver<NutrientStatementPanelHeader> {

	/**
	 * Fetch data for NutrientStatementPanelHeader Object
	 * @param d The object to resolve.
	 */
	@Override
	public void fetch(NutrientStatementPanelHeader d) {
		d.getNutrientPanelColumnHeaders().size();
		for(NutrientPanelColumnHeader nutrientPanelColumnHeader: d.getNutrientPanelColumnHeaders()){
			nutrientPanelColumnHeader.getNutrientPanelDetails().size();
			for(NutrientPanelDetail nutrientPanelDetail: nutrientPanelColumnHeader.getNutrientPanelDetails()){
				nutrientPanelDetail.getNutrient().getNutrientId();

			}
		}
	}
}
