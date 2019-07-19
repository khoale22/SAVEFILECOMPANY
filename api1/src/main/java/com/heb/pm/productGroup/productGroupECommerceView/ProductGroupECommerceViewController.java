/*
 *  ProductGroupECommerceViewController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup.productGroupECommerceView;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.productGroupType.ProductGroupTypeResolver;
import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.productGroup.productGroupInfo.ProductGroupInfo;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.heb.pm.entity.CustomerProductGroup;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * REST endpoint for get product group e-commerce view.
 *
 * @author vn87351
 * @since 2.15.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL+ "/productGroup")
@AuthorizedResource(ResourceConstants.PRODUCT_GROUP_SEARCH)
public class ProductGroupECommerceViewController {
	public static final Logger logger = LoggerFactory.getLogger(ProductGroupECommerceViewController.class);
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";

	// urls
	private static final String PRODUCT_GROUP_GET_IMAGE_FOR_PRODUCT_URL = "/productImages";
	private static final String PRODUCT_GROUP_ECOMMERCE_VIEW_URL = "/ecommerceView";
	private static final String URL_GET_IMAGE = "/imageData";
	private static final String SAVE_PRODUCT_GROUP_URL = "/updateProductGroup";
	private static final String DELETE_PRODUCT_GROUP = "/deleteProductGroupEcom";
	//logs
	private static final String PRODUCT_GROUP_GET_IMAGE_FOR_PRODUCT_URL_LOG_MESSAGE = "User %s from IP %s has requested" +
			" get images data for product id =%s";
	private static final String PRODUCT_GROUP_ECOMMERCE_VIEW_LOG_MESSAGE = "User %s from IP %s has requested get product" +
			" group e-commerce view data for product group id =%s and sales channel =%s and hierCntxtCd=%s";
	private static final String GET_IMAGE_DATA_LOG_MESSAGE =
			"User %s from IP %s requested to get image data by uri=%s.";
	private static final String SAVE_PRODUCT_GROUP_LOG_MESSAGE = "User %s from IP %s has requested save product group";
	private static final String DELETE_PRODUCT_GROUP_LOG_MESSAGE = "User %s from IP %s has requested delete product group";


	/**
	 * inject necessary beans
	 */
	@Autowired
	private ProductGroupECommerceViewService productGroupECommerceViewService;
	@Autowired
	private UserInfo userInfo;

	/**
	 * resolver to fetch data lazy
	 */
	private ProductGroupTypeResolver productGroupTypeResolver = new ProductGroupTypeResolver();

	/**
	 * Endpoint to find product image data by data choice option.
	 *
	 * @param productGroupId The text you are looking for.
	 * @param mapOption map data choice option
	 * @param request        The HTTP request that initiated this call.
	 * @return Product Image data
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductGroupECommerceViewController.PRODUCT_GROUP_GET_IMAGE_FOR_PRODUCT_URL)
	public List<ProductScanImageURI> findProductImageInfoById(
			@RequestParam("mapsOption") String mapOption,
			@RequestParam("productGroupId") Long productGroupId,
			@RequestParam("salesChannel") String salesChannel,
			HttpServletRequest request)throws IOException {
		ProductGroupECommerceViewController.logger.info(String.format(
				ProductGroupECommerceViewController.PRODUCT_GROUP_GET_IMAGE_FOR_PRODUCT_URL_LOG_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(),productGroupId));

		return productGroupECommerceViewService.getImageByProductGroupIdAndChoiceOption(productGroupId,salesChannel,mapOption);
	}

	/**
	 * Request to get the byte[] representation of the image
	 * @param request the request
	 * @return the byte[]
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductGroupECommerceViewController.URL_GET_IMAGE)
	public ModifiedEntity<byte[]> findImageByUri(@RequestParam(value = "imageUri", required = false) String imageUri,
												 HttpServletRequest request){
		//show log message when init method
		ProductGroupECommerceViewController.logger.info(String.format(ProductGroupECommerceViewController.GET_IMAGE_DATA_LOG_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), imageUri));
		byte[] image =this.productGroupECommerceViewService.findImageByUri(imageUri);
		return new ModifiedEntity<>(image, imageUri);
	}

	/**
	 * Endpoint to find product group e-commerce view data.
	 *
	 * @param productGroupId The text you are looking for.
	 * @param request        The HTTP request that initiated this call.
	 * @return A list of commodities whose name or number match the supplied search string.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductGroupECommerceViewController.PRODUCT_GROUP_ECOMMERCE_VIEW_URL)
	public ProductGroupInfo findProductGroupECommerceViewById(
			@RequestParam("productGroupId") Long productGroupId,
			@RequestParam("salesChannel") String salesChannel,
			@RequestParam("hierCntxtCd") String hierCntxtCd,
			HttpServletRequest request) {
		ProductGroupECommerceViewController.logger.info(String.format(
				ProductGroupECommerceViewController.PRODUCT_GROUP_ECOMMERCE_VIEW_LOG_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(),productGroupId,salesChannel,hierCntxtCd));
		ProductGroupInfo productGroupInfo=productGroupECommerceViewService.getDataECommerceView(productGroupId,salesChannel,hierCntxtCd);
		productGroupTypeResolver.fetch(productGroupInfo.getCustomerProductGroup().getProductGroupType());
		return productGroupInfo;
	}
	/**
	 *  Call the service to update product group.
	 *
	 * @param request  The HTTP request that initiated this call.
	 * @return Success message.
	 * @throws Exception
	 * @author vn70633
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductGroupECommerceViewController.SAVE_PRODUCT_GROUP_URL)
	public ModifiedEntity updateProductGroup(@RequestBody CustomerProductGroup customerProductGroup, HttpServletRequest request) throws Exception{
		ProductGroupECommerceViewController.logger.info(String.format(
				ProductGroupECommerceViewController.SAVE_PRODUCT_GROUP_LOG_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr()));
		this.productGroupECommerceViewService.updateProductGroup(customerProductGroup);
		return new ModifiedEntity<>(null, UPDATE_SUCCESS_MESSAGE);
	}
	/**
	 *  Call the service to delete product group.
	 *
	 * @param request  The HTTP request that initiated this call.
	 * @return Success message.
	 * @throws Exception
	 * @author vn70633
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductGroupECommerceViewController.DELETE_PRODUCT_GROUP)
	public ModifiedEntity deleteProductGroup(@RequestBody CustomerProductGroup customerProductGroup, HttpServletRequest request) throws Exception{
		ProductGroupECommerceViewController.logger.info(String.format(
				ProductGroupECommerceViewController.DELETE_PRODUCT_GROUP_LOG_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr()));
		this.productGroupECommerceViewService.deleteProductGroup(customerProductGroup);
		return new ModifiedEntity<>(null, UPDATE_SUCCESS_MESSAGE);
	}
}
