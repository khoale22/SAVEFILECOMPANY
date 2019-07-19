/*
 *   productInfoComponent.js
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Component of Product Info (tab-content) screen that is displayed under the right side work area of the Product detail
 * page.
 *
 * @author vn40486
 * @since 2.3.0
 * @updated 2.12.0 by m594201
 */
(function () {

	angular.module('productMaintenanceUiApp').component('productInfo', {
		// isolated scope binding
		bindings: {
			productMaster: '<'
		},
		// Inline template which is bound to message variable in the component controller
		templateUrl: 'src/productDetails/product/productInfo.html',
		// The controller that handles our component logic
		controller: ProductInfoController
	});

	ProductInfoController.$inject =
		['ProductInfoApi', 'PermissionsService', 'ProductSearchApi', 'UserApi', 'HomeSharedService', 'ngTableParams',
			'$rootScope', '$timeout','ProductDetailAuditModal','$filter','urlBase','DownloadService',
			'$state', 'appConstants', 'ProductSearchService','ProductGroupService','taskService', '$location', '$scope','productDetailApi'];

	/**
	 * Product Info component's controller definition.
	 * @constructor
	 * @param productInfoApi
	 * @param permissionsService
	 * @param productSearchApi
	 * @param homeSharedService
	 * @param ngTableParams
	 * @param $timeout
	 */
	function ProductInfoController(productInfoApi, permissionsService, productSearchApi, userApi,
								   homeSharedService, ngTableParams,$rootScope, $timeout,productDetailAuditModal,
								   $filter, urlBase, downloadService, $state, appConstants, productSearchService,
								   productGroupService, taskService, $location, $scope, productDetailApi) {
		/** Product Info page's  all CRUD operation controller goes here */
		var self = this;

		self.originalProductMasterParams = null;
		self.currentProductMasterParams = null;
		self.subCommodityValues = null;
		self.success = null;
		self.error = null;
		self.waitingForUpdate = false;
		self.isLoadingVertexTaxCategory = false;
		self.isLoadingVertexTaxQualifyingCode = false;
		self.isLoadingPriceDetail = false;
		self.isLoadingProductAssortment = false;
		self.isLoadingDepositRelatedProduct = false;
		self.isLoadingMapPrice = false;
		self.allEffectiveVertexTaxCategories = [];
		self.allEffectiveVertexTaxCategoriesOriginal = [];
		self.allProductRetailLinks = [];
		self.retailPrice = null;
		self.allTaxQualifyingConditions = [];
		self.vertexTaxCategoryProductParams = null;
		self.allVertexTaxCategories = null;

		// constants
		self.PRODUCT_DESCRIPTION_RECEIPT_TEXT = "The green highlighted portion of product description represents" +
			" the description as seen on a customer's register receipt (first 25 characters). Review for excellence" +
			" and appropriate language.";
		self.SSA_HELP_TEXT = "Number of stores franchise-wide that currently have this product in assortment.";
		self.UPDATING_MESSAGE_TEXT = "Updating...please wait.";
		self.UPDATE_SUCCESSFUL_MESSAGE = "Successfully updated.";
		self.SEVENTEEN_NUMBER_REGEX_ERROR_TEXT = "Must match the format (#####)(up to seventeen numbers).";
		self.FIVE_NUMBER_REGEX_ERROR_TEXT = "Must match the format (#####)(up to five numbers).";
		self.REMOVE_PRODUCT_RETAIL_ERROR_MESSAGE_TEXT = "Cannot remove current effective tax category.";
		self.REMOVE_PRODUCT_RETAIL_MESSAGE_TEXT = "Remove effective tax category.";
		self.EDIT_PRODUCT_RETAIL_ERROR_MESSAGE_TEXT = "Cannot modify current effective tax category.";
		self.EDIT_PRODUCT_RETAIL_TAXABLE_MESSAGE_TEXT = "Modify effective retail taxable status.";
		self.EDIT_PRODUCT_RETAIL_TAX_CATEGORY_MESSAGE_TEXT = "Modify effective retail tax category.";
		self.PAGE_SIZE = 10;
		self.SUB_COMMODITY_PAGE_SIZE = 20;
		self.RETURN_TO_PRODUCT_LIST = "returnToProductList";
		self.latestRequest = 0;
		self.EDIT_PRODUCT_FOOD_STAMP_ERROR_MESSAGE_TEXT = "Cannot modify current food stamp product.";
		self.EDIT_PRODUCT_FOOD_STAMP_MESSAGE_TEXT = "Modify food stamp product.";
		self.DELETE_PRODUCT_RETAIL_ERROR_MESSAGE_TEXT ='Product must have at least a Tax Category, cannot be deleted.'
		const RETAIL_TAX_DIFFERENT_THAN_SUB_COMMODITY_MESSAGE_TEMPLATE ='Retail Tax (selected as $retailTaxSwitchLabel) is different than the default for the Sub-commodity (set at $taxEligibleLabel).';
		var UNKNOWN_ERROR_TEXT = "An unknown error occurred.";
		var STYLE_DESCRIPTION_TYPE = "STYLE";
		var PAGE_SIZE = 20;
		var LATEST_REQUEST = 0;
		var ENGLISH_LANGUAGE_TYPE = "ENG";
		var MAX_DROPDOWN_SIZE = 20;
		var productDescriptionTemplate = null;
		var NO_QUALIFYING_CONDITION = {dvrCode: null, displayName: "(None)"};
		var FIRST_ROW_INDEX = 0;
		var subCommodityForDefaults = null;
		var TAX_CATEGORY_DIFFERENT_THAN_SUB_COMMODITY_MESSAGE_TEMPLATE = "$nonTaxCategoryTaxable Tax Category is " +
			"different than the default for the Sub-commodity.";
		var SELECTED_VERTEX_TAX_CATEGORY_APPLY_MESSAGE_TEMPLATE = "Apply vertex tax category: " +
			"$selectedVertexTaxCategory.";
		var DUPLICATE_DATE_ERROR_TEXT = "Only 1 effective tax maintenance can exist for: $effectiveDate.";
		var STRING_VALUE_FOR_BOOLEAN_TRUE = "Y";
		var STRING_VALUE_FOR_BOOLEAN_FALSE = "N";
		var TWO_DECIMAL_PLACES = 2;

		// No code table exists for quantity required
		self.quantityRequiredCodeTable = [
			{id: " ", description: "(None)"},
			{id: "Y", description: "Y - Required"},
			{id: "N", description: "N - Not required"},
			{id: "X", description: "X - Prohibited"}
		];
		// No code table exists for quantity required original
		self.quantityRequiredCodeTableOrg = angular.copy(self.quantityRequiredCodeTable);

		// No code table exists for critical product
		self.criticalProductCodeTable = [
			{id: "#", description: "(None)"},
			{id: "N", description: "Normal"},
			{id: "S", description: "Sensitive"},
			{id: "P", description: "Profit"},
			{id: "B", description: "Business Use Only"}
		];

		// No code table exists for critical product original
		self.criticalProductCodeTableOrg = angular.copy(self.criticalProductCodeTable);
		// Angular UIB datePicker options
		self.effectiveTaxCategoryDatePickerOptions = {
			minDate: getDateOffset(new Date(), 1)
		};
		/**
		 * enable or disable return to list button
		 *
		 * @type {Boolean}
		 */
		self.disableReturnToList = true;
		self.taxAndNonTaxCategoryBySubCommodity;
		/**
		 * The key to get tax category and non tax category
		 * @type {string}
		 */
		const TAX_CATEGORY_KEY = "taxCategory";
		const NON_TAX_CATEGORY_KEY = "nonTaxCategory";
		const DELETE_ACTION_CODE = 'D';
		/**
		 * the list of retail taxables to show on retail taxables select box.
		 * @type {*[]}
		 */
		self.retailTaxables = [{id: null ,label:'--Select--'}, {id: true ,label:'Yes'}, {id: false ,label:'No'}];
		self.isLoadingAllVertexTaxCategories = false;
		/**
		 * Tracks whether or not the user is waiting for a download.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;

		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;
		/**
		 * Clear message key.
		 * @type {string}
		 */
		self.CLEAR_MESSAGE = 'clearMessage';

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.disableReturnToList = productSearchService.getDisableReturnToList();
			console.log('ProductInfoComp - Initialized');
			self.buildVertexTaxCategoryProductsTable();
			setAllTaxQualifyingConditions();
			//Display success message after activate candidate product
			if(productSearchService.getActivateCandidateProductMessage()){
				self.success = productSearchService.getActivateCandidateProductMessage();
				productSearchService.setActivateCandidateProductMessage(null);
			}
		};

		/**
		 * Initializes the component and re-initializes it on changes.
		 */
		this.$onChanges = function () {
			resetDefaults();
			productDetailApi.getUpdatedProduct(
				{
					prodId: self.productMaster.prodId
				},
				function (result) {
					self.currentProductMasterParams.productMaster = angular.copy(result);
					self.originalProductMasterParams.productMaster = angular.copy(result);
					self.getData();
				},
				function (error) {
					fetchError('Error getting product master', error);
				});
		};

		/**
		 * Get data of product information tab.
		 */
		self.getData = function () {
			getCurrentSubCommodityDropDownResults();
			self.getAllTaxCategories();
			// calls to back end for additional information
			getVertexTaxCategoryByTaxCode();
			getVertexTaxCategoryByTaxQualifyingCode();
			getPriceDetail();
			getProductAssortment();
			getDepositRelatedProduct();
			getEffectiveMapPrices();
			getProductDescriptionTemplate();

			// set isolated variables that keep track of changes
			setIsolatedVariables();
			self.getAllVertexTaxCategoryEffectiveMaintenanceForProduct();
		};

		/**
		 * Sets variables that only exist on the front end to keep current status of elements that can be modified
		 * that cannot just be linked by an ng-model attribute that already exists.
		 */
		function setIsolatedVariables(){
			setProductStyleDescription();
			setSelectedQuantityRequired();
			setSelectedCriticalProduct();
		}

		/**
		 * Resets controller defaults.
		 */
		function resetDefaults(){
			self.originalProductMasterParams = {};
			self.originalProductMasterParams.productMaster = self.productMaster;
			self.currentProductMasterParams = {};
			self.currentProductMasterParams.productMaster = angular.copy(self.productMaster);
			self.allEffectiveVertexTaxCategories = [];
			self.allEffectiveVertexTaxCategoriesOriginal = [];
		}

		/**
		 * Component ngOnDestroy lifecycle hook. Called just before Angular destroys the directive/component.
		 */
		this.$onDestroy = function () {
			console.log('ProductInfoComp - Destroyed');
			/** Execute component destroy events if any. */
		};

		/**
		 * Function to retrieve vertex tax category information by a tax code.
		 */
		function getVertexTaxCategoryByTaxCode() {
			if(self.productMaster.goodsProduct !== null){
				self.isLoadingVertexTaxCategory = true;
				productInfoApi.getVertexTaxCategoryByTaxCode({taxCode :self.productMaster.goodsProduct.vertexTaxCategoryCode},
						function (results) {
					self.originalProductMasterParams.vertexTaxCategory = angular.copy(results);
					self.currentProductMasterParams.vertexTaxCategory = results;
					self.getTaxAndNonTaxCategoryBySubCommodity();
				},
				function (error) {
					self.isLoadingVertexTaxCategory = false;
					fetchError('Error getting Vertex tax category', error);
				});
			}
		}

		/**
		 * Function to retrieve vertex tax qualifying code information.
		 */
		function getVertexTaxCategoryByTaxQualifyingCode() {
			if(self.productMaster.taxQualifyingCode !== null) {
				self.isLoadingVertexTaxQualifyingCode = true;
				productInfoApi.getVertexTaxCategoryByTaxQualifyingCode({taxQualifyingCode: self.productMaster.taxQualifyingCode},
					function (results) {
						self.selectedVertexTaxQualifyingCondition = angular.copy(results);
						self.selectedVertexTaxQualifyingConditionOrg = angular.copy(self.selectedVertexTaxQualifyingCondition);
						self.isLoadingVertexTaxQualifyingCode = false;
					},
					function (error) {
						self.isLoadingVertexTaxQualifyingCode = false;
						fetchError('Error getting Vertex qualifying condition', error);
					});
			} else {
				self.selectedVertexTaxQualifyingCondition = NO_QUALIFYING_CONDITION;
				self.selectedVertexTaxQualifyingConditionOrg = angular.copy(self.selectedVertexTaxQualifyingCondition);
			}
		}

		/**
		 * Function to retrieve price detail information.
		 */
		function getPriceDetail() {
			self.isLoadingPriceDetail = true;
			if (self.productMaster.sellingUnits !== undefined && self.productMaster.sellingUnits !== null) {
				productInfoApi.getPriceDetail({upc: self.productMaster.sellingUnits[0].upc},
					function (results) {
						setRetailPrice(results);
						self.isLoadingPriceDetail = false;
					},
					function (error) {
						setRetailPrice(null);
						self.isLoadingPriceDetail = false;
					});
			}
		}

		/**
		 * Function to retrieve product assortment information.
		 */
		function getProductAssortment() {
			self.isLoadingProductAssortment = true;
			productInfoApi.getProductAssortment({productId : self.productMaster.prodId},
				function (results) {
					self.originalProductMasterParams.productAssortment = angular.copy(results);
					self.currentProductMasterParams.productAssortment = results;
					self.isLoadingProductAssortment = false;
				},
				function (error) {
					self.isLoadingProductAssortment = false;
					fetchError('Error getting product assortment', error);
				});
		}

		/**
		 * Function to retrieve deposit related product information.
		 */
		function getDepositRelatedProduct() {
			self.isLoadingDepositRelatedProduct = true;
			productInfoApi.getDepositRelatedProduct({productId : self.productMaster.prodId},
				function (results) {
					self.originalProductMasterParams.depositRelatedProduct = angular.copy(results);
					self.currentProductMasterParams.depositRelatedProduct = results;
					self.isLoadingDepositRelatedProduct = false;
				},
				function (error) {
					self.isLoadingDepositRelatedProduct = false;
					fetchError('Error getting deposit product', error);
				});
		}

		/**
		 * Function to retrieve current effective map price information.
		 */
		function getEffectiveMapPrices() {
			self.isLoadingMapPrice = true;
			self.mapPricesError = false;
			productInfoApi.getEffectiveMapPrices({upc : self.productMaster.productPrimaryScanCodeId},
				function (results) {
					setFormattedMapPrices(results);
					self.originalProductMasterParams.mapPrices = angular.copy(results);
					self.currentProductMasterParams.mapPrices = results;
					self.isLoadingMapPrice = false;
				},
				function (error) {
					self.isLoadingMapPrice = false;
					self.mapPricesError = true;
					fetchError('Error getting MAP retail', error);
				});
		}

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		function fetchError (message, error) {
			self.success = null;
			self.waitingForUpdate = false;
			self.error = message + ':' + getErrorMessage(error);
		}

		/**
		 * Returns error message.
		 *
		 * @param error Error object.
		 * @returns {string} String to set as error text.
		 */
		function getErrorMessage(error) {
			if(error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return UNKNOWN_ERROR_TEXT;
			}
		}

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
		 * This is a setter for the current quantity required attribute from the code table (the
		 * code table only exists only on the front end).
		 */
		function setSelectedQuantityRequired(){
			for(var index = 0; index < self.quantityRequiredCodeTable.length; index++){
				if(self.productMaster.quantityRequiredFlag === self.quantityRequiredCodeTable[index].id){
					self.selectedQuantityRequired = self.quantityRequiredCodeTable[index];
					break;
				}
			}
		}

		/**
		 * This method is called when a user that has edit permissions updates the selected quantity required. When
		 * this occurs, the quantity required code on product master needs to change, so the system is aware there was a
		 * change, which is what this method does.
		 */
		self.updateSelectedQuantityRequired = function(){
			self.quantityRequiredCodeTable = angular.copy(self.quantityRequiredCodeTableOrg);
			for(var index = 0; index < self.quantityRequiredCodeTable.length; index++){
				if(self.selectedQuantityRequired.id === self.quantityRequiredCodeTable[index].id){
					self.selectedQuantityRequired = self.quantityRequiredCodeTable[index];
					break;
				}
			}
			self.currentProductMasterParams.productMaster.quantityRequiredFlag = self.selectedQuantityRequired.id;
		};

		/**
		 * This is a setter for the current quantity required attribute from the code table (the
		 * code table only exists only on the front end).
		 */
		function setSelectedCriticalProduct(){
			for(var index = 0; index < self.criticalProductCodeTable.length; index++){
				if(self.currentProductMasterParams.productMaster.goodsProduct !== null
						&& self.currentProductMasterParams.productMaster.goodsProduct.criticalProductSwitch === self.criticalProductCodeTable[index].id){
					self.selectedCriticalProduct = self.criticalProductCodeTable[index];
					break;
				}
			}
		}

		/**
		 * This method is called when a user that has edit permissions updates the selected critical product. When this
		 * occurs, the critical product switch on goods product needs to change, so the system is aware there was a
		 * change, which is what this method does.
		 */
		self.updateSelectedCriticalProduct = function(){
			self.criticalProductCodeTable = angular.copy(self.criticalProductCodeTableOrg);
			for(var index = 0; index < self.criticalProductCodeTable.length; index++){
				if(self.selectedCriticalProduct.id === self.criticalProductCodeTable[index].id){
					self.selectedCriticalProduct = self.criticalProductCodeTable[index];
					break;
				}
			}
			if(self.currentProductMasterParams.productMaster.goodsProduct !== null){
				self.currentProductMasterParams.productMaster.goodsProduct.criticalProductSwitch = self.selectedCriticalProduct.id;
			}
		};

		/**
		 * Regular retails x for. Numeric unit available for retail, but not considered inventory. If price detail
		 * is null, set product retail price to null. Else if price detail's retail price is 0.00 set product retail
		 * price to "No price set". Else set the product retail price to the give price detail's retail price.
		 */
		function setRetailPrice(priceDetail) {

			if(priceDetail === null || priceDetail.retailPrice === null){
				self.retailPrice = priceDetail;
			}
			else {
				self.retailPrice = priceDetail;
			}
		}

		/**
		 * This method sets the product style description. This is done by looking for the first product description
		 * that has a description type of STYLE_DESCRIPTION_TYPE. If one is found, set the styleDescription to that
		 * product description's description. Else set the description to null.
		 */
		function setProductStyleDescription() {
			var styleDescription = null;
			if (self.productMaster.productDescriptions !== undefined && self.productMaster.productDescriptions !== null) {
				for (var index = 0; index < self.productMaster.productDescriptions.length; index++) {
					if (self.productMaster.productDescriptions[index].key.languageType === ENGLISH_LANGUAGE_TYPE &&
						self.productMaster.productDescriptions[index].key.descriptionType === STYLE_DESCRIPTION_TYPE) {
						styleDescription = self.productMaster.productDescriptions[index].description;
						break;
					}
				}
				self.styleDescription = angular.copy(styleDescription);
			}
		}

		/**
		 * This method gets a product description template used for editing (if the user can edit one of the product
		 * descriptions) with the product id from the current selected product already filled in.
		 */
		function getProductDescriptionTemplate(){
			if(self.canUserEditResource("PD_INFO_11")) {
				productDescriptionTemplate = null;
				productInfoApi.getProductDescriptionTemplateWithProdId(
					{prodId: self.productMaster.prodId},
					function (result) {
						productDescriptionTemplate = result;
					},
					function (error) {
						fetchError('Error fetching product description template', error);
					}
				)
			}
		}

		/**
		 * This method updates the product description that represents the 'STYLE' description type when a user
		 * updates the product style description on the front end so the system can recognize a change has been done.
		 * If the product master already has a 'STYLE' description, update that description; otherwise add a new
		 * product description to the list of descriptions.
		 */
		self.updateProductStyleDescription = function(){
			var request = ++LATEST_REQUEST;
			var foundIndex = -1;
			for(var index = 0; index < self.currentProductMasterParams.productMaster.productDescriptions.length; index++) {
				if(self.currentProductMasterParams.productMaster.productDescriptions[index].key.descriptionType === STYLE_DESCRIPTION_TYPE) {
					foundIndex = index;
					break;
				}
			}
			// since this function will be called as quickly as the user can type, ensure this is the latest call
			if(request === LATEST_REQUEST) {
				if (foundIndex !== -1) {
					self.currentProductMasterParams.productMaster.productDescriptions[foundIndex].description = self.styleDescription;
				} else {
					var temp = angular.copy(productDescriptionTemplate);
					temp.key.descriptionType = STYLE_DESCRIPTION_TYPE;
					temp.key.languageType = ENGLISH_LANGUAGE_TYPE;
					temp.description = self.styleDescription;
					self.currentProductMasterParams.productMaster.productDescriptions.push(temp);
				}
			}
		};

		/**
		 * Gets current results for sub-commodity dropdown by calling api.
		 */
		function getCurrentSubCommodityDropDownResults(){
			var params = {
				commodityCode: self.productMaster.commodityCode
			};
			productSearchApi.findSubCommoditiesByCommodity(params,
				//success
				function (results) {//get list sub commodity of current commodity
					self.listSubCommodityOfCurrentCommodity = angular.copy(results);
					self.findAllSubCommoditiesByPageRequest();
				},
				//error
				function(error) {
					fetchError('Error getting sub commodities', error);
				}
			);
		};

		/**
		 * Gets list of sub-commodity dropdown by page request.
		 */
		self.findAllSubCommoditiesByPageRequest = function(){
			productInfoApi.findAllSubCommoditiesByPageRequest({
					page: 0,
					pageSize: 200
				},
				//success
				function (results) {//get 200 rows at first time
					var tempArray = angular.copy(self.listSubCommodityOfCurrentCommodity);
					var listSubCommodity = angular.copy(results);
					angular.forEach(listSubCommodity,function (subCommodity) {
						if (!self.isDuplicateSubcomodity(subCommodity)) {
							tempArray.push(subCommodity);
						}
                    });
					self.subCommodityValues = angular.copy(tempArray);
					self.subCommodityValuesOrg = angular.copy(self.subCommodityValues);
				},
				//error
				function(error) {
					fetchError('Error getting sub commodities', error);
				}
			);
		};

        /**
		 * Check duplicate subcommodity in list subCommodity of current commodity.
		 *
         * @param subCommodity the subcommodity.
         * @returns {boolean} result check.
         */
		self.isDuplicateSubcomodity = function (subCommodity) {
			for (var i = 0; i<self.listSubCommodityOfCurrentCommodity.length; i++) {
				if (self.listSubCommodityOfCurrentCommodity[i].key.subCommodityCode
					=== subCommodity.key.subCommodityCode){
					return true;
				}
			}
			return false;
        };

		/**
		 * Change selected subCommodity.
		 */
		self.changeSelectedSubCommodity = function(selected){
			if(selected){
				self.currentSubCommodity = angular.copy(selected);
			}
		};

		/**
		 * Open close event for product brand filter
		 * @param isOpen
		 * @param selected
		 */
		self.onOpenCloseSubCommodity = function(isOpen, selected){
			if(isOpen){//open popup
				self.subCommodityValuesBackup = angular.copy(self.subCommodityValues);
				if(selected){//has data
					//do nothing
				}else{//no data => get 200 rows at first time
					self.subCommodityValues = angular.copy(self.subCommodityValuesOrg);
				}
			}else{//close popup
				var tempArray = angular.copy(self.subCommodityValues);
				var hasElement = self.checkArrayContainElement(tempArray, selected);
				if(hasElement){//get 200 rows at first time + current search list
					if(self.subCommodityValues.length <= self.SUB_COMMODITY_PAGE_SIZE){
						var tempArray = angular.copy(self.subCommodityValues);
						var tempArrayOrg = angular.copy(self.subCommodityValuesOrg);
						var tempList = tempArray.concat(tempArrayOrg.filter(function(i) {
							return tempArray.indexOf(i) == -1;
						}));
						self.subCommodityValues = angular.copy(tempList);
					}
				}else{//reset array
					self.subCommodityValues = angular.copy(self.subCommodityValuesBackup);
				}
			}
		};

		/**
		 * Check an array contains element.
		 */
		self.checkArrayContainElement = function(list, element) {
			var result = false;
			angular.forEach(list, function(item){
				if(angular.toJson(item) === angular.toJson(element)){
					result =  true;
				}
			});
			return result;
		};

		/**
		 * Gets list of sub-commodity dropdown by search text.
		 */
		self.findSubCommoditiesBySearchText = function(isOpen, query){
			if(isOpen){
				$('#productSubCommodityId .ui-select-refreshing').removeClass("ng-hide");
				productInfoApi.findSubCommoditiesBySearchText({
						searchText: query,
						page: 0,
						pageSize: self.SUB_COMMODITY_PAGE_SIZE
					},
					//success
					function (results) {
						$('#productSubCommodityId .ui-select-refreshing').addClass("ng-hide");
						self.subCommodityValues = results.data;
					},
					//error
					function(results) {
						$('#productSubCommodityId .ui-select-refreshing').addClass("ng-hide");
						self.subCommodityValues = [];
					}
				);
			}
		};

		/**
		 * This function returns whether or not the save or reset buttons are disabled or not. This is based on the
		 * current state of the product info form.
		 * For save button: If form is pristine or invalid, return true. Else if there is at least one modifiable
		 * 		attribute that has changed, return false, else return true.
		 * For reset button: If form is pristine return true. Else if there is at least one modifiable
		 * 		attribute that has changed, return false, else return true.
		 *
		 * @returns {boolean} True if save or reset buttons should be disabled, false otherwise.
		 */
		self.isSaveOrResetDisabled = function(){
			if(angular.toJson(self.originalProductMasterParams) !== angular.toJson(self.currentProductMasterParams)){
				$rootScope.contentChangedFlag = true;
				return false;
			}
			$rootScope.contentChangedFlag = false;
			return true;
		};

		/**
		 * This function returns whether the save and reset buttons are visible in the product info view. If the user
		 * has at least one edit access on the product info attributes, return true. Else return false.
		 *
		 * @returns {boolean} True if save and reset buttons should be visible, false otherwise.
		 */
		self.areSaveAndResetButtonsVisible = function(){
			return doesUserHaveEditAccessOnProductInfo();
		};

		/**
		 * This method returns whether a user has at least one edit access association for any of the product info
		 * editable fields.
		 *
		 * @returns {boolean} True if a user has at least one editable product info association, false otherwise.
		 */
		function doesUserHaveEditAccessOnProductInfo(){
			return permissionsService.getPermissions('PD_INFO_02', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_05', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_06', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_07', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_08', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_09', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_10', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_11', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_12', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_13', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_14', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_15', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_16', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_17', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_18', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_19', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_20', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_21', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_22', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_23', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_24', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_25', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_26', 'EDIT') ||
				permissionsService.getPermissions('PD_INFO_27', 'EDIT');
		}

		/**
		 * This method calls the back end to update product information.
		 */
		self.updateProductInfo = function(){
			self.success = null;
			self.error = null;
			self.waitingForUpdate = true;
			var productInfoParameters = self.currentProductMasterParams;
			self.prePriceNullToZero();
			productInfoApi.updateProductInformation(
				productInfoParameters, function (results) {
                            homeSharedService.broadcastUpdateProduct();
                            self.waitingForUpdate = false;
                            self.success = self.UPDATE_SUCCESSFUL_MESSAGE;
                        }, function(error){
                            self.waitingForUpdate = false;
                            fetchError('Error updating product information', error);
                        }
                    )
		};

		/**
		 * This method resets the product information by setting all the data back to its original values.
		 */
		self.resetProductInfo = function(){
			self.success = null;
			self.error = null;
			self.subCommodityValues = angular.copy(self.subCommodityValuesOrg);
			self.currentProductMasterParams = angular.copy(self.originalProductMasterParams);
			self.selectedVertexTaxQualifyingCondition = angular.copy(self.selectedVertexTaxQualifyingConditionOrg);
			setIsolatedVariables();
		};

		/**
		 * Gets current results for qualifying condition dropdown by calling api.
		 * @param query
		 */
		self.getCurrentQualifyingConditionDropDownResults = function(query){
			var request = ++LATEST_REQUEST;
			if (!(query === null || !query.length || query.length === 0)) {
				findTaxQualifyingConditionsByRegularExpression(query.toUpperCase(), request);
			} else {
				if(self.allTaxQualifyingConditions){
					self.qualifyingConditionValues = self.allTaxQualifyingConditions;
				} else {
					self.qualifyingConditionValues = [];
				}
			}
		};

		/**
		 * This is a setter for the all tax qualifying conditions to be used for the dropdown. This method will only
		 * call the back end if the user can edit tax qualifying conditions.
		 */
		function setAllTaxQualifyingConditions(){
			if(self.canUserEditResource("PD_INFO_24")){
				productInfoApi.getAllTaxQualifyingConditions({},
					//success
					function (results) {
						self.allTaxQualifyingConditions.push(NO_QUALIFYING_CONDITION);
						self.allTaxQualifyingConditions = self.allTaxQualifyingConditions.concat(results);
						self.qualifyingConditionValues = self.allTaxQualifyingConditions;
					},
					//error
					function(error) {
						fetchError('Error getting tax qualifying conditions', error);
					}
				);
			}
		}

		/**
		 * This method takes in a query the user has typed in the tax qualifying conditions drop down, and a number
		 * representing the request using the query. For each tax qualifying condition in the all tax qualifying
		 * conditions, if its display name contains the query (ignore case), add the qualifying condition to the list
		 * of conditions in the dropdown. After adding a condition, if the size of the list in the dropdown exceeds
		 * MAX_DROPDOWN_SIZE, stop the loop to look for more matches.
		 *
		 * @param queryUpperCased Typed in string from the user already upper-cased.
		 * @param request A number representing the current request asking to update the list.
		 */
		function findTaxQualifyingConditionsByRegularExpression(queryUpperCased, request){
			var qualifyingConditions = [];
			if(self.allTaxQualifyingConditions){
				var currentTaxQualifyingCondition;
				for(var index = 0; index < self.allTaxQualifyingConditions.length; index++){
					currentTaxQualifyingCondition = self.allTaxQualifyingConditions[index];
					if(currentTaxQualifyingCondition.displayName &&
						currentTaxQualifyingCondition.displayName.toUpperCase().includes(queryUpperCased)){
						qualifyingConditions.push(currentTaxQualifyingCondition);
						if(currentTaxQualifyingCondition.length === MAX_DROPDOWN_SIZE){
							break;
						}
					}
				}
			}

			// after finding all the results, if this is the latest request, set the list to the current value
			if(request === LATEST_REQUEST) {
				self.qualifyingConditionValues = qualifyingConditions;
			}
		}

		/**
		 * This method is called when a user that has edit permissions updates the selected qualifying condition. When
		 * this occurs, the tax qualifying code on product master needs to change, so the system is aware there was a
		 * change.
		 */
		self.updateQualifyingConditionCode = function(){
			self.currentProductMasterParams.productMaster.taxQualifyingCode =
				self.selectedVertexTaxQualifyingCondition ?
					self.selectedVertexTaxQualifyingCondition.dvrCode : null;
		};

		/**
		 * This method calls the back end to get all vertex tax category effective maintenance for the selected
		 * product. If a list is returned, set the variable self.allEffectiveVertexTaxCategories to the results.
		 * Else set self.allEffectiveVertexTaxCategories to empty array and display the error from the back end.
		 */
		self.getAllVertexTaxCategoryEffectiveMaintenanceForProduct = function(){
			self.isLoadingAllVertexTaxCategories = true;
			self.vertexTaxCategoryError = null;
			self.allEffectiveVertexTaxCategories = [];
			self.allEffectiveVertexTaxCategoriesOriginal = [];
			subCommodityForDefaults = null;
			productInfoApi.getAllVertexTaxCategoryEffectiveMaintenanceForProduct(
				{productId: self.productMaster.prodId},
				function(results){
					setDatesForDatePicker(results);
					angular.forEach(results, function(item){
						item.selectedRetailTaxable = item.retailTaxable? self.retailTaxables[1]:self.retailTaxables[2];
						if(item.effectiveDate == null){
							var tomorrow = getDateOffset(new Date(), 1);
							item.effectiveDateAsDate = tomorrow;
							item.effectiveDate = self.convertDateToStringWithYYYYMMDD(tomorrow);
							item.isCurrentTaxCategory = true;
						}
					});
					self.allEffectiveVertexTaxCategoriesOriginal = results;
					self.allEffectiveVertexTaxCategories = angular.copy(results);
					self.isLoadingAllVertexTaxCategories = false;
				}, function(error){
					self.vertexTaxCategoryError = getErrorMessage(error);
					self.isLoadingAllVertexTaxCategories = false;
				}
			);
			if(self.productMaster.subCommodity){
				subCommodityForDefaults = self.productMaster.subCommodity;
			} else {
				var key = {};
				key.subCommodityCode = self.productMaster.subCommodityCode;
				key.commodityCode = self.productMaster.commodityCode;
				key.classCode = self.productMaster.classCode;
				productInfoApi.getSubCommodityDefaults(
					key,
					function(result){
						subCommodityForDefaults = result;
					},
					function(error){
						self.vertexTaxCategoryError = getErrorMessage(error);
					}
				)
			}
		};

		/**
		 * This method calls the back end to get all product retail links for the selected product. If a list is
		 * returned, set the variable self.allProductRetailLinks to the results. Else set self.allProductRetailLinks to
		 * empty array and display the error from the back end.
		 */
		self.getAllProductRetailLinksByRetailLinkCode = function () {
			self.productRetailLinkError = null;
			self.allProductRetailLinks = [];
			self.waitLoadProductRetailLink = true;
			productInfoApi.getAllProductRetailLinksByRetailLinkCode(
				{retailLinkCode: self.productMaster.retailLink},
				function (results) {
					self.waitLoadProductRetailLink = false;
					self.allProductRetailLinks = results;
				}, function(error){
					self.waitLoadProductRetailLink = false;
					self.productRetailLinkError = getErrorMessage(error);
				}
			)
		};

		/**
		 * Adds a row to effective tax maintenance.
		 */
		self.addEffectiveTaxMaintenance = function(){
			if(self.isValidateEffectiveVertexTaxCategory()) {
				var newEffectiveTaxMaintenance = {};
				newEffectiveTaxMaintenance.productId = self.productMaster.prodId;
				newEffectiveTaxMaintenance.vertexTaxCategory = {};
				newEffectiveTaxMaintenance.vertexTaxCategory.displayName = "Select";
				newEffectiveTaxMaintenance.vertexTaxCategory.dvrCode = null;
				newEffectiveTaxMaintenance.isAdded =  true;
				var tomorrow = getDateOffset(new Date(), 1);
				newEffectiveTaxMaintenance.effectiveDateAsDate = tomorrow;
				newEffectiveTaxMaintenance.effectiveDate = self.convertDateToStringWithYYYYMMDD(tomorrow);
				newEffectiveTaxMaintenance.selectedRetailTaxable = self.retailTaxables[0];
				self.allEffectiveVertexTaxCategories.push(newEffectiveTaxMaintenance);
			}
		};

		/**
		 * Sends a call to api to update effective tax maintenance.
		 * @returns {String} true if the data needs to save is valid or false.
		 */
		self.updateEffectiveTaxMaintenance = function(){
			self.success = null;
			self.error = null;
			if(self.allEffectiveVertexTaxCategories == null || self.allEffectiveVertexTaxCategories.length == 0){
				if(self.allEffectiveVertexTaxCategoriesOriginal !== null && self.allEffectiveVertexTaxCategoriesOriginal.length == 1 &&
					self.allEffectiveVertexTaxCategoriesOriginal[0].isCurrentTaxCategory === true){
					self.vertexTaxCategoryError = self.DELETE_PRODUCT_RETAIL_ERROR_MESSAGE_TEXT;
					return false;
				}
				self.allEffectiveVertexTaxCategories = [];
				self.allEffectiveVertexTaxCategories.push({productId:self.productMaster.prodId, actionCode:DELETE_ACTION_CODE});
			}
			self.waitingForUpdate = true;
			productInfoApi.updateVertexTaxCategoryEffectiveMaintenance(
				self.allEffectiveVertexTaxCategories,
				function (results) {
					self.waitingForUpdate = false;
					self.success = self.UPDATE_SUCCESSFUL_MESSAGE;
				}, function(error){
					self.waitingForUpdate = false;
					fetchError('Error updating Vertex tax category', error);
				}
			)
			return true;
		};

		/**
		 * Convert a date (full day/ date/ time/ timezone) into hyphen-separated yyyy-MM-dd format.
		 *
		 * @param date Full date to convert.
		 * @returns {String}
		 */
		function convertDateToYYYYMMDD(date){
			if(date != null) {
				var d = new Date(date);
				var month = '' + (d.getMonth() + 1);
				var day = '' + d.getDate();
				var year = d.getFullYear();

				// If the month is 1-9, format has to be 0#.
				if (month.length < 2) {
					month = '0' + month;
				}

				// If the date is 1-9, format has to be 0#.
				if (day.length < 2) {
					day = '0' + day;
				}

				return [year, month, day].join('-');
			} else {
				return null;
			}
		}

		/**
		 * Resets effective tax maintenance to original values.
		 */
		self.resetEffectiveTaxMaintenance = function(){
			self.vertexTaxCategoryError = null;
			self.allEffectiveVertexTaxCategories = angular.copy(self.allEffectiveVertexTaxCategoriesOriginal);
		};

		/**
		 * Removes a row from effective tax maintenance.
		 *
		 * @param index The index of the effective tax maintenance in question.
		 */
		self.removeEffectiveTaxMaintenance = function (index) {
			self.allEffectiveVertexTaxCategories.splice(index, 1);
			self.isValidateEffectiveVertexTaxCategory();
		};

		/**
		 * Helper function to determine if the index in question is the first index.
		 *
		 * @param effectiveTaxCategory The tax catetory to look at.
		 * @returns {boolean} True if index is first index, false otherwise.
		 */
		self.isTaxCategoryReadOnly = function(effectiveTaxCategory){
			// return index === FIRST_ROW_INDEX;
			// return false;
			var isAdded = effectiveTaxCategory.isAdded === true;
			return !isAdded;
		};


		/**
		 * Whether the tax category save and reset buttons are disabled. This is true when the updatable effective tax
		 * maintenance records is not equal to the original effective tax maintenance records.
		 *
		 * @returns {boolean} True if disabled, false otherwise.
		 */
		self.isTaxCategorySaveAndResetButtonsDisabled = function (checkForValid) {
			if(checkForValid) {
				if (!isAllEffectiveDatesValid()) {
					return true;
				}
				if (!isAllEffectiveTaxCategoriesValid()) {
					return true;
				}
			}
			if(JSON.stringify(getEffectiveMaintenanceChanges(self.allEffectiveVertexTaxCategoriesOriginal, self.allEffectiveVertexTaxCategories)) === JSON.stringify({}) &&
				self.allEffectiveVertexTaxCategoriesOriginal.length == self.allEffectiveVertexTaxCategories.length){
				return true;
			}
			return false;
		};

		/**
		 * Whether a given index from effective tax maintenance records is edit-disabled or not. This will occur when
		 * the user does not have edit access, or if the index is the first record.
		 *
		 * @param effectiveTaxCategory The tax category to look at.
		 * @returns {boolean} True if disabled, false otherwise.
		 */
		self.isEffectiveRetailTaxDisabled = function(effectiveTaxCategory){
			return permissionsService.hasViewOnly('PD_INFO_25') || self.isTaxCategoryReadOnly(effectiveTaxCategory);
		};


		/**
		 * Call api to get all vertex tax category codes used in a sub commodity.
		 */
		self.getAllVertexTaxCategoryCodesBySubCommodityCode = function(){
			self.suggestedVertexTaxCategories = [];
			self.selectVertexTaxCategoryError = null;
			productInfoApi.getAllVertexTaxCategoryCodesBySubCommodityCode(
				{subCommodityCode: self.productMaster.subCommodityCode},
				function(results){
					self.allAvailableTaxCategories = results;
				},
				function(error){
					self.selectVertexTaxCategoryError = getErrorMessage(error);
				}
			)
		};

		/**
		 * Initialize the vertex tax category ng table.
		 */
		self.buildVertexTaxCategoryProductsTable = function(){
			self.vertexTaxCategoryProductParams = new ngTableParams(
				{
					// set defaults for ng-table
					page: 1,
					count: self.PAGE_SIZE
				}, {

					// hide page size
					counts: [],

					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {

						if(!self.selectedVertexTaxCategory){
							return [];
						}
						self.isWaitingForProducts = true;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.firstSearch){
							self.includeCount = true;
							params.page(1);
							self.firstSearch = false;
						} else {
							self.includeCount = false;
						}

						self.getProductsByVertexTaxCategoryCode(params.page() - 1);
					}
				}
			);
		};

		/**
		 * Call api to get a page of products by vertex tax category code.
		 *
		 * @param page Which page to retrieve.
		 */
		self.getProductsByVertexTaxCategoryCode = function (page) {
			if(self.selectedVertexTaxCategory) {
				var thisRequest = ++self.latestRequest;
				var parameters = {};
				parameters.page = page;
				parameters.pageSize = self.PAGE_SIZE;
				parameters.includeCount = self.includeCount;
				parameters.vertexTaxCategoryCode = self.selectedVertexTaxCategory;
				productInfoApi.getProductsByVertexTaxCategoryCode(
					parameters,
					function(results){
						if(thisRequest === self.latestRequest) {
							self.error = null;

							// If this was the fist page, it includes record count and total pages.
							if (results.complete) {
								self.totalRecordCount = results.recordCount;
								self.totalPages = results.pageCount;
							}

							// Return the data back to the ngTable.
							self.dataResolvingParams.total(self.totalRecordCount);
							self.defer.resolve(results.data);
							self.isWaitingForProducts = false;
						}
					}, setVertexTaxCategorySelectedError);
			}
		};

		/**
		 * Setter for vertex tax category selected error.
		 *
		 * @param error Error message to set.
		 */
		function setVertexTaxCategorySelectedError(error) {
			self.vertexTaxCategorySelectedError = getErrorMessage(error);
		}

		/**
		 * Sets given vertex tax category as the selected vertex tax category.
		 *
		 * @param vertexTaxCategory Tax category to set as selected vertex tax category.
		 */
		function setSelectedVertexTaxCategory(vertexTaxCategory){
			self.selectedVertexTaxCategory = vertexTaxCategory.dvrCode;
			self.resetProductTable(vertexTaxCategory.dvrCode);
		}

		/**
		 * Initializes the selected vertex tax category to the given tax category.
		 *
		 * @param effectiveTaxCategory Effective vertex tax category to set as the selected vertex tax category.
		 */
		self.initSelectedVertexTaxCategory = function(effectiveTaxCategory){
			self.resetProductTable(null);
			self.selectedVertexTaxCategoryList = null;
			self.allAvailableTaxCategories = [];
			var selectedVertexTaxCategory;
			if(effectiveTaxCategory.vertexTaxCategory.displayName !== "Select") {
				selectedVertexTaxCategory = effectiveTaxCategory.vertexTaxCategory;
			} else {
				selectedVertexTaxCategory = self.allEffectiveVertexTaxCategories[0].vertexTaxCategory;
			}
			setSelectedVertexTaxCategory(selectedVertexTaxCategory);
			self.allAvailableTaxCategories.push(selectedVertexTaxCategory);
			self.selectedEffectiveVertexTaxCategory = effectiveTaxCategory;
		};

		/**
		 * Resets the products seen on the vertex tax category popup to the currently selected vertex tax category.
		 *
		 * @param dvrCode Dvr code of the vertex tax category for the initial load of products.
		 */
		self.resetProductTable = function(dvrCode){
			self.vertexTaxCategorySelectedError = null;
			self.selectedVertexTaxCategory = dvrCode;
			self.firstSearch = true;
			self.vertexTaxCategoryProductParams.reload();
		};

		/**
		 * Calls api to get all tax categories.
		 */
		self.getAllTaxCategories = function(){
			if(self.allVertexTaxCategories === null){
				productInfoApi.getAllVertexTaxCategories(
					function (results) {
						self.allVertexTaxCategories = results;
						self.allAvailableTaxCategories = self.allVertexTaxCategories
					},
					function (error) {
						self.vertexTaxCategorySelectedError = getErrorMessage(error);
					}
				)
			} else {
				self.allAvailableTaxCategories = self.allVertexTaxCategories;
			}
		};

		/**
		 * Calls api to get the suggested tax categories, which are all the tax categories used within the sub-commodity
		 * this product is tied to.
		 *
		 */
		self.getSuggestedTaxCategories = function(){
			productInfoApi.getAllVertexTaxCategoryCodesBySubCommodityCode(
				{
					subCommodityCode: self.productMaster.subCommodityCode
				},
				function (results) {
					self.allAvailableTaxCategories = results;
				},
				function (error) {
					self.vertexTaxCategorySelectedError = getErrorMessage(error);
				}
			)
		};

		/**
		 * Sets a variable to true for a particular record in a list so only one datePicker is shown at a time.
		 *
		 * @param openIndex Index of list to set as true for date picker opened.
		 */
		self.openDatePicker = function(openIndex){
			for(var index = 0; index < self.allEffectiveVertexTaxCategories.length; index++){
				self.allEffectiveVertexTaxCategories[index].showEffectiveDatePopup = false;
			}
			self.allEffectiveVertexTaxCategories[openIndex].showEffectiveDatePopup = true;
		};

		/**
		 * Returns whether tax/ non-tax tax category is different then the sub-commodity default.
		 * If effective tax category is taxable: returns effective tax category not equal to sub commodity tax category
		 * If effective tax category is non-taxable: returns effective tax category not equal to sub commodity non-tax category
		 * Else return false.
		 *
		 * @param effectiveTaxCategory Effective tax category to compare to sub commodity default
		 * @returns {boolean} True if different than sub commodity default, false otherwise.
		 */
		self.isTaxCategoryDifferentThanSubCommodityDefaultOnPopup = function(effectiveTaxCategory){
			if(self.taxAndNonTaxCategoryBySubCommodity != null && self.taxAndNonTaxCategoryBySubCommodity !== undefined && effectiveTaxCategory.vertexTaxCategory !== undefined) {
				if (effectiveTaxCategory.retailTaxable) {
					if(self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] != null
						&& self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] !== undefined
						&& self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode != null) {
						return effectiveTaxCategory.vertexTaxCategory.dvrCode !== self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode;
					}
				} else {
					if(self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] != null
						&& self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] !== undefined
						&& self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode != null) {
						return effectiveTaxCategory.vertexTaxCategory.dvrCode !== self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode;
					}
				}
				return true;
			} else {
				return false;
			}
		};

		/**
		 * Returns whether tax/ non-tax tax category is different then the sub-commodity default.
		 * If effective tax category is taxable: returns effective tax category not equal to sub commodity tax category
		 * If effective tax category is non-taxable: returns effective tax category not equal to sub commodity non-tax category
		 * Else return false.
		 *
		 * @returns {boolean} True if different than sub commodity default, false otherwise.
		 */
		self.isTaxCategoryDifferentThanSubCommodityDefault = function(){
			if(self.taxAndNonTaxCategoryBySubCommodity != null && self.taxAndNonTaxCategoryBySubCommodity !== undefined) {
				if (self.currentProductMasterParams.vertexTaxCategory && self.currentProductMasterParams.vertexTaxCategory.dvrCode) {
					if (self.currentProductMasterParams.productMaster.goodsProduct !== null 
							&& self.currentProductMasterParams.productMaster.goodsProduct.retailTaxSwitch) {
						if(self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] != null
							&& self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] !== undefined
							&& self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode != null) {
							return self.currentProductMasterParams.vertexTaxCategory.dvrCode !== self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode;
						}
					} else {
						if(self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] != null
							&& self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] !== undefined
							&& self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode != null) {
							return self.currentProductMasterParams.vertexTaxCategory.dvrCode !== self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode;
						}
					}
					return true;
				}
			}
			return false;
		};

		/**
		 * Gets a tax category warning message when the taxable/ non-taxable tax category is different than the
		 * sub-commodity default.
		 *
		 * @param effectiveTaxCategory Effective tax category that is different than sub commodity default.
		 * @returns {string}
		 */
		self.getTaxCategoryDefaultWarningMessageOnPopup = function(effectiveTaxCategory){
			return TAX_CATEGORY_DIFFERENT_THAN_SUB_COMMODITY_MESSAGE_TEMPLATE
				.replace("$nonTaxCategory", effectiveTaxCategory.retailTaxable ? "" : "Non-");
		};

		/**
		 * Gets a tax category warning message when the taxable/ non-taxable tax category is different than the
		 * sub-commodity default.
		 *
		 * @returns {string}
		 */
		self.getTaxCategoryDefaultWarningMessage = function(){
			return TAX_CATEGORY_DIFFERENT_THAN_SUB_COMMODITY_MESSAGE_TEMPLATE.replace("$nonTaxCategory", 
						((self.currentProductMasterParams.productMaster.goodsProduct !== null 
								&& self.currentProductMasterParams.productMaster.goodsProduct.retailTaxSwitch) ? "" : "Non-"));
		};

		/**
		 * Set the value as a date so date picker understands how to represent value.
		 */
		function setDatesForDatePicker(effectiveTaxCategories){
			for(var index = 0; index < effectiveTaxCategories.length; index++) {
				effectiveTaxCategories[index].showEffectiveDatePopup = false;
				effectiveTaxCategories[index].duplicateDate = false;
				if (effectiveTaxCategories[index].effectiveDate) {
					effectiveTaxCategories[index].effectiveDateAsDate = new Date(effectiveTaxCategories[index].effectiveDate.replace(/-/g, '\/'));
				}
			}
		}

		/**
		 * Gets a date offset by a certain number of days. This can be used to return 'dayOffset' days from a given
		 * date.
		 *
		 * @param date Date to offset.
		 * @param dayOffset Number of days to offset the given date.
		 * @returns {Date}
		 */
		function getDateOffset(date, dayOffset){
			if(!date || date === null){
				return null;
			}
			if(!dayOffset || dayOffset === null){
				dayOffset = 0;
			}
			var returnDate = date.setDate(date.getDate() + dayOffset);
			return new Date(returnDate);
		}

		/**
		 * Checks if all the effective dates for the effective maintenance records are valid. If one of the dates
		 * is not set, return false. Else if a date is used more than once, return false. Else return true.
		 *
		 * @returns {boolean}
		 */
		function isAllEffectiveDatesValid(){
			var effectiveDatesUsed = {};
			var currentEffectiveDate;
			for(var index = 0; index < self.allEffectiveVertexTaxCategories.length; index++){
				currentEffectiveDate = getNormalizedDateString(self.allEffectiveVertexTaxCategories[index].effectiveDateAsDate);
				if(currentEffectiveDate === null){
					return false;
				} else if(typeof effectiveDatesUsed[currentEffectiveDate] !== 'undefined'){
					return false;
				} else {
					effectiveDatesUsed[currentEffectiveDate] = index;
				}
			}
			return true;
		}

		/**
		 * This method checks for duplicate effective dates within a list of effective maintenance. The data allows
		 * for more than one effective maintenance, but it shouldn't exist. If a duplicate date is found, set a
		 * duplicate date variable so the UI can display to the user that a date can only be used once.
		 */
		function checkForDuplicateEffectiveDates(){
			var effectiveDatesUsed = {};
			var currentEffectiveDate;
			for(var index = 0; index < self.allEffectiveVertexTaxCategories.length; index++){
				self.allEffectiveVertexTaxCategories[index].duplicateDate = false;
				currentEffectiveDate = getNormalizedDateString(self.allEffectiveVertexTaxCategories[index].effectiveDateAsDate);
				if(currentEffectiveDate !== null) {
					if (typeof effectiveDatesUsed[currentEffectiveDate] !== 'undefined') {
						self.allEffectiveVertexTaxCategories[index].duplicateDate = true;
						self.allEffectiveVertexTaxCategories[effectiveDatesUsed[currentEffectiveDate]].duplicateDate = true;
					} else {
						effectiveDatesUsed[currentEffectiveDate] = index;
					}
				}
			}
		}

		/**
		 * Helper function that gets the string value for a date. This was necessary because the back end dates were
		 * being set to 12 noon, and dates selected in the datePicker were midnight. This function normalizes the dates
		 * such that two dates will be considered equal if their Month/ Day/ Year matches regardless of time.
		 *
		 * @param date Date to retrieved normalized string version of.
		 * @returns {String} String value of day (without time).
		 */
		function getNormalizedDateString(date){
			if(!date || date === null){
				return null;
			}
			return date.toDateString();
		}

		/**
		 * Helper function to determine if all of the vertex tax categories are valid. If there is a vertex tax
		 * category without a dvrCode (i.e. a new row that hasn't had the tax category selected), return false.
		 * Else return true;
		 *
		 * @returns {boolean} True if valid, false otherwise.
		 */
		function isAllEffectiveTaxCategoriesValid(){
			for(var index = 0; index < self.allEffectiveVertexTaxCategories.length; index++){
				if(self.allEffectiveVertexTaxCategories[index].vertexTaxCategory.dvrCode === null){
					return false;
				}
			}
			return true;
		}

		/**
		 * This method sets the vertex tax category that has been selected as the vertex tax category for the
		 * effective maintenance that is being modified.
		 */
		self.setEffectiveVertexTaxCategory = function(){
			var vertexTaxCategory;
			for(var index = 0; index < self.allAvailableTaxCategories.length; index++){
				vertexTaxCategory = self.allAvailableTaxCategories[index];
				if(vertexTaxCategory.dvrCode === self.selectedVertexTaxCategory){
					self.selectedEffectiveVertexTaxCategory.vertexTaxCategory = vertexTaxCategory;
					self.selectedEffectiveVertexTaxCategory.selectedRetailTaxable = self.getSelectedRetailFlagByTaxCatOrNonTaxCat(vertexTaxCategory, self.selectedEffectiveVertexTaxCategory.selectedRetailTaxable);
					self.selectedEffectiveVertexTaxCategory.retailTaxable = self.selectedEffectiveVertexTaxCategory.selectedRetailTaxable.id;
					break;
				}
			}
		};

		/**
		 * Creates a vertex tax category message for the user to see if they were to click on the 'Apply' button. This
		 * message states they are about to set the selected vertex tax category as the vertex tax category for the
		 * current effective maintenance being modified.
		 *
		 * @returns {string}
		 */
		self.getSelectedVertexTaxCategoryApplyMessage = function(){
			return SELECTED_VERTEX_TAX_CATEGORY_APPLY_MESSAGE_TEMPLATE
				.replace("$selectedVertexTaxCategory", self.selectedVertexTaxCategory);
		};

		/**
		 * This function modifies the effective date (value kept on back end) when the user changes the effective date
		 * in a popup (variable effectiveDateAsDate is only kept on front end) so the front end system understands
		 * the data has changed. This method also call a duplicate date checker that needs to be looked to ensure
		 * there are no duplicate dates.
		 *
		 * @param effectiveTaxCategory Effective tax category that needs to update it's effective date.
		 */
		self.updateEffectiveDate = function(effectiveTaxCategory){
			effectiveTaxCategory.effectiveDate = convertDateToYYYYMMDD(effectiveTaxCategory.effectiveDateAsDate);
			checkForDuplicateEffectiveDates();
		};

		/**
		 * This recursively traverses through an object that may contain lists or other objects inside of it. This will
		 * check each field inside of each object or list to determine whether or not that field has changed. If it has
		 * changed, it saves it to a temporary and then the temporary will be returned.
		 *
		 * @param original Original state of object in question
		 * @param current Current state of the object in question
		 * @returns {Object} Object containing the changes
		 */
		function getEffectiveMaintenanceChanges(original, current) {
			var temp = {};
			for (var key in current) {
				if (!current.hasOwnProperty(key)) continue;

				// account for new rows of effective maintenance
				if(typeof original === 'undefined'){
					if(isInt(key)) {
						if(!Array.isArray(temp)) {
							temp = [];
						}
						temp[key] = getEffectiveMaintenanceChanges(null, current[key]);
					} else {
						temp[key] = current[key];
					}
				}

				// else compare current value to original value
				else if(angular.toJson(current[key]) !== angular.toJson(original[key])) {
					// It is in a list. This will look through a list of things to check whether any of them have
					// changed.
					if(isInt(key)) {
						if(!Array.isArray(temp)) {
							temp = [];
						}
						temp[key] = getEffectiveMaintenanceChanges(original[key], current[key]);
					} else {
						temp[key] = current[key];
					}
				}
			}
			return temp;
		}

		/**
		 * Whether or not the value is an integer. This determines whether or not it is in a key.
		 * @param value Value to check if is an integer
		 * @returns {boolean}
		 */
		function isInt(value) {
			var x = parseFloat(value);
			return !isNaN(value) && (x | 0) === x;
		}

		/**
		 * This method returns the duplicate date error text with the date that was duplicated.
		 *
		 * @param effectiveDate Date to show in error text.
		 * @returns {string}
		 */
		self.getDuplicateErrorMessageForDate = function(effectiveDate){
			return DUPLICATE_DATE_ERROR_TEXT.replace('$effectiveDate', effectiveDate);
		};

		/**
		 * Show product information audit information modal
		 */
		self.showProductInfoAuditInfo = function () {
			self.productInfoAuditInfo = productInfoApi.getProductInfoAudits;
			var title ="Product Info";
			var parameters = {'prodId' :self.productMaster.prodId};
			productDetailAuditModal.open(self.productInfoAuditInfo, parameters, title);
		};

		/**
		 * Initializes all map prices to their formatted values. This was done to account for showing 2 decimal places
		 * in an input field using angular filter.
		 *
		 * @param mapPrices
		 */
		function setFormattedMapPrices(mapPrices){
			angular.forEach(mapPrices, function(mapPrice){
				self.formatMapPrice(mapPrice);
			});
		}

		/**
		 * Formats the map price {since an angular filter could not be placed on an html input}.
		 */
		self.formatMapPrice = function(mapPrice){
			var twoDecimalFormat = parseFloat(mapPrice.mapAmount).toFixed(TWO_DECIMAL_PLACES);
			// if the map amount is a number, set the value to always have 2 decimal places
			if(!isNaN(twoDecimalFormat)){
				mapPrice.mapAmount = twoDecimalFormat;
			}
			// otherwise leave it as is
		};

		/**
		 * Resets a map price's map amount to null if it is currently an empty string {when an input:text is modified,
		 * then modified again to show nothing, the value is "" not 'null'}. This is done so that when there is no
		 * value, it is treated the same as a null value to compare against the original value.
		 *
		 * @param mapPrice
		 */
		self.resetMapAmountIfEmpty = function(mapPrice){
			if(mapPrice.mapAmount === ""){
				mapPrice.mapAmount = null;
			}
		};

		/**
		 * Whether a given user has edit permission on food stamp
		 *
		 * @param foodStamp The food stamp product to look at.
		 * @returns {boolean} True if disabled, false otherwise.
		 */
		self.isFoodStampDisabled = function(){
			return permissionsService.hasViewOnly('PD_INFO_27');
		};

		/**
		 * Returns whether the food stamp attribute is same as the sub-commodity default.
		 *
		 * @param foodStamp Current food stamp product compared to sub commodity default
		 * @returns {boolean} True if same as sub commodity default, false otherwise.
		 */
		self.isFoodStampSameAsSubCommodityFoodStamp = function(){
			if (self.productMaster.subCommodity !== undefined 
					&& self.productMaster.subCommodity !== null
					&& self.currentProductMasterParams.productMaster.goodsProduct !== null) {
				return self.currentProductMasterParams.productMaster.goodsProduct.foodStampSwitch === self.productMaster.subCommodity.foodStampEligible;
			}
			return false;
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function () {
			var toDate = new Date();
			var nameFileExport = 'SearchResults_' + toDate.getMonth().toString() +'_' + toDate.getDay().toString() +'_' + toDate.getFullYear().toString()
				+ '_' + toDate.getHours().toString() + '_' + toDate.getMinutes().toString() + '_' + toDate.getSeconds().toString() + '.csv' ;
			var encodedUri = urlBase + '/pm/productInformation/exportProductRetailLinksToCsv?' + 'retailLinkCode=' + self.productMaster.retailLink;
			if (encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, nameFileExport, self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}
		}
		/**
		 * handle when click on return to list button
		 */
		self.returnToList = function(){
			$rootScope.$broadcast('returnToListEvent');
		};
		/**
		 * Get Get the tax and non tax category by sub-commodity.
		 */
		self.getTaxAndNonTaxCategoryBySubCommodity = function(){
			self.isLoadingVertexTaxCategory = true;
			var key = {};
			key.subCommodityCode = self.productMaster.subCommodityCode;
			key.commodityCode = self.productMaster.commodityCode;
			key.classCode = self.productMaster.classCode;
			productInfoApi.getTaxAndNonTaxCategoryBySubCommodity(key,
				function (result) {
					self.taxAndNonTaxCategoryBySubCommodity = result;
					self.isLoadingVertexTaxCategory = false;
				},
				function (error) {
					self.isLoadingVertexTaxCategory = false;
					fetchError('Error getting Vertex tax category', error);
				});
		}
		/**
		 * Return the retail tax warning message to show on retail tax warning icon.
		 * @return {string}
		 */
		self.getRetailTaxWarningMessage = function(){
			var retailTaxSwitchLabel = '';
			var taxEligibleLabel= '';
			if (self.currentProductMasterParams.productMaster.goodsProduct !== null
					&& self.currentProductMasterParams.productMaster.goodsProduct.retailTaxSwitch != self.productMaster.subCommodityCode.taxEligible) {
				if (self.currentProductMasterParams.productMaster.goodsProduct.retailTaxSwitch) {
					retailTaxSwitchLabel = "Y=Yes";
					taxEligibleLabel = "N=No";
				} else {
					taxEligibleLabel = "Y=Yes";
					retailTaxSwitchLabel = "N=No";
				}
			}
			var msg = RETAIL_TAX_DIFFERENT_THAN_SUB_COMMODITY_MESSAGE_TEMPLATE
				.replace("$retailTaxSwitchLabel", retailTaxSwitchLabel);
			msg  = msg.replace("$taxEligibleLabel", taxEligibleLabel);
			return msg;
		}
		/**
		 * This method will be called when user click on Close button of tax category modal.
		 * It will check the change of data. if there is any the data has been changed, then show save confirmation,
		 * otherwise the tax category modal will be closed.
		 */
		self.closeOrShowSaveConfirmTaxCategoryModal = function(){
			if(self.isTaxCategorySaveAndResetButtonsDisabled(false)) {
				self.vertexTaxCategoryError = null;
				$('#taxCategoryModal').modal('hide');
			}else{
				$('#confirmModal').modal({backdrop: 'static', keyboard: false});
				$('.modal-backdrop').attr('style',' z-index: 100000; ');
			}
		}
		self.saveEffectiveTaxMaintenance = function(){
			if(self.isValidateEffectiveVertexTaxCategory()) {
				if(self.updateEffectiveTaxMaintenance()) {
					$('#taxCategoryModal').modal('hide');
				}
			}
		}
		/**
		 * Validate the EffectiveTaxCategories and save it if it's valid.
		 * This method will bed called when user click on save button of save confirmation poppup.
		 */
		self.saveTaxCategoryOnConfirmationPopup = function(){
			if(self.isValidateEffectiveVertexTaxCategory()) {
				if(self.updateEffectiveTaxMaintenance()) {
					self.vertexTaxCategoryError = null;
					$('#taxCategoryModal').modal('hide');
				}
				self.hideConfirmationPopup();
			}else{
				self.hideConfirmationPopup();
			}
		}
		/**
		 * Hide save confirmation popup and tax category popup when user click on no button of save confirmation popup.
		 */
		self.cancelConfirmationPopup = function(){
			self.vertexTaxCategoryError = null;
			$('#taxCategoryModal').modal('hide');
			self.hideConfirmationPopup();
		}
		/**
		 * Hide save confirmation popup.
		 */
		self.hideConfirmationPopup = function(){
			$('#confirmModal').modal('hide');
			$('.modal-backdrop').attr('style','');
		}
		/**
		 * Convert the date to string with format: yyyy-MM-dd.
		 * @param date the date object.
		 * @returns {*} string
		 */
		self.convertDateToStringWithYYYYMMDD = function (date) {
			return $filter('date')(date, 'yyyy-MM-dd');
		};
		/**
		 * Reset the RetailTaxable when RetailTaxable select box has changed.
		 * @param effectiveTaxCategory the effectiveTaxCategory.
		 */
		self.onRetailTaxable = function(effectiveTaxCategory){
			effectiveTaxCategory.retailTaxable = effectiveTaxCategory.selectedRetailTaxable.id;
			var selectedDvrCode = null;
			if(effectiveTaxCategory.retailTaxable != null) {
				if (effectiveTaxCategory.retailTaxable && self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] != undefined &&
					self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] != null && self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode != null) {
					selectedDvrCode = self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode;
				} else if (!effectiveTaxCategory.retailTaxable && self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] != undefined &&
					self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] != null && self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode != null) {
					selectedDvrCode = self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode;
				}
			}
			if(selectedDvrCode!= null && self.allAvailableTaxCategories!=null && self.allAvailableTaxCategories !== undefined) {
				for (var index = 0; index < self.allAvailableTaxCategories.length; index++) {
					if (self.allAvailableTaxCategories[index].dvrCode == selectedDvrCode) {
						effectiveTaxCategory.vertexTaxCategory = angular.copy(self.allAvailableTaxCategories[index]);
					}
				}
			}
		}
		/**
		 * Return true if the allEffectiveVertexTaxCategories are valid or false.
		 * @return {boolean}
		 */
		self.isValidateEffectiveVertexTaxCategory = function(){
			self.vertexTaxCategoryError = null;
			var errorMsgs = [];
			var effectiveDatesUsed = {};
			var currentEffectiveDate;
			for(var index = 0; index < self.allEffectiveVertexTaxCategories.length; index++){
				if(self.allEffectiveVertexTaxCategories[index].retailTaxable == null || self.allEffectiveVertexTaxCategories[index].retailTaxable == undefined
					|| self.allEffectiveVertexTaxCategories[index].selectedRetailTaxable.id == null){
					errorMsgs.push('<li>Retail Tax Flag must be entered at row ' + (index + 1)+'.</li>');
				}
				if(self.allEffectiveVertexTaxCategories[index].vertexTaxCategory.dvrCode === null){
					errorMsgs.push('<li>Tax Category must be entered at row ' + (index + 1)+'.</li>');
				}
				currentEffectiveDate = getNormalizedDateString(self.allEffectiveVertexTaxCategories[index].effectiveDateAsDate);
				if (typeof effectiveDatesUsed[currentEffectiveDate] !== 'undefined') {
					errorMsgs.push('<li>Effective Date ' + currentEffectiveDate + ' is duplicated at row ' + (index + 1)+'.</li>');
				}else{
					effectiveDatesUsed[currentEffectiveDate] = index;
				}
			}
			if(errorMsgs.length > 0 ){
				// Show error message on popup
				var errorMessagesAsString = '<ul class="text-left">';
				angular.forEach(errorMsgs, function (errorMessage) {
					errorMessagesAsString += errorMessage;
				});
				errorMessagesAsString+='</ul>';
				self.vertexTaxCategoryError = errorMessagesAsString;
				return false;
			}
			return true;
		}
		/**
		 * Return selected retail flag by tax or non tax with selected effectiveTaxCategory's drv code.
		 * @param effectiveTaxCategory the effectiveTaxCategory.
		 * @param selectedRetailTaxable the selected Retail Taxable.
		 * @return {*}
		 */
		self.getSelectedRetailFlagByTaxCatOrNonTaxCat = function(effectiveTaxCategory, selectedRetailTaxable){
			var selectedRetailTax = selectedRetailTaxable;
			if(effectiveTaxCategory != null && self.taxAndNonTaxCategoryBySubCommodity != null){
				if (self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] != undefined &&
					self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY] != null &&
					self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode != null &&
					self.taxAndNonTaxCategoryBySubCommodity[TAX_CATEGORY_KEY].dvrCode == effectiveTaxCategory.dvrCode) {
					selectedRetailTax = self.retailTaxables[1];
				}
				if (self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] != undefined &&
					self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY] != null &&
					self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode != null &&
					self.taxAndNonTaxCategoryBySubCommodity[NON_TAX_CATEGORY_KEY].dvrCode == effectiveTaxCategory.dvrCode) {
					selectedRetailTax = self.retailTaxables[2];
				}
			}
			return selectedRetailTax;
		}

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
		 * Returns createUser or '' if not present. productMaster
		 */
		self.getCreateUser = function() {
			if(self.productMaster.displayCreatedName === null || self.productMaster.displayCreatedName.trim().length == 0) {
				return '';
			}
			return self.productMaster.displayCreatedName;
		};

		/**
		 * Clear message.
		 */
		self.clearMessaage = function(){
			self.error = '';
			self.success='';
		};
		/**
		 * Clear message listener.
		 */
		$scope.$on(self.CLEAR_MESSAGE, function() {
			self.clearMessaage();
		});
		self.prePriceNullToZero = function() {
			if (self.currentProductMasterParams.productMaster.goodsProduct !== null 
					&& self.currentProductMasterParams.productMaster.goodsProduct.prePrice==null) {
				self.currentProductMasterParams.productMaster.goodsProduct.prePrice=0;
			}
		}
		/**
		 * Handles the results of the confirmation modal
		 */
		$rootScope.$on('modalResults', function(event, args){
			if(args.result){
				self.currentProductMasterParams = angular.copy(self.originalProductMasterParams);
                self.success = null;
				self.error = null;
				self.selectedVertexTaxQualifyingCondition = angular.copy(self.selectedVertexTaxQualifyingConditionOrg);
                setIsolatedVariables();
			}
		});
	}

})();
