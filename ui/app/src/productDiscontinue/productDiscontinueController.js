/*
 *
 * productDeleteController.js
 *
 * Copyright (c) 2016 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * The controller for the product discontinue report.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ProductDeleteController', productDeleteController);

	productDeleteController.$inject = ['ProductDiscontinueApi', 'RemoveFromStoresApi', 'ngTableParams', 'ProductInfoService',
		'discontinueDefinitions', 'PermissionsService', 'InventoryFactory',	'ProductSearchService','urlBase',
		'DownloadService'];

	/**
	 * Constructs the controller.
	 *
	 * @param productDiscontinueApi The API to fetch data from the backend.
	 * @param removeFromStoresApi The API to send remove from stores data to backend.
	 * @param ngTableParams The API to set up the report table.
	 * @param productInfoService The service that will pass data to the panel to show detailed product information.
	 * @param discontinueDefinitions The definitions for the info on what Delete and Discontinue is.
	 * @param permissionsService The service used to look at user permissions.
	 * @param inventoryFactory The factory used to get inventory information from TIM.
	 * @param productSearchService The service used to communicate with reusable product search template.
	 * @param urlBase The base url used for building export requests.
	 * @param downloadService The service used for downloading a file.
	 */
	function productDeleteController(productDiscontinueApi, removeFromStoresApi ,ngTableParams, productInfoService,
									 discontinueDefinitions, permissionsService, inventoryFactory, productSearchService,
									 urlBase, downloadService) {

		var self = this;

		/**
		 * The current error message.
		 * @type {String}
		 */
		self.error = null;

		/**
		 * The current in Removed In Stores error message.
		 * @type {String}
		 */
		self.removedInStoresError = null;

		/**
		 * The current success message.
		 * @type {String}
		 */
		self.success = null;

		/**
		 * Whether or not the search paramaters panel is collapsed or open.
		 * @type {boolean}
		 */
		self.discontinueDataSearchToggle = true;

		/**
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;

		/**
		 * The total number of pages in the report.
		 * @type {null}
		 */
		self.totalPages = null;

		/**
		 * Wheter or not to ask the backed for the number of records and pages are available.
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;

		/**
		 * The message to display about the nubmer of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * The currently selected search type.
		 * @type {string}
		 */
		self.discontinueDataSearchBtnSwitch = null;

		/**
		 * Wheterer or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Today's Date.
		 * @type {Date}
		 */
		self.today = new Date();

		/**
		 * Whether or not the table has been built. We don't want to build the table until there is something
		 * to search for.
		 * @type {boolean} True if it has and false otherwise.
		 */
		self.tableBuilt = false;

		/**
		 * The paramaters that define the table showing the report.
		 * @type {ngTableParams}
		 */
		self.tableParams = null;

		/**
		 * The paramaters passed from the ngTable when it is asking for data.
		 * @type {?}
		 */
		self.dataResolvingParams = null;
		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 * @type {?}
		 */

		self.defer = null;

		/**
		 * The object thatwill hold the defenitions for desicontinue terms.
		 * @type {?}
		 */
		self.discontinueDefinition = discontinueDefinitions;

		/**
		 * The number of records to show on the report.
		 * --lowered from 100 to speed up search (m314029)
		 * @type {number}
		 */
		self.PAGE_SIZE = 25;

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
		 * Stores WHS inventories.
		 *
		 * @type {Object}
		 */
		self.selectedItem = null;
		/**
		 * Stores store inventory.
		 *
		 * @type {number}
		 */
		self.storeInventory = null;
		/**
		 * Stores vendor inventory.
		 *
		 * @type {number}
		 */
		self.vendorInventory = null;

		// String constants
		self.NO_SALES_CONSTANT = "Has Sales within the past 18 months.";
		self.NO_RECEIPTS_CONSTANT = "Has Store Receipts within the past 18 months.";
		self.NO_INVENTORY_CONSTANT = "Has Inventory.";
		self.NO_INVENTORY_WHS_CONSTANT = "Has Warehouse Inventory.";
		self.NEW_PRODUCT_CONSTANT = "Item has been in H-E-B systems for 12 months.";
		self.NO_PO_CONSTANT = "Has PO's within the past 18 months.";
		self.DSV_INVENTORY_CONSTANT = "Has DSV Inventory.";
		self.STORE_INVENTORY_CONSTANT = "Has Store Inventory.";
		self.WHS_INVENTORY_CONSTANT = "Has WHS Inventory.";
		self.UPC = "UPC";
		self.ITEM_CODE = "item code";
		self.PRODUCT_ID = "product ID";
		self.SUB_DEPARTMENT = "subDepartment";
		self.CLASS = "class";
		self.COMMODITY = "commodity";
		self.SUB_COMMODITY = "subCommodity";
		self.BDM = "BDM";

		const TAB_BASIC = "tabBasic";
		const TAB_PRODUCT_HIERARCHY = "tabProductHierarchy";
		const TAB_BDM = "tabBDM";
		const COMMODITY_CODE_FOR_ITEM_CLASS = 0;

        const DEFAULT_ITEM_STATUS = "NONE";

		/**
		 * Initializes the controller.
		 */
		self.bdmSelected = null;

		/**
		 * Product hierarchy selected.
		 */
		self.hierarchySelected = null;

		/**
		 * Stores what the user last searched for so it can be used during export.
		 * @type {null}
		 */
		self.searchSelection = null;

		/**
		 * Stores the type of search selection the user last did so it can be used during export.
		 *
		 * @type {null}
		 */
		self.searchSelectionType = null;

		/**
		 * Stores the type of search the user last did so it can be used during export.
		 *
		 * @type {null}
		 */
		self.searchType = null;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			productSearchService.init(false, true, self.newSearch, self.searchByHierarchy);
			self.initBasicSearch();
		};

		/**
		 * Initializes the basic search.
		 */
		self.initBasicSearch = function(){
			self.resetView(self.UPC, TAB_BASIC);
		};

		/**
		 * Initializes the product hierarchy search.
		 */
		self.initProductHierarchySearch = function(){
			self.resetView(self.SUB_DEPARTMENT, TAB_PRODUCT_HIERARCHY);
			self.hierarchySelected = null;
		};

		/**
		 * Initializes the bdm search.
		 */
		self.initBDMSearch = function(){
			self.resetView(self.BDM, TAB_BDM);
			self.bdmSelected = null;
		};

		/**
		 * Reset page view by hiding product view, setting the search radio button switch and the tab.
		 *
		 * @param newSwitchValue New value for the search.
		 * @param newTabValue New value for the tab.
		 */
		self.resetView = function(newSwitchValue, newTabValue){
			productInfoService.hide();
			productSearchService.setSelectionType(newSwitchValue);
			self.tabSelected = newTabValue;
			self.selectedItem = null;
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
		 * Callback for the request for the number of items found and not found.
		 *
		 * @param results The object returned from the backend with a list of found and not found items.
		 */
		self.showMissingData = function(results){
			self.missingValues = results;
		};


		/**
		 * Decides whether or not a record should have been deleted.
		 *
		 * @param productDiscontinue The product discontinue record to check.
		 * @returns {boolean} True if this record should have been deleted and false otherwise.
		 */
		self.shouldBeDeleted = function (productDiscontinue){
			return new Date(productDiscontinue.projectedDeleteDate) < self.today;
		};

		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return "Result " + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " + self.totalRecordCount;
		};

		/**
		 * Clears the search box.
		 */
		self.clearDiscontinueDataSearch = function(){
			self.itemSearch = '';
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			self.data = null;
			if (error && error.data) {
				self.setError(error.data.message);
			}
			else {
				self.setError("An unknown error occurred.");
			}
		};

		/**
		 * Returns the text to display in the search box when it is empty.
		 *
		 * @returns {string} The text to display in the search box when it is empty.
		 */
		self.getTextPlaceHolder = function(){
			return 'Enter ' + productSearchService.getSelectionType() + 's to search'
		};

		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(){
			self.firstSearch = true;

			// Save the search selection in case they need to do an export.
			self.searchSelection = productSearchService.getSearchSelection();
			self.searchSelectionType = productSearchService.getSelectionType();
			self.searchType = productSearchService.getSearchType();

			productSearchService.setSearchPanelVisibility(false);
			productInfoService.hide();

			// The first time through, build the table. The rest of the time, just tell it to fetch new data.
			if (self.tableBuilt) {
				self.tableParams.reload();
			} else {
				self.tableBuilt = true;
				self.buildTable();
			}

		};

        /**
		 * Define method search, that has been call from hierarchy search. If the user has selected the tab to get data by product hierarchy,
         *  a call to the backend to get the data.
		 *
		 * @param searchSelection The search selection
         */
        self.searchByHierarchy = function (searchSelection) {
            self.firstSearch = true;

            // Default is hierarchy search
            // Save the search selection in case they need to do an export.
            self.searchType = productSearchService.HIERARCHY_SEARCH;
            self.setupSearchOptionBySearchSelectionHierarchy(searchSelection);

            productSearchService.setItemStatus(DEFAULT_ITEM_STATUS);
            productSearchService.setSearchPanelVisibility(false);
            productInfoService.hide();

            // The first time through, build the table. The rest of the time, just tell it to fetch new data.
            if (self.tableBuilt) {
                self.tableParams.reload();
            } else {
                self.tableBuilt = true;
                self.buildTable();
            }
        };

        /**
         *  Setup search selection information by multi selection from hierarchy. Basing each case has been selected
		 *  to set value for search selection object.
		 *
         * @param searchSelection The search selection
         */
        self.setupSearchOptionBySearchSelectionHierarchy = function (searchSelection) {
        	if(searchSelection.subDepartment && searchSelection.itemClass && searchSelection.classCommodity && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
					searchSelection.itemClass.itemClassCode, searchSelection.classCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
			}
			else if(searchSelection.subDepartment && searchSelection.itemClass && searchSelection.classCommodity){
        		self.searchSelectionType = productSearchService.DEPARTMENT_CLASS_COMMODITY;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
                    searchSelection.itemClass.itemClassCode, searchSelection.classCommodity.key.commodityCode);
			}
            else if(searchSelection.subDepartment && searchSelection.itemClass && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
                    searchSelection.itemClass.itemClassCode, searchSelection.subCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
            }
            else if(searchSelection.subDepartment && searchSelection.classCommodity && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.DEPARTMENT_COMMODITY_SUB_COMMODITY;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
                    null, searchSelection.classCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
			}
			else if(searchSelection.itemClass && searchSelection.classCommodity && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.SUB_COMMODITY;
                self.searchSelection = {};
                self.searchSelection.key = self.buildSearchSelection(null, null, searchSelection.itemClass.itemClassCode, searchSelection.classCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
            }
            else if(searchSelection.subDepartment && searchSelection.itemClass){
                self.searchSelectionType = productSearchService.DEPARTMENT_CLASS;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
                    searchSelection.itemClass.itemClassCode, null, null);
            }
            else if(searchSelection.subDepartment && searchSelection.classCommodity){
                self.searchSelectionType = productSearchService.DEPARTMENT_CLASS_COMMODITY;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
                    searchSelection.classCommodity.key.classCode, searchSelection.classCommodity.key.commodityCode, null);
            }
            else if(searchSelection.subDepartment && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY;
                self.searchSelection = self.buildSearchSelection(searchSelection.subDepartment.key.department, searchSelection.subDepartment.key.subDepartment,
                    searchSelection.subCommodity.key.classCode, searchSelection.subCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
            }
            else if(searchSelection.itemClass && searchSelection.classCommodity){
                self.searchSelectionType = productSearchService.COMMODITY;
                self.searchSelection = {};
                self.searchSelection.key = self.buildSearchSelection(null, null, searchSelection.itemClass.itemClassCode, searchSelection.classCommodity.key.commodityCode, null);
            }
            else if(searchSelection.itemClass && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.SUB_COMMODITY;
                self.searchSelection = {};
                self.searchSelection.key = self.buildSearchSelection(null, null, searchSelection.itemClass.itemClassCode, searchSelection.subCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
            }
            else if(searchSelection.classCommodity && searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.COMMODITY_SUB_COMMODITY;
                self.searchSelection = self.buildSearchSelection(null, null,null, searchSelection.classCommodity.key.commodityCode, searchSelection.subCommodity.key.subCommodityCode);
            }
            else if(searchSelection.subDepartment) {
                self.searchSelectionType = productSearchService.SUB_DEPARTMENT;
                self.searchSelection = searchSelection.subDepartment;
            }
            else if(searchSelection.itemClass){
                self.searchSelectionType = productSearchService.CLASS;
                self.searchSelection = searchSelection.itemClass;
            }
            else if(searchSelection.classCommodity){
                self.searchSelectionType = productSearchService.COMMODITY;
                self.searchSelection = searchSelection.classCommodity;
            }
            else if(searchSelection.subCommodity){
                self.searchSelectionType = productSearchService.SUB_COMMODITY;
                self.searchSelection = searchSelection.subCommodity;
            }
        };

        /**
		 * Build data search for critical search object.
         * @param department The department for searching
         * @param subDepartment  The department for searching
         * @param classCode
         * @param commodityCode
         * @param subCommodityCode
         */
         self.buildSearchSelection = function (department, subDepartment, classCode, commodityCode, subCommodityCode) {
             var searchSelection = {};
             if(department){
             	searchSelection.department = department;
             }
             if(subDepartment){
                 searchSelection.subDepartment = subDepartment;
             }
             if(classCode){
                 searchSelection.classCode = classCode;
             }
             if(commodityCode){
                 searchSelection.commodityCode = commodityCode;
             }
             if(subCommodityCode){
                 searchSelection.subCommodityCode = subCommodityCode;
             }
             return searchSelection;
         };

		/**
		 * Returns selected upcs to be removed from stores to the backend.
		 */
		self.removeFromStore = function(){
			var isRemoved;
			var returnList = [];
			var product;

			for(var index = 0 ; index < self.data.length ; index++){
				isRemoved = document.getElementById("removedInStores" + index).checked;
				if(self.data[index].sellingUnit.processedByScanMaintenance == isRemoved){
					product = self.data[index];
					product.sellingUnit.processedByScanMaintenance = isRemoved;
					returnList.push({upc: self.data[index].key.upc, removedInStores: self.data[index].sellingUnit.processedByScanMaintenance});
					console.log(self.data[index].sellingUnit.processedByScanMaintenance);
				}
			}
			if(returnList.length > 0) {
				removeFromStoresApi.save(returnList, self.successMessage, self.fetchError);
			}
		};

		/**
		 * Callback for a successful call to the backend.
		 *
		 * @param results The response from the backend.
		 */
		self.successMessage = function(results){
			if(results.message == "Remove from Stores Successful") {
				self.removedInStoresError = null;
				self.success = results.message;
			} else {
				self.removedInStoresError = results.message;
				self.tableParams = null;
				self.buildTable();
			}

		};

		/**
		 * Returns whether or not the user is currently viewing product information.
		 *
		 * @returns {boolean} True if the user is viewing product information and false otherwise.
		 */
		self.isViewingProductInfo = function(){
			return productInfoService.isVisible();
		};

		/**
		 * Shows the panel that contains product detail information.
		 *
		 * @param productId The product ID of the product to show.
		 */
		self.showProductInfo = function(productId){
			productInfoService.show(self, productId);
		};

		/**
		 * Returns whether or not the user is currently downloading a CSV,
		 *
		 * @returns {boolean} True if the user is downloading product information CSV and false otherwise.
		 */
		self.isDownloading = function(){
			return self.downloading;
		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function()
		{
			self.tableParams = new ngTableParams(
				{
					// set defaults for ng-table
					page: 1,
					count: self.PAGE_SIZE,
					sorting: {UPC: "asc"}
				}, {

					// hide page size
					counts: [],

					defaultSort: 'asc',

					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {

						self.isWaiting = true;
						self.data = null;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// Get what to sort on and the direction.
						var keys = Object.keys(params.sorting());
						var sortColumn = keys[0];
						var sortDirection = params.sorting()[sortColumn].toUpperCase();

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.firstSearch){
							self.includeCounts = true;
							params.page(1);
							self.firstSearch = false;
						} else {
							self.includeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getReportByTab(sortColumn, sortDirection, params.page() -1);
					}
				}
			);
		};

		/**
		 *  Calls the method to get data based on tab selected.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByTab = function(sortColumn, sortDirection, page) {

			switch (self.searchType){
				case productSearchService.BASIC_SEARCH:{
					self.getReportByBasicTab(sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.HIERARCHY_SEARCH:{
					self.getReportByProductHierarchyTab(sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.BDM_SEARCH:{
					self.getReportByBdmTab(sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.VENDOR_SEARCH:
				{
					self.getReportByVendor(sortColumn, sortDirection, page);
                    break;
				}
			}
		};

		/**
		 * Issue the call to get data by vendor.
		 *
		 * @param sortColumn column to sort on.
		 * @param sortDirection direction to sort.
		 * @param page The page to get.
		 */
		self.getReportByVendor = function(sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryByVendor({
				vendor: self.searchSelection.vendorNumber,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);
		};

		/**
		 *  If the user has selected the tab to get data by the basic tab, this will issue
		 *  a call to the appropriate method to get the data.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByBasicTab = function(sortColumn, sortDirection, page) {
			switch (self.searchSelectionType) {
				case productSearchService.UPC:
				{
					self.getReportByUpc(sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.ITEM_CODE:
				{
					self.getReportByItemCode(sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.PRODUCT_ID:
				{
					self.getReportByProductId(sortColumn, sortDirection, page);
					break;
				}
			}
		};

		/**
		 *  If the user has selected the tab to get data by product hierarchy, this will issue
		 *  a call to the backend to get the data.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByProductHierarchyTab = function(sortColumn, sortDirection, page) {

			switch (self.searchSelectionType) {
				case productSearchService.SUB_DEPARTMENT:
				{
					self.getReportBySubDepartment(self.searchSelection.key, sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.CLASS:
				{
					self.getReportByItemClass(self.searchSelection, sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.COMMODITY:
				{
					self.getReportByClassAndCommodity(self.searchSelection.key, sortColumn, sortDirection, page);
					break;
				}
				case productSearchService.SUB_COMMODITY:
				{
					self.getReportBySubCommodity(self.searchSelection.key, sortColumn, sortDirection, page);
                    break;
				}
				case productSearchService.DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY :
				{
                    self.getReportByDepartmentAndClassAndCommodityAndSubCommodity(self.searchSelection, sortColumn, sortDirection, page);
                    break;
				}
                case productSearchService.DEPARTMENT_CLASS_COMMODITY :
                {
                    self.getReportByDepartmentAndClassAndCommodity(self.searchSelection, sortColumn, sortDirection, page);
                    break;
                }
                case productSearchService.DEPARTMENT_COMMODITY_SUB_COMMODITY :
                {
                    self.getReportByDepartmentAndCommodityAndSubCommodity(self.searchSelection, sortColumn, sortDirection, page);
                    break;
                }
                case productSearchService.DEPARTMENT_CLASS :
                {
                    self.getReportByDepartmentAndClass(self.searchSelection, sortColumn, sortDirection, page);
                    break;
                }
                case productSearchService.COMMODITY_SUB_COMMODITY :
				{
                    self.getReportByCommodityAndSubCommodity(self.searchSelection, sortColumn, sortDirection, page);
                    break;
                }
			}
		};

        /**
         * Issue the call to get data by sub commodity.
         *
         * @param selectedObject Object containing necessary data.
         * @param sortColumn The column to sort on.
         * @param sortDirection The direction to sort in.
         * @param page The page to get.
         */
        self.getReportByDepartmentAndClass = function(selectedObject, sortColumn, sortDirection, page) {
            // Get the report data.
            productDiscontinueApi.queryByDepartmentAndClass({
                department: selectedObject.department,
                subDepartment: selectedObject.subDepartment,
                classCode: selectedObject.classCode,
                status: productSearchService.getItemStatus(),
                includeCounts: self.includeCounts,
                page: page,
                pageSize: self.PAGE_SIZE,
                sortColumn: sortColumn,
                sortDirection: sortDirection
            }, self.loadData, self.fetchError);
        };

        /**
         * Issue the call to get data by sub commodity.
         *
         * @param selectedObject Object containing necessary data.
         * @param sortColumn The column to sort on.
         * @param sortDirection The direction to sort in.
         * @param page The page to get.
         */
        self.getReportByDepartmentAndCommodityAndSubCommodity = function(selectedObject, sortColumn, sortDirection, page) {
            // Get the report data.
            productDiscontinueApi.queryByDepartmentAndCommodityAndSubCommodity({
                department: selectedObject.department,
                subDepartment: selectedObject.subDepartment,
                commodityCode: selectedObject.commodityCode,
                subCommodityCode: selectedObject.subCommodityCode,
                status: productSearchService.getItemStatus(),
                includeCounts: self.includeCounts,
                page: page,
                pageSize: self.PAGE_SIZE,
                sortColumn: sortColumn,
                sortDirection: sortDirection
            }, self.loadData, self.fetchError);
        };

        /**
         * Issue the call to get data by sub commodity.
         *
         * @param selectedObject Object containing necessary data.
         * @param sortColumn The column to sort on.
         * @param sortDirection The direction to sort in.
         * @param page The page to get.
         */
        self.getReportByDepartmentAndClassAndCommodity = function(selectedObject, sortColumn, sortDirection, page) {
            // Get the report data.
            productDiscontinueApi.queryByDepartmentAndClassAndCommodity({
                department: selectedObject.department,
                subDepartment: selectedObject.subDepartment,
                classCode: selectedObject.classCode,
                commodityCode: selectedObject.commodityCode,
                status: productSearchService.getItemStatus(),
                includeCounts: self.includeCounts,
                page: page,
                pageSize: self.PAGE_SIZE,
                sortColumn: sortColumn,
                sortDirection: sortDirection
            }, self.loadData, self.fetchError);
        };


        /**
         * Issue the call to get data by sub commodity.
         *
         * @param selectedObject Object containing necessary data.
         * @param sortColumn The column to sort on.
         * @param sortDirection The direction to sort in.
         * @param page The page to get.
         */
        self.getReportByCommodityAndSubCommodity = function(selectedObject, sortColumn, sortDirection, page) {
            // Get the report data.
            productDiscontinueApi.queryByCommodityAndSubCommodity({
                commodityCode: selectedObject.commodityCode,
                subCommodityCode: selectedObject.subCommodityCode,
                status: productSearchService.getItemStatus(),
                includeCounts: self.includeCounts,
                page: page,
                pageSize: self.PAGE_SIZE,
                sortColumn: sortColumn,
                sortDirection: sortDirection
            }, self.loadData, self.fetchError);
        };

        /**
         * Issue the call to get data by sub commodity.
         *
         * @param selectedObject Object containing necessary data.
         * @param sortColumn The column to sort on.
         * @param sortDirection The direction to sort in.
         * @param page The page to get.
         */
        self.getReportByDepartmentAndClassAndCommodityAndSubCommodity = function(selectedObject, sortColumn, sortDirection, page) {
            // Get the report data.
            productDiscontinueApi.queryByDepartmentAndClassAndCommodityAndSubCommodity({
                department: selectedObject.department,
                subDepartment: selectedObject.subDepartment,
                classCode: selectedObject.classCode,
                commodityCode: selectedObject.commodityCode,
                subCommodityCode: selectedObject.subCommodityCode,
                status: productSearchService.getItemStatus(),
                includeCounts: self.includeCounts,
                page: page,
                pageSize: self.PAGE_SIZE,
                sortColumn: sortColumn,
                sortDirection: sortDirection
            }, self.loadData, self.fetchError);
        };

		/**
		 *  If the user has selected the tab to get data by bdm, this will issue a call to the
		 * backend to get the data.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByBdmTab = function(sortColumn, sortDirection, page) {
			// Get the report data.
			productDiscontinueApi.queryByBDM({
				bdm: self.searchSelection.bdmCode,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);
		};

		/**
		 * Issue the call to get data by sub department.
		 *
		 * @param selectedObject Object containing necessary data.
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportBySubDepartment = function(selectedObject, sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryBySubDepartment({
				department: selectedObject.department,
				subDepartment: selectedObject.subDepartment,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);
		};

		/**
		 * Issue the call to get data by class and commodity.
		 *
		 * @param selectedObject Object containing necessary data.
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByClassAndCommodity = function(selectedObject, sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryByClassAndCommodity({
				classCode: selectedObject.classCode,
				commodityCode: selectedObject.commodityCode,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);
		};

		/**
		 * Issue the call to get data by item-class.
		 *
		 * @param selectedObject Object containing necessary data.
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByItemClass = function(selectedObject, sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryByClassAndCommodity({
				classCode: selectedObject.itemClassCode,
				commodityCode: COMMODITY_CODE_FOR_ITEM_CLASS,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);
		};

		/**
		 * Issue the call to get data by sub commodity.
		 *
		 * @param selectedObject Object containing necessary data.
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportBySubCommodity = function(selectedObject, sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryBySubCommodity({
				classCode: selectedObject.classCode,
				commodityCode: selectedObject.commodityCode,
				subCommodityCode: selectedObject.subCommodityCode,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);
		};

		/**
		 *  If the user has selected the option to get data by item code, this will issue a call to the
		 * backend to get the data.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByItemCode = function(sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryByItemCodes({
				itemCodes: self.searchSelection,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);

			// Get the numbers of found and missing item codes.
			productDiscontinueApi.queryForMissingItemCodes({
				itemCodes: self.searchSelection
			}, self.showMissingData, self.fetchError);

		};

		/**
		 * If the user has selected the option to get data by UPC, this will issue a call to the
		 * backend to get the data.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByUpc = function(sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryByUPCs({
				upcs:  self.searchSelection,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData, self.fetchError);

			// Get the numbers of found and missing item codes.
			productDiscontinueApi.queryForMissingUPCs({
				upcs: self.searchSelection
			}, self.showMissingData, self.fetchError);
		};

		/**
		 * If the user has selected the option to get data by product ID, this will issue a call to the
		 * backend to get the data.
		 *
		 * @param sortColumn The column to sort on.
		 * @param sortDirection The direction to sort in.
		 * @param page The page to get.
		 */
		self.getReportByProductId = function(sortColumn, sortDirection, page) {

			// Get the report data.
			productDiscontinueApi.queryByProductIds({
				productIds: self.searchSelection,
				status: productSearchService.getItemStatus(),
				includeCounts: self.includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE,
				sortColumn: sortColumn,
				sortDirection: sortDirection
			}, self.loadData , self.fetchError);

			// Get the numbers of found and missing item codes.
			productDiscontinueApi.queryForMissingProductIds({
				productIds: self.searchSelection
			}, self.showMissingData, self.fetchError);
		};

		/**
		 * Only show match count information for basic search
		 * @returns {boolean}
		 */
		self.showMatchCount = function(){

			return self.searchType == productSearchService.BASIC_SEARCH;

		};

		/**
		 * Callback for a successful call to get data from the backend. It requries the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results) {
			self.success = null;

			self.isWaiting = false;
			self.discontinueDataSearch = false;
			self.selectedItem = null;

			self.myColumns =
			{ showRemovedInStores: self.getRemoveFromStorePermissions() };

			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
			}

			if (results.data.length === 0) {
				self.error = "No records found.";
			} else {
				self.error = self.removedInStoresError != null ? self.removedInStoresError : null;
				self.data = results.data;
				self.resultMessage = self.getResultMessage(results.data.length, results.page);

				if(self.showMatchCount()){
					self.discontinueDataSearchBtnSwitch = self.searchSelectionType;
				}
				var currentObject;

				// For each object in the result, if it can be discontinued but it isn't, set it
				// as in-progress.
				for (var index = 0; index < results.data.length; index++) {
					currentObject = results.data[index];
					currentObject.isDiscontinueCriteriaExisting = (!currentObject.inventorySet || !currentObject.newItemSet ||
					!currentObject.openPoSet || !currentObject.salesSet || !currentObject.warehouseInventorySet ||
					!currentObject.storeReceiptsSet);

					if (!results.data[index].discontinued) {
						currentObject.inProgress = (currentObject.inventorySet && currentObject.newItemSet &&
						currentObject.openPoSet && currentObject.salesSet &&
						currentObject.storeReceiptsSet);
					}
				}
			}

			// Return the data back to the ngTable.
			self.dataResolvingParams.total(self.totalRecordCount);
			self.defer.resolve(results.data);

		};

		/**
		 * Returns permissionsService for the user loggedin.
		 *
		 * @returns {boolean}
		 */
		self.getRemoveFromStorePermissions = function(){
			return permissionsService.getPermissions('PD_UPCM_01', 'EDIT');
		};

		/**
		 * Returns true if the record is a primary UPC and discontinued.
		 *
		 * @param productDeleteRecord the current product delete record.
		 * @returns {boolean}
		 */
		self.isDiscontinuedPrimary = function(productDeleteRecord){
			return productDeleteRecord.sellingUnit.primaryUpc;
		};

		/**
		 * Returns true if the upc is an associate and discontinued at the associate level.
		 *
		 * @param productDeleteRecord the current product delete record.
		 * @returns {boolean}
		 */
		self.isDiscontinuedAssociate = function(productDeleteRecord){
			return (!productDeleteRecord.sellingUnit.primaryUpc && productDeleteRecord.sellingUnit.discontinueDate != null);
		};

		/**
		 * Returns 3 if the UPC is an associate and is discontinue and the primary (case pack level).
		 *
		 * @param productDeleteRecord the current product delete record.
		 * @returns {boolean}
		 */
		self.isDiscontinuedAssociateAtCasepack = function(productDeleteRecord){
			return (!productDeleteRecord.sellingUnit.primaryUpc && productDeleteRecord.sellingUnit.discontinueDate == null);
		};

		/**
		 * Retrieves the purchase order
		 *
		 * @param productDeleteRecord the current product delete record
		 */
		self.getPurchaseOrder = function(productDeleteRecord){
			inventoryFactory.queryPurchaseOrders({itemCode: productDeleteRecord.key.itemCode}, self.setPurchaseOrder, self.fetchError);
		};

		/**
		 * Sets the Purchase Order attributes
		 *
		 * @param results Purchase Order results
		 */
		self.setPurchaseOrder = function(results) {
			self.purchaseOrderDate = results.purchaseOrderDate;
			self.purchaseOrderNumber = results.purchaseOrderNumber;
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {

			self.downloading = true;
			downloadService.export(self.generateExportUrl(), 'productDiscontinues.csv', self.WAIT_TIME,
				function() { self.downloading = false; });
		};

		/**
		 * Generates the URL to ask for the export.
		 *
		 * @returns {string} The URL to ask for the export.
		 */
		self.generateExportUrl = function() {
			var hierarchy;

			switch(self.searchSelectionType){
				case productSearchService.BDM:
				{
					return urlBase + '/pm/productDiscontinue/exportBdmToCsv?bdm=' +
						encodeURI(self.searchSelection .bdmCode) + "&statusFilter= " +
						productSearchService.getItemStatus() + "&totalRecordCount="
						+ self.totalRecordCount;
				}
				case productSearchService.SUB_DEPARTMENT:
				{
					hierarchy = self.searchSelection.key;

					return urlBase + '/pm/productDiscontinue/exportDepartmentToCsv?department=' +	encodeURI(hierarchy.department)
						+ "&subdepartment=" +encodeURI(hierarchy.subDepartment) + "&statusFilter= " + productSearchService.getItemStatus()
						+	"&totalRecordCount=" + self.totalRecordCount;
				}
				case productSearchService.CLASS:
				{
					hierarchy = self.searchSelection;

					return urlBase + '/pm/productDiscontinue/exportClassCommodityToCsv?classCode=' + hierarchy.itemClassCode +
						"&commodityCode=0&statusFilter= " + productSearchService.getItemStatus() +
						"&totalRecordCount=" + self.totalRecordCount;
				}
				case productSearchService.COMMODITY:
				{
					hierarchy = self.searchSelection.key;

					return urlBase + '/pm/productDiscontinue/exportClassCommodityToCsv?classCode=' + hierarchy.classCode +
						"&commodityCode=" +hierarchy.commodityCode + "&statusFilter= " + productSearchService.getItemStatus() +
						"&totalRecordCount=" + self.totalRecordCount;
				}
				case productSearchService.SUB_COMMODITY:
				{
					hierarchy = self.searchSelection.key;

					return urlBase + '/pm/productDiscontinue/exportSubCommodityToCsv?classCode=' + hierarchy.classCode +
						"&commodityCode=" +hierarchy.commodityCode +  "&subCommodityCode=" + hierarchy.subCommodityCode+ "&statusFilter= "
						+ productSearchService.getItemStatus() + "&totalRecordCount=" + self.totalRecordCount;
				}
				case productSearchService.UPC:
				{
					return urlBase + '/pm/productDiscontinue/exportUpcsToCsv?upcs=' +
						encodeURI(self.searchSelection ) +
						"&statusFilter= " + productSearchService.getItemStatus() +
						"&totalRecordCount=" + self.totalRecordCount;
				}
				case productSearchService.ITEM_CODE:
				{
					return urlBase + '/pm/productDiscontinue/exportItemCodesToCsv?itemCodes=' +
						encodeURI(self.searchSelection ) +
						"&statusFilter= " + productSearchService.getItemStatus() +
						"&totalRecordCount=" + self.totalRecordCount;
				}
				case productSearchService.PRODUCT_ID:
				{
					return urlBase + '/pm/productDiscontinue/exportProductIdsToCsv?productIds=' +
						encodeURI(self.searchSelection) +
						"&statusFilter= " + productSearchService.getItemStatus() +
						"&totalRecordCount=" + self.totalRecordCount;
				}
                case productSearchService.DEPARTMENT_CLASS_COMMODITY_SUB_COMMODITY:
                {
                    hierarchy = self.searchSelection;

                    return urlBase + '/pm/productDiscontinue/exportDepartmentClassCommoditySubCommodityToCsv?department=' +	encodeURI(hierarchy.department)
                        + "&subdepartment=" +encodeURI(hierarchy.subDepartment)
                        + "&classCode=" +hierarchy.classCode
                        + "&commodityCode=" +hierarchy.commodityCode
                        + "&subCommodityCode=" +hierarchy.subCommodityCode
						+ "&statusFilter= " + productSearchService.getItemStatus()
                        +	"&totalRecordCount=" + self.totalRecordCount;
                }
                case productSearchService.DEPARTMENT_CLASS_COMMODITY:
                {
                    hierarchy = self.searchSelection;

                    return urlBase + '/pm/productDiscontinue/exportDepartmentClassCommodityToCsv?department=' +	encodeURI(hierarchy.department)
                        + "&subdepartment=" +encodeURI(hierarchy.subDepartment)
                        + "&classCode=" +hierarchy.classCode
                        + "&commodityCode=" +hierarchy.commodityCode
                        + "&statusFilter= " + productSearchService.getItemStatus()
                        +	"&totalRecordCount=" + self.totalRecordCount;
                }
                case productSearchService.DEPARTMENT_COMMODITY_SUB_COMMODITY:
                {
                    hierarchy = self.searchSelection;

                    return urlBase + '/pm/productDiscontinue/exportDepartmentCommoditySubCommodityToCsv?department=' +	encodeURI(hierarchy.department)
                        + "&subdepartment=" +encodeURI(hierarchy.subDepartment)
                        + "&commodityCode=" +hierarchy.commodityCode
                        + "&subCommodityCode=" +hierarchy.subCommodityCode
                        + "&statusFilter= " + productSearchService.getItemStatus()
                        +	"&totalRecordCount=" + self.totalRecordCount;
                }
                case productSearchService.DEPARTMENT_CLASS:
                {
                    hierarchy = self.searchSelection;

                    return urlBase + '/pm/productDiscontinue/exportDepartmentClassToCsv?department=' +	encodeURI(hierarchy.department)
                        + "&subdepartment=" +encodeURI(hierarchy.subDepartment)
                        + "&classCode=" +hierarchy.classCode
                        + "&statusFilter= " + productSearchService.getItemStatus()
                        +	"&totalRecordCount=" + self.totalRecordCount;
                }
                case productSearchService.COMMODITY_SUB_COMMODITY:
                {
                    hierarchy = self.searchSelection;

                    return urlBase + '/pm/productDiscontinue/exportCommodityAndSubCommodityToCsv?commodityCode=' +	hierarchy.commodityCode
                        + "&subCommodityCode=" +hierarchy.subCommodityCode
						+ "&statusFilter= " + productSearchService.getItemStatus()
                        +	"&totalRecordCount=" + self.totalRecordCount;
                }
			}
		};

		/**
		 * Sets the current selected item to get whs inventory for.
		 * @param item The current item.
		 */
		self.setSelectedItem = function(item){
			self.selectedItem = item;
			console.log(self.selectedItem)
		};

		/**
		 * calls the inventory factory to get store inventory.
		 * @param product the product to get store inventory
		 */
		self.setStoreInventory = function(productId){
			inventoryFactory.queryStoreInventory({
				productId: productId
			}, self.setStoreInventoryData, self.fetchError);

		};
		/**
		 * calls the inventory factory to get vendor inventory.
		 * @param product the product to get vendor inventory
		 */
		self.setDsvInventory = function(productId){
			inventoryFactory.queryVendorInventory({
				productId: productId
			}, self.setVendorInventoryData, self.fetchError);
		};


		/**
		 * Callback for the API call to get product level store inventory.
		 *
		 * @param result The results sent from the backend.
		 */
		self.setStoreInventoryData = function(result){
			self.storeInventory = result.inventory;
		};

		/**
		 * Callback for the API call to get product level vendor inventory.
		 *
		 * @param result The results sent from the backend.
		 */
		self.setVendorInventoryData = function(result){
			self.vendorInventory = result.inventory;
		};
	}
})();
