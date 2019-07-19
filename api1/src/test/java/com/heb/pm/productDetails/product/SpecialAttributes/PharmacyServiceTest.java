package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.pm.entity.DrugScheduleType;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.DrugScheduleTypeRepository;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

public class PharmacyServiceTest {
	private String DEFAULT_STRING="TEST";

	@InjectMocks
	private PharmacyService service;

	@Mock
	private DrugScheduleTypeRepository repository;

	@Mock
	private ProductInfoRepository productInfoRepository;

	@Mock
	private ProductManagementServiceClient productManagementServiceClient;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	private ProductInfoResolver resolver;

	/**
	 * Tests the getDrugScheduleType method
	 */
	@Test
	public void getDrugScheduleTypeTest() {
		List<DrugScheduleType> test = getDefaultDrugScheduleTypeList();
		Mockito.when(this.repository.findAll()).thenReturn(getDefaultDrugScheduleTypeList());
		List<DrugScheduleType> result = this.service.getDrugSchedulerTypes();
		Assert.assertEquals(test, result);
	}

	/**
	 * Tests the saving of pharmacy changes.
	 */
	@Test
	public void testSavePharmacyChanges() {
		ProductMaster productMaster = this.getDefaultProductMaster();
		Mockito.when(this.productInfoRepository.findOne(Mockito.anyLong())).thenReturn(productMaster);
		ProductMaster result = this.service.savePharmacyChanges(productMaster);
		Assert.assertEquals(result, productMaster);
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
	 * Creates a default list for testing
	 * @return
	 */
	private List<DrugScheduleType> getDefaultDrugScheduleTypeList(){
		ArrayList<DrugScheduleType> list = new ArrayList<>();
		list.add(getDefaultDrugScheduleType());
		return list;
	}

	/**
	 * Creates a default record for testing
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