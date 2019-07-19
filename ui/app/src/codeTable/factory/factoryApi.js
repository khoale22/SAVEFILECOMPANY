/*
 * choiceApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table factory.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function(){
	angular.module('productMaintenanceUiApp').factory('FactoryApi', factoryApi);

	factoryApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function factoryApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/factory';
		return $resource(
			urlBase ,null,
			{
				'findAllFactory': {
					method: 'GET',
					url: urlBase + '/findAllFactory',
					isArray:true
				},
				'findAllCountry' : {
					method: 'GET',
					url: urlBase + '/findAllCountry',
					isArray:true
				},
				'addNewFactory':{
					method: 'POST',
					url: urlBase + '/addNewFactory',
					isArray:false
				},
				'updateFactory':{
					method: 'POST',
					url: urlBase + '/updateFactory',
					isArray:false
				},
				'deleteFactories':{
					method: 'POST',
					url: urlBase + '/deleteFactories',
					isArray:false
				}
			}
		);
	}
})();
