/*
 * taxonomy.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product > taxonomy.
 *
 * @author vn73545
 * @since 2.15.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('ProductTaxonomyApi', productTaxonomyApi);

	productTaxonomyApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function productTaxonomyApi(urlBase, $resource) {
		return $resource(
			urlBase += '/pm/productTaxonomy', null,
			{
				'findAll': {
					method: 'GET',
					url: urlBase + '/level/findAll',
					isArray: true
				},
				'getByProductIdAndProductType': {
					method: 'GET',
					url: urlBase + '/attribute/getByProductIdAndProductType',
					isArray: true
				},
				'getProductAttributeValues': {
					method: 'GET',
					url: urlBase + '/attribute/getAttributeValues',
					isArray: true
				},
				'updateProductAttributes': {
					method: 'POST',
					url: urlBase + '/attribute/update',
					isArray: true
				},
				'requestProductAttributeValues': {
					method: 'POST',
					url: urlBase + '/attribute/request-new',
					isArray: false
				}
			}
		);
	}
})();
