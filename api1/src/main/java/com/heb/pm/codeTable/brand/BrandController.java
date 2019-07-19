/*
 *  ProductBrandController
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of H-E-B.
 */
package com.heb.pm.codeTable.brand;


import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.codeTable.productBrand.ProductBrandController;
import com.heb.pm.entity.ProductBrand;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to get the list of code table product brand information.
 *
 * @author vn87351
 * @since 2.41.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + BrandController.CODE_TABLE_BRAND_OPTION_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_BRAND_5)
public class BrandController {
    private static final Logger logger = LoggerFactory.getLogger(ProductBrandController.class);
    protected static final String CODE_TABLE_BRAND_OPTION_URL = "/codeTable/brand";
    private static final String URL_GET_PRODUCT_BRAND_PAGE = "/findBrands";
    private static final String UPDATE_BRAND = "/updateBrand";
    private static final String DELETE_BRAND = "/deleteBrand";
    private static final String ADD_BRANDS = "/addBrands";

    // Log Messages.
    private static final String DELETE_PRODUCT_BRAND_MESSAGE = "User %s from IP %s has requested to delete the " +
            "ProductBrand with the id: %d.";
    private static final String FIND_BRAND_MESSAGE = "User %s from IP %s requested to find brand " +
            "by page: %d, page size: %d, productBrandId: '%s', productBrandDescription: '%s', and include count: %s.";
    private static final String UPDATE_PRODUCT_BRAND_MESSAGE = "User %s from IP %s has requested to update the " +
            "ProductBrand with the id: %s to have the description: '%s'.";
    private static final String ADD_PRODUCT_BRAND_MESSAGE = "User %s from IP %s requested to add product " +
            "brands.";
    private static final String BRAND_ALREADY_EXISTS_ERROR_MESSAGE = "Brand already exists.";

    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully updated";
    private static final String DELETE_SUCCESS_MESSAGE = "Successfully deleted";
    private static final String ADD_SUCCESS_MESSAGE = "Successfully added.";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private BrandService service;

    @Autowired
    private UserInfo userInfo;

    /**
     * Get all brand records.
     *
     * @param page                    The page number.
     * @param pageSize                The page size.
     * @param productBrandId          The productBrandId to search.
     * @param productBrandDescription The productBrandDescription to search.
     * @param includeCount            Whether count of total records needs to be done.
     * @param request                 The http servlet request.
     * @return The page of product brands.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = BrandController.URL_GET_PRODUCT_BRAND_PAGE)
    public PageableResult<ProductBrand> findBrands(@RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "productBrandId", required = false, defaultValue = "") String productBrandId,
                                                   @RequestParam(value = "productBrandDescription", required = false, defaultValue = "") String productBrandDescription,
                                                   @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                   HttpServletRequest request) {

        int pageNo = page == null ? BrandController.DEFAULT_PAGE : page;
        int size = pageSize == null ? BrandController.DEFAULT_PAGE_SIZE : pageSize;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        this.logGetProductBrandsPage(request.getRemoteAddr(), pageNo, size, productBrandId, productBrandDescription, count);

        return this.service.findByPage(pageNo, size, productBrandId, productBrandDescription, count);
    }

    /**
     * Updates a brand Description.
     *
     * @param productBrand The productBrand to be updated.
     * @param request      the HTTP request that initiated this call.
     * @return The updated productBrand and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.PUT, value = BrandController.UPDATE_BRAND)
    public ModifiedEntity<ProductBrand> updateProductBrand(@RequestBody ProductBrand productBrand,
                                                                      HttpServletRequest request) {
        this.logUpdate(request.getRemoteAddr(), productBrand);
        ProductBrand updatedProductBrand = this.service.updateProductBrand(productBrand);
        return new ModifiedEntity<>(updatedProductBrand, BrandController.UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Deletes a productBrand.
     *
     * @param productBrandId The productBrand's productBrandId to be deleted.
     * @param request        the HTTP request that initiated this call.
     * @return The deleted productBrandId and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = DELETE_BRAND)
    public ModifiedEntity<Long> deleteProductBrand(@RequestParam Long productBrandId,
                                                   HttpServletRequest request) {
        this.logDelete(request.getRemoteAddr(), productBrandId);
        this.service.deleteProductBrand(productBrandId);
        return new ModifiedEntity<>(productBrandId, BrandController.DELETE_SUCCESS_MESSAGE);
    }

    /**
     * Adds new brands.
     *
     * @param productBrands the list of productBrand.
     * @param request       the HTTP request that initiated this call.
     * @return The productBrands and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = BrandController.ADD_BRANDS)
    public ModifiedEntity<List<ProductBrand>> addProductBrands(@RequestBody List<ProductBrand> productBrands,
                                                               HttpServletRequest request) {

        this.logAdd(request.getRemoteAddr());
        List<ProductBrand> validateList = this.service.validateBrands(productBrands);
        if (CollectionUtils.isNotEmpty(validateList)) {
            return new ModifiedEntity<>(validateList, BrandController.BRAND_ALREADY_EXISTS_ERROR_MESSAGE);
        }

        List<ProductBrand> productBrandList = this.service.addProductBrands(productBrands);
        return new ModifiedEntity<>(productBrandList, BrandController.ADD_SUCCESS_MESSAGE);
    }

    /**
     * Logs a user's request to get all brands.
     *
     * @param ipAddress    The user's ip.
     * @param page         The page number.
     * @param pageSize     The page size.
     * @param id           The id to search.
     * @param description  The description to search.
     * @param includeCount Whether count of total records needs to be done.
     */
    private void logGetProductBrandsPage(String ipAddress, int page, int pageSize, String id, String description, boolean includeCount) {
        BrandController.logger.info(String.format(BrandController.FIND_BRAND_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, id, description, includeCount));
    }


    /**
     * Logs a user's request to update a productBrand.
     *
     * @param ip           The IP address the user is logged in from.
     * @param productBrand The productBrand to be updated.
     */
    private void logUpdate(String ip, ProductBrand productBrand) {
        BrandController.logger.info(String.format(BrandController.UPDATE_PRODUCT_BRAND_MESSAGE,
                this.userInfo.getUserId(), ip, productBrand.getProductBrandId(), productBrand.getProductBrandDescription()));
    }

    /**
     * Logs a user's request to delete a productBrand.
     *
     * @param ip             The IP address the user is logged in from.
     * @param productBrandId the productBrandId of the ProductBrand to be deleted.
     */
    private void logDelete(String ip, Long productBrandId) {
        BrandController.logger.info(String.format(BrandController.DELETE_PRODUCT_BRAND_MESSAGE,
                this.userInfo.getUserId(), ip, productBrandId));
    }

    /**
     * Logs a users request to add product brand.
     *
     * @param ip The user's IP.
     */
    private void logAdd(String ip) {
        BrandController.logger.info(String.format(BrandController.ADD_PRODUCT_BRAND_MESSAGE,
                this.userInfo.getUserId(), ip));
    }

}
