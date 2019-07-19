/*
 *
 * scaleManagementNutrientsController.js
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
 * The controller for the Scale Management Nutrients Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ScaleManagementNutrientsController', scaleManagementNutrientsController);

	scaleManagementNutrientsController.$inject = ['ScaleManagementApi', 'ngTableParams', 'PermissionsService'];

	/**
	 * Constructs the controller.
	 * @param scaleManagementApi The API to fetch data from the backend.
	 * @param ngTableParams The API to set up the report table.
	 * @param permissionsService The service that gives access to a user's permissions.
	 */
	function scaleManagementNutrientsController(scaleManagementApi, ngTableParams, permissionsService) {

		var self = this;

		// rounding rule boundary constants
		const MAX_ROUNDING_RULE_SIZE = 9999;
		const MIN_ROUNDING_RULE_SIZE = 0;

		// Only metric units of measure can be set for nutrients.
		const METRIC_MEASURE_SYSTEM = "M";

		self.NUTRIENT_CODE = "Nutrient Code";
		self.DESCRIPTION = "Description";

		/**
		 * The type of data the user has selected. For basic search, this is nutrientCode, or description
		 * search.
		 *
		 * @type {string}
		 */
		self.selectionType = null;

		/**
		 * Whether or not the search search panel is collapsed or open.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		/**
		 * Wheterer or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;

		/**
		 * Whether or not a user is adding a nutrient code.
		 * @type {boolean}
		 */
		self.isAddingNutrient = false;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * The paramaters passed from the ngTable when it is asking for data.
		 * @type {?}
		 */
		self.dataResolvingParams = null;

		/**
		 * Whether or not the user is searching for all nutrients.
		 * @type {boolean}
		 */
		self.findAll = false;

		/**
		 * Index of data that is being edited.
		 * @type {number}
		 */
		self.previousEditingIndex = null;

		/**
		 * Original state of nutrient code being edited.
		 * @type {null}
		 */
		self.currentEditingObject = null;

		/**
		 * The number of records to show on the report.
		 * @type {number}
		 */
		self.PAGE_SIZE = 16;

		/**
		 * The data being shown in the report.
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Whether or not to ask the backed for the number of records and pages are available.
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * The nutrient code to be used to search for the nutrient rounding rules.
		 * @type {Integer}
		 */
		self.nutrientCode = null;

		/**
		 * Whether or not the controller is waiting for rounding rule data.
		 * @type {boolean}
		 */
		self.isWaitingForRoundRules = false;

		/**
		 * Whether or not the user is adding rounding rules.
		 * @type {boolean}
		 */
		self.isAddingRoundingRule = false;
		/**
		 * Whether or not the user is editing rounding rules.
		 * @type {boolean}
		 */
		self.isEditing = false;
		/**
		 * Stores the previous rounding rules before changes were made.
		 * @type {object}
		 */
		self.previousRoundingRules = null;
		/**
		 * Sets boolean to whether or not there's a boundary error on a rounding rule.
		 * @type {boolean}
		 */
		self.isBoundaryErrorMessage = false;
		/**
		 * Keep track of latest Request for asynchronous calls.
		 * @type {number}
		 */
		self.latestRequest = 0;
		/**
		 * Whether or not the Nutrient code is being removed.
		 * @type {boolean}
		 */
		self.removedNutrient = false;
		/**
		 * Whether or not the Nutrient code was changed.
		 * @type {boolean}
		 */
		self.nutrientCodeChanged = false;
		/**
		 * Message to display for deleted nutrient code.
		 * @type {string}
		 */
		self.deleteMessage = null;
		/**
		 * Whether the current nutrient  code is available.
		 * @type {boolean}
		 */
		self.isNutrientCodeAvailable = null;
		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.selectionType = self.NUTRIENT_CODE;
			if(self.removedNutrient) {
				self.error = null;
				self.data = null;
				self.searchPanelVisible = true;
				self.removedNutrient = false
			}
		};

		/**
		 * Returns whether or not the user is allowed to edit nutrient statements.
		 *
		 * @returns {boolean} Whether or not the user is allowed to edit nutrient statements.
		 */
		self.canEditNutrients = function() {
			return permissionsService.getPermissions("SM_NTRN_01", "EDIT");
		};

		/**
		 * set data to null
		 */
		self.selectionTypeChanged = function(){
			self.data = null;
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
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.firstSearch = true;
			self.nutrientCode = null;
			self.modifyMessage = null;
			self.deleteMessage = null;
			self.modifyError = null;
			self.roundingRulesError = null;
			self.roundingRulesSuccess = null;
			self.findAll = findAll;
			self.tableParams.page(1);
			self.isAddingNutrient = false;
			self.tableParams.reload();
		};

		/**
		 * Issue call to newSearch to call back end to fetch all nutrition codes.
		 */
		self.searchAll = function (){
			self.clearSearch();
			self.newSearch(true);
		};

		/**
		 * Clears the search box.
		 */
		self.clearSearch = function () {
			self.searchSelection = null;
		};

		/**
		 * Constructs the table that shows the report.
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

						if(self.isCurrentStateNull()) {
							self.defer = $defer;
							return;
						}

						self.isWaiting = true;
						self.data = null;
						self.isWaitingForRoundRules = false;
						self.roundingRulesSuccess = null;
						self.roundingRulesData = null;

						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// If this is the first time the user is running this search (clicked the search button,
						// not the next arrow), pull the counts and the first page. Every other time, it does
						// not need to search for the counts.
						if(self.firstSearch){
							self.includeCounts = true;
							self.firstSearch = false;
						} else {
							self.includeCounts = false;
						}

						// Issue calls to the backend to get the data.
						self.getReportByTab(params.page() - 1);
					}
				}
			);
		};


		/**
		 * Checks whether searchSelection is null and if the find all option is not selected
		 * @returns {boolean} return true
		 */
		self.isCurrentStateNull = function(){
			return (!self.findAll && self.searchSelection == null);

		};

		/**
		 * Gets current results for dropdown by calling api.
		 * @param query
		 */
		self.getCurrentDropDownResults = function(query){
			var thisRequest = ++self.latestRequest;
			if (!(query === null || !query.length || query.length === 0)) {
				scaleManagementApi.
					findNutrientUomByRegularExpression({
						searchString: query,
						measureSystem: METRIC_MEASURE_SYSTEM,
						page: 0,
						pageSize: self.PAGE_SIZE
					},
					//success
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.valueList = results.data;
						}
					},
					//error
					function(results) {
						if (thisRequest === self.latestRequest) {
							self.valueList = [];
						}
					}
				);
			} else {
				self.valueList = [];
			}
		};

		/**
		 * The parameters that define the table showing the report.
		 */
		self.tableParams = self.buildTable();

		self.clearBasicSearch = function(){
			self.searchSelection = '';
		};

		/**
		 *  Calls the method to get data based on tab selected.
		 *
		 * @param page The page to get.
		 */
		self.getReportByTab = function(page) {
			if(this.findAll){
				self.getReportByAllNutrientCodes( page);
				return;
			}
			switch (self.selectionType){
				case self.NUTRIENT_CODE:{
					self.getReportByNutrientCode( page);
					break;
				}
				case self.DESCRIPTION:{
					self.getReportByDescription(page);
					break;
				}
			}
		};

		/**
		 * Calls the API method to get data for all nutrient codes.
		 * @param page
		 */
		self.getReportByAllNutrientCodes = function(page){
			scaleManagementApi.queryForNutrientsByAll({
					page: page,
					includeCounts: self.includeCounts,
					pageSize: self.PAGE_SIZE
				},
				self.loadData,
				self.fetchError);
		};

		/**
		 * Calls API method to get data based on nutrient codes also retrieves missing codes.
		 * @param page
		 */
		self.getReportByNutrientCode = function(page){
			scaleManagementApi.queryForNutrientsByNutrientCode({
					nutrientCodes: self.searchSelection,
					page: page,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
			scaleManagementApi.queryForMissingNutrientCodes({
				nutrientCodes: self.searchSelection}, self.showMissingData, self.fetchError);
		};

		/**
		 * Calls API method to get nutrition data based on nutrient description.
		 * @param page
		 */
		self.getReportByDescription = function(page){
			scaleManagementApi.queryByNutrientDescription({
					nutrientDescription: self.searchSelection,
					page: page,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
		};

		/**
		 * Edit current row of nutritin data
		 * @param index
		 * @param nutrient
		 */
		self.editNutrition = function(index, nutrient){
			self.previousEditingIndex = index;
			self.currentEditingObject = angular.copy(nutrient);
			self.isAddingNutrient = false;
			self.isEditing = true;
			self.selectedNutrientUom = nutrient.nutrientUom;
		};

		self.compareEditingIndexToCurrentIndex = function(index) {
			return self.previousEditingIndex != null && self.previousEditingIndex == index;
		};

		/**
		 * Cancel edit of row for nutrients
		 * @param index
		 */
		self.cancelEdit = function(index) {
			self.previousEditingIndex = null;
			self.data[index] = self.currentEditingObject;

		};

		/**
		 * Checks whether nutrition is being added or being updated.
		 */
		self.saveNutrition = function() {
			self.nutrientCodeChanged = true;
			self.nutrientCode = null;
			if(self.isAddingNutrient){
				self.addNutrientData();
			} else if(self.previousEditingIndex != null) {
				self.updateNutrientData();
			}
		};
		/**
		 * Adds a new nutrient code.
		 */
		self.addNutrientData = function() {
			var nutrientCode = self.data[0];
			self.totalRecordCount = null;
			nutrientCode.lstModifiedDate = null;
			scaleManagementApi.addNutrientData(nutrientCode, self.loadData, self.fetchModifyError)
		};

		/**
		 * Push updated nutrition data to back end to write to database.
		 */
		self.updateNutrientData = function() {
			var nutritionData  = self.data[self.previousEditingIndex];
			var difference = angular.toJson(nutritionData) != angular.toJson(self.currentEditingObject);

			if(difference) {
				self.isWaiting = true;
				scaleManagementApi.updateNutritionData(nutritionData, self.loadData, self.fetchModifyError);

			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchModifyError = function(error) {
			self.isWaiting = false;
			if (error && error.data) {
				if(self.previousEditingIndex != null){
					self.cancelEdit(self.previousEditingIndex);
				}
				if (error.data.message) {
					self.error = null;
					self.modifyMessage = null;
					self.deleteMessage = null;
					self.modifyError = error.data.message;
				} else {
					self.error = null;
					self.modifyMessage = null;
					self.deleteMessage = null;
					self.modifyError = error.data.error;
				}
			}
			else {
				self.setError("An unknown error occurred.");
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
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			if (error && error.data) {
				if(self.previousEditingIndex != null){
					self.cancelEdit(self.previousEditingIndex);
				}
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

		/**
		 * Callback for when the backend returns a rounding rules error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchRoundingRulesError = function(error) {
			self.isWaitingForRoundRules = false;
			self.roundingRulesData = null;
			if (error) {
				self.roundingRulesError = error;
			}
			else {
				self.roundingRulesError = "An unknown error occurred.";
			}
		};

		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		self.setError = function(error) {
			self.error = error;
			self.modifyMessage = null;
			self.deleteMessage = null;
			self.modifyError = null;
		};

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results){
			self.error = null;
			self.modifyMessage = null;
			self.deleteMessage = null;
			self.modifyError = null;
			self.roundingRulesError = null;
			self.roundingRulesSuccess = null;
			self.success = null;
			self.isWaiting = false;

			// If this was the first page, it includes record count and total pages .
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No records found.";
			} else if(self.isAddingNutrient && self.nutrientCodeChanged) {
				self.modifyMessage = results.message;
				self.error = null;
				self.isAddingNutrient = false;
				self.data = [];
				self.data.push(results.data);
				self.defer.resolve(results.data);
			} else if(self.previousEditingIndex != null && self.nutrientCodeChanged){
				self.error = null;
				self.modifyMessage = results.message;
				self.data[self.previousEditingIndex].isEditing = false;
				self.data[self.previousEditingIndex] = results.data;
				self.previousEditingIndex = null;
				self.currentEditingObject = null;
			}
			else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
			if(self.removedNutrient) {
				self.removedNutrient = false;
			} else {
				self.deleteMessage = null;
			}
			self.nutrientCodeChanged = false;
			self.isAddingNutrient = false;
			self.nutrientCode = null;
			// Return the data back to the ngTable.
		};

		/**
		 * Callback for a successful call to get Rounding rule data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadRoundingRulesData = function(results){
			self.roundingRulesError = null;
			self.roundingRulesSuccess = null;
			self.isWaitingForRoundRules = false;
			if(self.isEditing == true){
				if(results.data.length != 0){
					self.roundingRulesData = results.data;
					self.roundingRulesSuccess = results.message;
				}
			} else if (results.length === 0) {
				self.roundingRulesData = null;
				self.roundingRulesError = "No rounding rules found.";
			} else {
				self.roundingRulesData = results;
			}
			self.roundingRuleChanged = false;
			self.isEditing = false;
		};

		/**
		 * Sets the nutrient code to be used to search for the rounding rules.
		 *
		 * @param nutrient the nutrient code to be used to search for the rounding rules.
		 */
		self.getRoundingRules = function(nutrient){
			self.setEditing(false);
			self.nutrientCode = nutrient;
			self.isWaitingForRoundRules = true;
			self.searchPanelVisible = false;
			scaleManagementApi.queryForNutrientRoundingRules({nutrientCode: nutrient.nutrientCode},
				self.loadRoundingRulesData, self.fetchRoundingRulesError);
		};

		/**
		 * Makes a copy of the original rounding rules and sets the is editing value to true.
		 */
		self.editRoundingRules = function(){
			self.previousRoundingRules = angular.copy(self.roundingRulesData);
			self.setEditing(true);
			self.roundingRulesSuccess = null;
		};

		/**
		 * Sets the isEditing value the boolean passed.
		 * @param bool
		 */
		self.setEditing = function(bool){
			self.isEditing = bool;
		};

		/**
		 * Adds a row to the rounding rule table so that a new rule can be added.
		 */
		self.addRoundingRuleRow = function(){
			if(self.roundingRulesData == null){
				self.roundingRulesData = [];
			}
			var rule = {key: {}};
			rule.key.nutrientCode = self.nutrientCode.nutrientCode;
			self.roundingRulesData.push(rule);
		};

		/**
		 * Refreshed rounding rule data to the original values.
		 */
		self.refresh = function(){
			self.roundingRulesData = angular.copy(self.previousRoundingRules);
			self.resetBoundaryErrors();
		};

		/**
		 * Validates the rules and then sends a request to the backend to update the rounding rules.
		 */
		self.saveRoundingRules = function(){
			self.isBoundaryErrorMessage = false;
			if(JSON.stringify(self.roundingRulesData) == JSON.stringify(self.previousRoundingRules)){
				self.setRoundingRulesErrorMessage("No changes detected. ");
				return;
			}
			if(self.isValidRoundingRules()){
				self.resetBoundaryErrors();
				var rules = [];
				rules.push(self.roundingRulesData);
				rules.push(self.previousRoundingRules);
				scaleManagementApi.updateRoundingRule(rules,
					self.loadRoundingRulesData, self.fetchRoundingRulesError);
			}
		};

		/**
		 * Compares each rule with the other rules for overlaps or rules containing other rules, more than one or
		 * no max/min boundaries (must contain exactly one of the max boundaries as one minimum boundary to span the
		 * entire range). This also checks if the each rule has an appropriate value.
		 *
		 * @returns {boolean}
		 */
		self.isValidRoundingRules = function() {
			self.resetBoundaryErrors();
			var currentRule;
			var nextRule;
			var isNoError = true;
			var lowestBoundCount = 0;
			var highestBoundCount = 0;

			for (var x = 0; x < self.roundingRulesData.length; x++) {
				currentRule = self.roundingRulesData[x];
				self.checkRuleValues(currentRule);
				// adds count of lowest boundary
				if(currentRule.key.lowerBound == MIN_ROUNDING_RULE_SIZE){
					lowestBoundCount++;
				}
				// adds count of the highest boundary
				if(currentRule.upperBound == MAX_ROUNDING_RULE_SIZE){
					highestBoundCount++;
				}
				for(var y = x+1; y < self.roundingRulesData.length; y++) {
					nextRule = self.roundingRulesData[y];

					// if two rules have the same lower bound, set message for the rules in conflict.
					if (currentRule.key.lowerBound == nextRule.key.lowerBound) {
						self.setBoundaryErrorMessage(currentRule, "Rule has same lower boundary as line: " + (y + 1) + ". ");
						self.setBoundaryErrorMessage(nextRule, "Rule has same lower boundary as line: " + (x + 1) + ". ");
						self.isBoundaryErrorMessage = true;
						isNoError = false;
					}
					// if two rules have the same upper bound, set message for the rules in conflict.
					if (currentRule.upperBound == nextRule.upperBound) {
						self.setBoundaryErrorMessage(currentRule, "Rule has same upper lower boundary as line: " + (y + 1) + ". ");
						self.setBoundaryErrorMessage(nextRule, "Rule has same upper boundary as line: " + (x + 1) + ". ");
						self.isBoundaryErrorMessage = true;
						isNoError = false;
					}
					// If there's boundary overlaps or a boundary within another boundary, set the boundary error
					// message for those rules that conflict.
					if (
						(currentRule.upperBound > nextRule.key.lowerBound &&
						currentRule.upperBound < nextRule.upperBound)  ||
						(currentRule.key.lowerBound < nextRule.key.lowerBound &&
						currentRule.upperBound > nextRule.upperBound)) {
						self.setBoundaryErrorMessage(currentRule, "Boundary conflict with line: " + (y + 1) + ". ");
						self.setBoundaryErrorMessage(nextRule, "Boundary conflict with line: " + (x + 1) + ". ");
						self.isBoundaryErrorMessage = true;
						isNoError = false;
					}
				}
			}
			// if there's not exactly one of the MAX and MIN rounding rule value, set message.
			if(lowestBoundCount != 1){
				self.setRoundingRulesErrorMessage("There must be exactly one lower boundary of 0. ");
				isNoError = false;
			}
			if(highestBoundCount != 1){
				self.setRoundingRulesErrorMessage("There must be exactly one upper boundary of 9999. ");
				isNoError = false;
			}
			if(self.isGapBetweenRoundingRules()){
				isNoError = false;
			}
			return isNoError;
		};

		/**
		 * Checks to see if there's a gap between the boundaries.
		 *
		 * @returns {boolean}
		 */
		self.isGapBetweenRoundingRules = function(){
			var currentUpperBound;
			var nextLowerBound;
			var closestBoundary;
			var isError = false;
			for (var x = 0; x < self.roundingRulesData.length; x++) {
				currentUpperBound = self.roundingRulesData[x].upperBound;
				closestBoundary = 0;
				for(var y = 0; y < self.roundingRulesData.length; y++) {
					if(x == y){
						continue;
					}
					nextLowerBound = self.roundingRulesData[y].key.lowerBound;
					if(currentUpperBound == nextLowerBound || currentUpperBound == MAX_ROUNDING_RULE_SIZE){
						break;
					} else {
						if(currentUpperBound < nextLowerBound &&
							(closestBoundary == 0 || nextLowerBound < closestBoundary)){
							closestBoundary = nextLowerBound;
						}
					}
				}
				if(closestBoundary != 0){
					self.setRoundingRulesErrorMessage(
						"Gap between: " + currentUpperBound + " and: " + closestBoundary + ". ");
					isError = true;
				}
			}
			return isError;
		};

		/**
		 * Checks that rules aren't NaN, bounds are integers, and the upper bound isn't lower than the  lower bound.
		 */
		self.checkRuleValues = function(roundingRule){
			// If bounds are empty or NAN set boundary error message.
			if(self.isEmptyOrNAN(roundingRule.key.lowerBound) || !(roundingRule.key.lowerBound % 1 == 0)){
				self.setBoundaryErrorMessage(roundingRule, "Lower boundary must be an integer and must be set. ");
			}
			if(self.isEmptyOrNAN(roundingRule.upperBound) || !(roundingRule.upperBound % 1 == 0)){
				self.setBoundaryErrorMessage(roundingRule, "Upper boundary must be an integer and must be set. ");
			}
			if(self.isEmptyOrNAN(roundingRule.incrementQuantity)){
				self.setBoundaryErrorMessage(roundingRule, "Increment amount can't be empty or NaN. ");
			}
			if(roundingRule.key.lowerBound > roundingRule.upperBound){
				self.setBoundaryErrorMessage(roundingRule, "Lower boundary cannot be greater than upper boundary")
			}
		};

		/**
		 * Removes boundary errors on the rounding rules.
		 */
		self.resetBoundaryErrors = function(){
			self.roundingRulesError = null;
			self.isBoundaryErrorMessage = false;
			if(self.roundingRulesData != null) {
				for (var x = 0; x < self.roundingRulesData.length; x++) {
					delete self.roundingRulesData[x].boundaryErrorMessage;
				}
			} else {
				self.init();
			}
		};

		/**
		 * Sets the boundary message to the rule if no messages already exist. Concats the message if one does exist.
		 *
		 * @param rule The rule to add the message to.
		 * @param message The message to add.
		 */
		self.setBoundaryErrorMessage = function(rule, message){
			if(rule.boundaryErrorMessage == null){
				rule.boundaryErrorMessage = message;
			} else {
				rule.boundaryErrorMessage += message;
			}
		};

		/**
		 * Sets the roundRules error message to the message. If one exists, then it concats the message.
		 *
		 * @param message
		 */
		self.setRoundingRulesErrorMessage = function(message){
			if(self.roundingRulesError == null){
				self.roundingRulesError = message;
			} else {
				self.roundingRulesError += message;
			}
		};

		/**
		 * Checks if a string is empty or not a number.
		 *
		 * @param value
		 * @returns {boolean}
		 */
		self.isEmptyOrNAN = function(value){
			return (value == null || isNaN(value));
		};

		/**
		 * Removes selected rounding rule from the table.
		 *
		 * @param index The rounding rule's index.
		 */
		self.removeRoundingRule = function(index){
			self.roundingRulesData.splice(index, 1);
		};

		/**
		 * Add an empty row to the nutrient code data.
		 */
		self.addRow = function(){
			self.error = null;
			self.searchPanelVisible = false;
			self.deleteMessage = null;
			self.nutrientCode = null;
			self.isAddingNutrient = true;
			self.data = [];
			self.data.push({
				lstModifiedDate: "Auto-generated"
			});
			self.tableParams.data = [];
		};
		/**
		 * Deletes a nutrient.
		 */
		self.deleteNutrient = function(index) {
			var nutrientCode = self.data[index].nutrientCode;
			self.isWaiting = true;
			scaleManagementApi.deleteNutrientData({nutrientCode: nutrientCode}, self.reloadRemovedData, self.fetchError)
		};

		/**
		 * Return of back end after a user removes a nutrient. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.modifyMessage = null;
			self.deleteMessage = null;
			self.modifyError = null;
			self.removedNutrient = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Only show match count information for searches by nutrient code (not description).
		 *
		 * @returns {boolean} True if the search is by nutrient code.
		 */
		self.showMatchCount = function(){
			return self.selectionType === self.NUTRIENT_CODE;
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
		 * Callback for the request for the number of items found and not found.
		 *
		 * @param results The object returned from the backend with a list of found and not found items.
		 */
		self.showMissingData = function(results){
			self.missingValues = results;
		};

		/**
		 * If the current typed in statement ID is available or not.
		 */
		self.isCodeAvailable = function () {
			if (self.data[0].nutrientCode != null) {
				scaleManagementApi.searchForAvailableNutrientCode({
						nutrientCode: self.data[0].nutrientCode
					},
					//success
					function (results) {
							self.isNutrientCodeAvailable = results.data;
					},
					//error
					function(results) {
							self.isNutrientCodeAvailable = "Not Available.";
					}
				);
			} else {
				self.isNutrientCodeAvailable = "Not Available.";
			}
		};
	}
})();
