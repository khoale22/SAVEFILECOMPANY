/*
 * checkstatusApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.14.0
 */
'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('checkstatusApi', checkstatusApi);
	checkstatusApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's checkstatus API.
	 *
	 * Supported method:
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The batch upload API factory.
	 */
	function checkstatusApi(urlBase, $resource) {
		return $resource(null, null, {
			'getListTracking': {
				url:urlBase+'/pm/batchUpload/getListTracking',
				method: 'GET'
			},
			'getTrackingDetail': {
				url:urlBase+'/pm/batchUpload/getTrackingDetail',
				method: 'GET'
			},
			'getTrackingDetailById': {
				url:urlBase+'/pm/batchUpload/getTrackingById',
				method: 'GET'
			},
			'getUserList': {
				url:urlBase+'/pm/batchUpload/getUserList',
				method: 'GET',
				isArray: true
			},
			'countItemExport': {
				url:urlBase+'/pm/batchUpload/countItemExport',
				method: 'GET',
				isArray: false
			}
		});
	}

	function downloadTemplate(){

	}
})();
