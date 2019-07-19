/*
 *
 * scaleManagementIngredientsController.js
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
 * The controller for the Scale Management Ingredients Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ScaleManagementIngredientsController', scaleManagementIngredientsController);

	scaleManagementIngredientsController.$inject = ['ScaleManagementApi', 'ngTableParams', 'urlBase', 'DownloadService', 'PermissionsService'];

	/**
	 * Constructs the controller.
	 * @param scaleManagementApi The API to fetch data from the backend.
	 * @param ngTableParams The API to set up the report table.
	 * @param downloadService The service used for downloading a file.
	 * @param permissionsService Service that provides access to a user's access levels.
	 */
	function scaleManagementIngredientsController(scaleManagementApi, ngTableParams, urlBase, downloadService, permissionsService) {

		var self = this;

		self.DEFAULT_ITEM_STATUS = "NONE";
		// Different levels of searches available.
		self.INGREDIENT_CODE = "INGREDIENT CODE";
		self.DESCRIPTION = "DESCRIPTION";
		self.INCLUDE ="INCLUDE";
		self.EXCLUDE ="EXCLUDE";
		self.EMPTY = "EMPTY";
		self.MAX_INGREDIENT_CODE_LENGTH = 5;
		/**
		 * Informational text for the user adding sub-ingredients.
		 * @type {string}
		 */
		self.SUB_INGREDIENT_TEXT = "A sub-ingredient cannot be added that exists as a sub-ingredient already, " +
			"or as a sub-ingredient of those sub-ingredients. An ingredient also cannot be added as a " +
			"sub-ingredient if it exists as a super ingredient, or a super ingredient of those super ingredients.";
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
		 * The data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;
		/**
		 * The number of records to show on the report.
		 * @type {number}
		 */
		self.PAGE_SIZE = 12;
		/**
		 * The paramaters that define the table showing the report.
		 * @type {ngTableParams}
		 */
		self.tableParams = null;
		/**
		 * The parameter that defines if pagination will be visible
		 * @type {boolean}
		 */
		self.noPagination = false;
		/**
		 *
		 * @type {boolean}
		 */
		self.includeCategorySwitch = null;
		/**
		 * Wheterer or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;
		/**
		 * The type of data the user has selected. For basic search, this is ingredientCode, ingredient statement, or description.
		 * search, it is BDM. For hierarchy search, it is the level of the hierarchy.
		 *
		 * @type {string}
		 */
		self.selectionType = null;
		/**
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;
		/**
		 * Keep full list of ingredient categories
		 * @type {Array}
		 */
		self.categoryList = null;
		/**
		 * The total number of pages in the report.
		 * @type {null}
		 */
		self.totalPages = null;
		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;
		/**
		 * Whether or not to ask the backed for the number of records and pages are available.
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * Whether or not the table has been built. We don't want to build the table until there is something
		 * to search for.
		 * @type {boolean} True if it has and false otherwise.
		 */
		self.tableBuilt = false;
		/**
		 * Array of Category select options.
		 * @type {Array}
		 */
		self.ingredientCategoryIncludeOptions = [{name: "Include", value: true}, {name: "Exclude", value: false}];
		/**
		 * Whether or not user is adding an ingredient.
		 * @type {boolean}
		 */
		self.isAddingIngredient = false;
		/**
		 * Whether or not user is editing an ingredient.
		 * @type {boolean}
		 */
		self.isEditingIngredient = false;
		/**
		 * Previous sub ingredient that was focused.
		 * @type {Object}
		 */
		self.previousFocusedSubIngredient = null;
		/**
		 * Whether or not ingredient was removed.
		 * @type {boolean}
		 */
		self.removedIngredient = false;
		/**
		 * Message to display for deleted action code.
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * Whether or not the user is searching for all action codes.
		 * @type {boolean}
		 */
		self.findAll = false;
		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultsMessage = null;
		/**
		 * Keep track of latest Request for asynchronous calls.
		 * @type {number}
		 */
		self.latestRequest = 0;

		/**
		 * The ngTable object that will be waiting for data while the report of super-ingredients is being refreshed.
		 * @type {?}
		 */
		self.superIngredientsDefer = null;

		/**
		 * The data returned from the request to get all super-ingredients for an ingredient.
		 * @type {array}
		 */
		self.superIngredientsData = null;

		/**
		 * The parameters passed from the ngTable when it is asking for super-ingredients data.
		 * @type {?}
		 */
		self.superIngredientsResolvingParams = null;

		/**
		 * The ingredient the user clicked on to get super-ingredient data.
		 * @type {ingredient}
		 */
		self.selectedIngredient = null;

		/**
		 * Whether or not we're waiting for super-ingredient data.
		 * @type {boolean}
		 */
		self.superIngredientsWaiting = false;

		/**
		 * Whether or not this is the first search for super-ingredients for a particular ingredient.
		 * @type {boolean}
		 */
		self.superIngredientFirstSearch = true;

		/**
		 * Whether or not to include the counts in the request for super-ingredients.
		 * @type {boolean}
		 */
		self.superIngredientsIncludeCounts = true;

		/**
		 * The text to show in the heading for the super-ingredients tabe.
		 * @type {string}
		 */
		self.superIngredientsResultMessage = null;

		/**
		 * The total number of super-ingredient records there are for the selected ingredient.
		 * @type {number}
		 */
		self.superIngredientRecordCount = 0;

		self.superIngredientStack = [];



		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;

		self.currentSearch = null;
		self.currentSearchParameters = "";

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.itemStatus = self.DEFAULT_ITEM_STATUS;
			self.getAllCategories();
			self.selectionType = self.INGREDIENT_CODE;
			if(self.removedIngredient) {
				self.error = null;
				self.data = null;
				self.removedIngredient = false;
			}
		};

		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.superIngredientsData = null;
			self.firstSearch = true;
			self.findAll = findAll;
			self.tableParams.page(1);
			self.tableParams.reload();
			if(self.findAll){
				self.currentSearch = "all";
			}else{
				self.currentSearchParameters = self.searchSelection;
				switch (self.selectionType) {
					case self.INGREDIENT_CODE:
					{
						self.currentSearch = "code";
						break;
					}
					case self.DESCRIPTION:
					{
						self.currentSearch = "description";
						break;
					}
				}
			}
		};

		/**
		 * Returns whether or not the currently logged-in user can edit ingredients.
		 *
		 * @returns {boolean}
		 */
		self.canEditIngredients = function() {
			return permissionsService.getPermissions("SM_INGR_01", "EDIT");
		};

		/**
		 * Set category selected as "COOKING INSTRUCTIONS", with either include or exclude.
		 * @param includeCooking to include or exclude cooking instructions
		 */
		self.includeCooking = function(includeCooking){

				for(var index = 0; index < self.ingredientCategoryIncludeOptions.length; index++){
				if(self.ingredientCategoryIncludeOptions[index].value == includeCooking){
					self.includeCategorySwitch = self.ingredientCategoryIncludeOptions[index];
					for(var index2 = 0; 0 < self.categoryList.length; index2++) {
						if(self.categoryList[index2].normalizedId == 12) {
							self.categorySelected = self.categoryList[index2];
							break;
						}
					}
					break;
				}
			}

		};

		/**
		 * Gets all categories from back end.
		 */
		self.getAllCategories = function () {
			scaleManagementApi.getAllCategoriesList(
				self.loadCategories,
				self.fetchError);
		};

		/**
		 * Returns the text to display in the search box when it is empty.
		 *
		 * @returns {string} The text to display in the search box when it is empty.
		 */
		self.getTextPlaceHolder = function(){
			return 'Enter ' + self.selectionType + 's to search'
		};

		/**
		 * Get report by ingredient code.
		 * @param page page to fetch.
		 */
		self.getReportByIngredientCode = function(page){
			scaleManagementApi.queryByIngredientCode({
					ingredientCodes: self.searchSelection,
					categoryCode: self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected,
					includeCategory: this.getIncludeCategory(),
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
		};

		/**
		 * Get report by ingredient statement.
		 * @param page page to fetch.
		 */
		self.getReportByIngredientStatement = function(page){
			scaleManagementApi.queryByIngredientStatement({
					statementCodes: self.searchSelection,
					categoryCode: self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected,
					includeCategory: this.getIncludeCategory(),
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError)
		};

		/**
		 * Get report by ingredient description.
		 * @param page page to fetch.
		 */
		self.getReportByDescription = function(page){
			scaleManagementApi.queryByIngredientDescription({
					description: self.searchSelection,
					categoryCode: self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected,
					includeCategory: this.getIncludeCategory(),
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			self.superIngredientsWaiting = false;
			self.data = null;
			self.isAddingIngredient = false;
			self.isEditingIngredient = false;
			if (error && error.data) {
				if(error.data.message != null && error.data.message != "") {
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
		 * Populates current category list based on search query string.
		 * @param query search string
		 * @returns {Array}
		 */
		self.getCurrentCategoryList = function(query){
			var currentList = [];
			if (query === null || !query.length || query.length === 0) {
				currentList = self.categoryList;
			} else {
				for (var index = 0; index < self.categoryList.length; index++) {
					if (self.equalsIgnoreCase(self.categoryList[index].displayName, query)) {
						currentList.push(self.categoryList[index]);
					}
				}
			}
			self.currentCategoryList = currentList;
		};

		/**
		 * Populates current ingredient list based on search query string.
		 * @param query search string
		 */
		self.getCurrentIngredientList = function(query){
			var thisRequest = ++self.latestRequest;
			if (!(query === null || !query.length || query.length === 0)) {
				scaleManagementApi.
				getIngredientsByRegularExpression({
						searchString: query,
						currentIngredientCode: self.isAddingIngredient ? "" : self.ingredient.ingredientCode,
						currentIngredientCodeList: self.getCurrentIngredientCodeList(),
						page: 0,
						pageSize: 100
					},

								//success
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.currentIngredientList = results.data;
						}
					},

					//error
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.currentIngredientList = [];
						}
					}
				);
			} else {
				self.currentIngredientList = [];
			}
		};

		/**
		 * Populates current ingredient code list based on search query string.
		 * @param query search string
		 */
		self.getNextIngredientCodeList = function(query){
			var thisRequest = query;
			if (!(query === null || !query.length || query.length === 0 ||
				query.length > self.MAX_INGREDIENT_CODE_LENGTH)) {
				scaleManagementApi.
				getNextIngredientCode({
						searchString: query
					},

					//success
					function (results) {
						if(thisRequest === query) {
							self.nextIngredientCodeList = results;
						}
					},

					//error
					function (results) {
						if(thisRequest === query) {
							self.nextIngredientCodeList = [];
						}
					}
				);
			} else {
				self.nextIngredientCodeList = [];
			}
		};

		/**
		 * Helper comparison function for populating category dropdown.
		 * @param compareString category dropdown string
		 * @param query search string
		 * @returns {Array|{index: number, input: string}}
		 */
		self.equalsIgnoreCase = function(compareString, query){
			return compareString.toUpperCase().match( query.toUpperCase());
		};

		/**
		 * Set data to null.
		 */
		self.resetData = function(){
			self.data = null;
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
		 * Clear search panel back to initial values.
		 */
		self.clearBasicSearch = function () {
			self.searchSelection = '';
			self.includeCategorySwitch = null;
			self.categorySelected = null;
			self.resetView();
		};

		/**
		 * Return current state of include category dropdown
		 * @returns {String}
		 */
		self.getIncludeCategory = function(){
			if(self.includeCategorySwitch != null) {
				switch (self.includeCategorySwitch.value) {
					case true: return self.INCLUDE;
					case false: return self.EXCLUDE;
				}
			} else{
				return self.EMPTY;
			}
		};

		/**
		 * Reset page view by hiding product view, setting the search radio button switch and the tab.
		 *
		 */
		self.resetView = function(){
			self.searchSelection = null;
			self.error = null;
			self.success = null;
		};

		/**
		 * Constructs the table that shows the report.
		 *
		 * @returns {ngTableParams}
		 */
		self.buildTable = function()
		{
			return new ngTableParams(
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
						self.defer = $defer;

							if (self.isCurrentStateNull()) {
							return;
						}
						self.isWaiting = true;
						self.data = null;
						self.modifyMessage = null;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.dataResolvingParams = params;

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
						self.getReportByTab(params.page() -1);
					}
				}
			);
		};

		/**
		 * Constructs the ngTableParams for the super-ingredients table.
		 *
		 * @returns {ngTableParams}
		 */
		self.buildSuperIngredientsTable = function() {
			return new ngTableParams(
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
						if (self.selectedIngredient == null) {
							return;
						}

						self.superIngredientsDefer = $defer;

						self.superIngredientsData = null;
						self.superIngredientsWaiting = true;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.superIngredientsResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.superIngredientFirstSearch){
							self.includeCounts = true;
							params.page(1);
							self.superIngredientFirstSearch = false;
							self.superIngredientsIncludeCounts = true;
						} else {
							self.superIngredientsIncludeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getSuperIngredients(params.page() - 1, self.selectedIngredient);
					}
				}
			);
		};

		/**
		 * Called by the HTML to show the super-ingredients report.
		 *
		 * @param ingredient The ingredient to search for.
		 * @param newSearch Whether or not this is issuing a new search or is drilling down.
		 */
		self.showSuperIngredients = function(ingredient, newSearch) {

			if (newSearch) {
				self.superIngredientStack = [];
				self.selectedIngredient = null;
			}

			if (self.selectedIngredient != null) {
				self.superIngredientStack.push(self.selectedIngredient);
			}

			self.selectedIngredient = ingredient;
			self.superIngredientFirstSearch = true;
			self.superIngredientsTableParams.reload();
		};

		/**
		 * Called by the HTML to display the super-ingredients report for the previously viewed SOI.
		 */
		self.showPreviousSuperIngredients = function() {
			self.selectedIngredient = self.superIngredientStack.pop();
			self.superIngredientFirstSearch = true;
			self.superIngredientsTableParams.reload();
		};

		/**
		 * Calls the backend to retrieve the super-ingredients report.
		 *
		 * @param page The page of data to search for.
		 * @param ingredient The ingredient to search for.
		 */
		self.getSuperIngredients = function(page, ingredient) {
			scaleManagementApi.queryForSuperIngredients({
					ingredientCode: ingredient.ingredientCode,
					includeCounts: self.superIngredientsIncludeCounts,
					pageSize: self.PAGE_SIZE,
					page: page},
				self.loadSuperIngredientData, self.fetchError);
		};

		/**
		 * Callback for when the super-ingredients report returns.
		 *
		 * @param result The list of super-ingredients reqested.
		 */
		self.loadSuperIngredientData = function(result) {
			if (result.complete) {
				self.superIngredientRecordCount = result.recordCount;
				self.superIngredientsResolvingParams.total(result.recordCount);
			}
			self.superIngredientsWaiting = false;
			self.superIngredientsData = result.data;
			self.superIngredientsDefer.resolve(self.superIngredientsData);
			self.superIngredientsResultMessage = "Showing " + (self.PAGE_SIZE * result.page + 1) +
				" - " + (self.PAGE_SIZE * result.page + result.data.length) + " of " +
				self.superIngredientRecordCount.toLocaleString();
		};

		/**
		 * Ng-table params variable for maintaining data.
		 * @type {ngTableParams}
		 */
		self.tableParams = self.buildTable();

		/**
		 * The ngTableParams for the super-ingredients table.
		 *
		 * @type {ngTableParams}
		 */
		self.superIngredientsTableParams = self.buildSuperIngredientsTable();

		/**
		 *  Calls the method to get data based on tab selected.
		 *
		 * @param page The page to get.
		 */
		self.getReportByTab = function(page) {
			if(self.findAll){
				scaleManagementApi.queryAllIngredients({
					categoryCode: self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected,
					includeCategory: this.getIncludeCategory(),
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				}, self.loadData, self.fetchError);
			} else {
				switch (self.selectionType) {
					case self.INGREDIENT_CODE:
					{
						self.getReportByIngredientCode(page);
						break;
					}
					case self.DESCRIPTION:
					{
						self.getReportByDescription(page);
						break;
					}
				}
			}
		};

		/**
		 * Helper function for getData function of buildTable(). If current state of page is considered null, return
		 * true; otherwise false.
		 *
		 * @returns {boolean}
		 */
		self.isCurrentStateNull = function(){
			return self.searchSelection == null &&
				!self.findAll &&
				(!self.isAddingIngredient) &&
				(!self.isEditingIngredient);
		};

		/**
		 * Callback to load data into table.
		 * @param results
		 */
		self.loadData = function(results){
			self.error = null;
			self.success = null;
			self.isWaiting = false;

			// If this was the fist page, it includes record count and total pages .
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No records found.";
			} else if(self.isAddingIngredient) {
				self.isAddingIngredient = false;
				self.modifyMessage = results.message;
				self.resultsMessage = null;
				self.data = [];
				self.data.push(results.data);
				self.resultsMessage = self.getResultMessage(self.totalRecordCount, 0);
				self.noPagination = !(self.data != null && self.totalRecordCount > self.data.length);
				self.defer.resolve(self.data);
			} else if(self.isEditingIngredient){
				self.error = null;
				self.modifyMessage = results.message;
				self.isEditingIngredient = false;
				self.data[self.editingIndex] = results.data;
				self.editingIndex = null;
				self.ingredient = null;
			} else {
				self.resultsMessage = self.getResultMessage(results.data.length, results.page);
				self.data = results.data;
				self.noPagination = !(self.data != null && self.totalRecordCount > self.data.length);
				self.defer.resolve(self.data);
			}
			if(self.removedIngredient) {
				self.removedIngredient = false;
			} else {
				self.deleteMessage = null;
			}
		};

		self.exportIngredientList = function(){
			switch (self.currentSearch) {
				case "all":
				{
					self.exportAllIngredients();
					break;
				}
				case "description":
				{
					self.exportIngredientsByDescription();
					break;
				}
				case "code":
				{
					self.exportIngredientsByCode();
					break;
				}
			}
		}

		/**
		 * Generates an export of the super-ingredients for the selected ingredient.
		 */
		self.exportSuperIngredients = function() {
			self.downloading = true;
			var exportUrl = urlBase + "/pm/ingredient/exportToCSV?ingredientCode=" +
				self.selectedIngredient.ingredientCode;
			var fileName = "ingredient-" + self.selectedIngredient.ingredientCode + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloading = false;
				});
		};

		//Exports the list of ingredients returned by a description search.
		self.exportIngredientsByDescription = function() {
			self.downloadingIngredientList = true;
			var catCode = self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected;
			var exportUrl = urlBase + "/pm/ingredient/exportByDescription?description=" +
				self.currentSearchParameters + "&categoryCode=" + catCode + "&includeCategory=" + this.getIncludeCategory();

			var fileName = "ingredientDescription-" + self.currentSearchParameters + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientList = false;
				});
		};

		//Exports the list of ingredients returned by a description search.
		self.exportIngredientsByCode = function() {
			self.downloadingIngredientList = true;
			var catCode = self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected;
			var exportUrl = urlBase + "/pm/ingredient/exportByCode?ingredientCode=" +
				self.currentSearchParameters + "&categoryCode=" + catCode + "&includeCategory=" + this.getIncludeCategory();

			var fileName = "ingredientCode-" + self.currentSearchParameters + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientList = false;
				});
		};

		//Exports the list of ingredients returned by a description search.
		self.exportAllIngredients = function() {
			self.downloadingIngredientList = true;
			var catCode = self.categorySelected != null ? self.categorySelected.categoryCode : self.categorySelected;
			var exportUrl = urlBase + "/pm/ingredient/exportAllIngredients?categoryCode=" + catCode + "&includeCategory=" + this.getIncludeCategory();
			var fileName = "allIngredients.csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientList = false;
				});
		};

		/**
		 * Loads categories for drop down.
		 * @param results
		 */
		self.loadCategories = function(results){
			self.categoryList = results;
			self.includeCooking(false);
		};

		/**
		 * Make calls to back end to save an ingredient information.
		 */
		self.saveIngredient = function(){
			var ingredient;
			if(self.isAddingIngredient) {
				self.isWaiting = true;
				ingredient = angular.copy(self.ingredient);
				self.totalRecordCount = 1;
				scaleManagementApi.addIngredient(ingredient, self.loadData, self.fetchError)
			} else if(self.isEditingIngredient){
				if(angular.toJson(self.ingredient) != angular.toJson(self.data[self.editingIndex])) {
					self.isWaiting = true;
					ingredient = self.ingredient;
					scaleManagementApi.updateIngredient(ingredient, self.loadData, self.fetchError)
				} else {
					self.error = "No changes detected";
				}
			}
		};

		/**
		 * Add sub ingredient to an ingredient.
		 */
		self.addSubIngredient = function(index){
			self.ingredient.soiFlag = true;
			if(self.ingredient == null) {
				self.ingredient = {ingredientSubs: []};
			} else if(self.ingredient.ingredientSubs == null) {
				self.ingredient.ingredientSubs = [];
			}
			self.ingredient.ingredientSubs.splice(index, 0, {key: {soIngredientCode: 0}})
		};

		/**
		 * Whether ingredient has no sub ingredients.
		 * @returns {boolean}
		 */
		self.hasNoSubIngredient = function(){
			if(self.ingredient != null) {
				if(self.ingredient.ingredientSubs != null){
					if(self.ingredient.ingredientSubs.length > 0){
						return false;
					}
				}
			}
			return true;
		};

		/**
		 * Remove sub-ingredient at specified index.
		 * @param index index of sub-ingredient to remove
		 */
		self.removeSubIngredient = function(index){
			if(self.ingredient != null) {
				if(self.ingredient.ingredientSubs != null){
					if(self.ingredient.ingredientSubs.length > 0){
						if(self.ingredient.ingredientSubs.length == 1){
							self.ingredient.soiFlag = false;
						}
						self.ingredient.ingredientSubs.splice(index, 1);
					}
				}
			}
		};

		/**
		 * Whether required ingredient information has been filled in or not.
		 * @returns {boolean}
		 */
		self.isRequiredIngredientInfoFilled = function(){
			return !(self.ingredient != null && self.ingredient.ingredientCode != null &&
			self.ingredient.ingredientCategory != null &&
			self.ingredient.ingredientDescription != null && !self.isAnySubIngredientNull());
		};

		/**
		 * Whether any sub-ingredient is null or not.
		 * @returns {boolean}
		 */
		self.isAnySubIngredientNull = function () {
			if(self.ingredient != null){
				if(self.ingredient.ingredientSubs != null){
					var obj;
					for(var index = 0; index < self.ingredient.ingredientSubs.length; index++){
						obj = self.ingredient.ingredientSubs[index];
						if(obj == null || obj.subIngredient == null){
							return true;
						}
					}
				}
			}
			return false;
		};

		/**
		 * Initialize a new ingredient.
		 */
		self.createIngredient = function(){
			self.ingredient = {};
			self.ingredient.soiFlag = false;
			self.isAddingIngredient = true;
		};

		/**
		 * Set modal ready to modify ingredient.
		 * @param ingredient
		 * @param index
		 */
		self.editIngredient = function(ingredient, index){
			self.ingredient = angular.copy(ingredient);
			self.editingIndex = index;
			self.isEditingIngredient = true;
		};

		/**
		 * Return of back end after a user removes an ingredient. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedIngredient = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Call back end to remove ingredient.
		 */
		self.removeIngredient = function(){
			self.isWaiting = true;
			scaleManagementApi.deleteIngredient(self.ingredient, self.reloadRemovedData, self.fetchError);
		};

		/**
		 * Reset modal values to default.
		 */
		self.resetModal = function(){
			self.ingredient = null;
			self.isEditingIngredient = false;
			self.isAddingIngredient = false;
		};

		/**
		 * Set ingredientToRemove.
		 */
		self.setRemoveIngredient = function(ingredient){
			self.ingredient = ingredient;
		};

		/**
		 * Reset ingredientToRemove.
		 */
		self.cancelRemove = function(){
			self.ingredient = null;
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
			return "Showing " + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Move a sub-ingredient up or down in the list.
		 *
		 * @param indexFrom index of element to move from
		 * @param indexTo index of element to move to
		 */
		self.moveSubIngredient = function(indexFrom, indexTo){
			self.ingredient.ingredientSubs.splice(indexTo, 0, self.ingredient.ingredientSubs.splice(indexFrom, 1)[0]);
		};

		/**
		 * Move a sub-ingredient up in the list.
		 *
		 * @param indexFrom index of element to move
		 */
		self.moveSubIngredientUp = function(indexFrom){
			var indexTo = indexFrom - 1;
			self.ingredient.ingredientSubs.splice(indexTo, 0, self.ingredient.ingredientSubs.splice(indexFrom, 1)[0]);
		};

		/**
		 * Get current ingredient codes of selected ingredient.
		 */
		self.getCurrentIngredientCodeList = function(){
			var currentIngredientCodeList = [];
			if(self.ingredient.ingredientCode != null && !self.isAddingIngredient) {
				currentIngredientCodeList.push(self.ingredient.ingredientCode);
			}
			var subIngredient;
			for(var index = 0; index < self.ingredient.ingredientSubs.length; index++){
				subIngredient = self.ingredient.ingredientSubs[index];
				if (subIngredient.subIngredient != null && subIngredient.subIngredient.ingredientCode != null) {
					currentIngredientCodeList.push(subIngredient.subIngredient.ingredientCode);
				}
			}
			return currentIngredientCodeList;
		};

		/**
		 * Returns the text to display in the search box when it is empty.
		 *
		 * @returns {string} The text to display in the search box when it is empty.
		 */
		self.getTextPlaceHolder = function(){
			return 'Enter ' + self.selectionType + 's to search'
		};

		/**
		 * Called when the user selects a sub-ingredient. This is needed for when the user is trying to replace
		 * a sub-ingredient in place (rather than remove and delete).
		 *
		 * @param subIngredient The sub-ingredient the user is editing.
		 */
		self.subIngredientSelected = function(subIngredient) {
			subIngredient.key.ingredientCode = subIngredient.subIngredient.ingredientCode;
		};
	}
})();
