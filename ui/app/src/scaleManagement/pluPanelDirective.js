/*
 * pluPanel.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

/**
 * Directive to support the PLU search by code type.
 *
 * @author m314029
 * @since 2.0.8
 */
'use strict';

(function() {
	var app = angular.module('productMaintenanceUiApp');
	app.directive('pluPanel', pluPanel);
	pluPanel.$inject = ['ScaleManagementApi','ngTableParams', 'urlBase', 'DownloadService', 'singlePluPanelService'];

	function pluPanel(scaleManagementApi, ngTableParams, urlBase, downloadService, singlePluPanelService) {
		return {
			restrict: 'E',
			templateUrl: 'src/scaleManagement/pluPanel.html',
			scope: {
				currentCode: '=',
				codeType: '='
			},
			replace: false,
			controllerAs: 'pluPanelDirective',
			controller: function ($scope) {

				var self = this;

				const NOT_SINGLE_VIEW = "notSingleView";
				const SINGLE_VIEW = "singleView";

				// variables accessed in pluPanel.html (need 'self' reference)

				/**
				 * Tracks whether or not the user is waiting for data from the back end.
				 * @type {boolean}
				 */
				self.isWaitingForPlus = false;
				/**
				 * The action code data being shown in the report.
				 * @type {Array}
				 */
				self.data = null;
				/**
				 * Holds error messages.
				 * @type {string}
				 */
				self.error = null;
				/**
				 * The message to display about the number of records viewing and total (eg. Result 1-100 of 130).
				 * @type {String}
				 */
				self.pluResultMessage = null;

				self.currentView = NOT_SINGLE_VIEW;

				self.init = function () {
					self.currentView= NOT_SINGLE_VIEW;
					singlePluPanelService.setCurrentView(self.currentView);
				};

				self.getSinglePlu=function(scaleUpc, index){
					singlePluPanelService.setCurrentView(SINGLE_VIEW);
					singlePluPanelService.setSelectedScaleUpc(angular.copy(scaleUpc));
					singlePluPanelService.setSelectedScaleUpcIndex(index);
					singlePluPanelService.setData(self.data);
					self.currentView=SINGLE_VIEW;
				};

				self.isViewPluPanel=function () {
					self.currentView=singlePluPanelService.getCurrentView();
					return !self.isWaitingForPlus && self.data != null && self.currentView == NOT_SINGLE_VIEW;
				};

				self.isViewingSinglePlu=function () {
					self.currentView=singlePluPanelService.getCurrentView();
					return !self.isWaitingForPlus && self.data != null && self.currentView == SINGLE_VIEW;
				};

				// variables only needed inside this controller

				/**
				 * Whether back end should get counts or not.
				 * @type {boolean}
				 */
				var includeCounts = true;
				/**
				 * Whether or not this is the first search with the current parameters.
				 * @type {boolean}
				 */
				var firstPluSearch = true;
				/**
				 * The promise given to the ng-tables getData method. It should be called after data is fetched.
				 * @type {null}
				 */
				var pluDefer = null;
				/**
				 * The parameters passed to the application from ng-tables getData method. These need to be stored
				 * until all the data are fetched from the backend.
				 * @type {null}
				 */
				var pluDataResolvingParams = null;
				/**
				 * The total number of records in the report.
				 * @type {int}
				 */
				var totalPluRecordCount = 0;


				// string code constants
				var ACTION_CODE = "action code";

				var FORMAT_CODE = "format code";

				var GRAPHICS_CODE = "graphics code";

				var INGREDIENT_STATEMENT = "ingredient statement";

				// integer constants
				var PAGE_SIZE = 10;
				var FIRST_PAGE = 1;
				var FIRST_ELEMENT = 1;

				/**
				 * Constructs the ng-table.
				 */
				var buildPluTable = function () {
					return new ngTableParams(
						{
							page: FIRST_PAGE,
							count: PAGE_SIZE
						}, {
							counts: [],
							getData: function ($defer, params) {
								if($scope.currentCode == null){
									return;
								}
								self.isWaitingForPlus = true;
								pluDefer = $defer;
								pluDataResolvingParams = params;
								includeCounts = false;
								if (firstPluSearch) {
									includeCounts = true;
									firstPluSearch = false;
								}

								getPlusByCodeType(params.page() - 1);
							}
						}
					)
				};

				/**
				 * Selects the appropriate PLU search by the code type.
				 *
				 * @param page The page number requested.
				 */
				var getPlusByCodeType = function(page){
					switch ($scope.codeType){
						case ACTION_CODE: {
							scaleManagementApi.queryForPLUsByActionCode({
								actionCode: $scope.currentCode.actionCode,
								includeCounts: includeCounts,
								page: page,
								pageSize: PAGE_SIZE
							}, self.loadPluData, self.fetchError);
							break;
						}
						case FORMAT_CODE: {
							if ($scope.currentCode.labelFormatSelected == 1) {
								scaleManagementApi.queryforUpcsByFormatCodeOne({
										formatCode: $scope.currentCode.formatCode,
										includeCounts: includeCounts,
										page: page,
										pageSize: PAGE_SIZE
									}, self.loadPluData, self.fetchError
								);
							}
							else {
								scaleManagementApi.queryforUpcsByFormatCodeTwo({
										formatCode: $scope.currentCode.formatCode,
										includeCounts: includeCounts,
										page: page,
										pageSize: PAGE_SIZE
									}, self.loadPluData, self.fetchError
								);
							}
							break;
						}
						case GRAPHICS_CODE: {
							scaleManagementApi.queryScaleUpcByScaleCode({
									graphicsCode: $scope.currentCode.scaleGraphicsCode,
									includeCounts: includeCounts,
									page: page,
									pageSize: PAGE_SIZE
								}, self.loadPluData, self.fetchError
							);
							break;
						}
						case INGREDIENT_STATEMENT: {
							scaleManagementApi.queryforUpcsByIngredientStatementNumber({
								ingredientStatement: $scope.currentCode.statementNumber,
								includeCounts: includeCounts,
								page: page,
								pageSize: PAGE_SIZE
							}, self.loadPluData, self.fetchError
							);
							break;
						}
					}
				};

				/**
				 * Callback for a successful call to get PLU data from the backend.
				 *
				 * @param results The data returned by the backend.
				 */
				self.loadPluData = function (results) {
					self.isWaitingForPlus = false;
					// If this was the first page, it includes record count and total pages.
					if (results.complete) {
						totalPluRecordCount = results.recordCount;
						pluDataResolvingParams.total(totalPluRecordCount);
					}
					if (results.data.length === 0) {
						self.error = "No records found.";
						self.data = null;
					} else {
						self.error = null;
						self.data = results.data;
						self.pluResultMessage = getPluResultMessage(results.data.length, results.page);
						pluDefer.resolve(results.data);
					}
					// Return the data back to the ngTable.
				};

				self.pluTableParams = buildPluTable();

				/**
				 * Callback for when the backend returns an error.
				 *
				 * @param results The error from the backend.
				 */
				self.fetchError = function (results) {
					self.data = null;
					self.isWaitingForPlus = false;
					self.error = results.data.message;
				};

				/**
				 * Generates the message that shows how many plu records and pages there are and what page the user is currently
				 * on.
				 *
				 * @param dataLength The number of records there are.
				 * @param currentPage The current page showing.
				 * @returns {string} The message.
				 */
				var getPluResultMessage = function(dataLength, currentPage){
					return $scope.currentCode.displayName + " Result " + (PAGE_SIZE * currentPage + FIRST_ELEMENT) +
						" - " + (PAGE_SIZE * currentPage + dataLength) + " of " +
						totalPluRecordCount.toLocaleString();
				};

				/**
				 * Reload the ng-table when user selects different code
				 */
				$scope.$watch('currentCode', function (newValue, oldValue) {
					if (newValue != undefined) {
						firstPluSearch = true;
						self.pluTableParams.page(FIRST_PAGE);
						self.pluTableParams.reload();
					}
				});

				/**
				 * Selects the appropriate PLU search by the code type.
				 */
				self.exportPLUs = function(){
					self.downloading = true;

					var exportUrl = urlBase + "/pm/scaleManagement/exportToCSV?requestedCode=";
					var fileName = "";

					switch ($scope.codeType) {
						case ACTION_CODE: {
							exportUrl += $scope.currentCode.actionCode + "&requestedCodeType=" +  $scope.codeType;
							fileName = $scope.codeType + "-" + $scope.currentCode.actionCode + ".csv";
							break;
						}
						case FORMAT_CODE: {
							// sending the label format selected to back to specify what type of label format
							// $scope.currentCode.labelFormatSelected should be a 1 or 2
							exportUrl += $scope.currentCode.formatCode + "&requestedCodeType=" +
								$scope.currentCode.labelFormatSelected;
							fileName = $scope.codeType + "-" + $scope.currentCode.formatCode + ".csv";
							break;
						}
						case GRAPHICS_CODE: {
							exportUrl += $scope.currentCode.scaleGraphicsCode + "&requestedCodeType=" + $scope.codeType;
							fileName = $scope.codeType + "-" + $scope.currentCode.scaleGraphicsCode + ".csv";
							break;
						}
					}

					downloadService.export(exportUrl, fileName, self.WAIT_TIME,
						function() {
							self.downloading = false;
						});
				};
			}
		}
	}
})();
