/*
 *  ingredientStatementsController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

/**
 * Controller to support the ingredient statement codes search.
 *
 * @author s573181
 * @since 2.0.8
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('IngredientStatementsController', ingredientStatementsController);

	ingredientStatementsController.$inject = ['ScaleManagementApi','ngTableParams', '$scope', 'PermissionsService', '$stateParams', 'singlePluPanelService', 'DownloadService', 'urlBase'];

	function ingredientStatementsController(scaleManagementApi, ngTableParams, $scope, permissionsService, $stateParams, singlePluPanelService, downloadService, urlBase) {
		var self = this;

		// Search string constants.
		self.INGREDIENT_STATEMENT = "ingredient statement";
		self.INGREDIENT_CODE = "ingredient code";
		self.DESCRIPTION = "description";
		self.ORDERED_CODE_LIST = "orderedCodeList";

		/**
		 * Used to show that the current state is not looking at a single PLU
		 * @type {string}
		 */
		const IS_NOT_SINGLE_PLU = "notSingleView";
		const MINUTES_TO_MILLISECONDS = 60000;

		/**
		 * Informational text for the user adding sub-ingredients.
		 * @type {string}
		 */
		self.SUB_INGREDIENT_TEXT = "A sub-ingredient cannot be added that exists as a sub-ingredient already, " +
			"or as a sub-ingredient of those sub-ingredients. An ingredient also cannot be added as a " +
			"sub-ingredient if it exists as a super ingredient, or a super ingredient of those super ingredients.";

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
		 * The type of data the user has selected. Either ingredient statement or code.
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
		 * The ingredient statement code data being shown in the report.
		 *
		 * @type {Array}
		 */
		self.data = null;
		/**
		 * Whether or not this is the first search with the current parameters.
		 *
		 * @type {boolean}
		 */
		self.firstSearch = true;
		/**
		 * Whether or not the user is searching for all ingredient statement codes.
		 *
		 * @type {boolean}
		 */
		self.findAll = false;
		/**
		 * Whether or not a user is adding an ingredient statement.
		 *
		 * @type {boolean}
		 */
		self.isAddingIngredientStatement = false;
		/**
		 * Index of data that is being edited.
		 *
		 * @type {number}
		 */
		self.editingIndex = null;
		/**
		 * Original state of ingredient statement code being edited.
		 *
		 * @type {null}
		 */
		self.currentEditingObject = null;
		/**
		 * Whether or not the ingredient statement was removed.
		 *
		 * @type {boolean}
		 */
		self.removedIngredientStatement = false;
		/**
		 * Message to display for deleted ingredient statement code.
		 *
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * If ingredient statement code is being changed.
		 *
		 * @type {boolean}
		 */
		self.ingredientStatementChanged = false;
		/**
		 * Keep track of latest Request for asynchronous calls.
		 *
		 * @type {number}
		 */
		self.latestRequest = 0;
		/**
		 * Whether or not maintenance date picker is visible.
		 *
		 * @type {boolean}
		 */
		self.maintenanceDatePickerOpened = false;
		/**
		 * The max length of a statement code.
		 *
		 * @type {number}
		 */
		self.MAX_STATEMENT_CODE_LENGTH = 7;
		/**
		 * Whether every row is selected.
		 *
		 * @type {boolean}
		 */
		self.selectAll = false;

		/**
		 * The ingredient statement header the user has selected.
		 *
		 * @type {IngredientStatementHeader}
		 */
		self.ingredientStatement = null;
		/**
		 * Used to store the current state of the view if it is currently looking at a single PLU or not
		 * @type {string}
		 */
		self.currentView="";

		self.currentSearch = null;
		self.currentSearchParameters = "";


		/**
		 * The departments array.
		 *
		 * @type {string[]}
		 */
		self.departmentArray = [
			{id: 131, isSelected: false},
			{id: 151, isSelected: false},
			{id: 161, isSelected: false},
			{id: 213, isSelected: false},
			{id: 301, isSelected: false},
			{id: 601, isSelected: false},
			{id: 603, isSelected: false},
			{id: 701, isSelected: false},
			{id: 901, isSelected: false}];


		/**
		 * Initializes the controller.
		 */
		self.init = function () {
			self.currentView=IS_NOT_SINGLE_PLU;
			singlePluPanelService.setCurrentView(self.currentView);
			self.resetView(self.INGREDIENT_STATEMENT);
			if(self.removedIngredientStatement) {
				self.error = null;
				self.data = null;
				self.searchPanelVisible = true;
				self.removedIngredientStatement = false;
			}
			if($stateParams && $stateParams.ingStmtCode){
				self.searchSelection = $stateParams.ingStmtCode;
				self.newSearch();
			}
		};

		/**
		 * Is current state not waiting and looking at multiple records.
		 * @returns {boolean}
		 */
		self.isNotViewingSinglePlu = function(){
			self.currentView=singlePluPanelService.getCurrentView();
			return !self.isWaiting && self.data != null && self.currentView == IS_NOT_SINGLE_PLU;
		};

		self.showPlus = function(ingredientStatement) {
			self.ingredientStatement = ingredientStatement;
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
						self.data = null;
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (self.firstSearch) {
							self.includeCounts = true;
							self.firstSearch = false;
						}
						self.getStatementBySearchType(params.page() - 1);
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
			return self.searchSelection == null && !self.findAll && !self.isAddingIngredientStatement && self.editingIndex == null;
		};

		/**
		 * Reset page view by hiding result screens and setting the search radio button switch.
		 *
		 * @param selectionType The type of data in the list - Ingredient category or ingredient code.
		 */
		self.resetView = function (selectionType) {
			self.resetDefaults();
			self.selectionType = selectionType;
		};

		/**
		 * Reset default values.
		 */
		self.resetDefaults = function(){
			self.ingredientStatement = null;
			self.searchSelection = null;
			self.error = null;
			self.success = null;
			self.editingIndex = null;
			self.isAddingIngredientStatement = false;
			self.maintenanceDatePickerOpened = false;
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
		 * Issue call to newSearch to call back end to fetch all ingredient statement codes.
		 */
		self.searchAll = function (){
			self.clearSearch();
			self.newSearch(true);
		};

		/**
		 * Callback for a successful call to get ingredient statement code data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function (results) {
			self.searchedCodes = self.searchSelection;
			self.selectedType = self.selectionType;
			self.success = null;
			self.isWaiting = false;
			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.error = "No records found.";
			} else if(self.isAddingIngredientStatement && self.ingredientStatementChanged) {
				self.success = results.message;
				self.resultMessage = null;
				self.error = null;
				self.resetModal();
				self.data = [];
				self.data.push(results.data);
				self.defer.resolve(results.data);
			} else if(self.editingIndex != null && self.ingredientStatementChanged){
				self.error = null;
				self.success = results.message;
				self.data[self.editingIndex].isEditing = false;
				self.data[self.editingIndex] = results.data;
				self.editingIndex = null;
				self.resetModal();
				self.currentEditingObject = null;
				self.defer.resolve(results.data);
			} else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
			if(self.removedIngredientStatement) {
				self.removedIngredientStatement = false;
			} else {
				self.deleteMessage = null;
			}
			self.ingredientStatementChanged = false;
			if (results.data.length != 0) {
				self.searchPanelVisible = false;
			}
			self.isEditing = false;
			self.isAddingIngredientStatement = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function (error) {
			self.success = null;
			self.isWaiting = false;
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
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
		};

		/**
		 * Sets up the ng-table for a ingredient statement code search.
		 */
		self.newSearch = function (findAll) {
			singlePluPanelService.setCurrentView(IS_NOT_SINGLE_PLU);
			self.ingredientStatement = null;
			self.firstSearch = true;
			self.findAll = findAll;
			self.isAddingIngredientStatement = false;
			self.editingIndex = null;
			self.tableParams.page(1);
			self.tableParams.reload();

			if(self.findAll){
				self.currentSearch = "all";
			}else{
				self.currentSearchParameters = self.searchSelection;
				switch (self.selectionType) {
					case self.DESCRIPTION:
					{
						self.currentSearch = "description";
						break;
					}
					case self.INGREDIENT_STATEMENT:
					{
						self.currentSearch = "statementCode";
						break;
					}
					case self.INGREDIENT_CODE:
					{
						self.currentSearch = "ingredientCode";
						break;
					}
					case self.ORDERED_CODE_LIST:
					{
						self.currentSearch = "orderedCodes";
						break;
					}
				}
			}
		};

		/**
		 * Polymorphic export call which decides based on the previous search which specific export function to call
		 */
		self.exportIngredientStatementList = function(){
			switch (self.currentSearch) {
				case "all":
				{
					self.exportAllIngredientStatements();
					break;
				}
				case "description":
				{
					self.exportIngredientStatementsByDescription();
					break;
				}
				case "statementCode":
				{
					self.exportIngredientStatementsByStatementCode();
					break;
				}
				case "ingredientCode":
				{
					self.exportIngredientStatementsByIngredientCode();
					break;
				}
				case "orderedCodes":
				{
					self.exportIngredientStatementsByOrderedCodes();
					break;
				}
			}
		}

		/**
		 * Exports the list of all ingredients statements.
		 */
		self.exportAllIngredientStatements = function() {
			self.downloadingIngredientStatementList = true;
			var exportUrl = urlBase + "/pm/ingredientStatement/exportAll";
			var fileName = "allIngredientStatements.csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientStatementList = false;
				});
		};

		/**
		 * Exports Ingredient statements by returned by a description search.
		 */
		self.exportIngredientStatementsByDescription = function() {
			self.downloadingIngredientStatementList = true;
			var exportUrl = urlBase + "/pm/ingredientStatement/exportByDescription?description=" + self.currentSearchParameters;
			var fileName = "ingredientStatementDescription-" + self.currentSearchParameters + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientStatementList = false;
				});
		};

		/**
		 * Exports Ingredient statements by returned by a statement code search.
		 */
		self.exportIngredientStatementsByStatementCode = function() {
			self.downloadingIngredientStatementList = true;
			var exportUrl = urlBase + "/pm/ingredientStatement/exportByStatementCode?code=" + self.currentSearchParameters;

			var fileName = "ingredientStatementCode-" + self.currentSearchParameters + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientStatementList = false;
				});
		};

		/**
		 * Exports the list of ingredients statements returned by an ingredient code search.
		 */
		self.exportIngredientStatementsByIngredientCode = function() {
			self.downloadingIngredientStatementList = true;
			var exportUrl = urlBase + "/pm/ingredientStatement/exportByIngredientCode?ingredientCode=" +
				self.currentSearchParameters;

			var fileName = "ingredientCode-" + self.currentSearchParameters + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientStatementList = false;
				});
		};

		/**
			Exports Ingredient statements by returned by a description search.
		 */
		self.exportIngredientStatementsByOrderedCodes = function() {
			self.downloadingIngredientStatementList = true;
			var exportUrl = urlBase + "/pm/ingredientStatement/exportByOrderedCodes?orderedCodes=" +
				self.currentSearchParameters;

			var fileName = "orderedCodes-" + self.currentSearchParameters + ".csv";

			downloadService.export(exportUrl, fileName, self.WAIT_TIME,
				function() {
					self.downloadingIngredientStatementList = false;
				});
		};

		/**
		 * Sets up the ng-table for a ingredient statement code search.
		 */
		self.ingredientStatementChanges = function () {
			self.modifyIngredientStatement();
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
		 * Selects the appropriate search by the search type specified.
		 *
		 * @param page The page number requested.
		 */
		self.getStatementBySearchType = function(page) {
			if(self.findAll){
				scaleManagementApi.queryAllIngredientStatements({
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.INGREDIENT_STATEMENT && self.searchSelection !== null) {
				scaleManagementApi.queryIngredientStatementByStatementCode({
					ingredientStatements: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
				scaleManagementApi.queryIngredientStatementsForMissingIngredientStatements({
					ingredientStatements: self.searchSelection}, self.showMissingData, self.fetchError);
			}else if (self.selectionType === self.INGREDIENT_CODE && self.searchSelection !== null) {
				scaleManagementApi.queryIngredientStatementByIngredientCode({
					ingredientCodes: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.DESCRIPTION && self.searchSelection !== null){
				scaleManagementApi.queryIngredientStatementByDescription({
					description: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			} else if (self.selectionType === self.ORDERED_CODE_LIST && self.searchSelection !== null){
				scaleManagementApi.queryIngredientStatementByIngredientCodeOrdered({
					ingredientCodes: self.searchSelection,
					includeCounts: self.includeCounts,
					page: page,
					pageSize: self.PAGE_SIZE
				}, self.loadData, self.fetchError);
			}
		};

		/**
		 * Decide whether to add an ingredient statement or update an ingredient statement.
		 */
		self.modifyIngredientStatement = function(){
			if(self.isAddingIngredientStatement) {
				self.ingredientStatementChanged = true;
				self.saveIngredient();
			} else if(self.editingIndex != null && self.isEditingIngredientStatement){
				self.saveIngredient();
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
		 * Only show match count information for searches by ingredient statement code.
		 *
		 * @returns {boolean} True if the search is by ingredient statement code.
		 */
		self.showMatchCount = function(){
			return !self.findAll && self.selectionType === self.INGREDIENT_STATEMENT;
		};

		/**
		 * Send data to back end to add a new ingredient statement code.
		 */
		self.addIngredientStatement = function(){

			self.searchPanelVisible = true;

			// If they are doing an ordered search and have some values there, then we'll use that
			// as the base of a new ingredient statement.
			if (self.selectionType === self.ORDERED_CODE_LIST && self.searchSelection !== null) {
				scaleManagementApi.newIngredientStatement({ingredientCodes: self.searchSelection},
				self.handleNewIngredientStatement, self.showError);
			}
			// Otherwise, just throw up a blank one for them to edit.
			else {
				self.isAddingIngredientStatement = true;
				self.data = null;
				self.selectedIngredientStatement = {};
			}
		};

		/**
		 * Callback for when the user adds a new ingredient statement that already has ingredient codes tied to it.
		 *
		 * @param results The new ingredient statment sent from the back end.
		 */
		self.handleNewIngredientStatement = function(results) {
			self.data = null;
			self.selectedIngredientStatement = results;
			self.isAddingIngredientStatement = true;
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
		 * Set modal ready to modify ingredient.
		 *
		 * @param ingredientStatement The ingredient statement to edit.
		 * @param index The index of the ingredient statement to edit.
		 */
		self.editIngredientStatement = function(ingredientStatement, index){
			self.selectedIngredientStatement = angular.copy(ingredientStatement);
			self.editingIndex = index;
			self.isEditingIngredientStatement = true;
			self.getSingleView(ingredientStatement,index);
		};

		/**
		 * If a change was detected, this method sends the ingredient statement code to the back end to be updated.
		 * If not, an error message stating 'No difference detected' will be displayed.
		 */
		self.updateIngredientStatement = function(){
			var statement = self.data[self.editingIndex];
			var difference = angular.toJson(statement) != angular.toJson(self.selectedIngredientStatement);
			if(difference){
				self.ingredientStatementChanged = true;
				scaleManagementApi.updateIngredientStatement(self.selectedIngredientStatement,
					self.loadData, self.fetchError
				);
			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Return of back end after a user removes an ingredient statement code. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedIngredientStatement = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Send ingredient statement code to back end to be deleted.
		 */
		self.removeIngredientStatement = function(){
			var ingredientStatementCode = self.data[self.editingIndex].statementNumber;
			var deptList = [];
			for(var index = 0; index < self.departmentArray.length; index++){
				if(self.departmentArray[index].isSelected == true){
					deptList.push(self.departmentArray[index].id);
				}
			}
			self.isWaiting = true;
			scaleManagementApi.deleteIngredientStatement({
					statementCode: ingredientStatementCode,
					deptList: deptList
				},
				self.reloadRemovedData, self.fetchError)
		};

		/**
		 * Whether an ingredient has not been filled in, so user cannot add another ingredient.
		 *
		 * @returns {boolean}
		 */
		self.isAnyIngredientsNull = function(){
			if(self.selectedIngredientStatement != null) {
				if(self.selectedIngredientStatement.ingredientStatementDetails != null){
					if(self.selectedIngredientStatement.ingredientStatementDetails.length > 0) {
						for (var x = 0; x < self.selectedIngredientStatement.ingredientStatementDetails.length; x++) {
							var obj = self.selectedIngredientStatement.ingredientStatementDetails[x];
							if(obj == null || obj.ingredient == null){
								return true;
							}
						}
					}
				}
			}
			return false;
		};

		/**
		 * Populates current ingredient list based on search query string.
		 *
		 * @param query search string
		 */
		self.getCurrentIngredientList = function(query){
			var thisRequest = ++self.latestRequest;
			if (!(query === null || !query.length || query.length === 0)) {
				scaleManagementApi.
				getIngredientsByRegularExpression({
						searchString: query,
						currentIngredientStatementCode: self.isAddingIngredientStatement ? "" : self.selectedIngredientStatement.statementNumber,
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
		 * Move a sub-ingredient up or down in the list.
		 *
		 * @param indexFrom index of element to move from
		 * @param indexTo index of element to move to
		 */
		self.moveIngredient = function(indexFrom, indexTo){
			self.selectedIngredientStatement.ingredientStatementDetails.splice(indexTo, 0, self.selectedIngredientStatement.ingredientStatementDetails.splice(indexFrom, 1)[0]);
		};

		/**
		 * Remove sub-ingredient at specified index.
		 *
		 * @param index index of sub-ingredient to remove
		 */
		self.removeIngredient = function(index){
			if(self.selectedIngredientStatement != null) {
				if(self.selectedIngredientStatement.ingredientStatementDetails != null){
					self.selectedIngredientStatement.ingredientStatementDetails.splice(index, 1);
				}
			}
		};

		/**
		 * Reset modal values to default.
		 */
		self.resetModal = function(){
			self.selectedIngredientStatement = null;
			self.isEditingIngredientStatement = false;
			self.isAddingIngredientStatement = false;
		};

		/**
		 * Make calls to back end to save an ingredient information.
		 */
		self.saveIngredient = function(){
			var ingredientStatement;
			self.isWaiting = true;
			if(self.isAddingIngredientStatement) {
				ingredientStatement = angular.copy(self.selectedIngredientStatement);
				ingredientStatement.maintenanceDate = $scope.convertDate(ingredientStatement.maintenanceDate);
				self.totalRecordCount = null;
				scaleManagementApi.addIngredientStatement(ingredientStatement, self.loadData, self.fetchError)
			} else if(self.isEditingIngredientStatement){
				var statement = self.data[self.editingIndex];
				var difference = angular.toJson(statement) != angular.toJson(self.selectedIngredientStatement);
				if(difference){
					var editedStatement = angular.copy(self.selectedIngredientStatement);
					editedStatement.maintenanceDate = $scope.convertDate(editedStatement.maintenanceDate);
					self.ingredientStatementChanged = true;
					scaleManagementApi.updateIngredientStatement(editedStatement,
						self.loadData, self.fetchError
					);
				} else {
					self.error = "No difference detected";
				}
			}
		};

		/**
		 * Reset ingredientToRemove.
		 */
		self.cancelRemove = function(){
			self.selectedIngredientStatement = null;
		};

		/**
		 * Get current ingredient codes of selected ingredient.
		 */
		self.getCurrentIngredientCodeList = function(){
			var currentIngredientCodeList = [];
			var subIngredient;
			for(var index = 0; index < self.selectedIngredientStatement.ingredientStatementDetails.length; index++){
				subIngredient = self.selectedIngredientStatement.ingredientStatementDetails[index];
				if (subIngredient.ingredient != null && subIngredient.ingredient.ingredientCode != null) {
					currentIngredientCodeList.push(subIngredient.ingredient.ingredientCode);
				}
			}
			return currentIngredientCodeList;
		};

		/**
		 * Adds an ingredient to the ingredient statement.
		 *
		 * @param index the index of the ingredient.
		 */
		self.addIngredient = function(index){
			if(self.selectedIngredientStatement == null) {
				self.selectedIngredientStatement = {ingredientStatementDetails: []};
			} else if(self.selectedIngredientStatement.ingredientStatementDetails == null) {
				self.selectedIngredientStatement.ingredientStatementDetails = [];
			}
			self.selectedIngredientStatement.ingredientStatementDetails.splice(index, 0, {key: {statementNumber: 0}});
		};

		/**
		 * Whether required ingredient statement information has been filled in or not.
		 *
		 * @returns {boolean}
		 */
		self.isRequiredIngredientInfoNotFilled = function(){
			return (self.selectedIngredientStatement == null || self.isAnyIngredientsNull() ||
			self.selectedIngredientStatement.maintenanceDate == null
			|| self.selectedIngredientStatement.statementNumber == null);
		};

		/**
		 * Set the value as a date so date picker understands how to represent value.
		 */
		self.setDateForDatePicker = function(){
			self.maintenanceDatePickerOpened = false;
			if(self.selectedIngredientStatement.maintenanceDate != null) {
				self.selectedIngredientStatement.maintenanceDate =
					new Date(self.selectedIngredientStatement.maintenanceDate.replace(/-/g, '\/'));
			}
		};

		/**
		 * Open the effective date picker to select a new date.
		 */
		self.openMaintenanceDatePicker = function(){
			self.maintenanceDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Change the selected scale upc.
		 *
		 * @param ingredientStatement ingredientStatement to change to.
		 */
		self.getSingleView = function(ingredientStatement) {
			self.success = null;
			self.modifyError = null;
			self.selectedIngredientStatement = angular.copy(ingredientStatement);
			self.setDateForDatePicker();
			self.currentView = SINGLE_VIEW
		};

		/**
		 * Closes single view.
		 */
		self.closeSingleView = function(){
			self.currentView = MULTIPLE_VIEW;
			if(self.isEditingIngredientStatement) {
				self.cancelEdit();
			}
			self.isEditingIngredientStatement = false;
			self.success = null;
			self.modifyError = null;
			self.selectedIngredientStatement = null;
			self.selectedIngredientStatement.maintenanceDate = $scope.convertDate(self.selectedIngredientStatement.maintenanceDate);
		};

		/**
		 * Editing is being cancelled.
		 **/
		self.cancelEdit = function() {
			self.isEditing = false;
			self.selectedIngredientStatement = self.currentEditingObject;
		};

		/**
		 * When the current selected ingredient statement is being edited.
		 */
		self.edit = function() {
			self.currentEditingObject = angular.copy(self.selectedIngredientStatement);
			self.isEditingIngredientStatement = true;
		};

		/**
		 * Is current state not waiting and looking at multiple records.
		 *
		 * @returns {boolean}
		 */
		self.isViewingMultipleRecords = function(){
			return !self.isWaiting && self.data != null && self.currentView == MULTIPLE_VIEW;
		};

		/**
		 * Is current state not waiting and looking at single records.
		 *
		 * @returns {boolean}
		 */
		self.isViewingSingleRecord = function(){
			return !self.isWaiting && self.data != null && self.currentView == SINGLE_VIEW;
		};

		/**
		 * Sets the editing index.
		 *
		 * @param index The editing index.
		 */
		self.setEditingIndex = function(index){
			self.editingIndex = index;
			self.selectedIngredientStatement = self.data[self.editingIndex];
		};

		/**
		 * Returns true if the ingredient code is  one that was specifically searched for.
		 *
		 * @param ingredientCode the ingredient code.
		 */
		self.isSearchedIngredientCode = function(ingredientCode){
			if((self.selectedType == self.INGREDIENT_CODE || self.selectedType == self.ORDERED_CODE_LIST)
				&& self.isEditing == false && self.isAddingIngredientStatement == false){
				if(self.searchedCodes != null && self.searchedCodes.indexOf(ingredientCode) != -1){
					return true;
				}
			}
			return false;
		};

		/**
		 * Finds the nearest available statement number that is greater than or equal to the statement number entered.
		 *
		 * @param statementNumber The statement number entered.
		 */
		self.getNextAvailableIngredientStatementNumber = function(statementNumber){
			var thisRequest = ++self.latestRequest;
			if (!(statementNumber === null || !statementNumber.length || statementNumber.length === 0 || statementNumber.length > self.MAX_STATEMENT_CODE_LENGTH)) {
				scaleManagementApi.queryForNextIngredientStatementNumber({statementNumber: statementNumber},
					//success
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.currentStatementNumberList = results;
						}
					},
					//error
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.currentStatementNumberList = [];
						}
					}
				);
			} else {
				self.currentStatementNumberList = [];
			}
		};

		/**
		 * Prompts the department modal to remove ingredient statement.
		 */
		self.chooseIngredientStatementDepartments = function() {
			self.resetDepartmentList();
			self.deleteMessage = null;
			if(self.data[self.editingIndex].maintenanceCode == 'A'){
				self.removeIngredientStatement();
			} else {
				$("#ingredientStatementChooseDepartmentModal").modal("show");
			}
		};

		/**
		 * Returns whether or not the user can edit ingredient statements.
		 *
		 * @returns {*}
		 */
		self.canEditIngredientStatements = function() {
			return permissionsService.getPermissions("SM_INGR_02", "EDIT");
		};


		/**
		 * If any department is chosen.
		 */
		self.isDepartmentChosen = function() {
			for(var index = 0; index < self.departmentArray.length; index++) {
				if(self.departmentArray[index].isSelected == true){
					return self.departmentArray[index].isSelected == true;
				}
			}
		};

		/**
		 * Resets the department list to all empty.
		 */
		self.resetDepartmentList = function() {
			for(var index = 0; index < self.departmentArray.length; index++) {
				self.departmentArray[index].isSelected = false;
			}
		};

		/**
		 * Adds all of the departments when th select all button is pressed.
		 *
		 * @param value
		 */
		self.addAllDepartments = function(value) {
			if(value) {
				for(var index = 0; index < self.departmentArray.length; index++) {
					self.departmentArray[index].isSelected = true;
				}
			} else {
				for(index = 0; index < self.departmentArray.length; index++) {
					self.departmentArray[index].isSelected = false;
				}
			}
		};

		/**
		 * Updates the select all switch if all of them are selected without pushing the select all checkbox.
		 *
		 * @param value
		 */
		self.updateSelectAllSwitch = function(value) {
			if(!value) {
				self.selectAll = false;
			} else {
				var found = false;
				for(var index = 0; index < self.departmentArray.length; index++) {
					if(!self.departmentArray[index].isSelected){
						found = true;
						break;
					}
				}
				if(!found){
					self.selectAll = true;
				}
			}
		};
	}
})();

