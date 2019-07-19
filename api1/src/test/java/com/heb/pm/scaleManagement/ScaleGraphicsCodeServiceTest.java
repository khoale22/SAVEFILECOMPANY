/*
 * ScaleGraphicsCodeServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.ScaleGraphicsCode;
import com.heb.pm.repository.ScaleGraphicsCodeRepository;
import com.heb.pm.repository.ScaleGraphicsCodeRepositoryWithCounts;
import com.heb.pm.repository.ScaleUpcRepositoryWithCount;
import com.heb.pm.repository.ScaleUpcRepository;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class for ScaleGraphicsCodeService.
 *
 * @author vn40486
 * @since 2.1.0
 */
public class ScaleGraphicsCodeServiceTest {

	private ScaleGraphicsCodeRepository scaleGraphicsCodeRepository;
	private ScaleGraphicsCodeRepositoryWithCounts scaleGraphicsCodeRepositoryWithCounts;
	private ScaleUpcRepositoryWithCount scaleUpcRepositoryWithCount;
	private ScaleUpcRepository scaleUpcRepository;
	private ScaleGraphicsCodeService scaleGraphicsCodeService;

	public static final boolean DFLT_INCLUDE_COUNTS =true;
	public static final int DFLT_PAGE = 0;
	public static final int DFLT_PAGE_SIZE = 100;
	public static final ScaleGraphicsCodeService.SortColumn DFLT_SORT_COLUMN = ScaleGraphicsCodeService.SortColumn.SCALE_GRAPHICS_CD;
	public static final ScaleGraphicsCodeService.SortDirection DFLT_SORT_DIRECTION = ScaleGraphicsCodeService.SortDirection.ASC;
	public static final int DFLT_UPC_COUNT = 10;
	public static final String DFLT_SEARCH_BY_SCALE_GRAPHICS_CODE_DESC = "OVEN";
	public static final String DFLT_SCALE_GRAPHICS_CODE_DESC = "STOVE 3-5 MIN_MICRO 2-3";
	public static final long DFLT_SCALE_GRAPHICS_CODE = 10L;
	public static final long NEW_SCALE_GRAPHICS_CODE = 999L;


	@Before
	public void before() throws Exception {
		scaleGraphicsCodeService = new ScaleGraphicsCodeService();
		scaleGraphicsCodeRepositoryWithCounts
				= Mockito.mock(ScaleGraphicsCodeRepositoryWithCounts.class);
		Field f = ScaleGraphicsCodeService.class.getDeclaredField("scaleGraphicsCodeRepositoryWithCounts");
		f.setAccessible(true);
		f.set(this.scaleGraphicsCodeService, this.scaleGraphicsCodeRepositoryWithCounts);

		scaleUpcRepositoryWithCount = Mockito.mock(ScaleUpcRepositoryWithCount.class);
		Field f2 = ScaleGraphicsCodeService.class.getDeclaredField("scaleUpcRepository");
		f2.setAccessible(true);
		f2.set(this.scaleGraphicsCodeService, this.scaleUpcRepositoryWithCount);

		scaleGraphicsCodeRepository = Mockito.mock(ScaleGraphicsCodeRepository.class);
		Field f3 = ScaleGraphicsCodeService.class.getDeclaredField("scaleGraphicsCodeRepository");
		f3.setAccessible(true);
		f3.set(this.scaleGraphicsCodeService, this.scaleGraphicsCodeRepository);

		scaleUpcRepository = Mockito.mock(ScaleUpcRepository.class);
		Field f4 = ScaleGraphicsCodeService.class.getDeclaredField("scaleUpcRepositoryWithoutCount");
		f4.setAccessible(true);
		f4.set(this.scaleGraphicsCodeService, this.scaleUpcRepository);
	}

	/**
	 * Test findAll method of ScaleGraphicsCodeService.
	 */
	@Test
	public void findAll() {
		//Given
		Answer<Page<ScaleGraphicsCode>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[0];
			Assert.assertEquals(DFLT_PAGE, pageable.getPageNumber());
			Assert.assertEquals(DFLT_PAGE_SIZE, pageable.getPageSize());
			Assert.assertEquals(ScaleGraphicsCode.getDefaultSort(), pageable.getSort());

			Page<ScaleGraphicsCode> data =  new PageImpl<>(this.getResultList(), pageable, 3);
			return data;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeRepositoryWithCounts).findAll(Mockito.<Pageable>anyObject());
		Mockito.doReturn(DFLT_UPC_COUNT).when(this.scaleUpcRepositoryWithCount).countByGraphicsCode(Mockito.anyLong());

		//Execute
		PageableResult<ScaleGraphicsCode> results =
				this.scaleGraphicsCodeService.findAll(DFLT_INCLUDE_COUNTS, DFLT_PAGE, DFLT_PAGE_SIZE,
						DFLT_SORT_COLUMN, DFLT_SORT_DIRECTION);

		//Assert results
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(DFLT_PAGE, results.getPage());
		Assert.assertEquals(Long.valueOf(3), results.getRecordCount());
	}

	/**
	 * Test findByScaleGraphicsCode method of ScaleGraphicsCodeService.
	 */
	@Test
	public void findByScaleGraphicsCode() {
		//Given
		Answer<Page<ScaleGraphicsCode>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(DFLT_PAGE, pageable.getPageNumber());
			Assert.assertEquals(DFLT_PAGE_SIZE, pageable.getPageSize());
			Assert.assertEquals(ScaleGraphicsCode.getDefaultSort(), pageable.getSort());

			Page<ScaleGraphicsCode> data =  new PageImpl<>(this.getResultList(), pageable, 3);
			return data;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeRepositoryWithCounts).findByScaleGraphicsCodeIn(
				Mockito.anyList(), Mockito.anyObject());
		Mockito.doReturn(DFLT_UPC_COUNT).when(this.scaleUpcRepositoryWithCount).countByGraphicsCode(Mockito.anyLong());

		//Execute
		PageableResult<ScaleGraphicsCode> results =
				this.scaleGraphicsCodeService.findByScaleGraphicsCode(DFLT_INCLUDE_COUNTS, DFLT_PAGE, DFLT_PAGE_SIZE,
						DFLT_SORT_COLUMN, DFLT_SORT_DIRECTION,
						this.getDefaultScaleGraphicsCodes());

		//Assert results
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(DFLT_PAGE, results.getPage());
		Assert.assertEquals(Long.valueOf(3), results.getRecordCount());
	}

	/**
	 * Test findByScaleGraphicsCodeDescription method of ScaleGraphicsCodeService.
	 */
	@Test
	public void findByScaleGraphicsCodeDescription() {
		//Given
		Answer<Page<ScaleGraphicsCode>> checkSearchCall = invocation -> {
			Pageable pageable = (Pageable)invocation.getArguments()[1];
			Assert.assertEquals(DFLT_PAGE, pageable.getPageNumber());
			Assert.assertEquals(DFLT_PAGE_SIZE, pageable.getPageSize());
			Assert.assertEquals(ScaleGraphicsCode.getDefaultSort(), pageable.getSort());

			Page<ScaleGraphicsCode> data =  new PageImpl<>(this.getResultList(), pageable, 3);
			return data;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeRepositoryWithCounts).findByScaleGraphicsCodeDescriptionIgnoreCaseContaining(
				Mockito.anyString(), Mockito.anyObject());
		Mockito.doReturn(DFLT_UPC_COUNT).when(this.scaleUpcRepositoryWithCount).countByGraphicsCode(Mockito.anyLong());

		//Execute
		PageableResult<ScaleGraphicsCode> results =
				this.scaleGraphicsCodeService.findByScaleGraphicsCodeDescription(DFLT_INCLUDE_COUNTS, DFLT_PAGE, DFLT_PAGE_SIZE,
						DFLT_SORT_COLUMN, DFLT_SORT_DIRECTION,
						DFLT_SEARCH_BY_SCALE_GRAPHICS_CODE_DESC);

		//Assert results
		Assert.assertNotNull(results.getData());
		Assert.assertEquals(Integer.valueOf(1), results.getPageCount());
		Assert.assertEquals(DFLT_PAGE, results.getPage());
		Assert.assertEquals(Long.valueOf(3), results.getRecordCount());
	}

	/**
	 * Test findHitsByGraphicsCodes method of ScaleGraphicsCodeService.
	 */
	@Test
	public void findHitsByGraphicsCodes() {
		//Given
		Mockito.doReturn(getScaleGraphicsCodesMatches()).when(this.scaleGraphicsCodeRepository).findScaleGraphicsCodes(Mockito.anyList());

		//Execute
		Hits hits =
				this.scaleGraphicsCodeService.findHitsByGraphicsCodes(getDefaultScaleGraphicsCodes());

		//Assert results
		Assert.assertNotNull(hits);
		Assert.assertEquals(getScaleGraphicsCodesMatches().size(), hits.getMatchCount());
		Assert.assertEquals(2, hits.getNoMatchCount());
		Assert.assertEquals(2, hits.getNoMatchList().size());
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
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeRepository).save(Mockito.any(ScaleGraphicsCode.class));

		ScaleGraphicsCode maxSaleGraphicsCode = new ScaleGraphicsCode(); maxSaleGraphicsCode.setScaleGraphicsCode(NEW_SCALE_GRAPHICS_CODE);
		Mockito.doReturn(maxSaleGraphicsCode).when(this.scaleGraphicsCodeRepository).findTopByOrderByScaleGraphicsCodeDesc();


		ScaleGraphicsCode scaleGraphics = new ScaleGraphicsCode();
		scaleGraphics.setScaleGraphicsCodeDescription(DFLT_SCALE_GRAPHICS_CODE_DESC);
		//Execute
		ScaleGraphicsCode scaleGraphicsCode = this.scaleGraphicsCodeService.add(scaleGraphics);

		//Assert results
		Assert.assertNotNull(scaleGraphicsCode);
		Assert.assertEquals(scaleGraphicsCode.getScaleGraphicsCode().longValue(), NEW_SCALE_GRAPHICS_CODE);
	}

	/**
	 * Test update method of ScaleGraphicsCodeController.
	 */
	@Test
	public void update() {
		//Given
		Answer<ScaleGraphicsCode> checkSearchCall = invocation -> {
			ScaleGraphicsCode scaleGraphics = (ScaleGraphicsCode) invocation.getArguments()[0];
			Assert.assertEquals(getDefaultScaleGraphicsCodeList().get(0), scaleGraphics);
			return scaleGraphics;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeRepository).save(Mockito.any(ScaleGraphicsCode.class));

		//Execute
		ScaleGraphicsCode scaleGraphicsCode = this.scaleGraphicsCodeService.update(getDefaultScaleGraphicsCodeList().get(0));

		//Assert results
		Assert.assertNotNull(scaleGraphicsCode);
		Assert.assertEquals(getDefaultScaleGraphicsCodeList().get(0), scaleGraphicsCode);
	}

	/**
	 * Test update delete of ScaleGraphicsCodeController.
	 */
	@Test
	public void delete() {
		//Given
		Answer<?> checkSearchCall = invocation -> {
			Long deleteScaleGraphicsCode = (Long) invocation.getArguments()[0];
			Assert.assertEquals(DFLT_SCALE_GRAPHICS_CODE, deleteScaleGraphicsCode.longValue());
			return null;
		};
		Mockito.doAnswer(checkSearchCall).when(this.scaleGraphicsCodeRepository).delete(Mockito.any(ScaleGraphicsCode.class));

		//Execute
		this.scaleGraphicsCodeService.delete(DFLT_SCALE_GRAPHICS_CODE);
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
