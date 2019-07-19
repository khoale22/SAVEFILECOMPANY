/*
 *   tobaccoProductTypeCodeComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table -> Tobacco Product Type Code component.
 *
 * @author vn75469
 * @since 2.16.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('tobaccoProductTypeCodeComponent', {
		templateUrl: 'src/codeTable/tobaccoProductTypeCode/tobaccoProductTypeCode.html',
		bindings: {seleted: '<'},
		controller: TobaccoProductTypeCodeController
	});
	app.directive('validationTaxRate', validationTaxRate);

	/**
	 * Create a directive for validating tax rate.
	 *
	 * @type {Array}
	 */
	validationTaxRate.$inject = [];
	function validationTaxRate( ) {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('keypress', function(event) {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
				});
				element.on('blur', function (event) {
					const MIN_TAX_RATE = 0;
					const MAX_TAX_RATE = 999.999999;
					if(event.target.value !== undefined && !isNumber(event.target.value)) {
						$(element).attr('title', attrs.validationTaxRate);
						$(element).addClass('ng-invalid ng-touched');
					} else if (isNumber(event.target.value)){
						event.target.value = parseFloat(event.target.value).toFixed(6);
						if (event.target.value < MIN_TAX_RATE || event.target.value > MAX_TAX_RATE){
							$(element).attr('title', attrs.validationTaxRate);
							$(element).addClass('ng-invalid ng-touched');
						}
					}
				});
				$('.taxRateInput').bind('paste', function() {
					$(element).attr('title', '');
					$(element).removeClass('ng-touched');
					$(element).removeClass('ng-invalid');
					var el = this;
					setTimeout(function() {
						el.value = el.value.replace(/[^0-9.]/g, '');
					}, 0);
				});
				function isNumber(value) {
					return !isNaN(parseFloat(value)) && isFinite(value);
				}
			}
		};
	}

	TobaccoProductTypeCodeController.$inject = ['$rootScope', '$scope', 'ngTableParams','tobaccoProductTypeCodeApi'];

	/**
	 * Constructs the controller.
	 */
	function TobaccoProductTypeCodeController($rootScope, $scope, ngTableParams, tobaccoProductTypeCodeApi) {
		var self = this;

		/**
		 * The default page number.
		 *
		 * @type {number}
		 */
		const PAGE = 1;

		/**
		 * The default page size of main table.
		 *
		 * @type {number}
		 */
		const PAGE_SIZE = 20;

		/**
		 * Minimum value of tax rate.
		 * @type {number}
		 */
		const MIN_TAX_RATE = 0;

		/**
		 * Maximum value of tax rate.
		 * @type {number}
		 */
		const MAX_TAX_RATE = 999.999999;

		/**
		 * Handle on change tab.
		 * @type {string}
		 */
		const VALIDATE_TOBACCO_PRODUCT_TYPE_CODE_DETAIL = 'validateTobaccoProductTypeCode';

		/**
		 * Return tab key.
		 * @type {string}
		 */
		const RETURN_TAB = 'returnTab';

		/**
		 * Messages.
		 * @type {string}
		 */
		const MESSAGE_CHANGE_TAB_CONFIRM = "Unsaved data will be lost. Do you want to save the changes before continuing?";
		const MESSAGE_NO_DATA_CHANGE = "There are no changes on this page to be saved. Please make any changes to update.";
		const MESSAGE_INVALID_TAX_RATE = "Tax Rate value must be equal or greater than 0 and less than or equal 999.999999."
		const MESSAGE_NOT_INPUT_TAX_RATE = "Tax Rate is a mandatory field.";
		const MESSAGE_UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * Flag for waiting response from back end.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Message success has returned from api.
		 * @type {null}
		 */
		self.success = '';

		/**
		 * Message error has returned from api.
		 * @type {null}
		 */
		self.error = '';

		/**
		 * Flags to handle business.
		 * @type {boolean}
		 */
		self.isEditing = false;
		self.hasOtherRowEditing = false;
		self.isReturnToTab = false;

		/**
		 * tobaccoProductTypeCodeId of selected row.
		 * @type {null}
		 */
		self.selectedRowId = null;

		/**
		 * Index of selected row.
		 * @type {number}
		 */
		self.selectedRowIndex = -1;

		/**
		 * selected row.
		 * @type {null}
		 */
		self.selectedRow = null;

		/**
		 * Array contain data has returned from API.
		 * @type {Array}
		 */
		self.tobaccoProductTypeCodes = [];

		/**
		 * String contain content of confirmation message.
		 * @type {null}
		 */
		self.confirmationMessage = null;

		/**
		 * Initiates the construction of the Tobacco Product Type Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			if ($rootScope.isEditedOnPreviousTab) {
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
			self.getAllTobaccoProductTypeCode();
		};

		/**
		 * Call api to get all Tobacco Product Type Codes.
		 */
		self.getAllTobaccoProductTypeCode = function () {
			self.isWaiting = true;
			tobaccoProductTypeCodeApi.getAllTobaccoTypeCodes().$promise.then(function (results) {
				self.callApiSuccess(results);
			},
				self.fetchError);
		};

		/**
		 * If there is an error this will display the error.
		 *
		 * @param error returned from api.
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = MESSAGE_UNKNOWN_ERROR;
			}
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
		};

		/**
		 * Handle when call API success.
		 *
		 * @param result
		 */
		self.callApiSuccess = function (result) {
			self.isWaiting = false;
			if (!self.isNullOrEmpty(result.message)){
				self.success = result.message;
			}
			if(self.isReturnToTab){
				$rootScope.success = self.success;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.tobaccoProductTypeCodes = result.data || result;
			//remove element if it has typeCode < 0.
			self.tobaccoProductTypeCodes = self.tobaccoProductTypeCodes.filter( function(element) {
				return parseFloat(element.tobaccoProductTypeCode) > 0;
			});
			angular.forEach(self.tobaccoProductTypeCodes, function (element) {
				element.isEditing = false;
			});
			self.formatTaxRate();
			self.resetAllFlag();
			self.buildDataTable();
			self.returnToTab();
			self.isWaiting = false;
		};

		/**
		 * Format tax rate value to display on UI.
		 */
		self.formatTaxRate = function () {
			angular.forEach(self.tobaccoProductTypeCodes, function (element) {
				element.taxRate = Number(element.taxRate).toFixed(6);
			});
		};

		/**
		 * Build a ng-table to show on UI.
		 */
		self.buildDataTable = function(){
			$scope.tableParams = new ngTableParams({
				page: PAGE,
				count: PAGE_SIZE,
			}, {
				counts: [],
				data: self.tobaccoProductTypeCodes
			});
		};

		/**
		 * Select record to edit and enable it.
		 *
		 * @param tobaccoProductTypeCode
		 */
		self.enableRow = function(tobaccoProductTypeCode){
			self.error = '';
			self.success = '';
			tobaccoProductTypeCode.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = tobaccoProductTypeCode.tobaccoProductTypeCode;
			self.selectedRowIndex = self.tobaccoProductTypeCodes.indexOf(tobaccoProductTypeCode);
			self.selectedRow = angular.copy(tobaccoProductTypeCode);
			$scope.tableParams.reload();
		};

		/**
		 * Reset data of record.
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.tobaccoProductTypeCodes[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.tobaccoProductTypeCodes[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			$scope.tableParams.reload();
		};

		/**
		 * Reset all flags.
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 * Get style of button Edit.
		 *
		 * @param tobaccoProductTypeCode
		 * @returns {*}
		 */
		self.getButtonStyle = function(tobaccoProductTypeCode){
			var defaultStyle = 'color: orange; font-size: large;background: transparent; border-width:0px;padding: 0px;';
			if (self.selectedRowId !== null && tobaccoProductTypeCode !== self.selectedRowId){
				return 'opacity: 0.5;' + defaultStyle
			}
			return 'opacity: 1.0;' + defaultStyle;
		};

		/**
		 * Handle event change tab.
		 */
		$scope.$on(VALIDATE_TOBACCO_PRODUCT_TYPE_CODE_DETAIL, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findTobaccoProductTypeByCode(self.selectedRowId))) {
				self.confirmationMessage = MESSAGE_CHANGE_TAB_CONFIRM;
				self.isReturnToTab = true;
				self.error = '';
				self.success = '';
				$('#confirmationModalSave').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(RETURN_TAB);
			}
		});

		/**
		 * Saving data after edit.
		 *
		 * @param tobaccoProductType to save.
		 */
		self.saveData = function(tobaccoProductType){
			$('#confirmationModalSave').modal("hide");
			if (self.hasDataChanged(tobaccoProductType)){
				if(self.validationData(tobaccoProductType))
					self.callApiToSave(tobaccoProductType);
			} else {
				self.error = MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * Validation data before call api to save.
		 *
		 * @param tobaccoProductType to save.
		 * @returns {boolean} true if valid data, otherwise return false.
		 */
		self.validationData = function(tobaccoProductType){
			var returnFlag = true;
			if (self.isNullOrEmpty(tobaccoProductType.taxRate)){
				self.error = MESSAGE_NOT_INPUT_TAX_RATE;
				returnFlag = false;
			} else if (!self.isNumber(tobaccoProductType.taxRate)){
				self.error = MESSAGE_INVALID_TAX_RATE;
				returnFlag = false;
			} else {
				var value = parseFloat(tobaccoProductType.taxRate);
				if (value < MIN_TAX_RATE || value > MAX_TAX_RATE){
					self.error = MESSAGE_INVALID_TAX_RATE;
					returnFlag = false;
				}
			}
			return returnFlag;
		};

		/**
		 * Check a value is a number.
		 * @param value
		 * @returns {boolean|*}
		 */
		self.isNumber = function(value) {
			return !isNaN(parseFloat(value)) && isFinite(value);
		};

		/**
		 * call api to save data.
		 * @param tobaccoProductType
		 */
		self.callApiToSave = function(tobaccoProductType){
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			var tobaccoProductTypes = [];
			tobaccoProductTypes.push(tobaccoProductType);
			tobaccoProductTypeCodeApi.updateTobaccoProductType(
				tobaccoProductTypes,
				function (results) {
					self.callApiSuccess(results)
				},
				function (error) {
					self.fetchError(error);
				});
		};

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(RETURN_TAB);
			}
		};

		/**
		 * Check object null or empty
		 *
		 * @param object
		 * @returns {boolean} true if Object is null/ false or equals blank, otherwise return false.
		 */
		self.isNullOrEmpty = function (object) {
			return object === undefined || object === null || !object || object === "";
		};

		/**
		 * Check if data has changed.
		 *
		 * @param tobaccoProductType
		 * @returns {boolean} True if tax rate has changed.
		 */
		self.hasDataChanged = function(tobaccoProductType){
			var returnFlag = false;
			if (tobaccoProductType !== null && tobaccoProductType !== undefined){
				if (tobaccoProductType.taxRate !== self.selectedRow.taxRate){
					returnFlag =true;
				}
			}
			return returnFlag;
		};

		/**
		 * Find Tobacco Product Type object in array by typeCode.
		 *
		 * @param typeCode tobaccoProductTypeCode
		 * @returns  tobaccoProductTypeCode object
		 */
		self.findTobaccoProductTypeByCode = function(typeCode){
			for(var i = 0; i < self.tobaccoProductTypeCodes.length; i++) {
				if (self.tobaccoProductTypeCodes[i].tobaccoProductTypeCode === typeCode){
					return self.tobaccoProductTypeCodes[i];
				}
			}
		};

		/**
		 * Close confirmation popup and change tab.
		 */
		self.doClosePopupConfirmation = function () {
			$('#confirmationModalSave').modal("hide");
			if (self.isReturnToTab) {
				$('#confirmationModalSave').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			}
		};

		/**
		 * Hides confirmation popup.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style', '');
			$('#confirmationModalSave').modal('hide');
		};
	}
})();
