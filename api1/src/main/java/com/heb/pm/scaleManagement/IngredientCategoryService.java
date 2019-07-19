/*
 * IngredientCategoryService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.Ingredient;
import com.heb.pm.entity.IngredientCategory;
import com.heb.pm.repository.IngredientCategoryRepository;
import com.heb.pm.repository.IngredientCategoryRepositoryWithCount;
import com.heb.pm.repository.IngredientRepository;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Holds all business logic related for ingredient categories.
 *
 * @author s573181
 * @since 2.1.0
 */
@Service
public class IngredientCategoryService {

	private static final String INGREDIENT_CATEGORY_REMOVE_FAIL_MESSAGE = "Ingredient category code: %s cannot be " +
			"removed because it is used by the following ingredient codes: %s";
	private static final String MAX_CATEGORY_CODE_SIZE_ERROR =
			"The category code value can't be larger than 999.";
	private static final int MAX_CATEGORY_CODE_SIZE = 999;
	/**
	 * The Ingredient category repository.
	 */
	@Autowired
	private IngredientCategoryRepository repository;

	@Autowired
	private IngredientCategoryRepositoryWithCount repositoryWithCount;

	@Autowired
	private IngredientRepository ingredientRepository;

	/**
	 * Find all ingredient categories list.
	 *
	 * @return the list
	 */
	public List<IngredientCategory> findAllIngredientCategories() {
		return this.repository.findAll();
	}

	/**
	 * Find all ingredient categories list by page.
	 *
	 * @param includeCounts the include counts
	 * @param page          the page
	 * @param pageSize      the page size
	 * @return the list
	 */
	public PageableResult<IngredientCategory> findAll(boolean includeCounts, int page,
																		  int pageSize) {
		Pageable pageRequest = new PageRequest(page, pageSize);

		return includeCounts ? this.findAllIngredientCategoriesWithCount(pageRequest) :
				this.findAllIngredientCategoriesWithoutCount(pageRequest);
	}

	/**
	 * Finds a pageable Ingredient category code list based on the category code.
	 *
	 * @param categoryCodes the category code list.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param pageSize The size of the page.
	 * @return a pageableResult of IngredientCategory from a list of category codes.
	 */
	public PageableResult<IngredientCategory> findByCategoryCode(List<Long> categoryCodes,
																 boolean includeCounts, int page, int pageSize) {
		for(Long code : categoryCodes){
			if(code > IngredientCategoryService.MAX_CATEGORY_CODE_SIZE){
				throw new IllegalArgumentException(IngredientCategoryService.MAX_CATEGORY_CODE_SIZE_ERROR);
			}
		}
		Pageable pageRequest = new PageRequest(page, pageSize);
		List<Long> categoryCodeList = categoryCodes.stream().collect(Collectors.toList());

		return includeCounts ? this.findByCategoryCodeWithCount(categoryCodeList, pageRequest) :
				this.findByCategoryCodeWithoutCount(categoryCodeList, pageRequest);

	}

	/**
	 * Finds a pageable Ingredient category code list based on the category code.
	 *
	 * @param descriptions a string containing the descriptions to look for.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param pageSize The size of the page.
	 * @return a pageableResult of IngredientCategory from a list of category codes.
	 */
	public PageableResult<IngredientCategory> findByCategoryDescription(String descriptions,
																		boolean includeCounts, int page, int pageSize){
		Pageable pageRequest = new PageRequest(page, pageSize);
		List<String> descriptionList = new ArrayList<>();
		Collections.addAll(descriptionList, descriptions.split("[\\s,]+"));

		return includeCounts ? this.findByCategoryCodeDescriptionWithCount(descriptionList, pageRequest) :
				this.findByCategoryCodeDescriptionWithoutCount(descriptionList, pageRequest);
	}
	/**
	 * Returns the Hits count with match and non-match, along with non-match category codes from the input list.
	 *
	 * @param categoryCodes The category codes to search for.
	 * @return Hits for the categoryCodes.
	 */
	public Hits findHitsByCategoryCode(List<Long> categoryCodes){
		List<Long> categoryCodeList = categoryCodes.stream().collect(Collectors.toList());
		List<IngredientCategory> ingredientCategories = this.repositoryWithCount.findAll(categoryCodeList);
		List<Long> hitCategoryCodes = ingredientCategories.stream().
				map(IngredientCategory::getCategoryCode).collect(Collectors.toList());
		return Hits.calculateHits(categoryCodes, hitCategoryCodes);
	}

	/**
	 * Adds a new IngredientCategory code with the current highest code + 1, and the new descriptions.
	 *
	 * @param description The description to be added to the new category code.
	 * @return a new IngredientCategory code with the current highest code + 1, and the new descriptions.
	 */
	public IngredientCategory add(String description){
		Long categoryCode = this.repository.findFirstByOrderByCategoryCodeDesc().getCategoryCode() + 1;

		IngredientCategory ingredientCategory = new IngredientCategory();
		ingredientCategory.setCategoryCode(categoryCode);
		ingredientCategory.setCategoryDescription(description.trim().toUpperCase());
		return this.repository.save(ingredientCategory);
	}

	/**
	 * Updates an Ingredient Category with the new description.
	 *
	 * @param ingredientCategory The IngredientCategory to be updated.
	 * @return The IngredientCategory to be updated.
	 */
	public IngredientCategory update(IngredientCategory ingredientCategory){
		ingredientCategory.setCategoryDescription(ingredientCategory.getCategoryDescription().trim().toUpperCase());
		return this.repository.save(ingredientCategory);
	}

	/**
	 * Checks to see if an ingredient category code exists based on its code, if it does, then it deletes it.
	 *
	 * @param ingredientCategoryCode the ingredient category for the ingredient category to be deleted.
	 */
	public void delete(Long ingredientCategoryCode){
		IngredientCategory ingredientCategory = this.repository.findOne(ingredientCategoryCode);

		if(ingredientCategory != null)
		{
			Pageable request = new PageRequest(0, 1);
			List<Ingredient> ingredients =
					this.ingredientRepository.findByCategoryCode(ingredientCategory.getCategoryCode(), request);
			if(ingredients == null || ingredients.size() == 0) {
				this.repository.delete(ingredientCategory);
			} else {
				Set<String> ingredientCodes = ingredients.stream().map(
						Ingredient::getIngredientCode).collect(Collectors.toSet());
				throw new IllegalArgumentException(String.format(INGREDIENT_CATEGORY_REMOVE_FAIL_MESSAGE,
						ingredientCategoryCode, ingredientCodes));
			}
		}
	}
	/**
	 * Finds a pageable Ingredient category code list  with counts based on the category code description.
	 *
	 * @param descriptionList The descriptions to search for.
	 * @param pageRequest The page request.
	 * @return a pageableResult of IngredientCategory from a list of category code descriptions.
	 */
	private PageableResult<IngredientCategory> findByCategoryCodeDescriptionWithCount(List<String> descriptionList,
																					  Pageable pageRequest) {
		Page<IngredientCategory> data = this.repositoryWithCount.findByCategoryDescriptionContains(descriptionList,
				pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}

	/**
	 * Finds a pageable Ingredient category code list  without counts based on the category code description.
	 *
	 * @param descriptionList The descriptions to search for.
	 * @param pageRequest The page request.
	 * @return a pageableResult of IngredientCategory from a list of category code descriptions.
	 */
	private PageableResult<IngredientCategory> findByCategoryCodeDescriptionWithoutCount(List<String> descriptionList,
																						 Pageable pageRequest) {
		List<IngredientCategory> data = this.repository.findByCategoryDescriptionContains(descriptionList,
				pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(), data);

	}

	/**
	 * Finds a pageable Ingredient category code list  with counts based on the category codes.
	 *
	 * @param categoryCodes The category codes to search for.
	 * @param pageRequest The page request.
	 * @return a pageableResult of IngredientCategory from a list of category codes.
	 */
	private PageableResult<IngredientCategory> findByCategoryCodeWithCount(List<Long> categoryCodes,
																		   Pageable pageRequest) {
		Page<IngredientCategory> data = this.repositoryWithCount.findByCategoryCodeIn(categoryCodes, pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(), data.getTotalPages(),
				data.getTotalElements(), data.getContent());
	}

	/**
	 * Finds a pageable Ingredient category code list  without counts based on the category codes.
	 *
	 * @param categoryCodes The category codes to search for.
	 * @param pageRequest The page request.
	 * @return a pageableResult of IngredientCategory from a list of category codes.
	 */
	private PageableResult<IngredientCategory> findByCategoryCodeWithoutCount(List<Long> categoryCodes,
																			  Pageable pageRequest) {

		List<IngredientCategory> data = this.repository.findByCategoryCodeIn(categoryCodes, pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(), data);
	}

	/**
	 * Finds a pageable Ingredient category code list containing all ingredient categories with counts.
	 *
	 * @param pageRequest The page request.
	 * @return a pageableResult of IngredientCategory.
	 */
	private PageableResult<IngredientCategory> findAllIngredientCategoriesWithCount(Pageable pageRequest) {
		Page<IngredientCategory> data = this.repositoryWithCount.findAll(pageRequest);

		return new PageableResult<>(pageRequest.getPageNumber(), data.getTotalPages(), data.getTotalElements(),
				data.getContent());
	}

	/**
	 * Finds a pageable Ingredient category code list containing all ingredient categories without counts.
	 *
	 * @param pageRequest The page request.
	 * @return a pageableResult of IngredientCategory.
	 */
	private PageableResult<IngredientCategory> findAllIngredientCategoriesWithoutCount(Pageable pageRequest){
		List<IngredientCategory> data = this.repository.findAllByPage(pageRequest);
		return new PageableResult<>(pageRequest.getPageNumber(), data);
	}
}
