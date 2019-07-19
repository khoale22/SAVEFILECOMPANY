/*
 * nutritionFactsApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product > nutrition facts.
 *
 * @author vn73545
 * @since 2.15.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('NutritionFactsApi', nutritionFactsApi);

	nutritionFactsApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function nutritionFactsApi(urlBase, $resource) {
		return $resource(
			urlBase, null,
			{
				'getAllNutritionFactsInformation': {
					method: 'GET',
					url: urlBase + '/pm/nutritionFacts/getAll',
					isArray: true
				},
				'approveNutritionFactInformation': {
					method: 'POST',
					url: urlBase + '/pm/nutritionFacts/approve',
					isArray: false
				}
			}
		);
	}
})();
