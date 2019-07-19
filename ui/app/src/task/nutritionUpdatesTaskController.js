/*
 * nutritionUpdatesTaskController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to handle lifecycle of nutrition updates task screen and handle its associated functions.
 *
 * @author vn40486
 * @since 2.11.0
 */
(function(){
	angular.module('productMaintenanceUiApp')
		.controller('NutritionUpdatesTaskController', nutritionUpdatesTaskController);
	nutritionUpdatesTaskController.$inject = ['$scope', '$location', 'NutritionUpdatesTaskApi', 'ngTableParams', 'ProductSearchService', '$state',
		'appConstants', 'DownloadService', 'urlBase','taskService'];

	/**
	 * TaskSummaryController definition.
	 * @param taskSummaryApi api used for data server communication.
	 */
	function nutritionUpdatesTaskController($scope, $location, nutritionUpdatesTaskApi, ngTableParams, productSearchService, $state,
			appConstants, downloadService, urlBase,taskService) {
		var self = this;
		/**
		 * Whether or not the search search panel is collapsed or open.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		/**
		 * Denotes whether back end should fetch total counts or not. Accordingly server side will decide on executing
		 * additional logic to fetch count information.
		 *
		 * @type {boolean}
		 */
		self.includeCounts = true;
		/**
		 * check if check selection is null
		 */
		self.searchSelectionIsNull = true;
		/**
		 * The number of rows per page constant.
		 * @type {int}
		 */
		self.DEFAULT_PAGE_SIZE = 25;
		/**
		 * The number of rows per page constant when navigate to Home page.
		 * @type {int}
		 */
		self.NAVIGATE_PAGE_SIZE = 100;

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
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;

		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */

		self.dataResolvingParams = null;
		/**
		 * The promise given to the ng-tables getData method. It should be called after data is fetched.
		 *
		 * @type {null}
		 */
		self.defer = null;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		$scope.data = null;

		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * Keeps track of selected selected Alerts/Rows.
		 * @type {Array}
		 */
		self.selectedAlerts = [];

		/**
		 * Keeps track of excluded Alerts/Rows when "Select All" is checked.
		 * @type {Array}
		 */
		self.excludedAlerts = [];

		/**
		 * maintains state of data call.
		 * @type {boolean}
		 */
		self.firstFetch = true;

		/**
		 * Represents state of Select All check box.
		 */
		self.allAlerts = false;
		/**
		 * Initialize the default value for selectionType
		 * @type {String}
		 */
		const UPC = "UPC";
		const ITEM = "Item Code";
		const PRODUCT_ID = "Product ID";
		/**
		 * Initialize constant variable ALERT_ID
		 * @type {String}
		 */
		const ALERT_ID='alertID';
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
		 * Nutrition Fact tab
		 * @type {String}
		 */
		const NUTRITION_FACT_TAB = 'nutritionFactsTab';

		/**
		 * Initialize the default value for searchCriteria
		 */
		self.searchCriteria = null;
		/**
		 * Initialize the controller.
		 */
		self.init = function(){
            self.selectionType = UPC;
            self.searchSelection = null;
            self.searchPanelVisible = true;
            if(taskService.getReturnToListFlag() && (productSearchService.productUpdatesTaskCriteria !== undefined
                && productSearchService.productUpdatesTaskCriteria !== null
                && productSearchService.productUpdatesTaskCriteria.searchCriteria !== undefined
            	&& productSearchService.productUpdatesTaskCriteria.searchCriteria!==null)){
                if(productSearchService.productUpdatesTaskCriteria.searchCriteria.upcs != null){
                    self.searchSelectionIsNull =false;
                    self.selectionType = UPC;
                    self.searchSelection = productSearchService.productUpdatesTaskCriteria.searchCriteria.upcs;
                }else if(productSearchService.productUpdatesTaskCriteria.searchCriteria.productIds != null){
                    self.searchSelectionIsNull =false;
                    self.selectionType = PRODUCT_ID;
                    self.searchSelection = productSearchService.productUpdatesTaskCriteria.searchCriteria.productIds;
                }else if(productSearchService.productUpdatesTaskCriteria.searchCriteria.itemCodes != null){
                    self.searchSelectionIsNull =false;
                    self.selectionType = ITEM;
                    self.searchSelection = productSearchService.productUpdatesTaskCriteria.searchCriteria.itemCodes;
                }
                self.findAll = false;
                productSearchService.productUpdatesTaskCriteria = null;
            }else {
                self.searchSelectionIsNull =true;
                self.findAll = true;
            }
            taskService.setReturnToListFlag(false);
		};
		/**
		 * Check if searchSelection is null
		 */
		self.checkIfSearchSelectionIsNull = function () {
            self.searchSelectionIsNull = self.searchSelection == '' ? true : false;
		};
		/**
		 * Clears the search box.
		 */
		self.clearSearch = function () {
			self.searchSelectionIsNull = true;
			self.searchSelection = null;
			self.message = null ;
		};
		/**
		 * Issue call to newSearch to call back end to fetch all nutrition task
		 */
		self.searchAll = function (){
			self.searchSelection = null;
			self.newSearch(true);
		};
		/**
		 * Returns the text to display in the search box when it is empty.
		 *
		 * @returns {string} The text to display in the search box when it is empty.
		 */
		self.getTextPlaceHolder = function(){
			return 'Enter ' + self.selectionType + 's to Search For'
		};
		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.message = null ;
			self.findAll = findAll;
			self.firstFetch = true;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Constructs the ng-table based data table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.DEFAULT_PAGE_SIZE
				}, {
					counts: [25,50,100],
					getData: function ($defer, params) {
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						if (self.firstFetch) {
							self.includeCounts = true;
							self.firstFetch = false;
						}else {self.includeCounts = false;}
                        self.searchActiveNutritionUpdates(params.page() - 1);
					}
				}
			)
		};

		/**
		 * Fetches all the nutrition updates from database with pagination.
		 * @param page  selected page number.
		 */
		self.searchActiveNutritionUpdates = function(page) {
			self.checkHits = false;
			self.missingValues = null;
			self.searchCriteria = {
				firstSearch: self.includeCounts,
				page : page,
				pageSize : self.tableParams.count()
			};
			if (self.findAll) {
				// search all do not call Hits
				self.checkHits = true;
			} else {
				if (self.selectionType == UPC) {
					self.searchCriteria.upcs = self.searchSelection;
				} else if (self.selectionType == ITEM) {
					self.searchCriteria.itemCodes = self.searchSelection;
				} else if (self.selectionType == PRODUCT_ID) {
					self.searchCriteria.productIds = self.searchSelection;
				}
			}
			nutritionUpdatesTaskApi.searchActiveNutritionUpdates(self.searchCriteria,
						self.loadData,self.handleError);
		};

		/**
		 * Ng-table params variable for maintaining data table configurations.
		 */
		self.tableParams = self.buildTable();

		/**
		 * Callback for a successful call to get data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
				self.firstFetch = false;
			}
			self.resetAlertSelection(true);
			self.resultMessage = self.getResultMessage(results.data.length, self.tableParams.page() -1);
			self.renderUserSelection(results.data, self.allAlerts, self.selectedAlerts, self.excludedAlerts);
			self.alertStagings = results.data;
			self.defer.resolve(results.data);
			if (results.data && results.data.length === 0) {
				self.alertStagings = null;
				self.isNoRecordsFound = true;
			}else {
				// See if we get back hits and misses
				if(!self.checkHits){
					nutritionUpdatesTaskApi.hits({}, self.showMissingData, self.handleError);
				}
				self.error = null;
				self.alertStagings = results.data;
				self.isNoRecordsFound = false;
			}

			self.isWaiting = false;
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
		 * Used to maintain the state previous selections by the user.
		 * @param alertsDataArr	new list of data fetched from backend.
		 * @param allAlertsChecked is select all checked; true/false.
		 * @param selectedAlerts list of selected alert ids.
		 * @param excludedAlerts list of excluded alerts.
		 */
		self.renderUserSelection = function(alertsDataArr, allAlertsChecked, selectedAlerts, excludedAlerts) {
			if (allAlertsChecked) {
				_.forEach(alertsDataArr, function (alert) {
					var index = _.findIndex(excludedAlerts, function(o) { return o == alert.alertID;});
                    alert.checked = index > -1 ? false : true;
				});
			} else {
				_.forEach(alertsDataArr, function (alert) {
					var index = _.findIndex(selectedAlerts, function(o) { return o == alert.alertID;});
                    alert.checked = index > -1 ? true : false;
				});
			}
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
			return "" + (self.tableParams.count() * currentPage + 1) +
				" - " + (self.tableParams.count() * currentPage + dataLength) + "  of  " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Used to reload table data.
		 */
		self.refreshTable = function (page, clearMsg) {
			if(clearMsg) {self.clearMessage();}
			self.firstFetch = true;
			self.resetAlertSelection(true);
			self.tableParams.page(page);
			self.tableParams.reload();
		};

		/**
		 * Resets all previous row selections when "Select All" is checked/unchecked.
		 */
		self.resetAlertSelection = function(includeSelectAll) {
			if (includeSelectAll) {
				self.allAlerts = false;
			}
			self.selectedAlerts = [];
			self.excludedAlerts = [];
		};

		/**
		 * Keeps track of row selection and exclusions in consideration to the state of "Select All" option.
		 *
		 * @param alertChecked selected row state. True/False.
		 * @param alert	data (Alert) object of the row that was modified.
		 */
		self.toggleAlertSelection = function(alert) {
			if (self.allAlerts) {//is "Select All" checked?
				!alert.checked ? self.excludedAlerts.push(alert.alertID) : _.pull(self.excludedAlerts, alert.alertID);
			} else {
				alert.checked ? self.selectedAlerts.push(alert.alertID) : _.pull(self.selectedAlerts, alert.alertID)
			}
		};

		/**
		 * Handle select alert to reject.
		 */
		self.handleRejectAlert = function (){
			if((self.allAlerts && self.excludedAlerts.length < self.totalRecordCount)||(self.selectedAlerts.length > 0)){
				$('#rejectReasonModal').modal({ backdrop: 'static', keyboard: true });
			}else {
				$('#confirmSelectAlertModal').modal({ backdrop: 'static', keyboard: true });
			}
		};

		/**
		 * Send alert id to back end to be deleted.
		 *
		 * @param index Index of data to be removed.
		 */
		self.doRejectAlert = function(rejectReason){
			self.clearMessage();
			var data = {};
			if (self.allAlerts) {
				self.isWaiting = true;
				//var searchCriteriaRejectAll={};

				var massFillData = {
					isSelectAll: self.allAlerts,
					selectedAlertStaging:self.selectedAlerts,
					searchCriteria: {},
					excludedAlerts:self.excludedAlerts,
					description:rejectReason
				};
				if (self.selectionType == UPC) {
					massFillData.searchCriteria.upcs = self.searchSelection;
				} else if (self.selectionType == ITEM) {
					massFillData.searchCriteria.itemCodes = self.searchSelection;
				} else if (self.selectionType == PRODUCT_ID) {
					massFillData.searchCriteria.productIds = self.searchSelection;
				}

				nutritionUpdatesTaskApi.rejectAllUpdates(massFillData,self.handleSuccess,self.handleError);
			} else {
				self.isWaiting = true;
				data = {'rejectReason' : rejectReason,'alertIds' : self.selectedAlerts};
				nutritionUpdatesTaskApi.rejectUpdates(data,self.handleSuccess,self.handleError);
			}
		};

		/**
		 * Backup search condition product  for ecommerce View
		 */
		self.navigateToNutritionFact = function(productId, alertId, productPosition) {
			//Set search condition
			productSearchService.setSearchType(SEARCH_TYPE);
			productSearchService.setSelectionType(SELECTION_TYPE);
			productSearchService.setSearchSelection(parseFloat(productId));
			productSearchService.setListOfProducts(self.alertStagings);
			//Backup alert id
			productSearchService.setAlertId(alertId);
			//Set selected tab is ecommerceViewTab tab to navigate ecommerce view page
			productSearchService.setSelectedTab(NUTRITION_FACT_TAB);
			//productGroupService.setProductGroupId(self.cusProductGroup.customerProductGroup.custProductGroupId);
			//Set from page navigated to
			productSearchService.setFromPage(appConstants.NUTRITION_UPDATES_TASK);
			productSearchService.setDisableReturnToList(false);
			productSearchService.productUpdatesTaskCriteria = {};
			productSearchService.productUpdatesTaskCriteria.pageIndex = self.convertPagePerFifteenToPagePerOneHundred(productPosition);
			productSearchService.productUpdatesTaskCriteria.pageSize = self.NAVIGATE_PAGE_SIZE;
			productSearchService.productUpdatesTaskCriteria.searchCriteria = self.searchCriteria;
			productSearchService.productUpdatesTaskCriteria.searchCriteria.pageSize = self.NAVIGATE_PAGE_SIZE;
			$state.go(appConstants.HOME_STATE);
		};
		/**
		 * Convert page per fifteen to page per one hundred.
		 * @param productPosition the position of product on grid table.
		 * @returns {number} the position of product per one hundred.
		 */
		self.convertPagePerFifteenToPagePerOneHundred = function(productPosition){
			var productPositionInDataBase = (self.tableParams.page()-1) * self.tableParams.count() + productPosition;
			return Math.floor(productPositionInDataBase/self.NAVIGATE_PAGE_SIZE) + 1;
		}
		/**
		 * Return of back end after a user removes an alert. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.handleSuccess = function(data){
			self.displayMessage(data.message, false);
            if(self.tableParams.data.length == 1 && self.tableParams.page() > 0)
            	self.refreshTable(self.tableParams.page() - 1, false);
            else
				self.refreshTable(self.tableParams.page(), false);
		};

		/**
		 * Callback that will respond to errors sent from the backend.
		 *
		 * @param error The object with error information.
		 */
		self.handleError = function(error){
			if (error && error.data) {
				self.displayMessage(error.data, true);
			} else {
				self.displayMessage("An unknown error occurred.", true);
			}
			self.isWaiting = false;
		};

		/**
		 * Used to display any success of failure messages above the data table.
		 * @param message message to be displayed.
		 * @param isError is error or not; True - message displayed in red. False- message get displayed in blue.
		 */
		self.displayMessage = function(message, isError) {
			self.isError = isError;
			self.message = message;
		};

		self.clearMessage = function() {
			self.isError = false;
			self.message = '';
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = self.generateExportUrl();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'nutritionUpdates.csv', self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}
		};

		/**
		 * Generates the URL to ask for the export.
		 *
		 * @returns {string} The URL to ask for the export.
		 */
		self.generateExportUrl = function() {
			return urlBase + '/pm/task/nutritionUpdates/exportNutritionUpdatesToCsv';
		};
	}
})();
