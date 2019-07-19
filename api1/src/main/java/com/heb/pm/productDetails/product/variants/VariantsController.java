/*
 *
 * VariantsController.java
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */
package com.heb.pm.productDetails.product.variants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.product.ProductInfoController;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.productDetails.product.OnlineAttributes.RelatedProductsController;
import com.heb.pm.productDetails.product.OnlineAttributes.RelatedProductsService;
import com.heb.pm.productDetails.product.ProductInformationController;
import com.heb.pm.productDetails.sellingUnit.ImageInfoController;
import com.heb.pm.productGroup.productGroupECommerceView.ProductGroupECommerceViewController;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST endpoint for product variant item.
 *
 * @author vn87351
 * @since 2.16.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + VariantsController.VARIANTS_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_VARIANTS)
public class VariantsController {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);

	/**
	 * The constant VARIANTS_URL.
	 */
	protected static final String VARIANTS_URL = "/variants";
	private static final String GET_IMAGE_URL = "/imageData";
    protected static final String PRIMARY_IMAGE_URL = "/findPrimaryImage";
	protected static final String SAVE_VARIANT_ITEM_DATA = "/saveVariantItemData";
	protected static final String SAVE_RELATED_PRODUCTS = "/updateRelatedProduct";
	protected static final String CREATE_CANDIDATE_WORK_REQUEST_VARIANTS = "/createCandidateWorkRequest";
	protected static final String UPDATE_CANDIDATE_WORK_REQUEST_VARIANTS = "/updateCandidateWorkRequest";
	private static final String ASSIGN_IMAGE_URL = "/assignImage";

    // logs
    private static final String GET_VARIANTS_MESSAGE_LOG = "User %s from IP %s has requested get variants for the following product Ids [%s]";
	private static final String GET_IMAGE_MESSAGE_LOG = "User %s from IP %s has requested get image BY URI [%s]";
	private static final String SAVE_RELATED_PRODUCT_MESSAGE_LOG = "User %s from IP %s has requested Save related product";
	private static final String SAVE_VARIANT_ITEMS_MESSAGE_LOG = "User %s from IP %s has requested Save variants item";
	private static final String CREATE_WORK_REQUEST_MESSAGE_LOG = "User %s from IP %s has requested to create work request for the following product Id [%s]";
	private static final String UPDATE_WORK_REQUEST_MESSAGE_LOG = "User %s from IP %s has requested to update work request for the following work Request Id [%s]";
    private static final String PRIMARY_IMAGE_MESSAGE_LOG = "User %s from IP %s has requested get primary image for the following upc [%s]";
	private static final String ASSIGN_IMAGE_MESSAGE_LOG = "User %s from IP %s has requested assign images to variants product";

	//key to get list of upcs and list of images from client.
	private static final String UPCS_KEY = "upcs";
	private static final String IMAGES_KEY = "images";

    @Autowired private VariantsService variantsService;
    @Autowired private UserInfo userInfo;
    @Autowired
	private LazyObjectResolver<ProductMaster> productVariantResolver;
    @Autowired
	private RelatedProductsService relatedProductsService;
	private LazyObjectResolver<ProductScanImageURI> resolver = new ImageInfoResolver();
	/**
	 * Find variants by product id.
	 *
	 * @param productId    the product ids
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "/findVariantsData")
    public List<ProductRelationship> findVariantsByProductIds(@RequestParam(value = "productId") Long productId,HttpServletRequest request){
        this.logFindVariantsByProductId(userInfo.getUserId(),request.getRemoteAddr(), productId);
		List<ProductRelationship> productRelationshipList=variantsService.getVariantsData(productId);
		productRelationshipList.forEach(productRelationship -> {
			this.productVariantResolver.fetch(productRelationship.getRelatedProduct());
			this.productVariantResolver.fetch(productRelationship.getParentProduct());
		});
		return productRelationshipList;
    }
	/**
	 *
	 * Request all images associated with a products upc
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = VariantsController.PRIMARY_IMAGE_URL)
	public ModifiedEntity<List<ProductScanImageURI>> getImageByUpcs(@RequestBody List<Long> upcs, HttpServletRequest request){
		this.logFindPrimaryImageByUpcs(userInfo.getUserId(),request.getRemoteAddr(), upcs);
		ModifiedEntity<List<ProductScanImageURI>> results=this.variantsService.getPrimaryImage(upcs, null);
		return  results;
	}
	/**
	 * Request to get the byte[] representation of the image
	 * @param request the request
	 * @return the byte[]
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = VariantsController.GET_IMAGE_URL)
	public ModifiedEntity<byte[]> findImageByUri(@RequestParam(value = "imageUri", required = false) String imageUri,
												 HttpServletRequest request){
		//show log message when init method
		VariantsController.logger.info(String.format(VariantsController.GET_IMAGE_MESSAGE_LOG, this.userInfo.getUserId(),
				request.getRemoteAddr(), imageUri));
		byte[] image =this.variantsService.findImageByUri(imageUri);
		return new ModifiedEntity<>(image, imageUri);
	}

	/**
	 *  Request to save item variant
	 * @param lstVariant list variant item
	 * @param request
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = VariantsController.SAVE_VARIANT_ITEM_DATA)
	public void saveVariantItem(@RequestBody List<ProductItemVariant> lstVariant,HttpServletRequest request){
		//show log message when init method
		VariantsController.logger.info(String.format(VariantsController.SAVE_VARIANT_ITEMS_MESSAGE_LOG, this.userInfo.getUserId(),
				request.getRemoteAddr()));
		this.variantsService.saveVariantItem(lstVariant);
	}

	/**
	 * Request to create candidate work request for variant
	 * @param productId product id
	 * @param request the request
	 * @return candidate work request
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET, value = VariantsController.CREATE_CANDIDATE_WORK_REQUEST_VARIANTS)
	public CandidateWorkRequest createCandidateWorkRequestVariants(@RequestParam (value = "productId") long productId, HttpServletRequest request){
		//show log message when init method
		VariantsController.logger.info(String.format(VariantsController.CREATE_WORK_REQUEST_MESSAGE_LOG, this.userInfo.getUserId(),
				request.getRemoteAddr(),productId));
		return this.variantsService.createCandidateWorkRequest(productId,this.userInfo.getUserId());
	}

	/**
	 * Request to update candidate work request
	 * @param candidateWorkRequest
	 * @param request
	 * @return
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = VariantsController.UPDATE_CANDIDATE_WORK_REQUEST_VARIANTS)
	public CandidateWorkRequest updateCandidateWorkRequestVariants(@RequestBody CandidateWorkRequest candidateWorkRequest, HttpServletRequest request){
		//show log message when init method
		VariantsController.logger.info(String.format(VariantsController.UPDATE_WORK_REQUEST_MESSAGE_LOG, this.userInfo.getUserId(),
				request.getRemoteAddr(),candidateWorkRequest.getWorkRequestId()));
		return this.variantsService.updateCandidateWorkRequest(candidateWorkRequest,this.userInfo.getUserId());
	}
	/**
	 * Save product relationships updates and deletes
	 *
	 * @param relatedProducts  the related products to be deleted
	 * @param request the request
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = VariantsController.SAVE_RELATED_PRODUCTS)
	public void saveRelatedProducts(@RequestBody List<ProductRelationship> relatedProducts, HttpServletRequest request) {
		//show log message when init method
		VariantsController.logger.info(String.format(VariantsController.SAVE_RELATED_PRODUCT_MESSAGE_LOG, this.userInfo.getUserId(),
				request.getRemoteAddr()));
		this.relatedProductsService.saveRelatedProducts(relatedProducts);
	}
	/**
	 * Log's a user's request to get variants for a product Id.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param prodId The item codes the user is searching for.
	 */
	private void logFindVariantsByProductId(String ip,String user, Long prodId){
		logger.info(String.format(VariantsController.GET_VARIANTS_MESSAGE_LOG, this.userInfo.getUserId(), ip, prodId));
	}
	/**
	 * Log's a user's request to get primary image.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param lstUpcs The item codes the user is searching for.
	 */
	private void logFindPrimaryImageByUpcs(String ip,String user, List<Long> lstUpcs){
		logger.info(String.format(VariantsController.PRIMARY_IMAGE_MESSAGE_LOG, this.userInfo.getUserId(), ip, lstUpcs));
	}

	/**
	 * Assign Image(s) to Variant Product(s).
	 *
	 * @param dataInputMap    contain list of upcs and list of images.
	 * @param request       the http request
	 * @return return message.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ASSIGN_IMAGE_URL)
	public String assignImages(@RequestBody String dataInputMap, HttpServletRequest request) throws IOException, JSONException {
		this.logAssignImage(userInfo.getUserId(),request.getRemoteAddr());
		Map<String,List> map;
		ObjectMapper mapper = new ObjectMapper();
		map = mapper.readValue(dataInputMap, HashMap.class);
		List<Long> upcs = mapper.convertValue(map.get(UPCS_KEY), new TypeReference<List<Long>>(){});
		List<ProductScanImageURI> images = mapper.convertValue(map.get(IMAGES_KEY), new TypeReference<List<ProductScanImageURI>>(){});
		return this.variantsService.assignImages(images,upcs,this.userInfo.getUserId()).toString();
	}

	/**
	 * Log's a user's request to assign images for variants product.
	 *
	 * @param userId The IP address th user is logged in from.
	 * @param ipAdress The userId of user has request assign images.
	 */
	private void logAssignImage(String userId,String ipAdress){
		logger.info(String.format(VariantsController.ASSIGN_IMAGE_MESSAGE_LOG, userId, ipAdress));
	}
}
