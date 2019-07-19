/*
 *
 * codeTableApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.12.0
 */
'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('codeTableApi', codeTableApi);
	codeTableApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to Code Table API.
	 *
	 * Supported method:
	 * codetableApi: Will return all varietal from database.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The API factory.
	 */
	function codeTableApi(urlBase, $resource) {
		return $resource(null, null, {
			'getAllVarietal': {
				method: 'GET',
				url: urlBase + '/pm/code-table/getAllVarietal',
				isArray: true
			},
			'getAllVarietalType': {
				method: 'GET',
				url: urlBase + '/pm/code-table/getAllVarietalType',
				isArray: true
			},
			'getAllWineArea': {
				method: 'GET',
				url: urlBase + '/pm/code-table/getAllWineArea',
				isArray: true
			},
			'getAllWineMaker': {
				method: 'GET',
				url: urlBase + '/pm/code-table/getAllWineMaker',
				isArray: true
			},
			'getAllWineRegion': {
				method: 'GET',
				url: urlBase + '/pm/code-table/getAllWineRegion',
				isArray: true
			},
			'getAllProductStateWarnings': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/productStateWarning/findAll',
				isArray:true
			},
			'getAllRetailUnitsOfMeasure': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/retailUnitOfMeasure/findAll',
				isArray:true
			},
			'findAllProductGroupTypes': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/productGroupType/findAllProductGroupTypes',
				isArray: true
			},
			'addNewVarietal' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/addNewlVarietal',
				isArray: false
			},
			'updateVarietal' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/updateVarietal',
				isArray: false
			},
			'deleteVarietal' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/deleteVarietal',
				isArray: false
			},
			'addNewVarietalType' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/addNewlVarietalType',
				isArray: false
			},
			'updateVarietalType' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/updateVarietalType',
				isArray: false
			},
			'deleteVarietalType' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/deleteVarietalType',
				isArray: false
			},
			'addNewWineRegion' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/addNewlWineRegion',
				isArray: false
			},
			'updateWineRegion' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/updateWineRegion',
				isArray: false
			},
			'deleteWineRegion' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/deleteWineRegion',
				isArray: false
			},
			'addNewWineMaker' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/addNewlWineMaker',
				isArray: false
			},
			'updateWineMaker' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/updateWineMaker',
				isArray: false
			},
			'deleteWineMaker' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/deleteWineMaker',
				isArray: false
			},
			'addNewWineArea' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/addNewlWineArea',
				isArray: false
			},
			'updateWineArea' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/updateWineArea',
				isArray: false
			},
			'deleteWineArea' : {
				method: 'POST',
				url: urlBase + '/pm/code-table/deleteWineArea',
				isArray: false
			},
			'getAllProductCategories': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/productCategory/allProductCategories',
				isArray: true
			},
			'getAllMarketConsumerEventTypes': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/productCategory/allMarketConsumerEventTypes',
				isArray:true
			},
			'getAllProductCategoryRoles': {
				method: 'GET',
				url: urlBase + '/pm/codeTable/productCategory/allProductCategoryRoles',
				isArray:true
			},
			'updateProductCategory': {
				method: 'POST',
				url: urlBase + '/pm/codeTable/productCategory/updateProductCategory',
				isArray:false
			},
			'addProductCategory': {
				method: 'POST',
				url: urlBase + '/pm/codeTable/productCategory/addProductCategory',
				isArray:false
			},
			'deleteProductCategory': {
				method: 'POST',
				url: urlBase + '/pm/codeTable/productCategory/deleteProductCategory',
				isArray:false
			},
			'deleteProductGroupTypes': {
				method: 'POST',
				url: urlBase + '/pm/codeTable/productGroupType/deleteProductGroupTypes',
			},
		});
	}
})();
