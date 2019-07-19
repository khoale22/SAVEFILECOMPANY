/*
 * ScaleGraphicsCodeControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.entity.ScaleGraphicsCode;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import testSupport.CommonMocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class for ScaleGraphicsCodeController.
 *
 * @author vn40486
 * @since 2.1.0
 */
public class ScaleGraphicsCodeControllerTest {

	private ScaleGraphicsCodeController scaleGraphicsCodeController;
	private ScaleGraphicsCodeService scaleGraphicsCodeService;

	public static final boolean DFLT_INCLUDE_COUNTS =true;
	public static final int DFLT_PAGE = 0;
	public static final int DFLT_PAGE_SIZE = 100;
	public static final ScaleGraphicsCodeService.SortColumn DFLT_SORT_COLUMN = ScaleGraphicsCodeService.SortColumn.SCALE_GRAPHICS_CD;
	public static final ScaleGraphicsCodeService.SortDirection DFLT_SORT_DIRECTION = ScaleGraphicsCodeService.SortDirection.ASC;
	public static final String DFLT_SEARCH_BY_SCALE_GRAPHICS_CODE_DESC = "OVEN";
	public static final String DFLT_SCALE_GRAPHICS_CODE_DESC = "STOVE 3-5 MIN_MICRO 2-3";
	public static final long DFLT_SCALE_GRAPHICS_CODE = 10L;
	public static final long NEW_SCALE_GRAPHICS_CODE = 999L;

	@Before
	public void before() throws Exception {
		scaleGraphicsCodeController = new ScaleGraphicsCodeController();
		scaleGraphicsCodeService
				= Mockito.mock(ScaleGraphicsCodeService.class);
		Field f = ScaleGraphicsCodeController.class.getDeclaredField("scaleGraphicsCodeService");
		f.setAccessible(true);
		f.set(this.scaleGraphicsCodeController, this.scaleGraphicsCodeService);

		Field f2 = ScaleGraphicsCodeController.class.getDeclaredField("userInfo");
		f2.setAccessible(true);
		f2.set(this.scaleGraphicsCodeController, CommonMocks.getUserInfo());

		Field f3 = ScaleGraphicsCodeController.class.getDeclaredField("parameterValidator");
		f3.setAccessible(true);
		f3.set(this.scaleGraphicsCodeController, new NonEmptyParameterValidator());

	}

	/**
	 * Test findAll method of ScaleGraphicsCodeController.
	 */
	@Test
	public void findAll() {
		//Given
		Answer<PageableResult<ScaleGraphicsCode>> checkSearchCall = invocation -> {
			Assert.assertEquals(DFLT_INCLUDE_COUNTS, invocation.getArguments()[0]);
			Assert.assertEquals(DFLT_PAGE, invocation.getArguments()[1]);
			Assert.assertEquals(DFLT_PAGE_SIZE, invocation.getArguments()[2]);
			Assert.assertEquals(DFLT_SORT_COLUMN, invocation.getArguments()[3]);
			Assert.assertEquals(DFLT_SORT_DIRECTION, invocation.getArguments()[4]);

			Page<ScaleGraphicsCode> data =  new PageImpl<>(this.getResultList(), getPageable(), this.getResultList().size());
			PageableResult<ScaleGraphicsCode> results = new PageableResult<>((int)invocation.getArguments()[1],
					data.getTotalPages(),
					data.getTotalElements(),
					data.getContent());
			return results;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeService).findAll(
				Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject());

		//Execute
		PageableResult<ScaleGraphicsCode> results =
				this.scaleGraphicsCodeController.findAll(DFLT_INCLUDE_COUNTS, DFLT_PAGE, DFLT_PAGE_SIZE,
						DFLT_SORT_COLUMN, DFLT_SORT_DIRECTION, CommonMocks.getServletRequest());

		//Assert results
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(DFLT_PAGE, results.getPage());
		Assert.assertEquals(Long.valueOf(3), results.getRecordCount());
	}

	/**
	 * Test findAll method of ScaleGraphicsCodeController.
	 */
	@Test
	public void findByScaleGraphicCode() {
		//Given
		Answer<PageableResult<ScaleGraphicsCode>> checkSearchCall = invocation -> {
			Assert.assertEquals(DFLT_INCLUDE_COUNTS, invocation.getArguments()[0]);
			Assert.assertEquals(DFLT_PAGE, invocation.getArguments()[1]);
			Assert.assertEquals(DFLT_PAGE_SIZE, invocation.getArguments()[2]);
			Assert.assertEquals(DFLT_SORT_COLUMN, invocation.getArguments()[3]);
			Assert.assertEquals(DFLT_SORT_DIRECTION, invocation.getArguments()[4]);
			Assert.assertEquals(getDefaultScaleGraphicsCodes(), invocation.getArguments()[5]);

			Page<ScaleGraphicsCode> data =  new PageImpl<>(this.getResultList(), getPageable(), this.getResultList().size());
			PageableResult<ScaleGraphicsCode> results = new PageableResult<>((int)invocation.getArguments()[1],
					data.getTotalPages(),
					data.getTotalElements(),
					data.getContent());
			return results;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeService).findByScaleGraphicsCode(
				Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());

		//Execute
		PageableResult<ScaleGraphicsCode> results =
				this.scaleGraphicsCodeController.findByScaleGraphicCode(DFLT_INCLUDE_COUNTS, DFLT_PAGE, DFLT_PAGE_SIZE,
						DFLT_SORT_COLUMN, DFLT_SORT_DIRECTION, getDefaultScaleGraphicsCodes(), CommonMocks.getServletRequest());

		//Assert results
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(DFLT_PAGE, results.getPage());
		Assert.assertEquals(Long.valueOf(3), results.getRecordCount());
	}

	/**
	 * Test findAll method of ScaleGraphicsCodeController.
	 */
	@Test
	public void findByScaleGraphicsCodeDescription() {
		//Given
		Answer<PageableResult<ScaleGraphicsCode>> checkSearchCall = invocation -> {
			Assert.assertEquals(DFLT_INCLUDE_COUNTS, invocation.getArguments()[0]);
			Assert.assertEquals(DFLT_PAGE, invocation.getArguments()[1]);
			Assert.assertEquals(DFLT_PAGE_SIZE, invocation.getArguments()[2]);
			Assert.assertEquals(DFLT_SORT_COLUMN, invocation.getArguments()[3]);
			Assert.assertEquals(DFLT_SORT_DIRECTION, invocation.getArguments()[4]);
			Assert.assertEquals(DFLT_SEARCH_BY_SCALE_GRAPHICS_CODE_DESC, invocation.getArguments()[5]);

			Page<ScaleGraphicsCode> data =  new PageImpl<>(this.getResultList(), getPageable(), this.getResultList().size());
			PageableResult<ScaleGraphicsCode> results = new PageableResult<>((int)invocation.getArguments()[1],
					data.getTotalPages(),
					data.getTotalElements(),
					data.getContent());
			return results;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeService).findByScaleGraphicsCodeDescription(
				Mockito.anyBoolean(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());

		//Execute
		PageableResult<ScaleGraphicsCode> results =
				this.scaleGraphicsCodeController.findByScaleGraphicsCodeDescription(DFLT_INCLUDE_COUNTS, DFLT_PAGE, DFLT_PAGE_SIZE,
						DFLT_SORT_COLUMN, DFLT_SORT_DIRECTION, DFLT_SEARCH_BY_SCALE_GRAPHICS_CODE_DESC, CommonMocks.getServletRequest());

		//Assert results
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(DFLT_PAGE, results.getPage());
		Assert.assertEquals(Long.valueOf(3), results.getRecordCount());
	}

	/**
	 * Test add method of ScaleGraphicsCodeController.
	 */
	@Test
	public void add() {
		//Given
		Answer<ScaleGraphicsCode> checkSearchCall = invocation -> {
			ScaleGraphicsCode scaleGraphics = (ScaleGraphicsCode) invocation.getArguments()[0];
			Assert.assertEquals(DFLT_SCALE_GRAPHICS_CODE_DESC, scaleGraphics.getScaleGraphicsCodeDescription());
			ScaleGraphicsCode scaleGraphicsCode = new ScaleGraphicsCode();
			scaleGraphicsCode.setScaleGraphicsCodeDescription(getDefaultScaleGraphicsCodeList().get(0).getScaleGraphicsCodeDescription());;
			scaleGraphicsCode.setScaleGraphicsCode(NEW_SCALE_GRAPHICS_CODE);
			return scaleGraphicsCode;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeService).add(Mockito.anyObject());

		ScaleGraphicsCode scaleGraphics = new ScaleGraphicsCode();
		scaleGraphics.setScaleGraphicsCodeDescription(DFLT_SCALE_GRAPHICS_CODE_DESC);
		//Execute
		ScaleGraphicsCode scaleGraphicsCode =
				this.scaleGraphicsCodeController.add(scaleGraphics, CommonMocks.getServletRequest()).getData();

		//Assert results
		Assert.assertNotNull(scaleGraphicsCode);
		Assert.assertEquals(scaleGraphicsCode.getScaleGraphicsCode().longValue(), NEW_SCALE_GRAPHICS_CODE);
	}

	/**
	 * Test findAll method of ScaleGraphicsCodeController.
	 */
	@Test
	public void update() {
		//Given
		Answer<ScaleGraphicsCode> checkSearchCall = invocation -> {
			ScaleGraphicsCode scaleGraphics = (ScaleGraphicsCode) invocation.getArguments()[0];
			Assert.assertEquals(getDefaultScaleGraphicsCodeList().get(0), scaleGraphics);
			ScaleGraphicsCode scaleGraphicsCode = new ScaleGraphicsCode();
			scaleGraphicsCode.setScaleGraphicsCodeDescription(scaleGraphics.getScaleGraphicsCodeDescription());;
			scaleGraphicsCode.setScaleGraphicsCode(scaleGraphics.getScaleGraphicsCode());
			return scaleGraphicsCode;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeService).update(Mockito.anyObject());

		ScaleGraphicsCode scaleGraphics = getDefaultScaleGraphicsCodeList().get(0);
		//Execute
		ScaleGraphicsCode scaleGraphicsCode =
				this.scaleGraphicsCodeController.update(scaleGraphics, CommonMocks.getServletRequest()).getData();

		//Assert results
		Assert.assertNotNull(scaleGraphicsCode);
		Assert.assertEquals(scaleGraphicsCode, getDefaultScaleGraphicsCodeList().get(0));
	}

	@Test
	public void delete() {
		//Given
		Answer<ScaleGraphicsCode> checkSearchCall = invocation -> {
			Assert.assertEquals(DFLT_SCALE_GRAPHICS_CODE, invocation.getArguments()[0]);
			ScaleGraphicsCode scaleGraphicsCode = new ScaleGraphicsCode();
			scaleGraphicsCode.setScaleGraphicsCode((Long) invocation.getArguments()[0]);
			return scaleGraphicsCode;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeService).delete(Mockito.anyLong());

		ScaleGraphicsCode scaleGraphics = getDefaultScaleGraphicsCodeList().get(0);
		//Execute
		Long scaleGraphicsCode = this.scaleGraphicsCodeController.delete(DFLT_SCALE_GRAPHICS_CODE,
				CommonMocks.getServletRequest()).getData();

		//Assert results
		Assert.assertNotNull(scaleGraphicsCode);
		Assert.assertEquals(scaleGraphicsCode.longValue(), DFLT_SCALE_GRAPHICS_CODE);
	}

	/**
	 * Returns an object that has the page to get, it's size, and how to sort.
	 *
	 * @return An object that has the page to get, it's size, and how to sort.
	 */
	private Pageable getPageable() {
		return new PageRequest(0, 100, ScaleGraphicsCode.getDefaultSort());
	}

	/**
	 * Prepares and returns set of Graphics Codes.
	 * @return a list of Graphics Codes.
	 */
	private List<ScaleGraphicsCode> getDefaultScaleGraphicsCodeList() {
		List<ScaleGraphicsCode> defaultList = new ArrayList<ScaleGraphicsCode>();
		ScaleGraphicsCode scaleGraphicsCode = new ScaleGraphicsCode();

		scaleGraphicsCode.setScaleGraphicsCode(20L);
		scaleGraphicsCode.setScaleGraphicsCodeDescription("GREAT FOR GRILL 20 MINUTES");
		scaleGraphicsCode.setScaleScanCodeCount(10);
		defaultList.add(scaleGraphicsCode);

		scaleGraphicsCode = new ScaleGraphicsCode();
		scaleGraphicsCode.setScaleGraphicsCode(28L);
		scaleGraphicsCode.setScaleGraphicsCodeDescription("OVEN 300 20-25 MIN MICRO 3-5");
		scaleGraphicsCode.setScaleScanCodeCount(5);
		defaultList.add(scaleGraphicsCode);

		scaleGraphicsCode = new ScaleGraphicsCode();
		scaleGraphicsCode.setScaleGraphicsCode(52L);
		scaleGraphicsCode.setScaleGraphicsCodeDescription("STOVE 3-5 MIN_MICRO 2-3");
		scaleGraphicsCode.setScaleScanCodeCount(2);
		defaultList.add(scaleGraphicsCode);

		return defaultList;
	}

	/**
	 * Returns default list of Graphics Codes.
	 * @return Graphics Codes.
	 */
	private List<Long> getDefaultScaleGraphicsCodes() {
		return getDefaultScaleGraphicsCodeList().stream().map(ScaleGraphicsCode::getScaleGraphicsCode).collect(Collectors.toList());
	}

	/**
	 * Returns matched/found list of Graphics Codes.
	 * @return matched graphics Codes.
	 */
	private List<Long> getScaleGraphicsCodesMatches() {
		List<Long> allCodes = getDefaultScaleGraphicsCodeList().stream().map(ScaleGraphicsCode::getScaleGraphicsCode).collect(Collectors.toList());
		return allCodes.subList(0,1);
	}

	/**
	 * Returns an empty list of ScaleGraphicsCode objects for testing.
	 *
	 * @return An empty list of ScaleGraphicsCode objects.
	 */
	private List<ScaleGraphicsCode> getResultList() {
		return getDefaultScaleGraphicsCodeList();
	}
}
