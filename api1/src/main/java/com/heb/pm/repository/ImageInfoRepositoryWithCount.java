/*
 *  ImageInfoRepositoryWithCount
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductScanImageURI;
import com.heb.pm.entity.ProductScanImageURIKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * JPA repository for the Product Scan Image URI table with count.
 *
 * @author vn70529
 * @since 2.27.0
 */
public interface ImageInfoRepositoryWithCount extends JpaRepository<ProductScanImageURI, ProductScanImageURIKey>,  ImageInfoRepositoryCommon {

    /**
     * Get submitted image with not rejected image by upcs.
     *
     * @param upcs the list of upc.
     * @param page the Pageable.
     * @return the List<ProductScanImageURI>
     */
    @Query(value = QUERY_FIND_SUBMITTED_IMAGE_NOT_REJECTED_BY_UPCS)
    Page<ProductScanImageURI> findSubmittedImageAndNotRejectedByUpcs(@Param("upcs") List<Long> upcs, Pageable page);

    /**
     * Get submitted image with not rejected image or rejected images of 3 months from today's date by upcs.
     *
     * @param upcs the list of upc.
     * @param page the Pageable.
     * @return the Page<ProductScanImageURI>
     */
    @Query(value = QUERY_FIND_SUBMITTED_IMAGE_BY_UPCS)
    Page<ProductScanImageURI> findSubmittedImageByUpcs(@Param("upcs") List<Long> upcs, Pageable page);


    /**
     * Get rejected images of 3 months from today's date by upcs.
     *
     * @param upcs the list of upc.
     * @param page the Pageable.
     * @return the Page<ProductScanImageURI>
     */
    @Query(value = QUERY_FIND_REJECTED_IMAGE_BY_UPCS)
    Page<ProductScanImageURI> findRejectedImageByUpcs(@Param("upcs") List<Long> upcs, Pageable page);
}
