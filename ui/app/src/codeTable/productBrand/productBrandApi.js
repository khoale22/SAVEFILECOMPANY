/*
 * productBrandApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table product brand.
 *
 * @author vn00602
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('ProductBrandApi', productBrandApi);

	productBrandApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function productBrandApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/productBrand';
		return $resource(
			urlBase, null,
			{
				"findAllProductBrands": {
					method: 'GET',
					url: urlBase + '/findAllProductBrands',
					isArray: false
				},
				"filterProductBrands": {
					method: 'GET',
					url: urlBase + '/filterProductBrands',
					isArray: false
				}
			}
		);
	}
})();
