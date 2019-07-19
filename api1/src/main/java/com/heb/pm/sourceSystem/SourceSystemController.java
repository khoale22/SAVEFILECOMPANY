/*
 * SourceSystemController.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.sourceSystem;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.ecommerce.AttributeMaintenanceController;
import com.heb.pm.entity.SourceSystem;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for Source System.
 *
 * @author s769046
 * @since 2.21.0
 */

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + SourceSystemController.SOURCE_SYSTEM_URL )
@AuthorizedResource(ResourceConstants.CODE_TABLE_SOURCE_SYSTEM)
public class SourceSystemController {

    private static final Logger logger = LoggerFactory.getLogger(SourceSystemController.class);

    protected static final String SOURCE_SYSTEM_URL  = "/sourceSystem";

    private static final String FIND_ALL_SOURCE_SYSTEMS =
            "User %s from IP %s has requested all source systems";

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private SourceSystemService sourceSystemService;

    /**
     * Get all source systems
     *
     * @param request the request
     * @return the list
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "findAll")
    public List<SourceSystem> findAllSourceSystems(HttpServletRequest request) {
        this.logFindAllSourceSystems(request.getRemoteAddr());
        return this.sourceSystemService.findAllSourceSystems();
    }

    /**
     * Log's a user's request to get source system values
     *
     * @param ip The IP address the user is logged in from
     */
    private void logFindAllSourceSystems(String ip){
        SourceSystemController.logger.info(String.format(SourceSystemController.FIND_ALL_SOURCE_SYSTEMS, this.userInfo.getUserId(), ip));
    }
}
