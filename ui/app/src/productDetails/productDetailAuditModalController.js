/*
 * productDetailAuditModalController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
/**
 * Modal controller to support the Modal Controller.
 *
 * @author l730832
 * @since 2.6.0
 */
'use strict';
(function () {
	'use strict';
	var app = angular.module('productMaintenanceUiApp');
	app.controller('ProductDetailAuditModalController', ProductDetailAuditModalController);
	ProductDetailAuditModalController.$inject = ['ngTableParams', 'callback', 'parameters', 'title', '$uibModalInstance', '$scope'];
	function ProductDetailAuditModalController(ngTableParams, callback, parameters, title, $uibModalInstance, $scope) {
		var self = this;
		self.callback = callback;
		self.parameters = parameters;
		self.hasUpc = false;
		self.isWaiting = true;
		/**
		 * Constant order by asc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_ASC = "asc";
		/**
		 * Constant order by desc.
		 *
		 * @type {String}
		 */
		const ORDER_BY_DESC = "desc";
		$uibModalInstance.rendered.then(function() {
			self.data = self.callback(self.parameters, self.loadData, self.fetchError);
			self.title = title + ": Change Log";
		});
		self.init = function () {
			self.isWaiting = true;
		};
		/**
		 * Closes the Audit Modal
		 */
		self.close = function () {
			self.isWaiting = false;
			if($uibModalInstance) {
				$uibModalInstance.close();
				$uibModalInstance = null;
			}
		};
		/**
		 * Loads the data into an ngTable.
		 * @param results
		 */
		self.loadData = function (results) {
			// Check if the first record contains a variable 'upc'. If the first record does contain this variable,
			// all of them should contain this variable, so only the first one needs to be checked. Set hasUpc = true so
			// the audit table shows the 'UPC' column.
			if (results !== null && results.length > 0 && results[0].key !== null && results[0].upc) {
				self.hasUpc = true;
			}
			// Else set hasUpc = false to not show the 'UPC' column
			else {
				self.hasUpc = false;
			}

			// Check for optional product ID
			if (results !== null && results.length > 0 && results[0].key !== null && results[0].prodId) {
				self.hasProdId = true;
			}
			// Else set hasProdId = false to not show the 'Product Id' column
			else {
				self.hasProdId = false;
			}

			// Check for optional warehouse ID
			if (results !== null && results.length > 0 && results[0].key !== null && results[0].warehouseId) {
				self.hasWarehouseId = true;
			}
			// Else set hasWarehouseId = false to not show the 'Warehouse Id' column
			else {
				self.hasWarehouseId = false;
			}
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			// Loads the table data.
			self.tableParams = new ngTableParams(
				{
					page: 1,
					count: 10,
					filter: $scope.filter,
					sorting: $scope.sorting

				},
				{
					counts: [],
					data: results
				}
			)
			self.isWaiting = false;
		};
		/**
		 * Change sort.
		 */
		self.changeSort = function (){
			if($scope.sorting.changedOn === ORDER_BY_DESC){
				$scope.sorting.changedOn = ORDER_BY_ASC;
			}else {
				$scope.sorting.changedOn = ORDER_BY_DESC;
			}
		}
		/**
		 * If an error has occured set the error.
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			if (error && error.data) {
				if (error.data.message) {
					self.setError(error.data.message);
				} else {
					self.setError(error.data.error);
				}
			}
			else {
				self.setError("An unknown error occurred.");
			}
		};
		/**
		 * Sets the error
		 * @param error
		 */
		self.setError = function (error) {
			self.error = error;
		};
	}
})();
