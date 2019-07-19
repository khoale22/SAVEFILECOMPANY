/*
 *  IngredientStatementHeaderTest
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import com.heb.pm.repository.IngredientStatementHeaderRepository;
import com.heb.pm.repository.IngredientStatementHeaderRespositoryTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests IngredientStatementHeader entity.
 * @author s573181
 * @since 2.2.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class IngredientStatementHeaderTest {

	private static final long TEST_STATEMENT = 5L;

	@Autowired
	private IngredientStatementHeaderRespositoryTest repository;

	/**
	 * JPA mapping.
	 */
	@Test
	public void mappingUpc() {
		IngredientStatementHeader header = this.repository.findOne(TEST_STATEMENT);
		Assert.assertEquals(TEST_STATEMENT, header.getStatementNumber());
	}

	/**
	 * Tests getStatementNumber.
	 */
	@Test
	public void getStatementNumber() {

		IngredientStatementHeader header = this.repository.findOne(TEST_STATEMENT);
		Assert.assertEquals(TEST_STATEMENT, header.getStatementNumber());
	}

	/**
	 * Tests getMaintenanceDate.
	 */
	@Test
	public void getMaintenanceDate() {
		Assert.assertEquals(this.getDefaultRecord().getMaintenanceDate(), LocalDate.of(2014,03,17));
	}

	/**
	 * Tests isMaintenanceSwitch.
	 */
	@Test
	public void isMaintenanceSwitch() {
		Assert.assertEquals(this.getDefaultRecord().isMaintenanceSwitch(), true);
	}


	/**
	 * Tests getMaintenanceCode.
	 */
	@Test
	public void getMaintenanceCode() {
		Assert.assertEquals(this.getDefaultRecord().getMaintenanceCode(), 'C');
	}

	/**
	 * Returns a default IngredientStatementHeader to test upon.
	 * @return a default IngredientStatementHeader to test upon.
	 */
	private IngredientStatementHeader getDefaultRecord(){

		IngredientStatementHeader header = new IngredientStatementHeader();
		header.setMaintenanceCode('C');
		header.setStatementNumber(1);
		header.setMaintenanceDate(LocalDate.of(2014,03,17));
		header.setMaintenanceSwitch(true);
		return header;
	}
}
