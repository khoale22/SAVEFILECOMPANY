/*
 * inventoryApi.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('InventoryFactory', inventoryFactory);
	inventoryFactory.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's inventory API.
	 *
	 * Supported methods:
	 * queryStoreInventory: Will return total inventory of a product across all stores. Pass in a parameter
	 * of productId.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The inventory API factory.
	 */
	function inventoryFactory(urlBase, $resource) {
		return $resource(urlBase + '/pm/inventory/:productId', null, {
			'queryStoreInventory': {
				method: 'GET',
				url: urlBase + '/pm/inventory/store',
				isArray: false
			},
			"queryPurchaseOrders": {
				method: 'GET',
				url: urlBase + '/pm/inventory/purchaseOrders',
				isArray: false
			},
			'queryVendorInventory': {
				method: 'GET',
				url: urlBase + '/pm/inventory/vendor',
				isArray: false
			}
		});
	}
})();

