/*
 * InventoryController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.inventory;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.tim.services.vo.PurchaseOrderVO;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** REST controller that returns inventory information.
 *
 * @author d116773
 * @since 2.0.1
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.INVENTORY_LOOKUP_STORE)
public class InventoryController implements MessageSourceAware {

	private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

	// The URLs this controller supports
	private static final String STORE_INVENTORY_URL = "inventory/store";
	private static final String STORE_LATEST_PURCHASE_ORDER = "inventory/purchaseOrders";
	private static final String VENDOR_INVENTORY_URL = "inventory/vendor";

	// Constants related to messages on missing product IDs.
	private static final String PRODUCT_IDS_MESSAGE_KEY = "InventoryController.missingProductIds";
	private static final String DEFAULT_N0_PRODUCT_ID_MESSAGE = "Must search for at least one product ID.";

	// Logging messages.
	private static final String STORE_INVENTORY_MESSAGE =
			"User %s from IP %s has requested inventory information on product %d";

	// Stores user facing messages.
	private MessageSource messageSource;

	// Looks up inventory data.
	@Autowired
	private InventoryService inventoryService;

	// Provides information about the logged in user.
	@Autowired
	private UserInfo userInfo;

	/**
	 * Since inventory is just an float, this class wraps it in an object so the REST client can access it
	 * as part of an object instead of as a raw number.
	 */
	public class InventoryWrapper {

		private long productId;
		private float inventory;
		private LocalDateTime purchaseOrderDate = null;
		private long purchaseOrderNumber;
		private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");


		/**
		 * Creates a new InventoryWrapper.
		 *
		 * @param inventory The inventory number to return.
		 */
		public InventoryWrapper(long productId, float inventory) {
			this.productId = productId;
			this.inventory = inventory;
		}

		/**
		 * Creates a new Inventory Wrapper for purchase order
		 */
		public InventoryWrapper(PurchaseOrderVO purchaseOrderVO){
			if(purchaseOrderVO != null){
				this.purchaseOrderDate = LocalDateTime.parse(purchaseOrderVO.getOrderedDate(), dateTimeFormatter);
				this.purchaseOrderNumber = Long.valueOf(purchaseOrderVO.getPoNumber());
			}
			else {
				this.purchaseOrderDate = null;
			}
		}

		/**
		 * Returns the inventory number.
		 *
		 * @return The inventory number.
		 */
		public float getInventory() {
			return this.inventory;
		}

		/**
		 * Returns the date of the purchase order.
		 *
		 * @return The date of the purchase order.
		 */
		public LocalDateTime getPurchaseOrderDate() {
			return this.purchaseOrderDate;
		}

		/**
		 * Returns the number of the purchase order.
		 *
		 * @return The number of the purchase order.
		 */
		public long getPurchaseOrderNumber(){
			return this.purchaseOrderNumber;
		}

		/**
		 * Returns the product ID this inventory is for.
		 *
		 * @return The product ID this inventory is for.
		 */
		public long getProductId() {
			return this.productId;
		}
	}


	/**
	 * Returns total store inventory for a product.
	 *
	 * @param productId The product ID to search for information about.
	 * @param request The HTTP request that initiated the call.
	 * @return The total inventory across all stores for the product.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = InventoryController.STORE_INVENTORY_URL)
	public InventoryWrapper getTotalStoreInventory(@RequestParam("productId") Long productId, HttpServletRequest request) {

		if (productId == null) {
			String message = this.messageSource == null ? InventoryController.DEFAULT_N0_PRODUCT_ID_MESSAGE :
					this.messageSource.getMessage(InventoryController.PRODUCT_IDS_MESSAGE_KEY, null,
							InventoryController.DEFAULT_N0_PRODUCT_ID_MESSAGE, request.getLocale());
			throw new IllegalArgumentException(message);
		}

		this.logGetTotalStoreInventory(request.getRemoteAddr(), productId);

		return new InventoryWrapper(productId, this.inventoryService.getTotalStoreUnitsOnHand(productId));
	}

	/**
	 * Returns latest Purchase order for an item.
	 *
	 * @param itemCode The item id to search for information about.
	 * @param request The HTTP request that initiated the call.
	 * @return The latest purchase order.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = InventoryController.STORE_LATEST_PURCHASE_ORDER)
	public InventoryWrapper getLatestPurchaseOrder(@RequestParam("itemCode") Long itemCode, HttpServletRequest request) {

		if (itemCode == null) {
			String message = this.messageSource == null ? InventoryController.DEFAULT_N0_PRODUCT_ID_MESSAGE :
					this.messageSource.getMessage(InventoryController.PRODUCT_IDS_MESSAGE_KEY, null,
							InventoryController.DEFAULT_N0_PRODUCT_ID_MESSAGE, request.getLocale());
			throw new IllegalArgumentException(message);
		}

		this.logGetPurchaseOrders(request.getRemoteAddr(), itemCode);
		return new InventoryWrapper(this.inventoryService.getLatestPurchaseOrder(itemCode));
	}


	/**
	 * Returns total vendor units on hand for a product.
	 *
	 * @param productId The product ID to search for information about.
	 * @param request The HTTP request that initiated the call.
	 * @return The total inventory across all vendors for the product.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = InventoryController.VENDOR_INVENTORY_URL)
	public InventoryWrapper getVendorOnHandQuantity(@RequestParam("productId") Integer productId,
												   HttpServletRequest request) {

		if (productId == null) {
			String message = this.messageSource == null ? InventoryController.DEFAULT_N0_PRODUCT_ID_MESSAGE :
					this.messageSource.getMessage(InventoryController.PRODUCT_IDS_MESSAGE_KEY, null,
							InventoryController.DEFAULT_N0_PRODUCT_ID_MESSAGE, request.getLocale());
			throw new IllegalArgumentException(message);
		}

		this.logGetVendorOnHandQuantity(request.getRemoteAddr(), productId);

		return new InventoryWrapper(productId, this.inventoryService.getVendorOnHandQuantity(productId));
	}


	/**
	 * Sets the inventory service to use.
	 *
	 * @param inventoryService The inventory service to use.
	 */
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	/**
	 * Sets the UserInfo object to use.
	 *
	 * @param userInfo The UserInfo object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the message source to use.
	 *
	 * @param messageSource The message source to use.
	 */
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Logs a user's request for inventory.
	 *
	 * @param ip The IP the user request came from.
	 * @param productId The product ID the user is searching for inventory data about.
	 */
	private void logGetTotalStoreInventory(String ip, long productId) {
		InventoryController.logger.info(String.format(InventoryController.STORE_INVENTORY_MESSAGE,
				this.userInfo.getUserId(), ip, productId));
	}

	/**
	 * Logs a user's request for purchase orders
	 * @param ip The IP the user request came from
	 * @param itemCode The item code the user is searching for product information about.
	 */
	private void logGetPurchaseOrders(String ip, long itemCode) {
		InventoryController.logger.info(String.format(InventoryController.STORE_INVENTORY_MESSAGE,
				this.userInfo.getUserId(), ip, itemCode));
	}

	/**
	 * Logs a user's request for vendor inventory.
	 *
	 * @param ip The IP the user request came from.
	 * @param productId The product ID the user is searching for inventory data about.
	 */
	private void logGetVendorOnHandQuantity(String ip, long productId) {
		InventoryController.logger.info(String.format(InventoryController.STORE_INVENTORY_MESSAGE,
				this.userInfo.getUserId(), ip, productId));
	}

}
