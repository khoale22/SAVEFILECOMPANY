/*
*
* userApi.js
*
* Copyright (c) 2017 HEB
* All rights reserved.
*
* This software is the confidential and proprietary information
* of HEB.
*
*/

'use strict';

/**
 * Constructs the API to call the backend for the user details.
 * */

(function () {

	angular.module('productMaintenanceUiApp').factory('UserApi', userApi);

	userApi.$inject = ['urlBase', '$resource'];

	function userApi(urlBase, $resource) {
		return $resource(null, null, {
			// Queries user by onepass id.
			'getUserById': {
				method: 'GET',
				url: urlBase + '/pm/userInformation/getUserById',
				isArray:false
			}
		});
	}
})();
