/*
 *
 * productGroupTypeComponent.js
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
 * The controller for the Product Group Type Controller.
 * @author vn87351
 * @since 2.12.0
 */
(function () {

	var app = angular.module('productMaintenanceUiApp');
	app.component('productGroupTypeComponent', {
		templateUrl: 'src/codeTable/productGroupType/productGroupType.html',
		bindings: { seleted: '<' },
		controller: ProductGroupTypeController
	});
	app.filter('trim', function () {
		return function (value) {
			if (!angular.isString(value)) {
				return value;
			}
			return value.replace(/^\s+|\s+$/g, ''); // you could use .trim, but it's not going to work in IE<9
		};
	});

	ProductGroupTypeController.$inject = ['$rootScope','codeTableApi', '$scope', 'ngTableParams', '$filter', 'ProductGroupService', 'customHierarchyService'];

	/**
	 * Constructs the controller.
	 */
	function ProductGroupTypeController($rootScope,codeTableApi, $scope, ngTableParams, $filter, productGroupService, customHierarchyService) {

		var self = this;
		self.details = {
			isShowing: false
		};
		/**
		 * The unknown error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * The default no records found message.
		 *
		 * @type {string}
		 */
		self.NO_RECORDS_FOUND = "No records found.";

		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * check all flag.
		 * @type {boolean}
		 */
		self.checkAllValue = false;

		/**
		 * list data product group type.
		 * @type {Array}
		 */
		self.productGroupType = [];

		/**
		 * Product Group Type Delete.
		 * @type {Object}
		 */
		self.productGroupTypeDelete = {}

		self.VALIDATE_PRODUCT_GROUP_TYPE = 'validateProductGroupType';
		self.RETURN_TAB = 'returnTab';

		/**
		 * The error message.
		 *
		 * @type {string}
		 */
		self.error = '';
		/**
		 * Initiates the construction of the product Group Type Controller.
		 */
		self.init = function () {
			self.isWaiting = true;
			self.findAllProductGroupType();
			self.checkAllValue = false;
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
				$rootScope.isEditedOnPreviousTab = false;
			}
		};

		/**
		 * get product Group Type
		 */
		self.findAllProductGroupType = function () {
			codeTableApi.findAllProductGroupTypes().$promise.then(function (results) {
				if (!results.length) {
					self.productGroupType = [];
					self.error = self.NO_RECORDS_FOUND;
				} else {
					self.error = null;
					self.productGroupType = results;
					self.filterDatas();
					if(productGroupService.getNavFromProdGrpTypePageFlag()){
						var productGroupTypeTemp = {};
						for(var i=0; i < self.productGroupType.length; i++){
							if(self.productGroupType[i].productGroupTypeCode == productGroupService.getProductGroupTypeCodeNav()){
								productGroupTypeTemp = self.productGroupType[i];
							}
						}
						productGroupService.setNavFromProdGrpTypePageFlag(false);
						productGroupService.setProductGroupTypeCodeNav(null);
						self.showDetails(productGroupTypeTemp);
					}else if(customHierarchyService.getNavigateFromProdGroupTypePage()){
						var productGroupTypeTemp = {};
						for(var i=0; i < self.productGroupType.length; i++){
							if(self.productGroupType[i].productGroupTypeCode == customHierarchyService.getProductGroupTypeCodeNav()){
								productGroupTypeTemp = self.productGroupType[i];
							}
						}
						customHierarchyService.setNavigateFromProdGroupTypePage(false);
						customHierarchyService.setDisableReturnToList(true);
						customHierarchyService.setProductGroupTypeCodeNav(null);
						self.showDetails(productGroupTypeTemp);
					}
				}
				self.isWaiting = false;
			});
		};

		/**
		 * filter data product Group Type
		 */
		self.filterDatas = function () {
			$scope.filter = {
				productGroupTypeSummary: undefined
			};
			$scope.tableParams = new ngTableParams({
				page: 1,
				count: 20,
				filter: $scope.filter
			}, {
					counts: [],
					debugMode: true,
					data: self.productGroupType
				});
		};

		/**
		 *If there is an error this will display the error
		 * @param error
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				if (error.data.message) {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			} else {
				self.error = self.UNKNOWN_ERROR;
			}
		};

		/**
		 * Show product group type details.
		 *
		 * @param productGroupType selected productGroupType.
		 */
		self.showDetails = function (productGroupType) {
			self.success = '';
			self.error = '';
			self.details.isShowing = true;
			self.details.productGroupType = productGroupType;
			self.details.productGroupTypes = self.productGroupType;
		};

		/**
		 * Show add new product group type.
		 */
		self.addNewProductGroupType = function () {
			self.success = '';
			self.error = '';
			var productGroupType = {
				customerProductGroups: null,
				departmentNumberString: "",
				productGroupChoiceTypes: null,
				productGroupType: "",
				productGroupTypeCode: "",
				productGroupTypeSummary: "",
				subDepartmentId: "",
			}
			self.details.isShowing = true;
			self.details.productGroupType = productGroupType;
			self.details.productGroupTypes = [];
		};

		/**
		 *Show modal delete Product Group Type.
		 */
		self.deleteProductGroupChoiceTypes = function (productGroupTypeDelete) {
			self.productGroupTypeDelete = productGroupTypeDelete;
			self.error = '';
			self.success = '';
			self.details.success = '';
			$('#common-confirm-modal').modal({ backdrop: 'static', keyboard: true });
		}

		/**
		 * Do delete Product Group Type.
		 */
		self.doDeleteProductGroupChoiceTypes = function () {
			self.isWaiting = true;
			codeTableApi.deleteProductGroupTypes(
				self.productGroupTypeDelete,
				function (results) {
					self.isWaiting = false;
					self.checkAllValue = false;
					self.success = results.message;
					self.productGroupType = results.data;
					self.filterDatas();
				},
				function (error) {
					self.fetchError(error);
				}
			);
		}
		$scope.$on(self.VALIDATE_PRODUCT_GROUP_TYPE, function () {
			if (!self.details.isShowing) {
				$rootScope.$broadcast(self.RETURN_TAB);
			}
		})
		self.reLoadData = function () {
			self.details.isLoadData = false;
			self.findAllProductGroupType();
		}
	}
})();
