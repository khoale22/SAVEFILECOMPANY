/*
 *  ChoiceController
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
import com.heb.pm.entity.ChoiceOption;
import com.heb.pm.entity.ChoiceType;
import com.heb.pm.entity.CustomerProductChoice;
import com.heb.pm.entity.ImageMetaData;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Represents code table choice information.
 *
 * @author vn70516
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ChoiceController.CODE_TABLE_CHOICE_OPTION_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_CHOICE_OPTION)
public class ChoiceController {

    protected static final String CODE_TABLE_CHOICE_OPTION_URL = "/codeTable/choice";
    private static final Logger logger = LoggerFactory.getLogger(ChoiceController.class);

    // Log messages.
    private static final String FIND_ALL_CHOICE_TYPE_MESSAGE = "User %s from IP %s requested all choice type.";
    private static final String FIND_ALL_CHOICE_OPTION_MESSAGE = "User %s from IP %s requested all choice option.";
    private static final String FIND_ALL_CHOICE_OPTION_IMAGE_URI_MESSAGE = "User %s from IP %s requested all image uri information for choice option.";
    private static final String FIND_ALL_CHOICE_OPTION_IMAGE_BY_URI_MESSAGE = "User %s from IP %s requested image for choice option by uri %s.";
    private static final String ADD_NEW_CHOICE_OPTIONS_MESSAGE = "User %s from IP %s requested add new the list of choice option [%s]";
    private static final String UPDATE_CHOICE_OPTIONS_MESSAGE = "User %s from IP %s requested edit the list of choice option [%s]";
    private static final String DELETE_CHOICE_OPTIONS_MESSAGE = "User %s from IP %s requested add new the list of choice option [%s]";

    private static final String LOG_CHOICE_BY_PRODUCT__ID = "User %s from IP %s has requested choice information: %s";

    private static final String DELETE_SUCCESS_MESSAGE = "Deleted successfully.";
    private static final String UPDATE_SUCCESS_MESSAGE = "Updated successfully.";
    private static final String ADD_SUCCESS_MESSAGE = "Added successfully.";

    private static final String FIND_ALL_CHOICE_TYPE = "/findAllChoiceType";
    private static final String FIND_ALL_CHOICE_OPTION = "/findAllChoiceOption";
    private static final String FIND_ALL_CHOICE_OPTION_IMAGE_URI = "/findAllChoiceOptionImageUri";
    private static final String FIND_CHOICE_OPTION_IMAGE_BY_URI = "/findChoiceOptionImageByUri";
    private static final String ADD_NEW_CHOICE_OPTIONS = "/addNewChoiceOptions";
    private static final String UPDATE_CHOICE_OPTIONS = "/updateChoiceOptions";
    private static final String DELETE_CHOICE_OPTIONS = "/deleteChoiceOptions";

    protected static final String GET_CHOICE_URL = "/getChoice";

    @Autowired
    private ChoiceService service;

    @Autowired
    private UserInfo userInfo;

    /**
     * Returns a list of all choice type default order by choice type description.
     *
     * @param request The HTTP request that initiated this call.
     * @return A List of all choice type order by choice type description.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ChoiceController.FIND_ALL_CHOICE_TYPE)
    public List<ChoiceType> findAllChoiceType(HttpServletRequest request) {
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.FIND_ALL_CHOICE_TYPE_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        List<ChoiceType> choiceTypes = this.service.findAllChoiceType();
        //return all data from service
        return choiceTypes;
    }

    /**
     * Returns a list of all choice option default order by choice option code.
     *
     * @param request The HTTP request that initiated this call.
     * @return A List of all choice option ordered by choice option code.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ChoiceController.FIND_ALL_CHOICE_OPTION)
    public List<ChoiceOption> findAllChoiceOption(HttpServletRequest request) {
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.FIND_ALL_CHOICE_OPTION_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
        //return all data from service
        return this.service.findAllChoiceOption();
    }

    /**
     * Returns a list of all image for all list choice option.
     *
     * @param request The HTTP request that initiated this call.
     * @return A List of all image for list of choice option.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ChoiceController.FIND_ALL_CHOICE_OPTION_IMAGE_URI)
    public List<ImageMetaData> findAllChoiceOptionImageUri(HttpServletRequest request) {
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.FIND_ALL_CHOICE_OPTION_IMAGE_URI_MESSAGE, this.userInfo.getUserId(),
                request.getRemoteAddr()));
        //return all data from service
        return this.service.findAllChoiceOptionImage();
    }

    /**
     * Request to get the byte[] representation of the image
     * @param request the request
     * @return the byte[]
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ChoiceController.FIND_CHOICE_OPTION_IMAGE_BY_URI)
    public ModifiedEntity<byte[]> findChoiceOptionImageByUri(@RequestParam(value = "imageUri", required = false) String imageUri,
                                                             HttpServletRequest request){
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.FIND_ALL_CHOICE_OPTION_IMAGE_BY_URI_MESSAGE, this.userInfo.getUserId(),
                request.getRemoteAddr(), imageUri));
        byte[] image =this.service.findChoiceOptionImageByUri(imageUri);
        return new ModifiedEntity<>(image, imageUri);
    }

    /**
     * Delete the list of choice option.
     * @param choiceOptions  The list of choice option to delete.
     * @param request      The HTTP request that initiated this call.
     * @return  The list of choice option after deleted and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ChoiceController.DELETE_CHOICE_OPTIONS)
    public ModifiedEntity<List<ChoiceOption>> deleteChoiceOptions(@RequestBody List<ChoiceOption> choiceOptions,
                                                          HttpServletRequest request) {
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.DELETE_CHOICE_OPTIONS_MESSAGE, this.userInfo.getUserId(),
                request.getRemoteAddr(), ListFormatter.formatAsString(choiceOptions)));
        //call handle from service
        this.service.deleteChoiceOptions(choiceOptions);
        //research data after delete successfully
        List<ChoiceOption> choiceOptionRet = this.service.findAllChoiceOption();
        return new ModifiedEntity<>(choiceOptionRet, DELETE_SUCCESS_MESSAGE);
    }

    /**
     * Update information for the list of choice option.
     * @param choiceOptions  The list of choice option to update.
     * @param request       The HTTP request that initiated this call.
     * @return  The list of choice option after update and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ChoiceController.UPDATE_CHOICE_OPTIONS)
    public ModifiedEntity<List<ChoiceOption>> updateChoiceOptions(@RequestBody List<ChoiceOption> choiceOptions,
                                                               HttpServletRequest request) {
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.UPDATE_CHOICE_OPTIONS_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(choiceOptions)));
        //call handle from service
        this.service.updateChoiceOptions(choiceOptions);
        //research data after delete successfully
        List<ChoiceOption> choiceOptionRet = this.service.findAllChoiceOption();
        return new ModifiedEntity<>(choiceOptionRet, UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Add new the list of choice option.
     * @param choiceOptions  The list of choice option to add new.
     * @param request       The HTTP request that initiated this call.
     * @return  The list of choice option after add new and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ChoiceController.ADD_NEW_CHOICE_OPTIONS)
    public ModifiedEntity<List<ChoiceOption>> addChoiceOptions(@RequestBody List<ChoiceOption> choiceOptions,
                                                               HttpServletRequest request) {
        //show log message when init method
        ChoiceController.logger.info(String.format(ChoiceController.ADD_NEW_CHOICE_OPTIONS_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), ListFormatter.formatAsString(choiceOptions)));
        //call handle from service
        this.service.addChoiceOptions(choiceOptions);
        //research data after delete successfully
        List<ChoiceOption> choiceOptionRet = this.service.findAllChoiceOption();
        return new ModifiedEntity<>(choiceOptionRet, ADD_SUCCESS_MESSAGE);
    }

    /**
     * Returns a list of customer product choices to the front end.
     * @param prodId the product id
     * @param request the httpservlet request
     * @return a list of customer product choices to the front end.
     */
    @ViewPermission
    @RequestMapping(method= RequestMethod.GET, value=ChoiceController.GET_CHOICE_URL)
    public List<CustomerProductChoice> getChoice(@RequestParam(value = "prodId") Long prodId, HttpServletRequest request) {
        this.logGetChoiceInformation(request.getRemoteAddr(), prodId);

       return this.service.retrieveCustomerProductChoiceList(prodId);
    }

    /**
     * Gets the choice information.
     * @param remoteAddr the ip address of the requestee
     * @param prodId the product id
     */
    private void logGetChoiceInformation(String remoteAddr, Long prodId) {
        ChoiceController.logger.info(
                String.format(ChoiceController.LOG_CHOICE_BY_PRODUCT__ID, this.userInfo.getUserId(), remoteAddr, prodId)
        );
    }
}