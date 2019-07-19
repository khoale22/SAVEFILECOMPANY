/*
 *  ScaleLabelFormatServiceTest
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
import com.heb.pm.repository.ScaleLabelFormatRepository;
import com.heb.pm.repository.ScaleLabelFormatRepositoryWithCounts;
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
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class for ScaleLabelFormatServiceTest.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleLabelFormatServiceTest {

	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final int testInt = 0;
	private static final LocalDate testLocalDate = LocalDate.now();
	private static final double testDouble = 0.0;
	private static final boolean testBoolean = false;

	@InjectMocks
	private ScaleLabelFormatService scaleLabelFormatService;

	@Mock
	private ScaleLabelFormatRepository scaleLabelFormatRepository;

	@Mock
	private ScaleLabelFormatRepositoryWithCounts scaleLableFormatRepositoryWithCounts;

	@Mock
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;

	@Mock
	private ScaleUpcRepository scaleUpcRepository;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

	}

	/**
	 * Test find upcs by format code one with count.
	 */
	@Test
	public void testFindUpcsByFormatCodeOneWithCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());
		Page<ScaleUpc> page = new PageImpl<>(this.getDefaultScaleUpcList(), null, 0);

		Mockito.when(this.scaleUpcRepositoryWithCount.findByLabelFormatOne(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(page);
		PageableResult<ScaleUpc> returnResult = this.scaleLabelFormatService.findUpcsByFormatCodeOne(
				this.getDefaultScaleUpcList().get(0).getLabelFormatOne(), true, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find upcs by format code one without count.
	 */
	@Test
	public void testFindUpcsByFormatCodeOneWithoutCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		Mockito.when(this.scaleUpcRepository.findByLabelFormatOne(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(this.getDefaultScaleUpcList());
		PageableResult<ScaleUpc> returnResult = this.scaleLabelFormatService.findUpcsByFormatCodeOne(
				this.getDefaultScaleUpcList().get(0).getLabelFormatOne(), false, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find upcs by format code two with count.
	 */
	@Test
	public void testFindUpcsByFormatCodeTwoWithCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());
		Page<ScaleUpc> page = new PageImpl<>(this.getDefaultScaleUpcList(), null, 0);

		Mockito.when(this.scaleUpcRepositoryWithCount.findByLabelFormatTwo(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(page);
		PageableResult<ScaleUpc> returnResult = this.scaleLabelFormatService.findUpcsByFormatCodeTwo(
				this.getDefaultScaleUpcList().get(0).getLabelFormatOne(), true, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find upcs by format code two without count.
	 */
	@Test
	public void testFindUpcsByFormatCodeTwoWithoutCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0, this.getDefaultScaleUpcList());

		Mockito.when(this.scaleUpcRepository.findByLabelFormatTwo(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(this.getDefaultScaleUpcList());
		PageableResult<ScaleUpc> returnResult = this.scaleLabelFormatService.findUpcsByFormatCodeTwo(
				this.getDefaultScaleUpcList().get(0).getLabelFormatOne(), false, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find all label formats with count.
	 */
	@Test
	public void testFindAllLabelFormatsWithCount() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(0, this.getDefaultScaleLabelFormatList());
		Page<ScaleLabelFormat> page = new PageImpl<>(this.getDefaultScaleLabelFormatList(), null, 0);

		Mockito.when(this.scaleLableFormatRepositoryWithCounts.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatService.findAllLabelFormats(true, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleLabelFormatList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find all label formats without count.
	 */
	@Test
	public void testFindAllLabelFormatsWithoutCount() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(0, this.getDefaultScaleLabelFormatList());

		Mockito.when(this.scaleLabelFormatRepository.findAllByPage(Mockito.any(PageRequest.class))).thenReturn(this.getDefaultScaleLabelFormatList());
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatService.findAllLabelFormats(false, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleLabelFormatList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by description with count.
	 */
	@Test
	public void testFindByDescriptionWithCount() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(0, this.getDefaultScaleLabelFormatList());
		Page<ScaleLabelFormat> page = new PageImpl<>(this.getDefaultScaleLabelFormatList(), null, 0);

		Mockito.when(this.scaleLableFormatRepositoryWithCounts.findByDescriptionContains(Mockito.anyString(), Mockito.any(PageRequest.class))).thenReturn(page);
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatService.findByDescription(testString, true, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleLabelFormatList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by description without count.
	 */
	@Test
	public void testFindByDescriptionWithoutCount() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(0, this.getDefaultScaleLabelFormatList());

		Mockito.when(this.scaleLabelFormatRepository.findByDescriptionContains(Mockito.anyString(), Mockito.any(PageRequest.class))).thenReturn(this.getDefaultScaleLabelFormatList());
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatService.findByDescription(testString, false, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleLabelFormatList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by format code with count.
	 */
	@Test
	public void testFindByFormatCodeWithCount() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(0, this.getDefaultScaleLabelFormatList());
		Page<ScaleLabelFormat> page = new PageImpl<>(this.getDefaultScaleLabelFormatList(), null, 0);

		List<Long> formatCodes = new ArrayList<>();
		formatCodes.add(this.getDefaultScaleLabelFormatList().get(0).getFormatCode());

		Mockito.when(this.scaleLableFormatRepositoryWithCounts.findDistinctByFormatCodeIn(Mockito.anyListOf(Long.class), Mockito.any(PageRequest.class))).thenReturn(page);
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatService.findByFormatCode(formatCodes, true, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleLabelFormatList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by format code without count.
	 */
	@Test
	public void testFindByFormatCodeWithoutCount() {
		PageableResult<ScaleLabelFormat> pageableResult = new PageableResult<>(0, this.getDefaultScaleLabelFormatList());

		List<Long> formatCodes = new ArrayList<>();
		formatCodes.add(this.getDefaultScaleLabelFormatList().get(0).getFormatCode());

		Mockito.when(this.scaleLabelFormatRepository.findDistinctByFormatCodeIn(Mockito.anyListOf(Long.class), Mockito.any(PageRequest.class))).thenReturn(this.getDefaultScaleLabelFormatList());
		PageableResult<ScaleLabelFormat> returnResult = this.scaleLabelFormatService.findByFormatCode(formatCodes, false, testInt, 3);
		Assert.assertEquals(this.getDefaultScaleLabelFormatList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find hits by label format code list.
	 */
	@Test
	public void testFindHitsByLabelFormatCodeList() {
		Hits hits = Hits.calculateHits(this.getDefaultNoMatchList(), this.getDefaultHits());
		Mockito.when(this.scaleLableFormatRepositoryWithCounts.findAll(Mockito.anyListOf(Long.class))).thenReturn(this.getDefaultScaleLabelFormatList());
		Hits returnHits = this.scaleLabelFormatService.findHitsByLabelFormatCodeList(this.getDefaultNoMatchList());

		Assert.assertEquals(hits.getMatchCount(), returnHits.getMatchCount());
		Assert.assertEquals(hits.getNoMatchCount(), returnHits.getNoMatchCount());
		Assert.assertEquals(hits.getNoMatchList(), returnHits.getNoMatchList());
	}

	/**
	 * Test update.
	 */
	@Test
	public void testUpdate() {
		ScaleLabelFormat scaleLabelFormat = this.getDefaultScaleLabelFormatList().get(0);
		Mockito.when(this.scaleLabelFormatRepository.save(Mockito.any(ScaleLabelFormat.class))).thenReturn(scaleLabelFormat);
		ScaleLabelFormat returnScaleLabelFormatCode = this.scaleLabelFormatService.update(scaleLabelFormat);

		Assert.assertEquals(scaleLabelFormat.getDescription(), returnScaleLabelFormatCode.getDescription());
		Assert.assertEquals(scaleLabelFormat, returnScaleLabelFormatCode);
	}

	/**
	 * Test add.
	 */
	@Test
	public void testAdd() {
		ScaleLabelFormat scaleLabelFormat = this.getDefaultScaleLabelFormatList().get(0);
		Mockito.when(this.scaleLabelFormatRepository.findOne(Mockito.any(Long.class))).thenReturn(null);
		Mockito.when(this.scaleLabelFormatRepository.save(Mockito.any(ScaleLabelFormat.class))).thenReturn(scaleLabelFormat);
		ScaleLabelFormat returnScaleLabelFormatCode = this.scaleLabelFormatService.add(scaleLabelFormat.getFormatCode(), scaleLabelFormat.getDescription());
		Assert.assertEquals(scaleLabelFormat.getFormatCode(), returnScaleLabelFormatCode.getFormatCode());
		Assert.assertEquals(scaleLabelFormat.getDescription(), returnScaleLabelFormatCode.getDescription());
		Assert.assertEquals(scaleLabelFormat, returnScaleLabelFormatCode);
	}

	/**
	 * Test add exception.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddException() {
		ScaleLabelFormat scaleLabelFormat = this.getDefaultScaleLabelFormatList().get(0);
		Mockito.when(this.scaleLabelFormatRepository.findOne(Mockito.any(Long.class))).thenReturn(scaleLabelFormat);
		Mockito.when(this.scaleLabelFormatRepository.save(Mockito.any(ScaleLabelFormat.class))).thenReturn(scaleLabelFormat);
		Assert.assertNotNull(this.scaleLabelFormatService.add(scaleLabelFormat.getFormatCode(), scaleLabelFormat.getDescription()));
	}

	/**
	 * Test delete.
	 */
	@Test
	public void testDelete() {
		ScaleLabelFormat scaleLabelFormat = this.getDefaultScaleLabelFormatList().get(0);
		Mockito.when(this.scaleLabelFormatRepository.findOne(Mockito.any(Long.class))).thenReturn(scaleLabelFormat);
		Mockito.doNothing().when(this.scaleLabelFormatRepository).delete(Mockito.any(ScaleLabelFormat.class));
		ScaleLabelFormat returnLabelFormatCode = this.getDefaultScaleLabelFormatList().get(0);
		this.scaleLabelFormatService.delete(returnLabelFormatCode.getFormatCode());

		Assert.assertEquals(returnLabelFormatCode.getFormatCode(), scaleLabelFormat.getFormatCode());

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

	/**
	 * Gets Default hits.
	 * @return an action code list of hits.
	 */
	private List<Long> getDefaultHits() {
		List<ScaleLabelFormat> actionCodeList = this.getDefaultScaleLabelFormatList();

		return actionCodeList.stream().map(ScaleLabelFormat::getFormatCode).collect(
				Collectors.toList());
	}

}