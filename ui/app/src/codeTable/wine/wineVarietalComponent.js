/*
 *
 * WineVarietalComponent.js
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
	app.component('wineVarietalComponent', {
		templateUrl: 'src/codeTable/wine/wineVarietal.html',
		bindings: {seleted: '<'},
		controller: WineVarietalController
	});

	app.filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});
	app.directive('trimSpace', function () {
		return {
			link: function link(scope, element) {
				element.bind('change', function () {
					$(element).val($.trim($(element).val()));
				});
			}
		}
	});
	app.directive('uiSelectRequired', function () {
		return {
			require: 'ngModel',
			link: function (scope, elm, attrs, ctrl) {
				ctrl.$validators.uiSelectRequired = function (modelValue, viewValue) {
					if ($(elm).hasClass('ui-select')) {
						if (!$(elm).hasClass("open")) {
							if (modelValue === undefined || modelValue === null || modelValue === '') {
								$(elm).closest('td').addClass("has-error-popup");
								$(elm).attr("data-toggle", "tooltip");
								$(elm).attr("data-original-title", $(elm).attr('error-message'));
								$(elm).tooltip({
									placement: 'bottom',
									trigger: 'hover manual' // hover manual
								});
							}
							else {
								$(elm).closest('td').removeClass("has-error-popup");
								$(elm).tooltip('hide');
								$(elm).removeAttr("data-toggle");
								$(elm).removeAttr("data-original-title");
							}
						} else {
							$(elm).closest('td').removeClass("has-error-popup");
							$(elm).tooltip('hide');
							$(elm).removeAttr("data-toggle");
							$(elm).removeAttr("data-original-title");
						}
					} else {
						if (modelValue === undefined || modelValue === null || modelValue === '') {
							$(elm).closest('td').addClass("has-error-popup");
							$(elm).attr("data-toggle", "tooltip");
							$(elm).attr("data-original-title", $(elm).attr('error-message'));
							$(elm).tooltip({
								placement: 'bottom',
								trigger: 'hover manual' // hover manual
							});
						} else {
							$(elm).closest('td').removeClass("has-error-popup");
							$(elm).tooltip('hide');
							$(elm).removeAttr("data-toggle");
							$(elm).removeAttr("data-original-title");

						}
					}
					return true;
				};
			}
		};
	});

	WineVarietalController.$inject = ['codeTableApi', '$rootScope', '$scope', 'ngTableParams', '$filter'];
	/**
	 * Constructs the controller.
	 */
	function WineVarietalController(codeTableApi, $rootScope, $scope, ngTableParams, $filter) {

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
		self.varietalToDelete = null;
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
		self.VALIDATE_VARIETAL = 'validateVarietal';

		/**
		 * list data get from api and handle.
		 * @type {Array}
		 */
		self.varietals = [];
		self.varietalTypes = [];
		self.varietalsHandle = null;

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
			self.findAllVarietal();
		};

		/**
		 * get all varietal type.
		 */
		self.findAllVarietalType = function () {
			codeTableApi.getAllVarietalType().$promise.then(function (res) {
				self.varietalTypes = res;
				angular.forEach(self.varietalTypes,function (varietalType) {
					varietalType.varietalTypeCode = varietalType.varietalTypeCode.toString();
				});
			});
		};

		/**
		 * get all varietal.
		 */
		self.findAllVarietal = function () {
			codeTableApi.getAllVarietal().$promise.then(function (res) {
				self.varietals = res;
				self.loadData();
				self.isWaiting = false;
			});
		};

		/**
		 * Load data and show on ui
		 */
		self.loadData = function () {
			$scope.filter = {
				varietalTypeCode: undefined,
				varietalName: undefined,
				varietalType: {
					varietalTypeSummary: undefined
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
			angular.forEach(self.varietals,function (varietal) {
				varietal.varietalTypeCode = varietal.varietalTypeCode.trim();
				varietal.varietalName = varietal.varietalName.trim();
				varietal.isEditing = false;
			});
			return self.varietals;
		};


		/**
		 * Select record to edit and enable it.
		 *
		 * @param varietal
		 */
		self.enableRow = function(varietal){
			self.error = '';
			self.success = '';
			varietal.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = varietal.varietalId;
			self.selectedRowIndex = self.varietals.indexOf(varietal);
			self.selectedRow = angular.copy(varietal);
			$scope.tableParams.reload();
		};

		/**
		 * reset data of record
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.varietals[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.varietals[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			$scope.tableParams.reload();
		};



		/**
		 * call api to update varietal type
		 * @param varietal
		 */
		self.saveData = function(varietal){
			$('#confirmationModalSave').modal("hide");
			if (self.hasDataChanged(varietal)){
				if (self.validationBeforeUpdate(varietal)){
					self.callApiToSave(self.createDataForRequestApi(varietal), self.EDIT_ACTION);
				}

			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Create list data for update
		 * @param varietal
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(varietal){
			var varietals = [];
			var varietalForRequest = {
				varietalId: varietal.varietalId,
				varietalName: varietal.varietalName,
				varietalType: {
					varietalTypeCode: varietal.varietalTypeCode
				}
			};
			varietals.push(varietalForRequest);
			return varietals;
		};

		/**
		 * Check if has changed data
		 * @param varietal
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(varietal){
			if (varietal !== null && varietal !== undefined) {
				var varietalTemp = angular.copy(varietal);
				var varietalSelectedTemp = angular.copy(self.selectedRow);
				delete varietalTemp.isEditing;
				delete varietalSelectedTemp.isEditing;
				return !(JSON.stringify(varietalTemp) === JSON.stringify(varietalSelectedTemp))
			}
			return false;
		};

		/**
		 * Validation data before update
		 * @param varietal
		 * @returns {boolean}
		 */
		self.validationBeforeUpdate = function (varietal) {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			if (self.isNullOrEmpty(varietal.varietalTypeCode)) {
				varietal.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Varietal Type Id is mandatory field.</li>';
			}
			if (self.isNullOrEmpty(varietal.varietalName)) {
				varietal.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Varietal Name is mandatory field.</li>';
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
		$scope.$on(self.VALIDATE_VARIETAL, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findVarietalById(self.selectedRowId))) {
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
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};


		/**
		 * find varietal Type by varietal Type Code
		 * @param varietalCode
		 * @returns {*}
		 */
		self.findVarietalById = function(varietalId){
			for(var i = 0; i < self.varietals.length; i++) {
				if (self.varietals[i].varietalId === varietalId){
					return self.varietals[i];
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
		self.addVarietal = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetValidation();
			self.resetCSS();
			self.varietalsHandle = [];
			var varietal = self.createVarietalEmpty();
			self.varietalsHandle.push(varietal);
			self.varietalsHandleOrig = angular.copy(self.varietalsHandle);
			$('#varietalModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Add one more row to add varietal Type
		 */
		self.addMoreRow = function () {
			self.errorPopup = '';
			if (self.validationBeforeAdd()) {
				var varietal = self.createVarietalEmpty();
				self.varietalsHandle.push(varietal);
			}
		};

		/**
		 * Create a empty varietal type object.
		 * @returns {{}}
		 */
		self.createVarietalEmpty = function () {
			var varietal = {
				"varietalId" : null,
				"varietalTypeCode" : "",
				"varietalName" : "",
				"varietalType" : {
					"varietalTypeCode" : null
				}
			};
			return varietal;
		};

		/**
		 * Check all field is valid before add new or update varietal type.
		 * @returns {boolean}
		 */
		self.validationBeforeAdd = function () {
			var messageContent = '';
			for (var i = 0, length = self.varietalsHandle.length ; i < length; i++) {
				var varietal = self.varietalsHandle[i];
				if (self.isNullOrEmpty(varietal.varietalTypeCode)) {
					varietal.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Varietal Type Id is mandatory field.</li>';
				}
				if (self.isNullOrEmpty(varietal.varietalName)) {
					varietal.addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Varietal Name is mandatory field.</li>';
				}
				if (messageContent !== '') {
					self.errorPopup = "Varietal:" + messageContent;
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
				$('#varietalModal').modal("hide");
			}
		};

		self.hasDataToAdd = function () {
			for(var i=0, length = self.varietalsHandle.length; i < length; i++){
				if (!self.isNullOrEmpty(self.varietalsHandle[i].varietalTypeCode)
					|| !self.isNullOrEmpty(self.varietalsHandle[i].varietalName)){
					return true;
				}
			}
			return false;
		};


		/**
		 * call api to save data.
		 * @param varietal.
		 * @param action
		 */
		self.callApiToSave = function(varietal, action){
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			if(action === self.EDIT_ACTION){
				codeTableApi.updateVarietal(
					varietal,
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
				codeTableApi.deleteVarietal(
					varietal,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			} else if(action === self.ADD_ACTION){
				codeTableApi.addNewVarietal(
					varietal,
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
			self.varietals = results.data;
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
				$('#varietalModal').modal("hide");
				self.isWaiting = true;
				self.callApiToSave(self.createListVarietalToAdd(), self.ADD_ACTION);
			}
		};

		self.createListVarietalToAdd = function(){
			angular.forEach(self.varietalsHandle,function (varietal) {
				delete varietal.addClass;
				varietal.varietalType.varietalTypeCode = varietal.varietalTypeCode;
			});
			return self.varietalsHandle;
		};

		/**
		 * show popup to confirmation delete.
		 * @param varietal
		 */
		self.showPopupConfirmDelete = function(varietal){
			self.error = '';
			self.success = '';
			self.varietalToDelete = angular.copy(varietal);
			self.confirmMessage = self.MESSAGE_CONFIRM_DELETE;
			$('#confirmationDelete').modal({backdrop: 'static', keyboard: true});
			$('.modal-backdrop').attr('style', ' z-index: 100000; ');
		};

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			self.varietalToDelete = null;
			$('#confirmationDelete').modal("hide");
			$('#confirmationModalSave').modal("hide");
			$('#varietalModal').modal('hide');
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
		self.doDeleteData = function(varietal){
			$('#confirmationDelete').modal("hide");
			if (self.varietalToDelete !== null){
				self.callApiToSave(self.createDataForRequestApi(varietal), self.DELETE_ACTION);
			}
		};

		/**
		 * Create list of varietal type to request api.
		 * @param varietal
		 * @returns {Array}
		 */
		self.createVarietalList = function(varietal){
			var varietalList = [];
			varietalList.push(varietal);
			return varietalList;
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.varietalToDelete = null;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 *
		 * @param varietalId
		 * @returns {*}
		 */
		self.getButtonStyle = function(varietalId){
			if (self.selectedRowId !== null && varietalId !== self.selectedRowId){
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
		 * Clear filter.
		 */
		self.clearFilter = function () {
			$scope.filter.varietalTypeCode = undefined;
			$scope.filter.varietalName = undefined;
			$scope.filter.varietalType.varietalTypeSummary = undefined;
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
