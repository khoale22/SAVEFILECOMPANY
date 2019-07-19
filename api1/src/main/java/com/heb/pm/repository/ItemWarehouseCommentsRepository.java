package com.heb.pm.repository;

import com.heb.pm.entity.ItemWarehouseComments;
import com.heb.pm.entity.ItemWarehouseCommentsKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author s753601
 * @version 2.8.0
 */
public interface ItemWarehouseCommentsRepository extends JpaRepository<ItemWarehouseComments, ItemWarehouseCommentsKey>{

	/**
	 * Returns all comments associated with a specific item in a specific warehouse.
	 * @param itemId id component to uniquely identify the item
	 * @param itemType type component to uniquely identify the item
	 * @param warehouseNumber number component to uniquely identify the warehouse
	 * @param warehouseType type component to uniquely identify the warehouse
	 * @return all comments associated with a specific item in a specific warehouse.
	 */
	List<ItemWarehouseComments> findAllByKeyItemIdAndKeyItemTypeAndKeyWarehouseNumberAndKeyWarehouseType(
			long itemId, String itemType, int warehouseNumber, String warehouseType);
}
