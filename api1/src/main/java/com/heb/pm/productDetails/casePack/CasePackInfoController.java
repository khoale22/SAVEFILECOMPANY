/*
 *  CasePackInfoController
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
import com.heb.pm.entity.*;
import com.heb.pm.product.ProductInfoService;
import com.heb.pm.upcMaintenance.UpcSwap;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.upc.UpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents case pack information.
 *
 * @author l730832
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CasePackInfoController.CASE_PACK_INFO)
@AuthorizedResource(ResourceConstants.CASE_PACK_INFO)
public class CasePackInfoController {

	private static final Logger logger = LoggerFactory.getLogger(CasePackInfoController.class);

	private static final String CHECK_DIGIT_CALCULATED_SUCCESSFULLY ="CasePackInfoController.checkDigitCalculatedSuccessully";
	private static final String DEFAULT_CHECK_DIGIT_CALCULATED_SUCCESSFULLY_MESSAGE ="Check Digit calculated successfully for: %d";

	private static final String UPDATE_SUCCESS_MESSAGE = "CasePackInfoController.updateSuccessful";

	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Item Code: %d, Case Upc: %d updated successfully.";
	private static final String DEFAULT_CHECK_DIGIT_CONFIRMED = "Correct";
	private static final String DEFAULT_CHECK_DIGIT_UNCONFIRMED = "Incorrect";
	private static final String CHECK_DIGIT_CONFIRMED = "CasePackInfoController.checkDigitConfirmed";
	private static final String CHECK_DIGIT_UNCONFIRMED = "CasePackInfoController.checkDigitUnconfirmed";
	private static final String MESSAGE_ERROR_REACTIVE_ITEM = "User does not have permission to re-active item.";

	private static final String MESSAGE_BY_METHOD = "User %s from IP %s requested %s";
	private static final String LOG_CASE_PACK_AUDIT_BY_UPC =
			"User %s from IP %s has requested Case Pack Audit information for ItemMaster with code %s, and type %s";
	private static final String LOG_CASE_PACK_AUDIT_BY_UPC_COMPLETE =
			"Case Pack Audit information has been retrieved for ItemMaster with code %s, and type %s";
	private static final String LOG_GET_ALL_DISCONTINUE_REASONS = "get all Discontinue Reasons";
	private static final String LOG_GET_CASE_PACK_INFORMATION = "get Case Pack information with Item Code %d, and Item Type %s";

	private static final String UPDATE_CASE_PACK_INFO =	"User %s from IP %s has updated item Id: %d and case pack upc: %d";
	private static final String CHANGE_ITEM_PRIMARY_UPC_MESSAGE = "change item primary UPC to %s";
	private static final String CHANGE_ITEM_PRIMARY_UPC_MESSAGE_SUCCESS ="Item primary changed successfully, please press save to update other fields.";

	protected static final String CASE_PACK_INFO = "/casePack";
	protected static final String CASE_PACK_AUDIT = "/casePackAudits";
	protected static final String CHANGE_ITEM_PRIMARY_UPC = "/changeItemPrimaryUPC";

	@Autowired
	private CasePackInfoService service;

	@Autowired
	private ProductInfoService productInfoService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private UserService userService;

	private LazyObjectResolver<ItemMaster> itemMasterLazyObjectResolver = new ItemMasterResolver();

	/**
	 * Resolves a ItemMaster object. It will load the following properties:
	 * 1 itemMasterKey
	 */
	private class ItemMasterResolver implements LazyObjectResolver<ItemMaster> {

		@Override
		public void fetch(ItemMaster itemMaster) {
			itemMaster.getKey().getItemCode();
			if (itemMaster.getKey().isWarehouse()) {
				this.fetchWarehouseData(itemMaster);
				this.fetchPrimaryUpc(itemMaster);
			} else {
				this.fetchMerchandiseTypes(itemMaster);
			}

			if (userService != null) {
				User user = userService	.getUserById(itemMaster.getAddedUsrId());
				if (user != null) {
					itemMaster.setDisplayCreatedName(user.getDisplayName());
				} else {
					itemMaster.setDisplayCreatedName(itemMaster.getAddedUsrId());
				}
			}
		}

		/**
		 * Traverses the UPC portion of the object model for warehouse products.
		 *
		 * @param im The item to traverse.
		 */
		private void fetchWarehouseData(ItemMaster im) {
			im.getWarehouseLocationItems().size();
			im.getWarehouseLocationItemExtendedAttributes().size();
		}

		/**
		 * Resolves all of the merchandies types. JPA needs to be able to find a merchandise type code and the jpa resolver
		 * fails whenever the field is empty(not null). i.e. If the database doesn't have a pss department it is saved as ' '.
		 * This will trim all of the whitespace and then check to see if it is empty. This is lazily loaded because it should
		 * only be resolved for dsd items.
		 *
		 * @param im The item to fetch merchandise types for.
		 */
		private void fetchMerchandiseTypes(ItemMaster im) {
			if(!im.getMerchandiseTypeCodeOne().toString().trim().isEmpty()) {
				im.getMerchandiseTypeOne().getId();
			}

			if(!im.getMerchandiseTypeCodeTwo().toString().trim().isEmpty()) {
				im.getMerchandiseTypeTwo().getId();
			}

			if(!im.getMerchandiseTypeCodeThree().toString().trim().isEmpty()) {
				im.getMerchandiseTypeThree().getId();
			}

			if(!im.getMerchandiseTypeCodeFour().toString().trim().isEmpty()) {
				im.getMerchandiseTypeFour().getId();
			}
		}

		/**
		 * Resolves all of the Associate Upc information. Get all information primary upc contain list of upc
		 * Associated to product basing on item id.
		 * @param itemMaster
		 */
		private void fetchPrimaryUpc(ItemMaster itemMaster){
			if(itemMaster.getPrimaryUpc() != null) {
				itemMaster.getPrimaryUpc().getUpc();
				if(itemMaster.getPrimaryUpc().getAssociateUpcs() != null) {
					itemMaster.getPrimaryUpc().getAssociateUpcs().size();
					itemMaster.getPrimaryUpc().getAssociateUpcs().forEach((asUpc) ->{
						if(asUpc.getSellingUnit() != null) {
							asUpc.getSellingUnit().getUpc();
						}
					});
				}
			}
		}
	}

	/**
	 * Calculates and returns the check digit..
	 *
	 * @param request the request
	 * @return the calculated check digit.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "calculateCheckDigit")
	public ModifiedEntity<Long> calculateCheckDigit(@RequestParam(value = "upc") long upc, HttpServletRequest request) {

		String updateMessage = this.messageSource.getMessage(CasePackInfoController.CHECK_DIGIT_CALCULATED_SUCCESSFULLY,
				new Object[]{upc}, String.format(CasePackInfoController.DEFAULT_CHECK_DIGIT_CALCULATED_SUCCESSFULLY_MESSAGE, upc),
				request.getLocale());

		return new ModifiedEntity<>((long) UpcUtils.calculateCheckDigit(upc), updateMessage);
	}

	/**
	 * This returns the OneTouchType of the associated code that is attached to the item.
	 *
	 * @param request the request
	 * @return The OneTouchType of the associated code that is attached to the item.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllOneTouchTypes")
	public List<OneTouchType> getAllOneTouchType(HttpServletRequest request) {

		return this.service.getAllOneTouchTypes();
	}

	/**
	 * Get full item types list from the code table.
	 *
	 * @param request the request
	 * @return the list
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllItemTypes")
	public List<ItemType> getAllItemTypes(HttpServletRequest request){

		return this.service.getAllItemTypes();
	}

	/**
	 * Get full Discontinue Reasons list from the code table.
	 *
	 * @param request the request
	 * @return the list
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET, value = "getAllDiscontinueReasons")
	public List<DiscontinueReason> getAllDiscontinueReasons(HttpServletRequest request){
		//show log for method
		CasePackInfoController.logger.info(String.format(CasePackInfoController.MESSAGE_BY_METHOD, this.userInfo
				.getUserId(), request.getRemoteAddr(),  CasePackInfoController.LOG_GET_ALL_DISCONTINUE_REASONS));
		//get all discontinue reasons handle
		return this.service.getAllDiscontinueReasons();
	}

	/**
	 * Get the case pack information.
	 * @param itemMasterKey The item ID of the requested Item Master
	 * @param request HTTP request
	 * @return
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = "getCasePackInformation")
	public ItemMaster getCasePackInformation(@RequestBody ItemMasterKey itemMasterKey, HttpServletRequest request) {
		//Show log for method
		CasePackInfoController.logger.info(String.format(CasePackInfoController.MESSAGE_BY_METHOD, this.userInfo
				.getUserId(), request.getRemoteAddr(), String.format(CasePackInfoController
				.LOG_GET_CASE_PACK_INFORMATION, itemMasterKey.getItemCode(), itemMasterKey.getItemType())));
		//get case pack information handle
		ItemMaster im = this.productInfoService.findItemByItemId(itemMasterKey);
		this.itemMasterLazyObjectResolver.fetch(im);
		return im;
	}

	/**
	 * Checks to make sure the check digit is correct.
	 *
	 * @param checkDigit the check digit that was entered to check with the upc
	 * @param upc the case upc to check with the check digit.
	 * @param request the request
	 * @return A string to determine whether the check digit is correct or not.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "confirmCheckDigit")
	public ModifiedEntity<Boolean> confirmCheckDigit(@RequestParam("checkDigit") Integer checkDigit,
													 @RequestParam("upc") Long upc, HttpServletRequest request) {
		String updateMessage;
		Boolean confirmCheckDigit = UpcUtils.validateCheckDigit(upc, checkDigit);
		// If the check digit is correct.
		if(confirmCheckDigit) {
			updateMessage = this.messageSource.getMessage(
					CasePackInfoController.CHECK_DIGIT_CONFIRMED,
					new Object[]{checkDigit}, CasePackInfoController.DEFAULT_CHECK_DIGIT_CONFIRMED, request.getLocale());
		} else {
			// If the check digit is incorrect.
			updateMessage = this.messageSource.getMessage(
					CasePackInfoController.CHECK_DIGIT_UNCONFIRMED,
					new Object[]{checkDigit}, CasePackInfoController.DEFAULT_CHECK_DIGIT_UNCONFIRMED, request.getLocale());
		}
		return new ModifiedEntity<>(confirmCheckDigit, updateMessage);
	}

	/**
	 * Updates the case pack information.
	 *
	 * @param itemMaster The item master to be updated.
	 * @param request the request.
	 * @return Returns the updated item master.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "saveCasePackInfoChanges")
	public ModifiedEntity<ItemMaster> saveCasePackInfoChanges(@RequestBody ItemMaster itemMaster, HttpServletRequest request) {

		this.logSaveCasePackInfoChanges(request.getRemoteAddr(), itemMaster);

		String updateMessage = this.messageSource.getMessage(
				CasePackInfoController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{itemMaster.getKey().getItemCode(), itemMaster.getCaseUpc()},
				CasePackInfoController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		if(itemMaster.getReActive()!=null && itemMaster.getReActive() && !userInfo.canUserEditResource(ResourceConstants.REACTIVE_ITEM)){
			throw new IllegalArgumentException(MESSAGE_ERROR_REACTIVE_ITEM);
		}
		return new ModifiedEntity<>(this.service.updateCasePackInfo(itemMaster), updateMessage);
	}

	/**
	 * Retrieves Case Pack audit information.
	 * @param key The ItemMasterKey that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of case pack audits attached to given item master key.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackInfoController.CASE_PACK_AUDIT)
	public List<AuditRecord> getCasePackAuditInfo(@RequestBody ItemMasterKey key, HttpServletRequest request) {
		this.logGetCasePackAuditInformation(request.getRemoteAddr(), key.getItemCode(), key.getItemType());
		List<AuditRecord> casePackAudit = this.service.getCasePackAuditInformation(key, FilterConstants.CASE_PACK_INFO_AUDIT);
		this.logGetCasePackAuditInformationComplete(key.getItemCode(), key.getItemType());
		return casePackAudit;
	}

	/**
	 * Change another upc to item primary upc
	 * @param upcSwap The upc swap that contain all information current item primary upc and new item primary upc.
	 * @param request The HTTP request that initiated this call.
	 * @return The message for status changed and item master.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = CasePackInfoController.CHANGE_ITEM_PRIMARY_UPC)
	public ModifiedEntity<ItemMaster> changeItemPrimaryUPC(@RequestBody UpcSwap upcSwap, HttpServletRequest request) throws
			CheckedSoapException {
		//Show log for method
		CasePackInfoController.logger.info(String.format(CasePackInfoController.MESSAGE_BY_METHOD, this.userInfo
				.getUserId(), request.getRemoteAddr(),  String.format(CHANGE_ITEM_PRIMARY_UPC_MESSAGE, upcSwap.getDestination().getPrimaryUpc())));
		//change item primary upc handle
		this.service.changeItemPrimaryUPC(upcSwap);
		//refresh item master information after change item primary upc
		ItemMasterKey itemMasterKey = new ItemMasterKey();
		itemMasterKey.setItemType(upcSwap.getSource().getItemType());
		itemMasterKey.setItemCode(upcSwap.getSource().getItemCode());
		ItemMaster itemMaster = this.productInfoService.findItemByItemId(itemMasterKey);
		this.itemMasterLazyObjectResolver.fetch(itemMaster);
		//return message updated successfully and item master.
		return new ModifiedEntity<>(itemMaster, CasePackInfoController.CHANGE_ITEM_PRIMARY_UPC_MESSAGE_SUCCESS);
	}

	/**
	 * Logs a user's request to update case pack info changes..
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param itemMaster The itemMaster to retrieve the item id and case upc to be updated.
	 */
	private void logSaveCasePackInfoChanges(String ip, ItemMaster itemMaster) {
		CasePackInfoController.logger.info(
				String.format(CasePackInfoController.UPDATE_CASE_PACK_INFO, this.userInfo.getUserId(),
						ip, itemMaster.getKey().getItemCode(), itemMaster.getCaseUpc()));
	}

	/**
	 * Logs get case pack audit information completion.
	 *
	 * @param itemCode The identification number for the item's Case pack being requested
	 * @param itemType the type of item whose Case pack is being requested
	 */
	private void logGetCasePackAuditInformationComplete(Long itemCode, String itemType) {
		CasePackInfoController.logger.info(
				String.format(CasePackInfoController.LOG_CASE_PACK_AUDIT_BY_UPC_COMPLETE, itemCode, itemType)
		);
	}

	/**
	 * Logs get case pack audit information.
	 *
	 * @param itemCode The identification number for the item's Case pack being requested
	 * @param itemType the type of item whose Case pack is being requested
	 */
	private void logGetCasePackAuditInformation(String ip, Long itemCode, String itemType) {
		CasePackInfoController.logger.info(
				String.format(CasePackInfoController.LOG_CASE_PACK_AUDIT_BY_UPC, this.userInfo.getUserId(), ip, itemCode, itemType)
		);
	}
}
