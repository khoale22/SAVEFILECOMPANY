package com.heb.pm.repository;

/**
 * Repository constants for queries to retrieve ingredient information.
 *
 * @author m594201
 * @since 2.0.9
 */
public interface IngredientRepositoryCommon {

    /**
     * The constant INGREDIENT_SELECT.
     */
    String INGREDIENT_SELECT = "select i from Ingredient i ";

    /**
     * The constant DESCRIPTION_SEARCH.
     */
    String DESCRIPTION_SEARCH = "where (upper(i.ingredientDescription) like :description ";

    /**
     * The constant INGREDIENT_CODE_SEARCH.
     */
    String INGREDIENT_CODE_SEARCH = "where i.ingredientCode in :ingredientCodes ";

    /**
     * The constant INGREDIENT_STATEMENT_JOIN.
     */
    String INGREDIENT_STATEMENT_JOIN = "join i.statementDetails sd  ";

    /**
     * Constant to join the SOI table for searching for super-ingredients.
     */
    String INGREDIENT_SOI_JOIN = "join i.ingredientSubs iSub";

    /**
     * Where clause for searching for super-ingredients.
     */
    String WHERE_SOI = " where iSub.key.ingredientCode = :ingredientCode ";

    /**
     * The constant ORDER_BY_INGREDIENT_CODE_NUMBER.
     */
    String ORDER_BY_INGREDIENT_CODE_NUMBER = "order by to_number(i.ingredientCode) ";

    /**
     * The constant INCLUDE_CATEGORY.
     */
    String INCLUDE_CATEGORY = "and i.categoryCode = :categoryCode ";

    /**
     * The constant INCLUDE_CATEGORY.
     */
    String WHERE_INCLUDE_CATEGORY = "where i.categoryCode = :categoryCode ";

    /**
     * The constant EXCLUDE_CATEGORY.
     */
    String EXCLUDE_CATEGORY = "and i.categoryCode != :categoryCode ";

    /**
     * The constant EXCLUDE_CATEGORY.
     */
    String WHERE_EXCLUDE_CATEGORY = "where i.categoryCode != :categoryCode ";

    /**
	 * The constant NOT_DELETES.
	 */
	String NOT_DELETES = "and i.maintFunction <> 'D' ";

	/**
	 * The constant EXT_DESCRIPTION.
	 */
	String EXT_DESCRIPTION = "or upper(i.ingredientCatDescription) like :description)  ";

    /**
     * The constant WHERE_STATEMENT_NUMBER.
     */
	String WHERE_STATEMENT_NUMBER = "where sd.key.statementNumber = :statementNumber ";

    /**
     * The constant ORDER_BY_INGREDIENT_PERCENTAGE.
     */
	String ORDER_BY_INGREDIENT_PERCENTAGE = "order by sd.ingredientPercentage desc ";
}
