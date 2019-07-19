/*
 * ProductSubBrandRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductSubBrand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository of the ProductSubBrand entity.
 *
 * @author vn00602
 * @since 2.12.0
 */
public interface ProductSubBrandRepository extends JpaRepository<ProductSubBrand, Long> {

    String FIND_BY_PRODUCT_SUB_BRAND_ID_SQL = "select productSubBrand from ProductSubBrand productSubBrand " +
            "where productSubBrand.prodSubBrandId like concat('%', :prodSubBrandId, '%')";

    String FIND_BY_PRODUCT_SUB_BRAND_ID_AND_SUB_BRAND_NAME_SQL = "select productSubBrand from ProductSubBrand productSubBrand " +
            "where productSubBrand.prodSubBrandId like concat('%', :prodSubBrandId, '%') " +
            "and upper(productSubBrand.prodSubBrandName) like concat('%', upper(:prodSubBrandName), '%')";

    /**
     * Find all product sub brands data filter by prod sub brand id.
     *
     * @param prodSubBrandId the prod sub brand id to search.
     * @param pageRequest    the page request for pagination.
     * @return the page of product sub brands.
     */
    @Query(value = FIND_BY_PRODUCT_SUB_BRAND_ID_SQL)
    List<ProductSubBrand> findByProductSubBrandId(@Param("prodSubBrandId") String prodSubBrandId, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param prodSubBrandName the prod sub brand name to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product sub brands.
     */
    List<ProductSubBrand> findByProdSubBrandNameIgnoreCaseContaining(String prodSubBrandName, Pageable pageRequest);

    /**
     * Find all product sub brands data filter by prod sub brand name.
     *
     * @param prodSubBrandId   the prod sub brand id to search.
     * @param prodSubBrandName the prod sub brand name to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product sub brands.
     */
    @Query(value = FIND_BY_PRODUCT_SUB_BRAND_ID_AND_SUB_BRAND_NAME_SQL)
    List<ProductSubBrand> findByProdSubBrandIdAndProdSubBrandName(@Param("prodSubBrandId") String prodSubBrandId,
                                                                  @Param("prodSubBrandName") String prodSubBrandName,
                                                                  Pageable pageRequest);
}
