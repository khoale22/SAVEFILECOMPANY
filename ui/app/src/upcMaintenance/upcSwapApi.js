/*
 * moveWarehouseUpcSwapApi.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

/**
 * Created by m314029 on 8/4/2016.
 */

'use strict';

/**
 * Constructs the API to call the backend for a upc swap.
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('UpcSwapApi', upcSwapApi);

	upcSwapApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API to call the backend functions related to warehouse move upc swap.
	 *
	 * @param urlBase The Base URL for the back-end.
	 * @param $resource Angular $resource factory used to create the API.
	 * @returns {*}
	 */
	function upcSwapApi(urlBase, $resource){
		return $resource(null, null, {
			// Submit list of upc swaps.
			'submitWarehouseMoveUpcSwaps': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/warehouseMoveUpcSwap',
				isArray:false
			},
			// Submit list of upc swaps for warehouse to warehouse.
			'submitWhsToWhsUpcSwap': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/warehouseToWarehouseSwap',
				isArray:true
			},
			// Get details on warehouse move upc swaps.
			'getWarehouseMoveUpcSwapsDetails': {
				method: 'GET',
				url: urlBase + '/pm/upcMaintenance/warehouseMoveUpcSwap',
				isArray:false
			},'getWarehouseSwapUpcSwapsDetails': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/warehouseToWarehouseSwap/fetchDetails',
				isArray:true
			},
			'submitDsdToBoth': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/dsdToBoth',
				isArray:false
			},
			'getDsdToBothDetails': {
				method: 'GET',
				url: urlBase + '/pm/upcMaintenance/dsdToBoth',
				isArray:false
			},
			'submitBothToDsd': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/bothToDsd',
				isArray:false
			},
			'getBothToDsdDetails': {
				method: 'GET',
				url: urlBase + '/pm/upcMaintenance/bothToDsd',
				isArray:false
			},
			'getAddAssociateDetails': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/addAssociate/fetchDetails',
				isArray:true
			},
			'getEmptySwap': {
				method: 'GET',
				url: urlBase + '/pm/upcMaintenance/upcSwapCommons/emptySwap',
				isArray:false
			},
			'addAssociateUpcs': {
				method: 'POST',
				url: urlBase + '/pm/upcMaintenance/addAssociate',
				isArray:false
			}
		});
	}
})();
