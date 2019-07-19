/*
 * wicLebApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn70529
 * @since 2.12
 */

'use strict';

/**
 * API to support viewing and updating WicLeb.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('WicLebApi', wicLebApi);
	wicLebApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's wicLebApi API.
	 *
	 * Supported method:
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The wicLebApi factory.
	 */
	function wicLebApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/wicLeb';
		return $resource(urlBase, null, {
			'findAllWicLebs': {
				url:urlBase+'/findAllWicLebs',
				method: 'GET',
				isArray: true
			},
			'findAllLebUpcsByWicCategoryIdAndWicSubCategoryId': {
				url:urlBase+'/findAllLebUpcsByWicCategoryIdAndWicSubCategoryId',
				method: 'GET',
				isArray: true
			},
			'findAllLebSizesByWicCategoryIdAndWicSubCategoryId': {
				url:urlBase+'/findAllLebSizesByWicCategoryIdAndWicSubCategoryId',
				method: 'GET',
				isArray: true
			},
		});
	}
})();
