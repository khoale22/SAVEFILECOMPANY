/*
 * ScaleManagementServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.scaleManagement;

import com.heb.pm.Hits;
import com.heb.pm.entity.AssociatedUpc;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.PrimaryUpc;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ScaleActionCode;
import com.heb.pm.entity.ScaleGraphicsCode;
import com.heb.pm.entity.ScaleUpc;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.pm.repository.ScaleActionCodeRepository;
import com.heb.pm.repository.ScaleGraphicsCodeRepository;
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
 * Test class for ScaleManagementServiceTest.
 *
 * @author l730832
 * @since 2.2.0
 */
public class ScaleManagementServiceTest {

	private static final String testString = "Test";
	private static final long testLong = 0;
	private static final int testInt = 0;
	private static final LocalDate testLocalDate = LocalDate.now();
	private static final double testDouble = 0.0;
	private static final boolean testBoolean = false;

	@InjectMocks
	private ScaleManagementService scaleManagementService;

	@Mock
	private ScaleUpcRepositoryWithCount repositoryWithCount;

	@Mock
	private ScaleUpcRepository repository;

	@Mock
	private ScaleGraphicsCodeRepository graphicsCodeRepository;

	@Mock
	private ScaleActionCodeRepository scaleActionCodeRepository;

	/**
	 * Initializes mockitos.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test find by plu with count.
	 */
	@Test
	public void testFindByPluWithCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0,  this.getDefaultScaleUpcList());
		Page<ScaleUpc> page = new PageImpl<>(this.getDefaultScaleUpcList(), null, 0);
		List<Long> scalePluList = new ArrayList<>();
		scalePluList.add(this.getDefaultScaleUpcList().get(0).getPlu());

		Mockito.when(this.repositoryWithCount.findByUpcIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleUpc> returnResult = this.scaleManagementService.findByPlu(scalePluList, true, 0);

		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by plu without count.
	 */
	@Test
	public void testFindByPluWithoutCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0,  this.getDefaultScaleUpcList());
		List<Long> scalePluList = new ArrayList<>();
		scalePluList.add(this.getDefaultScaleUpcList().get(0).getPlu());

		Mockito.when(this.repository.findByUpcIn(Mockito.anyListOf(Long.class), Mockito.any(Pageable.class))).thenReturn(this.getDefaultScaleUpcList());
		PageableResult<ScaleUpc> returnResult = this.scaleManagementService.findByPlu(scalePluList, false, 0);

		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by description with count.
	 */
	@Test
	public void testFindByDescriptionWithCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0,  this.getDefaultScaleUpcList());
		Page<ScaleUpc> page = new PageImpl<>(this.getDefaultScaleUpcList(), null, 0);

		Mockito.when(this.repositoryWithCount.findByEnglishDescriptionOneContainsOrEnglishDescriptionTwoContainsOrEnglishDescriptionThreeContainsOrEnglishDescriptionFourContains(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(page);
		PageableResult<ScaleUpc> returnResult = this.scaleManagementService.findByDescription(testString, true, 0);

		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}

	/**
	 * Test find by description without count.
	 */
	@Test
	public void testFindByDescriptionWithoutCount() {
		PageableResult<ScaleUpc> pageableResult = new PageableResult<>(0,  this.getDefaultScaleUpcList());

		Mockito.when(this.repository.findByEnglishDescriptionOneContainsOrEnglishDescriptionTwoContainsOrEnglishDescriptionThreeContainsOrEnglishDescriptionFourContains(
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(this.getDefaultScaleUpcList());
		PageableResult<ScaleUpc> returnResult = this.scaleManagementService.findByDescription(testString, false, 0);

		Assert.assertEquals(this.getDefaultScaleUpcList(), returnResult.getData());
		Assert.assertEquals(pageableResult.getData(), returnResult.getData());
	}
	/**
	 * Test find hits by plu list with count.
	 */
	@Test
	public void testFindHitsByPluListWithCount() {
		Hits hits = Hits.calculateHits(this.getDefaultNoMatchList(), this.getDefaultHits());
		Mockito.when(this.repositoryWithCount.findAll(Mockito.anyListOf(Long.class))).thenReturn(this.getDefaultScaleUpcList());
		Hits returnHits = this.scaleManagementService.findHitsByPluList(this.getDefaultNoMatchList());

		Assert.assertEquals(hits.getMatchCount(), returnHits.getMatchCount());
		Assert.assertEquals(hits.getNoMatchCount(), returnHits.getNoMatchCount());
		Assert.assertEquals(hits.getNoMatchList(), returnHits.getNoMatchList());
	}

	/**
	 * Test update.
	 */
	@Test
	public void testUpdate() {
		Mockito.when(this.scaleActionCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultActionCodeList().get(0));
		Mockito.when(this.graphicsCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultGraphicsCodeList().get(0));
		Mockito.when(this.repository.save(Mockito.any(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList().get(0));
		ScaleUpc returnScaleUpc = this.scaleManagementService.update(this.getDefaultScaleUpcList().get(0));
		Assert.assertEquals(returnScaleUpc, this.getDefaultScaleUpcList().get(0));

	}

	/**
	 * Test update with action code exception.
	 */
	@Test
	public void testUpdateActionCodeException() {
		Mockito.when(this.scaleActionCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultActionCodeList().get(0));
		Mockito.when(this.graphicsCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultGraphicsCodeList().get(0));
		Mockito.when(this.repository.save(Mockito.any(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList().get(0));
		ScaleUpc returnScaleUpc = this.scaleManagementService.update(this.getDefaultScaleUpcList().get(0));
		Assert.assertEquals(returnScaleUpc, this.getDefaultScaleUpcList().get(0));

	}

	/**
	 * Test update with Graphics Code exception.
	 */
	@Test
	public void testUpdateGraphicsCodeException() {
		Mockito.when(this.scaleActionCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultActionCodeList().get(0));
		Mockito.when(this.graphicsCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultGraphicsCodeList().get(0));
		Mockito.when(this.repository.save(Mockito.any(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList().get(0));
		ScaleUpc returnScaleUpc = this.scaleManagementService.update(this.getDefaultScaleUpcList().get(0));
		Assert.assertEquals(returnScaleUpc, this.getDefaultScaleUpcList().get(0));

	}

	/**
	 * Test bulk update with count.
	 */
	@Test
	public void testBulkUpdate() {
		Mockito.when(this.scaleActionCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultActionCodeList().get(0));
		Mockito.when(this.graphicsCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultGraphicsCodeList().get(0));
		Mockito.when(this.repository.save(Mockito.anyListOf(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList());
		ScaleManagementBulkUpdate scaleManagementBulkUpdate = new ScaleManagementBulkUpdate();
		for (ScaleManagementBulkUpdate.BulkUpdateAttribute bulkUpdate : ScaleManagementBulkUpdate.BulkUpdateAttribute.values()){
			scaleManagementBulkUpdate.setScaleUpcs(this.getDefaultScaleUpcList());
			scaleManagementBulkUpdate.setAttribute(bulkUpdate);
			switch(bulkUpdate) {
				case SERVICE_COUNTER_TARE : {
					scaleManagementBulkUpdate.setValue(String.valueOf(testLong));
					break;
				}
				case ACTION_CODE: {
					scaleManagementBulkUpdate.setValue(String.valueOf(testLong));
					break;
				}
				case FORCE_TARE: {
					scaleManagementBulkUpdate.setValue(String.valueOf(testBoolean));
					break;
				}
				case ENGLISH_LINE_1: {
					scaleManagementBulkUpdate.setValue(testString);
					break;
				}
				case ENGLISH_LINE_2: {
					scaleManagementBulkUpdate.setValue(testString);
					break;
				}
				case ENGLISH_LINE_3: {
					scaleManagementBulkUpdate.setValue(testString);
					break;
				}
				case ENGLISH_LINE_4: {
					scaleManagementBulkUpdate.setValue(testString);
					break;
				}
				default : {
					scaleManagementBulkUpdate.setValue(String.valueOf(testInt));
					break;
				}

			}
			List<ScaleUpc> scaleUpcList = this.scaleManagementService.bulkUpdate(scaleManagementBulkUpdate);
			Assert.assertEquals(scaleUpcList, scaleManagementBulkUpdate.getScaleUpcs());
		}
	}

	/**
	 * Test bulk update with count.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBulkUpdatewithActionCodeException() {
		Mockito.when(this.scaleActionCodeRepository.findOne(Mockito.anyLong())).thenReturn(null);
		Mockito.when(this.graphicsCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultGraphicsCodeList().get(0));
		Mockito.when(this.repository.save(Mockito.anyListOf(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList());
		ScaleManagementBulkUpdate scaleManagementBulkUpdate = new ScaleManagementBulkUpdate();
		scaleManagementBulkUpdate.setScaleUpcs(this.getDefaultScaleUpcList());
		scaleManagementBulkUpdate.setAttribute(ScaleManagementBulkUpdate.BulkUpdateAttribute.ACTION_CODE);
		scaleManagementBulkUpdate.setValue(String.valueOf(testLong));

		List<ScaleUpc> scaleUpcList = this.scaleManagementService.bulkUpdate(scaleManagementBulkUpdate);
		Assert.assertEquals(scaleUpcList, scaleManagementBulkUpdate.getScaleUpcs());
	}

	/**
	 * Test bulk update with count.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBulkUpdatewithGraphicsCodeException() {
		Mockito.when(this.scaleActionCodeRepository.findOne(Mockito.anyLong())).thenReturn(this.getDefaultActionCodeList().get(0));
		Mockito.when(this.graphicsCodeRepository.findOne(Mockito.anyLong())).thenReturn(null);
		Mockito.when(this.repository.save(Mockito.anyListOf(ScaleUpc.class))).thenReturn(this.getDefaultScaleUpcList());
		ScaleManagementBulkUpdate scaleManagementBulkUpdate = new ScaleManagementBulkUpdate();
		scaleManagementBulkUpdate.setScaleUpcs(this.getDefaultScaleUpcList());
		scaleManagementBulkUpdate.setAttribute(ScaleManagementBulkUpdate.BulkUpdateAttribute.GRAPHICS_CODE);
		scaleManagementBulkUpdate.setValue(String.valueOf(testInt));

		List<ScaleUpc> scaleUpcList = this.scaleManagementService.bulkUpdate(scaleManagementBulkUpdate);
		Assert.assertEquals(scaleUpcList, scaleManagementBulkUpdate.getScaleUpcs());
	}

	/**
	 * Creates a list of scale upcs.
	 * @return the list of scale upcs.
	 */
	private List<ScaleUpc> getDefaultScaleUpcList() {
		List<ScaleUpc> scaleUpcList = new ArrayList<>();
		ScaleUpc scaleUpc = new ScaleUpc();
		AssociatedUpc associatedUpc = new AssociatedUpc();
		PrimaryUpc primaryUpc = new PrimaryUpc();
		SubDepartment subDepartment = new SubDepartment();
		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
		SellingUnit sellingUnit = new SellingUnit();
		ProductMaster productMaster = new ProductMaster();

		subDepartmentKey.setDepartment(testString);
		subDepartmentKey.setSubDepartment(testString);
		primaryUpc.setItemMasters(this.getDefaultItemMaster());
		associatedUpc.setPrimaryUpc(primaryUpc);
		subDepartment.setKey(subDepartmentKey);
		subDepartment.setName(testString);
		productMaster.setSubDepartment(subDepartment);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);

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
		scaleUpc.setAssociateUpc(associatedUpc);
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
	 * Creates an item master list.
	 * @return a list of item masters.
	 */
	private List<ItemMaster> getDefaultItemMaster() {
		List<ItemMaster> itemMasterList = new ArrayList<>();
		ItemMaster itemMaster = new ItemMaster();
		itemMasterList.add(itemMaster);
		return itemMasterList;
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
	 * Creates a list of graphics codes.
	 * @return a list of graphics codes.
	 */
	private List<ScaleGraphicsCode> getDefaultGraphicsCodeList() {
		List<ScaleGraphicsCode> graphicsCodeList = new ArrayList<>();
		ScaleGraphicsCode scaleGraphicsCode = new ScaleGraphicsCode();
		scaleGraphicsCode.setScaleGraphicsCode(0L);
		scaleGraphicsCode.setScaleGraphicsCodeDescription(testString);
		scaleGraphicsCode.setScaleScanCodeCount(testInt);
		graphicsCodeList.add(scaleGraphicsCode);
		return graphicsCodeList;
	}

	/**
	 * Gets Default hits.
	 * @return a list of hits.
	 */
	private List<Long> getDefaultHits() {
		List<ScaleUpc> actionCodeList = this.getDefaultScaleUpcList();

		return actionCodeList.stream().map(ScaleUpc::getPlu).collect(
				Collectors.toList());
	}
}