/*
 *   productCategoryComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table ->product category page component.
 *
 * @author vn70529
 * @since 2.12.0
 */
(function () {
	var app = angular.module('productMaintenanceUiApp');
	app.component('productCategory', {
		scope: '=',
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/codeTable/productCategory/productCategory.html',
		bindings: {seleted: '<'},
		// The controller that handles our component logic
		controller: ProductCategoryController
	});

	ProductCategoryController.$inject = ['$rootScope', '$scope', 'codeTableApi', 'ngTableParams'];

	/**
	 * Case Pack Warehouse component's controller definition.
	 * @param $scope    scope of the case pack info component.
	 * @param codeTableApi
	 * @param ngTableParams
	 * @param $filter
	 * @constructor
	 */
	function ProductCategoryController($rootScope, $scope, codeTableApi, ngTableParams) {
		/** All CRUD operation controls of product category page goes here */

		var self = this;

		/**
		 * Messages
		 * @type {string}
		 */
		self.MESSAGE_CONFIRM_CLOSE = "Unsaved data will be lost. Do you want to save the changes before continuing?";

		self.MESSAGE_NO_DATA_CHANGE = 'There are no changes on this page to be saved. Please make any changes to update.';

		self.MESSAGE_CONFIRM_DELETE = "Are you sure you want to delete the selected Product Category?";

		/**
		 * action code
		 */
		self.DELETE_ACTION = 'DELETE';
		self.ADD_ACTION = 'ADD';
		self.EDIT_ACTION = 'EDIT';


		/**
		 * Array for contain data has return from api
		 * @type {Array}
		 */
		self.productCategories = [];
		self.marketConsumerEventTypes = [];
		self.productCategoryRoles = [];
		self.productCategoriesHandle = null;
		self.VALIDATE_PRODUCT_CATEGORY = 'validateProductCategory';
		self.RETURN_TAB = 'returnTab';
		self.isReturnToTab = false;

		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = true;
		self.hasOtherRowEditing = false;
		self.productCategoryDelete = null;
		self.selectedRowId = null;
		self.selectedRowIndex = -1;
		self.confirmHeaderTitle = 'Confirmation';
		self.PAGE_SIZE = 20;

		/**
		 * Initiates the construction of the product category Controller.
		 */
		self.init = function () {
			self.getAllProductCategories();
			self.getAllMarketConsumerEventTypes();
			self.getAllProductCategoryRoles();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
			}
			$rootScope.isEditedOnPreviousTab = false;
		};

		/**
		 * get all product category
		 */
		self.getAllProductCategories = function () {
			self.isWaiting = true;
			codeTableApi.getAllProductCategories().$promise.then(function (response) {
				self.productCategories = response;
				self.loadData();
				self.isWaiting = false;
			}, self.fetchError);
		};

		/**
		 * get all market consumer event type
		 */
		self.getAllMarketConsumerEventTypes = function () {
			codeTableApi.getAllMarketConsumerEventTypes().$promise.then(function (response) {
				self.marketConsumerEventTypes = response;
			});
		};

		/**
		 * get all product category role
		 */
		self.getAllProductCategoryRoles = function () {
			codeTableApi.getAllProductCategoryRoles().$promise.then(function (response) {
				self.productCategoryRoles = response;
			});
		};

		/**
		 *If there is an error this will display the error
		 * @param error
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
				self.error = "An unknown error occurred.";
			}
			if(self.isReturnToTab){
				$rootScope.error = self.error;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.returnToTab();
			self.resetAllFlag();
			self.loadData();
		};

		/**
		 * Load data and show on ui
		 */
		self.loadData = function () {
			$scope.filter = {
				productCategoryAbb: undefined,
				productCategorySummary: undefined,
				marketConsumerEventType: {
					marketConsumerEventTypeSummary: undefined
				},
				productCategoryRole: {
					productCategoryRoleSummary: undefined
				}
			};
			$scope.tableParams = new ngTableParams({
				page: 1,
				count: self.PAGE_SIZE,
				filter: $scope.filter
			}, {
				counts: [],
				data: self.initDataForNgTable()
			});
		};

		self.initDataForNgTable = function(){
			angular.forEach(self.productCategories,function (productCategory) {
				productCategory.productCategoryAbb = productCategory.productCategoryAbb.trim();
				productCategory.productCategoryName = productCategory.productCategoryName.trim();
				productCategory.isEditing = false;
			});
			return self.productCategories;
		};

		/**
		 * Select record to edit and enable it.
		 *
		 * @param productCategory
		 */
		self.enableRow = function(productCategory){
			self.error = '';
			self.success = '';
			productCategory.isEditing = true;
			self.hasOtherRowEditing = true;
			self.selectedRowId = productCategory.productCategoryId;
			self.selectedRowIndex = self.productCategories.indexOf(productCategory);
			self.selectedRow = angular.copy(productCategory);
			$scope.tableParams.reload();
		};

		/**
		 * reset data of record
		 */
		self.resetCurrentRow = function(){
			self.error = '';
			self.success = '';
			self.productCategories[self.selectedRowIndex] = angular.copy(self.selectedRow);
			self.productCategories[self.selectedRowIndex].isEditing = false;
			self.resetAllFlag();
			$scope.tableParams.reload();
		};

		/**
		 * reset all flags
		 */
		self.resetAllFlag = function(){
			self.hasOtherRowEditing = false;
			self.productCategoryDelete = null;
			self.selectedRowId = null;
			self.selectedRowIndex = -1;
			self.selectedRow = null;
		};

		/**
		 * check if data is change
		 * @param productCategory
		 * @returns {boolean}
		 */
		self.hasDataChanged = function(productCategory){
			if (productCategory !== null && productCategory !== undefined){
				if (productCategory.productCategoryAbb === self.selectedRow.productCategoryAbb
					&& productCategory.productCategoryName === self.selectedRow.productCategoryName
					&& productCategory.marketConsumerEventCode === self.selectedRow.marketConsumerEventCode
					&& productCategory.productCategoryRoleCode === self.selectedRow.productCategoryRoleCode){
					return false;
				} else {
					return true;
				}
			}
			return false;
		};

		/**
		 * create object for request add or update data
		 * @param productCategory
		 * @returns {Array}
		 */
		self.createDataForRequestApi = function(productCategory){
			var productCategories = [];
			var productCategoryRequest = {
				productCategoryId: productCategory.productCategoryId,
				productCategoryAbb: productCategory.productCategoryAbb,
				productCategoryName: productCategory.productCategoryName,
				marketConsumerEventCode: productCategory.marketConsumerEventCode,
				productCategoryRoleCode: productCategory.productCategoryRoleCode
			};
			productCategories.push(productCategoryRequest);
			return productCategories;
		};

		/**
		 * saving data after edit
		 * @param productCategory
		 */
		self.saveData = function(productCategory){
			$('#confirmationModalSave').modal("hide");
			self.error = self.MESSAGE_NO_DATA_CHANGE;
			if (self.hasDataChanged(productCategory)){
				if (self.validationBeforeSave(productCategory)){
					self.callApiToSave(self.createDataForRequestApi(productCategory), self.EDIT_ACTION);
				}
			} else {
				self.error = self.MESSAGE_NO_DATA_CHANGE;
			}
		};

		/**
		 * show popup to confirmation delete
		 * @param productCategory
		 */
		self.showPopupConfirmDelete = function(productCategory){
			self.productCategoryDelete = angular.copy(productCategory);
			$('#confirmationDelete').modal({backdrop: 'static', keyboard: true});
			$('.modal-backdrop').attr('style', ' z-index: 100000; ');
		};

		/**
		 * hide popup and call api to delete data.
		 */
		self.doDeleteData = function(){
			$('#confirmationDelete').modal("hide");
			if (self.productCategoryDelete !== null){
				self.callApiToSave(self.createDataForRequestApi(self.productCategoryDelete), self.DELETE_ACTION);
			}
		};

		/**
		 * closing popup confirmation.
		 */
		self.closePopupConfirmation = function () {
			self.productCategoryDelete = null;
			$('#confirmationDelete').modal("hide");
		};

		/**
		 * call api to save data.
		 * @param productCategories
		 * @param action
		 */
		self.callApiToSave = function(productCategories, action){
			self.isWaiting = true;
			self.error = '';
			self.success = '';
			if(action === self.EDIT_ACTION){
				codeTableApi.updateProductCategory(
					productCategories,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			} else if (action === self.DELETE_ACTION){
				codeTableApi.deleteProductCategory(
					productCategories,
					function (results) {
						self.callApiSuccess(results)
					},
					function (error) {
						self.fetchError(error);
					});
			} else if(action === self.ADD_ACTION){
				codeTableApi.addProductCategory(
					productCategories,
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
			self.productCategories = results.data;
			self.success = results.message;
			if(self.isReturnToTab){
				$rootScope.success = self.success;
				$rootScope.isEditedOnPreviousTab = true;
			}
			self.loadData();
			self.resetAllFlag();
			self.returnToTab();
		};

		/**
		 * Check all field is valid before add new or update product category
		 * @returns {boolean}
		 */
		self.validationBeforeSave = function (productCategory) {
			self.error = '';
			if (self.isNullOrEmpty(productCategory.productCategoryName)) {
				productCategory.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Product Category Name is a mandatory field.</li>';
			}
			if (self.isNullOrEmpty(productCategory.marketConsumerEventCode)) {
				productCategory.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Consumer Market Event Code is a mandatory field.</li>';
			}
			if (self.isNullOrEmpty(productCategory.productCategoryRoleCode)) {
				productCategory.addClass = 'active-tooltip ng-invalid ng-touched';
				self.error += '<li>Product Category Role is a mandatory field.</li>';
			}
			if (self.error !== ''){
				self.error = 'Product Category:' + self.error;
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
		 * Clear filter.
		 */
		self.clearFilter = function () {
			$scope.filter.productCategoryAbb = undefined;
			$scope.filter.productCategorySummary = undefined;
			$scope.filter.marketConsumerEventType.marketConsumerEventTypeSummary = undefined;
			$scope.filter.productCategoryRole.productCategoryRoleSummary = undefined;
		};

		/**
		 *
		 * @param productCategoryId
		 * @returns {*}
		 */
		self.getButtonStyle = function(productCategoryId){
			if (self.selectedRowId !== null && productCategoryId !== self.selectedRowId){
				return 'opacity: 0.5;'
			}
			return 'opacity: 1.0;';
		};

		//For Add
		/**
		 * Show Popup to add new Product Category
		 */
		self.addProductCategory = function () {
			self.error = '';
			self.success = '';
			self.errorPopup = '';
			self.resetCSS();
			$('#addEditContainer').attr('style', '');
			self.productCategoriesHandle = [];
			var productCategory = self.createProductCategoryEmpty();
			self.productCategoriesHandle.push(productCategory);
			self.productCategoriesHandleOrig = angular.copy(self.productCategoriesHandle);
			self.titleModel = "Add New Product Category";
			$('#productCategoryModal').modal({backdrop: 'static', keyboard: true});
		};

		/**
		 * Create a empty choice type object.
		 * @returns {{}}
		 */
		self.createProductCategoryEmpty = function () {
			var productCategory = {};
			productCategory["productCategoryId"] = null;
			productCategory["productCategoryAbb"] = "";
			productCategory["productCategoryName"] = "";
			productCategory["marketConsumerEventCode"] = "";
			productCategory["productCategoryRoleCode"] = "";
			return productCategory;
		};

		/**
		 * Add one more row to add product category
		 */
		self.addMoreRowProductCategory = function () {
			self.errorPopup = '';
			if (self.validationBeforeAdd()) {
				var productCategory = self.createProductCategoryEmpty();
				self.productCategoriesHandle.push(productCategory);
				self.showScrollToAddEditForm();
				scrollToBottom();
			}
		};

		/**
		 * Scroll to bottom.
		 */
		function scrollToBottom() {
			if (self.productCategoriesHandle.length > self.TOTAL_ROWS_TO_SHOW_SCROLL) {
				setTimeout(function () {
					var element = document.getElementById('addEditContainer');
					element.scrollTop = element.scrollHeight - element.clientHeight;
				}, 200);
			}
		}

		/**
		 * Add scroll to addedit form when the total of rows are greater than 15 rows.
		 */
		self.showScrollToAddEditForm = function () {
			if (self.productCategoriesHandle.length > self.TOTAL_ROWS_TO_SHOW_SCROLL) {
				$('#addEditContainer').attr('style', 'overflow-y: auto;height:500px');
			} else {
				$('#addEditContainer').attr('style', '');
			}
		};

		/**
		 * Check all field is valid before add new or update product category
		 * @returns {boolean}
		 */
		self.validationBeforeAdd = function () {
			var messageContent = '';
			for (var i = 0, length = self.productCategoriesHandle.length ; i < length; i++) {
				var productCategory = self.productCategoriesHandle[i];
				if (self.isNullOrEmpty(productCategory.productCategoryName)) {
					self.productCategoriesHandle[i].addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Product Category Name is a mandatory field.</li>';
				}
				if (self.isNullOrEmpty(productCategory.marketConsumerEventCode)) {
					self.productCategoriesHandle[i].addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Consumer Market Event Code is a mandatory field.</li>';
				}
				if (self.isNullOrEmpty(productCategory.productCategoryRoleCode)) {
					self.productCategoriesHandle[i].addClass = 'active-tooltip ng-invalid ng-touched';
					messageContent += '<li>Product Category Role is a mandatory field.</li>';
				}
				if (messageContent !== '') {
					self.errorPopup = "Product Category:" + messageContent;
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
				$('#productCategoryModal').modal("hide");
			}
		};

		/**
		 * Check data has changed
		 *
		 * @returns {boolean} Return true if data has change. Otherwise return false.
		 */
		self.hasDataToAdd = function () {
			for(var i=0, length = self.productCategoriesHandle.length; i < length; i++){
				if (!self.isNullOrEmpty(self.productCategoriesHandle[i].productCategoryAbb)
					|| !self.isNullOrEmpty(self.productCategoriesHandle[i].productCategoryName)
					|| !self.isNullOrEmpty(self.productCategoriesHandle[i].productCategoryRoleCode)
					|| !self.isNullOrEmpty(self.productCategoriesHandle[i].marketConsumerEventCode)){
					return true;
				}
			}
			return false;
		};

		/**
		 *
		 */
		self.doAdd = function () {
			if (self.validationBeforeAdd()) {
				$('#productCategoryModal').modal("hide");
				$('#confirmationModalSave').modal("hide");
				self.isWaiting = true;
				self.callApiToSave(self.productCategoriesHandle, self.ADD_ACTION);
			}
		};

		/**
		 * Close Popup
		 */
		self.doClosePopupConfirmation = function () {
			$('#confirmationModalSave').modal("hide");
			$('#productCategoryModal').modal("hide");
			if (self.isReturnToTab) {
				$('#confirmationModalSave').on('hidden.bs.modal', function () {
					self.returnToTab();
					$scope.$apply();
				});
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
		 * remove property of object product category has used for setting style
		 */
		self.resetCSS = function(){
			angular.forEach(self.productCategoriesHandle, function (value) {
				if(value.addClass){
					delete value.addClass;
				}
			});
		};

		/**
		 * Clear message listener.
		 */
		$scope.$on(self.VALIDATE_PRODUCT_CATEGORY, function () {
			if (self.selectedRow !== null && self.hasDataChanged(self.findProductCategoryById(self.selectedRowId))) {
				self.isReturnToTab = true;
				// show popup
				self.confirmHeaderTitle = 'Confirmation';
				self.error = '';
				self.success = '';
				self.messageConfirm = self.MESSAGE_CONFIRM_CLOSE;
				$('#confirmationModalSave').modal({backdrop: 'static', keyboard: true});
			} else {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		});

		/**
		 * find productCategory object in array by productCategoryId
		 * @param productCategoryId
		 * @returns {*}
		 */
		self.findProductCategoryById = function(productCategoryId){
			for(var i = 0; i < self.productCategories.length; i++) {
				if (self.productCategories[i].productCategoryId === productCategoryId){
					return self.productCategories[i];
				}
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
		 * Reset validation.
		 */
		self.resetValidation = function(){
			$scope.addForm.$setPristine();
			$scope.addForm.$setUntouched();
			$scope.addForm.$rollbackViewValue();
		};
	}
})();
