package com.heb.pm.repository;

import com.heb.pm.entity.IngredientCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository to retrieve ingredient category.
 *
 * @author m594201
 * @since 2.0.9
 */
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long>,
		IngredientCategoryRepositoryDelegate {
	/**
	 * Returns requested list with all Ingredient Category information.
	 *
	 * @param request The requested page with all Ingredient Category information.
	 * @return  A List with all Ingredient Category information.
	 */
	@Query(value = SELECT + QUERY_ALL)
	List<IngredientCategory> findAllByPage(Pageable request);

	/**
	 * Returns a list of ingredient categories based on the category codes.
	 *
	 * @param categoryCodes The category codes requested.
	 * @param request The page requested.
	 * @return a list of ingredient categories based on the category codes.
	 */
	List<IngredientCategory> findByCategoryCodeIn(List<Long> categoryCodes, Pageable request);

	/**
	 * Finds the Ingredient Category with the highest category code.
	 *
	 * @return the Ingredient Category with the highest category code.
	 */
	IngredientCategory findFirstByOrderByCategoryCodeDesc();

}
