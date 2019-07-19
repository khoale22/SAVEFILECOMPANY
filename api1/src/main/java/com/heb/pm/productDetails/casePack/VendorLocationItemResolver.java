/*
 *
 * VendorLocationItemResolver
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.ItemWarehouseVendor;
import com.heb.pm.entity.VendorLocationItem;
import com.heb.util.jpa.LazyObjectResolver;


/**
 * Resolver for the vendor location item.
 *
 * @author l730832
 * @since 2.5.0
 */
public class VendorLocationItemResolver implements LazyObjectResolver<VendorLocationItem> {

	/**
	 * Resolves the VendorLocationItem object.
	 *
	 * @param d The vendorLocationItem object to resolve.
	 */
	@Override
	public void fetch(VendorLocationItem d) {
		d.getItemWarehouseVendorList().size();

		for (ItemWarehouseVendor itemWarehouseVendor : d.getItemWarehouseVendorList()) {
			if(itemWarehouseVendor.getWarehouseLocationItem() != null)
			itemWarehouseVendor.getWarehouseLocationItem().getKey();
		}

		if(d.getLocation() != null) {
			d.getLocation().getKey();
			if(d.getLocation().getApLocation() != null) {
				d.getLocation().getApLocation().getKey();
			}
		}
		if(d.getSca() != null) {
			d.getSca().getScaCode();
		}

		if(d.getCostOwner() != null) {
			d.getCostOwner().getCostOwnerId();
		}
		if(d.getCostOwner().getTopToTop() != null) {
			d.getCostOwner().getTopToTop().getTopToTopId();
		}
		if(d.getItemMaster()!= null) {
			if (d.getItemMaster().getPrimaryUpc() != null) {
				d.getItemMaster().getPrimaryUpc().getUpc();
			}
		}
		if(d.getCountry() != null) {
			d.getCountry().getCountryId();
		}
		if(d.getVendorLocationItemMaster() != null) {
			d.getVendorLocationItemMaster().getKey();
		}
		if(d.getItemMaster().getKey().isDsd()) {
			d.getDsdLocation().getKey();
		} else {
			d.getVendorLocation().getKey();
		}
	}
}
