/*
 * ProductScanImageURIRepository
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.ProductScanImageURIKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Product Scan Image URI Repository .
 *
 * @author vn87351
 * @since 2.16.0
 */
public interface ProductScanImageURIRepository extends JpaRepository<ProductScanImageURI, ProductScanImageURIKey>{
	String GET_IMAGE_BY_PRIORITY_CODE_QUERY= "select o from ProductScanImageURI o " +
			"where o.key.id in (:scanCodeIds) " +
			"and trim(o.imagePriorityCode) =:imagePriorityCode";
	/**
	 * Find list of image by list scan code ids.
	 * @param scanCodeIds
	 * @return
	 */
	List<ProductScanImageURI> findByKeyIdIn(List<Long> scanCodeIds);

	/**
	 * Find list of Primary Image by list of scan code id.
	 * 
	 * @param scanCodeIds
	 * @param imagePriorityCode
	 * @return
	 */
	@Query(GET_IMAGE_BY_PRIORITY_CODE_QUERY)
	List<ProductScanImageURI> findPrimaryImageByScnCodes(@Param("scanCodeIds") List<Long> scanCodeIds,
														 @Param("imagePriorityCode") String imagePriorityCode);
}