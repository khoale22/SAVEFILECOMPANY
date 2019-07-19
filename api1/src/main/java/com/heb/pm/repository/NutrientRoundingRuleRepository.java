/*
 *  NutrientRoundingRuleRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.NutrientRoundingRule;
import com.heb.pm.entity.NutrientRoundingRuleKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for data from the pd_nutrient_rules table.
 *
 * @author s573181
 * @since 2.1.0
 */
public interface NutrientRoundingRuleRepository extends JpaRepository<NutrientRoundingRule, NutrientRoundingRuleKey> {

	/**
	 * Finds rounding rules by the nutrient code.
	 *
	 * @param nutrientCode the nutrient code.
	 * @return a List of rounding rules.
	 */
	List<NutrientRoundingRule> findByKeyNutrientCode(int nutrientCode);
}
