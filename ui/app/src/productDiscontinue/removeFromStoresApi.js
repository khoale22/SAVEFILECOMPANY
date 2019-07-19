/*
 * removeFromStoresApi.js
 *  *
 *  *
 *  *  Copyright (c) 2016 HEB
 *  *  All rights reserved.
 *  *
 *  *  This software is the confidential and proprietary information
 *  *  of HEB.
 *  *
 */

'use strict';

/**
 * Constructs the API to call the backend for remove from stores.
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('RemoveFromStoresApi', removeFromStoresApi);

	removeFromStoresApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function removeFromStoresApi(urlBase, $resource) {
		return $resource(urlBase + '/pm/productDiscontinue/removeFromStores ');
	}
})();
