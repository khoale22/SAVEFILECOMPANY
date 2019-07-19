/*
 * gdsnApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Constructs the API to call GDSN functions.
 */
(
	function() {
		angular.module('productMaintenanceUiApp').factory('GdsnApi', gdsnApi);

		gdsnApi.$inject = ['urlBase', '$resource'];

		function gdsnApi(urlBase, $resource) {
			return $resource(null, null,{
				'queryVendorSubscriptions': {
					method: 'GET',
					url: urlBase + '/pm/gdsn/vendorSubscription',
					isArray: false
				},
				'queryVendorSubscriptionsByGln': {
					method: 'GET',
					url: urlBase + '/pm/gdsn/vendorSubscription/gln',
					isArray: false
				},
				'queryVendorSubscriptionsByVendorName': {
					method: 'GET',
					url: urlBase + '/pm/gdsn/vendorSubscription/vendorName',
					isArray: false
				},
				'saveVendorSubscription': {
					method: 'PUT',
					url: urlBase + '/pm/gdsn/vendorSubscription',
					isArray: false
				}
			});
		}
	}
)();
