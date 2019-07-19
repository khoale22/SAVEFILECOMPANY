/*
 * productUpdatesTaskApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
* API to support display product updates task details.
*
* @author vn40486
* @since 2.15.0
*/
(function () {

	angular.module('productMaintenanceUiApp').factory('ProductUpdatesTaskApi', productUpdatesTaskApi);
	productUpdatesTaskApi.$inject = ['$http', 'urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to task summary.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function productUpdatesTaskApi($http, urlBase, $resource) {
		return $resource(null, null, {
			getActiveProductUpdatesCount: {
				method: 'GET',
				url: urlBase + '/pm/task/productUpdates/count',
				isArray: false
			},
			getProducts: {
				method: 'GET',
				url: urlBase + '/pm/task/productUpdates/products',
				isArray: false
			},
			getAllProducts: {
				method: 'GET',
				url: urlBase + '/pm/task/productUpdates/getAllProducts',
				isArray: false
			},
			getProductsAssignee : {
				method: 'GET',
				url: urlBase + '/pm/task/productUpdates/products/assignee/:alertType',
				isArray: true
			},
			saveData : {
				method: 'POST',
				url: urlBase + '/pm/task/productUpdates/saveData',
				isArray: false
			},
			assignToBDM : {
				method: 'POST',
				url: urlBase + '/pm/task/productUpdates/assignToBDM',
				isArray: false
			},
			assignToEBM : {
				method: 'POST',
				url: urlBase + '/pm/task/productUpdates/assignToEBM',
				isArray: false
			},
			publishProduct : {
				method: 'POST',
				url: urlBase + '/pm/task/productUpdates/publishProduct',
				isArray: false
			},
			deleteAlerts : {
				method: 'POST',
				url: urlBase + '/pm/task/productUpdates/deleteAlerts',
				isArray: false
			}
		});

	}
})();
