/*
 *  ProductLineService
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productLine;

import com.heb.pm.entity.ProductLine;
import com.heb.pm.entity.ProductMat;
import com.heb.pm.repository.ProductLineRepository;
import com.heb.pm.repository.ProductLineRepositoryWithCount;
import com.heb.pm.repository.ProductMatRepository;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table product line.
 *
 * @author m314029
 * @since 2.26.0
 */
@Service
public class ProductLineService {

    @Autowired
    private ProductLineRepository repository;

    @Autowired
    private ProductLineRepositoryWithCount repositoryWithCount;

    @Autowired
    private ProductMatRepository productMatRepository;

    /**
     * Get all records of PROD_SUB_BRND table by heb pagination.
     *
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @return the page of product brands.
     */
    public PageableResult<ProductLine> findByPage(int page, int pageSize, String id,
                                                  String description, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize, ProductLine.getDefaultSort());
        PageableResult<ProductLine> results;
        if (includeCount) {
            Page<ProductLine> prodSubBrands = this.findProductSubBrandsWithCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), prodSubBrands.getTotalPages(),
                    prodSubBrands.getTotalElements(), prodSubBrands.getContent());
        } else {
            List<ProductLine> productSubBrands = this.findProductSubBrandsWithoutCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), productSubBrands);
        }
        return results;
    }

    /**
     * Get all product lines with pagination.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private Page<ProductLine> findProductSubBrandsWithCount(String id, String description, Pageable pageRequest) {
        Page<ProductLine> productLines;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines.
            productLines = this.repositoryWithCount.findAll(pageRequest);
        } else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines filter by product line id.
            productLines = this.repositoryWithCount.findByIdIgnoreCaseContaining(id, pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            // get all product lines filter by product line name.
            productLines = this.repositoryWithCount.findByDescriptionIgnoreCaseContaining(description, pageRequest);
        } else {
            // get all product lines filter by product line id and product line name.
            productLines = this.repositoryWithCount.findByIdIgnoreCaseContainingAndDescriptionIgnoreCaseContaining(
                    id, description, pageRequest);
        }
        return productLines;
    }

    /**
     * Get all product lines with pagination.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private List<ProductLine> findProductSubBrandsWithoutCount(String id, String description, Pageable pageRequest) {
        List<ProductLine> productLines;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines.
            productLines = this.repository.findAllByPage(pageRequest);
        } else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all product lines filter by product line id.
            productLines = this.repository.findByIdIgnoreCaseContaining(id, pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            // get all product lines filter by product line name.
            productLines = this.repository.findByDescriptionIgnoreCaseContaining(description, pageRequest);
        } else {
            // get all product lines filter by product line id and product line name.
            productLines = this.repository.findByIdIgnoreCaseContainingAndDescriptionIgnoreCaseContaining(
                    id, description, pageRequest);
        }
        return productLines;
    }

    /**
     * Adds new product lines.
     *
     * @param productLineDescriptions the product line descriptions to add.
     * @return the newly added product lines.
     */
    public List<ProductLine> addProductLines(List<ProductLine> productLineDescriptions) {
        List<ProductLine> newProductLines = new ArrayList<>();
        Integer maxId = Integer.valueOf(this.repository.findTop1ByOrderByIdDesc().getId());
        for(ProductLine productLineDescription : productLineDescriptions) {
            List<ProductLine> productLines = this.repository.findByDescriptionIgnoreCase(productLineDescription.getDescription());
            for(ProductLine productLine : productLines) {
                if(productLine.getDescription().trim().equalsIgnoreCase(productLineDescription.getDescription().trim())) {
                    throw new IllegalArgumentException("Product Line (Sub Brand) already exists.");
                }
            }
            maxId++;
            newProductLines.add(new ProductLine().setDescription(productLineDescription.getDescription()).
                    setId(this.getPaddedId(maxId)));
        }
        return this.repository.save(newProductLines);
    }

    /**
     * Saves an updated product line. Throws error if description is already in use.
     *
     * @param productLine the product line to be updated.
     * @return the updated product line.
     */
    public ProductLine updateProductLineDescription(ProductLine productLine) {
        List<ProductLine> productLines =
                this.repository.findByDescriptionIgnoreCase(productLine.getDescription());
        if(!CollectionUtils.isEmpty(productLines)) {
            throw new IllegalArgumentException("Product Line (Sub Brand) already exists.");
        }
        return this.repository.save(productLine);
    }

    /**
     * Deletes a product line. Throws exception if the product line is tied to a product.
     *
     * @param productLineId the id of the Product Line to delete.
     */
    public void deleteProductLine(String productLineId) {
        List<ProductMat> productLines = this.productMatRepository.findAllByProductLineCode(productLineId);
        if(!CollectionUtils.isEmpty(productLines)) {
            throw new IllegalArgumentException("Products are assigned to this sub brand.");
        }
        ProductLine productLineToDelete = this.repository.findOne(productLineId);
        if(productLineToDelete != null) {
            this.repository.delete(productLineToDelete);
        }
    }

    /**
     * Returns a string padded with 0s to a length of 5.
     *
     * @param id the id to pad.
     * @return a string padded with 0s to a length of 5.
     */
    private String getPaddedId(int id) {
        return String.format("%05d", id);
    }

    /**
     * Returns a list of Product Line by code or description.
     * @param searched the search parameters, to be searched against both id and description.
     * @param page the page number.
     * @param pageSize the page size.
     * @return a list of Product Line by code or description.
     */
    public List<ProductLine> findAllByProductLineCodeOrDescription(String searched, int page, int pageSize){
        Pageable pageRequest = new PageRequest(page, pageSize, ProductLine.getDefaultSort());
        return this.repository.findByIdIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(searched,searched,pageRequest);
    }
}
