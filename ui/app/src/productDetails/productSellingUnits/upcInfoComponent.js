/*
 *   casePackInfoComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Selling Unit -> UPC Info component.
 *
 * @author vn40486
 * @since 2.3.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('upcInfo', {
		// isolated scope binding
		bindings: {
			sellingUnit: '<',
			productMaster: '<',
			onProductMasterChange:'&',
			onSellingUnitChange: '&',
			isCollapseCasePack:'=',
			isCollapseSellingUnit:'='
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/productDetails/productSellingUnits/upcInfo.html',
		// The controller that handles our component logic
		controller: UpcInfoController
	});

	UpcInfoController.$inject = ['productSellingUnitsApi', 'UserApi', '$filter', '$rootScope', 'ngTableParams',
		'PermissionsService', 'ProductDetailAuditModal','ProductSearchService', '$timeout'];

	/**
	 * UPC Info component's controller definition.
	 * @constructor
	 * @param productSellingUnitsApi
	 */
	function UpcInfoController(productSellingUnitsApi, userApi, $filter, $rootScope, ngTableParams, permissionsService,
							   ProductDetailAuditModal, productSearchService, $timeout) {

		var self = this;

		self.UPC_INFO_TITLE = "UPC Info";

		/**
		 * Constant for users who can edit.
		 * @type {string}
		 */
		self.modifyLebSwitchConstant = "Modify whether this UPC is LEB.";

		/**
		 * Constant for users who do not have edit access.
		 * @type {string}
		 */
		self.restrictedModifyLebSwitchConstant = "User does not have access to modify LEB.";

		/**
		 * If the wic is not LEB eligible.
		 * @type {string}
		 */
		self.isWICLebEligibleConstant = "WIC category $wicCategory and sub category $wicSubCategory is not LEB eligible.";

		/**
		 * Constant for validate Dimensions.
		 */
		const RETAIL_LENGTH_ERROR_MESSAGE = "Retail Length is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99";
		const RETAIL_WIDTH_ERROR_MESSAGE = "Retail Width is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99";
		const RETAIL_HEIGHT_ERROR_MESSAGE = "Retail Height is a mandatory field, greater than or equal to 0 and less than or equal to 99999.99";
		const RETAIL_WEIGHT_ERROR_MESSAGE = "Retail Weight is a mandatory field, greater than 0 and less than or equal to 99999.9999";

		/**
		 * This represents an empty cross linked list.
		 * @type {boolean}
		 */
		self.emptyCrossLinkedList = false;

		/**
		 * Represents an empty LEB size list.
		 * @type {boolean}
		 */
		self.emptyLEBSizeList = false;
		/**
		 * This determines whether or not the LEB Sizes modal is loading.
		 * @type {boolean}
		 */
		self.isLEBSizesModalLoading = false;

		/**
		 * Loading icon
		 *
		 * @type {boolean}
		 */
		self.isLoading = false;

		/**
		 * Success message to show when a successful save is complete.
		 *
		 * @type {null}
		 */
		self.success = null;

		self.error = null;

		/**
		 * Determine if Item Master is completed loading.
		 *
		 * @type {boolean}
		 */
		self.isItemMasterDataLoading = false;

		/**
		 * The constant for UPC Info tab.
		 * @type {string}
		 */
		var upcInfoTab= "upcInfo";

		/**
		 * The constant for Dimensions tab.
		 * @type {string}
		 */
		var dimensionsTab = "dimensions";

		/**
		 * The constant for WIC tab.
		 * @type {string}
		 */
		var wicTab = "wic";

		/**
		 * Determines which tab is currently selected.
		 * @type {null}
		 */
		self.currentTab = upcInfoTab;
		/**
		 * The created by user.
		 *
		 * @type {Object}
		 */
		self.createUser = null;

		self.doubleValueQuantity=null;
		self.originalDoubleValueQuantity = null;
		self.selectedRetailUnitOfMeasure = null;
		self.originalSelectedRetailUnitOfMeasure = null;
		/**
		 * The Product Master that is currently displayed.
		 *
		 * @type {Object}
		 */
		self.currentProductMaster = null;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			//Set collapse value when UPC Info tab is chose automatically
			self.isCollapseCasePack = true;
			self.isCollapseSellingUnit = false;
			//call the product detail component to set styles for product summary
			$timeout( function(){
				$rootScope.$broadcast('setStylesForProductSummary');
			}, 500);
			console.log('UPCInfoComp - Initialized');
			self.disableReturnToList = productSearchService.getDisableReturnToList();
		};

		/**
		 * Component will reload the vendor data whenever the item is changed in casepack.
		 */
		this.$onChanges = function(){
			self.isLoading = true;
			if(self.originalSellingUnit != null && self.originalSellingUnit.upc !== self.sellingUnit.upc){ self.success = null; }
			self.currentProductMaster = angular.copy(self.productMaster);
			self.originalSellingUnit = angular.copy(self.sellingUnit);
			self.currentSellingUnit= angular.copy(self.sellingUnit);
			self.currentSellingUnit.tagSize = self.currentSellingUnit.tagSize.toUpperCase();
			self.selectedRetailUnitOfMeasure = angular.copy(self.sellingUnit.retailUnitOfMeasure);
			self.originalSelectedRetailUnitOfMeasure = angular.copy(self.sellingUnit.retailUnitOfMeasure);
			//Makes a call to the backend for gladson data based on the selling unit upc selected
			productSellingUnitsApi.getGladsonData({'upc' :self.sellingUnit.upc} ,self.loadData, self.fetchError);
			productSellingUnitsApi.getByUPC({'upc' : self.sellingUnit.upc}, self.loadWicData, self.fetchError);
			self.isItemMasterDataLoading = true;
			self.itemMasterLinkedtoUPC = null;
			productSellingUnitsApi.findAllItemMastersLinkedToUpc({'upc' : self.sellingUnit.upc}, self.loadItemMasterData, self.fetchError);
			productSellingUnitsApi.findAllRetailUnitOfMeasure('', self.loadRetailUnitOfMeasure, self.fetchError);
			getDoubleValueQuantity();
			self.getUserById();
		};

		/**
		 * Update UI Quantity value to double
		 *
		 */
		function getDoubleValueQuantity() {
			self.doubleValueQuantity = $filter('number')(self.sellingUnit.quantity, 2);
			self.originalDoubleValueQuantity = angular.copy(self.doubleValueQuantity)
		}

		/**
		 * Update quantity value modified in UI
		 *
		 */
		self.updateSellingUnitQuantity = function () {
			self.currentSellingUnit.quantity = self.doubleValueQuantity;
		};

		/**
		 * Load returned UOM from returned from API.
		 *
		 * @param results
		 */
		self.loadRetailUnitOfMeasure = function (results) {
			self.isLoading = false;
			self.retailUnitOfMeasures = angular.copy(results);
		};

		/**
		 * Load WIC data returned from API
		 *
		 * @param results
		 */
		self.loadWicData = function(results) {
			self.isLoading = false;
			self.emptyCrossLinkedList = false;
			self.wicData = results;
			if(self.wicData.length > 0) {
				self.noWicData = false;
				buildWicTable();
			} else {
				self.noWicData = true;
			}
		};

		/**
		 * Load data returned from API.
		 *
		 * @param results
		 */
		self.loadData = function (results) {
			self.isLoading = false;
			self.gladson = angular.copy(results);
			self.originalGladson = angular.copy(results);
		};

		/**
		 * Load returned Item Master Data.
		 *
		 * @param results
		 */
		self.loadItemMasterData = function (results) {
			self.isLoading = false;
			self.isItemMasterDataLoading = false;
			self.itemMasterLinkedtoUPC = angular.copy(results);
		};

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			console.log('UPCInfoComp - Destroyed');
			/** Execute component destroy events if any. */
		};

		/**
		 * Builds the Wic Table.
		 */
		function buildWicTable () {
			self.tableParams = new ngTableParams(
				{
					page: 1,
					count: 10
				},
				{
					counts: [],
					data: self.wicData
				}
			)
		}

		/**
		 * Retrieves a list of cross linked list of upcs.
		 * @param currentWic The wic apl id to get a cross linked list of upcs for.
		 */
		self.retrieveCrossLinkedListOfUPCs = function(currentWic) {
			self.currentModalWic = angular.copy(currentWic);
			var wicAplId = currentWic.key.wicApprovedProductListId;
			self.isCrossLinkedModalLoading = true;
			productSellingUnitsApi.getCrossLinkedListOfUPCs({wicAplId: wicAplId}, self.loadWicCrossLinkedUPCsModal,
				self.fetchError);
		};

		/**
		 * Loads the list of wic cross linked upcs modal.
		 * @param results list of cross linked upcs
		 */
		self.loadWicCrossLinkedUPCsModal = function(results) {
			self.listOfCrossLinkedUPCs = results.data;
			if(results.data.length > 0) {
				self.emptyCrossLinkedList = false;
				self.buildCrossLinkedTable(results.data);
			} else {
				self.emptyCrossLinkedList = true;
			}
			self.isCrossLinkedModalLoading = false;
		};

		/**
		 * Builds the cross linked upc list table.
		 * @param crossLinkedUpcList
		 */
		self.buildCrossLinkedTable = function(crossLinkedUpcList) {
			self.crossLinkedUPCTableParams = new ngTableParams(
				{
					page: 1,
					count: 15
				},
				{
					counts: [],
					data: crossLinkedUpcList
				}
			)
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.fetchError = function(error){
			self.isDimensionsLoading = false;
			self.isLoading = false;
			if (error && error.data) {
				self.error = error.data.message;
			} else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * This determines whether the leb switch is editable or not.
		 * @param wic
		 * @returns {boolean}
		 */
		self.isLebSwitchDisabled = function(wic) {
			if(!permissionsService.getPermissions('UP_INFO_01', 'EDIT')) {
				return true;
			}
			return !wic.wicSubCategory.lebSwitch;
		};

		/**
		 * The checkbox title for the LEB.
		 * @param wic
		 * @returns {string}
		 */
		self.getLEBCheckboxTitle = function(wic) {
			if(!permissionsService.getPermissions('UP_INFO_01', 'EDIT')) {
				return self.restrictedModifyLebSwitchConstant;
			} else if(wic.wicSubCategory.lebSwitch){
				return self.modifyLebSwitchConstant;
			} else {
				return self.isWICLebEligibleConstant.replace("$wicCategory", wic.key.wicCategoryId).replace("$wicSubCategory", wic.key.wicSubCategoryId);
			}
		};

		/**
		 * This retrieves the list of leb upcs.
		 * @param currentWic
		 */
		self.retrieveLEBUPCs = function(currentWic) {
			self.currentModalWic = angular.copy(currentWic);
			var wicCategoryId = currentWic.wicSubCategory.key.wicCategoryid;
			var wicSubCategoryId = currentWic.wicSubCategory.key.wicSubCategoryId;
			self.isLEBUPCModalLoading = true;
			productSellingUnitsApi.getLEBUPCList({
					wicCategoryId: wicCategoryId,
					wicSubCategoryId: wicSubCategoryId}, self.loadLEBUPCsModal,
				self.fetchError);
		};

		/**
		 * Loads the list of leb upcs being returned from the back end.
		 * @param results
		 */
		self.loadLEBUPCsModal = function(results) {
			if(results.length > 0) {
				self.emptyLEBUPCList = false;
				self.buildLEBUPCsTable(results);
			} else {
				self.emptyLEBUPCList = true;
			}
			self.isLEBUPCModalLoading = false;
		};

		/**
		 * Builds the LEB UPC list table.
		 * @param LEBUPCList
		 */
		self.buildLEBUPCsTable = function(LEBUPCList) {
			self.LEBUPCsTableParams = new ngTableParams(
				{
					page: 1,
					count: 15
				},
				{
					counts: [],
					data: LEBUPCList
				}
			)
		};

		/**
		 * This retrieves the information for the LEB Sizes table.
		 * @param currentWic
		 */
		self.retrieveLEBSizes = function(currentWic) {
			self.currentModalWic = angular.copy(currentWic);
			self.isLEBSizesModalLoading = true;
			var wicCategoryId = currentWic.wicSubCategory.key.wicCategoryid;
			var wicSubCategoryId = currentWic.wicSubCategory.key.wicSubCategoryId;
			productSellingUnitsApi.getLEBSizesList({
					wicCategoryId: wicCategoryId,
					wicSubCategoryId: wicSubCategoryId}, self.buildLEBSizesTable,
				self.fetchError);
		};

		/**
		 * Builds the LEB Sizes table.
		 * @param results
		 */
		self.buildLEBSizesTable = function(results) {
			if(results.length < 0) {
				self.emptyLEBSizeList = true;
			} else {
				self.emptyLEBSizeList = false;
				self.LEBSizesTableParams = new ngTableParams(
					{
						page: 1,
						count: 15
					},
					{
						counts: [],
						data: results
					})
			}
			self.isLEBSizesModalLoading = false;
		};

		/**
		 * Function that checks dsd and whs switch to determine if product is on the DSD channel or WHS channel.
		 *
		 * @param dsd
		 * @param whs
		 * @returns {*}
		 */
		self.isDsdOrWhs = function (dsd, whs) {
			if(dsd === true) {
				return "DSD";
			} else if (whs === true) {
				return "WHS";
			}
		};

		/**
		 * Function to determine if a product is part of an MRT, Fake MRT, or Alt-Pack.
		 *
		 * @param shippers
		 * @param mrt
		 * @returns {*}
		 */
		self.isAltPkOrMrt = function (shippers, mrt) {
			// If the mrt switch is set to true then it is an MRT.
			if(mrt === true) {
				return "MRT";
			} else {
				if(shippers.length === 0) {
					return "-";
				} else {
					// Else check to see if the shipperUPC contains 6666666666, if so then it is part of a fake MRT.
					for (var index = 0; index < shippers.length; index++) {
						if (shippers[index].key.shipperUpc === 6666666666) {
							return "-";
						}
					}
				}
				// If its anything else then it is an Alt-Pack.
				return "Alt-Pack";
			}
		};

		/**
		 * When the audit button is clicked, this loads the modal and shows
		 */
		self.showUpcInfoAuditInfo = function() {
			self.upcAuditInfo = productSellingUnitsApi.getUpcInfoAudits;
			var title = self.UPC_INFO_TITLE;
			ProductDetailAuditModal.open(self.upcAuditInfo, self.sellingUnit, title);
		};

		/**
		 * Set values to original value
		 */
		self.reset = function () {
			$('#quantity').removeClass('ng-touched ng-invalid');
			self.selectedRetailUnitOfMeasure = angular.copy(self.originalSelectedRetailUnitOfMeasure);
			self.doubleValueQuantity= self.originalDoubleValueQuantity;
			self.error = null;
			self.success = null;
			self.currentSellingUnit = angular.copy(self.originalSellingUnit);
		};

		/**
		 * Reset data for Dimensions.
		 */
		self.resetDimensions = function () {
			self.error = null;
			self.success = null;
			self.currentSellingUnit = angular.copy(self.originalSellingUnit);
			self.currentProductMaster = angular.copy(self.productMaster);
			self.selectedRetailUnitOfMeasure = angular.copy(self.originalSelectedRetailUnitOfMeasure);
			self.gladson = angular.copy(self.originalGladson)

		};

		/**
		 * Check has changed data SellingUnit.
		 */
		self.isDifferent = function () {
			if (((angular.toJson(self.currentSellingUnit) === angular.toJson(self.originalSellingUnit))  &&
				(self.originalDoubleValueQuantity === self.doubleValueQuantity) &&
				(angular.toJson(self.selectedRetailUnitOfMeasure) === angular.toJson(self.originalSelectedRetailUnitOfMeasure)))){
				$rootScope.contentChangedFlag = false;
				return true;
			} else{
				$rootScope.contentChangedFlag = true;
				return false;
			}
		};

		/**
		 * Compare current product and original product. Return true when any thing has changed.
		 * @returns {boolean}
		 */
		self.isDimensionChanged = function () {
			if (self.productMaster.goodsProduct !== null
					&& ((self.productMaster.goodsProduct.retailLength != self.currentProductMaster.goodsProduct.retailLength)
							|| (self.productMaster.goodsProduct.retailWidth != self.currentProductMaster.goodsProduct.retailWidth)
							|| (self.productMaster.goodsProduct.retailHeight != self.currentProductMaster.goodsProduct.retailHeight)
							|| (self.productMaster.goodsProduct.retailWeight != self.currentProductMaster.goodsProduct.retailWeight))){
				$rootScope.contentChangedFlag = true;
				return false;
			}
			$rootScope.contentChangedFlag = false;
			return true;
		};

		/**
		 * This method takes in a resource name, calls permissions service to see if the current user has edit
		 * permissions on the given resource, then returns true if the user does have edit access, and false otherwise.
		 *
		 * @param resourceName Resource to check for edit access on.
		 * @returns {boolean} True if user has edit access, false otherwise.
		 */
		self.canUserEditResource = function(resourceName){
			return permissionsService.getPermissions(resourceName, 'EDIT');
		};

		/**
		 * This recursively traverses through an object that may contain lists or other objects inside of it. This will
		 * check each field inside of each object or list to determine whether or not that field has changed. If it has changed,
		 * it saves it to a temporary and then the temporary will be returned.
		 *
		 * @param original
		 * @param current
		 * @returns {{}}
		 */
		self.getProductMasterChanges = function(original, current) {
			var temp = {};
			for (var key in current) {
				if (!current.hasOwnProperty(key)) continue;

				if(angular.toJson(current[key]) !== angular.toJson(original[key])) {
					// It is in a list. This will look through a list of things to check whether any of them have
					// changed.
					if(self.isInt(key)) {
						if(!Array.isArray(temp)) {
							temp = [];
						}
						temp[key] = self.getProductMasterChanges(original[key], current[key]);
						temp[key].upc = current[key].upc;
					} else if(key !== "goodsProduct" && key !== "productCubiscan") {
						// If the object is not a sellingUnit, goodsProduct or an rxProduct object and it has been changed
						// then save it into the temporary from the current.
						temp[key] = current[key];
					} else if(key === "goodsProduct" || key === "productCubiscan") {
						// If it is a sellingUnits List, goodsProduct object or an rxProduct. Go inside and check each
						// object to check and see if any of it has changed.
						temp[key] = self.getProductMasterChanges(original[key], current[key]);
					}
				}
			}
			return temp;
		};

		/**
		 * Get updated front end UI values to send to the API.
		 *
		 * @param original values
		 * @param current new current values
		 * @return {{}}
		 */
		self.getSellingUnitChanges = function(original, current) {
			var temp = {};
			for (var key in current) {
				if (!current.hasOwnProperty(key)) continue;

				if(angular.toJson(current[key]) !== angular.toJson(original[key])) {

					temp[key] = current[key];
				}
			}

			temp.upc = current.upc;
			temp.prodId = current.prodId;
			console.log((temp));
			return temp;
		};

		/**
		 * Whether or not the value is an integer. This determines whether or not it is in a key.
		 * @param value
		 * @returns {boolean}
		 */
		self.isInt = function(value) {
			var x = parseFloat(value);
			return !isNaN(value) && (x | 0) === x;
		};

		/**
		 * Sends modified mrt to the backend for saving
		 */
		self.saveUpcInfo = function () {
			self.error = null;
			self.success = null;
			if(self.currentSellingUnit.quantity>0&&self.currentSellingUnit.quantity<=999.99){
				if (self.checkQuantity(self.currentSellingUnit.quantity)){
					$('#quantity').removeClass('ng-touched ng-invalid');
					self.isLoading = true;
					var sellingUnit = self.getSellingUnitChanges(self.originalSellingUnit, self.currentSellingUnit);
					productSellingUnitsApi.saveUpcInfo(sellingUnit, self.reloadSavedData, self.fetchError);
				}
				else {
					self.error = 'Selling Unit - UPC Info:'+'<li>Quantity value must be a decimal number with 2 places. Example: 123.45</li>';
					$('#quantity').addClass('ng-touched ng-invalid');
				}
			}
			else {
				self.error = 'Selling Unit - UPC Info:'+'<li>Quantity value must be greater than 0 and less than or equal to 999.99</li>';
				$('#quantity').addClass('ng-touched ng-invalid');
			}
		};

		/**
		 * Validate quantity
		 * @param quantity the quantity number.
		 * @returns {boolean}
		 */
		self.checkQuantity = function (quantity) {
			var rx = /^\d+(?:\.\d{1,2})?$/;
			if(rx.test(quantity)){
				return true;
			}
			else{
				return false;
			}
		};

		/**
		 * Save data Dimensions.
		 */
		self.saveDimensions = function () {
			self.error = null;
			self.success = null;
			// validate data Dimensions before save.
			if (self.validateDimensions()) {
				self.isDimensionsLoading = true;
				var dimensionsParameters = {};
				dimensionsParameters.productMaster = self.getProductMasterChanges(self.productMaster, self.currentProductMaster);
				dimensionsParameters.productMaster.prodId = self.currentProductMaster.prodId;
				dimensionsParameters.sellingUnit = self.getSellingUnitChanges(self.originalSellingUnit, self.currentSellingUnit);
				productSellingUnitsApi.saveDimensions(dimensionsParameters, self.reloadDimensionsSavedData, self.fetchError);
			}
		};

		/**
		 * Validate Dimensions.
		 */
		self.validateDimensions = function () {
			var errorMessages = [];
			if (self.productMaster.goodsProduct.retailLength != self.currentProductMaster.goodsProduct.retailLength
				&& (self.currentProductMaster.goodsProduct.retailLength == '' || self.currentProductMaster.goodsProduct.retailLength < 0 || self.currentProductMaster.goodsProduct.retailLength > 99999.99)) {
				errorMessages.push(RETAIL_LENGTH_ERROR_MESSAGE);
			}

			if (self.productMaster.goodsProduct.retailWidth != self.currentProductMaster.goodsProduct.retailWidth
				&& (self.currentProductMaster.goodsProduct.retailWidth == '' || self.currentProductMaster.goodsProduct.retailWidth < 0 || self.currentProductMaster.goodsProduct.retailWidth > 99999.99)) {
				errorMessages.push(RETAIL_WIDTH_ERROR_MESSAGE);
			}

			if (self.productMaster.goodsProduct.retailHeight != self.currentProductMaster.goodsProduct.retailHeight
				&& (self.currentProductMaster.goodsProduct.retailHeight == '' || self.currentProductMaster.goodsProduct.retailHeight < 0 || self.currentProductMaster.goodsProduct.retailHeight > 99999.99)) {
				errorMessages.push(RETAIL_HEIGHT_ERROR_MESSAGE);
			}

			if (self.productMaster.goodsProduct.retailWeight != self.currentProductMaster.goodsProduct.retailWeight
				&& (self.currentProductMaster.goodsProduct.retailWeight == '' || self.currentProductMaster.goodsProduct.retailWeight <= 0 || self.currentProductMaster.goodsProduct.retailWeight > 99999.9999)) {
				errorMessages.push(RETAIL_WEIGHT_ERROR_MESSAGE);
			}
			if (errorMessages.length > 0) {
				self.error = '';
				angular.forEach(errorMessages, function (value) {
					self.error += "<li>" + value + "</li>";
				});
			}

			return errorMessages.length == 0;
		};

		/**
		 * Reload data Dimensions after save data.
		 * 
		 * @param results the results after save data.
		 */
		self.reloadDimensionsSavedData = function (results) {

			// update selling unit info for CPS Dimensions after Retail Dimensions are updated
			if (results.sellingUnits !== undefined && results.sellingUnits !== null && results.sellingUnits.length > 0) {
				self.originalSellingUnit = angular.copy(results.sellingUnits[0]);
				self.currentSellingUnit = angular.copy(results.sellingUnits[0]);
			}

			var product = {product: results};
			self.onProductMasterChange(product);
			self.isDimensionsLoading = false;
			self.error = null;
			self.success = "Updated Successfully!";

		};

		/**
		 * Callback for a successful call to update Upc Info
		 *
		 * @param results
		 */
		self.reloadSavedData = function (results) {
			// update selling unit info for CPS Dimensions after Retail Dimensions are updated
			if (results.sellingUnits !== undefined && results.sellingUnits !== null && results.sellingUnits.length > 0) {
				self.originalSellingUnit = angular.copy(results.sellingUnits[0]);
				self.currentSellingUnit = angular.copy(results.sellingUnits[0]);
			}

			var product = {product: results};
			self.onProductMasterChange(product);
			self.isLoading = false;
			self.error = null;
			self.success = "Updated Successfully!";
		};

		/**
		 * Updates the retail unit of measure code to new code.
		 *
		 */
		self.updateSellingUnitRetailUnitOfMeasureCode = function () {
			self.currentSellingUnit.retailUnitOfMeasureCode = self.selectedRetailUnitOfMeasure.id;
		};

		/**
		 * Show audit folder icon
		 * @return - boolean to show icon
		 */
		self.showAuditFolder = function () {
			var result = true;

			if (angular.equals(self.currentTab, wicTab)) {
				result = false;
			}

			return result;
		};

		/**
		 * Determines which tab you are currently on.
		 * @param tab
		 */
		self.chooseTab = function(tab) {
			self.currentTab = tab;
		};

		/**
		 * Show related product audit information modal
		 */
		self.showUPCInfoAuditInfo = function () {
			if (angular.equals(self.currentTab, upcInfoTab)) {
				self.showUpcInfoAuditInfo();
			} else if (angular.equals(self.currentTab, dimensionsTab)) {
				self.showDimensionsAuditInfo();
			}
		}

		/**
		 * Show online attributes audit information modal
		 */
		self.showDimensionsAuditInfo = function () {
			self.dimensionsAuditInfo = productSellingUnitsApi.getDimensionsAudits;
			var title ="Dimensions";
			var parameters = {'prodId' :self.currentProductMaster.prodId};
			ProductDetailAuditModal.open(self.dimensionsAuditInfo, parameters, title);
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
		/**
		 * Returns user by createUid id;
		 */
		self.getUserById = function() {
			self.createUser = null;
			if (self.originalSellingUnit.createId && self.originalSellingUnit.createId.toString().trim() !== '') {
				userApi.getUserById({userId: self.originalSellingUnit.createId},
					function (results) {
						self.createUser = results;
						if(self.createUser.displayName === null || self.createUser.displayName === undefined) {
							self.createUser.displayName = self.originalSellingUnit.createId;
						}
					},//error handle
					self.fetchError);
			}
		};

		/**
		 * Returns the added date, or null value if date doesn't exist.
		 */
		self.getAddedDate = function() {
			if(self.originalSellingUnit.createTime === null || angular.isUndefined(self.originalSellingUnit.createTime)) {
				return '01/01/1901 00:00';
			} else if (parseInt(self.originalSellingUnit.createTime.substring(0, 4)) < 1900) {
				return '01/01/0001 00:00';
			}  else {
				return $filter('date')(self.originalSellingUnit.createTime, 'MM/dd/yyyy HH:mm');
			}
		};

		/**
		 * Returns createUser or '' if not present.
		 */
		self.getCreateUser = function() {
			if(self.createUser === null || angular.isUndefined(self.createUser) ||
				self.createUser.displayName === null || angular.isUndefined(self.createUser.displayName)) {
				return '';
			} else {
				return self.createUser.displayName;
			}
		};
	}
})();
