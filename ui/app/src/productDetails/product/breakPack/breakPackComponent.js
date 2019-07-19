/*
 *   breakPackComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';


/**
 * Product Details -> Product -> break pack page component.
 *
 * @author vn70516
 * @since 2.15.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('breakPack', {
		bindings: {
			productMaster: '<',
			breakPackLabel:'='
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/product/breakPack/breakPack.html',
		// The controller that handles our component logic
		controller: BreakPackController
	});

	BreakPackController.$inject = ['BreakPackApi', 'UserApi', '$scope', '$timeout', '$filter', '$sce', '$rootScope',
		'$state', 'appConstants', 'ngTableParams', 'ProductSearchService'];

	function BreakPackController(breakPackApi, userApi, $scope, $timeout, $filter, $sce, $rootScope, $state, appConstants,
								 ngTableParams , productSearchService) {
		var self = this;

		/**
		 * The flag loading data. when flag is true show loading spinner to wait data from back end.
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Return error message when handle in screen.
		 * @type {string}
		 */
		self.error = "";

		/**
		 * Return success message when handle in screen.
		 * @type {string}
		 */
		self.success = "";

		/**
		 * The column defs for outers data grid. (field name and header display)
		 * @type {[*]}
		 */
		self.col_defs = [{field: "productId", displayName:"Product ID"},
			{field: "description", displayName:"Description"},
			{field: "size",displayName: "Size"},
			{field: "productQuantity",displayName: "Selling Units"}];

		/**
		 * The column name is root
		 * @type {string}
		 */
		self.expanding_property = {field: "upc", displayName:"UPC"};

		/**
		 * The array contain outers break pack data provider.
		 * @type {Array}
		 */
		self.breakPackData = [];

		/**
		 * The array contain inners break pack data provider.
		 * @type {Array}
		 */
		self.innersBreakPackData = [];

		/**
		 * The array contain inners break pack data provider to backup.
		 * @type {Array}
		 */
		self.innersBreakPackDataOrig = [];

		/**
		 * Flag check all row on inners product break pack grid.
		 * @type {boolean}
		 */
		self.checkAllFlag = false;
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

		/**
		 * RegExp check number is integer type, and max value is 99999
		 * @type {{}}
		 */
		self.regexIntQuantity = new RegExp("^[0-9]{1,5}$");

		/**
		 * RegExp check number is integer type, and max value is 9999999999999999999
		 * @type {{}}
		 */
		self.regexIntId = new RegExp("^[0-9]{1,19}$");

		/**
		 * Max length for product quantity.
		 * @type {number}
		 */
		self.MAX_LENGTH_QUANTITY = 5;

		/**
		 * Max length for upc number
		 * @type {number}
		 */
		self.MAX_LENGTH_LONG = 19;
		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};
		/**
		 * On load break pack screen. Call to back end to get data when on load screen.
		 */
		this.$onChanges= function () {
			self.getProductBreakPackByProductId();
		};

		/**
		 * Call back end to get product break pack data by product id.
		 */
		self.getProductBreakPackByProductId = function () {
			self.isWaitingForResponse = true;
			breakPackApi.getProductBreakPackByProductId(
				{
					productId: self.productMaster.prodId
				},
				//success case
				function (results) {
					self.isWaitingForResponse = false;
					if(results != null && results.length > 0){
						self.breakPackLabel = "Break Pack(*)";
					}else{
						self.breakPackLabel = "Break Pack";
					}
					self.breakPackData = results;
					self.findInnersProductBreakPack(results);
				}
				//error case
				, self.fetchError
			);
		};

		/**
		 * Find all inners product break pack from product break pack data from back end.
		 * @param productRelationships
		 */
		self.findInnersProductBreakPack = function (productRelationships) {
			if(productRelationships != null && productRelationships.length > 0){
				if (productRelationships[0].productId === self.productMaster.prodId) {
					self.innersBreakPackData = angular.copy(productRelationships[0].children);
				}else{
					self.innersBreakPackData = self.findChildren(productRelationships[0].children);
				}

				//set flag for list inner product break pack
				if(self.innersBreakPackData != null){
					angular.forEach(self.innersBreakPackData, function(elm){
						elm["selected"] = false;
						elm["upcOrig"] = elm.upc;
					});
				}
				//backup data for the list of inner product break pack
				self.innersBreakPackDataOrig = angular.copy(self.innersBreakPackData);
			}else{
				self.innersBreakPackData = [];
				self.innersBreakPackDataOrig = [];
			}
		};

		/**
		 * Loop find the list of inners product break pack.
		 * @param productRelationships
		 * @returns {null}
		 */
		self.findChildren = function (productRelationships) {
			if(productRelationships != null && productRelationships.length > 0) {
				for (var i = 0; i < productRelationships.length; i++) {
					if (productRelationships[i].productId === self.productMaster.prodId) {
						return productRelationships[i].children;
					}
					var children = self.findChildren(productRelationships[i].children);
					if(children != null){
						return children;
					}
				}
			}
			return null;
		};

		/**
		 * User checked/unchecked in checkbox on header data tables.
		 */
		self.checkAllHandle = function () {
			if(self.innersBreakPackData != null){
				angular.forEach(self.innersBreakPackData, function(elm){
					elm.selected = self.checkAllFlag;
				});
			}
		};

		/**
		 * Add new a inner product break pack
		 */
		self.addInnerProductBreakPack = function () {
			self.success = "";
			self.error = self.validationProductRelationship();
			if(self.error  == null || self.error == ''){
				var productRelationship = {};
				productRelationship["upc"] = "";
				productRelationship["productId"] = "";
				productRelationship["description"] = "";
				productRelationship["size"] = "";
				productRelationship["productQuantity"] = "";
				if(self.innersBreakPackData == null){
					self.innersBreakPackData = [];
				}
				self.innersBreakPackData.push(productRelationship);
			}
		};

		/**
		 * Validation value input into product relationship
		 */
		self.validationProductRelationship = function () {
			var upcArray = [];
			var errorMessage = '';
			if(self.innersBreakPackData != null && self.innersBreakPackData.length > 0) {
				for (var i = 0; i < self.innersBreakPackData.length; i++) {
					if (self.innersBreakPackData[i].upc === null || self.innersBreakPackData[i].upc === '') {
						errorMessage = errorMessage +"<li>UPC is a mandatory field at row " + (i+1) + "</li>";
					}else if(self.innersBreakPackData[i].upc && !self.regexIntId.test(self.innersBreakPackData[i].upc)){
						errorMessage = errorMessage +"<li>UPC must be an integer value at row " + (i+1) +"</li>";
					}else if(self.innersBreakPackData[i].upc && (self.innersBreakPackData[i].productId === null || self.innersBreakPackData[i].productId === '')){
						errorMessage = errorMessage +"<li>No matches found for the entered UPC " + self.innersBreakPackData[i].upc +"</li>";
					}else if(upcArray.indexOf(self.innersBreakPackData[i].upc) >= 0){
						errorMessage = errorMessage +"<li>Product "+ self.innersBreakPackData[i].productId+" is duplicated at row " + (i+1) +"</li>";
					}else{
						upcArray.push(self.innersBreakPackData[i].upc);
					}
					if((self.innersBreakPackData[i].productQuantity != null && !self.regexIntQuantity.test(self.innersBreakPackData[i].productQuantity)) || self.innersBreakPackData[i].productQuantity === ''
						|| self.innersBreakPackData[i].productQuantity === null){
						errorMessage = errorMessage +"<li>Selling Units value must be greater than or equal to 0 and less than or equal to 99999 at row " + (i+1) +"</li>";
					}
					if(self.breakPackData != null && self.breakPackData.length > 0 && self.breakPackData[0].parentNodes != null && self.breakPackData[0].parentNodes.indexOf(self.innersBreakPackData[i].productId) >= 0){
						errorMessage = errorMessage +"<li>The UPC " + self.innersBreakPackData[i].upc + " belongs to the outer product and cannot be added as an inner product </li>";
					}
				}
			}
			return errorMessage;
		};

		/**
		 * Check valid the upc has been entered.
		 * @param upcNumber - The upc number
		 * @param index - The index of add
		 */
		self.checkUPCNumber = function (breakPack, index) {
			if(breakPack.upc != null && breakPack.upc != '' && breakPack.upc != breakPack.upcOrig){
				self.error = "";
				self.success = "";
				self.isWaitingForResponse = true;
				breakPackApi.getProductByUpc(
					{
						productId: self.productMaster.prodId,
						upc:breakPack.upc
					},
					//success case
					function (results) {
						self.isWaitingForResponse = false;
						if(results != null && results.upc === 0){
							self.error = "No matches found for the entered UPC "+breakPack.upc;
						}else{
							results["upcOrig"] = results.upc;
							self.innersBreakPackData[index] = results;
						}
					}
					//error case
					, self.fetchError
				);
			}
		};

		/**
		 * This method will unlock the save button when changes are present and inform the user if they try to leave
		 * without saving
		 * @returns {boolean}
		 */
		self.isSaveDisabled = function () {
			if (self.innersBreakPackDataOrig.length === self.innersBreakPackData.length) {

				var isFound;
				for (var x = 0; x < self.innersBreakPackDataOrig.length; x++) {
					isFound = false;
					for (var y = 0; y < self.innersBreakPackData.length; y++) {
						if (self.innersBreakPackDataOrig[x].upc === parseInt(self.innersBreakPackData[y].upc)) {
							isFound = true;
							if (self.innersBreakPackDataOrig[x].productQuantity !== self.innersBreakPackData[y].productQuantity) {
								$rootScope.contentChangedFlag = true;
								return false;
							}
						}
					}
					if (isFound === false) {
						$rootScope.contentChangedFlag = true;
						return false;
					}
				}
				$rootScope.contentChangedFlag = false;
				return true;
			}
		};

		/**
		 * Call back end to update information inners product break pack.
		 */
		self.saveInnerProductBreakPack = function () {
			self.success = "";
			self.error = self.validationProductRelationship();
			if(self.error == null || self.error == ''){
				var arrayChange = [];
				angular.forEach(self.innersBreakPackData, function(elm){
					var children = self.checkExistInnersProductBreakPack(self.innersBreakPackDataOrig, elm);
					if(children === null || (children != null && angular.toJson(children) !== angular.toJson(elm))){
						elm["actionCode"] = "A";
						arrayChange.push(elm);
					}
				});
				angular.forEach(self.innersBreakPackDataOrig, function(elm){
					if(self.checkExistInnersProductBreakPack(self.innersBreakPackData, elm) === null){
						elm["actionCode"] = "D";
						arrayChange.push(elm);
					}
				});
				if(arrayChange != null && arrayChange.length > 0){
					var productRelationship = {};
					productRelationship["productId"] = self.productMaster.prodId;
					productRelationship["children"] = arrayChange;
					self.isWaitingForResponse = true;
					breakPackApi.updateProductRelationship(productRelationship,
						//success
						function (results) {
							self.error = '';
							self.success = results.message;
							self.getProductBreakPackByProductId();
						},
						self.fetchError
					);
				}else{
					self.error = "There are no changes on this page to be saved. Please make any changes to update";
				}
			}
		};

		/**
		 * Find product relationship in array original.
		 * @param prodRelationship
		 * @returns {*}
		 */
		self.checkExistInnersProductBreakPack = function(sourceArray, prodRelationship){
			if(sourceArray != null && sourceArray.length > 0){
				for(var i = 0; i<sourceArray.length; i++){
					if(sourceArray[i].upc == prodRelationship.upc){
						return sourceArray[i];
					}
				}
			}
			return null;
		};

		/**
		 * Remove all element has been seleted.
		 */
		self.deleteInnerProductBreakPack = function () {
			self.success = "";
			self.error = "";
			var innersBreakPackDataFilter = [];
			if(self.innersBreakPackData != null){
				angular.forEach(self.innersBreakPackData, function(elm){
					if(!elm.selected){
						innersBreakPackDataFilter.push(elm);
					}
				});
			}
			if(innersBreakPackDataFilter != null && innersBreakPackDataFilter.length > 0
				&&  innersBreakPackDataFilter.length  == self.innersBreakPackData.length){
				self.error = "Please select a row to delete.";
			}
			else{
				self.innersBreakPackData = angular.copy(innersBreakPackDataFilter);
			}
		};

		/**
		 * Reset data has been changed.
		 */
		self.resetInnerProductBreakPack = function () {
			self.error = "";
			self.success = "";
			self.checkAllFlag = false;
			self.innersBreakPackData = angular.copy(self.innersBreakPackDataOrig);
		};

		/**
		 * Show Break Pack attribute Changed Log.
		 */
		self.showBreakPackAttributeLog = function(){
			self.isWaitingGetBreakPackAttributeAudit = true;
			breakPackApi.getBreakPackAttributesAudits(
				{
					prodId: self.productMaster.prodId,
					upc: self.productMaster.productPrimaryScanCodeId
				},
				//success case
				function (results) {
					self.breakPackAttributeAudits = results;
					self.initBreakPackAttributeAuditTable();
					self.isWaitingGetBreakPackAttributeAudit = false;
				}, self.fetchError);

			$('#break-pack-attribute-log-modal').modal({backdrop: 'static', keyboard: true});
		};
		/**
		 * Init data Break Pack attribute Audit.
		 */
		self.initBreakPackAttributeAuditTable = function () {
			$scope.filter = {
				attributeName: '',
			};
			$scope.sorting = {
				changedOn: ORDER_BY_DESC
			};
			$scope.breakPackAttributeAuditTableParams = new ngTableParams({
				page: 1,
				count: 10,
				filter: $scope.filter,
				sorting: $scope.sorting

			}, {
				counts: [],
				data: self.breakPackAttributeAudits
			});
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
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			if (error && error.data) {
				if (error.data.message) {
					self.error = (error.data.message);
				} else if (error.data.error) {
					self.error = (error.data.error);
				} else {
					self.error = error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			}
			$rootScope.$broadcast(self.RELOAD_AFTER_SAVE_POPUP, self.currentAttributeId, true);
		};
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
			if(self.productMaster.createdDateTime === null || angular.isUndefined(self.productMaster.createdDateTime)) {
				return '01/01/1901 00:00';
			} else if (parseInt(self.productMaster.createdDateTime.substring(0, 4)) < 1900) {
				return '01/01/0001 00:00';
			} else {
				return $filter('date')(self.productMaster.createdDateTime, 'MM/dd/yyyy HH:mm');
			}
		};

		/**
		 * Returns createUser or '' if not present.
		 */
		self.getCreateUser = function() {
			if(self.productMaster.displayCreatedName === null || self.productMaster.displayCreatedName.trim().length == 0) {
				return '';
			}
			return self.productMaster.displayCreatedName;
		};
	}

})();
