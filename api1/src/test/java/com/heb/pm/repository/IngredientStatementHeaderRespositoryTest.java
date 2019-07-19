/*
 *  IngredientStatementHeaderRespositoryTest
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.IngredientStatementHeader;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Tests IngredientStatementHeaderRespository.
 * @author s573181
 * @since 2.2.0
 */
public interface IngredientStatementHeaderRespositoryTest extends JpaRepository<IngredientStatementHeader, Long> {
}
