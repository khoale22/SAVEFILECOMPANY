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

	angular.module('productMaintenanceUiApp').factory('attributeMaintenanceApi', attributeMaintenanceApi);
	attributeMaintenanceApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's attributeMaintenance API.
	 *
	 * Supported methods:
	 * getAttributeMaintenanceTable: Will return all the Dynamic Attributes.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The inventory API factory.
	 */
	function attributeMaintenanceApi(urlBase, $resource) {
		return $resource(null, null, {
			'getAttributes': {
				method: 'GET',
				url: urlBase + '/pm/eCommerce/attributeMaintenance/getAttributes',
				isArray: false
			},
			'getAttributeDetails': {
				method: 'GET',
				url: urlBase + '/pm/eCommerce/attributeMaintenance/getAttributeDetails',
				isArray: false
			},
			'updateAttributeDetails': {
				method: 'POST',
				url: urlBase + '/pm/eCommerce/attributeMaintenance/updateAttributeDetails',
				isArray: false
			},
			'getAttributeValues': {
				method: 'GET',
				url: urlBase + '/pm/eCommerce/attributeMaintenance/getAttributeValues',
				isArray: true
			},
			'getAttributeDomains': {
				method: 'GET',
				url: urlBase + '/pm/eCommerce/attributeMaintenance/getAttributeDomains',
				isArray: true
			}
		});
	}
})();
