/*
 *
 * productUnitsApi.js
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Constructs the API to call the backend for productApi .
 *
 * @author m594201
 * @since 2.8.0
 */

(function () {

	angular.module('productMaintenanceUiApp').factory('productApi', productApi);

	productApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related product.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function productApi(urlBase, $resource) {
		return $resource(null, null, {
			'getKitsData': {
				method: 'GET',
				url: urlBase + '/pm/product/getKitsData',
				isArray:true
			},
			'getKitsDataByElements': {
				method: 'POST',
				url: urlBase + '/pm/product/getKitsDataByElements',
				isArray:true
			},
			'getDrugSchedule': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getDrugSchedule',
				isArray:true
			},
			'getRestrictions': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/ratingRestrictions',
				isArray:true
			},
			'getRestrictionGroups': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/ratingRestrictionGroup',
				isArray:true
			},
			'getAllShippingRestrictions': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getAllShippingRestrictions',
				isArray:true
			},
			'getShippingMethodExceptions': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getShippingMethodRestrictions',
				isArray:true
			},
			'getAllShippingMethodExceptions': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getAllShippingMethodExceptions',
				isArray:true
			},
			'getAllStateWarnings': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getAllStateWarnings',
				isArray:true
			},
			'getStateWarnings' : {
				url: urlBase + '/pm/specialAttributes//getStateWarnings',
				isArray: true
			},
			'getProductShippingRestrictions': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getProductShippingRestrictions',
				isArray:true
			},
			'getSalesRestrictionsBySubCommodity': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getSalesRestrictionsBySubCommodity',
				isArray:true
			},
			'getStateWarningBySubcommodity': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getStateWarningBySubcommodity',
				isArray:true
			},
			'savePharmacyChanges': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/savePharmacyChanges',
				isArray: false
			},
			'saveCodeDateChanges': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/saveCodeDateChanges',
				isArray: false
			},
			'getTobaccoType': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getTobaccoTypes',
				isArray: true
			},
			'getTobaccoProduct': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getTobaccoProduct',
				isArray: false
			},
			'getKitsProduct': {
				method: 'GET',
				url: urlBase + '/pm/product/getKitsProductMaster',
				isArray: false
			},
			'saveTierPricingChanges': {
				method: 'POST',
				url: urlBase + '/pm/onlineAttributes/saveTierPricingChanges',
				isArray: false
			},
			'saveTobaccoChanges': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/saveTobaccoProduct',
				isArray: false
			},
			'saveRatingRestrictionsChanges': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/updateRatingRestrictions',
				isArray: false
			},
			'getTagsAndSpecsAttribute': {
				method: 'GET',
				url: urlBase + '/pm/onlineAttributes/getTagsAndSpecs',
				isArray: true
			},
			'updateTagsAndSpecsAttribute': {
				method: 'POST',
				url: urlBase + '/pm/onlineAttributes/updateTagsAndSpecs',
				isArray: false
			},
			'getGuaranteeImages': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getGuaranteeImages',
				isArray: true
			},
			'getProductTrashCan': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getProductTrashCan',
				isArray: false
			},
			'getSoldByOptions': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getSoldByOptions',
				isArray: true
			},
			'getRelatedProducts': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getRelatedProducts',
				isArray: true
			},

			'getTierPricing': {
			method: 'GET',
				url: urlBase + '/pm/onlineAttributes/getTierPricing',
				isArray: true
		},
			'updateShippingHandlingChanges': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/updateShippingHandlingChanges',
				isArray: false
			},
			'updateShippingRestrictionList': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/updateShippingRestrictions',
				isArray:false
			},
			'updateShippingMethods': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/updateShippingMethods',
				isArray:false
			},
			'updateProductStateWarnings': {
				method: 'POST',
				url: urlBase + '/pm/specialAttributes/updateProductStateWarnings',
				isArray:false
			},
			'saveRelatedProducts': {
				method: 'POST',
				url: urlBase + '/pm/onlineInfo/saveRelatedProducts',
				isArray: true
			},
			'getChoice': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/choice/getChoice',
				isArray: true
			},
			'getProductGroups': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getProductGroup',
				isArray: true
			},
			'updateThirdPartySellable': {
				method: 'POST',
				url: urlBase + '/pm/onlineInfo/updateThirdPartySellable',
				isArray: false
			},
			'updateProductTrashCan': {
				method: 'POST',
				url: urlBase + '/pm/onlineInfo/updateProductTrashCan',
				isArray: false
			},
			'updateOnlineAttribute': {
				method: 'POST',
				url: urlBase + '/pm/onlineInfo/updateOnlineAttributes',
				isArray: false
			},
			'getTagsAndSpecsAttributeOptions': {
				method: 'GET',
				url: urlBase + '/pm/onlineAttributes/getTagsAndSpecsAttributeOptions',
				isArray: true
			},
			'getTagsAndSpecsAttributeValues': {
				method: 'GET',
				url: urlBase + '/pm/onlineAttributes/getTagsAndSpecsValues',
				isArray: true
			},
			'getRelatedProductsAudits': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getRelatedProductsAudits',
				isArray: true
			},
			'getOnlineAttributesAudits': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getOnlineAttributesAudits',
				isArray: true
			},
			'getTagsAndSpecsAudits': {
				method: 'GET',
				url: urlBase + '/pm/onlineInfo/getTagsAndSpecsAudits',
				isArray: true
			},
			'getPharmacyAudits': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getPharmacyAudits',
				isArray: true
			},
			'getTobaccoAudits': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getTobaccoAudits',
				isArray: true
			},
			'getCodeDateAudits': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getCodeDateAudits',
				isArray: true
			},
			'getShippingHandlingAudits': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getShippingHandlingAudits',
				isArray: true
			},
			'getRatingRestrictionsAudits': {
				method: 'GET',
				url: urlBase + '/pm/specialAttributes/getRatingRestrictionsAudits',
				isArray: true
			},
			'getTierPricingAudits': {
				method: 'GET',
				url: urlBase + '/pm/onlineAttributes/getTierPricingAudits',
				isArray: true
			},
		});
	}
})();
