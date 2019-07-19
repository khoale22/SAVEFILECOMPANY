/*
 *
 * variantsApi.js
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
 * Constructs the API to call the backend for dictionaryApi .
 * */

(function () {

	angular.module('productMaintenanceUiApp').factory('variantsApi', variantsApi);

	variantsApi.$inject = ['urlBase', '$resource'];

	function variantsApi(urlBase, $resource) {
		return $resource(null, null, {
			'findVariantsData': {
				method: 'GET',
				url: urlBase + '/pm/variants/findVariantsData',
				isArray:true
			},
			'findPrimaryImage': {
				method: 'POST',
				url: urlBase + '/pm/variants/findPrimaryImage',
				isArray:false
			},
			'getImages': {
				method: 'GET',
				url: urlBase + '/pm/variants/imageData',
				isArray:false
			},
			'saveVariantItem': {
				method: 'POST',
				url:  urlBase + '/pm/variants/saveVariantItemData',
				isArray:false
			},
			'createCandidateWorkRequestVariants': {
				method: 'GET',
				url:  urlBase + '/pm/variants/createCandidateWorkRequest',
				isArray:false
			},
			'updateCandidateWorkRequestVariants': {
				method: 'POST',
				url:  urlBase + '/pm/variants/updateCandidateWorkRequest',
				isArray:false
			},
			'saveRelatedProducts': {
				method: 'POST',
				url: urlBase + '/pm/variants/updateRelatedProduct',
				isArray: true
			},
			'assignImages': {
				method: 'POST',
				url: urlBase + '/pm/variants/assignImage',
				isArray:false
			}
		});
	}
})();
