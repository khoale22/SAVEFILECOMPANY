/*
 *
 * productDiscontinueApi.js
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
 * Constructs the API to call the backend for product discontinue reporting.
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('ProductDiscontinueApi', productDiscontinueApi);

	productDiscontinueApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function productDiscontinueApi(urlBase, $resource) {
		return $resource(urlBase + '/pm/productDiscontinue/:id', null, {
			// Get the product discontinue report by a list of item codes.
			'queryByItemCodes': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/itemCodes	',
				isArray:false
			},
			// Get the product discontinue report by a list of  UPCs.
			'queryByUPCs': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/upcs',
				isArray:false
			},
			// Get the product discontinue report by a list of  product IDs.
			'queryByProductIds': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/productIds',
				isArray:false
			},
			// Retrieve a list of found and not found UPCs in the product discontinue table.
			'queryForMissingUPCs': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/hits/upcs',
				isArray:false
			},
			// Retrieve a list of found and not found item codes in the product discontinue table.
			'queryForMissingItemCodes': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/hits/itemCodes',
				isArray:false
			},
			// Retrieve a list of found and not found product IDs in the product discontinue table.
			'queryForMissingProductIds': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/hits/productIds',
				isArray:false
			},
			// Get the product discontinue report by a BDM.
			'queryByBDM': {
			method: 'GET',
				url: urlBase + '/pm/productDiscontinue/bdm',
				isArray: false
			},
			// Get the product discontinue report by a product hierarchy level.
			'queryBySubDepartment': {
			method: 'GET',
				url: urlBase + '/pm/productDiscontinue/subDepartment',
				isArray: false
			},
			// Get the product discontinue report by a product hierarchy level.
			'queryByClassAndCommodity': {
			method: 'GET',
				url: urlBase + '/pm/productDiscontinue/classAndCommodity',
				isArray: false
			},
			// Get the product discontinue report by a product hierarchy level.
			'queryBySubCommodity': {
			method: 'GET',
				url: urlBase + '/pm/productDiscontinue/subCommodity',
				isArray: false
			},
			// Gets discontinue data and returns it as a Map<string,string> with the csv as the value (key is CSV).
			'exportToExcel': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/exportToExcel',
				isArray: false
			},
			// Gets discontinue data report by a vendor.
			'queryByVendor':{
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/vendor',
				isArray:false
			},
            // Get the product discontinue report by a product hierarchy level with criteria search is department, class, commodity, sub_commodity.
            'queryByDepartmentAndClassAndCommodityAndSubCommodity': {
                method: 'GET',
                url: urlBase + '/pm/productDiscontinue/departmentAndClassAndCommodityAndSubCommodity',
                isArray: false
            },
            // Get the product discontinue report by a product hierarchy level with criteria search is commodity, sub_commodity.
            'queryByCommodityAndSubCommodity': {
                method: 'GET',
                url: urlBase + '/pm/productDiscontinue/commodityAndSubCommodity',
                isArray: false
            },
            // Get the product discontinue report by a product hierarchy level with criteria search is department and class and commodity.
            'queryByDepartmentAndClassAndCommodity': {
                method: 'GET',
                url: urlBase + '/pm/productDiscontinue/departmentAndClassAndCommodity',
                isArray: false
            },
			// Get the product discontinue report by a product hierarchy level with criteria search is department and commodity and sub_commodity.
			'queryByDepartmentAndCommodityAndSubCommodity': {
				method: 'GET',
				url: urlBase + '/pm/productDiscontinue/departmentAndCommodityAndSubCommodity',
				isArray: false
        	},
            // Get the product discontinue report by a product hierarchy level with criteria search is department and class code.
            'queryByDepartmentAndClass': {
                method: 'GET',
                url: urlBase + '/pm/productDiscontinue/departmentAndClass',
                isArray: false
            }
		});
	}
})();
