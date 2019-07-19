package com.heb.pm.repository;

import com.heb.pm.entity.Nutrient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about an nutrition.
 * @author m594201
 * @since 2.1.0
 */
public interface NutrientRepository extends JpaRepository<Nutrient, Long>, NutrientRepositoryDelegate {

	String NUTRIENT_CODE_AND_NUTRIENT_DESCRIPTION_LIKE_QUERY =
			"select n from Nutrient n where (to_char(n.nutrientCode) like :searchString and n.nutrientCode not in (:doNotIncludeList)) or (upper(n.nutrientDescription) like upper(:searchString) and (n.nutrientCode not in  (:doNotIncludeList))) ";

	/**
	 * Find by nutrient code in list.
	 *
	 * @param nutrientCodes   the nutrient codes
	 * @param deleteCode      the delete code
	 * @param nutrientRequest the nutrient request
	 * @return the list
	 */
	List<Nutrient> findByNutrientCodeInAndMaintenanceSwitchNot(List<Long> nutrientCodes, char deleteCode, Pageable nutrientRequest);

	/**
	 * Find all by page list.
	 *
	 * @param request the request
	 * @return the list
	 */
	List<Nutrient> findByMaintenanceSwitchNot(char maintenanceSwitch, Pageable request);

	/**
	 * Finds first nutrient code.
	 *
	 * @return nutrient code
	 */
	Nutrient findFirstByOrderByNutrientCodeDesc();

	/**
	 * Find by nutrient code and nutrient code not in or nutrient description ignore case and nutrient code not in list.
	 *
	 * @param searchStringNumber       the search string number
	 * @param currentNutrientCodeList  the current nutrient code list
	 * @param searchString             the search string
	 * @param currentNutrientCodeList1 the current nutrient code list 1
	 * @param pageRequest              the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientCodeAndNutrientCodeNotInOrNutrientDescriptionIgnoreCaseAndNutrientCodeNotIn(long searchStringNumber, List<Long> currentNutrientCodeList, String searchString, List<Long> currentNutrientCodeList1, Pageable pageRequest);

	/**
	 * Find by nutrient description ignore case and nutrient code not in list.
	 *
	 * @param searchString            the search string
	 * @param currentNutrientCodeList the current nutrient code list
	 * @param pageRequest             the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientDescriptionIgnoreCaseAndNutrientCodeNotIn(String searchString, List<Long> currentNutrientCodeList, Pageable pageRequest);

	/**
	 * Find by nutrient code and nutrient description ignore case list.
	 *
	 * @param searchStringNumber the search string number
	 * @param searchString       the search string
	 * @param pageRequest        the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientCodeAndNutrientDescriptionIgnoreCase(long searchStringNumber, String searchString, PageRequest pageRequest);

	/**
	 * Find by nutrient description ignore case list.
	 *
	 * @param searchString the search string
	 * @param pageRequest  the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientDescriptionIgnoreCase(String searchString, PageRequest pageRequest);

	/**
	 * Find by nutrient code containing and nutrient code not in or nutrient description containing ignore case and nutrient code not in list.
	 *
	 * @param searchString             the search string
	 * @param currentNutrientCodeList1 the current nutrient code list 1
	 * @param pageRequest              the page request
	 * @return the list
	 */
	@Query(value = NutrientRepository.NUTRIENT_CODE_AND_NUTRIENT_DESCRIPTION_LIKE_QUERY)
	List<Nutrient> findByNutrientCodeContainingAndNutrientCodeNotInOrNutrientDescriptionContainingIgnoreCaseAndNutrientCodeNotIn(@Param("searchString") String searchString,@Param("doNotIncludeList") List<Long> currentNutrientCodeList1, Pageable pageRequest);

	/**
	 * Find by nutrient description containing ignore case and nutrient code not in list.
	 *
	 * @param searchString            the search string
	 * @param currentNutrientCodeList the current nutrient code list
	 * @param pageRequest             the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientDescriptionContainingIgnoreCaseAndNutrientCodeNotIn(String searchString, List<Long> currentNutrientCodeList, Pageable pageRequest);

	/**
	 * Find by nutrient code containing and nutrient description containing ignore case list.
	 *
	 * @param searchStringNumber the search string number
	 * @param searchString       the search string
	 * @param pageRequest        the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientCodeContainingAndNutrientDescriptionContainingIgnoreCase(long searchStringNumber, String searchString, PageRequest pageRequest);

	/**
	 * Find by nutrient description containing ignore case list.
	 *
	 * @param searchString the search string
	 * @param pageRequest  the page request
	 * @return the list
	 */
	List<Nutrient> findByNutrientDescriptionContainingIgnoreCase(String searchString, PageRequest pageRequest);

	/**
	 * Find by fed lbl sequence list.
	 *
	 * @param i the
	 * @return the list
	 */
	List<Nutrient> findByFedLblSequence(double i);

	/**
	 * Find by fed lbl sequence not list.
	 *
	 * @param i the
	 * @return the list
	 */
	List<Nutrient> findByFedLblSequenceNot(double i);

    /**
     * Find by Uom Code.
     *
     * @param uomCode the value being searched by.
     * @return the list
     */
	List<Nutrient> findByUomCode(long uomCode, Pageable pageRequest);
}
