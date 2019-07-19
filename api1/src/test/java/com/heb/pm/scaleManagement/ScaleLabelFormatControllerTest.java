/*
 *  ScaleLabelFormatControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.ScaleLabelFormat;
import com.heb.pm.entity.ScaleUpc;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 /**
 * Test class for ScaleLabelFormatServiceTest.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleLabelFormatControllerTest {

	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final int testInt = 0;
	private static final Long testLongObject = 0L;
	private static final LocalDate testLocalDate = LocalDate.now();
	private static final double testDouble = 0.0;
	private static final boolean testBoolean = false;

	@InjectMocks
	private ScaleLabelFormatController scaleLabelFormatController;

	@Mock
	private ScaleLabelFormatService service;

	@Mock
	private UserInfo userInfo;

	@Mock
	private NonEmptyParameterValidator parameterValidator;

	@Mock
	private MessageSource messageSource;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test find all.
	 */
	@Test
	public void testFindAll() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(testInt, testInt, testLongObject, this.getDefaultScaleLabelFormatList());

		Mockito.when(this.service.findAllLabelFormats(Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatController.findAll(false, testInt, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
		Assert.assertEquals(pageableResult, returnResult);
	}

	/**
	 * Test find by description.
	 */
	@Test
	public void testFindByDescription() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(testInt, testInt, testLongObject, this.getDefaultScaleLabelFormatList());

		Mockito.when(this.service.findByDescription(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatController.findByDescription(testString,false, testInt, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
		Assert.assertEquals(pageableResult, returnResult);
	}

	/**
	 * Test find by format codes.
	 */
	@Test
	public void testFindByFormatCodes() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(testInt, testInt, testLongObject, this.getDefaultScaleLabelFormatList());

		List<Long> formatCodes = new ArrayList<>();
		formatCodes.add(this.getDefaultScaleLabelFormatList().get(0).getFormatCode());
		Mockito.when(this.service.findByFormatCode(Mockito.anyListOf(Long.class), Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatController.findByFormatCodes(formatCodes,false, testInt, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
		Assert.assertEquals(pageableResult, returnResult);
	}

	/**
	 * Test find upcs by format code one.
	 */
	@Test
	public void testFindUpcsByFormatCodeOne() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(testInt, testInt, testLongObject, this.getDefaultScaleUpcList());

		Mockito.when(this.service.findUpcsByFormatCodeOne(Mockito.any(Long.class), Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleUpc> returnResult = this.scaleLabelFormatController.findUpcsByFormatCodeOne(
				this.getDefaultScaleLabelFormatList().get(0).getFormatCode(),false, testInt, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
		Assert.assertEquals(pageableResult, returnResult);
	}

	/**
	 * Test find upcs by format code two.
	 */
	@Test
	public void testFindUpcsByFormatCodeTwo() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(testInt, testInt, testLongObject, this.getDefaultScaleUpcList());

		Mockito.when(this.service.findUpcsByFormatCodeTwo(Mockito.any(Long.class), Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleUpc> returnResult = this.scaleLabelFormatController.findUpcsByFormatCodeTwo(
				this.getDefaultScaleLabelFormatList().get(0).getFormatCode(),false, testInt, testInt, CommonMocks.getServletRequest());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
		Assert.assertEquals(pageableResult, returnResult);
	}

	/**
	 * Test update scale label format.
	 */
	@Test
	public void testUpdateScaleLabelFormat() {
		ModifiedEntity<ScaleLabelFormat> modifiedEntity = new ModifiedEntity<>(this.getDefaultScaleLabelFormatList().get(0), testString);

		ScaleLabelFormat updatedFormatCode = this.getDefaultScaleLabelFormatList().get(0);
		Mockito.when(this.service.update(Mockito.any(ScaleLabelFormat.class))).thenReturn(updatedFormatCode);
		ModifiedEntity<ScaleLabelFormat> returnEntity = this.scaleLabelFormatController.updateScaleLabelFormat(updatedFormatCode, CommonMocks.getServletRequest());
		Assert.assertEquals(updatedFormatCode, returnEntity.getData());
		Assert.assertEquals(modifiedEntity.getData(), returnEntity.getData());

	}

	/**
	 * Test add scale label format.
	 */
	@Test
	public void testAddScaleLabelFormat() {
		ModifiedEntity<ScaleLabelFormat> modifiedEntity = new ModifiedEntity<>(this.getDefaultScaleLabelFormatList().get(0), testString);

		ScaleLabelFormat updatedFormatCode = this.getDefaultScaleLabelFormatList().get(0);
		Mockito.when(this.service.add(Mockito.any(Long.class), Mockito.anyString())).thenReturn(updatedFormatCode);
		ModifiedEntity<ScaleLabelFormat> returnEntity = this.scaleLabelFormatController.addScaleLabelFormat(updatedFormatCode, CommonMocks.getServletRequest());
		Assert.assertEquals(updatedFormatCode, returnEntity.getData());
		Assert.assertEquals(modifiedEntity.getData(), returnEntity.getData());

	}

	/**
	 * Test delete scale label format code.
	 */
	@Test
	public void testDeleteScaleLabelFormatCode() {
		ModifiedEntity<Long> modifiedEntity = new ModifiedEntity<>(this.getDefaultScaleLabelFormatList().get(0).getFormatCode(), testString);

		Long updatedFormatCode = this.getDefaultScaleLabelFormatList().get(0).getFormatCode();
		Mockito.doNothing().when(this.service).delete(Mockito.any(Long.class));
		ModifiedEntity<Long> returnEntity = this.scaleLabelFormatController.deleteScaleLabelFormatCode(updatedFormatCode, CommonMocks.getServletRequest());
		Assert.assertEquals(updatedFormatCode, returnEntity.getData());
		Assert.assertEquals(modifiedEntity.getData(), returnEntity.getData());

	}

	/**
	 * Test find hits by format code.
	 */
	@Test
	public void testFindHitsByFormatCode() {
		Hits hits = new Hits(testInt, testInt, this.getDefaultNoMatchList());

		Mockito.when(this.service.findHitsByLabelFormatCodeList(Mockito.anyListOf(Long.class))).thenReturn(hits);
		Hits returnHits = this.scaleLabelFormatController.findHitsByFormatCode(this.getDefaultNoMatchList(), CommonMocks.getServletRequest());
		Assert.assertEquals(hits, returnHits);

	}

	/**
	 * Creates a list of scale label formats.
	 * @return List of scale label formats.
	 */
	private List<ScaleLabelFormat> getDefaultScaleLabelFormatList() {
		List<ScaleLabelFormat> scaleLabelFormats = new ArrayList<>();
		ScaleLabelFormat scaleLabelFormat = new ScaleLabelFormat();

		scaleLabelFormat.setDescription(testString);
		scaleLabelFormat.setCountOfLabelFormatOneUpcs(testInt);
		scaleLabelFormat.setCountOfLabelFormatTwoUpcs(testInt);
		scaleLabelFormat.setFormatCode(testLong);
		scaleLabelFormats.add(scaleLabelFormat);
		return scaleLabelFormats;
	}

	/**
	 * Creates a list of scale upcs.
	 * @return the list of scale upcs.
	 */
	private List<ScaleUpc> getDefaultScaleUpcList() {
		List<ScaleUpc> scaleUpcList = new ArrayList<>();
		ScaleUpc scaleUpc = new ScaleUpc();

		scaleUpc.setShelfLifeDays(testInt);
		scaleUpc.setFreezeByDays(testInt);
		scaleUpc.setServiceCounterTare(testDouble);
		scaleUpc.setIngredientStatement(testLong);
		scaleUpc.setNutrientStatement(testLong);
		scaleUpc.setLabelFormatOne(testLong);
		scaleUpc.setLabelFormatTwo(testLong);
		scaleUpc.setEnglishDescriptionOne(testString);
		scaleUpc.setEnglishDescriptionTwo(testString);
		scaleUpc.setEnglishDescriptionThree(testString);
		scaleUpc.setEnglishDescriptionFour(testString);
		scaleUpc.setSpanishDescriptionOne(testString);
		scaleUpc.setSpanishDescriptionTwo(testString);
		scaleUpc.setSpanishDescriptionThree(testString);
		scaleUpc.setSpanishDescriptionFour(testString);
		scaleUpc.setEffectiveDate(testLocalDate);
		scaleUpc.setEatByDays(testInt);
		scaleUpc.setPrePackTare(testDouble);
		scaleUpc.setActionCode(testLong);
		scaleUpc.setGraphicsCode(testInt);
		scaleUpc.setForceTare(testBoolean);
		scaleUpc.setPriceOverride(testBoolean);
		scaleUpc.setNetWeight(testDouble);
		scaleUpc.setFirstLabelFormat(this.getDefaultScaleLabelFormatList().get(0));
		scaleUpc.setSecondLabelFormat(this.getDefaultScaleLabelFormatList().get(0));

		scaleUpcList.add(scaleUpc);
		return scaleUpcList;
	}

	/**
	 * List of no match action code hits.
	 * @return list of no match action code hits.
	 */
	private List<Long> getDefaultNoMatchList() {
		List<Long> noMatchList = new ArrayList<>();

		noMatchList.add(testLong);
		noMatchList.add(testLong);
		return noMatchList;
	}
}