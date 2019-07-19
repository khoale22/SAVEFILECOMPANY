/*
 * choiceTypeApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for code table choice.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('ChoiceTypeApi', choiceTypeApi);

	choiceTypeApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function choiceTypeApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/choiceType';
		return $resource(
			urlBase, null,
			{
				'findAllChoiceType': {
					method: 'GET',
					url: urlBase + '/findAllChoiceType',
					isArray: true
				},
				'addNewChoiceTypes': {
					method: 'POST',
					url: urlBase + '/addNewChoiceTypes',
					isArray: false
				},
				'updateChoiceTypes': {
					method: 'POST',
					url: urlBase + '/updateChoiceTypes',
					isArray: false
				},
				'deleteChoiceTypes': {
					method: 'POST',
					url: urlBase + '/deleteChoiceTypes',
					isArray: false
				}
			}
		);
	}
})();
