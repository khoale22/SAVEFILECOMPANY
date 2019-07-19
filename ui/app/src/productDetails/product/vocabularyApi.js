/*
 *
 * vocabularyApi.js
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
 * Constructs the API to call the backend for dictionaryApi .
 * */

(function () {

	angular.module('productMaintenanceUiApp').factory('VocabularyApi', vocabularyApi);

	vocabularyApi.$inject = ['urlBase', '$resource'];

	function vocabularyApi(urlBase, $resource) {
		return $resource(null, null, {
			// Queries all Import Items by item code.
			'validateText': {
				method: 'GET',
				url: urlBase + '/pm/dictionary/vocabulary/validateText',
				isArray:false
			},
			'validateCamelCaseText': {
				method: 'GET',
				url: urlBase + '/pm/spellCheck/validateText',
				isArray:false
			},
			'validateSpellCheckText': {
				method: 'GET',
				url: urlBase + '/pm/spellCheck/validateSpellCheckText',
				isArray:false
			},
			'validateRomanceCopySpellCheck': {
				method: 'GET',
				url: urlBase + '/pm/spellCheck/validateRomanceCopySpellCheck',
				isArray:false
			},
			'validateBrandSpellCheck': {
				method: 'GET',
				url: urlBase + '/pm/spellCheck/validateBrandSpellCheck',
				isArray:false
			},
			'findVocabularies': {
				method: 'GET',
				url: urlBase + '/pm/spellCheck/findVocabularies',
				isArray:false
			},
			'addNewVocabulary': {
				method: 'GET',
				url: urlBase + '/pm/spellCheck/addNewVocabulary',
				isArray:false
			}
		});
	}
})();
