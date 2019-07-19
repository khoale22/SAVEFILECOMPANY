/*
 *  ChoiceTypeController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.choice;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ChoiceType;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
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
 * Represents code table choice type information.
 *
 * @author vn70516
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ChoiceTypeController.CODE_TABLE_CHOICE_TYPE_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_CHOICE_TYPE)
public class ChoiceTypeController {

    protected static final String CODE_TABLE_CHOICE_TYPE_URL = "/codeTable/choiceType";
    private static final Logger logger = LoggerFactory.getLogger(ChoiceTypeController.class);

    // Log messages.
    private static final String FIND_ALL_CHOICE_TYPE_MESSAGE = "User %s from IP %s requested all choice type.";
    private static final String ADD_NEW_CHOICE_TYPES_MESSAGE = "User %s from IP %s requested add new the list of choice [%s] type";
    private static final String UPDATE_CHOICE_TYPES_MESSAGE = "User %s from IP %s requested edit the list of choice [%s] type";
    private static final String DELETE_CHOICE_TYPES_MESSAGE = "User %s from IP %s requested add new the list of choice [%s] type";

    private static final String DELETE_SUCCESS_MESSAGE = "Deleted successfully.";
    private static final String UPDATE_SUCCESS_MESSAGE = "Updated successfully.";
    private static final String ADD_SUCCESS_MESSAGE = "Added successfully.";

    private static final String FIND_ALL_CHOICE_TYPE = "/findAllChoiceType";
    private static final String ADD_NEW_CHOICE_TYPES = "/addNewChoiceTypes";
    private static final String UPDATE_CHOICE_TYPES = "/updateChoiceTypes";
    private static final String DELETE_CHOICE_TYPES = "/deleteChoiceTypes";

    @Autowired
    private ChoiceService service;

    @Autowired
    private UserInfo userInfo;

    private LazyObjectResolver<List<ChoiceType>> choiceTypeResolver = new ChoiceTypeResolver();

    /**
     * Resolves a ChoiceType object. It will load the following properties:
     */
    private class ChoiceTypeResolver implements LazyObjectResolver<List<ChoiceType>> {
        @Override
        public void fetch(List<ChoiceType> choiceTypes) {
            choiceTypes.forEach((p) -> {
                if (p.getParentChoiceType() != null) {
                    p.getParentChoiceType().getChoiceTypeCode();
                    if (p.getParentChoiceType().getChoiceTypeList() != null) {
                        p.getParentChoiceType().getChoiceTypeList().size();
                    }
                }
            });
        }
    }

    /**
     * Returns a list of all choice type default order by choice type description.
     *
     * @param request The HTTP request that initiated this call.
     * @return A List of all choice type order by choice type description.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ChoiceTypeController.FIND_ALL_CHOICE_TYPE)
    public List<ChoiceType> findAllChoiceType(HttpServletRequest request) {
        //show log message when init method
        ChoiceTypeController.logger.info(String.format(ChoiceTypeController.FIND_ALL_CHOICE_TYPE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        List<ChoiceType> choiceTypes = this.service.findAllChoiceType();
        choiceTypeResolver.fetch(choiceTypes);
        //return all data from service
        return choiceTypes;
    }

    /**
     * Delete the list of choice type.
     *
     * @param choiceTypes The list of choice type to delete.
     * @param request     The HTTP request that initiated this call.
     * @return The list of choice type after deleted and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ChoiceTypeController.DELETE_CHOICE_TYPES)
    public ModifiedEntity<List<ChoiceType>> deleteVocabularies(@RequestBody List<ChoiceType> choiceTypes,
                                                               HttpServletRequest request) {
        //show log message when init method
        ChoiceTypeController.logger.info(String.format(ChoiceTypeController.DELETE_CHOICE_TYPES_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(choiceTypes)));
        //call handle from service
        this.service.deleteChoiceTypes(choiceTypes);
        //research data after delete successfully
        List<ChoiceType> choiceTypesRet = this.service.findAllChoiceType();
        choiceTypeResolver.fetch(choiceTypesRet);
        return new ModifiedEntity<>(choiceTypesRet, DELETE_SUCCESS_MESSAGE);
    }

    /**
     * Update information for the list of choice type.
     *
     * @param choiceTypes The list of choice type to update.
     * @param request     The HTTP request that initiated this call.
     * @return The list of choice type after update and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ChoiceTypeController.UPDATE_CHOICE_TYPES)
    public ModifiedEntity<List<ChoiceType>> updateChoiceTypes(@RequestBody List<ChoiceType> choiceTypes,
                                                              HttpServletRequest request) {
        //show log message when init method
        ChoiceTypeController.logger.info(String.format(ChoiceTypeController.UPDATE_CHOICE_TYPES_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(choiceTypes)));
        //call handle from service
        this.service.updateChoiceTypes(choiceTypes);
        //research data after delete successfully
        List<ChoiceType> choiceTypesRet = this.service.findAllChoiceType();
        choiceTypeResolver.fetch(choiceTypesRet);
        return new ModifiedEntity<>(choiceTypesRet, UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Add new the list of choice type.
     *
     * @param choiceTypes The list of choice type to add new.
     * @param request     The HTTP request that initiated this call.
     * @return The list of choice type after add new and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ChoiceTypeController.ADD_NEW_CHOICE_TYPES)
    public ModifiedEntity<List<ChoiceType>> addChoiceTypes(@RequestBody List<ChoiceType> choiceTypes,
                                                           HttpServletRequest request) {
        //show log message when init method
        ChoiceTypeController.logger.info(String.format(ChoiceTypeController.ADD_NEW_CHOICE_TYPES_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(choiceTypes)));
        //call handle from service
        this.service.addChoiceTypes(choiceTypes);
        //research data after delete successfully
        List<ChoiceType> choiceTypesRet = this.service.findAllChoiceType();
        choiceTypeResolver.fetch(choiceTypesRet);
        return new ModifiedEntity<>(choiceTypesRet, ADD_SUCCESS_MESSAGE);
    }
}