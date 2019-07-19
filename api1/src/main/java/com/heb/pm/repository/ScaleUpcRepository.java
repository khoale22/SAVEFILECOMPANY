package com.heb.pm.repository;

import com.heb.pm.entity.ScaleUpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for scale upc without count.
 *
 * Created by l730832 on 10/27/2016.
 * @since 2.0.8
 */
public interface ScaleUpcRepository extends JpaRepository<ScaleUpc, Long> {

	/**
	 * Search ScaleUpc by description.
	 *
	 * @param queryString1 The first query string.
	 * @param queryString2 The second query string.
     * @param queryString3 The third query string.
     * @param queryString4 The fourth query string.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleUpc. Will include total available
	 * record counts and number of available pages.
	 */
	List<ScaleUpc> findByEnglishDescriptionOneContainsOrEnglishDescriptionTwoContainsOrEnglishDescriptionThreeContainsOrEnglishDescriptionFourContains(
			@Param("queryString1") String queryString1,
            @Param("queryString2") String queryString2,
            @Param("queryString3") String queryString3,
            @Param("queryString4") String queryString4,
			Pageable pageRequest);

	/**
	 * Search ScaleUpc by graphics Code.
	 *
	 * @param graphicsCode The first query string.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated list with records from ScaleUpc.
	 */
	List<ScaleUpc> findByGraphicsCode(@Param("graphicsCode") long graphicsCode,Pageable pageRequest);

	/**
	 * Search ScaleUpc by description.
	 *
	 * @param upcList The list of upcs to search for.
	 * @param pageRequest Page information to include page, page size, and sort order.
	 * @return A populated Page with records from ScaleUpc. Will include total available
	 * record counts and number of available pages.
	 */
	List<ScaleUpc> findByUpcIn(@Param("upcList") List<Long> upcList, Pageable pageRequest);

	/**
	 * Find by label format one list.
	 *
	 * @param firstLabelFormat the first label format
	 * @param pageRequest      the page request
	 * @return the list
	 */
	List<ScaleUpc> findByLabelFormatOne(@Param("firstLabelFormat") long firstLabelFormat,
										Pageable pageRequest);

	/**
	 * Find by label format two list.
	 *
	 * @param secondLabelFormat the second label format
	 * @param pageRequest       the page request
	 * @return the list
	 */
	List<ScaleUpc> findByLabelFormatTwo(@Param("secondLabelFormat") long secondLabelFormat,
										Pageable pageRequest);

	/**
	 * Search ScaleUpc by ActionCode.
	 *
	 * @param actionCode the ActionCode.
	 * @param pageRequest  Page information to include page, page size, and sort order.
	 * @return A List with records from ScaleUpc.
	 */
	List<ScaleUpc> findByActionCode(Long actionCode, Pageable pageRequest);

	/**
	 *
	 * @param nutrientStatement
	 * @return
	 */
	List<ScaleUpc> findByNutrientStatement(Long nutrientStatement);

	/**
	 * Returns a list of scale UPCs that have a looked for ingredient statement number.
	 *
	 * @param ingredientStatement The ingredient statement number to look for.
	 * @param pageRequest Page request to include page, page size, and sort order.
	 * @return A list of scale UPCs with a given ingredient statement number.
	 */
	List<ScaleUpc> findByIngredientStatement(@Param("ingredientStatement") Long ingredientStatement,
											 Pageable pageRequest);

	/**
	 * Returns a list of scale UPCs that have ingredient statements not belonging ot a list.
	 *
	 * @param ingredientStatements The list of ingredient statements to ignore.
	 * @param pageRequest Page request to include page, page size, and sort order.
	 * @return A list of scale UPCs that have ingredient statements not belonging ot a list.
	 */
	List<ScaleUpc> findByIngredientStatementNotIn(List<Long> ingredientStatements, Pageable pageRequest);

	/**
	 * Returns a list of scale UPCs that have a looked for ingredient statement number.
	 * @param ingredientStatement The ingredient statement number to look for.
	 * @return A list of scale UPCs with a given ingredient statement number.
	 */
	List<ScaleUpc> findByIngredientStatement(Long ingredientStatement);

	/**
	 * Returns a page of scale UPCs that are tied to the given department.
	 *
	 * @param department Department to look for.
	 * @param pageRequest Page request to include page, page size, and sort order.
	 * @return A list of scale UPCs that have ingredient statements not belonging ot a list.
	 */
	Page<ScaleUpc> findByAssociateUpcSellingUnitProductMasterSubDepartmentKeyDepartment(String department, Pageable pageRequest);

}
