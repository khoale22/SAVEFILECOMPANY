package com.heb.pm.entity;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the ProductDiscontinue class.
 *
 * @author d116773
 * @since 2.0.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProductDiscontinueTest {

	@Autowired
	ProductDiscontinueRepositoryTest productDiscontinueRepository;

	/*
	 * hashCode
	 */

	/**
	 * Tests hashCode with the same object.
	 */
	@Test
	public void hashCodeSameObject() {
		ProductDiscontinue productDiscontinue = this.getDefaultObject();
		Assert.assertEquals("hash code wrong", productDiscontinue.getKey().hashCode(), productDiscontinue.hashCode());
	}

	/**
	 * Tests hashCode with a null key.
	 */
	@Test
	public void hashCodeNullKey() {
		ProductDiscontinue productDiscontinue = new ProductDiscontinue();
		Assert.assertEquals("hash code no key wrong", 0, productDiscontinue.hashCode());
	}

	/**
	 * Tests hashCode with two objects with the same value.
	 */
	@Test
	public void hashCodeSimilarObjects() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		ProductDiscontinue productDiscontinue2 = this.getDefaultObject();

		Assert.assertEquals("hash codes not equal", productDiscontinue1.hashCode(), productDiscontinue2.hashCode());
	}

	/**
	 * Tests hashCode with two different objects.
	 */
	@Test
	public void hashCodeDifferentObject() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		ProductDiscontinue productDiscontinue2 = this.getTestObjectDifferentKey();

		Assert.assertNotEquals("new key hash code equal", productDiscontinue1.hashCode(), productDiscontinue2.hashCode());
	}

	/*
	 * equals
	 */

	/**
	 * Tests equals on the same object.
	 */
	@Test
	public void equalsSameObject() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		boolean equals = productDiscontinue1.equals(productDiscontinue1);
		Assert.assertTrue("equals same object fails", equals);
	}

	/**
	 * Tests equals on a different type of object.
	 */
	@Test
	public void equalsDifferentObjectType() {
		ProductDiscontinue productDiscontinue = this.getDefaultObject();
		boolean equals = productDiscontinue.equals(Integer.valueOf(0));
		Assert.assertFalse("different object type fails", equals);
	}

	/**
	 * Tests equals on two different objects with the same values.
	 */
	@Test
	public void equalsSimilarObject() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		ProductDiscontinue productDiscontinue2 = this.getDefaultObject();
		boolean equals = productDiscontinue1.equals(productDiscontinue2);
		Assert.assertTrue("equals similar object fails", equals);
	}

	/**
	 * Tests equals with two object with different keys.
	 */
	@Test
	public void equalsDifferentKey() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		ProductDiscontinue productDiscontinue2 = this.getTestObjectDifferentKey();
		boolean equals = productDiscontinue1.equals(productDiscontinue2);
		Assert.assertFalse("equals different key fails", equals);
	}

	/**
	 * Test equals where the object compared to has null for a key.
	 */
	@Test
	public void equalsNullKey() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		productDiscontinue1.setKey(null);
		ProductDiscontinue productDiscontinue2 = this.getDefaultObject();
		boolean equals = productDiscontinue1.equals(productDiscontinue2);
		Assert.assertFalse("null key fails", equals);
	}

	/**
	 * Tests equals when both objects have a null key.
	 */
	@Test
	public void equalsBothNullKey() {
		ProductDiscontinue productDiscontinue1 = this.getDefaultObject();
		productDiscontinue1.setKey(null);
		ProductDiscontinue productDiscontinue2 = this.getDefaultObject();
		productDiscontinue2.setKey(null);
		boolean equals = productDiscontinue1.equals(productDiscontinue2);
		Assert.assertTrue("two null keys fails", equals);
	}

	/**
	 * Tests equals when null is passed in.
	 */
	@Test
	public void equalsNull() {
		ProductDiscontinue productDiscontinue = this.getDefaultObject();
		boolean equals = productDiscontinue.equals(null);
		Assert.assertFalse("null other fails", equals);
	}


	/*
	 * JPA mapping
	 */
	/**
	 * Tests that the object is mapping correctly.
	 */
	@Test
	@Transactional
	public void mapping() {

		ProductDiscontinue pd = this.productDiscontinueRepository.findOne(this.getDefaultObject().getKey());
		this.fullProductDiscontinueCompare(this.getDefaultObject(), pd);

		pd = this.productDiscontinueRepository.findOne(this.getRecord15().getKey());
		this.fullProductDiscontinueCompare(this.getRecord15(), pd);
	}

	/*
	 * getSortByAllColumns
	 */

	/**
	 * Test getSortByAllColumns when doing ascending sort.
	 */
	@Test
	public void getSortByAllColumnsAscending() {
		Sort s = new Sort(
				new Sort.Order(Sort.Direction.ASC, "salesSet"),
				new Sort.Order(Sort.Direction.ASC, "inventorySet"),
				new Sort.Order(Sort.Direction.ASC, "storeReceiptsSet"),
				new Sort.Order(Sort.Direction.ASC, "newItemSet"),
				new Sort.Order(Sort.Direction.ASC, "openPoSet"),
				new Sort.Order(Sort.Direction.ASC, "warehouseInventorySet"),
				new Sort.Order(Sort.Direction.ASC, "key.itemCode"),
				new Sort.Order(Sort.Direction.ASC, "key.upc")
		);
		Assert.assertEquals("sort ascending wrong", s, ProductDiscontinue.getSortByAllColumns(Sort.Direction.ASC));
	}

	/**
	 * Test getSortByAllColumns when doing descending sort.
	 */
	@Test
	public void sortByAllColumnsDescending() {
		Sort s = new Sort(
				new Sort.Order(Sort.Direction.DESC, "salesSet"),
				new Sort.Order(Sort.Direction.DESC, "inventorySet"),
				new Sort.Order(Sort.Direction.DESC, "storeReceiptsSet"),
				new Sort.Order(Sort.Direction.DESC, "newItemSet"),
				new Sort.Order(Sort.Direction.DESC, "openPoSet"),
				new Sort.Order(Sort.Direction.DESC, "warehouseInventorySet"),
				new Sort.Order(Sort.Direction.DESC, "key.itemCode"),
				new Sort.Order(Sort.Direction.DESC, "key.upc")
		);
		Assert.assertEquals("sort descending wrong", s, ProductDiscontinue.getSortByAllColumns(Sort.Direction.DESC));
	}

	/*
	 * getProjectedDeleteDate
	 */

	/**
	 * Tests getProjectedDeleteDate on an active item.
	 */
	@Test
	public void getProjectedDeleteDateActive() {
		ProductDiscontinue pd = this.getDefaultObject();
		Assert.assertNull(pd.getProjectedDeleteDate());
	}

	/**
	 * Test getProjectedDeleteDate on a warehouse item that was discontinued on a Saturday.
	 */
	@Test
	public void getProjectedDeleteDateWarehouseSaturday() {
		ProductDiscontinue pd = this.getDiscontinuedWarehouse(LocalDateTime.of(2016, 6, 11, 12, 34, 15));
		Assert.assertEquals(LocalDate.of(2016, 7, 3), pd.getProjectedDeleteDate());
	}

	/**
	 * Test getProjectedDeleteDate on a warehouse item that was discontinued on a Sunday.
	 */
	@Test
	public void getProjectedDeleteDateWarehouseSunday() {
		ProductDiscontinue pd = this.getDiscontinuedWarehouse(LocalDateTime.of(2016, 6, 12, 12, 34, 15));
		Assert.assertEquals(LocalDate.of(2016, 7, 10), pd.getProjectedDeleteDate());
	}

	/**
	 * Test getProjectedDeleteDate on a warehouse item that was discontinued on a Monday.
	 */
	@Test
	public void getProjectedDeleteDateWarehouseMonday() {
		ProductDiscontinue pd = this.getDiscontinuedWarehouse(LocalDateTime.of(2016, 6, 13, 12, 34, 15));
		Assert.assertEquals(LocalDate.of(2016, 7, 10), pd.getProjectedDeleteDate());
	}

	/**
	 * Tests getProjectedDeleteDate on a discontinued DSD item.
	 */
	@Test
	public void getProjectedDeleteDateDsd() {
		ProductDiscontinue pd = this.getDiscontinuedDSD(LocalDate.of(2016, 5, 16));
		Assert.assertEquals(LocalDate.of(2016, 6, 6), pd.getProjectedDeleteDate());
	}

	/*
	 * getters
	 */

	/**
	 * Test getSalesSet
	 */
	@Test
	public void getSalesSet() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertTrue("sales wrong", p.getSalesSet());
	}

	/**
	 * Test getInventorySet
	 */
	@Test
	public void getInventorySet() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertFalse("inventory wrong", p.getInventorySet());
	}

	/**
	 * Test getStoreReceiptsSet
	 */
	@Test
	public void getStoreReceiptsSet() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertFalse("store receipts wrong", p.getStoreReceiptsSet());
	}

	/**
	 * Test getLastReceivedDate
	 */
	@Test
	public void getLastReceivedDate() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertEquals("last received date",LocalDate.of(2015, 12, 20), p.getLastReceivedDate() );
	}

	/**
	 * Test getNewItemSet
	 */
	@Test
	public void getNewItemSet() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertTrue("new item set wrong", p.getNewItemSet());
	}

	/**
	 * Test getOpenPoSet
	 */
	@Test
	public void getOpenPoSet() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertFalse("open po set wrong", p.getOpenPoSet());
	}

	/**
	 * Test getAutoDiscontinued.
	 */
	@Test
	public void getAutoDiscontinued() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertTrue("auto discontinue wrong", p.getAutoDiscontinued());
	}

	/**
	 * Test getDiscontinued
	 */
	@Test
	public void getDiscontinued() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertFalse("discontinued wrong", p.getDiscontinued());
	}

	/**
	 * Test getCreateId
	 */
	@Test
	public void getCreateId() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertEquals("create id wrong","TIBCRAWL            ", p.getCreateId());
	}

	/**
	 * Test getCreateTime
	 */
	@Test
	public void getCreateTime() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertEquals("create time wrong", LocalDateTime.of(2016, 4, 30, 14, 28, 56), p.getCreateTime());
	}

	/**
	 * Test getLastUpdateId
	 */
	@Test
	public void getLastUpdateId() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertEquals("last update id wrong", "BATCH               ", p.getLastUpdateId());
	}

	/**
	 * Test getLastUpdateTime
	 */
	@Test
	public void getLastUpdateTime() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertEquals("last update time wrong", LocalDateTime.of(2016, 5, 1, 0, 12, 10), p.getLastUpdateTime());
	}

	/**
	 * Test getWarehouseInventorySet
	 */
	@Test
	public void getWarehouseInventorySet() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertTrue("warehouse inventory wrong", p.getWarehouseInventorySet());
	}

	/**
	 * Test getLastBillDate
	 */
	@Test
	public void getLastBillDate() {
		ProductDiscontinue p = this.getDefaultObject();
		Assert.assertEquals("last bill date wrong", LocalDate.of(2015, 7, 22), p.getLastBillDate());
	}

	/**
	 * Test getSellingUnit
	 */
	@Test
	public void getSellingUnit() {
		ProductDiscontinue p = this.getDefaultObject();
		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(4775412479L);
		Assert.assertEquals(sellingUnit, p.getSellingUnit());
	}

	/*
	 * Support functions.
	 */

	/**
	 * Since equals on ProductDiscontinue only looks at the key, this examines the whole object ot make sure
	 * all the values are the same.
	 *
	 * @param a The first ProductDiscontinue to look at.
	 * @param b The second ProductDiscontinue to look at.
	 */
	private void fullProductDiscontinueCompare(ProductDiscontinue a, ProductDiscontinue b) {

		Assert.assertEquals(a.getKey(), b.getKey());
		Assert.assertEquals(a.getSalesSet(), b.getSalesSet());
		Assert.assertEquals(a.getInventorySet(), b.getInventorySet());
		Assert.assertEquals(a.getStoreReceiptsSet(), b.getStoreReceiptsSet());
		Assert.assertEquals(a.getLastReceivedDate(), b.getLastReceivedDate());
		Assert.assertEquals(a.getNewItemSet(), b.getNewItemSet());
		Assert.assertEquals(a.getOpenPoSet(), b.getOpenPoSet());
		Assert.assertEquals(a.getAutoDiscontinued(), b.getAutoDiscontinued());
		Assert.assertEquals(a.getCreateId(), b.getCreateId());
		Assert.assertEquals(a.getCreateTime(), b.getCreateTime());
		Assert.assertEquals(a.getLastUpdateId(), b.getLastUpdateId());
		Assert.assertEquals(a.getLastUpdateTime(), b.getLastUpdateTime());
		Assert.assertEquals(a.getWarehouseInventorySet(), b.getWarehouseInventorySet());
		Assert.assertEquals(a.getLastBillDate(), b.getLastBillDate());
		Assert.assertEquals(a.getItemMaster().getKey(), b.getItemMaster().getKey());
	}

	/**
	 * Returns the default ProductDelete to test against. This is the first record in the sample data.
	 *
	 * @return The default ProductDelete to test against.
	 */
	private ProductDiscontinue getDefaultObject() {
		ProductDiscontinueKey key = new ProductDiscontinueKey();
		key.setUpc(4775412479L);
		key.setProductId(1783158);
		key.setItemCode(622);
		key.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMasterKey itemMasterKey = new ItemMasterKey();
		itemMasterKey.setItemCode(622L);
		itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(itemMasterKey);
		itemMaster.setDescription("LICENSED WALL PUZZLE");

		WarehouseLocationItemKey wliKey = new WarehouseLocationItemKey();
		wliKey.setWarehouseType("W");
		wliKey.setItemType(ItemMasterKey.WAREHOUSE);
		wliKey.setWarehouseNumber(404);
		wliKey.setItemCode(622);
		WarehouseLocationItem wli = new WarehouseLocationItem();
		wli.setKey(wliKey);
		wli.setPurchasingStatus("A");
		List<WarehouseLocationItem> wliItems = new ArrayList<>();
		wliItems.add(wli);
		itemMaster.setWarehouseLocationItems(wliItems);

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(4775412479L);

		ProductDiscontinue pd = new ProductDiscontinue();
		pd.setKey(key);
		pd.setSalesSet(true);
		pd.setInventorySet(false);
		pd.setStoreReceiptsSet(false);
		pd.setLastReceivedDate(LocalDate.of(2015, 12, 20));
		pd.setNewItemSet(true);
		pd.setOpenPoSet(false);
		pd.setAutoDiscontinued(true);
		pd.setCreateId("TIBCRAWL            ");
		pd.setCreateTime(LocalDateTime.of(2016, 4, 30, 14, 28, 56));
		pd.setLastUpdateId("BATCH               ");
		pd.setLastUpdateTime(LocalDateTime.of(2016, 5, 1, 0, 12, 10));
		pd.setWarehouseInventorySet(true);
		pd.setLastBillDate(LocalDate.of(2015, 7, 22));
		pd.setItemMaster(itemMaster);
		pd.setSellingUnit(sellingUnit);

		return pd;
	}

	/**
	 * Returns the default object with a different UPC.
	 *
	 * @return The default object with a different UPC.
	 */
	private ProductDiscontinue getTestObjectDifferentKey() {
		ProductDiscontinue productDiscontinue = this.getDefaultObject();
		productDiscontinue.getKey().setUpc(4775412474L);

		return productDiscontinue;
	}

	/**
	 * Returns a ProductDiscontinue record that matches the 15th record in the sample data.
	 *
	 * @return A ProductDiscontinue record that matches the 15th record in the sample data.
	 */
	private ProductDiscontinue getRecord15() {
		ProductDiscontinueKey key = new ProductDiscontinueKey();
		key.setUpc(9015909553L);
		key.setProductId(1783150);
		key.setItemCode(223);
		key.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMasterKey itemMasterKey = new ItemMasterKey();
		itemMasterKey.setItemCode(223L);
		itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(itemMasterKey);
		itemMaster.setDescription("1:12 HARLEY DAVIDSON CYCLES");

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setUpc(9015909553L);

		ProductDiscontinue pd = new ProductDiscontinue();
		pd.setKey(key);
		pd.setSalesSet(false);
		pd.setInventorySet(false);
		pd.setStoreReceiptsSet(false);
		pd.setLastReceivedDate(LocalDate.of(2016, 4, 13));
		pd.setNewItemSet(true);
		pd.setOpenPoSet(false);

		pd.setAutoDiscontinued(false);
		pd.setCreateId("TIBCRAWL            ");
		pd.setCreateTime(LocalDateTime.of(2016, 4, 30, 15, 45, 17));
		pd.setLastUpdateId("TIBCRAWL            ");
		pd.setLastUpdateTime(LocalDateTime.of(2016, 4, 30, 15, 45, 17));
		pd.setWarehouseInventorySet(false);
		pd.setLastBillDate(LocalDate.of(2016, 4, 13));
		pd.setItemMaster(itemMaster);
		pd.setSellingUnit(sellingUnit);

		return pd;
	}

	/**
	 * Returns a discontinued warehouse record. This only populates enough to test calculateWarehouseDeleteDate.
	 *
	 * @param discontinueTime The time to use as discontinue item.
	 * @return A discontinued warehouse record
	 */
	private ProductDiscontinue getDiscontinuedWarehouse(LocalDateTime discontinueTime) {
		WarehouseLocationItem wlItem = new WarehouseLocationItem();
		wlItem.setPurchasingStatus(PurchasingStatusCode.CodeValues.DISCONTINUED.getPurchasingStatus());
		wlItem.setPurchaseStatusUpdateTime(discontinueTime);
		List<WarehouseLocationItem> wlItems = new ArrayList<>();
		wlItems.add(wlItem);

		ItemMasterKey imKey = new ItemMasterKey();
		imKey.setItemType(ItemMasterKey.WAREHOUSE);

		ItemMaster im = new ItemMaster();
		im.setKey(imKey);
		im.setWarehouseLocationItems(wlItems);

		ProductDiscontinueKey pdKey = new ProductDiscontinueKey();
		pdKey.setItemType(ItemMasterKey.WAREHOUSE);

		ProductDiscontinue pd = new ProductDiscontinue();
		pd.setItemMaster(im);
		pd.setKey(pdKey);

		return pd;
	}

	/**
	 * Returns a discontinued DSD record. This only populates enough to test calculateDSDDeleteDate.
	 *
	 * @param discontinueDate The date to use as discontinue item.
	 * @return A discontinued DSD record
	 */
	private ProductDiscontinue getDiscontinuedDSD(LocalDate discontinueDate) {

		ItemMasterKey imKey = new ItemMasterKey();
		imKey.setItemType(ItemMasterKey.DSD);

		ItemMaster im = new ItemMaster();
		im.setKey(imKey);
		im.setDiscontinueDate(discontinueDate);

		ProductDiscontinueKey pdKey = new ProductDiscontinueKey();
		pdKey.setItemType(ItemMasterKey.DSD);

		ProductDiscontinue pd = new ProductDiscontinue();
		pd.setItemMaster(im);
		pd.setKey(pdKey);

		return pd;
	}
}
