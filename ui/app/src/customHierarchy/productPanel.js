/*
 *   productPanel.js
 *
 *   Copyright (c) 2018 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Custom Hierarchy -> product panel component.
 *
 * @author l730832
 * @since 2.16.0
 */
(function () {

	angular.module('productMaintenanceUiApp').component('productPanel', {
		// isolated scope binding
		bindings: {
			currentLevel: '<'
		},
		scope: {},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/customHierarchy/productPanel.html',
		// The controller that handles our component logic
		controller: productPanelController
	});


	productPanelController.$inject = ['customHierarchyService', 'CustomHierarchyApi', 'PermissionsService', 'ngTableParams', 'ProductGroupService', '$state', 'appConstants', 'ProductSearchService', '$timeout', '$scope'];

	/**
	 * Product Panel controller definition.
	 * @constructor
	 * @param customHierarchyService
	 * @param customHierarchyApi
	 * @param permissionsService
	 * @param ngTableParams
	 */
	function productPanelController(customHierarchyService, customHierarchyApi, permissionsService, ngTableParams, productGroupService, $state, appConstants, productSearchService, $timeout, $scope) {
		var self = this;
		/**
		 * Search type product for ecommerce view
		 * @type {String}
		 */
		const SEARCH_TYPE = "basicSearch";
		/**
		 * Selection type product for ecommerce view
		 * @type {String}
		 */
		const SELECTION_TYPE = "Product ID";
		/**
		 * Product info tab
		 * @type {String}
		 */
		const PRODUCT_INFO_TAB = 'productInfoTab';
		/**
		 * Ecommerce view tab
		 * @type {String}
		 */
		const ECOMMERCE_VIEW_TAB = 'ecommerceViewTab';
		// Mass update to remove a product.
		self.REMOVE_PRODUCTS = "UNASSIGN_PRODUCTS";
		self.ADD_HIERARCHY_PRODUCT = "ADD_HIERARCHY_PRODUCT";
		self.REMOVE_HIERARCHY_PRODUCT = "REMOVE_HIERARCHY_PRODUCT";
		self.MOVE_HIERARCHY_PRODUCT = "MOVE_HIERARCHY_PRODUCT";
		self.ADD_HIERARCHY_PRODUCT_GROUP = "ADD_HIERARCHY_PRODUCT_GROUP";
		self.REMOVE_HIERARCHY_PRODUCT_GROUP = "REMOVE_HIERARCHY_PRODUCT_GROUP";
		self.descriptionBooleanValue = "true";
		self.descriptionStringValue = "";
		self.description = null;
		self.selectedProducts = [];
		self.transactionMessage = null;
		self.actionCode = null;
		self.ACTION_CODE_ADDING = 'Y';
		self.ACTION_CODE_REMOVING = 'D';
		self.data = [];
		self.includeCounts = true;
		self.isWaitingForProductGroups = false;
		self.firstSearch = true;
		self.builtNgTableParamsTable = false;
		self.PRIMARY_PATH_NOT_FOUND_ERROR = "Error: Primary path not found." +
			"Click to set.";
		self.MORE_THAN_ONE_PRIMARY_PATH_ERROR = "Error: More than one primary path specified." +
			"Click to set.";
		self.productOrProductGroupTab = "PRODUCT";
		self.currentSelectedProductGroup = null;
		self.MORE_THAN_ONE_PRIMARY_PATH_ERROR = "Error: More than one primary path specified.";
		self.currentSelectedProductData = null;

		self.newDefaultParent=null;
		self.newDefaultParentDescription=null;

		self.massUpdateDescription = null;

		self.allSelected = false;
		const antiSelectedList=[];
		var PAGE_SIZE = 10;
		var productOrProductGroupTabSelected = "PRODUCT";
		var latestRequest = 0;
		var MASS_FILL_ATTRIBUTE = "MASS_FILL";
		var PRODUCT_GROUP_ENTITY_TYPE = "PGRP";
		/**
		 * The number of rows per page constant when navigate to Home page.
		 * @type {int}
		 */
		self.NAVIGATE_PAGE_SIZE = 100;
		/**
		 * Active product group tab class
		 * @type {String}
		 */
		self.activeProductGroupTabClass = null;
		/**
		 * Active product tab class
		 * @type {String}
		 */
		self.activeProductTabClass = null;
		/**
		 * Product group tab pane class
		 * @type {String}
		 */
		self.productGroupTabPaneClass = null;
		/**
		 * Product tab pane class
		 * @type {String}
		 */
        self.productTabPaneClass = null;
        /**
		 * Flag first load product panel
		 * @type {Boolean}
		 */
        self.isFirstLoadProductPanel = false;
		const FROM_PRODUCT_GROUP_SEARCH = "FROM_PRODUCT_GROUP_SEARCH";

		self.$onInit = function(){
			if( customHierarchyService.getSelectedTab()== 'PRODUCT_GROUP') {
				customHierarchyService.setSelectedTab('PRODUCT');
				self.toggleProductOrProductGroupTab('PRODUCT_GROUP');
				self.productGroupTabPaneClass = 'tab-pane active';
				self.productTabPaneClass = 'tab-pane';
				self.activeProductGroupTabClass = 'ng-scope active';
				self.activeProductTabClass = '';
			}
			else{
				self.toggleProductOrProductGroupTab('PRODUCT');
				self.activeProductTabClass = 'ng-scope active';
				self.productTabPaneClass = 'tab-pane active';
				self.productGroupTabPaneClass = 'tab-pane';
				self.activeProductGroupTabClass = '';
			}
		};

		self.$onChanges = function () {
			self.isFirstLoadProductPanel = true;
			self.selectedProducts = [];
			self.massUpdateType = null;
			self.description = "";
			self.transactionMessage = null;
			self.firstSearch = true;
			self.allSelected = false;
			self.newDefaultParent = {
				key: {
					parentEntityId: self.currentLevel.key.parentEntityId,
					childEntityId: self.currentLevel.key.childEntityId,
					hierarchyContext: self.currentLevel.key.hierarchyContext
				},
				defaultParentSwitch: true,
				newDataSwitch: true
			};
			self.newDefaultParentDescription = "->"+self.currentLevel.childDescription.shortDescription;
			self.toggleProductOrProductGroupTab('PRODUCT');
			self.activeProductTabClass = 'ng-scope active';
			self.productTabPaneClass = 'tab-pane active';
			self.productGroupTabPaneClass = 'tab-pane';
			self.activeProductGroupTabClass = '';
			// The first time through, build the table. The rest of the time, just tell it to fetch new data.
			if (self.tableBuilt) {
				self.tableParams.reload();
			} else {
				self.tableBuilt = true;
				self.buildTable();
			}
		};

		/**
		 * Navigates to the selected Product Group by the Product Group Id
		 * @param productGroupId
		 */
		self.goToProductGroup = function(productGroupId){
			productGroupService.setBrowserAll(false);
			customHierarchyService.setDisableReturnToList(true);
			productGroupService.setNavigateFromCustomerHierPage(true);
			productGroupService.setProductGroupId(productGroupId);
			productGroupService.setListOfProducts(self.tableParams.data);
			$state.go(appConstants.PRODUCT_GROUP);
		};

		/**
		 * Clears out the modal.
		 */
		self.clearModal = function() {
			self.searchCriteria = "";
			self.description = null;
		};

		/**
		 * This method will take a product ID link in the custom hierarchy and take the user to the products ecom view
		 * page
		 * @param productId
		 */
		self.goToProduct = function(productId) {
			var productIds = [];
			productIds.push(productId);
			productSearchService.setListOfProducts(productIds.toString());
			productSearchService.setToggledTab('ecommerceViewTab');
			productSearchService.setSelectedTab('ecommerceViewTab');
			$state.go(appConstants.HOME_STATE);
		};

		/**
		 * These are all of the selected products to Add or move for a hierarchy.
		 * @param selectedProducts
		 */
		self.setSelectedProducts = function(selectedProducts) {
			self.selectedProducts = [];
			angular.forEach(selectedProducts, function(product) {
				self.selectedProducts.push(product.prodId);
			});
		};

		/**
		 * This removes all of the selected products that are checked.
		 */
		self.fillBatchUpdate = function() {
			var massUpdateParameters = {
				attribute: self.massUpdateType,
				booleanValue: self.descriptionBooleanValue,
				stringValue: self.descriptionStringValue,
				description: self.description,
				rootId: customHierarchyService.getHierarchyContextSelected().parentEntityId,
				entityRelationship: self.entityRelationship
			};

			/**
			 * the search criteria for the
			 * @type {{productIds: string}}
			 */
			var productSearchCriteria = {
				productIds: self.selectedProducts.toString()
			};

			/**
			 * The mass update request.
			 * @type {{parameters: {attribute: null|*, booleanValue: string, stringValue: string, description: null, rootId: number}, productSearchCriteria: {productIds: string}}}
			 */
			var massUpdateRequest = {
				parameters: massUpdateParameters,
				productSearchCriteria: productSearchCriteria
			};

			self.transactionMessage = null;
			self.submittedMassUpdate = true;
			customHierarchyApi.massUpdate(massUpdateRequest, self.massUpdateSuccess, self.fetchError);
		};

		/**
		 * Callback for a successful start of a mass update job.
		 *
		 * @param data The data sent from the back end.
		 */
		self.massUpdateSuccess = function(data) {
			customHierarchyService.setSuccessMessage(data.message, true);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			self.isWaitingForData = false;
			self.data = null;
            self.isFirstLoadProductPanel = false;
			self.setError(getError(error));
		};

		/**
		 * Gets the error given an api error message.
		 *
		 * @param error Error message from api.
		 * @returns {string}
		 */
		function getError(error){
			if (error && error.data) {
				if(error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			}
			else {
				return "An unknown error occurred.";
			}
		}

		/**
		 * Sets the controller's error message.
		 * @param error The error message.
		 */
		self.setError = function (error) {
			self.error = error;
		};

		/**
		 * This mthod reset the description string value
		 */
		self.clearDescription = function() {
			self.description = "";
		};

		/**
		 * This is used for the html to be able to determine which modal is opened up.
		 * @param boolean
		 */
		self.setRemoveValue = function(boolean) {
			self.massUpdateType = self.REMOVE_HIERARCHY_PRODUCT;
			self.isRemovingValue = boolean;
			if(self.allSelected){
				self.totalUpdates = self.totalRecordCount - antiSelectedList.length;
			} else {
				self.totalUpdates = antiSelectedList.length;
			}
		};

		/**
		 * This clears the selected product list whenever the modal is closed.
		 */
		self.clearProductList = function() {
			self.selectedProducts = [];
			self.massUpdateType = null;
		};

		/**
		 * Sends the mass update.
		 */
		self.alterProducts = function() {
			if(productOrProductGroupTabSelected === "PRODUCT_GROUP") {
				massUpdateProductGroups();
			} else if(productOrProductGroupTabSelected === "PRODUCT") {
				var entityRelationship = self.buildEntityRelationship();
				entityRelationship.actionCode = self.ACTION_CODE_REMOVING;
				entityRelationship.defaultParent = false;
				var massUpdateRequest = {
					parameters: {
						attribute: self.REMOVE_HIERARCHY_PRODUCT,
						description: self.description,
						entityRelationship: entityRelationship
					},
					productSearchCriteria: self.buildSearchCriteria()
				};
				customHierarchyApi.massUpdate(massUpdateRequest, self.massUpdateSuccess, self.fetchError);
			}
		};

		/**
		 * Constructs the table that shows the report.
		 * @returns {ngTableParams}
		 */
		self.buildTable = function()
		{
			self.tableParams = new ngTableParams(
				{
					// set defaults for ng-table
					page: 1,
					count: PAGE_SIZE
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

						// The first time through, build the table. The rest of the time, just tell it to fetch new data.
						if (!self.tableDataBuilt) {
							self.tableDataBuilt = true;
							return [];
						}
						self.isWaitingForData = true;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if (self.firstSearch) {
							self.includeCounts = true;
							params.page(1);
							self.firstSearch = false;
							antiSelectedList.splice(0, antiSelectedList.length);
						} else {
							self.includeCounts = false;
						}
						var search = {};
						search.page = (params.page() - 1);
						search.pageSize = PAGE_SIZE;
						if(customHierarchyService.getNavigatedForFirstSearch()){
							search.firstSearch = true;
						}else{
						search.firstSearch = self.includeCounts;
						}

						search.hierarchyParentId = self.currentLevel.key.childEntityId;
						search.hierarchyContext = self.currentLevel.key.hierarchyContext;

						if(productOrProductGroupTabSelected === "PRODUCT_GROUP") {
							customHierarchyApi.findAllCustomerProductGroupsByParent(search,
								function(results){
									self.loadProductLevelData(results, ++latestRequest)
								},
								self.fetchError);
						} else if(productOrProductGroupTabSelected === "PRODUCT") {
							customHierarchyApi.findProductsByParent(search,
								function(results){
									self.loadProductLevelData(results, ++latestRequest)
								},
								self.fetchError);
						}
					}
				}
			);
		};

		/**
		 * Helper function to return the key of a product or product group, depending on the tab your are on.
		 *
		 * @param data Data to look for id on.
		 * @returns {number}
		 */
		function getKeyOfProductOrProductGroup(data) {
			switch(productOrProductGroupTabSelected){
				case "PRODUCT": {
					return data.productMaster.prodId;
				}
				case "PRODUCT_GROUP":{
					return data.customerProductGroup.custProductGroupId;
				}
			}
		}

		/**
		 * Callback for loading product level data.
		 *
		 * @param results Pageable result from api.
		 * @param currentRequest Number associated with this request to ensure this is the latest request.
		 */
		self.loadProductLevelData = function(results, currentRequest){
			if(latestRequest === currentRequest) {
				self.error = null;

				if (results.data.length > 0) {
					self.hasData = true;
				} else {
					self.hasData = false;
				}
				self.isWaitingForData = false;

				// If this was the fist page, it includes record count and total pages.
				if (results.complete) {
					self.totalRecordCount = results.recordCount;
					self.totalPages = results.pageCount;
					self.dataResolvingParams.total(self.totalRecordCount);
				} else {
					for (var index = 0; index < results.data.length; index++) {
						if (antiSelectedList.indexOf(getKeyOfProductOrProductGroup(results.data[index])) > -1) {
							results.data[index].selected = !self.allSelected;
						} else {
							results.data[index].selected = self.allSelected;
						}
					}
				}
				self.resultMessage = getResultMessage(results.data.length, results.page);

				// Return the data back to the ngTable.
				self.defer.resolve(results.data);
				self.isFirstLoadProductPanel= false;
			}
		};

		/**
		 * Helper function to determine if a user has edit access for custom hierarchy products and product groups.
		 *
		 * @returns {boolean}
		 */
		self.canEditProductsAndProductGroups = function(){
			return permissionsService.getPermissions('CH_HIER_02', 'EDIT');
		};

		/**
		 * When the product/ product group tab is selected, this function sets values necessary to promote a fresh
		 * search for products and product groups, setting the tab selected variable, as well as reloading the ng-table.
		 *
		 * @param tabName Tab name to set as selected.
		 */
		self.toggleProductOrProductGroupTab = function(tabName){
			if(tabName == 'PRODUCT_GROUP'){
				self.productGroupTabPaneClass = 'tab-pane active';
				self.productTabPaneClass = 'tab-pane';
				self.activeProductGroupTabClass = 'ng-scope active';
				self.activeProductTabClass = '';
			}
			else{
				self.activeProductTabClass = 'ng-scope active';
				self.productTabPaneClass = 'tab-pane active';
				self.productGroupTabPaneClass = 'tab-pane';
				self.activeProductGroupTabClass = '';
			}
			productOrProductGroupTabSelected = tabName;
			self.firstSearch = true;
			self.allSelected = false;
			if(self.tableParams != null && self.tableParams != undefined && !self.isFirstLoadProductPanel){
			self.tableParams.reload();
			}

		};

		/**
		 * Set all 'is selected' within a list to the value set in the 'select all' checkbox depending on whether it
		 * is checked or unchecked.
		 *
		 * @param data Data to set as all checked or unchecked.
		 */
		self.selectAllInData = function(data){
			for(var index = 0; index < data.length; index++){
				data[index].isSelected = self.selectAll;
			}
		};

		/**
		 * Gets a string value to display for all parents panel title. If a product group is selected, return a user
		 * friendly title. Else return an empty string.
		 *
		 * @returns {string}
		 */
		self.getViewAllParentsTitle = function(){
			if(self.currentSelectedProductData){
				switch(productOrProductGroupTabSelected){
					case "PRODUCT": {
						return "All Parents of Product ID: " + self.currentSelectedProductData.prodId;
					}
					case "PRODUCT_GROUP":{
						return "All Parents of Product Group ID: " + self.currentSelectedProductData.custProductGroupId;
					}
				}
			} else {
				return "";
			}
		};

		/**
		 * Setter for current selected product data.
		 *
		 * @param productData Product data to set as selected.
		 */
		self.setCurrentSelectedProductData = function(productData){
			self.transactionMessage = null;
			if(productData) {
				switch(productOrProductGroupTabSelected){
					case "PRODUCT": {
						self.currentSelectedProductData = angular.copy(productData.productMaster);
						self.currentSelectedProductData.id = self.currentSelectedProductData.prodId;
						break;
					}
					case "PRODUCT_GROUP":{
						self.currentSelectedProductData = angular.copy(productData.customerProductGroup);
						self.currentSelectedProductData.id = self.currentSelectedProductData.custProductGroupId;
						break;
					}
				}
				self.parentPathError = null;
				self.pathList = null;
				self.entityList = [];
				self.primaryPath = null;
				self.changedPrimaryPath = null;
				self.multipleParents = false;
				self.isWaitingForParentPaths = true;
				customHierarchyApi.findAllParentsByChild(
					{
						hierarchyContext: customHierarchyService.getHierarchyContextSelected().id,
						hierarchyParentId: customHierarchyService.getHierarchyContextSelected().parentEntityId,
						childId : productData.entityId
					}, self.loadPath
					, self.fetchParentPathsError
				)
			}
		};
		/**
		 * Checks to see if primary path changed or resolving an error and saves the new primary path entity
		 */
		self.savePrimaryPath = function() {
			self.isWaiting = true;
			if(self.primaryPath !== self.changedPrimaryPath || self.multipleParents){
				self.entityList[self.changedPrimaryPath].defaultParent = true;
				customHierarchyApi.updateCurrentLevel(self.entityList[self.changedPrimaryPath],
					function(results){
						self.tableParams.reload();
						self.isWaiting = false;
						self.transactionMessage = "Primary Path has been successfully updated.";

					},
					function (error) {
						fetchError(error);

					});

			}
			self.isWaiting = false;
		};

		/**
		 * Loads the path list for each path.
		 * @param results Results from api.
		 */
		self.loadPath = function (results) {
			self.pathList = results;
			self.isWaitingForParentPaths = false;
		};

		/**
		 * Fetches the parent path error.
		 * @param error Error from api.
		 */
		self.fetchParentPathsError = function (error) {
			self.parentPathError = getError(error);
			self.isWaitingForParentPaths = false;
		};

		/**
		 * Callback for load customer product groups.
		 *
		 * @param results
		 */
		function loadCustomerProductGroups(results){
			self.allCustomerProductGroups = results;
			self.isWaitingForAllProductGroups = false;
		}

		/**
		 * Callback for load customer product groups error.
		 *
		 * @param error
		 */
		function loadCustomerProductGroupsError(error){
			self.customerProductGroupsError = getError(error);
			self.isWaitingForAllProductGroups = false;
		}
		/**
		 * This method will generate the string to represent an products fulfillment programs
		 * @param product
		 */
		self.getFullfilmentPrograms = function (product) {
			if(product !== null || undefined) {
				if (productOrProductGroupTabSelected === "PRODUCT" && product.productFullfilmentChanels.length > 0) {
					product.fullfilmentProgramString = product.productFullfilmentChanels[0].fulfillmentChannel.description.trim();
					for (var index = 1; index < product.productFullfilmentChanels.length; index++) {
						product.fullfilmentProgramString = product.fullfilmentProgramString + ", " + product.productFullfilmentChanels[index].fulfillmentChannel.description.trim();
					}
				}
			}
		};

		/**
		 * This method handles the selection and deselection of all products
		 */
		self.selectAll = function(){
			antiSelectedList.splice(0, antiSelectedList.length);
			for(var index = 0; index < self.tableParams.data.length; index++){
				self.tableParams.data[index].selected=self.allSelected;
			}
		};

		/**
		 * This method handles selecting an individuals products checkbox
		 * @param productLevelInfo
		 */
		self.updateSelected=function (productLevelInfo) {
			var key = getKeyOfProductOrProductGroup(productLevelInfo);
			if(productLevelInfo.selected !== self.allSelected){
				antiSelectedList.push(key);
			} else if(antiSelectedList.indexOf(key)>-1){
				antiSelectedList.splice(antiSelectedList.indexOf(key), 1);
			}
		};

		/**
		 * The method creates the new default parent for a mass update
		 */
		self.updateNewDefaultParent = function () {
			if(self.newDefaultParent === null){
				self.newDefaultParent = {
					key: {
						parentEntityId: self.currentLevel.key.parentEntityId,
						childEntityId: self.currentLevel.key.childEntityId,
						hierarchyContext: self.currentLevel.key.hierarchyContext
					},
					defaultParentSwitch: true,
					newDataSwitch: true
				};
				self.newDefaultParentDescription = self.currentLevel.childDescription.shortDescription;
			} else {
				self.newDefaultParent = null;
				self.newDefaultParentDescription = null;
			}
		};

		/**
		 * Gets all customer product groups to be able to add them to a hierarchy relationship. This call sends the
		 * current hierarchy level information, so the currently attached product groups are not included in the
		 * return.
		 */
		self.getAllCustomerProductGroups = function(){
			self.isWaitingForAllProductGroups = true;
			self.allCustomerProductGroups = [];
			self.customerProductGroupsError = null;
			self.allCustomProductGroupsChecked = false;
			customHierarchyApi.findAllCustomerProductGroupsNotOnParentEntity(
				{
					hierarchyContext: self.currentLevel.key.hierarchyContext,
					parentEntityId: self.currentLevel.key.childEntityId
				},
				loadCustomerProductGroups,
				loadCustomerProductGroupsError
			)
		};

		self.productsSelected = function () {
			if(self.allSelected){
				return true;
			} else if(antiSelectedList.length>0){
				return true;
			} else{
				return false;
			}
		};

		/**
		 * Call api to mass update remove product groups.
		 */
		self.removeProductGroups = function(){
			self.massUpdateType = self.REMOVE_HIERARCHY_PRODUCT_GROUP;
			if(self.allSelected){
				self.totalUpdates = self.totalRecordCount - antiSelectedList.length;
			} else {
				self.totalUpdates = antiSelectedList.length;
			}
			self.clearModal();
		};

		/**
		 * Call api to mass update add product groups.
		 */
		self.addCustomerProductGroups = function(){
			var addedProductGroupIds = [];
			angular.forEach(self.allCustomerProductGroups, function(customerProductGroup){
				if(customerProductGroup.isChecked){
					addedProductGroupIds.push(customerProductGroup.custProductGroupId);
				}
			});
			self.productIds = addedProductGroupIds;
			self.massUpdateType = self.ADD_HIERARCHY_PRODUCT_GROUP;
			self.totalUpdates = addedProductGroupIds.length;
		};

		/**
		 * Set all product groups selected value to 'all selected' value.
		 */
		self.setAllCustomProductGroups = function(){
			angular.forEach(self.allCustomerProductGroups, function(customerProductGroup){
				customerProductGroup.isChecked = self.allCustomProductGroupsChecked;
			});
		};

		/**
		 * Returns whether or not remove product groups is disabled. Returns false if 'all selected' is true AND
		 * anti-selected length is not equal to total record count, or anti-selected list is not empty. Returns true
		 * otherwise.
		 *
		 * @returns {boolean} True if remove products should be disabled, False otherwise.
		 */
		self.isRemoveProductGroupsDisabled = function(){
			if(self.allSelected){
				if(antiSelectedList.length !== self.totalRecordCount){
					return false;
				} else {
					return true;
				}
			}
			if(antiSelectedList.length > 0){
				return false;
			}
			return true;
		};

		self.massFillButtonClicked = function () {
			var massUpdateRequest = {
				parameters: {
					attribute: "PRIMARY_PATH",
					description: self.massUpdateDescription,
					entityRelationship: self.buildEntityRelationship()
				},
				productSearchCriteria : self.buildSearchCriteria()
			};
			customHierarchyApi.massUpdate(massUpdateRequest, self.massUpdateSuccess, self.fetchError);
		};

		/**
		 * This method builds the entity relationship for a mass update.
		 *
		 * @returns {Object}
		 */
		self.buildEntityRelationship = function(){
			return {
				key: {
					parentEntityId: self.currentLevel.key.parentEntityId,
					hierarchyContext: self.currentLevel.key.hierarchyContext,
					childEntityId: self.currentLevel.key.childEntityId
				},
				defaultParent: true
			}
		};

		/**
		 * This method builds the search criteria for a mass update.
		 *
		 * @returns {Object}
		 */
		self.buildSearchCriteria = function () {
			var productSearchCriteria = {};
			if(self.allSelected){
				productSearchCriteria.lowestCustomerHierarchyNode= {
					parentEntityId: self.currentLevel.key.parentEntityId,
					hierarchyContext: self.currentLevel.key.hierarchyContext,
					childEntityId: self.currentLevel.key.childEntityId
				};
				productSearchCriteria.excludedProducts=antiSelectedList;
			} else {
				productSearchCriteria.productIds = antiSelectedList.toString();
			}
			return productSearchCriteria;
		};

		/**
		 * Mass updates product groups by assigning the product search criteria and mass update parameters, then
		 * calling the api to mass update the selected body of product groups.
		 */
		function massUpdateProductGroups(){

			var productSearchCriteria = {
				lowestCustomerHierarchyNode: self.currentLevel.key,
				entityType: PRODUCT_GROUP_ENTITY_TYPE
			};
			var massUpdateParameters = {
				attribute: self.massUpdateType,
				stringValue: self.descriptionStringValue,
				description: self.description,
				entityRelationship: {
					key: self.currentLevel.key
				}
			};
			if(self.massUpdateType === self.ADD_HIERARCHY_PRODUCT_GROUP){
				productSearchCriteria.productGroupIds = self.productIds.toString();
				massUpdateParameters.entityRelationship.actionCode = self.ACTION_CODE_ADDING;
			}
			else if(self.massUpdateType === self.REMOVE_HIERARCHY_PRODUCT_GROUP){
				massUpdateParameters.entityRelationship.actionCode = self.ACTION_CODE_REMOVING;
				if (self.allSelected) {
					productSearchCriteria.excludedProducts = antiSelectedList
				} else {
					productSearchCriteria.productGroupIds = antiSelectedList.toString();
				}
			}
			var massUpdateRequest = {
				parameters: massUpdateParameters,
				productSearchCriteria: productSearchCriteria
			};

			self.transactionMessage = null;
			self.submittedMassUpdate = true;
			customHierarchyApi.massUpdate(massUpdateRequest, self.massUpdateSuccess, self.fetchError);
		}
		/**
		 * Backup search condition product  for ecommerce View
		 */
		self.navigateToEcommerceViewPage= function(productId) {
			//Set search condition
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(productId);
			productSearchService.setListOfProducts(self.tableParams.data);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(ECOMMERCE_VIEW_TAB);
			//Set from page navigated from
			productSearchService.setFromPage(appConstants.CUSTOM_HIERARCHY_ADMIN);
			self.test = productSearchService.getFromPage();
			productSearchService.setDisableReturnToList(false);

			productSearchService.productUpdatesTaskCriteria = {};
			productSearchService.productUpdatesTaskCriteria.searchType = productOrProductGroupTabSelected;
			productSearchService.productUpdatesTaskCriteria.hierarchyParentId = self.currentLevel.key.childEntityId;
			productSearchService.productUpdatesTaskCriteria.hierarchyContext = self.currentLevel.key.hierarchyContext;
			productSearchService.productUpdatesTaskCriteria.pageIndex = Math.floor((((self.tableParams.page()-1)*PAGE_SIZE)+(PAGE_SIZE-1))/self.NAVIGATE_PAGE_SIZE)+1;
			productSearchService.productUpdatesTaskCriteria.pageSize = self.NAVIGATE_PAGE_SIZE;

			$state.go(appConstants.HOME_STATE);
		};

		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		function getResultMessage(dataLength, currentPage){
			return "Result " + (PAGE_SIZE * currentPage + 1) +
				" - " + (PAGE_SIZE * currentPage + dataLength) + " of " + self.totalRecordCount;
		}
	}
}());
