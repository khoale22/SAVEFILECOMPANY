/*
 *  MrtInfoService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditRecordWithUpc;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.repository.MrtRepository;
import com.heb.pm.repository.ProdItemRepository;
import com.heb.pm.repository.SellingUnitRepository;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds business logic related to MrtInfo. *
 *
 * @author m594201
 * @since 2.6.0
 */
@Service
class MrtInfoService {

	@Autowired
	private MrtRepository mrtRepository;

	@Autowired
	private SellingUnitRepository sellingUnitRepository;

	@Autowired
	private ProdItemRepository prodItemRepository;

	@Autowired
	private AuditService auditService;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * This returns a list of shipper based on the ordering UPC.
	 * @param upc
	 * @return
	 */
	List<Shipper> getMrtInformation(Long upc) {
		return this.mrtRepository.findByKeyUpc(upc);
	}

	/**
	 * This returns a list of product item based on the item code and item type.
	 * @param itemCode The item code that the audit is being searched on.
	 * @param itemType The item type that the audit is being searched on.
	 * @return
	 */
	List<ProdItem> getMrtItemInformation(Long itemCode, String itemType) {
		return this.prodItemRepository.findByKeyItemCodeAndKeyItemType(itemCode, itemType);
	}

	/**
	 * This returns a selling unit based on the UPC number.
	 * @param upc
	 * @return
	 */
	SellingUnit getMrtElementInfo(Long upc) {
		return this.sellingUnitRepository.findOne(upc);
	}

	/**
	 * Update mrt information by item master.
	 *
	 * @param itemMaster the item master
	 * @return the item master
	 */
	void updateMrtInfo(ItemMaster itemMaster){
		//call ws to update MRT information.
		this.productManagementServiceClient.updateMrtInfo(itemMaster);
	}

	/**
	 * This returns a list of shipper audits based on the upc.
	 * @param upc
	 * @return
	 */
	List<AuditRecordWithUpc> getMrtAuditInformation(Long upc) {
		return this.auditService.getMrtAuditInformation(upc);
	}
}