/*
 * wineScoringOrganizationApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 * @author vn70529
 * @since 2.12
 */

'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('WineScoringOrganizationApi', wineScoringOrganizationApi);
	wineScoringOrganizationApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's wineScoringOrganization API.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The wineScoringOrganizationApi factory.
	 */
	function wineScoringOrganizationApi(urlBase, $resource) {
		var newUrlBase = urlBase + '/pm/wineScoringOrganization';
		return $resource(urlBase, null, {

			'getAllWineScoringOrganizationsOrderById': {
				url: newUrlBase + '/getAllWineScoringOrganizationsOrderById',
				method: 'GET',
				isArray: true
			},
			'addNewWineScoringOrganizations': {
				method: 'POST',
				url: newUrlBase + '/addNewWineScoringOrganizations',
				isArray: false
			},
			'updateWineScoringOrganizations': {
				method: 'POST',
				url: newUrlBase + '/updateWineScoringOrganizations',
				isArray: false
			},
			'deleteWineScoringOrganizations': {
				method: 'POST',
				url: newUrlBase + '/deleteWineScoringOrganizations',
				isArray: false
			}
		});
	}

})();
