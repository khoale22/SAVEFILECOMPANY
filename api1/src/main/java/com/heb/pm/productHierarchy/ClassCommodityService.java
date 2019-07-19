/*
 * ClassCommodityService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.productHierarchy;

import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ClassCommodity;
import com.heb.pm.entity.ClassCommodityKey;
import com.heb.pm.index.DocumentWrapperUtil;
import com.heb.pm.repository.ClassCommodityRepository;
import com.heb.pm.repository.CommodityIndexRepository;
import com.heb.pm.user.UserService;
import com.heb.pm.ws.ProductHierarchyManagementServiceClient;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.ws.SoapException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business functions related to Class/Commodity.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
public class ClassCommodityService {

	private static final Logger logger = LoggerFactory.getLogger(ClassCommodityService.class);

	private static final String CLASS_COMMODITY_REGULAR_EXPRESSION = "*%s*";
	private static final String COMMODITY_SEARCH_LOG_MESSAGE =
			"searching for commodities by the regular expression '%s'";
	private static final Integer COMMODITY_CODE_FOR_CLASS = 0;
	private static final String USER_EDITABLE_COMMODITY_LOG_MESSAGE =
			"User %s is authorized to update commodity: %s.";
	private static final String ERROR_UPDATE_ABDM = "ABDM: '%s' value is not a valid one pass Id.";
	private static final String ERROR_UPDATE_EBM = "eBM: '%s' value is not a valid one pass Id.";

	@Autowired
	private CommodityIndexRepository commodityIndexRepository;

	@Autowired
	private ClassCommodityRepository commodityRepository;

	@Autowired
	private ProductHierarchyUtils productHierarchyUtils;

	@Autowired
	private ProductHierarchyManagementServiceClient productHierarchyManagementServiceClient;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private UserService userService;

	/**
	 * Searches for a list of classes by a regular expression. This is a wildcard search, meaning that
	 * anything partially matching the text passed in will be returned.
	 *
	 * @param searchString The text to search for classes by.
	 * @param page The page to look for.
	 * @param pageSize The maximum size for the page.
	 * @return A PageableResult with classes matching the search criteria.
	 */
	public PageableResult<ClassCommodity> findCommoditiesByRegularExpression(String searchString,
																			 int page, int pageSize) {

		String regexString = String.format(ClassCommodityService.CLASS_COMMODITY_REGULAR_EXPRESSION,
				searchString.toUpperCase());

		ClassCommodityService.logger.debug(String.format(ClassCommodityService.COMMODITY_SEARCH_LOG_MESSAGE,
				regexString));

		Page<CommodityDocument> ccp = this.commodityIndexRepository.findByRegularExpression(regexString,
				new PageRequest(page, pageSize));

		List<ClassCommodity> classCommodities = new ArrayList<>(ccp.getSize());
		DocumentWrapperUtil.toDataCollection(ccp, classCommodities);

		return new PageableResult<>(page, ccp.getTotalPages(), ccp.getTotalElements(), classCommodities);
	}

	/**
	 * Searches for a commodity by it's commodity ID (pd_com_com_cd). Class is ignored.
	 *
	 * @param commodityId The ID of the commodity to search for.
	 * @return A commodity with ID requested. WIll return null if not found.
	 */
	public ClassCommodity findCommodity(int commodityId) {

		CommodityDocument cd = this.commodityIndexRepository.findOne(Integer.toString(commodityId));
		return DocumentWrapperUtil.toData(cd);
	}

	/**
	 * Sets the CommodityIndexRepository for this object to use. This is used for testing.
	 *
	 * @param commodityIndexRepository The CommodityIndexRepository for this object to use
	 */
	public void setCommodityIndexRepository(CommodityIndexRepository commodityIndexRepository) {
		this.commodityIndexRepository = commodityIndexRepository;
	}

	/**
	 * Finds commodities by page.
	 *
	 * @param pageRequest the page request
	 * @return page of class-commodity defined by page request.
	 */
	public Page<ClassCommodity> findAllCommoditiesByPage(PageRequest pageRequest) {
		Page<ClassCommodity> commodityList =
				this.commodityRepository.findAllByKeyCommodityCodeNot(COMMODITY_CODE_FOR_CLASS, pageRequest);
		for(ClassCommodity commodity : commodityList){
			commodity.getItemClassMaster().getItemClassCode();
			this.productHierarchyUtils.extrapolateSubDepartmentOfItemClass(
					commodity.getItemClassMaster());
		}
		return commodityList;
	}

	/**
	 * Finds commodities by page for commodity index (not resolved).
	 *
	 * @param pageRequest the page request
	 * @return page of class-commodity defined by page request.
	 */
	public Page<ClassCommodity> findAllCommoditiesForIndexByPage(PageRequest pageRequest) {
		return this.commodityRepository.findAllByKeyCommodityCodeNot(COMMODITY_CODE_FOR_CLASS, pageRequest);
	}

	/**
	 * This method calls the product hierarchy management service client to update a commodity.
	 *
	 * @param commodity Commodity to update.
	 */
	public void update(ClassCommodity commodity) {
		try {
			if(commodity.geteBMid() == null || StringUtils.EMPTY.equals(commodity.geteBMid()) || this.userService.getUserById(StringUtils.trim(commodity.geteBMid()))!= null){
				if(commodity.getbDAid() == null || StringUtils.EMPTY.equals(commodity.getbDAid()) || this.userService.getUserById(StringUtils.trim(commodity.getbDAid()))!= null){
					this.productHierarchyManagementServiceClient.updateCommodity(this.getUserEditableCommodity(commodity));
				}else{
					throw new SoapException(String.format(
							ERROR_UPDATE_ABDM,  StringUtils.trim(commodity.getbDAid())));
				}
			}else{
				throw new SoapException(String.format(
						ERROR_UPDATE_EBM,  StringUtils.trim(commodity.geteBMid())));
			}
		} catch (Exception e){
			throw  new SoapException(e.getMessage());
		}
	}

	/**
	 * Searches for a specific class-commodity by key through the commodity repository.
	 *
	 * @param key The key of the class-commodity searched for.
	 * @return A class-commodity with the key requested. Will return null if not found.
	 */
	public ClassCommodity findOne(ClassCommodityKey key) {
		return this.commodityRepository.findOne(key);
	}

	/**
	 * This method takes in the updatable commodity from the front end, and returns the commodity with the fields
	 * the user has access to edit from that updatable commodity, so only the fields the user has access to edit will
	 * be sent to the webservice for updating.
	 *
	 * @param commodity Commodity to update sent from the front end.
	 * @return Commodity the user has editable access for.
	 */
	private ClassCommodity getUserEditableCommodity(ClassCommodity commodity) {
		ClassCommodity userEditableCommodity = new ClassCommodity();
		userEditableCommodity.setKey(new ClassCommodityKey(commodity.getKey()));
		if(commodity.getName() != null && !StringUtils.EMPTY.equals(commodity.getName().trim()) &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_COMMODITY_DESCRIPTION)) {
			userEditableCommodity.setName(commodity.getName());
		}
		if(commodity.getPssDepartment() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_COMMODITY_PSS_DEPARTMENT)) {
			userEditableCommodity.setPssDepartment(commodity.getPssDepartment());
		}
		if(commodity.getBdmCode() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_COMMODITY_BDM_ID)) {
			userEditableCommodity.setBdmCode(commodity.getBdmCode());
		}
		if(commodity.getbDAid() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_COMMODITY_BDA)) {
			userEditableCommodity.setbDAid(commodity.getbDAid());
		}
		if(commodity.geteBMid() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_COMMODITY_EBM)) {
			userEditableCommodity.seteBMid(commodity.geteBMid());
		}
		if(commodity.getClassCommodityActive() != null &&
				this.userInfo.canUserEditResource(ResourceConstants.PRODUCT_HIERARCHY_COMMODITY_ACTIVE)) {
			userEditableCommodity.setClassCommodityActive(commodity.getClassCommodityActive());
		}
		this.logUserEditableCommodity(userEditableCommodity);
		return userEditableCommodity;
	}

	/**
	 * Logger for the commodity the user has permissions to edit.
	 *
	 * @param userEditableCommodity The commodity the user has edit permissions for.
	 */
	private void logUserEditableCommodity(ClassCommodity userEditableCommodity) {
		ClassCommodityService.logger.info(
				String.format(ClassCommodityService.USER_EDITABLE_COMMODITY_LOG_MESSAGE,
						this.userInfo.getUserId(), userEditableCommodity.toString())
		);
	}
}