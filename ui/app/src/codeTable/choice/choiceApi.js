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
 * Constructs the API to call the backend for code table choice.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').factory('ChoiceApi', choiceApi);

	choiceApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function choiceApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/choice';
		return $resource(
			urlBase, null,
			{
				'findAllChoiceType': {
					method: 'GET',
					url: urlBase + '/findAllChoiceType',
					isArray: true
				},
				'findAllChoiceOption': {
					method: 'GET',
					url: urlBase + '/findAllChoiceOption',
					isArray: true
				},
				'findAllChoiceOptionImageUri':{
					method: 'GET',
					url: urlBase + '/findAllChoiceOptionImageUri',
					isArray: true
				},
				'findChoiceOptionImageByUri' :{
					method: 'GET',
					url: urlBase + '/findChoiceOptionImageByUri',
					isArray: false
				},
				'addNewChoiceOptions': {
					method: 'POST',
					url: urlBase + '/addNewChoiceOptions',
					isArray: false
				},
				'updateChoiceOptions':{
					method: 'POST',
					url: urlBase + '/updateChoiceOptions',
					isArray:false
				},
				'deleteChoiceOptions':{
					method: 'POST',
					url: urlBase + '/deleteChoiceOptions',
					isArray: false
				}
			}
		);
	}
})();
