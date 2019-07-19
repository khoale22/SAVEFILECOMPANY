/*
 *
 * wineRegionComponent.js
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
 * The controller for the Wine Region Controller.
 * @author vn87351
 * @since 2.12.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('wineRegionComponent', {
		templateUrl: 'src/codeTable/wine/wineRegion.html',
		bindings: {seleted: '<'},
		controller: WineRegionController
	});

	WineRegionController.$inject = ['codeTableApi', '$rootScope', '$scope', 'ngTableParams', '$filter'];
	/**
	 * Constructs the controller.
	 */
	function WineRegionController(codeTableApi, $rootScope, $scope, ngTableParams, $filter) {

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
		self.wineRegionToDelete = null;
		self.hasOtherRowEditing = false;
		self.selectedRowId = null;
		self.selectedRowIndex = -1;
		self.selectedRow = null;
		self.RETURN_TAB = 'returnTab';
		self.isReturnToTab = false;
		self.confirmMessage = '';
		self.MESSAGE_CONFIRM_DELETE = "Are you sure you want to delete the selected Wine Region ?";
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		self.MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';
		self.VALIDATE_WINE_Region = 'validateWineRegion';

		/**
		 * list data Wine Region
		 * @type {Array}
		 */
		self.wineRegions = [];
		self.wineAreas = [];

		self.wineRegionsHandle = null;

		/**
		 * Initiates the construction of the Wine Region Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.findAllWineAres();
			self.findAllWineRegion();
		};

		/**
		 * get all varietal type.
		 */
		self.findAllWineAres = function () {
			codeTableApi.getAllWineArea().$promise.then(function (res) {
				self.wineAreas = res;
			});
		};
		/**
		 * get Wine Region
		 */
		self.findAllWineRegion = function () {
			codeTableApi.getAllWineRegion().$promise.then(function (res) {
				self.wineRegions = res;
				self.loadData();
				self.isWaiting = false;
			});
		};

		/**
		 * Load data and show on ui
		 */
		self.loadData = function () {
			$scope.filter = {
				wineRegionId: undefined,
				wineRegionName: undefined,
				wineRegionDescription: undefined,
				wineArea: {
					wineAreaSummary: undefined
				}
			};
			$scope.tableParams = new ngTableParams({
				page: 1,
				count: 20,
				filter: $scope.filter
			}, {
				counts: [],
				data: self.initDataForNgTable()
			});
		};

		/**
		 * processing data has returned from api
		 * @returns {Array}
		 */
		self.initDataForNgTable = function(){
			angular.forEach(self.wineRegions,function (wineRegion) {
				wineRegion.wineRegionName = wineRegion.wineRegionName.trim();
				wineRegion.wineRegionDescription = wineRegion.wineRegionDescription.trim();
				wineRegion.isEditing = false;
			});
			return self.wineRegions;
		};


		/**
		 * Select record to edit and enable it.
		 *
		 * @param wineRegion
		 */
		self.enableRow = function(wineRegion){
			self.error = '';
			self.success = '';
			wineRegion.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = wineRegion.wineRegionId;
			self.selectedRowIndex = self.wineRegions.indexOf(wineRegion);
			self.selectedRow = angular.copy(wineRegion);
			$scope.tableParams.reload();
		};

		/**
		 * reset data of record
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.wineRegions[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.wineRegions[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			$scope.tableParams.reload();
		};



		/**
		 * call api to update Wine Region
		 * @param wineRegion
		 */
		self.saveData = function(wineRegion){
			$('#confirmationModalSave').modal("hide");
			if (self.hasDataChanged(wineRegion)){
				if (self.validationBeforeUpdate(wineRegion)){
					self.callApiToSave(self.createDataForRequestApi(wineRegion), self.EDIT_ACTION);
				}

			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Create list data for update
		 * @param wineRegion
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(wineRegion){
			var wineRegions = [];
			var wineRegionForRequest = {
				wineRegionId: wineRegion.wineRegionId,
				wineRegionName: wineRegion.wineRegionName,
				wineRegionDescription: wineRegion.wineRegionDescription,
				wineArea: {
					wineAreaId: wineRegion.wineArea.wineAreaId
				}
			};
			wineRegions.push(wineRegionForRequest);
			return wineRegions;
		};

		/**
		 * Check if has changed data
		 * @param wineRegion
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(wineRegion){
			if (wineRegion !== null && wineRegion !== undefined) {
				var wineRegionTemp = angular.copy(wineRegion);
				var wineRegionSelectedTemp = angular.copy(self.selectedRow);
				delete wineRegionTemp.isEditing;
				delete wineRegionSelectedTemp.isEditing;
				return !(JSON.stringify(wineRegionTemp) === JSON.stringify(wineRegionSelectedTemp))
			}
			return false;
		};

		/**
		 * Validation data before save
		 * @param wineRegion
		 * @returns {boolean}
		 */
		self.validationBeforeUpdate = function (wineRegion) {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			if (self.isNullOrEmpty(wineRegion.wineArea.wineAreaId)) {
				wineRegion.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Wine Area Id is mandatory field.</li>';
			}
			if (self.isNullOrEmpty(wineRegion.wineRegionName)) {
				self.error += '<li>Wine Region Name is mandatory field.</li>';
			}
			if (self.isNullOrEmpty(wineRegion.wineRegionDescription)) {
				self.error += '<li>Wine Region Description is mandatory field.</li>';
			}
			if (self.error !== ''){
				self.error = 'Wine Region:' + self.error;
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
		$scope.$on(self.VALIDATE_WINE_Region, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findWineRegionById(self.selectedRowId))) {
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
		 * find Wine Region by Wine Region Id
		 * @param wineRegionId
		 * @returns {*}
		 */
		self.findWineRegionById = function(wineRegionId){
			for(var i = 0; i < self.wineRegions.length; i++) {
				if (self.wineRegions[i].wineRegionId === wineRegionId){
					return self.wineRegions[i];
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
		 * Show Popup to add new Wine Region
		 */
		self.addWineRegion = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.resetCSS();
			self.wineRegionsHandle = [];
			var wineRegion = self.createWineRegionEmpty();
			self.wineRegionsHandle.push(wineRegion);
			self.wineRegionsHandleOrig = angular.copy(self.wineRegionsHandle);
			$('#wineRegionModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Add one more row to add Wine Region
		 */
		self.addMoreRow = function () {
			self.errorPopup = '';
			if (self.validationBeforeAdd()) {
				var wineRegion = self.createWineRegionEmpty();
				self.wineRegionsHandle.push(wineRegion);
			}
		};

		/**
		 * Create a empty Wine Region object.
		 * @returns {{}}
		 */
		self.createWineRegionEmpty = function () {
			var wineRegion = {
				"wineRegionId" : null,
				"wineRegionDescription" : "",
				"wineRegionName" : "",
				"wineArea" : {
					"wineAreaId" : null
				}
			};
			return wineRegion;
		};

		/**
		 * Check all field is valid before add new or update Wine Region.
		 * @returns {boolean}
		 */
		self.validationBeforeAdd = function () {
			var messageContent = '';
			for (var i = 0, length = self.wineRegionsHandle.length ; i < length; i++) {
				var wineRegion = self.wineRegionsHandle[i];
				if (self.isNullOrEmpty(wineRegion.wineArea.wineAreaId)) {
					wineRegion.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Wine Area Id is mandatory field.</li>';
				}
				if (self.isNullOrEmpty(wineRegion.wineRegionName)) {
					wineRegion.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Wine Region Name is mandatory field.</li>';
				}
				if (self.isNullOrEmpty(wineRegion.wineRegionDescription)) {
					wineRegion.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Wine Region Description is mandatory field.</li>';
				}
				if (messageContent !== '') {
					self.errorPopup = "Wine Region:" + messageContent;
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
				$('#wineRegionModal').modal("hide");
			}
		};

		self.hasDataToAdd = function () {
			for(var i=0, length = self.wineRegionsHandle.length; i < length; i++){
				if (!self.isNullOrEmpty(self.wineRegionsHandle[i].wineRegionName)
					|| !self.isNullOrEmpty(self.wineRegionsHandle[i].wineRegionDescription)
					|| !self.isNullOrEmpty(self.wineRegionsHandle[i].wineArea.wineAreaId)){
					return true;
				}
			}
			return false;
		};


		/**
		 * call api to save data.
		 * @param wineRegion.
		 * @param action
		 */
		self.callApiToSave = function(wineRegion, action){
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			if(action === self.EDIT_ACTION){
				codeTableApi.updateWineRegion(
					wineRegion,
					function (results) {
						self.callApiSuccess(results)
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
				codeTableApi.deleteWineRegion(
					wineRegion,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			} else if(action === self.ADD_ACTION){
				codeTableApi.addNewWineRegion(
					wineRegion,
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
			self.wineRegions = results.data;
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
		 * Call api to add Wine Region.
		 */
		self.doAdd = function () {
			$('#confirmationModalSave').modal("hide");
			$('.modal-backdrop').attr('style', '');
			if (self.validationBeforeAdd()) {
				$('#wineRegionModal').modal("hide");
				$('#confirmationModalSave').modal("hide");
				self.isWaiting = true;
				self.callApiToSave(self.wineRegionsHandle, self.ADD_ACTION);
			}
		};

		/**
		 * show popup to confirmation delete.
		 * @param wineRegion
		 */
		self.showPopupConfirmDelete = function(wineRegion){
			self.error = '';
			self.success = '';
			self.wineRegionToDelete = angular.copy(wineRegion);
			self.confirmMessage = self.MESSAGE_CONFIRM_DELETE;
			$('#confirmationDelete').modal({backdrop: 'static', keyboard: true});
			$('.modal-backdrop').attr('style', ' z-index: 100000; ');
		};

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			self.wineRegionToDelete = null;
			$('#confirmationDelete').modal("hide");
			$('#confirmationModalSave').modal("hide");
			$('#wineRegionModal').modal('hide');
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
		self.doDeleteData = function(wineRegion){
			$('#confirmationDelete').modal("hide");
			if (self.wineRegionToDelete !== null){
				self.callApiToSave(self.createDataForRequestApi(wineRegion), self.DELETE_ACTION);
			}
		};

		/**
		 * Create list of Wine Region to request api.
		 * @param wineRegion
		 * @returns {Array}
		 */
		self.createWineRegionList = function(wineRegion){
			var wineRegionList = [];
			wineRegionList.push(wineRegion);
			return wineRegionList;
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.wineRegionToDelete = null;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 *
		 * @param wineRegionId
		 * @returns {*}
		 */
		self.getButtonStyle = function(wineRegionId){
			if (self.selectedRowId !== null && wineRegionId !== self.selectedRowId){
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
			angular.forEach(self.wineRegionsHandle, function (value) {
				if(value.addClass){
					delete value.addClass;
				}
			});
		};

		self.clearFilter = function () {
			$scope.filter.wineRegionId = undefined;
			$scope.filter.wineRegionName = undefined;
			$scope.filter.wineRegionDescription = undefined;
			$scope.filter.wineArea.wineAreaSummary = undefined;
		};

		$scope.filter = {
			wineRegionId: undefined,
			wineRegionName: undefined,
			wineRegionDescription: undefined,
			wineArea: {
				wineAreaSummary: undefined
			}
		};
	}
})();
