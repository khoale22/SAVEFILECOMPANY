/*
 *  CustomerProductChoiceRepository
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.CustomerProductChoice;
import com.heb.pm.entity.CustomerProductChoiceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This is the repository for CustomerProductChoice.
 *
 * @author l730832
 * @since 2.14.0
 */
public interface CustomerProductChoiceRepository extends JpaRepository<CustomerProductChoice, CustomerProductChoiceKey> {

	/**
	 * finds  a list of customer product choices by their product id and picker switch.
	 * @param productId
	 * @return list of customer product choices.
	 */
	List<CustomerProductChoice> findByKeyProductIdAndProductGroupChoiceTypePickerSwitch(Long productId,Boolean picker);

	/**
	 * finds  a list of customer product choices by their product id.
	 * @param productId
	 * @return list of customer product choices.
	 */
	List<CustomerProductChoice> findByKeyProductId(Long productId);

	/**
	 * finds  a list of customer product choices by their product id.
	 * @param productId
	 * @param productGroupTypeCode
	 * @return list of customer product choices.
	 */
	List<CustomerProductChoice> findByKeyProductIdAndKeyProductGroupTypeCode(Long productId, String productGroupTypeCode);

	/**
	 * Find CustomerProductChoice by productId, productGroupTypeCode, ChoiceTypeCode and ChoiceOptionCode.
	 *
	 * @param productId the product id.
	 * @param productGroupTypeCode the productGroupTypeCode
	 * @param choiceTypeCode the ChoiceTypeCode
	 * @param choiceOptionCode the choiceOptionCode.
	 * @return the list of CustomerProductChoices.
	 */
	List<CustomerProductChoice> findByKeyProductIdAndKeyProductGroupTypeCodeAndKeyChoiceTypeCodeAndKeyChoiceOptionCode(Long productId, String productGroupTypeCode, String choiceTypeCode, String choiceOptionCode);

	/**
	 * Find CustomerProductChoice by productGroupTypeCode, choiceTypeCode.
	 *
	 * @param productGroupTypeCode the productGroupTypeCode
	 * @param choiceTypeCode the ChoiceTypeCode
	 * @return the list of CustomerProductChoices.
	 */
	List<CustomerProductChoice> findByKeyProductGroupTypeCodeAndKeyChoiceTypeCode(String productGroupTypeCode,String choiceTypeCode);

	/**
	 * Find CustomerProductChoice by choiceTypeCode, list of choiceOptionCode.
	 *
	 * @param choiceTypeCode the ChoiceTypeCode.
	 * @param choiceOptionCodeList the list of choiceOptionCode.
	 * @return the list of CustomerProductChoices.
	 */
	List<CustomerProductChoice> findByKeyChoiceTypeCodeAndKeyChoiceOptionCodeIn(String choiceTypeCode, List<String> choiceOptionCodeList);

}
