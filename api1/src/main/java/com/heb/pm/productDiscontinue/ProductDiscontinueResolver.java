/*
 * ProductDiscontinueResolver
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productDiscontinue;

import com.heb.pm.entity.AssociatedUpc;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ProductDiscontinue;
import com.heb.util.jpa.LazyObjectResolver;

/**
 * Resolves a ProductDiscontinue object returned by the ProductDiscontinue REST endpoint.
 *
 * @author d116773
 * @since 2.0.1
 */
public class ProductDiscontinueResolver implements LazyObjectResolver<ProductDiscontinue> {

	/**
	 * Resolves the ProductDiscontinue object. This will load its itemMaster and sellingUnit properties.
	 *
	 * @param d The object to resolve.
	 */
	@Override
	public void fetch(ProductDiscontinue d) {

		if (d == null) {
			return;
		}

		// Make sure sellingUnit is loaded.
		if (d.getSellingUnit() != null) {
			d.getSellingUnit().getUpc();
		}

		// Make sure itemMaster is loaded.
		if (d.getItemMaster() != null) {
			d.getItemMaster().getKey();
			d.getItemMaster().getItemNotDeleted().size();
			if (d.getItemMaster().getKey().isWarehouse()) {
				this.fetchWarehouseData(d.getItemMaster());
			}
		}



	}

	/**
	 * Traverses the UPC portion of the object model for warehouse products.
	 *
	 * @param im The item to traverse.
	 */
	private void fetchWarehouseData(ItemMaster im) {

		im.getWarehouseLocationItems().size();
		im.getWarehouseLocationItemExtendedAttributes().size();
		if (im.getPrimaryUpc() != null) {
			im.getPrimaryUpc().getAssociateUpcs().size();
			im.getPrimaryUpc().getShipper().size();
			im.getPrimaryUpc().getShipper().forEach((s) -> {
				if (s.getRealUpc() != null) {
					s.getRealUpc().getAssociateUpcs().size();
				}
			});
			im.getPrimaryUpc().getAssociateUpcs().forEach(AssociatedUpc::getSellingUnit);
		}
//		im.getWarehouseLocationItems().size();
	}
}
