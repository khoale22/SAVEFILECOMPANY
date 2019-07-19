/*
 * productHierarchyApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product hierarchy.
 *
 * @author m314029
 * @since 2.4.0
 */
(function(){
	angular.module('productMaintenanceUiApp').factory('ProductHierarchyApi', productHierarchyApi);

	productHierarchyApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function productHierarchyApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/productHierarchy';
		return $resource(
			urlBase ,null,
			{
				'getFullHierarchy': {
					method: 'GET',
					url: urlBase + '/getFullHierarchy',
					isArray:true
				},
				'findAllSellingRestrictions': {
					method: 'GET',
					url: urlBase + '/sellingRestriction/findAll',
					isArray:true
				},
				'getSellingRestrictionHierarchyLevelTemplate': {
					method: 'GET',
					url: urlBase + '/sellingRestrictionHierarchyLevel/getTemplate',
					isArray:false
				},
				'updateSellingRestrictionHierarchyLevel': {
					method: 'POST',
					url: urlBase + '/sellingRestrictionHierarchyLevel/update',
					isArray:false
				},
				'findAllShippingRestrictions': {
					method: 'GET',
					url: urlBase + '/shippingRestriction/findAll',
					isArray:true
				},
				'getShippingRestrictionHierarchyLevelTemplate': {
					method: 'GET',
					url: urlBase + '/shippingRestrictionHierarchyLevel/getTemplate',
					isArray:false
				},
				'updateShippingRestrictionHierarchyLevel': {
					method: 'POST',
					url: urlBase + '/shippingRestrictionHierarchyLevel/update',
					isArray:false
				},
				'getViewableShippingRestrictions': {
					method: 'GET',
					url: urlBase + '/shippingRestrictionHierarchyLevel/getViewable',
					isArray:true
				},
				'getViewableSellingRestrictions': {
					method: 'GET',
					url: urlBase + '/sellingRestrictionHierarchyLevel/getViewable',
					isArray:true
				},
				'getProductHierarchyBySearch': {
					method: 'GET',
					url: urlBase + '/getProductHierarchyBySearch',
					isArray:true
				},
				'updateItemClass': {
					method: 'POST',
					url: urlBase + '/itemClass/update',
					isArray:false
				},
				'getAllMerchantTypes': {
					method: 'GET',
					url: urlBase + '/merchantType/findAll',
					isArray:true
				},
				'getEmptyItemClassForUpdate': {
					method: 'GET',
					url: urlBase + '/itemClass/getEmpty',
					isArray:false
				},
				'getEmptyCommodityForUpdate': {
					method: 'GET',
					url: urlBase + '/commodity/getEmpty',
					isArray:false
				},
				'updateCommodity': {
					method: 'POST',
					url: urlBase + '/commodity/update',
					isArray:false
				},
				'getEmptySubCommodityForUpdate': {
					method: 'GET',
					url: urlBase + '/subCommodity/getEmpty',
					isArray:false
				},
				'updateSubCommodity': {
					method: 'POST',
					url: urlBase + '/subCommodity/update',
					isArray:false
				},
				'updateSubCommodityStateWarnings': {
					method: 'POST',
					url: urlBase + '/subCommodity/stateWarning/update',
					isArray:true
				},
				'getEmptySubCommodityStateWarning': {
					method: 'GET',
					url: urlBase + '/subCommodity/stateWarning/getEmpty',
					isArray:false
				},
				'updateSubCommodityPreferredUnitsOfMeasure': {
					method: 'POST',
					url: urlBase + '/subCommodity/preferredUnitOfMeasure/update',
					isArray:true
				},
				'getEmptySubCommodityPreferredUnitOfMeasure': {
					method: 'GET',
					url: urlBase + '/subCommodity/preferredUnitOfMeasure/getEmpty',
					isArray:false
				},
				'getAllVertexTaxCategories': {
					method: 'GET',
					url: urlBase + '/subCommodity/getAllVertexTaxCategories',
					isArray: true
				},
				'getCurrentItemClassInfo': {
					method: 'GET',
					url: urlBase + '/itemClass/getCurrentItemClassInfo',
					isArray:false
				},
				'getCurrentCommodityClassInfo': {
					method: 'POST',
					url: urlBase + '/commodity/getCurrentCommodityClassInfo',
					isArray:false
				},
				'getCurrentSubCommodityClassInfo': {
					method: 'POST',
					url: urlBase + '/subCommodity/getCurrentSubCommodityClassInfo',
					isArray:false
				},
				'getSubCommodityDefaultsAudits': {
					method: 'GET',
					url: urlBase + '/subCommodity/getSubCommodityDefaultsAudits',
					isArray:true
				}
			}
		);
	}
})();
