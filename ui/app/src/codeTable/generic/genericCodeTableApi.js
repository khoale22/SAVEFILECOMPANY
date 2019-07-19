/*
 * genericCodeTableApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for generic code table.
 *
 * @author m314029
 * @since 2.21.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('GenericCodeTableApi', genericCodeTableApi);

	genericCodeTableApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function genericCodeTableApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/generic';
		return $resource(
			urlBase, null,
			{
				"findAllByTable": {
					method: 'GET',
					url: urlBase + '/findAllByTable',
					isArray: true
				},
				"findCodeByTableNameAndId": {
					method: 'GET',
					url: urlBase + '/findCodeByTableNameAndId',
					isArray: false
				},
				"addAllByTableNameAndCodeTables": {
					method: 'POST',
					url: urlBase + '/addAllByTableNameAndCodeTables',
					isArray: false
				},
				"deleteCodeByTableNameAndId": {
					method: 'DELETE',
					url: urlBase + '/deleteCodeByTableNameAndId',
					isArray: false
				},
				"saveCodeByTableNameAndCodeTable": {
					method: 'POST',
					url: urlBase + '/saveCodeByTableNameAndCodeTable',
					isArray: false
				}
			}
		);
	}
})();
