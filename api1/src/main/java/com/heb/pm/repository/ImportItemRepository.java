/*
 * ImportItemRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ImportItem;
import com.heb.pm.entity.ImportItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about vendors.
 *
 * @author s573181
 * @since 2.5.0
 */
public interface ImportItemRepository extends JpaRepository<ImportItem, ImportItemKey> {

	/**
	 * Search ImportItems by item code.
	 *
	 * @param itemCode The item code.
	 * @param itemType The item type.
	 * @return A List of ImportItems.
	 */
	List<ImportItem> findByKeyItemCodeAndKeyItemType(Long itemCode, String itemType);
}
