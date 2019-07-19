/*
 *  actionCodesController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the action codes search.
 *
 * @author s573181
 * @since 2.0.8
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('ActionCodesController', actionCodesController);

	actionCodesController.$inject = ['ScaleManagementApi','ngTableParams', 'singlePluPanelService'];

	function actionCodesController(scaleManagementApi, ngTableParams, singlePluPanelService) {
		var self = this;

		// Search string constants.
		self.ACTION_CODE = "action code";
		self.DESCRIPTION = "action code description";

		// Integer constants
		self.PAGE_SIZE = 10;

		// Error constants
		self.ACTION_CODE_AND_DESCRIPTION_REQUIRED = "Action code and description are required.";
		self.ACTION_CODE_REQUIRED = "Action code is required.";
		self.ACTION_CODE_DESCRIPTION_REQUIRED = "Description is required.";

		/**
		 * Whether back end should get counts or not.
		 *
		 * @type {boolean}
		 */
		self.includeCounts = false;
		/**
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;
		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;
		/**
		 * Tracks whether or not he search pane is visible.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;
		/**
		 * Tracks whether or not the user is waiting for data from the back end.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;
		/**
		 * Holds success messages.
		 *
		 * @type {string}
		 */
		self.success = null;
		/**
		 * Holds error messages.
		 *
		 * @type {string}
		 */
		self.error = null;
		/**
		 * The type of data the user has selected. Either action code or description.
		 *
		 * @type {string}
		 */
		self.selectionType = null;
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
		 * The action code data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;
		/**
		 * The action code for the plu search results.
		 * @type {number}
		 */
		self.pluActionCode = null;
		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;
		/**
		 * Whether or not the user is searching for all action codes.
		 * @type {boolean}
		 */
		self.findAll = false;
		/**
		 * Whether or not a user is adding an action code.
		 * @type {boolean}
		 */
		self.isAddingActionCode = false;
		/**
		 * Index of data that is being edited.
		 * @type {number}
		 */
		self.previousEditingIndex = null;
		/**
		 * Original state of action code being edited.
		 * @type {null}
		 */
		self.currentEditingObject = null;
		/**
		 * Whether or not the action code was removed.
		 * @type {boolean}
		 */
		self.removedActionCode = false;
		/**
		 * Message to display for deleted action code.
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * If action code is being changed.
		 * @type {boolean}
		 */
		self.actionCodeChanged = false;
		/**
		 * Keep track of latest Request for asynchronous calls.
		 * @type {number}
		 */
		self.latestRequest = 0;
		/**
		 * Used to communicate the state of the view if it is showing a single plu or not
		 * @type {string}
		 */
		self.currentView = "";
		/**
		 * Constant stating that the current state is not viewing a single plu
		 * @type {string}
		 */
		const NOT_SINGLE_VIEW = "notSingleView";


		/**
		 * Initializes the controller.
		 */
		self.init = function () {
			self.currentView=NOT_SINGLE_VIEW;
			singlePluPanelService.setCurrentView(self.currentView);
			if(self.removedActionCode || self.isAddingActionCode) {
				self.error = null;
				self.data = null;
				self.searchPanelVisible = true;
				self.isAddingActionCode = false;
				self.removedActionCode = false;
			}
			self.resetView(self.ACTION_CODE);
		};

		/**
		 * Is current state not waiting and looking at multiple records.
		 * @returns {boolean}
		 */
		self.isNotViewingSinglePlu = function(){
			self.currentView=singlePluPanelService.getCurrentView();
			return !self.isWaiting && self.data != null && self.currentView == NOT_SINGLE_VIEW;
		};

		/**
		 * Constructs the ng-table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						if (self.isCurrentStateNull()) {
							self.defer = $defer;
							return;
						}
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (self.firstSearch) {
							self.includeCounts = true;
							self.firstSearch = false;
						}
						self.getReportBySearchType(params.page() - 1);
					}
				}
			)
		};

		/**
		 * Ng-table params variable for maintaining data.
		 */
		self.tableParams = self.buildTable();

		/**
		 * Helper function for getData function of buildTable(). If current state of page is considered null, return
		 * true; otherwise false.
		 *
		 * @returns {boolean}
		 */
		self.isCurrentStateNull = function(){

			return self.searchSelection == null &&
				!self.findAll &&
				(!self.isAddingActionCode || self.data[0].description == null) &&
				self.previousEditingIndex == null;
		};

		/**
		 * Reset page view by hiding result screens and setting the search radio button switch.
		 *
		 * @param selectionType The type of data in the list - description or action code.
		 */
		self.resetView = function (selectionType) {
			self.resetDefaults();
			self.selectionType = selectionType;
		};

		/**
		 * Reset default values.
		 */
		self.resetDefaults = function(){
			self.modifyError = null;
			self.searchSelection = null;
			self.error = null;
			self.previousEditingIndex = null;
			self.isAddingActionCode = false;
			self.modifyMessage = null;
		};

		/**
		 * Returns the text to display in the search box when it is empty.
		 *
		 * @returns {string} The text to display in the search box when it is empty.
		 */
		self.getTextPlaceHolder = function () {
			return 'Enter ' + self.selectionType + ' to search'
		};

		/**
		 * Clears the search box.
		 */
		self.clearSearch = function () {
			self.searchSelection = null;
		};

		/**
		 * Issue call to newSearch to call back end to fetch all action codes.
		 */
		self.searchAll = function (){
			self.clearSearch();
			self.newSearch(true);
		};

		/**
		 * Callback for a successful call to get action code data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			self.success = null;
			self.isWaiting = false;
			self.modifyMessage = null;
			self.modifyError = null;
			self.missingValues = null;
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.error = "No records found.";
			} else if(self.isAddingActionCode && self.actionCodeChanged) {
				self.error = null;
				self.modifyMessage = results.message;
				self.resultMessage = null;
				self.error = null;
				self.isAddingActionCode = false;
				self.data = [];
				self.data.push(results.data);
				self.defer.resolve(results.data);
			} else if(self.previousEditingIndex != null && self.actionCodeChanged){
				self.error = null;
				self.modifyMessage = results.message;
				self.data[self.previousEditingIndex].isEditing = false;
				self.data[self.previousEditingIndex] = results.data;
				self.previousEditingIndex = null;
				self.currentEditingObject = null;
			} else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
			if(self.removedActionCode) {
				self.removedActionCode = false;
			} else {
				self.deleteMessage = null;
			}

			self.actionCodeChanged = false;
			self.searchPanelVisible = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			if(error && error.data) {
				if (error.data.message) {
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
		 * Sets up the ng-table for a action code search.
		 */
		self.newSearch = function (findAll) {
			singlePluPanelService.setCurrentView(NOT_SINGLE_VIEW);
			self.modifyError = null;
			self.firstSearch = true;
			self.pluActionCode = null;
			self.findAll = findAll;
			self.isAddingActionCode = false;
			self.previousEditingIndex = null;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Sets up the ng-table for a action code search.
		 */
		self.actionCodeChanges = function () {
			self.pluActionCode = null;
			self.actionCodeChanged = true;
			self.modifyActionCode();
		};

		/**
		 * Sets the plu ng-table for a new search.
		 *
		 * @param actionCode The action code to search for PLUs on.
		 */
		self.getPlus = function(actionCode){
			self.pluActionCode = actionCode;
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
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Selects the appropriate action code search by the search type specified (description of action cd number).
		 *
		 * @param page The page number requested.
		 */
		self.getReportBySearchType = function(page) {
			if(self.findAll){
				scaleManagementApi.queryAllActionCodes({
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.ACTION_CODE && self.searchSelection !== null) {
				scaleManagementApi.queryByActionCodes({
					actionCodesList: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadDataAndFindHits, self.fetchError);
			} else if (self.selectionType === self.DESCRIPTION && self.searchSelection !== null) {
				scaleManagementApi.queryByActionCodeDescription({
					description: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			}
		};

		/**
		 * Calls load data and find hits. Used on success to prevent find hits to be called whenever the find by
		 * graphics code fails.
		 * @param results The results.
		 */
		self.loadDataAndFindHits = function(results){
			self.loadData(results);
			scaleManagementApi.queryForMissingActionCodes({
				actionCodes: self.searchSelection}, self.showMissingData, self.fetchError);
		};
		/**
		 * Decide whether to add actionCode or update actionCode.
		 */
		self.modifyActionCode = function(){
			if(self.isAddingActionCode) {
				self.addActionCode();
			} else if(self.previousEditingIndex != null){
				self.updateActionCode();
			}
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
		 * Only show match count information for searches by action code (not description).
		 *
		 * @returns {boolean} True if the search is by action code.
		 */
		self.showMatchCount = function(){
			return self.selectionType === self.ACTION_CODE;
		};

		/**
		 * Add an empty row to the action code data.
		 */
		self.addRow = function(){
			self.resetDefaults();
			self.searchPanelVisible = false;
			self.deleteMessage = null;
			self.pluActionCode = null;
			self.isAddingActionCode = true;
			self.data = [];
			self.data.push({});
			self.tableParams.data = [];
		};

		/**
		 * Send data to back end to add a new action code.
		 */
		self.addActionCode = function(){
			if(!self.data[0].actionCode && !self.data[0].description){
				self.fetchModifiedError({data: {message: self.ACTION_CODE_AND_DESCRIPTION_REQUIRED}});
			} else if(!self.data[0].actionCode){
				self.fetchModifiedError({data: {message: self.ACTION_CODE_REQUIRED}});
			} else if(!self.data[0].description){
				self.fetchModifiedError({data: {message: self.ACTION_CODE_DESCRIPTION_REQUIRED}});
			} else {
				self.modifyMessage = null;
				self.modifyError = null;
				var actionCode = self.data[0];
				self.totalRecordCount = null;
				scaleManagementApi.addActionCode(actionCode, self.loadData, self.fetchModifiedError);
			}
		};

		/**
		 * Allows the user to edit an action code. This method alters the state of an action code's description to
		 * be able to be edited.
		 *
		 * @param index Index of current data to be edited.
		 */
		self.editActionCode = function(index){
			if(self.previousEditingIndex != null){
				self.data[self.previousEditingIndex].isEditing = false;
			}
			self.previousEditingIndex = index;
			self.currentEditingObject = angular.copy(self.data[index]);
			self.data[index].isEditing = true;
		};

		/**
		 * If a change was detected, this method sends the action code to the back end to be updated. If not, an
		 * error message stating 'No difference detected' will be displayed.
		 */
		self.updateActionCode = function(){
			var scaleActionCode = self.data[self.previousEditingIndex];
			var difference = scaleActionCode.description != self.currentEditingObject.description;
			if(difference){
				scaleManagementApi.updateActionCode(scaleActionCode,
					self.loadData, self.fetchModifiedError
				);
			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Return of back end after a user removes an action code. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedActionCode = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Send action code to back end to be deleted.
		 *
		 * @param index Index of data to be removed.
		 */
		self.removeActionCode = function(index){
			var actionCode = self.data[index].actionCode;
			self.isWaiting = true;
			scaleManagementApi.deleteActionCode({actionCode: actionCode}, self.reloadRemovedData, self.fetchError)
		};

		/**
		 * Editing is being cancelled.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.cancel = function(index){
			if(self.data[index].isEditing != null) {
				self.data[index] = self.currentEditingObject;
				self.data[index].isEditing = false;
			} else {
				self.deleteMessage = null;
				self.init();
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
	}
})();
