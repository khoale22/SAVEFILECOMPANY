/*
 *  ProductBrandService
 *  Copyright (c) 2019 H-E-B.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */

package com.heb.pm.codeTable.brand;


import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table product brand information.
 *
 * @author vn87351
 * @since 2.41.0
 */

@Service
public class BrandService {
    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private ProductBrandCostOwnerRepository productBrandCostOwnerRepository;

    @Autowired
    private ProductBrandRepositoryWithCount productBrandRepositoryWithCount;

    @Autowired
    private ProductLineBrandRepository productLineBrandRepository;

    @Autowired
    private SellingUnitRepository sellingUnitRepository;

    private static final String BRAND_EXIST_DEFAULT = "Brand already exists";
    private static final String UNABLE_TO_DELETE_IN_RELATIONSHIP_AND_ITEMS_TIED_ERROR_MESSAGE = "Unable to delete - In relationship code table and items tied.";
    private static final String UNABLE_TO_DELETE_IN_RELATIONSHIP_ERROR_MESSAGE = "Unable to delete - In relationship code table.";
    private static final String UNABLE_TO_DELETE_ITEMS_TIED_ERROR_MESSAGE = "Unable to delete - Items tied.";

    /**
     * Get all records of ProductBrand table by heb pagination.
     *
     * @param page                    The page number.
     * @param pageSize                The page size.
     * @param productBrandId          The productBrandId to search.
     * @param productBrandDescription The productBrandDescription to search.
     * @return the page of product brands.
     */
    public PageableResult<ProductBrand> findByPage(int page, int pageSize, String productBrandId,
                                                   String productBrandDescription, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize, ProductBrand.getDefaultSort());
        PageableResult<ProductBrand> results;
        if (includeCount) {
            Page<ProductBrand> prodSubBrands = this.findBrandsWithCount(productBrandId, productBrandDescription, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), prodSubBrands.getTotalPages(),
                    prodSubBrands.getTotalElements(), prodSubBrands.getContent());
        } else {
            List<ProductBrand> productSubBrands = this.findBrandsWithoutCount(productBrandId, productBrandDescription, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productSubBrands);
        }
        return results;
    }

    /**
     * Get all product brands with pagination.
     *
     * @param productBrandId          the productBrandId to search.
     * @param productBrandDescription the productBrandDescription to search.
     * @param pageRequest             the page request for pagination.
     * @return the page of product brands.
     */
    private Page<ProductBrand> findBrandsWithCount(String productBrandId, String productBrandDescription, Pageable pageRequest) {
        Page<ProductBrand> productBrands;
        if (StringUtils.isEmpty(productBrandId) && StringUtils.isEmpty(productBrandDescription)) {
            // get all product brands.
            productBrands = this.productBrandRepositoryWithCount.findAll(pageRequest);
        } else if (!StringUtils.isEmpty(productBrandId) && StringUtils.isEmpty(productBrandDescription)) {
            // get all product brands filter by productBrandId.
            productBrands = this.productBrandRepositoryWithCount.findByProductBrandId(productBrandId, pageRequest);
        } else if (StringUtils.isEmpty(productBrandId) && !StringUtils.isEmpty(productBrandDescription)) {
            // get all product brands filter by productBrandDescription.
            productBrands = this.productBrandRepositoryWithCount.findByProductBrandDescriptionIgnoreCaseContaining(productBrandDescription, pageRequest);
        } else {
            // get all product brands filter by product productBrandId and productBrandDescription.
            productBrands = this.productBrandRepositoryWithCount.findByProductBrandIdAndProductBrandDescription(
                    productBrandId, productBrandDescription, pageRequest);
        }
        return productBrands;
    }

    /**
     * Get all product brands with pagination.
     *
     * @param productBrandId          the productBrandId to search.
     * @param productBrandDescription the productBrandDescription to search.
     * @param pageRequest             the page request for pagination.
     * @return the page of product brands.
     */
    private List<ProductBrand> findBrandsWithoutCount(String productBrandId, String productBrandDescription, Pageable pageRequest) {
        List<ProductBrand> productBrands;
        if (StringUtils.isEmpty(productBrandId) && StringUtils.isEmpty(productBrandDescription)) {
            // get all product sub brands.
            productBrands = this.productBrandRepository.findAll(pageRequest).getContent();
        } else if (!StringUtils.isEmpty(productBrandId) && StringUtils.isEmpty(productBrandDescription)) {
            // get all product sub brands filter by product sub brand id.
            productBrands = this.productBrandRepository.findByProductBrandId(productBrandId, pageRequest);
        } else if (StringUtils.isEmpty(productBrandId) && !StringUtils.isEmpty(productBrandDescription)) {
            // get all product sub brands filter by product sub brand name.
            productBrands = this.productBrandRepository.findByProductBrandDescriptionIgnoreCaseContaining(productBrandDescription, pageRequest);
        } else {
            // get all product sub brands filter by product sub brand id and product sub brand name.
            productBrands = this.productBrandRepository.findByProductBrandIdAndProductBrandDescription(
                    productBrandId, productBrandDescription, pageRequest);
        }
        return productBrands;
    }

    /**
     * Saves an updated product brand. Throws error if description is already in use.
     *
     * @param productBrand the product brand to be updated.
     * @return the updated product brand.
     */
    public ProductBrand updateProductBrand(ProductBrand productBrand) {
        List<ProductBrand> productBrands =
                this.productBrandRepository.findByProductBrandDescriptionIgnoreItself(productBrand.getProductBrandDescription(), productBrand.getProductBrandId());
        if (!CollectionUtils.isEmpty(productBrands)) {
            throw new IllegalArgumentException(BRAND_EXIST_DEFAULT);
        }
        return this.productBrandRepository.save(productBrand);
    }

    /**
     * Deletes a product brand. Throws exception if The Brand is in the Brand-Product Line
     * or in the Brand-Cost Owner table or tied to UPCs in PROD_SCN_CODES
     *
     * @param productBrandId the productBrandId of the Product Brand to delete.
     */
    public void deleteProductBrand(Long productBrandId) {
        long count = 0;
        ProductLineBrand productLineBrand = this.productLineBrandRepository.findFirstByKeyBrandId(productBrandId);
        ProductBrandCostOwner productBrandCostOwner = this.productBrandCostOwnerRepository.findFirstByKeyProductBrandId(productBrandId);
        count = this.sellingUnitRepository.countSellingUnitByProductBrandProductBrandId(productBrandId);
        if ((productLineBrand != null && count > 0) || (productBrandCostOwner != null && count > 0)) {
            throw new IllegalArgumentException(UNABLE_TO_DELETE_IN_RELATIONSHIP_AND_ITEMS_TIED_ERROR_MESSAGE);
        } else if ((productLineBrand != null && count == 0)
                || (productBrandCostOwner != null && count == 0)) {
            throw new IllegalArgumentException(UNABLE_TO_DELETE_IN_RELATIONSHIP_ERROR_MESSAGE);
        } else if (count > 0 && productLineBrand == null && productBrandCostOwner == null) {
            throw new IllegalArgumentException(UNABLE_TO_DELETE_ITEMS_TIED_ERROR_MESSAGE);
        }

        ProductBrand productBrandToDelete = this.productBrandRepository.findOneProductBrand(productBrandId);
        if (productBrandToDelete != null) {
            this.productBrandRepository.delete(productBrandToDelete);
        }
    }

    /**
     * Add new brands.
     *
     * @param productBrands the list of productBrand.
     * @return the list of productBrands and a message for the front end.
     */
    public List<ProductBrand> addProductBrands(List<ProductBrand> productBrands) {
        Long maxId = this.productBrandRepository.findTop1ByOrderByProductBrandIdDesc().getProductBrandId();
            for (ProductBrand productBrand : productBrands) {
                productBrand.setProductBrandId(++maxId);
                productBrand.setProductBrandAbbreviation(ProductBrand.BLANK);
                productBrand.setProductBrandTierId(ProductBrandTier.DEFAULT_PRODUCT_BRAND_TIER_ID);
            }
        return this.productBrandRepository.save(productBrands);
    }

    /**
     * Validate brands is existed or not.
     *
     * @param productBrands the list of added ProductBrands.
     * @return the list of existed ProductBrands.
     */
    public List<ProductBrand> validateBrands(List<ProductBrand> productBrands) {
        List<ProductBrand> newProductBrands = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(productBrands)) {
                for (ProductBrand productBrand : productBrands) {
                    List<ProductBrand> productBrandList =
                            this.productBrandRepository.findByTrimmedProductBrandDescriptionIgnoreCase(productBrand.getProductBrandDescription());
                    if (CollectionUtils.isNotEmpty(productBrandList)) {
                        newProductBrands.add(productBrand);
                    }
                }
            }
        return newProductBrands;
    }

}
