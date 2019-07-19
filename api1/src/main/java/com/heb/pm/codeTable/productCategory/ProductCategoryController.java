/*
 * ProductCategoryController
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

package com.heb.pm.codeTable.productCategory;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.MarketConsumerEventType;
import com.heb.pm.entity.ProductCategory;
import com.heb.pm.entity.ProductCategoryRole;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that returns all information related to product category.
 *
 * @author vn70529
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductCategoryController.PRODUCT_CATEGORY_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_CATEGORY)
public class ProductCategoryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryController.class);

	/**
	 * Holds information of user.
	 */
	@Autowired
	private UserInfo userInfo;

	/**
	 * Holds the business logic around product category.
	 */
	@Autowired
	private ProductCategoryService productCategoryService;

	protected static final String PRODUCT_CATEGORY_URL = "/codeTable/productCategory";
	private static final String GET_ALL_PRODUCT_CATEGORY_URL = "/allProductCategories";
	private static final String GET_ALL_CONSUMER_MARKET_EVENT_TYPE_URL = "/allMarketConsumerEventTypes";
	private static final String GET_ALL_PRODUCT_CATEGORY_ROLE_URL = "/allProductCategoryRoles";
	private static final String ADD_PRODUCT_CATEGORY_URL = "/addProductCategory";
	private static final String UPDATE_PRODUCT_CATEGORY_URL = "/updateProductCategory";
	private static final String DELETE_PRODUCT_CATEGORY_URL = "/deleteProductCategory";

	/**
	 * Message log when called method
	 */
	private static final String GET_ALL_PRODUCT_CATEGORY_MESSAGE =
			"User %s from IP %s has requested all product category data.";
	private static final String GET_ALL_MARKET_CONSUMER_EVENT_TYPE_MESSAGE =
			"User %s from IP %s has requested all consumer market event type.";
	private static final String GET_ALL_PRODUCT_CATEGORY_ROLE_MESSAGE =
			"User %s from IP %s has requested all product category role.";
	private static final String ADD_PRODUCT_CATEGORY_MESSAGE =
			"User %s from IP %s requested add new the list of product category";
	private static final String UPDATE_PRODUCT_CATEGORY_MESSAGE =
			"User %s from IP %s requested update the list of product category";
	private static final String DELETE_PRODUCT_CATEGORY_MESSAGE =
			"User %s from IP %s requested delete the list of product category";
	private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	private static final String ADD_SUCCESS_MESSAGE = "Successfully Added.";

	/**
	 * Get all product categories list.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return the list of product category.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductCategoryController.GET_ALL_PRODUCT_CATEGORY_URL)
	public List<ProductCategory> findAllProductCategories(HttpServletRequest request) {
		//show log message when init method
		this.logMessage(ProductCategoryController.GET_ALL_PRODUCT_CATEGORY_MESSAGE, request.getRemoteAddr());
		//call handle from service
		return this.productCategoryService.findAllProductCategories();
	}

	/**
	 * Get all market consumer event types.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return the market consumer event type.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductCategoryController.GET_ALL_CONSUMER_MARKET_EVENT_TYPE_URL)
	public List<MarketConsumerEventType> findAllMarketConsumerEventTypes(HttpServletRequest request) {
		//show log message when init method
		this.logMessage(ProductCategoryController.GET_ALL_MARKET_CONSUMER_EVENT_TYPE_MESSAGE,
				request.getRemoteAddr());
		//call service
		return this.productCategoryService.findAllMarketConsumerEventTypes();
	}

	/**
	 * Get all product category roles.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return the list of product category roles
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductCategoryController.GET_ALL_PRODUCT_CATEGORY_ROLE_URL)
	public List<ProductCategoryRole> findAllProductCategoryRoles(HttpServletRequest request) {
		//show log message when init method
		this.logMessage(ProductCategoryController.GET_ALL_PRODUCT_CATEGORY_ROLE_MESSAGE, request.getRemoteAddr());
		//call service
		return this.productCategoryService.findAllProductCategoryRoles();
	}

	/**
	 * Call service to add new Product Category.
	 *
	 * @param listOfProductCategory The list of product category to add new.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of product category after add new and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductCategoryController.ADD_PRODUCT_CATEGORY_URL)
	public ModifiedEntity<List<ProductCategory>> addProductCategory(
			@RequestBody List<ProductCategory> listOfProductCategory, HttpServletRequest request) {
		//show log message when init method
		this.logMessage(ProductCategoryController.ADD_PRODUCT_CATEGORY_MESSAGE, request.getRemoteAddr());
		//call service
		this.productCategoryService.addProductCategories(listOfProductCategory);
		//get new data after add new product category successfully
		List<ProductCategory> productCategories = this.productCategoryService.findAllProductCategories();
		return new ModifiedEntity<>(productCategories, ADD_SUCCESS_MESSAGE);
	}

	/**
	 * Call service to update Product Category.
	 *
	 * @param listOfProductCategory The list of product category to update.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of product category after update and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductCategoryController.UPDATE_PRODUCT_CATEGORY_URL)
	public ModifiedEntity<List<ProductCategory>> updateProductCategory(
			@RequestBody List<ProductCategory> listOfProductCategory, HttpServletRequest request) {
		//show log message when init method
		this.logMessage(ProductCategoryController.UPDATE_PRODUCT_CATEGORY_MESSAGE, request.getRemoteAddr());
		//call handle from service
		this.productCategoryService.updateProductCategories(listOfProductCategory);
		//get new data after update product category successfully
		List<ProductCategory> productCategories = this.productCategoryService.findAllProductCategories();
		return new ModifiedEntity<>(productCategories, UPDATE_SUCCESS_MESSAGE);
	}

	/**
	 * Call service to delete Product Category.
	 *
	 * @param listOfProductCategory The list of product category to delete.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of product category after delete and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductCategoryController.DELETE_PRODUCT_CATEGORY_URL)
	public ModifiedEntity<List<ProductCategory>> deleteProductCategory(
			@RequestBody List<ProductCategory> listOfProductCategory, HttpServletRequest request) {
		//show log message when init method
		this.logMessage(ProductCategoryController.DELETE_PRODUCT_CATEGORY_MESSAGE, request.getRemoteAddr());
		//call handle from service
		this.productCategoryService.deleteProductCategories(listOfProductCategory);
		//get new data after delete product category successfully
		List<ProductCategory> productCategories = this.productCategoryService.findAllProductCategories();
		return new ModifiedEntity<>(productCategories, DELETE_SUCCESS_MESSAGE);
	}

	/**
	 * Logs a user's request.
	 *
	 * @param message specific method name has called.
	 * @param ip address of the client request.
	 */
	private void logMessage(String message, String ip){
		ProductCategoryController.LOGGER.info(
				String.format(message,
						this.userInfo.getUserId(), ip));
	}
}