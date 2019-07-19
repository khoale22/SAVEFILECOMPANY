/*
 *   itemSubstitution.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * ItemSubstitution -> Item Substitution Page.
 *
 * @author m594201
 * @since 2.8.0
 */

(function () {

	angular.module('productMaintenanceUiApp').component('itemSubstitution', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
			productMaster: '<'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/itemSubstitution.html',
		// The controller that handles our component logic
		controller: ItemSubstitutionController
	});

	ItemSubstitutionController.$inject = ['ProductCasePackApi', '$scope','ProductSearchService','$rootScope'];


	function ItemSubstitutionController(productCasePackApi, $scope, productSearchService, $rootScope) {

		var self = this;

		/**
		 * Keeps track of whether front end is waiting for back end response.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Selected Warehouse in dropdown filter.
		 *
		 * @type {Array}
		 */
		var selectedWarehouse = [];

		/**
		 * Initializes the controller.
		 */
		this.$onInit = function () {
			console.log('itemSubstitutionComponent - Initialized');
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			console.log('itemSubstitutionComponent - Destroyed');
			/** Execute component destroy events if any. */
		};

		/**
		 * Component will reload the vendor data whenever the item is changed in casepack.
		 */
		this.$onChanges = function () {
			self.isWaitingForResponse = true;

			var itemCode = self.itemMaster.key.itemCode;
			productCasePackApi.getItemSubInfo({'itemCode': itemCode}, self.loadData, self.fetchError)
		};

		/**
		 * Callback for a successful call to get mrt info from backend
		 * @param results
		 */
		self.loadData = function (results) {
			self.itemSubInfo = angular.copy(results);
			self.isWaitingForResponse = false;
		};

		/**
		 * Fetches the error from the back end.
		 *
		 * @param error
		 */
		self.fetchError = function () {
			self.isWaitingForResponse = false;
			if(error && error.data) {
				self.isLoading = false;
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
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		};

		/**
		 * Fetches the value of the filter and pulls the warehouse Number, which is then used to filter the table.
		 *
		 * @returns {*} the selectedWarehouse number.
		 */
		self.filterTable = function (item) {
			if(angular.isDefined($scope.whsFilter)) {
				return $scope.whsFilter == item.key.locationNumber;
			}
			return true;
		};
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
	}
})();
