/*
 * VendorInfoController
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.productDetails.casePack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.BicepVendor;
import com.heb.pm.entity.CandidateItemMaster;
import com.heb.pm.entity.VendorLocationItem;
import com.heb.pm.entity.VendorLocationItemKey;
import com.heb.pm.vendor.VendorController;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents vendor information.
 *
 * @author l730832
 * @since 2.6.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + VendorInfoController.VENDOR_INFO_URL)
@AuthorizedResource(ResourceConstants.CASE_PACK_VENDOR)
public class VendorInfoController {

	private static final Logger logger = LoggerFactory.getLogger(VendorController.class);

	protected static final String VENDOR_INFO_URL = "/vendorInfo";
	protected static final String VENDOR_ITEM_ID = "/findByItemId";
	protected static final String VENDOR_UPDATE = "/updateVendorInfo";
	protected static final String GET_VENDOR_AUDIT_INFORMATION = "/getAuditInformation";
	protected static final String FIND_ADD_TO_WAREHOUSE_CANDIDATE_WITH_PROD_ID = "/findAddToWarehouseCandidate";
	protected static final String FIND_BICEP_VENDOR_LIST_BY_AP_VENDOR_AND_CLASS = "/findBicepVendorsByApVendorAndClass";
	protected static final String COPY_ITEM_DATA_TO_PS_TABLE = "/copyItemDataToPsTable";


	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Update successful";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY = "VendorInfoController.updateSuccessful";
	private static final String NO_VENDOR_ITEM__KEY = "VendorInfoController.missingItemId";
	private static final String NO_VENDOR_ITEM_ID = "Vendor cannot be found.";
	private static final String REQUIRE_SELECT_WHS = "Please select WHS";

	// log messages
	private static final String VENDOR_SEARCH_BY_ITEM_ID = "User %s from IP %s searched for vendor by Item Id %d";
	private static final String SELECTED_VENDOR_UPDATE = "User %s from IP %s updated the list of vendor [%s] by Item Id %s.";
	private static final String VENDOR_AUDIT_BY_KEY = "User %s from IP %s has requested audit information for: %s.";
	private static final String VENDOR_AUDIT_BY_KEY_COMPLETE = "Audit information retrieved for: %s.";

	private static final String MESSAGE_BY_METHOD = "User %s from IP %s requested %s";
	private static final String LOG_GET_CANDIDATE_INFORMATION = "get Candidate information With psWorkId: %s";
	private static final String FIND_ADD_TO_WAREHOUSE_CANDIDATE_WITH_PROD_ID_MESSAGE = "find add to warehouse Candidate associated With Product Id: %s";
	private static final String FIND_BICEP_VENDOR_LIST_BY_AP_VENDOR_AND_CLASS_MESSAGE = "find bicep vendor list by ap vendor: %s and class code: %s";
	protected static final String COPY_ITEM_DATA_TO_PS_TABLE_MESSAGE = "copy all data from master into ps table to create candidate associated to product with list of warehouse [%s]";
	private static final String VENDOR_UPDATE_SUCCESS_MESSAGE = "Vendor item: %s, vendors: %s updated successfully";

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private VendorInfoService vendorInfoService;

	@Autowired
	private MorphDsdItemManagementService morphDsdItemManagementService;
	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AuditService auditService;

	private LazyObjectResolver<VendorLocationItem> vendorLocationItemResolver = new VendorLocationItemResolver();

	private LazyObjectResolver<List<CandidateItemMaster>> psItemMasterLazyObjectResolver = new PsItemMasterResolver();

	/**
	 * Resolves a PsItemMaster object. It will load the following properties:
	 */
	private class PsItemMasterResolver implements LazyObjectResolver<List<CandidateItemMaster>> {

		@Override
		public void fetch(List<CandidateItemMaster> candidateItemMasters) {
			if(candidateItemMasters != null){
				candidateItemMasters.forEach((p) -> {
					p.getCandidateWarehouseLocationItems().size();
				});
			}
		}
	}

	/**
	 * Searches for a particular vendor by Item Id.
	 *
	 * @param itemId The AP number of the vendor you wish to find.
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = VendorInfoController.VENDOR_ITEM_ID)
	public List<VendorLocationItem> findByItemId(@RequestParam("itemId") Long itemId,
												 @RequestParam("itemType") String itemType, HttpServletRequest request){

		this.parameterValidator.validate(itemId, VendorInfoController.NO_VENDOR_ITEM_ID,
				VendorInfoController.NO_VENDOR_ITEM__KEY, request.getLocale());
		this.logFindByItemId(request.getRemoteAddr(), itemId);

		List<VendorLocationItem> results = this.vendorInfoService.findByKeyItemCodeAndKeyItemType(itemId, itemType);
		results.forEach(this.vendorLocationItemResolver::fetch);

		return results;
	}

	/**
	 * Saves a particular vendor.
	 *
	 * @param vendors the list of vendor location item that is being updated.
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = VendorInfoController.VENDOR_UPDATE)
	public ModifiedEntity<String> updateVendorInfo(@RequestBody List<VendorLocationItem> vendors,
															   HttpServletRequest request){
		this.parameterValidator.validate(vendors, VendorInfoController.NO_VENDOR_ITEM_ID,
				VendorInfoController.NO_VENDOR_ITEM__KEY, request.getLocale());
		this.logUpdateVendorInfo(request.getRemoteAddr(), vendors);
		this.vendorInfoService.updateVendorInfo(vendors);
		List<Integer> vendorNumbers = new ArrayList<>();
		vendors.forEach(vendorLocationItem ->{
			vendorNumbers.add(vendorLocationItem.getKey().getVendorNumber());
		});
		return new ModifiedEntity<>(VendorInfoController.UPDATE_SUCCESS_MESSAGE_KEY,
				String.format(VendorInfoController.VENDOR_UPDATE_SUCCESS_MESSAGE,
						(vendors != null && !vendors.isEmpty())? vendors.get(0).getKey().getItemCode().toString():"",
						vendorNumbers.toString()));
	}

	/**
	 * Retrieves audit information on a particular vendor.
	 * @param key The key that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of audits attached to given vendor.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = VendorInfoController.GET_VENDOR_AUDIT_INFORMATION)
	public List<AuditRecord> getVendorAuditInformation(
			@RequestBody VendorLocationItemKey key, HttpServletRequest request) {

		this.logGetVendorAuditInformation(request.getRemoteAddr(), key);

		List<AuditRecord> toReturn = this.auditService.getVendorAuditInformation(key);
		this.logGetVendorAuditInformationComplete(key);
		return toReturn;
	}

	/**
	 * Check candidate associated with product. It's mean check product already exists an candidate. If exist, we
	 * will search this candidate and view, not show list beciep vendor to user add, and create an new candidate
	 * associated to product id.
	 *
	 * @param prodId The product id, you wish to find.
	 * @param request The HTTP request that initiated this call.
	 * @return The ps work id if already exists an candidate associated to product. Else return zero.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = VendorInfoController.FIND_ADD_TO_WAREHOUSE_CANDIDATE_WITH_PROD_ID)
	public ModifiedEntity<Long> findAddToWarehouseCandidate(@RequestParam("prodId") Long prodId,
																 HttpServletRequest request){
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(VendorInfoController.FIND_ADD_TO_WAREHOUSE_CANDIDATE_WITH_PROD_ID_MESSAGE, prodId));
		Long psWorkId = this.vendorInfoService.findAddToWarehouseCandidate(prodId);
		return new ModifiedEntity<>(psWorkId, VendorInfoController.FIND_ADD_TO_WAREHOUSE_CANDIDATE_WITH_PROD_ID);
	}

    /**
	 * Get candidate information associated with product.
	 *
	 * @param psWorkId - The ps work id.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of PsItemMaster.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/getCandidateInformation")
	public List<CandidateItemMaster> getCandidateInformation(@RequestParam("psWorkId") Long psWorkId, HttpServletRequest request){
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(VendorInfoController.LOG_GET_CANDIDATE_INFORMATION, psWorkId));
		List<CandidateItemMaster> returnList = this.vendorInfoService.getCandidateInformation(psWorkId);
		this.psItemMasterLazyObjectResolver.fetch(returnList);
		return returnList;
	}

	/**
	 * Find list of bicep vendor (A bicep vendor contain information vendor location number, bicep, ap location
	 * number, warehouse number, facility) to user add, and create an new candidate associated to product.
	 *
	 * @param apVendor The app location vendor
	 * @param classCode  - The class code
	 * @param request The HTTP request that initiated this call.
	 * @return The list of bicep vendor.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = VendorInfoController.FIND_BICEP_VENDOR_LIST_BY_AP_VENDOR_AND_CLASS)
	public List<BicepVendor> findBicepVendorsByApVendorAndClass(@RequestParam("apVendor") Integer apVendor,
																@RequestParam("classCode") Integer classCode,
																HttpServletRequest request) throws CheckedSoapException{
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(VendorInfoController.FIND_BICEP_VENDOR_LIST_BY_AP_VENDOR_AND_CLASS_MESSAGE, apVendor, classCode));
		return this.vendorInfoService.findBicepVendorsByApVendorAndClass(apVendor, classCode);
	}

	/**
	 * Copy data from master to ps table => create candidate associated to current product, when use add new warehouse
	 * to product.
	 *
	 * @param bicepVendors The list of bicep vendor user add to product.
	 * @param request The HTTP request that initiated this call.
	 * @return The vendor matching the AP number you passed in. Null if not found.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = VendorInfoController.COPY_ITEM_DATA_TO_PS_TABLE)
	public ModifiedEntity<Long> copyItemDataToPsTable(@RequestBody List<BicepVendor> bicepVendors,
														HttpServletRequest request) throws Exception{
		this.showLogFromMethodRequest(request.getRemoteAddr(), String.format(VendorInfoController.COPY_ITEM_DATA_TO_PS_TABLE_MESSAGE, ListFormatter.formatAsString(bicepVendors)));
		long workRequestId = 0;
		String returnMessage = DEFAULT_UPDATE_SUCCESS_MESSAGE; 
		if(bicepVendors !=null && !bicepVendors.isEmpty()){
			try {
				workRequestId = this.morphDsdItemManagementService.copyItemDataToPsTable(bicepVendors, userInfo);
			}catch (Exception e){
				logger.info(e.getMessage());
				returnMessage = e.getMessage();
			}
		} else {
			throw new DataIntegrityViolationException(REQUIRE_SELECT_WHS);
		}
		return new ModifiedEntity<>(workRequestId, returnMessage);
	}

	/**
	 * Logs a users request for all handle in Vendor Info Controller.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @messageFromMethod messageFromMethod The detail message sent from method.
	 */
	private void showLogFromMethodRequest(String ipAddress, String messageFromMethod) {
		VendorInfoController.logger.info(String.format(VendorInfoController.MESSAGE_BY_METHOD, this.userInfo
				.getUserId(), ipAddress, messageFromMethod));
	}

	/**
	 * Logs get vendor audit information by key completion.
	 *
	 * @param key The vendor location item key that was searched on.
	 */
	private void logGetVendorAuditInformationComplete(VendorLocationItemKey key) {
		VendorInfoController.logger.info(
				String.format(VendorInfoController.VENDOR_AUDIT_BY_KEY_COMPLETE, key)
		);
	}

	/**
	 * Logs get vendor audit information by key.
	 *
	 * @param ip The user's ip.
	 * @param key The vendor location item key that is being searched on.
	 */
	private void logGetVendorAuditInformation(String ip, VendorLocationItemKey key) {
		VendorInfoController.logger.info(
				String.format(VendorInfoController.VENDOR_AUDIT_BY_KEY, this.userInfo.getUserId(), ip, key)
		);
	}

	/**
	 * Logs the findy by item Id
	 * @param ip the users ip
	 * @param itemId the item id that is being searched on.
	 */
	private void logFindByItemId(String ip, long itemId) {
		VendorInfoController.logger.info(
				String.format(VendorInfoController.VENDOR_SEARCH_BY_ITEM_ID, this.userInfo.getUserId(),
						ip, itemId)
		);
	}

	/**
	 * Logs the findy by item Id
	 * @param ip the users ip
	 * @param vendorLocationItem the vendor location item that is being updated.
	 */
	private void logUpdateVendorInfo(String ip, List<VendorLocationItem> vendorLocationItem) {
		VendorInfoController.logger.info(
				String.format(VendorInfoController.SELECTED_VENDOR_UPDATE, this.userInfo.getUserId(),
						ip, ListFormatter.formatAsString(vendorLocationItem), vendorLocationItem.get(0).getKey()
								.getItemCode().toString())
		);
	}

}
