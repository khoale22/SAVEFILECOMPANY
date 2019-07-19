/*
 * WarehouseItemLocationService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productDetails.casePack;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.repository.FlowTypeRepository;
import com.heb.pm.repository.ItemWarehouseCommentsRepository;
import com.heb.pm.repository.OrderQuantityTypeRepository;
import com.heb.pm.repository.WarehouseLocationItemRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service for warehouse item location controller.
 *
 * @author s753601
 * @since 2.8.0
 */
@Service
public class WarehouseLocationItemService {

	@Autowired
	private WarehouseLocationItemRepository warehouseLocationItemRepository;
	@Autowired
	private OrderQuantityTypeRepository orderQuantityTypeRepository;
	@Autowired
	private FlowTypeRepository flowTypeRepository;
	@Autowired
	private ItemWarehouseCommentsRepository itemWarehouseCommentsRepository;
	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;
	@Autowired
	private AuditService auditService;

	/**
	 * This service gets all of the warehouses based on the item code and type
	 * @param itemCode the item code
	 * @param itemType the item type
	 * @return  a list of all the warehouse location items for that particular item
	 */
	public List<WarehouseLocationItem> findAllWarehouseLocationItemsByItemCode(long itemCode, String itemType){
		return this.warehouseLocationItemRepository.findByKeyItemCodeAndKeyItemType(itemCode, itemType);
	}

	/**
	 * Returns all of the order quantity types from the code table
	 * @return the order quantity types
	 */
	public List<OrderQuantityType> findAllOrderQuantityTypes(){
		return this.orderQuantityTypeRepository.findAll();
	}

	/**
	 * Returns all of the flow types from the code table
	 * @return the flow types
	 */
	public List<FlowType> findAllFlowTypes(){
		return this.flowTypeRepository.findAll();
	}

	/**
	 * Returns all the remarks for the item at a location
	 * @param key the item and location information
	 * @return all the remarks for the item at al location
	 */
	public List<ItemWarehouseComments> findAllItemWarehouseCommentsByItemAndWarehouse(ItemWarehouseCommentsKey key){
		return this.itemWarehouseCommentsRepository.
				findAllByKeyItemIdAndKeyItemTypeAndKeyWarehouseNumberAndKeyWarehouseType(
						key.getItemId(), key.getItemType(), key.getWarehouseNumber(), key.getWarehouseType());
	}

	/**
	 * Updates list of warehouse location item.
	 *
	 * @param warehouseLocationItems The list of warehouse location item to be updated.
	 */
	public List<WarehouseLocationItem> updateWarehouseLocationItems(List<WarehouseLocationItem> warehouseLocationItems){
		this.productManagementServiceClient.updateWarehouseLocationItem(warehouseLocationItems);
		// Call back data after search with item code and item type referencing to the list of warehouse location item.
		// Note: The list of warehouse location item have been sent from font-ent have the same item code, item type.
		// So we can get only one element of the list to get item code and item type.
		List<WarehouseLocationItem> warehouseLocationItemsUpdate = this.warehouseLocationItemRepository
				.findByKeyItemCodeAndKeyItemType(warehouseLocationItems.get(0).getKey().getItemCode(), warehouseLocationItems.get(0).getKey().getItemType());
		return warehouseLocationItemsUpdate;
	}

	/**
	 * Updating for Remark and Comment of Warehouse.
	 * @param warehouseLocationItem Warehouse location item.
	 */
	public void saveRemarkAndCommentForWarehouse(WarehouseLocationItem warehouseLocationItem){
		this.productManagementServiceClient.saveRemarkAndCommentForWarehouse(warehouseLocationItem);
	}

	/**
	 * This service gets all of the warehouse audits based on the item code and type
	 * @param itemCode the item code
	 * @param itemType the item type
	 * @return  a list of all the warehouse location audit items for that particular item
	 */
	List<AuditRecord> getWarehouseAuditInformation(long itemCode, String itemType) {
		return this.auditService.getWarehouseAuditInformation(itemCode, itemType);
	}
}
