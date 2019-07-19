/*
 *  BreakPackController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.breakPack;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductRelationship;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.heb.util.audit.AuditRecord;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents break pack information.
 *
 * @author vn70516
 * @since 2.15.0
 */

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + BreakPackController.BREAK_PACK)
@AuthorizedResource(ResourceConstants.PRODUCT_BREAK_PACK)
public class BreakPackController {
    protected static final String BREAK_PACK = "/breakPack";
    private static final Logger logger = LoggerFactory.getLogger(BreakPackController.class);

    //url request
    private static final String COUNT_PRODUCT_BREAK_PACK_BY_PRODUCT_ID = "/countProductRelationshipByProductId";
    private static final String GET_PRODUCT_BREAK_PACK_BY_PRODUCT_ID = "/getProductBreakPackByProductId";
    private static final String GET_PRODUCT_BY_UPC = "/getProductByUpc";
    private static final String UPDATE_PRODUCT_RELATIONSHIP = "/updateProductRelationship";
    private static final String GET_BREAK_PACK_ATTRIBUTES_AUDITS = "/getBreakPackAttributesAudits";

    //log message
    private static final String COUNT_PRODUCT_BREAK_PACK_BY_PRODUCT_ID_LOG_MESSAGE = "User %s from IP %s has requested count product break pack hierarchy by product id %s";
    private static final String GET_PRODUCT_BREAK_PACK_BY_PRODUCT_ID_LOG_MESSAGE = "User %s from IP %s has requested get all product break pack hierarchy by product id %s";
    private static final String GET_PRODUCT_BY_UPC_LOG_MESSAGE = "User %s from IP %s has requested get all product information by upc number %s";
    private static final String UPDATE_PRODUCT_RELATIONSHIP_LOG_MESSAGE = "User %s from IP %s has requested update product relationship";
    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
    private static final String GET_BREAK_PACK_ATTRIBUTES_AUDITS_LOG_MESSAGE = "User %s from IP %s has requested get break pack attribute";

    @Autowired
    private UserInfo userInfo;
    @Autowired
    private BreakPackService breakPackService;

    /**
     * Count product break pack hierarchy by product id.
     * @param productId - The product id
     * @param request - The HTTP request that initiated this call.
     * @return The list of ProductRelationship object
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = BreakPackController.COUNT_PRODUCT_BREAK_PACK_BY_PRODUCT_ID)
    public ModifiedEntity countProductRelationshipByProductId(@RequestParam(value = "productId") long productId,
                                                                    HttpServletRequest request) throws Exception{
        BreakPackController.logger.info(String.format(BreakPackController.COUNT_PRODUCT_BREAK_PACK_BY_PRODUCT_ID_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId));
        int count = this.breakPackService.countProductRelationshipByProductId(productId);
        return new ModifiedEntity<>(count, StringUtils.EMPTY);
    }

    /**
     * Get all product break pack hierarchy by product id.
     * @param productId - The product id
     * @param request - The HTTP request that initiated this call.
     * @return The list of ProductRelationship object
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = BreakPackController.GET_PRODUCT_BREAK_PACK_BY_PRODUCT_ID)
    public List<ProductRelationship> getProductBreakPackByProductId(@RequestParam(value = "productId") long productId,
                                                                    HttpServletRequest request) throws Exception{
        BreakPackController.logger.info(String.format(BreakPackController.GET_PRODUCT_BREAK_PACK_BY_PRODUCT_ID_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId));
        return this.breakPackService.getProductBreakPackByProductId(productId);
    }

    /**
     * Get all product information by upc number.
     * @param productId - The related product id
     * @param upc - The upc number
     * @param request - The HTTP request that initiated this call.
     * @return The ProductRelationship object contain product information will be relate to current product.
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = BreakPackController.GET_PRODUCT_BY_UPC)
    public ProductRelationship getProductByUpc(@RequestParam(value = "productId") long productId,
                                               @RequestParam(value = "upc") long upc,
                                               HttpServletRequest request) throws Exception{
        BreakPackController.logger.info(String.format(BreakPackController.GET_PRODUCT_BY_UPC_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), upc));
        return this.breakPackService.getProductByUpc(productId, upc);
    }

    /**
     * Call webservice to update product relationship.
     *
     * @param productRelationship the ProductRelationship entity to be updated.
     * @param request the http servlet request.
     * @return the modified entity.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = BreakPackController.UPDATE_PRODUCT_RELATIONSHIP)
    public ModifiedEntity updateProductRelationship(@RequestBody ProductRelationship productRelationship,
                                                    HttpServletRequest request) {
        BreakPackController.logger.info(String.format(BreakPackController.UPDATE_PRODUCT_RELATIONSHIP_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        this.breakPackService.updateProductRelationship(productRelationship);
        return new ModifiedEntity<>(null, BreakPackController.UPDATE_SUCCESS_MESSAGE);
    }
    /**
     * Retrieves break pack Attributes audit information.
     * @param prodId The Product ID that the audit is being searched on.
     * @param request The HTTP request that initiated this call.
     * @return The list of break pack Attributes audits attached to given product ID.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = BreakPackController.GET_BREAK_PACK_ATTRIBUTES_AUDITS)
    public List<AuditRecord> getBreakPackAttributesAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
        BreakPackController.logger.info(String.format(BreakPackController.GET_BREAK_PACK_ATTRIBUTES_AUDITS_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.breakPackService.getBreakPackAttributesAuditInformation(prodId);
    }
}