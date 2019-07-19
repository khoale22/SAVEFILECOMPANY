/*
 *  ProductSubBrandService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productSubBrand;

import com.heb.pm.entity.ProductSubBrand;
import com.heb.pm.repository.ProductSubBrandRepository;
import com.heb.pm.repository.ProductSubBrandRepositoryWithCount;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Holds all business logic related to code table product sub brand information.
 *
 * @author vn00602
 * @since 2.12.0
 */
@Service
public class ProductSubBrandService {

    @Autowired
    private ProductSubBrandRepository productSubBrandRepository;

    @Autowired
    private ProductSubBrandRepositoryWithCount productSubBrandRepositoryWithCount;

    /**
     * Get all records of PROD_SUB_BRND table by heb pagination.
     *
     * @param page             the page number.
     * @param pageSize         the page size.
     * @param prodSubBrandId   the product sub brand id to search.
     * @param prodSubBrandName the product sub brand name to search.
     * @return the page of product brands.
     */
    public PageableResult<ProductSubBrand> findProductSubBrandsPage(int page, int pageSize, String prodSubBrandId,
                                                                    String prodSubBrandName, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize);
        PageableResult<ProductSubBrand> results;
        if (includeCount) {
            Page<ProductSubBrand> prodSubBrands = this.findProductSubBrandsWithCount(prodSubBrandId, prodSubBrandName, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), prodSubBrands.getTotalPages(),
                    prodSubBrands.getTotalElements(), prodSubBrands.getContent());
        } else {
            List<ProductSubBrand> productSubBrands = this.findProductSubBrandsWithoutCount(prodSubBrandId, prodSubBrandName, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productSubBrands);
        }
        return results;
    }

    /**
     * Get all product sub brands with pagination.
     *
     * @param prodSubBrandId   the product sub brand id to search.
     * @param prodSubBrandName the product sub brand name to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product brands.
     */
    private Page<ProductSubBrand> findProductSubBrandsWithCount(String prodSubBrandId, String prodSubBrandName, Pageable pageRequest) {
        Page<ProductSubBrand> productBrands;
        if (StringUtils.isEmpty(prodSubBrandId) && StringUtils.isEmpty(prodSubBrandName)) {
            // get all product sub brands.
            productBrands = this.productSubBrandRepositoryWithCount.findAll(pageRequest);
        } else if (!StringUtils.isEmpty(prodSubBrandId) && StringUtils.isEmpty(prodSubBrandName)) {
            // get all product sub brands filter by product sub brand id.
            productBrands = this.productSubBrandRepositoryWithCount.findByProductSubBrandId(prodSubBrandId, pageRequest);
        } else if (StringUtils.isEmpty(prodSubBrandId) && !StringUtils.isEmpty(prodSubBrandName)) {
            // get all product sub brands filter by product sub brand name.
            productBrands = this.productSubBrandRepositoryWithCount.findByProdSubBrandNameIgnoreCaseContaining(prodSubBrandName, pageRequest);
        } else {
            // get all product sub brands filter by product sub brand id and product sub brand name.
            productBrands = this.productSubBrandRepositoryWithCount.findByProdSubBrandIdAndProdSubBrandName(
                    prodSubBrandId, prodSubBrandName, pageRequest);
        }
        return productBrands;
    }

    /**
     * Get all product sub brands with pagination.
     *
     * @param prodSubBrandId   the product sub brand id to search.
     * @param prodSubBrandName the product sub brand name to search.
     * @param pageRequest      the page request for pagination.
     * @return the page of product brands.
     */
    private List<ProductSubBrand> findProductSubBrandsWithoutCount(String prodSubBrandId, String prodSubBrandName, Pageable pageRequest) {
        List<ProductSubBrand> productBrands;
        if (StringUtils.isEmpty(prodSubBrandId) && StringUtils.isEmpty(prodSubBrandName)) {
            // get all product sub brands.
            productBrands = this.productSubBrandRepository.findAll(pageRequest).getContent();
        } else if (!StringUtils.isEmpty(prodSubBrandId) && StringUtils.isEmpty(prodSubBrandName)) {
            // get all product sub brands filter by product sub brand id.
            productBrands = this.productSubBrandRepository.findByProductSubBrandId(prodSubBrandId, pageRequest);
        } else if (StringUtils.isEmpty(prodSubBrandId) && !StringUtils.isEmpty(prodSubBrandName)) {
            // get all product sub brands filter by product sub brand name.
            productBrands = this.productSubBrandRepository.findByProdSubBrandNameIgnoreCaseContaining(prodSubBrandName, pageRequest);
        } else {
            // get all product sub brands filter by product sub brand id and product sub brand name.
            productBrands = this.productSubBrandRepository.findByProdSubBrandIdAndProdSubBrandName(
                    prodSubBrandId, prodSubBrandName, pageRequest);
        }
        return productBrands;
    }
}
