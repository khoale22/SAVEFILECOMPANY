/*
 * VendorItemFactoryRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.VendorItemFactory;
import com.heb.pm.entity.VendorItemFactoryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about vendor item factories.
 *
 * @author s573181
 * @since 2.6.0
 */
public interface VendorItemFactoryRepository extends JpaRepository<VendorItemFactory, VendorItemFactoryKey>{

	public List<VendorItemFactory> findAllByKeyItemTypeAndKeyItemCodeAndKeyVendorTypeAndKeyVendorNumber(
			String itemType, long itemCode, String vendorType, int vendorNumber);
}
