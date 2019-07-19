/*
 *  CreateDiscontinueCsvTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.*;
import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author s573181
 * @since 2.0.7
 */
public class CreateDiscontinueCsvTest {

	public static final String DEFAULT_CSV_VALUE = "\"1783158\",\"4775412479\",\"Associate\",\"1 ft\",\"622\"," +
			"\"Warehouse\",\"LICENSED WALL PUZZLE\",\"Not Eligible:  Store receipts as of: 2015-12-20, Has Inventory," +
			" has PO \",\"\"\n";
	@Test
	public void createCsv(){
		Assert.assertEquals(CreateDiscontinueCsv.createCsv(this.getDefaultObject()), DEFAULT_CSV_VALUE);
	}

	/**
	 * Returns the default ProductDelete to test against. This is the first record in the sample data.
	 *
	 * @return The default ProductDelete to test against.
	 */
	private PageableResult<ProductDiscontinue> getDefaultObject() {

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
		sellingUnit.setTagSize("1 ft");

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

		List<ProductDiscontinue> productDiscontinues = new ArrayList<>();
		productDiscontinues.add(pd);
		return new PageableResult<ProductDiscontinue>(1, productDiscontinues);
	}

}
