/*
 *
 * WineAreaComponent.js
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
 * The controller for the Wine Area Controller.
 * @author vn87351
 * @since 2.12.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('wineAreaComponent', {
		templateUrl: 'src/codeTable/wine/wineArea.html',
		bindings: {seleted: '<'},
		controller: WineAreaController
	});

	WineAreaController.$inject = ['codeTableApi', '$rootScope', '$scope', 'ngTableParams', '$filter'];
	/**
	 * Constructs the controller.
	 */
	function WineAreaController(codeTableApi, $rootScope, $scope, ngTableParams, $filter) {

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
		self.wineAreaToDelete = null;
		self.hasOtherRowEditing = false;
		self.selectedRowId = null;
		self.selectedRowIndex = -1;
		self.selectedRow = null;
		self.RETURN_TAB = 'returnTab';
		self.isReturnToTab = false;
		self.confirmMessage = '';
		self.MESSAGE_CONFIRM_DELETE = "Are you sure you want to delete the selected Wine Area ?";
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.VALIDATE_WINE_AREA = 'validateWineArea';

		/**
		 * list data Wine Area
		 * @type {Array}
		 */
		self.wineAreas = [];

		self.wineAreasHandle = null;

		/**
		 * Initiates the construction of the Wine Area Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.findAllWineArea();
		};
		/**
		 * get Wine Area
		 */
		self.findAllWineArea = function () {
			codeTableApi.getAllWineArea().$promise.then(function (res) {
				self.wineAreas = res;
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
			angular.forEach(self.wineAreas,function (wineArea) {
				wineArea.wineAreaName = wineArea.wineAreaName.trim();
				wineArea.wineAreaDescription = wineArea.wineAreaDescription.trim();
				wineArea.isEditing = false;
			});
			return self.wineAreas;
		};


		/**
		 * Select record to edit and enable it.
		 *
		 * @param wineArea
		 */
		self.enableRow = function(wineArea){
			self.error = '';
			self.success = '';
			wineArea.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = wineArea.wineAreaId;
			self.selectedRowIndex = self.wineAreas.indexOf(wineArea);
			self.selectedRow = angular.copy(wineArea);
			$scope.tableParams.reload();
		};

		/**
		 * reset data of record
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.wineAreas[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.wineAreas[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			$scope.tableParams.reload();
		};



		/**
		 * call api to update Wine Area
		 * @param wineArea
		 */
		self.saveData = function(wineArea){
			$('#confirmationModalSave').modal("hide");
			if (self.hasDataChanged(wineArea)){
				if (self.validationBeforeUpdate(wineArea)){
					self.callApiToSave(self.createDataForRequestApi(wineArea), self.EDIT_ACTION);
				}

			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Create list data for update
		 * @param wineArea
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(wineArea){
			var wineAreas = [];
			var wineAreaForRequest = {
				wineAreaId: wineArea.wineAreaId,
				wineAreaName: wineArea.wineAreaName,
				wineAreaDescription: wineArea.wineAreaDescription,
			};
			wineAreas.push(wineAreaForRequest);
			return wineAreas;
		};

		/**
		 * Check if has changed data
		 * @param wineArea
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(wineArea){
			if (wineArea !== null && wineArea !== undefined) {
				var wineAreaTemp = angular.copy(wineArea);
				var wineAreaSelectedTemp = angular.copy(self.selectedRow);
				delete wineAreaTemp.isEditing;
				delete wineAreaSelectedTemp.isEditing;
				return !(JSON.stringify(wineAreaTemp) === JSON.stringify(wineAreaSelectedTemp))
			}
			return false;
		};

		/**
		 * Validation data before save
		 * @param wineArea
		 * @returns {boolean}
		 */
		self.validationBeforeUpdate = function (wineArea) {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			if (self.isNullOrEmpty(wineArea.wineAreaName)) {
				self.error += '<li>Wine Area Name is mandatory field.</li>';
			}
			if (self.isNullOrEmpty(wineArea.wineAreaDescription)) {
				self.error += '<li>Wine Area Description is mandatory field.</li>';
			}
			if (self.error !== ''){
				self.error = 'Wine Area:' + self.error;
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
		$scope.$on(self.VALIDATE_WINE_AREA, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findWineAreaById(self.selectedRowId))) {
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
		 * find Wine Area by Wine Area Id
		 * @param wineAreaId
		 * @returns {*}
		 */
		self.findWineAreaById = function(wineAreaId){
			for(var i = 0; i < self.wineAreas.length; i++) {
				if (self.wineAreas[i].wineAreaId === wineAreaId){
					return self.wineAreas[i];
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
		 * Show Popup to add new Wine Area
		 */
		self.addWineArea = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.resetCSS();
			self.wineAreasHandle = [];
			var wineArea = self.createWineAreaEmpty();
			self.wineAreasHandle.push(wineArea);
			self.wineAreasHandleOrig = angular.copy(self.wineAreasHandle);
			$('#wineAreaModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Add one more row to add Wine Area
		 */
		self.addMoreRow = function () {
			self.errorPopup = '';
			if (self.validationBeforeAdd()) {
				var wineArea = self.createWineAreaEmpty();
				self.wineAreasHandle.push(wineArea);
			}
		};

		/**
		 * Create a empty Wine Area object.
		 * @returns {{}}
		 */
		self.createWineAreaEmpty = function () {
			var wineArea = {};
			wineArea["wineAreaId"] = null;
			wineArea["wineAreaName"] = "";
			wineArea["wineAreaDescription"] = "";
			return wineArea;
		};

		/**
		 * Check all field is valid before add new or update Wine Area.
		 * @returns {boolean}
		 */
		self.validationBeforeAdd = function () {
			var messageContent = '';
			for (var i = 0, length = self.wineAreasHandle.length ; i < length; i++) {
				var wineArea = self.wineAreasHandle[i];
				if (self.isNullOrEmpty(wineArea.wineAreaName)) {
					wineArea.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Wine Area Name is mandatory field.</li>';
				}
				if (self.isNullOrEmpty(wineArea.wineAreaDescription)) {
					wineArea.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Wine Area Description is mandatory field.</li>';
				}
				if (messageContent !== '') {
					self.errorPopup = "Wine Area:" + messageContent;
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
				$('#wineAreaModal').modal("hide");
			}
		};

		self.hasDataToAdd = function () {
			for(var i=0, length = self.wineAreasHandle.length; i < length; i++){
				if (!self.isNullOrEmpty(self.wineAreasHandle[i].wineAreaName)
					|| !self.isNullOrEmpty(self.wineAreasHandle[i].wineAreaDescription)){
					return true;
				}
			}
			return false;
		};


		/**
		 * call api to save data.
		 * @param wineArea.
		 * @param action
		 */
		self.callApiToSave = function(wineArea, action){
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			if(action === self.EDIT_ACTION){
				codeTableApi.updateWineArea(
					wineArea,
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
				codeTableApi.deleteWineArea(
					wineArea,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			} else if(action === self.ADD_ACTION){
				codeTableApi.addNewWineArea(
					wineArea,
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
			self.wineAreas = results.data;
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
		 * Call api to add Wine Area.
		 */
		self.doAdd = function () {
			$('#confirmationModalSave').modal("hide");
			$('.modal-backdrop').attr('style', '');
			if (self.validationBeforeAdd()) {
				$('#wineAreaModal').modal("hide");
				$('#confirmationModalSave').modal("hide");
				self.isWaiting = true;
				self.callApiToSave(self.wineAreasHandle, self.ADD_ACTION);
			}
		};

		/**
		 * show popup to confirmation delete.
		 * @param wineArea
		 */
		self.showPopupConfirmDelete = function(wineArea){
			self.error = '';
			self.success = '';
			self.wineAreaToDelete = angular.copy(wineArea);
			self.confirmMessage = self.MESSAGE_CONFIRM_DELETE;
			$('#confirmationDelete').modal({backdrop: 'static', keyboard: true});
			$('.modal-backdrop').attr('style', ' z-index: 100000; ');
		};

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			self.wineAreaToDelete = null;
			$('#confirmationDelete').modal("hide");
			$('#confirmationModalSave').modal("hide");
			$('#wineAreaModal').modal('hide');
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
		self.doDeleteData = function(wineArea){
			$('#confirmationDelete').modal("hide");
			if (self.wineAreaToDelete !== null){
				self.callApiToSave(self.createDataForRequestApi(wineArea), self.DELETE_ACTION);
			}
		};

		/**
		 * Create list of Wine Area to request api.
		 * @param wineArea
		 * @returns {Array}
		 */
		self.createWineAreaList = function(wineArea){
			var wineAreaList = [];
			wineAreaList.push(wineArea);
			return wineAreaList;
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.wineAreaToDelete = null;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 *
		 * @param wineAreaId
		 * @returns {*}
		 */
		self.getButtonStyle = function(wineAreaId){
			if (self.selectedRowId !== null && wineAreaId !== self.selectedRowId){
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
			angular.forEach(self.wineAreasHandle, function (value) {
				if(value.addClass){
					delete value.addClass;
				}
			});
		};
	}
})();
