/*
 *   druInfoComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';
/**
 * DruInfo -> Dru Info page component.
 *
 * @author s753601
 * @since 2.6.1
 */
(function () {
	angular.module('productMaintenanceUiApp').component('druInfo', {
		// isolated scope binding
		bindings: {
			itemMaster: '<',
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productCasePacks/druInfo.html',
		// The controller that handles our component logic
		controller: DruInfoController
	});
	DruInfoController.$inject = ['ProductCasePackApi', 'UserApi', 'ProductDetailAuditModal', '$rootScope',
		'ProductSearchService', '$filter'];
	function DruInfoController(productCasePackApi, userApi, ProductDetailAuditModal, $rootScope, productSearchService,
							   $filter) {
		/** All CRUD operation controls of Case pack Info page goes here */
		var self = this;
		/**
		 * Error Message
		 * @type {null}
		 */
		self.error = null;
		/**
		 * Original Data being received from item master and stored encase the user decides to reset the fields.
		 *
		 * @type {null}
		 */
		self.originalDRU = {
			displayReadyUnit: null,
			typeOfDRU: null,
			alwaysSubWhenOut: null,
			rowsFacing: null,
			rowsDeep: null,
			rowsHigh: null,
			orientation: null
		};
		/**
		 * Displayed DRU attributes that the user can manipulate.
		 *
		 * @type {null}
		 */
		self.currentDRU = null;
		/**
		 * This flag is set to determine if the fields on the DRU attribute form are editable or read only.
		 * @type {boolean}
		 */
		self.readOnly = true;
		/**
		 * This flag is set to show a loading spinning wheel while the application waits for a response from the api
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;
		/**
		 * Error message.
		 * @type {null}
		 */
		self.error = null;
		/**
		 * Success Message span content for when DRU attributes are successfully updated.
		 * @type {null}
		 */
		self.success=null;
		/**
		 * Flag set to ensure that the data set has changed.
		 * @type {boolean}
		 */
		self.enableSave = false;
		/**
		 * Array showing all the information for all the changes made to an items DRU attributes
		 * @type {Array}
		 */
		self.druAuditInfo=[];

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		}
		this.$onChanges = function () {
			self.isWaitingForResponse=true;
			self.error=null;
			self.success = null;
			var key ={
				itemCode:self.itemMaster.key.itemCode,
				itemType: self.itemMaster.key.itemType
			}
			productCasePackApi.getItemMaster(key, self.getData, self.fetchError);
		};
		this.$onDestroy = function () {
		};
		/**
		 * This will grab the latest ItemMaster.
		 * @param results
		 */
		self.getData=function (results) {
			self.itemMaster = results;
			self.getDRUAttributes();
		};
		/**
		 * This method will isolate the DRU data and store it encase the user decides to reset the fields the original
		 * data isn't lost.
		 */
		self.getDRUAttributes = function () {
			self.originalDRU.displayReadyUnit = self.itemMaster.displayReadyUnit;
			if (self.itemMaster.displayReadyUnitDisplayName != null) {
				self.originalDRU.typeOfDRU = self.itemMaster.displayReadyUnitDisplayName.trim();
			} else {
				self.originalDRU.typeOfDRU ="";
			}
			self.originalDRU.alwaysSubWhenOut = self.itemMaster.alwaysSubWhenOut;
			self.originalDRU.rowsFacing = self.itemMaster.rowsFacing;
			self.originalDRU.rowsDeep = self.itemMaster.rowsDeep;
			self.originalDRU.rowsHigh = self.itemMaster.rowsHigh;
			self.originalDRU.orientation = self.itemMaster.orientation.toString();
			self.currentDRU = angular.copy(self.originalDRU);
			self.isWaitingForResponse = false;
			self.checkChange();
		};
		/**
		 * This function will reset all of the current DRU attributes back to the original values that are currently in
		 * the DB.
		 */
		self.reset = function () {
			$rootScope.contentChangedFlag = false;
			self.currentDRU = angular.copy(self.originalDRU);
		};
		/**
		 * This function handles the logic for when the display ready unit is clicked.  This method will prevent erroneous
		 * data from being saved.  It will ensure that non-DRU's have no type and that the if the user accidentally clicks
		 * the DRU checkbox when they recheck it the original value will be returned.
		 */
		self.toggleDisplayReadyUnit = function () {
			if(self.currentDRU.displayReadyUnit){
				self.currentDRU.typeOfDRU = self.originalDRU.typeOfDRU;
			} else {
				self.currentDRU.typeOfDRU = " ";
			}
			self.checkChange();
		};
		/**
		 * This is used to update the DRU attributes in the database. Executed after pressing save button.
		 */
		self.saveDRUInfo = function () {
			self.error=null;
			self.success = null;
			var updatedDRU=self.currentDRU;
			var key={itemType:self.itemMaster.key.itemType,
				itemCode:self.itemMaster.key.itemCode};
			updatedDRU.key = key;
			self.isWaitingForResponse=true;
			productCasePackApi.saveDRU(updatedDRU, self.loadData, self.fetchError);
		};
		/**
		 * After updating the database get the latest ItemNaster
		 * @param results
		 */
		self.loadData = function (results) {
			self.isWaitingForResponse=false;
			self.success = results.message;
			self.itemMaster=results.data;
			self.getDRUAttributes();
		};
		/**
		 * Fetches the error from the back end.
		 *
		 * @param error
		 */
		self.fetchError = function(error) {
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
		 * Makes a call to the API to get the Audit information for the DRU view
		 */
		self.showDruAuditInfo = function () {
			console.log('Getting DRU Audits');
			self.druAuditInfo = productCasePackApi.getDruAudits;
			console.log(self.druAuditInfo);
			var title ="Case Pack DRU";
			ProductDetailAuditModal.open(self.druAuditInfo, self.itemMaster.key, title);
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
		 * Wheneever a DRU is changed this will check to verify it is different from the original DRU values. If the
		 * DRU is back to its original state save will be disabled otherwise(DRU attributes have changed) then save
		 * will be enabled.
		 */
		self.checkChange = function () {
			$rootScope.contentChangedFlag = true;
			self.enableSave = true;
			if(angular.equals(self.currentDRU, self.originalDRU)){
				$rootScope.contentChangedFlag = false;
				self.enableSave = false;
			}
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};

		/**
		 * Returns the added date, or null value if date doesn't exist.
		 */
		self.getAddedDate = function() {
			if(self.itemMaster.createdDateTime === null || angular.isUndefined(self.itemMaster.createdDateTime)) {
				return '01/01/1901 00:00';
			} else if (parseInt(self.itemMaster.createdDateTime.substring(0, 4)) < 1900) {
				return '01/01/0001 00:00';
			} else {
				return $filter('date')(self.itemMaster.createdDateTime, 'MM/dd/yyyy HH:mm');
			}
		};

		/**
		 * Returns createUser or '' if not present.
		 */
		self.getCreateUser = function() {
			if(self.itemMaster.displayCreatedName === null || self.itemMaster.displayCreatedName.trim().length == 0) {
				return '';
			}
			return self.itemMaster.displayCreatedName;
		};
	}
})();
