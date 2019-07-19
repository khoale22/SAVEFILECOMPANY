/*
 * productLineApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author m314029
 * @since 2.26.0
 */

'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('productLineApi', productLineApi);
	productLineApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's ProductLineApi API.
	 *
	 * Supported method:
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The productLineApi factory.
	 */
	function productLineApi(urlBase, $resource) {
		var newUrlBase = urlBase + '/pm/codeTable/productLine';
		return $resource(urlBase, null, {
			'findAll': {
				url: newUrlBase  + '/findPage',
				method: 'GET',
				isArray: false
			},
			'filterProductLines': {
				url: newUrlBase  + '/filterProductLines',
				method: 'GET',
				isArray: true
			},
			'addProductLines': {
				url: newUrlBase  + '/addProductLines',
				method: '' +
				'POST',
				isArray: false
			},
			'updateProductLine': {
				method: 'PUT',
				url: newUrlBase + '/updateProductLine',
				isArray: false
			},
			'deleteProductLine': {
				method: 'DELETE',
				url: newUrlBase + '/deleteProductLine',
				isArray: false
			}
		});
	}

})();
