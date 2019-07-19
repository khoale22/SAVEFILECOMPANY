/*
 *  ImportItemService
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ImportItem;
import com.heb.pm.entity.ImportItemAudit;
import com.heb.pm.entity.ImportItemKey;
import com.heb.pm.entity.VendorLocationItemMaster;
import com.heb.pm.repository.ImportItemAuditRepository;
import com.heb.pm.repository.ImportItemRepository;
import com.heb.pm.repository.VendorLocationItemMasterRepository;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to item/vendor information for import.
 *
 * @author s573181
 * @since 2.5.0
 */
@Service
public class ImportItemService {

	@Autowired
	private ImportItemRepository repository;

	@Autowired
	private VendorLocationItemMasterRepository vendorLocationItemMasterRepository;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * Search for import item by an item code.
	 * @param itemCode The item code.
	 * @param itemType The item type.
	 * @return a List of VendorLocationItemMaster by item codes.
	 */
	public List<VendorLocationItemMaster> findByKeyItemCodeAndKeyItemType(Long itemCode, String itemType){
		return this.vendorLocationItemMasterRepository.findByKeyItemCodeAndKeyItemType(itemCode, itemType);
	}

	/**
	 * Updates the import item.
	 * @param importItem The import item.
	 */
	public void update(ImportItem importItem){
		this.productManagementServiceClient.updateVendLocImprtItm(importItem);
	}
}
