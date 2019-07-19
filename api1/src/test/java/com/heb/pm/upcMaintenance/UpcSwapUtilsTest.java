/*
 *  UpcSwapUtilsTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.entity.*;
import com.heb.pm.repository.AssociatedUpcRepository;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.TimRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * @author s573181
 * @since 2.0.5
 */
public class UpcSwapUtilsTest {


	@Test
	public void findAllDsdToBoth(){
		// DSD to Both
		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		AssociatedUpcRepository associatedUpcRepository = Mockito.mock(AssociatedUpcRepository.class);
		ItemMasterRepository itemMasterRepository = Mockito.mock(ItemMasterRepository.class);
		upcSwapUtils.setAssociatedUpcRepository(associatedUpcRepository);
		upcSwapUtils.setItemMasterRepository(itemMasterRepository);
		upcSwapUtils.setTimRepository(this.getTimRepository());
		Mockito.when(itemMasterRepository.findOne(this.getDsdToBothItemMasterKey())).thenReturn(this.getDsdToBothItemMaster());
		Mockito.when(associatedUpcRepository.findOne(this.getDsdToBothUpcList().get(0))).thenReturn(this.getDsdToBothAssociatedUpc());
		this.fullDsdToBothCompare(upcSwapUtils.findAll(this.getDsdToBothUpcList(), this.getDsdToBothItemCodeList(), UpcSwapUtils.DSD_TO_BOTH).get(0));
	}

	@Test
	public void findAllWhsToWhs(){
		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		AssociatedUpcRepository associatedUpcRepository = Mockito.mock(AssociatedUpcRepository.class);
		ItemMasterRepository itemMasterRepository = Mockito.mock(ItemMasterRepository.class);
		upcSwapUtils.setAssociatedUpcRepository(associatedUpcRepository);
		upcSwapUtils.setItemMasterRepository(itemMasterRepository);
		upcSwapUtils.setTimRepository(this.getTimRepository());
		Mockito.when(itemMasterRepository.findOne(this.getWhsToWhsDestinationItemMasterKey())).thenReturn(this.getWhsToWhsDestinationItemMaster());
		Mockito.when(associatedUpcRepository.findOne(this.getWhsToWhsUpcList().get(0))).thenReturn(this.getWhsToWhsAssociatedUpc());
		this.fullWhsToWhsCompare(upcSwapUtils.findAll(this.getWhsToWhsUpcList(), this.getWhsToWhsItemCodeList(), UpcSwapUtils.WHS_TO_WHS).get(0));
	}

	@Test
	public void findAllBothToDsd(){
		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		AssociatedUpcRepository associatedUpcRepository = Mockito.mock(AssociatedUpcRepository.class);
		ItemMasterRepository itemMasterRepository = Mockito.mock(ItemMasterRepository.class);
		upcSwapUtils.setAssociatedUpcRepository(associatedUpcRepository);
		upcSwapUtils.setItemMasterRepository(itemMasterRepository);
		upcSwapUtils.setTimRepository(this.getTimRepository());
		Mockito.when(associatedUpcRepository.findOne(this.getBothToDsdUpcList().get(0))).thenReturn(this.getBothToDsdAssociatedUpc());
		this.fullBothToDsdCompare(upcSwapUtils.findAll(this.getBothToDsdUpcList(), null, UpcSwapUtils.BOTH_TO_DSD).get(0));
	}

	@Test
	public void getPrimaryFirstUpcList(){
		AssociatedUpcRepository associatedUpcRepository = Mockito.mock(AssociatedUpcRepository.class);
		ItemMasterRepository itemMasterRepository = Mockito.mock(ItemMasterRepository.class);
		Mockito.when(itemMasterRepository.findOne(this.getDefaultItemMasterKey())).thenReturn(this.getDefaultItemMaster());
		UpcSwapUtils upcSwapUtils = new UpcSwapUtils();
		upcSwapUtils.setAssociatedUpcRepository(associatedUpcRepository);
		upcSwapUtils.setItemMasterRepository(itemMasterRepository);
		List<Long> upcList = upcSwapUtils.getPrimaryFirstUpcList(this.getDefaultItemMasterKey());
		for(int x = 0; x<upcList.size(); x++){
			if (x == 0){
				Assert.assertEquals(12345L, upcList.get(x).longValue());
			} else {
				Assert.assertNotEquals(12345L, upcList.get(x).longValue());
			}
		}
	}

	@Test
	public void validateUpcSwapList(){

	}

	/**
	 * Support functions.
	 */


	private List<UpcSwap> getUpcSwapWithMultipleSameUpcs(){
		List<UpcSwap> upcSwaps = new ArrayList<>();
		UpcSwap upcSwap = new UpcSwap();
		upcSwap.setSourceUpc(4011L);
		upcSwap.setMakeDestinationPrimaryUpc(false);
		UpcSwap.SwappableEndPoint source = new UpcSwap().new SwappableEndPoint();

		UpcSwap.SwappableEndPoint destination = new UpcSwap().new SwappableEndPoint();
		return upcSwaps;
	}

	/**
	 * Returns an ItemMasterKey for a non DSD item code.
	 *
	 * @return an ItemMasterKey for a non DSD item code.
	 */
	private ItemMasterKey getDefaultItemMasterKey(){
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType(ItemMasterKey.WAREHOUSE);
		key.setItemCode(123L);
		return key;
	}

	/**
	 * Returns an ItemMaster for a non DSD item code.
	 *
	 * @return an ItemMaster for a non DSD item code.
	 */
	private ItemMaster getDefaultItemMaster(){
		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(this.getDefaultItemMasterKey());
		itemMaster.setPrimaryUpc(this.getDefaultPrimaryUpc());
		return itemMaster;
	}

	/**
	 * Returns a default PrimaryUpc for a non DSD item code.
	 *
	 * @return a default PrimaryUpc for a non DSD item code.
	 */
	private PrimaryUpc getDefaultPrimaryUpc(){
		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(12345);

		AssociatedUpc associatedUpc1 = new AssociatedUpc();
		associatedUpc1.setUpc(12345);
		associatedUpc1.setPrimaryUpc(primaryUpc);
		associatedUpc1.setSellingUnit(new SellingUnit());
		associatedUpc1.getSellingUnit().setPrimaryUpc(true);

		AssociatedUpc associatedUpc2 = new AssociatedUpc();
		associatedUpc2.setUpc(23456);
		associatedUpc2.setPrimaryUpc(primaryUpc);
		associatedUpc2.setSellingUnit(new SellingUnit());
		associatedUpc2.getSellingUnit().setPrimaryUpc(false);

		List<AssociatedUpc> associatedUpcs = new ArrayList<>();
		associatedUpcs.add(associatedUpc2);
		associatedUpcs.add(associatedUpc1);
		primaryUpc.setAssociateUpcs(associatedUpcs);
		return primaryUpc;
	}

	/**
	 * Gets a UPC list for the DSD to both find all function.
	 *
	 * @return  a UPC list for the DSD to both find all function.
	 */
	private List<Long> getDsdToBothUpcList(){
		List<Long> upcList = new ArrayList<>();
		upcList.add(20302500000L);
		return upcList;
	}
	/**
	 * Gets an Item Code list for the DSD to both find all function.
	 *
	 * @return  an Item Code UPC list for the DSD to both find all function.
	 */
	private List<Long> getDsdToBothItemCodeList(){
		List<Long> itemCodeList = new ArrayList<>();
		itemCodeList.add(72553L);
		return itemCodeList;
	}

	/**
	 * Gets an Item Code list for the whs to whs find all function.
	 *
	 * @return  an Item Code list for  the whs whs find all function.
	 */
	private List<Long> getWhsToWhsItemCodeList(){
		List<Long> itemCodeList = new ArrayList<>();
		itemCodeList.add(46740L);
		return itemCodeList;
	}

	/**
	 * Returns an Item Master Key for DSD to Both functions.
	 *
	 * @return an Item Master Key for DSD to Both functions.
	 */
	private ItemMasterKey getDsdToBothItemMasterKey(){
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(72553L);
		key.setItemType(ItemMasterKey.WAREHOUSE);
		return key;
	}

	/**
	 * Returns an Item Master for DSD to Both functions.
	 *
	 * @return an Item Master for DSD to Both functions.
	 */
	private ItemMaster getDsdToBothItemMaster(){
		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(this.getDsdToBothItemMasterKey());
		itemMaster.setOrderingUpc(3025);
		itemMaster.setDescription("CHILACA PEPPER                ");
		itemMaster.setItemSize("lb");
		List<ProdItem> prodItems = new ArrayList<>();
		ProdItem prodItem = new ProdItem();
		ProdItemKey key = new ProdItemKey();
		key.setProductId(894446L);
		prodItem.setKey(key);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		productMaster.setProdId(894446L);
		prodItem.setProductMaster(productMaster);

		prodItems.add(prodItem);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(3025L);
		itemMaster.setPrimaryUpc(primaryUpc);

		List<WarehouseLocationItem> warehouseList = new ArrayList<>();
		WarehouseLocationItem warehouseLocationItem = new WarehouseLocationItem();
		warehouseLocationItem.setTotalOnHandInventory(0);

		List<WarehouseLocationItemExtendedAttributes> warehouseAttributesList = new ArrayList<>();
		WarehouseLocationItemExtendedAttributes warehouseAttributes = new WarehouseLocationItemExtendedAttributes();
		warehouseAttributes.setTotalOnOrder(0);
		warehouseAttributesList.add(warehouseAttributes);

		itemMaster.setProdItems(prodItems);
		itemMaster.setWarehouseLocationItems(warehouseList);
		itemMaster.setWarehouseLocationItemExtendedAttributes(warehouseAttributesList);

		return itemMaster;
	}

	/**
	 * Returns an associated upc for DSD to Both functions.
	 *
	 * @return an associated upc for DSD to Both functions.
	 */
	private AssociatedUpc getDsdToBothAssociatedUpc(){
		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(20302500000L);

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setPrimaryUpc(true);
		sellingUnit.setUpc(20302500000L);
		sellingUnit.setProdId(894446);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		productMaster.setProdId(894446L);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(20302500000L);
		List<ItemMaster> itemMasters = new ArrayList<>();
		ItemMaster itemMaster = new ItemMaster();
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType(ItemMasterKey.DSD);
		key.setItemCode(20302500000L);
		itemMaster.setKey(key);

		List<ProdItem> prodItems = new ArrayList<>();
		ProdItem prodItem = new ProdItem();
		ProdItemKey prodItemKey = new ProdItemKey();
		prodItemKey.setProductId(894446L);
		prodItem.setKey(prodItemKey);
		itemMaster.setProdItems(prodItems);
		itemMasters.add(itemMaster);

		primaryUpc.setItemMasters(itemMasters);
		associatedUpc.setPrimaryUpc(primaryUpc);

		List<AssociatedUpc> associatedUpcs = new ArrayList<>();
		AssociatedUpc associatedUpc1 = new AssociatedUpc();
		associatedUpc1.setUpc(20302500000L);
		associatedUpc1.setSellingUnit(sellingUnit);
		associatedUpc1.setPrimaryUpc(primaryUpc);
		associatedUpcs.add(associatedUpc1);

		return associatedUpc;
	}

	/**
	 * A full compare for DSD to Both upc swaps.
	 *
	 * @param a upc swap to compare.
	 */
	private void fullDsdToBothCompare(UpcSwap a){
		Assert.assertEquals(a.getSourceUpc().longValue(), 20302500000L);
		Assert.assertTrue(a.isSourcePrimaryUpc());
		Assert.assertEquals(a.getDestinationPrimaryUpc().longValue(), 3025L);
		Assert.assertFalse(a.isMakeDestinationPrimaryUpc());

		Assert.assertEquals(a.getSource().getProductId().longValue(), 894446L);
		Assert.assertEquals(a.getSource().getProductRetailLink().longValue(), 0L);
		Assert.assertNull(a.getSource().getAssociatedUpcList());
		Assert.assertNull(a.getSource().getErrorMessage());
		Assert.assertEquals(a.getSource().getItemCode().longValue(), 20302500000L);

		Assert.assertEquals(a.getDestination().getProductId().longValue(), 894446L);
		Assert.assertEquals(a.getDestination().getItemDescription(), "CHILACA PEPPER                ");
		Assert.assertEquals(a.getDestination().getItemSize(), "lb");
		Assert.assertEquals(a.getDestination().getItemCode().longValue(), 72553L);
		Assert.assertEquals(a.getDestination().getProductRetailLink().longValue(), 0L);
		Assert.assertNull(a.getDestination().getErrorMessage());
		Assert.assertNull(a.getDestination().getErrorMessage());
	}

	/**
	 * Returns a destination Item Master Key for WHS to Whs functions.
	 *
	 * @return a destination Item Master Key for WHS to Whs functions.
	 */
	private ItemMasterKey getWhsToWhsDestinationItemMasterKey(){
		ItemMasterKey key = new ItemMasterKey();
		key.setItemCode(46740L);
		key.setItemType(ItemMasterKey.WAREHOUSE);
		return key;
	}

	/**
	 * Returns a destination item master for whs to whs functions.
	 *
	 * @return  a destination item master for whs to whs functions.
	 */
	private ItemMaster getWhsToWhsDestinationItemMaster(){
		ItemMaster itemMaster = new ItemMaster();
		itemMaster.setKey(this.getWhsToWhsDestinationItemMasterKey());

		itemMaster.setOrderingUpc(40046740000L);
		itemMaster.setDescription("#191 RENAISSANCE CUT FRUIT CD ");
		itemMaster.setItemSize("ounce");

		List<ProdItem> prodItems = new ArrayList<>();
		ProdItem prodItem = new ProdItem();
		ProdItemKey key = new ProdItemKey();
		key.setProductId(490896L);
		prodItem.setKey(key);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		productMaster.setProdId(490896L);
		prodItem.setProductMaster(productMaster);

		prodItems.add(prodItem);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(40046740000L);
		itemMaster.setPrimaryUpc(primaryUpc);

		List<WarehouseLocationItem> warehouseList = new ArrayList<>();
		WarehouseLocationItem warehouseLocationItem = new WarehouseLocationItem();
		warehouseLocationItem.setTotalOnHandInventory(0);

		List<WarehouseLocationItemExtendedAttributes> warehouseAttributesList = new ArrayList<>();
		WarehouseLocationItemExtendedAttributes warehouseAttributes = new WarehouseLocationItemExtendedAttributes();
		warehouseAttributes.setTotalOnOrder(0);
		warehouseAttributesList.add(warehouseAttributes);

		itemMaster.setProdItems(prodItems);
		itemMaster.setWarehouseLocationItems(warehouseList);
		itemMaster.setWarehouseLocationItemExtendedAttributes(warehouseAttributesList);

		return itemMaster;
	}

	/**
	 * Returns a list of associated upcs for the source in the whs to whs functions.
	 *
	 * @return a list of associated upcs for the source in the whs to whs functions.
	 */
	private List<Long> getWhsToWhsSourceAssociatedUpcs(){
		List<Long> associatedUpcList = new ArrayList<>();
		associatedUpcList.add(4186L);
		return associatedUpcList;
	}

	/**
	 * Returns a list of upcs to be used in the whs to whs find all function.
	 *
	 * @return a list of upcs to be used in the whs to whs find all function.
	 */
	private List<Long> getWhsToWhsUpcList(){
		List<Long> upcList = new ArrayList<>();
		upcList.add(4011L);
		return upcList;
	}

	/**
	 * Returns an associated upc to be used in the whs to whs find all function.
	 *
	 * @return an associated upc to be used in the whs to whs find all function.
	 */
	private AssociatedUpc getWhsToWhsAssociatedUpc(){
		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(4011L);

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setPrimaryUpc(true);
		sellingUnit.setUpc(4011L);
		sellingUnit.setProdId(377497);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		productMaster.setProdId(894446L);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(4011L);
		List<ItemMaster> itemMasters = new ArrayList<>();
		ItemMaster itemMaster = new ItemMaster();
		ItemMasterKey key = new ItemMasterKey();
		key.setItemType(ItemMasterKey.WAREHOUSE);
		key.setItemCode(428557L);
		itemMaster.setKey(key);
		itemMaster.setDescription("YELLOW BANANAS                ");
		itemMaster.setItemSize("lb");
		List<ProdItem> prodItems = new ArrayList<>();
		ProdItem prodItem = new ProdItem();
		ProdItemKey prodItemKey = new ProdItemKey();
		prodItemKey.setProductId(377497L);
		prodItem.setKey(prodItemKey);
		itemMaster.setProdItems(prodItems);
		itemMasters.add(itemMaster);

		primaryUpc.setItemMasters(itemMasters);
		associatedUpc.setPrimaryUpc(primaryUpc);

		List<AssociatedUpc> associatedUpcs = new ArrayList<>();
		AssociatedUpc associatedUpc1 = new AssociatedUpc();
		SellingUnit sellingUnit1 = new SellingUnit();
		sellingUnit1.setPrimaryUpc(true);
		sellingUnit1.setUpc(4186L);
		sellingUnit1.setProdId(377497);
		associatedUpc1.setUpc(4186L);
		associatedUpc1.setSellingUnit(sellingUnit1);

		associatedUpc1.setPrimaryUpc(primaryUpc);
		associatedUpcs.add(associatedUpc1);

		associatedUpc.getPrimaryUpc().setAssociateUpcs(associatedUpcs);
		return associatedUpc;
	}

	/**
	 * Compares a UPC swap returned in the whs to whs find all to what is expected
	 *
	 * @param a a UPC swap returned in the whs to whs find all function.
	 */
	private void fullWhsToWhsCompare(UpcSwap a){
		Assert.assertEquals(a.getSourceUpc().longValue(), 4011L);
		Assert.assertTrue(a.isSourcePrimaryUpc());
		Assert.assertEquals(a.getDestinationPrimaryUpc().longValue(), 40046740000L);
		Assert.assertFalse(a.isMakeDestinationPrimaryUpc());

		Assert.assertEquals(a.getSource().getProductId().longValue(), 377497L);
		Assert.assertEquals(a.getSource().getProductRetailLink().longValue(), 0L);
		Assert.assertEquals(a.getSource().getAssociatedUpcList(), this.getWhsToWhsSourceAssociatedUpcs());
		Assert.assertEquals(a.getSource().getItemDescription(), "YELLOW BANANAS                ");
		Assert.assertNull(a.getSource().getItemSize());
		Assert.assertNull(a.getSource().getErrorMessage());
		Assert.assertEquals(a.getSource().getItemCode().longValue(), 428557L);

		Assert.assertEquals(a.getDestination().getProductId().longValue(), 490896);
		Assert.assertEquals(a.getDestination().getItemDescription(), "#191 RENAISSANCE CUT FRUIT CD ");
		Assert.assertEquals(a.getDestination().getItemSize(), "ounce");
		Assert.assertEquals(a.getDestination().getItemCode().longValue(), 46740L);
		Assert.assertEquals(a.getDestination().getProductRetailLink().longValue(), 0L);
		Assert.assertNull(a.getDestination().getErrorMessage());
		Assert.assertNull(a.getDestination().getErrorMessage());
	}

	/**
	 * Returns an associated upc to be used in the both to dsd functions.
	 *
	 * @return an associated upc to be used in the both to dsd functions.
	 */
	private AssociatedUpc getBothToDsdAssociatedUpc(){
		AssociatedUpc associatedUpc = new AssociatedUpc();
		associatedUpc.setUpc(20302500000L);

		SellingUnit sellingUnit = new SellingUnit();
		sellingUnit.setPrimaryUpc(true);
		sellingUnit.setUpc(20302500000L);
		sellingUnit.setProdId(894446);

		ProductMaster productMaster = new ProductMaster();
		productMaster.setRetailLink(0);
		productMaster.setProdId(894446L);
		sellingUnit.setProductMaster(productMaster);
		associatedUpc.setSellingUnit(sellingUnit);

		PrimaryUpc primaryUpc = new PrimaryUpc();
		primaryUpc.setUpc(3025L);
		List<ItemMaster> itemMasters = new ArrayList<>();

		ItemMaster dsdItemMaster = new ItemMaster();
		ItemMasterKey dsdItemMasterKey = new ItemMasterKey();
		dsdItemMasterKey.setItemType(ItemMasterKey.DSD);
		dsdItemMasterKey.setItemCode(3025L);
		dsdItemMaster.setKey(dsdItemMasterKey);

		List<ProdItem> prodItems = new ArrayList<>();
		ProdItem prodItem = new ProdItem();
		ProdItemKey prodItemKey = new ProdItemKey();
		prodItemKey.setProductId(894446L);
		prodItemKey.setItemCode(3025L);
		prodItemKey.setItemType(ItemMasterKey.DSD);
		prodItem.setKey(prodItemKey);
		dsdItemMaster.setProdItems(prodItems);
		itemMasters.add(dsdItemMaster);

		ItemMaster warehouseItemMaster = new ItemMaster();
		ItemMasterKey warehouseKey = new ItemMasterKey();
		warehouseKey.setItemType(ItemMasterKey.WAREHOUSE);
		warehouseKey.setItemCode(72553L);
		warehouseItemMaster.setKey(warehouseKey);

		List<ProdItem> whsProdItems = new ArrayList<>();
		ProdItem whsProdItem = new ProdItem();
		ProdItemKey whsProdItemKey = new ProdItemKey();
		whsProdItemKey.setProductId(894446L);
		whsProdItemKey.setItemCode(72553L);
		whsProdItemKey.setItemType(ItemMasterKey.WAREHOUSE);
		whsProdItem.setKey(whsProdItemKey);
		warehouseItemMaster.setProdItems(whsProdItems);
		itemMasters.add(warehouseItemMaster);


		primaryUpc.setItemMasters(itemMasters);
		associatedUpc.setPrimaryUpc(primaryUpc);
		List<AssociatedUpc> associatedUpcs = new ArrayList<>();
		AssociatedUpc associatedUpc1 = new AssociatedUpc();
		associatedUpc1.setUpc(20302500000L);
		associatedUpc1.setSellingUnit(sellingUnit);
		associatedUpc1.setPrimaryUpc(primaryUpc);
		associatedUpcs.add(associatedUpc1);
		primaryUpc.setAssociateUpcs(associatedUpcs);
		return associatedUpc;
	}

	/**
	 * Returns a upc list to be used in the both to dsd functions.
	 *
	 * @return a upc list to be used in the both to dsd functions.
	 */
	private List<Long>  getBothToDsdUpcList(){
			List<Long> upcList = new ArrayList<>();
			upcList.add(20302500000L);
			return upcList;
	}

	/**
	 * Compares a UPC swap returned in the both to dsd find all to what is expected.
	 *
	 * @param a a UPC swap returned in the both to dsd find all to what is expected.
	 */
	private void fullBothToDsdCompare(UpcSwap a){
		Assert.assertEquals(a.getSourceUpc().longValue(), 20302500000L);
		Assert.assertTrue(a.isSourcePrimaryUpc());
		Assert.assertNull(a.getDestinationPrimaryUpc());
		Assert.assertFalse(a.isMakeDestinationPrimaryUpc());

		Assert.assertEquals(a.getSource().getProductId().longValue(), 894446L);
		Assert.assertEquals(a.getSource().getProductRetailLink().longValue(), 0L);
		Assert.assertNull(a.getSource().getErrorMessage());
		Assert.assertEquals(a.getSource().getItemCode().longValue(), 72553L);
		Assert.assertEquals(a.getSource().getItemType(), ItemMasterKey.WAREHOUSE);

		Assert.assertNull(a.getDestination());
	}

	/**
	 * Returns a TimRepository to test with.
	 *
	 * @return a TimRepository to test with.
	 */
	private TimRepository getTimRepository(){
		TimRepository repository = Mockito.mock(TimRepository.class);
		Mockito.when(repository.getPurchaseOrders(Mockito.anyLong())).thenReturn(null);
		return repository;
	}
}
