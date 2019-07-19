/*
 * ProductBrandTierRepository
 *
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

/**
 * Common of the ProductBrandTier entity.
 *
 * @author vn87351
 * @since 2.41.0
 */
public interface ProductBrandTierRepositoryCommon{

    /**
     * Get tier code list by productBrandTierCode.
     */
    String FIND_BY_TIER_CODE_ID_SQL = "from ProductBrandTier productBrandTier " +
            "where productBrandTier.productBrandTierCode like concat('%', :id, '%')";

    /**
     * Get tier code list by productBrandTierCode and productBrandName.
     */
    String FIND_BY_TIER_CODE_ID_AND_TIER_CODE_NAME_SQL = "from ProductBrandTier productBrandTier " +
            "where productBrandTier.productBrandTierCode like concat('%', :id, '%') " +
            "and upper(productBrandTier.productBrandName) like concat('%', upper(:name), '%')";

}
