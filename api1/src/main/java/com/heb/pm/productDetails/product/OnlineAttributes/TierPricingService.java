/*
 *  TierPricingService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.TierPricing;
import com.heb.pm.entity.TierPricingAudit;
import com.heb.pm.entity.TierPricingUpdates;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.repository.ProductInfoRepository;
import com.heb.pm.repository.TierPricingAuditRepository;
import com.heb.pm.repository.TierPricingRepository;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import com.heb.util.audit.AuditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This holds all of the business logic for tier pricing.
 *
 * @author l730832
 * @since 2.13.0
 */
@Service
public class TierPricingService {

	private String INSERT_CODE = "";
	private String DELETE_CODE="D";

	@Autowired
	private ProductInfoRepository productInfoRepository;

	@Autowired
	private ProductInfoResolver productInfoResolver = new ProductInfoResolver();

	@Autowired
	private ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Autowired
	private TierPricingRepository tierPricingRepository;

	@Autowired
	private TierPricingAuditRepository tierPricingAuditRepository;

	@Autowired
	@Qualifier("tierPricingAuditImpl")
	private TierPricingAuditImpl tierPricingAuditImpl;



	/**
	 * Saves any changes that happened to the tier pricing tab on online attributes.
	 * @param tierPricingUpdates the tier pricing changes to be updated.
	 * @return returns an updated product master.
	 */
	public ProductMaster saveTierPricingChanges(TierPricingUpdates tierPricingUpdates, String userId) {
		for (TierPricing tierPricing: tierPricingUpdates.getTierPricingsAdded()) {
			this.productAttributeManagementServiceClient.updateTierPricingUnit(tierPricing, userId, INSERT_CODE);
		}
		for (TierPricing tierPricing: tierPricingUpdates.getTierPricingsRemoved()) {
			this.productAttributeManagementServiceClient.updateTierPricingUnit(tierPricing, userId, DELETE_CODE);
		}
		ProductMaster updatedProductMaster = this.productInfoRepository.findOne(tierPricingUpdates.getProdId());
		this.productInfoResolver.fetch(updatedProductMaster);
		return updatedProductMaster;
	}

	/**
	 * This method will get the list of Related products
	 *
	 * @param prodId the product identification number
	 * @return
	 */
	public List<TierPricing> getTierPricing(Long prodId) {
		List<TierPricing> tierPricing = this.tierPricingRepository.findByKeyProdId(prodId);
		return tierPricing;
	}

	/**
	 * Get all records of changes to the tier pricing attributes
	 * @param parentProdId the product ID to get changes on.
	 * @return
	 */
	public List<AuditRecord> getTierPricingAuditInformation(Long parentProdId) {
		List<AuditRecord> result = new ArrayList<>();
		List<TierPricingAudit> tierPricingAudits =
				this.tierPricingAuditRepository.findByKeyProdIdOrderByKeyChangedOn(parentProdId);

		Map<LocalDateTime, List<TierPricingAudit>> mapTierPricingAudits = tierPricingAudits.stream()
				.collect(Collectors.groupingBy(s->s
						.getCreateDate()));
		result = this.tierPricingAuditImpl.processClassFromListTierPricingAudits(mapTierPricingAudits);

		// Sort result list
		result.sort(Comparator.comparing(AuditRecord::getChangedOn));

		return result;
	}

}
