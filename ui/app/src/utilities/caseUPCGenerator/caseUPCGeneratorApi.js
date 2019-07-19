/*
 * productUPCGeneratorApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * API to support display product updates task details.
 *
 * @author vn86116
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('CaseUPCGeneratorApi', caseUPCGeneratorApi);
	caseUPCGeneratorApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to task summary.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function caseUPCGeneratorApi(urlBase, $resource) {
		return $resource(null, null, {
			getUpcGenerateForCaseUpc: {
				method: 'GET',
				url: urlBase + '/pm/utilities/caseUPCGenerator/getUpcGenerateForCaseUpc',
				isArray: false
			}
		});

	}
})();
