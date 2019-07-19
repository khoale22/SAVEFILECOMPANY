/*
 * reportsApi.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

(function () {

	angular.module('productMaintenanceUiApp').factory('ReportsApi', reportsApi);

	reportsApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to product searches.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function reportsApi(urlBase, $resource) {
		return $resource(null, null, {
			'getProductsByIngredient': {
				method: 'GET',
				url: urlBase + '/pm/reports/ingredients',
				isArray: false
			}
		});
	}
})();
