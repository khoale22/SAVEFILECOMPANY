/*
 *  TierCodeService
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.tierCode;

import com.heb.pm.entity.ProductBrand;
import com.heb.pm.entity.ProductBrandTier;
import com.heb.pm.repository.*;
import com.heb.util.jpa.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to code table tier code.
 *
 * @author vn87351
 * @since 2.41.0
 */
@Service
public class TierCodeService {

    @Autowired
    private ProductBrandTierRepository repository;

    @Autowired
    private ProductBrandTierRepositoryWithCount repositoryWithCount;

    @Autowired
    private ProductBrandRepository productBrandRepository;

    /**
     * Get all records of Tier Code table by heb pagination.
     *
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @return the page of Tier Code.
     */
    public PageableResult<ProductBrandTier> findTierCodeList(int page, int pageSize, String id,
                                                       String description, boolean includeCount) {
        Pageable pageRequest = new PageRequest(page, pageSize, ProductBrandTier.getDefaultSort());
        PageableResult<ProductBrandTier> results;
        if (includeCount) {
            Page<ProductBrandTier> tierCodes = this.findTierCodesWithCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), tierCodes.getTotalPages(), tierCodes.getTotalElements(), tierCodes.getContent());
        } else {
            List<ProductBrandTier> tierCodeList = this.findTierCodesWithoutCount(id, description, pageRequest);
            results = new PageableResult<>(pageRequest.getPageNumber(), tierCodeList);
        }
        return results;
    }

    /**
     * Get all tier codes with pagination include count.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of tier code.
     */
    private Page<ProductBrandTier> findTierCodesWithCount(String id, String description, Pageable pageRequest) {
        Page<ProductBrandTier> tierCodes;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all tier codes.
            tierCodes = this.repositoryWithCount.findAll(pageRequest);
        } else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all tier codes filter by tier code id.
            tierCodes = this.repositoryWithCount.findById(id, pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            // get all tier codes filter by tier code name.
            tierCodes = this.repositoryWithCount.findByProductBrandNameIgnoreCaseContaining(description, pageRequest);
        } else {
            // get all tier codes filter by tier code id and tier code name.
            tierCodes = this.repositoryWithCount.findByIdAndName(id, description, pageRequest);
        }
        return tierCodes;
    }

    /**
     * Get all tier codes with pagination not include count.
     *
     * @param id The id to search.
     * @param description The description to search.
     * @param pageRequest The page request for pagination.
     * @return the page of product brands.
     */
    private List<ProductBrandTier> findTierCodesWithoutCount(String id, String description, Pageable pageRequest) {
        List<ProductBrandTier> tierCodes;
        if (StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all tier codes.
            tierCodes = this.repository.findAllByPage(pageRequest);
        } else if (!StringUtils.isEmpty(id) && StringUtils.isEmpty(description)) {
            // get all tier codes filter by tier code id.
            tierCodes = this.repository.findById(id, pageRequest);
        } else if (StringUtils.isEmpty(id) && !StringUtils.isEmpty(description)) {
            // get all tier codes filter by tier code name.
            tierCodes = this.repository.findByProductBrandNameIgnoreCaseContaining(description, pageRequest);
        } else {
            // get all tier codes filter by tier code id and tier code name.
            tierCodes = this.repository.findByIdAndName(id, description, pageRequest);
        }
        return tierCodes;
    }

    /**
     * Adds new tier codes.
     *
     * @param tierCodeDescriptions the tier code descriptions to add.
     * @return the newly added tier codes.
     */
    public List<ProductBrandTier> addTierCodes(List<ProductBrandTier> tierCodeDescriptions) {
        List<ProductBrandTier> newTierCodes = new ArrayList<>();
        if (CollectionUtils.isEmpty(tierCodeDescriptions)) {
            return newTierCodes;
        }
        Integer maxId = this.repository.findTop1ByOrderByProductBrandTierCodeDesc().getProductBrandTierCode();
        for (ProductBrandTier tierCodeDescription : tierCodeDescriptions) {
            tierCodeDescription.setProductBrandName(tierCodeDescription.getProductBrandName());
            tierCodeDescription.setProductBrandTierCode(++maxId);
            tierCodeDescription.setOwnerBrand(ProductBrandTier.DEFAULT_OWN_BRND_SW);
            newTierCodes.add(tierCodeDescription);
        }

        return this.repository.save(newTierCodes);
    }

    /**
     * Saves an updated tier code. Throws error if description is already in use.
     *
     * @param tierCode the tier code to be updated.
     * @return the updated tier code.
     */
    public ProductBrandTier updateTierCode(ProductBrandTier tierCode) {
        List<ProductBrandTier> tierCodes =
                this.repository.findByTrimmedProductBrandNameIgnoreCase(tierCode.getProductBrandName());
        if (!CollectionUtils.isEmpty(tierCodes)) {
            for (ProductBrandTier duplicateTierCode : tierCodes) {
                if (!duplicateTierCode.getProductBrandTierCode().equals(tierCode.getProductBrandTierCode())) {
            throw new IllegalArgumentException(TierCodeController.ALREADY_EXISTS_ERROR_MESSAGE);
        }
            }
        }
        return this.repository.save(tierCode);
    }

    /**
     * Deletes a tier code. Throws exception if the tier code is tied to a product.
     *
     * @param tierCodeId the id of the Tier Code to delete.
     */
    public void deleteTierCode(Integer tierCodeId) {
        List<ProductBrand> productBrands = this.productBrandRepository.findFirstByProductBrandTierProductBrandTierCode(tierCodeId);
        if (!CollectionUtils.isEmpty(productBrands)) {
            throw new IllegalArgumentException(TierCodeController.RELATIONSHIP_ERROR_MESSAGE);
        }
        ProductBrandTier tierCodeToDelete = this.repository.findOne(tierCodeId);
        if (tierCodeToDelete != null) {
            this.repository.delete(tierCodeToDelete);
        }
    }

    /**
     * Returns a list of Tier Code by code or description.
     *
     * @param searched the search parameters, to be searched against both id and description.
     * @param page the page number.
     * @param pageSize the page size.
     * @return a list of Tier Code by code or description.
     */
    public List<ProductBrandTier> findAllByTierCodeCodeOrBrandName(String searched, int page, int pageSize) {
        Pageable pageRequest = new PageRequest(page, pageSize, ProductBrandTier.getDefaultSort());
        return this.repository.findByProductBrandTierCodeIgnoreCaseContainingOrProductBrandNameIgnoreCaseContaining(searched, searched, pageRequest);
    }

    /**
     * Validate Tier Code is existed or not.
     *
     * @param tierCodes the list of added TopToTops.
     * @return the list of existed TierCode.
     */
    public List<ProductBrandTier> validateTierCodes(List<ProductBrandTier> tierCodes) {
        List<ProductBrandTier> newTierCodes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tierCodes)) {
            for (ProductBrandTier tierCode : tierCodes) {
                List<ProductBrandTier> tierCodeList =
                        this.repository.findByTrimmedProductBrandNameIgnoreCase(tierCode.getProductBrandName());
                if (CollectionUtils.isNotEmpty(tierCodeList)) {
                    newTierCodes.add(tierCode);
                }
            }
        }
        return newTierCodes;
    }
}
