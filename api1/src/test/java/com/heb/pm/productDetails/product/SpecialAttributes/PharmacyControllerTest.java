package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.DrugScheduleType;
import com.heb.pm.entity.ProductMaster;
import com.heb.util.controller.ModifiedEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import testSupport.CommonMocks;

import java.util.ArrayList;
import java.util.List;

public class PharmacyControllerTest {

	private String DEFAULT_STRING="TEST";

	@InjectMocks
	private PharmacyController controller;

	@Mock
	private PharmacyService service;

	@Mock
	private MessageSource messageSource;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		this.controller.setUserInfo(CommonMocks.getUserInfo());
	}

	/**
	 * Tests the getDrugScheduleType method
	 */
	@Test
	public void getDrugScheduleTypeTest() {
		List<DrugScheduleType> test = getDefaultDrugScheduleTypeList();
		Mockito.when(this.service.getDrugSchedulerTypes()).thenReturn(getDefaultDrugScheduleTypeList());
		List<DrugScheduleType> result = this.controller.getDrugScheduleType(CommonMocks.getServletRequest());
		Assert.assertEquals(test, result);
	}

	@Test
	public void testSavePharmacyChanges() {
		ProductMaster productMaster = this.getDefaultProductMaster();
		Mockito.when(this.service.savePharmacyChanges(Mockito.any(ProductMaster.class))).thenReturn(productMaster);
		ModifiedEntity<ProductMaster> result = this.controller.savePharmacyChanges(productMaster, CommonMocks.getServletRequest());
		Assert.assertEquals(productMaster, result.getData());
	}

	/**
	 * Creates a defualt product master used for testing.
	 * @return product Master
	 */
	private ProductMaster getDefaultProductMaster() {
		ProductMaster pm = new ProductMaster();
		pm.setProdItems(new ArrayList<>());
		pm.setSellingUnits(new ArrayList<>());
		return pm;
	}

	/**
	 * Generates a default list for testing
	 * @return
	 */
	private List<DrugScheduleType> getDefaultDrugScheduleTypeList() {
		ArrayList<DrugScheduleType> list = new ArrayList<>();
		list.add(getDefaultDrugScheduleType());
		return list;
	}

	/**
	 * Generates a default record for testing
	 * @return
	 */
	private DrugScheduleType getDefaultDrugScheduleType(){
		DrugScheduleType drugScheduleType = new DrugScheduleType();
		drugScheduleType.setAbbreviation(DEFAULT_STRING);
		drugScheduleType.setDescription(DEFAULT_STRING);
		drugScheduleType.setId(DEFAULT_STRING);
		return drugScheduleType;
	}
}