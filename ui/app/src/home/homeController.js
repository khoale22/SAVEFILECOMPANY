'use strict';

(function () {
	angular.module('productMaintenanceUiApp').controller('HomeController', homeController);

	homeController.$inject = ['HomeApi','ngTableParams', 'ProductInfoService','ProductSearchService',
		'PermissionsService', '$scope', 'DownloadService', 'urlBase', '$timeout', '$rootScope', 'customHierarchyService', '$state', 'appConstants',
		'MyAttributeFactory', 'ProductUpdatesTaskApi', 'NutritionUpdatesTaskApi', 'EcommerceTaskApi', 'productDetailApi', 'CustomHierarchyApi'];

	function homeController(homeApi, ngTableParams, productInfoService, productSearchService, permissionsService,
							$scope, downloadService, urlBase, $timeout, $rootScope, customHierarchyService, $state,
							appConstants, myAttributeFactory, productUpdatesTaskApi, nutritionUpdatesTaskApi,
							ecommerceTaskApi, productDetailApi, customHierarchyApi) {

		var self = this;

		self.UPC = "UPC";
		self.ITEM_CODE = "item code";
		self.PRODUCT_ID = "product ID";
		self.PRODUCT_DESCRIPTION = "product description";
		self.SUB_DEPARTMENT = "subDepartment";
		self.CLASS = "class";
		self.COMMODITY = "commodity";
		self.SUB_COMMODITY = "subCommodity";
		self.BDM = "BDM";

		// Mass update types. These should match the values from MassUpdateParameters.Attribute
		self.THIRD_PARTY_SELLABLE = "THIRD_PARTY_SELLABLE";
		self.PRODUCE_ORGANIC = "PRODUCE_ORGANIC";
		self.SELECT_INGREDIENTS = "SELECT_INGREDIENTS";
		self.TOTALLY_TEXAS = "TOTALLY_TEXAS";
		self.GO_LOCAL = "GO_LOCAL";
		self.FOOD_STAMP = "FOOD_STAMP";
		self.FSA = "FSA";
		self.TAX_FLAG = "TAX_FLAG";
		self.SELF_MANUFACTURED = "SELF_MANUFACTURED";
		self.TAX_CATEGORY = "TAX_CATEGORY";
		self.PRIMO_PICK = "PRIMO_PICK";
        self.DISTINCTIVE  = "DISTINCTIVE";
		self.TAG_TYPE = "TAG_TYPE";
		self.FULFILLMENT_CHANNEL="FULFILLMENT_CHANNEL";
		self.PDP_TEMPLATE="PDP_TEMPLATE";
		self.SHOW_ON_SITE = "SHOW_ON_SITE";

		// The types of primo pick updates allowed. These should match the values from MassUpdateParameters.PrimoPickFunction
		self.APPROVE_PRIMO_PICK = "APPROVE_PRIMO_PICK";
		self.REJECT_PRIMO_PICK = "REJECT_PRIMO_PICK";
		self.TURN_ON_PRIMO_PICK = "TURN_ON_PRIMO_PICK";
		self.TURN_OFF_PRIMO_PICK = "TURN_OFF_PRIMO_PICK";
		self.TURN_ON_DISTINCTIVE = "TURN_ON_DISTINCTIVE";
		self.TURN_OFF_DISTINCTIVE = "TURN_OFF_DISTINCTIVE";

		self.DISTINCTIVE_CLAIM_CODE = '00001';
		self.PRIMO_PICK_CLAIM_CODE = '00002';
		self.SELECT_INGREDIENTS_CLAIM_CODE = '00006';
		self.TOTALLY_TEXAS_CLAIM_CODE = '00005';
		self.GO_LOCAL_CLAIM_CODE = '00004';

		self.RETURN_TO_PRODUCT_LIST = "returnToProductList";
		/**
		 * Sales channel of HEB.Com
		 * @type {string}
		 */
		const HEB_COM_SALES_CHANNEL = '01';
		/**
		 * Sales channel of GOOGLE
		 * @type {string}
		 */
		const GOOGLE_SALE_CHANNEL = '05';
		/**
		 * Sales channel of STORE
		 * @type {string}
		 */
		const STORE_SALE_CHANNEL = '02';
        /**
         * Max end date of show on site.
         * @type {string}
         */
        const MAX_END_DATE = '12-31-9999';
        /**
         * Resource id of show on site.
         * @type {string}
         */
        const SHOW_ON_SITE_RESOURCE_ID = 'MU_SOS_01';
		/**
		 * This message will show when mass update that there is no any sale channels are select.
		 * @type {string}
		 */
		self.EMPTY_SALE_CHANNEL_MESSAGE = 'Please select at least one Sale Channel to Mass Update';
		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.DOWNLOAD_WAIT_TIME = 1200;

		/**
		 * Wheter or not the UI is waiting for a download to start.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;

		/**
		 * Whether or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Whether or this is the first search done on a query.
		 * @type {boolean} True if it has and false otherwise.
		 */
		self.firstSearch = true;

		/**
		 * The paramaters that define the table showing the report.
		 * @type {ngTableParams}
		 */
		self.tableParams = null;

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
		 * The number of records to show on the report.
		 * --lowered from 100 to speed up search (m314029)
		 * @type {number}
		 */
		self.DEFAULT_PAGE_SIZE = 25;

        self.pageSize = 25;

		/**
		 * If the product details page is being viewed.
		 * @type {boolean}
		 */
		self.viewingProductDetails = false;

		/**
		 * Whether or not the user is doing an MRT search.
		 * @type {boolean}
		 */
		self.mrtSearch = false;

		/**
		 * The criteria the user used to search for products.
		 * @type {null}
		 */
		self.searchCriteria = null;

		// Stuff related to mass update.
		/**
		 * On the product list, whether or not the user has chosen to select all the rows.
		 *
		 * @type {boolean}
		 */
		self.allSelected = false;

		/**
		 * A list of product IDs the user has selected. This is meaningful only if allSelected is false.
		 *
		 * @type {Array}
		 */
		self.selectedProudctIds = [];

		/**
		 * A list of product IDs the user has de-selected. This is meaningful only if allSelected is true.
		 *
		 * @type {Array}
		 */
		self.deselectedProductIds = [];

		/**
		 * The options for the date pickers.
		 *
		 * @type {Object}
		 */
		self.datePickerOptions = {minDate: new Date()};

		/**
		 * Whether or not the effective date calendar for mass update is open.
		 *
		 * @type {boolean}
		 */
		self.effectiveDatePickerOpened = false;

		/**
		 * Values for the true/false select boxes tied to mass update functionality.
		 *
		 * @type {string}
		 */
		self.massUpdateBooleanValue = "true";

		/**
		 * Values for strings for mass update
		 *
		 * @type {string}
		 */
		self.massUpdateStringValue = "";

		/**
		 * The description the user has given to their mass-update.
		 *
		 * @type {null}
		 */
		self.massUpdateDescription = null;

		/**
		 * The effective date a user has chosen for a mass update to take place (only used on some of the mass updates.)
		 * @type {Date}
		 */
		self.massUpdateEffectdiveDate = new Date();

		/**
		 * The end date a user has chosen for a mass update to take place (only used on some of the mass updates.)
		 */
		self.massUpdateEndDate = new Date();

		/**
		 * This is a list of fulfillment channels.
		 * @type {Array}
		 */
		self.listOfFullfilmentChannels = [];

		/**
		 * The type of mass update the user is trying to do.
		 *
		 * @type {null}
		 */
		self.massUpdateType = null;

		/**
		 * If they've selected to mass update primo pick, the function they are going to perform.
		 *
		 * @type {null}
		 */
		self.primoPickType = null;

		/**
		 * Whether or not a mass update has been submitted.
		 *
		 * @type {boolean}
		 */
		self.submittedMassUpdate = false;

		/**
		 * The message from the mass update submission..
		 *
		 * @type {null}
		 */
		self.massUpdateTransactionMessage = null;

		/**
		 * Map to keep track of modified primo pick descriptions.
		 *
		 * @type {{}}
		 */
		self.primoPickDescriptions = {};

		/**
		 * The list of vertex tax categories.
		 *
		 * @type {null}
		 */
		self.vertexTaxCategories = null;

		/**
		 * The list of tag types.
		 *
		 * @type {null}
		 */
		self.tagTypes = null;

		/**
		 *The holds all of the possible pdp template options and the current is the selected pdp template
		 * @type (Array)
		 */
		self.pdpTemplates=[];
		self.currentPdpTemplate=null;
		/**
		 * The holds all of the possible sale channel options and the current is the selected sale channel
		 * @type {Array}
		 */
		self.salesChannels=[];
		self.currentSalesChannel = null;
		/**
		 * The holds all of the possible fulfillment channel options and the current is the selected fulfillment channel
		 * @type {Array}
		 */
		self.fulfillmentChannels = [];
		self.filteredFulfillmentChannels=[];
		self.currentFulfillmentChannels = [];

		/**
		 * Search criteria used during Add Products to Task.
		 */
		self.searchCriteria = "";
		self.switchYN=[{value: 'YES'}, {value: 'NO'}];
		self.currentSwitchValue = null;
		/**
		 * These varibles hold the selected dates
		 * @type {null}
		 */
		self.effectiveDate =null;
		self.endDate=null;

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
		 * Holds loading status for next button
		 * @type {boolean}
		 */
		self.isLoadingNextPage = false;
		/**
		 * Holds loading status for back button.
		 * @type {boolean}
		 */
		self.isLoadingPrevPage = false;
		/**
		 * Holds loading status for back button.
		 * @type {boolean}
		 */
		self.isLoadingFirstOrLastPage = false;
		/**
		 * Holds the status to search with includes count or not.
		 * It used when open product detail view, on server will holds criteria of product detail view.
		 * So when close product detail view and click on other page is different one, then results return not found.
		 * @type {boolean}
		 */
		self.isNeededResetSearchParams = false;
		self.cacheProducts = {};
		self.cacheProductLoadedRequests = {};

		/**
		 * List of Additional Columns as the export options.
		 * @type {[*]}
		 */
		self.exportOptions = [
			{code:'PRIMO_PICK', desc:'Primo Pick', checked: false},
			{code:'TAX_FLAG', desc:'Tax Flag', checked: false},
			{code:'FOOD_STAMP_FLAG', desc:'Food Stamp Flag', checked: false},
			{code:'ECOMMERCE', desc:'eCommerce', checked: false},
			{code:'SERVICE_CASE_ATTRIBUTES', desc:'Service Case Attributes', checked: false}
		];
		/**
		 * Currently displayed export option/columns.
		 * @type {*}
		 */
		self.displayExportOptions = angular.copy(self.exportOptions);

		/**
		 * Keeps track of user selected export option/columns.
		 * @type {*}
		 */
		self.selectedExportOptions = angular.copy(self.exportOptions);
		/**
		 * Need more info
		 * @type {string}
		 */
		self.NEED_MORE_INFO = "Need more info";
		/**
		 * Does not meet Primo Pick criteria
		 * @type {string}
		 */
		self.DOES_NOT_MEET_PRIMO_PICK = "Does not meet Primo Pick criteria";
		/**
		 * The selected reason code
		 *
		 * @type {String}
		 */
		self.reasonCode = self.NEED_MORE_INFO;
		/**
		 * Setting for show on site dropdown multiple select.
		 */
		$scope.showOnSiteDropdownMultiselectSettings = {
			showCheckAll: false,
			showUncheckAll: false,
			smartButtonMaxItems: 5,
			scrollableHeight: '150px',
			scrollable: true,
			checkBoxes: true
		};
		/**
		 * list of selectedSaleChannels
		 * @type {Array}
		 */
		self.selectedSaleChannels = [];
		/**
		 * Holds the list sale channels for show on site.
		 * @type {Array}
		 */
		self.showOnSiteSaleChannels = [];
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
		 * This message will show when mass update that there is no any mass update function are select.
		 * @type {string}
		 */
		self.EMPTY_MASS_UPDATE_FUNCTION_MESSAGE = 'Please select Function to Mass Update';
		/**
         * Organic nutrition code.
         * @type {String}
         */
		const ORGANIC_NUTRITION_CODE = "O95";

        /**
         * If they've selected to mass update distinctive, the function they are going to perform.
         *
         * @type {null}
         */
        self.distinctiveFunction = null;

		/**
		 * Initializes the controller.
		 */
		self.init = function() {
			// if(customHierarchyService.getReferenceUPC() !== null ||
			// 	productSearchService.getListOfProducts().length !== 0){
			// 	self.buildTable();
			// }
			//hide product infor when reload home page.
            productInfoService.hide();
			if(customHierarchyService.getReferenceUPC() !== null){
				self.buildTable();
			}
			// The only type of simple search here is by mrt, so the function handles that specifically.
			productSearchService.init(true, false, self.newMrtSearch, self.newComplexSearch);
			if(productSearchService.getSearchSelection() !== null
				&& productSearchService.getSearchType() !== null
				&& productSearchService.getSelectionType() !== null
				&& !productSearchService.getNavigateFromHomeSearchingResult()
				&& productSearchService.getFromPage() !== appConstants.HOME
                && productSearchService.getSearchType() == productSearchService.BASIC_SEARCH
			){
				//Begin search automatically
				self.startAutomaticSearch();
			}else{
				productSearchService.setDisableReturnToList(true);
				productSearchService.setSearchSelection(null);
				productSearchService.setSearchType(null);
				productSearchService.setSelectionType(null);
				productSearchService.setNavigateFromHomeSearchingResult(false);
			}
			if(permissionsService.getPermissions("MU_MENU_00", "VIEW")){
				homeApi.getAllSalesChannels({}, self.loadSalesChannels, self.fetchError);
				homeApi.getAllPDPTemplates({}, self.loadPDPTemplates, self.fetchError);
				homeApi.getAllFulfillmentPrograms({}, self.loadFulfilmentChannels, self.fetchError);
			}
			// Load sale channel for show on site of mass update.
			// In this method is just request api when current user has show on site edit permission.
			self.loadAllSalesChannels();
		};
		/**
		 * loads all of the possible sales channels
		 * @param results
		 */
		self.loadSalesChannels = function (results) {
			self.currentSalesChannel = null;
			self.listOfFullfilmentChannels = [];
			self.salesChannels=results;
		};
		/**
		 * loads all of the possible pdp templates
		 * @param results
		 */
		self.loadPDPTemplates = function (results) {
			self.pdpTemplates = results;
		};
		/**
		 * loads all of the possible fulfillment channels
		 * @param results
		 */
		self.loadFulfilmentChannels = function (results) {
			self.fulfillmentChannels = results;
		};


		/**
		 * call the search component to search product automatically
		 */
		self.startAutomaticSearch = function(){
			$timeout( function(){
				$rootScope.$broadcast('startAutomaticSearch');
			}, 800);
		};
		/**
		 * hadle when user click on product id
		 *
		 * @param product The product the user clicked on the checkbox for.
		 */
		self.handleProductSelectChanged = function(product) {
			productSearchService.setFromPage(appConstants.HOME);
			productSearchService.setDisableReturnToList(false);
			self.productSelectChanged(product);
		};
		/**
		 * Called when the user either selects or deselects a product from the table.
		 *
		 * @param product The product the user clicked on the checkbox for.
		 */
		self.productSelectChanged = function(product) {
			if (product.selected) {
				// The user selected this one, so add it to the selected list and remove it from the deselected list.
				self.selectedProudctIds.push(product.prodId);
				for (var i = 0; i < self.deselectedProductIds.length; i++) {
					if (self.deselectedProductIds[i] == product.prodId) {
						self.deselectedProductIds.splice(i, 1);
					}
				}
			} else {
				// The user has deselected this one, so add it to the deselected list and remove it
				// from the selected list.
				self.deselectedProductIds.push(product.prodId);
				for (var i = 0; i < self.selectedProudctIds.length; i++) {
					if (self.selectedProudctIds[i] == product.prodId) {
						self.selectedProudctIds.splice(i, 1);
					}
				}
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
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function()
		{
			if(self.isFromHomePageOrSpecialPages()){
				self.tableParams = new ngTableParams(
						{
							// set defaults for ng-table
							page: 1,
							count: self.DEFAULT_PAGE_SIZE,
							sorting: {PRODUCT_ID: "asc"}
						}, {

							// hide page size
							counts: [25,50,100],

							defaultSort: 'asc',

							/**
							 * Called by ngTable to load data.
							 *
							 * @param $defer The object that will be waiting for data.
							 * @param params The parameters from the table helping the function determine what data to get.
							 */
							getData: function ($defer, params) {

								self.isWaiting = true;
								self.viewingProductDetails = false;
								self.data = null;

								// Save off these parameters as they are needed by the callback when data comes back from
								// the back-end.
								self.defer = $defer;
								self.dataResolvingParams = params;

								// If this is the first time the user is running this search (clicked the search button,
								// not the next arrow), pull the counts and the first page. Every other time, it does
								// not need to search for the counts.
								var startPage = params.page();
								if(self.firstSearch){
									self.includeCounts = true;
									startPage = 1;
									self.firstSearch = false;
								} else {
									self.includeCounts = false;
								}
								if(self.isExistingCacheProducts(startPage - 1)){
                                    if(!self.hasChangePageSize(self.tableParams.count())){
                                        // loading from cache
										self.loadData(self.getCacheProducts(startPage - 1));
                                    }else {
                                        self.doComplexSearch(startPage - 1);
                                    }
								}else {
									if (self.mrtSearch) {
										self.getReportByMRT(startPage - 1);
									} else {
										self.doComplexSearch(startPage - 1);
									}
								}
							}
						}
				);
			}else{
				self.tableParams = self.buildTableProductUpdatesTask();
			}
		};

        /**
		 * Check pageSize change
         * @param pageSize
         * @returns {boolean}
         */
		self.hasChangePageSize = function(pageSize){
            if(self.pageSize != pageSize){
            	self.pageSize = pageSize;
            	return true;
            }
            return false;
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
		};

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
			self.massUpdateEndDate = new Date();
			self.submittedMassUpdate = false;
			self.massUpdateTransactionMessage = null;
			self.allSelected = false;
			self.massUpdateType = null;
			self.isLoadingNextPage = false;
			self.isLoadingPrevPage = false;
			self.isLoadingFirstOrLastPage = false;
			self.isPrevNextPageWaiting = false;
			self.firstSearch = true;
			self.showOnSiteFlag = 'true';
			productInfoService.hide();
			self.resetCacheProducts();
			//In the case return to list to attribute-> related product or variant tabs
            if(productSearchService.getToggledTab() !== null
                && (productSearchService.getToggledTab() === RELATED_PRODUCT_TAB
                    || productSearchService.getToggledTab() === VARIANTS_SUB_TAB
					|| productSearchService.getToggledTab() === VARIANTS_ITEM_SUB_TAB)){
                self.mrtSearch = false;
                self.tableParams = null;
            }
			if (self.tableParams == null || productSearchService.getFromPage() == appConstants.HOME){
            	self.buildTable();
            }else{
				self.tableParams.page(1);
				self.tableParams.reload();
            }
		};

		/**
		 * Callback for when the user uses the complex search function.
		 */
		self.doComplexSearch = function(page) {
			var search = {};
			if(customHierarchyService.getReferenceUPC() !== null) {
				search.upcs = customHierarchyService.getReferenceUPC();
			}
			// else if(productSearchService.getListOfProducts().length !== 0) {
			// 	search.productIds = productSearchService.getListOfProducts();
			// }
			else {
				search = self.searchCriteria;
			}
			search.page = page;
			search.pageSize = self.tableParams.count();
			search.firstSearch = self.isFirstSearch();
			homeApi.search(search, self.loadData, self.fetchError);
		};
		/**
		 * Returns first search status.
		 * @return {boolean}
		 */
		self.isFirstSearch = function(){
			if(self.isLoadingNextPage || self.isLoadingPrevPage || self.isNeededResetSearchParams || self.isLoadingFirstOrLastPage){
				self.isNeededResetSearchParams = false;
				return true;
			}
			return self.includeCounts;
		};
		/**
		 * Starts an export of the current search results.
		 */
		self.export = function() {
			self.downloading = true;
			var exportUrl = urlBase + "/pm/newSearch/exportToCsv?parm1=1";
			var selectedExportOptions = self.getSelectedExportOptions();
			if(selectedExportOptions) {
				exportUrl = exportUrl + '&additionalColumns='+selectedExportOptions;
			}
			var fileName = "ProductExport.csv";
			downloadService.export(exportUrl, fileName,  self.DOWNLOAD_WAIT_TIME,
				function() {self.downloading = false;})
		};

		/**
		 *  If the user has selected the tab to get data by product hierarchy, this will issue
		 *  a call to the backend to get the data.
		 * @param page The page to get.
		 */
		self.getReportByMRT = function(page) {
			switch (productSearchService.getSelectionType()) {

				case productSearchService.ITEM_CODE:
				{
					homeApi.queryByMRTItemCode({
						itemCode: productSearchService.getSearchSelection(),
						includeCounts: self.includeCounts,
						page: page,
						pageSize: self.tableParams.count()
					}, self.loadData, self.fetchError);
					break;
				}
				case productSearchService.CASE_UPC:
				{
					homeApi.queryByMRTCasePack({
						casePack: productSearchService.getSearchSelection(),
						includeCounts: self.includeCounts,
						page: page,
						pageSize: self.tableParams.count()
					}, self.loadData, self.fetchError);
					break;
				}
			}
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
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return "Result " + (self.tableParams.count() * currentPage + 1) +
				" - " + (self.tableParams.count() * currentPage + dataLength) + " of " + self.totalRecordCount;
		};

		/**
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getPageMessage = function(dataLength, currentPage, pageSize){
			return "Displaying " + (pageSize * currentPage + 1) +
			" - " + (pageSize * currentPage + dataLength) + " of " + self.totalRecordCount;
		};

		/**
		 * Callback for the request for the number of items found and not found.
		 *
		 * @param results The object returned from the backend with a list of found and not found items.
		 */
		self.showMissingData = function(results){
			self.missingValues = results;
		};



		// *****************************************************************************
		// Functions related to showing product detail information.
		// *****************************************************************************

		/**
		 * Shows the panel that contains product info information.
		 *
		 * @param productId
		 */
		self.showProductInfo = function(productId){
			productInfoService.show(self, productId, null, null);
		};

		/**
		 * Shows the main product detail panel panel that contains product detail information.
		 *
		 * @param product
		 */
		self.showProductDetails = function(product){
			self.selectedProduct = angular.copy(product);
			self.viewingProductDetails = true;
			$rootScope.$broadcast('loadItemMaster');
		};

		/**
		 * Returns whether or not the user is allowed to look at the product detail screen.
		 * @returns {boolean}
		 */
		self.canSeeProductDetails = function() {
			return permissionsService.getPermissions('GN_PSON_00', 'VIEW');
		};

		/**
		 * Returns whether or not the user is looking at the product detail screen.
		 *
		 * @returns {boolean}
		 */
		self.isViewingProductDetails = function(){
			return self.viewingProductDetails;
		};

		// *****************************************************************************
		// Callbacks
		// *****************************************************************************

		/**
		 * Callback for a successful call to get data from the backend. It requries the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results){
			self.error = null;

			self.isWaiting = false;

			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
			}

			// loading cache.
			self.setCacheProducts(results);
			// QC-3022 fix by remove load cached data.
			// if(results.data != null && results.isLoaded != true) {
			// 	self.loadCacheProducts(results.page, 1);
			// }
			if (!results.data || results.data.length === 0) {
				productSearchService.setSearchPanelVisibility(true);
				self.error = "No records found.";
				self.isPrevNextPageWaiting = false;
			} else {
				productSearchService.setSearchPanelVisibility(false);
				self.data = results.data;

				// See if we get back hits and misses
				self.missingValues = null;
				homeApi.hits({}, self.showMissingData, self.handleError);
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.pageMessage = self.getPageMessage(results.data.length, results.page, self.tableParams.count());

				// If the user had been editing primo pick approved descriptions, load the one the user has set
				for (var i = 0; i < self.data.length; i++) {
					var product = self.data[i];
					// If the user had previously set a description, load that, otherwise, if it already has one,
					// store that off in case the user just checks the box and submits.
					var editedDescription = self.primoPickDescriptions[product.prodId];
					if (editedDescription != null) {
						product.primoPickDescription = editedDescription;
					} else {
						if (product.primoPickDescription != null) {
							self.primoPickDescriptions[product.prodId] = product.primoPickDescription;
						}
					}
				}
			}
			/**
			 * results.page == 0: this page is used to sure that just only a record in db then the product detail view is auto show.
			 */
			if(results.data && results.data.length === 1 && results.page == 0){
				self.isViewingSingleRecord = true;
				if (self.canSeeProductDetails()) {
					self.showProductDetails(results.data[0]);
				} else {
					self.showProductInfo(results.data[0].prodId);
				}
			} else {

				// Loop through the data and set the selected in case the user is moving between pages for mass update
				if (self.data !== null) {
					for (var i = 0; i < self.data.length; i++) {
						// If they've checked select all, then selecte it but check to make sure it hasn't been deselected
						// already.
						if (self.allSelected) {
							self.data[i].selected = true;
							if (self.deselectedProductIds.indexOf(self.data[i].prodId) >= 0) {
								self.data[i].selected = false;
							}
						}
						// Otherwise, see if the user has selected it previously.
						else if (self.selectedProudctIds.indexOf(self.data[i].prodId) >= 0) {
							self.data[i].selected = true;
						}
					}
				}
			}

			// Return the data back to the ngTable.
			self.dataResolvingParams.total(self.totalRecordCount);
			self.defer.resolve(results.data);

			if(self.isLoadingNextPage || self.isLoadingPrevPage || self.isLoadingFirstOrLastPage){
				var selectProd = null;
				if(self.isLoadingNextPage){
					self.isLoadingNextPage = false;
					selectProd = results.data[0];
				}else if(self.isLoadingPrevPage){
					self.isLoadingPrevPage = false;
					selectProd = results.data[results.data.length-1];
				}else if(self.isLoadingFirstOrLastPage){
					self.isLoadingFirstOrLastPage = false;
					selectProd = results.data[0];
				}
				if (self.canSeeProductDetails()) {
					self.showProductDetails(selectProd);
				} else {
					self.showProductInfo(selectProd.prodId);
				}
				self.isPrevNextPageWaiting = false;
			}
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
				if(error.data.message) {
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
		 * Callback for the request of all vertex tax categories.
		 *
		 * @param data
		 */
		self.loadVertexTaxCategories = function(data) {
			self.vertexTaxCategories = data;
		};

		self.loadTagTyeps = function(data) {
			self.tagTypes = data;
		};

		/**
		 * Callback for a successful start of a mass update job.
		 *
		 * @param data The data sent from the back end.
		 */
		self.massUpdateSuccess = function(data) {
			self.massUpdateTransactionMessage = data.message;
			self.submittedMassUpdate = false;
			self.resetProductFulfillmentValues();
		};

		// *****************************************************************************
		// Mass-update related functions
		// *****************************************************************************

		/**
		 * Shows or hides the mass update part of the screen.
		 */
		self.toggleMassUpdate = function() {
			// If it's showing already, then hide it.
			if (self.isShowingMassUpdate()) {
				self.massUpdateType = null;
				return;
			}

			// Only do this if they have the menu permission.
			if (permissionsService.getPermissions('MU_MENU_00', 'VIEW')) {

				// Set the default for primo pick type. If they are an approver, the set it to approve.
				// Otherwise, to distinctive (if they don't have that permission, it won't even show up.
				if (permissionsService.getPermissions('MU_PRPK_02', 'EDIT')) {
					self.primoPickType = self.APPROVE_PRIMO_PICK;
				}
				// Based on a basically random ordering, pick the default type based on what they
				// have permission to do.
				if (permissionsService.getPermissions('MU_TRDP_01', 'EDIT')) {
					self.massUpdateType = self.THIRD_PARTY_SELLABLE;
				} else if (permissionsService.getPermissions('MU_PRORG_01', 'EDIT')) {
					self.massUpdateType = self.PRODUCE_ORGANIC;
				} else if (permissionsService.getPermissions('MU_SING_01', 'EDIT')) {
					self.massUpdateType = self.SELECT_INGREDIENTS;
				} else if (permissionsService.getPermissions('MU_TOTX_01', 'EDIT')) {
					self.massUpdateType = self.TOTALLY_TEXAS;
				} else if (permissionsService.getPermissions('MU_GOLC_01', 'EDIT')) {
					self.massUpdateType = self.GO_LOCAL;
				} else if (permissionsService.getPermissions('MU_FDST_01', 'EDIT')) {
					self.massUpdateType = self.FOOD_STAMP;
				} else if (permissionsService.getPermissions('MU_FSA_01', 'EDIT')) {
					self.massUpdateType = self.FSA;
				} else if (permissionsService.getPermissions('MU_TXFL_01', 'EDIT')) {
					self.massUpdateType = self.TAX_FLAG;
				} else if (permissionsService.getPermissions('MU_SFMF_01', 'EDIT')) {
					self.massUpdateType = self.SELF_MANUFACTURED;
				} else if (permissionsService.getPermissions('PD_SHLF_04', 'EDIT')) {
					self.massUpdateType = self.DISTINCTIVE;
				} else if (permissionsService.getPermissions('MU_PRPK_02', 'EDIT')) {
					self.massUpdateType = self.PRIMO_PICK;
				} else if (permissionsService.getPermissions('MU_TAGT_01', 'EDIT')) {
					self.massUpdateType = self.TAG_TYPE;
				} else if (permissionsService.getPermissions('CH_HIER_01', 'EDIT')) {
					self.massUpdateType = self.PDP_TEMPLATE;
				}

				// If they have permission to update tax category, go get the list of tax categories.
				if (self.vertexTaxCategories == null &&
					permissionsService.getPermissions('MU_TXCT_01', 'EDIT')) {
					homeApi.queryVertexTaxCategories({}, self.loadVertexTaxCategories, self.fetchError);
				}

				// If they have permission to update tag type, go get that list.
				if (self.tagTypes == null &&
					permissionsService.getPermissions('MU_TAGT_01', 'EDIT')) {
					homeApi.queryTagTypes({}, self.loadTagTyeps, self.fetchError);
				}
			}
		};

		/**
		 * Returns whether or not to show the mass update part of the screen.
		 *
		 * @returns {boolean}
		 */
		self.isShowingMassUpdate = function() {
			return self.massUpdateType != null;
		};

		/**
		 * Calculates the number of records that will be updated with a mass update.
		 */
		self.getMassUpdateCount = function() {

			// If they've selected all the records, send the full count - any deselected
			if (self.allSelected) {
				return self.totalRecordCount - self.deselectedProductIds.length;
			} else {
				return self.selectedProudctIds.length
			}
		};

		/**
		 * Shows the mass update dialog box.
		 */
		self.showMassUpdateModal = function() {
			// This method just check valid or not when Show on site or Primo Pick is selected or distinctive function, otherwise always return true
			if(!self.isValidShowOnSite() || !self.isValidPrimoPick() || !self.isValidDistinctive()){
				return;
			}
			$("#massUpdateModal").modal("show");
		};

		/**
		 * Called when the user closes the mass update dialog.
		 */
		self.dismissMassUpdate = function() {
			self.massUpdateTransactionMessage = null;
			$("#massUpdateModal").modal("hide");
		};

		/**
		 * Called when the user clicks on the select all check boxa on th table.
		 */
		self.selectAll = function() {

			// Set all the data to match allSelected.
			for (var i = 0; i < self.data.length; i++) {
				self.data[i].selected = self.allSelected;
			}

			// Clear out the list of selected and deselected products
			self.selectedProudctIds = [];
			self.deselectedProductIds = [];
		};

		/**
		 * Toggles the state of the effective date picker.
		 */
		self.openEffectiveDatePicker = function() {
			self.effectiveDatePickerOpened = !self.effectiveDatePickerOpened;
		};

		/**
		 * Called by the front end when the user edits a primo pick approved description.
		 *
		 * @param product The product the user is modifying.
		 */
		self.primoPickDescriptionChange = function(product) {
			self.primoPickDescriptions[product.prodId] = product.primoPickDescription;
		};

		/**
		 * Returns whether or not the user has selected products to do a mass-update one.
		 *
		 * @returns {boolean|*}
		 */
		self.hasMassUpdateProductsSelected = function() {
			return this.allSelected || this.selectedProudctIds.length > 0;
		};

		/**
		 * Kicks off a mass update job.
		 */
		self.submitMassUpdate = function() {
			self.error = '';
			if(self.currentSalesChannel != null) {
				self.loadFulfillmentChannelsForMassUpdate();
			}
			// The parameters for what to change and what to change it to.
			var massUpdateParameters = {
				attribute: self.massUpdateType,
				booleanValue: self.massUpdateBooleanValue,
				stringValue: self.massUpdateStringValue,
				description: self.massUpdateDescription,
				primoPickFunction: self.primoPickType,
                distinctiveFunction:self.distinctiveFunction,
				changeReason: self.reasonCode,
				effectiveDate: $scope.convertDate(self.massUpdateEffectdiveDate),
				endDate: $scope.convertDate(self.massUpdateEndDate),
				primoPickDescriptions: self.primoPickDescriptions,
				pdpTemplate: self.currentPdpTemplate != null ? self.currentPdpTemplate.description : null,
				productFullfilmentChanels: self.listOfFullfilmentChannels,
				selectedSaleChannels:self.selectedSaleChannels
			};

			// The parameters to find the products to change.
			var productSearchCriteria;

			if (self.allSelected) {
				productSearchCriteria =	self.searchCriteria;
				productSearchCriteria.excludedProducts = self.deselectedProductIds;
			} else {
				productSearchCriteria = {
					productIds: self.selectedProudctIds.toString()
				};
			}

			// Construct the request to send back.
			var massUpdateRequest = {
				parameters: massUpdateParameters,
				productSearchCriteria: productSearchCriteria
			};

			self.massUpdateTransactionMessage = null;
			self.submittedMassUpdate = true;
			homeApi.massUpdate(massUpdateRequest, self.massUpdateSuccess, self.fetchError);
		};

		/**
		 * This method will generate the string to represent an products fulfillment programs
		 * @param product
		 */
		self.getFullfilmentPrograms = function (product) {
			if(product.productFullfilmentChanels.length > 0){
				product.fullfilmentProgramString = product.productFullfilmentChanels[0].fulfillmentChannel.description.trim();
				for(var index = 1; index<product.productFullfilmentChanels.length; index++){
					product.fullfilmentProgramString =product.fullfilmentProgramString + ", " + product.productFullfilmentChanels[index].fulfillmentChannel.description.trim();
				}
			}
		};
		/**
		 * This method will filter out the fulfillment channels that correspond to the current selected sales channel
		 */
		self.filterFulfillmentChannels = function () {
			self.filteredFulfillmentChannels = [];
			self.currentFulfillmentChannels= [];
			self.currentSwitchValue=null;
			angular.forEach(self.fulfillmentChannels, function (fulfillmentChannel) {
				if(angular.equals(self.currentSalesChannel.id, fulfillmentChannel.key.salesChannelCode.trim())){
					self.filteredFulfillmentChannels.push(angular.copy(fulfillmentChannel));
				}
			});
		};

		/**
		 * Set the value as a date so date picker understands how to represent value.
		 */
		self.setDateForDatePicker = function () {
			self.effectiveDatePickerOpened = false;
			self.endDatePickerOpened = false;
		};

		/**
		 * Open the effective date picker to select a new date.
		 */
		self.openEffectiveDatePicker = function () {
			self.effectiveDatePickerOpened = true;
		};
		/**
		 * Open the effective date picker to select a new date.
		 */
		self.openEndDatePicker = function () {
			self.endDatePickerOpened = true;
		};

		/**
		 * This method activates both date pickers
		 */
		self.activateDatePickers = function () {
			if(self.effectiveDate ===null){
				self.effectiveDate = new Date();
				self.endDate = new Date('12-31-9999');
				self.effectiveOptions = {
					minDate: new Date(),
					initDate: self.effectiveDate
				};
				self.endOptions = {
					minDate: new Date(),
					initDate: self.endDate
				};
			}
		};


		// *****************************************************************************
		// Utility functions for the mass-update functions on the table.
		// *****************************************************************************

		/**
		 * Returns whether or not a product is marked as third-party sellable.
		 *
		 * @param product The product to look at.
		 * @returns {boolean} True if the product is third party-sellable and false otherwise.
		 */
		self.isThirdPartySellable = function(product) {
			for (var i = 0; i < product.distributionFilters.length; i++) {
				if (product.distributionFilters[i].thirdPartySellable) {
					return true;
				}
			}
			return false;
		};

		/**
		 * Checks whether or not a product has a given marketing claim code.
		 *
		 * @param product The product to look at.
		 * @param claimCode The claim code to check.
		 * @returns {boolean} True if the product has that claim code and false otherwise.
		 */
		self.checkClaimCode = function(product, claimCode) {
			for (var i = 0; i < product.productMarketingClaims.length; i++) {
				if (product.productMarketingClaims[i].key.marketingClaimCode == claimCode) {
					return true;
				}
			}
			return false;
		};

		/**
		 * Returns whether or not a product is marked as organic.
		 *
		 * @param productMaster The product to look at.
		 * @returns {boolean} True if the product is organic and false otherwise.
		 */
		self.isProduceOrganic = function(productMaster) {
			var result = false;
			angular.forEach(productMaster.sellingUnits, function (sellingUnit) {
				if (sellingUnit.nutritionalClaims != null && sellingUnit.nutritionalClaims.length >= 0) {
					for (var i = 0; i < sellingUnit.nutritionalClaims.length; i++) {
						if(sellingUnit.nutritionalClaims[i].key.nutritionalClaimsCode.trim() === ORGANIC_NUTRITION_CODE){
							result = true;
							break;
						}
					}
				}
			});
			return result;
		};

		/**
		 * Returns the primo pick status of a requested product.
		 *
		 * @param product The product to get the status for.
		 * @returns {*} A text descripiotn of the status. Null if there is no status.
		 */
		self.getPrimoPickStatus = function(product) {
			for (var i = 0; i < product.productMarketingClaims.length; i++) {
				if (product.productMarketingClaims[i].key.marketingClaimCode == self.PRIMO_PICK_CLAIM_CODE) {
					return product.productMarketingClaims[i].primoPickStatus;
				}
			}
			return null;
		};

		/**
		 * Setter for viewing product details.
		 *
		 * @param boolean Value to set for viewing product details.
		 */
		self.setViewingProductDetails = function (boolean){
			self.viewingProductDetails = boolean;
		};

		/**
		 * This loads the current switch value
		 */
		self.loadFulfillmentChannelsForMassUpdate = function() {
			self.listOfFullfilmentChannels = [];
			self.actionCode = '';
			if(self.currentSwitchValue.value === "YES") {
				self.actionCode = 'Y';
			} else if(self.currentSwitchValue.value === "NO") {
				self.actionCode = 'D';
			}
			angular.forEach(self.currentFulfillmentChannels, function (fulfillmentChannel) {
				var productFulfillmentChannel = {
					effectDate: $scope.convertDate(self.massUpdateEffectdiveDate),
					expirationDate: $scope.convertDate(self.massUpdateEndDate),
					key: {
						salesChanelCode: self.currentSalesChannel.id,
						fullfillmentChanelCode: fulfillmentChannel.key.fulfillmentChannelCode
					},
					actionCode: self.actionCode
				};
				self.listOfFullfilmentChannels.push(productFulfillmentChannel)
			});
		};

		/**
		 * Watcher for the broadcast of 'returnToProductList' that calls the setter for viewing product details with
		 * a false value so the user will be looking at the list of products instead of details on one of the products.
		 */
		$scope.$on(self.RETURN_TO_PRODUCT_LIST, function(){
			self.setViewingProductDetails(false);
		});

		/**
		 * Receives new load ecommerce task summary event. On the event, this method hides the product info and product
		 * details sections to make way for displaying ecommerce task summary section.
		 */
		$scope.$on('loadEcommerceTaskSummary', function() {
			productInfoService.hide();
			self.data = null;
			self.viewingProductDetails = false;
		});

		/**
		 * reset the values to default for product fulfillment in mass update upon mass update success
		 */
		self.resetProductFulfillmentValues = function (){
			if(!self.hasShowOnSiteEditPermission()) {
				self.massUpdateEffectdiveDate = new Date();
				self.massUpdateEndDate = new Date();
			}
			self.currentSalesChannel = null;
			self.currentSwitchValue = null;
			self.actionCode = null;
			self.massUpdateDescription = null;
			self.currentFulfillmentChannels = [];
		};

		/**
		 * Backup search condition product  for ecommerce View
		 */
		self.navigateToProductInfo= function(productId) {
			//Set search condition
			self.isWaiting = true;
			//Backup searching criteria before navigated
            productSearchService.setSearchSelectionBackup(productSearchService.getSearchSelection());
            productSearchService.setSearchTypeBackup(productSearchService.getSearchType());
            productSearchService.setSelectionTypeBackup(productSearchService.getSelectionType());
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(productId);
			productSearchService.setNavigateFromHomeSearchingResult(true);
			productSearchService.setListOfProducts(self.data);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(PRODUCT_INFO_TAB);
			//Set from page navigated from
			productSearchService.setFromPage(appConstants.HOME);
			if(self.totalRecordCount !== null && self.totalRecordCount > 1){
				productSearchService.setDisableReturnToList(false);
			}
			$timeout( function(){
				self.viewProductDetail(productId);
			}, 500);
		};
		/**
		 * View product detail.
		 */
		self.viewProductDetail = function(prodId) {
            productDetailApi.getUpdatedProduct(
			{prodId: prodId}, function(results){
				if (self.canSeeProductDetails()) {
					self.showProductDetails(results);
				} else {
					self.showProductInfo(results.prodId);
				}
				self.isWaiting = false;
			}, function(error){
				self.isWaiting = false;
				if (error && error.data) {
					if(error.data.message) {
						self.setError(error.data.message);
					} else {
						self.setError(error.data.error);
					}
				}
				else {
					self.setError("An unknown error occurred.");
				}
			});
			if(permissionsService.getPermissions("MU_MENU_00", "VIEW")){
				homeApi.getAllSalesChannels({}, self.loadSalesChannels, self.fetchError);
				homeApi.getAllPDPTemplates({}, self.loadPDPTemplates, self.fetchError);
				homeApi.getAllFulfillmentPrograms({}, self.loadFulfilmentChannels, self.fetchError);
			}
		};

		/**
		 * Resets Mass update selected values to default values.
		 */
		self.resetMassUpdateValues = function() {
			self.massUpdateBooleanValue = "true";
			self.massUpdateStringValue = "";
			self.massUpdateEffectdiveDate = new Date();
			self.massUpdateEndDate = new Date();
			self.massUpdateDescription = null;
			self.filteredFulfillmentChannels=[];
			self.currentFulfillmentChannels = [];
			self.currentSalesChannel = null;
			self.currentPdpTemplate=null;
			self.primoPickType = null;
			self.reasonCode = null;
			self.currentSwitchValue = null;
			self.selectedSaleChannels = [];
			// Reset effective date and end date for show on site
			self.onChangedShowOnSiteFlag();
		};
		/**
		 * Get product master by product id.
		 */
		self.getProductMasterByProductId = function(productId) {
			productDetailApi.getUpdatedProduct(
					{prodId: productId},
					function (results) {
						self.isWaiting = false;
						if (self.canSeeProductDetails()) {
							self.showProductDetails(results);
						} else {
							self.showProductInfo(results.prodId);
						}
					}, self.fetchError);
		};
		/**
		 * Callback for a successful call to get data from the backend. It requries the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadDataFromCacheProducts = function(results){
			self.error = null;
			//self.isWaiting = false;
			var first = false;
			var index = 0;

			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
				//data is saved in cache, no neeed to call server to get data if return this page
				results.complete = false;
				first = true;
			}

			if(self.reloadCurrentPageAfterPublish){//reload current page after publish product
				index = self.findIndexOfProductId(self.data, self.selectedProduct.prodId);
				if(self.reloadPreviousPageAfterPublish){
					if(index == 0) {
						//productId is the first and last row of listOfProducts => navigate to last productId of previous page
						index = results.data.length - 1;
					}else{//productId is the last row of listOfProducts => navigate to previous productId of current page
						index = index - 1;
					}
				}
			}

			//Save product list to cache.
			self.setCacheProducts(results);

			if (results.data && results.data.length > 0) {
				productSearchService.setSearchPanelVisibility(false);
				self.data = self.getProductMasterList(results.data);
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.pageMessage = self.getPageMessage(results.data.length, results.page, productSearchService.productUpdatesTaskCriteria.pageSize);
				if (first) {
					productSearchService.setListOfProducts(results.data);
				}
			}

			//Return the data back to the ngTable.
			self.dataResolvingParams.total(self.totalRecordCount);
			self.defer.resolve(results.data);

			if (first) {
				if(self.reloadCurrentPageAfterPublish){
					//reload current page after publish product
					self.reloadCurrentPageAfterPublish = false;
					self.reloadPreviousPageAfterPublish = false;
					var selectProd = results.data[index].productMaster;
					self.getProductMasterByProductId(selectProd.prodId);
					self.isPrevNextPageWaiting = false;
				}else{
					//Other page navigate to Home page first time
					self.getProductMasterByProductId(self.searchCriteria.productIds);
				}
			}

			if(self.isLoadingNextPage || self.isLoadingPrevPage || self.isLoadingFirstOrLastPage){
				//when user click on next, back, first, last button
				var selectProd = null;
				if(self.isLoadingNextPage){
					self.isLoadingNextPage = false;
					selectProd = results.data[0].productMaster;
				}else if(self.isLoadingPrevPage){
					self.isLoadingPrevPage = false;
					selectProd = results.data[results.data.length-1].productMaster;
				}else if(self.isLoadingFirstOrLastPage){
					self.isLoadingFirstOrLastPage = false;
					selectProd = results.data[0].productMaster;
				}
				self.getProductMasterByProductId(selectProd.prodId);
				self.isPrevNextPageWaiting = false;
			}
		};
		/**
		 * Get product master list.
		 */
		self.findIndexOfProductId = function(data, productId){
			var index = 0;
			angular.forEach(data, function (item, key) {
				if(item.prodId === productId){
					index = key;
				}
			});
			return index;
		};
		/**
		 * Get product master list.
		 */
		self.getProductMasterList = function(data){
			var list = [];
			angular.forEach(data, function (item) {
				if(item.productMaster){
					list.push(item.productMaster);
				}
			});
			return list;
		};
		/**
		 * Get table data.
		 */
		self.getNavigateTableData = function(startPage){
			switch (productSearchService.getFromPage()) {
				case appConstants.PRODUCT_UPDATES_TASK:
				{
					productUpdatesTaskApi.getAllProducts(
							{
								alertType: productSearchService.productUpdatesTaskCriteria.alertType,
								assignee: productSearchService.productUpdatesTaskCriteria.assignee,
								attributes: productSearchService.productUpdatesTaskCriteria.attributes,
								showOnSite: productSearchService.productUpdatesTaskCriteria.showOnSite,
								salesChannelsFilter: productSearchService.productUpdatesTaskCriteria.salesChannelsFilter,
								includeCounts: self.includeCounts,
								page : startPage - 1,
								pageSize : productSearchService.productUpdatesTaskCriteria.pageSize
							}, self.loadDataFromCacheProducts, self.fetchError);
					break;
				}
				case appConstants.NUTRITION_UPDATES_TASK: {
					productSearchService.productUpdatesTaskCriteria.searchCriteria.page = startPage - 1;
					productSearchService.productUpdatesTaskCriteria.searchCriteria.firstSearch = self.includeCounts;
					nutritionUpdatesTaskApi.searchActiveNutritionUpdates(productSearchService.productUpdatesTaskCriteria.searchCriteria,
						self.loadDataFromCacheProducts, self.fetchError);
					break;
				}
				case appConstants.ECOMMERCE_TASK:
				{
					ecommerceTaskApi.getTaskProducts(
							{
								trackingId: productSearchService.productUpdatesTaskCriteria.trackingId,
								assignee: productSearchService.productUpdatesTaskCriteria.assignee,
								showOnSite: productSearchService.productUpdatesTaskCriteria.showOnSite,
								salesChannelsFilter: productSearchService.productUpdatesTaskCriteria.salesChannelsFilter,
								includeCounts: self.includeCounts,
								page : startPage - 1,
								pageSize : productSearchService.productUpdatesTaskCriteria.pageSize
							}, self.loadDataFromCacheProducts, self.fetchError);
					break;
				}
				case appConstants.CUSTOM_HIERARCHY_ADMIN:
				{
					if(productSearchService.productUpdatesTaskCriteria.searchType === "PRODUCT_GROUP") {
						customHierarchyApi.findAllCustomerProductGroupsByParent(
								{
									hierarchyParentId : productSearchService.productUpdatesTaskCriteria.hierarchyParentId,
									hierarchyContext : productSearchService.productUpdatesTaskCriteria.hierarchyContext,
									firstSearch: self.includeCounts,
									page : startPage - 1,
									pageSize : productSearchService.productUpdatesTaskCriteria.pageSize
								}, self.loadDataFromCacheProducts, self.fetchError);
					} else if(productSearchService.productUpdatesTaskCriteria.searchType === "PRODUCT") {
						customHierarchyApi.findProductsByParent(
								{
									hierarchyParentId : productSearchService.productUpdatesTaskCriteria.hierarchyParentId,
									hierarchyContext : productSearchService.productUpdatesTaskCriteria.hierarchyContext,
									firstSearch: self.includeCounts,
									page : startPage - 1,
									pageSize : productSearchService.productUpdatesTaskCriteria.pageSize
								}, self.loadDataFromCacheProducts, self.fetchError);
					}
					break;
				}
			}
		};
		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTableProductUpdatesTask = function () {
			return new ngTableParams(
					{
						page: productSearchService.productUpdatesTaskCriteria.pageIndex,
						count: productSearchService.productUpdatesTaskCriteria.pageSize
					}, {
						counts: [25,50,100],
						getData: function ($defer, params) {
							// Save off these parameters as they are needed by the callback when data comes back from
							// the back-end.
							self.defer = $defer;
							self.dataResolvingParams = params;

							// If this is the first time the user is running this search (clicked the search button,
							// not the next arrow), pull the counts and the first page. Every other time, it does
							// not need to search for the counts.
							var startPage = params.page();
							if(self.reloadCurrentPageAfterPublish){//reload current page after publish product
								self.isPrevNextPageWaiting = true;
								self.includeCounts = true;
								self.getNavigateTableData(startPage);
							}else{
								if(self.firstSearch){
									self.isWaiting = true;
									self.viewingProductDetails = false;
									self.data = null;

									self.includeCounts = true;
									self.firstSearch = false;
								} else {
									self.includeCounts = false;
								}
								if(self.isExistingCacheProducts(startPage - 1)){
                                    if(!self.hasChangePageSize(self.tableParams.count())){
                                        // loading from cache
                                        self.loadDataFromCacheProducts(self.getCacheProducts(startPage - 1));
                                    }else {
                                        self.getNavigateTableData(startPage);
                                    }
								}else {
									self.getNavigateTableData(startPage);
								}
							}
						}
					}
				)
		};
		/**
		 * Handles the next page when user click on next button of product detail header.
		 */
		$scope.$on('nextPage', function() {
			if(self.tableParams.page()< self.totalPages) {
				self.isLoadingNextPage = true;
				self.isLoadingPrevPage = false;
				self.isPrevNextPageWaiting = true;
				self.tableParams.page(self.tableParams.page()+1);
			}
		});
		/**
		 * Handles the back page when user click on back button of product detail header.
		 */
		$scope.$on('backPage', function() {
			if(self.tableParams.page() > 0) {
				self.isLoadingNextPage = false;
				self.isLoadingPrevPage = true;
				self.isPrevNextPageWaiting = true;
				self.tableParams.page(self.tableParams.page()-1);
			}
		});
		/**
		 * Handles the next page when user click on next button of product detail header.
		 */
		$scope.$on('lastPage', function() {
			if(self.tableParams.page()< self.totalPages) {
				self.isLoadingFirstOrLastPage = true;
				self.isPrevNextPageWaiting = true;
				self.tableParams.page(self.totalPages);
			}
		});
		/**
		 * Handles the back page when user click on back button of product detail header.
		 */
		$scope.$on('firstPage', function() {
			if(self.tableParams.page() > 0) {
				self.isLoadingFirstOrLastPage = true;
				self.isPrevNextPageWaiting = true;
				self.tableParams.page(1);
			}
		});
		/**
		 * Handles the next page when user click on next button of product detail header.
		 */
		$scope.$on('reloadCurrentPageAfterPublish', function() {
			self.reloadCurrentPageAfterPublish = true;
			self.reloadPreviousPageAfterPublish = false;
			//reload current page
			self.tableParams.reload();
		});
		/**
		 * Handles the next page when user click on next button of product detail header.
		 */
		$scope.$on('reloadPreviousPageAfterPublish', function() {
			var currentIndex = self.findIndexOfProductId(self.data, self.selectedProduct.prodId);
			if(currentIndex == 0) {
				//productId is the first and last row of listOfProducts => navigate to last productId of previous page
				if(self.tableParams.page() > 1) {
					self.reloadCurrentPageAfterPublish = true;
					self.reloadPreviousPageAfterPublish = true;
					self.tableParams.page(self.tableParams.page()-1);
				}
			}else{//productId is the last row of listOfProducts => navigate to previous productId of current page
				self.reloadCurrentPageAfterPublish = true;
				self.reloadPreviousPageAfterPublish = true;
				//reload current page
				self.tableParams.reload();
			}
		});
		/**
		 * Reset cache products and status to load cache.
		 */
		self.resetCacheProducts = function(){
			self.cacheProducts = {};
			self.cacheProductLoadedRequests = {};
		};
		/**
		 * Set cache product.
		 * @param results the list of products.
		 */
		self.setCacheProducts = function(results){
			self.cacheProducts[''+results.page] = results;
		};
		/**
		 * Get the list of products in cache by page.
		 * @param page the needs to get product.
		 * @return {*} the list of products.
		 */
		self.getCacheProducts = function(page){
			var results =  self.cacheProducts[''+page];
			if(results != null && results != undefined) {
				results.isLoaded = true;
			}
			return results;
		};
		/**
		 * Return true if the product of page is existing in cache.
		 * @param page the page needs to check.
		 * @return {boolean} true existing in cache.
		 */
		self.isExistingCacheProducts = function(page){
			var results = self.getCacheProducts(page);
			return results !== null && results !== undefined;
		};
		/**
		 * Load cache product by page.
		 * @param page the page needs to cache.
		 * @param numberOfNeedsLoadingPage number of pages need to cache.
		 */
		self.loadCacheProducts = function(page, numberOfNeedsLoadingPage){
			if(self.totalPages > 1) {
				var leftAndRightPagesNeedToLoadCache = self.getLeftAndRightPagesNeedToLoadCache(page, numberOfNeedsLoadingPage);
				for (var i = 0; i < leftAndRightPagesNeedToLoadCache.length; i++){
					self.loadProducts(leftAndRightPagesNeedToLoadCache[i]);
				}
			}
		};
		/**
		 * Finds product from server.
		 * @param page the page needs to cache.
		 */
		self.loadProducts = function(page){
			if (self.isExistingCacheProducts(page) || page > self.totalPages - 1 || page <= 0 || self.cacheProductLoadedRequests[''+page]) {
				return;
			}
			self.cacheProductLoadedRequests[''+page] = true;
			if(self.mrtSearch){
				switch (productSearchService.getSelectionType()) {
					case productSearchService.ITEM_CODE:
					{
						homeApi.queryByMRTItemCode({
							itemCode: productSearchService.getSearchSelection(),
							includeCounts: true,
							page: page,
							pageSize: self.tableParams.count()
						}, function (results) {
							results.complete = results.page == 0?true:false;
							self.setCacheProducts(results);
						}, function (error) {
							self.cacheProductLoadedRequests[''+page] = false;
						});
						break;
					}
					case productSearchService.CASE_UPC:
					{
						homeApi.queryByMRTCasePack({
							casePack: productSearchService.getSearchSelection(),
							includeCounts: true,
							page: page,
							pageSize: self.tableParams.count()
						}, function (results) {
							results.complete = results.page == 0?true:false;
							self.setCacheProducts(results);
						}, function (error) {
							self.cacheProductLoadedRequests[''+page] = false;
						});
						break;
					}
				}
			}else {
				var search = {};
				if (customHierarchyService.getReferenceUPC() !== null) {
					search.upcs = customHierarchyService.getReferenceUPC();
				}
				else {
					search = angular.copy(self.searchCriteria);
				}
				search.page = page;
				search.pageSize = self.tableParams.count();
				search.firstSearch = true;
				homeApi.search(search, function (results) {
					results.complete = results.page == 0?true:false;
					self.setCacheProducts(results);
				}, function (error) {
					self.cacheProductLoadedRequests[''+search.page] = false;
				});
			}
		};
		/**
		 * Get the left and right pages of product needs to cache.
		 * @param currentPage current page
		 * @param numberOfNeedsLoadingPage number of pages need to cache.
		 * @return {Array}
		 */
		self.getLeftAndRightPagesNeedToLoadCache = function(currentPage, numberOfNeedsLoadingPage){
			var pages = [];
			var numberOfAddedPages = 0;
			var startPage = currentPage - 1;
			// Get left page for cache
			for (var i = startPage; i > 1; i--) {
				if (self.cacheProductLoadedRequests['' + i] !== true) {
					pages.push(i);
					numberOfAddedPages++;
				}
				if (numberOfNeedsLoadingPage == numberOfAddedPages) {
					break;
				}
			}
			// Get right page for cache
			numberOfAddedPages = 0;
			startPage = currentPage + 1;
			for (var i = startPage; i < self.totalPages; i++){
				if (self.cacheProductLoadedRequests['' + i] !== true) {
					pages.push(i);
					numberOfAddedPages++;
				}
				if (numberOfNeedsLoadingPage == numberOfAddedPages) {
					break;
				}
			}
			return pages;
		};
		/**
		 * Listens next/pre event from product detail.
		 */
		$scope.$on('loadCacheProducts', function() {
			self.loadCacheProducts(self.tableParams.page() - 1, 1);
		});
		/**
		 * Handles applying export option choice and intiates downloading/exporting of the results.
		 */
		self.exportWithSelectedOptions = function() {
			self.selectedExportOptions = angular.copy(self.displayExportOptions);
			self.export();
			$('#chooseExportOptions').removeClass("open");
		};

		/**
		 * Resets export option selections to initial state.
		 */
		self.resetExportOptionSelection = function() {
			self.displayExportOptions = angular.copy(self.exportOptions);
			self.selectedExportOptions = angular.copy(self.exportOptions);
		};

		/**
		 * Cancels export option selections. Changes displayed options state to previously saved state.
		 */
		self.cancelExportOptionSelection = function() {
			self.displayExportOptions = angular.copy(self.selectedExportOptions);
			$('#chooseExportOptions').removeClass("open");
		};

		/**
		 * Used to build list of selected export options as a CSV value. If user haven't made any selection, it
		 * returns null.
		 */
		self.getSelectedExportOptions = function() {
			var selectedExpOptions = _.pluck(_.filter(self.displayExportOptions, function(o){ return o.checked == true;}),'code');
			return (selectedExpOptions.length > 0) ? selectedExpOptions.join() : null;
		};
		/**
		 * Show my attribute.
		 */
		self.findMyAttribute = function(){
			myAttributeFactory.showModal();
		};
		/**
		 * Check navigation from page is Home page or special pages to build correct ngTableParams.
		 */
		self.isFromHomePageOrSpecialPages = function(){
			if (productSearchService.getFromPage() == appConstants.HOME
					|| productSearchService.getFromPage() == appConstants.PRODUCT_GROUP){
				return true;
			}
			return false;
		};
		/**
		 * Returns true if current user has Show on Site edit permission.
		 * @returns {boolean|*} true or false.
		 */
		self.hasShowOnSiteEditPermission = function(){
			return permissionsService.getPermissions(SHOW_ON_SITE_RESOURCE_ID, "EDIT");
		};
		/**
		 * Load all Sales Channels from back end.
		 */
		self.loadAllSalesChannels = function(){
			if(self.hasShowOnSiteEditPermission())
			{
				homeApi.getAllSalesChannels({}, self.loadShowOnSiteSalesChannels, self.fetchError);
			}
		};
		/**
		 * This is the callback method that loads all of the Sales Channels received from the back end.
		 *
		 * @param results List of all Sales Channels.
		 */
		 self.loadShowOnSiteSalesChannels = function(results){
		 	var items = [];
			 results.forEach(function(item) {
			 	// Remove google sale channel and store sale channel on the list of sale channels of Show on site.
				 if (item.id.trim() != GOOGLE_SALE_CHANNEL && item.id.trim() != STORE_SALE_CHANNEL) {
				 		item.label = item.description.trim();
				 		items.push(item);
			 		}
				 }
			 );
			self.showOnSiteSaleChannels = angular.copy(items);
		};
		/**
		 * To reset effectiveDate and and date when mass update dropdown list is selecting show on site option
		 * and show on site flag changing.
		 */
		self.onChangedShowOnSiteFlag = function(){
			if(self.hasShowOnSiteEditPermission()) {
				var currentDate = new Date();
				self.massUpdateEffectdiveDate = currentDate;
				if(self.massUpdateBooleanValue == 'true') {
					// Show on site flag == Yes
					self.massUpdateEndDate = new Date(MAX_END_DATE);
				}else{
					// Show on site flag == No
					self.massUpdateEndDate = currentDate;
				}
				self.effectiveOptions = {
					minDate: currentDate,
					initDate: self.massUpdateEffectdiveDate
				};
				self.endOptions = {
					minDate: currentDate,
					initDate: self.massUpdateEndDate
				};
			}
		};
		/**
		 * Used to show the status of show on site based on the show on site data for HEB.Com. It is assumed the default
		 * sales channel for product is HEB.com
		 * @param productOnlines the list of product onlines.
		 * @returns {*} true or false based on HEB sale channel and it's expiration date.
		 */
		self.displayShowOnSite = function (productOnlines) {
			// filter heb sale channel get it's expiration date based on showOnSiteExpirationDate.
			return _.pluck(_.filter(productOnlines,
				{key :{'saleChannelCode': HEB_COM_SALES_CHANNEL}}), 'showOnSiteByExpirationDate')[0];
		};
		/**
		 * This method is used to for show on site mass update, otherwise it always return true.
		 * Validate sale channel is empty.
		 * @returns {boolean} true if sale channel and end date are true or ignore other type that without show on site.
		 */
		self.isValidShowOnSite = function(){
			self.error = '';
			if (self.massUpdateType == self.SHOW_ON_SITE && (self.selectedSaleChannels == null || self.selectedSaleChannels.length == 0)) {
				self.error = self.EMPTY_SALE_CHANNEL_MESSAGE;
				return false;
			}
			return true;
		};
		/**
		 * This method is used to for primo pick mass update, otherwise it always return true.
		 * Validate primo pick type is empty.
		 * @returns {boolean}.
		 */
		self.isValidPrimoPick = function(){
			self.error = '';
			if (self.massUpdateType == self.PRIMO_PICK && (self.primoPickType == undefined || self.primoPickType == null || self.primoPickType == "")) {
				self.error = self.EMPTY_MASS_UPDATE_FUNCTION_MESSAGE;
				return false;
			}
			return true;
		};

        /**
         * This method is used to for primo pick mass update, otherwise it always return true.
         * Validate primo pick type is empty.
         * @returns {boolean}.
         */
        self.isValidDistinctive = function(){
            self.error = '';
            if (self.massUpdateType == self.DISTINCTIVE && (self.distinctiveFunction == undefined || self.distinctiveFunction == null || self.distinctiveFunction == "")) {
                self.error = self.EMPTY_MASS_UPDATE_FUNCTION_MESSAGE;
                return false;
            }
            return true;
        };
	}
})();
