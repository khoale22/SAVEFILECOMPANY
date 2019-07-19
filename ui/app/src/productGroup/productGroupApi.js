/*
 * productGroupApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product group.
 *
 * @author vn75469
 * @since 2.15.0
 */

(function () {

	angular.module('productMaintenanceUiApp').factory('productGroupApi', productGroupApi);
	productGroupApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to Product Group API.
	 *
	 * Supported method:
	 * productGroupApi: Will return all varietal from database.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The API factory.
	 */
	function productGroupApi(urlBase, $resource) {
		return $resource(null, null, {
			'findProductGroupInfo': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/findProductGroupInfo',
				isArray: false
			},
			'findAllSaleChanel':{
				method: 'GET',
				url:urlBase + '/pm/productECommerceView/findAllSaleChanel',
				isArray:true
			},
			'findProductGroup': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/findProductGroup'
			},
			'getCustomerHierarchyAssignment': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/getCustomerHierarchyAssignment',
				isArray: false
			},
			'saveCustomerHierarchyAssignment': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/saveCustomerHierarchyAssignment',
				isArray: false
			},
			'getCustomerHierarchy': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getCustomerHierarchy',
				isArray: false
			},
			'findProductGroupType': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/findProductGroupType',
				isArray: true
			},
			'findProductGroupImages': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/productGroupImage/findAllProductGroupImages',
				isArray: true
			},
			'findProductGroupIds': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/findProductGroupIds',
				isArray: true
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
			'findProductDescription': {
				method: 'GET',
				url: urlBase + '/pm/productECommerceView/findProductDescription'
			},
			'getPrimaryImageForProduct': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getImagePrimaryForProduct',
				isArray: false
			},
			'getChoice': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getChoice',
				isArray: false
			},
			'getProductMasterByProId': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getProductMasterByProId',
			},
			'getProductPrimaryScanCode': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getProductPrimaryScanCode',
				isArray: false
			},
			'findProductGroupTypeDetailsById': {
				url: urlBase  + '/pm/productGroup/findProductGroupTypeDetailsById',
				method: 'GET',
			},
			'findSubDepartmentsByDepartment': {
				url: urlBase  + '/pm/productGroup/findSubDepartmentsByDepartment',
				method: 'GET',
				isArray: true,
			},
			'findAllChoiceTypes': {
				url: urlBase  + '/pm/productGroup/findAllChoiceTypes',
				method: 'GET',
				isArray: true,
			},
			'getDataForProductGroupHierarchy': {
				url: urlBase  + '/pm/productGroup/getDataForProductGroupHierarchy',
				method: 'GET',
				isArray: true,
			},
			'updateProductGroupImages': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/productGroupImage/update',
				isArray: false
			},
			'getProductGroupById': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/:productGroupId',
				isArray: false
			},
			'uploadImage': {
				method: 'POST',
				url:  urlBase + '/pm/productGroup/productGroupImage/upload',
				isArray: false,
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined }
			},
			'getImageData': {
				url: urlBase  + '/pm/productGroup/imageData',
				method: 'GET',
				isArray: false
			},
			'getProductImage' : {
				url: urlBase  + '/pm/productGroup/productImages',
				method: 'GET',
				isArray: true
			},
			'getEcommerceView' : {
				url: urlBase  + '/pm/productGroup/ecommerceView',
				method: 'GET',
				isArray: false
			},
			'updateProductGroup': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/updateProductGroup',
				isArray: false
			},
			'deleteProductGroup': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/deleteProductGroupEcom',
				isArray: false
			},
			'checkLowestLevel':{
				method: 'GET',
				url: urlBase + '/pm/productGroup/checkLowestLevel'
			},
			'deleteProductGroupInfo': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/deleteProductGroupInfo'
			},
			'getAssociatedProduct': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getAssociatedProduct',
				isArray: false
			},
			'deleteAssociatedProduct': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/deleteAssociatedProduct',
				isArray: false
			},
			'createProductGroupInfo': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/createProductGroupInfo'
			},
			'updateProductGroupInfo': {
				method: 'POST',
				url: urlBase + '/pm/productGroup/updateProductGroupInfo'
			},
			'getProductGroupInfoAudit': {
				method: 'GET',
				url: urlBase + '/pm/productGroup/getProductGroupInfoAudit',
				isArray: true
			}
		});
	}
})();
