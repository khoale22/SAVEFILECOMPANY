/*
 *  ScaleActionCodeServiceTest
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
import com.heb.pm.repository.ScaleActionCodeRepositoryWithCount;
import com.heb.pm.repository.ScaleUpcRepository;
import com.heb.pm.repository.ScaleUpcRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class for ScaleActionCodeServiceTest.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleActionCodeServiceTest {

	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final Long testLongObject = 0L;
	private static final int testInt = 0;
	private static final LocalDate testLocalDate = LocalDate.now();
	private static final double testDouble = 0.0;
	private static final boolean testBoolean = false;

	@InjectMocks
	private ScaleActionCodeService scaleActionCodeService;

	@Mock
	private ScaleActionCodeRepositoryWithCount repositoryWithCount;

	@Mock
	private ScaleActionCodeRepository repository;

	@Mock
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;

	@Mock
	private ScaleUpcRepository scaleUpcRepository;

	/**
	 * Initializes all mocks.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test find by action codes with count.
	 */
	@Test
	public void testFindByActionCodesWithCount() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultActionCodeList());
		Page<ScaleActionCode> page = new PageImpl<>(this.getDefaultActionCodeList(), null, 0);

		List<Long> actionCodes = new ArrayList<>();
		actionCodes.add(this.getDefaultActionCodeList().get(0).getActionCode());
		Mockito.when(this.repositoryWithCount.findByActionCodeIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleActionCode> returnResult = this.scaleActionCodeService.findByActionCodes(actionCodes, true, 0);

		Assert.assertEquals(this.getDefaultActionCodeList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by action codes without count.
	 */
	@Test
	public void testFindByActionCodesWithoutCount() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultActionCodeList());

		List<Long> actionCodes = new ArrayList<>();
		actionCodes.add(this.getDefaultActionCodeList().get(0).getActionCode());
		Mockito.when(this.repository.findByActionCodeIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(this.getDefaultActionCodeList());
		PageableResult<ScaleActionCode> returnResult = this.scaleActionCodeService.findByActionCodes(actionCodes, false, 0);

		Assert.assertEquals(this.getDefaultActionCodeList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find all with count.
	 */
	@Test
	public void testFindAllWithCount() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultActionCodeList());
		Page<ScaleActionCode> page = new PageImpl<>(this.getDefaultActionCodeList(), null, 0);

		Mockito.when(this.repositoryWithCount.findAll(Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleActionCode> returnResult = this.scaleActionCodeService.findAll(true, 0);

		Assert.assertEquals(this.getDefaultActionCodeList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find all without count.
	 */
	@Test
	public void testFindAllWithoutCount() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultActionCodeList());
		Page<ScaleActionCode> page = new PageImpl<>(this.getDefaultActionCodeList(), null, 0);

		Mockito.when(this.repository.findAll(Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleActionCode> returnResult = this.scaleActionCodeService.findAll(false, 0);

		Assert.assertEquals(this.getDefaultActionCodeList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by action code description with count.
	 */
	@Test
	public void testFindByActionCodeDescriptionWithCount() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultActionCodeList());
		Page<ScaleActionCode> page = new PageImpl<>(this.getDefaultActionCodeList(), null, 0);

		String description = this.getDefaultActionCodeList().get(0).getDescription();
		Mockito.when(this.repositoryWithCount.findByDescriptionContains(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleActionCode> returnResult = this.scaleActionCodeService.findByActionCodeDescription(description,true, 0);

		Assert.assertEquals(this.getDefaultActionCodeList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by action code description without count.
	 */
	@Test
	public void testFindByActionCodeDescriptionWithoutCount() {
		PageableResult<ScaleActionCode> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultActionCodeList());

		String description = this.getDefaultActionCodeList().get(0).getDescription();
		Mockito.when(this.repository.findByDescriptionContains(Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(this.getDefaultActionCodeList());
		PageableResult<ScaleActionCode> returnResult = this.scaleActionCodeService.findByActionCodeDescription(description,false, 0);

		Assert.assertEquals(this.getDefaultActionCodeList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find plus by action code with count.
	 */
	@Test
	public void testFindPlusByActionCodeWithCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultScaleUpcList());
		Page<ScaleUpc> page = new PageImpl<>(this.getDefaultScaleUpcList(), null, 0);

		Long actionCode = this.getDefaultScaleUpcList().get(0).getActionCode();
		Mockito.when(this.scaleUpcRepositoryWithCount.findByActionCode(Mockito.anyLong(), Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleUpc> returnResult = this.scaleActionCodeService.findPlusByActionCode(actionCode,true, 0);

		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find plus by action code without count.
	 */
	@Test
	public void testFindPlusByActionCodeWithoutCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(testInt, 0, testLongObject, this.getDefaultScaleUpcList());

		Long actionCode = this.getDefaultScaleUpcList().get(0).getActionCode();
		Mockito.when(this.scaleUpcRepository.findByActionCode(Mockito.anyLong(), Mockito.any(Pageable.class))).thenReturn(this.getDefaultScaleUpcList());
		PageableResult<ScaleUpc> returnResult = this.scaleActionCodeService.findPlusByActionCode(actionCode,false, 0);

		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find hits by action code list.
	 */
	@Test
	public void testFindHitsByActionCodeList() {
		Hits hits = Hits.calculateHits(this.getDefaultNoMatchList(), this.getDefaultHits());
		Mockito.when(this.repositoryWithCount.findAll(Mockito.anyListOf(Long.class))).thenReturn(this.getDefaultActionCodeList());
		Hits returnHits = this.scaleActionCodeService.findHitsByActionCodeList(this.getDefaultNoMatchList());

		Assert.assertEquals(hits.getMatchCount(), returnHits.getMatchCount());
		Assert.assertEquals(hits.getNoMatchCount(), returnHits.getNoMatchCount());
		Assert.assertEquals(hits.getNoMatchList(), returnHits.getNoMatchList());
	}

	/**
	 * Test update.
	 */
	@Test
	public void testUpdate() {
		ScaleActionCode scaleActionCode = this.getDefaultActionCodeList().get(0);
		Mockito.when(this.repository.save(Mockito.any(ScaleActionCode.class))).thenReturn(scaleActionCode);
		ScaleActionCode returnScaleActionCode = this.scaleActionCodeService.update(scaleActionCode);

		Assert.assertEquals(scaleActionCode.getDescription(), returnScaleActionCode.getDescription());
		Assert.assertEquals(scaleActionCode, returnScaleActionCode);
	}

	/**
	 * Test add.
	 */
	@Test
	public void testAdd() {
		ScaleActionCode scaleActionCode = this.getDefaultActionCodeList().get(0);
		Mockito.when(this.repository.findOne(Mockito.anyLong())).thenReturn(null);
		Mockito.when(this.repository.save(Mockito.any(ScaleActionCode.class))).thenReturn(scaleActionCode);
		ScaleActionCode returnScaleActionCode = this.scaleActionCodeService.add(scaleActionCode.getActionCode(), scaleActionCode.getDescription());
		Assert.assertEquals(scaleActionCode.getActionCode(), returnScaleActionCode.getActionCode());
		Assert.assertEquals(scaleActionCode.getDescription(), returnScaleActionCode.getDescription());
		Assert.assertEquals(scaleActionCode, returnScaleActionCode);
	}

	/**
	 * Test add with exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddWithException() {
		ScaleActionCode scaleActionCode = this.getDefaultActionCodeList().get(0);
		Mockito.when(this.repository.findOne(Mockito.anyLong())).thenReturn(scaleActionCode);
		Mockito.when(this.repository.save(Mockito.any(ScaleActionCode.class))).thenReturn(scaleActionCode);
		Assert.assertNotNull(this.scaleActionCodeService.add(scaleActionCode.getActionCode(), scaleActionCode.getDescription()));
	}

	/**
	 * Test delete.
	 */
	@Test
	public void testDelete() {
		ScaleActionCode scaleActionCode = this.getDefaultActionCodeList().get(0);
		Mockito.when(this.repository.findOne(Mockito.anyLong())).thenReturn(scaleActionCode);
		Mockito.doNothing().when(this.repository).delete(Mockito.any(ScaleActionCode.class));
		ScaleActionCode returnActionCode = this.getDefaultActionCodeList().get(0);
		this.scaleActionCodeService.delete(returnActionCode.getActionCode());

		Assert.assertEquals(returnActionCode.getActionCode(), scaleActionCode.getActionCode());
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

	/**
	 * Gets Default hits.
	 * @return an action code list of hits.
	 */
	private List<Long> getDefaultHits() {
		List<ScaleActionCode> actionCodeList = this.getDefaultActionCodeList();

		return actionCodeList.stream().map(ScaleActionCode::getActionCode).collect(
				Collectors.toList());
	}

}