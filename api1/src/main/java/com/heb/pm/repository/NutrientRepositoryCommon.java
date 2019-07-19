package com.heb.pm.repository;

/**
 * Repository constants for queries to retrieve nutrient information.
 *
 * @author m594201
 * @since 2.1.0
 */
public interface NutrientRepositoryCommon {

	/**
	 * The constant QUERY.
	 */
	String QUERY = "from Nutrient n where ";
	/**
	 * The constant DESCRIPTION.
	 */
	String DESCRIPTION = "n.nutrientDescription like '%s' ";
	/**
	 * The constant OR.
	 */
	String OR = "OR ";
	/**
	 * The constant SELECT.
	 */
	String SELECT = "select n ";
	/**
	 * The constant COUNT.
	 */
	String COUNT = "select count(n.nutrientCode) ";
}
