/*
 * productSearchComponent.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */


'use strict';

(function() {
	angular.module('productMaintenanceUiApp').component('productSearchCriteria', {
		// isolated scope binding
		require: {

		},
		bindings: {
			init: '<',
			onUpdate: '&'
		},
		// Inline template which is binded to message variable in the component controller
		templateUrl: 'src/search/productSearchCriteria.html',

		// The controller that handles our component logic
		controller: ProductSearchCriteriaController
	});

	ProductSearchCriteriaController.$inject = ['ProductSearchCriteriaService', 'ProductSearchApi', 'DiscontinueParametersFactory',
										'$scope', 'HomeSharedService'];

	function ProductSearchCriteriaController(productSearchCriteriaService, productSearchApi, discontinueParametersFactory, $scope,
									 homeSharedService) {

		var self = this;

		// Different levels of searches available.
		self.UPC = productSearchCriteriaService.UPC;
		self.ITEM_CODE = productSearchCriteriaService.ITEM_CODE;
		self.PRODUCT_ID = productSearchCriteriaService.PRODUCT_ID;
		self.CASE_UPC = productSearchCriteriaService.CASE_UPC;
		self.PRODUCT_DESCRIPTION = productSearchCriteriaService.PRODUCT_DESCRIPTION;
		self.SUB_DEPARTMENT = productSearchCriteriaService.SUB_DEPARTMENT;
		self.CLASS = productSearchCriteriaService.CLASS;
		self.COMMODITY = productSearchCriteriaService.COMMODITY;
		self.SUB_COMMODITY = productSearchCriteriaService.SUB_COMMODITY;
		self.BDM = productSearchCriteriaService.BDM;
		self.BYOS = productSearchCriteriaService.BYOS;
		self.MRT = productSearchCriteriaService.MRT;
		self.VENDOR = productSearchCriteriaService.VENDOR;
		self.CUSTOM_HIERARCHY = productSearchCriteriaService.CUSTOM_HIERARCHY;
		self.CASE_UPC = productSearchCriteriaService.CASE_UPC;
		self.DEFAULT_ITEM_STATUS = "NONE";
		self.ACTIVE_ITEM_STATUS = "ACTIVE";
		self.DISCONTINUED_ITEM_STATUS = "DISCONTINUED";
		self.SEARCH_PRODUCT_EVENT = "searchProduct";

		// Different types of searches available.
		self.TAB_BASIC = productSearchCriteriaService.BASIC_SEARCH;
		self.TAB_PRODUCT_HIERARCHY = productSearchCriteriaService.HIERARCHY_SEARCH;
		self.TAB_BDM = productSearchCriteriaService.BDM_SEARCH;
		self.TAB_BYOS = productSearchCriteriaService.BYOS_SEARCH;
		self.TAB_MRT = productSearchCriteriaService.MRT_SEARCH;
		self.TAB_VENDOR = productSearchCriteriaService.VENDOR_SEARCH;
		self.TAB_CUSTOM_HIERARCHY = productSearchCriteriaService.CUSTOM_HIERARCHY_SEARCH;
		self.TAB_DESCRIPTION = productSearchCriteriaService.DESCRIPTION_SEARCH;

		/**
		 * The list of all available objects from a user's search. Will be BDMs, sub-department, etc.
		 * returned from the backend.
		 *
		 * @type {Object}
		 */
		self.valueList = null;

		/**
		 * The currently selected search type (basic, hierarchy, bdm, etc).
		 *
		 * @type {string}
		 */
		self.searchType = null;

		/**
		 * The type of data the user has selected. For basic search, this is UPC, product ID, or item code. For BDM
		 * search, it is BDM. For hierarchy search, it is the level of the hierarchy.
		 *
		 * @type {string}
		 */
		self.selectionType = null;

		/**
		 * The item status user has selected
		 * @type {string}
		 */
		self.itemStatus = null;

		/**
		 * When the user is doing a basic search, this contains the list of UPCs,product IDs, or item codes. When the
		 * user is doing a different type of search, it holds what the user has selected from the dropdown.
		 *
		 * @type {string}
		 */
		self.searchSelection = '';
		/**
		 * Keep track of latest Request for asynchronous calls.
		 * @type {number}
		 */
		self.latestRequest = 0;

		/**
		 * Wheter the user has selected to search by product description.
		 * @type {boolean}
		 */
		self.searchByProductDescription = true;

		/**
		 * Whether the user has selected to search by customer friendly description.
		 * @type {boolean}
		 */
		self.searchByCustomerFriendlyDescription = false;

		/**
		 * Wheter the user has selected to search by display name.
		 * @type {boolean}
		 */
		self.searchByDisplayName = false;

		/**
		 * Whether the user has selected to search by all extended attributes.
		 * @type {boolean}
		 */
		self.searchAllExtendedAttributes = false;

		/**
		 * Where the search panel is visible
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		/**
		 * Is item status filter visible
		 * @type {boolean}
		 */
		self.itemStatusFilterVisible = false;

		/**
		 * Initialize the controller.
		 */
		self.$onInit = function(){
			productSearchCriteriaService.init(true, false, self.newMrtSearch, self.newComplexSearch);
			this.setSearchPanelVisibility(true);
			self.itemStatus = self.DEFAULT_ITEM_STATUS;
			self.initBasicSearch();
		};

		self.$onChanges = function() {
			// re initialize component every time it gets shown
			self.initBasicSearch();
			self.clearBasicSearch();
			self.clearComplexSearch();

			// switch to default tab
			var selector = self.getHtmlId('#productSearchBasicTab');
			$(selector).tab('show');
		};

		/**
		 * Initiates a new search for an MRT
		 */
		self.newMrtSearch = function(){
			self.mrtSearch = true;
			self.startSearch();
		};

		/**
		 * Initiates a new complex search.
		 *
		 * @param searchCriteria The user's search criteria.
		 */
		self.newComplexSearch = function(searchCriteria) {
			self.searchCriteria = searchCriteria;
			self.mrtSearch = false;
			self.startSearch();

			// call parent component with changes
			self.onUpdate({searchCriteria: self.searchCriteria});
		};

		/**
		 * Called by byosSearch component when user clicks on Search button
		 */
		self.updateByosSearch = function (searchCriteria) {
			self.newComplexSearch(searchCriteria);
		}

		/**
		 * Called by both of the search functions to do the actual work.
		 */
		self.startSearch = function() {
			// Reset all the mass update stuff.
			self.selectedProudctIds = [];
			self.deselectedProductIds = [];
			self.massUpdateBooleanValue = "true";
			self.massUpdateStringValue = "";
			self.massUpdateDescription = null;
			self.massUpdateEffectdiveDate = new Date();
			self.submittedMassUpdate = false;
			self.massUpdateTransactionMessage = null;
			self.allSelected = false;
			self.massUpdateType = null;

			self.firstSearch = true;

			//productInfoService.hide();
		};

		/**
		 * generate unique id for html id's
		 */
		self.generateHtmlId = function() {
			return Math.random().toString(36).substr(2, 10);
		}

		/**
		 * get unique id for html id's
		 */
		self.htmlid = self.generateHtmlId();

		/**
		 * return unique id for this component
		 */
		self.getHtmlId = function(prefix) {
			return prefix + "_" + self.htmlid;
		}

		/**
		 * Responds to a broadcast search event.
		 */
		$scope.$on(self.SEARCH_PRODUCT_EVENT, function(withCondition,searchType,selectionType,searchSelection) {
			if(withCondition!==undefined && withCondition){
				self.searchSelection = searchSelection;
				self.searchType = searchType;
				self.selectionType = selectionType;
			}

			self.search();
		});

		/**
		 * Get whether to show item status filter.
		 * @returns {boolean}
		 */
		self.showItemStatusFilter = function() {
			return itemStatusFilterVisible;
		};

		/**
		 * Sets whether or not to show the search panel.
		 *
		 * @param searchPanelVisible whether or not to show the search panel
		 */
		self.setSearchPanelVisibility = function(searchPanelVisible) {
			self.searchPanelVisible = searchPanelVisible;
		};

		/**
		 * Sets whether or not to show the search panel.
		 *
		 * @param searchPanelVisible whether or not to show the search panel
		 */
		self.toggleSearchPanelVisibility = function() {
			self.searchPanelVisible = !self.searchPanelVisible;
		};

		/**
		 * Returns whether or not to show the search panel.
		 *
		 * @returns {boolean|*}
		 */
		self.isSearchPanelVisible = function() {
			return self.searchPanelVisible;
		};

		/**
		 * Returns the text to display in the search box when it is empty.
		 *
		 * @returns {string} The text to display in the search box when it is empty.
		 */
		self.getBasicSearchTextPlaceHolder = function(){
			return 'Enter ' + self.selectionType + 's to Search For'
		};

		/**
		 * Returns the text to display in the MRT search box when it is empty.
		 *
		 * @returns {string} The text to display in the MRT search box when it is empty.
		 */
		self.getMrtSearchTextPlaceHolder = function() {

			if (self.selectionType == self.ITEM_CODE) {
				return "Enter an Item Code to Search For";
			}
			else {
				return "Enter a Case UPC to Search For";
			}
		};

		/**
		 * Initializes the basic search.
		 */
		self.initBasicSearch = function(){
			self.resetView(self.TAB_BASIC, self.UPC);
		};

		/**
		 * Initializes the product hierarchy search.
		 */
		self.initProductHierarchySearch = function(){
			self.resetView(self.TAB_PRODUCT_HIERARCHY, self.SUB_DEPARTMENT);
		};

		/**
		 * Initializes the bdm search.
		 */
		self.initBDMSearch = function(){
			self.resetView(self.TAB_BDM, self.BDM);
		};

		/**
		 * Initializes build your own search tab.
		 */
		self.initByosSearch = function(){
			self.resetView(self.TAB_BYOS, self.BYOS);
		};

		/**
		 * Initializes the MRT search tab.
		 */
		self.initMRTSearch = function(){
			self.resetView(self.TAB_MRT, self.ITEM_CODE);
		};

		/**
		 * Initializes the vendor search tab.
		 */
		self.initVendorSearch = function(){
			self.resetView(self.TAB_VENDOR, self.VENDOR);
		};

		self.initDescriptionSearch = function() {
			self.resetView(self.TAB_DESCRIPTION, self.PRODUCT_DESCRIPTION);
		};

		/**
		 * Reset page view by hiding product view, setting the search radio button switch and the tab.
		 *
		 * @param searchType The type of search being done (basic, hierarchy, bdm, etc.).
		 * @param selectionType The type of data in the list (UPC, item code, product ID).
		 */
		self.resetView = function(searchType, selectionType){
			$scope.$broadcast('resetBYOSCriteria');
			self.searchSelection = null;
			self.searchType = searchType;
			self.selectionType = selectionType;
		};

		/**
		 * Clears the search box.
		 */
		self.clearBasicSearch = function(){
			self.searchSelection = '';
		};

		/**
		 * Adds a basic search component to the compound search list.
		 */
		self.addBasicSearch = function() {
			if (self.selectionType == self.PRODUCT_ID) {
				productSearchCriteriaService.addProductIds(self.searchSelection);
			}
			if (self.selectionType == self.UPC) {
				productSearchCriteriaService.addUpcs(self.searchSelection);
			}
			if (self.selectionType == self.ITEM_CODE) {
				productSearchCriteriaService.addItemCodes(self.searchSelection);
			}
			if (self.selectionType == self.CASE_UPC) {
				productSearchCriteriaService.addCaseUpcs(self.searchSelection);
			}
			if (self.selectionType == self.CLASS) {
				productSearchCriteriaService.addClasses(self.searchSelection);
			}
			if (self.selectionType == self.COMMODITY) {
				productSearchCriteriaService.addCommodities(self.searchSelection);
			}
			if (self.selectionType == self.SUB_COMMODITY) {
				productSearchCriteriaService.addSubCommodities(self.searchSelection);
			}
		};

		/**
		 * Adds a product hierarchy search component to the compound list.
		 */
		self.addHierarchySearch = function() {
			if (self.selectionType == this.SUB_DEPARTMENT) {
				productSearchCriteriaService.addSubDepartment(self.searchSelection);
			}
			if (self.selectionType == this.CLASS) {
				productSearchCriteriaService.addClass(self.searchSelection);
			}
			if (self.selectionType == this.COMMODITY) {
				productSearchCriteriaService.addCommodity(self.searchSelection);
			}
			if (self.selectionType == this.SUB_COMMODITY) {
				productSearchCriteriaService.addSubCommodity(self.searchSelection);
			}
		};

		/**
		 * Returns whether or not the user has added a level of the product hierarchy to the compound search.
		 *
		 * @param type The level of the hierarchy to check.
		 * @returns {boolean} True if compound search includes that level and false otherwise.
		 */
		self.hasHierarchySearch = function(type) {

			var toReturn = false;

			if (type == null || type == this.SUB_DEPARTMENT) {
				toReturn = productSearchCriteriaService.getProductSearchCriteria().subDepartment != null;
			}
			if (type == null || type == this.CLASS) {
				toReturn = toReturn || productSearchCriteriaService.getProductSearchCriteria().itemClass != null;
			}
			if (type == null || type == this.COMMODITY) {
				toReturn = toReturn || productSearchCriteriaService.getProductSearchCriteria().classCommodity != null;
			}
			if (type == null || type == this.SUB_COMMODITY) {
				toReturn = toReturn || productSearchCriteriaService.getProductSearchCriteria().subCommodity != null;
			}

			return toReturn;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for the product hierarchy.
		 *
		 * @param type The level of the product hierarchy.
		 * @returns {*} Text to display.
		 */
		self.getHierarchySearchText = function(type) {

			if (type == this.SUB_DEPARTMENT) {
				if (productSearchCriteriaService.getProductSearchCriteria().subDepartment == null) {
					return "";
				}
				return "Sub-Department: " + productSearchCriteriaService.getProductSearchCriteria().subDepartment.displayName;
			}
			if (type == this.CLASS) {
				if (productSearchCriteriaService.getProductSearchCriteria().itemClass == null) {
					return "";
				}
				return "Class: " + productSearchCriteriaService.getProductSearchCriteria().itemClass.displayName;
			}
			if (type == this.COMMODITY) {
				if (productSearchCriteriaService.getProductSearchCriteria().classCommodity == null) {
					return "";
				}
				return "Commodity: " + productSearchCriteriaService.getProductSearchCriteria().classCommodity.displayName;
			}
			if (type == this.SUB_COMMODITY) {
				if (productSearchCriteriaService.getProductSearchCriteria().subCommodity == null) {
					return "";
				}
				return "Sub-commodity: " + productSearchCriteriaService.getProductSearchCriteria().subCommodity.displayName;
			}
			return "";
		};

		/**
		 * Removes a level of the product hierarchy from the compound search.
		 *
		 * @param type The level to remove.
		 */
		self.removeHierarchySearch = function(type) {
			if (type == this.SUB_DEPARTMENT) {
				productSearchCriteriaService.addSubDepartment(null);
			}
			if (type == this.CLASS) {
				productSearchCriteriaService.addClass(null);
			}
			if (type == this.COMMODITY) {
				productSearchCriteriaService.addCommodity(null);
			}
			if (type == this.SUB_COMMODITY) {
				productSearchCriteriaService.addSubCommodity(null);
			}
		};

		/**
		 * Returns whether or not the user has defined anything in their compound search.
		 *
		 * @returns {*|boolean} True if the user has added anything and false otherwise.
		 */
		self.hasComplexSearch = function() {
			var obj = productSearchCriteriaService.getByosObjects();
			return self.hasProductIdSearch() || self.hasUpcSearch() || self.hasItemCodeSearch() ||
				self.hasCaseUpcSearch() || self.hasClassSearch() || self.hasCommoditySearch() ||
				self.hasSubCommoditySearch() || self.hasHierarchySearch(null) || self.hasBdmSearch() ||
				self.hasVendorSearch() || self.hasDescriptionSearch() || obj.length != 0;
		};

		/**
		 * Deletes everything from the compound search.
		 */
		self.clearComplexSearch = function() {
			productSearchCriteriaService.clearProductSearchCriteria();
		};

		/**
		 * Returns whether or not the user has added product IDs to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasProductIdSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().productIds != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria the product IDs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getProductIdSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().productIds == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().productIds.length < 20) {
				return "Product IDs: " + productSearchCriteriaService.getProductSearchCriteria().productIds;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().productIds.length < 50) {
				return "A list of product IDs";
			}
			return "A large list of product IDs";
		};

		/**
		 * Removes product IDs from the user's complex search.
		 */
		self.removeProductIdSearch = function() {
			productSearchCriteriaService.addProductIds(null);
		};

		/**
		 * Returns whether or not the user has added case UPCs to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasCaseUpcSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().caseUpcs != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for case UPCs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getCaseUpcSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().caseUpcs == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().caseUpcs.length < 20) {
				return "Case UPCs: " + productSearchCriteriaService.getProductSearchCriteria().caseUpcs;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().caseUpcs.length < 50) {
				return "A list of case UPCs";
			}
			return "A large list of case UPCs";
		};

		/**
		 * Removes case UPCs from the user's complex search.
		 */
		self.removeCaseUpcSearch = function() {
			productSearchCriteriaService.addCaseUpcs(null);
		};

		/**
		 * Returns whether or not the user has added classes to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasClassSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().classCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for classes
		 *
		 * @returns {*} Text to display.
		 */
		self.getClassSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().classCodes == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().classCodes.length < 20) {
				return "Classes: " + productSearchCriteriaService.getProductSearchCriteria().classCodes;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().classCodes.length < 50) {
				return "A list of case classes";
			}
			return "A large list of case classes";
		};

		/**
		 * Removes classes from the user's complex search.
		 */
		self.removeClassSearch = function() {
			productSearchCriteriaService.addClasses(null);
		};

		/**
		 * Returns whether or not the user has added UPCs to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasUpcSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().upcs != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for UPCs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getUpcSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().upcs == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().upcs.length < 20) {
				return "UPCs: " + productSearchCriteriaService.getProductSearchCriteria().upcs;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().upcs.length < 50) {
				return "A list of UPCs";
			}
			return "A large list of UPCs";
		};

		/**
		 * Removes UPCs from the user's complex search.
		 */
		self.removeUpcSearch = function() {
			productSearchCriteriaService.addUpcs(null);
		};

		/**
		 * Returns whether or not the user has added commodities to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasCommoditySearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().commodityCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for commodities.
		 *
		 * @returns {*} Text to display.
		 */
		self.getCommoditySearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().commodityCodes == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().commodityCodes.length < 20) {
				return "Commodities: " + productSearchCriteriaService.getProductSearchCriteria().commodityCodes;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().commodityCodes.length < 50) {
				return "A list of Commodities";
			}
			return "A large list of Commodities";
		};

		/**
		 * Removes Commodities from the user's complex search.
		 */
		self.removeCommoditySearch = function() {
			productSearchCriteriaService.addCommodities(null);
		};

		/**
		 * Returns whether or not the user has added sub-commodities to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasSubCommoditySearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().subCommodityCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for commodities.
		 *
		 * @returns {*} Text to display.
		 */
		self.getSubCommoditySearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().subCommodityCodes == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().subCommodityCodes.length < 20) {
				return "Sub Commodities: " + productSearchCriteriaService.getProductSearchCriteria().subCommodityCodes;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().subCommodityCodes.length < 50) {
				return "A list of Sub Commodities";
			}
			return "A large list of Sub Commodities";
		};

		/**
		 * Removes sub commodities from the user's complex search.
		 */
		self.removeSubCommoditySearch = function() {
			productSearchCriteriaService.addSubCommodities(null);
		};

		/**
		 * Returns whether or not the user has added item codes to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasItemCodeSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().itemCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for item codes.
		 *
		 * @returns {*} Text to display.
		 */
		self.getItemCodeSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().itemCodes == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().itemCodes.length < 20) {
				return "Item Codes: " + productSearchCriteriaService.getProductSearchCriteria().itemCodes;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().itemCodes.length < 50) {
				return "A list of Item Codes";
			}
			return "A large list of Item Codes";
		};


		/**
		 * Returns whether or not the user has added item codes to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasItemCodeSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().itemCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for item codes.
		 *
		 * @returns {*} Text to display.
		 */
		self.getItemCodeSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().itemCodes == null) {
				return "";
			}
			if (productSearchCriteriaService.getProductSearchCriteria().itemCodes.length < 20) {
				return "Item Codes: " + productSearchCriteriaService.getProductSearchCriteria().itemCodes;
			}
			if (productSearchCriteriaService.getProductSearchCriteria().itemCodes.length < 50) {
				return "A list of Item Codes";
			}
			return "A large list of Item Codes";
		};

		/**
		 * Removes item codes from the user's complex search.
		 */
		self.removeItemCodeSearch = function() {
			productSearchCriteriaService.addItemCodes(null);
		};

		/**
		 * Adds a description component tot he compound search list.
		 */
		self.addDescriptionSearch = function() {
			productSearchCriteriaService.addDescription(self.searchSelection, self.searchByProductDescription,
				self.searchByCustomerFriendlyDescription, self.searchByDisplayName, self.searchAllExtendedAttributes);
		};

		/**
		 * Returns wheter or not the user's complex search includes descriptions.
		 *
		 * @returns {boolean}
		 */
		self.hasDescriptionSearch = function() {
			var v = productSearchCriteriaService.getProductSearchCriteria();
			return v.description != null;
		};

		/**
		 * Clears out the description component from complex search.
		 */
		self.removeDescriptionSearch = function() {
			productSearchCriteriaService.addDescription(null, false, false, false, false);
		};

		/**
		 * Returns the text to display for a description search as part of the complex search.
		 * @returns {*}
		 */
		self.getDescriptionSearchText = function () {
			if (self.hasDescriptionSearch()) {
				var hasOther = false;
				var text = productSearchCriteriaService.getProductSearchCriteria().description + "[";
				if (productSearchCriteriaService.getProductSearchCriteria().searchByProductDescription) {
					text += "Product Description";
					hasOther = true;
				}
				if (productSearchCriteriaService.getProductSearchCriteria().searchByCustomerFriendlyDescription) {
					if (hasOther) {
						text += ", "
					}
					text += "Customer Friendly Description ";
					hasOther = true;
				}
				if (productSearchCriteriaService.getProductSearchCriteria().searchByDisplayName) {
					if (hasOther) {
						text += ", "
					}
					text += "Display Name ";
					hasOther = true;
				}
				if (productSearchCriteriaService.getProductSearchCriteria().searchAllExtendedAttributes) {
					if (hasOther) {
						text += ", "
					}
					text += "All Extended Attributes  ";
				}
				text += "]";
				return text;
			} else {
				return "";
			}
		};

		/**
		 * Adds a BDM to the user's complex search criteria.
		 */
		self.addBdmSearch = function() {
			productSearchCriteriaService.addBdm(self.searchSelection);
		};

		/**
		 * Removes a BDM from the user's complex search criteria.
		 */
		self.removeBdmSearch = function() {
			productSearchCriteriaService.addBdm(null);
		};

		/**
		 * Returns whether or not the user has added a BDM to their compound search.
		 *
		 * @returns {boolean} True if they have and false otherwise.
		 */
		self.hasBdmSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().bdm != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for BDMs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getBdmSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().bdm == null) {
				return "";
			}
			return "BDM: " + productSearchCriteriaService.getProductSearchCriteria().bdm.displayName;
		};

		/**
		 * Returns byos objects
		 * @returns {*}
		 */
		self.getByosObjects = function() {
			return productSearchCriteriaService.getByosObjects();
		};

		/**
		 * Removes byos object
		 * @param byosObject
		 */
		self.removeByos = function(byosObject) {
			productSearchCriteriaService.removeByosObject(byosObject);
		};

		/**
		 * Adds a vendor to the user's complex search criteria.
		 */
		self.addVendorSearch = function() {
			productSearchCriteriaService.addVendor(self.searchSelection);
		};

		/**
		 * Removes a vendor from the user's complex search criteria.
		 */
		self.removeVendorSearch = function() {
			productSearchCriteriaService.addVendor(null);
		};

		/**
		 * Returns whether or not the user has added a vendor to their compound search.
		 *
		 * @returns {boolean} True if they have and false otherwise.
		 */
		self.hasVendorSearch = function() {
			return productSearchCriteriaService.getProductSearchCriteria().vendor != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria by vendor.
		 *
		 * @returns {*} Text to display.
		 */
		self.getVendorSearchText = function() {
			if (productSearchCriteriaService.getProductSearchCriteria().vendor == null) {
				return "";
			}
			return "Vendor: " + productSearchCriteriaService.getProductSearchCriteria().vendor.displayName;
		};

		/**
		 * Returns wheter or not this is the simplified panel or the complex panel.
		 *
		 * @returns {*}
		 */
		self.showFullPanel = function() {
			return productSearchCriteriaService.showFullPanel();
		};

		/**
		 * Initiates a search for products. This will delegate the search to a function configured
		 * in the productSearchCriteriaService.
		 */
		self.search = function() {

			productSearchCriteriaService.setSearchType(self.searchType);
			productSearchCriteriaService.setSelectionType(self.selectionType);
			productSearchCriteriaService.setItemStatus(self.itemStatus);
			productSearchCriteriaService.setSearchSelection(self.searchSelection);
			productSearchCriteriaService.setSearchByProductDescription(self.searchByProductDescription);
			productSearchCriteriaService.setSearchByCustomerFriendlyDescription(self.searchByCustomerFriendlyDescription);
			productSearchCriteriaService.setSearchByDisplayName(self.searchByDisplayName);
			productSearchCriteriaService.setSearchAllExtendedAttributes(self.searchAllExtendedAttributes);

			if (productSearchCriteriaService.showFullPanel()) {
				var searchObject = productSearchCriteriaService.getBasicSearchObject();
				// This way it'll continue to work while we're implementing the new search functions.
				if (searchObject != null) {
					productSearchCriteriaService.getComplexSearchCallback()(searchObject);
				} else {
					productSearchCriteriaService.getSearchCallback()();
				}
			} else {
				// Pass the user selections to the search service.
				// Delegate searching.
				productSearchCriteriaService.getSearchCallback()();
			}
			self.broadcastNewSearch();
		};

		/**
		 * Called when the user chooses the search on the bottom of the search panel to kick off a complex search.
		 */
		self.doComplexSearch = function() {
			productSearchCriteriaService.getComplexSearchCallback()(productSearchCriteriaService.getProductSearchCriteria());
			self.broadcastNewSearch();
		};

		/**
		 * Gets current results for dropdown by calling api.
		 * @param query
		 */
		self.getCurrentDropDownResults = function(query){
			var thisRequest = ++self.latestRequest;
			if (query === null || !query.length || query.length === 0) {
				self.valueList = [];
			} else {

				switch (self.selectionType) {
					case self.CLASS:
					{
						productSearchApi.
							getClassesByRegularExpression({
								searchString: query,
								page: 0,
								pageSize: 20
							},
							//success
							function (results) {
								if(thisRequest === self.latestRequest) {
									self.valueList = results.data;
								}
							},
							//error
							function(results) {
								if (thisRequest === self.latestRequest) {
									self.valueList = [];
								}
							}
						);
						break;
					}
					case self.COMMODITY:
					{
						productSearchApi.
							getCommoditiesByRegularExpression({
								searchString: query,
								page: 0,
								pageSize: 20
							},
							//success
							function (results) {
								if(thisRequest === self.latestRequest) {
									self.valueList = results.data;
								}
							},
							//error
							function(results) {
								if (thisRequest === self.latestRequest) {
									self.valueList = [];
								}
							}
						);
						break;
					}
					case self.SUB_DEPARTMENT:
					{
						productSearchApi.
							getSubDepartmentsByRegularExpression({
								searchString: query,
								page: 0,
								pageSize: 20
							},
							//success
							function (results) {
								if(thisRequest === self.latestRequest) {
									self.valueList = results.data;
								}
							},
							//error
							function(results) {
								if (thisRequest === self.latestRequest) {
									self.valueList = [];
								}
							}
						);
						break;
					}
					case self.SUB_COMMODITY:
					{
						productSearchApi.
							getSubCommoditiesByRegularExpression({
								searchString: query,
								page: 0,
								pageSize: 20
							},
							//success
							function (results) {
								if(thisRequest === self.latestRequest) {
									self.valueList = results.data;
								}
							},
							//error
							function(results) {
								if (thisRequest === self.latestRequest) {
									self.valueList = [];
								}
							}
						);
						break;
					}
					case self.BDM:
					{
						productSearchApi.
							getBDMByRegularExpression({
								searchString: query,
								page: 0,
								pageSize: 20
							},
							//success
							function (results) {
								if(thisRequest === self.latestRequest) {
									self.valueList = results.data;
								}
							},
							//error
							function(results) {
								if (thisRequest === self.latestRequest) {
									self.valueList = [];
								}
							}
						);
						break;
					}
					case self.VENDOR:
					{
						discontinueParametersFactory.
							getVendorsByRegularExpression({
								searchString: query,
								page: 0,
								pageSize: 20
							},
							//success
							function (results) {
								if(thisRequest === self.latestRequest) {
									self.valueList = results.data;
								}
							},
							//error
							function(results) {
								if (thisRequest === self.latestRequest) {
									self.valueList = [];
								}
							}
						);
					}
				}
			}
		};

		/**
		 * Used to broadcast a new search event. Other controllers like taskSummaryController which are subscribed to
		 * this event can take necessary accordingly.
		 */
		self.broadcastNewSearch = function() {
			homeSharedService.prepForNewSearchBroadcast();
		}

		/**
		 * Returns whether or not to show the options to filter on item status.
		 *
		 * @returns {boolean|*}
		 */
		self.showItemStatusFilter = function() {
			return self.itemStatusFilterVisible;
		}

		/**
		 * Receives new search event broadcast from search component.
		 */
		$scope.$on('handleNewSearch', function() {
			//self.searchPanelVisible = false;
		});
		/**
		 * Listen the reset product search event.
		 */
		$scope.$on('resetProductSearch', function() {
			self.$onChanges();
			self.searchByProductDescription = true;
			self.searchByCustomerFriendlyDescription = false;
			self.searchByDisplayName = false;
			self.searchAllExtendedAttributes = false;
			self.searchPanelVisible = true;
		});
	}
})();
