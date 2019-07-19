/*
 *  ProductLineController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productLine;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductLine;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Rest endpoint for product line.
 *
 * @author m314029
 * @since 2.26.0
 */
@RestController()
@RequestMapping(ProductLineController.ROOT_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_LINE)
public class ProductLineController {

    private static final Logger logger = LoggerFactory.getLogger(ProductLineController.class);

    protected static final String ROOT_URL = ApiConstants.BASE_APPLICATION_URL + "/codeTable/productLine";

    private static final String URL_GET_PRODUCT_LINE_PAGE = "/findPage";
    private static final String URL_FILTER_PRODUCT_LINES = "/filterProductLines";

    // Log Messages.
    private static final String DELETE_PRODUCT_LINE_MESSAGE = "User %s from IP %s has requested to delete the " +
            "ProductLine with the id: %s.";
    private static final String FIND_PRODUCT_LINE_MESSAGE = "User %s from IP %s requested to find product " +
            "lines by page: %d, page size: %d, id: '%s', description: '%s', and include count: %s.";
    private static final String FILTER_PRODUCT_LINE_MESSAGE = "User %s from IP %s requested to find product " +
            "lines by page: %d, page size: %d, and searched term: '%s'.";
    private static final String UPDATE_PRODUCT_LINE_MESSAGE = "User %s from IP %s has requested to update the " +
            "ProductLine with the id: %s to have the description: '%s'.";

    // Keys to user facing messages in the message resource bundle.
    private static final String ADD_PRODUCT_LINE_MESSAGE = "User %s from IP %s requested to add product " +
            "lines.";
    private static final String ADD_SUCCESS_MESSAGE = "Sucessfully added.";
    private static final String ADD_ERROR_MESSAGE = "Product Line (Sub Brand) already exists.";
    private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Product Line with ID %s deleted successfully.";
    private static final String DELETE_SUCCESS_MESSAGE_KEY ="ProductLineController.deleteSuccessful";
    private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Product Line: %s updated successfully.";
    private static final String UPDATE_SUCCESS_MESSAGE_KEY ="ProductLineController.updateSuccessful";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_NO_FILTER = "";

    @Autowired
    private ProductLineService service;

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private MessageSource messageSource;
    /**
     * Get all product line records.
     *
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @param includeCount Whether count of total records needs to be done.
     * @param request The http servlet request.
     * @return The page of product lines.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductLineController.URL_GET_PRODUCT_LINE_PAGE)
    public PageableResult<ProductLine> findByPage(@RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "id", required = false, defaultValue = "") String id,
                                                  @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                                  @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                  HttpServletRequest request) {

        int pageNo = page == null ? ProductLineController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductLineController.DEFAULT_PAGE_SIZE : pageSize;
        id = StringUtils.isEmpty(id) ? ProductLineController.DEFAULT_NO_FILTER : id;
        description = StringUtils.isEmpty(description) ? ProductLineController.DEFAULT_NO_FILTER : description;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        this.logGetProductLinesPage(request.getRemoteAddr(), pageNo, size, id, description, count);

        return this.service.findByPage(pageNo, size, id, description, count);
    }

    /**
     *
     * @param page the page number.
     * @param pageSize the page size.
     * @param searched the term to search against.
     * @param request The http servlet request.
     * @return the list of product lines.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductLineController.URL_FILTER_PRODUCT_LINES)
    public List<ProductLine> filterProductLines(@RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                  @RequestParam(value = "searched", required = false, defaultValue = "") String searched,
                                                  HttpServletRequest request) {

        int pageNo = page == null ? ProductLineController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductLineController.DEFAULT_PAGE_SIZE : pageSize;
        searched = StringUtils.isEmpty(searched) ? ProductLineController.DEFAULT_NO_FILTER : searched;
        this.logFilterProductLines(request.getRemoteAddr(), pageNo, size, searched);

        return this.service.findAllByProductLineCodeOrDescription(searched, page, size);
    }

    /**
     * Adds new product lines.
     *
     * @param productLineDescriptions the product line descriptions
     * @param request
     * @return
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "addProductLines")
    public ModifiedEntity<List<ProductLine>> addProductLines(@RequestBody List<ProductLine> productLineDescriptions,
                                                             HttpServletRequest request) {

        this.logAddProductLines(request.getRemoteAddr());
        List<ProductLine> productLines = this.service.addProductLines(productLineDescriptions);
        return new ModifiedEntity<>(productLines, ADD_SUCCESS_MESSAGE);
    }
    /**
     * Updates a ProductLine's Description.
     *
     * @param productLine The productLine to be updated.
     * @param request the HTTP request that initiated this call.
     * @return The updated ProductLine and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.PUT, value = "updateProductLine")
    public ModifiedEntity<ProductLine> updateProductLineDescription(@RequestBody ProductLine productLine,
                                                                    HttpServletRequest request){
        this.logUpdate(request.getRemoteAddr(), productLine);
        ProductLine updatedProductLine = this.service.updateProductLineDescription(productLine);
        String updateMessage = this.messageSource.getMessage(ProductLineController.UPDATE_SUCCESS_MESSAGE_KEY,
                new Object[]{productLine.getId()}, ProductLineController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
                request.getLocale());
        return new ModifiedEntity<>(updatedProductLine, updateMessage);
    }
    /**
     * Deletes a ProductLine.
     *
     * @param productLineId The ProductLine's productLineId to be deleted.
     * @param request the HTTP request that initiated this call.
     * @return The deleted ProductLine ID and a message for the front end.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.DELETE, value = "deleteProductLine")
    public ModifiedEntity<String> deleteProductLine(@RequestParam String productLineId,
                                                    HttpServletRequest request) {
        this.logDelete(request.getRemoteAddr(), productLineId);
        this.service.deleteProductLine(productLineId);
        String updateMessage = this.messageSource.getMessage(ProductLineController.DELETE_SUCCESS_MESSAGE_KEY,
                new Object[]{productLineId}, ProductLineController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());

        return new ModifiedEntity<>(productLineId, updateMessage);
    }

    /**
     * Logs a user's request to get all product lines.
     *
     * @param ipAddress The user's ip.
     * @param page The page number.
     * @param pageSize The page size.
     * @param id The id to search.
     * @param description The description to search.
     * @param includeCount Whether count of total records needs to be done.
     */
    private void logGetProductLinesPage(String ipAddress, int page, int pageSize, String id, String description, boolean includeCount) {
        ProductLineController.logger.info(String.format(ProductLineController.FIND_PRODUCT_LINE_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, id, description, includeCount));
    }

    private void logFilterProductLines(String ipAddress, int page, int pageSize, String searched) {
        ProductLineController.logger.info(String.format(ProductLineController.FILTER_PRODUCT_LINE_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, searched));
    }

    /**
     * Logs a users request to add product lines.
     *
     * @param ip The user's IP.
     */
    private void logAddProductLines(String ip) {
        ProductLineController.logger.info(String.format(ProductLineController.ADD_PRODUCT_LINE_MESSAGE,
                this.userInfo.getUserId(), ip));
    }

    /**
     * Logs a user's request to delete a product line.
     *
     * @param ip The IP address the user is logged in from.
     * @param id the id of the Product Line to be deleted.
     */
    private void logDelete(String ip, String id){
        ProductLineController.logger.info(String.format(ProductLineController.DELETE_PRODUCT_LINE_MESSAGE,
                this.userInfo.getUserId(), ip, id));
    }

    /**
     * Logs a user's request to update a product line.
     *
     * @param ip The IP address the user is logged in from.
     * @param productLine The product line to be updated.
     */
    private void logUpdate(String ip, ProductLine productLine){
        ProductLineController.logger.info(String.format(ProductLineController.UPDATE_PRODUCT_LINE_MESSAGE,
                this.userInfo.getUserId(), ip, productLine.getId(), productLine.getDescription()));
    }
}
