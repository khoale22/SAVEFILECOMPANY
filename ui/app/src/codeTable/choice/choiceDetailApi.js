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
	angular.module('productMaintenanceUiApp').factory('ChoiceDetailApi', choiceDetailApi);

	choiceDetailApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function choiceDetailApi(urlBase, $resource) {
		urlBase = urlBase + '/pm/codeTable/choiceDetail';
		return $resource(
			urlBase, null,
			{
				'findAllChoiceImages': {
					method: 'GET',
					url: urlBase + '/findAllChoiceImages',
					isArray: true
				},
				'updateChoiceImages' :{
					method: 'POST',
					url: urlBase + '/updateChoiceImage',
					isArray: false
				},
				'findChoiceImageByUri' :{
					method: 'GET',
					url: urlBase + '/findChoiceOptionImageByUri',
					isArray: false
				},
				'uploadChoiceImage': {
					method: 'POST',
					url:  urlBase + '/uploadChoiceImage',
					isArray: false,
					transformRequest: angular.identity,
					headers: { 'Content-Type': undefined }
				}
			}
		);
	}
})();
