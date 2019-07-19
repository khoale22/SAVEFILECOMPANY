/*
 *  ChoiceDetailService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.choice;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ContentManagementServiceClient;
import com.heb.pm.ws.ImageInforServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Holds all business logic related to code table choice detail information.
 *
 * @author vn86116
 * @since 2.12.0
 */
@Service
public class ChoiceDetailService {

	@Autowired
	private ImageMetaDataRepository imageMetaDataRepository;

	@Autowired
	ContentManagementServiceClient contentManagementServiceClient;

	@Autowired
	ImageInforServiceClient imageInforServiceClient;

	@Autowired
	ImageStatusRepository imageStatusRepository;

	private static final String IMAGE_CHOICE_OPTION_SUBJECT_TYPE = "CHCOP";

	/**
	 * Call ImageMetaDataRepository to get a list of image metadata is found by choice option, order by create date.
	 *
	 * @return A list of image metadata.
	 */
	public List<ImageMetaData> findImagesMetadataByChoiceOption(Long choiceOptionCode) {
		return this.imageMetaDataRepository.findImagesByKeyIdAndImageSubjectTypeCode(choiceOptionCode,IMAGE_CHOICE_OPTION_SUBJECT_TYPE);
	}

	/**
	 * Call ImageInforServiceClient to update one or more image metadata.
	 *
	 * @param listImageMetadata  A list of image metadata.
	 * @param userId The user id that send request.
	 */
	public void updateImageMetadata(List<ImageMetaData> listImageMetadata,String userId){
		imageInforServiceClient.updateContentMangementDetails(listImageMetadata,userId);
	}

	/**
	 * Call ContentManagementServiceClient to upload choice image.
	 *
	 * @param imageToUpload the ImageToUpload.
	 */
	public void uploadChoiceImage(ImageToUpload imageToUpload) throws CheckedSoapException {
		this.contentManagementServiceClient.uploadImage(imageToUpload);
	}
}
