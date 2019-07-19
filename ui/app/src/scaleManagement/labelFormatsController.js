/*
 * labelFormatsController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Controller for the Label Format screen.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('LabelFormatsController', labelFormatsController);

	labelFormatsController.$inject = ['ScaleManagementApi', 'ngTableParams', 'singlePluPanelService'];

	/**
	 * Constructs the controller.
	 *
	 */
	function labelFormatsController(scaleManagementApi, ngTableParams, singlePluPanelService) {

		var self = this;
		const IS_NOT_SINGLE_PLU = "notSingleView";


		/**
		 * The number of records to display in both the label format and the UPC table.
		 * @type {number}
		 */
		self.PAGE_SIZE = 10;

		/**
		 * Use as a constant that the user has selected to search by label format code.
		 * @type {string}
		 */
		self.FORMAT_CODE = "format code";
		/**
		 * Use as a constant that the user has selected to search by label format description.
		 * @type {string}
		 */
		self.DESCRIPTION = "description";

		/**
		 * Stores any error messages for the user.
		 * @type {string}
		 */
		self.error = null;

		/**
		 * The text the user has put into the search box.
		 * @type {string}
		 */
		self.searchText = null;

		/**
		 * Whether or not the user has collapsed the search box.
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		// Variables related to the variable formats list part of the page.
		/**
		 * The type of search the user has requested (format code or description).
		 * @type {string}
		 */
		self.filterType = self.FORMAT_CODE;
		/**
		 * Wheter or not the user has chosen to get a full list of label formats.
		 * @type {boolean}
		 */
		self.findAll = false;
		/**
		 * The list of label formats based on the user's search criteria.
		 * @type {LabelFormat[]}
		 */
		self.labelFormats = null;
		/**
		 * Whether or not the current search of label formats is the first time the search is performed
		 * for the current criteria.
		 * @type {boolean}
		 */
		self.initialSearchOfLabelFormats = true;
		/**
		 * Whether or not to request record counts from the backend when asking for a list of label formats.
		 * @type {boolean}
		 */
		self.includeCountsOfLabelFormats = true;
		/**
		 * The total number of records that match the user's search criteria.
		 * @type {number}
		 */
		self.labelFormatTotalRecords = 0;
		/**
		 * The record number out of the total of the first record in the label formats table.
		 * @type {number}
		 */
		self.labelFormatCurrentRow = 1;
		/**
		 * The record number of the last visible row in the label formats table.
		 * @type {number}
		 */
		self.labelFormatLastVisibleRow = 0;
		/**
		 * The label format currently selected by the user.
		 * @type {LabelFormat}
		 */
		self.selectedLabelFormat = null;
		/**
		 * Wheter or not the page list will show under the label format page.
		 * @type {boolean}
		 */
		self.noLabelFormatPagination = true;
		/**
		 * The label format column the user has chosen to search on.
		 * @type {number}
		 */
		self.labelFormatSelected = 0;
		/**
		 * Passed by both NG tables on the page to get the data to display.
		 * @type {Object}
		 */
		self.defer = null;
		/**
		 * Passed by both NG tables on the page to set the metadata about the data to display.
		 * @type {Object}
		 */
		self.dataResolvingParams = null;
		/**
		 * Whether or not a user is adding a label format.
		 * @type {boolean}
		 */
		self.isAddingLabelFormat = false;
		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;

		/**
		 * Tracks whether or not the user is waiting for data from the back end.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;
		/**
		 * If label format code is being changed.
		 * @type {boolean}
		 */
		self.labelFormatCodeChanged = false;
		/**
		 * Message to display for deleted format code.
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * Whether or not the format code was removed.
		 * @type {boolean}
		 */
		self.removedLabelFormatCode = false;
		/**
		 * Original state of format code being edited.
		 * @type {null}
		 */
		self.currentEditingObject = null;
		/**
		 * Holds the current state of the view if whether or not it is currently looking at a single PLU
		 * @type {string}
		 */
		self.currentView="";

		/**
		 * Initializes the controller.
		 */
		self.init = function() {
			self.currentView=IS_NOT_SINGLE_PLU;
			singlePluPanelService.setCurrentView(self.currentView);
			if(self.removedLabelFormatCode || self.isAddingLabelFormat) {
				self.error = null;
				self.labelFormats = null;
				self.searchPanelVisible = true;
				self.removedLabelFormatCode = false;
			}
			self.resetView(self.FORMAT_CODE);
		};

		/**
		 * Is current state not waiting and looking at multiple records.
		 * @returns {boolean}
		 */
		self.isNotViewingSinglePlu = function(){
			self.currentView=singlePluPanelService.getCurrentView();
			return !self.isWaiting && self.currentView == IS_NOT_SINGLE_PLU;
		};

		/**
		 * The text to display when the user has not entered anythign to search on.
		 *
		 * @returns {string} The text to display.
		 */
		self.emptySearchMessage = function () {
			return "Enter " + self.filterType + " to search";
		};

		/**
		 * Clears the text tied to the search box.
		 */
		self.clearSearch = function() {
			self.searchText = null;
		};

		/**
		 * Called when the user clicks on a link to get a list of UPCs with a given label format set for their
		 * label format one.
		 *
		 * @param labelFormat The format to search for.
		 */
		self.formatOneSelect = function(labelFormat) {
			labelFormat.labelFormatSelected = 1;
			self.formatSelect(labelFormat);
		};

		/**
		 * Called when the user clicks on a link to get a list of UPCs with a given label format set for their
		 * label format two.
		 *
		 * @param labelFormat The format to search for.
		 */
		self.formatTwoSelect = function(labelFormat) {
			labelFormat.labelFormatSelected = 2;
			self.formatSelect(labelFormat);
		};

		/**
		 * Common code for selecting a label format.
		 *
		 * @param labelFormat The format to search for.
		 */
		self.formatSelect = function(labelFormat) {
			self.selectedLabelFormat = angular.copy(labelFormat);
		};

		/**
		 * Issue call to newSearch to call back end to fetch all format codes.
		 */
		self.searchAll = function (){
			self.clearSearch();
			self.initSearch(true);
		};

		/**
		 * Starts a search for a list of label formats.
		 *
		 * @param findAll True if the user selected search all and false otherwise.
		 */
		self.initSearch = function(findAll) {
			singlePluPanelService.setCurrentView(IS_NOT_SINGLE_PLU);
			self.error = null;
			self.findAll = findAll;
			self.selectedLabelFormat = null;
			self.initialSearchOfLabelFormats = true;
			self.previousEditingIndex = null;
			self.isAddingLabelFormat = false;
			self.labelFormatTableParams.page(1);
			self.labelFormatTableParams.reload();
		};

		/**
		 * Constructs the ngTable that displays label format codes.
		 */
		self.reloadTable = function() {

			return new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE
				}, {
					counts: [],

					getData: function($defer, params) {
						if(self.isCurrentStateNull()){
							self.defer =  $defer;
							return;
						}
						self.isWaiting = true;
						self.defer =  $defer;
						self.dataResolvingParams = params;

						if (self.initialSearchOfLabelFormats) {
							self.includeCountsOfLabelFormats = true;
							self.initialSearchOfLabelFormats = false;
						} else {
							self.includeCountsOfLabelFormats = false;
						}

						self.searchLabelFormats(params.page() - 1);
					}
				}
			)
		};

		/**
		 * Ng-table params variable for maintaining data.
		 */
		self.labelFormatTableParams = self.reloadTable();

		/**
		 * Helper function for getData function of reloadTable(). If current state of page is considered null, return
		 * true; otherwise false.
		 *
		 * @returns {boolean}
		 */
		self.isCurrentStateNull = function(){
			return self.searchText == null && !self.findAll &&
				(!self.isAddingLabelFormat || self.labelFormats[0].description == null);
		};

		/**
		 * Reset page view by hiding result screens and setting the search radio button switch.
		 *
		 * @param selectionType The type of data in the list - description or format code.
		 */
		self.resetView = function (filterType) {
			self.resetDefaults();
			self.filterType = filterType;
		};

		/**
		 * Called by the ngTable of label formats to load its data.
		 *
		 * @param page The page number being requested.
		 */
		self.searchLabelFormats = function(page) {

			if (self.findAll) {
				scaleManagementApi.queryForLabelFormats({
						includeCounts: self.includeCountsOfLabelFormats,
						page: page,
						pageSize: self.PAGE_SIZE
					}, self.loadLabelFormats, self.fetchError
				);
			} else {
				if (self.filterType == self.FORMAT_CODE ) {
					scaleManagementApi.queryForLabelFormatsByCode({
							formatCode: self.searchText,
							includeCounts: self.includeCountsOfLabelFormats,
							page: page,
							pageSize: self.PAGE_SIZE
						}, self.loadDataAndFindHits, self.fetchError
					);
				} else {
					scaleManagementApi.queryForLabelFormatsByDescription({
							description: self.searchText,
							includeCounts: self.includeCountsOfLabelFormats,
							page: page,
							pageSize: self.PAGE_SIZE
						}, self.loadLabelFormats, self.fetchError
					);
				}
			}
		};
		/**
		 * Calls load data and find hits. Used on success to prevent find hits to be called whenever the find by
		 * graphics code fails.
		 * @param results The results.
		 */
		self.loadDataAndFindHits = function(results){
			self.loadLabelFormats(results);
			scaleManagementApi.queryForMissingLabelFormats({
				formatCodes: self.searchText}, self.showMissingData, self.fetchError);
		};
		/**
		 * Callback for the search for label formats.
		 *
		 * @param results The PageableResult with label format data.
		 */
		self.loadLabelFormats = function(results) {
			self.modifyMessage = null;
			self.isWaiting = false;
			self.missingValues = null;
			if (results.complete) {
				self.labelFormatTotalRecords = results.recordCount;
				self.dataResolvingParams.total(results.recordCount);
			}
			if (results.data.length == 0) {
				self.error = "No records found.";
			} else if(self.isAddingLabelFormat && self.labelFormatCodeChanged) {
				self.modifyMessage = results.message;
				self.error = null;
				self.isAddingLabelFormat = false;
				self.labelFormats = [];
				self.labelFormats.push(results.data);
				self.defer.resolve(results.data);
				self.resultMessage = null;
			} else if(self.previousEditingIndex != null && self.labelFormatCodeChanged){
				self.error = null;
				self.modifyMessage = results.message;
				self.labelFormats[self.previousEditingIndex].isEditing = false;
				self.labelFormats[self.previousEditingIndex] = results.data;
				self.previousEditingIndex = null;
				self.currentEditingObject = null;
			} else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.labelFormats = results.data;
				self.defer.resolve(results.data);
			}
			if(self.removedLabelFormatCode) {
				self.removedLabelFormatCode = false;
			} else {
				self.deleteMessage = null;
			}
			self.labelFormatCodeChanged = false;
			self.searchPanelVisible = false;
		};


		/**
		 * Returns a formatted description of the label format.
		 * @returns {string}
		 */
		self.labelFormatDescription = function() {
			if (self.selectedLabelFormat == null) {
				return null;
			}
			return self.selectedLabelFormat.description.trim() + '[' + self.selectedLabelFormat.formatCode + ']';
		};

		/**
		 * Common callback to handle an error coming from the backend.
		 *
		 * @param error The error sent from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			self.labelFormats = null;
			if (error && error.data) {
				if (error.data.message && error.data.message != "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}

			}
			else {
				self.error = "An unknown error occurred.";
			}
		};

		/**
		 * Reset default values.
		 */
		self.resetDefaults = function(){
			self.modifyError = null;
			self.searchText = null;
			self.error = null;
			self.previousEditingIndex = null;
			self.isAddingLabelFormat = false;
			self.modifyMessage = null;
		};

		/**
		 * Add an empty row to the format code data.
		 */
		self.addRow = function(){
			self.resetDefaults();
			self.deleteMessage = null;
			self.searchPanelVisible = false;
			self.selectedLabelFormat = null;
			self.isAddingLabelFormat = true;
			self.labelFormats = [];
			self.labelFormats.push({});
			self.labelFormatTableParams.data = [];
		};

		/**
		 * Send data to back end to add a new format code.
		 */
		self.addLabelFormat = function(){
			if(self.isAddingLabelFormat){
				var labelFormat = self.labelFormats[0];
				self.labelFormatTotalRecords = null;
				scaleManagementApi.addLabelFormat(labelFormat, self.loadLabelFormats, self.fetchError)
			}
		};

		/**
		 * Sets up the ng-table for a format code search.
		 */
		self.labelFormatCodeChanges = function () {
			self.selectedLabelFormat = null;
			self.labelFormatCodeChanged = true;
			self.modifyLabelFormatCode();
		};

		/**
		 * Decide whether to add formatCode or update formatCode.
		 */
		self.modifyLabelFormatCode = function(){
			if(self.isAddingLabelFormat) {
				self.addLabelFormat();
			} else if(self.previousEditingIndex != null) {
				self.updateFormatCode();
			}
		};

		/**
		 * Allows the user to edit an format code. This method alters the state of an format code's description to
		 * be able to be edited.
		 *
		 * @param index Index of current data to be edited.
		 */
		self.editFormatCode = function(index){
			if(self.previousEditingIndex != null){
				self.labelFormats[self.previousEditingIndex].isEditing = false;
			}
			self.previousEditingIndex = index;
			self.currentEditingObject = angular.copy(self.labelFormats[index]);
			self.labelFormats[index].isEditing = true;
		};

		/**
		 * If a change was detected, this method sends the action code to the back end to be updated. If not, an
		 * error message stating 'No difference detected' will be displayed.
		 */
		self.updateFormatCode = function(){
			var scaleLabelFormatCode = self.labelFormats[self.previousEditingIndex];
			var difference = scaleLabelFormatCode.description != self.currentEditingObject.description;
			if(difference){
				scaleManagementApi.updateFormatCode(scaleLabelFormatCode, self.loadLabelFormats, self.fetchError);
			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Return of back end after a user removes a format code. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedLabelFormatCode = true;
			if(self.labelFormatTotalRecords == null || self.labelFormatTotalRecords <= 1) {
				self.searchText = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.initSearch(self.findAll);
			}
		};

		/**
		 * Send format code to back end to be deleted.
		 *
		 * @param index Index of data to be removed.
		 */
		self.removeFormatCode = function(index){
			var formatCode = self.labelFormats[index].formatCode;
			self.isWaiting = true;
			scaleManagementApi.deleteFormatCode({formatCode: formatCode}, self.reloadRemovedData, self.fetchError)
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
				self.labelFormatTotalRecords.toLocaleString();
		};

		/**
		 * Editing is being cancelled.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.cancel = function(index){
			if(self.labelFormats[index].isEditing != null) {
				self.labelFormats[index] = self.currentEditingObject;
				self.labelFormats[index].isEditing = false;
			} else {
				self.deleteMessage = null;
				self.init();
			}
		};

		/**
		 * Only show match count information for searches by format code (not description).
		 *
		 * @returns {boolean} True if the search is by format code.
		 */
		self.showMatchCount = function(){
			return self.filterType === self.FORMAT_CODE;
		};

		/**
		 * Callback for the request for the number of items found and not found.
		 *
		 * @param results The object returned from the backend with a list of found and not found items.
		 */
		self.showMissingData = function(results){
			self.missingValues = results;
		};
	}
})();
