/*
 *  StateWarningsController.java
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.stateWarnings;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductStateWarning;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
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
 * code table state warning controller.
 *
 * @author vn87351
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + StateWarningsController.CODE_TABLE_STATE_WARNINGS_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_STATE_WARNING)
public class StateWarningsController {

    private static final Logger logger = LoggerFactory.getLogger(StateWarningsController.class);

    protected static final String CODE_TABLE_STATE_WARNINGS_URL = "/codeTable/stateWarnings";

    private static final String URL_GET_ALL_STATE_WARNINGS = "/getAllStateWarnings";
    private static final String URL_ADD_NEW_STATE_WARNINGS = "/addNewStateWarnings";
    private static final String URL_UPDATE_STATE_WARNINGS = "/updateStateWarnings";
    private static final String URL_DELETE_STATE_WARNINGS = "/deleteStateWarnings";

    /**
     * Log	messages.
     */
    private static final String FIND_ALL_STATE_WARNING_MESSAGE = "User %s from IP %s requested to find all state warnings. ";
    private static final String ADD_NEW_STATE_WARNINGS_MESSAGE = "User %s from IP %s requested to add new state warnings. ";
    private static final String UPDATE_STATE_WARNINGS_MESSAGE = "User %s from IP %s requested to update state warnings. ";
    private static final String DELETE_STATE_WARNINGS_MESSAGE = "User %s from IP %s requested to delete state warnings. ";

    private static final String DELETE_SUCCESS_MESSAGE = "Successfully Deleted.";
    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
    private static final String ADD_NEW_SUCCESS_MESSAGE = "Successfully Added.";

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private StateWarningsService stateWarningsService;

    /**
     * get list state warning from database.
     *
     * @return List value of process.
     * @author vn87351
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = StateWarningsController.URL_GET_ALL_STATE_WARNINGS)
    public List<ProductStateWarning> findAllStateWarnings(HttpServletRequest request) {

        StateWarningsController.logger.info(String.format(StateWarningsController.FIND_ALL_STATE_WARNING_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.stateWarningsService.findAllStateWarnings();
    }

    /**
     * Add new state warnings.
     *
     * @param stateWarnings the list of state warnings to add.
     * @param request       the http servlet request.
     * @return list of state warnings after add new and message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = StateWarningsController.URL_ADD_NEW_STATE_WARNINGS)
    public ModifiedEntity<List<ProductStateWarning>> addNewStateWarnings(@RequestBody List<ProductStateWarning> stateWarnings,
                                                                         HttpServletRequest request) {
        StateWarningsController.logger.info(String.format(StateWarningsController.ADD_NEW_STATE_WARNINGS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        this.stateWarningsService.addStateWarnings(stateWarnings);
        return new ModifiedEntity<>(this.stateWarningsService.findAllStateWarnings(), StateWarningsController.ADD_NEW_SUCCESS_MESSAGE);
    }

    /**
     * Update state warnings.
     *
     * @param stateWarnings the list of state warnings to update.
     * @param request       the http servlet request.
     * @return list of state warnings after update and message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = StateWarningsController.URL_UPDATE_STATE_WARNINGS)
    public ModifiedEntity<List<ProductStateWarning>> updateStateWarnings(@RequestBody List<ProductStateWarning> stateWarnings,
                                                                         HttpServletRequest request) {
        StateWarningsController.logger.info(String.format(StateWarningsController.UPDATE_STATE_WARNINGS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        this.stateWarningsService.updateStateWarning(stateWarnings);
        return new ModifiedEntity<>(this.stateWarningsService.findAllStateWarnings(), StateWarningsController.UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Delete state warnings.
     *
     * @param stateWarnings the list of state warnings to delete.
     * @param request       the http servlet request.
     * @return list of state warnings after delete and message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = StateWarningsController.URL_DELETE_STATE_WARNINGS)
    public ModifiedEntity<List<ProductStateWarning>> deleteStateWarnings(@RequestBody List<ProductStateWarning> stateWarnings,
                                                                         HttpServletRequest request) {
        StateWarningsController.logger.info(String.format(StateWarningsController.DELETE_STATE_WARNINGS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        this.stateWarningsService.deleteStateWarning(stateWarnings);
        return new ModifiedEntity<>(this.stateWarningsService.findAllStateWarnings(), StateWarningsController.DELETE_SUCCESS_MESSAGE);
    }
}
