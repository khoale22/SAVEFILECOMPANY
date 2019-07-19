/*
 *  ProductBrandController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.codeTable.productBrand;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.CostOwner;
import com.heb.pm.entity.ProductBrand;
import com.heb.pm.productDetails.product.eCommerceView.ProductECommerceViewController;
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

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint for functions related to get the list of code table product brand information.
 *
 * @author vn00602
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductBrandController.CODE_TABLE_PRODUCT_BRAND_OPTION_URL)
@AuthorizedResource(ResourceConstants.CODE_TABLE_PRODUCT_BRAND)
public class ProductBrandController {

    private static final Logger logger = LoggerFactory.getLogger(ProductBrandController.class);

    protected static final String CODE_TABLE_PRODUCT_BRAND_OPTION_URL = "/codeTable/productBrand";

    private static final String URL_FIND_ALL_PRODUCT_BRANDS = "/findAllProductBrands";
    private static final String URL_FILTER_PRODUCT_BRANDS = "/filterProductBrands";
	private static final String URL_FILTER_PRODUCT_BRANDS_BY_ID_AND_DESCRIPTION = "/filterProductBrandsByIdAndDescription";


    private static final String FIND_ALL_PRODUCT_BRANDS_MESSAGE = "User %s from IP %s requested to find all product brands.";
    private static final String FILTER_PRODUCT_BRANDS_MESSAGE = "User %s from IP %s requested to filter product brands.";
	private static final String FILTER_PRODUCT_BRANDS_BY_ID_AND_DESCRIPTION_MESSAGE = "User %s from IP %s requested to filter product brands by text ='%s'.";


    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final String DEFAULT_NO_FILTER = "";
    protected static final int TIER_CODE_UNASSIGNED = 0;

    @Autowired
    private ProductBrandService service;

    @Autowired
    private UserInfo userInfo;

    /**
     * Find all records of product brands.
     *
     * @param page         the page number.
     * @param pageSize     the page size.
     * @param ownBrand     the flag of show own brand only.
     * @param includeCount the flag that check include count or not.
     * @param request      the http servlet request.
     * @return the list of product brands.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductBrandController.URL_FIND_ALL_PRODUCT_BRANDS)
    public PageableResult<ProductBrand> findAllProductBrands(@RequestParam(value = "page", required = false) Integer page,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             @RequestParam(value = "ownBrand", required = false) Boolean ownBrand,
                                                             @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                             HttpServletRequest request) {
        this.logFindAllProductBrands(request.getRemoteAddr());

        int pageNo = page == null ? ProductBrandController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductBrandController.DEFAULT_PAGE_SIZE : pageSize;
        boolean ownBrnd = ownBrand == null ? Boolean.FALSE : ownBrand;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        return this.service.findAllProductBrands(pageNo, size, ownBrnd, count);
    }

    /**
     * Filter product brands with parameters.
     *
     * @param page         the page number.
     * @param pageSize     the page size.
     * @param productBrand the product brand to filter.
     * @param brandTier    the brand tier name to filter.
     * @param ownBrand     the flag of show own brand only.
     * @param includeCount the flag that check include count or not.
     * @param request      the http servlet request.
     * @return the list of product brands after filter.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductBrandController.URL_FILTER_PRODUCT_BRANDS)
    public PageableResult<ProductBrand> filterProductBrands(@RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                            @RequestParam(value = "productBrand", required = false) String productBrand,
                                                            @RequestParam(value = "brandTier", required = false) String brandTier,
                                                            @RequestParam(value = "ownBrand", required = false) Boolean ownBrand,
                                                            @RequestParam(value = "includeCount", required = false) Boolean includeCount,
                                                            HttpServletRequest request) {
        this.logFilterProductBrands(request.getRemoteAddr());

        int pageNo = page == null ? ProductBrandController.DEFAULT_PAGE : page;
        int size = pageSize == null ? ProductBrandController.DEFAULT_PAGE_SIZE : pageSize;
        String prodBrand = StringUtils.isEmpty(productBrand) ? ProductBrandController.DEFAULT_NO_FILTER : productBrand;
        String brndTier = StringUtils.isEmpty(brandTier) ? ProductBrandController.DEFAULT_NO_FILTER : brandTier;
        boolean ownBrnd = ownBrand == null ? Boolean.FALSE : ownBrand;
        boolean count = includeCount == null ? Boolean.FALSE : includeCount;
        return this.service.filterProductBrands(pageNo, size, prodBrand, brndTier, ownBrnd, count);
    }

	/**
	 * find product brand by id or description
	 * @param searchText the text to find
	 * @param pageSize page number
	 * @param includeCount include count or not
	 * @param request http request
	 * @return PageableResult with data ProductBrand
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET,
			value = ProductBrandController.URL_FILTER_PRODUCT_BRANDS_BY_ID_AND_DESCRIPTION)
	public PageableResult<ProductBrand> findProductBrandsByIdOrDesciption(
											@RequestParam(value = "searchText", required = false) String searchText,
											@RequestParam(value = "pageSize", required = false) Integer pageSize,
											@RequestParam(value = "includeCount", required = false) Boolean includeCount,
											HttpServletRequest request) {
		logger.info(String.format(FILTER_PRODUCT_BRANDS_BY_ID_AND_DESCRIPTION_MESSAGE, this.userInfo
				.getUserId(), request.getRemoteAddr(),searchText));
		return this.service.findProductBrandsByText(ProductBrandController.DEFAULT_PAGE,pageSize,
				searchText);
	}

    /**
     * Logs a user's request to get all product brands.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logFindAllProductBrands(String ipAddress) {
        ProductBrandController.logger.info(String.format(ProductBrandController.FIND_ALL_PRODUCT_BRANDS_MESSAGE,
                this.userInfo.getUserId(), ipAddress));
    }

    /**
     * Logs a user's request to filter product brands with parameters.
     *
     * @param ipAddress the IP address of logged in user.
     */
    private void logFilterProductBrands(String ipAddress) {
        ProductBrandController.logger.info(String.format(ProductBrandController.FILTER_PRODUCT_BRANDS_MESSAGE,
                this.userInfo.getUserId(), ipAddress));
    }
}
