/*
 * tierCodeApi.js
 *
 * Copyright (c) 2019 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author m314029
 * @since 2.26.0
 */

'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('tierCodeApi', tierCodeApi);
    tierCodeApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's TierCodeApi API.
	 *
	 * Supported method:
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The tierCodeApi factory.
	 */
	function tierCodeApi(urlBase, $resource) {
		var newUrlBase = urlBase + '/pm/codeTable/tierCode';
		return $resource(urlBase, null, {
			'findTierCodeList': {
				url: newUrlBase  + '/findTierCodeList',
				method: 'GET',
				isArray: false
			},
			'filterTierCodes': {
				url: newUrlBase  + '/filterTierCodes',
				method: 'GET',
				isArray: true
			},
			'addTierCodes': {
				url: newUrlBase  + '/addTierCodes',
				method: '' +
				'POST',
				isArray: false
			},
			'updateTierCode': {
				method: 'PUT',
				url: newUrlBase + '/updateTierCode',
				isArray: false
			},
			'deleteTierCode': {
				method: 'DELETE',
				url: newUrlBase + '/deleteTierCode',
				isArray: false
			}
		});
	}

})();
