/*
 * productionSupportApi.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 *
 */

'use strict';

/**
 * API to support functionality for the production support team.
 *
 * @author d116773
 * @since 2.0.2
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('BatchApi', batchApi);

	batchApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API related to the batch jobs.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function batchApi(urlBase, $resource) {
		return $resource(urlBase + '/pm/support/jobs/:jobName');
	}
})();
