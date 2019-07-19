/*
 *  TierPricingController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductRelationship;
import com.heb.pm.entity.TierPricing;
import com.heb.pm.entity.TierPricingUpdates;
import com.heb.pm.productDetails.product.SpecialAttributes.PharmacyController;
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
 * This is the controller for tier pricing.
 *
 * @author l730832
 * @since 2.13.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + TierPricingController.ONLINE_ATTRIBUTES)
@AuthorizedResource(ResourceConstants.ONLINE_ATTRIBUTES_TIER_PRICING)
public class TierPricingController {

	private static final Logger logger = LoggerFactory.getLogger(TierPricingController.class);

	protected static final String ONLINE_ATTRIBUTES = "/onlineAttributes";
	protected static final String SAVE_TIER_PRICING_CHANGES="/saveTierPricingChanges";
	protected static final String GET_TIER_PRICING = "/getTierPricing";
	private static final String GET_TIER_PRICING_AUDITS = "/getTierPricingAudits";

	private static final String TIER_PRICING_UPDATE_REQUEST="User %s from address %s has requested to update product with id %s Tier Pricing List ";
	private static final String TIER_PRICING_UPDATE_COMPLETION="User %s from address %s has completed an update product with id %s Tier Pricing List ";
	private static final String UPDATE_SUCCESS_MESSAGE ="OnlineAttributesTierPricingController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Tier Pricing List for product: %d updated successfully.";
	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TierPricingService service;
	/**
	 * Saves any tier pricing changes.
	 * @param tierPricingUpdates the list of additions and deletions to the tier pricing units
	 * @param request
	 * @return an updated product Master
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SAVE_TIER_PRICING_CHANGES)
	public ModifiedEntity<ProductMaster> saveTierPricingChanges(@RequestBody TierPricingUpdates tierPricingUpdates, HttpServletRequest request) {
		TierPricingController.logger.info(String.format(TIER_PRICING_UPDATE_REQUEST, this.userInfo.getUserId(), request.getRemoteAddr(), tierPricingUpdates.getProdId()));
		ProductMaster updatedProductMaster = this.service.saveTierPricingChanges(tierPricingUpdates, this.userInfo.getUserId());
		TierPricingController.logger.info(String.format(TIER_PRICING_UPDATE_COMPLETION, this.userInfo.getUserId(), request.getRemoteAddr(), tierPricingUpdates.getProdId()));

		String updateMessage = this.messageSource.getMessage(
				TierPricingController.UPDATE_SUCCESS_MESSAGE,
				new Object[]{tierPricingUpdates.getProdId()}, String.format(TierPricingController.DEFAULT_UPDATE_SUCCESS_MESSAGE, tierPricingUpdates.getProdId()), request.getLocale());

		return new ModifiedEntity<>(updatedProductMaster, updateMessage);
	}

	/**
	 * Gets tier pricing.
	 *
	 * @param prodId  the prod id
	 * @param request the request
	 * @return the list of tier pricing
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = TierPricingController.GET_TIER_PRICING)
	public List<TierPricing> getTierPricing(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		List<TierPricing> tierPricing = this.service.getTierPricing(prodId);
		return tierPricing;
	}

	/**
	 * Retrieves tier pricing audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of tier pricing audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = TierPricingController.GET_TIER_PRICING_AUDITS)
	public List<AuditRecord> getTierPricingAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		List<AuditRecord> tierPricingAuditRecords = this.service.getTierPricingAuditInformation(prodId);
		return tierPricingAuditRecords;
	}
}
