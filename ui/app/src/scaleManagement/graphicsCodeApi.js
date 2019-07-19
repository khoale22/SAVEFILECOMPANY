/*
 *
 * graphicsCodeApi.js
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
 * Constructs the API to call the backend for graphics code api.
 *
 * @author vn40486
 * @since 2.1.0
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('GraphicCode', graphicsCodeApi);

	graphicsCodeApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function graphicsCodeApi(urlBase, $resource) {
		return $resource(
			urlBase + '/pm/scaleManagement/graphicCode',null,
			{
				'queryByGraphicsCodes': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/graphicCode/graphicCodes',
					isArray:false
				},
				'queryByGraphicsCodeDescription': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/graphicCode/graphicCodeDescription',
					isArray:false
				},
				'queryScaleUpcByScaleCode': {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/graphicCode/scaleUpc',
					isArray:false
				},
				'findHitsByGraphicsCodes' : {
					method: 'GET',
					url: urlBase + '/pm/scaleManagement/graphicCode/hits',
					isArray:false
				},
				'update' : { method: 'PUT' }
			}
		);
	}
})();
