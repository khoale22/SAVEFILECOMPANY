/*
 * dictionaryController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Controller to support the Dictionary.
 *
 * @author vn47792
 * @since 2.7.0
 */

(function() {
	angular.module('productMaintenanceUiApp').controller('DictionaryController', dictionaryController);

	dictionaryController.$inject = ['DictionaryApi', 'ngTableParams'];

	/**
	 * Constructs the controller.
	 * @param dictionaryApi The API to fetch data from the backend.
	 * @param ngTableParams The API to set up the report table.
	 */
	function dictionaryController(dictionaryApi, ngTableParams) {

		var self = this;

		/**
		 * The number of records to show on the report.
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;
		self.optionDefault = {id:"", description:""};
		self.CASE_CODE = {cap1:"cap1",lower:"lower",upper:"upper",mixed:"mixed"};
		self.WORD_CODE = {banned:"banned",valid:"valid"};
		self.CASE_WORD_SORT_BY = "description";

		self.searchAfterHandle = false;

		self.caseCodeOptions = [];
		self.wordCodeOptions = [];

		self.wordTextSearch = '';
		self.caseCodeSelected = self.optionDefault;
		self.wordCodeSelected = self.optionDefault;

		self.currentWordTextSearch = '';
		self.currentCaseCodeSearch = '';
		self.currentWordCodeSearch = '';

		self.vocabularyList = null;
		self.suggestionList = null;

		self.errorPopup = '';
		self.titlePopup = {add:"Add New CFD Word",edit:"Edit CFD Word"};

		self.editVocabularyEnable = false;
		self.addVocabularyEnable = false;

		self.wordTextAdd = '';
		self.caseCodeAdd = self.optionDefault;
		self.wordCodeAdd = self.optionDefault;
		self.suggestionAdd = '';
		self.displayInput = false;

		self.checkAllFlag = false;
		self.vocabularyListSelected = [];
		self.vocabularyListSelectedEdit = [];
		self.validatorEdit = true;

		self.headerTitleConfirm = '';
		self.messageConfirm ='';
		self.action = '';
		self.closeBtnLabel = 'No';
		self.yesBtnEnable = true;

		self.messageWaiting;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Whether or not to ask the backed for the number of records and pages are available.
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * Whether or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * Whether or not the user is searching for all vocabulary.
		 * @type {boolean}
		 */
		self.findAll = false;

		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;

		/**
		 * The parameters passed from the ngTable when it is asking for data.
		 * @type {?}
		 */
		self.dataResolvingParams = null;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.clearAllResult();
			self.findAllCaseCode();
		};

		self.findAllCaseCode = function () {
			dictionaryApi.findAllCaseCode({
					sortBy:self.CASE_WORD_SORT_BY
				},
				self.loadDataCaseCode,
				self.fetchErrorCaseCode);
		};

		self.loadDataCaseCode = function(results){
			self.caseCodeOptions = results;
			self.findAllWordCode();
		}

		self.fetchErrorCaseCode = function (error) {
			self.caseCodeOptions = [];
			self.fetchError(error);
		}

		self.findAllWordCode = function () {
			dictionaryApi.findAllWordCode({
					sortBy:self.CASE_WORD_SORT_BY
				},
				self.loadDataWordCode,
				self.fetchErrorWordCode);
		};

		self.loadDataWordCode = function(results){
			self.wordCodeOptions = results;
		}

		self.fetchErrorWordCode = function () {
			self.wordCodeOptions = [];
			self.fetchError(error);
		}

		/**
		 * Will clear all critical and result search.
		 */
		self.clearAllResult = function(){
			self.error = null;
			self.success = null;

			self.firstSearch = false;
			self.findAll = false;

			self.wordTextSearch = '';
			self.caseCodeSelected = self.optionDefault;
			self.wordCodeSelected = self.optionDefault;

			self.currentWordTextSearch = '';
			self.currentCaseCodeSearch = '';
			self.currentWordCodeSearch = '';

			self.data = null;
			self.vocabularyList = null;
			self.vocabularyListSelected = [];

			self.tableParams.reload();
		};

		/**
		 * Will fetch a list of existing vocabulary for a word case.
		 *
		 * @param selectedOption The type of vocabulary to look for.
		 */
		self.caseCodeChanged = function(selectedOption){
			if(selectedOption != null && selectedOption != undefined){
				self.caseCodeSelected = selectedOption;
			}else{
				self.caseCodeSelected = self.optionDefault;
			}
			self.searchByParameter();
		};

		/**
		 * Will fetch a list of existing vocabulary for a classification.
		 *
		 * @param selectedOption The type of vocabulary to look for.
		 */
		self.wordCodeChanged = function(selectedOption){
			if(selectedOption != null && selectedOption != undefined){
				self.wordCodeSelected = selectedOption;
			}else{
				self.wordCodeSelected = self.optionDefault;
			}
			self.searchByParameter();
		};

		/**
		 * Checks whether searchSelection is null and if the find all option is not selected
		 * @returns {boolean} return true
		 */
		self.isCurrentStateNull = function(){
			return (!self.findAll && self.currentWordTextSearch == '' && self.currentCaseCodeSearch == '' && self.currentWordCodeSearch == '');
		};

		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.vocabularyListSelected = [];
			self.firstSearch = true;
			self.findAll = findAll;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Issue call to newSearch to call back end to fetch all nutrition codes.
		 */
		self.searchAll = function (){
			self.clearAllResult();
			self.newSearch(true);
		};

		/**
		 * Set parameter and call to search function.
		 */
		self.searchByParameter = function (){
			self.currentWordTextSearch = self.wordTextSearch;
			self.currentCaseCodeSearch = self.caseCodeSelected.id;
			self.currentWordCodeSearch = self.wordCodeSelected.id;
			self.newSearch(false);
		};

		/**
		 * Add properties selected for all vocabulary, that let we know, this vocabulary have been chosen for delete
		 * or edit function.
		 * @param _data
		 * @returns {*}
		 */
		self.setData = function (_data) {
			if(_data != null && _data.length > 0){
				for(var i=0;i<_data.length;i++) {
					if(self.findVocabularySelected(_data[i]) != -1){
						_data[i]["selected"] = true;
					}else{
						_data[i]["selected"] = false;
					}
					_data[i]["suggestion"] = self.getSuggestionByWordText(_data[i].key.wordText);
				}
			}
			return _data;
		}

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function()
		{
			return new ngTableParams(
				{
					// set defaults for ng-table
					page: 1,
					count: self.PAGE_SIZE
				}, {

					// hide page size
					counts: [],

					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {

						if(self.isCurrentStateNull()) {
							self.defer = $defer;
							params.data = [];
							return;
						}

						self.isWaiting = true;
						self.data = null;
						self.vocabularyList = null;
						self.checkAllFlag = false;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.firstSearch){
							self.includeCounts = true;
							self.firstSearch = false;
						} else {
							self.includeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getVocabulary(params.page() - 1);
					}
				}
			);
		};

		/**
		 * The parameters that define the table showing the report.
		 */
		self.tableParams = self.buildTable();

		/**
		 *  Calls the method to get data based on tab selected.
		 *
		 * @param page The page to get.
		 */
		self.getVocabulary = function(page) {
			if(self.currentWordTextSearch != '' || self.currentCaseCodeSearch != '' || self.currentWordCodeSearch != ''){
				dictionaryApi.findVocabularyByParameter({
						wordText:self.currentWordTextSearch,
						caseCode:self.currentCaseCodeSearch,
						wordCode:self.currentWordCodeSearch,
						includeCounts:self.includeCounts,
						page: page,
						pageSize: self.PAGE_SIZE
					},
					//success
					self.loadData,
					//error
					function (error) {
						self.isWaiting = false;
						self.dataResolvingParams.data = [];
						self.fetchError(error);
					}
				);
			}else{
				dictionaryApi.findAllVocabulary({
						page: page,
						pageSize: self.PAGE_SIZE,
						includeCounts: self.includeCounts,
					},
					//success
					self.loadData,
					//error
					function (error) {
						self.isWaiting = false;
						self.dataResolvingParams.data = [];
						self.fetchError(error);
					}
				);
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error =(error.data.message);
				} else if(error.data.error){
					self.error = (error.data.error);
				}else {
					self.error = error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results){
			self.error = null;
			if(!self.searchAfterHandle){
				self.success = null;
			} else {
				self.success = self.messageWaiting;
			}

			// If this was the fist page, it includes record count and total pages .
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.isWaiting = false;
				self.data = null;
				self.vocabularyList = null;
				self.suggestionList = null;
				self.dataResolvingParams.data = [];
				if(!self.searchAfterHandle){
					self.error = "No records found.";
				}
			} else {
				self.error = null;
				self.vocabularyList = results.data;
				self.defer.resolve(results.data);
				self.findWordTextNeedSuggestion(results.data);
			}
			if(self.searchAfterHandle){
				self.searchAfterHandle = false;
			}
		};

		/**
		 * Find all suggestion data basing word text list.
		 * @param data - The list of vocabulary return by search.
		 */
		self.findWordTextNeedSuggestion = function (data) {
			var wordTextList = [];
			for(var i=0;i<data.length;i++) {
				var vocabulary = data[i];
				if(vocabulary.caseCode.id == self.CASE_CODE.mixed && vocabulary.wordCode.id == self.WORD_CODE.valid){
					wordTextList.push(vocabulary.key.wordText);
				}
			}
			//call back end get suggestion information basing word text list.
			if(wordTextList.length > 0){
				self.findSuggestionByWordText(wordTextList);
			}else{
				self.isWaiting = false;
				self.data = self.setData(data);
				self.vocabularyList = self.setData(data);
				self.suggestionList = null;
				self.refreshCheckAllFlag();
			}
		};

		/**
		 * Find all suggestion information by list of word tẽt
		 * @param wordTextList - The list of word text to search
		 */
		self.findSuggestionByWordText = function (wordTextList) {
			dictionaryApi.findSuggestionByWordText({
					wordTextList:wordTextList
				},
				//success
				function (results) {
					self.isWaiting = false;
					self.suggestionList = results;
					self.data = self.setData(self.vocabularyList);
					self.refreshCheckAllFlag();
				},
				//error
				function (error) {
					self.isWaiting = false;
					self.data = self.setData(self.vocabularyList);
					self.refreshCheckAllFlag();
					self.fetchError(error);
				});
		};

		/**
		 * Return data for display field basing for case code, word code.
		 * @param wordText The word text field
		 * @param caseCd The case code field
		 * @param wordCd The word code field
		 * @returns {string}
		 */
		self.returnDisplayField = function (vocabulary) {
			var displayField = '';
			if(vocabulary.wordCode && vocabulary.caseCode && vocabulary.wordCode.id == self.WORD_CODE.valid){
				switch(vocabulary.caseCode.id){
					case self.CASE_CODE.cap1:
						displayField = self.capitalizeTheFirstLetter(vocabulary.key.wordText.toLowerCase());
						break;
					case self.CASE_CODE.lower:
						displayField = vocabulary.key.wordText.toLowerCase();
						break;
					case self.CASE_CODE.upper:
						displayField = vocabulary.key.wordText.toUpperCase();
						break;
					case self.CASE_CODE.mixed:
						if(vocabulary.suggestion && vocabulary.suggestion.key){
							displayField = vocabulary.suggestion.key.correctedText;
						}
						break;
				}
			}
			return displayField;
		};

		/**
		 * Return to capitalize the first letter in string.
		 * @param input The string input to capitalize the first letter.
		 */
		self.capitalizeTheFirstLetter = function (input) {
			return input.replace(/\b./g, function(m){ return m.toUpperCase(); });
		};

		/**
		 * Return to suggestion for mixed case and valid word.
		 * @param wordText - The word text input to find display value.
		 * @returns {string}
		 */
		self.getSuggestionByWordText = function (wordText) {
			var suggestionRet = null;
			if(self.suggestionList != null && self.suggestionList.length > 0){
				for(var i=0;i<self.suggestionList.length;i++) {
					var suggestion = self.suggestionList[i];
					if(suggestion.key.wordText.toUpperCase() == wordText.toUpperCase()){
						suggestionRet = suggestion;
						break;
					}
				}
			}
			return suggestionRet;
		};

		/**
		 * Handle add new a vocabulary when add new button clicked.
		 * Will open add new vocabulary popup, allow user enter value.
		 */
		self.addNewVocabulary = function () {
			self.clearVocabularyModal();
			self.success = '';
			self.error = '';
			self.editVocabularyEnable = false;
			self.addVocabularyEnable = true;
			$("#vocabularyModal").modal({backdrop: 'static',keyboard: true});
		};

		/**
		 * Validation display field. Auto populate suggestion information or show input to user enter.
		 */
		self.validationDisplayField = function () {
			if(self.caseCodeAdd != null && self.caseCodeAdd.id == self.CASE_CODE.mixed
				&& self.wordCodeAdd!= null && self.wordCodeAdd.id == self.WORD_CODE.valid){
				self.displayInput = true;
				self.suggestionAdd = "";
			}
			else{
				self.displayInput = false;
				self.suggestionAdd = "";
				if(self.wordCodeAdd.id == self.WORD_CODE.valid){
					switch(self.caseCodeAdd.id){
						case self.CASE_CODE.cap1:
							self.suggestionAdd = self.capitalizeTheFirstLetter(self.wordTextAdd);
							break;
						case self.CASE_CODE.lower:
							self.suggestionAdd = self.wordTextAdd.toLowerCase();
							break;
						case self.CASE_CODE.upper:
							self.suggestionAdd = self.wordTextAdd.toUpperCase();
							break;
						default:
							self.suggestionAdd = "";
							break;
					}
				}
			}
		};

		/**
		 * Case code drop down list handle changing in add new vocabulary popup.
		 *
		 * @param caseCode
		 */
		self.addCaseCodeToNewVocabulary = function (caseCode) {
			if(caseCode != null && caseCode != undefined){
				self.caseCodeAdd = caseCode;
			}else{
				self.caseCodeAdd = self.optionDefault;
			}
			self.validationDisplayField();
		};

		/**
		 * Word Code drop down list handle changing in add new vocabulary popup.
		 *
		 * @param wordCode
		 */
		self.addWordCodeToNewVocabulary = function (wordCode) {
			if(wordCode != null && wordCode != undefined) {
				self.wordCodeAdd = wordCode;
			}else{
				self.wordCodeAdd = self.optionDefault;
			}
			self.validationDisplayField();
		};

		/**
		 * Validation value entered to call add new vocabulary funtion if all value entered is valid.
		 */
		self.saveVocabulary = function(){
			self.errorPopup = '';
			if(self.addVocabularyEnable){
				if(self.wordTextAdd == '' || self.caseCodeAdd.id == '' || self.wordCodeAdd.id == '' || (self.displayInput && self.suggestionAdd == '')){
					self.errorPopup = "All fields are required.";
				}else{
					self.doAddNewVocabulary();
				}
			} else if(self.editVocabularyEnable){
				self.doUpdateVocabulary();
			}
		};

		/**
		 * Check data is changed.
		 */
		self.isDataChanged = function () {
			return(angular.toJson(self.vocabularyListSelected) != angular.toJson(self.vocabularyListSelectedEdit));
		}

		/**
		 * Validation information of vocabulary entered to request add new. Check vocabulary did not exist before
		 * add new vocabulary into Dictionary.
		 */
		self.doAddNewVocabulary = function () {
			$('#vocabularyModal').modal('hide');
			self.waitingHandle();
			dictionaryApi.addNewVocabulary({
					wordText:self.wordTextAdd,
					caseCode:self.caseCodeAdd.id,
					wordCode:self.wordCodeAdd.id,
					suggestionDesc:self.displayInput?self.suggestionAdd:''
				},
				//success
				function (results) {
					self.searchAfterHandle = true;
					self.messageWaiting = results.message;
					self.wordTextSearch = self.wordTextAdd;
					self.caseCodeSelected = self.caseCodeAdd;
					self.wordCodeSelected = self.wordCodeAdd;
					self.searchByParameter();
				},
				//error
				self.fetchError
			);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchErrorPopup = function(error) {
			if (error && error.data) {
				if (error.data.message) {
					self.errorPopup =(error.data.message);
				} else if(error.data.error){
					self.errorPopup = (error.data.error);
				}else {
					self.errorPopup = error;
				}
			}
			else {
				self.errorPopup = "An unknown error occurred.";
			}
		};

		/**
		 * Clear all value in add new popup when close popup or insert succesfully.
		 */
		self.clearVocabularyModal = function () {
			self.wordTextAdd = '';
			self.caseCodeAdd = self.optionDefault;
			self.wordCodeAdd = self.optionDefault;
			self.suggestionAdd = '';
			self.errorPopup = '';
			self.displayInput = false;
			self.addVocabularyEnable = false;
			self.editVocabularyEnable = false;
		};

		/**
		 * Delete vocabulary. Request has been called when click delete button.
		 */
		self.deleteVocabulary = function () {
			self.success = '';
			self.error = '';
			self.headerTitleConfirm = "Delete CFD Words";
			self.messageConfirm = "Are you sure you want to delete the selected word(s)?";
			self.action = "D";
			self.closeBtnLabel = 'No';
			self.yesBtnEnable = true;
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * User click yes on confirm message popup.
		 */
		self.yesConfirmAction = function () {
			$('#confirmModal').modal('hide');
			if(self.action == "D"){
				self.doDeleteVocabulary();
			}
			self.action = '';
		};

		self.waitingHandle = function () {
			self.data = null;
			if(self.dataResolvingParams){
				self.dataResolvingParams.data = [];
			}
			self.checkAllFlag = false;
			self.isWaiting = true;
		}

		/**
		 * Call to back end function deleted the vocabulary(s).
		 */
		self.doDeleteVocabulary = function () {
			var suggestions = [];
			for (var i = 0; i < self.vocabularyListSelected.length; i++) {
				var vocabularyIndex = self.vocabularyListSelected[i];
				if(vocabularyIndex.suggestion){
					suggestions.push(vocabularyIndex.suggestion);
				}
			}
			var dictionaryRuleVO = {};
			dictionaryRuleVO["vocabularyListDelete"] = self.vocabularyListSelected;
			dictionaryRuleVO["suggestionsDelete"] = suggestions;

			self.waitingHandle();

			dictionaryApi.deleteVocabularies(dictionaryRuleVO,
				//success
				function (results) {
					self.messageWaiting = results.message;
					self.searchAfterHandle = true;
					self.newSearch(self.findAll);
				},
				//error
				self.fetchError
			)
		};

		/**
		 * Edit information vocabulary. Request has been called when click edit button.
		 */
		self.editVocabulary = function () {
			self.clearVocabularyModal();
			self.errorPopup = '';
			self.success = '';
			self.error = '';
			self.editVocabularyEnable = true;
			self.addVocabularyEnable = false;
			self.vocabularyListSelectedEdit = angular.copy(self.vocabularyListSelected);
			$("#vocabularyModal").modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Do update list of vocabulary
		 */
		self.doUpdateVocabulary = function () {
			var vocabulariesUpdate = [];
			var vocabulariesDelete = [];
			var suggestionsUpdate = [];
			var suggestionsDelete = [];
			var validator = true;
			self.checkChangeData(vocabulariesUpdate, vocabulariesDelete, suggestionsUpdate, suggestionsDelete);
			if(self.validatorEdit == false){
				self.errorPopup = "You must enter value for all mandatory fields.";
				self.validatorEdit = true;
			}else{
				var dictionaryRuleVO = {};
				dictionaryRuleVO["vocabularyListUpdate"] = vocabulariesUpdate;
				dictionaryRuleVO["vocabularyListDelete"] = vocabulariesDelete;
				dictionaryRuleVO["suggestionListUpdate"] = suggestionsUpdate;
				dictionaryRuleVO["suggestionListDelete"] = suggestionsDelete;
				$('#vocabularyModal').modal('hide');
				self.waitingHandle();
				dictionaryApi.updateVocabularies(dictionaryRuleVO,
					//success
					function (results) {
						self.messageWaiting = results.message;
						self.searchAfterHandle = true;
						self.newSearch(self.findAll);
					},
					//error
					self.fetchError
				)
			}
		}

		/**
		 * Allow input display field with rule case code is mixed and word code is valid.
		 * @param caseCd - The case code of vocabulary
		 * @param wordCd - The word code of vocabulary
		 * @returns {boolean}
		 */
		self.hasSuggestionValue = function (caseCd, wordCd) {
			if(caseCd == self.CASE_CODE.mixed && wordCd == self.WORD_CODE.valid){
				return true;
			}else{
				return false;
			}
		}

		/**
		 * Check change data.
		 */
		self.checkChangeData = function (vocabulariesUpdate, vocabulariesDelete, suggestionsUpdate, suggestionsDelete) {
			//Check list vocabulary delete
			for (var i = 0; i < self.vocabularyListSelected.length; i++) {
				var vocabularyOrig = self.vocabularyListSelected[i];
				var vocabularyNew = self.vocabularyListSelectedEdit[i];
				if (angular.toJson(vocabularyOrig) != angular.toJson(vocabularyNew)) {
					//check required field
					if(vocabularyNew.caseCode == null || vocabularyNew.caseCode == undefined || vocabularyNew.wordCode == null || vocabularyNew.wordCode == undefined){
						self.validatorEdit = false;
						return;
					}
					//check change case code
					if (angular.toJson(vocabularyOrig.caseCode) != angular.toJson(vocabularyNew.caseCode)) {
						vocabulariesDelete.push(vocabularyOrig);
					}
					// check change suggestion
					if (self.hasSuggestionValue(vocabularyNew.caseCode.id, vocabularyNew.wordCode.id)) {
						//check requied suggestion
						if (vocabularyNew.suggestion == null || vocabularyNew.suggestion == undefined
							|| vocabularyNew.suggestion.key == null || vocabularyNew.suggestion.key == undefined
							|| vocabularyNew.suggestion.key.correctedText == undefined || vocabularyNew.suggestion.key.correctedText == '') {
							self.validatorEdit = false;
							return;
						}
						var suggestionUpdt = angular.copy(vocabularyNew.suggestion);
						suggestionUpdt["key"]["wordText"] = vocabularyNew.key.wordText;
						suggestionsUpdate.push(suggestionUpdt);
					}
					if (self.hasSuggestionValue(vocabularyOrig.caseCode.id, vocabularyOrig.wordCode.id) && vocabularyOrig.suggestion) {
						suggestionsDelete.push(vocabularyOrig.suggestion);
					}
					if (angular.toJson(vocabularyOrig.caseCode) != angular.toJson(vocabularyNew.caseCode) || angular.toJson(vocabularyOrig.wordCode) != angular.toJson(vocabularyNew.wordCode)) {
						vocabularyNew.key.caseCodeAttribute = vocabularyNew.caseCode.id;
						vocabularyNew.wordCodeAttribute = vocabularyNew.wordCode.id;
						vocabulariesUpdate.push(vocabularyNew);
					}
				}
			}
		}

		/**
		 * User handle checked/unchecked on checkbox each record data.
		 * @param vocabulary
		 */
		self.changeSelected = function (vocabulary) {
			if(vocabulary.selected == true){
				var vocabularyCopy = angular.copy(vocabulary);
				delete  vocabularyCopy.selected;
				self.vocabularyListSelected.push(vocabularyCopy);
			}else{
				var index = self.findVocabularySelected(vocabulary);
				if(index != -1){
					self.vocabularyListSelected.splice(index,1);
				}
			}
			self.refreshCheckAllFlag();
		};

		/**
		 * Refresh flag check all in header grid. This flag show to user know, all row in current page has been
		 * selected.
		 */
		self.refreshCheckAllFlag = function () {
			if(self.vocabularyListSelected == null || self.vocabularyListSelected.length == 0){
				self.checkAllFlag = false;
			}else{
				var checkAll = true;
				if(self.data != null && self.data.length > 0){
					for(var i=0;i<self.data.length;i++) {
						if(self.findVocabularySelected(self.data[i]) == -1){
							checkAll = false;
							break;
						}
					}
				}
				self.checkAllFlag = checkAll;
			}
		};

		/**
		 * Check a vocabulary is selected.
		 * @param vocabulary
		 * @returns {number}
		 */
		self.findVocabularySelected = function (vocabulary) {
			var index = -1;
			if(self.vocabularyListSelected != null && self.vocabularyListSelected.length > 0) {
				for (var i = 0; i < self.vocabularyListSelected.length; i++) {
					var vocabularyIndex = self.vocabularyListSelected[i];
					if(vocabularyIndex.key.wordText == vocabulary.key.wordText
						&& vocabularyIndex.key.caseCodeAttribute == vocabulary.key.caseCodeAttribute
						&& vocabularyIndex.wordCodeAttribute == vocabulary.wordCodeAttribute){
						index = i;
						break;
					}
				}
			}
			return index;
		};

		/**
		 * User checked/unchecked in checkbox on header data tables.
		 */
		self.checkAllHandle = function () {
			if(self.checkAllFlag  ==  true){
				if(self.data != null && self.data.length > 0){
					for(var i=0;i<self.data.length;i++) {
						self.data[i]["selected"] = true;
						var index = self.findVocabularySelected(self.data[i]);
						if(index == -1) {
							var vocabularyCopy = angular.copy(self.data[i]);
							delete  vocabularyCopy.selected;
							self.vocabularyListSelected.push(vocabularyCopy);
						}
					}
				}
			}else{
				if(self.data != null && self.data.length > 0){
					for(var i=0;i<self.data.length;i++) {
						self.data[i]["selected"] = false;
						var index = self.findVocabularySelected(self.data[i]);
						if(index != -1){
							self.vocabularyListSelected.splice(index, 1);
						}
					}
				}
			}
		};
	}
})();
