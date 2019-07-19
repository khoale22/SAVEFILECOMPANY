/*
 * StatusApi.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Creates API to call status functions on the backend.
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('StatusApi', statusApi);

	statusApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates and returns the API to call for status information.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function statusApi(urlBase, $resource) {
		return $resource(urlBase + '/status/');
	}
})();
