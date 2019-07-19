/*
 * WarehouseItemLocationRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.WarehouseLocationItem;
import com.heb.pm.entity.WarehouseLocationItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository for Warehouse Location Item Repository
 *
 * @author s753601
 * @since 2.8.0
 */
public interface WarehouseLocationItemRepository extends JpaRepository<WarehouseLocationItem, WarehouseLocationItemKey> {

	/**
	 * Find by nutrient code in list.
	 *
	 * @param itemCode the item id request
	 * @return the list
	 */
	List<WarehouseLocationItem> findByKeyItemCodeAndKeyItemType(long itemCode, String itemType);
}
