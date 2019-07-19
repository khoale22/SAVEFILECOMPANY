/*
 * AttributeMaintenanceController.java
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.pm.ecommerce;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.AttributeDomain;
import com.heb.pm.entity.EntityAttributeCode;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for Attribute Maintenance.
 *
 * @author a786878
 * @since 2.15.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + AttributeMaintenanceController.ATTRIBUTE_MAINTENANCE_URL )
@AuthorizedResource(ResourceConstants.ECOMMERCE_ATTRIBUTE_MAINTENANCE)
public class AttributeMaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttributeMaintenanceController.class);

    protected static final String ATTRIBUTE_MAINTENANCE_URL  = "/eCommerce/attributeMaintenance";

    // Log Messages
    private static final String GET_ATTRIBUTE_MAINTENANCE_TABLE =
            "User %s from IP %s has requested the attribute maintenance table for page %s and page size %s";
    private static final String GET_ATTRIBUTE_DETAILS =
            "User %s from IP %s has requested the attribute details for attribute with id %s";
    private static final String GET_ATTRIBUTE_VALUES =
            "User %s from IP %s has requested the attribute values for attribute with id %s";
    private static final String GET_ATTRIBUTE_DOMAINS =
            "User %s from IP %s has requested the attribute domain values";
    private static final String UPDATE_ATTRIBUTE_DETAILS =
            "User %s from IP %s has requested to update the attribute details for attribute with id %s";

    @Autowired private UserInfo userInfo;
    @Autowired private AttributeMaintenanceService attributeMaintenanceService;

	/**
	 * Get attribute maintenance table list
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "getAttributes")
    public PageableResult<Attribute> getAttributes(HttpServletRequest request, String attributeName, Long attributeId, Long sourceSystemId, boolean includeCounts, int page, int pageSize){

        this.logGetAttributeMaintenanceTable(request.getRemoteAddr(), page, pageSize);

        PageableResult<Attribute> pageableResult;
        Pageable pageable = new PageRequest(page, pageSize);

        Page<Attribute> result = attributeMaintenanceService.getAttributeMaintenanceTable(pageable, attributeName, sourceSystemId, attributeId);
        pageableResult = new PageableResult<>(page, result.getTotalPages(), result.getTotalElements(), result.getContent());

        return pageableResult;
    }

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "getAttributeDomains")
    public List<AttributeDomain> getAttributeDomains(HttpServletRequest request){

        this.logGetAttributeDomains(request.getRemoteAddr());

        return this.attributeMaintenanceService.getAttributeDomains();
    }

    /**
     * Get attribute details
     *
     * @param request the request
     * @return the attribute details
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "getAttributeDetails")
    public Attribute getAttributeDetails(HttpServletRequest request, Long id){

        this.logGetAttributeDetails(request.getRemoteAddr(), id);

        if (id == null) {
            return null;
        }

        return this.attributeMaintenanceService.getAttributeDetails(id);
    }

    /**
     * Get attribute values
     *
     * @param request the request
     * @return the attribute values
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "getAttributeValues")
    public  List<EntityAttributeCode> getAttributeValues(HttpServletRequest request, Long id){

        this.logGetAttributeValues(request.getRemoteAddr(), id);

        return this.attributeMaintenanceService.getAttributeValues(id);
    }

    /**
     * Update attribute details
     *
     * @param request
     * @param attribute the attribute to update
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "updateAttributeDetails")
    public void updateAttributeDetails(HttpServletRequest request, @RequestBody Attribute attribute) throws CheckedSoapException {
        Long id = attribute.getAttributeId();

        this.logUpdateAttributeDetails(request.getRemoteAddr(), id);

        this.attributeMaintenanceService.updateAttributeDetails(attribute);
    }

    /**
     * Log's a user's request to update attribute details
     *
     * @param ip The IP address the user is logged in from
     */
    private void logUpdateAttributeDetails(String ip, Long id){
        AttributeMaintenanceController.logger.info(String.format(AttributeMaintenanceController.UPDATE_ATTRIBUTE_DETAILS, this.userInfo.getUserId(), ip, id));
    }

    /**
     * Log's a user's request to get all records in attribute maintenance table
     *
     * @param ip The IP address the user is logged in from
     * @param page
     * @param pageSize
     */
    private void logGetAttributeMaintenanceTable(String ip, int page, int pageSize){
        AttributeMaintenanceController.logger.info(String.format(AttributeMaintenanceController.GET_ATTRIBUTE_MAINTENANCE_TABLE, this.userInfo.getUserId(), ip, page, pageSize));
    }

    /**
     * Log's a user's request to get attribute details for an attribute id
     *
     * @param ip The IP address the user is logged in from
     * @param id The attribute id
     */
    private void logGetAttributeDetails(String ip, Long id){
        AttributeMaintenanceController.logger.info(String.format(AttributeMaintenanceController.GET_ATTRIBUTE_DETAILS, this.userInfo.getUserId(), ip, id));
    }

    /**
     * Log's a user's request to get attribute values for an attribute id
     *
     * @param ip The IP address the user is logged in from
     * @param id The attribute id
     */
    private void logGetAttributeValues(String ip, Long id){
        AttributeMaintenanceController.logger.info(String.format(AttributeMaintenanceController.GET_ATTRIBUTE_VALUES, this.userInfo.getUserId(), ip, id));
    }

    /**
     * Log's a user's request to get attribute domain values
     *
     * @param ip The IP address the user is logged in from
     */
    private void logGetAttributeDomains(String ip){
        AttributeMaintenanceController.logger.info(String.format(AttributeMaintenanceController.GET_ATTRIBUTE_DOMAINS, this.userInfo.getUserId(), ip));
    }
}
