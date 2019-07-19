/*
 *
 *  vocabularyService.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 *
 *
 */

'use strict';

/**
 * Constructs a service to check spelling.
 */
(function () {
	angular.module('productMaintenanceUiApp').service('VocabularyService', vocabularyService);
	angular.module('productMaintenanceUiApp').directive('smallLoadingSpinner', function () {
		return {
			restrict: 'E',
			template: '<div style="width:16px;height:16px" class="small-loader"></div>'
		};
	});
	angular.module('productMaintenanceUiApp').directive('contenteditableModel', function ($parse, $timeout) {
		return {
			restrict: 'A', // only activate on element attribute
			require: '?ngModel', // get a hold of NgModelController
			link: function (scope, element, attrs, ngModel) {
				if (!ngModel) return; // do nothing if no ng-model
				/**
				 * Specify how UI should be updated
				 */
				ngModel.$render = function () {
					$(element).html(ngModel.$viewValue || '');
					ngModel.$setViewValue(element.text());
				};
				/**
				 * Listen for change events to enable binding
				 */
				element.on('keyup change', function () {
					scope.$apply(readViewText);
				});
				element.on('blur', function () {
					$timeout(function () {
						scope.$apply(readViewText);
						checkSpelling();
					}, 1000);
				});
				/**
				 * Write data to the model.
				 */
				function readViewText() {
					ngModel.$setViewValue(element.text());
				}

				/**
				 * Call check spelling funciton.
				 */
				function checkSpelling() {
					if (attrs.checkSpelling != null && attrs.checkSpelling != undefined) {
						if (attrs.isadding !== null && attrs.isadding === 'true') {
							return;
						}
						var invoker = $parse(attrs.checkSpelling);
						invoker(scope);
					}
				}
			}
		};
	});
	angular.module('productMaintenanceUiApp').directive('contenteditableRomancyModel', function ($parse, $timeout) {
		return {
			restrict: 'A', // only activate on element attribute
			require: '?ngModel', // get a hold of NgModelController
			link: function (scope, element, attrs, ngModel) {
				if (!ngModel) return; // do nothing if no ng-model
				/**
				 * Specify how UI should be updated
				 */
				ngModel.$render = function () {
					$(element).html(ngModel.$viewValue || '');
				};
				/**
				 * Listen for change events to enable binding
				 */
				element.on('keyup change', function () {
					scope.$apply(readViewText);
				});
				element.on('blur', function () {
					$timeout(function () {
						scope.$apply(readViewText);
						checkSpelling();
					}, 1000);
				});
				/**
				 * Write data to the model.
				 */
				function readViewText() {
					ngModel.$setViewValue(element.text());
				}

				/**
				 * Call check spelling funciton.
				 */
				function checkSpelling() {
					if (attrs.checkSpelling != null && attrs.checkSpelling != undefined) {
						if (attrs.isadding !== null && attrs.isadding === 'true') {
							return;
						}
						var invoker = $parse(attrs.checkSpelling);
						invoker(scope);
					}
				}
			}
		};
	});

	angular.module('productMaintenanceUiApp').directive('buttonSpellCheck', function(){
		return {
			restrict: 'A', // only activate on element attribute
			link: function(scope, element, attrs) {
				scope.$watch(attrs.buttonSpellCheck, function (value) {
					if(value === true){
						$(element).addClass("active");
					}else{
						$(element).removeClass("active");
					}
				});
			}
		};
	});

	vocabularyService.$inject = ['$compile', 'VocabularyApi', '$rootScope', '$timeout'];
	/**
	 * Construct the vocabularyService.
	 *
	 * setError - sets an error message to be displayed.
	 *
	 * @returns {{vocabularyService}}
	 */
	function vocabularyService($compile, vocabularyApi, $rootScope, $timeout) {
		var self = this;
		/**
		 * Constant for ADD_TO_DICTIONARY
		 * @type {string}
		 */
		const ADD_TO_DICTIONARY = 'Add to Dictionary';
		/**
		 * Constant for IS_ADDING
		 * @type {string}
		 */
		const IS_ADDING = 'isadding';
		self.UNKNOWN_ERROR = 'An unknown error occurred.';
		/* Possible word code values */
		self.BANNED = 'baned';
		self.FIX = 'fix';
		self.REVIEW = 'reviw';
		self.SUGGEST = 'sugst';
		self.VALID = 'valid';
		/* Possible case code values */
		self.MIXED = 'mixed';
		self.UPPER_CASE = 'upper';
		self.CAPITALIZE_FIRST_LETTER = 'cap1';
		self.LOWER_CASE_CODE = 'lower';
		self.PREPOSITION = 'preps';
		self.ANY_SPACE_REGEX = /\s+/;
		/* title case value (Capitalize first letter, lower case rest).*/
		self.TITLE_CASE_REGEX = /(?:^|\s)\S/g;
		self.SPECIAL_CASE_REGEX = /[&\/\\#,+-=()$`~!@%^|;.'":*?<>{}]/g;
		self.RULES = [self.BANNED, self.FIX, self.VALID, self.REVIEW, self.SUGGEST];
		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateRomancyTextForEdit = function (textToValidate, callback) {//Romancy popup edit mode
			var response = vocabularyApi.validateRomanceCopySpellCheck({textToValidate: textToValidate});
			response.$promise.then(
				function (result) {
					callback(result.validatedText);
				}, function (error) {
				}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateRomancyTextForView = function (textToValidate, callback) {//Romancy view mode
			var response = vocabularyApi.findVocabularies({textToValidate: textToValidate});
			response.$promise.then(
				function (result) {
					var newTextAndSuggestion = self.doValidationRomancyText(textToValidate, result.vocabularies, result.suggestions);
					callback(newTextAndSuggestion.newText, newTextAndSuggestion.suggestions);
				}, function (error) {
				}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateBrandTextForEdit = function (textToValidate, callback) {
			var response = vocabularyApi.validateBrandSpellCheck({textToValidate: textToValidate});
			response.$promise.then(
					function (result) {
						callback(result.validatedText, null);
					}, function (error) {
					}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateBrandTextForView = function (textToValidate, callback) {
			var response = vocabularyApi.findVocabularies({textToValidate: textToValidate});
			response.$promise.then(
					function (result) {
						var newTextAndSuggestion = self.doValidationBrandText(textToValidate, result.vocabularies, result.suggestions, true);
						callback(newTextAndSuggestion.newText, newTextAndSuggestion.suggestions);
					}, function (error) {
					}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateDisplayNameTextForEditOldWay = function (textToValidate, callback) {
				var response = vocabularyApi.validateSpellCheckText({textToValidate: textToValidate});
				response.$promise.then(
					function (result) {
						//blurEvent.currentTarget.value = result.validatedText;
						callback(result.validatedText, null);
					}, function (error) {
					}
				);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateCamelCaseDisplayNameOldWay = function (textToValidate, callback) {
			var response = vocabularyApi.validateCamelCaseText({textToValidate: textToValidate});
			response.$promise.then(
					function (result) {
						//blurEvent.currentTarget.value = result.validatedText;
						callback(result.validatedText, null);
					}, function (error) {
					}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateDisplayNameTextForView = function (textToValidate, callback) {
			var response = vocabularyApi.findVocabularies({textToValidate: textToValidate});
			response.$promise.then(
					function (result) {
						var newTextAndSuggestion = self.doValidationDisplayNameText(textToValidate, result.vocabularies, result.suggestions, true);
						callback(newTextAndSuggestion.newText, newTextAndSuggestion.suggestions);
					}, function (error) {
					}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateDisplayNameTextForEdit = function (textToValidate, callback) {//Display Name edit mode
			var response = vocabularyApi.findVocabularies({textToValidate: textToValidate});
			response.$promise.then(
					function (result) {
						var newTextAndSuggestion = self.doValidationDisplayNameTextForEditMode(textToValidate, result.vocabularies, result.suggestions, true);
						callback(newTextAndSuggestion.newText, newTextAndSuggestion.suggestions);
					}, function (error) {
					}
			);
		};
		/**
		 * Validate text for edit mode based on Vocabulary rules.
		 *
		 * @param textToValidate the text to validate.
		 * @param callback the callback function after validate is success.
		 * It holds two params, first param html text, second param is an object that holds the list of suggetions of textToValidate.
		 */
		self.validateCamelCaseDisplayName = function (textToValidate, callback) {
			var response = vocabularyApi.findVocabularies({textToValidate: textToValidate});
			response.$promise.then(
					function (result) {
						var newTextAndSuggestion = self.doValidateCamelCaseDisplayName(textToValidate, result.vocabularies, result.suggestions, true);
						callback(newTextAndSuggestion.newText, newTextAndSuggestion.suggestions);
					}, function (error) {
					}
			);
		};
		/**
		 * Validate a string based on Vocabulary rules.
		 *
		 * @param textToValidate The text to validate against the vocabulary rules.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @returns {{newText, suggestions: Array}}
		 */
		self.doValidationRomancyText = function (textToValidate, vocabularies, suggestions) {
			// Check spelling
			var spellCheckAppliedWords = self.checkSpellRomancy(textToValidate, vocabularies, suggestions);
			var newText = '';
			var suggestionVocabularies = [];
			// Replace error word with corrected word.
			angular.forEach(spellCheckAppliedWords, function (vocabulary) {
				if (vocabulary.newWordText.length > 0) {
					if (newText.length > 0) {
						newText += ' ';
					}
					newText += vocabulary.newWordText;
					suggestionVocabularies.push({key: vocabulary.wordText, suggestions: vocabulary.suggestions});
				}
			});
			return {newText: newText, suggestions: suggestionVocabularies};
		};
		/**
		 * This method is used to check spelling for the input string.
		 */
		self.checkSpellRomancy = function (textToValidate, vocabularies, suggestions) {
			var spellCheckRules = [];
			// Split string by space.
			var originalTextSplit = textToValidate.split(self.ANY_SPACE_REGEX);
			var newVocabulary = null;
			angular.forEach(originalTextSplit, function (wordText) {
				if(self.checkSpecialWord(wordText)){//if word is a special character
					newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					spellCheckRules.push(newVocabulary);
				}else{
					// Find all vocabularies in vocabulary table by word text.
					var vocabularyRules = self.findAllVocabularyRulesByText(wordText, vocabularies);
					if (vocabularyRules.length === 0) {//no rule word
						// There is no any vocabulary by word text.
						// Find suggestions by word text.
						newVocabulary = self.checkSpellingForNoRuleWord(wordText);
					} else {
						newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
						var vocabulary = null;
						//find rule of word
						if (vocabularyRules.length === 1) {//one rule word
							vocabulary = vocabularyRules[0];
						} else {
							// Get vocabulary by priority as below.
							vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.BANNED, vocabularyRules);
							if (vocabulary === null) {
								vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.SUGGEST, vocabularyRules);
							}
						}
						//add color for word
						if (vocabulary !== null) {
							if (self.BANNED === vocabulary.wordCodeAttribute) {
								// Check spelling for banned case.
								newVocabulary = self.checkSpellingForBannedRuleBrand(true, wordText, vocabulary, vocabularies, suggestions);
							}
							else if (self.SUGGEST === vocabulary.wordCodeAttribute) {
								// Check spelling for suggestion case.
								newVocabulary = self.checkSpellingForSuggestionRuleWord(wordText, vocabularies, suggestions);
							}
						}
					}
					spellCheckRules.push(newVocabulary);
				}
			});
			return spellCheckRules;
		};
		/**
		 * Validate a string based on Vocabulary rules.
		 *
		 * @param textToValidate The text to validate against the vocabulary rules.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @param isView true validate text for view or edit.
		 * @returns {{newText, suggestions: Array}}
		 */
		self.doValidationBrandText = function (textToValidate, vocabularies, suggestions, isView) {
			// Check spelling
			var spellCheckAppliedWords = self.checkSpellingBrand(textToValidate, vocabularies, suggestions, isView);
			var newText = '';
			var suggestionVocabularies = [];
			// Replace error word with corrected word, concat words
			angular.forEach(spellCheckAppliedWords, function (vocabulary) {
				if (vocabulary.newWordText.length > 0) {
					if (newText.length > 0) {
						newText += ' ';
					}
					newText += vocabulary.newWordText;
					suggestionVocabularies.push({key: vocabulary.wordText, suggestions: vocabulary.suggestions});
				}
			});
			return {newText: newText, suggestions: suggestionVocabularies};
		};
		/**
		 * This method is used to check spelling for the input string.
		 */
		self.checkSpellingBrand = function (textToValidate, vocabularies, suggestions, isView) {
			var spellCheckRules = [];
			// Split string by space.
			var originalTextSplit = textToValidate.split(self.ANY_SPACE_REGEX);
			var newVocabulary = null;
			angular.forEach(originalTextSplit, function (wordText) {
				// Find all vocabularies in vocabulary table by word text.
				var vocabularyRules = self.findAllVocabularyRulesByText(wordText, vocabularies);
				if (vocabularyRules.length === 0) {
					// There is no any vocabulary by word text.
					// Find suggestions by word text.
					var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					newVocabulary.suggestions = self.findSuggestionsForWordText(wordText, suggestions);
					spellCheckRules.push(newVocabulary);
				} else {
					newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					var vocabulary = null;
					if (vocabularyRules.length === 1) {
						vocabulary = vocabularyRules[0];
					} else {
						// Get vocabulary by priority as below.
						vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.BANNED, vocabularyRules);
					}
					if (vocabulary !== null) {
						if (self.BANNED === vocabulary.wordCodeAttribute) {
							// Check spelling for banned case.
							newVocabulary = self.checkSpellingForBannedRuleBrand(isView, wordText, vocabulary, vocabularies, suggestions);
						}
					}
					spellCheckRules.push(newVocabulary);
				}
			});
			return spellCheckRules;
		};
		/**
		 * Validate a string based on Vocabulary rules.
		 *
		 * @param textToValidate The text to validate against the vocabulary rules.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @param isView true validate text for view or edit.
		 * @returns {{newText, suggestions: Array}}
		 */
		self.doValidationDisplayNameText = function (textToValidate, vocabularies, suggestions, isView) {
			// Check spelling
			var spellCheckAppliedWords = self.checkSpellingDisplayName(textToValidate, vocabularies, suggestions, isView);
			var newText = '';
			var suggestionVocabularies = [];
			// Replace error word with corrected word, concat words
			angular.forEach(spellCheckAppliedWords, function (vocabulary) {
				if (vocabulary.newWordText.length > 0) {
					if (newText.length > 0) {
						newText += ' ';
					}
					newText += vocabulary.newWordText;
					suggestionVocabularies.push({key: vocabulary.wordText, suggestions: vocabulary.suggestions});
				}
			});
			return {newText: newText, suggestions: suggestionVocabularies};
		};
		/**
		 * Validate a string based on Vocabulary rules.
		 *
		 * @param textToValidate The text to validate against the vocabulary rules.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @param isView true validate text for view or edit.
		 * @returns {{newText, suggestions: Array}}
		 */
		self.doValidationDisplayNameTextForEditMode = function (textToValidate, vocabularies, suggestions, isView) {
			// Check spelling
			var spellCheckAppliedWords = self.checkSpellingDisplayNameForEditMode(textToValidate, vocabularies, suggestions, isView);
			var newText = '';
			var suggestionVocabularies = [];
			// Replace error word with corrected word, concat words
			angular.forEach(spellCheckAppliedWords, function (vocabulary) {
				if (vocabulary.newWordText.length > 0) {
					if (newText.length > 0) {
						newText += ' ';
					}
					newText += vocabulary.newWordText;
					suggestionVocabularies.push({key: vocabulary.wordText, suggestions: vocabulary.suggestions});
				}
			});
			return {newText: newText, suggestions: suggestionVocabularies};
		};
		/**
		 * Validate a string based on Vocabulary rules.
		 *
		 * @param textToValidate The text to validate against the vocabulary rules.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @param isView true validate text for view or edit.
		 * @returns {{newText, suggestions: Array}}
		 */
		self.doValidateCamelCaseDisplayName = function (textToValidate, vocabularies, suggestions, isView) {
			// Check spelling
			var spellCheckAppliedWords = self.checkCamelCaseDisplayName(textToValidate, vocabularies, suggestions, isView);
			var newText = '';
			var suggestionVocabularies = [];
			// Replace error word with corrected word, concat words
			angular.forEach(spellCheckAppliedWords, function (vocabulary) {
				if (vocabulary.newWordText.length > 0) {
					if (newText.length > 0) {
						newText += ' ';
					}
					newText += vocabulary.newWordText;
					suggestionVocabularies.push({key: vocabulary.wordText, suggestions: vocabulary.suggestions});
				}
			});
			return {newText: newText, suggestions: suggestionVocabularies};
		};
		/**
		 * This method is used to check spelling for the input string.
		 */
		self.checkSpellingDisplayName = function (textToValidate, vocabularies, suggestions, isView) {
			var spellCheckRules = [];
			// Split string by space.
			var originalTextSplit = textToValidate.split(self.ANY_SPACE_REGEX);
			var newVocabulary = null;
			angular.forEach(originalTextSplit, function (wordText) {
				// Find all vocabularies in vocabulary table by word text.
				var vocabularyRules = self.findAllVocabularyRulesByText(wordText, vocabularies);
				if (vocabularyRules.length === 0) {
					// There is no any vocabulary by word text.
					// Find suggestions by word text.
					var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					newVocabulary.suggestions = self.findSuggestionsForWordText(wordText, suggestions);
					spellCheckRules.push(newVocabulary);
				} else {
					newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					var vocabulary = null;
					if (vocabularyRules.length === 1) {
						vocabulary = vocabularyRules[0];
					} else {
						// Get vocabulary by priority as below.
						vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.BANNED, vocabularyRules);
					}
					if (vocabulary !== null) {
						if (self.BANNED === vocabulary.wordCodeAttribute) {
							// Check spelling for banned case.
							newVocabulary = self.checkSpellingForBannedRuleBrand(isView, wordText, vocabulary, vocabularies, suggestions);
						}
					}
					spellCheckRules.push(newVocabulary);
				}
			});
			return spellCheckRules;
		};
		/**
		 * This method is used to capitalize first letter of word.
		 */
		self.getTitleCaseWord = function (wordText) {
			var textReturn = angular.copy(wordText);
			textReturn = self.convertToTitleCase(textReturn);
			if(textReturn !== wordText){
				return self.getFixStyle(textReturn);
			}
		    return textReturn;
		};
		/**
		 * Check spelling for valid case.
		 *
		 * @param wordText the word text to search for.
		 * @param vocabulary the vocabulary for word text.
		 * @param vocabularyRules the list of vocabularies by wordText.
		 * @param vocabularies the list of vocabularies.
		 */
		self.checkSpellingForValidRuleDisplayName = function (isView, wordText, vocabulary, vocabularyRules, vocabularies, suggestions) {
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			var isFound = false;
			var isUpper = false;
			if (vocabularyRules.length > 1) {
				/*if (case code of aWord is included in LIST of <aWord.CS_CD in Vocabulary> OR follow BR5 above)*/
				angular.forEach(vocabularyRules, function(item){
					if (item.key.caseCodeAttribute === self.LOWER_CASE_CODE) {
						if (item.key.wordText.trim().toLowerCase() === wordText) {
							//Display in black color
							isFound = true;
						} else if (self.convertToTitleCase(item.key.wordText.trim()) === wordText) {//Rule BR5
							//Display in black color
							isFound = true;
						}
					} else if (item.key.caseCodeAttribute === self.CAPITALIZE_FIRST_LETTER) {
						if (self.convertToTitleCase(item.key.wordText.trim()) === wordText) {
							//Display in black color
							isFound = true;
						}
					} else if (item.key.caseCodeAttribute === self.UPPER_CASE) {
						if (item.key.wordText.trim().toUpperCase() === wordText) {
							//Display in black color
							isFound = true;
							isUpper = true;
						}
					} else if (item.key.caseCodeAttribute === self.MIXED) {
						// Check word text in suggestion table, it is equal then show black color.
						// or show suggestion list.
						var correctedWordArray = self.findCorrectedWords(wordText, suggestions);
						for (var i = 0; i < correctedWordArray.length; i++) {
							if (correctedWordArray[i].key.correctedText.trim() === wordText) {
								//Display in black color
								isFound = true;
								break;
							}
						}
					}
				});
			}
			if (!isFound) {
				var correctedWords = null;
				if (vocabularyRules.length === 1) {
					if (vocabulary.key.caseCodeAttribute === self.MIXED) {//mixed
						// Check word text in suggestion table, it is equal then show black color.
						// or show suggestion list.
						correctedWords = self.findCorrectedWords(wordText, suggestions);
						isFound = false;
						for (var i = 0; i < correctedWords.length; i++) {
							if (correctedWords[i].key.correctedText.trim() === wordText) {
								//Display in black color
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							// show suggestion list.
							// Provide suggestions by getting WORD_TXT
							if (isView) {
								newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
							} else {
								newVocabulary.suggestions = self.findSuggestionsForWordText(wordText, suggestions);
								newVocabulary.newWordText = self.getErrorStyle(wordText, newVocabulary.suggestions.length > 0);
							}
						}else{
							//Display in black color
							if (isView) {
								newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
							}
						}
					} else {
						if (vocabulary.key.caseCodeAttribute === self.LOWER_CASE_CODE) {//lower
							if (vocabulary.key.wordText.trim().toLowerCase() === wordText) {
								//Display in black color
								if (isView) {
									newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
								}
							}  else if (self.convertToTitleCase(vocabulary.key.wordText.trim()) === wordText) {//Rule BR5
								//Display in black color
								if (isView) {
									newVocabulary.newWordText = self.capitalizeFirstLetter(wordText.trim());
								}
							} else {
								if (isView) {
									newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
								} else {
									newVocabulary.suggestions = self.findSuggestionInVocabularyForWordText(wordText, vocabularies, suggestions);
									newVocabulary.newWordText = self.getErrorStyle(wordText, newVocabulary.suggestions.length > 0);
								}
							}
						} else {
							// Auto convert the aWord using its CS_CD and display in green
							if (vocabulary.key.caseCodeAttribute === self.UPPER_CASE) {//upper
								if (vocabulary.key.wordText.trim().toUpperCase() === wordText) {
									//Display in black color
									if (isView) {
										newVocabulary.newWordText = self.capitalizeFirstLetter(wordText.trim());
									}
								} else {
									if (isView) {
										newVocabulary.newWordText = self.getFixStyle(wordText.toUpperCase());
									} else {
										newVocabulary.suggestions = self.findSuggestionInVocabularyForWordText(wordText, vocabularies, suggestions);
										newVocabulary.newWordText = self.getErrorStyle(wordText, newVocabulary.suggestions.length > 0);
									}
								}
							} else if (vocabulary.key.caseCodeAttribute === self.CAPITALIZE_FIRST_LETTER) {//cap1
								if (self.convertToTitleCase(vocabulary.key.wordText.trim()) === wordText) {
									//Display in black color
									if (isView) {
										newVocabulary.newWordText = self.capitalizeFirstLetter(wordText.trim());
									}
								} else {
									if (isView) {
										newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
									} else {
										newVocabulary.suggestions = self.findSuggestionInVocabularyForWordText(wordText, vocabularies, suggestions);
										newVocabulary.newWordText = self.getErrorStyle(wordText, newVocabulary.suggestions.length > 0);
									}
								}
							} else{//other cases (Ex: preps,...)
								if (isView) {
									newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
								}
							}
						}
					}
				} else {
					// Display aWord as error
					// Provide suggestions by getting WORD_TXT + CS_CD from Vocabulary table
					newVocabulary.suggestions = self.findSuggestionInVocabularyForWordText(wordText, vocabularies, suggestions);
					newVocabulary.newWordText = self.getErrorStyle(wordText, newVocabulary.suggestions.length > 0);
				}
			}else{//word and it's case code are same (vocabulary rules >= 2)
				//Display in black color
				if (isView) {
					newVocabulary.newWordText = self.getTitleCaseWord(wordText.trim());
					if(isUpper){
						newVocabulary.newWordText = self.capitalizeFirstLetter(wordText.trim());
					}
				}
			}
			return newVocabulary;
		};
		/**
		 * This method is used to check spelling for the input string of Display Name on edit mode.
		 */
		self.checkSpellingDisplayNameForEditMode = function (textToValidate, vocabularies, suggestions, isView) {
			var spellCheckRules = [];
			// Split string by space.
			var originalTextSplit = textToValidate.split(self.ANY_SPACE_REGEX);
			var newVocabulary = null;
			angular.forEach(originalTextSplit, function (wordText) {
				if(self.checkSpecialWord(wordText)){//if word is a special character
					newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					spellCheckRules.push(newVocabulary);
				}else{
					// Find all vocabularies in vocabulary table by word text.
					var vocabularyRules = self.findAllVocabularyRulesByText(wordText, vocabularies);
					if (vocabularyRules.length === 0) {//no rule word
						// There is no any vocabulary by word text.
						// Find suggestions by word text.
						newVocabulary = self.checkSpellingForNoRuleWord(wordText);
					} else {
						newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
						var vocabulary = null;
						//find rule of word
						if (vocabularyRules.length === 1) {//one rule word
							vocabulary = vocabularyRules[0];
						} else {
							// Get vocabulary by priority as below.
							vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.VALID, vocabularyRules);
							if (vocabulary === null) {
								vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.BANNED, vocabularyRules);
								if (vocabulary === null) {
									vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.FIX, vocabularyRules);
									if (vocabulary === null) {
										vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.SUGGEST, vocabularyRules);
									}
								}
							}
						}
						//add color for word
						if (vocabulary !== null) {
							if (self.BANNED === vocabulary.wordCodeAttribute) {
								// Check spelling for banned case.
								newVocabulary = self.checkSpellingForBannedRuleDisplayName(wordText);
							} else if (self.FIX === vocabulary.wordCodeAttribute) {
								// Check spelling for fix cace.
								newVocabulary = self.checkSpellingForSuggestionRuleWord(wordText, vocabularies, suggestions);
							} else if (self.SUGGEST === vocabulary.wordCodeAttribute) {
								// Check spelling for suggestion case.
								newVocabulary = self.checkSpellingForSuggestionRuleWord(wordText, vocabularies, suggestions);
							} else if (self.VALID === vocabulary.wordCodeAttribute) {
								// Check spelling for valid case.
								newVocabulary = self.checkSpellingForValidRuleDisplayName(false, wordText, vocabulary, vocabularyRules, vocabularies, suggestions);
							}
						}
					}
					spellCheckRules.push(newVocabulary);
				}
			});
			return spellCheckRules;
		};
		/**
		 * This method is used to check spelling for the input string of Display Name when click Aa button.
		 */
		self.checkCamelCaseDisplayName = function (textToValidate, vocabularies, suggestions, isView) {
			var spellCheckRules = [];
			// Split string by space.
			var originalTextSplit = textToValidate.split(self.ANY_SPACE_REGEX);
			var newVocabulary = null;
			angular.forEach(originalTextSplit, function (wordText) {
				if(self.checkSpecialWord(wordText)){//if word is a special character
					newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
					spellCheckRules.push(newVocabulary);
					newVocabulary.newWordText = self.capitalizeFirstLetter(newVocabulary.newWordText);
				}else{
					// Find all vocabularies in vocabulary table by word text.
					var vocabularyRules = self.findAllVocabularyRulesByText(wordText, vocabularies);
					if (vocabularyRules.length === 0) {//no rule word
						// There is no any vocabulary by word text.
						// Find suggestions by word text.
						newVocabulary = self.checkSpellingForNoRuleWord(wordText);
						newVocabulary.newWordText = self.capitalizeFirstLetter(newVocabulary.newWordText);
					} else {
						newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
						var vocabulary = null;
						//find rule of word
						if (vocabularyRules.length === 1) {//one rule word
							vocabulary = vocabularyRules[0];
						} else {
							// Get vocabulary by priority as below.
							vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.VALID, vocabularyRules);
							if (vocabulary === null) {
								vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.BANNED, vocabularyRules);
								if (vocabulary === null) {
									vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.FIX, vocabularyRules);
									if (vocabulary === null) {
										vocabulary = self.findVocabularyInVocabularyRulesByWordCode(self.SUGGEST, vocabularyRules);
									}
								}
							}
						}
						//add color for word
						if (vocabulary !== null) {
							if (self.BANNED === vocabulary.wordCodeAttribute) {
								// Check spelling for banned case.
								newVocabulary = self.checkSpellingForBannedRuleDisplayName(wordText);
							} else if (self.FIX === vocabulary.wordCodeAttribute) {
								// Check spelling for fix cace.
								newVocabulary.newWordText = self.convertDataForFixedWord(wordText, suggestions);
							} else if (self.SUGGEST === vocabulary.wordCodeAttribute) {
								// Check spelling for suggestion case.
								newVocabulary = self.checkSpellingForSuggestionRuleWord(wordText, vocabularies, suggestions);
							} else if (self.VALID === vocabulary.wordCodeAttribute) {
								// Check spelling for valid case.
								newVocabulary = self.checkSpellingForValidRuleDisplayName(true, wordText, vocabulary, vocabularyRules, vocabularies, suggestions);
							}
						}else{
							newVocabulary.newWordText = self.capitalizeFirstLetter(newVocabulary.newWordText);
						}
					}
					spellCheckRules.push(newVocabulary);
				}
			});
			return spellCheckRules;
		};
		/**
		 * This method is used to capitalize first letter of word.
		 */
		self.capitalizeFirstLetter = function (string) {
			var textReturn = angular.copy(string);
			textReturn = textReturn.charAt(0).toUpperCase() + textReturn.slice(1);
			if(textReturn != string){
				return self.getFixStyle(textReturn);
			}
		    return textReturn;
		};
		/**
		 * Find the list of suggestion by word text.
		 *
		 * @param wordText the word text to search for.
		 * @param suggestions the list of suggestions in the suggestion table.
		 * @returns {Array} the list of suggestions.
		 */
		self.convertDataForFixedWord = function (wordText, suggestions) {
			var correctedWords = self.findCorrectedWords(wordText, suggestions);
			if(correctedWords != null && correctedWords.length > 0){
				return self.getFixStyle(correctedWords[0].key.correctedText.trim());
			}
			return wordText;
		};
		/**
		 * Check spelling for banned case.
		 *
		 * @param isView view or edit status.
		 * @param wordText the word text to search for.
		 * @param vocabulary the vocabulary by word text.
		 * @param vocabularies the list vocabularies by word text.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @returns {{wordText: *, newWordText: *, suggestions: Array}}
		 */
		self.checkSpellingForBannedRule = function (isView, wordText, vocabulary, vocabularies, suggestions) {//no use
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			if (vocabulary.key.caseCodeAttribute.trim() === self.MIXED) {
				// Find suggestion
				newVocabulary.suggestions = self.findSuggestionsForWordText(wordText, suggestions);
				// Display as error and provide suggestion.
				if (newVocabulary.suggestions.length > 1) {
					newVocabulary.newWordText = self.getErrorStyle(wordText, !isView && newVocabulary.suggestions.length > 0);
				} else {
					newVocabulary.suggestions = [];
					if (isView) {
						newVocabulary.newWordText = self.getBannedStyle(wordText);
					} else {
						newVocabulary.newWordText = '';
					}
				}
			} else {
				// Show banned word.
				if (isView) {
					// Show benned word on view mode.
					newVocabulary.newWordText = self.getBannedStyle(wordText);
				} else {
					// Remove banned in edit mode.
					newVocabulary.newWordText = '';
				}
			}
			return newVocabulary;
		};
		/**
		 * Check spelling for banned case.
		 *
		 * @param isView view or edit status.
		 * @param wordText the word text to search for.
		 * @param vocabulary the vocabulary by word text.
		 * @param vocabularies the list vocabularies by word text.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @returns {{wordText: *, newWordText: *, suggestions: Array}}
		 */
		self.checkSpellingForBannedRuleBrand = function (isView, wordText, vocabulary, vocabularies, suggestions) {
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			// Show banned word.
			if (isView) {
				// Show banned word on view mode.
				newVocabulary.newWordText = self.getBannedStyle(wordText);
			} else {
				// Remove banned in edit mode.
				newVocabulary.newWordText = '';
			}
			return newVocabulary;
		};
		/**
		 * Check spelling for banned case.
		 *
		 * @param isView view or edit status.
		 * @param wordText the word text to search for.
		 * @param vocabulary the vocabulary by word text.
		 * @param vocabularies the list vocabularies by word text.
		 * @param suggestions the list of suggestions in suggestion table.
		 * @returns {{wordText: *, newWordText: *, suggestions: Array}}
		 */
		self.checkSpellingForBannedRuleDisplayName = function (wordText) {
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			// Show banned word.
			newVocabulary.newWordText = self.getBannedStyle(wordText);
			return newVocabulary;
		};
		/**
		 * Check spelling for fix case.
		 *
		 * @param wordText the word text to search for.
		 * @param vocabulary the vocabulary for word text.
		 * @param suggestions the list of suggestions.
		 */
		self.checkSpellingForFixRule = function (isView, wordText, vocabulary, vocabularies, suggestions) {//no use
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			/* Find corrected wordText in Suggestion table */
			var correctedWordText = self.findCorrectedWordText(wordText, suggestions);
			var isFound = false;
			if (correctedWordText !== null) {
				if (correctedWordText === wordText) {
					/* Display in black color. */
					isFound = true;
				} else if (correctedWordText.toLowerCase() === wordText.toLowerCase()) {
					/* replace by exact corrected wordText and green */
					if (isView) {
						newVocabulary.newWordText = self.getFixStyle(correctedWordText);
					} else {
						newVocabulary.newWordText = correctedWordText;
					}
					isFound = true;
				} else {
					// if corrected word.CS_CD found in Vocabulary.
					// Replace the aWord by corrected wordText exactly and display in green.
					var rules = [self.VALID];
					var correctedVocs = self.findAllVocabularyRulesByTextAndRules(correctedWordText, rules, vocabularies, false);
					if (correctedVocs.length > 0) {
						if (correctedVocs[0].key.caseCodeAttribute === self.UPPER_CASE) {
							isFound = true;
							if (isView) {
								newVocabulary.newWordText = self.getFixStyle(correctedVocs[0].key.wordText.toUpperCase());
							} else {
								newVocabulary.newWordText = correctedVocs[0].key.wordText.toUpperCase();
							}
						} else if (correctedVocs[0].key.caseCodeAttribute === self.LOWER_CASE_CODE) {
							isFound = true;
							if (isView) {
								newVocabulary.newWordText = self.getFixStyle(correctedVocs[0].key.wordText.toLowerCase());
							} else {
								newVocabulary.newWordText = correctedVocs[0].key.wordText.toLowerCase();
							}
						} else if (correctedVocs[0].key.caseCodeAttribute === self.CAPITALIZE_FIRST_LETTER) {
							isFound = true;
							if (isView) {
								newVocabulary.newWordText = self.getFixStyle(self.convertToTitleCase(correctedVocs[0].key.wordText.toLowerCase()));
							} else {
								newVocabulary.newWordText = self.convertToTitleCase(correctedVocs[0].key.wordText.toLowerCase());
							}
						}
					}
				}
			}
			if (!isFound) {
				// Find suggestion
				newVocabulary.suggestions = self.findSuggestionsForWordText(wordText, suggestions);
				// Display as error and provide suggestion.
				newVocabulary.newWordText = self.getErrorStyle(wordText, !isView && newVocabulary.suggestions.length > 0);
			}
			return newVocabulary;
		};
		/**
		 * Check spelling for valid case.
		 *
		 * @param wordText the word text to search for.
		 * @param vocabulary the vocabulary for word text.
		 * @param vocabularyRules the list of vocabularies by wordText.
		 * @param vocabularies the list of vocabularies.
		 */
		self.checkSpellingForValidRule = function (isView, wordText, vocabulary, vocabularyRules, vocabularies, suggestions) {//no use
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			var isFound = false;
			if (vocabularyRules.length > 1) {
				/*if (case code of aWord is included in LIST of <aWord.CS_CD in Vocabulary> OR follow BR5 above)*/
				for (var i = 0; i < vocabularyRules.length; i++) {
					if (vocabularyRules[i].key.caseCodeAttribute === self.LOWER_CASE_CODE) {
						if (vocabularyRules[i].key.wordText.trim().toLowerCase() === wordText) {
							//Display in black color
							isFound = true;
						}
					} else if (vocabularyRules[i].key.caseCodeAttribute === self.CAPITALIZE_FIRST_LETTER) {
						if (self.convertToTitleCase(vocabularyRules[i].key.wordText.trim()) === wordText) {
							//Display in black color
							isFound = true;
						}
					} else if (vocabularyRules[i].key.caseCodeAttribute === self.UPPER_CASE) {
						if (vocabularyRules[i].key.wordText.trim().toUpperCase() === wordText) {
							//Display in black color
							isFound = true;
						}
					}
				}
			}
			if (!isFound) {
				var correctedWords = null;
				if (vocabularyRules.length === 1) {
					if (vocabulary.key.caseCodeAttribute === self.MIXED) {
						// Check word text in suggestion table, it is equal then show black color.
						// or show suggestion list.
						correctedWords = self.findCorrectedWords(wordText, suggestions);
						isFound = false;
						for (var i = 0; i < correctedWords.length; i++) {
							if (correctedWords[i].key.correctedText.trim() === wordText) {
								//Display in black color
								isFound = true;
								break;
							}
						}
						if (!isFound) {
							// show suggestion list.
							// Provide suggestions by getting WORD_TXT
							newVocabulary.suggestions = self.findSuggestionsForWordText(wordText, suggestions);
							newVocabulary.newWordText = self.getErrorStyle(wordText, !isView && newVocabulary.suggestions.length > 0);
						}
					} else {
						if (vocabulary.key.caseCodeAttribute === self.LOWER_CASE_CODE) {
							if (vocabulary.key.wordText.trim().toLowerCase() === wordText) {
								//Display in black color
							} else if (vocabulary.key.wordText.trim().toUpperCase() === wordText) {
								//Display in black color
							} else if (self.convertToTitleCase(vocabulary.key.wordText.trim()) === wordText) {
								//Display in black color
							} else {
								if (isView) {
									newVocabulary.newWordText = self.getFixStyle(wordText.toLowerCase());
								} else {
									newVocabulary.newWordText = wordText.toLowerCase();
								}
							}
						} else {
							// Auto convert the aWord using its CS_CD and display in green
							if (vocabulary.key.caseCodeAttribute === self.UPPER_CASE) {
								if (isView) {
									newVocabulary.newWordText = self.getFixStyle(wordText.toUpperCase());
								} else {
									newVocabulary.newWordText = wordText.toUpperCase();
								}
							} else if (vocabulary.key.caseCodeAttribute === self.CAPITALIZE_FIRST_LETTER) {
								if (isView) {
									newVocabulary.newWordText = self.getFixStyle(self.convertToTitleCase(wordText.trim()));
								} else {
									newVocabulary.newWordText = self.convertToTitleCase(wordText.trim());
								}
							}
						}
					}
				} else {
					// Display aWord as error
					// Provide suggestions by getting WORD_TXT + CS_CD from Vocabulary table
					newVocabulary.suggestions = self.findSuggestionInVocabularyForWordText(wordText, vocabularies, suggestions);
					newVocabulary.newWordText = self.getErrorStyle(wordText, !isView && newVocabulary.suggestions.length > 0);
				}
			}
			return newVocabulary;
		};
		/**
		 * Check spelling for suggestion case.
		 *
		 * @param wordText the wordText to search for.
		 * @param vocabularies the list of vocabularies.
		 * @param suggestions the list suggestions.
		 * @returns {{wordText: *, newWordText: *, suggestions: Array}}
		 */
		self.checkSpellingForSuggestionRuleWord = function (wordText, vocabularies, suggestions) {
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			// Get all corrected words in Suggestion Table
			// Provide suggestions by getting WORD_TXT + CS_CD from Vocabulary table
			newVocabulary.suggestions = self.findAllSuggestionsForWordText(wordText, vocabularies, suggestions);
			newVocabulary.newWordText = self.getErrorStyle(wordText, newVocabulary.suggestions.length > 0);
			return newVocabulary;
		};
		/**
		 * Find the list of suggestion by word text.
		 *
		 * @param wordText the word text to search for.
		 * @param suggestions the list of suggestions in the suggestion table.
		 * @returns {Array} the list of suggestions.
		 */
		self.findAllSuggestionsForWordText = function (wordText, vocabularies, suggestions) {
			var suggestionByWordText = [];
			suggestionByWordText.push({
				title: ADD_TO_DICTIONARY,
				correctedText: wordText,
				wordText: wordText,
				status: true
			});
			var correctedWords = self.findCorrectedWords(wordText, suggestions);
			if(correctedWords != null && correctedWords.length > 0){
				angular.forEach(correctedWords, function (correctedWord) {
					// Provide suggestions by getting WORD_TXT + CS_CD from Vocabulary
					if(vocabularies != null && vocabularies.length > 0){
						var hasData = false;
						var backupText = null;
						angular.forEach(vocabularies, function (voc) {
							var suggestionText = voc.key.wordText.trim();
							//find CS_CD of correctedText
							if(correctedWord.key.correctedText.trim() === suggestionText){
								if (voc.key.caseCodeAttribute === self.UPPER_CASE) {
									suggestionText = voc.key.wordText.trim().toUpperCase();
								} else if (voc.key.caseCodeAttribute === self.LOWER_CASE_CODE) {
									suggestionText = voc.key.wordText.trim().toLowerCase();
								} else {
									suggestionText = self.convertToTitleCase(voc.key.wordText.trim().toLowerCase());
								}
								suggestionByWordText.push({
									title: suggestionText,
									correctedText: suggestionText,
									wordText: voc.key.wordText,
									status: false
								});
								hasData = true;
							}
							//find CS_CD of wordText
							if(correctedWord.key.wordText.trim() === suggestionText){
								if (voc.key.caseCodeAttribute === self.UPPER_CASE) {
									backupText = correctedWord.key.correctedText.trim().toUpperCase();
								} else if (voc.key.caseCodeAttribute === self.LOWER_CASE_CODE) {
									backupText = correctedWord.key.correctedText.trim().toLowerCase();
								} else {
									backupText = self.convertToTitleCase(correctedWord.key.correctedText.trim().toLowerCase());
								}
							}
						});
						if(!hasData){//use CS_CD of wordText if not find CS_CD of correctedText
							suggestionByWordText.push({
								title: backupText,
								correctedText: backupText,
								wordText: wordText,
								status: false
							});
						}
					}
				});
			}
			return suggestionByWordText;
		};
		/**
		 * Check spelling for no rule word.
		 *
		 * @param wordText the wordText to search for.
		 * @param vocabularies the list of vocabularies.
		 * @param suggestions the list suggestions.
		 * @returns {{wordText: *, newWordText: *, suggestions: Array}}
		 */
		self.checkSpellingForNoRuleWord = function (wordText) {
			var newVocabulary = {wordText: wordText, newWordText: wordText, suggestions: []};
			//display only 'Add to Dictionary' on suggestion popup
			newVocabulary.suggestions = [{
				title: ADD_TO_DICTIONARY,
				correctedText: wordText,
				wordText: wordText,
				status: false
			}];
			newVocabulary.newWordText = self.getErrorStyleForNoRuleWord(wordText);
			return newVocabulary;
		};
		/**
		 * Get error style.
		 *
		 * @param wordText
		 * @returns {string}
		 */
		self.getErrorStyleForNoRuleWord = function (wordText) {
			var key = self.processSpecialChars(wordText);
			return '<span class="' + self.generateMenuOptionKey(wordText) + ' spelling-check-error" context-menu="suggestions.' + key + '">' + wordText + '</span>';
		};
		/**
		 * Find the list of suggestion by word text.
		 *
		 * @param wordText the word text to search for.
		 * @param suggestions the list of suggestions in the suggestion table.
		 * @returns {Array} the list of suggestions.
		 */
		self.findSuggestionsForWordText = function (wordText, suggestions) {
			var suggestionByWordText = [];
			suggestionByWordText.push({
				title: ADD_TO_DICTIONARY,
				correctedText: wordText,
				wordText: wordText
			});
			var correctedWords = self.findCorrectedWords(wordText, suggestions);
			angular.forEach(correctedWords, function (correctedWord) {
				// Provide suggestions by getting WORD_TXT + CS_CD from Vocabulary
				suggestionByWordText.push({
					title: correctedWord.key.correctedText.trim(),
					correctedText: correctedWord.key.correctedText.trim(),
					wordText: correctedWord.key.wordText
				});
			});
			return suggestionByWordText;
		};
		/**
		 * Find suggestion in vocabulary table by word text.
		 *
		 * @param wordText the word text to search for.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @returns {[*]} the list of suggestions.
		 */
		self.findSuggestionInVocabularyForWordText = function (wordText, vocabularies, suggestions) {
			var suggestionsByWordText = [{
				title: ADD_TO_DICTIONARY,
				correctedText: wordText,
				wordText: wordText
			}];
			var rules = [self.VALID];
			var vocabularyRules = self.findAllVocabularyRulesByTextAndRules(wordText, rules, vocabularies, true);
			var correctedWords;
			angular.forEach(vocabularyRules, function (voc) {
				var suggestionText = voc.key.wordText.trim();
				if (voc.key.caseCodeAttribute === self.UPPER_CASE) {
					suggestionText = voc.key.wordText.trim().toUpperCase();
				} else if (voc.key.caseCodeAttribute === self.LOWER_CASE_CODE) {
					suggestionText = voc.key.wordText.trim().toLowerCase();
				} else if (voc.key.caseCodeAttribute === self.CAPITALIZE_FIRST_LETTER) {
					suggestionText = self.convertToTitleCase(voc.key.wordText.trim().toLowerCase());
				} else if (voc.key.caseCodeAttribute === self.MIXED) {
					correctedWords = self.findCorrectedWords(wordText, suggestions);
					angular.forEach(correctedWords, function (suggestion) {
						if (suggestion.key.wordText.toLowerCase() === suggestionText.toLowerCase()) {
							suggestionText = suggestion.key.correctedText.trim();
						}
					});
				}
				suggestionsByWordText.push({
					title: suggestionText,
					correctedText: suggestionText,
					wordText: voc.key.wordText
				});
			});
			return suggestionsByWordText;
		};
		/**
		 * Find all entries in vocabulary matching current text and rule is BANNED, FIX, VALID, REVIEW and SUGGEST.
		 *
		 * @param wordText the word text to search for.
		 * @param vocabularies the list vocabularies.
		 * @returns {Array} the list of vocabularies.
		 */
		self.findAllVocabularyRulesByText = function (wordText, vocabularies) {
			return self.findAllVocabularyRulesByTextAndRules(wordText, self.RULES, vocabularies, true);
		};
		/**
		 * Find all entries in vocabulary matching current text and the list of rules.
		 *
		 * @param wordText the word to search for.
		 * @param rules the list of rules to search for.
		 * @param vocabularies the list of vocabularies in vocabulary table.
		 * @param ignoreCase true ignore case of wordText.
		 * @returns {Array} the list of vocabularies.
		 */
		self.findAllVocabularyRulesByTextAndRules = function (wordText, rules, vocabularies, ignoreCase) {
			var results = [];
			if (vocabularies !== null) {
				angular.forEach(vocabularies, function (vocabulary) {
					if (ignoreCase) {
						if (vocabulary.key.wordText.toLowerCase() === wordText.toLowerCase() &&
							rules.indexOf(vocabulary.wordCodeAttribute) !== -1) {
							results.push(angular.copy(vocabulary));
						}
					} else {
						if (vocabulary.key.wordText === wordText &&
							rules.indexOf(vocabulary.wordCodeAttribute) !== -1) {
							results.push(angular.copy(vocabulary));
						}
					}
				});
			}
			return results;
		};
		/**
		 * Find vocabulary in vocabulary rule by case code.
		 *
		 * @param wordCode the case code to search for.
		 * @param vocabularyRules the list of vocabulary rules.
		 * @returns {*} the vocabulary
		 */
		self.findVocabularyInVocabularyRulesByWordCode = function (wordCode, vocabularyRules) {
			var vocabulary = null;
			for (var i = 0; i < vocabularyRules.length; i++) {
				if (wordCode === vocabularyRules[i].wordCodeAttribute) {//wordCodeAttribute = valid, banned, fix,...
					vocabulary = vocabularyRules[i];
					break;
				}
			}
			return vocabulary;
		};
		/**
		 * Find the corrected text from the suggestions table by word text.
		 *
		 * @param wordText The word that was searched for.
		 * @param suggestions The list of suggestions in suggestion table.
		 * @return the corrected text.
		 */
		self.findCorrectedWordText = function (wordText, suggestions) {
			var result = null;
			if (suggestions !== null) {
				angular.forEach(suggestions, function (suggestion) {
					if (result === null && suggestion.key.wordText.toLowerCase() === wordText.toLowerCase()) {
						result = suggestion.key.correctedText.trim();
					}
				});
			}
			return result;
		};
		/**
		 * Find the list of the corrected words from the suggestions table by word text.
		 *
		 * @param wordText The word that was searched for.
		 * @return the list of suggestions.
		 */
		self.findCorrectedWords = function (wordText, suggestions) {
			var results = [];
			if (suggestions !== null) {
				angular.forEach(suggestions, function (suggestion) {
					if (suggestion.key.wordText.trim().toLowerCase() === wordText.trim().toLowerCase()) {
						results.push(suggestion);
					}
				});
			}
			return results;
		};
		/**
		 * Takes a string and converts to title case value (Capitalize first letter, lower case rest).
		 *
		 * @param text Text to convert to title case.
		 * @return Title case converted text value.
		 */
		self.convertToTitleCase = function (text) {
			return text.toLowerCase().replace(self.TITLE_CASE_REGEX, function (a) {
				return a.toUpperCase();
			});
		};
		/**
		 * Get banned style.
		 *
		 * @param wordText the word.
		 * @returns {string}
		 */
		self.getBannedStyle = function (wordText) {
			return '<span class="spelling-check-banned">' + wordText + '</span>';
		};
		/**
		 * Get fix style.
		 *
		 * @param wordText
		 * @returns {string}
		 */
		self.getFixStyle = function (wordText) {
			return '<span class="spelling-check-fix">' + wordText + '</span>';
		};
		/**
		 * Get error style.
		 *
		 * @param wordText
		 * @returns {string}
		 */
		self.getErrorStyle = function (wordText, hasSuggestion) {
			var key = self.processSpecialChars(wordText);
			if (hasSuggestion) {
				// Show menu context.
				return '<span class="' + self.generateMenuOptionKey(wordText) + ' spelling-check-error" context-menu="suggestions.' + key + '">' + wordText + '</span>';
			}
			return '<span class="' + self.generateMenuOptionKey(wordText) + ' spelling-check-error">' + wordText + '</span>';
		};
		/**
		 * Generate key for html tag that need to show context menu.
		 *
		 * @param wordText the word text.
		 * @returns {string}
		 */
		self.generateMenuOptionKey = function (wordText) {
			return 'spelling-check-' + self.processSpecialChars(wordText);
		};
		/**
		 * Process special chars.
		 *
		 * @param wordText the word text to spoccess.
		 */
		self.processSpecialChars = function (wordText) {
			var stringReturn = wordText;
			stringReturn = stringReturn.replace(self.SPECIAL_CASE_REGEX,'_');
			return stringReturn;
		};
		/**
		 * Check special word.
		 *
		 * @param wordText the word text to spoccess.
		 */
		self.checkSpecialWord = function (wordText) {
			var isSpecialWord = false;
			var word = wordText;
			if(word && word.trim().length > 0){
				var regex = self.SPECIAL_CASE_REGEX;
				isSpecialWord = regex.test(word);
				if(isSpecialWord === false){
					var regex2 =  /\W+/g;
					isSpecialWord = regex2.test(word);
				}
			}
			return isSpecialWord;
		};
		/**
		 * Create suggestion menu context.
		 *
		 * @param suggestions the object to holds the  list of suggestions by wordText.
		 */
		self.createSuggestionMenuContext = function (scope, suggestions) {
			if (scope.suggestions == null || scope.suggestions === undefined) {
				scope.suggestions = {};
			}
			angular.forEach(suggestions, function (suggestion) {
				if (suggestion.suggestions.length > 0) {
					var menus = [];
					// Create suggestion context menu for each word.
					angular.forEach(suggestion.suggestions, function (sug) {
						// Create item of suggestion menu.
						menus.push({
							text: sug.title,
							suggestion: sug,
							click: function ($itemScope, $event, modelValue, text, $li) {
								self.clickOnMenuItem($itemScope, $event, text);
							}
						});
					});
					var key = self.processSpecialChars(suggestion.key);
					scope.suggestions[key] = menus;
					// Compile context menu.
					var element = '.' + self.generateMenuOptionKey(key);
					$compile($(element))(scope);
				}
			});
		};
		/**
		 * This method will be called when user click on item of context menu.
		 *
		 * @param $itemScope
		 * @param $event
		 */
		self.clickOnMenuItem = function ($itemScope, $event, text) {
			var suggestions = $itemScope.suggestions[self.processSpecialChars($event.target.textContent)];
			var selectedSuggestion = null;
			var correctedWord = text[0].innerText;
			if (suggestions) {
				for (var i = 0; i < suggestions.length; i++) {
					selectedSuggestion = suggestions[i].suggestion;
					if (ADD_TO_DICTIONARY === correctedWord) {
						if (selectedSuggestion.title === correctedWord) {
							$($event.currentTarget).attr(IS_ADDING, 'true');
							break;
						}
					}
				}
			}
			if (selectedSuggestion !== null) {
				if (ADD_TO_DICTIONARY === correctedWord) {
					// Add to Dictionary
					$($event.currentTarget).attr(IS_ADDING, 'true');
					self.addNewVocabulary(selectedSuggestion, $itemScope, $event);
				} else {
					$($event.currentTarget).attr(IS_ADDING, 'true');
					self.addNewVocabulary(selectedSuggestion, $itemScope, $event);
					// Replace the correct word.
					// selectedSuggestion.wordText + ' / correctedText: ' + selectedSuggestion.correctedText;
					var html = $($event.currentTarget).parent().html();
					html = html.replace($event.currentTarget.outerHTML, correctedWord);
					$($event.currentTarget).parent().html(html);
					$timeout(function () {
						$('.spelling-check-error').each(function () {
							$compile(this)($itemScope);
						});
					}, 1000);
				}
			}
		};
		/**
		 * Add a new vocabulary.
		 *
		 * @param wordText word text to add.
		 * @param $itemScope
		 * @param $event
		 */
		self.addNewVocabulary = function (selectedSuggestion, $itemScope, $event) {
			self.isWaitingForResponse = true;
			var wordText = selectedSuggestion.correctedText;
			var caseCode = self.LOWER_CASE_CODE;
			if (wordText === wordText.toLowerCase()) {
				caseCode = self.LOWER_CASE_CODE;
			} else if (wordText === wordText.toUpperCase()) {
				caseCode = self.UPPER_CASE;
			} else if (wordText === self.convertToTitleCase(wordText.toLowerCase())) {
				//caseCode = self.CAPITALIZE_FIRST_LETTER;
				caseCode = self.LOWER_CASE_CODE;
			} else {
				caseCode = self.MIXED;
			}
			var response = vocabularyApi.addNewVocabulary({
				wordText: wordText.toLowerCase(),
				wordCode: self.VALID,
				caseCode: caseCode,
				status: selectedSuggestion.status
			});
			response.$promise.then(
					function (result) {
						$($event.currentTarget).attr(IS_ADDING, 'false');
						var html = $($event.currentTarget).parent().html();
						html = html.replace($event.currentTarget.outerHTML, wordText);
						$($event.currentTarget).parent().html(html);
						$timeout(function () {
							$('.spelling-check-error').each(function () {
								$compile(this)($itemScope);
							});
						}, 1000);
						self.isWaitingForResponse = false;
					}, function (error) {
						self.fetchError(error);
						$($event.currentTarget).attr(IS_ADDING, 'false');
					}
			);
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			$rootScope.$broadcast("error_message", {
				error: self.getErrorMessage(error)
			});
		};
		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return self.UNKNOWN_ERROR;
			}
		};
	}
})();
