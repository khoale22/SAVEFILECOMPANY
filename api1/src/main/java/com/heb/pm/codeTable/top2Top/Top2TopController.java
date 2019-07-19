/*
 *  Top2TopController.java
 *  
 *  Copyright (c) 2019 H-E-B
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.top2Top;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.TopToTop;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to get the list of code table TopToTop information.
 *
 * @author vn73545
 * @since 2.41.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + Top2TopController.CODE_TABLE_TOP_2_TOP_OPTION_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_TOP_2_TOP)
public class Top2TopController {

    private static final Logger logger = LoggerFactory.getLogger(Top2TopController.class);

    protected static final String CODE_TABLE_TOP_2_TOP_OPTION_URL = "/codeTable/top2Top";

    private static final String FIND_ALL_TOP_2_TOPS = "/findAll";
    private static final String ADD_TOP_2_TOP = "/add";
    private static final String UPDATE_TOP_2_TOP = "/update";
    private static final String DELETE_TOP_2_TOP = "/delete";
    
    private static final String FIND_ALL_TOP_2_TOPS_MESSAGE = "User %s from IP %s requested to find all top 2 tops.";
    private static final String ADD_TOP_2_TOP_MESSAGE = "User %s from IP %s requested to add top 2 tops.";
    private static final String UPDATE_TOP_2_TOP_MESSAGE = "User %s from IP %s requested to update top 2 tops.";
    private static final String DELETE_TOP_2_TOP_MESSAGE = "User %s from IP %s requested to delete top 2 tops.";
    private static final String TOP_TO_TOP_ALREADY_EXISTS_ERROR_MESSAGE = "Top 2 Top already exists.";

	private static final String ADD_SUCCESS_MESSAGE = "Successfully added.";
    private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Successfully deleted.";
    private static final String DELETE_SUCCESS_MESSAGE_KEY ="Top2TopController.deleteSuccessful";
    private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Successfully updated.";
    private static final String UPDATE_SUCCESS_MESSAGE_KEY ="Top2TopController.updateSuccessful";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private Top2TopService service;

    @Autowired
    private UserInfo userInfo;
    
    @Autowired
    private MessageSource messageSource;

    /**
     * Find all TopToTops with pagination.
     *
     * @param page         the page number.
     * @param pageSize     the page size.
     * @param topToTopId   the TopToTop id to search.
     * @param topToTopName the TopToTop name to search.
     * @param includeCount the flag that check include count or not.
     * @param request      the http servlet request.
     * @return the page of TopToTops.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = Top2TopController.FIND_ALL_TOP_2_TOPS)
    public PageableResult<TopToTop> findAllTop2Tops(@RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                    @RequestParam(value = "topToTopId", required = false, defaultValue = "") String topToTopId,
                                                    @RequestParam(value = "topToTopName", required = false, defaultValue = "") String topToTopName,
                                                    @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                    HttpServletRequest request) {
        Top2TopController.logger.info(String.format(Top2TopController.FIND_ALL_TOP_2_TOPS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        int pageNo = page == null ? Top2TopController.DEFAULT_PAGE : page;
        int size = pageSize == null ? Top2TopController.DEFAULT_PAGE_SIZE : pageSize;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        return this.service.findAllTop2Tops(pageNo, size, topToTopId, topToTopName, count);
    }
    
    /**
     * Add new TopToTops.
     *
     * @param topToTops the list of TopToTop.
     * @param request the http servlet request.
     * @return the list of TopToTops and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = Top2TopController.ADD_TOP_2_TOP)
    public ModifiedEntity<List<TopToTop>> addTopToTops(@RequestBody List<TopToTop> topToTops,
                                                       HttpServletRequest request) {
    	Top2TopController.logger.info(String.format(Top2TopController.ADD_TOP_2_TOP_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        List<TopToTop> validateList = this.service.validateTopToTops(topToTops);
        if(CollectionUtils.isNotEmpty(validateList)){
        	return new ModifiedEntity<>(validateList, Top2TopController.TOP_TO_TOP_ALREADY_EXISTS_ERROR_MESSAGE);
        }
        List<TopToTop> topToTopList = this.service.addTopToTops(topToTops);
        return new ModifiedEntity<>(topToTopList, ADD_SUCCESS_MESSAGE);
    }

    /**
     * Update a TopToTop's Description.
     *
     * @param topToTop The TopToTop to be updated.
     * @param request the HTTP request that initiated this call.
     * @return The updated TopToTop and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.PUT, value = Top2TopController.UPDATE_TOP_2_TOP)
    public ModifiedEntity<TopToTop> updateTopToTop(@RequestBody TopToTop topToTop,
                                                    HttpServletRequest request){
    	Top2TopController.logger.info(String.format(Top2TopController.UPDATE_TOP_2_TOP_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        TopToTop updatedTopToTop = this.service.updateTopToTop(topToTop);
        String updateMessage = this.messageSource.getMessage(Top2TopController.UPDATE_SUCCESS_MESSAGE_KEY,
                new Object[]{topToTop.getTopToTopId()}, Top2TopController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
                request.getLocale());
        return new ModifiedEntity<>(updatedTopToTop, updateMessage);
    }

    /**
     * Delete a TopToTop.
     *
     * @param topToTopId The TopToTop's id to be deleted.
     * @param request the HTTP request that initiated this call.
     * @return The deleted TopToTop's id and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = Top2TopController.DELETE_TOP_2_TOP)
    public ModifiedEntity<String> deleteTopToTop(@RequestParam Integer topToTopId,
                                                 HttpServletRequest request) {
    	Top2TopController.logger.info(String.format(Top2TopController.DELETE_TOP_2_TOP_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        this.service.deleteTopToTop(topToTopId);
        String updateMessage = this.messageSource.getMessage(Top2TopController.DELETE_SUCCESS_MESSAGE_KEY,
                new Object[]{topToTopId}, Top2TopController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());

        return new ModifiedEntity<>(topToTopId.toString(), updateMessage);
    }
}
