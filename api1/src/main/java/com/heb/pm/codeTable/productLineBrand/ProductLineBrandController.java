/*
 *  ProductLineBrandController
 *  Copyright (c) 2019 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productLineBrand;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductLineBrand;
import com.heb.pm.entity.ProductLineBrandKey;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rest endpoint for product line.
 *
 * @author s769046
 * @since 2.8.0
 */

@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductLineBrandController.CODE_TABLE_PRODUCT_LINE_BRAND_OPTION_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_LINE_BRAND)
public class ProductLineBrandController {

    private static final Logger logger = LoggerFactory.getLogger(ProductLineBrandController.class);

    protected static final String CODE_TABLE_PRODUCT_LINE_BRAND_OPTION_URL = "/codeTable/productLineBrand";

    private static final String URL_GET_PRODUCT_LINE_BRANDS_PAGE = "/getProductLineBrandsPage";
    private static final String URL_DELETE_PRODUCT_LINE_BRAND = "/deleteProductLineBrand";
    private static final String URL_FILTER_PRODUCT_LINE_BRANDS = "/filterProductLineBrandsPage";
    private static final String URL_EXPORT_LINE_BRAND_TO_CSV = "/exportLineBrandToCSV";
    private static final String ADD_PRODUCT_LINE_BRAND = "/addProductLineBrand";

    private static final String FIND_PRODUCT_LINE_BRAND_MESSAGE = "User %s from IP %s requested to find product " +
            "line brands by page: %d, page size: %d, id: '%s', description: '%s', and include count: %s.";
    private static final String DELETE_PRODUCT_LINE_BRAND_MESSAGE = "User %s from IP %s requested to delete product " +
            "line brands by id: '%s', description: '%s'.";
    private static final String ADD_PRODUCT_LINE_BRAND_MESSAGE = "User %s from IP %s requested to delete product " +
            "line brands by id: '%s', description: '%s'.";
    private static final String EXPORT_LINE_BRAND_TO_CSV_MESSAGE = "User %s from IP %s requested to export product sub-brands to csv with id %s and description %s.";
    private static final String FILTER_PRODUCT_LINE_BRANDS_MESSAGE = "User %s from IP %s requested to filter product line brands.";
    private static final String DELETE_SUCCESS_MESSAGE = "Deleted successfully.";
    private static final String ADD_SUCCESS_MESSAGE = "Added successfully.";

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_NO_FILTER = "";



    private LazyObjectResolver<Iterable<ProductLineBrand>> objectResolver = new ProductLineBrandResolver();

    @Autowired
    private ProductLineBrandService service;

    @Autowired
    private UserInfo userInfo;

    private class ProductLineBrandResolver implements LazyObjectResolver<Iterable<ProductLineBrand>>{

        @Override
        public void fetch(Iterable<ProductLineBrand> d) {
            for(ProductLineBrand pbl: d){
                pbl.getProductBrand().getProductBrandId();
                pbl.getProductLine().getId();
            }
        }
    }


    /**
     * Get all product sub brand records.
     *
     * @param page             the page number.
     * @param pageSize         the page size.
     * @param id   the product line brand id to search.
     * @param description the product line brand description to search.
     * @param request          the http servlet request.
     * @return the page of product sub brands.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductLineBrandController.URL_GET_PRODUCT_LINE_BRANDS_PAGE)
    public PageableResult<ProductLineBrand> getProductSubBrandsPage(@RequestParam(value = "page", required = false) Integer page,
                                                                    @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                    @RequestParam(value = "id", required = false, defaultValue = "") String id,
                                                                    @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                                                    @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                                    HttpServletRequest request) {
        this.logGetProductLineBrandsPage(request.getRemoteAddr(),page,pageSize,id,description,includeCount);

        int pageNo = page == null ? ProductLineBrandController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductLineBrandController.DEFAULT_PAGE_SIZE : pageSize;
        String brandId = StringUtils.isEmpty(id) ? ProductLineBrandController.DEFAULT_NO_FILTER : id;
        String lineDescription = StringUtils.isEmpty(description) ? ProductLineBrandController.DEFAULT_NO_FILTER : description;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        PageableResult<ProductLineBrand> results=this.service.findProductLineBrandsByPage(pageNo, size, brandId, lineDescription, count);
        this.objectResolver.fetch(results.getData());
        return results;
    }



    /**
     * Get all product sub brand records.
     *
     * @param productLineBrandKey the ProductLineBrand to delete.
     * @return the page of product sub brands.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductLineBrandController.URL_DELETE_PRODUCT_LINE_BRAND)
    public ModifiedEntity<ProductLineBrandKey> deleteProductLineBrand(@RequestBody ProductLineBrandKey productLineBrandKey, HttpServletRequest request) {
        Long id=productLineBrandKey.getBrandId();
        String description=productLineBrandKey.getLineCode();
        this.logDeleteProductLineBrand(this.userInfo.getUserId(), request.getRemoteAddr(), String.valueOf(id), description);
        ProductLineBrandKey key= this.service.deleteProductLineBrand(productLineBrandKey);
        return new ModifiedEntity<>(key, DELETE_SUCCESS_MESSAGE);
    }


    /**
     * Export product sub brand to csv file.
     *
     * @param id   the product line brand id to search.
     * @param description the product line brand description to search.
     * @param downloadId       the download id to set cookie.
     * @param request          the http servlet request.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductLineBrandController.URL_EXPORT_LINE_BRAND_TO_CSV)
    public void exportSubBrandToCSV(@RequestParam(value = "prodLineBrandId", required = false, defaultValue = "") String id,
                                    @RequestParam(value = "prodLineBrandName", required = false, defaultValue = "") String description,
                                    @RequestParam(value = "downloadId", required = false) String downloadId,
                                    HttpServletRequest request, HttpServletResponse response) {
        this.logExportLineBrandToCSV(request.getRemoteAddr(), id, description);

        if (!StringUtils.isEmpty(downloadId)) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }
        String subBrandId = StringUtils.isEmpty(id) ? ProductLineBrandController.DEFAULT_NO_FILTER : id;
        String subBrandName = StringUtils.isEmpty(description) ? ProductLineBrandController.DEFAULT_NO_FILTER : description;
        this.service.exportCsv(response,subBrandId,subBrandName);
    }



    /**
     * Export product sub brand to csv file.
     *
     * @param id   the product line brand id to search.
     * @param description the product line brand description to search.
     * @param request          the http servlet request.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductLineBrandController.ADD_PRODUCT_LINE_BRAND)
    public ModifiedEntity<ProductLineBrandKey> addProductLineBrand(@RequestParam(value = "id", required = false, defaultValue = "") String id,
                                    @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                    HttpServletRequest request, HttpServletResponse response) {
        this.logAddLineBrand(request.getRemoteAddr(), id, description);
        long subBrandId = Long.valueOf(StringUtils.isEmpty(id) ? ProductLineBrandController.DEFAULT_NO_FILTER : id);
        String subBrandName = StringUtils.isEmpty(description) ? ProductLineBrandController.DEFAULT_NO_FILTER : description;
        ProductLineBrandKey key=new ProductLineBrandKey().setBrandId(subBrandId).setLineCode(subBrandName);
        return new ModifiedEntity<>(this.service.addProductLineBrand(key,this.userInfo.getUserId()), ADD_SUCCESS_MESSAGE);
    }




    /**
     * Logs a user's request to get all product sub brands.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logGetProductLineBrandsPage(String ipAddress, int page, int pageSize, String id, String description, boolean includeCount) {
        ProductLineBrandController.logger.info(String.format(ProductLineBrandController.FIND_PRODUCT_LINE_BRAND_MESSAGE,
                this.userInfo.getUserId(), ipAddress, page, pageSize, id, description, includeCount));
    }


    /**
     * Logs a user's request to get all product sub brands.
     *
     */
    private void logDeleteProductLineBrand(String ip, String userId, String id, String description) {
        ProductLineBrandController.logger.info(String.format(ProductLineBrandController.DELETE_PRODUCT_LINE_BRAND_MESSAGE,
                userId,ip, id, description));
    }



    /**
     * Logs a user's request to export product sub brands to csv.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logExportLineBrandToCSV(String ipAddress, String id, String description) {
        ProductLineBrandController.logger.info(String.format(ProductLineBrandController.EXPORT_LINE_BRAND_TO_CSV_MESSAGE,
                this.userInfo.getUserId(), ipAddress, id, description));
    }

    /**
     * Logs a user's request to export product sub brands to csv.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logAddLineBrand(String ipAddress, String id, String description) {
        ProductLineBrandController.logger.info(String.format(ProductLineBrandController.ADD_PRODUCT_LINE_BRAND_MESSAGE,
                this.userInfo.getUserId(), ipAddress, id, description));
    }
}
