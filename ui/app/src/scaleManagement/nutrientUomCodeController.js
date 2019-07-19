/*
 *  nutrientUomCodeController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the nutrient uom codes search.
 *
 * @author vn18422
 * @since 2.16.0
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('NutrientUomCodeController', nutrientUomCodeController);

	nutrientUomCodeController.$inject = ['NutrientUomCodeApi','ngTableParams', 'singlePluPanelService', '$scope'];

	function nutrientUomCodeController(nutrientUomCodeApi, ngTableParams, singlePluPanelService, $scope) {
		var self = this;

		/**
		 * Constant used to state the view currently is not looking at a single PLU
		 * @type {string}
		 */
		const NOT_SINGLE_PLU_VIEW = "notSingleView";
		// Search string constants.
		self.NUTRIENT_UOM_CODE = "Nutrient UOM code";
		self.DESCRIPTION = "nutrient uom code description";
		self.VARIES_LABEL = " varies";

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
		 * Tracks whether or not the user is waiting for data from the back end regarding UOM Assocations with nutrients/nutrient statments..
		 *
		 * @type {boolean}
		 */
		self.uomAssociationsIsWaiting = false;

		/**
		 * Tracks whether or not the user is waiting for data from the back end regarding UOM Assocations with nutrients/nutrient statments..
		 *
		 * @type {boolean}
		 */
		self.stateAssociationsIsWaiting = false;

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
		 * The type of data the user has selected. Either nutrient uom code or description.
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
		 * The promise given to the ng-tables getData method. It should be called after data is fetched.
		 *
		 * @type {null}
		 */
		self.nutrientAssociationDefer = null;

		/**
		 * The nutrient data being shown in the report.
		 * @type {Array}
		 */
		self.nutrientAssociationData = null;

		/**
		 * Defines whether or not this is the first search for a set of nutrients by UOM code.
		 * @type {boolean}
		 */
		self.nutrientAssociationFirstSearch = false;

		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */
		self.nutrientAssociationResolvingParams = null;

		/**
		 * The currently selected UOM.
		 * @type {null}
		 */
		self.selectedUOM = null;

		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130) in the Nutrients Table.
		 * @type {String}
		 */
		self.nutrientAssociationResultMessage = null;

		/**
		 * The promise given to the ng-tables getData method. It should be called after data is fetched.
		 *
		 * @type {null}
		 */
		self.statementAssociationDefer = null;

		/**
		 * The nutrient data being shown in the report.
		 * @type {Array}
		 */
		self.statementAssociationData = null;

		/**
		 * Defines whether or not this is the first search for a set of nutrients by UOM code.
		 * @type {boolean}
		 */
		self.statementAssociationFirstSearch = false;

		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */
		self.statementAssociationResolvingParams = null;

		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130) in the Nutrients Table.
		 * @type {String}
		 */
		self.statementAssociationResultMessage = null;

		/**
		 * The nutrient uom code data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;
		/**
		 * The nutrient uom code for the plu search results.
		 * @type {number}
		 */
		self.pluNutrientUomCode = null;
		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;
		/**
		 * Whether or not the user is searching for all nutrient uom codes.
		 * @type {boolean}
		 */
		self.findAll = false;
		/**
		 * Whether or not a user is adding an nutrient uom code.
		 * @type {boolean}
		 */
		self.isAddingNutrientUomCode = false;
		/**
		 * Index of data that is being edited.
		 * @type {number}
		 */
		self.previousEditingIndex = null;
		/**
		 * Original state of nutrient uom code being edited.
		 * @type {null}
		 */
		self.currentEditingObject = null;
		/**
		 * Whether or not the nutrient uom code was removed.
		 * @type {boolean}
		 */
		self.removedNutrientUomCode = false;
		/**
		 * Message to display for deleted nutrient uom code.
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * If nutrient uom code is being changed.
		 * @type {boolean}
		 */
		self.nutrientUomCodeChanged = false;
		/**
		 * Keep track of latest Request for asynchronous calls.
		 * @type {number}
		 */
		self.latestRequest = 0;
		/**
		 * Used to store to current state of the view if it is looking at a single PLU or not
		 * @type {string}
		 */
		self.currentView="";

		/**
		 * Initializes the controller.
		 */
		self.init = function () {
			self.currentView=NOT_SINGLE_PLU_VIEW;
			singlePluPanelService.setCurrentView(self.currentView);
			if(self.removedNutrientUomCode || self.isAddingNutrientUomCode) {
				self.error = null;
				self.data = null;
				self.searchPanelVisible = true;
				self.isAddingNutrientUomCode = false;
				self.removedNutrientUomCode = false;
			}
			self.resetView(self.NUTRIENT_UOM_CODE);
		};

		/**
		 * Is current state not waiting and looking at multiple records.
		 * @returns {boolean}
		 */
		self.isNotViewingSinglePlu = function(){
			self.currentView=singlePluPanelService.getCurrentView();
			return !self.isWaiting && self.data != null && self.currentView == NOT_SINGLE_PLU_VIEW;
		};

		/**
		 * Constructs the ng-table.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE,
					SortColumn:"SCALE_NUTRIENT_UOM_CD",
					SortDirection: "ASC"
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
				(!self.isAddingNutrientUomCode || self.data[0].nutrientUomDescription == null) &&
				self.previousEditingIndex == null;
		};

		/**
		 * Reset page view by hiding result screens and setting the search radio button switch.
		 *
		 * @param selectionType The type of data in the list - description or nutrient uom code.
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
			self.isAddingNutrientUomCode = false;
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
		 * Issue call to newSearch to call back end to fetch all nutrient uom codes.
		 */
		self.searchAll = function (){
			self.restSelectedValues();
			self.clearSearch();
			self.newSearch(true);
		};

		/**
		 * Callback for a successful call to get nutrient uom code data from the backend.
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
			} else if(self.isAddingNutrientUomCode && self.nutrientUomCodeChanged) {
				self.error = null;
				self.modifyMessage = results.message;
				self.resultMessage = null;
				self.error = null;
				self.isAddingNutrientUomCode = false;
				self.data = [];
				self.data.push(results.data);
				self.defer.resolve(results.data);
			} else if(self.previousEditingIndex != null && self.nutrientUomCodeChanged){
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
			if(self.removedNutrientUomCode) {
				self.removedNutrientUomCode = false;
			} else {
				self.deleteMessage = null;
			}

			self.nutrientUomCodeChanged = false;
			self.searchPanelVisible = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.isWaiting = false;
			self.uomAssociationsIsWaiting = false;
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
				self.modifyError("An unknown error occurred.");
			}
		};

		/**
		 * Sets up the ng-table for a nutrient uom code search.
		 */
		self.newSearch = function (findAll) {
			self.restSelectedValues();
			singlePluPanelService.setCurrentView(NOT_SINGLE_PLU_VIEW);
			self.modifyError = null;
			self.firstSearch = true;
			self.pluNutrientUomCodeCode = null;
			self.findAll = findAll;
			self.isAddingNutrientUomCode = false;
			self.previousEditingIndex = null;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Sets up the ng-table for a nutrient uom code search.
		 */
		self.nutrientUomCodeChanges = function () {
			self.pluNutrientUomCode = null;
			self.nutrientUomCodeChanged = true;
			self.modifyNutrientUomCode();
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
		 * Selects the appropriate nutrient uom code search by the search type specified (description of nutrient uom cd number).
		 *
		 * @param page The page number requested.
		 */
		self.getReportBySearchType = function(page) {
			if(self.findAll){
				nutrientUomCodeApi.queryByAllNutrientUomCodes({
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.NUTRIENT_UOM_CODE && self.searchSelection !== null) {
				nutrientUomCodeApi.queryByNutrientUomCodes({
					nutrientUomCodes: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE,
					sortColumn:"SCALE_NUTRIENT_UOM_CD",
					sortDirection:"ASC"
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.DESCRIPTION && self.searchSelection !== null) {
				nutrientUomCodeApi.queryByNutrientUomCodeDescription({
					description: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			}
		};

		/**
		 * Calls load data and find hits. Used on success to prevent find hits to be called whenever the find by
		 * nutrient uom code fails.
		 * @param results The results.
		 */
		self.loadDataAndFindHits = function(results){
			self.loadData(results);
			nutrientUomCodeApi.findHitsByNutrientUomCodes({
				nutrientUomCodes: self.searchSelection}, self.showMissingData, self.fetchError);
		};
		/**
		 * Decide whether to add nutrientUomCode or update nutrientUomCode.
		 */
		self.modifyNutrientUomCode = function(){
			if(self.isAddingNutrientUomCode) {
				self.addNutrientUomCode();
			} else if(self.previousEditingIndex != null){
				self.updateNutrientUOmCode();
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
		 * Only show match count information for searches by nutrient uom code (not description).
		 *
		 * @returns {boolean} True if the search is by nutrient uom code.
		 */
		self.showMatchCount = function(){
			return self.selectionType === self.NUTRIENT_UOM_CODE;
		};

		/**
		 * Add an empty row to the nutrient uom code data.
		 */
		self.addRow = function(){
			self.restSelectedValues();
			self.resetDefaults();
			self.searchPanelVisible = false;
			self.deleteMessage = null;
			self.pluNutrientUomCode = null;
			self.isAddingNutrientUomCode = true;
			self.data = [];
			nutrientUomCodeApi.getNextNutrientUomCode({},
				loadNewRow,
				self.fetchModifiedError);
		};

		function loadNewRow(result){
			var newNutrientUOM = {
				nutrientUomCode : result.data,
				maintenanceDate : $scope.convertDate(new Date())
			};
			self.data.push(newNutrientUOM);
			self.tableParams.data = [];
		}

		/**
		 * Send data to back end to add a new nutrient uom code.
		 */
		self.addNutrientUomCode = function(){
			self.modifyMessage = null;
			self.modifyError = null;
			if(self.isAddingNutrientUomCode){
				var nutrientUomCode = self.data[0];
				self.totalRecordCount = null;
				nutrientUomCodeApi.addNutrientUomCode(nutrientUomCode, self.loadData, self.fetchModifiedError)
			}
		};

		/**
		 * Allows the user to edit an nutrient uom code. This method alters the state of an nutrient uom code's description to
		 * be able to be edited.
		 *
		 * @param index Index of current data to be edited.
		 */
		self.editNutrientUomCode = function(index){
			if(self.previousEditingIndex != null){
				self.data[self.previousEditingIndex].isEditing = false;
			}
			self.previousEditingIndex = index;
			self.currentEditingObject = angular.copy(self.data[index]);
			self.data[index].isEditing = true;
		};

		/**
		 * If a change was detected, this method sends the nutrient uom code to the back end to be updated. If not, an
		 * error message stating 'No difference detected' will be displayed.
		 */
		self.updateNutrientUOmCode = function(){
			var nutrientUomCodes = self.data[self.previousEditingIndex];

			if((nutrientUomCodes.nutrientUomDescription !== self.currentEditingObject.nutrientUomDescription) ||
				(nutrientUomCodes.extendedDescription !== self.currentEditingObject.extendedDescription) ||
				(nutrientUomCodes.form !== self.currentEditingObject.form) ||
				(nutrientUomCodes.systemOfMeasure !== self.currentEditingObject.systemOfMeasure))

			{
				nutrientUomCodeApi.updateNutrientUomCode(nutrientUomCodes,
					self.loadData, self.fetchModifiedError
				);
			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Return of back end after a user removes an nutrient uom  code. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedNutrientUomCode = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Send nutrient uom code to back end to be deleted.
		 *
		 * @param index Index of data to be removed.
		 */
		self.removeNutrientUomCode = function(index){
			var nutrientUomCodes = self.data[index];
			self.isWaiting = true;
			nutrientUomCodeApi.deleteNutrientUomCode(nutrientUomCodes, self.reloadRemovedData,  self.fetchError)
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

		/**
		 * Sets the controller's error message.
		 *
		 * @param nutrientUOMCode The UoM code being sought.
		 * @param
		 */
		self.showUOMAssociations = function(nutrientUOMCode) {
			self.selectedUOM = nutrientUOMCode;
			self.nutrientAssociationFirstSearch = true;
			self.nutrientAssociationTableParams.reload();
			self.statementAssociationTableParams.reload();
		};


		/**
		 * Callback for a successful call to get nutrient uom code data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.buildNutrientAssociationsTable = function() {
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
						if (self.selectedUOM == null) {
							return;
						}

						self.nutrientAssociationDefer = $defer;

						self.nutrientAssociationData = null;
						self.uomAssociationsIsWaiting = true;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.nutrientAssociationResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.nutrientAssociationFirstSearch){
							self.includeCounts = true;
							params.page(1);
							self.nutrientAssociationFirstSearch = false;
							self.nutrientAssociationIncludeCounts = true;
						} else {
							self.nutrientAssociationIncludeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getNutrientAssociations(params.page() - 1, self.selectedUOM);
					}
				}
			);
		};

		/**
		 * Callback for a successful call to get nutrient uom code data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.buildStatementAssociationsTable = function() {
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
						if (self.selectedUOM == null) {
							return;
						}

						self.statementAssociationDefer = $defer;

						self.statementAssociationData = null;
						self.statementAssociationsIsWaiting = true;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.statementAssociationResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.nutrientAssociationFirstSearch){
							self.includeCounts = true;
							params.page(1);
							self.statementAssociationFirstSearch = false;
							self.statementAssociationIncludeCounts = true;
						} else {
							self.statementAssociationIncludeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getNutrientStatementsAssociations(params.page() - 1, self.selectedUOM);
					}
				}
			);
		};

		self.nutrientAssociationTableParams = self.buildNutrientAssociationsTable();
		self.statementAssociationTableParams = self.buildStatementAssociationsTable();

		/**
		 * Calls the backend to retrieve the nutrient statements report.
		 *
		 * @param page The page of data to search for.
		 * @param uomCodeParam The uom code to search for.
		 */
		self.getNutrientStatementsAssociations = function(page, uomCodeParam) {
			nutrientUomCodeApi.queryForAssociatedNutrientStatements({
					uomCode: uomCodeParam.nutrientUomCode,
					includeCounts: true,
					pageSize: self.PAGE_SIZE,
					page: page},
				self.loadNutrientStatementAssociationsData, self.fetchError);
		};

		/**
		 * Calls the backend to retrieve the super-ingredients report.
		 *
		 * @param page The page of data to search for.
		 * @param ingredient The ingredient to search for.
		 */
		self.getNutrientAssociations = function(page, uomCodeParam) {
			nutrientUomCodeApi.queryForAssociatedNutrients({
					uomCode: uomCodeParam.nutrientUomCode,
					includeCounts: true,
					pageSize: self.PAGE_SIZE,
					page: page},
				self.loadNutrientAssociationsData, self.fetchError);
		};

		/**
		 * Callback for when the nutrient report returns.
		 *
		 * @param result The list of nutrients requested.
		 */
		self.loadNutrientAssociationsData = function(result) {
			if (result.complete) {
				self.nutrientRecordCount = result.recordCount;
				self.nutrientAssociationResolvingParams.total(result.recordCount);
			}
			self.uomAssociationsIsWaiting  = false;
			self.nutrientAssociationData  = result.data;
			self.nutrientAssociationDefer.resolve(self.nutrientAssociationData);
			self.nutrientAssociationResultMessage = "Showing " + (self.PAGE_SIZE * result.page + 1) +
				" - " + (self.PAGE_SIZE * result.page + result.data.length) + " of " +
				self.nutrientRecordCount.toLocaleString();
		};

		/**
		 * Callback for when the nutrient statement report returns.
		 *
		 * @param result The list of nutrient statements reqested.
		 */
		self.loadNutrientStatementAssociationsData = function(statementResult) {
			if (statementResult.complete) {
				self.statementRecordCount = statementResult.recordCount;
				self.statementAssociationResolvingParams.total(statementResult.recordCount);
			}
			self.statementAssociationsIsWaiting  = false;
			self.statementAssociationData  = statementResult.data;
			self.statementAssociationDefer.resolve(self.statementAssociationData);
			self.statementAssociationResultMessage = "Showing " + (self.PAGE_SIZE * statementResult.page + 1) +
				" - " + (self.PAGE_SIZE * statementResult.page + statementResult.data.length) + " of " +
				self.statementRecordCount.toLocaleString();
		};

		/**
		 *  reset the values for the last selected UOM
		 */
		self.restSelectedValues = function(){
			self.selectedUOM = null;
			self.nutrientAssociationData = null;
			self.statementAssociationData = null;
		};

		/**
		 * Restrict user to numeric characters only if "Nutrient UOM code" is selected
		 *
		 * @returns {string} Pattern
		 */
		self.setPatternForNutrientUomCode = function(){
			if(self.selectionType === "Nutrient UOM code"){
				return "[0-9\\s,]+";
			}
		};

		/**
		 * checks to see if nutrient association data or statement association data is fetched from backend
		 * @returns {boolean}
		 */
		self.isWaitingForAssociations = function(){
			return self.uomAssociationsIsWaiting || self.statementAssociationsIsWaiting;
		};

		/**
		 * if a UOM is selected, check to see if association data is fetched from the backend
		 * @returns {null|boolean}
		 */
		self.isUOMSelectedAndNotWaitingForAssociations = function(){
			return self.selectedUOM && !self.isWaitingForAssociations();
		};
	}
})();
