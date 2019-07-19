/*
 * ProductScanImageBannerRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanImageBanner;
import com.heb.pm.entity.ProductScanImageBannerKey;
import com.heb.pm.entity.ProductScanImageURI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository to retrieve information about an product scan code image banner.
 *
 * @author vn70516
 * @since 2.14.0
 */
public interface ProductScanImageBannerRepository extends JpaRepository<ProductScanImageBanner, ProductScanImageBannerKey> {

	final String GET_IMAGE_URI_QUERY= "select bn.productScanImageURI from ProductScanImageBanner bn join bn.productScanImageURI " +
			"where bn.key.id in (:scanCodeIds) " +
			"and bn.key.salesChannelCode = :salesChannelCode " +
			"and trim(bn.productScanImageURI.imageStatusCode)=:imageStatusCode " +
			"and trim(bn.productScanImageURI.imagePriorityCode) in (:lstImagePriorityCode)" +
			"and bn.productScanImageURI.activeOnline=:activeOnline";

	final String GET_IMAGE_BY_PRIORITY_CODE_QUERY= "select bn.productScanImageURI from ProductScanImageBanner bn join bn.productScanImageURI " +
			"where bn.key.id in (:scanCodeIds) " +
			"and bn.key.salesChannelCode = :salesChannelCode " +
			"and trim(bn.productScanImageURI.imageStatusCode)=:imageStatusCode " +
			"and trim(bn.productScanImageURI.imagePriorityCode) =:imagePriorityCode";
    /**
     * Find the list of image by list of scan code basing product
     * @param scanCodeIds - The list of scan code id.
     * @param salesChannelCode - The sale channel
     * @return List<ProductScanImageBanner>
     */
    List<ProductScanImageBanner> findByKeyIdInAndKeySalesChannelCode(List<Long> scanCodeIds, String
            salesChannelCode);
    /**
     * Find the list of image by list of scan code basing product, that active online
     * @param scanCodeIds - The list of scan code id.
     * @return List<ProductScanImageBanner>
     */
    List<ProductScanImageBanner>  findByKeyIdIn(List<Long> scanCodeIds);

	/**
	 * Find the list of image by list of scan code basing product and activated online
	 * @param scanCodeIds - The list of scan code id.
	 * @param salesChannelCode - The sale channel
	 * @param imageStatusCode- The image status code
	 * @param activeOnline - Active online switch
	 * @param lstImagePriorityCode  - The image priority code
	 * @return List<ProductScanImageBanner> list image
	 */
	@Query(GET_IMAGE_URI_QUERY)
	List<ProductScanImageURI> findImageActiveOnline(@Param("scanCodeIds") List<Long> scanCodeIds,
													@Param("salesChannelCode") String salesChannelCode,
													@Param("imageStatusCode") String imageStatusCode,
													@Param("activeOnline") Boolean activeOnline,
													@Param("lstImagePriorityCode") List<String> lstImagePriorityCode);
	/**
	 * Find the list of image by list of scan code basing product
	 * @param scanCodeIds - The list of scan code id.
	 * @param salesChannelCode - The sale channel
	 * @return List<ProductScanImageBanner>
	 */
	@Query(GET_IMAGE_BY_PRIORITY_CODE_QUERY)
	List<ProductScanImageURI> findByPriorityCode(@Param("scanCodeIds") List<Long> scanCodeIds,
													@Param("salesChannelCode") String salesChannelCode,
													@Param("imageStatusCode") String imageStatusCode,
													@Param("imagePriorityCode") String lstImagePriorityCode);
}
