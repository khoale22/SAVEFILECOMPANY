/*
 * countryCodeComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * Component to support the page that allows users to show brand Cost Owner T2T.
 *
 * @author vn70529
 * @since 2.12
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('countryCodeComponent', {
		templateUrl: 'src/codeTable/countryCode/countryCode.html',
		bindings: {
			seleted: '<'
		},
		controller: countryCodeController
	});

	app.filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});

	countryCodeController.$inject = ['$rootScope', '$scope', 'ngTableParams', '$filter', "ngTableEventsChannel", '$sce', 'CountryCodeApi'];
	/**
	 * Constructs for country code Controller.
	 *
	 * @param ngTableParams
	 * @param CountryCodeApi
	 */
	function countryCodeController($rootScope, $scope, ngTableParams, $filter, ngTableEventsChannel, $sce, countryCodeApi) {

		var self = this;
		/**
		 * Messages
		 */
		self.COUNTRY_CODE_DELETE_MESSAGE_HEADER = 'Delete Country Code';
		self.COUNTRY_CODE_LIMIT_DELETE_CONFIRM_MESSAGE_STRING = 'Deleting Country Codes is limited to 15 records at a time.';
		self.COUNTRY_CODE_DELETE_CONFIRM_MESSAGE_STRING = 'Are you sure you want to delete the selected Country Code?';
		self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING = 'Unsaved data will be lost. Do you want to save the changes before continuing?';
		/**
		 * Error Message
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = 'An unknown error occurred.';
		self.COUNTRY_ABBR_MANDATORY_FIELD_ERROR = 'Country Abbr. is a mandatory field.';
		self.COUNTRY_ABBR_MUST_BE_2_CHARS_ERROR = 'Country Abbr. must be 2 characters.';
		self.COUNTRY_MANDATORY_FIELD_ERROR = 'Country is a mandatory field.';
		self.ISO_ALPHA_MANDATORY_FIELD_ERROR = 'ISO Alpha (A3) is a mandatory field.';
		self.ISO_ALPHA_MUST_BE_3_CHARS_ERROR = 'ISO Alpha (A3) must be 3 characters.';
		self.NUMBERIC_N3_MANDATORY_FIELD_ERROR = 'Numeric (N3) Code is a mandatory field.';
		self.NUMBERIC_N3_MUST_BE_GREATER_THAN_0_AND_LESS_THAN_100_ERROR = 'Numeric (N3) Code value must be greater than 0 and less than or equal to 999.';
		/**
		 * Start position of page that want to show on country code table
		 *
		 * @type {number}
		 */
		self.PAGE = 1;
		/**
		 * The number of records to show on the  country code table.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;
		/**
		 * Add action code.
		 * @type {string}
		 */
		self.ADD_ACTION = 'ADD';
		/**
		 * Edit action code.
		 * @type {string}
		 */
		self.EDIT_ACTION = 'EDIT';
		/**
		 * Holds the total of rows to show scroll on add/edit
		 * @type {number}
		 */
		self.TOTAL_ROWS_TO_SHOW_SCROLL = 15;
		/**
		 * Validattion country code key.
		 * @type {string}
		 */
		self.VALIDATE_COUNTRY_CODE = 'validateCountryCode';
		/**
		 * Return tab key.
		 * @type {string}
		 */
		self.RETURN_TAB = 'returnTab';
		/**
		 * Empty model.
		 * @type {{countryAbbreviation: string, countryName: string, countIsoA3Cod: string, countIsoN3Cd: string, countryId: number}}
		 */
		self.EMPTY_MODEL = {
			countryAbbreviation: '',
			countryName: '',
			countIsoA3Cod: '',
			countIsoN3Cd: '',
			countryId: 0
		};
		self.isReturnToTab = false;
		/**
		 * Holds the list of country codes.
		 *
		 * @type {Array} the list of country codes.
		 */
		self.countryCodes = [];
		/**
		 * Selected edit country code.
		 * @type {null}
		 */
		self.selectedCountryCode = null;
		/**
		 * Selected edited row index.
		 * @type {null}
		 */
		self.selectedRowIndex = -1;
		/**
		 * Validation model.
		 */
		self.validationModel = angular.copy(self.EMPTY_MODEL);
		/**
		 * Initialize the controller.
		 */
		self.init = function () {
			self.loadCountryCodes();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		}
		/**
		 * Initial the table to show list of country codes.
		 */
		self.loadCountryCodes = function () {
			self.isWaitingForResponse = true;
			countryCodeApi.getCountryCodes(function (response) {
				self.isWaitingForResponse = false;
				self.setCountryCodes(response);
			}, function (error) {
				self.fetchError(error);
			});
		};
		/**
		 * Initial country codes table.
		 */
		self.initCountryCodesTable = function () {
			$scope.filter = {
				countryAbbreviation: undefined,
				displayNameOnGrid: undefined,
				displayCountIsoOnGrid: undefined
			};
			$scope.tableParams = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: self.PAGE_SIZE, /* count per page */
				filter: $scope.filter
			}, {
				counts: [],
				debugMode: false,
				data: self.countryCodes
			});
			/* Handles paging*/
			self.selectedTableEvents = [];
			var logPagesChangedEvent = _.partial(function (list, name) {
				if (self.selectedCountryCode !== null) {
					if (self.selectedCountryCode.countryAbbreviation === undefined) {
						self.selectedCountryCode.countryAbbreviation = self.validationModel.countryAbbreviation;
					}
					if (self.selectedCountryCode.countryName === undefined) {
						self.selectedCountryCode.countryName = self.validationModel.countryName;
					}
					if (self.selectedCountryCode.countIsoA3Cod === undefined) {
						self.selectedCountryCode.countIsoA3Cod = self.validationModel.countIsoA3Cod;
					}
					if (self.selectedCountryCode.countIsoN3Cd === undefined) {
						self.selectedCountryCode.countIsoN3Cd = self.validationModel.countIsoN3Cd;
					}
					if (self.selectedCountryCode.countryId === undefined) {
						self.selectedCountryCode.countryId = self.validationModel.countryId;
					}
				}
			}, self.selectedTableEvents, "pagesChanged");
			ngTableEventsChannel.onPagesChanged(logPagesChangedEvent, $scope.tableParams);
		}
		/**
		 * Initial country code table for add edit mode.
		 */
		self.initCountryCodesTableForAddMode = function(){
			$scope.tableParamsForAddEditMode = new ngTableParams({
				page: self.PAGE, /* show first page */
				count: 100000000, /* count per page */
			}, {
				counts: [],
				debugMode: false,
				data: self.countryCodesHandle
			});
		}
		/**
		 * Add new countryCode handle when add new button click.
		 */
		self.addNewCountryCode = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			$('#addEditContainer').attr('style', '');
			self.resetValidation();
			self.countryCodesHandle = [];
			var countryCode = angular.copy(self.EMPTY_MODEL);
			self.countryCodesHandle.push(countryCode);
			self.countryCodesHandleOrig = angular.copy(self.countryCodesHandle);
			self.titleModel = self.ADD_NEW_TITLE_HEADER;
			self.action = self.ADD_ACTION;
			self.initCountryCodesTableForAddMode();
			$('#countryCodeModal').modal({backdrop: 'static', keyboard: true});
		};
		/**
		 * Reset validation.
		 */
		self.resetValidation = function(){
			$scope.addEditForm.$setPristine();
			$scope.addEditForm.$setUntouched();
			$scope.addEditForm.$rollbackViewValue();
		};
		/**
		 * Resets tooltip on add/edit form.
		 */
		self.resetTooltips = function(){
			for (var i = 0; i < self.countryCodesHandle.length; i++) {
				$('#countryAbbreviation'+i).attr('title', "");
				$('#countryName'+i).attr('title', "");
				$('#countIsoA3Cod'+i).attr('title', "");
				$('#countIsoN3Cd'+i).attr('title', "");
			}
		}
		/**
		 * Add one more country Code row.
		 */
		self.addMoreRowCountryCode = function () {
			if(self.validateCountryCodeBeforeInsert()) {
				var countryCode = angular.copy(self.EMPTY_MODEL);
				self.countryCodesHandle.push(countryCode);
				self.initCountryCodesTableForAddMode();
				self.showScrollViewOnAddCountryCodeForm();
			}
		};
		/**
		 * Add scroll to add form when the total of rows are greater than 15 rows.
		 */
		self.showScrollViewOnAddCountryCodeForm = function(){
			if(self.countryCodesHandle.length > self.TOTAL_ROWS_TO_SHOW_SCROLL){
				$('#addEditContainer').attr('style', 'overflow-y: auto;height:500px');
				setTimeout(function(){
					var element = document.getElementById('addEditContainer');
					element.scrollTop = element.scrollHeight - element.clientHeight;
				},200);
			}else{
				$('#addEditContainer').attr('style', '');
			}
		};
		/**
		 * Edit Country Code handle. This method is called when click on edit button.
		 */
		self.editCountryCode = function (countryCode) {
			if (self.selectedRowIndex === -1) {
				self.action = self.EDIT_ACTION;
				self.error = '';
				self.success = '';
				countryCode.isEditing = true;
				self.validationModel = angular.copy(countryCode);
				self.selectedCountryCode = countryCode;
				self.selectedRowIndex = self.getRowIndex();
			}
		};
		/**
		 * Return edited row index.
		 *
		 * @returns {number}
		 */
		self.getRowIndex = function () {
			if (self.selectedCountryCode == null) {
				return -1;
			}
			if (self.selectedCountryCode.countryId == 0) {
				return 0;
			}
			for (var i = 0; i < self.countryCodes.length; i++) {
				if (self.countryCodes[i].countryId === self.selectedCountryCode.countryId) {
					return i;
				}
			}
		}
		/**
		 * Sets Country Codes into table modal.
		 */
		self.setCountryCodes = function (countryCodes) {
			self.countryCodes = countryCodes;
			self.countryCodesOrig = angular.copy(self.countryCodes);
			self.initCountryCodesTable();
		};
		/**
		 * Checks the countryCode is changed or not.
		 *
		 * @param countryCode the country code.
		 * @param origData the list of original countries
		 * @returns {boolean}
		 */
		self.isCountryCodeChanged = function (countryCode, origData) {
			var isChanged = false;
			var countryCodeTemp = angular.copy(countryCode);
			delete countryCodeTemp['isEditing'];
			if (countryCodeTemp.countryAbbreviation == null) {
				countryCodeTemp.countryAbbreviation = '';
			}
			if (countryCodeTemp.countryName == null) {
				countryCodeTemp.countryName = '';
			}
			if (countryCodeTemp.countIsoA3Cod == null) {
				countryCodeTemp.countIsoA3Cod = '';
			}
			if (countryCodeTemp.countIsoN3Cd == null) {
				countryCodeTemp.countIsoN3Cd = '';
			}
			for (var i = 0; i < origData.length; i++) {
				if (origData[i].countryId == countryCodeTemp.countryId) {
					if (JSON.stringify(origData[i]) !== JSON.stringify(countryCodeTemp)) {
						isChanged = true;
						break;
					}
				}
			}
			return isChanged;
		};
		/**
		 * Validate and call the api to update the list of Country Codes. Call to back end to insert to database.
		 */
		self.updateCountryCode = function () {
			self.error = '';
			self.success = '';
			if (self.selectedRowIndex > -1) {
				// editing mode.
				if (self.isCountryCodeChanged(self.selectedCountryCode, self.countryCodesOrig)) {
					if (self.validateCountryCodeBeforeUpdate()) {
						self.isWaitingForResponse = true;
						countryCodeApi.updateCountryCodes([self.selectedCountryCode],
							function (results) {
								self.resetSelectedCountryCode();
								self.isWaitingForResponse = false;
								self.checkAllFlag = false;
								self.setCountryCodes(results.data);
								self.success = results.message;
								if(self.isReturnToTab){
									$rootScope.success = self.success;
									$rootScope.isEditedOnPreviousTab = true;
								};
								self.returnToTab();
							},
							function (error) {
								self.fetchError(error);
							}
						);
					}
				} else {
					self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
				}
			} else {
				self.error = self.THERE_ARE_NO_DATA_CHANGES_MESSAGE_STRING;
			}
		};
		/**
		 * Check all field is valid before add new or update Country Code.
		 * @returns {boolean}
		 */
		self.validateCountryCodeBeforeUpdate = function () {
			var errorMessages = [];
			var message = '';
			if (self.validationModel.countryAbbreviation == null || self.validationModel.countryAbbreviation == undefined ||
				self.validationModel.countryAbbreviation.trim().length == 0) {
				message = '<li>' + self.COUNTRY_ABBR_MANDATORY_FIELD_ERROR + '</li>';
				errorMessages.push(message);
				self.showErrorOnTextBox('countryAbbreviation', self.COUNTRY_ABBR_MANDATORY_FIELD_ERROR);
			} else {
				if (self.validationModel.countryAbbreviation.trim().length == 1) {
					message = '<li>' + self.COUNTRY_ABBR_MUST_BE_2_CHARS_ERROR + '</li>';
					errorMessages.push(message);
					self.showErrorOnTextBox('countryAbbreviation', self.COUNTRY_ABBR_MUST_BE_2_CHARS_ERROR);
				}
			}
			if (self.validationModel.countryName == null || self.validationModel.countryName == undefined ||
				self.validationModel.countryName.trim().length == 0) {
				message = '<li>' + self.COUNTRY_MANDATORY_FIELD_ERROR + '</li>';
				errorMessages.push(message);
				self.showErrorOnTextBox('countryName', self.COUNTRY_MANDATORY_FIELD_ERROR);
			}
			if (self.validationModel.countIsoA3Cod == null || self.validationModel.countIsoA3Cod == undefined ||
				self.validationModel.countIsoA3Cod.trim().length == 0) {
				message = '<li>' + self.ISO_ALPHA_MANDATORY_FIELD_ERROR + '</li>';
				errorMessages.push(message);
				self.showErrorOnTextBox('countIsoA3Cod', self.ISO_ALPHA_MANDATORY_FIELD_ERROR);
			} else {
				if (self.validationModel.countIsoA3Cod.trim().length < 3) {
					message = '<li>' + self.ISO_ALPHA_MUST_BE_3_CHARS_ERROR + '</li>';
					errorMessages.push(message);
					self.showErrorOnTextBox('countIsoA3Cod', self.ISO_ALPHA_MUST_BE_3_CHARS_ERROR);
				}
			}
			if (self.validationModel.countIsoN3Cd == null || self.validationModel.countIsoN3Cd == undefined ||
				self.validationModel.countIsoN3Cd.length == 0) {
				message = '<li>' + self.NUMBERIC_N3_MANDATORY_FIELD_ERROR + '</li>';
				errorMessages.push(message);
				self.showErrorOnTextBox('countIsoN3Cd', self.NUMBERIC_N3_MANDATORY_FIELD_ERROR);
			} else {
				if (parseInt(self.validationModel.countIsoN3Cd) <= 0) {
					message = '<li>' + self.NUMBERIC_N3_MUST_BE_GREATER_THAN_0_AND_LESS_THAN_100_ERROR + '</li>';
					errorMessages.push(message);
					self.showErrorOnTextBox('countIsoN3Cd',
						self.NUMBERIC_N3_MUST_BE_GREATER_THAN_0_AND_LESS_THAN_100_ERROR);
				}
			}
			if (errorMessages.length > 0) {
				var errorMessagesAsString = 'Country Code:';
				angular.forEach(errorMessages, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				self.error = $sce.trustAsHtml('<ul style="text-align: left;">' + errorMessagesAsString + '</ul>')
				return false;
			}
			return errorMessages;
		}
		/**
		 * Delete countryCodes handle when click on delete button.
		 */
		self.deleteCountryCode = function (item) {
			self.selectedDeletedCountryCode = item;
			self.error = '';
			self.success = '';
			self.confirmHeaderTitle = self.COUNTRY_CODE_DELETE_MESSAGE_HEADER;
			self.messageConfirm = self.COUNTRY_CODE_DELETE_CONFIRM_MESSAGE_STRING;
			self.labelClose = 'No';
			self.allowDeleteCountryCode = true;
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};
		/**
		 * Do delete Country Code, call to back end to delete.
		 */
		self.doDeleteCountryCode = function () {
			$('#confirmModal').modal('hide');
			if (self.selectedDeletedCountryCode.countryId === 0) {
				self.setCountryCodes(self.countryCodes.slice(1, self.countryCodes.length));
				self.resetSelectedCountryCode();
			} else {
				self.isWaitingForResponse = true;
				countryCodeApi.deleteCountryCodes(
					[self.selectedDeletedCountryCode],
					function (results) {
						self.resetSelectedCountryCode();
						self.isWaitingForResponse = false;
						self.checkAllFlag = false;
						self.setCountryCodes(results.data);
						self.success = results.message;
					},
					function (error) {
						self.fetchError(error);
					}
				);
			}
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			self.isWaitingForResponse = false;
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			};
			self.isReturnToTab = false;
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
		/**
		 * Clear filter.
		 */
		self.clearFilter = function () {
			$scope.filter.countryAbbreviation = undefined;
			$scope.filter.displayNameOnGrid = undefined;
			$scope.filter.displayCountIsoOnGrid = undefined;
			self.error = '';
			self.success = '';
		};
		/**
		 * This method is used to close the add popup or show confirm data changes, if there are the data changes.
		 */
		self.closeAddNewPopup =  function(){
			if(self.isDataChangedForPopup() === true){
				self.isConfirmSaveCountryCode = true;
				self.allowDeleteCountryCode = false;
				self.allowCloseButton = false;
				// show popup
				self.confirmHeaderTitle='Confirmation';
				self.error = '';
				self.success = '';
				self.errorPopup = '';
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
				self.labelClose = 'No';
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style',' z-index: 100000; ');
			}else{
				$('#countryCodeModal').modal('hide');
				self.isConfirmSaveCountryCode = false;
			}
		};
		/**
		 * Check change data in add new pop up.
		 *
		 * @returns {boolean}
		 */
		self.isDataChangedForPopup = function () {
			var isChanged = false;
			var index = 0;
			angular.forEach(self.countryCodesHandle, function (countryCode) {
				if(!isChanged){
					self.synchronizeCountryModelWithInputText(countryCode, index);
					if(self.isCountryCodeChanged(countryCode, self.countryCodesHandleOrig)){
						isChanged = true;
					}
				}
				index++;
			});
			return isChanged;
		};
		/**
		 * Reset data orig for pop up handle add new, edit.
		 */
		self.resetDataOrig = function () {
			self.error = '';
			self.success = '';
			if (self.selectedCountryCode != null && self.selectedCountryCode.countryId == 0) {
				self.countryCodes = angular.copy(self.countryCodesOrig);
				self.setCountryCodes(self.countryCodes);
			} else {
				self.countryCodes[self.selectedRowIndex] = angular.copy(self.countryCodesOrig[self.selectedRowIndex]);
				$scope.tableParams.reload();
			}
			self.resetSelectedCountryCode();
		};
		/**
		 * Reset data orig for pop up handle add new, edit.
		 */
		self.resetDataOrigForPopUp = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.countryCodesHandle = angular.copy(self.countryCodesHandleOrig);
			self.initCountryCodesTableForAddEditMode();
			self.resetValidation();
			self.resetTooltips();
		};
		/**
		 * Reset the status add or edit country code.
		 */
		self.resetSelectedCountryCode = function () {
			self.selectedRowIndex = -1;
			self.selectedCountryCode = null;
			self.action = '';
		};
		/**
		 * Validate and call api to save new data. Call to back end to insert to database.
		 */
		self.saveNewData = function () {
			if (self.validateCountryCodeBeforeInsert()) {
				$('#countryCodeModal').modal('hide');
				self.isWaitingForResponse = true;
				countryCodeApi.addNewCountryCodes(
					self.countryCodesHandle,
					function (results) {
						self.resetSelectedCountryCode();
						self.isWaitingForResponse = false;
						self.checkAllFlag = false;
						self.setCountryCodes(results.data);
						self.success = results.message;
					},
					function (error) {
						self.fetchError(error);
					}
				);
			}
		};
		/**
		 * This method is used to save the data changes when user click on
		 * Yes button of confirm popup for data changes.
		 */
		self.saveDataChanged = function () {
			if(self.action == self.EDIT_ACTION) {
				// Edit
				if (self.validateCountryCodeBeforeUpdate()) {
					self.updateCountryCode();
				} else {
					self.isReturnToTab = false;
				}
			}else{
				// Add new
				self.saveNewData();
			}
			$('.modal-backdrop').attr('style','');
			self.isConfirmSaveCountryCode = false;
			$('#confirmModal').modal('hide');
		};
		/**
		 * This method is used to hide add/edit popup and confirm popup when user click on
		 */
		self.noSaveDataChanged = function () {
			if(self.action == self.EDIT_ACTION) {
				if (self.isReturnToTab) {
					$('#confirmModal').on('hidden.bs.modal', function () {
						self.returnToTab();
						$scope.$apply();
					});
				} else {
					self.countryCodes[self.selectedRowIndex] = angular.copy(self.countryCodesOrig[self.selectedRowIndex]);
					$scope.tableParams.reload();
					self.resetSelectedCountryCode();
				}
			}
			self.isConfirmSaveCountryCode = false;
			$('.modal-backdrop').attr('style','');
			$("#confirmModal").modal('hide');
			$('#countryCodeModal').modal('hide');
		};
		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style','');
			self.isReturnToTab = false;
			$('#confirmModal').modal('hide');
			self.isConfirmSaveCountryCode = false;
		};
		/**
		 * Show red border on input text.
		 *
		 * @param id if of input text.
		 */
		self.showErrorOnTextBox = function (id, message) {
			if ($('#' + id).length > 0) {
				$('#' + id).addClass('ng-invalid ng-touched');
				$('#' + id).attr('title', message);
			}
		};
		/**
		 * This method is used to synchronize country model with add/edit form
		 * when user enter the data on input text is invalid.
		 * @param country
		 */
		self.synchronizeCountryModelWithInputText = function(country, index){
			var inputTextId = index.toString();
			var countryAbbreviationInputValue = $('#countryAbbreviation'+inputTextId).val();
			if(countryAbbreviationInputValue != null){
				country.countryAbbreviation = countryAbbreviationInputValue.trim();
			}
			var countryNameInputValue = $('#countryName'+inputTextId).val();
			if(countryNameInputValue != null){
				country.countryName = countryNameInputValue.trim();
			}
			var countIsoA3CodInputValue = $('#countIsoA3Cod'+inputTextId).val();
			if(countIsoA3CodInputValue != null){
				country.countIsoA3Cod = countIsoA3CodInputValue.trim();
			}
			var countIsoN3CdInputValue = $('#countIsoN3Cd'+inputTextId).val();
			if(countIsoN3CdInputValue != null){
				if(countIsoN3CdInputValue.length>0){
					country.countIsoN3Cd = parseInt(countIsoN3CdInputValue);
				}
			}
		};
		/**
		 * Returns the display status for unedit button.
		 *
		 * @param country the selected country code.
		 * @returns {boolean} true the unedit button will be showed.
		 */
		self.isShowingUneditButton = function (country) {
			if (country.countryId == 0) {
				return false;
			}
			return country.isEditing;
		};
		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		}
		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_COUNTRY_CODE, function () {
			if (self.selectedCountryCode != null && self.isCountryCodeChanged(self.selectedCountryCode, self.countryCodesOrig)) {
				self.isReturnToTab = true;
				self.isConfirmSaveCountryCode = true;
				self.allowDeleteCountryCode = false;
				self.allowCloseButton = false;
				// show popup
				self.confirmHeaderTitle = 'Confirmation';
				self.error = '';
				self.success = '';
				self.messageConfirm = self.UNSAVED_DATA_CONFIRM_MESSAGE_STRING;
				self.labelClose = 'No';
				$('#confirmModal').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});
		/**
		 * This method is used to check invalid the data on textbox when paging.
		 *
		 * @param fieldName
		 */
		self.onChanged = function (fieldName) {
			var value = null;
			if (fieldName === 'countryAbbreviation') {
				value = $('#countryAbbreviation').val();
				if (value == null || value == undefined ||
					value.trim().length == 0) {
					self.showErrorOnTextBox('countryAbbreviation', self.COUNTRY_ABBR_MANDATORY_FIELD_ERROR);
				} else {
					if (value.trim().length == 1) {
						self.showErrorOnTextBox('countryAbbreviation', self.COUNTRY_ABBR_MUST_BE_2_CHARS_ERROR);
					}
				}
			} else if (fieldName === 'countryName') {
				value = $('#countryName').val();
				if (value == null || value == undefined ||
					value.trim().length == 0) {
					self.showErrorOnTextBox('countryName', self.COUNTRY_MANDATORY_FIELD_ERROR);
				}
			} else if (fieldName === 'countIsoA3Cod') {
				value = $('#countIsoA3Cod').val();
				if (value == null || value == undefined ||
					value.trim().length == 0) {
					self.showErrorOnTextBox('countIsoA3Cod', self.ISO_ALPHA_MANDATORY_FIELD_ERROR);
				} else {
					if (value.trim().length < 3) {
						self.showErrorOnTextBox('countIsoA3Cod', self.ISO_ALPHA_MUST_BE_3_CHARS_ERROR);
					}
				}
			} else if (fieldName === 'countIsoN3Cd') {
				value = $('#countIsoN3Cd').val();
				if (value == null || value == undefined ||
					value.length == 0) {
					self.showErrorOnTextBox('countIsoN3Cd', self.NUMBERIC_N3_MANDATORY_FIELD_ERROR);
				} else {
					if (parseInt(value) <= 0) {
						self.showErrorOnTextBox('countIsoN3Cd',
							self.NUMBERIC_N3_MUST_BE_GREATER_THAN_0_AND_LESS_THAN_100_ERROR);
					}
				}
			}
		};
		/**
		 * Check all field is valid before add new.
		 * @returns {boolean}
		 */
		self.validateCountryCodeBeforeInsert = function () {
			var errorMessages = [];
			var message = '';
			var countryCode = null;
			for (var i = 0; i < self.countryCodesHandle.length; i++) {
				countryCode = self.countryCodesHandle[i];
				if ($('#countryAbbreviation' + i.toString()).val() == null ||
					$('#countryAbbreviation' + i.toString()).val().trim().length == 0) {
					message = '<li>' + self.COUNTRY_ABBR_MANDATORY_FIELD_ERROR + '</li>';
					if (errorMessages.indexOf(message) == -1) {
						errorMessages.push(message);
					}
					self.showErrorOnTextBox('countryAbbreviation' + i.toString(), self.COUNTRY_ABBR_MANDATORY_FIELD_ERROR);
				} else {
					if ($('#countryAbbreviation' + i.toString()).val().trim().length == 1) {
						message = '<li>' + self.COUNTRY_ABBR_MUST_BE_2_CHARS_ERROR + '</li>';
						if (errorMessages.indexOf(message) == -1) {
							errorMessages.push(message);
						}
						self.showErrorOnTextBox('countryAbbreviation' + i.toString(), self.COUNTRY_ABBR_MUST_BE_2_CHARS_ERROR);
					}
				}
				if ($('#countryName' + i.toString()).val() == null ||
					$('#countryName' + i.toString()).val().trim().length == 0) {
					message = '<li>' + self.COUNTRY_MANDATORY_FIELD_ERROR + '</li>';
					if (errorMessages.indexOf(message) == -1) {
						errorMessages.push(message);
					}
					self.showErrorOnTextBox('countryName' + i.toString(), self.COUNTRY_MANDATORY_FIELD_ERROR);
				}
				if ($('#countIsoA3Cod' + i.toString()).val() == null ||
					$('#countIsoA3Cod' + i.toString()).val().trim().length == 0) {
					message = '<li>' + self.ISO_ALPHA_MANDATORY_FIELD_ERROR + '</li>';
					if (errorMessages.indexOf(message) == -1) {
						errorMessages.push(message);
					}
					self.showErrorOnTextBox('countIsoA3Cod' + i.toString(), self.ISO_ALPHA_MANDATORY_FIELD_ERROR);
				} else {
					if ($('#countIsoA3Cod' + i.toString()).val().trim().length < 3) {
						message = '<li>' + self.ISO_ALPHA_MUST_BE_3_CHARS_ERROR + '</li>';
						if (errorMessages.indexOf(message) == -1) {
							errorMessages.push(message);
						}
						self.showErrorOnTextBox('countIsoA3Cod' + i.toString(), self.ISO_ALPHA_MUST_BE_3_CHARS_ERROR);
					}
				}
				if ($('#countIsoN3Cd' + i.toString()).val() == null ||
					$('#countIsoN3Cd' + i.toString()).val().length == 0) {
					message = '<li>' + self.NUMBERIC_N3_MANDATORY_FIELD_ERROR + '</li>';
					if (errorMessages.indexOf(message) == -1) {
						errorMessages.push(message);
					}
					self.showErrorOnTextBox('countIsoN3Cd' + i.toString(), self.NUMBERIC_N3_MANDATORY_FIELD_ERROR);
				} else {
					if (parseInt($('#countIsoN3Cd' + i.toString()).val()) <= 0) {
						message = '<li>' + self.NUMBERIC_N3_MUST_BE_GREATER_THAN_0_AND_LESS_THAN_100_ERROR + '</li>';
						if (errorMessages.indexOf(message) == -1) {
							errorMessages.push(message);
						}
						self.showErrorOnTextBox('countIsoN3Cd' + i.toString(),
							self.NUMBERIC_N3_MUST_BE_GREATER_THAN_0_AND_LESS_THAN_100_ERROR);
					}
				}
			}
			if (errorMessages.length > 0) {
				var errorMessagesAsString = 'Country Code:';
				angular.forEach(errorMessages, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				self.errorPopup = errorMessagesAsString;
				return false;
			}
			return true;
		};
		/**
		 * Returns the disabled status of button by country code id.
		 *
		 * @param id the country code id.
		 * @returns {boolean} the disable status.
		 */
		self.isDisabledButton = function (id) {
			if (self.selectedRowIndex == -1 || self.selectedCountryCode.countryId == id) {
				return false;
			}
			return true;
		};
		/**
		 * Returns the style for icon button.
		 *
		 * @param id the id of country code.
		 * @returns {*} the style.
		 */
		self.getDisabledButtonStyle = function (id) {
			if (self.isDisabledButton(id)) {
				return 'opacity: 0.5;'
			}

			return 'opacity: 1.0;';
		}
	}
})();
