/*
 * BatchUploadApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.14.0
 */
'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('BatchUploadApi', BatchUploadApi);
	BatchUploadApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's BatchUpload API.
	 *
	 * Supported method:
	 * uploadeCommerceAttribute: Will upload eCommerce Attribute file to batch process.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The batch upload API factory.
	 */
	function BatchUploadApi(urlBase, $resource) {
		return $resource(urlBase, null, {

			'uploadeCommerceAttribute': {
				url:urlBase+'/pm/eCommerceAttribute/batchUpload',
				method: 'POST',
				transformRequest: formDataObject,
				headers: { 'Content-Type': undefined, enctype: 'multipart/form-data' }

			},
			'uploadAssortment': {
				url:urlBase+'/pm/assortment/batchUpload',
				method: 'POST',
				transformRequest: formDataObject,
				headers: { 'Content-Type': undefined, enctype: 'multipart/form-data' }
			},
			'uploadRelatedProducts': {
				url:urlBase+'/pm/relatedProducts/batchUpload',
				method: 'POST',
				transformRequest: formDataObject,
				headers: { 'Content-Type': undefined, enctype: 'multipart/form-data' }
			},
			'uploadMatAttributeValuesFile' : {
				url: urlBase + '/pm/matAttributeValues/batchUpload',
				method: 'POST',
				transformRequest: formDataObject,
				headers: { 'Content-Type': undefined, enctype: 'multipart/form-data' }
			},
			/* Upload for category specific*/
			'uploadCategorySpecific': {
				url:urlBase + '/pm/categorySpecific/batchUpload',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined }
			},
			/* upload Template For nutrition And Ingredients */
			'uploadNutritionIngredient':{
				url:urlBase + '/pm/nutritionAndIngredients/batchUpload',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},/*u pload Template For PrimoPick */
			'uploadPrimoPick':{
				url:urlBase + '/pm/primoPick/batchUpload',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},
			/* Upload for Service Case Signs*/
			'uploadTemplateForServiceCaseSigns': {
				url:urlBase + '/pm/serviceCaseSigns/batchUpload',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},
			'uploadEarleyHierarchyFile' : {
				url: urlBase + '/pm/earley/hierarchy',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},
			'uploadEarleyProductFile' : {
				url: urlBase + '/pm/earley/product',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},
			'uploadEarleyAttributeFile' : {
				url: urlBase + '/pm/earley/attribute',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},
			'uploadEarleyAttributeValuesFile' : {
				url: urlBase + '/pm/earley/attributeValues',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},
			'uploadEarleyProductAttributeFile' : {
				url: urlBase + '/pm/earley/proudctAttribute',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			},/*u pload Template For eBM_BDA */
			'uploadEbmBda':{
				url:urlBase + '/pm/ebmBda/batchUpload',
				method: 'POST',
				transformRequest: angular.identity,
				headers: { 'Content-Type': undefined}
			}
		});
	}
	function formDataObject(data) {
		return data;
	}

})();
