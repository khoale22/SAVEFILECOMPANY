/*
 *   factoryComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table -> factory page component.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('factory', {
		bindings:{
			onHandleTabChange: '&',
			sendingDataToTab:'&',
			messageSending:'='
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/factory/factory.html',
		// The controller that handles our component logic
		controller: FactoryController
	}).filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});

	FactoryController.$inject = ['$rootScope', '$scope', 'FactoryApi','ngTableParams','$filter'];

	/**
	 * Code Table factory component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function FactoryController($rootScope, $scope, factoryApi, ngTableParams, $filter) {
		/** All CRUD operation controls of choice option page goes here */
		var self = this;

		/**
		 * The default error message.
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * Default status of factory.
		 * @type {[*]}
		 */
		self.statusList = [{id:"I",name:"INACTIVE"},{id:"A",name:"ACTIVE"}];

		/**
		 * Flag for waiting response from back end.
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The list of choice type information.
		 * @type {Array}
		 */
		self.factoryList = [];

		/**
		 * The list of choice type information Orig.
		 * @type {Array}
		 */
		self.factoryListOrig = [];

		/**
		 * The flag all choice option seleted.
		 * @type {boolean}
		 */
		self.checkAllFlag = false;
		/**
		 * Factory details.
		 */
		self.details = {
			isShowing: false
		};
		self.RETURN_TAB = 'returnTab';
		self.VALIDATE_FACTORY_DETAIL = 'validateFactoryDetail';
		self.VALIDATE_FACTORY = 'validateFactory';
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.isWaitingForResponse = true;
			self.findAllFactory();
			if(self.messageSending != ''){
				self.success = self.messageSending;
				self.messageSending = '';
			}
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
				$rootScope.isEditedOnPreviousTab = false;
			}
		};
		/**
		 * Find all the list of choice type information.
		 */
		self.findAllFactory = function () {
			factoryApi.findAllFactory(
				function (results) {
					self.isWaitingForResponse = false;
					angular.forEach(results, function(value) {
						value.factoryName=self.toTrimValue(value.factoryName);
						value.addressOne=self.toTrimValue(value.addressOne);
						value.addressTwo=self.toTrimValue(value.addressTwo);
						value.city=self.toTrimValue(value.city);
						value.county=self.toTrimValue(value.county);
						value.state=self.toTrimValue(value.state);
						value.country=self.toTrimValue(value.country);
						value.phone=self.toTrimValue(value.phone);
						value.fax=self.toTrimValue(value.fax);
						value.contactName=self.toTrimValue(value.contactName);
						value.contactEmail=self.toTrimValue(value.contactEmail);
						value["selected"] = self.checkAllFlag;
					});
					self.loadFactoryData(results);
				},
				function (error) {
					self.fetchError(error);
				}
			);
		};

		/**
		 * Saves the factory data, updates switches to add checks to factories, sets the ng table up.
		 * @param results
		 */
		self.loadFactoryData = function(results){
			$scope.filter = {
				displayName: undefined,
				status:undefined
			};
			self.factoryList = angular.copy(results);
			self.factoryListOrig = angular.copy(results);
			self.factoryTableParams = new ngTableParams(
				{
					page: 1,
					count:20,
					filter: $scope.filter
				}, {
					counts: [],
					data: self.factoryList
				}
			);
			self.updateFactoryList();
		};

		/**
		 * Loads the 1st pages.
		 */
		self.updateFactoryList = function(){
			if(null!=self.factoryTableParams){
				self.factoryTableParams.page(1);
				self.factoryTableParams.reload();
			}
		};

		/**
		 * Refresh flag check all in header grid. This flag show to user know, all row in current page has been
		 * selected.
		 */
		self.refreshCheckAllFlag = function () {
			self.checkAllFlag = true;
			for(var i=0; i<self.factoryList.length; i++){
				if(self.factoryList[i] && self.factoryList[i].selected == false){
					self.checkAllFlag = false;
					break;
				}
			}
		};

		/**
		 * User checked/unchecked in checkbox on header data tables.
		 */
		self.checkAllHandle = function () {
			angular.forEach(self.factoryList, function(value) {
				value.selected = self.checkAllFlag;
			});
		};

		/**
		 * Check element has been selected to delete.
		 * @returns {boolean}
		 */
		self.hasDataSelected = function () {
			for(var i=0; i<self.factoryList.length; i++){
				if(self.factoryList[i] && self.factoryList[i].selected == true){
					return true;
				}
			}
			return false;
		};

		/**
		 * go to factory detail page, show more information for user. User an edit, add new on here.
		 * @param tab
		 * @param item
		 */
		self.gotoFactoryDetails = function (tab, item) {
			if(item == null){
				item = self.createEmptyFactory();
			}
			self.success = null;
			self.error = null;
			self.details = item;
			self.details.isShowing = true;
		};

		self.createEmptyFactory = function () {
			var factory = {};
			factory["factoryName"] = '';
			factory["country"] = '';
			factory["city"] = '';
			factory["status"] = '';
			factory["addressOne"] = '';
			factory["addressTwo"] = '';
			factory["addressThree"] = '';
			factory["state"] = '';
			factory["county"] = '';
			factory["zip"] = '';
			factory["phone"] = '';
			factory["fax"] = '';
			factory["contactName"] = '';
			factory["contactEmail"] = '';
			return factory;
		};

		/**
		 * Clear all filter handle.
		 */
		self.clearFilter = function () {
			self.loadFactoryData(self.factoryList);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function(error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
			self.isWaitingForResponse = false;
		};

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function(error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return "An unknown error occurred.";
			}
		};

		//Update Delete only 1 record one time
		self.factoryToDelete = null;
		/**
		 * Delete factory button handle
		 */
		self.deleteFactory = function (factory) {
			self.factoryToDelete = angular.copy(factory);
			$('#confirmModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Do delete factory action.
		 */
		self.doDeleteFactory = function (factory) {
			$('#confirmModal').modal("hide");
			self.error = '';
			self.success = '';
			self.isWaitingForResponse = true;

			//call to controller handle delete the list of factory
			factoryApi.deleteFactories(self.createFactoriesList(factory),
				function (results) {
					self.isWaitingForResponse = false;
					self.factoryToDelete = null;
					self.success = results.message;
					self.factoryList = results.data;
				},
				function (error) {
					self.fetchError(error);
				}
			)
		};

		/**
		 * create list factory to delete
		 * @param factory
		 * @returns {Array}
		 */
		self.createFactoriesList = function(factory){
			var factories = [];
			factories.push(factory);
			return factories;
		}

		/**
		 * To trim a value of String.
		 * @param value
		 * @returns {*}
		 */
		self.toTrimValue = function (value) {
			if(value){
				return value.trim();
			}
			return value;
		}

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_FACTORY, function () {
			if(!self.details.isShowing){
				$rootScope.$broadcast(self.RETURN_TAB);
			} else {
				$rootScope.$broadcast(self.VALIDATE_FACTORY_DETAIL);
			}
		});

		/**
		 * Reload data after save factory success.
		 */
		$scope.$on('reloadDataAfterSaveFactory', function (event, message) {
			self.success = message;
			if(message != null){
				self.isWaitingForResponse = true;
				self.findAllFactory();
			}else{
				self.loadFactoryData(self.factoryList);
			}
		});
	}
})();
