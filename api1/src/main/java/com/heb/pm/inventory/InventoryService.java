/*
 * InventoryService
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Supports pulling inventory information from TIM.
 *
 * @author d116773
 * @since 2.0.1
 */
@Component
@SuppressWarnings("rawtypes")
public class InventoryService {

	private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

	// Error messages.
	private static final String EMPTY_RESULTS_ERROR_MESSAGE = "TIM returned empty results for product ID %d.";
	private static final String INVALID_DSV_FORMAT_FROM_TIM = "DSV inventory received from TIM is not a number.";

	// Logging messages.
	private static final String STORE_UNITS_LOOKUP_MESSAGE = "Looking up inventory for product %d.";

	// Date format.
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	// Initial inventory
	private static final float INITIAL_INVENTORY = 0.0f;
	private static final Float NO_VENDOR_INVENTORY = 0.0f;

	private static final String VENDOR_QUANTITY_KEY = "QTY";

	@Autowired
	private TimRepository timRepository;

	/**
	 * Returns total number of units-on-hand across all stores for a particular product ID.
	 *
	 * @param productId The product ID to search for inventory on.
	 * @return The total number of units-on-hand across all stores for a particular product ID.
	 */
	public float getTotalStoreUnitsOnHand(long productId) {

		InventoryService.logger.debug(String.format(InventoryService.STORE_UNITS_LOOKUP_MESSAGE, productId));

		// Get a list of units on hand
		List unitsOnHandList = this.timRepository.getStoreInventory(productId);

		if (unitsOnHandList == null) {
			throw new TimException(String.format(InventoryService.EMPTY_RESULTS_ERROR_MESSAGE, productId));
		}

		// Loop through all the values returned and add them up.
		float totalInventory = INITIAL_INVENTORY;
		for (Object o : unitsOnHandList) {
			StoreInventoryVO vo = (StoreInventoryVO) o;
			if (vo.getUnitOnHand() != null) {
				totalInventory += Float.valueOf(vo.getUnitOnHand());
			}
		}

		return totalInventory;
	}

	/**
	 * Returns the latest purchase order by order date.
	 *
	 * @param itemCode The item id to search for information about.
	 * @return The latest purchase order by order date or null if no purchase order is found.
	 */
	public PurchaseOrderVO getLatestPurchaseOrder(long itemCode) {

		InventoryService.logger.debug(String.format(InventoryService.STORE_UNITS_LOOKUP_MESSAGE, itemCode));

		List purchaseOrdersOnOrderList = this.timRepository.getPurchaseOrders(itemCode);


		if (purchaseOrdersOnOrderList == null) {
			throw new TimException(String.format(InventoryService.EMPTY_RESULTS_ERROR_MESSAGE, itemCode));
		}
		PurchaseOrderVO returnPurchaseOrderVO = null;
		LocalDateTime mostRecentDateTime = null;
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

		for (Object o : purchaseOrdersOnOrderList) {
			PurchaseOrderVO purchaseOrderVO = (PurchaseOrderVO) o;
			if (mostRecentDateTime == null || LocalDateTime.parse(purchaseOrderVO.getOrderedDate(), dateTimeFormatter)
					.isAfter(mostRecentDateTime)) {
				mostRecentDateTime = LocalDateTime.parse(purchaseOrderVO.getOrderedDate(), dateTimeFormatter);
				returnPurchaseOrderVO = purchaseOrderVO;
			}
		}
		if (returnPurchaseOrderVO != null) {
			return returnPurchaseOrderVO;
		}
		return null;
	}

	/**
	 * Sets the repository used to connect to TIM.
	 *
	 * @param timRepository The repository used to connect to TIM.
	 */
	public void setTimRepository(TimRepository timRepository) {
		this.timRepository = timRepository;
	}

	/**
	 * Returns total vendor units on hand for a product.
	 *
	 * @param productId The product ID to search for information about.
	 * @return The total inventory across all vendors for the product.
	 */
	public Float getVendorOnHandQuantity(int productId){

		InventoryService.logger.debug(String.format(InventoryService.STORE_UNITS_LOOKUP_MESSAGE, productId));

		Map storeData = this.timRepository.getVendorOnHandQuantity(productId);
		if (storeData == null) {
			throw new TimException(String.format(InventoryService.EMPTY_RESULTS_ERROR_MESSAGE, productId));
		}
		try {
			Object object = storeData.get(InventoryService.VENDOR_QUANTITY_KEY);
			if(object != null){
				return Float.valueOf(object.toString());
			} else {
				return InventoryService.NO_VENDOR_INVENTORY;
			}
		} catch (NumberFormatException e){
			throw new NumberFormatException(InventoryService.INVALID_DSV_FORMAT_FROM_TIM);
		}
	}
}
