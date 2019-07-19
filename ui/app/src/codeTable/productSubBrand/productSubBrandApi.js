/*
 *  productSubBrandApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table product sub brand.
 *
 * @author vn00602
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('ProductSubBrandApi', productSubBrandApi);

	productSubBrandApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function productSubBrandApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/productSubBrand';
		return $resource(
			urlBase, null,
			{
				'getProductSubBrandsPage': {
					method: 'GET',
					url: urlBase + '/getProductSubBrandsPage',
					isArray: false
				}
			}
		);
	}
})();
