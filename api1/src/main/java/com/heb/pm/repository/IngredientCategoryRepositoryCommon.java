/*
 *  IngredientCategoryRepositoryCommon
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

/**
 * Repository constants for queries to retrieve ingredient category information.
 *
 * @author s573181
 * @since 2.1.0
 */
public interface IngredientCategoryRepositoryCommon {

	String QUERY = "from IngredientCategory ic where ";
	String DESCRIPTION = "upper(trim(ic.categoryDescription)) like '%s' ";
	String OR = "OR ";
	String SELECT = "select ic ";
	String COUNT = "select count(ic.categoryDescription) ";
	String QUERY_ALL = "from IngredientCategory ic";

}
