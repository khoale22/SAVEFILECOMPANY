package com.heb.pm.repository;

import com.heb.pm.entity.IngredientStatementHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve Ingredient Statement Header
 *
 * @author m594201
 * @since 2.0.9
 */
public interface IngredientStatementHeaderRepository extends JpaRepository<IngredientStatementHeader, Long>{

	char INGREDIENT_LIST_DELIMETER = '|';
	char REGEX_MATCH_ALL = '%';

	/**
	 * Returns a pageable list of all IngredientStatementHeader.
	 *
	 * @param pageable The page request.
	 * @return A pageable list of IngredientStatementHeader.
	 */
	@Query(value = "select ish from IngredientStatementHeader ish where ish.maintenanceCode <> 'D'")
	List<IngredientStatementHeader> findAllByPage(Pageable pageable);


	/**
	 * Find by maintenance code not list.
	 *
	 * @param maintenanceCode the maintenance code
	 * @param pageable        the pageable
	 * @return the list
	 */
	List<IngredientStatementHeader> findByMaintenanceCodeNot(char maintenanceCode, Pageable pageable);

	/**
	 * Returns a pageable list of IngredientStatementHeader by StatementNumber.
	 *
	 * @param pageable The page request.
	 * @return A pageable list of IngredientStatementHeader.
	 */
	List<IngredientStatementHeader> findByStatementNumberInAndMaintenanceCodeNot(List<Long> statementNumber, char deleteCode, Pageable pageable);

	/**
	 * Returns a pageable list of IngredientStatementHeader by ingredientCode.
	 *
	 * @param pageable The page request.
	 * @return A pageable list of IngredientStatementHeader.
	 */
	List<IngredientStatementHeader> findByIngredientStatementDetailsIngredientIngredientCodeInAndMaintenanceCodeNot(
			List<String> ingredientCodes, char deleteCode, Pageable pageable);

	/**
	 * Finds the IngredientStatementHeader with the highest statement number.
	 *
	 * @return the IngredientStatementHeader with the highest statement number.
	 */
	IngredientStatementHeader findFirstByOrderByStatementNumberDesc();

	/**
	 * Returns a list of IngredientStatementHeader by ingredientCode. Used to calculate hits.
	 *
	 * @return A List of IngredientStatementHeader requested.
	 */
	List<IngredientStatementHeader> findByIngredientStatementDetailsIngredientIngredientCodeInAndMaintenanceCodeNot(
			List<String> ingredientCodes, char deleteCode);

	/**
	 * Returns a list of IngredientStatementHeader by StatementNumber and is not codes maintenance.
	 *
	 * @param ingredientCodes a list of statement number.
	 * @param deleteCode the maintenance code
	 * @return A list of IngredientStatementHeader.
	 */
	List<IngredientStatementHeader> findByStatementNumberInAndMaintenanceCodeNot(
			List<Long> ingredientCodes, char deleteCode);

	/**
	 * Finds the first 100 statement numbers greater than or equal to the statement number provided. Used to find
	 * the next missing statement number.
	 *
	 * @param statementNumber The statement number.
	 * @return The first 100 statement numbers greater than or equal to the statement number provided.
	 */
	List<IngredientStatementHeader> findFirst100ByStatementNumberGreaterThanEqualOrderByStatementNumber(Long statementNumber);

	/**
	 * Returns a list of IngredientStatementHeader by ingredient description.
	 *
	 * @param pageable The page request.
	 * @return A page of IngredientStatementHeader requested.
	 */
	List<IngredientStatementHeader> findDistinctByIngredientStatementDetailsIngredientIngredientDescriptionIgnoreCaseContainingAndMaintenanceCodeNot(
			String description, char deleteCode, Pageable pageable);

	/**
	 * Returns a list of IngredientStatementHeaders that contain all of the requested ingredient codes
	 * in no particular order.
	 *
	 * @param ingredientCodes The ingredient codes to search for. The method will only return ingredient statemetns with
	 *                        all the requested ingredients.
	 * @param ingredientCount The number of ingredient codes in the list.
	 * @param pageable The page request.
	 * @param ignoreMaintenanceCode A delete maintenance code to ignore.
	 * @return A page of IngredientStatementHeaders.
	 */
	@Query(value="  select ish from IngredientStatementHeader ish where ish.statementNumber in (" +
			"select isd.key.statementNumber " +
			"from IngredientStatementDetail isd " +
			"where isd.key.ingredientCode in :ingredientCodes " +
			"group by isd.key.statementNumber " +
			"having count(isd.key.ingredientCode) = :ingredientCount) " +
			"and ish.maintenanceCode <> :ignoreMaintenanceCode")
	List<IngredientStatementHeader> findByIngredientCodesMatchingAll(@Param("ingredientCodes")List<String> ingredientCodes,
														  @Param("ingredientCount")long ingredientCount,
														  @Param("ignoreMaintenanceCode")char ignoreMaintenanceCode,
														  Pageable pageable);

	/**
	 * Returns all IngredientStatementHeaders with the ingredients in the list.
	 *
	 * @param ingredientCodeList
	 * @return
	 */
	List<IngredientStatementHeader> findByIngredientStatementDetailsKeyIngredientCodeIn(List<String> ingredientCodeList);

	/**
	 * Returns a list of ingredient statements that have an ingredient code as one of the ingredients in it's list.
	 *
	 * @param ingredientCode The ingredient code to look for.
	 * @return A list of ingredient statements with that ingredient as a top-level ingredient.
	 */
	List<IngredientStatementHeader> findByIngredientStatementDetailsKeyIngredientCode(String ingredientCode);

	// DB2ToOracle - this was my preferred method, but could not get it to replace the schema in the SQL, so had
	// to write a native query in the class itself. Work with me on porting to put it back to this way.
	/**
	 * Search for a list of ingredient statements that contain a list of ingredients as their fist ingredients. To call
	 * this method, pass in a list of ingredient codes formatted at [code 1]|[code 2]|%. There are constants defined
	 * for the delimiter (|) and the match all regular expression (%).
	 *
	 * Note that, thought the method is defined as returning a list of Longs, in practice, it is returning BigDecimals,
	 * at least from DB2.
	 *
	 * @param ingredientStatementRegularExpression The regular expression with the codes to search for formatted as
	 *                                             described above.
	 * @return A list of ingredient statement numbers that contain those ingredient codes as the first ingredients
	 * in the statement.
	 */
	@Query(value = "select pd_ingrd_stmt_no from ( " +
			"select pd_ingrd_stmt_no,  replace(replace(replace(cast(XMLSERIALIZE(CONTENT XMLAGG(XMLELEMENT(NAME \"x\",rtrim(pd_ingrd_cd)  )order by pd_ingrd_pct desc ) " +
			") as varchar(4000)),  '</x><x>', '|') , '<x>', '') , '</x>', '') as ingrdcodes " +
			"from EMD.sl_ingrd_stmt_dtl a " +
			"group by pd_ingrd_stmt_no ) a where ingrdcodes like ?1 ", nativeQuery = true)
	List<Long> findIngredientStatementIdsWithSignificantOrder(String ingredientStatementRegularExpression);
}
