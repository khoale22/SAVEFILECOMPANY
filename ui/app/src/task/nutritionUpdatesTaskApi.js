/*
 * nutritionUpdatesTaskApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
* API to support display nutrition updates task details.
*
* @author vn40486
* @since 2.11.0
*/
(function () {

	angular.module('productMaintenanceUiApp').factory('NutritionUpdatesTaskApi', nutritionUpdatesTaskApi);
	nutritionUpdatesTaskApi.$inject = ['$http', 'urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to task summary.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function nutritionUpdatesTaskApi($http, urlBase, $resource) {
		return $resource(null, null, {
			getActiveNutritionUpdatesCount: {
				method: 'GET',
				url: urlBase + '/pm/task/nutritionUpdates/nutritionUpdatesCount',
				isArray: false
			},
			searchActiveNutritionUpdates : {
				method: 'POST',
				url: urlBase + '/pm/task/nutritionUpdates/searchActiveNutritionUpdates',
				isArray: false
			},
			getAllActiveNutritionUpdatesByParameter : {
				method: 'POST',
				url: urlBase + '/pm/task/nutritionUpdates/getAllActiveNutritionUpdatesByParameter',
				isArray: false
			},
			getAllProducts : {
				method: 'GET',
				url: urlBase + '/pm/task/nutritionUpdates/getAllProducts',
				isArray: false
			},
			rejectUpdates : {
				method: 'DELETE',
				url: urlBase + '/pm/task/nutritionUpdates/rejectUpdates',
				isArray: false
			},
			rejectAllUpdates : {
				method: 'POST',
				url: urlBase + '/pm/task/nutritionUpdates/rejectAllUpdates',
				isArray: false
			},
			hits : {
				method: 'GET',
				url: urlBase + '/pm/task/nutritionUpdates/hits',
				isArray: false
			}
		});

	}
})();
