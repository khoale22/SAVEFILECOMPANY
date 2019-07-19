/*
 *  MrtInfoController
 *  Copyright (c) 2017 HEB
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
import com.heb.pm.audit.AuditRecordWithUpc;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ProdItem;
import com.heb.pm.entity.SellingUnit;
import com.heb.pm.entity.Shipper;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents mrtInfo information.
 *
 * @author m594201
 * @since 2.6.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + MrtInfoController.MRT_URL)
@AuthorizedResource(ResourceConstants.MRT_INFO)
public class MrtInfoController {

	private static final Logger logger = LoggerFactory.getLogger(MrtInfoController.class);

	/**
	 * The constant MRT_URL.
	 */
	protected static final String MRT_URL = "/mrtInfo";
	/**
	 * The constant GET_MRT_AUDIT_INFO.
	 */
	protected static final String GET_MRT_INFO = "/{upc}";
	/**
	 * The constant GET_MRT_AUDIT_INFO.
	 */
	protected static final String GET_MRT_ITEM_INFO = "/item/{itemCode}/{itemType}";
	/**
	 * The constant GET_MRT_ELEMENT_INFO.
	 */
	protected static final String GET_MRT_ELEMENT_INFO = "/elements/{upc}";
	/**
	 * The constant SAVE_MRT_INFO_URL.
	 */
	protected static final String SAVE_MRT_INFO_URL = "/saveMrtInfo";
	/**
	 * The constant GET_MRT_AUDIT_INFO.
	 */
	protected static final String GET_MRT_AUDIT_INFO = "/getMrtAuditInformation";

	// log messages
	private static final String LOG_MRT_INFO_BY_UPC = "User %s from IP %s has requested MRT information for upc: %s";
	private static final String LOG_MRT_ITEM_INFO_BY_UPC = "User %s from IP %s has requested MRT Item information for item code: %s and item type: %s";
	private static final String LOG_MRT_ELEMENT_INFO_BY_UPC = "User %s from IP %s has requested MRT element information for upc: %s";
	private static final String SUBMIT_MRT_UPDATE = "User %s from IP %s has submitted an MRT details update : %s.";
	private static final String LOG_MRT_AUDIT_BY_UPC = "User %s from IP %s has requested audit information for upc: %s";
	private static final String LOG_MRT_AUDIT_BY_UPC_COMPLETE = "Audit information has been retrieved for upc: %s.";

	private static final String UPDATE_SUCCESS_MESSAGE_KEY = "MrtInfoController.updateSuccessful";

	@Autowired
	private MrtInfoService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	private LazyObjectResolver<Shipper> shipperResolver = new ShipperResolver();

	/**
	 * Retrieves MRT information.
	 * @param upc The upc that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of audits attached to given mrt upc.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = MrtInfoController.GET_MRT_INFO)
	public List<Shipper> getMrtInfo(@PathVariable("upc") Long upc, HttpServletRequest request) {
		this.logGetMrtInformation(request.getRemoteAddr(), upc);
		List<Shipper> shippers = this.service.getMrtInformation(upc);
		shippers.forEach(shipper -> this.shipperResolver.fetch(shipper));
		return shippers;
	}

	/**
	 * Retrieves MRT Item information.
	 * @param itemCode The item code that the audit is being searched on.
	 * @param itemType The item type that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of audits attached to given mrt upc.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = MrtInfoController.GET_MRT_ITEM_INFO)
	public List<ProdItem> getMrtItemInfo(@PathVariable(value="itemCode") Long itemCode,
										 @PathVariable(value="itemType") String itemType,
										 HttpServletRequest request) {
		this.logGetMrtItemInformation(request.getRemoteAddr(), itemCode, itemType);
		List<ProdItem> prodItems = this.service.getMrtItemInformation(itemCode, itemType);
		return prodItems;
	}

	/**
	 * Retrieves MRT Element information.
	 * @param upc The upc that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of audits attached to given mrt upc.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = MrtInfoController.GET_MRT_ELEMENT_INFO)
	public SellingUnit getMrtElementInfo(@PathVariable(value="upc") Long upc, HttpServletRequest request) {
		this.logGetMrtInformation(request.getRemoteAddr(), upc);
		SellingUnit sellingUnit = this.service.getMrtElementInfo(upc);
		if (sellingUnit != null) {
			sellingUnit.getProductMaster().getProdId();
		}
		return sellingUnit;
	}

	/**
	 * Save mrt info modified entity.
	 *
	 * @param mrtSaveData the mrt save data
	 * @param request     the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = MrtInfoController.SAVE_MRT_INFO_URL)
	public ModifiedEntity<String> saveMrtInfo(@RequestBody ItemMaster mrtSaveData, HttpServletRequest request){
		this.logSubmitMrtUpdate(request.getRemoteAddr(), mrtSaveData);
		String updateMessage = this.messageSource.getMessage(
				MrtInfoController.UPDATE_SUCCESS_MESSAGE_KEY,
				null, MrtInfoController.SUBMIT_MRT_UPDATE, request.getLocale());
		this.service.updateMrtInfo(mrtSaveData);
		return new ModifiedEntity<>(UPDATE_SUCCESS_MESSAGE_KEY, updateMessage);
	}

	/**
	 * Retrieves MRT audit information.
	 * @param upc The upc that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of audits attached to given mrt upc.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = MrtInfoController.GET_MRT_AUDIT_INFO)
	public List<AuditRecordWithUpc> getMrtAuditInfo(@RequestParam(value="upc") Long upc, HttpServletRequest request) {

		this.logGetMrtAuditInformation(request.getRemoteAddr(), upc);
		List<AuditRecordWithUpc> toReturn = this.service.getMrtAuditInformation(upc);
		this.logGetMrtAuditInformationComplete(upc);
		return toReturn;
	}

	/**
	 * Logs get mrt audit information by upc completion.
	 *
	 * @param upc The upc being searched on.
	 */
	private void logGetMrtAuditInformationComplete(Long upc) {
		MrtInfoController.logger.info(
				String.format(MrtInfoController.LOG_MRT_AUDIT_BY_UPC_COMPLETE, upc)
		);
	}

	/**
	 * Logs get mrt audit information by upc.
	 *
	 * @param ip The user's ip.
	 * @param upc The upc being searched on.
	 */
	private void logGetMrtAuditInformation(String ip, Long upc) {
		MrtInfoController.logger.info(
				String.format(MrtInfoController.LOG_MRT_AUDIT_BY_UPC, this.userInfo.getUserId(), ip, upc)
		);
	}

	/**
	 * Logs get mrt information by upc.
	 *
	 * @param ip The user's ip.
	 * @param upc The upc being searched on.
	 */
	private void logGetMrtInformation(String ip, Long upc) {
		MrtInfoController.logger.info(
				String.format(MrtInfoController.LOG_MRT_INFO_BY_UPC, this.userInfo.getUserId(), ip, upc)
		);
	}

	/**
	 * Logs get mrt item information by upc.
	 *
	 * @param ip The user's ip.
	 * @param itemCode The item code being searched on.
	 * @param itemType The item type being searched on.
	 */
	private void logGetMrtItemInformation(String ip, Long itemCode, String itemType) {
		MrtInfoController.logger.info(
				String.format(MrtInfoController.LOG_MRT_ITEM_INFO_BY_UPC, this.userInfo.getUserId(), ip, itemCode, itemType)
		);
	}

	/**
	 * Logs get mrt element information by upc.
	 *
	 * @param ip The user's ip.
	 * @param upc The upc being searched on.
	 */
	private void logGetMrtElementInformation(String ip, Long upc) {
		MrtInfoController.logger.info(
				String.format(MrtInfoController.LOG_MRT_ELEMENT_INFO_BY_UPC, this.userInfo.getUserId(), ip, upc)
		);
	}

	/**
	 * Logs a user's request for submitting MRT Update.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param mrtSaveData The list of DSDs to both to submit.
	 */
	private void logSubmitMrtUpdate(String ip, ItemMaster mrtSaveData) {

			MrtInfoController.logger.info(
					String.format(MrtInfoController.SUBMIT_MRT_UPDATE, this.userInfo.getUserId(),
							ip, mrtSaveData));
	}
}