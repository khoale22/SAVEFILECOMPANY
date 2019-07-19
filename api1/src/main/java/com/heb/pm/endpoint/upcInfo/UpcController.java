/*
 * UpcController.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.endpoint.upcInfo;


import com.heb.pm.ApiConstants;
import com.heb.pm.entity.Upc;
import com.heb.pm.product.UpcService;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * REST endpoint for UPCs.
 *
 * @author s769046
 * @since 2.21.0
 */

@RestController()
@RequestMapping(ApiConstants.BASE_WSAG_APPLICATION_URL + UpcController.UPC_BASE_URL)
public class UpcController {
    private static final Logger logger = LoggerFactory.getLogger(UpcController.class);

    protected static final String UPC_BASE_URL = "/upc";

    private static final String FIND_ALL_STATUS_BY_UPCS =
            "User %s from IP %s has requested all statuses by UPC: %d";

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private UpcService upcService;

    /**
     * Get all statuses by UPCs
     *
     * @param request the request
     * @return the list
     */
    @RequestMapping(method = RequestMethod.GET, value = "{upc}")
    public Upc findUpcInfoByUpc(@PathVariable(value="upc") Long upc, HttpServletRequest request, HttpServletResponse response) {
        this.logFindUpcInfoByUpc(request.getRemoteAddr(),upc);
        Upc upcInfo = this.upcService.getUpcInfo(upc);
        if(upcInfo == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return upcInfo;

    }

    /**
     * Log's a user's request to get all upc info by upc.
     *
     * @param ip The IP address the user is logged in from
     */
    private void logFindUpcInfoByUpc(String ip, long upc){
        UpcController.logger.info(String.format(UpcController.FIND_ALL_STATUS_BY_UPCS, this.userInfo.getUserId(), ip, upc));
    }

}
