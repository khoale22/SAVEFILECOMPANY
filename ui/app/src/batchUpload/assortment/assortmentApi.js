/*
 * eCommerceAttributeApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author s753601
 * @since 2.5.0
 */
'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('assortmentApi', assortmentApi);
	assortmentApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's eCommerceAttribute API.
	 *
	 * Supported method:
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The batch upload API factory.
	 */
	function assortmentApi(urlBase, $resource) {
		return $resource(null, null, {
		});
	}

	function downloadTemplate(){

	}
})();
