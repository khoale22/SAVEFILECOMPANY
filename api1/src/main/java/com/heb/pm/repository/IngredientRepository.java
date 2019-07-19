package com.heb.pm.repository;

import com.heb.pm.entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve ingredient without count.
 *
 * @author m594201
 * @since 2.0.9
 */
public interface IngredientRepository extends JpaRepository<Ingredient, String>, IngredientRepositoryDelegate {

    /**
     * Returns a list of ingredients that are super-ingredients for a requested ingredient.
     *
     * @param ingredientCode The ingredient code that is a sub-ingredient to search for.
     * @param pageRequest The page request.
     * @return A list of ingredients which contain ingredientCode as a sub-ingredient.
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_SOI_JOIN + WHERE_SOI + ORDER_BY_INGREDIENT_CODE_NUMBER)
    List<Ingredient> findBySoiChild(@Param("ingredientCode")String ingredientCode, Pageable pageRequest);

	String INGREDIENT_SEARCH_FOR_NEXT_AVAILABLE_INGREDIENT_CODE = "select i from Ingredient i " +
			"where to_number(i.ingredientCode)>=:ingredientCode ORDER BY to_number(i.ingredientCode) ";

    /**
     * Find by ingredient code in list.
     *
     * @param ingredientCodes the ingredient codes
     * @param pageRequest     the page request
     * @return the list
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_CODE_SEARCH + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
    List<Ingredient> findByIngredientCodeIn(@Param("ingredientCodes") List<String> ingredientCodes, Pageable pageRequest);

    /**
     * Find by ingredient description containing list.
     *
     * @param description the description
     * @param request     the request
     * @return the list
     */
    @Query(value = INGREDIENT_SELECT + DESCRIPTION_SEARCH + EXT_DESCRIPTION + NOT_DELETES)
    List<Ingredient> findByIngredientDescriptionContaining(@Param("description")String description, Pageable request);


    /**
     * Find by ingredient description containing list. Does not require Pageable
     *
     * @param description the description
     * @return the list
     */

    /**
     * Find by ingredient code in and category code list.
     *
     * @param ingredientCodes the ingredient codes
     * @param categoryCode    the category code
     * @param pageRequest     the page request
     * @return the list
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_CODE_SEARCH + NOT_DELETES + INCLUDE_CATEGORY + ORDER_BY_INGREDIENT_CODE_NUMBER)
    List<Ingredient> findByIngredientCodeInAndCategoryCode(@Param("ingredientCodes") List<String> ingredientCodes, @Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by ingredient code in and category code not list.
     *
     * @param ingredientCodes the ingredient codes
     * @param categoryCode    the category code
     * @param pageRequest     the page request
     * @return the list
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_CODE_SEARCH + EXCLUDE_CATEGORY + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
    List<Ingredient> findByIngredientCodeInAndCategoryCodeNot(@Param("ingredientCodes") List<String> ingredientCodes, @Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by ingredient description containing and category code list.
     *
     * @param description  the description
     * @param categoryCode the category code
     * @param pageRequest  the page request
     * @return the list
     */
    @Query(value = INGREDIENT_SELECT + DESCRIPTION_SEARCH + EXT_DESCRIPTION + INCLUDE_CATEGORY + NOT_DELETES)
    List<Ingredient> findByIngredientDescriptionContainingAndCategoryCode(@Param("description") String description,@Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by ingredient description containing and category code not list.
     *
     * @param description  the description
     * @param categoryCode the category code
     * @param pageRequest  the page request
     * @return the list
     */
    @Query(value = INGREDIENT_SELECT + DESCRIPTION_SEARCH + EXT_DESCRIPTION + EXCLUDE_CATEGORY + NOT_DELETES)
    List<Ingredient> findByIngredientDescriptionContainingAndCategoryCodeNot(@Param("description") String description,@Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by category code.
     * @param categoryCode category code to include
     * @param request the page request
     * @return the list
     */
	@Query(value = INGREDIENT_SELECT + WHERE_INCLUDE_CATEGORY + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
	List<Ingredient> findByCategoryCode(@Param("categoryCode") Long categoryCode, Pageable request);

    /**
     * Find where category code is excluded.
     * @param categoryCode category code to exclude
     * @param request the page request
     * @return the list
     */
	@Query(value = INGREDIENT_SELECT + WHERE_EXCLUDE_CATEGORY + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
	List<Ingredient> findByCategoryCodeNot(@Param("categoryCode") Long categoryCode, Pageable request);

    /**
     * Find all by page.
     * @param request the page request
     * @return the list
     */
    List<Ingredient> findByMaintFunctionNot(String maintFunction, Pageable request);

	/**
	 * Finds the first 100 next ingredients starting with an ingredient code.
	 * @param ingredientCode The ingredient code to search for.
	 * @param pageRequest the page request
	 * @return the list
	 */
    @Query(value = INGREDIENT_SEARCH_FOR_NEXT_AVAILABLE_INGREDIENT_CODE)
	List<Ingredient> findFirst100ByIngredientCodeGreaterThanEqual(@Param("ingredientCode")long ingredientCode, Pageable pageRequest);

    // This set is used to search by ingredient codes by regular expression.
    /**
     * Searches for ingredients by looking for an exact match on the ingredient code. It's got a parameter to
     * ignore ingredients that are marked for delete.
     *
     * @param ingredientCode The code the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return A list of ingredients that match the criteria.
     */
    List<Ingredient> findByIngredientCodeAndMaintFunctionNot(String ingredientCode, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by looking for an exact match on the ingredient code. It's got a parameter to
     * with a list of ingredient codes to ignore and another to ignore ingredients that are marked for delete.
     *
     * @param ingredientCode The code the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientCodeAndIngredientCodeNotInAndMaintFunctionNot(String ingredientCode, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient code that starts with a certain string.
     * It's got a parameter to ignore ingredients that are marked for delete.
     *
     * @param ingredientCode The code the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientCodeStartsWithAndMaintFunctionNot(String ingredientCode, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient code that starts with a certain string.
     * It's got a parameter to with a list of ingredient codes to ignore and another to ignore ingredients that are
     * marked for delete.
     *
     * @param ingredientCode The code the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientCodeStartsWithAndIngredientCodeNotInAndMaintFunctionNot(String ingredientCode, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient code that contains a certain string.
     * It's got a parameter to ignore ingredients that are marked for delete.
     *
     * @param ingredientCode The code the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientCodeContainingAndMaintFunctionNot(String ingredientCode, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient code that contains a certain string.
     * It's got a parameter to with a list of ingredient codes to ignore and another to ignore ingredients that are
     * marked for delete.
     *
     * @param ingredientCode The code the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     */
    List<Ingredient> findByIngredientCodeContainingAndIngredientCodeNotInAndMaintFunctionNot(String ingredientCode, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    // This set is used to search by ingredient description by regular expression and is case insensitive

    /**
     * Searches for ingredients by looking for an exact match on the ingredient description. It's got a parameter to
     * ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionIgnoreCaseAndMaintFunctionNot(String ingredientDescription, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by looking for an exact match on the ingredient description. It's got a parameter to
     * with a list of ingredient codes to ignore and another to ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionIgnoreCaseAndIngredientCodeNotInAndMaintFunctionNot(String ingredientDescription, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that starts with a certain
     * string. It's got a parameter to ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionStartsWithIgnoreCaseAndMaintFunctionNot(String ingredientDescription, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that starts with a certain
     * string. It's got a parameter to with a list of ingredient codes to ignore and another to ignore ingredients that
     * are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionStartsWithIgnoreCaseAndIngredientCodeNotInAndMaintFunctionNot(String ingredientDescription, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that contains a certain
     * string. It's got a parameter to ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionContainingIgnoreCaseAndMaintFunctionNot(String ingredientDescription, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that contains a certain
     * string. It's got a parameter to with a list of ingredient codes to ignore and another to ignore ingredients that
     * are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionContainingIgnoreCaseAndIngredientCodeNotInAndMaintFunctionNot(String ingredientDescription, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    // This set is used to search by ingredient description by regular expression and is case sensitive

    /**
     * Searches for ingredients by looking for an exact match on the ingredient description. It's got a parameter to
     * ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionAndMaintFunctionNot(String ingredientDescription, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by looking for an exact match on the ingredient description. It's got a parameter to
     * with a list of ingredient codes to ignore and another to ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionAndIngredientCodeNotInAndMaintFunctionNot(String ingredientDescription, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that starts with a certain
     * string. It's got a parameter to ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionStartsWithAndMaintFunctionNot(String ingredientDescription, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that starts with a certain
     * string. It's got a parameter to with a list of ingredient codes to ignore and another to ignore ingredients that
     * are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param currentCodeList The list of ingredient codes to exclude.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionStartsWithAndIngredientCodeNotInAndMaintFunctionNot(String ingredientDescription, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that contains a certain
     * string. It's got a parameter to ignore ingredients that are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionContainingAndMaintFunctionNot(String ingredientDescription, String deleteCode, Pageable pageRequest);

    /**
     * Searches for ingredients by regular expression looking for an ingredient description that contains a certain
     * string. It's got a parameter to with a list of ingredient codes to ignore and another to ignore ingredients that
     * are marked for delete.
     *
     * @param ingredientDescription The description the user is searching for.
     * @param currentCodeList
     * @param deleteCode The maintenance code to exclude from the search.
     * @param pageRequest The page, size, and sort being requested.
     * @return
     */
    List<Ingredient> findByIngredientDescriptionContainingAndIngredientCodeNotInAndMaintFunctionNot(String ingredientDescription, List<String> currentCodeList, String deleteCode, Pageable pageRequest);

    /**
     * Returns the list of ingredients by the statement number and ignore deleted ones,
     * and order by percentage of ingredient statement details.
     *
     * @param statementNumber the statement number to find.
     * @return the list of ingredients.
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_STATEMENT_JOIN + WHERE_STATEMENT_NUMBER + NOT_DELETES + ORDER_BY_INGREDIENT_PERCENTAGE)
    List<Ingredient> findByIngredientCodeOrderByStatementDetails(@Param("statementNumber") Long statementNumber);
}
