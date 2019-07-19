/*
 * ProductBrandTierRepository
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductBrandTier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository of the ProductBrandTier entity.
 *
 * @author vn87351
 * @since 2.41.0
 */
public interface ProductBrandTierRepository extends JpaRepository<ProductBrandTier, Integer>, ProductBrandTierRepositoryCommon {

    /**
     * Find all Tier Code item filtered by product brand name.
     *
     * @param productBrandName the description to search.
     * @param pageRequest the page request for pagination.
     * @return the page of product sub brands.
     */
    List<ProductBrandTier> findByProductBrandNameIgnoreCaseContaining(@Param("productBrandName") String productBrandName, Pageable pageRequest);

    /**
     * Find Tier Code with the highest Product Brand Tier Code.
     *
     * @return ProductLine with the highest Product Brand Tier Code.
     */
    ProductBrandTier findTop1ByOrderByProductBrandTierCodeDesc();

    /**
     * Find all Tier Code item filtered by product brand tier code or product brand name.
     *
     * @param productBrandTierCode the id to search.
     * @param productBrandName the description to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of Tier Code.
     */
    List<ProductBrandTier> findByProductBrandTierCodeIgnoreCaseContainingOrProductBrandNameIgnoreCaseContaining(String productBrandTierCode, String productBrandName,
                                                                                    Pageable pageRequest);

    @Query(value = "select tierCode from ProductBrandTier tierCode")
    List<ProductBrandTier> findAllByPage(Pageable pageable);

    /**
     * Find all Tier Code item that It contain productBrandTierCode param.
     *
     * @param id          The product brand tier code to search..
     * @param pageRequest The page request for pagination.
     * @return the page of product sub brands.
     */
    @Query(value = FIND_BY_TIER_CODE_ID_SQL)
    List<ProductBrandTier> findById(@Param("id") String id, Pageable pageRequest);

    /**
     * Find all Tier Code item filtered by product brand name.
     *
     * @param name  The prod sub brand name to search.
     * @return Tier Code found.
     */
    List<ProductBrandTier> findByTrimmedProductBrandNameIgnoreCase(String name);

    /**
     * Find all Tier Code item filtered by product brand name and product brand tier code.
     *
     * @param id          The product brand tier code to search.
     * @param name        The prod sub brand name to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product sub brands.
     */
    @Query(value = FIND_BY_TIER_CODE_ID_AND_TIER_CODE_NAME_SQL)
    List<ProductBrandTier> findByIdAndName(@Param("id") String id, @Param("name") String name, Pageable pageRequest);

}
