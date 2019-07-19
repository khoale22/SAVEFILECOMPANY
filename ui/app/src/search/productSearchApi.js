/*
 * productSearchApi.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
* API to support viewing and updating product discontinue rules.
*
* @author m594201
* @since 2.0.2
*/
(function () {

	angular.module('productMaintenanceUiApp').factory('ProductSearchApi', productSearchApi);

	productSearchApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to product searches.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function productSearchApi(urlBase, $resource) {
		return $resource(null, null, {
			'getClassesByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/itemClass/findByRegularExpression',
				isArray: false
			},
			'getCommoditiesByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/commodity/findByRegularExpression',
				isArray: false
			},
			'getSubDepartmentsByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/subDepartment',
				isArray: false
			},
			'getSubCommoditiesByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/subCommodity/findByRegularExpression',
				isArray: false
			},
			'getBDMByRegularExpression': {
				method: 'GET',
				url: urlBase + '/pm/bdm',
				isArray: false
			},
			'queryVertexTaxCategories': {
				method: 'GET',
				url:urlBase + '/pm/vertex',
				isArray: true
			},
			'queryVertexQualifyingConditions': {
				method: 'GET',
				url:urlBase + '/pm/vertex/qualifyingConditions',
				isArray: true
			},
			'queryFulfilmentChannels': {
				method: 'GET',
				url:urlBase + '/pm/salesChannel/fulfilmentChannel',
				isArray: true
			},
			'findSubCommoditiesByCommodity': {
				method: 'GET',
				url: urlBase + '/pm/productHierarchy/subCommodity/findByCommodity',
				isArray: true
			}
		});

	}
})();
