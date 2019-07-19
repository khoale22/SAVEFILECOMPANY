/*
 *  ingredientCategoryController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the ingredient category search.
 *
 * @author s573181
 * @since 2.1.0
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('IngredientCategoryController', ingredientCategoryController);

	ingredientCategoryController.$inject = ['ScaleManagementApi','ngTableParams'];

	function ingredientCategoryController(scaleManagementApi, ngTableParams) {
		var self = this;

		// Search string constants.
		self.INGREDIENT_CODE = "ingredient code";
		self.DESCRIPTION = "ingredient code description";

		// integer constants
		self.PAGE_SIZE = 10;

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
		 * The type of data the user has selected. Either ingredient code or description.
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
		 * The ingedient code data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;
		/**
		 * The ingredient code for the plu search results.
		 * @type {number}
		 */
//		self.pluActionCode = null;
		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;
		/**
		 * Whether or not the user is searching for all ingredient codes.
		 * @type {boolean}
		 */
		self.findAll = false;
		/**
		 * Whether or not a user is adding an ingredient code.
		 * @type {boolean}
		 */
		self.isAddingIngredientCode = false;
		/**
		 * Index of data that is being edited.
		 * @type {number}
		 */
		self.previousEditingIndex = null;
		/**
		 * Original state of ingredient code being edited.
		 * @type {null}
		 */
		self.currentEditingObject = null;
		/**
		 * Whether or not the ingredient code was removed.
		 * @type {boolean}
		 */
		self.removedIngredientCode = false;
		/**
		 * Message to display for deleted ingredient code.
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * If category code is being changed.
		 * @type {boolean}
		 */
		self.categoryCodeChanged = false;
		/**
		 * Initializes the controller.
		 */
		self.init = function () {
			self.tableParams.reload();
			self.resetView(self.INGREDIENT_CODE);
			if(self.removedIngredientCode) {
				self.error = null;
				self.data = null;
				self.searchPanelVisible = true;
				self.removedIngredientCode = false;
			}
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
				(!self.isAddingIngredientCode || self.data[0].categoryDescription == null) &&
				self.previousEditingIndex == null;
		};

		/**
		 * Reset page view by hiding result screens and setting the search radio button switch.
		 *
		 * @param selectionType The type of data in the list - description or ingredient code.
		 */
		self.resetView = function (selectionType) {
			self.resetDefaults();
			self.selectionType = selectionType;
		};

		/**
		 * Reset default values.
		 */
		self.resetDefaults = function(){
			self.searchSelection = null;
			self.error = null;
			self.previousEditingIndex = null;
			self.isAddingIngredientCode = false;
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
		 * Issue call to newSearch to call back end to fetch all ingredient codes.
		 */
		self.searchAll = function (){
			self.clearSearch();
			self.newSearch(true);
		};

		/**
		 * Callback for a successful call to get ingredient code data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			self.success = null;
			self.isWaiting = false;
			self.modifyMessage = null;
			self.missingValues = null;
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.error = "No records found.";
			} else if(self.isAddingIngredientCode && self.categoryCodeChanged) {
				self.modifyMessage = results.message;
				self.resultMessage = null;
				self.error = null;
				self.isAddingIngredientCode = false;
				self.data = [];
				self.data.push(results.data);
				self.defer.resolve(results.data);
			} else if(self.previousEditingIndex != null && self.categoryCodeChanged){
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
			if(self.removedIngredientCode) {
				self.removedIngredientCode = false;
			} else {
				self.deleteMessage = null;
			}

			self.categoryCodeChanged = false;
			self.searchPanelVisible = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param results The error from the backend.
		 */
		self.fetchError = function (results) {
			self.isWaiting = false;
			if(self.isAddingIngredientCode || self.previousEditingIndex != null){
				self.modifyError = results.message;
			} else {
				self.error = results.data.message;
			}
		};

		/**
		 * Sets up the ng-table for a ingredient code search.
		 */
		self.newSearch = function (findAll) {
			self.firstSearch = true;
			self.findAll = findAll;
			self.isAddingIngredientCode = false;
			self.previousEditingIndex = null;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Sets up the ng-table for a ingredient code search.
		 */
		self.ingredientCodeChanges = function () {
			self.categoryCodeChanged = true;
			self.modifyIngredientCode();
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
		 * Selects the appropriate ingredient code search by the search type specified (description of ingredient cd number).
		 *
		 * @param page The page number requested.
		 */
		self.getReportBySearchType = function(page) {
			if(self.findAll){
				scaleManagementApi.getAllCategories({
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.INGREDIENT_CODE && self.searchSelection !== null) {
				scaleManagementApi.queryByIngredientCategoryCode({
					categoryCodes: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadDataAndFindHits, self.fetchError);
			} else if (self.selectionType === self.DESCRIPTION && self.searchSelection !== null) {
				scaleManagementApi.queryByIngredientCategoryDescription({
					categoryDescription: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			}
		};
		/**
		 * Calls load data and find hits. Used on success to prevent find hits to be called whenever the find by
		 * category code fails.
		 * @param results The results.
		 */
		self.loadDataAndFindHits = function(results){
			self.loadData(results);
			scaleManagementApi.queryForMissingIngredientCategoryCodes({
				categoryCodes: self.searchSelection}, self.showMissingData, self.fetchError);
		};
		/**
		 * Decide whether to add ingredientCode or update ingredientCode.
		 */
		self.modifyIngredientCode = function(){
			if(self.isAddingIngredientCode) {
				self.addIngredientCode();
			} else if(self.previousEditingIndex != null){
				self.updateIngredientCode();
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
		 * Only show match count information for searches by ingredient code (not description).
		 *
		 * @returns {boolean} True if the search is by ingredient code.
		 */
		self.showMatchCount = function(){
			return self.selectionType === self.INGREDIENT_CODE;
		};

		/**
		 * Add an empty row to the ingredient code data.
		 */
		self.addRow = function(){
			self.resetDefaults();
			self.searchPanelVisible = false;
			//self.pluActionCode = null;
			self.isAddingIngredientCode = true;
			self.data = [];
			self.data.push({categoryCode: "Auto-generated"});
			self.tableParams.data = [];
		};

		/**
		 * Send data to back end to add a new ingredient code.
		 */
		self.addIngredientCode = function(){
			if(self.isAddingIngredientCode){
				var ingredientCategory = self.data[0];
				ingredientCategory.categoryCode = 0;
				self.totalRecordCount = null;
				scaleManagementApi.addIngredientCategory(ingredientCategory, self.loadData, self.fetchError)
			}
		};

		/**
		 * Helper function to determine if value is a number.
		 *
		 * @param value Number to check.
		 * @returns {boolean} true if number, false otherwise.
		 */
		self.isNumber = function(value){
			return !isNaN(value);
		};

		/**
		 * Allows the user to edit an ingredient code. This method alters the state of an ingredient code's description to
		 * be able to be edited.
		 *
		 * @param index Index of current data to be edited.
		 */
		self.editIngredientCode = function(index){
			if(self.previousEditingIndex != null){
				self.data[self.previousEditingIndex].isEditing = false;
			}
			self.previousEditingIndex = index;
			self.currentEditingObject = angular.copy(self.data[index]);
			self.data[index].isEditing = true;
		};

		/**
		 * If a change was detected, this method sends the ingredient code to the back end to be updated. If not, an
		 * error message stating 'No difference detected' will be displayed.
		 */
		self.updateIngredientCode = function(){
			var ingredientCategory = self.data[self.previousEditingIndex];
			var difference = ingredientCategory.categoryDescription != self.currentEditingObject.categoryDescription;
			if(difference){
				scaleManagementApi.updateIngredientCategory(ingredientCategory,
					self.loadData, self.fetchError
				);
			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Return of back end after a user removes an ingredient code. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedIngredientCode = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Send ingredient code to back end to be deleted.
		 *
		 * @param index Index of data to be removed.
		 */
		self.removeIngredientCode = function(index){
			var ingredientCode = self.data[index].categoryCode;
			self.isWaiting = true;
			scaleManagementApi.deleteIngredientCategory({ingredientCode: ingredientCode}, self.reloadRemovedData, self.fetchError)
		};

		/**
		 * Editing is being cancelled.
		 *
		 * @param index Index of data to be refreshed.
		 */
		self.cancel = function(index){
			self.data[index] = self.currentEditingObject;
			self.data[index].isEditing = false;
		};
	}
})();
