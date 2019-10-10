/*
 *  ProductECommerceViewController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.eCommerceView;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.audit.AuditService;
import com.heb.pm.entity.*;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.util.audit.AuditRecord;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Represents product eCommerce View.
 *
 * @author vn70516
 * @since 2.0.14
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductECommerceViewController.PRODUCT_ECOMMERCE_VIEW)
@AuthorizedResource(ResourceConstants.PRODUCT_ECOMMERCE_VIEW)
public class ProductECommerceViewController {

    private static final Logger logger = LoggerFactory.getLogger(ProductECommerceViewController.class);
    protected static final String PRODUCT_ECOMMERCE_VIEW = "/productECommerceView";

    // urls
    private static final String FIND_ALL_PRODUCT_PANEL_TYPE = "findAllProductPanelType";
    private static final String FIND_ALL_SALE_CHANNEL = "findAllSaleChanel";
    private static final String FIND_ALL_PDP_TEMPLATE = "findAllPDPTemplate";
    private static final String GET_ECOMMERCE_VIEW_INFORMATION = "/getECommerceViewInformation";
    private static final String GET_MORE_ECOMMERCE_VIEW_INFORMATION_BY_TAB = "/getMoreECommerceViewInformationByTab";
    private static final String GET_BRAND_INFORMATION = "/getBrandInformation";
    private static final String GET_DISPLAY_NAME_INFORMATION = "/getDisplayNameInformation";
    private static final String GET_SIZE_INFORMATION = "/getSizeInformation";
    private static final String FIND_ECOMMERCE_VIEW_IMAGE_BY_URI = "/findECommerceViewImageByUri";
    private static final String GET_CUSTOM_HIERACHY_ASSGIMENT = "/getCustomHierarchyAssigment";
    private static final String GET_CUSTOM_HIERACHY = "/getCustomHierarchy";
    private static final String SAVE_CUSTOM_HIERACHY_ASSGIMENT = "/saveCustomHierarchyAssigment";
    private static final String URL_FIND_PRODUCT_RELATIONSHIPS = "/findProductRelationships";
    private static final String URL_FIND_WARNINGS = "/findWarnings";
    private static final String VALIDATE_CHANGED_PUBLISHED_SOURCE = "/validateChangedPublishedSource";
    private static final String GET_NUTRITION_FACT_INFORMATION = "/getNutritionFactInformation";
    private static final String UPDATE_NUTRITION_FACT_INFORMATION = "/updateNutritionFactInformation";
    private static final String GET_ALL_NUTRITION_FACTS_BY_ATTRIBUTE_PRIORITIES = "/getAllNutritionFactsByAttributePriorities";
    private static final String FIND_PRODUCT_DESCRIPTION = "findProductDescription";
    private static final String FIND_TAGS = "findTags";
    private static final String URL_FIND_DIRECTIONS = "/findDirections";
    private static final String GET_DIMENSION_INFORMATION = "/getDimensionInformation";
    private static final String URL_FIND_INGREDIENTS = "/findIngredients";
    private static final String URL_FIND_ALL_INGREDIENT_ATTRIBUTE = "/findAllIngredientAttributePriorities";
    private static final String GET_SPECIFICATION_INFORMATION = "/getSpecificationInformation";
    private static final String CHECK_FULFILLMENT = "/checkFulfillment";
    private static final String URL_UPDATE_INGREDIENT_ATTRIBUTE_PRIORITIES = "/updateIngredientAttributePriorities";
    private static final String FIND_HEB_GUARANTEE_TYPE_CODE = "findHebGuaranteeTypeCode";
    private static final String GET_ECOMMERCE_VIEW_DATA_SOURCE = "/getECommerceViewDataSource";
    private static final String UPDATE_ECOMMERCE_VIEW_DATA_SOURCE = "/updateECommerceViewDataSource";
    private static final String URL_FIND_ALL_TAG_ATTRIBUTE = "/findAllTagAttributePriorities";
    private static final String URL_FIND_ALL_SPECIFICATION_ATTRIBUTE = "/findAllSpecificationAttributePriorities";
    private static final String UPDATE_ECOMMERCE_VIEW_INFORMATION = "/updateECommerceViewInformation";
    private static final String GET_ALL_DIMENSIONS_BY_ATTRIBUTE_PRIORITIES = "/getAllDimensionsByAttributePriorities";
    private static final String FIND_ATTRIBUTE_MAPPING_BY_LOGICAL_ATTRIBUTE = "/findAttributeMappingByLogicalAttribute";
    private static final String GET_ECOMMERCE_VIEW_AUDIT = "/eCommerceViewAudit";
    private static final String GET_PUBLISHED_ATTRIBUTES_AUDITS = "/getPublishedAttributesAudits";
    private static final String GET_FULFILLMENT_ATTRIBUTES_AUDITS = "/getFulfillmentAttributesAudits";
    private static final String FIND_FAVOR_ITEM_DESCRIPTION = "findFavorItemDescription";

    //logs
    private static final String GET_ALL_DIMENSIONS_BY_ATTRIBUTE_PRIORITIES_LOG_MESSAGE = "User %s from IP %s has requested get all dimensions information by attribute priorities";
    private static final String UPDATE_NUTRITION_FACT_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested update nutrition fact information";
    private static final String VALIDATE_CHANGED_PUBLISHED_SOURCE_LOG_MESSAGE = "User %s from IP %s has requested to validate changed published source";
    private static final String GET_NUTRITION_FACT_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested get nutrition fact information";
    private static final String GET_ALL_NUTRITION_FACTS_BY_ATTRIBUTE_PRIORITIES_LOG_MESSAGE = "User %s from IP %s has requested get all nutrition facts information by attribute priorities";
    private static final String GET_DIMENSION_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested get dimension information";
    private static final String GET_SPECIFICATION_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested get specification information";
    private static final String FIND_ALL_PRODUCT_PANEL_TYPE_LOG_MESSAGE = "User %s from IP %s has requested find all Product Panel Type";
    private static final String FIND_ALL_SALE_CHANNEL_LOG_MESSAGE = "User %s from IP %s has requested find all Sale Chanel";
    private static final String FIND_ALL_PDP_TEMPLATE_LOG_MESSAGE = "User %s from IP %s has requested find all PDP Template";
    private static final String GET_ECOMMERCE_VIEW_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested eCommerce View information with product id %s and upc %s";
    private static final String FIND_ECOMMERCE_VIEW_IMAGE_BY_URI_LOG_MESSAGE  = "User %s from IP %s has requested eCommerce View image with URI %s.";
    private static final String GET_MORE_ECOMMERCE_VIEW_INFORMATION_BY_TAB_LOG_MESSAGE  = "User %s from IP %s has requested eCommerce View information with product id %s, product primary upc %s, commodity code %s";
    private static final String GET_BRAND_INFORMATION_LOG_MESSAGE  = "User %s from IP %s has requested get brand information";
    private static final String GET_DISPLAY_NAME_INFORMATION_LOG_MESSAGE  = "User %s from IP %s has requested get display name information";
    private static final String GET_SIZE_INFORMATION_LOG_MESSAGE  = "User %s from IP %s has requested get size information";
    private static final String FIND_PRODUCT_RELATIONSHIPS_MESSAGE = "User %s from IP %s requested to find related products";
    private static final String FIND_WARNINGS_MESSAGE = "User %s from IP %s requested to find warnings";
    private static final String FIND_PRODUCT_DESCRIPTION_LOG_MESSAGE = "User %s from IP %s requested to find product description by product id: %s, upc: %s";
    private static final String FIND_TAGS_LOG_MESSAGE = "User %s from IP %s requested to find tags by product id: %s, upc: %s";
    private static final String FIND_DIRECTIONS_MESSAGE = "User %s from IP %s requested to find directions";
    private static final String FIND_INGREDIENTS_MESSAGE = "User %s from IP %s requested to find ingredients";
    private static final String FIND_ALL_INGREDIENT_ATTRIBUTE_MESSAGE = "User %s from IP %s requested to find all ingredient attribute priorities";
    private static final String CHECK_FULFILLMENT_MESSAGE = "User %s from IP %s requested to check fulfillment for publish.";
    private static final String UPDATE_INGREDIENT_ATTRIBUTE_PRIORITIES_MESSAGE = "User %s from IP %s requested to update ingredient attribute priorities.";
    private static final String FIND_HEB_GUARANTEE_TYPE_CODE_MESSAGE = "User %s from IP %s requested to find Heb Guarantee Type Code by productId: %s.";
    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";
    private static final String GET_ECOMMERCE_VIEW_DATA_SOURCE_LOG_MESSAGE = "User %s from IP %s requested to get eCommerce View data source.";
    private static final String UPDATE_ECOMMERCE_VIEW_DATA_SOURCE_LOG_MESSAGE = "User %s from IP %s requested to update eCommerce View data source.";
    private static final String FIND_ALL_TAG_ATTRIBUTE_MESSAGE = "User %s from IP %s requested to find all tag attribute priorities";
    private static final String FIND_ALL_SPECIFICATION_ATTRIBUTE_MESSAGE = "User %s from IP %s requested to find all specification attribute priorities";
    private static final String UPDATE_ECOMMERCE_VIEW_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested update eComerce View information";
    private static final String FIND_ATTRIBUTE_MAPPING_BY_LOGICAL_ATTRIBUTE_LOG_MESSAGE = "User %s from IP %s has requested find attribute mapping by logical attribute %s";
    private static final String SAVE_CUSTOM_HIERARCHY_ASSIGMENT_LOG_MESSAGE = "User %s from IP %s has requested save custom hierarchy assigment";
    private static final String GET_CUSTOM_HIERARCHY_LOG_MESSAGE = "User %s from IP %s has requested get custom hierarchy";
    private static final String GET_CUSTOM_HIERARCHY_ASSIGMENT_LOG_MESSAGE = "User %s from IP %s has requested get custom hierarchy assigment";
    private static final String GET_ECOMMERCE_VIEW_AUDIT_LOG_MESSAGE = "User %s from IP %s has requested get eCommerce view audit";
    private static final String GET_PUBLISHED_ATTRIBUTES_AUDITS_LOG_MESSAGE = "User %s from IP %s has requested get published attribute";
    private static final String GET_FULFILLMENT_ATTRIBUTES_AUDITS_LOG_MESSAGE = "User %s from IP %s has requested get fulfillment attribute";
    private static final String FIND_FAVOR_ITEM_DESCRIPTION_LOG_MESSAGE = "User %s from IP %s requested to find favor item description by product id: %s, upc: %s";
    @Autowired
    private ProductECommerceViewService service;

    @Autowired
    private UserInfo userInfo;

    @Autowired
    private CodeTableManagementServiceClient codeTableManagementServiceClient;

    @Autowired
    private AuditService auditService;

    private LazyObjectResolver<List<ECommerceViewAttributePriorities>> eCommerceViewAttributePrioritiesLazyObjectResolver = new ECommerceViewAttributePrioritiesResolver();

    private LazyObjectResolver<List<ProductRelationship>> productRelationshipResolver = new ProductRelationshipResolver();

    /**
     * Resolves a ECommerceViewAttributePriorities object. It will load the following properties:
     */
    private class ECommerceViewAttributePrioritiesResolver implements LazyObjectResolver<List<ECommerceViewAttributePriorities>> {
        @Override
        public void fetch(List<ECommerceViewAttributePriorities> eCommerceViewAttributePriorities) {
            if(eCommerceViewAttributePriorities != null && eCommerceViewAttributePriorities.size() > 0) {
                eCommerceViewAttributePriorities.forEach(eCommerceViewAttributePriority -> {
                    List<ProductNutrient> productNutrients = eCommerceViewAttributePriority.getProductNutrients();
                    List<ProductPkVariation> productPkVariations = eCommerceViewAttributePriority.getProductPkVariations();
                    ScaleUpc scaleUpc = eCommerceViewAttributePriority.getScaleUpc();
                    if(productNutrients != null && productNutrients.size() > 0) {
                        productNutrients.forEach((productNutrient) -> {
                            productNutrient.getKey().getUpc();
                            if (productNutrient.getNutrientMaster() != null) {
                                productNutrient.getNutrientMaster().getMasterId();
                            }
                            if (productNutrient.getServingSizeUOM() != null) {
                                productNutrient.getServingSizeUOM().getServingSizeUomCode();
                            }
                        });
                    }
                    if(productPkVariations != null && productPkVariations.size() > 0) {
                        productPkVariations.forEach((productPkVariation) -> {
                            productPkVariation.getKey().getUpc();
                        });
                    }
                    if(scaleUpc != null) {
                        scaleUpc.getUpc();
                        if(scaleUpc.getNutrientStatementDetails() != null && scaleUpc.getNutrientStatementDetails().size() > 0) {
                            scaleUpc.getNutrientStatementDetails().forEach((nutrientStatementDetail) -> {
                                nutrientStatementDetail.getKey().getNutrientStatementNumber();
                                if(nutrientStatementDetail.getNutrient() != null) {
                                    nutrientStatementDetail.getNutrient().getNutrientCode();
                                    if(nutrientStatementDetail.getNutrient().getNutrientUom() != null) {
                                        nutrientStatementDetail.getNutrient().getNutrientUom().getNutrientUomCode();
                                    }
                                }
                            });
                        }
                        if(scaleUpc.getNutrientStatementHeader() != null) {
                            scaleUpc.getNutrientStatementHeader().getNutrientStatementNumber();
                            if(scaleUpc.getNutrientStatementHeader().getNutrientCommonUom() != null) {
                                scaleUpc.getNutrientStatementHeader().getNutrientCommonUom().getNutrientUomCode();
                            }
                            if(scaleUpc.getNutrientStatementHeader().getNutrientMetricUom() != null) {
                                scaleUpc.getNutrientStatementHeader().getNutrientMetricUom().getNutrientUomCode();
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Resolver fetch product master data for Product relationship object.
     */
    private class ProductRelationshipResolver implements LazyObjectResolver<List<ProductRelationship>> {

        /**
         * Resolves a product relationship object with following properties:
         * 1. productMaster -> productId
         * 2. productMaster -> productEnglishDescription
         * 3. productMaster -> productPrimaryScanCode
         * 4. productMaster -> goodsProduct -> sellableProductSwitch
         *
         * @param productRelationships the product relationship to fetch product master for.
         */
        @Override
        public void fetch(List<ProductRelationship> productRelationships) {
            productRelationships.forEach((productRelationship) -> {
                if (productRelationship.getRelatedProduct() != null) {
                    productRelationship.getRelatedProduct().getProdId();
                    productRelationship.getRelatedProduct().getDescription();
                    productRelationship.getRelatedProduct().getProductPrimaryScanCodeId();
                    if (productRelationship.getRelatedProduct().getGoodsProduct() != null) {
                        productRelationship.getRelatedProduct().getGoodsProduct().isSellableProduct();
                    }
                }
            });
        }
    }

    /**
     * Check the data of current source is different with the publish source.
     *
     * @param productId - The product id
     * @param logicAttributeCode - The logic attribute code
     * @param request The HTTP request that initiated this call.
     * @return The Boolean.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.VALIDATE_CHANGED_PUBLISHED_SOURCE)
    public ModifiedEntity<Boolean> validateChangedPublishedSource(@RequestParam(value = "productId") long productId,
    															  @RequestParam(value = "logicAttributeCode") long logicAttributeCode,
    															  HttpServletRequest request) {
    	ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.VALIDATE_CHANGED_PUBLISHED_SOURCE_LOG_MESSAGE, this.userInfo
    			.getUserId(), request.getRemoteAddr()));
    	Boolean returnData = this.service.validateChangedPublishedSource(productId, logicAttributeCode);
    	return new ModifiedEntity<>(returnData, null);
    }

    /**
     * Update nutrition fact information.
     *
     * @param eCommerceViewDetails - The ECommerceViewDetails
     * @param request The HTTP request that initiated this call.
     * @return The list of ECommerceViewAttributePriorities.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductECommerceViewController.UPDATE_NUTRITION_FACT_INFORMATION)
    public ModifiedEntity<List<ECommerceViewAttributePriorities>> updateNutritionFactInformation(@RequestBody ECommerceViewDetails eCommerceViewDetails,
                                                                                                 HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.UPDATE_NUTRITION_FACT_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        this.service.updateNutritionFactInformation(eCommerceViewDetails.getNutrientList(), eCommerceViewDetails.getProductId(), this.userInfo.getUserId());
        return new ModifiedEntity<>(null, ProductECommerceViewController.UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Get nutrition fact information.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @param request The HTTP request that initiated this call.
     * @return The list of ECommerceViewAttributePriorities.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_NUTRITION_FACT_INFORMATION)
    public List<ECommerceViewAttributePriorities> getNutritionFactInformation(@RequestParam(value = "productId") long productId,
                                                                              @RequestParam(value = "upc") long upc,
                                                                              @RequestParam(value = "sourceSystem") long sourceSystem,
                                                                              HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_NUTRITION_FACT_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        List<ECommerceViewAttributePriorities> returnList = this.service.getNutritionFactInformation(productId, upc, sourceSystem);
        this.eCommerceViewAttributePrioritiesLazyObjectResolver.fetch(returnList);
        return returnList;
    }

    /**
     * Get all nutrition facts information by attribute priorities.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @param request The HTTP request that initiated this call.
     * @return The list of ECommerceViewAttributePriorities.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_ALL_NUTRITION_FACTS_BY_ATTRIBUTE_PRIORITIES)
    public List<ECommerceViewAttributePriorities> getAllNutritionFactsByAttributePriorities(@RequestParam(value = "productId") long productId,
                                                                                            @RequestParam(value = "upc") long upc,
                                                                                            @RequestParam(value = "sourceSystem") long sourceSystem,
                                                                                            HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_ALL_NUTRITION_FACTS_BY_ATTRIBUTE_PRIORITIES_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        List<ECommerceViewAttributePriorities> returnList = this.service.getAllNutritionFactsByAttributePriorities(productId, upc, sourceSystem);
        this.eCommerceViewAttributePrioritiesLazyObjectResolver.fetch(returnList);
        return returnList;
    }

    /**
     * Get dimension information.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param request The HTTP request that initiated this call.
     * @return The list of dimension.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_DIMENSION_INFORMATION)
    public List<AttributeValue> getDimensionInformation(@RequestParam(value = "productId") long productId,
                                                   @RequestParam(value = "upc") long upc,
                                                   HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_DIMENSION_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        List<AttributeValue> returnList = this.service.getDimensionInformation(productId, upc);
        return returnList;
    }


    /**
     * Get all dimensions information by attribute priorities.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param sourceSystem - The source system
     * @param request The HTTP request that initiated this call.
     * @return The list of ECommerceViewAttributePriorities.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_ALL_DIMENSIONS_BY_ATTRIBUTE_PRIORITIES)
    public ECommerceViewAttributePriority getAllDimensionsByAttributePriorities(@RequestParam(value = "productId") long productId,
                                                                                        @RequestParam(value = "upc") long upc,
                                                                                        @RequestParam(value = "sourceSystem") long sourceSystem,
                                                                                        HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_ALL_DIMENSIONS_BY_ATTRIBUTE_PRIORITIES_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
       return this.service.getAllDimensionsByAttributePriorities(productId, upc, sourceSystem);
       // this.eCommerceViewAttributePrioritiesLazyObjectResolver.fetch(returnList);
       // return returnList;
    }

    /**
     * Get specification information.
     *
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param request The HTTP request that initiated this call.
     * @return The list of specification.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_SPECIFICATION_INFORMATION)
    public List<AttributeValue> getSpecificationInformation(@RequestParam(value = "productId") long productId,
                                                           @RequestParam(value = "upc") long upc,
                                                           HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_SPECIFICATION_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        List<AttributeValue> returnList = this.service.getSpecificationInformation(productId, upc);
        return returnList;
    }

    /**
     * Find all the list of product panel type for eCommerce View screen.
     *
     * @param request - Get eCommerce View Information
     * @return List<ProductPanelType> - The list of product panel type
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_ALL_PRODUCT_PANEL_TYPE)
    public List<ProductPanelType> findAllProductPanelType(HttpServletRequest request){
        //Show log for method
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ALL_PRODUCT_PANEL_TYPE_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        //call service get all information
        return this.service.findAllProductPanelTypes();
    }

    /**
     * Find all the list of pdp template for eCommerce View screen.
     *
     * @param request - Get eCommerce View Information
     * @return List<PDPTemplate> - The list of pdp template
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_ALL_PDP_TEMPLATE)
    public List<PDPTemplate> findAllPDPTemplate(HttpServletRequest request){
        //Show log for method
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ALL_PDP_TEMPLATE_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        //call service get all information
        return this.service.findAllPDPTemplate();
    }

    /**
     * Find all the list of sale channel for eCommerce View screen.
     *
     * @param request - Get eCommerce View Information
     * @return List<SalesChannel> - The list of sale channel
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_ALL_SALE_CHANNEL)
    public List<SalesChannel> findAllSaleChannel(HttpServletRequest request){
        //Show log for method
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ALL_SALE_CHANNEL_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        //call service get all information
        return this.service.findAllSaleChannels();
    }

    /**
     * Get information for eCommerce View screen. Load all basic information.
     * @param productId - The product id
     * @param upc - The scan code id
     * @param request - Get eCommerce View Information
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_ECOMMERCE_VIEW_INFORMATION)
    public ECommerceViewDetails getECommerceViewInformation(@RequestParam(value = "productId") long productId,
                                                            @RequestParam(value = "upc") long upc,
                                                            HttpServletRequest request) throws Exception{
        //Show log for method
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_ECOMMERCE_VIEW_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId, upc));
        //call service get all information
        return this.service.getECommerceViewInformation(productId, upc);
    }

    /**
     * Get information for eCommerce View screen. Load all more information by tab(sale channel).
     * @param productId - The product id
     * @param upc - The primary scan code
     * @param commodity - The commodity code.
     * @param request - Get eCommerce View Information
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_MORE_ECOMMERCE_VIEW_INFORMATION_BY_TAB)
    public ECommerceViewDetails getMoreECommerceViewInformationByTab(@RequestParam(value = "productId") long productId,
                                                                     @RequestParam(value = "upc") long upc,
                                                                     @RequestParam(value = "commodity") String commodity,
                                                                     @RequestParam(value = "saleChannel") String saleChannel,
                                                                     @RequestParam(value = "hierCntxtCd") String hierCntxtCd,
                                                                     HttpServletRequest request) throws Exception{
        //Show log for method
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController
                .GET_MORE_ECOMMERCE_VIEW_INFORMATION_BY_TAB_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId, upc, commodity));
        //call service get all information
        return this.service.getMoreECommerceViewInformationByTab(productId, upc, commodity, saleChannel, hierCntxtCd);
    }

    /**
     * Get brand information.
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param saleChannel - The sale channel.
     * @param request - Get eCommerce View Information
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_BRAND_INFORMATION)
    public ECommerceViewDetails getBrandInformation(@RequestParam(value = "productId") long productId,
    																 @RequestParam(value = "upc") long upc,
    																 @RequestParam(value = "saleChannel") String saleChannel,
    																 HttpServletRequest request) throws Exception{
    	ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController
    			.GET_BRAND_INFORMATION_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
    	return this.service.getBrandInformation(productId, upc, saleChannel);
    }

    /**
     * Get display name information.
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param saleChannel - The sale channel.
     * @param request - Get eCommerce View Information
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_DISPLAY_NAME_INFORMATION)
    public ECommerceViewDetails getDisplayNameInformation(@RequestParam(value = "productId") long productId,
    		@RequestParam(value = "upc") long upc,
    		@RequestParam(value = "saleChannel") String saleChannel,
    		HttpServletRequest request) throws Exception{
    	ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController
    			.GET_DISPLAY_NAME_INFORMATION_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
    	return this.service.getDisplayNameInformation(productId, upc, saleChannel);
    }

    /**
     * Get size information.
     *
     * @param productId - The product id
     * @param upc       - The primary scan code
     * @param saleChannel - The sale channel.
     * @param request - Get eCommerce View Information
     * @return ECommerceViewDetails - Contain information eCommerce View
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_SIZE_INFORMATION)
    public ECommerceViewDetails getSizeInformation(@RequestParam(value = "productId") long productId,
    		@RequestParam(value = "upc") long upc,
    		@RequestParam(value = "saleChannel") String saleChannel,
    		HttpServletRequest request) throws Exception{
    	ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController
    			.GET_SIZE_INFORMATION_LOG_MESSAGE, this.userInfo.getUserId(), request.getRemoteAddr()));
    	return this.service.getSizeInformation(productId, upc, saleChannel);
    }

    /**
     * Request to get the byte[] representation of the image
     * @param request the request
     * @return the byte[]
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_ECOMMERCE_VIEW_IMAGE_BY_URI)
    public ModifiedEntity<byte[]> findECommerceViewImageByUri(@RequestParam(value = "imageUri", required = false) String imageUri,
                                                              HttpServletRequest request){
        //show log message when init method
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ECOMMERCE_VIEW_IMAGE_BY_URI_LOG_MESSAGE, this.userInfo.getUserId(),
                request.getRemoteAddr(), imageUri));
        byte[] image =this.service.findECommerceViewImageByUri(imageUri);
        return new ModifiedEntity<>(image, imageUri);
    }

    /**
     * Find the product relationship data by product id.
     *
     * @param productId the product id to find.
     * @param request   the http servlet request.
     * @return the list of product relationship.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_PRODUCT_RELATIONSHIPS)
    public List<ProductRelationship> findProductRelationshipByProductId(@RequestParam("productId") Long productId,
                                                                        HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_PRODUCT_RELATIONSHIPS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        List<ProductRelationship> results = this.service.getProductRelationshipByProductId(productId);
        this.productRelationshipResolver.fetch(results);
        return results;
    }

    /**
     * Find the warnings of eCommerceView by product id or upc.
     *
     * @param productId  the product id to find.
     * @param scanCodeId the upc to find.
     * @param request    the http servlet request.
     * @return the eCommerceViewAttributePriorities entity that contains the warnings.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_WARNINGS)
    public ECommerceViewAttributePriorities getWarningsByProductIdOrUpc(@RequestParam("productId") long productId,
                                                                        @RequestParam("scanCodeId") long scanCodeId,
                                                                        HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_WARNINGS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        return this.service.getWarningsByProductIdOrUpc(productId, scanCodeId);
    }

    /**
     * Find product description by product id and upc.
     *
     * @param productId the id of product.
     * @param upc the upc.
     * @param request The HTTP request that initiated this call.
     * @return the ECommerceViewAttributePriorities object.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_PRODUCT_DESCRIPTION)
    public ECommerceViewAttributePriorities findProductDescriptionByProductIdAndUpc(@RequestParam(value = "productId") long productId,
                                                                                    @RequestParam(value = "upc") long upc,
                                                                                    HttpServletRequest request){
        //Log message.
        ProductECommerceViewController.logger.info(ProductECommerceViewController.FIND_PRODUCT_DESCRIPTION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId, upc);
        return this.service.findProductDescriptionByProductIdAndUpcAndLogAttrId(productId, upc);
    }

    /**
     * Find tags by product id and upc.
     *
     * @param productId the id of product.
     * @param upc the upc.
     * @param request The HTTP request that initiated this call.
     * @return the ECommerceViewAttributePriorities object.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_TAGS)
    public ECommerceViewAttributePriorities findTagsByProductIdAndUpc(@RequestParam(value = "productId") long productId,
                                                                      @RequestParam(value = "upc") long upc,
                                                                      HttpServletRequest request){
        //Log message.
        ProductECommerceViewController.logger.info(ProductECommerceViewController.FIND_TAGS_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId, upc);
        return this.service.findTagsByProductIdAndUpc(productId, upc);
    }

    /**
     * Find the directions of eCommerceView by product id or upc.
     *
     * @param productId  the product id to find.
     * @param scanCodeId the upc to find.
     * @param request    the http servlet request.
     * @return the eCommerceViewAttributePriorities entity that contains the directions.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_DIRECTIONS)
    public ECommerceViewAttributePriorities getDirectionsByProductIdOrUpc(@RequestParam("productId") long productId,
                                                                          @RequestParam("scanCodeId") long scanCodeId,
                                                                          HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_DIRECTIONS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        return this.service.getDirectionsByProductIdOrUpc(productId, scanCodeId);
    }

    /**
     * Find the ingredients of eCommerceView by product id or upc.
     *
     * @param productId  the product id to find.
     * @param scanCodeId the upc to find.
     * @param request    the http servlet request.
     * @return the eCommerceViewAttributePriorities entity that contains the ingredients.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_INGREDIENTS)
    public ECommerceViewAttributePriorities getIngredientsByProductIdOrUpc(@RequestParam("productId") long productId,
                                                                           @RequestParam("scanCodeId") long scanCodeId,
                                                                           HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_INGREDIENTS_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        return this.service.getIngredientsByProductIdOrUpc(productId, scanCodeId);
    }

    /**
     * Get information for eCommerce View screen. Load all basic information.
     * @param request - Get eCommerce View Information
     * @return CustomerHierarchyAssigment - Contain information Customer Hierarchy Assigment
     */
    /**
     * Get information for eCommerce View screen. Load all basic information.
     * @param productId - The product to get Customer Hierarchy Assigment
     * @param upc - The upc
     * @param subCommodity - The sub Commodity code
     * @param hierachyContextCode - The hierachy Context Code
     * @param entyId - The enty id
     * @param commodityCode  - The commodity Code
     * @param classCode - The class Code
     * @param request  - Get eCommerce View Information
     * @return information Customer Hierarchy Assigment
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_CUSTOM_HIERACHY_ASSGIMENT)
    public CustomerHierarchyAssigment getCustomHierarchyAssigment(@RequestParam("productId") long productId,
                                                                  @RequestParam("upc") long upc,
                                                                  @RequestParam("subCommodity") int subCommodity,
                                                                  @RequestParam("hierachyContextCode") String hierachyContextCode,
                                                                  @RequestParam("entyId") long entyId,
                                                                  @RequestParam("commodityCode") int commodityCode,
                                                                  @RequestParam("classCode") int classCode,
                                                                  HttpServletRequest request) throws Exception{
        ProductECommerceViewController.logger.info(String.format(
                ProductECommerceViewController.GET_CUSTOM_HIERARCHY_ASSIGMENT_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        CustomerHierarchyAssigment customerHierarchyAssigment = new CustomerHierarchyAssigment();
        HierarchyContext hierarchyContexts = this.service.findById(hierachyContextCode);
        customerHierarchyAssigment.setCustomerHierarchyContext(hierarchyContexts);
        customerHierarchyAssigment.setEntyId(entyId);
        customerHierarchyAssigment.setProductId(productId);
        customerHierarchyAssigment.setClassCode(classCode);
        customerHierarchyAssigment.setCommodityCode(commodityCode);
        customerHierarchyAssigment.setSubCommodity(subCommodity);
        customerHierarchyAssigment.setHierachyContextCode(hierachyContextCode);
        customerHierarchyAssigment.setUpc(upc);
        /*
        *   call service to get suggestion, lowest level and current hierarchy
         */
        return this.service.getCustomHierarchyAssigment(customerHierarchyAssigment);
    }

    /**
     * Get information for eCommerce View screen. Load all basic information.
     * @param request - Get eCommerce View Information
     * @return CustomerHierarchyAssigment - Contain information Customer Hierarchy Assigment
     */

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_CUSTOM_HIERACHY)
    public CustomerHierarchyAssigment getCustomHierarchy(@RequestParam(value = "hierachyContext") String hierachyContext,
                                                         HttpServletRequest request) throws Exception{
    	ProductECommerceViewController.logger.info(String.format(
                ProductECommerceViewController.GET_CUSTOM_HIERARCHY_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        CustomerHierarchyAssigment customerHierarchyAssigment = new CustomerHierarchyAssigment();
        HierarchyContext hierarchyContexts = this.service.findById(hierachyContext);
        customerHierarchyAssigment.setCustomerHierarchyContext(hierarchyContexts);
        return customerHierarchyAssigment;
    }

    /**
     * Returns all the ingredient attribute priorities to edit source.
     *
     * @param productId the product id to find.
     * @param scanCodeId the upc to find.
     * @param request the http servlet request.
     * @return all the ingredient attribute priorities.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_ALL_INGREDIENT_ATTRIBUTE)
    public List<ECommerceViewAttributePriorities> findAllIngredientAttributePriorities(@RequestParam("productId") long productId,
                                                                                       @RequestParam("scanCodeId") long scanCodeId,
                                                                                       HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ALL_INGREDIENT_ATTRIBUTE_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        return this.service.findAllIngredientAttributePriorities(productId, scanCodeId);
    }

    /**
     *  Call the service to save eCommerce data.
     *
     * @param currentHierarchys the ECommerceViewAttributePriorities object.
     * @param request  The HTTP request that initiated this call.
     * @return Success message.
     * @throws Exception
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductECommerceViewController.SAVE_CUSTOM_HIERACHY_ASSGIMENT)
    public ModifiedEntity saveCustomHierarchyAssigment(@RequestBody List<GenericEntityRelationship> currentHierarchys, HttpServletRequest request) throws Exception{
    	ProductECommerceViewController.logger.info(String.format(
                ProductECommerceViewController.SAVE_CUSTOM_HIERARCHY_ASSIGMENT_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
    	this.codeTableManagementServiceClient.updateCustomerHierarchyAssignment(currentHierarchys,currentHierarchys.get(0).getProductId(),userInfo.getUserId());
        // this.service.save(product);
        return new ModifiedEntity<>(null, UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Check fulfillment.
     *
     * @param productId the product id.
     * @param request The HTTP request that initiated this call.
     * @return true if the fulfillment is valid or not.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.CHECK_FULFILLMENT)
    public ModifiedEntity checkFulfillmentByProductId(@RequestParam("productId") long productId, HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.CHECK_FULFILLMENT_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        boolean isValidFulfillmentByProductId =  this.service.isValidFulfillmentByProductId(productId);
        return new ModifiedEntity<>(isValidFulfillmentByProductId, null);
    }

    /**
     * Call webservice to update ingredient product attribute override.
     *
     * @param eCommerceViewDetails the ECommerceViewDetails entity to be updated.
     * @param request the http servlet request.
     * @return the modified entity.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductECommerceViewController.URL_UPDATE_INGREDIENT_ATTRIBUTE_PRIORITIES)
    public ModifiedEntity updateIngredientAttributePriorities(@RequestBody ECommerceViewDetails eCommerceViewDetails,
                                                              HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(
                ProductECommerceViewController.UPDATE_INGREDIENT_ATTRIBUTE_PRIORITIES_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));

        this.service.updateIngredientAttributePriorities(eCommerceViewDetails, this.userInfo.getUserId());
        return new ModifiedEntity<>(null, ProductECommerceViewController.UPDATE_SUCCESS_MESSAGE);
    }
    /**
     * Find the HebGuaranteeTypeCode By ProductId.
     *
     * @param productId the product id.
     * @param request the http servlet request.
     * @return the HebGuaranteeTypeCode object or null.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_HEB_GUARANTEE_TYPE_CODE)
    public HebGuaranteeTypeCode findHebGuaranteeTypeCodeByProductId(@RequestParam("productId") long productId, HttpServletRequest request) {
        ProductECommerceViewController.logger.info(ProductECommerceViewController.FIND_HEB_GUARANTEE_TYPE_CODE_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId);
        return  this.service.findHebGuaranteeTypeCodeByProductId(productId);
    }


    /**
     * Find information eCommerce View data source. Contain product attribute overwrite, attribute priority, content
     * text. Handle classify source(source default, source editable...ect).
     * @param productId - The product id
     * @param attributeId - The attribute id
     * @param scanCodeId - The scan code id
     * @param salesChannel - The sales channel code
     * @param request - The HTTP request that initiated this call.
     * @return ECommerceViewAttributePriority object
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_ECOMMERCE_VIEW_DATA_SOURCE)
    public ECommerceViewAttributePriority findECommerceViewDataSourceByAttributeId(@RequestParam("productId") long productId,
                                                                                   @RequestParam("attributeId") long attributeId,
                                                                                   @RequestParam("scanCodeId") long scanCodeId,
                                                                                   @RequestParam("salesChannel") String salesChannel,
                                                                                   HttpServletRequest request) throws Exception{

        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_ECOMMERCE_VIEW_DATA_SOURCE_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.service.findECommerceViewDataSourceByAttributeId(productId, attributeId, scanCodeId, salesChannel);
    }

    /**
     *  Update eCommerce View data source. Will by update content and set product attribute overwrite.
     *
     * @param eCommerceViewAttributePriority the eCommerceViewAttributePriority object.
     * @param request  The HTTP request that initiated this call.
     * @return {{message}}
     * @throws Exception
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductECommerceViewController.UPDATE_ECOMMERCE_VIEW_DATA_SOURCE)
    public ModifiedEntity updateECommerceViewDataSource(@RequestBody ECommerceViewAttributePriority eCommerceViewAttributePriority,
                                                        HttpServletRequest request) throws Exception{
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.UPDATE_ECOMMERCE_VIEW_DATA_SOURCE_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        this.service.updateECommerceViewDataSource(eCommerceViewAttributePriority, userInfo.getUserId());
        return new ModifiedEntity<>(null, UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Returns all the tags attribute priorities to edit source.
     *
     * @param productId the product id to find.
     * @param scanCodeId the upc to find.
     * @param request the http servlet request.
     * @return the ECommerceViewAttributePriority object.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_ALL_TAG_ATTRIBUTE)
    public ECommerceViewAttributePriority findAllTagsAttributePriorities(@RequestParam("productId") long productId,
                                                                                 @RequestParam("scanCodeId") long scanCodeId,
                                                                                 HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ALL_TAG_ATTRIBUTE_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.service.findAllTagsAttributePriorities(productId, scanCodeId);
    }
    /**
     * Returns all the specification attribute priorities to edit source.
     *
     * @param productId the product id to find.
     * @param scanCodeId the upc to find.
     * @param request the http servlet request.
     * @return the ECommerceViewAttributePriority object.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.URL_FIND_ALL_SPECIFICATION_ATTRIBUTE)
    public ECommerceViewAttributePriority findAllSpecificationAttributePriorities(@RequestParam("productId") long productId,
                                                                         @RequestParam("scanCodeId") long scanCodeId,
                                                                         HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ALL_SPECIFICATION_ATTRIBUTE_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.service.findAllSpecificationAttributePriorities(productId, scanCodeId);
    }

    /**
     *  Update information for eCommerce View.
     *
     * @param eCommerceViewDetails the ECommerceViewDetails object.
     * @param request  The HTTP request that initiated this call.
     * @return Success message.
     * @throws Exception
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = ProductECommerceViewController.UPDATE_ECOMMERCE_VIEW_INFORMATION)
    public ModifiedEntity updateECommerceViewInformation(@RequestBody ECommerceViewDetails eCommerceViewDetails,
                                                         HttpServletRequest request) throws Exception{
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.UPDATE_ECOMMERCE_VIEW_INFORMATION_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        this.service.updateECommerceViewInformation(eCommerceViewDetails, userInfo.getUserId());
        return new ModifiedEntity<>(null, UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Find the list of Target System Attribute Priority for show name attribute.
     * @param attributeId - The attribute id
     * @param request - The HTTP request that initiated this call.
     * @return ECommerceViewAttributePriority object
     * @throws Exception
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_ATTRIBUTE_MAPPING_BY_LOGICAL_ATTRIBUTE)
    public List<TargetSystemAttributePriority> findAttributeMappingByLogicalAttribute(@RequestParam("attributeId") int attributeId,
                                                                                      HttpServletRequest request) throws Exception{
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.FIND_ATTRIBUTE_MAPPING_BY_LOGICAL_ATTRIBUTE_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr(), attributeId));
        return this.service.findAttributeMappingByLogicalAttribute(attributeId);
    }

    /**
     * Get the list of eCommerce view audit by primaryUPC.
     * @param primaryUPC - The primaryUPC
     * @param request - The HTTP request that initiated this call.
     * @return List<AuditRecord> the list of eCommerce view audit.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_ECOMMERCE_VIEW_AUDIT)
    public List<AuditRecord> getECommerceViewAuditsInformation(@RequestParam(value = "primaryUPC") long primaryUPC, HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_ECOMMERCE_VIEW_AUDIT_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.auditService.getECommerceViewAuditsInformation(primaryUPC);
    }
    /**
     * Retrieves published Attributes audit information.
     * @param prodId The Product ID that the audit is being searched on.
     * @param request The HTTP request that initiated this call.
     * @return The list of published Attributes audits attached to given product ID.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_PUBLISHED_ATTRIBUTES_AUDITS)
    public List<AuditRecord> getPublishedAttributesAuditInfo(@RequestParam(value="prodId") Long prodId, @RequestParam(value="upc") Long upc, HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_PUBLISHED_ATTRIBUTES_AUDITS_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.service.getPublishedAttributesAuditInformation(prodId, upc);
    }
    /**
     * Retrieves published Attributes audit information.
     * @param prodId The Product ID that the audit is being searched on.
     * @param request The HTTP request that initiated this call.
     * @return The list of published Attributes audits attached to given product ID.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.GET_FULFILLMENT_ATTRIBUTES_AUDITS)
    public List<AuditRecord> getFulfillmentAttributesAuditInfo(@RequestParam(value="prodId") Long prodId, HttpServletRequest request) {
        ProductECommerceViewController.logger.info(String.format(ProductECommerceViewController.GET_FULFILLMENT_ATTRIBUTES_AUDITS_LOG_MESSAGE,
                this.userInfo.getUserId(), request.getRemoteAddr()));
        return this.service.getFulfillmentAttributesAuditInformation(prodId);
    }
    /**
     * Find favor item description by product id and upc.
     *
     * @param productId the id of product.
     * @param upc the upc.
     * @param request The HTTP request that initiated this call.
     * @return the ECommerceViewAttributePriorities object.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = ProductECommerceViewController.FIND_FAVOR_ITEM_DESCRIPTION)
    public ECommerceViewAttributePriorities findFavorItemDescriptionByProductIdAndUpc(@RequestParam(value = "productId") long productId,
                                                                                    @RequestParam(value = "upc") long upc,
                                                                                    HttpServletRequest request){
        //Log message.
        ProductECommerceViewController.logger.info(ProductECommerceViewController.FIND_FAVOR_ITEM_DESCRIPTION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr(), productId, upc);
        return this.service.findFavorItemDescriptionByProductId(productId, upc);
    }
    /**
     * Find alert staging by product id.
     *
     * @param productId the product id.
     * @return list of alert staging.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "findAlertStagingByProductId")
    public List<AlertStaging> findAlertStagingByProductId(@RequestParam(value = "productId") Long productId) {
        return this.service.findAlertStagingByProductId(productId);
    }
}