/*
 * VendorLocationItemRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.VendorLocationItem;
import com.heb.pm.entity.VendorLocationItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Vendor Location Item Repository
 *
 * @author l730832
 * @since 2.5.0
 */
public interface VendorLocationItemRepository extends JpaRepository<VendorLocationItem, VendorLocationItemKey> {

	/**
	 * Find by nutrient code in list.
	 *
	 * @param itemCode the item id request
	 * @return the list
	 */
	List<VendorLocationItem> findByKeyItemCodeAndKeyItemType(long itemCode, String itemType);
}
