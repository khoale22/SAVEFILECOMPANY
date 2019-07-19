/*
 *  ImportItemController
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.ImportItem;
import com.heb.pm.entity.ImportItemAudit;
import com.heb.pm.entity.ImportItemKey;
import com.heb.pm.entity.VendorItemFactory;
import com.heb.pm.entity.VendorLocationItemMaster;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents item/vendor information for import.
 *
 * @author s573181
 * @since 2.5.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ImportItemController.IMPORT_ITEM_URL)
@AuthorizedResource(ResourceConstants.CASE_PACK_IMPORT)
public class ImportItemController {

	private static final Logger logger = LoggerFactory.getLogger(ImportItemController.class);

	protected static final String IMPORT_ITEM_URL = "/importItem";
	protected static final String IMPORT_ITEM_BY_ITEM_CODE_AND_ITEM_TYPE_URL = "/queryByItemCodeAndItemType";
	protected static final String CONTAINER_SIZES = "/containerSizes";
	protected static final String INCO_TERMS = "/incoTerms";
	protected static final String UPDATE = "/update";
	protected static final String IMPORT_ITEM_AUDIT = "/importItemAudit";

	// Keys to user facing messages in the message resource bundle.
	private static final String DEFAULT_NO_PRODUCT_ID_MESSAGE = "Item code cannot be null.";
	private static final String DEFAULT_PROD_ID_MESSAGE_KEY = "ImportItemController.missingItemCode";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="ImportItemController.updateSuccessful";

	// Log Messages
	private static final String GET_PROD_INFO_BY_PROD_ID_MESSAGE =
			"User %s from IP %s has requested case pack import information for the following item code [%s]";
	private static final String GET_CONTAINER_SIZES_MESSAGE =
			"User %s from IP %s has requested case pack import container information.";
	private static final String GET_INCO_TERMS_MESSAGE =
			"User %s from IP %s has requested case pack Inco Terms information.";
	private static final String UPDATE_IMPORT_ITEM_MESSAGE =
			"User %s from IP %s has requested to update the import item: '%s'.";
	private static final String GET_IMPORT_ITEM_AUDIT_MESSAGE =
			"User %s from IP %s has requested case pack import audit information for the following item code [%s] and vendorId [%s]";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Import Item: %s updated successfully.";


	@Autowired
	private ImportItemService importService;

	@Autowired
	private AuditService auditService;

	@Autowired
	private UserInfo userInfo;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	private VendorLocationItemMasterResolver vendorLocationItemMasterResolver = new VendorLocationItemMasterResolver();
	
	private class VendorLocationItemMasterResolver implements LazyObjectResolver<List<VendorLocationItemMaster>> {
		
		@Override
		public void fetch(List<VendorLocationItemMaster> vendorLocationItemMasters) {
			if(vendorLocationItemMasters != null) {
				for (VendorLocationItemMaster vendorLocationItemMaster : vendorLocationItemMasters) {
					vendorLocationItemMaster.getLocation().getKey();
					ImportItem importItem = vendorLocationItemMaster.getImportItem();
					if(importItem != null) {
						importItem.getLocation().getKey();
						importItem.getVendorItemFactory();
						if(importItem.getVendorItemFactory() != null){
							importItem.getVendorItemFactory().size();
							for(VendorItemFactory vendorItemFactory : importItem.getVendorItemFactory()){
								if(vendorItemFactory.getFactory() != null){
									vendorItemFactory.getFactory().getFactoryId();
								}
							}
						}
					}
				}
			}
		}
	}

	private List<ContainerSize> containerSizes = new ArrayList<>();

	private List<IncoTerms> incoTermsList = new ArrayList<>();

	/**
	 * Default constructor that initiates the enumeration lists.
	 */
	public ImportItemController(){
		// Load container size list.
		containerSizes.add(ContainerSize.CFS);
		containerSizes.add(ContainerSize.FCL20);
		containerSizes.add(ContainerSize.FCL40);
		containerSizes.add(ContainerSize.FCL45);
		containerSizes.add(ContainerSize.FCL40HQ);

		// Load inco terms list.
		incoTermsList.add(IncoTerms.FOB);
	}
	
	/**
	 * Enumeration class to contain ContainerSize.
	 */
	private enum ContainerSize {
		CFS("CFS    "),
		FCL20("FCL20  "),
		FCL40("FCL40  "),
		FCL45("FCL45  "),
		FCL40HQ("FCL40HQ");

		private String containerSize;

		ContainerSize(String containerSize) { this.containerSize = containerSize; }

		public String getContainerSize(){
			return this.containerSize;
		}
	};

	/**
	 * Enumeration class to contain incoterms.
	 */
	private enum IncoTerms {
		FOB("FOB");

		private String incoTerms;

		IncoTerms(String incoTerms) { this.incoTerms = incoTerms; }

		public String getIncoTerms(){
			return this.incoTerms;
		}
	};

	/**
	 * Returns a list of ImportItems by item code.
	 *
	 * @param itemCode The item code.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of ImportItems by item code.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImportItemController.IMPORT_ITEM_BY_ITEM_CODE_AND_ITEM_TYPE_URL)
	public List<VendorLocationItemMaster> findByKeyItemCode(@RequestParam(value = "itemCode") Long itemCode,
															@RequestParam(value = "itemType") String itemType,
															HttpServletRequest request){
		this.parameterValidator.validate(itemCode, ImportItemController.DEFAULT_NO_PRODUCT_ID_MESSAGE,
				ImportItemController.DEFAULT_PROD_ID_MESSAGE_KEY, request.getLocale());
		
		this.logFindByItemCode(request.getRemoteAddr(), itemCode);
		List<VendorLocationItemMaster> results = this.importService.findByKeyItemCodeAndKeyItemType(itemCode, itemType);
		this.vendorLocationItemMasterResolver.fetch(results);
		return results;
	}

	/**
	 * Returns a list of the enumeration with all container sizes.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return The container sizes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImportItemController.CONTAINER_SIZES)
	public List<ContainerSize> getContainerSizes(HttpServletRequest request) {
		this.logGetContainerSizes(request.getRemoteAddr());
		return containerSizes;
	}

	/**
	 * Returns a list of the enumerations with all inco terms.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return The incoTerms.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ImportItemController.INCO_TERMS)
	public List<IncoTerms> getIncoTerms(HttpServletRequest request) {
		this.logGetIncoTerms(request.getRemoteAddr());
		return incoTermsList;
	}

	/**
	 * Updates a new Import Item.
	 *
	 * @param importItem The import item to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The new ImportItem and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST,  value = ImportItemController.UPDATE)
	public ModifiedEntity<ImportItem> update(
			@RequestBody ImportItem importItem, HttpServletRequest request){
		ImportItemController.logger.info(String.format(
				ImportItemController.UPDATE_IMPORT_ITEM_MESSAGE,  this.userInfo.getUserId(),
				request.getRemoteAddr(), null));
		this.importService.update(importItem);
		String updateMessage = this.messageSource.getMessage(ImportItemController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{importItem.getKey().getItemCode(), importItem.getKey().getVendorNumber()},
				ImportItemController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());

		return new ModifiedEntity<>(null, updateMessage);
	}

	/**
	 * Returns a list of the audit records in regard to Import Items.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return the audit records
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ImportItemController.IMPORT_ITEM_AUDIT)
	public List<AuditRecord> getAuditRecords(@RequestBody ImportItemKey key, HttpServletRequest request) {
		this.logGetImportAudits(request.getRemoteAddr(), key.getItemCode(), key.getVendorNumber());
		List<AuditRecord> test = this.auditService.getImportItemAuditInformation(key);
		return test;
	}

	/**
	 * Logs a user's request to get all import items for an item code.
	 * @param ip the IP address the user is logged in from
	 * @param itemCode the item code the user is requesting an audit for
	 * @param vendorId the vendor Id the user is requesting an audit for.
	 */
	private void logGetImportAudits(String ip, long itemCode, long vendorId){
		ImportItemController.logger.info(String.format(ImportItemController.GET_IMPORT_ITEM_AUDIT_MESSAGE,
				this.userInfo.getUserId(), ip, itemCode, vendorId));
	}

	/**
	 * Logs a user's request to get all ImportItems for an item code.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param itemCode The item code the user is searching for.
	 */
	private void logFindByItemCode(String ip, Long itemCode) {
		ImportItemController.logger.info(String.format(ImportItemController.GET_PROD_INFO_BY_PROD_ID_MESSAGE,
				this.userInfo.getUserId(), ip, itemCode));
	}

	/**
	 * Logs a user's request to get all ImportItems for an item code.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetContainerSizes(String ip) {
		ImportItemController.logger.info(String.format(ImportItemController.GET_CONTAINER_SIZES_MESSAGE,
				this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs a user's request to get all ImportItems for an item code.
	 *
	 * @param ip The IP address th user is logged in from.
	 */
	private void logGetIncoTerms(String ip) {
		ImportItemController.logger.info(String.format(ImportItemController.GET_INCO_TERMS_MESSAGE,
				this.userInfo.getUserId(), ip));
	}
	/**
	 * Logs a user's request to update an import item.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param importItem The import item to be updated.
	 */
	private void logUpdate(String ip, ImportItem importItem){
		ImportItemController.logger.info(String.format(
				ImportItemController.UPDATE_IMPORT_ITEM_MESSAGE,  this.userInfo.getUserId(),
				ip, importItem.getDisplayName()));
	}

}
