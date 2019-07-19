package com.heb.pm.productDetails.product;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Bdm;
import com.heb.pm.entity.ProductMaster;
import com.heb.pm.entity.ProductRelationship;
import com.heb.pm.product.ProductInfoController;
import com.heb.pm.product.ProductInfoResolver;
import com.heb.pm.product.ProductInfoService;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for product information.
 *
 * @author s573181
 * @since 2.0.1
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductController.PRODUCT_INFO_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductInfoController.class);

	/**
	 * The constant PRODUCT_INFO_URL.
	 */
	protected static final String PRODUCT_INFO_URL = "/product";

    protected static final String GET_KITS_DATA = "/getKitsData";
    protected static final String GET_KITS_DATA_BY_ELEMENTS="/getKitsDataByElements";
    protected static final String GET_PARENT_PRODUCT="/getKitsProductMaster";

    // Keys to user facing messages in the message resource bundle.
    private static final String FIND_BY_CLASS_AND_COMMODITY_MESSAGE =
            "User %s from IP %s has requested product data for following class: %d, " +
                    "and commodity: %d.";
    private static final String DEFAULT_NO_PRODUCT_ID_MESSAGE = "Product ID cannot be null.";
    private static final String DEFAULT_PROD_ID_MESSAGE_KEY = "ProductInfoController.missingProductId";

    private static final String UPCS_MESSAGE_KEY = "ProductDiscontinueController.missingUpcs";
    private static final String DEFAULT_NO_UPCS_MESSAGE = "Must search for at least one UPC.";

    private static final String DEFAULT_NO_SUB_COMMODITY_MESSAGE = "Must search for a sub commodity.";
    private static final String SUB_COMMODITY_MESSAGE_KEY = "ProductDiscontinueController.missingSubCommodity";

    private static final String SUB_DEPARTMENT_MESSAGE_KEY = "ProductDiscontinueController.missingSubDepartment";

    private static final String DEFAULT_NO_PRODUCT_DESCRIPTION_MESSAGE = "Product Description cannot be null";
    private static final String DEFAULT_NO_PRODUCT_DESCRIPTION_KEY = "ProductInfoController.missingProductDescripiton";

    private static final String DEFAULT_NO_SUB_DEPARTMENT_MESSAGE = "Must search for at least one subdepartment.";
    private static final String BDM_MESSAGE_KEY = "ProductDiscontinueController.missingBdm";
    private static final String DEFAULT_NO_BDM_MESSAGE = "Must search for a bdm.";
    private static final String MRT_ITEM_CODE_MESSAGE_KEY = "ProductDiscontinueController.missingMrtItemCode";
    private static final String DEFAULT_NO_MRT_ITEM_CODE_MESSAGE = "Must search for an MRT item code.";
    private static final String MRT_CASE_PACK_MESSAGE_KEY = "ProductDiscontinueController.missingMrtCasePack";
    private static final String DEFAULT_NO_MRT_CASE_PACK_MESSAGE = "Must search for an MRT case pack UPC.";
    private static final String DEFAULT_NO_VENDOR_MESSAGE = "Must search for a vendor.";
    private static final String VENDOR_MESSAGE_KEY = "ProductDiscontinueController.missingVendor";

    // Defaults related to paging.
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25; // lowered from 100 to speed up search (m314029)

    // Log Messages
    private static final String FIND_BY_BDM_MESSAGE =
            "User %s from IP %s has requested product data for the following bdm: %s.";
    private static final String GET_PROD_INFO_BY_PROD_IDS_MESSAGE =
            "User %s from IP %s has requested product info for the following product Ids [%s]";

    private static final String FIND_HITS_BY_PRODUCT_IDS =
            "User %s from IP %s has requested product hit info for the following product Ids [%s]";

    private static final String FIND_BY_MULTIPLE_ITEM_CODE_MESSAGE  =
            "User %s from IP %s has requested product data for the following item codes [%s]";
    private static final String FIND_BY_MULTIPLE_UPC_MESSAGE  =
            "User %s from IP %s has requested product data for the following UPCs [%s]";
    private static final String FIND_BY_SUB_COMMODITY_MESSAGE =
            "User %s from IP %s has requested product data for following class: %d, commodity: %d, " +
                    "and sub commodity: %d.";
    private static final String FIND_BY_MRT_ITEM_CODE_MESSAGE =
            "User %s from IP %s has requested product data for the following MRT item code: %d.";
    private static final String FIND_BY_MRT_CASE_PACK_MESSAGE =
            "User %s from IP %s has requested product data for the following MRT case pack UPC: %d.";
    private static final String FIND_BY_VENDOR_MESSAGE =
            "User %s from IP %s has requested product data for the following vendor number: %d.";
    private static final String FIND_BY_PRODUCT_DESCRIPTION =
            "User %s from IP %s has requested product data for the following products with the description: %s.";
    private static final String FIND_KIT_INFO_BY_UPC=
            "User %s from IP %s has requested all kit product relationships based on upcs: %s";
    private static final String FIND_KIT_PRODUCT_INFO=
            "User %s from IP %s has requested the kit product master with id: %s";
    private static final String FIND_KIT_INFO=
            "User %s from IP %s has requested if a kit product relationship exists with the id: %s";



    private static final String DEFAULT_NO_CLASS_MESSAGE = "Must search for a class.";
    private static final String CLASS_MESSAGE_KEY = "ProductDiscontinueController.missingClass";
    private static final String DEFAULT_NO_COMMODITY_MESSAGE = "Must search for a commodity.";
    private static final String COMMODITY_MESSAGE_KEY = "ProductDiscontinueController.missingCommodity";

    private LazyObjectResolver<ProductMaster> objectResolver = new ProductInfoResolver();

    @Autowired private ProductInfoService productInfoService;
    @Autowired private UserInfo userInfo;
    @Autowired private NonEmptyParameterValidator parameterValidator;
    @Autowired private ProductService productService;

	/**
	 * Find by product ids pageable result.
	 *
	 * @param productIds    the product ids
	 * @param includeCounts the include counts
	 * @param page          the page
	 * @param pageSize      the page size
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "productIds")
    public PageableResult<ProductMaster> findByProductIds(@RequestParam List<Long> productIds,
                                                          @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                          @RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          HttpServletRequest request){

        this.parameterValidator.validate(productIds, ProductController.DEFAULT_NO_PRODUCT_ID_MESSAGE,
                ProductController.DEFAULT_PROD_ID_MESSAGE_KEY, request.getLocale());

        this.logFindByProductIds(request.getRemoteAddr(), productIds);

        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findProductInfoByProdIDs(productIds, ic, pg, ps));
    }

    /**
     * Returns a page of product data for an list of UPCs
     *
     * @param upcs The UPCs to look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="upcs")
    public PageableResult<ProductMaster> findAllByUpcs(@RequestParam List<Long> upcs,
                                                       @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                       @RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                       HttpServletRequest request) {

        // UPCs are required.
        this.parameterValidator.validate(upcs, ProductController.DEFAULT_NO_UPCS_MESSAGE,
                ProductController.UPCS_MESSAGE_KEY, request.getLocale());

        this.logFindByUpcs(request.getRemoteAddr(), upcs);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findByUpcs(upcs, ic, pg, ps));
    }

    /**
     * Returns a page of product data for an list of UPCs
     *
     * @param itemCodes The itemCodes to look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="itemCodes")
    public PageableResult<ProductMaster> findAllByItemCodes(@RequestParam List<Long> itemCodes,
                                                            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                            @RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                            HttpServletRequest request) {

        // UPCs are required.
        this.parameterValidator.validate(itemCodes, ProductController.DEFAULT_NO_UPCS_MESSAGE,
                ProductController.UPCS_MESSAGE_KEY, request.getLocale());

        this.logFindByItemCodes(request.getRemoteAddr(), itemCodes);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findByItemCodes(itemCodes, ic, pg, ps));
    }

    /**
     * Returns a page of product data for an list of UPCs
     *
     * @param department The UPCs to look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="subDepartment")
    public PageableResult<ProductMaster> findBySubDepartment(@RequestParam("department") String department,
                                                             @RequestParam("subDepartment") String subDepartmentCode,
                                                             @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                             @RequestParam(value = "page", required = false) Integer page,
                                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                             HttpServletRequest request) {

        // subDepartment is required.
        this.parameterValidator.validate(department, ProductController.DEFAULT_NO_SUB_DEPARTMENT_MESSAGE,
                ProductController.SUB_DEPARTMENT_MESSAGE_KEY, request.getLocale());

        this.logFindBySubDepartnemt(request.getRemoteAddr(), department);

        // Set some defaults for the parameters that make sense
        // to have default values.
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findBySubDepartment(department, subDepartmentCode, pg, ps, includeCounts));
    }


    /**
     * Returns a page of product data for a sub commodity.
     *
     * @param classCode The class to look for data about.
     * @param commodityCode The commodity to look for data about.
     * @param subCommodityCode The sub commodity to look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="subCommodity")
    public PageableResult<ProductMaster> findBySubCommodity(
            @RequestParam("classCode") int classCode,
            @RequestParam("commodityCode") int commodityCode,
            @RequestParam("subCommodityCode") int subCommodityCode,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        // hierarchy is required.
        this.parameterValidator.validate(classCode, ProductController.DEFAULT_NO_CLASS_MESSAGE,
                ProductController.CLASS_MESSAGE_KEY, request.getLocale());
        this.parameterValidator.validate(commodityCode, ProductController.DEFAULT_NO_COMMODITY_MESSAGE,
                ProductController.COMMODITY_MESSAGE_KEY, request.getLocale());
        this.parameterValidator.validate(subCommodityCode,
                ProductController.DEFAULT_NO_SUB_COMMODITY_MESSAGE,
                ProductController.SUB_COMMODITY_MESSAGE_KEY, request.getLocale());

        this.logFindBySubCommodity(request.getRemoteAddr(), classCode, commodityCode, subCommodityCode);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.
                findBySubCommoditySearch(classCode, commodityCode, subCommodityCode,
                        ic, pg, ps));
    }

    /**
     * Returns a page of product data for a class and commodity.
     *
     * @param classCode The class to look for data about.
     * @param commodityCode The commodity to look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="classAndCommodity")
    public PageableResult<ProductMaster> findByClassAndCommodity(
            @RequestParam("classCode") int classCode,
            @RequestParam("commodityCode") int commodityCode,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        // hierarchy is required.
        this.parameterValidator.validate(classCode,	ProductController.DEFAULT_NO_CLASS_MESSAGE,
                ProductController.CLASS_MESSAGE_KEY, request.getLocale());

        // hierarchy is required.
        this.parameterValidator.validate(commodityCode,	ProductController.DEFAULT_NO_COMMODITY_MESSAGE,
                ProductController.COMMODITY_MESSAGE_KEY, request.getLocale());

        this.logFindByClassAndCommodity(request.getRemoteAddr(), classCode, commodityCode);

        // Set some defaults for the parameters that make sense
        // to have default values.

        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.
                findByClassAndCommodity(classCode, commodityCode, pg, ps, ic));
    }

    /**
     * Returns a page of product data for a bdm.
     *
     * @param bdm The bdm code to look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="bdm")
    public PageableResult<ProductMaster> findByBdm(
            @RequestParam("bdm") Bdm bdm,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        String bdmCode = bdm.getBdmCode();

        // products are required.
        this.parameterValidator.validate(bdmCode, ProductController.DEFAULT_NO_BDM_MESSAGE,
                ProductController.BDM_MESSAGE_KEY, request.getLocale());

        this.logFindByBdm(request.getRemoteAddr(), bdmCode);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findByBdm(bdmCode, ic, pg, ps));
    }

    /**
     * Returns a page of product master data for an MRT by item code.
     *
     * @param itemCode The MRT item code look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="mrtItemCode")
    public PageableResult<ProductMaster> findByMrtItemCode(
            @RequestParam("itemCode") Long itemCode,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        // products are required.
        this.parameterValidator.validate(itemCode, ProductController.DEFAULT_NO_MRT_ITEM_CODE_MESSAGE,
                ProductController.MRT_ITEM_CODE_MESSAGE_KEY, request.getLocale());

        this.logFindByMrtItemCode(request.getRemoteAddr(), itemCode);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findByMrtItemCode(itemCode, ic, pg, ps));
    }

    /**
     * Returns a page of product master data for an MRT by case pack UPC.
     *
     * @param casePack The MRT case pack UPC look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="mrtCasePack")
    public PageableResult<ProductMaster> findByMrtCaseUpc(
            @RequestParam("casePack") Long casePack,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        // products are required.
        this.parameterValidator.validate(casePack, ProductController.DEFAULT_NO_MRT_CASE_PACK_MESSAGE,
                ProductController.MRT_CASE_PACK_MESSAGE_KEY, request.getLocale());

        this.logFindByMrtCasePack(request.getRemoteAddr(), casePack);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findByMrtCasePack(casePack, ic, pg, ps));
    }

    /**
     * Returns a page of product master data for a vendor by id.
     *
     * @param vendorNumber The Vendor id look for data about.
     * @param includeCounts Whether or not to include total record and page counts.
     * @param page The page of data to pull.
     * @param pageSize The number of records being asked for.
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="vendor")
    public PageableResult<ProductMaster> findByVendorNumber(
            @RequestParam("vendor") int vendorNumber,
            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        // vendor number required.
        this.parameterValidator.validate(vendorNumber, ProductController.DEFAULT_NO_VENDOR_MESSAGE,
                ProductController.VENDOR_MESSAGE_KEY, request.getLocale());

        this.logFindByVendorNumber(request.getRemoteAddr(), vendorNumber);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveResults(this.productInfoService.findByVendorNumber(vendorNumber, ic, pg, ps));
    }

    /**
     * Search against Product for occurrence of the input products List.
     *
     * @param productIds The product Ids user searched for
     * @param request The HTTP request that initiated this call.
     * @return Hits result with Not found products List
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="hits/productIds")
    public Hits findHitsByProductId(@RequestParam("productIds") List<Long> productIds, HttpServletRequest request) {

        // Min one product Id is required.
        this.parameterValidator.validate(productIds, ProductController.DEFAULT_NO_PRODUCT_ID_MESSAGE,
                ProductController.DEFAULT_PROD_ID_MESSAGE_KEY, request.getLocale());

        this.logFindHitsByProductIds(request.getRemoteAddr(), productIds);
        return this.productInfoService.findHitsByProducts(productIds);
    }

    /**
     *  Search against Products where either the products description or the customers description matches
     * @param descriptions the phrase to be search against
     * @param includeCounts whether or not to include the number of products having this phrase
     * @param page the page to pull data from
     * @param pageSize The number of records being asked for
     * @param request The HTTP request that initiated this call.
     * @return A list of product data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value="productDescription")
    public PageableResult<ProductMaster> findAllByProductDescription(@RequestParam String descriptions,
                                                            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                            @RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                            HttpServletRequest request) {

        // UPCs are required.
        this.parameterValidator.validate(descriptions, ProductController.DEFAULT_NO_PRODUCT_DESCRIPTION_MESSAGE,
                ProductController.DEFAULT_NO_PRODUCT_DESCRIPTION_KEY, request.getLocale());
        this.logFindByProductDescriptions(request.getRemoteAddr(), descriptions);

        // Set some defaults for the parameters that make sense
        // to have default values.
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? ProductController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? ProductController.DEFAULT_PAGE_SIZE : pageSize;
//*
        return this.resolveResults(this.productInfoService.findByProductDescription(descriptions, ic, pg, ps));
    }

    /**
     * Gets kits data.
     *
     * @param productId the productId of the product.
     * @param request The HTTP request that initiated this call.
     * @return the ProductRelationship entity that contains the kits data and its joined ProductMaster data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductController.GET_KITS_DATA)
    public List<ProductRelationship> getKitsData(@RequestParam(value="productId") Long productId, HttpServletRequest request) {
        ProductController.logger.info(String.format(FIND_KIT_INFO, this.userInfo.getUserId(), request.getRemoteAddr(), productId.toString()));
    	List<ProductRelationship> productRelationshipList = this.productService.getKitsData(productId);
		productRelationshipList.forEach(productRelationship -> {
			this.objectResolver.fetch(productRelationship.getRelatedProduct());
			this.objectResolver.fetch(productRelationship.getParentProduct());
		});        return productRelationshipList;
    }

    /**
     * Gets kits data.
     *
     * @param upcs the list of upcs that may be in a product.
     * @param request The HTTP request that initiated this call.
     * @return the ProductRelationship entity that contains the kits data and its joined ProductMaster data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductController.GET_KITS_DATA_BY_ELEMENTS)
    public List<ProductRelationship> getKitsDataByElements(@RequestBody List<Long> upcs, HttpServletRequest request) {
    	ProductController.logger.info(String.format(FIND_KIT_INFO_BY_UPC, this.userInfo.getUserId(), request.getRemoteAddr(), upcs.toString()));
        List<ProductRelationship> productRelationshipList = this.productService.getKitsDataByElements(upcs);
        productRelationshipList.forEach(productRelationship -> {
        	this.objectResolver.fetch(productRelationship.getRelatedProduct());
        	this.objectResolver.fetch(productRelationship.getParentProduct());
		});
        return productRelationshipList;
    }

    /**
     * Returns the product master for the kit.
     *
     * @param prodId product ID for the kit.
     * @param request The HTTP request that initiated this call.
     * @return the ProductRelationship entity that contains the kits data and its joined ProductMaster data.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductController.GET_PARENT_PRODUCT)
    public ProductMaster getKitsProduct(@RequestParam String prodId, HttpServletRequest request) {
    	ProductController.logger.info(String.format(FIND_KIT_PRODUCT_INFO, this.userInfo.getUserId(), request.getRemoteAddr(), prodId));
        ProductMaster productMaster = productInfoService.findProductInfoByProdId(Long.valueOf(prodId));
        this.objectResolver.fetch(productMaster);
        return productMaster;
    }
    /**
     * Sets the UserInfo for this class to use. This is primarily for testing.
     *
     * @param userInfo The UserInfo for this class to use.
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Sets the NonEmptyParameterValidator for this class to use. This is primarily for testing.
     *
     * @param parameterValidator The NonEmptyParameterValidator for this class to use.
     */
    public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
        this.parameterValidator = parameterValidator;
    }

    /**
     * Log's a user's request to get all records for multiple UPCs.
     *
     * @param ip The IP address th user is logged in from.
     * @param upcs The UPCs the user is searching for.
     */
    private void logFindByUpcs(String ip, List<Long> upcs) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_MULTIPLE_UPC_MESSAGE, this.userInfo.getUserId(),
                        ip, ListFormatter.formatAsString(upcs)));
    }

    /**
     * Log's a user's request to get all records for multiple UPCs.
     *
     * @param ip The IP address th user is logged in from.
     * @param department The UPCs the user is searching for.
     */
    private void logFindBySubDepartnemt(String ip, String department) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_MULTIPLE_UPC_MESSAGE, this.userInfo.getUserId(),
                        ip, String.format(department)));
    }

    /**
     * Log's a user's request to get records for a bdm.
     *
     * @param ip The IP address th user is logged in from.
     * @param bdm The bdm the user is searching for.
     */
    private void logFindByBdm(String ip, String bdm) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_BDM_MESSAGE,
                        this.userInfo.getUserId(), ip, bdm)
        );
    }

    /**
     * Log's a user's request to get records for a class and commodity.
     *
     * @param ip The IP address th user is logged in from.
     * @param classCode The class the user is searching for.
     * @param commodity The commodity the user is searching for.
     */
    private void logFindByClassAndCommodity(String ip, int classCode, int commodity) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_CLASS_AND_COMMODITY_MESSAGE,
                        this.userInfo.getUserId(), ip, classCode, commodity)
        );
    }

    /**
     * Log's a user's request to get all records for multiple itemCodes.
     *
     * @param ip The IP address th user is logged in from.
     * @param itemCodes The itemCodes the user is searching for.
     */
    private void logFindByItemCodes(String ip, List<Long> itemCodes) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_MULTIPLE_ITEM_CODE_MESSAGE, this.userInfo.getUserId(),
                        ip, ListFormatter.formatAsString(itemCodes)));
    }

    /**
     * Log's a user's request to get all records for a prodId.
     *
     * @param ip The IP address th user is logged in from.
     * @param prodId The item codes the user is searching for.
     */
    private void logFindByProductIds(String ip, List<Long> prodId){
        ProductController.logger.info(String.format(ProductController.GET_PROD_INFO_BY_PROD_IDS_MESSAGE, this.userInfo.getUserId(), ip, prodId));
    }

    /**
     * Log's a user's request to get records for a sub commodity.
     * @param ip The IP address th user is logged in from.
     * @param classCode The class the user is searching for.
     * @param commodity The commodity the user is searching for.
     * @param subCommodity The subCommodity the user is searching for.
     */
    private void logFindBySubCommodity(String ip, int classCode,
                                       int commodity, int subCommodity) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_SUB_COMMODITY_MESSAGE,
                        this.userInfo.getUserId(), ip, classCode, commodity, subCommodity)
        );
    }

    /**
     * Log's a user's request to get records for an MRT by item code.
     * @param ip The IP address th user is logged in from.
     * @param itemCode The item code the user is searching for.
     */
    private void logFindByMrtItemCode(String ip, long itemCode) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_MRT_ITEM_CODE_MESSAGE,
                        this.userInfo.getUserId(), ip, itemCode)
        );
    }

    /**
     * Log's a user's request to get records for an MRT by case pack UPC.
     * @param ip The IP address th user is logged in from.
     * @param casePack The case pack UPC the user is searching for.
     */
    private void logFindByMrtCasePack(String ip, long casePack) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_MRT_CASE_PACK_MESSAGE,
                        this.userInfo.getUserId(), ip, casePack)
        );
    }

    /**
     * Log's a user's request to get records for a vendor number.
     * @param ip The IP address th user is logged in from.
     * @param vendorNumber The vendor number the user is searching for.
     */
    private void logFindByVendorNumber(String ip, int vendorNumber) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_VENDOR_MESSAGE,
                        this.userInfo.getUserId(), ip, vendorNumber)
        );
    }

    /**
     * Log's a user's request to get records for multiple product IDs.
     *
     * @param ip The IP address th user is logged in from.
     * @param productIds The product IDs the user is searching for.
     */
    private void logFindHitsByProductIds(String ip, List<Long> productIds) {
        ProductController.logger.info(
                String.format(ProductController.FIND_HITS_BY_PRODUCT_IDS,
                        this.userInfo.getUserId(), ip, ListFormatter.formatAsString(productIds))
        );
    }

    /**
     * Log's a user's request to get records for multiple product descriptions.
     *
     * @param ip The IP address th user is logged in from.
     * @param productDescriptions The product descriptions the user is searching for.
     */
    private void logFindByProductDescriptions(String ip, String productDescriptions) {
        ProductController.logger.info(
                String.format(ProductController.FIND_BY_PRODUCT_DESCRIPTION,
                        this.userInfo.getUserId(), ip, productDescriptions)
        );
    }

    /**
     * Loops through the data of the PageableResult passed in an loads the lazily loaded objects needed by the
     * Product search functions of the REST endpoint.
     *
     * @param results The PageableResult to load data for.
     * @return The PageableResult with its data resolved.
     */
    private PageableResult<ProductMaster> resolveResults(PageableResult<ProductMaster> results) {
        results.getData().forEach(this.objectResolver::fetch);
        return results;
    }
}
