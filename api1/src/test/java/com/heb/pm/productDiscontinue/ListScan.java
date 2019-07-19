/*
 * ListScan
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.ProductDiscontinue;

/**
 * Used to scan a list of ProductDiscontinue objects for a particular UPC, item code, or product ID.
 *
 * @author d116773
 * @since 2.0.1
 */
final class ListScan {

	/**
	 * Private so the class cannot be instantiated.
	 *
	 */

	private ListScan() {}
	/**
	 * Check to see if an item code is in a list.
	 *
	 * @param itemCode The item code to look for.
	 * @param itemType The type of item to look for.
	 * @param list The list to look through.
	 * @return True if the item code is in the list and false otherwise.
	 */
	public static boolean listIncludesItem(long itemCode, String itemType, Iterable<ProductDiscontinue> list) {

		for (ProductDiscontinue p : list) {
			if (p.getKey().getItemCode() == itemCode && p.getKey().getItemType().equals(itemType)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check to see if an item code is missing from a list.
	 *
	 * @param itemCode The item code to look for.
	 * @param itemType The type of item to look for.
	 * @param list The list to look through.
	 * @return True if the item code is not in the list and false otherwise.
	 */
	public static boolean listDoesNotIncludeItem(long itemCode, String itemType, Iterable<ProductDiscontinue> list) {

		for (ProductDiscontinue p : list) {
			if (p.getKey().getItemCode() == itemCode && p.getKey().getItemType().equals(itemType)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check to see if a UPC is in alist.
	 *
	 * @param upc The upc to look for.
	 * @param list The list to look through.
	 * @return True if the item code is in the list and false otherwise.
	 */
	public static boolean listIncludesUpc(long upc, Iterable<ProductDiscontinue> list) {

		for (ProductDiscontinue p : list) {
			if (p.getKey().getUpc() == upc) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check to see if a UPC is missing from a list.
	 *
	 * @param upc The item code to look for.
	 * @param list The list to look through.
	 * @return True if the item code is not in the list and false otherwise.
	 */
	public static boolean listDoesNotIncludeUpc(long upc, Iterable<ProductDiscontinue> list) {

		for (ProductDiscontinue p : list) {
			if (p.getKey().getUpc() == upc) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check to see if a product ID is in a list.
	 *
	 * @param productId The product ID to look for.
	 * @param list The list to look through.
	 * @return True if the item code is in the list and false otherwise.
	 */
	public static boolean listIncludesProductId(long productId, Iterable<ProductDiscontinue> list) {

		for (ProductDiscontinue p : list) {
			if (p.getKey().getProductId() == productId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check to see if a product ID is missing from a list.
	 *
	 * @param prodcutId The product ID to look for.
	 * @param list The list to look through.
	 * @return True if the item code is not in the list and false otherwise.
	 */
	public static boolean listDoesNotIncludeProductId(long prodcutId, Iterable<ProductDiscontinue> list) {

		for (ProductDiscontinue p : list) {
			if (p.getKey().getProductId() == prodcutId) {
				return false;
			}
		}
		return true;
	}
}
