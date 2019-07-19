/*
 *  ChoiceDetailController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.choice;

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
 * Represents code table choice detail information.
 *
 * @author vn86116
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ChoiceDetailController.CODE_TABLE_CHOICE_OPTION_DETAIL_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_CHOICE_OPTION)
public class ChoiceDetailController {
	protected static final String CODE_TABLE_CHOICE_OPTION_DETAIL_URL = "/codeTable/choiceDetail";
	private static final Logger logger = LoggerFactory.getLogger(ChoiceDetailController.class);

	// Log messages.
	private static final String FIND_IMAGE_METADTA_BY_CHOICE_OPT_MESSAGE = "User %s from IP %s requested image metadata by choice option";
	private static final String UPDATE_IMAGE_METADATA_MESSAGE = "User %s from IP %s requested update image metadata";
	private static final String UPLOAD_CHOICE_IMAGE_MESSAGE = "User %s from IP %s requested upload choice image";
	//value of url
	private static final String FIND_IMAGE_METADTA_BY_CHOICE_OPT = "/findAllChoiceImages";
	private static final String UPDATE_IMAGE_METADATA = "/updateChoiceImage";
	private static final String UPLOAD_CHOICE_IMAGE="/uploadChoiceImage";
	// constant
	private static final String KEY_TYPE_CHOICE_IMAGE= "CHCOP";
	// message response
	private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated";
	private static final String UPLOAD_SUCCESS_MESSAGE = "Upload successfully.";

	@Autowired
	private ChoiceDetailService choiceDetailService;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Get all image metadata of choice options.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @param choiceOptionId The choice option code to find image metadata.
	 * @return the list of image metadata.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ChoiceDetailController.FIND_IMAGE_METADTA_BY_CHOICE_OPT )
	public List<ImageMetaData> findImagesMetadataByChoiceOpt(@RequestParam (value = "choiceOptionId",required = true) String choiceOptionId, HttpServletRequest request) {
		//show log message when init method
		logger.info(String.format(ChoiceDetailController.FIND_IMAGE_METADTA_BY_CHOICE_OPT_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		return choiceDetailService.findImagesMetadataByChoiceOption(Long.parseLong(choiceOptionId));
	}

	/**
	 * Call service to update one or more image metadata.
	 *
	 * @param imageMetadataList The list of product category to update.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of image metadata after update image metadata and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ChoiceDetailController.UPDATE_IMAGE_METADATA)
	public ModifiedEntity<List<ImageMetaData>> updateImageMetadata(
			@RequestBody List<ImageMetaData> imageMetadataList, HttpServletRequest request) {
		//show log message when init method
		logger.info(String.format(ChoiceDetailController.UPDATE_IMAGE_METADATA_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		//call handle from service
		this.choiceDetailService.updateImageMetadata(imageMetadataList,this.userInfo.getUserId());
		//get new data after update product category successfully
		List<ImageMetaData> imageMetaDatas = this.choiceDetailService.findImagesMetadataByChoiceOption(imageMetadataList.get(0).getKey().getId());
		return new ModifiedEntity<>(imageMetaDatas, UPDATE_SUCCESS_MESSAGE);
	}

	/**
	 * Upload Choice image.
	 *
	 * @param fileUpload The file to upload.
	 * @param chcOptCd The chcOptCd of Choice Option Image upload.
	 * @param imgCategoryCode The imgCategoryCode of Choice Option Image upload.
	 * @param imgSourceCode The imgSourceCode of Choice Option Image upload.
	 * @param request The HTTP request that initiated this call.
	 * @return A object of ImageToUpload.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ChoiceDetailController.UPLOAD_CHOICE_IMAGE)
	public ModifiedEntity<List<ImageMetaData>> uploadChoiceImage(@RequestParam("fileUpload") MultipartFile fileUpload, @RequestParam("chcOptCd") String chcOptCd,
																 @RequestParam("imgCategoryCode") String imgCategoryCode , @RequestParam("imgSourceCode") String imgSourceCode,HttpServletRequest request) throws IOException,CheckedSoapException {
		//show log message when init method
		logger.info(String.format(ChoiceDetailController.UPLOAD_CHOICE_IMAGE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
		ImageToUpload choiceImage = new ImageToUpload();
		choiceImage.setUserId(this.userInfo.getUserId());
		choiceImage.setKeyId(chcOptCd);
		choiceImage.setKeyType(KEY_TYPE_CHOICE_IMAGE);
		choiceImage.setImageName(fileUpload.getOriginalFilename());
		choiceImage.setImageCategoryCode(imgCategoryCode);
		choiceImage.setImageSourceCode(imgSourceCode);
		choiceImage.setImageData(fileUpload.getBytes());
		//return all data from service
		this.choiceDetailService.uploadChoiceImage(choiceImage);

		//get new data after update product category successfully
		List<ImageMetaData> imageMetaDatas = this.choiceDetailService.findImagesMetadataByChoiceOption(Long.parseLong(chcOptCd));

		return new ModifiedEntity<>(imageMetaDatas,UPLOAD_SUCCESS_MESSAGE);
	}
}
