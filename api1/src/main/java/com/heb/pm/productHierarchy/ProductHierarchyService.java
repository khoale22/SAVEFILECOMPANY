/*
 * ProductHierarchyService
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.entity.MerchantType;
import com.heb.pm.entity.PDPTemplate;
import com.heb.pm.entity.ProductTemperatureControl;
import com.heb.pm.repository.MerchantTypeRepository;
import com.heb.pm.repository.PDPTemplateRepository;
import com.heb.pm.repository.ProductTemperatureControlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related for product hierarchy.
 *
 * @author m314029
 * @since 2.4.0
 */
@Service
public class ProductHierarchyService {

	@Autowired
	private MerchantTypeRepository merchantTypeRepository;

	@Autowired
	private ProductTemperatureControlRepository productTemperatureControlRepository;

	@Autowired
	private PDPTemplateRepository pdpTemplateRepository;

	/**
	 * Get all merchant types.
	 *
	 * @return list with all merchant types.
	 */
	public List<MerchantType> getAllMerchantTypes() {
		return this.merchantTypeRepository.findAll();
	}

	/**
	 * Get all product temperature controls.
	 *
	 * @return the list with all temperature controls.
	 */
	public List<ProductTemperatureControl> getAllProductTemperatureControls() {
		return this.productTemperatureControlRepository.findAll();
	}

	/**
	 * Get all PDP templates.
	 *
	 * @return the list with all PDPTemplates.
	 */
	public List<PDPTemplate> getAllPDPTemplates() {
		return this.pdpTemplateRepository.findAll();
	}
}
