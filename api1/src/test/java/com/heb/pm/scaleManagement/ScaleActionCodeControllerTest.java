/*
 *  ScaleActionCodeControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.ScaleActionCode;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.repository.ScaleActionCodeRepository;
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
 * Test class for ScaleActionCodeControllerTest.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleActionCodeControllerTest {

	// Test Constants
	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final int testInt = 0;
	private static final LocalDate testLocalDate = LocalDate.now();
	private static final double testDouble = 0.0;
	private static final boolean testBoolean = false;

	@InjectMocks
	private ScaleActionCodeController scaleActionCodeController;

	@Mock
	private ScaleActionCodeRepository scaleActionCodeRepository;

	@Mock
	private ScaleActionCodeService service;

	@Mock
	private MessageSource messageSource;

	@Mock
	private UserInfo userInfo;

	@Mock
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}


	/**
	 * Test find all by action codes.
	 */
	@Test
	public void testFindAllByActionCodes() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(0, this.getDefaultActionCodeList());

		List<Long> actionCodes = new ArrayList<>();
		actionCodes.add(this.getDefaultActionCodeList().get(0).getActionCode());
		Mockito.when(this.service.findByActionCodes(Mockito.anyListOf(Long.class), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleActionCode> returnPage = this.scaleActionCodeController.findAllByActionCodes(actionCodes, true, 0, CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultActionCodeList(), returnPage.getData());
		Assert.assertEquals(pageableResult, returnPage);
	}

	/**
	 * Test find all by action code description.
	 */
	@Test
	public void testFindAllByActionCodeDescription() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(0, this.getDefaultActionCodeList());

		String actionCodeDescription = this.getDefaultActionCodeList().get(0).getDescription();
		Mockito.when(this.service.findByActionCodeDescription(Mockito.anyString(), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleActionCode> returnPage = this.scaleActionCodeController.findAllByActionCodeDescription(actionCodeDescription, true, 0, CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultActionCodeList(), returnPage.getData());
		Assert.assertEquals(pageableResult, returnPage);

	}

	/**
	 * Test find all action codes.
	 */
	@Test
	public void testFindAllActionCodes() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(0, this.getDefaultActionCodeList());

		Mockito.when(this.service.findAll(Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleActionCode> returnPage = this.scaleActionCodeController.findAllActionCodes(true, 0, CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultActionCodeList(), returnPage.getData());
		Assert.assertEquals(pageableResult, returnPage);

	}

	/**
	 * Test find all plus by action code.
	 */
	@Test
	public void testFindAllPlusByActionCode() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		Long actionCode = this.getDefaultScaleUpcList().get(0).getActionCode();
		Mockito.when(this.service.findPlusByActionCode(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(pageableResult);
		PageableResult<ScaleUpc> returnPage = this.scaleActionCodeController.findAllPlusByActionCode(actionCode, true, 0, CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultScaleUpcList(), returnPage.getData());
		Assert.assertEquals(pageableResult, returnPage);

	}

	/**
	 * Test update scale action code.
	 */
	@Test
	public void testUpdateScaleActionCode() {
		ModifiedEntity<ScaleActionCode> modifiedEntity = new ModifiedEntity<>(this.getDefaultActionCodeList().get(0), testString);

		ScaleActionCode updatedActionCode = this.getDefaultActionCodeList().get(0);
		Mockito.when(this.service.update(Mockito.any(ScaleActionCode.class))).thenReturn(updatedActionCode);
		ModifiedEntity<ScaleActionCode> returnEntity = this.scaleActionCodeController.updateScaleActionCode(updatedActionCode, CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultActionCodeList().get(0), returnEntity.getData());
		Assert.assertEquals(modifiedEntity.getData(), returnEntity.getData());

	}

	/**
	 * Test add scale action code.
	 */
	@Test
	public void testAddScaleActionCode() {
		ModifiedEntity<ScaleActionCode> addedEntity = new ModifiedEntity<>(this.getDefaultActionCodeList().get(0), testString);

		ScaleActionCode addedActionCode = this.getDefaultActionCodeList().get(0);
		Mockito.when(this.service.add(Mockito.anyLong(), Mockito.anyString())).thenReturn(addedActionCode);
		ModifiedEntity<ScaleActionCode> returnEntity = this.scaleActionCodeController.addScaleActionCode(addedActionCode, CommonMocks.getServletRequest());
		Assert.assertEquals(this.getDefaultActionCodeList().get(0), returnEntity.getData());
		Assert.assertEquals(addedEntity.getData(), returnEntity.getData());
	}

	/**
	 * Test delete scale action code.
	 */
	@Test
	public void testDeleteScaleActionCode() {
		ModifiedEntity<Long> deletedEntity = new ModifiedEntity<>(this.getDefaultActionCodeList().get(0).getActionCode(), null);

		Long deletedActionCode = this.getDefaultActionCodeList().get(0).getActionCode();
		Mockito.doNothing().when(this.service).delete(Mockito.anyLong());
		ModifiedEntity<Long> returnEntity = this.scaleActionCodeController.deleteScaleActionCode(deletedActionCode, CommonMocks.getServletRequest());
		Assert.assertEquals(deletedActionCode, returnEntity.getData());
		Assert.assertEquals(deletedEntity.getData(), returnEntity.getData());
	}

	/**
	 * Test find hits by action code.
	 */
	@Test
	public void testFindHitsByActionCode() {
		Hits hits = new Hits(testInt, testInt, this.getDefaultNoMatchList());

		Mockito.when(this.service.findHitsByActionCodeList(Mockito.anyListOf(Long.class))).thenReturn(hits);
		Hits returnHits = this.scaleActionCodeController.findHitsByActionCode(this.getDefaultNoMatchList(), CommonMocks.getServletRequest());
		Assert.assertEquals(hits, returnHits);
	}

	/**
	 * Creates a list of action codes.
	 * @return the list of action codes.
	 */
	private List<ScaleActionCode> getDefaultActionCodeList() {
		List<ScaleActionCode> actionCodeList = new ArrayList<>();
		ScaleActionCode scaleActionCode = new ScaleActionCode();
		scaleActionCode.setActionCode(testLong);
		scaleActionCode.setDescription(testString);
		actionCodeList.add(scaleActionCode);

		return actionCodeList;
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