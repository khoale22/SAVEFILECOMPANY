/*
 *
 * scaleManagementUpcMaintenanceController.js
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
 * The controller for the Scale Management UPC Maintenance Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ScaleManagementUpcMaintenanceController', scaleManagementUpcMaintenanceController);

	scaleManagementUpcMaintenanceController.$inject = ['ScaleManagementApi', 'ngTableParams', '$scope', 'urlBase', 'DownloadService', 'singlePluPanelService', '$state', 'appConstants'];

	/**
	 * Constructs the controller.
	 *
	 * @param scaleManagementApi
	 * @param ngTableParams
	 * @param urlBase The base url used for building export requests.
	 * @param downloadService The service used for downloading a file.
	 * @param $scope
	 */
	function scaleManagementUpcMaintenanceController(scaleManagementApi, ngTableParams, $scope, urlBase, downloadService, singlePluPanelService, $state, appConstants) {

		var self = this;
		/**
		 * The current error message.
		 * @type {String}
		 */
		self.error = null;

		/**
		 * The current success message.
		 * @type {String}
		 */
		self.success = null;

		/**
		 * Whether or not the search panel is collapsed or open.
		 * @type {boolean}
		 */
		self.dataSearchToggle = true;

		/**
		 * The current search text field.
		 * @type {String}
		 */
		self.searchText = null;

		/**
		 * The current view.
		 * @type {String}
		 */
		self.currentView = null;

		/**
		 * Whether or not page is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Controller data.
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Whether or not effective date picker is visible.
		 * @type {boolean}
		 */
		self.effectiveDatePickerOpened = false;

		/**
		 * Whether or not searching by description.
		 * @type {boolean}
		 */
		self.searchedByDescription = false;

		/**
		 * Controller date format.
		 * @type {String}
		 */
		self.format = "yyyy-MM-dd";

		/**
		 * Options for bulk update.
		 * @type {string[]}
		 */
		self.bulkUpdateOptionList = [
			{name: "Maintenance", id: "MAINTENANCE"},
			{name: "Shelf Life Days", id: "SHELF_LIFE_DAYS"},
			{name: "Freeze By Days", id: "FREEZE_BY_DAYS"},
			{name: "Service Counter Tare", id: "SERVICE_COUNTER_TARE"},
			{name: "Eat by Days", id: "EAT_BY_DAYS"},
			{name: "Prepack Tare", id: "PREPACK_TARE"},
			{name: "Action Code", id: "ACTION_CODE"},
			{name: "Graphics Code", id: "GRAPHICS_CODE"},
			{name: "Force Tare", id: "FORCE_TARE"},
			{name: "English Line 1", id: "ENGLISH_LINE_1"},
			{name: "English Line 2", id: "ENGLISH_LINE_2"},
			{name: "English Line 3", id: "ENGLISH_LINE_3"},
			{name: "English Line 4", id: "ENGLISH_LINE_4"},
			{name: "Ingredient Statement Number", id: "INGREDIENT_STATEMENT_NUMBER"}
		];

		/**
		 * The option to bulk update.
		 * @type {String}
		 */
		self.bulkUpdateOptionSelected = null;

		/**
		 * The value to bulk update.
		 * @type {null}
		 */
		self.bulkUpdateValue = null;

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
		 * Whether or not user is searching by PLU
		 * @type {boolean}
		 */
		self.searchingByPlu = true;

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
		 * The message to display about the nubmer of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * The number of records to show on the report.
		 * @type {number}
		 */
		self.PAGE_SIZE = 100;
		/**
		 * Include category is empty.
		 * @type {string}
		 */
		self.EMPTY = "EMPTY";

		/** Whether or not the plu is being edited.
		 * @type {boolean}
		 */
		self.isEditing = false;

		/** Current object that is being edited
		 * @type {null}
		 */
		self.currentEditingObject = null;

		/**
		 * If the plu has been changed.
		 * @type {boolean}
		 */
		self.pluChanged = false;

		/**
		 * If a bulk update is being done.
		 * @type {boolean}
		 */
		self.onBulkUpdate = false;
		/**
		 * Whether a row is selected for a bulk update.
		 * @type {boolean}
		 */
		self.isBulkUpdateSelected = true;
		/**
		 * Whether every row is selected.
		 * @type {boolean}
		 */
		self.selectAll = true;
		/**
		 * Total amount that will be bulk updated.
		 * @type {number}
		 */
		self.bulkUpdateCount = 0;
		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;
		/**
		 * Tracks whether or not the user is waiting for a download.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;

		const NOT_SINGLE_VIEW = "notSingleView";
		const SINGLE_VIEW = "singleView";
		const MINUTES_TO_MILLISECONDS = 60000;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.resetValues();
		};

		/**
		 * Initializes the item search.
		 */
		self.initItemSearch = function(){
			self.resetValues();
			self.searchingByPlu = true;
		};

		/**
		 * Initializes the description search.
		 */
		self.initDescriptionSearch = function(){
			self.resetValues();
			self.searchingByPlu = false;
		};

		/**
		 * Reset necessary variables to default values.
		 */
		self.resetValues = function(){
			self.modifyError = null;
			self.searchText = null;
			self.error = null;
			self.modifyMessage = null;
			self.success = null;
			self.data = null;

			self.effectiveDatePickerOpened = false;
			self.firstSearch = true;
			self.dataSearchToggle = true;
		};

		/**
		 * Search for products by one or more PLUs.
		 */
		self.newItemSearch = function(page) {
			if (self.setNewSearchValues(false)) {
				scaleManagementApi.queryByPluList({
						pluList: self.searchText,
						page: page,
						includeCounts: self.includeCounts
					},
					self.loadData,
					self.fetchError);
				scaleManagementApi.queryForMissingPLUs({
						pluList: self.searchText
					},
					self.loadMissingData,
					self.fetchError);
			}
			else {
				self.error = "Please enter a PLU to search for.";
			}
		};

		/**
		 * Search for products by description.
		 */
		self.newDescriptionSearch = function(page) {
			if (self.setNewSearchValues(true)) {
				scaleManagementApi.queryByDescription({
						description: self.searchText,
						page: page,
						includeCounts: self.includeCounts
					},
					self.loadData,
					self.fetchError);
			}
			else {
				self.error = "Please enter a description to search for.";
			}
		};


		/**
		 * Sets new Search Values.
		 * @param searchByDescription
		 * @returns {boolean}
		 */
		self.setNewSearchValues = function(searchByDescription) {
			if (self.searchText == null) {
				return false;
			}
			self.searchedByDescription = searchByDescription;
			self.isWaiting = true;
			return true;
		};

		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(){
			singlePluPanelService.setCurrentView(NOT_SINGLE_VIEW);
			self.modifyMessage = null;
			self.modifyError = null;
			self.firstSearch = true;
			self.isEditing = false;
			self.selectedScaleUpcIndex = null;
			self.selectAll = true;
			// The first time through, build the table. The rest of the time, just tell it to fetch new data.
			if (self.tableBuilt) {
				self.tableParams.reload();
			} else {
				self.tableBuilt = true;
				self.buildTable();
			}

		};

		/**
		 * Submit bulk update to back end if a change was detected
		 */
		self.submitBulkUpdate = function() {
			self.modifyMessage = null;
			self.modifyError = null;
			self.onBulkUpdate = true;
			var tmpArray = [];
			for(var index = 0; index < self.data.length; index++){
				if(self.data[index].isBulkUpdateSelected){
					tmpArray.push(self.data[index]);
				}
			}
			var values = {
				scaleUpcs: tmpArray,
				attribute: self.bulkUpdateOptionSelected,
				value: self.bulkUpdateValue
			};
			self.isWaiting = true;
			scaleManagementApi.bulkUpdateScaleUpc(values, self.loadBulkUpdateData, self.fetchModifiedError);
		};

		/**
		 * Loads bulk updated scale upc's without clearing the other ones.
		 * @param results
		 */
		self.loadBulkUpdateData = function(results) {
			self.modifyMessage = results.message;
			self.tableParams.reload();
		};
		/**
		 * Is current state not waiting and looking at multiple records.
		 * @returns {boolean}
		 */
		self.isViewingMultipleRecords = function(){
			self.currentView=singlePluPanelService.getCurrentView();
			return !self.isWaiting && self.data != null && self.currentView != SINGLE_VIEW;
		};

		/**
		 * Is current state not waiting and looking at single records.
		 * @returns {boolean}
		 */
		self.isViewingSingleRecord = function(){
			self.currentView=singlePluPanelService.getCurrentView();
			return !self.isWaiting && self.data != null && self.currentView == SINGLE_VIEW;
		};

		/**
		 * Has the user not selected an option or not selected a value for bulk updates.
		 * @returns {boolean}
		 */
		self.isAttributeNotSelected = function(){
			return self.bulkUpdateOptionSelected == null || (self.bulkUpdateValue == null && self.bulkUpdateOptionSelected != 'MAINTENANCE') || self.bulkUpdateCount == 0;
		};

		//callback functions

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

		self.fetchModifiedError = function(error) {
			self.isWaiting = false;
			self.modifyMessage = null;
			if (error && error.data){
				if(error.data.message) {
					self.modifyError = error.data.message;
				} else {
					self.modifyError = error.data.error;
				}
			} else {
				self.modifyError("An unknown error occured.");
			}
		};

		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.modifyMessage = null;
			self.modifyError = null;
			self.error = error;
		};

		/**
		 * Callback for when the backend returns a success.
		 *
		 * @param results The results from the backend.
		 */
		self.loadData = function(results) {
			self.success = null;
			self.isWaiting = false;
			self.modifyError = null;
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No records found.";
				return;
			} else {
				self.error = null;
				self.dataSearchToggle = false;
				if(self.selectedScaleUpcIndex != null && self.pluChanged) {
					self.modifyMessage = results.message;
					self.data[self.selectedScaleUpcIndex] = results.data;
					self.pluChanged = false;
					self.isEditing = false;
					self.getSingleView(results.data, self.selectedScaleUpcIndex);
					self.defer.resolve(self.data);
					return;
				} else if(self.onBulkUpdate){
					self.data = results.data;
					self.bulkUpdateOptionSelected = null;
					self.bulkUpdateValue = null;
					self.onBulkUpdate = false;
				} else {
					self.modifyMessage = null;
					self.data = results.data;
					self.resultMessage = self.getResultMessage(results.data.length, self.tableParams.page() - 1);
				}
				for(var index = 0; index < self.data.length; index++) {
					self.data[index].isBulkUpdateSelected = true;
				}
				self.bulkUpdateCount = results.data.length;
				self.bulkUpdateTotal = results.data.length;
				self.selectAll = true;

				if (self.searchedByDescription) {
					self.loadMissingData({matchCount: self.totalRecordCount, noMatchCount: 0, noMatchList: []});
				}
				self.defer.resolve(self.data);
			}
			if(self.data.length === 1){
				self.getSingleView(self.data[0], 0);
			} else {
				self.currentView = NOT_SINGLE_VIEW;
				singlePluPanelService.setCurrentView(self.currentView);
			}
		};

		/**
		 * Sends all relevant information to the single plu directive
		 * @param scaleUpc the currently selected scale upc
		 * @param index index of the scale upc in the list
		 */
		self.getSingleView=function(scaleUpc, index){
			singlePluPanelService.setSelectedScaleUpcIndex(index);
			singlePluPanelService.setSelectedScaleUpc(scaleUpc);
			singlePluPanelService.setCurrentView(SINGLE_VIEW);
			singlePluPanelService.setData(self.data);

		};
		/**
		 * Callback for the request for the number of items found and not found.
		 *
		 * @param results The object returned from the backend with a list of found and not found items.
		 */
		self.loadMissingData = function(results){
			self.missingValues = results;
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
			return (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " + self.totalRecordCount;
		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function () {
			self.tableParams = new ngTableParams(
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
						self.isWaiting = true;
						self.data = null;
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
						} else {
							self.includeCounts = false;
						}
						// Issue calls to the backend to get the data.
						self.searchingByPlu ?
							self.newItemSearch(params.page() - 1) : self.newDescriptionSearch(params.page() - 1);
					}
				}
			);
		};

		/**
		 * Changes single update all checkbox. Also checks whether one is unchecked and will uncheck the select
		 * all checkbox.
		 * @param value value true or false depending on whether it was checked or unchecked
		 */
		self.updateSelectAllSwitch = function(value) {
			if(!value) {
				self.selectAll = false;
				self.bulkUpdateCount--;
			} else {
				var found = false;
				for(var index = 0; index < self.data.length; index++) {
					if(!self.data[index].isBulkUpdateSelected){
						found = true;
						break;
					}
				}
				if(!found){
					self.selectAll = true;
					self.bulkUpdateCount = self.data.length;
				} else {
					self.bulkUpdateCount++;
				}
			}
		};

		/**
		 * When the select all box is clicked on.
		 * If already checked, all of the checkboxes will then be unchecked.
		 * If already unchecked, all of the checkboxes will then be checked.
		 * @param value true or false depending on whether it was checked or unchecked
		 */
		self.updateAllBulkUpdate = function(value) {
			if(value) {
				for(var index = 0; index < self.data.length; index++) {
					self.data[index].isBulkUpdateSelected = true;
					self.bulkUpdateCount = self.data.length;
				}
			} else {
				for(index = 0; index < self.data.length; index++) {
					self.data[index].isBulkUpdateSelected = false;
					self.bulkUpdateCount = 0;
				}
			}
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			self.downloading = true;
			downloadService.export(self.generateExportUrl(), 'pluScaleMaintenance.csv', self.WAIT_TIME,
				function() { self.downloading = false; });
		};

		/**
		 * Generates the URL to ask for the export.
		 *
		 * @returns {string} The URL to ask for the export.
		 */
		self.generateExportUrl = function() {
			if(self.searchedByDescription) {
				return urlBase + '/pm/scaleManagement/exportDescriptionToCsv?description=' + encodeURI(self.searchText) +
					"&totalPages=" + self.totalPages;
			} else {
				return urlBase + '/pm/scaleManagement/exportPlusToCsv?plus=' + encodeURI(self.searchText) +
					"&totalPages=" + self.totalPages;
			}
		};

		/**
		 * Navigate to Ingredient Statement Page and search by Ingredient Statement Number.
		 */
		self.navigateToIngredientStatements = function (ingStmtCode) {
			$state.go(appConstants.SCALE_MANAGEMENT_INGREDIENT_STATEMENTS,{ingStmtCode:ingStmtCode});
		};

		/**
		 * Navigate to Nutrient Statement Page and search by Nutrient Statement Number.
		 */
		self.navigateToNutrientStatement = function (ntrStmtCode) {
			$state.go(appConstants.SCALE_MANAGEMENT_NUTRIENT_STATEMENT,{ntrStmtCode:ntrStmtCode});
		};

		/**
		 * Check ingredient number is fake number.
		 * @param ingStmtCode
		 * @returns {boolean}
		 */
		self.isFakeIngredientStatement = function(ingStmtCode) {
			return (ingStmtCode != null && ingStmtCode != 0 && ingStmtCode != 9999);
		};

		/**
		 * Check nutrient number is fake number.
		 * @param ntrStmtCode
		 * @returns {boolean}
		 */
		self.isFakeNutrientStatement = function(ntrStmtCode) {
			return (ntrStmtCode != null && ntrStmtCode != 9999999 && ntrStmtCode != 0);
		};
	}
})();
