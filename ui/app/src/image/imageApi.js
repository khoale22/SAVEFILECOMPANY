/*
 * imageApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * API to support display nutrition updates task details.
 *
 * @author vn40486
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('ImageApi', imageApi);
	imageApi.$inject = ['$http', 'urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to ecommerce tasks.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function imageApi($http, urlBase, $resource) {
		return $resource(null, null, {
			getPrimaryImageByProductId: {
				method: 'GET',
				url: urlBase + '/pm/imageInfo/imagePrimary'
			}
		});

	}
})();
