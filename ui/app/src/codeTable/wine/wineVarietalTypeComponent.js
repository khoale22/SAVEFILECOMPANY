/*
 *
 * WineVarietalTypeComponent.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 * @author vn87351
 * @since 2.12.0
 */
'use strict';

/**
 * The controller for the Wine Varietal Type Controller.
 * @author vn87351
 * @since 2.12.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('wineVarietalTypeComponent', {
		templateUrl: 'src/codeTable/wine/wineVarietalType.html',
		bindings: {seleted: '<'},
		controller: WineVarietalTypeController
	});

	WineVarietalTypeController.$inject = ['codeTableApi', '$rootScope', '$scope', 'ngTableParams', '$filter'];
	/**
	 * Constructs the controller.
	 */
	function WineVarietalTypeController(codeTableApi, $rootScope, $scope, ngTableParams, $filter) {

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * message susscess from api
		 */
		self.success = '';
		/**
		 * message error form api
		 */
		self.error = '';

		/**
		 * Action.
		 * @type {string}
		 */
		self.DELETE_ACTION = 'DELETE';
		self.ADD_ACTION = 'ADD';
		self.EDIT_ACTION = 'EDIT';

		/**
		 * Init flag.
		 * @type {string}
		 */
		self.messageConfirm = '';
		self.varietalTypeToDelete = null;
		self.hasOtherRowEditing = false;
		self.selectedRowId = null;
		self.selectedRowIndex = -1;
		self.selectedRow = null;
		self.RETURN_TAB = 'returnTab';
		self.isReturnToTab = false;
		self.confirmMessage = '';
		self.MESSAGE_CONFIRM_DELETE = "Are you sure you want to delete the selected Varietal Type ?";
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.VALIDATE_VARIETAL_TYPE = 'validateVarietalType';

		/**
		 * list data varietal type
		 * @type {Array}
		 */
		self.varietalTypes = [];

		self.varietalTypesHandle = null;

		/**
		 * Initiates the construction of the Wine Varietal Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.findAllVarietalType();
		};
		/**
		 * get varietal type
		 */
		self.findAllVarietalType = function () {
			codeTableApi.getAllVarietalType().$promise.then(function (res) {
				self.varietalTypes = res;
				self.loadData();
				self.isWaiting = false;
			});
		};

		/**
		 * Load data and show on ui
		 */
		self.loadData = function () {
			$scope.tableParams = new ngTableParams({
				page: 1,
				count: 20
			}, {
				counts: [],
				debugMode: true,
				data: self.initDataForNgTable()
			});
		};

		/**
		 * processing data has returned from api
		 * @returns {Array}
		 */
		self.initDataForNgTable = function(){
			angular.forEach(self.varietalTypes,function (varietalType) {
				varietalType.varietalTypeCode = varietalType.varietalTypeCode;
				varietalType.varietalTypeAbbreviations = varietalType.varietalTypeAbbreviations.trim();
				varietalType.varietalTypeDescription = varietalType.varietalTypeDescription.trim();
				varietalType.isEditing = false;
			});
			return self.varietalTypes;
		};


		/**
		 * Select record to edit and enable it.
		 *
		 * @param varietalType
		 */
		self.enableRow = function(varietalType){
			self.error = '';
			self.success = '';
			varietalType.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = varietalType.varietalTypeCode;
			self.selectedRowIndex = self.varietalTypes.indexOf(varietalType);
			self.selectedRow = angular.copy(varietalType);
			$scope.tableParams.reload();
		};

		/**
		 * reset data of record
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.varietalTypes[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.varietalTypes[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			$scope.tableParams.reload();
		};



		/**
		 * call api to update varietal type
		 * @param varietalType
		 */
		self.saveData = function(varietalType){
			$('#confirmationModalSave').modal("hide");
			if (self.hasDataChanged(varietalType)){
				if (self.validationBeforeUpdate(varietalType)){
					self.callApiToSave(self.createDataForRequestApi(varietalType), self.EDIT_ACTION);
				}

			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Create list data for update
		 * @param varietalType
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(varietalType){
			var varietalTypes = [];
			var varietalTypeForRequest = {
				varietalTypeCode: varietalType.varietalTypeCode,
				varietalTypeAbbreviations: varietalType.varietalTypeAbbreviations,
				varietalTypeDescription: varietalType.varietalTypeDescription,
			};
			varietalTypes.push(varietalTypeForRequest);
			return varietalTypes;
		};

		/**
		 * Check if has changed data
		 * @param varietalType
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(varietalType){
			if (varietalType !== null && varietalType !== undefined) {
				var varietalTypeTemp = angular.copy(varietalType);
				var varietalTypeSelectedTemp = angular.copy(self.selectedRow);
				delete varietalTypeTemp.isEditing;
				delete varietalTypeSelectedTemp.isEditing;
				return !(JSON.stringify(varietalTypeTemp) === JSON.stringify(varietalTypeSelectedTemp))
			}
			return false;
		};

		/**
		 * Validation data before save
		 * @param varietalType
		 * @returns {boolean}
		 */
		self.validationBeforeUpdate = function (varietalType) {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			if (self.isNullOrEmpty(varietalType.varietalTypeAbbreviations)) {
				self.error += '<li>Varietal Type Abbreviations is mandatory field.</li>';
			}
			if (self.isNullOrEmpty(varietalType.varietalTypeDescription)) {
				self.error += '<li>Varietal Type Description is mandatory field.</li>';
			}
			if (self.error !== ''){
				self.error = 'Varietal Type:' + self.error;
				return false;
			}
			return true;
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === null || !object || object === "";
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_VARIETAL_TYPE, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findVarietalTypeById(self.selectedRowId))) {
				self.isReturnToTab = true;
				self.confirmTitle = 'Confirmation';
				self.confirmMessage = self.MESSAGE_CONFIRM_CLOSE;
				self.error = '';
				self.success = '';
				$('#confirmationModalSave').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.isEditedOnPreviousTab = true;
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};


		/**
		 * find varietal Type by varietal Type Code
		 * @param varietalTypeCode
		 * @returns {*}
		 */
		self.findVarietalTypeById = function(varietalTypeCode){
			for(var i = 0; i < self.varietalTypes.length; i++) {
				if (self.varietalTypes[i].varietalTypeCode === varietalTypeCode){
					return self.varietalTypes[i];
				}
			}
		};

		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style', '');
			$('#confirmationModalSave').modal('hide');
		};


		/**
		 * Show Popup to add new varietal type
		 */
		self.addVarietalType = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.resetCSS();
			self.varietalTypesHandle = [];
			var varietalType = self.createVarietalTypeEmpty();
			self.varietalTypesHandle.push(varietalType);
			self.varietalTypesHandleOrig = angular.copy(self.varietalTypesHandle);
			$('#varietalTypeModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Add one more row to add varietal Type
		 */
		self.addMoreRow = function () {
			self.errorPopup = '';
			if (self.validationBeforeAdd()) {
				var varietalType = self.createVarietalTypeEmpty();
				self.varietalTypesHandle.push(varietalType);
			}
		};

		/**
		 * Create a empty varietal type object.
		 * @returns {{}}
		 */
		self.createVarietalTypeEmpty = function () {
			var varietalType = {};
			varietalType["varietalTypeCode"] = null;
			varietalType["varietalTypeAbbreviations"] = "";
			varietalType["varietalTypeDescription"] = "";
			return varietalType;
		};

		/**
		 * Check all field is valid before add new or update varietal type.
		 * @returns {boolean}
		 */
		self.validationBeforeAdd = function () {
			var messageContent = '';
			for (var i = 0, length = self.varietalTypesHandle.length ; i < length; i++) {
				var varietalType = self.varietalTypesHandle[i];
				if (self.isNullOrEmpty(varietalType.varietalTypeAbbreviations)) {
					varietalType.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Varietal Type Abbreviations is mandatory field.</li>';
				}
				if (self.isNullOrEmpty(varietalType.varietalTypeDescription)) {
					varietalType.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Varietal Type Description is mandatory field.</li>';
				}
				if (messageContent !== '') {
					self.errorPopup = "Varietal Type:" + messageContent;
					return false;
				}
			}
			return true;
		};

		/**
		 * Close modal popup or show confirmation popup when click close button
		 */
		self.closeAddPopup = function () {
			if (self.hasDataToAdd()) {
				$('#confirmationModalSave').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else {
				$('#varietalTypeModal').modal("hide");
			}
		};

		self.hasDataToAdd = function () {
			for(var i=0, length = self.varietalTypesHandle.length; i < length; i++){
				if (!self.isNullOrEmpty(self.varietalTypesHandle[i].varietalTypeAbbreviations)
					|| !self.isNullOrEmpty(self.varietalTypesHandle[i].varietalTypeDescription)){
					return true;
				}
			}
			return false;
		};


		/**
		 * call api to save data.
		 * @param varietalType.
		 * @param action
		 */
		self.callApiToSave = function(varietalType, action){
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			if(action === self.EDIT_ACTION){
				codeTableApi.updateVarietalType(
					varietalType,
					function (results) {
						self.callApiSuccess(results);
						self.success = results.message;
						if (self.isReturnToTab) {
							$rootScope.success = self.success;
							$rootScope.isEditedOnPreviousTab = true;
						}
						self.returnToTab();
					},
					function (error) {
						self.fetchError(error);
					});
			} else if (action === self.DELETE_ACTION){
				codeTableApi.deleteVarietalType(
					varietalType,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			} else if(action === self.ADD_ACTION){
				codeTableApi.addNewVarietalType(
					varietalType,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			}
		};

		/**
		 * Processing if call api success
		 * @param results
		 */
		self.callApiSuccess = function(results){
			self.isWaiting = false;
			self.varietalTypes = results.data;
			self.success = results.message;
			self.loadData();
			self.resetAllFlag();
		};

		/**
		 *If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message != null && error.data.message != "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Call api to add varietal type.
		 */
		self.doAdd = function () {
			$('#confirmationModalSave').modal("hide");
			$('.modal-backdrop').attr('style', '');
			if (self.validationBeforeAdd()) {
				$('#varietalTypeModal').modal("hide");
				$('#confirmationModalSave').modal("hide");
				self.isWaiting = true;
				self.callApiToSave(self.varietalTypesHandle, self.ADD_ACTION);
			}
		};

		/**
		 * show popup to confirmation delete.
		 * @param varietalType
		 */
		self.showPopupConfirmDelete = function(varietalType){
			self.error = '';
			self.success = '';
			self.varietalTypeToDelete = angular.copy(varietalType);
			self.confirmMessage = self.MESSAGE_CONFIRM_DELETE;
			$('#confirmationDelete').modal({backdrop: 'static', keyboard: true});
			$('.modal-backdrop').attr('style', ' z-index: 100000; ');
		};

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			self.varietalTypeToDelete = null;
			$('#confirmationDelete').modal("hide");
			$('#confirmationModalSave').modal("hide");
			$('#varietalTypeModal').modal('hide');
			if (self.isReturnToTab) {
				$('#confirmationModalSave').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			}
		};

		/**
		 * hide popup and call api to delete data.
		 */
		self.doDeleteData = function(varietalType){
			$('#confirmationDelete').modal("hide");
			if (self.varietalTypeToDelete !== null){
				self.callApiToSave(self.createDataForRequestApi(varietalType), self.DELETE_ACTION);
			}
		};

		/**
		 * Create list of varietal type to request api.
		 * @param varietalType
		 * @returns {Array}
		 */
		self.createVarietalTypeList = function(varietalType){
			var varietalTypeList = [];
			varietalTypeList.push(varietalType);
			return varietalTypeList;
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.varietalTypeToDelete = null;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 *
		 * @param varietalTypeCode
		 * @returns {*}
		 */
		self.getButtonStyle = function(varietalTypeCode){
			if (self.selectedRowId !== null && varietalTypeCode !== self.selectedRowId){
				return 'opacity: 0.5;'
			}
			return 'opacity: 1.0;';
		};

		/**
		 * Reset validation.
		 */
		self.resetValidation = function(){
			$scope.addForm.$setPristine();
			$scope.addForm.$setUntouched();
			$scope.addForm.$rollbackViewValue();
		};

		/**
		 * remove property of object product category has used for setting style
		 */
		self.resetCSS = function(){
			angular.forEach(self.varietalsHandle, function (value) {
				if(value.addClass){
					delete value.addClass;
				}
			});
		};
	}
})();
