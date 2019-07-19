/*
 *
 * attributeMaintenanceApi.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author a786878
 * @since 2.15.0
 */
'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('sourceSystemApi', sourceSystemApi);
	sourceSystemApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's sourceSystem API.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The inventory API factory.
	 */
	function sourceSystemApi(urlBase, $resource) {
		return $resource(null, null, {
			'findAll': {
				method: 'GET',
				url: urlBase + '/pm/sourceSystem/findAll',
				isArray: true
			}
		});
	}
})();
