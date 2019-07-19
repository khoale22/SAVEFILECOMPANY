package com.heb.pm.productDetails.casePack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.FilterConstants;
import com.heb.pm.entity.ItemMaster;
import com.heb.pm.entity.ItemMasterKey;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Resstful service for updating the DRU attributes in item master.
 * @author  s753601
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + DruInfoController.DRU_URL)
@AuthorizedResource(ResourceConstants.DRU_INFO)
public class DruInfoController {
	private static final Logger logger = LoggerFactory.getLogger(DruInfoController.class);

	/**
	 * The constant DRU_URL.
	 */
	protected static final String DRU_URL = "/druInfo";
	protected static final String DRU_AUDIT="/getDruAudits";

	/**
	 * Common Logger Strings
	 */
	private static final String SUBMIT_DRU_UPDATE =
			"User %s from IP %s has submitted an DRU details update : %s.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY =
			"DruInfoController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Save successful";
	private static final String NO_ITEM__KEY = "DruInfoController.missingItemId";
	private static final String NO_ITEM_ID = "Item cannot be found.";
	private static final String LOG_DRU_AUDIT_BY_UPC = "User %s from IP %s has requested DRU audit information for ItemMaster with code %s, and type %s";
	private static final String LOG_DRU_AUDIT_BY_UPC_COMPLETE = "DRU Audit information has been retrieved for ItemMaster with code %s, and type %s";

	@Autowired
	private DruInfoService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;


	/**
	 * Recieves new DRU attributes from the UI and sends the new data to the database service
	 * @param druSaveData The new dru attributes to be submitted to the database
	 * @param request the HTTP request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public ModifiedEntity<ItemMaster> saveDruInfo(@RequestBody ItemMaster druSaveData, HttpServletRequest request){

		this.logSubmitDruUpdate(request.getRemoteAddr(), druSaveData);

		this.parameterValidator.validate(druSaveData, DruInfoController.NO_ITEM_ID,
				DruInfoController.NO_ITEM__KEY, request.getLocale());

		String updateMessage = this.messageSource.getMessage(
				DruInfoController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{druSaveData.getKey().getItemCode()}, DruInfoController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());
		ItemMaster updatedItemMaster = this.service.updateDruQuantity(druSaveData);
		return new ModifiedEntity<>(updatedItemMaster, updateMessage);
	}

	/**
	 * Retrieves DRU audit information.
	 * @param key The ItemMasterKey that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of DRU audits attached to given item master key.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = DruInfoController.DRU_AUDIT)
	public List<AuditRecord> getDruAuditInfo(@RequestBody ItemMasterKey key, HttpServletRequest request) {
		this.logGetDruAuditInformation(request.getRemoteAddr(), key.getItemCode(), key.getItemType());
		List<AuditRecord> druAudit = this.service.getDruAuditInformation(key, FilterConstants.DRU_AUDIT);
		this.logGetDruAuditInformationComplete(key.getItemCode(), key.getItemType());
		return druAudit;
	}

	/**
	 * Logs get dru audit information completion.
	 *
	 * @param itemCode The identification number for the item's DRU being requested
	 * @param itemType the type of item whose DRU is being requested
	 */
	private void logGetDruAuditInformationComplete(Long itemCode, String itemType) {
		DruInfoController.logger.info(
				String.format(DruInfoController.LOG_DRU_AUDIT_BY_UPC_COMPLETE, itemCode, itemType)
		);
	}

	/**
	 * Logs get dru audit information.
	 *
	 * @param ip The user's ip.
	 * @param itemCode The identification number for the item's DRU being requested
	 * @param itemType the type of item whose DRU is being requested
	 */
	private void logGetDruAuditInformation(String ip, Long itemCode, String itemType) {
		DruInfoController.logger.info(
				String.format(DruInfoController.LOG_DRU_AUDIT_BY_UPC, this.userInfo.getUserId(), ip, itemCode, itemType)
		);
	}

	/**
	 * Log changes made to the DRU attributes
	 * @param ip the ip where the request came from
	 * @param druSaveData the new DRU information
	 */
	private void logSubmitDruUpdate(String ip, ItemMaster druSaveData) {

		DruInfoController.logger.info(
				String.format(DruInfoController.SUBMIT_DRU_UPDATE, this.userInfo.getUserId(),
						ip, druSaveData));
	}

	/**
	 * Sets the ProductInfoService for this object to use. This is primarily for testing.
	 *
	 * @param druInfoService The ProductInfoService for this object to use.
	 */
	public void setService(DruInfoService druInfoService) {
		this.service = druInfoService;
	}

	/**
	 * Sets the UserInfo for this class to use. This is primarily for testing.
	 *
	 * @param userInfo The UserInfo for this class to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this class to use. This is primarily for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this class to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets the message source for this class to use. This is primarily for testing.
	 *
	 * @param messageSource The NonEmptyParameterValidator for this class to use.
	 */
	public void setMessageSource(MessageSource messageSource){
		this.messageSource = messageSource;
	}
}
