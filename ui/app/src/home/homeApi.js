/*
 *
 * productDiscontinueApi.js
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
 * Constructs the API to call the backend for home controller.
 * */
(function () {

	angular.module('productMaintenanceUiApp').factory('HomeApi', homeApi);

	homeApi.$inject = ['urlBase', '$resource'];

	function homeApi(urlBase, $resource){
		return $resource(null, null, {
			'search': {
				method: 'POST',
				url: urlBase + '/pm/newSearch',
				isArray: false
			},
			'hits': {
				method: 'GET',
				url: urlBase + '/pm/newSearch/hits',
				isArray: false
			},
			'queryVertexTaxCategories': {
				method: 'GET',
				url:urlBase + '/pm/vertex',
				isArray: true
			},
			'massUpdate': {
				method: 'POST',
				url: urlBase + "/pm/massUpdate",
				isArray: false
			},
			'queryTagTypes': {
				method: 'GET',
				url: urlBase + "/pm/tagType",
				isArray: true
			},
			'getAllSalesChannels': {
				method: 'GET',
				url: urlBase + "/pm/getAllSalesChannels",
				isArray: true
			},
			'getAllPDPTemplates': {
				method: 'GET',
				url: urlBase + "/pm/getAllPDPTemplates",
				isArray: true
			},
			'getAllFulfillmentPrograms': {
				method: 'GET',
				url: urlBase + "/pm/getAllFulfillmentPrograms",
				isArray: true
			}

		});
	}

})();


// This is here to make it easier to find backend code to remove later.
// ,
// // Get the product report by a list of  ProductIds.
// 'queryByProductIds': {
// 	method: 'GET',
// 		url: urlBase + '/pm/product/productIds',
// 		isArray: false
// },
// 'queryForMissingProductIds': {
// 	method: 'GET',
// 		url: urlBase + '/pm/product/hits/productIds',
// 		isArray:false
// },
// 'queryByUPCs':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/upcs',
// 		isArray:false
// },
// 'queryByItemCodes':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/itemCodes',
// 		isArray:false
// },
// 'queryBySubDepartment':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/subDepartment',
// 		isArray:false
// },
// 'queryByClassAndCommodity':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/classAndCommodity',
// 		isArray:false
// },
// 'queryBySubCommodity':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/subCommodity',
// 		isArray:false
// },
// 'queryByBDM':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/bdm',
// 		isArray:false
// },
// 'queryByMRTItemCode':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/mrtItemCode',
// 		isArray:false
// },
// 'queryByMRTCasePack':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/mrtCasePack',
// 		isArray:false
// },
// 'queryByVendor':{
// 	method: 'GET',
// 		url: urlBase + '/pm/product/vendor',
// 		isArray:false
// },
// 'queryByProductDescription':{
// 	method: 'GET',
// 		url:urlBase + '/pm/product/productDescription',
// 		isArray: false
// },
