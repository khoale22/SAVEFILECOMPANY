/*
 *  VendorItemFactoryController
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
import com.heb.pm.entity.ImportItem;
import com.heb.pm.entity.VendorItemFactory;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.MessageSource;
import com.heb.util.controller.ModifiedEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents VendorItemFactory information.
 *
 * @author s573181
 * @since 2.5.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + VendorItemFactoryController.VENDOR_ITEM_FACTORY_URL)
@AuthorizedResource(ResourceConstants.CASE_PACK_VENDOR_ITEM_FACTORY_INFO)
public class VendorItemFactoryController {

	private static final Logger logger = LoggerFactory.getLogger(VendorItemFactoryController.class);

	protected static final String VENDOR_ITEM_FACTORY_URL = "/vendorItemFactory";
	protected static final String FIND_BY_VENDOR_ITEM_URL = "/findByVendorItem";
	protected static final String UPDATE_VENDOR_ITEM_URL = "/update";

	// Log Messages
	private static final String GET_ALL_FACTORIES_BY_VENDOR_ITEM = "User %s from IP %s has requested vendor item " +
			"factory information for itemType: %s, itemCode: %s, vendorType: %s, vendorNumber: %s.";
	private static final String UPDATE_FACTORIES_BY_VENDOR_ITEM = "User %s from IP %s has requested to update " +
			"vendor item " + "factory information for itemType: %s, itemCode: %s, vendorType: %s, vendorNumber: %s.";

    private static final String UPDATE_SUCCESS_MESSAGE_KEY ="VendorItemFactoryController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Import Factories Item: %s updated successfully.";			

	@Autowired
	private VendorItemFactoryService service;

	@Autowired
	private UserInfo userInfo;
	
	@Autowired
	private MessageSource messageSource;

	private VendorItemFactoryResolver resolver = new VendorItemFactoryResolver();

	/**
	 * Resolves the lazy loading of VendorItemFactory entity.
	 */
	private class VendorItemFactoryResolver implements LazyObjectResolver<VendorItemFactory> {
		@Override
		public void fetch(VendorItemFactory vendorItemFactory){
			if(vendorItemFactory.getFactory() != null){
				vendorItemFactory.getFactory().getFactoryId();
			}
		}
	}
	
	private ImportItemResolver importItemResolver = new ImportItemResolver();
	
    /**
	 * Resolves the lazy loading of ImportItme entity.
	 */
	private class ImportItemResolver implements LazyObjectResolver<ImportItem> {

		@Override
		public void fetch(ImportItem importItem) {
			importItem.getLocation().getKey();
			importItem.getVendorItemFactory();
			if(importItem.getVendorItemFactory() != null){
				importItem.getVendorItemFactory().size();
				if(importItem.getVendorItemFactory().size() > 0){
					for(VendorItemFactory vendorItemFactory : importItem.getVendorItemFactory()){
						if(vendorItemFactory.getFactory() != null){
							vendorItemFactory.getFactory().getFactoryId();
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a list of VendorItemFactories by a vendor item.
	 * 
	 * @param itemType The itemType.
	 * @param itemCode The itemCode.
	 * @param vendorType The vendorType.
	 * @param vendorNumber The vendorNumber.
	 * @param request The HTTP request that initiated this call.
	 * @return A List of VendorItemFactories.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = VendorItemFactoryController.FIND_BY_VENDOR_ITEM_URL)
	public List<VendorItemFactory> findAllFactories(@RequestParam(value = "itemType") String itemType,
													@RequestParam(value = "itemCode") long itemCode,
													@RequestParam(value = "vendorType") String vendorType,
													@RequestParam(value = "vendorNumber") int vendorNumber,
													HttpServletRequest request) {


		this.logFindAllFactories(itemType,itemCode, vendorType, vendorNumber, request.getRemoteAddr());

		List<VendorItemFactory> results = this.service.findAllFactories(itemType, itemCode, vendorType, vendorNumber);
		results.forEach(this.resolver::fetch);
		return results;
	}

	/**
	 * Removes vendorItemFactories not in new list, and adds the factories that are new.
	 *
	 * @param importItem the import item containing the factories that the vendor item will now contain.
	 * @return the new import and the message to front end
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = VendorItemFactoryController.UPDATE_VENDOR_ITEM_URL)
	public ModifiedEntity<ImportItem> update(@RequestBody ImportItem importItem,
											 HttpServletRequest request) {

		this.logUpdateVendorItemFactories(importItem, request.getRemoteAddr());

		ImportItem updatedImportItem = this.service.update(importItem);
		this.importItemResolver.fetch(updatedImportItem);
		String updateMessage = this.messageSource.getMessage(UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{importItem.getKey().getItemCode(), importItem.getKey().getVendorNumber()},
				DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(updatedImportItem,updateMessage);
	}

	/**
	 * Logs a user's request to get all VendorItemFactories for an vendor item.
	 *
	 * @param itemType The itemType.
	 * @param itemCode The itemCode.
	 * @param vendorType The vendorType.
	 * @param vendorNumber The vendorNumber.
	 * @param ip The ip address of the request.
	 */
	private void logFindAllFactories(String itemType, long itemCode, String vendorType, int vendorNumber, String ip){
		VendorItemFactoryController.logger.info(String.format(
				VendorItemFactoryController.GET_ALL_FACTORIES_BY_VENDOR_ITEM, this.userInfo.getUserId(), ip,
				itemType,itemCode, vendorType, vendorNumber));
	}
	/**
	 * Logs a user's request to update VendorItemFactories for an vendor item.
	 *
	 * @param importItem the current import item.
	 * @param ip The ip address of the request.
	 */
	private void logUpdateVendorItemFactories(ImportItem importItem, String ip){
		VendorItemFactoryController.logger.info(String.format(
				VendorItemFactoryController.UPDATE_FACTORIES_BY_VENDOR_ITEM, this.userInfo.getUserId(), ip,
				importItem.getKey().getItemType(),importItem.getKey().getItemCode(),
				importItem.getKey().getVendorType(), importItem.getKey().getVendorNumber()));
	}
}
