/*
 *  VendorItemFactoryService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.entity.ImportItem;
import com.heb.pm.entity.VendorItemFactory;
import com.heb.pm.entity.VendorItemFactoryKey;
import com.heb.pm.repository.VendorItemFactoryRepository;
import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heb.pm.repository.ImportItemRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.apache.commons.collections.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to item/vendor information for import.
 *
 * @author s573181
 * @since 2.6.0
 */
@Service
public class VendorItemFactoryService {

	@Autowired
	private VendorItemFactoryRepository repository;

	@Autowired
	private UserInfo userInfo;
	
	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private ImportItemRepository importItemRepository;

	/**
	 * Finds all VendorItemFactories associated with a specific vendor and item.
	 *
	 * @param itemType The itemType.
	 * @param itemCode The itemCode.
	 * @param vendorType The vendorType.
	 * @param vendorNumber The vendorNumber.
	 * @return A list of VendorItemFactories.
	 */
	public List<VendorItemFactory> findAllFactories(
			String itemType, long itemCode, String vendorType, int vendorNumber){

		return this.repository.findAllByKeyItemTypeAndKeyItemCodeAndKeyVendorTypeAndKeyVendorNumber(
				itemType, itemCode, vendorType, vendorNumber);
	}

	/**
	 * Removes vendorItemFactories not in new list, and adds the factories that are new.
	 *
	 * @param importItem The import item.
	 * @return  updated importItem
	 */
	public ImportItem update(ImportItem importItem) {
		List<VendorItemFactory> oldList =
				this.findAllFactories(importItem.getKey().getItemType(),importItem.getKey().getItemCode(),
						importItem.getKey().getVendorType(), importItem.getKey().getVendorNumber());
		List<VendorItemFactory> newList = importItem.getVendorItemFactory();
		List<VendorItemFactory> removedFactories = new ArrayList<>();
		List<VendorItemFactory> addedFactories = new ArrayList<>();

		VendorItemFactory tempVendorItemFactory;
		VendorItemFactoryKey tempKey;
		Boolean isNotFound;
		//find factory ids in old list, but not new list;
		for (VendorItemFactory oldFactory : oldList) {
			isNotFound = true;
			for (VendorItemFactory newFactory : newList) {
				if (oldFactory.getKey().getFactoryId().equals(newFactory.getKey().getFactoryId())) {
					isNotFound = false;
				}
			}
			if (isNotFound) {
				removedFactories.add(oldFactory);
			}
		}
		//find factory ids in new list, but not old list;
		for (VendorItemFactory newFactory : newList) {
			isNotFound = true;
			for (VendorItemFactory oldFactory : oldList) {
				if (newFactory.getKey().getFactoryId().equals(oldFactory.getKey().getFactoryId())) {
					isNotFound = false;
				}
			}
			if (isNotFound) {
				tempVendorItemFactory = new VendorItemFactory();

				tempKey = new VendorItemFactoryKey();
				tempKey.setFactoryId(newFactory.getKey().getFactoryId());
				tempKey.setItemCode(newFactory.getKey().getItemCode());
				tempKey.setItemType(newFactory.getKey().getItemType());
				tempKey.setVendorNumber(newFactory.getKey().getVendorNumber());
				tempKey.setVendorType(newFactory.getKey().getVendorType());

				tempVendorItemFactory.setKey(tempKey);
				tempVendorItemFactory.setLastUpdatedUserID(userInfo.getUserId());
				tempVendorItemFactory.setLastUpdatedTimeStamp(LocalDate.now());

				addedFactories.add(tempVendorItemFactory);
			}
		}

		if(CollectionUtils.isNotEmpty(removedFactories) || CollectionUtils.isNotEmpty(addedFactories)){
			productManagementServiceClient.updateVendorItemFactory(removedFactories, addedFactories);
		}

		return importItemRepository.findOne(importItem.getKey());
	}


}
