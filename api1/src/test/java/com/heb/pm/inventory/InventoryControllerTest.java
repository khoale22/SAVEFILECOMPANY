/*
 * InventoryControllerTest
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.inventory;

import com.heb.tim.services.vo.PurchaseOrderVO;
import com.heb.util.controller.UserInfo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Tests InventoryController.
 *
 * @author d116773
 * @since 2.0.1
 */
public class InventoryControllerTest {

	private static final float INVENTORY = 8789877;
	private static final String PURCHASE_ORDER_NUMBER = "2";
	private static final long PRODUCT_ID = 127127l;

	// Date Time Formatter
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	/**
	 * Tests the getInventory method of InventoryController's inner InventoryWrapper class.
	 */
	@Test
	public void inventoryWrapperGetInventory() {

		InventoryController inventoryController = new InventoryController();
		InventoryController.InventoryWrapper inventoryWrapper =
				inventoryController.new InventoryWrapper(
						InventoryControllerTest.PRODUCT_ID, InventoryControllerTest.INVENTORY);
		Assert.assertEquals(InventoryControllerTest.INVENTORY, inventoryWrapper.getInventory(), 1.0);
	}

	/**
	 * Tests the getProductId method of InventoryController's inner InventoryWrapper class.
	 */
	@Test
	public void inventoryWrapperGetProductId() {

		InventoryController inventoryController = new InventoryController();
		InventoryController.InventoryWrapper inventoryWrapper =
				inventoryController.new InventoryWrapper(
						InventoryControllerTest.PRODUCT_ID, InventoryControllerTest.INVENTORY);
		Assert.assertEquals(InventoryControllerTest.PRODUCT_ID, inventoryWrapper.getProductId());
	}

	/**
	 * Tests the constructor for a purchase order with a PO number and date
	 * of InventoryController's inner InventoryWrapper class.
	 */
	@Test
	public void inventoryWrapperPurchaseOrderConstructor() {

		InventoryController inventoryController = new InventoryController();
		PurchaseOrderVO purchaseOrderVO = new PurchaseOrderVO();
		String formattedDate = dateTimeFormatter.format(LocalDateTime.now());
		purchaseOrderVO.setOrderedDate(formattedDate);
		purchaseOrderVO.setPoNumber(InventoryControllerTest.PURCHASE_ORDER_NUMBER);

		InventoryController.InventoryWrapper inventoryWrapper =
				inventoryController.new InventoryWrapper(purchaseOrderVO);
		Assert.assertNotNull("Results are null", inventoryWrapper);
	}

	/**
	 * Tests the constructor for a null purchase order of InventoryController's inner InventoryWrapper class.
	 */
	@Test
	public void inventoryWrapperPurchaseOrderConstructorNull() {

		InventoryController inventoryController = new InventoryController();

		InventoryController.InventoryWrapper inventoryWrapper =
				inventoryController.new InventoryWrapper(null);
		Assert.assertNotNull("Results are null", inventoryWrapper);
	}

	/**
	 * Tests the getPurchaseOrderNumber method of InventoryController's inner InventoryWrapper class.
	 */
	@Test
	public void inventoryWrapperGetPurchaseOrderNumber() {

		InventoryController inventoryController = new InventoryController();
		PurchaseOrderVO purchaseOrderVO = new PurchaseOrderVO();
		String formattedDate = dateTimeFormatter.format(LocalDateTime.now());
		purchaseOrderVO.setOrderedDate(formattedDate);
		purchaseOrderVO.setPoNumber(InventoryControllerTest.PURCHASE_ORDER_NUMBER);

		InventoryController.InventoryWrapper inventoryWrapper =
				inventoryController.new InventoryWrapper(purchaseOrderVO);
		Assert.assertEquals(Long.parseLong(InventoryControllerTest.PURCHASE_ORDER_NUMBER), inventoryWrapper.getPurchaseOrderNumber(), 1.0);
	}

	/**
	 * Tests the getPurchaseOrderNumber method of InventoryController's inner InventoryWrapper class.
	 */
	@Test
	public void inventoryWrapperGetPurchaseOrderDate() {

		InventoryController inventoryController = new InventoryController();
		PurchaseOrderVO purchaseOrderVO = new PurchaseOrderVO();

		String formattedDate = dateTimeFormatter.format(LocalDateTime.now());
		purchaseOrderVO.setOrderedDate(formattedDate);
		purchaseOrderVO.setPoNumber(InventoryControllerTest.PURCHASE_ORDER_NUMBER);

		InventoryController.InventoryWrapper inventoryWrapper =
				inventoryController.new InventoryWrapper(purchaseOrderVO);
		Assert.assertEquals(dateTimeFormatter.format(LocalDateTime.now()),
				dateTimeFormatter.format(inventoryWrapper.getPurchaseOrderDate()));
	}

	/**
	 * Tests getLatestPurchaseOrder when passed null for item code.
	 */
	@Test
	public void getLatestPurchaseOrderNullProduct() {

		InventoryController inventoryController = new InventoryController();
		try {
			inventoryController.getLatestPurchaseOrder(null, this.getServletRequest());
			Assert.fail("did not throw exception");
		} catch (IllegalArgumentException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Tests getLatestPurchaseOrder under right case.
	 */
	@Test
	public void getLatestPurchaseOrderGoodProduct() {

		InventoryController inventoryController = new InventoryController();
		inventoryController.setInventoryService(this.getInventoryService());
		inventoryController.setUserInfo(this.getUserInfo());

		InventoryController.InventoryWrapper wrapper =
				inventoryController.getLatestPurchaseOrder(1L, this.getServletRequest());
		Assert.assertEquals(0, wrapper.getPurchaseOrderNumber(), 1.0);
	}

	/**
	 * Doesn't do much extra other than provide some coverage when a message source exists.
	 */
	@Test
	public void getLatestPurchaseOrderNullProductWithMessageSource() {

		InventoryController inventoryController = new InventoryController();
		inventoryController.setMessageSource(this.getMessageSource());

		try {
			inventoryController.getLatestPurchaseOrder(null, this.getServletRequest());
			Assert.fail("did not throw exception");
		} catch (IllegalArgumentException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Tests getTotalStoreInventory when passed null for product ID.
	 */
	@Test
	public void getTotalStoreInventoryNullProduct() {

		InventoryController inventoryController = new InventoryController();
		try {
			inventoryController.getTotalStoreInventory(null, this.getServletRequest());
			Assert.fail("did not throw exception");
		} catch (IllegalArgumentException e) {
			// This is supposed to happen.
		}
	}

	/**
	 * Tests getTotalStoreInventory under right case.
	 */
	@Test
	public void getTotalStoreInventoryGoodProduct() {

		InventoryController inventoryController = new InventoryController();
		inventoryController.setInventoryService(this.getInventoryService());
		inventoryController.setUserInfo(this.getUserInfo());

		InventoryController.InventoryWrapper wrapper =
				inventoryController.getTotalStoreInventory(1L, this.getServletRequest());
		Assert.assertEquals(InventoryControllerTest.INVENTORY, wrapper.getInventory(), 1.0);
	}

	/**
	 * Doesn't do much extra other than provide some coverage when a message source exists.
	 */
	@Test
	public void getTotalStoreInventoryNullProductWithMessageSource() {

		InventoryController inventoryController = new InventoryController();
		inventoryController.setMessageSource(this.getMessageSource());

		try {
			inventoryController.getTotalStoreInventory(null, this.getServletRequest());
			Assert.fail("did not throw exception");
		} catch (IllegalArgumentException e) {
			// This is supposed to happen.
		}
	}

	/*
	 * Support functions.
	 */

	/**
	 * Returns a HttpServletRequest to use in the tests.
	 *
	 * @return A HttpServletRequest to use in the tests.
	 */
	private HttpServletRequest getServletRequest() {

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		Mockito.when(request.getLocale()).thenReturn(Locale.US);

		return request;
	}

	/**
	 * Returns an InventoryService to use in the tests.
	 *
	 * @return An InventoryService to use in the tests.
	 */
	private InventoryService getInventoryService() {

		InventoryService inventoryService = Mockito.mock(InventoryService.class);
		Mockito.when(inventoryService.getTotalStoreUnitsOnHand(Mockito.anyLong()))
				.thenReturn(InventoryControllerTest.INVENTORY);

		return inventoryService;
	}

	/**
	 * Returns a MessageSource to test with.
	 *
	 * @return A MessageSource to test with.
	 */
	private MessageSource getMessageSource() {

		MessageSource messageSource = Mockito.mock(MessageSource.class);
		Mockito.when(messageSource.getMessage(Mockito.eq("InventoryController.missingProductIds"), Mockito.anyObject(),
				Mockito.anyString(), Mockito.eq(Locale.US))).thenReturn("Must search for at least one Product ID.");

		return messageSource;

	}

	private UserInfo getUserInfo() {

		UserInfo userInfo = Mockito.mock(UserInfo.class);
		Mockito.when(userInfo.getUserId()).thenReturn("PGDV");

		return userInfo;
	}
}
