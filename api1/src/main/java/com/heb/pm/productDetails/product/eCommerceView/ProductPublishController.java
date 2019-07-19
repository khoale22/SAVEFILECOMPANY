/*
 *  ProductPublishController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Represents product publish controller.
 *
 * @author vn70516
 * @since 2.0.14
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductPublishController.PRODUCT_PUBLISH)
@AuthorizedResource(ResourceConstants.PRODUCT_ECOMMERCE_VIEW_PUBLISH)
public class ProductPublishController {

    private static final Logger logger = LoggerFactory.getLogger(ProductPublishController.class);
    protected static final String PRODUCT_PUBLISH = "/productPublish";
    // urls
    private static final String PUBLISH_ECOMMERCE_DATA_TO_HEB_COM = "/publishECommerceViewDataToHebCom";
    //logs
    private static final String PUBLISH_ECOMMERCE_DATA_TO_HEB_COM_MESSAGE = "User %s from IP %s requested to publish eCommerce data to HEB COM.";
    private static final String PUBLISHED_SUCCESS_MESSAGE = "Successfully Published.";
    @Autowired
    private ProductECommerceViewService service;

    @Autowired
    private UserInfo userInfo;

    /**
     *  Call the service to public eCommerce data to HEB Com.
     *
     * @param product         the ECommerceViewDetails object.
     * @param request  The HTTP request that initiated this call.
     * @return Success message.
     * @throws Exception
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductPublishController.PUBLISH_ECOMMERCE_DATA_TO_HEB_COM)
    public ModifiedEntity publishECommerceViewDataToHebCom(@RequestBody ECommerceViewDetails product,
                                                           HttpServletRequest request) throws Exception{
        ProductPublishController.logger.info(String.format(ProductPublishController.PUBLISH_ECOMMERCE_DATA_TO_HEB_COM_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        this.service.publishECommerceViewDataToHebCom(product, userInfo.getUserId());
        return new ModifiedEntity<>(null, PUBLISHED_SUCCESS_MESSAGE);
    }
}