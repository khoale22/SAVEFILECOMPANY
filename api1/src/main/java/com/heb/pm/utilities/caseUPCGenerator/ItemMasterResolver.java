/*
 *  CaseUPCGeneratorService
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.utilities.caseUPCGenerator;

import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.Shipper;
import com.heb.util.jpa.LazyObjectResolver;

import java.util.List;

public class ItemMasterResolver implements LazyObjectResolver<List<ItemMaster>> {
	/**
	 * Method to resolve list of shipper for item master.
	 * @param itemMasters list of item master.
	 */
	@Override
	public void fetch(List<ItemMaster> itemMasters) {
		for (ItemMaster itemMaster : itemMasters) {
			if (itemMaster.getPrimaryUpc() != null && itemMaster.getPrimaryUpc().getProductShipper() != null) {
				for (Shipper shipper : itemMaster.getPrimaryUpc().getProductShipper()) {
					shipper.getKey();
				}
			}
		}
	}
}
