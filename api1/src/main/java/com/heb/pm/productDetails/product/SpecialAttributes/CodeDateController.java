/*
 *  CodeDateController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.SpecialAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This is the controller for code date.
 *
 * @author l730832
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeDateController.SPECIAL_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.SPECIAL_ATTRIBUTES_CODE_DATE)
public class CodeDateController {

	private static final Logger logger = LoggerFactory.getLogger(CodeDateController.class);

	private static final String UPDATE_SUCCESS_MESSAGE = "Code Date prouduct [%d] has been successfully updated.";

	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "CodeDateController.saveCodeDateChanges";
	private static final String CODE_DATE_LOG = "User %s from IP %s requested to update the code table" +
			"product [%d].";
	private static final String CODE_DATE_LOG_COMPLETED = "Code Date product [%d] has completed updating.";
	private static final String LOG_CODE_DATE_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested special attributes code date audit information for prod ID: %s";

	@Autowired
	private CodeDateService service;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserInfo userInfo;

	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	protected static final String SPECIAL_ATTRIBUTES = "/specialAttributes";
	private static final String SAVE_CODE_DATE = "/saveCodeDateChanges";
	private static final String GET_CODE_DATE_AUDITS = "/getCodeDateAudits";

	/**
	 * This updates a code date product where changes have been made from the code date screen.
	 * @param productMaster
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SAVE_CODE_DATE)
	public ModifiedEntity<ProductMaster> saveCodeDateChanges(@RequestBody ProductMaster productMaster, HttpServletRequest request) {
		this.logSaveCodeDateChanges(request.getRemoteAddr(), productMaster.getProdId());
		ProductMaster updatedProductMaster = this.service.saveCodeDateChanges(productMaster);

		this.productInfoResolver.fetch(updatedProductMaster);

		this.logSaveCodeDateChangesCompleted(productMaster.getProdId());

		String updateMessage = this.messageSource.getMessage(
				CodeDateController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				new Object[]{productMaster.getProdId()}, String.format(CodeDateController.UPDATE_SUCCESS_MESSAGE,
						productMaster.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Code Date update has been completed.
	 * @param prodId
	 */
	private void logSaveCodeDateChangesCompleted(Long prodId) {
		CodeDateController.logger.info(String.format(CodeDateController.CODE_DATE_LOG_COMPLETED, prodId));
	}

	/**
	 * Logs a save code date change.
	 * @param remoteAddr
	 * @param prodId
	 */
	private void logSaveCodeDateChanges(String remoteAddr, Long prodId) {
		CodeDateController.logger.info(String.format(CodeDateController.CODE_DATE_LOG,
				this.userInfo.getUserId(), remoteAddr, prodId));
	}

	/**
	 * Retrieves Code Date audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of Code Date audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = CodeDateController.GET_CODE_DATE_AUDITS)
	public List<AuditRecord> getCodeDateAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetCodeDateAuditInformation(request.getRemoteAddr(), prodId);
		List<AuditRecord> codeDateAuditRecords = this.service.getCodeDateAuditInformation(prodId);
		return codeDateAuditRecords;
	}

	/**
	 * Logs get code date audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetCodeDateAuditInformation(String ip, Long prodId) {
		CodeDateController.logger.info(
				String.format(CodeDateController.LOG_CODE_DATE_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}
}
