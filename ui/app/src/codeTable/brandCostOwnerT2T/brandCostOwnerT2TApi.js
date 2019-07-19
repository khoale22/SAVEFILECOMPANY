/*
 * brandCostOwnerT2TApi.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn70529
 * @since 2.12.0
 */

'use strict';

(function () {

	angular.module('productMaintenanceUiApp').factory('BrandCostOwnerT2TApi', brandCostOwnerT2TApi);
	brandCostOwnerT2TApi.$inject = ['urlBase', '$resource'];

	/**
	 * Creates a factory to create methods to contact product maintenance's brandCostOwnerT2TApi API.
	 *
	 * Supported method:
	 * getBrandCostOwnerT2Ts: Get the list of getBrandCostOwnerT2Ts, getBrands, getCostOwners and getTop2Tops.
	 *
	 * @param urlBase The base URL to use to contact the backend.
	 * @param $resource Angular $resource used to construct the client to the REST service.
	 * @returns {*} The brandCostOwnerT2TApi factory.
	 */
	function brandCostOwnerT2TApi(urlBase, $resource) {
		var urlBaseCurrentTab = urlBase+'/pm/brndCstOwnrT2T';
		return $resource(urlBase, null, {

			'findAllBrndCstOwnrT2Ts': {
				url:urlBaseCurrentTab+'/findAllBrndCstOwnrT2Ts',
				method: 'GET'
			},
			'findBrndCstOwnrT2TsByCriteria': {
				url:urlBaseCurrentTab+'/findBrndCstOwnrT2TsByCriteria',
				method: 'GET'
			},
			'findProductBrands': {
				url:urlBase+'/pm/codeTable/productBrand/filterProductBrandsByIdAndDescription',
				method: 'GET'
			},
			filterCostOwnerByIdAndDescription: {
				url:urlBaseCurrentTab+'/filterCostOwnerByIdAndDescription',
				method: 'GET'
			},
			filterTopToTopByIdAndName: {
				url: urlBaseCurrentTab + '/filterTopToTopByIdAndName',
				method: 'GET'
			},
			filterBySelectedData: {
				url:urlBaseCurrentTab+'/filterBySelectedData',
				method: 'GET'
			}
		});
	}

})();
