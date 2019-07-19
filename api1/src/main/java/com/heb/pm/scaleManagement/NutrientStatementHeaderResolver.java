/*
 *
 * NutrientStatementHeaderResolver
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.NutrientStatementDetail;
import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;

/**
 * Resolver for NutrientStatementHeaderResolver
 *
 * @author m594201
 * @since 2.2.0
 */
public class NutrientStatementHeaderResolver implements LazyObjectResolver<NutrientStatementHeader> {

	private static final Logger logger = LoggerFactory.getLogger(NutrientStatementHeaderResolver.class);

	public static String ASSOCIATED_UPC_NOT_FOUND_ERROR = "Associated UPC: %d matching Nutrient Statement Header %d not found.";
	public static String SELLING_UNIT_NOT_FOUND_ERROR = "Selling Unit UPC: %d matching Nutrient Statement Header %d not found.";
	private static String PRODUCT_NOT_FOUND_ERROR = "Product Master Id: %d matching Nutrient Statement Header %d not found.";
	@Override
	public void fetch(NutrientStatementHeader d) {
		d.getNutrientStatementDetailList().size();
		for(NutrientStatementDetail detail: d.getNutrientStatementDetailList()){
			detail.getNutrient().getNutrientCode();
		}

		if(d.getScaleUpc() != null) {
			if (d.getScaleUpc().getAssociateUpc() != null) {
				if (d.getScaleUpc().getAssociateUpc().getSellingUnit() != null) {
					if (d.getScaleUpc().getAssociateUpc().getSellingUnit().getProductMaster() != null) {
						d.getScaleUpc().getAssociateUpc().getSellingUnit().getProductMaster().getDepartmentCode();
					} else {
						throw new EntityNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR,
								d.getScaleUpc().getAssociateUpc().getSellingUnit().getProdId(), d.getNutrientStatementNumber()));
					}
				} else {
					throw new EntityNotFoundException(String.format(SELLING_UNIT_NOT_FOUND_ERROR,
							d.getScaleUpc().getAssociateUpc().getUpc(), d.getNutrientStatementNumber()));
				}
			} else {
				throw new EntityNotFoundException(String.format(ASSOCIATED_UPC_NOT_FOUND_ERROR, d.getScaleUpc().getUpc(), d.getNutrientStatementNumber()));
			}
		}
	}
}
