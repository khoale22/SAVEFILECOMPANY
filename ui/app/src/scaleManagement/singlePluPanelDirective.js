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
 * Directive to support the single PLU panel view
 *
 * @author s75601
 * @since 2.8.0
 */
'use strict';

(function() {
		var app = angular.module('productMaintenanceUiApp');
		app.directive('singlePluPanel', singlePluPanel);
		singlePluPanel.$inject = ['singlePluPanelService', 'ScaleManagementApi', 'urlBase', 'DownloadService', '$state', 'appConstants', '$filter'];

		function singlePluPanel(singlePluPanelService, scaleManagementApi, urlBase, downloadService, $state, appConstants, $filter) {
			return {
				restrict: 'E',
				templateUrl: 'src/scaleManagement/singlePluPanel.html',
				scope: {},
				controllerAs: 'singlePluPanelDirective',
				controller: function ($scope) {

					const NOT_SINGLE_VIEW = "notSingleView";
					const SINGLE_VIEW = "singleView";

					var self = this;

					/**
					 * The original State of the ScaleUpc before editing
					 * @type {null}
					 */
					self.originalScaleUpc = null;
					/**
					 * The currently selected UPC object that is a PLU
					 * @type {{}}
					 */
					self.selectedScaleUpc= {};
					/**
					 * Tracks whether or not the user is waiting for a download.
					 * @type {boolean}
					 */
					self.downloading=false;

					/**
					 * Controller data.
					 * @type {Array}
					 */
					self.data = [];
					/**
					 * Max time to wait for excel download.
					 * @type {number}
					 */
					self.WAIT_TIME = 1200;
					/**
					 * Whether or not effective date picker is visible.
					 * @type {boolean}
					 */
					self.effectiveDatePickerOpened = false;
					/**
					 * Holds a copy of the date in string form before it is converted for the date picker
					 * @type {string}
					 */
					self.currentPluDateString="";
					/**
					 * Index of the currently selectedScaleUpc that is in the view
					 * @type {null}
					 */
					self.selectedScaleUpcIndex=null;
					/**
					 * The current error message.
					 * @type {String}
					 */
					self.error = null;
					/**
					 * The current success message.
					 * @type {String}
					 */
					self.success= null;
					/**
					 * Whether or not the ingredient statement is one of the fake ones.
					 * @type {boolean}
					 */
					self.isIngredientStatementFake = false;
					/**
					 * True if the nutrient statement is there and is not one of the fake ones.
					 * @type {boolean}
					 */
					self.isNutrientStatementReal = false;

					/**
					 * True if the nutrient statement one of the fake ones.
					 * @type {boolean}
					 */
					self.isNutrientStatementFake = false;

					/**
					 * Whether or not page is waiting for data.
					 * @type {boolean}
					 */
					self.isWaiting = false;

					/**
					 * Initialize the controller.
					 */
					self.init = function(){
						singlePluPanelService.setCurrentView(SINGLE_VIEW);
						self.data=singlePluPanelService.getData();
						self.getSingleView(singlePluPanelService.getSelectedScaleUpc(), singlePluPanelService.getSelectedScaleUpcIndex())
					};

					/**
					 * Set the value as a date so date picker understands how to represent value.
					 */
					self.setDateForDatePicker = function(){
						self.effectiveDatePickerOpened = false;
						if(self.selectedScaleUpc.effectiveDate != null) {
							self.selectedScaleUpc.effectiveDate = self.selectedScaleUpc.effectiveDate.replace(/-/g, '\/');
							self.selectedScaleUpc.effectiveDate = new Date(self.selectedScaleUpc.effectiveDate);
						}
						self.originalScaleUpc = angular.copy(self.selectedScaleUpc);
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
					 * Change the selected scale upc.
					 *
					 * @param scaleUpc Scale upc to change to.
					 */
					self.getSingleView = function(scaleUpc, index){
						self.error=null;
						self.success=null;
						self.selectedScaleUpc = scaleUpc;
						self.selectedScaleUpcIndex = index;
						self.currentPluDateString = self.selectedScaleUpc.effectiveDate;
						self.updateIngredientStatement();
						self.updateNutrientStatement();
						self.setDateForDatePicker();
					};

					/**
					 * Closes single view.
					 */
					self.closeSingleView = function(){
						self.reset();
						if(self.selectedScaleUpc.effectiveDate != null) {
							self.selectedScaleUpc.effectiveDate = self.convertDateToStringWithYYYYMMDD(self.selectedScaleUpc.effectiveDate);
						}
						self.data[self.selectedScaleUpcIndex] = angular.copy(self.selectedScaleUpc);
						singlePluPanelService.setCurrentView(NOT_SINGLE_VIEW);
					};

					/**
					 * Convert the date to string with format: yyyy-MM-dd.
					 * @param date the date object.
					 * @returns {*} string
					 */
					self.convertDateToStringWithYYYYMMDD = function (date) {
						return $filter('date')(date, 'yyyy-MM-dd');
					};

					/**
					 * Editing is being cancelled.
					 *
					 * @param index Index of data to be refreshed.
					 */
					self.reset = function() {
						self.error=null;
						self.success=null;
						self.selectedScaleUpc = angular.copy(self.originalScaleUpc);
					};

					/**
					 * Updates the current selected scale upc.
					 */
					self.update = function() {
						self.error=null;
						self.success=null;
						var difference = angular.toJson(self.selectedScaleUpc) != angular.toJson(self.originalScaleUpc);
						if(difference){
							self.pluChanged = true;
							var scaleUpc = angular.copy(self.selectedScaleUpc);
							scaleUpc.effectiveDate = self.convertDate(scaleUpc.effectiveDate);
							scaleManagementApi.updateScaleUpc(scaleUpc, self.loadData, self.fetchError);
						} else {
							self.error = "No difference detected";
						}
					};

					/**
					 * Initiates a download of all the records.
					 */
					self.export = function() {
						self.downloading = true;
						downloadService.export(self.generateExportUrl(), 'pluScaleMaintenance.csv', self.WAIT_TIME,
							function() { self.downloading = false; });
					};

					/**
					 * Generates the URL to ask for the export.
					 * @returns {string} The URL to ask for the export.
					 */
					self.generateExportUrl = function() {
						return urlBase + '/pm/scaleManagement/exportPlusToCsv?plus=' + encodeURI(self.selectedScaleUpc.plu) +
								"&totalPages=" + 1;
					};

					/**
					 * Converts a date to a readable format (2014-03-16)
					 * @param date Date to be formatted.
					 * @returns {string}
					 */
					self.convertDate=function (date) {
						if(date != null) {
							var d = new Date(date);
							var month = '' + (d.getMonth() + 1);
							var day = '' + d.getDate();
							var year = d.getFullYear();

							// If the month is 1-9, format has to be 0#.
							if (month.length < 2) {
								month = '0' + month;
							}

							// If the date is 1-9, format has to be 0#.
							if (day.length < 2) {
								day = '0' + day;
							}
							return [year, month, day].join('-');
						} else {
							return null;
						}
					};

					self.loadData = function(results) {
						self.isWaiting = false;
						self.success=results.message;
						self.data[self.selectedScaleUpcIndex] = angular.copy(results.data);
						self.selectedScaleUpc=angular.copy(results.data);
						self.setDateForDatePicker();
					};

					/**
					 * Calls API method to get data based on nutrient statement.
					 */
					self.getNutrientsByNutrientStatement = function(){
						self.isModalWaiting = true;
						self.loadedStatement = null;
						scaleManagementApi.queryForNutrientStatementByStatementId({
								nutrientStatementId:  self.selectedScaleUpc.nutrientStatement,
								page: 0,
								pageSize: 1,
								includeCounts: false
							},
							self.loadStatementData,
							self.fetchError);
					};

					/**
					 * Calls API method to get data based on ingredient statement.
					 */
					self.getIngredientsByIngredientStatement = function() {
						if (self.isIngredientStatementFake) {
							self.isIngredientFound = false;
							self.setError("No ingredients found.");
						} else {
							self.isIngredientFound = true;
							self.isModalWaiting = true;
							self.loadedStatement = null;
							scaleManagementApi.queryIngredientStatementByStatementCode({
									ingredientStatements: self.selectedScaleUpc.ingredientStatement,
									includeCounts: false,
									page: 0,
									pageSize: 1
								},
								self.loadStatementData,
								self.fetchError);
						}
					};

					/**
					 * Loads nutrient Statement Data.
					 * @param results
					 */
					self.loadStatementData = function (results) {
						self.isModalWaiting = false;
						if (results.data.length == 0) {
							self.error = "Nutrient statement not found";
						} else {
							// Only one record will come back
							self.loadedStatement = results.data[0];
						}
					};

					/**
					 * Clears the data when statement modal is closed.
					 */
					self.closeModal = function() {
						self.loadedStatement = null;
						self.isModalWaiting = false;
					};

					/**
					 * Navigate to Ingredient Statement Page and search by Ingredient Statement Number.
					 */
					self.navigateToIngredientStatements = function (ingStmtCode) {
						self.reset();
						$state.go(appConstants.SCALE_MANAGEMENT_INGREDIENT_STATEMENTS,{ingStmtCode:ingStmtCode});
					};

					/**
					 * Navigate to Nutrient Statement Page and search by Nutrient Statement Number.
					 */
					self.navigateToNutrientStatement = function (ntrStmtCode) {
						self.reset();
						$state.go(appConstants.SCALE_MANAGEMENT_NUTRIENT_STATEMENT,{ntrStmtCode:ntrStmtCode});
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
					 * Callback for when the backend returns an error.
					 *
					 * @param error The error from the backend.
					 */
					self.fetchError = function(error) {
						self.isWaiting = false;
						self.isModalWaiting = false;
						self.data = null;
						if (error && error.data) {
							if(error.data.message) {
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
					 * Called when the nutrient statement is updated.
					 */
					self.updateNutrientStatement = function(){
						self.isNutrientStatementReal =
							self.selectedScaleUpc.nutrientStatement == self.selectedScaleUpc.plu;

						self.isNutrientStatementFake =
							(self.selectedScaleUpc.nutrientStatement == 9999999 ||
							self.selectedScaleUpc.nutrientStatement == 0);
					};

					/**
					 * Returns whether or not the nutrient statement is a valid value, basically, one of the
					 * fake ones or equal to the PLU.
					 *
					 * @returns {boolean}
					 */
					self.isNutrientStatementValid = function() {

						return self.isNutrientStatementFake || self.isNutrientStatementReal;
					};


					/**
					 * Called when the ingredient statement is updated.
					 */
					self.updateIngredientStatement = function(){
						self.isIngredientStatementFake =
							self.selectedScaleUpc.ingredientStatement == '9999' ||
							self.selectedScaleUpc.ingredientStatement == '0';
					};

					/**
					 * Maintenance the current selected scale upc.
					 */
					self.sendScaleMaintenance = function() {
						self.error=null;
						self.success=null;
						var scaleUpc = angular.copy(self.selectedScaleUpc);
						scaleUpc.effectiveDate = self.convertDate(scaleUpc.effectiveDate);
						self.isWaiting = true;
						scaleManagementApi.sendScaleMaintenance(scaleUpc, self.loadData, self.fetchError);
					};
				}
			}
		}
	}
)();


