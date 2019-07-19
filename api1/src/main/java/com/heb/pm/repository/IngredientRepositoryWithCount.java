package com.heb.pm.repository;

import com.heb.pm.entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve ingredient information based on different criteria.
 *
 * @author m594201
 * @since 2.0.9
 */
public interface IngredientRepositoryWithCount extends JpaRepository<Ingredient, Long>, IngredientRepositoryWithCountDelegate {

    /**
     * Returns a pageable list of ingredients that are super-ingredients for a requested ingredient.
     *
     * @param ingredientCode The ingredient code that is a sub-ingredient to search for.
     * @param pageRequest The page request.
     * @return A list of ingredients which contain ingredientCode as a sub-ingredient.
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_SOI_JOIN + WHERE_SOI + ORDER_BY_INGREDIENT_CODE_NUMBER)
    Page<Ingredient> findBySoiChild(@Param("ingredientCode")String ingredientCode, Pageable pageRequest);

    /**
     * Find by ingredient code in page.
     *
     * @param ingredientCodes the ingredient codes
     * @param pageRequest     the page request
     * @return the page
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_CODE_SEARCH + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
    Page<Ingredient> findByIngredientCodeIn(@Param("ingredientCodes") List<String> ingredientCodes,  Pageable pageRequest);

    /**
     * Find by ingredient description containing page.
     *
     * @param description the description
     * @param request     the request
     * @return the page
     */
    @Query(value = INGREDIENT_SELECT + DESCRIPTION_SEARCH + EXT_DESCRIPTION + NOT_DELETES)
    Page<Ingredient> findByIngredientDescriptionContaining(@Param("description")String description, Pageable request);

    /**
     * Find by ingredient code in and category code page.
     *
     * @param ingredientCodes the ingredient codes
     * @param categoryCode    the category code
     * @param pageRequest     the page request
     * @return the page
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_CODE_SEARCH + NOT_DELETES + INCLUDE_CATEGORY + ORDER_BY_INGREDIENT_CODE_NUMBER)
    Page<Ingredient> findByIngredientCodeInAndCategoryCode(@Param("ingredientCodes") List<String> ingredientCodes, @Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by ingredient code in and category code not page.
     *
     * @param ingredientCodes the ingredient codes
     * @param categoryCode    the category code
     * @param pageRequest     the page request
     * @return the page
     */
    @Query(value = INGREDIENT_SELECT + INGREDIENT_CODE_SEARCH + NOT_DELETES + EXCLUDE_CATEGORY + ORDER_BY_INGREDIENT_CODE_NUMBER)
    Page<Ingredient> findByIngredientCodeInAndCategoryCodeNot(@Param("ingredientCodes") List<String> ingredientCodes, @Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by ingredient description containing and category code page.
     *
     * @param description  the description
     * @param categoryCode the category code
     * @param pageRequest  the page request
     * @return the page
     */
    @Query(value = INGREDIENT_SELECT + DESCRIPTION_SEARCH + EXT_DESCRIPTION + NOT_DELETES + INCLUDE_CATEGORY)
    Page<Ingredient> findByIngredientDescriptionContainingAndCategoryCode(@Param("description") String description,@Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find by ingredient description containing and category code not page.
     *
     * @param description  the description
     * @param categoryCode the category code
     * @param pageRequest  the page request
     * @return the page
     */
    @Query(value = INGREDIENT_SELECT + DESCRIPTION_SEARCH + EXT_DESCRIPTION + NOT_DELETES + EXCLUDE_CATEGORY)
    Page<Ingredient> findByIngredientDescriptionContainingAndCategoryCodeNot(@Param("description") String description,@Param("categoryCode") Long categoryCode, Pageable pageRequest);

    /**
     * Find all by page.
     * @param request the page request
     * @return the page
     */
    Page<Ingredient> findByMaintFunctionNot(String maintFunction, Pageable request);

    /**
     * Find by category code.
     * @param categoryCode category code to include
     * @param request the page request
     * @return the page
     */
	@Query(value = INGREDIENT_SELECT + WHERE_INCLUDE_CATEGORY + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
	Page<Ingredient> findByCategoryCode(@Param("categoryCode") Long categoryCode, Pageable request);

    /**
     * Find where category code is excluded.
     * @param categoryCode category code to exclude
     * @param request the page request
     * @return the page
     */
	@Query(value = INGREDIENT_SELECT + WHERE_EXCLUDE_CATEGORY + NOT_DELETES + ORDER_BY_INGREDIENT_CODE_NUMBER)
	Page<Ingredient> findByCategoryCodeNot(@Param("categoryCode") Long categoryCode, Pageable request);
}
