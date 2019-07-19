/*
 * ChoiceOptionRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ImageMetaData;
import com.heb.pm.entity.ImageMetaDataKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA Repository for Choice Option.
 *
 * @author vn70516
 * @update by vn86116
 * @since 2.12.0
 */
public interface ImageMetaDataRepository extends JpaRepository<ImageMetaData, ImageMetaDataKey> {
	/**
	 * Query to find all choice option images.
	 */
	String FIND_ALL_CHOICE_OPT_IMG_QUERY = "select o from ImageMetaData o where o.key.imageSubjectTypeCode = :imageSubjectTypeCode and o.active = :active and o.activeOnline = :activeOnline and o.imagePriorityCode = :imagePriorityCode order by o.key.id";

	/**
	 * Query to find primary choice option images.
	 */
	String FIND_PRIMARY_CHOICE_OPT_IMG_QUERY = "select o from ImageMetaData o where o.key.imageSubjectTypeCode = :imageSubjectTypeCode and o.active = :active and o.activeOnline = :activeOnline and o.imagePriorityCode = :imagePriorityCode and o.key.id = :id";

	/**
	 * Query to find images by key id and image subject type code.
	 */
	String FIND_IMG_BY_KEY_ID_AND_IMG_SUB_TYPE_CODE_QUERY = "select o from ImageMetaData o where o.key.id = :id and o.key.imageSubjectTypeCode = :imageSubjectTypeCode order by o.imageCategoryCode";
	/**
	 * Query to find thumbnail images by customer product groups.
	 */
	String FIND_THUMBNAIL_IMAGE_URIS_BY_CUSTOMER_PRODUCT_GROUPS = "select o from ImageMetaData o where o.key.id = :id and o.key.imageSubjectTypeCode = :imageSubjectTypeCode and o.active = :active and o.activeOnline = :activeOnline and o.imagePriorityCode = :imagePriorityCode";
	/**
	 * Find all choice option images.
	 *
	 * @param imageSubjectTypeCode the subject type code request.
	 * @param active the status request.
	 * @param activeOnline the status request.
	 * @param imagePriorityCode the image Priority code request.
	 * @return list of Image meta data.
	 */
	@Query(value = FIND_ALL_CHOICE_OPT_IMG_QUERY)
	List<ImageMetaData> findAllChoiceOptionImages(@Param("imageSubjectTypeCode") String imageSubjectTypeCode,@Param("active") Boolean active,@Param("activeOnline") Boolean activeOnline,@Param("imagePriorityCode") String imagePriorityCode);

	/**
	 * Find images by id and image subject type code.
	 *
	 * @param id the user Id do this action.
	 * @param imageSubjectTypeCode the subject type code request.
	 * @return list of Image meta data.
	 */
	@Query(value = FIND_IMG_BY_KEY_ID_AND_IMG_SUB_TYPE_CODE_QUERY)
	List<ImageMetaData> findImagesByKeyIdAndImageSubjectTypeCode(@Param("id") Long id,@Param("imageSubjectTypeCode") String imageSubjectTypeCode);

	/**
	 * Find the ImageMetaData by id and imageSubjectTypeCode and Active and ActiveOnline and ImagePriorityCode.
	 *
	 * @param id the id of ImageMetaData.
	 * @param imageSubjectTypeCode the image Subject Type Code.
	 * @param active the active sw.
	 * @param activeOnline the active Online sw.
	 * @param imagePriorityCode the image Primary Code.
	 * @return the instance of ImageMetaData.
	 */
	@Query(value = FIND_THUMBNAIL_IMAGE_URIS_BY_CUSTOMER_PRODUCT_GROUPS)
	ImageMetaData findThumbnailImageUrisByCustProductGroups(
			@Param("id") long id,@Param("imageSubjectTypeCode") String imageSubjectTypeCode,@Param("active") Boolean active,@Param("activeOnline") Boolean activeOnline,@Param("imagePriorityCode") String imagePriorityCode);

	/**
	 * Find primary images for choice option .
	 *
	 * @param imageSubjectTypeCode the subject type code request.
	 * @param active the status request.
	 * @param activeOnline the status request.
	 * @param imagePriorityCode the image Priority code request.
	 * @param id the id image
	 * @return list of Image meta data.
	 */
	@Query(value = FIND_PRIMARY_CHOICE_OPT_IMG_QUERY)
	ImageMetaData findPrimaryChoiceOptionImages(@Param("imageSubjectTypeCode") String imageSubjectTypeCode,@Param("active") Boolean active,@Param("activeOnline") Boolean activeOnline,@Param("imagePriorityCode") String imagePriorityCode,@Param("id") Long id);

	/**
	 * This method will find all image meta data based on the data's id (entity id)
	 * @param id
	 * @return
	 */
	List<ImageMetaData> findByKeyId(long id);
}
