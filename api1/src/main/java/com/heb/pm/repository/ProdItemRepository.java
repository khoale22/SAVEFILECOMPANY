/*
 * ProdItemRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProdItem;
import com.heb.pm.entity.ProdItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository to retrieve information about an item.
 *
 * @author vn70516
 * @since 2.21.0
 */
public interface ProdItemRepository extends JpaRepository<ProdItem, ProdItemKey>{

	/**
	 * Returns a list of ProdItem object searching by item code and item type. This can return multiples for items
	 * that are both warehouse and DSD.
	 *
	 * @param itemCode The item code to look for.
	 * @param itemType The item type to look for.
	 * @return A list of product item that have the looked for item code and item type.
	 */
	List<ProdItem> findByKeyItemCodeAndKeyItemType(long itemCode, String itemType);
}
