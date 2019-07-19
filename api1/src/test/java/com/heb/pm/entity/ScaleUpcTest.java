/*
 *  ScaleUpcTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDate;

/**
 * @author s573181
 * @since 2.0.7
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ScaleUpcTest {

	@Autowired
	private ScaleUpcRepositoryTest repository;

	/*
	 * JPA Mapping
	 */
	@Transactional
	@Test
	public void mapping() {

		ScaleUpc scaleUpc = this.repository.findOne(20260600000L);
		Assert.assertEquals(0, scaleUpc.getFirstLabelFormat().getFormatCode());
		Assert.assertEquals("DEFAULT LABEL FORMAT          ", scaleUpc.getFirstLabelFormat().getDescription());

		Assert.assertEquals(46, scaleUpc.getSecondLabelFormat().getFormatCode());
		Assert.assertEquals("BEEF                          ", scaleUpc.getSecondLabelFormat().getDescription());
	}

	/**
	 * Tests getUpc.
	 */
	@Test
	public void getUpc() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getUpc(),20260600000L);
	}

	/**
	 * Tests getEffectiveDate.
	 */
	@Test
	public void getEffectiveDate() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getEffectiveDate(), LocalDate.of(2014,03,17));
	}


	/**
	 * Tests getShelfLifeDays.
	 */
	@Test
	public void getShelfLifeDays() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getShelfLifeDays(), 999);
	}

	/**
	 * Tests getFreezeByDays.
	 */
	@Test
	public void getFreezeByDays() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getFreezeByDays(), 0);
	}


	/**
	 * Tests getEatByDays.
	 */
	@Test
	public void getEatByDays() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getEatByDays(),0);
	}


	/**
	 * Test getActionCode.
	 */
	@Test
	public void getActionCode() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getActionCode(),0);
	}


	/**
	 * Tests getGraphicsCode.
	 */
	@Test
	public void getGraphicsCode() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getGraphicsCode(),0);
	}

	/**
	 * Tests getLabelFormatOne.
	 */
	@Test
	public void getLabelFormatOne() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getLabelFormatOne(),0);
	}

	/**
	 * Tests getLabelFormatTwo.
	 */
	@Test
	public void getLabelFormatTwo() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getLabelFormatTwo(),0);
	}


	/**
	 * Tests getGrade.
	 */
	@Test
	public void getGrade() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getGrade(),0);
	}


	/**
	 * Tests getServiceCounterTare.
	 */
	@Test
	public void getServiceCounterTare() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertTrue(scaleUpc.getServiceCounterTare()==0.000);
	}

	/**
	 *  Tests getIngredientStatement.
	 */
	@Test
	public void getIngredientStatement() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getIngredientStatement(),9999);
	}


	/**
	 * Tests getNutrientStatement.
	 */
	@Test
	public void getNutrientStatement() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getNutrientStatement(),0);
	}

	/**
	 * Tests getPrePackTare.
	 */
	@Test
	public void getPrePackTare() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertTrue(scaleUpc.getPrePackTare() == 0.000);
	}

	/**
	 * Tests getNetWeight.
	 */
	@Test
	public void getNetWeight() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertTrue(scaleUpc.getNetWeight()==0.000);
	}

	/**
	 * Tests isForceTare.
	 */
	@Test
	public void isForceTare() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertFalse(scaleUpc.isForceTare());
	}

	/**
	 * Tests isPriceOverride.
	 */
	@Test
	public void isPriceOverride() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertFalse(scaleUpc.isPriceOverride());
	}

	/**
	 * Tests getEnglishDescriptionOne.
	 */
	@Test
	public void getEnglishDescriptionOne() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getEnglishDescriptionOne(),"JUAN CANARY");
	}


	/**
	 * Tests getEnglishDescriptionTwo.
	 */
	@Test
	public void getEnglishDescriptionTwo() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getEnglishDescriptionTwo(),"MELON");
	}


	/**
	 * Tests getEnglishDescriptionThree.
	 */
	@Test
	public void getEnglishDescriptionThree() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getEnglishDescriptionThree(),"                                                  ");
	}

	/**
	 * Tests getEnglishDescriptionFour.
	 */
	@Test
	public void getEnglishDescriptionFour() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getEnglishDescriptionFour(),"                                                  ");
	}


	/**
	 * Tests getSpanishDescriptionOne.
	 */
	@Test
	public void getSpanishDescriptionOne() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getSpanishDescriptionOne(), " ");
	}


	/**
	 * Tests getSpanishDescriptionTwo.
	 */
	@Test
	public void getSpanishDescriptionTwo() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getSpanishDescriptionTwo(), " ");
	}


	/**
	 * Tests getSpanishDescriptionThree.
	 */
	@Test
	public void getSpanishDescriptionThree() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getSpanishDescriptionThree(), "                                                  ");
	}



	/**
	 * Tests getSpanishDescriptionFour.
	 */
	@Test
	public void getSpanishDescriptionFour() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getSpanishDescriptionFour(), "                                                  ");
	}


	/**
	 * Tests getPlu.
	 */
	@Test
	public void getPlu() {
		ScaleUpc scaleUpc = this.getDefaultRecord();
		Assert.assertEquals(scaleUpc.getPlu(), 2606);
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a ScaleUpc object that is equal to the first one in the test table.
	 *
	 * @return Returns a ScaleUpc object that is equal to the first one in the test table.
	 */
	private ScaleUpc getDefaultRecord() {
		ScaleUpc scaleUpc = new ScaleUpc();
		scaleUpc.setUpc(20260600000L);
		scaleUpc.setEffectiveDate(LocalDate.of(2014,03,17));
		scaleUpc.setServiceCounterTare(0.000);
		scaleUpc.setPrePackTare(0.000);
		scaleUpc.setShelfLifeDays(999);
		scaleUpc.setEatByDays(0);
		scaleUpc.setFreezeByDays(0);
		scaleUpc.setIngredientStatement(9999);
		scaleUpc.setNutrientStatement(0);
		scaleUpc.setActionCode(0);
		scaleUpc.setGraphicsCode(0);
		scaleUpc.setEnglishDescriptionOne("JUAN CANARY");
		scaleUpc.setEnglishDescriptionTwo("MELON");
		scaleUpc.setSpanishDescriptionOne(" ");
		scaleUpc.setSpanishDescriptionTwo(" ");
		scaleUpc.setLabelFormatOne(0);
		scaleUpc.setLabelFormatTwo(0);
		scaleUpc.setForceTare(false);
		scaleUpc.setGrade(0);
		scaleUpc.setNetWeight(0.000);
		scaleUpc.setEnglishDescriptionThree("                                                  ");
		scaleUpc.setEnglishDescriptionFour("                                                  ");
		scaleUpc.setSpanishDescriptionThree("                                                  ");
		scaleUpc.setSpanishDescriptionFour("                                                  ");
		scaleUpc.setPriceOverride(false);
		return scaleUpc;
	}
}
