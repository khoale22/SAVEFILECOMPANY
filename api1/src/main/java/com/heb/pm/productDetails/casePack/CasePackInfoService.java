/*
 *  CasePackInfoService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.repository.DiscontinueReasonRepository;
import com.heb.pm.repository.ItemMasterRepository;
import com.heb.pm.repository.ItemTypeRepository;
import com.heb.pm.repository.OneTouchTypeRepository;
import com.heb.pm.upcMaintenance.UpcSwap;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to case pack information.
 *
 * @author l730832
 * @since 2.7.0
 */
@Service
public class CasePackInfoService {

	@Autowired
	private ItemTypeRepository itemTypeRepository;

	@Autowired
	private OneTouchTypeRepository oneTouchTypeRepository;

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private AuditService auditService;

	@Autowired
	private DiscontinueReasonRepository discontinueReasonRepository;
	/**
	 * Get all item types.
	 *
	 * @return list with all item types.
	 */
	public List<ItemType> getAllItemTypes() {
		return this.itemTypeRepository.findAll();
	}

	/**
	 * Get all Discontinue Reasons.
	 *
	 * @return list with all Discontinue Reasons.
	 */
	public List<DiscontinueReason> getAllDiscontinueReasons() {
		return this.discontinueReasonRepository.findAll();
	}

	/**
	 * Gets all one touch types from the one touch type code table.
	 *
	 * @return The one touch type associated to that code.
	 */
	public List<OneTouchType> getAllOneTouchTypes() {
		return this.oneTouchTypeRepository.findAll();
	}

	/**
	 * Updates case pack information.
	 *
	 * @param itemMaster The item master that is sent to the web service to be updated.
	 * @return the updated item master.
	 */
	public ItemMaster updateCasePackInfo(ItemMaster itemMaster) {
		this.productManagementServiceClient.updateCasePackInfo(itemMaster);
		return this.itemMasterRepository.findOne(itemMaster.getKey());
	}

	/**
	 * This returns a list of Case pack info audits based on the key.
	 * @param key way to uniquely ID the set of case pack info audits requested
	 * @return a list of all the changes made to an item master's case pack attributes.
	 */
	List<AuditRecord> getCasePackAuditInformation(ItemMasterKey key, String filter) {
		return this.auditService.getCasePackAuditInformation(key, filter);
	}

	/**
	 * Change item primary upc.
	 * @param upcSwap The upc swap that contain all information current item primary upc and new item primary upc.
	 */
	public void changeItemPrimaryUPC(UpcSwap upcSwap) throws CheckedSoapException {
		this.productManagementServiceClient.changeItemPrimaryUPC(upcSwap);
	}
}
