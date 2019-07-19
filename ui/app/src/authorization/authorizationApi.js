/*
 * authorizationApi.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for authorizationApi.
 *
 * @author vn70529
 * @since 2.22.0
 */

(function () {

	angular.module('productMaintenanceUiApp').factory('AuthorizationApi', authorizationApi);
	authorizationApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to authorizationApi.
	 *
	 * Supported method:
	 * authorizationApi: Will return all varietal from database.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The API factory.
	 */
	function authorizationApi(urlBase, $resource) {
		return $resource(null, null, {
			'findItems': {
				method: 'GET',
				url: urlBase + '/pm/authorization/findItems',
				isArray: false
			},
			'getStores': {
				method: 'GET',
				url: urlBase + '/pm/authorization/getStores',
				isArray: false
			},'submitAuthorizeItem': {
				method: 'POST',
				url: urlBase + '/pm/authorizeItem/submit',
				isArray: false
			},
		});
	}
})();
