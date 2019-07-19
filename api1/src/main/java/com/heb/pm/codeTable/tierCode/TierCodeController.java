/*
 *  TierCodeController
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.tierCode;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductBrandTier;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Rest endpoint for tier code.
 *
 * @author vn87351
 * @since 2.41.0
 */
@RestController()
@RequestMapping(TierCodeController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_TIER_CODE)
public class TierCodeController {

    private static final Logger logger = LoggerFactory.getLogger(TierCodeController.class);

    protected static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/codeTable/tierCode";

    private static final String URL_GET_TIER_CODE_PAGE = "/findTierCodeList";
    private static final String URL_FILTER_TIER_CODES = "/filterTierCodes";

    // Log Messages.
    private static final String DELETE_TIER_CODE_MESSAGE = "User %s from IP %s has requested to delete the " +
            "ProductLine with the id: %s.";
    private static final String FIND_TIER_CODE_MESSAGE = "User %s from IP %s requested to find product " +
            "lines by page: %d, page size: %d, id: '%s', description: '%s', and include count: %s.";
    private static final String FILTER_TIER_CODE_MESSAGE = "User %s from IP %s requested to find product " +
            "lines by page: %d, page size: %d, and searched term: '%s'.";
    private static final String UPDATE_TIER_CODE_MESSAGE = "User %s from IP %s has requested to update the " +
            "ProductLine with the id: %s to have the description: '%s'.";
    private static final String ADD_TIER_CODE_MESSAGE = "User %s from IP %s requested to add product " +
            "lines.";

    private static final String ADD_SUCCESS_MESSAGE = "Successfully added.";
    private static final String DEFAULT_DELETE_SUCCESS_MESSAGE = "Successfully deleted";
    private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE = "Successfully updated.";

    public static final String ALREADY_EXISTS_ERROR_MESSAGE = "Tier Code already exists.";
    public static final String RELATIONSHIP_ERROR_MESSAGE = "Unable to delete - In relationship code table.";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_NO_FILTER = "";

    @Autowired
    private TierCodeService service;

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private MessageSource messageSource;

    /**
     * Get list of tier code.
     *
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @param includeCount Whether count of total records needs to be done.
     * @param request The http servlet request.
     * @return The page of tier code.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = TierCodeController.URL_GET_TIER_CODE_PAGE)
    public PageableResult<ProductBrandTier> findTierCodeList(@RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                       @RequestParam(value = "productBrandTierCode", required = false, defaultValue = "") String id,
                                                       @RequestParam(value = "productBrandName", required = false, defaultValue = "") String description,
                                                       @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                       HttpServletRequest request) {

        int pageNo = page == null ? TierCodeController.DEFAULT_PAGE : page;
        int size = pageSize == null ? TierCodeController.DEFAULT_PAGE_SIZE : pageSize;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        this.logGetTierCodesPage(request.getRemoteAddr(), pageNo, size, id, description, count);

        return this.service.findTierCodeList(pageNo, size, id, description, count);
    }

    /**
     * Get list of tier code with filter condition.
     * @param page the page number.
     * @param pageSize the page size.
     * @param searched the term to search against.
     * @param request The http servlet request.
     * @return the list of tier code.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = TierCodeController.URL_FILTER_TIER_CODES)
    public List<ProductBrandTier> filterTierCodes(@RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "searched", required = false, defaultValue = "") String searched,
                                                  HttpServletRequest request) {

        int pageNo = page == null ? TierCodeController.DEFAULT_PAGE : page;
        int size = pageSize == null ? TierCodeController.DEFAULT_PAGE_SIZE : pageSize;
        searched = StringUtils.isEmpty(searched) ? TierCodeController.DEFAULT_NO_FILTER : searched;
        this.logFilterTierCodes(request.getRemoteAddr(), pageNo, size, searched);

        return this.service.findAllByTierCodeCodeOrBrandName(searched, page, size);
    }

    /**
     * Adds new tier codes.
     *
     * @param tierCodeDescriptions the tier code descriptions
     * @param request the HTTP request that initiated this call.
     * @return
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "addTierCodes")
    public ModifiedEntity<List<ProductBrandTier>> addTierCodes(@RequestBody List<ProductBrandTier> tierCodeDescriptions,
                                                             HttpServletRequest request) {

        this.logAddTierCodes(request.getRemoteAddr());
        List<ProductBrandTier> validateList = this.service.validateTierCodes(tierCodeDescriptions);
        if (CollectionUtils.isNotEmpty(validateList)) {
            return new ModifiedEntity<>(validateList, TierCodeController.ALREADY_EXISTS_ERROR_MESSAGE);
        }
        List<ProductBrandTier> tierCodes = this.service.addTierCodes(tierCodeDescriptions);
        return new ModifiedEntity<>(tierCodes, ADD_SUCCESS_MESSAGE);
    }

    /**
     * Updates a TierCode's Description.
     *
     * @param tierCode The tierCode to be updated.
     * @param request the HTTP request that initiated this call.
     * @return The updated TierCode and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.PUT, value = "updateTierCode")
    public ModifiedEntity<ProductBrandTier> updateTierCode(@RequestBody ProductBrandTier tierCode,
                                                                      HttpServletRequest request) {
        this.logUpdate(request.getRemoteAddr(), tierCode);
        ProductBrandTier updatedTierCode = this.service.updateTierCode(tierCode);
        return new ModifiedEntity<>(updatedTierCode, TierCodeController.DEFAULT_UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Deletes a TierCode.
     *
     * @param productBrandTierCode The TierCode's productBrandTierCode to be deleted.
     * @param request the HTTP request that initiated this call.
     * @return The deleted TierCode ID and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "deleteTierCode")
    public ModifiedEntity<String> deleteProductLine(@RequestParam String productBrandTierCode,
                                                    HttpServletRequest request) {
        this.logDelete(request.getRemoteAddr(), productBrandTierCode);
        this.service.deleteTierCode(Integer.parseInt(productBrandTierCode));
        return new ModifiedEntity<>(productBrandTierCode, TierCodeController.DEFAULT_DELETE_SUCCESS_MESSAGE);
    }

    /**
     * Logs a user's request to get all tier codes.
     *
     * @param ipAddress The user's ip.
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @param includeCount Whether count of total records needs to be done.
     */
    private void logGetTierCodesPage(String ipAddress, int page, int pageSize, String id, String description, boolean includeCount) {
        TierCodeController.logger.info(String.format(TierCodeController.FIND_TIER_CODE_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, id, description, includeCount));
    }

    /**
     * Logs a user's request to get all filter tier codes.
     *
     * @param ipAddress the IP address
     * @param page The page number.
     * @param pageSize  The page size.
     * @param searched Search string
     */
    private void logFilterTierCodes(String ipAddress, int page, int pageSize, String searched) {
        TierCodeController.logger.info(String.format(TierCodeController.FILTER_TIER_CODE_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, searched));
    }

    /**
     * Logs a users request to add tier codes.
     *
     * @param ip The user's IP.
     */
    private void logAddTierCodes(String ip) {
        TierCodeController.logger.info(String.format(TierCodeController.ADD_TIER_CODE_MESSAGE,
                this.userInfo.getUserId(), ip));
    }

    /**
     * Logs a user's request to delete a tier code.
     *
     * @param ip The IP address the user is logged in from.
     * @param id the id of the Tier Code to be deleted.
     */
    private void logDelete(String ip, String id) {
        TierCodeController.logger.info(String.format(TierCodeController.DELETE_TIER_CODE_MESSAGE,
                this.userInfo.getUserId(), ip, id));
    }

    /**
     * Logs a user's request to update a tier code.
     *
     * @param ip The IP address the user is logged in from.
     * @param tierCode The tier code to be updated.
     */
    private void logUpdate(String ip, ProductBrandTier tierCode) {
        TierCodeController.logger.info(String.format(TierCodeController.UPDATE_TIER_CODE_MESSAGE,
                this.userInfo.getUserId(), ip, tierCode.getProductBrandTierCode(), tierCode.getProductBrandName()));
    }
}
