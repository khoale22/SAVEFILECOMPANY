/*
 * dictionaryApi.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Constructs the API to call the backend for product hierarchy.
 *
 * @author vn47792
 * @since 2.7.0
 */
(function () {

	angular.module('productMaintenanceUiApp').factory('DictionaryApi', dictionaryApi);

	dictionaryApi.$inject = ['urlBase', '$resource'];

	/**
	 * Constructs the API.
	 *
	 * @param urlBase The base URL to contact the backend.
	 * @param $resource Angular $resource to extend.
	 * @returns {*} The API.
	 */
	function dictionaryApi(urlBase, $resource) {
		return $resource(null, null, {
			'findAllVocabulary':{
				method: 'GET',
				url: urlBase + '/pm/dictionary',
				isArray:false
			},
			'findVocabularyByParameter':{
				method: 'GET',
				url: urlBase + '/pm/dictionary/findVocabularyByParameter',
				isArray:false
			},
			'findSuggestionByWordText':{
				method: 'GET',
				url: urlBase + '/pm/dictionary/findSuggestionByWordText',
				isArray:true
			},
			'findAllCaseCode':{
				method: 'GET',
				url: urlBase + '/pm/dictionary/findAllCaseCode',
				isArray:true
			},
			'findAllWordCode':{
				method: 'GET',
				url: urlBase + '/pm/dictionary/findAllWordCode',
				isArray:true
			},
			'addNewVocabulary':{
				method: 'GET',
				url: urlBase + '/pm/dictionary/addNewVocabulary',
				isArray:false
			},
			'deleteVocabularies':{
				method: 'POST',
				url: urlBase + '/pm/dictionary/deleteVocabularies',
				isArray:false
			},
			'updateVocabularies':{
				method: 'POST',
				url: urlBase + '/pm/dictionary/updateVocabularies',
				isArray:false
			}
		});
	}
})();
