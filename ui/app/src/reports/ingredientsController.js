/*
 * ingredientsController.js
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the ingredients report.
 *
 * @author d116773
 * @since 2.0.7
 */
(function(){
	angular.module('productMaintenanceUiApp').controller('IngredientsReportController', ingredientsReportController);

	ingredientsReportController.$inject = ['ReportsApi', 'ngTableParams', 'urlBase', 'DownloadService'];

	/**
	 * Creates the controller for the ingredients report.
	 */
	function ingredientsReportController(reportsApi, ngTableParams, urlBase, downloadService) {

		var self = this;

		self.PAGE_SIZE = 100;
		self.WAIT_TIME = 120;

		/**
		 * The ingredients in the search box.
		 *
		 * @type {string}
		 */
		self.ingredients = null;
		/**
		 * The ingredients the user has searched on. If the user has clicked search and then
		 * changed the search box, this will save what was there before the change.
		 *
		 * @type {string}
		 */
		self.searchedIngredients = null;

		// State related
		/**
		 * Tracks whether or not he search pane is visible.
		 *
		 * @type {boolean}
		 */
		self.searchPanelVisible = true;
		/**
		 * Tracks whether or not the user is waiting for data for the report.
		 *
		 * @type {boolean}
		 */
		self.isWaiting = false;
		/**
		 * Tracks whether or not the user is waiting for a download.
		 *
		 * @type {boolean}
		 */
		self.downloading = false;

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

		// State of the data.
		/**
		 * The current page the user is looking at.
		 *
		 * @type {number}
		 */
		self.currentPage = 0;
		/**
		 * The number of the first record being shown in the full data set.
		 * @type {number}
		 */
		self.startRecord = 0;
		/**
		 * The number of records on the report.
		 *
		 * @type {number}
		 */
		self.recordsVisible = 0;
		/**
		 * The total number of records available in the report.
		 *
		 * @type {number}
		 */
		self.totalRecords = 0;

		// Stuff related to the ng-table
		/**
		 * The table parameters.
		 *
		 * @type {null}
		 */
		self.tableParams = null;
		/**
		 * The parameters passed to the application from ng-tables getData method. These need to be stored
		 * until all the data are fetched from the backend.
		 *
		 * @type {null}
		 */
		self.dataResolvingParams = null;
		/**
		 * The promise given to the ng-tables getData method. It shouod be called after data are fetched.
		 *
		 * @type {null}
		 */
		self.defer = null;

		/**
		 * Initializes the controller.
		 */
		self.init = function() {
			self.buildTable();
		};

		/**
		 * Initiates a search (the user clicked on the search button).
		 */
		self.doSearch = function() {
			self.searchedIngredients = self.ingredients;
			self.firstSearch = true;
			self.tableParams.reload();
		};

		/**
		 * Callback for the request for data from the backend for the report.
		 *
		 * @param results The data returned for the report.
		 */
		self.loadData = function(results) {
			self.isWaiting = false;
			self.data = results.data;

			if (results.complete) {
				self.totalPages = results.pageCount;
				self.totalRecords = results.recordCount;
				self.dataResolvingParams.total(self.totalRecords);
			}

			self.startRecord = results.page * self.PAGE_SIZE;
			self.recordsVisible = results.data.length;
			self.defer.resolve(results.data);
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param results The error from the backend.
		 */
		self.fetchError = function(results) {
			self.isWaiting = false;
			self.error = results.data.message;
		};

		/**
		 * Initiates a call to get report data from the backend.
		 *
		 * @param includeCounts Whether or not to ask for total page and record counts.
		 * @param page The page of data to ask for.
		 */
		self.fetchData = function(includeCounts, page) {
			reportsApi.getProductsByIngredient({
				ingredient: self.searchedIngredients,
				includeCounts: includeCounts,
				page: page,
				pageSize: self.PAGE_SIZE
			}, self.loadData, self.fetchError)
		};

		/**
		 * Constructs the ng-table.
		 */
		self.buildTable = function() {
			self.tableParams = new ngTableParams(
				{
					page: 1,
					count: self.PAGE_SIZE
				}, {
					counts: [],
					getData: function($defer, params) {

						if (self.searchedIngredients == null) {
							return;
						}

						self.recordsVisible = 0;
						self.isWaiting = true;
						self.data = null;

						self.defer = $defer;
						self.dataResolvingParams = params;

						var includeCounts = false;

						if (self.firstSearch) {
							includeCounts = true;
							params.page(1);
							self.firstSearch = false;
							self.startRecord = 0;
						}

						self.fetchData(includeCounts, params.page() - 1);
					}

				}
			)
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {

			self.downloading = true;

			downloadService.export(self.generateExportUrl(), 'ingredients.csv', self.WAIT_TIME,
				function() { self.downloading = false; });
		};

		/**
		 * Generates the URL to ask for the export.
		 *
		 * @returns {string} The URL to ask for the export.
		 */
		self.generateExportUrl = function() {

			return urlBase + '/pm/reports/ingredientsCsv?ingredient=' +
				encodeURI(self.searchedIngredients) +
				"&recordCount=" + self.totalRecords;
		};
	}
})();
