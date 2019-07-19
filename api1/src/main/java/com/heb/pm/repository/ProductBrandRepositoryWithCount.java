/*
 * ProductBrandRepositoryWithCount
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

import com.heb.pm.entity.ProductBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository of the ProductBrand entity.
 *
 * @author vn00602
 * @since 2.12.0
 */
public interface ProductBrandRepositoryWithCount extends JpaRepository<ProductBrand, Integer>, ProductBrandCommon {

    /**
     * Find by product brand tier code is not Unassigned [0]
     *
     * @param productBrandTierCode the product brand tier code is Unassigned [0].
     * @param pageRequest          the page request for pagination.
     * @return the page of product brands.
     */
    Page<ProductBrand> findByProductBrandTierProductBrandTierCodeNot(Integer productBrandTierCode, Pageable pageRequest);

    /**
     * Find by product brand description by like query.
     *
     * @param productBrand the product brand description to find.
     * @param pageRequest  the page request for pagination.
     * @return the page of product brands.
     */
    @Query(value = FIND_BY_PRODUCT_BRAND_SQL)
    Page<ProductBrand> findByProductBrand(@Param("productBrand") String productBrand, Pageable pageRequest);

    /**
     * Find by product brand description by like query.
     *
     * @param productBrand         the product brand description to find.
     * @param productBrandTierCode the product brand tier code is Unassigned [0].
     * @param pageRequest          the page request for pagination.
     * @return the page of product brands.
     */
    @Query(value = FIND_BY_PRODUCT_BRAND_AND_OWN_BRAND_SQL)
    Page<ProductBrand> findByProductBrandAndOwnBrand(@Param("productBrand") String productBrand,
                                                     @Param("productBrandTierCode") Integer productBrandTierCode,
                                                     Pageable pageRequest);

    /**
     * Find by product brand tier name by like query.
     *
     * @param productBrandName the product brand tier name to find.
     * @param pageRequest      the page request for pagination.
     * @return the page of product brands.
     */
    Page<ProductBrand> findByProductBrandTierProductBrandNameIgnoreCaseContaining(String productBrandName, Pageable pageRequest);

    /**
     * Find by product brand description by like query and brand tier code is not Unassigned [0].
     *
     * @param productBrandName     the product brand tier name to find.
     * @param productBrandTierCode the product brand tier code is Unassigned [0].
     * @param pageRequest          the page request for pagination.
     * @return the page of product brands.
     */
    Page<ProductBrand> findByProductBrandTierProductBrandNameIgnoreCaseContainingAndProductBrandTierProductBrandTierCodeNot(
            String productBrandName, Integer productBrandTierCode, Pageable pageRequest);

    /**
     * Find by product brand description and product brand tier name by like query.
     *
     * @param productBrand the product brand description to find.
     * @param brandTier    the product brand tier name to find.
     * @param pageRequest  the page request for pagination.
     * @return the page of product brands.
     */
    @Query(value = FIND_BY_PRODUCT_BRAND_AND_BRAND_TIER_SQL)
    Page<ProductBrand> findByProductBrandAndBrandTier(@Param("productBrand") String productBrand,
                                                      @Param("brandTier") String brandTier,
                                                      Pageable pageRequest);

    /**
     * Find by product brand description and product brand tier name by like query and brand tier code is not Unassigned [0].
     *
     * @param productBrand         the product brand description to find.
     * @param brandTier            the product brand tier name to find.
     * @param productBrandTierCode the product brand tier code is Unassigned [0].
     * @param pageRequest          the page request for pagination.
     * @return the page of product brands.
     */
    @Query(value = FIND_BY_PRODUCT_BRAND_AND_BRAND_TIER_AND_OWN_BRAND_SQL)
    Page<ProductBrand> findByProductBrandAndBrandTierAndOwnBrand(@Param("productBrand") String productBrand,
                                                                 @Param("brandTier") String brandTier,
                                                                 @Param("productBrandTierCode") Integer productBrandTierCode,
                                                                 Pageable pageRequest);

	/**
	 * find product brand by id or description
	 * @param id the id to find
	 * @param desciption the description to find
	 * @param pageRequest pagging data
	 * @return page data
	 */
	@Query(value = FIND_BY_PRODUCT_BRAND_AND_DESCRIPTION_SQL)
	Page<ProductBrand> findByProductBrandIdOrDescription(@Param("id") String id,
														  @Param("desciption") String desciption,
														  Pageable pageRequest);

    /**
     * Find all brands data filter by brand id.
     *
     * @param productBrandId the productBrandId to search.
     * @param pageRequest    the page request for pagination.
     * @return the page of brands.
     */
    @Query(value = FIND_BY_BRAND_ID_SQL)
    Page<ProductBrand> findByProductBrandId(@Param("productBrandId") String productBrandId, Pageable pageRequest);


    /**
     * Find all product brands data filter by productBrandDescription.
     *
     * @param productBrandDescription the productBrandDescription to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product brands.
     */
    Page<ProductBrand> findByProductBrandDescriptionIgnoreCaseContaining(String productBrandDescription, Pageable pageRequest);

    /**
     * Find all product brands data filter by productBrandDescription and productBrandId.
     *
     * @param productBrandId   the productBrandId to search.
     * @param productBrandDescription the productBrandDescription to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product brands.
     */
    @Query(value = FIND_BY_PRODUCT_BRAND_ID_AND_BRAND_DESCRIPTION_SQL)
    Page<ProductBrand> findByProductBrandIdAndProductBrandDescription(@Param("productBrandId") String productBrandId,
                                                                      @Param("productBrandDescription") String productBrandDescription,
                                                                      Pageable pageRequest);

}
