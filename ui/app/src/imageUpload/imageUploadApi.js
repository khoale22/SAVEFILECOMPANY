/*
 *
 * sourcePriorityController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author s753601
 * @since 2.15.0
 */
'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('eCommerceApi', eCommerceApi);
	eCommerceApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's eCommerce API.
	 *
	 * Supported method:
	 * getSourcePriorityTable: Will return all the Target System Attribute Priorities sorted by attribute and then
	 *                         priority number.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The inventory API factory.
	 */
	function eCommerceApi(urlBase, $resource) {
		return $resource(null, null, {
			'getSourcePriorityTable': {
				method: 'GET',
				url: urlBase + '/pm/eCommerce/sourcePriority',
				isArray: true
			}
		});
	}
})();
