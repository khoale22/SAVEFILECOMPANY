/*
 *
 * nutrientUomCodeApi.js
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Constructs the API to call the backend for nutrient uom code api.
 *
 * @author vn18422
 * @since 2.1.0
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('NutrientUomCodeApi', nutrientUomCodeApi);

	nutrientUomCodeApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function nutrientUomCodeApi(urlBase, $resource) {
		return $resource(
			urlBase + 'scaleManagement/nutrientUomCode',null,
			{
				'queryByNutrientUomCodes': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/code',
					isArray:false
				},
				'queryByAllNutrientUomCodes': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/all',
					isArray:false
				},
				'queryForAssociatedNutrientStatements':{
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/associatedStatements',
					isArray: false
				},
				'queryForAssociatedNutrients':{
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/associatedNutrients',
					isArray: false
				},
				'queryByNutrientUomCodeDescription': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/findNutrientUomByDescription',
					isArray:false
				},
				'queryScaleUpcByScaleCode': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUOMCode/scaleUpc',
					isArray:false
				},
				'findHitsByNutrientUomCodes' : {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUOMCode/hits',
					isArray:false
				},
				// Creates a new nutrient uom code ode with a new description.
				'addNutrientUomCode': {
					method: 'POST',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/add',
					isArray:false
				},
				// Deletes an nutrient uom code.
				'deleteNutrientUomCode': {
					method: 'POST',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/delete',
					isArray:false
				},
				// Updates a label format code with a new description.
				'updateNutrientUomCode': {
					method: 'PUT',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/update',
					isArray:false
				},
				'update' : { method: 'PUT' },
				'getNextNutrientUomCode' : {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/nutrientUomCode/getNextNutrientUomCode',
					isArray: false
				}
			}
		);
	}
})();
