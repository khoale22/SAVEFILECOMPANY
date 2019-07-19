/*
 * checkDigitCalculatorController.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * Controller for Check Digit Calculator.
 *
 * @author vn75469
 * @since 2.16.0
 */

(function() {
	angular.module('productMaintenanceUiApp').controller('CheckDigitCalculatorController', checkDigitCalculatorController);
	checkDigitCalculatorController.$inject = ['$scope','checkDigitCalculatorApi', 'ngTableParams'];

	/**
	 * Constructs the controller.
	 * @param $scope
	 * @param checkDigitCalculatorApi
	 * @param ngTableParams The API to set up the report table.
	 */
	function checkDigitCalculatorController($scope, checkDigitCalculatorApi, ngTableParams) {

		var self = this;

		/**
		 * Whether or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * UPC Code
		 * @type {string}
		 */
		self.upcCode = '';

		/**
		 * Result number of check digit.
		 * @type {number}
		 */
		self.checkDigitResult = '';

		/**
		 * List ItemCodes return from api.
		 * @type {Array}
		 */
		self.itemCodes = [];
		self.error = '';

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
		const PAGE_SIZE = 3;
		const ZERO_STRING = "0";
		const UPC_CODE_MAX_LENGTH = 13;
		const UPC_TYPE = 'WHS';

		/**
		 * The default error message.
		 *
		 * @type {string}
		 */
		const UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * Handle calculate Check Digit and show list Item return from Api.
		 */
		self.calculate = function () {
			self.error = '';
			if(self.isNullOrEmpty(self.upcCode)){
				$('#popupWarning').modal({backdrop: 'static', keyboard: true});
				$('.modal-backdrop').attr('style', ' z-index: 100000; ');
			} else {
				self.upcCode = self.paddingChar(self.upcCode, ZERO_STRING, UPC_CODE_MAX_LENGTH);
				self.doCalculate();
				self.itemCodes = [];
				self.isWaiting = true;
				checkDigitCalculatorApi.get({upcCode: self.upcCode},self.callApiSuccess, self.callApiError);
			}
		};

		/**
		 * Calculate check digit.
		 */
		self.doCalculate = function(){
			var evenDigitCount = 0;
			var oddDigitCount = 0;
			/**
			 * If the UPC Number is ABCDEFGHIJKLM:
			 * SUM = (A+C+E+G+I+K+M)x3 + (B+D+F+H+J+L)
			 */
			for (var i = 0, length = self.upcCode.length; i < length; i++) {
				if (i % 2 !== 0)
					evenDigitCount += parseInt(self.upcCode.charAt(i));
				else {
					oddDigitCount += parseInt(self.upcCode.charAt(i));
				}
			}
			self.checkDigitResult = (evenDigitCount + oddDigitCount*3) % 10;
			if (self.checkDigitResult !== 0) {
				self.checkDigitResult = 10 - self.checkDigitResult;
			}
		};

		/**
		 * Add character in front of string.
		 * @param val source number
		 * @param padCharacter to padding.
		 * @param length max length of string
		 * @returns {*}
		 */
		self.paddingChar = function(val, padCharacter, length) {
			while (val.length < length)
				val = padCharacter + val;
			return val;
		};

		/**
		 * Handle when call API success.
		 *
		 * @param result has returned from api.
		 */
		self.callApiSuccess = function (result) {
			if (!self.isNullOrEmpty(result.key)){
				result.upcType = UPC_TYPE;
				self.itemCodes.push(result);
			}
			self.buildDataTable();
			self.isWaiting = false;
		};

		/**
		 * call api error and throw message.
		 *
		 * @param error has returned from api.
		 */
		self.callApiError = function(error){
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = UNKNOWN_ERROR;
			}
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
				data: self.itemCodes
			});
		};

		/**
		 * Check object null or empty.
		 *
		 * @param object
		 * @returns {boolean}
		 */
		self.isNullOrEmpty = function (object) {
			return object === undefined || object === null || !object || object === "";
		};
	}
})();
