/*
 *  CheckDigitCalculatorController
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.utilities.checkDigitCalculator;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemMaster;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * Represents check digit calculator controller.
 *
 * @author vn75469
 * @since 2.16.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL)
@AuthorizedResource(ResourceConstants.CHECK_DIGIT_CALCULATOR)
public class CheckDigitCalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CheckDigitCalculatorController.class);
    protected static final String CHECK_DIGIT_URL = "/utilities/checkDigitCalculator/{upcCode}";
    private static final String CHECK_DIGIT_LOG = "User %s from IP %s requested get item with UPC code = %s";
    @Autowired
    private CheckDigitCalculatorService service;
    @Autowired
    private UserInfo userInfo;
    /**
     * Get Item which specific upc code.
     *
     * @param upcCode code.
     * @param request The HTTP request that initiated this call.
     * @return ItemMaster object.
     */
    @ViewPermission
    @RequestMapping(value=CHECK_DIGIT_URL, method = RequestMethod.GET)
    public ItemMaster getItems(@PathVariable Long upcCode, HttpServletRequest request) {
        CheckDigitCalculatorController.logger.info(String.format(CheckDigitCalculatorController.CHECK_DIGIT_LOG,
                this.userInfo.getUserId(), request.getRemoteAddr(), upcCode));
        return this.service.getItems(upcCode);
    }
}
