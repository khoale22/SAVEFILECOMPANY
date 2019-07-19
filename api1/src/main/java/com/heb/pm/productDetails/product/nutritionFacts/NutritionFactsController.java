/*
 *  NutritionFactsController
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.productDetails.product.nutritionFacts;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.*;
import com.heb.pm.ws.CodeTableManagementServiceClient;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents product eCommerce View.
 *
 * @author vn73545
 * @since 2.15.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NutritionFactsController.PRODUCT_NUTRITION_FACTS)
@AuthorizedResource(ResourceConstants.PRODUCT_NUTRITION_FACTS)
public class NutritionFactsController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionFactsController.class);
    protected static final String PRODUCT_NUTRITION_FACTS = "/nutritionFacts";

    //urls
    private static final String GET_ALL_NUTRITION_FACTS_INFORMATION = "/getAll";
    private static final String APPROVE_NUTRITION_FACT_INFORMATION = "/approve";

    //logs
    private static final String GET_NUTRITION_FACT_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested get nutrition fact information";
    private static final String APPROVE_NUTRITION_FACT_INFORMATION_LOG_MESSAGE = "User %s from IP %s has requested approve nutrition fact information";
    private static final String UPDATE_SUCCESS_MESSAGE = "Successfully Updated.";

    @Autowired
    private NutritionFactsService service;

    @Autowired
    private UserInfo userInfo;

    private LazyObjectResolver<List<NutritionFact>> nutritionFactsLazyObjectResolver = new NutritionFactResolver();

    /**
     * Resolves a NutritionFact object. It will load the following properties:
     */
    private class NutritionFactResolver implements LazyObjectResolver<List<NutritionFact>> {
    	@Override
    	public void fetch(List<NutritionFact> nutritionFacts) {
    		if(nutritionFacts != null) {
    			nutritionFacts.forEach(eCommerceViewAttributePriority -> {
    				List<CandidateNutrient> candidateNutrients = eCommerceViewAttributePriority.getCandidateNutrients();
    				List<CandidateProductPkVariation> candidateProductPkVariations = eCommerceViewAttributePriority.getCandidateProductPkVariations();
    				if(candidateNutrients != null) {
    					candidateNutrients.forEach((candidateNutrient) -> {
    						candidateNutrient.getKey().getUpc();
    						if (candidateNutrient.getNutrientMaster() != null) {
    							candidateNutrient.getNutrientMaster().getMasterId();
    						}
    						if (candidateNutrient.getServingSizeUOM() != null) {
    							candidateNutrient.getServingSizeUOM().getServingSizeUomCode();
    						}
    					});
    				}
    				if(candidateProductPkVariations != null) {
    					candidateProductPkVariations.forEach((candidateProductPkVariation) -> {
    						candidateProductPkVariation.getKey().getUpc();
    					});
    				}

    				List<ProductNutrient> productNutrients = eCommerceViewAttributePriority.getProductNutrients();
    				List<ProductPkVariation> productPkVariations = eCommerceViewAttributePriority.getProductPkVariations();
    				if(productNutrients != null) {
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
    				if(productPkVariations != null) {
    					productPkVariations.forEach((productPkVariation) -> {
    						productPkVariation.getKey().getUpc();
    					});
    				}

    				ScaleUpc scaleUpc = eCommerceViewAttributePriority.getScaleUpc();
    				if(scaleUpc != null) {
    					scaleUpc.getUpc();
    					if(scaleUpc.getNutrientStatementDetails() != null) {
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
     * Get all nutrition facts information.
     *
     * @param productId - The product id.
     * @param primaryUpc - The primary scan code.
     * @param request The HTTP request that initiated this call.
     * @return The list of NutritionFact.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = NutritionFactsController.GET_ALL_NUTRITION_FACTS_INFORMATION)
    public List<NutritionFact> getAllNutritionFactsInformation(@RequestParam(value = "productId") long productId,
                                                 			   @RequestParam(value = "primaryUpc") long primaryUpc,
                                                 			   HttpServletRequest request) {
        NutritionFactsController.logger.info(String.format(NutritionFactsController.GET_NUTRITION_FACT_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        List<NutritionFact> returnList = this.service.getAllNutritionFactsInformation(productId, primaryUpc);
        this.nutritionFactsLazyObjectResolver.fetch(returnList);
        return returnList;
    }

    /**
     * Approve nutrition fact information.
     *
     * @param psWorkIds - The list of candidate work id.
     * @param request The HTTP request that initiated this call.
     * @return The success message.
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = NutritionFactsController.APPROVE_NUTRITION_FACT_INFORMATION)
    public ModifiedEntity<List<NutritionFact>> approveNutritionFactInformation(@RequestBody List<Long> psWorkIds,
                                                                               HttpServletRequest request) {
        NutritionFactsController.logger.info(String.format(NutritionFactsController.APPROVE_NUTRITION_FACT_INFORMATION_LOG_MESSAGE, this.userInfo
                .getUserId(), request.getRemoteAddr()));
        this.service.approveNutritionFactInformation(psWorkIds);
        return new ModifiedEntity<>(null, NutritionFactsController.UPDATE_SUCCESS_MESSAGE);
    }
}