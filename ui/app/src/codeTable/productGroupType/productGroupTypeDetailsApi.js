/*
 * productGroupTypeDetailsApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.12.0
 */

'use strict';

(function () {
	angular.module('productMaintenanceUiApp').factory('ProductGroupTypeDetailsApi', productGroupTypeDetailsApi);
	productGroupTypeDetailsApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's productGroupTypeDetails API.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The productGroupTypeDetailsApi factory.
	 */
	function productGroupTypeDetailsApi(urlBase, $resource) {
		urlBase += '/pm/codeTable/productGroupType';
		return $resource(urlBase, null, {
			'findProductGroupTypeDetailsById': {
				url: urlBase  + '/findProductGroupTypeDetailsById',
				method: 'GET',
			},
			'findSubDepartmentsByDepartment': {
				url: urlBase  + '/findSubDepartmentsByDepartment',
				method: 'GET',
				isArray: true,
			},
			'findChoiceOptionsByChoiceTypeCode': {
				url: urlBase  + '/findChoiceOptionsByChoiceTypeCode',
				method: 'GET',
				isArray: true,
			},
			'findThumbnailImageByUri': {
				url: urlBase  + '/findThumbnailImageByUri',
				method: 'GET',
			},
			'findAllDepartments': {
				url: urlBase  + '/findAllDepartments',
				method: 'GET',
				isArray: true,
			},
			'findAllChoiceTypes': {
				url: urlBase  + '/findAllChoiceTypes',
				method: 'GET',
				isArray: true,
			},
			'addProductGroupType': {
				url: urlBase  + '/addProductGroupType',
				method: 'POST'
			},
			'updateProductGroupType': {
				url: urlBase  + '/updateProductGroupType',
				method: 'POST',
			}
		});
	}
})();
