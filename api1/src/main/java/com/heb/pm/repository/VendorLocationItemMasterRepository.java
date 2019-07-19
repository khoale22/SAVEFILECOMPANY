/*
 * VendorLocationItemMasterRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.VendorLocationItemMaster;
import com.heb.pm.entity.VendorLocationItemMasterKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about vendors.
 *
 * @author vn73545
 * @since 2.16.0
 */
public interface VendorLocationItemMasterRepository extends JpaRepository<VendorLocationItemMaster, VendorLocationItemMasterKey> {

	/**
	 * Search VendorLocationItemMasters by item code.
	 *
	 * @param itemCode The item code.
	 * @param itemType The item type.
	 * @return A List of VendorLocationItemMasters.
	 */
	List<VendorLocationItemMaster> findByKeyItemCodeAndKeyItemType(Long itemCode, String itemType);
}
