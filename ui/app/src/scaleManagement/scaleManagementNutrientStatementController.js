/*
 *
 * scaleManagementNutrientsController.js
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 *
 */

'use strict';

/**
 * The controller for the Scale Management Nutrient Statement Controller.
 */
(function () {
	angular.module('productMaintenanceUiApp').controller('ScaleManagementNutrientStatementController', scaleManagementNutrientStatementController);

	scaleManagementNutrientStatementController.$inject = ['ScaleManagementApi','ngTableParams','urlBase','DownloadService', 'PermissionsService' ,'$scope', '$stateParams'];

	/**
	 * Constructs the controller.
	 *
	 * @param scaleManagementApi The API to fetch data from the backend.
	 * @param ngTableParams The API to set up the report table.
	 * @param urlBase The base url used for building export requests.
	 * @param downloadService The service used for downloading a file.
	 */
	function scaleManagementNutrientStatementController(scaleManagementApi, ngTableParams, urlBase, downloadService, permissionsService, $scope, $stateParams) {
		var self = this;

		self.NUTRIENT_CODE = "nutrient code";
		self.STATEMENT_ID = "statement id";
		self.UNKNOWN_ERROR = "An unknown error occurred.";
		self.EMPTY_STRING = "";

		self.METRIC_MEASURE_SYSTEM = "M";
		self.COMMON_MEASURE_SYSTEM = "C";
		self.VARIES_LABEL = " varies";

		self.PAGE_SIZE = 20;
		/**
		 * Informational text for the user adding sub-ingredients.
		 *
		 * @type {string}
		 */
		self.SUB_NUTRIENT_TEXT = "A sub-nutrient cannot be added that exists as a sub-nutrient already, " +
			"or as a sub-nutrient of those sub-nutrients. An nutrient also cannot be added as a " +
			"sub-nutrient if it exists as a super nutrient, or a super nutrient of those super nutrient.";

		/**
		 * The type of data the user has selected. For basic search, this is nutrientCode, or statement id
		 * search.
		 *
		 * @type {string}
		 */
		self.selectionType = null;

		/**
		 * The options for the date picker on the popup.
		 *
		 * @type {Object}
		 */
		self.datePickerOptions = {minDate: new Date()};

		/**
		 * Whether or not the user is editing nutrient statement.
		 *
		 * @type {boolean}
		 */
		self.isEditing = false;

		/**
		 * Whether or not user is adding an nutrient statement.
		 *
		 * @type {boolean}
		 */
		self.isAddingNutrientStatement = false;

		self.nutrientStatement = {};

		/**
		 * Keeps track of current request.
		 *
		 * @type {number}
		 */
		self.latestRequest = 0;

		/**
		 * Whether or not effective date picker is visible.
		 *
		 * @type {boolean}
		 */
		self.effectiveDatePickerOpened = false;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 *
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * Whether or not the search search panel is collapsed or open.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		/**
		 * Whether or not the controller is waiting for data.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Whether or not this is the first search with the current parameters.
		 *
		 * @type {boolean}
		 */
		self.firstSearch = true;

		/**
		 * Index of data that is being edited.
		 *
		 * @type {number}
		 */
		self.previousEditingIndex = null;

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 *
		 * @type {?}
		 */
		self.defer = null;

		/**
		 * The paramaters passed from the ngTable when it is asking for data.
		 *
		 * @type {?}
		 */
		self.dataResolvingParams = null;

		/**
		 * The number of records to show on the report.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;

		/**
		 * Whether or not a user is adding an nutrition statement.
		 *
		 * @type {boolean}
		 */
		self.isAddingNutrientStatement = false;

		/**
		 * The data being shown in the report.
		 *
		 * @type {Array}
		 */
		self.data = null;

		/**
		 * Whether or not to ask the backed for the number of records and pages are available.
		 *
		 * @type {boolean}
		 */
		self.includeCounts = true;

		/**
		 * The nutrient code to be used to search for the nutrient rounding rules.
		 *
		 * @type {Integer}
		 */
		self.nutrientCode = null;

		/**
		 * Tracks whether or not the user is waiting for a download.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;
		/**
		 * Is Waiting for Mandated nutrients.
		 *
		 * @type {boolean}
		 */
		self.isMandatedWaiting = false;
		/**
		 * Current non zero list.
		 * @type {Array}
		 */
		self.currentNonZeroNutrientList = [];
		/**
		 * Whether the current nutrient statement code is available.
		 *
		 * @type {boolean}
		 */
		self.isNutrientStatementCodeAvailable = null;
		/**
		 * Message to display for deleted nutrient statement.
		 *
		 * @type {string}
		 */
		self.deleteMessage = null;

		/**
		 * Max time to wait for excel download.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;
		/**
		 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
		 * @type {String}
		 */
		self.resultMessage = null;
		/**
		 * The departments array.
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
		 * Whether every row is selected.
		 *
		 * @type {boolean}
		 */
		self.selectAll = false;

		/**
		 * Remaining available characters for the serving label.
		 *
		 * @type {number}
		 */
		self.servingTextCharacterRemainingCharacterCount = 22;

		/**
		 * Current String for the serving size label
		 *
		 * @type {string}
		 */
		self.servingLabel = "";

		/**
		 * Show varies label when Servings Per Container = 999.
		 *
		 * @type {string}
		 */
		self.showVariesLabel = "";

		/**
		 * Servings Per Container value = 999;.
		 *
		 * @type {number}
		 */
		self.servingPerContainerVariesVary = 999;

		self.isCheckExistNLEA2016Waiting = false;

		self.isExistNLEA2016 = false;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			if(self.removedNutrientStatement) {
				self.modifyMessage = null;
				self.error = null;
				self.data = null;
				self.searchPanelVisible = true;
				self.removedNutrientStatement = false;
			}
			self.selectionType = self.STATEMENT_ID;
			self.disableNumberInputFeatures("measureQuantity");
			self.disableNumberInputFeatures("metricQuantity");
			if($stateParams && $stateParams.ntrStmtCode){
				self.searchSelection = $stateParams.ntrStmtCode;
				self.newSearch();
			}
		};

		/**
		 * Returns wheter or not the user can edit nutrient statements.
		 *
		 * @returns {*}
		 */
		self.canEditNutrientStatements = function() {
			return permissionsService.getPermissions("SM_NTRN_02", "EDIT");
		};

		/**
		 * set data to null
		 */
		self.selectionTypeChanged = function(){
			self.data = null;
		};

		const MINUTES_TO_MILLISECONDS = 60000;

		/**
		 * Returns whether or not the user is currently downloading a CSV,
		 *
		 * @returns {boolean} True if the user is downloading product information CSV and false otherwise.
		 */
		self.isDownloading = function(){
			return self.downloading;
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
		 * Issue call to newSearch to call back end to fetch all nutrition codes.
		 */
		self.searchAll = function (){
			self.newSearch(true);
		};

		/**
		 * Set the value as a date so date picker understands how to represent value.
		 */
		self.setDateForDatePicker = function(nutrientStatement){
			if(self.nutrientStatement.effectiveDate != null) {
				self.nutrientStatement.effectiveDate = new Date(self.nutrientStatement.effectiveDate.replace(/-/g, '\/'));
			}
		};

		/**
		 * Gets current results for dropdown by calling api.
		 * @param query
		 */
		self.getCurrentDropDownResults = function(measureSystem, query){
			var request = ++self.latestRequest;
			if (!(query === null || !query.length || query.length === 0)) {
				var requestParams = {searchString: query, measureSystem: measureSystem, page: 0, pageSize: self.pageSize};

				// If we're searching for the metric unit of measure and the common UOM has been set, then
				// constrain on the form types that match the common UOM's form.
				if (measureSystem == self.METRIC_MEASURE_SYSTEM && self.nutrientStatement.nutrientCommonUom != null) {
					requestParams.form = self.nutrientStatement.nutrientCommonUom.form;
				}

				scaleManagementApi.
					findNutrientUomByRegularExpression(requestParams,
					//success
					function (results) {
						if(request === self.latestRequest) {
							self.valueList = results.data;
						}
					},
					//error
					function(results) {
						if (request === self.latestRequest) {
							self.valueList = [];
						}
					}
				);
			} else {
				self.valueList = [];
			}
		};


		/**
		 * Open the effective date picker to select a new date.
		 */
		self.openEffectiveDatePicker = function(){
			self.effectiveDatePickerOpened = true;
			self.options = {
				minDate: new Date()
			};
		};

		/**
		 * Cancel edit of row for nutrients statement
		 */
		self.cancelEdit = function() {
			self.nutrientStatement = null;
			self.isEditing = false;
			self.isAddingNutrientStatement = false;
			self.previousEditingIndex = null;

		};

		/**
		 * Clears the search box.
		 */
		self.clearSearch = function () {
			self.searchSelection = null;
		};

		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.modifyMessage = null;
			self.modifyError = null;
			self.deleteMessage = null;
			self.firstSearch = true;
			self.nutrientCode = null;
			self.previousEditingIndex = null;
			self.findAll = findAll;
			self.tableParams.page(1);
			self.isAddingNutrientStatement = false;
			self.tableParams.reload();
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
			self.data = null;
			self.isAddingNutrientStatement = false;
			self.isEditing = false;
			self.isCheckExistNLEA2016Waiting = false;
			if (error && error.data) {
				if(error.data.message != null && error.data.message != "") {
					self.setError(error.data.message);
				} else {
					self.setError(error.data.error);
				}
			}
			else {
				self.setError(self.UNKNOWN_ERROR);
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
		 * Initialize a new nutrient statement.
		 */
		self.createNutrientStatement = function () {
			self.deleteMessage = null;
			self.isEditing = false;
			self.isAddingNutrientStatement = true;
			self.nutrientStatement= {effectiveDate: new Date()};
			self.getAvailableMandatedNutrients({nutrientStatementCode: null});
			self.updateServingLabelComponents();
			self.updateShowVariesLabelComponents();
		};

		/**
		 * Checks to see if any nutrients are null.
		 *
		 * @returns {boolean}
		 */
		self.isAnyNutrientsNull = function () {
			if(self.nutrientStatement != null) {
				if(self.nutrientStatement.nutrientStatementDetailList != null){
					if(self.nutrientStatement.nutrientStatementDetailList.length > 0) {
						for (var x = 0; x < self.nutrientStatement.nutrientStatementDetailList.length; x++) {
							var obj = self.nutrientStatement.nutrientStatementDetailList[x];
							if(obj == null || obj.nutrient == null){
								return true;
							}
						}
					}
				}
			}
			return false;
		};

		/**
		 * Adds an nutrient to the nutrient statement.
		 */
		self.addNutrient = function(){
			if(self.nutrientStatement == null) {
				self.nutrientStatement = {nutrientStatementDetailList: []};
			} else if(self.nutrientStatement.nutrientStatementDetailList == null) {
				self.nutrientStatement.nutrientStatementDetailList = [];
			}
			for(var index =0; index < self.currentNonZeroNutrientList.length; index++) {
				self.nutrientStatement.nutrientStatementDetailList.splice(index, 0, self.currentNonZeroNutrientList[index]);
				self.nutrientStatement.nutrientStatementDetailList[index].isNewElement = true;
			}
			self.currentNonZeroNutrientList = [];
		};

		/**
		 * Populates current nutrient list based on search query string.
		 *
		 * @param query search string
		 */
		self.getCurrentNutrientCodeList = function(query){
			var thisRequest = ++self.latestRequest;
			if (!(query === null || !query.length || query.length === 0)) {
				scaleManagementApi.
					getNutrientsByRegularExpression({
						searchString: query,
						currentNutrientList: self.getCurrentNutrientList()
					},
					//success
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.currentNutrientList = results.data;
						}
					},
					//error
					function (results) {
						if(thisRequest === self.latestRequest) {
							self.currentNutrientList = [];
						}
					}
				);
			} else {
				self.currentNutrientList = [];
			}
		};


		/**
		 * Get current ingredient codes of selected ingredient.
		 */
		self.getCurrentNutrientList = function(){
			var currentNutrientCodeList = [];
			var detail;
			for(var index = 0; index < self.nutrientStatement.nutrientStatementDetailList.length; index++){
				detail = self.nutrientStatement.nutrientStatementDetailList[index];
				if (detail.nutrient != null && detail.nutrient.nutrientCode != null) {
					currentNutrientCodeList.push(detail.nutrient.nutrientCode);
				}
			}
			return currentNutrientCodeList;
		};

		/**
		 * Takes in new or modified data and creates or updates nutrient Statement
		 */
		self.addNutrientStatement = function () {
			if(self.nutrientStatement != null && self.isAddingNutrientStatement) {
				var nutrientStatement = angular.copy(self.nutrientStatement);
				nutrientStatement.effectiveDate = $scope.convertDate(nutrientStatement.effectiveDate);
				this.isWaiting = true;
				scaleManagementApi.addStatement(nutrientStatement, self.loadData, self.fetchError);
			} else if(self.nutrientStatement != null && self.isEditing){
				self.saveNutritionStatement();
			}
			else {
				self.error = "No changes detected";
			}
		};

		/**
		 * Check weather fields that are needed for an update or new statement are filled out.
		 *
		 * @returns {boolean} True if some information is missing and false otherwise.
		 */
		self.isRequiredNutrientInfoMissing = function () {

			return !(self.nutrientStatement != null && self.nutrientStatement.nutrientStatementNumber != null && (self.servingTextCharacterRemainingCharacterCount>=0) &&
			self.nutrientStatement.measureQuantity != null && self.nutrientStatement.nutrientMetricUom != null && self.nutrientStatement.metricQuantity != null
			&& self.nutrientStatement.servingsPerContainer != null && self.nutrientStatement.effectiveDate != null && self.nutrientStatement.nutrientCommonUom != null
			&& (self.isNutrientStatementCodeAvailable == 'Available' || self.isEditing));
		};

		/**
		 *  Calls the method to get data based on tab selected.
		 *
		 * @param page The page to get.
		 */
		self.getReportByTab = function (page) {
			if (self.findAll) {
				self.getReportByAll(page);
				return;
			}
			switch (self.selectionType) {
				case self.NUTRIENT_CODE: {
					self.getReportByNutrientCode(page);
					break;
				}
				case self.STATEMENT_ID: {
					self.getReportByStatementId(page);
					break;
				}
			}
		};

		/**
		 * Calls the API method to get data for all.
		 *
		 * @param page
		 */
		self.getReportByAll = function (page) {
			scaleManagementApi.queryForNutrientStatementsByAll({
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
		};

		/**
		 * Calls API method to get data based on nutrient codes.
		 *
		 * @param page
		 */
		self.getReportByNutrientCode = function (page) {
			scaleManagementApi.queryForNutrientStatementByNutrientCode({
					nutrientCodes: self.searchSelection,
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
			// Commented out per PM-985
			//scaleManagementApi.queryForMissingNutrientCodesByNutrientStatements({nutrientCodes: self.searchSelection},
			//	self.showMissingData, self.fetchError);
		};

		/**
		 * Calls PI method to get dat a based on statement id.
		 *
		 * @param page
		 */
		self.getReportByStatementId = function (page) {
			scaleManagementApi.queryForNutrientStatementByStatementId({
					nutrientStatementId: self.searchSelection,
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.fetchError);
			// Commented out per PM-985
			//scaleManagementApi.queryForMissingNutrientStatementsByNutrientStatements({
			//	nutrientStatements: self.searchSelection}, self.showMissingData, self.fetchError);
		};


		/**
		 * Changes the edit mode of a nutrient detail object to the opposite of what it currently is.
		 *
		 * @param nutrientDetail The nutrient detail object to flip the edit mode of.
		 */
		self.flipEditMode = function(nutrientDetail) {
			nutrientDetail.inAlternateEditMode = !nutrientDetail.inAlternateEditMode;
		};

		/**
		 * Returns whether or not to not show nutrient measure at all for a nutrient detail object.
		 *
		 * @param nutrientDetail The nutrient detail object to not show the measure of.
		 * @returns {boolean} True if the UI should not show nutrient measure and false otherwise.
		 */
		self.showBlankNutrientMeasure = function(nutrientDetail) {

			// Never show it if there is no defined recommendedDailyAmount. They have to edit the measure.
			if (nutrientDetail.nutrient.recommendedDailyAmount == 0) {
				return false;
			}

			// If they are in alternate edit mode, then, by definition, you'd never see a blank one here.
			if (nutrientDetail.inAlternateEditMode) {
				return false;
			}

			// So basically, show blank if we usePercentDailyValue and are not in alternate edit mode.
			return nutrientDetail.nutrient.usePercentDailyValue;
		};

		/**
		 * Returns whether or not to show a read-only version of the nutrient measure for a nutirent detail object.
		 *
		 * @param nutrientDetail The nutrient detail object to show a read-only nutrient measure for.
		 * @returns {boolean} True if the UI should show read-only nutrient measure and false otherwise.
		 */
		self.showReadOnlyNutrientMeasure = function(nutrientDetail) {
			// If there is no recommended daily value, you always have to edit that way, so never show the empty box.
			if (nutrientDetail.nutrient.recommendedDailyAmount == 0) {
				return false;
			}

			if (nutrientDetail.inAlternateEditMode) {
				return !nutrientDetail.nutrient.usePercentDailyValue;
			} else {
				nutrientDetail.nutrient.usePercentDailyValue;
			}
		};

		/**
		 * Returns whether or not a user should be able to edit a nutrient detail's regular measure quantity.
		 *
		 * @param nutrientDetail The nutrient detail object to look at.
		 * @returns {boolean} True if the user can edit the measure quantity and false otherwise.
		 */
		self.isEditableNutrientMeasure = function(nutrientDetail) {

			// If there is no recommended daily value, you always have to edit this way.
			if (nutrientDetail.nutrient.recommendedDailyAmount == 0) {
				return true;
			}

			if (nutrientDetail.inAlternateEditMode) {
				return nutrientDetail.nutrient.usePercentDailyValue;
			} else {
				return !nutrientDetail.nutrient.usePercentDailyValue;
			}
		};

		/**
		 * Returns whether or not a percent daily value is editable for a nutrient detail.
		 *
		 * @param nutrientDetail The nutrient detail object to look at.
		 * @returns {boolean} True if the user can edit the percent daily value and false otherwise.
		 */
		self.isEditablePercentDailyValue = function(nutrientDetail) {

			// If there is no recommended daily value, you can never edit.
			if (nutrientDetail.nutrient.recommendedDailyAmount == 0.0) {
				return false;
			}

			/// If there is a recommended daily value, you should defer to usePercentDailyValue unless
			// the user has specifically said they want to do the opposite.
			if (nutrientDetail.inAlternateEditMode) {
				return !nutrientDetail.nutrient.usePercentDailyValue;
			} else {
				return nutrientDetail.nutrient.usePercentDailyValue;
			}
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
						self.defer = $defer;

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
		 * The parameters that define the table showing the report.
		 */
		self.tableParams = self.buildTable();

		/**
		 * Checks whether searchSelection is null and if the find all option is not selected.
		 *
		 * @returns {boolean} return true
		 */
		self.isCurrentStateNull = function(){
			return (!self.findAll && self.searchSelection == null);

		};

		/**
		 * Edit current row of nutrition statement data.
		 *
		 * @param statement
		 * @param index
		 */
		self.editNutritionStatement = function(statement, index){
			self.deleteMessage = null;
			self.isEditing = true;
			self.nutrientStatement = angular.copy(statement);
			self.updateServingLabelComponents();
			self.updateShowVariesLabelComponents();
			self.setDateForDatePicker(self.nutrientStatement);
			self.previousEditingIndex = index;
			self.getAvailableMandatedNutrients(statement);
		};

		/**
		 * Gets available mandated nutrients... if there are any available.
		 *
		 * @param statement
		 */
		self.getAvailableMandatedNutrients = function(statement) {
			self.isMandatedWaiting = true;
			self.currentNonZeroNutrientList = [];
			if (!self.isAddingNutrientStatement){
				self.isCheckExistNLEA2016Waiting = true;
				scaleManagementApi.isNLEA16NutrientStatementExists({
						nutrientStatementNumber: statement.nutrientStatementNumber
					},
					function (result) {
						self.isExistNLEA2016 = angular.copy(result.data);
						self.isCheckExistNLEA2016Waiting = false;
					},
					self.fetchError);
			}
			scaleManagementApi.getMandatedNutrientsByStatementId({
					statementNumber: statement.nutrientStatementNumber
				},
				//success
				function (results) {
					self.currentNonZeroNutrientList = results;
					self.isMandatedWaiting = false;
				},
				//error
				function (results) {
					self.currentNonZeroNutrientList = [];
					self.isMandatedWaiting = false;
				}
			);
		};

		/**
		 * Push updated nutrition statement data to back end to write to database
		 */
		self.saveNutritionStatement = function() {

			var difference = angular.toJson(self.data[self.previousEditingIndex]) != angular.toJson(self.nutrientStatement);

			if(difference) {
				var nutrientStatement = angular.copy(self.nutrientStatement);
				nutrientStatement.effectiveDate = $scope.convertDate(nutrientStatement.effectiveDate);
				this.isWaiting = true;
				scaleManagementApi.updateNutritionStatementData(nutrientStatement, self.loadData, self.fetchError);

			} else {
				self.error = "No difference detected";
			}
		};

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
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
			}
			else if(self.isAddingNutrientStatement){
				self.isAddingNutrientStatement = false;
				self.modifyMessage = results.message;
				self.resultMessage = null;
				self.data = [];
				self.data.push(results.data);
				self.defer.resolve(self.data);
			}
			else if(self.previousEditingIndex != null && self.isEditing){
				self.error = null;
				self.modifyMessage = results.message;
				self.data[self.previousEditingIndex] = results.data;
				self.previousEditingIndex = null;
				self.nutrientStatement = null;
				self.isEditing = false;
			}
			else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
			if(self.removedNutrientStatement) {
				self.removedNutrientStatement = false;
			} else {
				self.deleteMessage = null;
			}
			// Return the data back to the ngTable.
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = self.generateExportUrl();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'nutrientStatements.csv', self.WAIT_TIME,
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
			if(self.selectionType == self.NUTRIENT_CODE && !self.findAll) {
				return urlBase + '/pm/nutrientsStatement/exportNutrientStatementByNutrientCodesToCsv?nutrientCodes=' +
					encodeURI(self.searchSelection) + "&totalRecordCount=" + self.totalRecordCount;
			} else if (self.selectionType == self.STATEMENT_ID && !self.findAll){
				return urlBase + '/pm/nutrientsStatement/exportNutrientStatementByIdsToCsv?nutrientStatements=' +
					encodeURI(self.searchSelection) + "&totalRecordCount=" + self.totalRecordCount;
			} else if (self.findAll){
				return urlBase + '/pm/nutrientsStatement/exportAllNutrientStatementsToCsv?totalRecordCount=' +
					self.totalRecordCount;
			} else {
				return self.EMPTY_STRING;
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
			return self.selectionType === self.STATEMENT_ID || self.selectionType === self.NUTRIENT_CODE;
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
		 * If there are available mandated nutrients to add or remove.
		 *
		 * @returns {boolean}
		 */
		self.isMandated = function () {
			return self.currentNonZeroNutrientList.length == 0;
		};

		/**
		 * If the current typed in statement ID is available or not.
		 */
		self.isStatementCodeAvailable = function () {
			if (self.nutrientStatement.nutrientStatementNumber != null) {
				scaleManagementApi.searchForAvailableNutrientStatement({
						nutrientStatement: self.nutrientStatement.nutrientStatementNumber
					},
					//success
					function (results) {
						self.isNutrientStatementCodeAvailable = results.data;
					},
					//error
					function(results) {
						self.isNutrientStatementCodeAvailable = "Not available";
					}
				);
			} else {
				self.isNutrientStatementCodeAvailable = "Not available";
			}
		};

		/**
		 * If the current mandated nutrients are still loading.
		 *
		 * @returns {boolean}
		 */
		self.isCurrentlyWaiting = function () {
			return self.isMandatedWaiting;
		};

		/**
		 * Sends nutrient statement to backend to be removed.
		 */
		self.removeNutrientStatement = function() {
			var nutrientStatementNumber = self.data[self.previousEditingIndex].nutrientStatementNumber;
			var deptList = [];
			for(var index = 0; index < self.departmentArray.length; index++){
				if(self.departmentArray[index].isSelected == true){
					deptList.push(self.departmentArray[index].id);
				}
			}
			self.isWaiting = true;
			scaleManagementApi.deleteNutrientStatement(
				{nutrientStatementNumber: nutrientStatementNumber,
					deptList: deptList}, self.reloadRemovedData, self.fetchError);
		};

		/**
		 * Return of back end after a user removes a nutrient statement. Based on the size of totalRecordCount,
		 * this method will either:
		 * 	--reset view to initial state
		 * 	--re-issue call to back end to update information displayed
		 *
		 * @param results Results from back end.
		 */
		self.reloadRemovedData = function(results){
			self.deleteMessage = results.message;
			self.removedNutrientStatement = true;
			if(self.totalRecordCount == null || self.totalRecordCount <= 1) {
				self.searchSelection = null;
				self.isWaiting = false;
				self.init();
			} else {
				self.newSearch(self.findAll);
			}
		};

		/**
		 * Apply the Nutrient round rules to the changed value (if changed).
		 *
		 * @param scope The Angular scope object. We'll use this to  pull out whether or not it is valid before
		 * trying to round.
		 * @param index The index of this control. This will be used to pull the form out of the page to chck
		 * for validity.
		 * @param nutrientStatementDetail current nutrient statement detail
		 */
		self.applyRoundingRuleToNutrientCode = function(scope, index, nutrientStatementDetail) {

			// Check to make sure what the user typed in is a valid number.
			var fieldName = "measure" + index;
			var field = scope.nutrientForm[fieldName];
			if (field.$error['pattern']) {

				self.fetchRoundingError(nutrientStatementDetail, {data: {message: 'Value must be numeric with no more than one decimal place.'}});
				nutrientStatementDetail.nutrientRoundingRequired = true;
				nutrientStatementDetail.stillNeedsRequired = false;
				return;
			}
			nutrientStatementDetail.roundingError = null;
			if (nutrientStatementDetail.nutrientStatementQuantity != null) {
				nutrientStatementDetail.nutrientRoundingRequired = true;
				nutrientStatementDetail.changedQuantity = true;
				nutrientStatementDetail.stillNeedsRequired = true;
				nutrientStatementDetail.isNewElement = undefined;
				scaleManagementApi.applyRoundingRuleToNutrient(nutrientStatementDetail,
					//success
					function (results) {
						field.$setValidity("rounding", true);
						nutrientStatementDetail.nutrientStatementQuantity = results.data.nutrientStatementQuantity;
						nutrientStatementDetail.nutrientDailyValue =  results.data.nutrientDailyValue;
						nutrientStatementDetail.nutrientRoundingRequired = false;
						nutrientStatementDetail.stillNeedsRequired = false;
					},
					//error
					function(error) {
						field.$setValidity("rounding", false);
						self.fetchRoundingError(nutrientStatementDetail, error);
						nutrientStatementDetail.stillNeedsRequired = false;
					}
				);
			}
		};

		/**
		 * Callback for when the backend rounding returns an error.
		 *
		 * @param detail Nutrient statement detail that has error.
		 * @param error The error from the backend.
		 */
		self.fetchRoundingError = function(detail, error) {
			detail.stillNeedsRequired = true;
			if (error && error.data) {
				if(error.data.message != null && error.data.message != "") {
					detail.roundingError = error.data.message;
				} else {
					detail.roundingError = error.data.error;
				}
			}
			else {
				detail.roundingError = self.UNKNOWN_ERROR;
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

		/**
		 * Prompts the department modal to remove nutrient statement.
		 *
		 * @param index
		 */
		self.chooseNutrientStatementDepartments = function(index){
			self.resetDepartmentList();
			self.deleteMessage = null;
			self.isRemovingNutrientStatement = true;
			self.previousEditingIndex = index;

			if(self.data[index].statementMaintenanceSwitch == 'A'){
				self.removeNutrientStatement();
			} else {
				$("#nutrientStatementChooseDepartmentModal").modal("show");
			}
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
		 * Updates all of the information for the serving size label.
		 */
		self.updateServingLabelComponents= function () {
			self.servingLabel="";
			var bothPresent = false;
			if(self.nutrientStatement.measureQuantity !=null && self.nutrientStatement.nutrientCommonUom !=null){
				self.parseNumberToText(self.nutrientStatement.measureQuantity);
				self.servingTextCharacterRemainingCharacterCount -= self.nutrientStatement.nutrientCommonUom.nutrientUomDescription.length;
				self.servingLabel += " " + self.nutrientStatement.nutrientCommonUom.nutrientUomDescription.trim();
				bothPresent = true;
			} else {
				self.servingLabel+="";
			}
			if(self.nutrientStatement.metricQuantity !=null && self.nutrientStatement.nutrientMetricUom != null){
				var metricPart = ""+ Math.round(self.nutrientStatement.metricQuantity)+ self.nutrientStatement.nutrientMetricUom.nutrientUomDescription.trim();
				if(bothPresent){
					metricPart = " (" + metricPart + ")";
				}
				self.servingLabel += metricPart;
			} else {
				self.servingLabel+="";
			}
			self.colorServingLabel();
		};

		/**
		 * Breaks up number to whole number and decimal components
		 * The first round for decimal is to correct for binary rounding errors.
		 *
		 * @param number
		 */
		self.parseNumberToText = function (number) {
			var wholeNumber = Math.floor(number);
			var decimal = Math.round(((number%1)*1000))/1000;
			self.convertDecimalToFraction(wholeNumber, Math.floor(((decimal)*100))/100);
		};

		/**
		 * This method will determine if the decimal is to be converted into a fraction or not.
		 *
		 * @param wholeNumber whole number component of fraction
		 * @param decimal decimal component to be possibly converted to fraction.
		 */
		self.convertDecimalToFraction = function (wholeNumber, decimal) {
			var decimalPart = "";
			var converted = false;
			switch(decimal){
				case(0.0):
					decimalPart = "";
					converted = true;
					break;
				case(0.08):
					decimalPart = "1/12 ";
					converted = true;
					break;
				case(0.10):
					decimalPart = "1/10";
					converted = true;
					break;
				case(0.12):
					decimalPart = "1/8";
					converted = true;
					break;
				case(0.16):
					decimalPart = "1/6";
					converted = true;
					break;
				case(0.20):
					decimalPart = "1/5";
					converted = true;
					break;
				case(0.25):
					decimalPart = "1/4";
					converted = true;
					break;
				case(0.33):
					decimalPart = "1/3";
					converted = true;
					break;
				case(0.50):
					decimalPart = "1/2";
					converted = true;
					break;
				case(0.66):
					decimalPart = "2/3";
					converted = true;
					break;
				case(0.75):
					decimalPart = "3/4";
					converted = true;
					break;
				default:
					decimalPart = "" + decimal;
					break;
			}
			if(wholeNumber>0) {
				if(converted){
					decimalPart = wholeNumber + " " +decimalPart;
				} else {
					decimalPart = wholeNumber + decimalPart.substring(1);
				}
			}
			self.servingLabel += decimalPart;
		};

		/**
		 * This method will determine which characters are in bounds (black) and which ones are out of bounds
		 * (anything over 22, red).
		 */
		self.colorServingLabel = function () {
			var servingLabelTextField = document.getElementById("scaleManagementNutrientStatementLabel");
			self.servingTextCharacterRemainingCharacterCount = 22 - self.servingLabel.length;
			if(self.servingLabel.length > 22){
				self.servingLabel = self.servingLabel.substring(0, 22).fontcolor("black") + self.servingLabel.substring(22).fontcolor("red");
			}
			servingLabelTextField.innerHTML = self.servingLabel;
		};

		/**
		 * The method will disable all of the increment/decrement features found int number inputs for HTML5.
		 *
		 * @param inputId
		 */
		self.disableNumberInputFeatures = function (inputId) {
			var input = document.getElementById(inputId);

			if (input == null) {
				return;
			}

			var ignoreKey = false;
			var handler = function(e) {
				if (e.keyCode == 38 || e.keyCode == 40) {
					e.preventDefault();
				}
			};
			input.addEventListener("mousewheel", function(event){ this.blur() });
			input.addEventListener('keydown',handler,false);
			input.addEventListener('keypress',handler,false);
		}

		/**
		 * Show varies when Servings Per Container = 999.
		 */
		self.updateShowVariesLabelComponents = function () {
			if(self.isServingSizeVariable()){
				self.showVariesLabel= self.VARIES_LABEL;
			}else{
				self.showVariesLabel="";
			}
		};

		/**
		 * Check varies when Servings Per Container = 999.
		 * @returns {boolean}
		 */
		self.isServingSizeVariable  = function () {
			if (self.nutrientStatement.servingsPerContainer != null && self.nutrientStatement.servingsPerContainer == self.servingPerContainerVariesVary){
				return true;
			}else{
				return false;
			}
		};
	}
})();
