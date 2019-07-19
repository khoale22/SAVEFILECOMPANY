/*
 *  ProductSubBrandController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productSubBrand;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductSubBrand;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Represents code table product sub brand information.
 *
 * @author vn00602
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductSubBrandController.CODE_TABLE_PRODUCT_SUB_BRAND_OPTION_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_SUB_BRAND)
public class ProductSubBrandController {

    private static final Logger logger = LoggerFactory.getLogger(ProductSubBrandController.class);

    protected static final String CODE_TABLE_PRODUCT_SUB_BRAND_OPTION_URL = "/codeTable/productSubBrand";

    private static final String URL_GET_PRODUCT_SUB_BRANDS_PAGE = "/getProductSubBrandsPage";
    private static final String URL_EXPORT_SUB_BRAND_TO_CSV = "/exportSubBrandToCSV";

    private static final String FIND_PRODUCT_SUB_BRAND_MESSAGE = "User %s from IP %s requested to find all product sub-brands.";
    private static final String EXPORT_SUB_BRAND_TO_CSV_MESSAGE = "User %s from IP %s requested to export product sub-brands to csv.";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_NO_FILTER = "";

    private static final String TEXT_EXPORT_FORMAT = "\"%s\",";
    private static final String NEWLINE_TEXT_EXPORT_FORMAT = "\n";
    private static final String CSV_HEADING = "Sub-Brand ID, Sub-Brand";

    @Autowired
    private ProductSubBrandService service;

    @Autowired
    private UserInfo userInfo;

    /**
     * Get all product sub brand records.
     *
     * @param page             the page number.
     * @param pageSize         the page size.
     * @param prodSubBrandId   the product brand id to search.
     * @param prodSubBrandName the product sub brand name to search.
     * @param request          the http servlet request.
     * @return the page of product sub brands.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductSubBrandController.URL_GET_PRODUCT_SUB_BRANDS_PAGE)
    public PageableResult<ProductSubBrand> getProductSubBrandsPage(@RequestParam(value = "page", required = false) Integer page,
                                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                   @RequestParam(value = "prodSubBrandId", required = false, defaultValue = "") String prodSubBrandId,
                                                                   @RequestParam(value = "prodSubBrandName", required = false, defaultValue = "") String prodSubBrandName,
                                                                   @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                                   HttpServletRequest request) {
        this.logGetProductSubBrandsPage(request.getRemoteAddr());

        int pageNo = page == null ? ProductSubBrandController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductSubBrandController.DEFAULT_PAGE_SIZE : pageSize;
        String subBrandId = StringUtils.isEmpty(prodSubBrandId) ? ProductSubBrandController.DEFAULT_NO_FILTER : prodSubBrandId;
        String subBrandName = StringUtils.isEmpty(prodSubBrandName) ? ProductSubBrandController.DEFAULT_NO_FILTER : prodSubBrandName;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;

        return this.service.findProductSubBrandsPage(pageNo, size, subBrandId, subBrandName, count);
    }

    /**
     * Export product sub brand to csv file.
     *
     * @param prodSubBrandId   the product sub brand id to search.
     * @param prodSubBrandName the product sub brand name to search.
     * @param totalPages       to total pages.
     * @param downloadId       the download id to set cookie.
     * @param request          the http servlet request.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductSubBrandController.URL_EXPORT_SUB_BRAND_TO_CSV)
    public void exportSubBrandToCSV(@RequestParam(value = "prodSubBrandId", required = false, defaultValue = "") String prodSubBrandId,
                                    @RequestParam(value = "prodSubBrandName", required = false, defaultValue = "") String prodSubBrandName,
                                    @RequestParam(value = "totalPages", required = false) int totalPages,
                                    @RequestParam(value = "downloadId", required = false) String downloadId,
                                    HttpServletRequest request, HttpServletResponse response) {
        this.logExportSubBrandToCSV(request.getRemoteAddr());

        if (!StringUtils.isEmpty(downloadId)) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        String subBrandId = StringUtils.isEmpty(prodSubBrandId) ? ProductSubBrandController.DEFAULT_NO_FILTER : prodSubBrandId;
        String subBrandName = StringUtils.isEmpty(prodSubBrandName) ? ProductSubBrandController.DEFAULT_NO_FILTER : prodSubBrandName;
        try {
            for (int pageNo = 0; pageNo < totalPages; pageNo++) {
                if (pageNo == 0) {
                    response.getOutputStream().println(this.getHeading());
                }
                response.getOutputStream().print(this.createCsv(
                        this.service.findProductSubBrandsPage(pageNo, ProductSubBrandController.DEFAULT_PAGE_SIZE,
                                subBrandId, subBrandName, Boolean.FALSE)));
            }
        } catch (IOException e) {
            ProductSubBrandController.logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Creates a CSV string from a list of product sub brands.
     *
     * @param productSubBrands a list of product sub brands.
     * @return a CSV string with product sub brand information.
     */
    private String createCsv(PageableResult<ProductSubBrand> productSubBrands) {
        StringBuilder csv = new StringBuilder();
        for (ProductSubBrand productSubBrand : productSubBrands.getData()) {
            csv.append(String.format(TEXT_EXPORT_FORMAT, productSubBrand.getProdSubBrandId()));
            csv.append(String.format(TEXT_EXPORT_FORMAT, productSubBrand.getProdSubBrandName().trim()));

            csv.append(NEWLINE_TEXT_EXPORT_FORMAT);
        }
        return csv.toString();
    }

    /**
     * Returns the heading to the CSV.
     *
     * @return The heading to the CSV.
     */
    private String getHeading() {
        return CSV_HEADING;
    }

    /**
     * Logs a user's request to get all product sub brands.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logGetProductSubBrandsPage(String ipAddress) {
        ProductSubBrandController.logger.info(String.format(ProductSubBrandController.FIND_PRODUCT_SUB_BRAND_MESSAGE,
                this.userInfo.getUserId(), ipAddress));
    }

    /**
     * Logs a user's request to export product sub brands to csv.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logExportSubBrandToCSV(String ipAddress) {
        ProductSubBrandController.logger.info(String.format(ProductSubBrandController.EXPORT_SUB_BRAND_TO_CSV_MESSAGE,
                this.userInfo.getUserId(), ipAddress));
    }
}
