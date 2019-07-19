/*
 * ProductBrandCommon
 *
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
package com.heb.pm.repository;

/**
 * Common SQL of JPA Repository for ProductBrand.
 *
 * @author s769046
 * @since 2.12
 */
public interface ProductLineBrandCommon {

    /**
     * SQL statement that filter product brand by its description or id.
     */
    String FIND_BY_PRODUCT_LINE_BRAND_SQL = "select productLineBrand from ProductLineBrand productLineBrand " +
            "where productLineBrand.productLineBrandId like concat('%', :productLineBrand, '%') " +
            "or upper(productLineBrand.productLineBrandDescription) like concat('%', upper(:productLineBrand), '%')";

    /**
     * SQL statement that filter product brands by brand description or id.
     */
    String FIND_BY_PRODUCT_LINE_BRAND_AND_DESCRIPTION_SQL =
            "from ProductBrand productBrand " +
                    "where (productBrand.productBrandId like concat('%', :id, '%') " +
                    "or upper(productBrand.productBrandDescription) like concat('%', upper(:desciption), '%')) ";
}
