package com.heb.pm.productDetails.product;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.productHierarchy.SubCommodityService;
import com.heb.pm.taxCategory.TaxCategoryService;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * This is the controller for Product Info Screen.
 *
 * @author m594201
 * @since 2.12.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductInformationController.PRODUCT_INFORMATION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_BASIC_INFORMATION)
public class ProductInformationController {

	private static final Logger logger = LoggerFactory.getLogger(ProductInformationController.class);

	// urls
	protected static final String PRODUCT_INFORMATION_URL = "/productInformation";
	private static final String GET_VERTEX_TAX_CATEGORY_BY_TAX_CODE = "/getVertexTaxCategoryByTaxCode";
	private static final String GET_VERTEX_TAX_CATEGORY_BY_TAX_QUALIFYING_CODE = "/getVertexTaxCategoryByTaxQualifyingCode";
	private static final String GET_PRICE_DETAIL = "/getPriceDetail";
	private static final String GET_PRODUCT_ASSORTMENT = "/getProductAssortment";
	private static final String GET_DEPOSIT_RELATED_PRODUCT = "/getDepositRelatedProduct";
	private static final String GET_EFFECTIVE_MAP_PRICES = "/getEffectiveMapPrices";
	private static final String UPDATE = "/update";
	private static final String GET_PRODUCT_DESCRIPTION_TEMPLATE_WITH_PROD_ID =
			"/getProductDescriptionTemplateWithProdId";
	private static final String GET_ALL_TAX_QUALIFYING_CONDITIONS =	"/getAllTaxQualifyingConditions";
	private static final String GET_ALL_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE_FOR_PRODUCT =
			"/getAllVertexTaxCategoryEffectiveMaintenanceForProduct";
	private static final String GET_ALL_PRODUCT_RETAIL_LINKS_BY_RETAIL_LINK_CODE =
			"/getAllProductRetailLinksByRetailLinkCode";
	private static final String GET_ALL_VERTEX_TAX_CATEGORY_CODES_BY_SUB_COMMODITY_CODE =
			"/getAllVertexTaxCategoryCodesBySubCommodityCode";
	private static final String GET_PRODUCTS_BY_VERTEX_TAX_CATEGORY_CODE =
			"/getProductsByVertexTaxCategoryCode";
	private static final String GET_ALL_VERTEX_TAX_CATEGORIES =	"/getAllVertexTaxCategories";
	private static final String GET_SUB_COMMODITY_DEFAULTS = "/getSubCommodityDefaults";
	private static final String GET_SHELF_ATTRIBUTES_AUDITS = "/getShelfAttributesAudits";

	private static final String UPDATE_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE = "/updateVertexTaxCategoryEffectiveMaintenance";
	private static final String GET_PRODUCT_INFO_AUDITS = "/getProductInfoAudits";
	private static final String EXPORT_PRODUCT_RETAIL_LINKS_TO_CSV_URL = "/exportProductRetailLinksToCsv";
	private static final String GET_TAX_OR_NON_TAX_CAT_BY_SUB_COM_KEY= "/getTaxAndNonTaxCategoryBySubCommodity";
	//logs
	private static final String GET_VERTEX_TAX_CATEGORY_BY_TAX_CODE_LOG_MESSAGE =
			"User %s from IP %s has requested vertex tax category with tax code: [%s].";
	private static final String GET_VERTEX_TAX_CATEGORY_BY_TAX_QUALIFYING_CODE_LOG_MESSAGE =
			"User %s from IP %s has requested vertex tax category with tax qualifying code: [%s].";
	private static final String GET_PRICE_DETAIL_LOG_MESSAGE =
			"User %s from IP %s has requested price detail for the upc: [%s].";
	private static final String GET_PRODUCT_ASSORTMENT_LOG_MESSAGE =
			"User %s from IP %s has requested product assortment for the product Id: [%s].";
	private static final String GET_DEPOSIT_RELATED_PRODUCT_LOG_MESSAGE =
			"User %s from IP %s has requested a deposit related product for the product Id: [%d].";
	private static final String GET_EFFECTIVE_MAP_PRICES_LOG_MESSAGE =
			"User %s from IP %s has requested the effective map prices for upc: [%d].";
	private static final String LOG_COMPLETE_MESSAGE =
			"The ProductInformationController method: %s is complete.";
	private static final String UPDATE_LOG_MESSAGE =
			"User %s from IP %s has requested update following product information: [%s].";
	private static final String GET_PRODUCT_DESCRIPTION_TEMPLATE_WITH_PROD_ID_LOG_MESSAGE =
			"User %s from IP %s has requested an empty product description with product id: [%d].";
	private static final String GET_ALL_TAX_QUALIFYING_CONDITIONS_LOG_MESSAGE =
			"User %s from IP %s has requested all tax qualifying conditions.";
	private static final String GET_ALL_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE_FOR_PRODUCT_LOG_MESSAGE =
			"User %s from IP %s has requested all vertex tax category effective maintenance for product id: [%d].";
	private static final String GET_ALL_PRODUCT_RETAIL_LINKS_BY_RETAIL_LINK_CODE_LOG_MESSAGE =
			"User %s from IP %s has requested all product retail links that have a retail link code: [%d].";
	private static final String UPDATE_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE_LOG_MESSAGE =
			"User %s from IP %s has requested to update vertex tax category effective maintenance: %s.";
	private static final String GET_ALL_VERTEX_TAX_CATEGORY_CODES_BY_SUB_COMMODITY_CODE_LOG_MESSAGE =
			"User %s from IP %s has requested all vertex tax category codes used by sub commodity code: %d.";
	private static final String GET_PRODUCTS_BY_VERTEX_TAX_CATEGORY_CODE_LOG_MESSAGE =
			"User %s from IP %s has requested products by vertex tax category code: %s.";
	private static final String GET_SUB_COMMODITY_DEFAULTS_LOG_MESSAGE =
			"User %s from IP %s has requested sub commodity defaults for: %s.";
	private static final String LOG_SHELF_ATTRIBUTES_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested shelf attributes audit information for prod ID: %s";
	private static final String LOG_PRODUCT_INFO_AUDIT_BY_PRODUCT_ID = "User %s from IP %s has requested product info audit information for prod ID: %s";
	private static final String LOG_EXPORT_PRODUCT_RETAIL_LINKS =
			"User %s from IP %s requested to export all product detail links with : retailLinkCode=%d";
	private static final String LOG_GET_TAX_OR_NON_TAX_CAT_BY_SUB_COM_KEY =
			"User %s from IP %s requested to tax or non tax category by sub com key.";
	//errors
	private static final String MISSING_TAX_CODE_ERROR_MESSAGE_KEY = "ProductInformationController.missingTaxCode";
	private static final String MISSING_TAX_CODE_ERROR_MESSAGE = "Must have a tax code to search for.";
	private static final String MISSING_UPC_ERROR_MESSAGE_KEY = "ProductInformationController.missingUpc";
	private static final String MISSING_UPC_ERROR_MESSAGE = "Must have a upc to search for.";
	private static final String MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY = "ProductInformationController.missingProductId";
	private static final String MISSING_PRODUCT_ID_ERROR_MESSAGE = "Must have a product id to search for.";
	private static final String MISSING_PRODUCT_INFORMATION_ERROR_MESSAGE_KEY = "ProductInformationController.missingUpdateProductInformation";
	private static final String MISSING_PRODUCT_INFORMATION_ERROR_MESSAGE = "Must have product information to update.";
	private static final String MISSING_PRODUCT_MASTER_ERROR_MESSAGE_KEY = "ProductInformationController.missingProduct";
	private static final String MISSING_PRODUCT_MASTER_ERROR_MESSAGE = "Must have a product to update.";
	private static final String MISSING_RETAIL_LINK_ERROR_MESSAGE_KEY = "ProductInformationController.missingRetailLink";
	private static final String MISSING_RETAIL_LINK_ERROR_MESSAGE = "Must have a retail link to search for.";
	private static final String MISSING_EFFECTIVE_VERTEX_CATEGORIES_ERROR_MESSAGE_KEY = "ProductInformationController.missingEffectiveVertexTaxCategories";
	private static final String MISSING_EFFECTIVE_VERTEX_CATEGORIES_ERROR_MESSAGE = "Must have an effective vertex tax category to update.";
	private static final String MISSING_SUB_COMMODITY_CODE_ERROR_MESSAGE_KEY = "ProductInformationController.missingSubCommodityCode";
	private static final String MISSING_SUB_COMMODITY_CODE_ERROR_MESSAGE = "Must have a sub commodity code to search for.";
	private static final String MISSING_SUB_COMMODITY_KEY_ERROR_MESSAGE_KEY = "ProductInformationController.missingSubCommodityKey";
	private static final String MISSING_SUB_COMMODITY_KEY_ERROR_MESSAGE = "Must have a sub commodity key to search for.";
	private static final String MISSING_COMMODITY_CODE_ERROR_MESSAGE_KEY = "ProductInformationController.missingCommodityCode";
	private static final String MISSING_COMMODITY_CODE_ERROR_MESSAGE = "Must have a commodity code to search for.";
	private static final String MISSING_CLASS_CODE_ERROR_MESSAGE_KEY = "ProductInformationController.missingClassCode";
	private static final String MISSING_CLASS_CODE_ERROR_MESSAGE = "Must have a class code to search for.";
	private static final String TAX_CATEGORY_KEY = "taxCategory";
	private static final String NON_TAX_CATEGORY_KEY = "nonTaxCategory";

	// Defaults related to paging.
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired
	private ProductInformationService productInformationService;

	@Autowired
	private TaxCategoryService taxCategoryService;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private SubCommodityService subCommodityService;

	private LazyObjectResolver<ProductMaster> vertexCategoryProductResolver = new VertexCategoryProductResolver();

	/**
	 * Resolver fetches primary selling unit data for Product master object.
	 */
	private class VertexCategoryProductResolver implements LazyObjectResolver<ProductMaster> {

		/**
		 * Resolves a product primary selling unit object from lazy JPQL mapping
		 *
		 * @param productMaster the product master to fetch product master for.
		 */
		@Override
		public void fetch(ProductMaster productMaster) {
			if (productMaster.getProductPrimarySellingUnit() != null) {
				productMaster.getProductPrimarySellingUnit().getUpc();
			}
		}
	}

	private LazyObjectResolver<EffectiveVertexTaxCategory> effectiveVertexTaxCategoryResolver =
			new EffectiveVertexTaxCategoryResolver();

	/**
	 * Resolver class for EffectiveVertexTaxCategory searches.
	 */
	private class EffectiveVertexTaxCategoryResolver implements LazyObjectResolver<EffectiveVertexTaxCategory> {

		/**
		 * Loads the vertex tax category into EffectiveVertexTaxCategory.
		 *
		 * @param effectiveVertexTaxCategory The object to resolve.
		 */
		@Override
		public void fetch(EffectiveVertexTaxCategory effectiveVertexTaxCategory) {
			if (effectiveVertexTaxCategory.getVertexTaxCategoryCode()!= null) {
				// This is considered not-critical data, so just eat the error and move on.
				try {
					effectiveVertexTaxCategory.setVertexTaxCategory(
							taxCategoryService.fetchOneTaxCategory(effectiveVertexTaxCategory.getVertexTaxCategoryCode()));
				} catch (CheckedSoapException e) {
					ProductInformationController.logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * Get vertex tax category given a tax code.
	 *
	 * @param taxCode The tax code to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return The vertex tax category matching the search.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_VERTEX_TAX_CATEGORY_BY_TAX_CODE)
	public VertexTaxCategory getVertexTaxCategoryByTaxCode(@RequestParam String taxCode, HttpServletRequest request){

		// tax code is required.
		this.parameterValidator.validate(taxCode, ProductInformationController.MISSING_TAX_CODE_ERROR_MESSAGE,
				ProductInformationController.MISSING_TAX_CODE_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetVertexTaxCategoryByTaxCode(request.getRemoteAddr(), taxCode);

		return this.productInformationService.getVertexTaxCategory(taxCode);
	}

	/**
	 * Get vertex tax qualifying condition given a tax qualifying code.
	 *
	 * @param taxQualifyingCode The tax qualifying code to search for.
	 * @param request           The HTTP request that initiated this call.
	 * @return The vertex tax category matching the search.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_VERTEX_TAX_CATEGORY_BY_TAX_QUALIFYING_CODE)
	public VertexTaxCategory getVertexTaxCategoryByTaxQualifyingCode(@RequestParam String taxQualifyingCode, HttpServletRequest request) throws Exception{

		// tax code is required.
		this.parameterValidator.validate(taxQualifyingCode, ProductInformationController.MISSING_TAX_CODE_ERROR_MESSAGE,
				ProductInformationController.MISSING_TAX_CODE_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetVertexTaxCategoryByTaxQualifyingCode(request.getRemoteAddr(), taxQualifyingCode);

		return this.taxCategoryService.fetchOneQualifyingCondition(taxQualifyingCode);
	}

	/**
	 * Get price detail given a upc.
	 *
	 * @param upc     The upc to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return The price detail matching the search.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_PRICE_DETAIL)
	public PriceDetail getPriceDetail(@RequestParam Long upc, HttpServletRequest request){

		// upc is required.
		this.parameterValidator.validate(upc, ProductInformationController.MISSING_UPC_ERROR_MESSAGE,
				ProductInformationController.MISSING_UPC_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetPriceDetailByUpc(request.getRemoteAddr(), upc);

		return this.productInformationService.getPriceInformation(upc);
	}

	/**
	 * Get product assortment for a given product id.
	 *
	 * @param productId The product id to search for.
	 * @param request   The HTTP request that initiated this call.
	 * @return The product assortment matching the search.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_PRODUCT_ASSORTMENT)
	public ProductAssortment getProductAssortment(@RequestParam String productId, HttpServletRequest request){

		// product id is required.
		this.parameterValidator.validate(productId, ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetProductAssortmentByProductId(request.getRemoteAddr(), productId);

		return this.productInformationService.getTotalCount(productId);
	}

	/**
	 * For a given product id, find a product relationship (if any) that has a deposit product relationship code, and
	 * return the related product from that relationship.
	 *
	 * @param productId The product id to search for.
	 * @param request   The HTTP request that initiated this call.
	 * @return The related product master of the product relationship found.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_DEPOSIT_RELATED_PRODUCT)
	public ProductMaster getRelatedProductByDepositUpcRelationship(@RequestParam Long productId, HttpServletRequest request){

		// product id is required.
		this.parameterValidator.validate(productId, ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetRelatedProductByDepositUpcRelationship(request.getRemoteAddr(), productId);

		return this.productInformationService.findRelatedProductByDepositProductRelationship(productId);
	}

	/**
	 * For a given upc, find a the current effective map price and return it.
	 *
	 * @param upc     The upc to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return The current effective map price.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_EFFECTIVE_MAP_PRICES)
	public List<MapPrice> getEffectiveMapPrices(@RequestParam Long upc, HttpServletRequest request){

		// upc is required.
		this.parameterValidator.validate(upc, ProductInformationController.MISSING_UPC_ERROR_MESSAGE,
				ProductInformationController.MISSING_UPC_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetEffectiveMapPrices(request.getRemoteAddr(), upc);

		return this.productInformationService.getEffectiveMapPrices(upc);
	}

	/**
	 * For a given product id, return an empty product description with the product id defined.
	 *
	 * @param prodId  The product id to set on a product description.
	 * @param request The HTTP request that initiated this call.
	 * @return A new product description with the product id given.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_PRODUCT_DESCRIPTION_TEMPLATE_WITH_PROD_ID)
	public ProductDescription getProductDescriptionTemplateWithProdId(@RequestParam Long prodId, HttpServletRequest request){

		// upc is required.
		this.parameterValidator.validate(prodId, ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetProductDescriptionTemplateWithProdId(request.getRemoteAddr(), prodId);

		ProductDescription productDescription = new ProductDescription();
		productDescription.setKey(new ProductDescriptionKey());
		productDescription.getKey().setProductId(prodId);
		return productDescription;
	}

	/**
	 * Saves new product information changes based on the fields that were changed.
	 *
	 * @param productInfoParameters The product information to update.
	 * @param request               The HTTP request that initiated this call.
	 */
	@RequestMapping(method = RequestMethod.POST, value = ProductInformationController.UPDATE)
	public void update(@RequestBody ProductInfoParameters productInfoParameters, HttpServletRequest request) {

		// productInfoParameters is required.
		this.parameterValidator.validate(productInfoParameters,
				ProductInformationController.MISSING_PRODUCT_INFORMATION_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_INFORMATION_ERROR_MESSAGE_KEY, request.getLocale());
		// product master within productInfoParameters is required.
		this.parameterValidator.validate(productInfoParameters.getProductMaster(),
				ProductInformationController.MISSING_PRODUCT_MASTER_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_MASTER_ERROR_MESSAGE_KEY, request.getLocale());
		// product master id within productInfoParameters is required.
		this.parameterValidator.validate(productInfoParameters.getProductMaster().getProdId(),
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY, request.getLocale());

		// initial log
		this.logUpdate(request.getRemoteAddr(), productInfoParameters);
					this.productInformationService.update(productInfoParameters);

		// completion log
		this.logRequestComplete(ProductInformationController.UPDATE);
	}

	/**
	 * Gets all tax qualifying conditions.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return Collection of all tax qualifying conditions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_ALL_TAX_QUALIFYING_CONDITIONS)
	public Collection<VertexTaxCategory> getAllTaxQualifyingConditions(HttpServletRequest request){

		this.logGetAllTaxQualifyingConditions(request.getRemoteAddr());

		Collection<VertexTaxCategory> toReturn =  this.taxCategoryService.fetchAllQualifyingConditions();
		this.logRequestComplete(ProductInformationController.GET_ALL_TAX_QUALIFYING_CONDITIONS);
		return toReturn;
	}

	/**
	 * Gets all vertex tax category effective maintenance for a product.
	 *
	 * @param productId the product id
	 * @param request   The HTTP request that initiated this call.
	 * @return List of effective vertex tax categories.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_ALL_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE_FOR_PRODUCT)
	public List<EffectiveVertexTaxCategory> getAllVertexTaxCategoryEffectiveMaintenanceForProduct(Long productId, HttpServletRequest request){

		// product id is required.
		this.parameterValidator.validate(productId,
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE,
				ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY, request.getLocale());
		this.logGetAllVertexTaxCategoryEffectiveMaintenanceForProduct(request.getRemoteAddr(), productId);

		List<EffectiveVertexTaxCategory> toReturn =
				this.productInformationService.getAllVertexTaxCategoryEffectiveMaintenanceForProduct(productId);
		this.resolveVertexTaxCategoryResults(toReturn);
		return toReturn;
	}

	/**
	 * Gets all product retail links given a retail link code.
	 *
	 * @param retailLinkCode the retail link code
	 * @param request        The HTTP request that initiated this call.
	 * @return List of product retail links.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_ALL_PRODUCT_RETAIL_LINKS_BY_RETAIL_LINK_CODE)
	public List<ProductRetailLink> getAllProductRetailLinksByRetailLinkCode(Long retailLinkCode, HttpServletRequest request){

		// retail link is required.
		this.parameterValidator.validate(retailLinkCode,
				ProductInformationController.MISSING_RETAIL_LINK_ERROR_MESSAGE,
				ProductInformationController.MISSING_RETAIL_LINK_ERROR_MESSAGE_KEY, request.getLocale());
		this.logGetAllProductRetailLinksByRetailLinkCode(request.getRemoteAddr(), retailLinkCode);
		return this.productInformationService.getAllProductRetailLinksByRetailLinkCode(retailLinkCode);
	}

	/**
	 * Updates vertex tax category effective maintenance.
	 *
	 * @param updatedEffectiveVertexCategories The list of effective vertex tax categories to update.
	 * @param request The HTTP request that initiated this call.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.POST, value = ProductInformationController.UPDATE_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE)
	public void updateVertexTaxCategoryEffectiveMaintenance(@RequestBody List<EffectiveVertexTaxCategory> updatedEffectiveVertexCategories, HttpServletRequest request){

		// effective categories are required.
		this.parameterValidator.validate(updatedEffectiveVertexCategories,
				ProductInformationController.MISSING_EFFECTIVE_VERTEX_CATEGORIES_ERROR_MESSAGE,
				ProductInformationController.MISSING_EFFECTIVE_VERTEX_CATEGORIES_ERROR_MESSAGE_KEY, request.getLocale());

		// product id is required.
		for(EffectiveVertexTaxCategory effectiveVertexTaxCategory : updatedEffectiveVertexCategories) {
			this.parameterValidator.validate(effectiveVertexTaxCategory.getProductId(),
					ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE,
					ProductInformationController.MISSING_PRODUCT_ID_ERROR_MESSAGE_KEY, request.getLocale());
		}
		this.logUpdateVertexTaxCategoryEffectiveMaintenance(request.getRemoteAddr(), updatedEffectiveVertexCategories);

		this.productInformationService.updateVertexTaxCategoryEffectiveMaintenance(updatedEffectiveVertexCategories);
	}

	/**
	 * Finds all vertex tax category codes used by a sub commodity.
	 *
	 * @param subCommodityCode The sub commodity code to look for.
	 * @param request The HTTP request that initiated this call.
	 * @return List of effective vertex tax categories.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_ALL_VERTEX_TAX_CATEGORY_CODES_BY_SUB_COMMODITY_CODE)
	public List<VertexTaxCategory> getAllVertexTaxCategoryCodesBySubCommodityCode(Integer subCommodityCode, HttpServletRequest request){

		// sub commodity code is required.
		this.parameterValidator.validate(subCommodityCode,
				ProductInformationController.MISSING_SUB_COMMODITY_CODE_ERROR_MESSAGE,
				ProductInformationController.MISSING_SUB_COMMODITY_CODE_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetAllVertexTaxCategoryCodesBySubCommodityCode(request.getRemoteAddr(), subCommodityCode);

		List<String> taxCategoryCodes =
				this.productInformationService.getAllVertexTaxCategoryCodesBySubCommodityCode(subCommodityCode);
		return this.taxCategoryService.findVertexTaxCategoriesByTaxCategoryCodes(taxCategoryCodes);
	}

	/**
	 * Finds all vertex tax category codes used by a sub commodity.
	 *
	 * @param vertexTaxCategoryCode The vertex tax category code to look for.
	 * @param includeCount Whether to include count.
	 * @param page The page requested.
	 * @param pageSize The page size requested.
	 * @param request The HTTP request that initiated this call.
	 * @return List of effective vertex tax categories.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_PRODUCTS_BY_VERTEX_TAX_CATEGORY_CODE)
	public PageableResult<ProductMaster> getProductMastersByVertexTaxCategoryCode(String vertexTaxCategoryCode, Boolean includeCount,
																				  Integer page, Integer pageSize, HttpServletRequest request){

		// vertex tax category code is required.
		this.parameterValidator.validate(vertexTaxCategoryCode,
				ProductInformationController.MISSING_EFFECTIVE_VERTEX_CATEGORIES_ERROR_MESSAGE,
				ProductInformationController.MISSING_EFFECTIVE_VERTEX_CATEGORIES_ERROR_MESSAGE_KEY, request.getLocale());

		this.logGetProductMastersByVertexTaxCategoryCode(request.getRemoteAddr(), vertexTaxCategoryCode);

		// Set some defaults for the parameters that make sense
		// to have default values.
		boolean ic = includeCount == null ? Boolean.FALSE : includeCount;
		int pg = page == null ? ProductInformationController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ProductInformationController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<ProductMaster> toReturn = this.productInformationService.
				getProductMastersByVertexTaxCategoryCode(vertexTaxCategoryCode, ic, pg, ps);
		toReturn.getData().forEach(productMaster -> this.vertexCategoryProductResolver.fetch(productMaster));
		return toReturn;
	}

	/**
	 * Gets all tax qualifying conditions.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return Collection of all tax qualifying conditions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_ALL_VERTEX_TAX_CATEGORIES)
	public Collection<VertexTaxCategory> getAllVertexTaxCategories(HttpServletRequest request){

		this.logGetAllTaxQualifyingConditions(request.getRemoteAddr());

		Collection<VertexTaxCategory> toReturn =  this.taxCategoryService.fetchAllTaxCategories();
		this.logRequestComplete(ProductInformationController.GET_ALL_VERTEX_TAX_CATEGORIES);
		return toReturn;
	}

	/**
	 * Gets sub commodity for defaults residing at sub commodity level. This includes tax category and non-tax
	 * category.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return Collection of all tax qualifying conditions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_SUB_COMMODITY_DEFAULTS)
	public SubCommodity getSubCommodityDefaults(SubCommodityKey key, HttpServletRequest request){

		// sub commodity key is required.
		this.parameterValidator.validate(key,
				ProductInformationController.MISSING_SUB_COMMODITY_KEY_ERROR_MESSAGE,
				ProductInformationController.MISSING_SUB_COMMODITY_KEY_ERROR_MESSAGE_KEY, request.getLocale());
		// sub commodity code is required.
		this.parameterValidator.validate(key.getSubCommodityCode(),
				ProductInformationController.MISSING_SUB_COMMODITY_CODE_ERROR_MESSAGE,
				ProductInformationController.MISSING_SUB_COMMODITY_CODE_ERROR_MESSAGE_KEY, request.getLocale());
		// commodity code is required.
		this.parameterValidator.validate(key.getCommodityCode(),
				ProductInformationController.MISSING_COMMODITY_CODE_ERROR_MESSAGE,
				ProductInformationController.MISSING_COMMODITY_CODE_ERROR_MESSAGE_KEY, request.getLocale());
		// class code is required.
		this.parameterValidator.validate(key.getClassCode(),
				ProductInformationController.MISSING_CLASS_CODE_ERROR_MESSAGE,
				ProductInformationController.MISSING_CLASS_CODE_ERROR_MESSAGE_KEY, request.getLocale());
		this.logGetSubCommodityDefaults(request.getRemoteAddr(), key);

		return this.subCommodityService.findOne(key);
	}

	private void logGetSubCommodityDefaults(String ip, SubCommodityKey key) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_SUB_COMMODITY_DEFAULTS_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, key));
	}

	/**
	 * Log's a user's request to get products by vertex tax category code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param vertexTaxCategoryCode The vertex tax category code to look for.
	 */
	private void logGetProductMastersByVertexTaxCategoryCode(String ip, String vertexTaxCategoryCode) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_PRODUCTS_BY_VERTEX_TAX_CATEGORY_CODE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, vertexTaxCategoryCode));
	}

	/**
	 * Log's a user's request to get all vertex tax category codes by sub commodity.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param subCommodityCode The sub commodity code to search for.
	 */
	private void logGetAllVertexTaxCategoryCodesBySubCommodityCode(String ip, Integer subCommodityCode) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_ALL_VERTEX_TAX_CATEGORY_CODES_BY_SUB_COMMODITY_CODE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, subCommodityCode));
	}

	/**
	 * Log's a user's request to update vertex tax category effective maintenance.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param updatedEffectiveVertexCategories The vertex tax categories to update.
	 */
	private void logUpdateVertexTaxCategoryEffectiveMaintenance(String ip, List<EffectiveVertexTaxCategory> updatedEffectiveVertexCategories) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.UPDATE_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, updatedEffectiveVertexCategories));
	}
	/**
	 * Resolves list passed in and loads the lazily loaded objects needed by the effective vertex tax category functions
	 * of the REST endpoint.
	 *
	 * @param effectiveVertexTaxCategories The list to load data for.
	 */
	private void resolveVertexTaxCategoryResults(List<EffectiveVertexTaxCategory> effectiveVertexTaxCategories) {
		effectiveVertexTaxCategories.forEach(
				effectiveVertexTaxCategory ->
						this.effectiveVertexTaxCategoryResolver.fetch(effectiveVertexTaxCategory));
	}

	/**
	 * Log's a user's request to get all product retail links for a retail link code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param retailLinkCode The retail link code to search for.
	 */
	private void logGetAllProductRetailLinksByRetailLinkCode(String ip, Long retailLinkCode) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_ALL_PRODUCT_RETAIL_LINKS_BY_RETAIL_LINK_CODE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, retailLinkCode));
	}

	/**
	 * Log's a user's request to get all effective maintenance for a product.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param productId The product id to search for.
	 */
	private void logGetAllVertexTaxCategoryEffectiveMaintenanceForProduct(String ip, Long productId) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_ALL_VERTEX_TAX_CATEGORY_EFFECTIVE_MAINTENANCE_FOR_PRODUCT_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, productId));
	}

	/**
	 * Log's a user's request to get all tax qualifying conditions.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logGetAllTaxQualifyingConditions(String ip) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_ALL_TAX_QUALIFYING_CONDITIONS_LOG_MESSAGE,
				this.userInfo.getUserId(), ip));
	}

	/**
	 * Log's a user's request to update product information.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param productInfoParameters The product information to update.
	 */
	private void logUpdate(String ip, ProductInfoParameters productInfoParameters) {
		ProductInformationController.logger.info(String.format(ProductInformationController.UPDATE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, productInfoParameters.toString()));
	}

	/**
	 * Log's a user's request to get a product description with the product id defined.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param prodId The product id to set on a product description.
	 */
	private void logGetProductDescriptionTemplateWithProdId(String ip, Long prodId) {
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_PRODUCT_DESCRIPTION_TEMPLATE_WITH_PROD_ID_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, prodId));
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ProductInformationController.logger.info(
				String.format(ProductInformationController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Log's a user's request to get current effective map price by upc.
	 * @param ip The IP address the user is logged in from.
	 * @param upc The upc to search on.
	 */
	private void logGetEffectiveMapPrices(String ip, Long upc){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_EFFECTIVE_MAP_PRICES_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, upc));
	}

	/**
	 * Log's a user's request to get vertex tax category by tax code.
	 * @param ip The IP address the user is logged in from.
	 * @param taxCode The tax code to search on.
	 */
	private void logGetVertexTaxCategoryByTaxCode(String ip, String taxCode){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_VERTEX_TAX_CATEGORY_BY_TAX_CODE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, taxCode));
	}

	/**
	 * Log's a user's request to get vertex tax category by tax qualifying code.
	 * @param ip The IP address the user is logged in from.
	 * @param taxQualifyingCode The tax qualifying code to search on.
	 */
	private void logGetVertexTaxCategoryByTaxQualifyingCode(String ip, String taxQualifyingCode){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_VERTEX_TAX_CATEGORY_BY_TAX_QUALIFYING_CODE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, taxQualifyingCode));
	}

	/**
	 * Log's a user's request to get price detail by upc.
	 * @param ip The IP address the user is logged in from.
	 * @param upc The UPC to search on.
	 */
	private void logGetPriceDetailByUpc(String ip, Long upc){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_PRICE_DETAIL_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, upc));
	}

	/**
	 * Log's a user's request to get product assortment by prodId.
	 * @param ip The IP address the user is logged in from.
	 * @param prodId The product id to search on.
	 */
	private void logGetProductAssortmentByProductId(String ip, String prodId){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_PRODUCT_ASSORTMENT_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, prodId));
	}

	/**
	 * Log's a user's request to get a related product by prodId.
	 * @param ip The IP address the user is logged in from.
	 * @param prodId The product id to search on.
	 */
	private void logGetRelatedProductByDepositUpcRelationship(String ip, Long prodId){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.GET_DEPOSIT_RELATED_PRODUCT_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, prodId));
	}

	/**
	 * Gets shelf attributes audit information
	 * @param request The HTTP request that initiated this call.
	 * @return shelf attributes audit information
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_SHELF_ATTRIBUTES_AUDITS)
	public List<AuditRecord> getShelfAttributesAudits(@RequestParam(value="prodId") Long prodId, HttpServletRequest request){
		this.logGetShelfAttributesAuditInformation(request.getRemoteAddr(), prodId);

		List<AuditRecord> shelfAttributesAuditRecords = this.productInformationService.getShelfAttributesAuditInformation(prodId);

		return shelfAttributesAuditRecords;
	}

	/**
	 * Logs get shelf attribute audit information by prodId.
	 *
	 * @param ip The user's ip.
	 * @param prodId The prodId being searched on.
	 */
	private void logGetShelfAttributesAuditInformation(String ip, Long prodId) {
		ProductInformationController.logger.info(
				String.format(ProductInformationController.LOG_SHELF_ATTRIBUTES_AUDIT_BY_PRODUCT_ID, this.userInfo.getUserId(), ip, prodId)
		);
	}


	/**
	 * Log's a user's request to get a product info audits by prodId.
	 * @param ip The IP address the user is logged in from.
	 * @param prodId The product id to search on.
	 */
	private void logGetProductInfoAuditInformation(String ip, Long prodId){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.LOG_PRODUCT_INFO_AUDIT_BY_PRODUCT_ID,
				this.userInfo.getUserId(), ip, prodId));
	}

	/**
	 * Retrieves product info audit information.
	 * @param prodId The Product ID that the audit is being searched on.
	 * @param request The HTTP request that initiated this call.
	 * @return The list of product info audits attached to given product ID.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_PRODUCT_INFO_AUDITS)
	public List<AuditRecord> getProductInfoAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
		this.logGetProductInfoAuditInformation(request.getRemoteAddr(), prodId);

		List<AuditRecord> productInfoAuditRecords = this.productInformationService.getProductInfoAuditInformation(prodId);

		return productInfoAuditRecords;
	}

	/**
	 * Calls excel export for product retail link.
	 * @param retailLinkCode the retail link code to get list of product retail link.
	 * @param downloadId the download id.
	 * @param request The HTTP request that initiated this call.
	 * @param response The HTTP response.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = EXPORT_PRODUCT_RETAIL_LINKS_TO_CSV_URL, headers = "Accept=text/csv")
	public void exportProductRetailLinksToCsv(@RequestParam(value = "retailLinkCode",  required = true) Long retailLinkCode,
											  @RequestParam(value = "downloadId", required = false) String downloadId,
											  HttpServletRequest request,
											  HttpServletResponse response) {
		this.logExportProductRetailLinksToCsvByRetailLinkCode(request.getRemoteAddr(), retailLinkCode);
		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}
		try {
			this.productInformationService.exportProductRetailLinksToCsv(response.getOutputStream(), retailLinkCode);
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Log's a user's request to export product retail link.
	 * @param ip The IP address the user is logged in from.
	 * @param retailLinkCode the retail link code to get list of product retail link.
	 */
	private void logExportProductRetailLinksToCsvByRetailLinkCode(String ip, Long retailLinkCode){
		ProductInformationController.logger.info(String.format(
				ProductInformationController.LOG_EXPORT_PRODUCT_RETAIL_LINKS,
				this.userInfo.getUserId(), ip, retailLinkCode));
	}

	/**
	 * Get Tax or non tax of category by subCommodityKey.
	 * @param key the subCommodityKey
	 * @param request
	 * @return VertexTaxCategory
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ProductInformationController.GET_TAX_OR_NON_TAX_CAT_BY_SUB_COM_KEY)
	public Map<String, VertexTaxCategory> getTaxAndNonTaxCategoryBySubCommodity(SubCommodityKey key,
																				HttpServletRequest request) {
		ProductInformationController.logger.info(
				String.format(ProductInformationController.LOG_GET_TAX_OR_NON_TAX_CAT_BY_SUB_COM_KEY, this.userInfo.getUserId(), request.getRemoteAddr())
		);
		SubCommodity subCommodity = this.subCommodityService.findOne(key);
		Map<String, VertexTaxCategory> result = new HashMap<>();
		if (subCommodity != null) {
			if (StringUtils.isNotBlank(subCommodity.getTaxCategoryCode())) {
				result.put(TAX_CATEGORY_KEY, productInformationService.getVertexTaxCategory(subCommodity.getTaxCategoryCode()));
			}
			if (StringUtils.isNotBlank(subCommodity.getNonTaxCategoryCode())) {
				result.put(NON_TAX_CATEGORY_KEY, productInformationService.getVertexTaxCategory(subCommodity.getNonTaxCategoryCode()));
			}
		}
		return result;
	}
}