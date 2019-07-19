/*
 *  stateWarningsApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table state warnings.
 *
 * @author vn00602
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('StateWarningsApi', stateWarningsApi);

	stateWarningsApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function stateWarningsApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/stateWarnings';
		return $resource(
			urlBase, null,
			{
				'getAllStateWarnings': {
					method: 'GET',
					url: urlBase + '/getAllStateWarnings',
					isArray: true
				},
				'addNewStateWarnings' : {
					method: 'POST',
					url: urlBase + '/addNewStateWarnings',
					isArray: false
				},
				'updateStateWarnings' : {
					method: 'POST',
					url: urlBase + '/updateStateWarnings',
					isArray: false
				},
				'deleteStateWarnings' : {
					method: 'POST',
					url: urlBase + '/deleteStateWarnings',
					isArray: false
				}
			}
		);
	}
})();
