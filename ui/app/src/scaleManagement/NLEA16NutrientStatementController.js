/*
 *
 * NLEA16NutrientStatementController.js
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
 * The controller for the Scale Management NLEA Nutrients Controller.
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('NLEA16NutrientStatementController', nutrientStatementController);

	/**
	 * Directive for shortening variable name.
	 */
	angular.module('productMaintenanceUiApp').directive('alias', function(){
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				var splits = attrs['alias'].trim().split(/\s+as\s+/);
				scope.$watch(splits[0], function(val) {
					scope.$eval(splits[1]+'=('+splits[0]+')');
				});
			}
		};
	});

	nutrientStatementController.$inject = ['ScaleManagementApi', 'ngTableParams', 'PermissionsService','urlBase','DownloadService'];

	/**
	 * Constructs the controller.
	 * @param scaleManagementApi The API to fetch data from the backend.
	 * @param ngTableParams The API to set up the report table.
	 * @param permissionsService The service that gives access to a user's permissions.
	 */
	function nutrientStatementController(scaleManagementApi, ngTableParams, permissionsService, urlBase, downloadService) {

		var self = this;

		/**
		 * Wheterer or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;
		/**
		 * Wheterer or not the controller is waiting for data editing nutrient statement.
		 * @type {boolean}
		 */
		self.isWaitingForEditingNutrientPanel = false;
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
		 * Whether or not the search search panel is collapsed or open.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;

		/**
		 * The source system reference id.
		 * @type {String}
		 */
		self.sourceSystemReferenceId = null;

		/**
		 * Wheterer or not the controller is waiting for search panel in add new panel modal.
		 * @type {boolean}
		 */
		self.isWaitingSearchPanel = false;

		/**
		 * Whether or not this is the first search with the current parameters.
		 * @type {boolean}
		 */
		self.firstSearch = true;

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
		 * Current String for the serving size label
		 *
		 * @type {string}
		 */
		self.servingLabel = "";

		/**
		 * Empty String
		 *
		 * @type {string}
		 */
		self.EMPTY_STRING = "";

		/**
		 * Remaining available characters for the serving label.
		 *
		 * @type {number}
		 */
		self.servingTextCharacterRemainingCharacterCount = 22;
		/**
		 * List of unit of measure
		 *
		 * @type {Array}
		 */
		self.measureUOMs = [];
		/**
		 * List of unit of metric
		 *
		 * @type {Array}
		 */
		self.metricUOMs = [];
		/**
		 * The measure uom code switch flag
		 *
		 * @type {Boolean}
		 */
		const MEASURE_UOM_CODES_SW = 'I';
		/**
		 * The metric uom code switch flag
		 *
		 * @type {Boolean}
		 */
		const METRIC_UOM_CODES_SW = 'M';

		/**
		 * Dual column Panel flag
		 *
		 * @type {Boolean}
		 */
		self.checkedDualColumnPanel = false;
		/**
		 * Showing Dual Panel flag
		 *
		 * @type {Boolean}
		 */
		self.isShowDualColumnPanel = false;
		/**
		 * Index for the column 1
		 *
		 * @type {Number}
		 */
		self.nutritionFactsLabelColumn1Index = 0;
		/**
		 * Index for the column 2
		 *
		 * @type {Number}
		 */
		self.nutritionFactsLabelColumn2Index = 1;
		/**
		 * Servings Per Container value = 999;.
		 *
		 * @type {number}
		 */
		self.servingPerContainerVaries = 999;
		/**
		 * Varies text.
		 *
		 * @type {String}
		 */
		self.VARIES_LABEL = " varies";
		/**
		 * Show varies label when Servings Per Container = 999.
		 *
		 * @type {string}
		 */
		self.showVariesLabel = "";
		/**
		 * Show error on panel header popup
		 *
		 * @type {string}
		 */
		self.panelModalError = null;
		/**
		 * Show success message on panel header popup
		 *
		 * @type {string}
		 */
		self.panelModalSuccess = null;

		const NO_CHANGE_UPDATE_MESSAGE = "There are no changes on this page to be saved. Please make any changes to update.";
		const MEASURE_QUANTITY_MSG = "Measure Quantity is a mandatory field, greater than or equal to 0 and less than or equal to 99999999.99.";
		const MEASURE_UOM_CODE_MSG = "Measure UOM Code is a mandatory field.";
		const METRIC_QUANTITY_MSG = "Metric Quantity is a mandatory field, greater than or equal to 0 and less than or equal to 99999999.99.";
		const METRIC_UOM_CODE_MSG = "Metric UOM Code is a mandatory field.";
		const SERVINGS_PER_CONTAINER_MSG = "Servings Per Container is a mandatory field, greater than 0 and less than or equal to 999.";
		const CALORIES_MSG = "Calories must be greater than or equal to 0 and less than or equal to 99999.9999.";
		const AMOUNT_MSG = "Amount must be greater than or equal to 0 and less than or equal to 99999.99.";
		const PDV_MSG = "PDV must be greater than or equal to 0 and less than or equal to 99999.99.";
		const SERVING_SIZE_LABEL_MSG = "Serving Size Label should be less than or equal to 22 characters.";
		const STATEMENT_ID_MANDATORY_MSG = "Statement ID is a mandatory field.";
		const STATEMENT_ID_INVALID_MSG = "Nutrient statement number 0 is invalid; it must be a positive number with fewer than six digits.";
		const STATEMENT_ID_NOT_AVAILABLE_MSG = "Statement ID is not available.";
		const NOT_AVAILABLE_MSG = "Not available";
		const NUTRIENT_PDV = 'P';
		const DELETE_STATEMENT_ID_SUCCESS_MSG = "Successfully Updated";
		self.AVAILABLE_MSG = "Available";

		/**
		 * Maximum of Serving Size Label to show on UI.
		 * @type {number}
		 */
		self.MAX_LENGTH_SERVING_SIZE_LABEL = 22;

		/**
		 * Current non zero list.
		 * @type {Array}
		 */
		self.currentNonZeroNutrientList = [];

		/**
		 * Red color for CSS.
		 * @type {string}
		 */
		const RED_COLOR = 'red';

		/**
		 * Black color for CSS.
		 * @type {string}
		 */
		const BLACK_COLOR = 'black';

		/**
		 * Flag when user add new NLEA 2016 Nutrient Statements
		 *
		 * @type {Boolean}
		 */
		self.isAddNewNutrientStatement = false;

        /**
         * Delete nutrient statement by statement Id.
         * @type {boolean}
         */
		self.isDeleteNutrientStatement = false;

		/**
		 * Initialize the controller.
		 */
		self.init = function(){
			self.searchSelection = null;
			self.modifyMessage = null;
			self.findAllUnitOfMeasures();
			$('#addNewPanelModal').on('shown.bs.modal', function () {
				$('#sourceSystemReferenceId').focus();
			})
		};

		/**
		 * get all unit of measures.
		 */
		self.findAllUnitOfMeasures = function () {
			scaleManagementApi.findAllUnitOfMeasures().$promise.then(function (response) {
				self.unitOfMeasures = response;
				self.measureUOMs = response.filter(function(element) {
					return element.metricOrImperialUomSwitch.trim() === MEASURE_UOM_CODES_SW;
				});
				self.metricUOMs = response.filter(function(element) {
					return element.metricOrImperialUomSwitch.trim() === METRIC_UOM_CODES_SW;
				});
			}, self.handleError);
		};

		/**
		 * Returns whether or not the user is allowed to edit NLEA nutrient statements.
		 *
		 * @returns {boolean} Whether or not the user is allowed to edit NLEA nutrient statements.
		 */
		self.canEditNutrientStatement = function() {
			return permissionsService.getPermissions("SM_NLEA_01", "EDIT");
		};

		/**
		 * Clears the search box.
		 */
		self.clearSearch = function () {
			self.searchSelection = null;
		};

		/**
		 * get nutrient statement panel header by source system reference id.
		 */
		self.getNutrientStatementPanelHeaderBySourceSystemReferenceId = function(){
			self.errorSearchPanel = null;
			self.isWaitingSearchPanel = true;
			scaleManagementApi.getNutrientStatementPanelHeaderBySourceSystemReferenceId({
					sourceSystemReferenceId: parseInt(self.sourceSystemReferenceId)
				},
				self.handleAddNewPanelResult,
				self.handleAddNewPanelError);

		};

		/**
		 * Handle go to add new or edit panel page after search data in add new panel.
		 */
		self.handleAddNewPanelResult = function (results){
			if(results == null || results.nutrientPanelHeaderId == null){
				self.viewPanelModalTitle = "Create NLEA 2016 Nutrient Statements";
				self.isAddNewNutrientStatement = true;
				self.createNewNutrientStatement();
			}else{
				self.viewPanelModalTitle = "Nutrient Statement Detail";
				self.setDataNutrientStatementDetail(results);
			}
			$('#addNewPanelModal').modal("hide");
			$('#addNewPanelModal').on('hidden.bs.modal', function () {
				if(self.isWaitingSearchPanel){
					self.isWaitingSearchPanel = false;
					$("#viewPanelModal").modal("show");
				}
			});
		};

		/**
		 * Initialize a new nutrient statement.
		 */
		self.createNewNutrientStatement = function () {
			self.error = null;
			self.success = null;
			self.modifyMessage = null;
			self.panelModalSuccess = null;
			self.panelModalError = null;
			self.isShowDualColumnPanel = false;
			self.checkedDualColumnPanel = false;
			self.lastRequest = 0;
			self.availableOrNotAvailableNutrientStatementMessage = self.AVAILABLE_MSG;
			self.nutrientStatementDetail= {
				sourceSystemReferenceId: self.sourceSystemReferenceId
			};
			self.buildServingSizeLabel();
			self.updateShowVariesLabelComponents();
			self.getMandatedNutrients();
		};

		/**
		 * Gets available mandated nutrients... if there are any available.
		 *
		 */
		self.getMandatedNutrients = function() {
			self.isMandatedWaiting = true;
			self.currentNonZeroNutrientList = [];
			scaleManagementApi.getMandatedNutrients({},
				self.handleMandatedNutrientsResult,
				function (errors) {
					self.currentNonZeroNutrientList = [];
					self.isMandatedWaiting = false;
				}
			);
		};

		/**
		 * Handle add nutrient panel detail for new nutrient panel column header.
		 */
		self.handleMandatedNutrientsResult = function (results) {
			self.currentNonZeroNutrientList = results;
			if (self.isAddNewNutrientStatement) {
				var nutrientPanelColumnHeaders = [{
					nutrientPanelDetails: [],
					caloriesQuantity: 0,
					key: {
						nutrientPanelColumnId: 1
					}
				}];
				self.nutrientStatementDetail.nutrientPanelColumnHeaders = nutrientPanelColumnHeaders;
				for (var index = 0; index < self.currentNonZeroNutrientList.length; index++) {
					self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].nutrientPanelDetails.splice(index, 0, self.currentNonZeroNutrientList[index]);
					self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].nutrientPanelDetails[index].key.nutrientPanelColumnId = 1;
					if (self.currentNonZeroNutrientList[index].nutrient.recommendedDailyAmount != null) {
						self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].nutrientPanelDetails[index].nutrientDailyValue = 0
					}
					if (self.currentNonZeroNutrientList[index].nutrient.pdvOrWhole != NUTRIENT_PDV) {
						self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].nutrientPanelDetails[index].nutrientQuantity = 0
					}
				}
			}
			self.isMandatedWaiting = false;
		}

		/**
		 * If the current typed in statement ID is available or not.
		 */
		self.checkAvailableNutrientStatement = function () {
			var thisRequest = ++self.lastRequest;
			self.availableOrNotAvailableNutrientStatementMessage = "";
			if (self.nutrientStatementDetail.sourceSystemReferenceId != null) {
				scaleManagementApi.isNutrientStatementExists({
					nutrientStatementNumber: parseInt(self.nutrientStatementDetail.sourceSystemReferenceId)
					},
					//success
					function (results) {
						if(thisRequest == self.lastRequest){
							self.availableOrNotAvailableNutrientStatementMessage = results.data ? self.AVAILABLE_MSG : NOT_AVAILABLE_MSG;
						}
					},
					//error
					function (errors) {
						self.availableOrNotAvailableNutrientStatementMessage = NOT_AVAILABLE_MSG;
					}
				);
			} else {
				self.availableOrNotAvailableNutrientStatementMessage = NOT_AVAILABLE_MSG;
			}
		};

		/**
		 * Callback for when has an error.
		 *
		 * @param error The error.
		 */
		self.handleAddNewPanelError = function(error) {
			self.isWaitingSearchPanel = false;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.errorSearchPanel = error.data.message;
				} else {
					self.errorSearchPanel = error.data.error;
				}
			}
			else {
				self.errorSearchPanel = "An unknown error occurred.";
			};
		};
		/**
		 * Initiates a new search.
		 */
		self.newSearch = function(findAll){
			self.modifyMessage = null;
			self.firstSearch = true;
			self.findAll = findAll;
			self.tableParams.page(1);
			self.tableParams.reload();
		};

		/**
		 * Issue call to newSearch to call back end to fetch all nutrition codes.
		 */
		self.searchAll = function (){
			self.searchSelection = null;
			self.newSearch(true);
		};

		/**
		 * Checks whether searchSelection is null and if the find all option is not selected
		 * @returns {boolean} return true
		 */
		self.isCurrentStateNull = function(){
			return (!self.findAll && self.searchSelection == null);

		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function() {
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
						self.getReport(params.page() - 1);
					}
				}
			);
		};

		/**
		 * The parameters that define the table showing the report.
		 */
		self.tableParams = self.buildTable();

		/**
		 * Search  all or search by statement id NLEA nutrient statement panel headers.
		 *
		 * @param page The page to get.
		 */
		self.getReport = function (page) {
			if (self.findAll) {
				self.getReportByAll(page);
				return;
			}else if(self.selectionType == self.STATEMENT_ID){
				self.getReportByStatementId(page);
				return;
			}
		};

		/**
		 * Calls API method to search data(NLEA Nutrient Statement Panel Header) a based on statement id.
		 *
		 * @param page
		 */
		self.getReportByStatementId = function (page) {
			var searchSelection = [];
			searchSelection = self.searchSelection.split(/[\n,]/g);
			scaleManagementApi.findAllNutrientStatementPanelsByIds({
					nutrientStatementId: searchSelection,
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: self.includeCounts
				},
				self.loadData,
				self.handleError);
		};
		/**
		 * Call API to search all NLEA Nutrient Statement panel headers.
		 *
		 * @param page
		 */
		self.getReportByAll = function (page) {
			scaleManagementApi.findAllNutrientStatementPanels({
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: true
				},
				self.loadData,
				self.handleError);
		};

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results){
			self.error = null;
			if(self.success != null){
			    self.isDeleteNutrientStatement = false;
            }
			self.success = null;
			self.isWaiting = false;
			// // If this was the first page, it includes record count and total pages .
			self.totalRecordCount = 0;
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No records found.";
			} else {
				self.resultMessage = self.getResultMessage(results.data.length, results.page);
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
			if(self.isDeleteNutrientStatement){
			    self.success = DELETE_STATEMENT_ID_SUCCESS_MSG;
                self.error = null;
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
			return "Result " + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " +
				self.totalRecordCount.toLocaleString();
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = self.generateExportUrl();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'NLEANutrientStatements.csv', self.WAIT_TIME,
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
			if (self.selectionType == self.STATEMENT_ID && !self.findAll){
				return urlBase + '/pm/NLEA16NutrientStatement/exportNutrientStatementPanelByIdsToCsv?nutrientStatements=' +
					encodeURI(self.searchSelection) + "&totalRecordCount=" + self.totalRecordCount;
			} else if (self.findAll){
				return urlBase + '/pm/NLEA16NutrientStatement/exportAllNutrientStatementPanelToCsv?totalRecordCount=' +
					self.totalRecordCount;
			} else {
				return self.EMPTY_STRING;
			}
		};
		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.handleError = function(error) {
			self.isWaiting = false;
			self.message = null;
			if (error && error.data) {
				if (error.data.message !== null && error.data.message !== "") {
					self.error = error.data.message;
				} else {
					self.error = error.data.error;
				}
			}
			else {
				self.error = "An unknown error occurred.";
			};
		};

		/**
		 * Handle view nutrientStatement full panel popup.
		 * @param nutrientStatement nutrientStatement selected.
		 */
		self.viewFullPanel = function (nutrientStatement) {
			self.setDataNutrientStatementDetail(nutrientStatement);
			self.viewPanelModalTitle = "Nutrient Statement Detail";
			$("#viewPanelModal").modal("show");
		};

		/**
		 * Set data details for nutrient statement.
		 * @param nutrientStatement nutrientStatement.
		 */
		self.setDataNutrientStatementDetail = function (nutrientStatement) {
			self.isAddNewNutrientStatement = false;
			self.error = null;
			self.success = null;
			self.modifyMessage = null;
			self.panelModalSuccess = null;
			self.panelModalError = null;
			self.nutrientStatementDetail = angular.copy(nutrientStatement);
			//Sort nutrient by sequence number.
			if (self.nutrientStatementDetail.nutrientPanelColumnHeaders === null
				|| self.nutrientStatementDetail.nutrientPanelColumnHeaders.length <= 1) {
				self.checkedDualColumnPanel = false;
				self.isShowDualColumnPanel = false;
			} else if (self.nutrientStatementDetail.nutrientPanelColumnHeaders.length > 1){
				self.checkedDualColumnPanel = true;
				self.isShowDualColumnPanel = true;
			}
			angular.forEach(self.nutrientStatementDetail.nutrientPanelColumnHeaders, function (nutrientPanelColumnHeader) {
				nutrientPanelColumnHeader.nutrientPanelDetails.sort(function(nutrientPanelDetailOne, nutrientPanelDetailsTwo) {
					return nutrientPanelDetailOne.nutrient.nutrientDisplaySequenceNumber - nutrientPanelDetailsTwo.nutrient.nutrientDisplaySequenceNumber;
				});
			});

			for(var i = 0; i < self.nutrientStatementDetail.nutrientPanelColumnHeaders; i++){
				if(self.nutrientStatementDetail.nutrientPanelColumnHeaders[i].key.nutrientPanelColumnId == 1){
					self.nutritionFactsLabelColumn1Index = i;
				}
				if(self.nutrientStatementDetail.nutrientPanelColumnHeaders[i].key.nutrientPanelColumnId == 2){
					self.nutritionFactsLabelColumn2Index = i;
				}
			}
			self.nutrientStatementDetailOrg = angular.copy(self.nutrientStatementDetail);
			self.buildServingSizeLabel();
			self.updateShowVariesLabelComponents();
		}

		/**
		 * Generate serving size label on UI based on the changes: Measure Quantity, Measure UOM Code, Metric Quantity, Metric UOM Code.
		 */
		self.buildServingSizeLabel= function () {
			self.servingLabel="";
			var bothPresent = false;

			for(var i=0; i < self.measureUOMs.length; i++) {
				if (self.nutrientStatementDetail.imperialServingSizeUomId === self.measureUOMs[i].uomCode) {
					self.nutrientStatementDetail.nutrientImperialUom = angular.copy(self.measureUOMs[i]);
					break;
				}
			}
			for(var i=0; i < self.metricUOMs.length; i++) {
				if (self.nutrientStatementDetail.metricServingSizeUomId === self.metricUOMs[i].uomCode) {
					self.nutrientStatementDetail.nutrientMetricUom = angular.copy(self.metricUOMs[i]);
					break;
				}
			}
			if(!self.isEmpty(self.nutrientStatementDetail.measureQuantity) && !self.isEmpty(self.nutrientStatementDetail.nutrientImperialUom)){
				self.formatMeasureQuantity(self.nutrientStatementDetail.measureQuantity);
				self.servingTextCharacterRemainingCharacterCount -= self.nutrientStatementDetail.nutrientImperialUom.uomDisplayName.length;
				if(!self.isEmpty(self.nutrientStatementDetail.imperialServingSizeUomId)){
					self.servingLabel += " " + self.nutrientStatementDetail.nutrientImperialUom.uomDisplayName.trim();
					bothPresent = true;
				}
			} else {
				self.servingLabel+="";
			}
			if(!self.isEmpty(self.nutrientStatementDetail.metricQuantity)&& !self.isEmpty(self.nutrientStatementDetail.nutrientMetricUom)){
				var metricPart = ""+ Math.round(self.nutrientStatementDetail.metricQuantity)+ self.nutrientStatementDetail.nutrientMetricUom.uomDisplayName.trim();
				if(!self.isEmpty(self.nutrientStatementDetail.metricServingSizeUomId)){
					if(bothPresent){
						metricPart = " (" + metricPart + ")";
					}
					self.servingLabel += metricPart;
				}
			} else {
				self.servingLabel+="";
			}
			self.colorServingLabel();
		};

		/**
		 * This method will determine which characters are in bounds (black) and which ones are out of bounds
		 * (anything over 22, red).
		 */
		self.colorServingLabel = function () {
			var servingLabelTextFieldSingleColumnPanel = document.getElementById("scaleManagementNutrientStatementLabel");
			var servingLabelTextFieldDualColumnPanel = document.getElementById("scaleManagementNutrientStatementLabelDualColumnPanel");
			self.servingTextCharacterRemainingCharacterCount = self.MAX_LENGTH_SERVING_SIZE_LABEL - self.servingLabel.length;
			if(self.servingLabel.length > self.MAX_LENGTH_SERVING_SIZE_LABEL){
				self.servingLabel = self.servingLabel.substring(0, self.MAX_LENGTH_SERVING_SIZE_LABEL).fontcolor("BLACK_COLOR")
					+ self.servingLabel.substring(self.MAX_LENGTH_SERVING_SIZE_LABEL).fontcolor("RED_COLOR");
			}
			if(servingLabelTextFieldSingleColumnPanel !== null){
				servingLabelTextFieldSingleColumnPanel.innerHTML = self.servingLabel;
			}
			if(servingLabelTextFieldDualColumnPanel !== null){
				servingLabelTextFieldDualColumnPanel.innerHTML = self.servingLabel;
			}

		};

		/**
		 * Breaks up number to whole number and decimal components
		 * The first round for decimal is to correct for binary rounding errors.
		 * Example usage:
		 * -If number value is "2.2" then wholeNumber value is "2" and decimal value is "0.2"
		 * -If number value is "2.33" then wholeNumber value is "2" and decimal value is "0.33"
		 * -If number value is "2.75" then wholeNumber value is "2" and decimal value is "0.75"
		 *
		 * @param number
		 */
		self.formatMeasureQuantity = function (number) {
			var wholeNumber = Math.floor(Number(number));
			var decimal = Math.round(((number%1)*1000))/1000;
			self.convertDecimalToFraction(wholeNumber, Math.floor(((decimal)*100))/100);
		};

		/**
		 * This method will determine if the decimal is to be converted into a fraction or not.
		 * Example usage:
		 * -If wholeNumber value is "2" and decimal value is "0.2" then Serving Size Label value is "2 1/5"
		 * -If wholeNumber value is "2" and decimal value is "0.33" then Serving Size Label value is "2 1/3"
		 * -If wholeNumber value is "2" and decimal value is "0.75" then Serving Size Label value is "2 3/4"
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
					if (decimalPart) {
						decimalPart = wholeNumber + " " +decimalPart;
					} else {
						decimalPart = wholeNumber;
					}
				} else {
					decimalPart = wholeNumber + decimalPart.substring(1);
				}
			}
			self.servingLabel += decimalPart;
		};

		/**
		 * Call API to update NLEA2016 Nutrient Statement.
		 */
		self.updateStatement = function () {
			self.panelModalSuccess = null;
			self.panelModalError = null;
			if(self.isAddNewNutrientStatement || self.isPanelModified()){
				var errorMessages = self.validatePanel(self.nutrientStatementDetail);
				if(errorMessages == null || errorMessages.length == 0){
					if(self.isAddNewNutrientStatement){
						if(self.availableOrNotAvailableNutrientStatementMessage == self.AVAILABLE_MSG){
							self.isWaitingForEditingNutrientPanel = true;
							self.nutrientStatementDetail.sourceSystemReferenceId = parseInt(self.nutrientStatementDetail.sourceSystemReferenceId);
							scaleManagementApi.addNLEA16Statement(
							self.nutrientStatementDetail, self.loadDataAfterUpdatingNutrientStatement, self.handleError);
						}
					}else{
						self.isWaitingForEditingNutrientPanel = true;
						scaleManagementApi.updateNLEA16Statement(
							self.nutrientStatementDetail, self.loadDataAfterUpdatingNutrientStatement, self.handleError);
					}
				}else{
					self.panelModalError = 'Nutrient Statement:';
					angular.forEach(errorMessages, function (value) {
						self.panelModalError += "<li>" +value + "</li>";
					});
				}
			} else {
				self.panelModalError = NO_CHANGE_UPDATE_MESSAGE;
			}
		};

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadDataAfterUpdatingNutrientStatement = function (results) {
			self.error = null;
			self.success = null;
			if (self.isAddNewNutrientStatement) {
				$("#viewPanelModal").modal("hide");
				self.searchSelection = results.data.sourceSystemReferenceId
				self.newSearch();
				self.modifyMessage = results.message;
			} else {
				self.viewFullPanel(results.data);
				self.panelModalSuccess = results.message;
			}
			self.isWaitingForEditingNutrientPanel = false;
			self.tableParams.reload();
		};

		/**
		 * Handle when user click on the checkbox on single column panel
		 */
		self.toggleDualColumnPanelChoice = function () {
			if (!self.checkedDualColumnPanel && self.isShowDualColumnPanel) {
				$("#deleteDualColumnPanelConfirm").modal({ backdrop: 'static', keyboard: false });
			} else if (self.checkedDualColumnPanel && !self.isShowDualColumnPanel) {
				if (self.nutrientStatementDetail.nutrientPanelColumnHeaders != null && self.nutrientStatementDetail.nutrientPanelColumnHeaders.length > 0) {
					self.isShowDualColumnPanel = true;
					var item = angular.copy(self.nutrientStatementDetail.nutrientPanelColumnHeaders[0]);
					item.key.nutrientPanelColumnId = self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].key.nutrientPanelColumnId + 1;
					item.caloriesQuantity = 0;
					item.servingSizeLabel = null;
					angular.forEach(item.nutrientPanelDetails, function (nutrientPanelDetail) {
						nutrientPanelDetail.key.nutrientPanelColumnId = self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].key.nutrientPanelColumnId + 1;
						nutrientPanelDetail.nutrientDailyValue = nutrientPanelDetail.nutrient.recommendedDailyAmount != null ? 0 : null;
						nutrientPanelDetail.nutrientQuantity = nutrientPanelDetail.nutrient.pdvOrWhole != NUTRIENT_PDV ? 0 : null;
					});
					self.nutrientStatementDetail.nutrientPanelColumnHeaders.push(item);
				} else {
					self.getMandatedNutrients();
				}
			}
		};

		/**
		 * Check varies when Servings Per Container = 999.
		 * @returns {boolean}
		 */
		self.isServingSizeVariable  = function () {
			if (self.nutrientStatementDetail.servingsPerContainer != null && self.nutrientStatementDetail.servingsPerContainer == self.servingPerContainerVaries){
				return true;
			}
				return false;
		};
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
		 * Delete dual column panel.
		 * @param index - The index of column panel.
		 */
		self.deleteDualColumnPanel = function (index) {
			//self.isDeletingDualPanel = false;
			if (self.isAddNewNutrientStatement) {
				self.nutrientStatementDetail.nutrientPanelColumnHeaders.splice(index, 1);
				self.checkedDualColumnPanel = false;
				self.isShowDualColumnPanel = false;
			} else {
				self.panelModalError = null;
				self.panelModalSuccess = null;
				var errorMessages = self.validatePanel(self.nutrientStatementDetail);
				if (errorMessages == null || errorMessages.length == 0) {
					self.isWaitingForEditingNutrientPanel = true;
					self.nutrientStatementDetail.nutrientPanelColumnHeaders.splice(index, 1);
					self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].key.nutrientPanelColumnId = 1;
					self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].servingSizeLabel = null;
					angular.forEach(self.nutrientStatementDetail.nutrientPanelColumnHeaders[0].nutrientPanelDetails, function (nutrientPanelDetail) {
						nutrientPanelDetail.key.nutrientPanelColumnId = 1;
					});
					scaleManagementApi.updateNLEA16Statement(
						self.nutrientStatementDetail, self.loadDataAfterUpdatingNutrientStatement, self.handleError);
				} else {
					self.checkedDualColumnPanel = true;
					self.panelModalError = 'Nutrient Statement:';
					angular.forEach(errorMessages, function (value) {
						self.panelModalError += "<li>" + value + "</li>";
					});
				}
			}
		};

		/**
		 * Changes the edit mode of a nutrient detail object to the opposite of what it currently is.
		 *
		 * @param nutrientDetail The nutrient detail object to flip the edit mode of.
		 */
		self.toggleEditMode = function(nutrientDetail) {
			nutrientDetail.inAlternateEditMode = !nutrientDetail.inAlternateEditMode;
			nutrientDetail.isPanelModified = true;
		};

		/**
		 * Returns true if there's been a change made.
		 * @returns {boolean}
		 */
		self.isPanelModified = function(){
			return (self.nutrientStatementDetail.measureQuantity !== self.nutrientStatementDetailOrg.measureQuantity
			|| self.nutrientStatementDetail.imperialServingSizeUomId !== self.nutrientStatementDetailOrg.imperialServingSizeUomId
			|| self.nutrientStatementDetail.metricQuantity !== self.nutrientStatementDetailOrg.metricQuantity
			|| self.nutrientStatementDetail.metricServingSizeUomId !== self.nutrientStatementDetailOrg.metricServingSizeUomId
			|| self.nutrientStatementDetail.servingsPerContainer != self.nutrientStatementDetailOrg.servingsPerContainer
			|| angular.toJson(self.nutrientStatementDetail.nutrientPanelColumnHeaders) != angular.toJson(self.nutrientStatementDetailOrg.nutrientPanelColumnHeaders));
		};
		/**
		 * This method is used to validate data before call save.
		 * @param item - The nutrient statement object.
		 * @returns {Array} - The list of error messages.
		 */
		self.validatePanel = function (item) {
			var errorMessages = [];
			var regexInt = new RegExp("^[0-9]{1,3}?$");
			var regexDouble = new RegExp("^[0-9]{1,8}(\\.[0-9]{0,2})?$");
			var regexCalories = new RegExp("^[0-9]{1,5}(\\.[0-9]{0,4})?$");
			var regexAmount = new RegExp("^[0-9]{1,5}(\\.[0-9]{0,2})?$");
			var regexPDV = new RegExp("^[0-9]{1,5}(\\.[0-9]{0,2})?$");

			if(self.isAddNewNutrientStatement){
				if(self.isEmpty(self.nutrientStatementDetail.sourceSystemReferenceId)){
					errorMessages.push(STATEMENT_ID_MANDATORY_MSG);
				}else if(parseInt(self.nutrientStatementDetail.sourceSystemReferenceId) == 0){
					errorMessages.push(STATEMENT_ID_INVALID_MSG);
				}else if(self.availableOrNotAvailableNutrientStatementMessage == NOT_AVAILABLE_MSG){
					errorMessages.push(STATEMENT_ID_NOT_AVAILABLE_MSG);
				}
			}

			if(self.isEmpty(item.measureQuantity)){
				errorMessages.push(MEASURE_QUANTITY_MSG);
			}else if(!regexDouble.test(item.measureQuantity)){
				errorMessages.push(MEASURE_QUANTITY_MSG);
			}
			if(self.isEmpty(item.imperialServingSizeUomId)){
				errorMessages.push(MEASURE_UOM_CODE_MSG);
			}
			if(self.isEmpty(item.metricQuantity)){
				errorMessages.push(METRIC_QUANTITY_MSG);
			}else if(!regexDouble.test(item.metricQuantity)){
				errorMessages.push(METRIC_QUANTITY_MSG);
			}
			if(self.isEmpty(item.metricServingSizeUomId)){
				errorMessages.push(METRIC_UOM_CODE_MSG);
			}
			if(self.isEmpty(item.servingsPerContainer)){
				errorMessages.push(SERVINGS_PER_CONTAINER_MSG);
			}else if(!regexInt.test(item.servingsPerContainer)){
				errorMessages.push(SERVINGS_PER_CONTAINER_MSG);
			}else if(item.servingsPerContainer <= 0 || item.servingsPerContainer > 999){
				errorMessages.push(SERVINGS_PER_CONTAINER_MSG);
			}

			if(item.nutrientPanelColumnHeaders !== null && item.nutrientPanelColumnHeaders.length > 0) {
				angular.forEach(item.nutrientPanelColumnHeaders, function (nutrientPanelColumnHeader) {//2 columns
					if(nutrientPanelColumnHeader.caloriesQuantity === undefined){
						if(errorMessages.indexOf(CALORIES_MSG) === -1){
							errorMessages.push(CALORIES_MSG);
						}
					}else{
						if(nutrientPanelColumnHeader.caloriesQuantity !== null
							&& nutrientPanelColumnHeader.caloriesQuantity !== ''
							&& !regexCalories.test(nutrientPanelColumnHeader.caloriesQuantity)){
							if(errorMessages.indexOf(CALORIES_MSG) === -1){
								errorMessages.push(CALORIES_MSG);
							}
						}
					}
					angular.forEach(nutrientPanelColumnHeader.nutrientPanelDetails, function (nutrientPanelDetail) {//14 rows
						if(nutrientPanelDetail.nutrientQuantity === undefined){
							if(errorMessages.indexOf(AMOUNT_MSG) === -1){
								errorMessages.push(AMOUNT_MSG);
							}
						}else{
							if(nutrientPanelDetail.nutrientQuantity !== null
								&& nutrientPanelDetail.nutrientQuantity !== ''
								&& !regexAmount.test(nutrientPanelDetail.nutrientQuantity)){
								if(errorMessages.indexOf(AMOUNT_MSG) === -1){
									errorMessages.push(AMOUNT_MSG);
								}
							}
						}
						if(nutrientPanelDetail.nutrientDailyValue === undefined){
							if(errorMessages.indexOf(PDV_MSG) === -1){
								errorMessages.push(PDV_MSG);
							}
						}else{
							if(nutrientPanelDetail.nutrientDailyValue !== null
								&& nutrientPanelDetail.nutrientDailyValue !== ''
								&& !regexPDV.test(nutrientPanelDetail.nutrientDailyValue)){
								if(errorMessages.indexOf(PDV_MSG) === -1){
									errorMessages.push(PDV_MSG);
								}
							}
						}
					});
				});
			}
			if(self.servingTextCharacterRemainingCharacterCount < 0){
				errorMessages.push(SERVING_SIZE_LABEL_MSG);
			}
			return errorMessages;
		};
		/**
		 * Check empty value.
		 *
		 * @param val
		 * @returns {boolean}
		 */
		self.isEmpty = function(val){
			if (val == null || val == undefined || val == "") {
				return true;
			}
			return false;
		};
		/**
		 * Calculate percentage for daily value when change amount value.
		 * @param scope the scope.
		 * @param fieldName the field name.
		 * @param panelDetail the nutrient panel detail.
		 */
		self.calculateDailyValue = function (scope, fieldName, panelDetail) {
			var field = scope.nutrientForm[fieldName];
			if (field.$error['pattern']) {
				return;
			}
			if (panelDetail.nutrientQuantity != null && panelDetail.nutrient.recommendedDailyAmount != null) {
				if (panelDetail.nutrientQuantity == "") panelDetail.nutrientQuantity = 0;
				panelDetail.nutrientDailyValue = Math.round((panelDetail.nutrientQuantity / panelDetail.nutrient.recommendedDailyAmount) * 100);
			}
		};

        /**
		 * Select a nutrient statement to delete.
         * @param nutrientStatement nutrientStatement selected.
         */
		self.getNutrientStatement = function (nutrientStatement) {
            self.nutrientStatementDetail = angular.copy(nutrientStatement);
        };

        /**
		 *  Load data after delete nutrient statement.
         */
		self.loadDataAfterDeleteNutrientStatement = function () {
            self.isDeleteNutrientStatement = true;
            self.firstSearch = true;
            self.tableParams.page(1);
            self.tableParams.reload();
        };

        /**
		 * Delete a nutrient statement.
         */
		self.deleteNLEA16NutrientStatement = function () {
            self.isWaiting = true;
			scaleManagementApi.deleteNLEA16NutrientStatement(
                {nutrientCode: self.nutrientStatementDetail.nutrientPanelHeaderId}, self.loadDataAfterDeleteNutrientStatement, self.handleError);
        };
	}
})();
