/*
 *
 * productApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Constructs the API to call the backend for the product section in product details.
 * */

(function () {

	angular.module('productMaintenanceUiApp').factory('ProductInfoApi', productInfoApi);

	productInfoApi.$inject = ['urlBase', '$resource'];

	function productInfoApi(urlBase, $resource) {
		return $resource(null, null, {
			// Queries all Import Items by item code.
			'updateShelfAttributes': {
				method: 'POST',
				url: urlBase + '/pm/shelfAttributes/updateShelfAttributes',
				isArray:false
			},
			'updateServiceCaseTagData':{
				method: 'POST',
				url: urlBase + '/pm/shelfAttributes/updateServiceCaseTagData',
				isArray: false
			},
			'getListOfDescriptionTypes': {
				method: 'GET',
				url: urlBase + '/pm/shelfAttributes/getListOfDescriptionTypes',
				isArray: true
			},
			'getServiceCaseTagData': {
				method: 'POST',
				url: urlBase + '/pm/shelfAttributes/getServiceCaseTagData',
				isArray: false
			},
			'getVertexTaxCategoryByTaxCode': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getVertexTaxCategoryByTaxCode',
				isArray: false
			},
			'getVertexTaxCategoryByTaxQualifyingCode': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getVertexTaxCategoryByTaxQualifyingCode',
				isArray: false
			},
			'getPriceDetail': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getPriceDetail',
				isArray: false
			},
			'getProductAssortment': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getProductAssortment',
				isArray: false
			},
			'getDepositRelatedProduct': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getDepositRelatedProduct',
				isArray: false
			},
			'getEffectiveMapPrices': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getEffectiveMapPrices',
				isArray: true
			},
			'updateProductInformation': {
				method: 'POST',
				url: urlBase + '/pm/productInformation/update',
				isArray: false
			},
			'getProductDescriptionTemplateWithProdId': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getProductDescriptionTemplateWithProdId',
				isArray: false
			},
			'getAllTaxQualifyingConditions': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getAllTaxQualifyingConditions',
				isArray: true
			},
			'getAllVertexTaxCategoryEffectiveMaintenanceForProduct': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getAllVertexTaxCategoryEffectiveMaintenanceForProduct',
				isArray: true
			},
			'getAllProductRetailLinksByRetailLinkCode': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getAllProductRetailLinksByRetailLinkCode',
				isArray: true
			},
			'getAllVertexTaxCategoryCodesBySubCommodityCode': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getAllVertexTaxCategoryCodesBySubCommodityCode',
				isArray: true
			},
			'getProductsByVertexTaxCategoryCode': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getProductsByVertexTaxCategoryCode',
				isArray: false
			},
			'getAllVertexTaxCategories': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getAllVertexTaxCategories',
				isArray: true
			},
			'getSubCommodityDefaults': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getSubCommodityDefaults',
				isArray: false
			},
			'updateVertexTaxCategoryEffectiveMaintenance': {
				method: 'POST',
				url: urlBase + '/pm/productInformation/updateVertexTaxCategoryEffectiveMaintenance',
				isArray: true
			},
			'getProductInfoAudits': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getProductInfoAudits',
				isArray: true
			},
			'getShelfAttributesAudits': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getShelfAttributesAudits',
				isArray: true
			},
			'queryTagTypes': {
				method: 'GET',
				url: urlBase + "/pm/tagType",
				isArray: true
			},
			'getTaxAndNonTaxCategoryBySubCommodity': {
				method: 'GET',
				url: urlBase + '/pm/productInformation/getTaxAndNonTaxCategoryBySubCommodity',
				isArray: false
			},
			'findAllSubCommoditiesByPageRequest': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/subCommodity/findAllByPageRequest',
				isArray: true
			},
			'findSubCommoditiesBySearchText': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/subCommodity/findBySearchText',
				isArray: false
			}
		});
	}
})();
