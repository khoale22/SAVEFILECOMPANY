/*
 *  tobaccoProductTypeCodeApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table tobacco product type code.
 *
 * @author vn75469
 * @since 2.16.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('tobaccoProductTypeCodeApi', tobaccoProductTypeCodeApi);

	tobaccoProductTypeCodeApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function tobaccoProductTypeCodeApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/tobaccoProductType';
		return $resource(
			urlBase, null,
			{
				'getAllTobaccoTypeCodes': {
					method: 'GET',
					url: urlBase,
					isArray: true
				},
				'updateTobaccoProductType': {
					method: 'POST',
					url: urlBase,
					isArray: false
				}
			}
		);
	}
})();
