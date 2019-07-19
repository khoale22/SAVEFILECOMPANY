/*
 *  NutrientRoundingRuleRepositoryTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.NutrientRoundingRule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository to test NutrientRoundingRule JPA functions.
 *
 * @author s5731810
 * @since 2.1.0
 */
public interface NutrientRoundingRuleRepositoryTest extends JpaRepository<NutrientRoundingRule, Integer> {
}

