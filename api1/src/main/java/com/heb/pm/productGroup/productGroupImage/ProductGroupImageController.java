/*
 *  ProductGroupImageController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup.productGroupImage;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * REST endpoint for get product group image.
 *
 * @author vn75469
 * @since 2.15.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductGroupImageController.PRODUCT_GROUP_IMAGE_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_GROUP_SEARCH)
public class ProductGroupImageController {
	@Autowired
	private ProductGroupImageService productGroupImageService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Url of backend controller.
	 */
	protected static final String PRODUCT_GROUP_IMAGE_URL = "/productGroup/productGroupImage";
	private static final Logger logger = LoggerFactory.getLogger(ProductGroupImageController.class);
	/* Log messages.*/
	private static final String FIND_IMAGES_OF_PRODUCT_GROUP_MSG = "User %s from IP %s requested all images of product group %s";
	private static final String UPDATE_IMAGE_METADATA_MESSAGE = "User %s from IP %s requested update image metadata";
	private static final String UPLOAD_IMAGE_MESSAGE = "User %s from IP %s requested upload product group image";
	//value of url
	private static final String FIND_IMAGES_OF_PRODUCT_GROUP_URL = "/findAllProductGroupImages";
	private static final String UPDATE_IMAGE_METADATA = "/update";
	private static final String UPLOAD_IMAGE="/upload";
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
	private static final String UPLOAD_SUCCESS_MESSAGE = "Successfully Uploaded.";

	/**
	 * Get all image metadata.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @param productGroupId The product group id to find image metadata.
	 * @return the list of image metadata.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductGroupImageController.FIND_IMAGES_OF_PRODUCT_GROUP_URL )
	public List<ImageMetaData> findImageMetadataByChoiceOpt(@RequestParam (value = "productGroupId",required = true) String productGroupId, HttpServletRequest request) {
		//show log message when init method
		logger.info(String.format(ProductGroupImageController.FIND_IMAGES_OF_PRODUCT_GROUP_MSG, this.userInfo.getUserId(), request.getRemoteAddr(), productGroupId));
		return productGroupImageService.findImageMetadataByProductGroupId(Long.parseLong(productGroupId));
	}

	/**
	 * Call service to update one or more image metadata.
	 *
	 * @param listImageMetadata The list of product group images to update.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of image metadata after update image metadata and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductGroupImageController.UPDATE_IMAGE_METADATA)
	public ModifiedEntity<List<ImageMetaData>> updateImageMetadata(
			@RequestBody List<ImageMetaData> listImageMetadata, HttpServletRequest request) {
		//show log message when init method
		logger.info(String.format(ProductGroupImageController.UPDATE_IMAGE_METADATA_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		//call handle from service
		this.productGroupImageService.updateImageMetadata(listImageMetadata,this.userInfo.getUserId());
		//get new data after update product category successfully
		List<ImageMetaData> images = this.productGroupImageService.findImageMetadataByProductGroupId(listImageMetadata.get(0).getKey().getId());
		return new ModifiedEntity<>(images, UPDATE_SUCCESS_MESSAGE);
	}

	/**
	 * Upload product group image.
	 *
	 * @param fileUpload The file to upload.
	 * @param productGroupCode The product group code of product group image upload.
	 * @param imgCategoryCode The imgCategoryCode of product group image upload.
	 * @param imgSourceCode The imgSourceCode of product group image upload.
	 * @param request The HTTP request that initiated this call.
	 * @return A object of ImageToUpload.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductGroupImageController.UPLOAD_IMAGE)
	public ModifiedEntity<List<ImageMetaData>> uploadImage(@RequestParam("fileUpload") MultipartFile fileUpload, @RequestParam("productGroupCode") String productGroupCode,
																 @RequestParam("imgCategoryCode") String imgCategoryCode , @RequestParam("imgSourceCode") String imgSourceCode, HttpServletRequest request) throws IOException,CheckedSoapException {
		//show log message when init method
		logger.info(String.format(ProductGroupImageController.UPLOAD_IMAGE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		ImageToUpload productGroupImage = new ImageToUpload();
		productGroupImage.setUserId(this.userInfo.getUserId());
		productGroupImage.setKeyId(productGroupCode);
		productGroupImage.setKeyType(ImageMetaData.IMAGE_PRODUCT_GROUP);
		productGroupImage.setImageName(fileUpload.getOriginalFilename());
		productGroupImage.setImageCategoryCode(imgCategoryCode);
		productGroupImage.setImageSourceCode(imgSourceCode);
		productGroupImage.setImageData(fileUpload.getBytes());
		//return all data from service
		this.productGroupImageService.uploadImage(productGroupImage);
		//get new data after update product category successfully
		List<ImageMetaData> images = this.productGroupImageService.findImageMetadataByProductGroupId(Long.parseLong(productGroupCode));
		return new ModifiedEntity<>(images,UPLOAD_SUCCESS_MESSAGE);
	}
}
