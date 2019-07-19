/*
 *
 *  DiscontinueParametersToDiscontinueRulesTest
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import com.heb.pm.entity.DiscontinueExceptionParametersAudit;
import com.heb.pm.entity.DiscontinueExceptionParametersAuditKey;
import com.heb.pm.entity.DiscontinueParameterType;
import com.heb.pm.entity.DiscontinueParameters;
import com.heb.pm.entity.DiscontinueParametersAudit;
import com.heb.pm.entity.DiscontinueParametersAuditKey;
import com.heb.pm.entity.DiscontinueParametersKey;
import com.heb.pm.entity.ItemClass;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.entity.SubCommodity;
import com.heb.pm.entity.SubDepartment;
import com.heb.pm.entity.SubDepartmentKey;
import com.heb.pm.entity.Vendor;
import com.heb.pm.productHierarchy.ItemClassDocument;
import com.heb.pm.productHierarchy.ItemClassService;
import com.heb.pm.repository.ItemClassIndexRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.product.UpcService;
import com.heb.pm.productHierarchy.ClassCommodityService;
import com.heb.pm.productHierarchy.CommodityDocument;
import com.heb.pm.repository.CommodityIndexRepository;
import com.heb.pm.productHierarchy.SubCommodityDocument;
import com.heb.pm.repository.SubCommodityIndexRepository;
import com.heb.pm.productHierarchy.SubCommodityService;
import com.heb.pm.productHierarchy.SubDepartmentService;
import com.heb.pm.vendor.VendorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by l730832 on 9/27/2016.
 *
 * Test class to unit test DiscontinueParametersToDiscontinueRules class
 */
public class DiscontinueParametersToDiscontinueRulesTest {

	private static final Integer DEFAULT_CLASS_CODE = 1;
	private static final Integer DEFAULT_COMMODITY_CODE = 1;
	private static final String DEFAULT_CLASS_DESCRIPTION = "Class";

	@InjectMocks
	private DiscontinueParametersToDiscontinueRules discontinueParametersToDiscontinueRules;

	@Mock
	private VendorService vendorService;

	@Mock
	private SubDepartmentService subDepartmentService;

	@Mock
	private ClassCommodityService classCommodityService;

	@Mock
	private SubCommodityService subCommodityService;

	@Mock
	private UpcService upcService;

	@Mock
	private ItemClassIndexRepository itemClassIndexRepository;

	@Mock
	private CommodityIndexRepository commodityIndexRepository;

	@Mock
	private SubCommodityIndexRepository subCommodityIndexRepository;

	@Mock
	private SellingUnitRepository sellingUnitRepository;

	@Mock
	private ItemClassService itemClassService;

	private DiscontinueRules discontinueRules;
	private DiscontinueParametersKey discontinueParametersKey;
	private DiscontinueParameterType discontinueParameterType;
	private DiscontinueParameters discontinueParameters;
	private DiscontinueParametersAudit discontinueParametersAudit;
	private DiscontinueParametersAuditKey discontinueParametersAuditKey;
	private DiscontinueExceptionParametersAuditKey discontinueExceptionParametersAuditKey;
	private DiscontinueExceptionParametersAudit discontinueExceptionParametersAudit;
	private List<DiscontinueParameters> discontinueParametersList;
	private List<DiscontinueParametersAudit> discontinueParametersAuditList;
	private List<DiscontinueExceptionParametersAudit> discontinueExceptionParametersAuditList;
	private Vendor vendor;
	private SubDepartment subDepartment;
	private SubDepartmentKey subDepartmentKey;
	private ClassCommodity classCommodity;
	private ItemClass itemClass;
	private ClassCommodityKey classCommodityKey;
	private ItemClassDocument itemClassDocument;
	private CommodityDocument commodityDocument;
	private SubCommodityDocument subCommodityDocument;
	private SubCommodity subCommodity;
	private SellingUnit sellingUnit;
	private ProductMaster productMaster;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		discontinueRules = new DiscontinueRules();
		discontinueParametersList = new ArrayList<>();
		discontinueParameters = new DiscontinueParameters();
		discontinueParametersAudit = new DiscontinueParametersAudit();
		discontinueExceptionParametersAudit = new DiscontinueExceptionParametersAudit();
		discontinueParametersKey = new DiscontinueParametersKey();
		discontinueParametersAuditKey = new DiscontinueParametersAuditKey();
		discontinueExceptionParametersAuditKey = new DiscontinueExceptionParametersAuditKey();
		discontinueParametersAuditList = new ArrayList<>();
		discontinueExceptionParametersAuditList = new ArrayList<>();

		vendor = new Vendor();
		subDepartment = new SubDepartment();
		subDepartmentKey = new SubDepartmentKey();
		classCommodity = new ClassCommodity();
		itemClass = new ItemClass();
		classCommodityKey = new ClassCommodityKey();
		itemClassDocument = new ItemClassDocument();
		commodityDocument = new CommodityDocument();
		subCommodity = new SubCommodity();
		subCommodityDocument = new SubCommodityDocument();
		sellingUnit = new SellingUnit();
		productMaster = new ProductMaster();


		discontinueParameters.setKey(discontinueParametersKey);
		discontinueParametersList.add(discontinueParameters);

		discontinueParametersAuditKey.setTimestamp(LocalDateTime.now());
		discontinueParametersAudit.setKey(discontinueParametersAuditKey);
		discontinueParametersAuditList.add(discontinueParametersAudit);

		discontinueExceptionParametersAuditKey.setTimestamp(LocalDateTime.now());
		discontinueExceptionParametersAudit.setKey(discontinueExceptionParametersAuditKey);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);

		subDepartmentKey.setDepartment("Dept");
		subDepartmentKey.setSubDepartment("Sub");
		subDepartment.setKey(subDepartmentKey);

		classCommodityKey.setClassCode(DEFAULT_CLASS_CODE);
		classCommodityKey.setCommodityCode(DEFAULT_COMMODITY_CODE);
		classCommodity.setKey(classCommodityKey);

		itemClass.setItemClassCode(DEFAULT_CLASS_CODE);

		productMaster.setDescription("UPC");
	}

	/**
	 * Tests toDiscontinueRules function with the type Store Sales
	 */
	@Test
	public void testToDiscontinueRulesStoreSales() {
		setDiscountRulesAttributes();
		setDiscontinueParametersAttributes();
		discontinueParameters.getKey().setId(1);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDiscontinueRules function with the type New Item Period to introduce Added Date
	 */
	@Test
	public void testToDiscontinueRulesAddedDate() {
		setDiscountRulesAttributes();
		setDiscontinueParametersAttributes();
		discontinueParameters.getKey().setId(2);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDiscontinueRules function with the type WareHouse Units
	 */
	@Test
	public void testToDiscontinueRulesWareHouseInventory() {
		setDiscountRulesAttributes();
		setDiscontinueParametersAttributes();
		discontinueParameters.getKey().setId(3);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDiscontinueRules function with the type Store Units
	 */
	@Test
	public void testToDiscontinueRulesStoreInventory() {
		setDiscountRulesAttributes();
		setDiscontinueParametersAttributes();
		discontinueParameters.getKey().setId(4);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDiscontinueRules function with the type Store Receipts
	 */
	@Test
	public void testToDiscontinueRulesLastRecieved() {
		setDiscountRulesAttributes();
		setDiscontinueParametersAttributes();
		discontinueParameters.getKey().setId(5);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDiscontinueRules function with the type Purchase Orders
	 */
	@Test
	public void testToDiscontinueRulesOrderedDate() {
		setDiscountRulesAttributes();
		setDiscontinueParametersAttributes();
		discontinueParameters.getKey().setId(6);
		discontinueParameters.getKey().setSequenceNumber(2);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDiscontinueRules function with an illegal argument
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testToDiscontinueRulesIllegalArgumentException() {
		setDiscountRulesAttributes();
		discontinueParameters.setParameterValue("FailedParameter");
		discontinueParameters.getKey().setId(5);
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueRules(discontinueParametersList));
	}

	/**
	 * Tests toDisctontinueParameters function with type Purchase Orders.
	 */
	@Test
	public void testToDiscontinueParametersPurchaseOrders() {
		setDiscountRulesAttributes();
		discontinueParameterType = discontinueParameterType.getTypeById(6);
		discontinueParametersKey.setId(discontinueParameterType.getId());
		Assert.assertNotNull("Results are null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueParameters(discontinueRules));
	}

	/**
	 * Tests toDisctontinueParameters function with type Purchase Orders but throws illegal arguments
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testToDiscontinueParametersIllegalArgumentException() {
		discontinueRules.setStoreSales("");
		discontinueRules.setSalesSwitch(false);
		discontinueParameterType = discontinueParameterType.getTypeById(6);
		discontinueParametersKey.setId(discontinueParameterType.getId());
		this.discontinueParametersToDiscontinueRules.toDiscontinueParameters(discontinueRules);
	}

	/**
	 * Tests toDisctontinueParameters function with type Purchase Orders but throws null pointer
	 */
	@Test(expected = NullPointerException.class)
	public void testToDiscontinueParametersNullPointerException() {
		discontinueParameterType = discontinueParameterType.getTypeById(6);
		discontinueParametersKey.setId(discontinueParameterType.getId());
		this.discontinueParametersToDiscontinueRules.toDiscontinueParameters(discontinueRules);
	}

	/**
	 * Tests toDisctontinueException function
	 */
	@Test
	public void testToDiscontinueExceptionParameters() {
		setDiscountRulesAttributes();
		discontinueParameterType = discontinueParameterType.getTypeById(6);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules.toDiscontinueExceptionParameters(discontinueRules));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Sales
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordStoreSales() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(1);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Sales with
	 * previous audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordStoreSalesPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(1);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type New Item Period
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordNewItemPeriod() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(2);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type New Item Period with
	 * previous audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordNewItemPeriodPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(2);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Warehouse Unit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordWareHouseUnits() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(3);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Warehouse unit with
	 * previous audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordWareHouseUnitsPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(3);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Unit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordStoreUnits() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(4);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Unit with
	 * previous Audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordStoreUnitsPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(4);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Receipts
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordStoreReceipts() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(5);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Receipts with
	 * previous audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordStoreReceiptsPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(5);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase Order
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordPurchaseOrders() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(6);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase Order with
	 * previous audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordPurchaseOrdersPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(6);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase Order with
	 * non-default sequence number
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordPurchaseOrdersSequenceNumber() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(6);
		discontinueParametersAudit.setSequenceNumber(2);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase Order with
	 * non-default sequence number and previous audit
	 */
	@Test
	public void testConvertDiscontinueParametersAuditToDiscontinueParametersAuditRecordPurchaseOrdersSequenceNumberPreviousAuditMap() {
		setDiscontinueParametersAuditAttributes();
		discontinueParametersAudit.setId(6);
		discontinueParametersAudit.setSequenceNumber(2);
		discontinueParametersAuditList.add(discontinueParametersAudit);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertDiscontinueParametersAuditToDiscontinueParametersAuditRecord(discontinueParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Sales
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordStoreSales() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(1);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Sales with
	 * previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordStoreSalesPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(1);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type New Item Period
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewItemPeriod() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(2);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type New Item Period with
	 * previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewItemPeriodPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(2);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type warehouse unit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordWareHouseUnits() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(3);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type warehouse unit with
	 * previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordWareHouseUnitsPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(3);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Unit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordStoreUnits() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(4);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Unit with
	 * previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordStoreUnitsPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(4);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Receipt
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordStoreReceipts() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(5);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Store Receipt with
	 * previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordStoreReceiptsPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(5);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase order
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordPurchaseOrders() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(6);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase Order with
	 * previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordPurchaseOrdersPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(6);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase order with
	 * non-default sequence number
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordPurchaseOrdersSequenceNumber() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(6);
		discontinueExceptionParametersAudit.setSequenceNumber(2);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with type Purchase order with
	 * non-default sequence number with previous audit
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordPurchaseOrdersSequenceNumberPreviousAuditMap() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		setVendorAttributes();
		discontinueExceptionParametersAudit.setId(6);
		discontinueExceptionParametersAudit.setSequenceNumber(2);
		discontinueExceptionParametersAuditList.add(discontinueExceptionParametersAudit);
		Mockito.when(this.vendorService.findByVendorNumber(Mockito.anyInt())).thenReturn(vendor);
		Assert.assertNotNull("Result is null", vendor);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with SBT attributes
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewProductSetupSBT() {
		setDiscontinueExceptionParametersAuditAttributes("SBT", "1");
		discontinueExceptionParametersAudit.setId(2);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with Dept attributes
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewProductSetupDEPT() {
		setDiscontinueExceptionParametersAuditAttributes("Dept", "3");
		discontinueExceptionParametersAudit.setId(2);
		Mockito.when(this.subDepartmentService.findSubDepartment(Mockito.anyString())).thenReturn(subDepartment);
		Assert.assertNotNull("Result is null", subDepartment);
		subDepartment.setName("Dept");
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with Class attributes
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewProductSetupCLASS() {
		setDiscontinueExceptionParametersAuditAttributes("Class", "4");
		discontinueExceptionParametersAudit.setId(2);
		Mockito.when(this.itemClassIndexRepository.findOne(Mockito.anyString())).thenReturn(itemClassDocument);
		Assert.assertNotNull("Result is null", itemClassDocument);

		Mockito.when(this.itemClassService.findOne(Mockito.anyString())).thenReturn(itemClass);
		Assert.assertNotNull("Result is null", itemClass);
		itemClass.setItemClassDescription(DEFAULT_CLASS_DESCRIPTION);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with Commodity attributes
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewProductSetupCOMMODITY() {
		setDiscontinueExceptionParametersAuditAttributes("Commodity", "5");
		discontinueExceptionParametersAudit.setId(2);
		Mockito.when(this.commodityIndexRepository.findOne(Mockito.anyString())).thenReturn(commodityDocument);
		Assert.assertNotNull("Results are null", commodityDocument);

		Mockito.when(this.classCommodityService.findCommodity(Mockito.anyInt())).thenReturn(classCommodity);
		Assert.assertNotNull("Result is null", classCommodity);
		classCommodity.setName("Commodity");
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with Sub-commodity attributes
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewProductSetupSUBCOMMODITY() {
		setDiscontinueExceptionParametersAuditAttributes("Sub-Commodity", "6");
		discontinueExceptionParametersAudit.setId(2);
		Mockito.when(this.subCommodityIndexRepository.findOne(Mockito.anyString())).thenReturn(subCommodityDocument);
		Assert.assertNotNull("Results are null", subCommodityDocument);

		Mockito.when(this.subCommodityService.findSubCommodity(Mockito.anyInt())).thenReturn(subCommodity);
		Assert.assertNotNull("Result is null", subCommodity);
		subCommodity.setName("Sub-Commodity");
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with UPC attributes
	 */
	@Test
	public void testConvertExceptionParametersAuditToDiscontinueParametersAuditRecordNewProductSetupUPC() {
		setDiscontinueExceptionParametersAuditAttributes("UPC", "8");
		discontinueExceptionParametersAudit.setId(2);
		Mockito.when(this.sellingUnitRepository.findOne(Mockito.anyLong())).thenReturn(sellingUnit);
		Assert.assertNotNull("Results are null", sellingUnit);

		Mockito.when(this.upcService.find(Mockito.anyLong())).thenReturn(sellingUnit);
		Assert.assertNotNull("Result is null", sellingUnit);
		sellingUnit.setProductMaster(productMaster);
		Assert.assertNotNull("Result is null",
				this.discontinueParametersToDiscontinueRules
						.convertExceptionParametersAuditToDiscontinueParametersAuditRecord(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with empty attributes
	 */
	@Test
	public void testConvertDeletedExceptionParametersAuditToDiscontinueRulesEmptyList() {
		discontinueExceptionParametersAuditList = null;
		Assert.assertNotNull("Results are null", this.discontinueParametersToDiscontinueRules.convertDeletedExceptionParametersAuditToDiscontinueRules(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with DEL action code
	 */
	@Test
	public void testConvertDeletedExceptionParametersAuditToDiscontinueRulesDeleteActionCode() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		discontinueExceptionParametersAudit.setAction("DEL");
		Assert.assertNotNull("Results are null", this.discontinueParametersToDiscontinueRules.convertDeletedExceptionParametersAuditToDiscontinueRules(discontinueExceptionParametersAuditList));
	}

	/**
	 * Tests convertExceptionDiscontinueParametersAuditToDiscontinueParametersAuditRecord with something other than DEL
	 * action code
	 */
	@Test
	public void testConvertDeletedExceptionParametersAuditToDiscontinueRulesNotDeleteActionCode() {
		setDiscontinueExceptionParametersAuditAttributes("Vendor", "7");
		discontinueExceptionParametersAudit.setAction("ADD");
		Assert.assertNotNull("Results are null", this.discontinueParametersToDiscontinueRules.convertDeletedExceptionParametersAuditToDiscontinueRules(discontinueExceptionParametersAuditList));
	}

	/**
	 * Sets Discontinue Rules attributes to test
	 */
	private void setDiscountRulesAttributes() {
		discontinueRules.setStoreSales("1");
		discontinueRules.setSalesSwitch(false);
		discontinueRules.setNewProductSetupSwitch(false);
		discontinueRules.setNewItemPeriod("2");
		discontinueRules.setWarehouseUnitSwitch(false);
		discontinueRules.setWarehouseUnits("3");
		discontinueRules.setStoreUnitSwitch(false);
		discontinueRules.setStoreUnits("4");
		discontinueRules.setReceiptsSwitch(false);
		discontinueRules.setStoreReceipts("5");
		discontinueRules.setPurchaseOrderSwitch(false);
		discontinueRules.setPurchaseOrders("6");
	}

	/**
	 * Sets Discontinue Parameters attributes to test
	 */
	private void setDiscontinueParametersAttributes() {
		discontinueParameters.setParameterValue("1");
		discontinueParameters.setActive(false);
	}

	/**
	 * Sets discontinue Parameters Audit attributes to test
	 */
	private void setDiscontinueParametersAuditAttributes() {
		discontinueParametersAudit.setParameterName("Parameter Name");
		discontinueParametersAudit.setParameterValue("1");
		discontinueParametersAudit.setPriority(0);
		discontinueParametersAudit.setActive(false);
		discontinueParametersAudit.setAction("Actions");
		discontinueParametersAudit.setUserId("User Id");
	}

	/**
	 * Sets discontinue Exception Parameters Audit attributes to test
	 * @param exceptionType Exception Type
	 * @param exceptionTypeId Exception Type Id
	 */
	private void setDiscontinueExceptionParametersAuditAttributes(String exceptionType, String exceptionTypeId) {
		discontinueExceptionParametersAudit.setParameterValue("1");
		discontinueExceptionParametersAudit.setPriority(0);
		discontinueExceptionParametersAudit.setActive(false);
		discontinueExceptionParametersAudit.setAction("Actions");
		discontinueExceptionParametersAudit.setUserId("User Id");
		discontinueExceptionParametersAudit.setExceptionNumber(1);
		discontinueExceptionParametersAudit.setExceptionType(exceptionType);
		discontinueExceptionParametersAudit.setExceptionTypeId(exceptionTypeId);
	}

	/**
	 * Sets vendor attributes to test
	 */
	private void setVendorAttributes() {
		vendor.setVendorName("Vendor");
		vendor.setVendorNumber(1);
		vendor.setVendorNumberAsString("1");
	}

}
