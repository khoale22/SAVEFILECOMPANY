/*
 *
 * productDetailApi.js
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
 * Constructs the API to call the backend for productDetailApi.
 *
 * @author m594201
 * @since 2.13.0
 */

(function () {

	angular.module('productMaintenanceUiApp').factory('productDetailApi', productDetailApi);

	productDetailApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to product detail data.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function productDetailApi(urlBase, $resource) {
		return $resource(null, null, {
			'getProductRecallData': {
				method: 'GET',
				url: urlBase + '/pm/productDetail/getProductRecallData'
			},
			'getUpdatedProduct': {
				method: 'GET',
				url: urlBase + '/pm/productDetail/getUpdatedProduct'
			},
			'getPublishedAttributesAudits': {
				method: 'GET',
				url: urlBase + '/pm/productECommerceView/getPublishedAttributesAudits',
				isArray: true
			},
			'getDisplayNameInformation': {
				method: 'GET',
				url: urlBase + '/pm/productECommerceView/getDisplayNameInformation',
				isArray: false
			}
		});
	}
})();
