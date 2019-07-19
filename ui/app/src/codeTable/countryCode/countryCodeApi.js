/*
 * countryCodeApi.js
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

	angular.module('productMaintenanceUiApp').factory('CountryCodeApi', countryCodeApi);
	countryCodeApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's CountryCodeApi API.
	 *
	 * Supported method:
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The brandCostOwnerT2TApi factory.
	 */
	function countryCodeApi(urlBase, $resource) {
		var newUrlBase = urlBase + '/pm/countryCode';
		return $resource(urlBase, null, {

			'getCountryCodes': {
				url: newUrlBase  + '/getAllCountriesOrderById',
				method: 'GET',
				isArray: true
			},
			'addNewCountryCodes': {
				method: 'POST',
				url: newUrlBase + '/addNewCountries',
				isArray: false
			},
			'updateCountryCodes': {
				method: 'POST',
				url: newUrlBase + '/updateCountries',
				isArray: false
			},
			'deleteCountryCodes': {
				method: 'POST',
				url: newUrlBase + '/deleteCountries',
				isArray: false
			}
		});
	}

})();
