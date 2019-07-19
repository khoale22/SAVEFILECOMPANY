/*
 * MyAttributeApi.js
 *  *
 *  *
 *  *  Copyright (c) 2018 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */

'use strict';

/**
 * Constructs the API to call the backend for find All My Attributes.
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('MyAttributeApi', MyAttributeApi);
	MyAttributeApi.$inject = ['urlBase', '$resource'];
	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function MyAttributeApi(urlBase, $resource) {
		return $resource(urlBase, null, {
			'findAllMyAttributes': {
				method: 'GET',
				url: urlBase + '/pm/attribute/findAllMyAttributes',
				isArray: true
			},
		});
	}
})();
