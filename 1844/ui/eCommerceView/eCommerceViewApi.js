/*
 * eCommerceViewApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product > eCommerce View.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('ECommerceViewApi', eCommerceViewApi);

	eCommerceViewApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function eCommerceViewApi(urlBase, $resource) {
		return $resource(
			urlBase, null,
			{
				'getNutritionFactInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getNutritionFactInformation',
					isArray: true
				},
				'getAllNutritionFactsByAttributePriorities': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getAllNutritionFactsByAttributePriorities',
					isArray: true
				},
				'getDimensionInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getDimensionInformation',
					isArray: true
				},
				'getSpecificationInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getSpecificationInformation',
					isArray: true
				},
				'updateNutritionFactInformation': {
					method: 'POST',
					url: urlBase + '/pm/productECommerceView/updateNutritionFactInformation',
					isArray: false
				},
				'validateChangedPublishedSource': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/validateChangedPublishedSource',
					isArray: false
				},
				'findAllProductPanelType': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllProductPanelType',
					isArray: true
				},
				'findAllSaleChanel': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllSaleChanel',
					isArray: true
				},
				'findAllPDPTemplate': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllPDPTemplate',
					isArray: true
				},
				'getECommerceViewInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getECommerceViewInformation',
					isArray: false
				},
				'getMoreECommerceViewInformationByTab': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getMoreECommerceViewInformationByTab',
					isArray: false
				},
				'getBrandInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getBrandInformation',
					isArray: false
				},
				'getDisplayNameInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getDisplayNameInformation',
					isArray: false
				},
				'getSizeInformation': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getSizeInformation',
					isArray: false
				},
				'findProductRelationships': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findProductRelationships',
					isArray: true
				},
				'findWarnings': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findWarnings',
					isArray: false
				},
				'findProductDescription': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findProductDescription'
				},
				'findDirections': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findDirections',
					isArray: false
				},
				'findTags': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findTags'
				},
				'findIngredients': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findIngredients',
					isArray: false
				},
				'findAllIngredientAttributePriorities': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllIngredientAttributePriorities',
					isArray: true
				},
				'getCustomHierarchyAssigment': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getCustomHierarchyAssigment',
					isArray: false
				},
				'getCustomHierarchy': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getCustomHierarchy',
					isArray: false
				},
				'saveCustomHierarchyAssigment': {
					method: 'POST',
					url: urlBase + '/pm/productECommerceView/saveCustomHierarchyAssigment',
					isArray: false
				},
				'publishECommerceViewDataToHebCom': {
					method: 'POST',
					url: urlBase + '/pm/productPublish/publishECommerceViewDataToHebCom',
				},
				'findECommerceViewImageByUri':{
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findECommerceViewImageByUri',
					isArray: false
				},
				'checkFulfillment':{
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/checkFulfillment'
				},
				'updateIngredientAttributePriorities': {
					method: 'POST',
					url: urlBase + '/pm/productECommerceView/updateIngredientAttributePriorities',
					isArray: false
				},
				'findHebGuaranteeTypeCode': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findHebGuaranteeTypeCode',
					isArray: false
				},
				'getECommerceViewDataSource':{
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getECommerceViewDataSource',
					isArray: false
				},

				'updateECommerceViewDataSource':{
					method: 'POST',
					url :urlBase + '/pm/productECommerceView/updateECommerceViewDataSource',
					isArray: false
				},
				'getAllDimensionsByAttributePriorities': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getAllDimensionsByAttributePriorities',
					isArray: false
				},
				'findAllRomanceCopyAttributePriorities': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllRomanceCopyAttributePriorities',
					isArray: false
				},
				'findAllTagAttributePriorities': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllTagAttributePriorities',
					isArray: false
				},
				'findAllSpecificationAttributePriorities': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAllSpecificationAttributePriorities',
					isArray: false
				},
				'updateECommerceViewInformation':{
					method: 'POST',
					url :urlBase + '/pm/productECommerceView/updateECommerceViewInformation',
					isArray: false
				},
				'findAttributeMappingByLogicalAttribute':{
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAttributeMappingByLogicalAttribute',
					isArray: true
				},
				'getECommerceViewAudit':{
					method: 'GET',
					url :urlBase + '/pm/productECommerceView/eCommerceViewAudit',
					isArray: true
				},
				'getFulfillmentAttributesAudits': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/getFulfillmentAttributesAudits',
					isArray: true
				},
                'findFavorItemDescription': {
                    method: 'GET',
                    url: urlBase + '/pm/productECommerceView/findFavorItemDescription'
                },
				'findAlertStagingByProductId': {
					method: 'GET',
					url: urlBase + '/pm/productECommerceView/findAlertStagingByProductId',
					isArray: true
				}
			}
		);
	}
})();
