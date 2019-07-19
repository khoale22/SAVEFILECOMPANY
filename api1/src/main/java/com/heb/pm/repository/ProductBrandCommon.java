/*
 * ProductBrandCommon
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

/**
 * Common SQL of JPA Repository for ProductBrand.
 *
 * @author vn70529
 * @since 2.12
 */
public interface ProductBrandCommon {

    /**
     * SQL statement that filter product brand by its description or id.
     */
    String FIND_BY_PRODUCT_BRAND_SQL = "select productBrand from ProductBrand productBrand " +
            "where productBrand.productBrandId like concat('%', :productBrand, '%') " +
            "or upper(productBrand.productBrandDescription) like concat('%', upper(:productBrand), '%')";

    /**
     * SQL statement that filter product brand by its description and show own brand only.
     */
    String FIND_BY_PRODUCT_BRAND_AND_OWN_BRAND_SQL = "select productBrand from ProductBrand productBrand " +
            "where (productBrand.productBrandId like concat('%', :productBrand, '%') " +
            "or upper(productBrand.productBrandDescription) like concat('%', upper(:productBrand), '%')) " +
            "and productBrand.productBrandTier.productBrandTierCode <> :productBrandTierCode";

    /**
     * SQL statement that filter product brands by brand description or id and brand tier name.
     */
    String FIND_BY_PRODUCT_BRAND_AND_BRAND_TIER_SQL = "select productBrand from ProductBrand productBrand " +
            "where (productBrand.productBrandId like concat('%', :productBrand, '%') " +
            "or upper(productBrand.productBrandDescription) like concat('%', upper(:productBrand), '%')) " +
            "and upper(productBrand.productBrandTier.productBrandName) like concat('%', upper(:brandTier),'%')";

    /**
     * SQL statement that filter product brands by brand description or id and brand tier name and show own brand only.
     */
    String FIND_BY_PRODUCT_BRAND_AND_BRAND_TIER_AND_OWN_BRAND_SQL =
            "select productBrand from ProductBrand productBrand join productBrand.productBrandTier brandTier " +
                    "where (productBrand.productBrandId like concat('%', :productBrand, '%') " +
                    "or upper(productBrand.productBrandDescription) like concat('%', upper(:productBrand), '%')) " +
                    "and upper(brandTier.productBrandName) like concat('%', upper(:brandTier),'%') " +
                    "and brandTier.productBrandTierCode <> :productBrandTierCode";

	/**
	 * SQL statement that filter product brands by brand description or id.
	 */
	String FIND_BY_PRODUCT_BRAND_AND_DESCRIPTION_SQL =
			"from ProductBrand productBrand " +
					"where (productBrand.productBrandId like concat('%', :id, '%') " +
					"or upper(productBrand.productBrandDescription) like concat('%', upper(:desciption), '%')) ";

    /**
     * SQL statement that filter brands by productBrandId.
     */
    String FIND_BY_BRAND_ID_SQL = "select productBrand from ProductBrand productBrand " +
            "where productBrand.productBrandId like concat('%', :productBrandId, '%')";

    /**
     * SQL statement that filter brands by productBrandId and productBrandDescription.
     */
    String FIND_BY_PRODUCT_BRAND_ID_AND_BRAND_DESCRIPTION_SQL = "select productBrand from ProductBrand productBrand " +
            "where productBrand.productBrandId like concat('%', :productBrandId, '%') " +
            "and upper(productBrand.productBrandDescription) like concat('%', upper(:productBrandDescription), '%')";


    String FIND_BY_BRAND_DESCRIPTION_EXCEPT_ITSELF_SQL = "select productBrand from ProductBrand productBrand " +
            "where upper(TRIM(productBrand.productBrandDescription)) = upper(:productBrandDescription) " +
            "and productBrand.productBrandId != :productBrandId";


}
