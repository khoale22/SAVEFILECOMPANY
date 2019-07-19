/*
 * myAttributeController.js
 *
 * Copyright (c) 2018 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */

'use strict';

/**
 * The controller to handle the layout for the MyAttributeController.
 *
 * @author vn70529
 * @since 2.17.0
 */
(function () {

	angular.module('productMaintenanceUiApp')
		.controller('MyAttributeController', MyAttributeController)
		.factory('MyAttributeFactory', MyAttributeFactory);
	MyAttributeController.$inject = ['$scope', '$timeout', 'ngTableParams', 'MyAttributeApi', 'MyAttributeFactory'];

	/**
	 * Constructs the controller.
	 *
	 * @param $scope the score of page.
	 * @param $uibModalInstance  the Modal Instance service.
	 * @param ngTableParams the ngTableParams.
	 * @param myAttributeApi the api.
	 */
	function MyAttributeController($scope, $timeout, ngTableParams, myAttributeApi, myAttributeFactory) {
		var self = this;
		self.isWaiting = false;
		$scope.propertyName = '';
		$scope.reverse = null;
		self.attributeMappings = [];
		self.isShowingOnModal = true;
		self.isModal;
		/**
		 * Init table data.
		 */
		self.init = function (isModal) {
			self.isModal = isModal;
			self.isWaiting = true;
			$timeout( function(){
				myAttributeApi.findAllMyAttributes().$promise.then(function (results) {
					self.attributeMappings = results;
					angular.forEach(self.attributeMappings, function (attributeMapping) {
						attributeMapping.attributeName = attributeMapping.attributeName.trim();
						attributeMapping.existingApplication = attributeMapping.existingApplication.trim();
						attributeMapping.newScreen = attributeMapping.newScreen.trim();
					});
					self.initTable();
					self.isWaiting = false;
				});
			}, 500);
		}
		/**
		 * Init table.
		 */
		self.initTable = function () {
			$scope.filter = {
				attributeName: undefined,
				existingApplication: undefined,
				newScreen: undefined
			};
			$scope.tableParams = new ngTableParams({
				count: self.attributeMappings.length,
				filter: $scope.filter
			}, {
				counts: [],
				debugMode: false,
				data: self.attributeMappings
			});
		};
		self.clearFilter = function () {
			$scope.filter.attributeName = undefined;
			$scope.filter.existingApplication = undefined;
			$scope.filter.newScreen = undefined;
			$scope.tableParams.reload();
		}
		/**
		 * Sort by property name. It will be called from table header.
		 * @param propertyName the property name
		 * @return {boolean} reverse value
		 */
		self.sortBy = function (propertyName) {
			if (self.isWaiting) {
				return true;
			}
			$scope.reverse = ($scope.propertyName === propertyName) ? !$scope.reverse : false;
			$scope.propertyName = propertyName;
		};
		/**
		 * Close this dialog
		 */
		self.close = function () {
			myAttributeFactory.close();
		};

		$scope.localeSensitiveComparator = function (v1, v2) {
			// If we don't get strings, just compare by index
			if (v1.type !== 'string' || v2.type !== 'string') {
				return (v1.index < v2.index) ? -1 : 1;
			}
			if (v1.value == '') {
				return 1;
			}
			if (v2.value == '') {
				return -1;
			}
			// Compare strings alphabetically.
			if (v1.value < v2.value) {
				return -1;
			}
			if (v1.value > v2.value) {
				return 1;
			}
			return 0;
		};
	}
	/**
	 * Constructs the factory for show modal.
	 *
	 * @param $uibModal the ui modal
	 * @returns {MyAttributeFactory} MyAttributeFactory object.
	 */
	function MyAttributeFactory($uibModal) {
		var self = this;
		self.modal = null;
		/**
		 * Show modal.
		 */
		self.showModal = function () {
			if(self.modal != null){
				self.modal.close();
			}
			self.modal = $uibModal.open({
				templateUrl: 'src/utilities/myAttribute/myAttributeModal.html',
				backdrop: 'static',
				windowClass: 'modal',
				controller: 'MyAttributeController',
				controllerAs: '$ctrl',
				size: 'lg',
				resolve: {}
			});
		};
		self.close = function(){
			self.modal.close();
			self.modal = null;
		}
		return self;
	}
})();
