/*
 *  ProductGroupImageService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productGroup.productGroupImage;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.pm.ws.ImageInforServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to product group image.
 *
 * @author vn75469
 * @since 2.15.0
 */
@Service
public class ProductGroupImageService {
	@Autowired
	private ImageMetaDataRepository imageMetaDataRepository;
	@Autowired
	ImageInforServiceClient imageInforServiceClient;
	@Autowired
	ContentManagementServiceClient contentManagementServiceClient;

	/**
	 * Return a list of image metadata is found by product group id, order by product category and create date.
	 *
	 * @return A list of image metadata.
	 */
	public List<ImageMetaData> findImageMetadataByProductGroupId(Long productGroupId) {
		return this.imageMetaDataRepository.findImagesByKeyIdAndImageSubjectTypeCode(productGroupId,ImageMetaData.IMAGE_PRODUCT_GROUP);
	}

	/**
	 * Call webservice to update information of image
	 *
	 * @param listImageMetadata list of images
	 * @param userId userId
	 */
	public void updateImageMetadata(List<ImageMetaData> listImageMetadata,String userId){
		imageInforServiceClient.updateContentMangementDetails(listImageMetadata,userId);
	}

	/**
	 * Call ContentManagementServiceClient to upload choice image.
	 *
	 * @param imageToUpload the ImageToUpload.
	 */
	public void uploadImage(ImageToUpload imageToUpload) throws CheckedSoapException {
		this.contentManagementServiceClient.uploadImage(imageToUpload);
	}
}
