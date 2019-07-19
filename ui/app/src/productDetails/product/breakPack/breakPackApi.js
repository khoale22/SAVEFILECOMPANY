/*
 * breakPackApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product > break pack.
 *
 * @author vn70516
 * @since 2.15.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('BreakPackApi', breakPackApi);

	breakPackApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function breakPackApi(urlBase, $resource) {
		return $resource(
			urlBase, null,
			{
				'countProductRelationshipByProductId':{
					method: 'GET',
					url: urlBase + '/pm/breakPack/countProductRelationshipByProductId',
					isArray: false
				},
				'getProductBreakPackByProductId': {
					method: 'GET',
					url: urlBase + '/pm/breakPack/getProductBreakPackByProductId',
					isArray: true
				},
				'getProductByUpc':{
					method: 'GET',
					url: urlBase + '/pm/breakPack/getProductByUpc',
					isArray: false
				},
				'updateProductRelationship':{
					method: 'POST',
					url: urlBase + '/pm/breakPack/updateProductRelationship',
					isArray: false
				},
				'getBreakPackAttributesAudits': {
					method: 'GET',
					url: urlBase + '/pm/breakPack/getBreakPackAttributesAudits',
					isArray: true
				}
			}
		);
	}
})();
