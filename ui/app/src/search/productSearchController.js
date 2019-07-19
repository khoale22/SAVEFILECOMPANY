/*
 * productSearchController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */


'use strict';

(function() {
	angular.module('productMaintenanceUiApp').controller('ProductSearchController', productSearchController);

	productSearchController.$inject = ['ProductSearchService', 'ProductSearchApi', 'DiscontinueParametersFactory',
		'$scope', 'HomeSharedService', '$rootScope', 'appConstants'];

	function productSearchController(productSearchService, productSearchApi, discontinueParametersFactory, $scope,
									 homeSharedService, $rootScope, appConstants) {

		var self = this;

		// Different levels of searches available.
		self.UPC = productSearchService.UPC;
		self.ITEM_CODE = productSearchService.ITEM_CODE;
		self.PRODUCT_ID = productSearchService.PRODUCT_ID;
		self.CASE_UPC = productSearchService.CASE_UPC;
		self.PRODUCT_DESCRIPTION = productSearchService.PRODUCT_DESCRIPTION;
		self.SUB_DEPARTMENT = productSearchService.SUB_DEPARTMENT;
		self.CLASS = productSearchService.CLASS;
		self.COMMODITY = productSearchService.COMMODITY;
		self.SUB_COMMODITY = productSearchService.SUB_COMMODITY;
		self.BDM = productSearchService.BDM;
		self.BYOS = productSearchService.BYOS;
		self.MRT = productSearchService.MRT;
		self.VENDOR = productSearchService.VENDOR;
		self.CUSTOM_HIERARCHY = productSearchService.CUSTOM_HIERARCHY;
		self.CASE_UPC = productSearchService.CASE_UPC;
		self.DEFAULT_ITEM_STATUS = "NONE";
		self.ACTIVE_ITEM_STATUS = "ACTIVE";
		self.DISCONTINUED_ITEM_STATUS = "DISCONTINUED";
		self.SEARCH_PRODUCT_EVENT = "searchProduct";

		// Different types of searches available.
		self.TAB_BASIC = productSearchService.BASIC_SEARCH;
		self.TAB_PRODUCT_HIERARCHY = productSearchService.HIERARCHY_SEARCH;
		self.TAB_BDM = productSearchService.BDM_SEARCH;
		self.TAB_BYOS = productSearchService.BYOS_SEARCH;
		self.TAB_MRT = productSearchService.MRT_SEARCH;
		self.TAB_VENDOR = productSearchService.VENDOR_SEARCH;
		self.TAB_CUSTOM_HIERARCHY = productSearchService.CUSTOM_HIERARCHY_SEARCH;
		self.TAB_DESCRIPTION = productSearchService.DESCRIPTION_SEARCH;

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
		 * Ecommerce view tab
		 * @type {String}
		 */
		self.ECOMMERCE_VIEW_TAB = 'ecommerceViewTab';
		var currentRequest = 0;
        /**
         * Related product tab
         * @type {String}
         */
        const RELATED_PRODUCT_TAB = "relatedProductTab";
        /**
         * Variant sub tab
         * @type {String}
         */
        const VARIANTS_SUB_TAB = "variantsSubTab";
        /**
         * Variant item sub tab
         * @type {String}
         */
        const VARIANTS_ITEM_SUB_TAB = "variantsItemSubTab";

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			productSearchService.setSearchPanelVisibility(true);
			self.itemStatus = self.DEFAULT_ITEM_STATUS;
			self.initBasicSearch();
		};

		/**
		 * Responds to a broadcast searc event.
		 */
		$scope.$on(self.SEARCH_PRODUCT_EVENT, function() {
			self.search();
		});
		/**
		 * watcher scope event for  automatic search
		 */
		$scope.$on('startAutomaticSearch', function(event) {
			//Set search conditions
			self.searchSelection = productSearchService.getSearchSelection();
			self.searchType = productSearchService.getSearchType();
			self.selectionType = productSearchService.getSelectionType();
			//Begin search automatically(not need click on the search button)
			self.startAutomaticSearch();
		});

		/**
		 * watcher scope event for  automatic search when back home page
		 */
		$scope.$on('searchAutomaticBackHomePage', function(event) {
			self.searchAutomaticBackHomePage();
		});
		/**
		 * Initiates a automatic search for products. This will delegate the search to a function configured
		 * in the productSearchService.
		 */
		self.startAutomaticSearch = function() {
			if (productSearchService.showFullPanel()) {
				var searchObject = productSearchService.getBasicSearchObject();
				// This way it'll continue to work while we're implementing the new search functions.
				if (searchObject != null) {
					productSearchService.getComplexSearchCallback()(searchObject);
				} else {
					productSearchService.getSearchCallback()();
				}
			} else {
				// Pass the user selections to the search service.
				// Delegate searching.
				productSearchService.getSearchCallback()();
			}
			self.broadcastNewSearch();
			productSearchService.setSearchType(null);
			//productSearchService.setSelectionType(null);
			productSearchService.setSearchSelection(null);
		};

		/**
		 * Get whether to show item status filter.
		 * @returns {boolean}
		 */
		self.showItemStatusFilter = function() {
			return productSearchService.showItemStatusFilter();
		};

		/**
		 * Sets whether the search panel is visible.
		 */
		self.isSearchPanelVisible = function() {
			return productSearchService.isSearchPanelVisible();
		};

		/**
		 * Sets whether the search panel should be visible.
		 */
		self.setSearchPanelVisibility = function() {
			productSearchService.setSearchPanelVisibility(!productSearchService.isSearchPanelVisible());
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
		 * Get id selection search tab.
         */
		self.getIdSelectionTab = function (event) {
            if(event!=null){
            	var target = event.target || event.srcElement || event.currentTarget;
            	productSearchService.setIdTab(target.id);
            }
        };
		/**
		 * Initializes the basic search.
		 */
		self.initBasicSearch = function(event){
			self.resetView(self.TAB_BASIC, self.UPC);
            self.getIdSelectionTab(event);
		};

		/**
		 * Initializes the product hierarchy search.
		 */
		self.initProductHierarchySearch = function(event){
			self.resetView(self.TAB_PRODUCT_HIERARCHY, self.SUB_DEPARTMENT);
			$rootScope.$broadcast('initProductHierarchySearchSelection');
			self.getIdSelectionTab(event);
		};

		/**
		 * Initializes the customer hierarchy search
		 */
		self.initCustomerHierarchySearch = function(event) {
			self.resetView(self.TAB_CUSTOM_HIERARCHY, self.CUSTOM_HIERARCHY);
			productSearchService.setCustomerHierarchySearching(true);
			self.initCustomerHierarchy();
            self.getIdSelectionTab(event);
		};
		/**
		 * call the search component to search product automatically
		 */
		self.initCustomerHierarchy = function(event){
			$rootScope.$broadcast('initCustomerHierarchy');
            self.getIdSelectionTab(event);
		};

		/**
		 * Initializes the bdm search.
		 */
		self.initBDMSearch = function(event){
			self.resetView(self.TAB_BDM, self.BDM);
            self.getIdSelectionTab(event);
		};

		/**
		 * Initializes build your own search tab.
		 */
		self.initByosSearch = function(event){
			self.resetView(self.TAB_BYOS, self.BYOS);
            self.getIdSelectionTab(event);
		};

		/**
		 * Initializes the MRT search tab.
		 */
		self.initMRTSearch = function(event){
			self.resetView(self.TAB_MRT, self.ITEM_CODE);
            self.getIdSelectionTab(event);
		};

		/**
		 * Initializes the vendor search tab.
		 */
		self.initVendorSearch = function(event){
			self.resetView(self.TAB_VENDOR, self.VENDOR);
            self.getIdSelectionTab(event);
		};

		self.initDescriptionSearch = function(event) {
			if(productSearchService.getSearchTypeBackup()!=null && productSearchService.getSearchTypeBackup() != 'descriptionSearch'){
				self.searchByProductDescription = true;
				self.searchAllExtendedAttributes = false;
				self.searchByDisplayName = false;
				self.searchByCustomerFriendlyDescription = false;
			}
			self.resetView(self.TAB_DESCRIPTION, self.PRODUCT_DESCRIPTION);
            self.getIdSelectionTab(event);
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
				productSearchService.addProductIds(self.searchSelection);
			}
			if (self.selectionType == self.UPC) {
				productSearchService.addUpcs(self.searchSelection);
			}
			if (self.selectionType == self.ITEM_CODE) {
				productSearchService.addItemCodes(self.searchSelection);
			}
			if (self.selectionType == self.CASE_UPC) {
				productSearchService.addCaseUpcs(self.searchSelection);
			}
			if (self.selectionType == self.CLASS) {
				productSearchService.addClasses(self.searchSelection);
			}
			if (self.selectionType == self.COMMODITY) {
				productSearchService.addCommodities(self.searchSelection);
			}
			if (self.selectionType == self.SUB_COMMODITY) {
				productSearchService.addSubCommodities(self.searchSelection);
			}
		};

		/**
		 * Adds a product hierarchy search component to the compound list.
		 */
		self.addHierarchySearch = function() {
			if (self.selectionType == this.SUB_DEPARTMENT) {
				productSearchService.addSubDepartment(self.searchSelection);
			}
			if (self.selectionType == this.CLASS) {
				productSearchService.addClass(self.searchSelection);
			}
			if (self.selectionType == this.COMMODITY) {
				productSearchService.addCommodity(self.searchSelection);
			}
			if (self.selectionType == this.SUB_COMMODITY) {
				productSearchService.addSubCommodity(self.searchSelection);
			}
		};

		/**
		 * Adds a customer hierarchy search component to the compund list.
		 */
		self.addCustomerHierarchySearch = function() {
			productSearchService.addlowestCustomerHierarchyNode(productSearchService.getLowestNode());
			productSearchService.setLowestCustomerHierarchyNodeName(productSearchService.getLowestNode());
		};

		/**
		 * Checks to see if we are at lowest level
		 * @returns {boolean}
		 */
		self.isLowestLevel = function() {
			var result = false;
			var lowestNode = productSearchService.getLowestNode();
			if (lowestNode !== null && lowestNode.lowestLevel === true) {
				result = true;
			}
			return result;
		};

		/**
		 * Checks whether or not it has a lowest customer hierarchy node.
		 *
		 * @returns {boolean}
		 */
		self.hasCustomerHierarchySearch = function() {
			return productSearchService.getProductSearchCriteria().lowestCustomerHierarchyNode != null;
		};

		/**
		 * This gets the customer hierarchy lowest node text.
		 * @returns {string}
		 */
		self.getCustomerHierachySearchText = function () {
			if(productSearchService.getProductSearchCriteria().lowestCustomerHierarchyNode == null) {
				return "";
			}
			return "Customer Hierarchy: " + productSearchService.getLowestCustomerHierarchyNodeName();
		};

		/**
		 * Removes the customer hierarchy search.
		 */
		self.removeCustomerHierarchySearch = function() {
			productSearchService.addlowestCustomerHierarchyNode(null);
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
				toReturn = productSearchService.getProductSearchCriteria().subDepartment != null;
			}
			if (type == null || type == this.CLASS) {
				toReturn = toReturn || productSearchService.getProductSearchCriteria().itemClass != null;
			}
			if (type == null || type == this.COMMODITY) {
				toReturn = toReturn || productSearchService.getProductSearchCriteria().classCommodity != null;
			}
			if (type == null || type == this.SUB_COMMODITY) {
				toReturn = toReturn || productSearchService.getProductSearchCriteria().subCommodity != null;
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
				if (productSearchService.getProductSearchCriteria().subDepartment == null) {
					return "";
				}
				return "Sub Department: " + productSearchService.getProductSearchCriteria().subDepartment.displayName;
			}
			if (type == this.CLASS) {
				if (productSearchService.getProductSearchCriteria().itemClass == null) {
					return "";
				}
				return "Class: " + productSearchService.getProductSearchCriteria().itemClass.displayName;
			}
			if (type == this.COMMODITY) {
				if (productSearchService.getProductSearchCriteria().classCommodity == null) {
					return "";
				}
				return "Commodity: " + productSearchService.getProductSearchCriteria().classCommodity.displayName;
			}
			if (type == this.SUB_COMMODITY) {
				if (productSearchService.getProductSearchCriteria().subCommodity == null) {
					return "";
				}
				return "Sub-commodity: " + productSearchService.getProductSearchCriteria().subCommodity.displayName;
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
				productSearchService.addSubDepartment(null);
			}
			if (type == this.CLASS) {
				productSearchService.addClass(null);
			}
			if (type == this.COMMODITY) {
				productSearchService.addCommodity(null);
			}
			if (type == this.SUB_COMMODITY) {
				productSearchService.addSubCommodity(null);
			}
		};

		/**
		 * Returns whether or not the user has defined anything in their compound search.
		 *
		 * @returns {*|boolean} True if the user has added anything and false otherwise.
		 */
		self.hasComplexSearch = function() {
			var obj =
				productSearchService.getByosObjects();
			return self.hasProductIdSearch() || self.hasUpcSearch() || self.hasItemCodeSearch() ||
				self.hasCaseUpcSearch() || self.hasClassSearch() || self.hasCommoditySearch() ||
				self.hasSubCommoditySearch() || self.hasHierarchySearch(null) || self.hasBdmSearch() ||
				self.hasVendorSearch() || self.hasDescriptionSearch() || self.hasCustomerHierarchySearch() || obj.length != 0;

		};

		/**
		 * Deletes everything from the compound search.
		 */
		self.clearComplexSearch = function() {
			productSearchService.clearProductSearchCriteria();
		};

		self.removeByos = function(byosObject) {
			productSearchService.removeByosObject(byosObject);
		};

		/**
		 * Returns whether or not the user has added product IDs to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasProductIdSearch = function() {
			return productSearchService.getProductSearchCriteria().productIds != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria the product IDs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getProductIdSearchText = function() {
			if (productSearchService.getProductSearchCriteria().productIds == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().productIds.length < 20) {
				return "Product IDs: " + productSearchService.getProductSearchCriteria().productIds;
			}
			if (productSearchService.getProductSearchCriteria().productIds.length < 50) {
				return "A list of product IDs";
			}
			return "A large list of product IDs";
		};

		/**
		 * Removes product IDs from the user's complex search.
		 */
		self.removeProductIdSearch = function() {
			productSearchService.addProductIds(null);
		};

		/**
		 * Returns whether or not the user has added case UPCs to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasCaseUpcSearch = function() {
			return productSearchService.getProductSearchCriteria().caseUpcs != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for case UPCs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getCaseUpcSearchText = function() {
			if (productSearchService.getProductSearchCriteria().caseUpcs == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().caseUpcs.length < 20) {
				return "Case UPCs: " + productSearchService.getProductSearchCriteria().caseUpcs;
			}
			if (productSearchService.getProductSearchCriteria().caseUpcs.length < 50) {
				return "A list of case UPCs";
			}
			return "A large list of case UPCs";
		};

		/**
		 * Removes case UPCs from the user's complex search.
		 */
		self.removeCaseUpcSearch = function() {
			productSearchService.addCaseUpcs(null);
		};

		/**
		 * Returns whether or not the user has added classes to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasClassSearch = function() {
			return productSearchService.getProductSearchCriteria().classCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for classes
		 *
		 * @returns {*} Text to display.
		 */
		self.getClassSearchText = function() {
			if (productSearchService.getProductSearchCriteria().classCodes == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().classCodes.length < 20) {
				return "Classes: " + productSearchService.getProductSearchCriteria().classCodes;
			}
			if (productSearchService.getProductSearchCriteria().classCodes.length < 50) {
				return "A list of case classes";
			}
			return "A large list of case classes";
		};

		/**
		 * Removes classes from the user's complex search.
		 */
		self.removeClassSearch = function() {
			productSearchService.addClasses(null);
		};

		/**
		 * Returns whether or not the user has added UPCs to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasUpcSearch = function() {
			return productSearchService.getProductSearchCriteria().upcs != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for UPCs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getUpcSearchText = function() {
			if (productSearchService.getProductSearchCriteria().upcs == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().upcs.length < 20) {
				return "UPCs: " + productSearchService.getProductSearchCriteria().upcs;
			}
			if (productSearchService.getProductSearchCriteria().upcs.length < 50) {
				return "A list of UPCs";
			}
			return "A large list of UPCs";
		};

		/**
		 * Removes UPCs from the user's complex search.
		 */
		self.removeUpcSearch = function() {
			productSearchService.addUpcs(null);
		};

		/**
		 * Returns whether or not the user has added commodities to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasCommoditySearch = function() {
			return productSearchService.getProductSearchCriteria().commodityCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for commodities.
		 *
		 * @returns {*} Text to display.
		 */
		self.getCommoditySearchText = function() {
			if (productSearchService.getProductSearchCriteria().commodityCodes == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().commodityCodes.length < 20) {
				return "Commodities: " + productSearchService.getProductSearchCriteria().commodityCodes;
			}
			if (productSearchService.getProductSearchCriteria().commodityCodes.length < 50) {
				return "A list of Commodities";
			}
			return "A large list of Commodities";
		};

		/**
		 * Removes Commodities from the user's complex search.
		 */
		self.removeCommoditySearch = function() {
			productSearchService.addCommodities(null);
		};

		/**
		 * Returns whether or not the user has added sub-commodities to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasSubCommoditySearch = function() {
			return productSearchService.getProductSearchCriteria().subCommodityCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for commodities.
		 *
		 * @returns {*} Text to display.
		 */
		self.getSubCommoditySearchText = function() {
			if (productSearchService.getProductSearchCriteria().subCommodityCodes == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().subCommodityCodes.length < 20) {
				return "Sub Commodities: " + productSearchService.getProductSearchCriteria().subCommodityCodes;
			}
			if (productSearchService.getProductSearchCriteria().subCommodityCodes.length < 50) {
				return "A list of Sub Commodities";
			}
			return "A large list of Sub Commodities";
		};

		/**
		 * Removes sub commodities from the user's complex search.
		 */
		self.removeSubCommoditySearch = function() {
			productSearchService.addSubCommodities(null);
		};

		/**
		 * Returns whether or not the user has added item codes to their compound search.
		 *
		 * @returns {boolean} True if they have an false otherwise.
		 */
		self.hasItemCodeSearch = function() {
			return productSearchService.getProductSearchCriteria().itemCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for item codes.
		 *
		 * @returns {*} Text to display.
		 */
		self.getItemCodeSearchText = function() {
			if (productSearchService.getProductSearchCriteria().itemCodes == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().itemCodes.length < 20) {
				return "Item Codes: " + productSearchService.getProductSearchCriteria().itemCodes;
			}
			if (productSearchService.getProductSearchCriteria().itemCodes.length < 50) {
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
			return productSearchService.getProductSearchCriteria().itemCodes != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for item codes.
		 *
		 * @returns {*} Text to display.
		 */
		self.getItemCodeSearchText = function() {
			if (productSearchService.getProductSearchCriteria().itemCodes == null) {
				return "";
			}
			if (productSearchService.getProductSearchCriteria().itemCodes.length < 20) {
				return "Item Codes: " + productSearchService.getProductSearchCriteria().itemCodes;
			}
			if (productSearchService.getProductSearchCriteria().itemCodes.length < 50) {
				return "A list of Item Codes";
			}
			return "A large list of Item Codes";
		};

		/**
		 * Removes item codes from the user's complex search.
		 */
		self.removeItemCodeSearch = function() {
			productSearchService.addItemCodes(null);
		};

		/**
		 * Adds a description component tot he compound search list.
		 */
		self.addDescriptionSearch = function() {
			productSearchService.addDescription(self.searchSelection, self.searchByProductDescription,
				self.searchByCustomerFriendlyDescription, self.searchByDisplayName, self.searchAllExtendedAttributes);
		};

		/**
		 * Returns wheter or not the user's complex search includes descriptions.
		 *
		 * @returns {boolean}
		 */
		self.hasDescriptionSearch = function() {
			var v = productSearchService.getProductSearchCriteria();
			return v.description != null;
		};

		/**
		 * Clears out the description component from complex search.
		 */
		self.removeDescriptionSearch = function() {
			productSearchService.addDescription(null, false, false, false, false);
		};

		/**
		 * Returns the text to display for a description search as part of the complex search.
		 * @returns {*}
		 */
		self.getDescriptionSearchText = function () {
			if (self.hasDescriptionSearch()) {
				var hasOther = false;
				var text = productSearchService.getProductSearchCriteria().description + "[";
				if (productSearchService.getProductSearchCriteria().searchByProductDescription) {
					text += "Product Description";
					hasOther = true;
				}
				if (productSearchService.getProductSearchCriteria().searchByCustomerFriendlyDescription) {
					if (hasOther) {
						text += ", "
					}
					text += "Customer Friendly Description ";
					hasOther = true;
				}
				if (productSearchService.getProductSearchCriteria().searchByDisplayName) {
					if (hasOther) {
						text += ", "
					}
					text += "Display Name ";
					hasOther = true;
				}
				if (productSearchService.getProductSearchCriteria().searchAllExtendedAttributes) {
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
			productSearchService.addBdm(self.searchSelection);
		};

		/**
		 * Removes a BDM from the user's complex search criteria.
		 */
		self.removeBdmSearch = function() {
			productSearchService.addBdm(null);
		};

		/**
		 * Returns whether or not the user has added a BDM to their compound search.
		 *
		 * @returns {boolean} True if they have and false otherwise.
		 */
		self.hasBdmSearch = function() {
			return productSearchService.getProductSearchCriteria().bdm != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria for BDMs.
		 *
		 * @returns {*} Text to display.
		 */
		self.getBdmSearchText = function() {
			if (productSearchService.getProductSearchCriteria().bdm == null) {
				return "";
			}
			return "BDM: " + productSearchService.getProductSearchCriteria().bdm.displayName;
		};

		/**
		 * Adds a vendor to the user's complex search criteria.
		 */
		self.addVendorSearch = function() {
			productSearchService.addVendor(self.searchSelection);
		};

		/**
		 * Removes a vendor from the user's complex search criteria.
		 */
		self.removeVendorSearch = function() {
			productSearchService.addVendor(null);
		};

		/**
		 * Returns whether or not the user has added a vendor to their compound search.
		 *
		 * @returns {boolean} True if they have and false otherwise.
		 */
		self.hasVendorSearch = function() {
			return productSearchService.getProductSearchCriteria().vendor != null;
		};

		/**
		 * Returns a reasonable text to show the user for their search criteria by vendor.
		 *
		 * @returns {*} Text to display.
		 */
		self.getVendorSearchText = function() {
			if (productSearchService.getProductSearchCriteria().vendor == null) {
				return "";
			}
			return "Vendor: " + productSearchService.getProductSearchCriteria().vendor.displayName;
		};

		self.getByosObjects = function() {
			return productSearchService.getByosObjects();
		};

		/**
		 * Returns wheter or not this is the simplified panel or the complex panel.
		 *
		 * @returns {*}
		 */
		self.showFullPanel = function() {
			return productSearchService.showFullPanel();
		};

		/**
		 * Initiates a search for products. This will delegate the search to a function configured
		 * in the productSearchService.
		 */
		self.search = function() {
            productSearchService.setIsBasicSearch(true);
			productSearchService.setFromPage(appConstants.HOME);
			productSearchService.setDisableReturnToList(true);

			productSearchService.setSearchType(self.searchType);
			productSearchService.setSelectionType(self.selectionType);
			productSearchService.setItemStatus(self.itemStatus);
			productSearchService.setSearchSelection(self.searchSelection);
			productSearchService.setSearchByProductDescription(self.searchByProductDescription);
			productSearchService.setSearchByCustomerFriendlyDescription(self.searchByCustomerFriendlyDescription);
			productSearchService.setSearchByDisplayName(self.searchByDisplayName);
			productSearchService.setSearchAllExtendedAttributes(self.searchAllExtendedAttributes);
			self.backupDataSearch();

			if (productSearchService.showFullPanel()) {
				var searchObject = productSearchService.getBasicSearchObject();
				// This way it'll continue to work while we're implementing the new search functions.
				if (searchObject != null) {
					productSearchService.getComplexSearchCallback()(searchObject);
				} else {
					productSearchService.getSearchCallback()();
				}
			} else {
				// Pass the user selections to the search service.
				// Delegate searching.
				productSearchService.getSearchCallback()();
			}
			self.broadcastNewSearch();
			//self.clearAfterSearch();
		};

		/**
		 * Backup data search
		 */
		self.backupDataSearch = function() {
			productSearchService.setSearchTypeBackup(productSearchService.getSearchType());
			productSearchService.setSelectionTypeBackup(productSearchService.getSelectionType());
			productSearchService.setSearchSelectionBackup(productSearchService.getSearchSelection());
		};

		/**
		 * Initiates a search automaticCustom hierarchy for products. This will delegate the search to a function configured
		 * in the productSearchService.
		 */
		self.searchAutomaticBackHomePage = function() {
			//In the case not return to list to online attribute->related product or variant tabs
            if(!(productSearchService.getSelectionType() === productSearchService.PRODUCT_ID
                && productSearchService.getToggledTab() !== null
                && (productSearchService.getToggledTab() === RELATED_PRODUCT_TAB
					|| productSearchService.getToggledTab() === VARIANTS_SUB_TAB || productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB)
                && productSearchService.getReturnToListButtonClicked())) {
                self.searchSelection = productSearchService.getSearchSelectionBackup();
                self.searchType = productSearchService.getSearchTypeBackup();
                self.selectionType = productSearchService.getSelectionTypeBackup();
                productSearchService.setSearchType(self.searchType);
                productSearchService.setSelectionType(self.selectionType);
                productSearchService.setSearchSelection(self.searchSelection);
            }else{
                self.searchSelection = productSearchService.getSearchSelection();
                self.searchType = productSearchService.getSearchType();
                self.selectionType = productSearchService.getSelectionType();
            }
            productSearchService.setItemStatus(self.itemStatus);
            productSearchService.setSearchByProductDescription(self.searchByProductDescription);
            productSearchService.setSearchByCustomerFriendlyDescription(self.searchByCustomerFriendlyDescription);
            productSearchService.setSearchByDisplayName(self.searchByDisplayName);
            productSearchService.setSearchAllExtendedAttributes(self.searchAllExtendedAttributes);
            if (productSearchService.showFullPanel()) {
            	var searchObject = null;
                if (productSearchService.getIsBasicSearch()){
                    searchObject = productSearchService.getBasicSearchObject();
                } else {
                    searchObject = productSearchService.getProductSearchCriteriaCallBack();
                }
                if (searchObject != null) {
                    productSearchService.getComplexSearchCallback()(searchObject);
                }
            } else {
                // Pass the user selections to the search service.
                // Delegate searching.
                productSearchService.getSearchCallback()();
            }
            self.broadcastNewSearch();
            productSearchService.setDisableReturnToList(true);
        };
		/**
		 * Clear all condition search after call search function
		 */
		self.clearAfterSearch = function() {
			// if(productSearchService.getSearchType() !== productSearchService.BASIC_SEARCH){
			// 	productSearchService.setSelectionType(null);
			// 	productSearchService.setSelectedTab(null);
			// productSearchService.setSearchType(null);
			// }
			//productSearchService.setSearchSelection(null);
			//productGroupService.setFromPage(null);
			productSearchService.setDisableReturnToList(true);

		}

		/**
		 * Called when the user chooses the search on the bottom of the search panel to kick off a complex search.
		 */
		self.doComplexSearch = function() {
			//Reset search criteria
			productSearchService.setIsBasicSearch(false);
			productSearchService.setFromPage(appConstants.HOME);
			productSearchService.setDisableReturnToList(true);
			productSearchService.setBackupProductSearchCriteria();
			productSearchService.setSelectionType(null);

			var searchCriteria = productSearchService.getProductSearchCriteria();
			if(searchCriteria != null){
                productSearchService.getComplexSearchCallback()(searchCriteria);
			}
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
		 * Receives new load ecommerce task summary event. On the event, this method hides the search result to make
		 * way for displaying ecommerce task summary section.
		 */
		$scope.$on('loadEcommerceTaskSummary', function() {
			productSearchService.setSearchPanelVisibility(false);
		});

		/**
		 * Add the selected item to the search box
		 * @param selectedItem
		 */
		self.addSelectedHierarchyItemToSearch = function(selectedItem) {
			if (selectedItem.subDepartment !== null) {
				productSearchService.addSubDepartment(selectedItem.subDepartment);
			} else if (selectedItem.class !== null) {
				productSearchService.addClass(selectedItem.class);
			} else if (selectedItem.commodity !== null) {
				productSearchService.addCommodity(selectedItem.commodity);
			} else if (selectedItem.subCommodity !== null) {
				productSearchService.addSubCommodity(selectedItem.subCommodity);
			}
		};
		/**
		 * Adds a custom hierarchy to the search criteria.
		 *
		 * @param selectedLevel
		 * @param requestNumber
		 */
		self.addSelectedCustomHierarchyToSearch = function(selectedLevel, requestNumber){
			if (typeof selectedLevel['id'] === 'undefined'){
				if(requestNumber > currentRequest) {
					currentRequest = requestNumber;
					if(isLowestBranchOrHasNoRelationships(selectedLevel)){
						productSearchService.setCustomerHierarchyLowestNode(angular.copy(selectedLevel));
						return;
					}
				}
			}
			productSearchService.setCustomerHierarchyLowestNode(null);
		};

		/**
		 * Helper method to determine if a level in the product hierarchy should be collapsed or not.
		 *
		 * @param relationship
		 * @returns {boolean}
		 */
		function isLowestBranchOrHasNoRelationships(relationship){
			if(relationship.lowestBranch){
				return true;
			} else if(relationship.childRelationships === null || relationship.childRelationships.length === 0) {
				return true;
			}
			return false;
		}

		/**
		 * Checks to see if we are at lowest level
		 * @returns {boolean}
		 */
		self.isLowestCustomHierarchyLevel = function() {
			var lowestNode = productSearchService.getLowestNode();
			if(!lowestNode){
				return false;
			}
			if(lowestNode.lowestBranch){
				return true;
			} else if(lowestNode.childRelationships === null || lowestNode.childRelationships.length === 0) {
				return true;
			}
			return false;
		};
	}
})();
