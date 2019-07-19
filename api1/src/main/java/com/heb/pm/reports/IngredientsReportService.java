/*
 * IngredientsReportService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.reports;

import com.heb.pm.entity.DynamicAttribute;
import com.heb.pm.entity.DynamicAttributeKey;
import com.heb.pm.repository.DynamicAttributeRepository;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that returns data for the ingredients report.
 *
 * @author d116773
 * @since 2.0.7
 */
@Service
public class IngredientsReportService {

	@Value("${app.ecommerce.ingredientsAttributeId}")
	private int ingredientsAttributeId;

	@Value("${app.ecommerce.sourceSystemId}")
	int eCommerceSourceSystem;

	@Autowired
	private DynamicAttributeRepository ingredientsRepository;

	/**
	 * Runs the ingredients report. It pulls ingredients searched by text and limits the results to source system
	 * 13, the one for eCommerce.
	 *
	 * @param ingredient The text to search for in the ingredients.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param pageSize How big of a page you want.
	 * @return A PageableResult with DynamicAttribute objects that can be used to pull the report. If you pass true
	 * to includeCount, then it will be fully populated with the page count and total number of records. If you
	 * pass false, it will only include the data.
	 */
	public PageableResult<DynamicAttribute> getIngredientsReport(String ingredient, boolean includeCounts,
																 int page, int pageSize) {

		Pageable pageRequest = new PageRequest(page, pageSize, DynamicAttributeKey.getDefaultSort());

		if (includeCounts) {
			Page<DynamicAttribute> results =
					this.ingredientsRepository.findByTextAttributeRegularExpressionWithCounts(
							this.ingredientsAttributeId, ingredient.toUpperCase(),
							this.eCommerceSourceSystem, pageRequest);
			return new PageableResult<>(page, results.getTotalPages(), results.getTotalElements(),
					results.getContent());
		} else {
			List<DynamicAttribute> results =
					this.ingredientsRepository.findByTextAttributeRegularExpression(this.ingredientsAttributeId,
							ingredient.toUpperCase(), this.eCommerceSourceSystem, pageRequest);
			return new PageableResult<>(page, results);
		}
	}

	/**
	 * A DynamicAttributeRepository the service can use to pull data. This is mainly for testing.
	 *
	 * @param dynamicAttributeRepository A DynamicAttributeRepository the service can use to pull data.
	 */
	public void setIngredientsRepository(DynamicAttributeRepository dynamicAttributeRepository) {
		this.ingredientsRepository = dynamicAttributeRepository;
	}
}
