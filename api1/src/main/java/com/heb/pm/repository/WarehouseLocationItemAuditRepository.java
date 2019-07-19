/*
 *  WarehouseLocationItemAuditRepository
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  @author a786878
 *  @since 2.15.0
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.WarehouseLocationItemAudit;
import com.heb.pm.entity.WarehouseLocationItemAuditKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Warehouse Location Item Audit Repository
 *
 */
public interface WarehouseLocationItemAuditRepository extends JpaRepository<WarehouseLocationItemAudit, WarehouseLocationItemAuditKey> {

	/**
	 * Find by item code and type.
	 *
	 * @param itemCode the item id request
	 * @param itemType the item type
	 * @return the list
	 */
	List<WarehouseLocationItemAudit> findByKeyItemCodeAndKeyItemType(long itemCode, String itemType);
}
