/*
 *  CodeTableFactoryController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.factory;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Country;
import com.heb.pm.entity.Factory;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents code table factory information.
 *
 * @author vn70516
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + CodeTableFactoryController.CODE_TABLE_FACTORY_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_FACTORY)
public class CodeTableFactoryController {

    protected static final String CODE_TABLE_FACTORY_URL = "/codeTable/factory";
    private static final Logger logger = LoggerFactory.getLogger(CodeTableFactoryController.class);

    // Log messages.
    private static final String FIND_ALL_FACTORY_MESSAGE = "User %s from IP %s requested all factory.";
    private static final String FIND_ALL_COUNTRY_MESSAGE = "User %s from IP %s requested all country.";
    private static final String DELETE_FACTORIES_MESSAGE = "User %s from IP %s requested delete the list of factory [%s]";
    private static final String ADD_FACTORY_MESSAGE = "User %s from IP %s requested insert a factory [%s]";
    private static final String UPDATE_FACTORY_MESSAGE = "User %s from IP %s requested update a factory [%s]";

    private static final String DELETE_SUCCESS_MESSAGE = "Deleted successfully.";
    private static final String UPDATE_SUCCESS_MESSAGE = "Updated successfully.";
    private static final String ADD_SUCCESS_MESSAGE = "A factory is added successfully.";

    private static final String FIND_ALL_FACTORY = "/findAllFactory";
    private static final String FIND_ALL_COUNTRY = "/findAllCountry";
    private static final String DELETE_LIST_OF_FACTORY = "/deleteFactories";
    private static final String ADD_NEW_FACTORY = "/addNewFactory";
    private static final String UPDATE_FACTORY = "/updateFactory";

    @Autowired
    private CodeTableFactoryService service;

    @Autowired
    private UserInfo userInfo;

    /**
     * Returns a list of all factory default order by factory id.
     *
     * @param request The HTTP request that initiated this call.
     * @return A List of all factory order by factory id.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = CodeTableFactoryController.FIND_ALL_FACTORY)
    public List<Factory> findAllFactory(HttpServletRequest request) {
        //show log message when init method
        CodeTableFactoryController.logger.info(String.format(CodeTableFactoryController.FIND_ALL_FACTORY_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        //return all data from service
        return this.service.findAllFactory();
    }

    /**
     * Returns a list of all country default order by country name.
     *
     * @param request The HTTP request that initiated this call.
     * @return A List of all country order by country name.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = CodeTableFactoryController.FIND_ALL_COUNTRY)
    public List<Country> findAllCountry(HttpServletRequest request) {
        //show log message when init method
        CodeTableFactoryController.logger.info(String.format(CodeTableFactoryController.FIND_ALL_COUNTRY_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        //return all data from service
        return this.service.findAllCountry();
    }

    /**
     * Add new a factory in code table.
     * @param factory  The factory requested to insert.
     * @param request  The HTTP request that initiated this call.
     * @return  The list of factory after add new and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = CodeTableFactoryController.ADD_NEW_FACTORY)
    public ModifiedEntity<List<Factory>> addNewFactory(@RequestBody Factory factory, HttpServletRequest request) {
        //show log message when init method
        CodeTableFactoryController.logger.info(String.format(CodeTableFactoryController.ADD_FACTORY_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(), factory.toString()));
        //delete the list of factory handle.
        this.service.addNewFactory(factory);
        // return list of factory after deleted successfully.
        return new ModifiedEntity<>(this.service.findAllFactory(), ADD_SUCCESS_MESSAGE);
    }

    /**
     * Update information a factory in code table.
     * @param factory  The factory requested to update.
     * @param request  The HTTP request that initiated this call.
     * @return  The list of factory after update and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = CodeTableFactoryController.UPDATE_FACTORY)
    public ModifiedEntity<List<Factory>> updateFactory(@RequestBody Factory factory, HttpServletRequest request) {
        //show log message when init method
        CodeTableFactoryController.logger.info(String.format(CodeTableFactoryController.UPDATE_FACTORY_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(), factory.toString()));
        //delete the list of factory handle.
        this.service.updateFactory(factory);
        // return list of factory after deleted successfully.
        return new ModifiedEntity<>(this.service.findAllFactory(), UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Delete a list of factory in code table.
     * @param factories  The list of factory requested to delete.
     * @param request       The HTTP request that initiated this call.
     * @return  The list of factory after deleted and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = CodeTableFactoryController.DELETE_LIST_OF_FACTORY)
    public ModifiedEntity<List<Factory>> deleteFactories(@RequestBody List<Factory> factories, HttpServletRequest request) {
        //show log message when init method
        CodeTableFactoryController.logger.info(String.format(CodeTableFactoryController.DELETE_FACTORIES_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(factories)));
        //delete the list of factory handle.
        this.service.deleteFactories(factories);
        // return list of factory after deleted successfully.
        return new ModifiedEntity<>(this.service.findAllFactory(), DELETE_SUCCESS_MESSAGE);
    }
}
