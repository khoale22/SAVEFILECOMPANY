/*
 * costOwnerApi.js
 *
 * Copyright (c) 2019 H-E-B
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('costOwnerApi', costOwnerApi);
	costOwnerApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's CostOwner API.
	 *
	 * Supported method:
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The costOwnerApi factory.
	 */
	function costOwnerApi(urlBase, $resource) {
		var costOwnerUrl = urlBase + '/pm/codeTable/costOwner';
		return $resource(urlBase, null, {
			'findCostOwners': {
				url: costOwnerUrl + '/findCostOwners',
				method: 'GET',
				isArray: false
			},
			'addCostOwners': {
				url: costOwnerUrl + '/addCostOwners',
				method: 'POST',
				isArray: false
			},
			'updateCostOwner': {
				url: costOwnerUrl + '/updateCostOwner',
				method: 'PUT',
				isArray: false
			},
			'deleteCostOwner': {
				url: costOwnerUrl + '/deleteCostOwner',
				method: 'DELETE',
				isArray: false
			}
		})
	}
})();