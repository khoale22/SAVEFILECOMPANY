/*
 * InventoryServiceTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.inventory;

import com.heb.pm.repository.TimRepository;
import com.heb.tim.services.vo.PurchaseOrderVO;
import com.heb.tim.services.vo.StoreInventoryVO;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * @author d116773
 * @since 2.0.1
 */
public class InventoryServiceTest {

	private static final int GOOD_STORE = 444;
	private static final int BAD_STORE = 116;

	private static final long PRODUCT_ID = 127127L;
	private static final int INVENTORY = 1234490;
	private static final long ITEM_CODE = 207877L;
	private static final String ORDERED_DATE = "1600-10-10 00:00:00";

	/*
	 * getTotalStoreUnitsOnHand
	 */

	/**
	 * Tests getTotalStoreUnitsOnHand when the TIM DAO returns null for getUnitsOnHandSingleProduct.
	 */
	@Test
	public void getTotalStoreUnitsOnHandNullMap() {
		InventoryService inventoryService = new InventoryService();

		inventoryService.setTimRepository(this.getTimRespositoryStoreNull());

		try {
			inventoryService.getTotalStoreUnitsOnHand(InventoryServiceTest.PRODUCT_ID);
			Assert.fail("should have thrown an exception");
		} catch (TimException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Tests getTotalStoreUnitsOnHand with a map with good and bad stores.
	 */
	@Test
	public void getTotalStoreUnitsOnHandFullMap() {
		InventoryService inventoryService = new InventoryService();

		inventoryService.setTimRepository(this.getTimRepositoryStore());

		float inventory = inventoryService.getTotalStoreUnitsOnHand(InventoryServiceTest.PRODUCT_ID);
		Assert.assertEquals(InventoryServiceTest.INVENTORY, inventory, 1.0);
	}

	/**
	 * Tests getTotalStoreUnitsOnHand when the repository throws an exception.
	 */
	@Test
	public void getTotalStoreUnitsOnHandTimException() {
		InventoryService inventoryService = new InventoryService();

		inventoryService.setTimRepository(this.getTimRepositoryStoreError());

		try {
			inventoryService.getTotalStoreUnitsOnHand(InventoryServiceTest.PRODUCT_ID);
			Assert.fail("did not throw exception");
		} catch (TimException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Returns a List that will contain inventory data. There will be one good store and one bad store.
	 *
	 * @return A List that will contain inventory data. There will be one good store and one bad store.
	 */
	private ArrayList getInventoryList() {
		ArrayList<StoreInventoryVO> list = new ArrayList<>();

		StoreInventoryVO voGood = new StoreInventoryVO();
		voGood.setLocationNbr(Integer.toString(InventoryServiceTest.GOOD_STORE));
		voGood.setUnitOnHand(Integer.toString(InventoryServiceTest.INVENTORY));
		list.add(voGood);

		StoreInventoryVO voBad = new StoreInventoryVO();
		voBad.setLocationNbr(Integer.toString(InventoryServiceTest.BAD_STORE));
		// No units on hand.
		list.add(voBad);

		return list;
	}

	/**
	 * Return a TimRepository that will return null for getStoreInventory.
	 *
	 * @return A TimRepository that will return null for getStoreInventory.
	 */
	private TimRepository getTimRespositoryStoreNull() {

		TimRepository timRepository = Mockito.mock(TimRepository.class);
		Mockito.when(timRepository.getStoreInventory(Mockito.anyLong())).thenReturn(null);

		return timRepository;
	}

	/**
	 * Returns a TimRepository that will throw an error when getStoreInventory is called.
	 *
	 * @return A TimRepository that will throw an error when getStoreInventory is called.
	 */
	private TimRepository getTimRepositoryStoreError() {

		TimException e = new TimException("bad data");
		TimRepository timRepository = Mockito.mock(TimRepository.class);
		Mockito.when(timRepository.getStoreInventory(Mockito.anyLong())).thenThrow(e);

		return timRepository;
	}

	/**
	 * Returns a TimRepository that will return a list with one good store and on null store.
	 *
	 * @return A TimRepository  that will return a list with one good store and on null store..
	 */
	private TimRepository getTimRepositoryStore() {

		TimRepository timRepository = Mockito.mock(TimRepository.class);
		Mockito.when(timRepository.getStoreInventory(Mockito.anyLong()))
				.thenReturn(this.getInventoryList());

		return timRepository;
	}


	/**
	 * Tests getLatestPurchaseOrder to see if it recieves the latest purchase order
	 */
	@Test
	public void getLatestPurchaseOrderTest() {
		InventoryService inventoryService = new InventoryService();

		inventoryService.setTimRepository(this.getTimRepositoryPO());

		PurchaseOrderVO purchaseOrderVO = inventoryService.getLatestPurchaseOrder(InventoryServiceTest.ITEM_CODE);
		Assert.assertNotNull("Purchase orders was null", purchaseOrderVO);
	}

	/**
	 * Tests getLatestPurchaseOrder when the repository throws an exception.
	 */
	@Test
	public void getLatestPurchaseOrderTimException() {
		InventoryService inventoryService = new InventoryService();

		inventoryService.setTimRepository(this.getTimRepositoryPOError());

		try {
			inventoryService.getLatestPurchaseOrder(InventoryServiceTest.ITEM_CODE);
			Assert.fail("did not throw exception");
		} catch (TimException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Tests getLatestPurchaseOrder when the TIM DAO returns null for getPurchaseOrders.
	 */
	@Test
	public void getLatestPurchaseOrderNull() {
		InventoryService inventoryService = new InventoryService();

		inventoryService.setTimRepository(this.getTimRespositoryPONull());

		try {
			inventoryService.getLatestPurchaseOrder(InventoryServiceTest.ITEM_CODE);
			Assert.fail("should have thrown an exception");
		} catch (TimException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Return a TimRepository that will return null for getPurchaseOrders.
	 *
	 * @return A TimRepository that will return null for getPurchaseOrders.
	 */
	private TimRepository getTimRespositoryPONull() {

		TimRepository timRepository = Mockito.mock(TimRepository.class);
		Mockito.when(timRepository.getPurchaseOrders(Mockito.anyLong())).thenReturn(null);

		return timRepository;
	}

	/**
	 * Returns a TimRepository that will throw an error when getPurchaseOrders is called.
	 *
	 * @return A TimRepository that will throw an error when getPurchaseOrders is called.
	 */
	private TimRepository getTimRepositoryPOError() {

		TimException e = new TimException("bad data");
		TimRepository timRepository = Mockito.mock(TimRepository.class);
		Mockito.when(timRepository.getPurchaseOrders(Mockito.anyLong())).thenThrow(e);

		return timRepository;
	}

	/**
	 * Returns a TimRepository that will return a list of purchase orders.
	 *
	 * @return A TimRepository  that will return a list of purchase orders.
	 */
	private TimRepository getTimRepositoryPO() {

		TimRepository timRepository = Mockito.mock(TimRepository.class);
		Mockito.when(timRepository.getPurchaseOrders(Mockito.anyLong()))
				.thenReturn(this.getPurchaseOrderList());

		return timRepository;
	}

	/**
	 * Returns a List that will contain purchase orders.
	 *
	 * @return A List that will contain purchase orders.
	 */
	private ArrayList getPurchaseOrderList() {
		ArrayList<PurchaseOrderVO> list = new ArrayList<>();

		PurchaseOrderVO purchaseOrderVOGood = new PurchaseOrderVO();
		purchaseOrderVOGood.setOrderedDate(InventoryServiceTest.ORDERED_DATE);
		list.add(purchaseOrderVOGood);

		return list;
	}
}
