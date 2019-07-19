/*
 *   factoryDetailComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table -> factory -> factory detail page component.
 *
 * @author vn70516
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('factoryDetails', {
		bindings:{
			itemSending: '=',
			arraySending: '='
		},
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/factory/factoryDetails.html',
		// The controller that handles our component logic
		controller: FactoryDetailsController
	});

	FactoryDetailsController.$inject = ['$rootScope', '$scope', 'FactoryApi'];

	/**
	 * Code Table factory component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @constructor
	 */
	function FactoryDetailsController($rootScope, $scope, factoryApi) {
		/** All CRUD operation controls of choice option page goes here */
		var self = this;

		/**
		 * The default error message.
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

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
		 * The flag all choice option seleted.
		 * @type {boolean}
		 */
		self.checkAllFlag = false;

		/**
		 * The list of information contry.
		 * @type {Array}
		 */
		self.countries = []

		/**
		 * Default status of factory.
		 * @type {[*]}
		 */
		self.statusList = [{id:"I",name:"INACTIVE"},{id:"A",name:"ACTIVE"}];

		/**
		 * Factory information backup.
		 * @type {{}}
		 */
		self.factoryOrig = {};

		/**
		 * Index of current element.
		 * @type {number}
		 */
		self.currentIndex = null;

		self.isReturnToTab = false;
		self.RETURN_TAB = 'returnTab';
		self.VALIDATE_FACTORY_DETAIL = 'validateFactoryDetail';
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";

		/**
		 * The flag return to list Product Group Type.
		 *
		 * @type {boolean}
		 */
		self.isReturnToList = false;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.factoryOrig = angular.copy(self.itemSending);
			self.isWaitingForResponse = true;
			self.findIndexOfCurrentElement();
			//call to controller handle delete the list of factory
			factoryApi.findAllCountry(
				//success
				function (results) {
					self.isWaitingForResponse = false;
					angular.forEach(results, function(value) {
						if(value.countryName){
							value.countryName = value.countryName.trim();
						}
					});
					self.countries = results;
				},
				//error
				self.fetchError
			)
		};

		/**
		 * Find index of current element factory.
		 */
		self.findIndexOfCurrentElement = function () {
			if(self.itemSending && self.itemSending.factoryId){
				for(var i=0;i<self.arraySending.length;i++){
					if(self.arraySending[i].factoryId == self.itemSending.factoryId){
						self.currentIndex = i;
						break;
					}
				}
			}
		}

		/**
		 * Check current factory is first element
		 * @returns {boolean}
		 */
		self.previousDisable = function () {
			if(self.currentIndex != null && self.currentIndex >0){
				return false;
			}else{
				return true;
			}
		}

		/**
		 * Check current factory is last element
		 * @returns {boolean}
		 */
		self.nextDisable = function () {
			if(self.currentIndex != null && self.currentIndex < self.arraySending.length -1){
				return false;
			}else{
				return true;
			}
		}

		/**
		 * Return to factory search page.
		 */
		self.returnToList = function () {
			self.itemSending.isShowing = false;
			$rootScope.$broadcast('reloadDataAfterSaveFactory', null);
		}

		/**
		 * Data is changed.
		 * @returns {boolean}
		 */
		self.isDataChanged = function () {
			if(angular.toJson(self.itemSending) != angular.toJson(self.factoryOrig)){
				return true;
			}
			return false;
		}

		/**
		 * Go to previous Factory
		 */
		self.gotoPreviousFactory = function () {
			if (self.isDataChanged()) {
				self.isGotoPreviousFactory = true;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				if(self.isGotoPreviousFactory){
					self.isGotoPreviousFactory = false;
				} else{
					self.error = '';
					self.success = '';
				}
			self.itemSending = self.arraySending[self.currentIndex - 1];
				self.itemSending.isShowing = true;
			self.factoryOrig = angular.copy(self.itemSending);
			self.currentIndex = self.currentIndex - 1;
		}
		}

		/**
		 * Go to next Factory
		 */
		self.gotoNextFactory = function () {
			if (self.isDataChanged()) {
				self.isGotoNextFactory = true;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				if(self.isGotoNextFactory){
					self.isGotoNextFactory = false;
				} else{
					self.error = '';
					self.success = '';
				}
			self.itemSending = self.arraySending[self.currentIndex + 1];
				self.itemSending.isShowing = true;
			self.factoryOrig = angular.copy(self.itemSending);
			self.currentIndex = self.currentIndex + 1;
		}
		}

		/**
		 * Reset data
		 */
		self.resetFactory = function () {
			self.itemSending = angular.copy(self.factoryOrig);
		}

		/**
		 * Save data factory
		 */
		self.saveFactory = function () {
			self.isWaitingForResponse = true;
			$('#changeTabConfirmation').modal('hide');
			if(self.itemSending.factoryId){
				//call to controller handle delete the list of factory
				factoryApi.updateFactory(self.itemSending,
					//success
					function (results) {
						self.isWaitingForResponse = false;
						self.factoryOrig = angular.copy(self.itemSending);
						angular.forEach(results.data, function(value) {
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
						});
						self.arraySending = angular.copy(results.data);
						self.success = results.message;
						if(self.isReturnToList){
							self.itemSending.isShowing = false;
							$rootScope.$broadcast('reloadDataAfterSaveFactory', results.message);
						}
						if(self.isGotoNextFactory){
							self.gotoNextFactory();
						}
						if(self.isGotoPreviousFactory){
							self.gotoPreviousFactory();
						}
						if(self.isReturnToTab){
							$rootScope.success = self.success;
							$rootScope.isEditedOnPreviousTab = true;
						}
						self.returnToTab();
					},
					//error
					self.fetchError
				)
			}else{
				//call to controller handle delete the list of factory
				factoryApi.addNewFactory(self.itemSending,
					//success
					function (results) {
						self.isWaitingForResponse = false;
						self.itemSending.isShowing = false;
						$rootScope.$broadcast('reloadDataAfterSaveFactory', results.message);
					},
					//error
					self.fetchError
				)
			}
		}

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
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
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
				return self.UNKNOWN_ERROR;
			}
		};

		/**
		 * This method is used to return to the selected tab.
		 */
		self.returnToTab = function () {
			if (self.isReturnToTab) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_FACTORY_DETAIL, function () {
			if(self.itemSending.isShowing){
			if (self.isDataChanged()) {
				self.isReturnToTab = true;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
			}
		});

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			$('#changeTabConfirmation').modal('hide');
			self.itemSending = angular.copy(self.factoryOrig);
			self.arraySending[self.currentIndex] = self.itemSending;
			if (self.isReturnToTab) {
				$('#changeTabConfirmation').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
			}else if (self.isReturnToList) {
				$('#changeTabConfirmation').on('hidden.bs.modal', function () {
					self.returnToList();
					$scope.$apply();
				});
			}else if (self.isGotoNextFactory){
				self.isGotoNextFactory = false;
				self.gotoNextFactory();
			}else if(self.isGotoPreviousFactory){
				self.isGotoPreviousFactory = false;
				self.gotoPreviousFactory();
			}
		};

		/**
		 * Hides save confirm dialog.
		 */
		self.cancelConfirmDialog = function () {
			$('.modal-backdrop').attr('style', '');
			$('#changeTabConfirmation').modal('hide');
			self.isReturnToList = false;
			self.isReturnToTab = false;
			self.isGotoNextFactory = false;
			self.isGotoPreviousFactory = false;
		};

		/**
		 * Handle return to list.
		 */
		self.returnToListClick = function () {
			if (self.isDataChanged()) {
				self.isReturnToList = true;
				self.error = '';
				self.success = '';
				$('#changeTabConfirmation').modal({backdrop: 'static', keyboard: true});
			} else {
				self.returnToList();
			}
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
	}
})();
