/*
 *   productSubBrandComponent.js
 *
 *   Copyright (c) 2017 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *   of HEB.
 */
'use strict';

/**
 * Code Table -> product brand option page component.
 *
 * @author vn00602
 * @since 2.12.0
 */
(function () {
	angular.module('productMaintenanceUiApp').component('productSubBrand', {
		scope: '=',
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/codeTable/productSubBrand/productSubBrand.html',
		// The controller that handles our component logic
		controller: ProductSubBrandController
	});

	ProductSubBrandController.$inject = ['$rootScope', '$scope', 'ProductSubBrandApi', 'ngTableParams', 'urlBase', 'DownloadService'];

	/**
	 * Product Sub Brand component's controller definition.
	 *
	 * @param $scope scope of the case pack info component.
	 * @param productSubBrandApi the api of product brands.
	 * @param ngTableParams the table display product brands.
	 * @constructor
	 */
	function ProductSubBrandController($rootScope, $scope, productSubBrandApi, ngTableParams, urlBase, downloadService) {
		/** All CRUD operation controls of choice option page goes here */
		var self = this;

		/**
		 * The default error message.
		 *
		 * @type {string}
		 */
		self.UNKNOWN_ERROR = "An unknown error occurred.";

		/**
		 * The default no records found message.
		 *
		 * @type {string}
		 */
		self.NO_RECORDS_FOUND = "No records found.";

		/**
		 * The default page number.
		 *
		 * @type {number}
		 */
		self.PAGE = 1;

		/**
		 * The default page size.
		 *
		 * @type {number}
		 */
		self.PAGE_SIZE = 20;

		/**
		 * The param to indicate not filter.
		 * @type {number}
		 */
		self.NO_FILTER_BY_PARAMETER = '';

		/**
		 * Max time to wait for export file.
		 *
		 * @type {number}
		 */
		self.WAIT_TIME = 1200;

		/**
		 * Flag for waiting response from back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * The flag check whether downloading or not.
		 *
		 * @type {boolean}
		 */
		self.isDownloading = false;

		/**
		 * The list of product sub brands information.
		 *
		 * @type {Array}
		 */
		self.productSubBrands = [];

		/**
		 * The ngTable object that will be waiting for data while the report is being refreshed.
		 *
		 * @type {object}
		 */
		self.defer = null;

		/**
		 * The parameters passed from the ngTable when it is asking for data.
		 *
		 * @type {object}
		 */
		self.dataResolvingParams = null;

		/**
		 * The total number of pages in the report.
		 *
		 * @type {null}
		 */
		self.totalPages = null;

		/**
		 * The total records in the report.
		 *
		 * @type {null}
		 */
		self.totalRecordCount = null;

		/**
		 *  Holds the product sub brand id when user type on product sub brand id text box.
		 *
		 * @type {String}
		 */
		self.prodSubBrandId = '';

		/**
		 * Holds the selected product sub brand name when user type on product sub brand name text box.
		 *
		 * @type {String}
		 */
		self.prodSubBrandName = '';

		/**
		 * Check if it is the first time to filter or not.
		 *
		 * @type {boolean}
		 */
		self.firstSearch = true;

		/**
		 * Check if it is search with count or not.
		 *
		 * @type {boolean}
		 */
		self.includeCount = false;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.isWaitingForResponse = true;
			self.firstSearch = true;
			self.tableParams = self.buildTable();
			if($rootScope.isEditedOnPreviousTab){
				self.error = $rootScope.error;
				self.success = $rootScope.success;
				$rootScope.isEditedOnPreviousTab = false;
			}
		};

		/**
		 * Constructs the table that shows the product sub brands.
		 */
		self.buildTable = function () {
			return new ngTableParams(
				{
					page: self.PAGE,
					count: self.PAGE_SIZE
				}, {
					counts: [],

					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {

						self.isWaitingForResponse = true;
						// Save off these parameters as they are needed by the callback when data comes back from
						// the back-end.
						self.defer = $defer;
						self.dataResolvingParams = params;

						// If it is first time to search, then it search with count, otherwise it doesn't include count.
						if (self.firstSearch) {
							self.includeCount = true;
							self.firstSearch = false;
						} else {
							self.includeCount = false;
						}

						// Issue calls to the backend to get the data.
						self.getProductSubBrandsPage(params.page() - 1);
					}
				}
			);
		};

		/**
		 * Find all the list of product sub brands information.
		 *
		 * @param page the page number.
		 */
		self.getProductSubBrandsPage = function (page) {
			productSubBrandApi.getProductSubBrandsPage({
					page: page,
					pageSize: self.PAGE_SIZE,
					prodSubBrandId: self.prodSubBrandId,
					prodSubBrandName: self.prodSubBrandName,
					includeCount: self.includeCount
				},
				self.productSubBrandResponseSuccess,
				self.fetchError);
		};

		/**
		 * Handle event when click show own brand only checkbox, or filter by product sub brand id or name.
		 */
		self.refreshProductSubBrandTable = function () {
			self.firstSearch = true;
			self.tableParams = self.buildTable();
		};

		/**
		 * Clear search criteria.
		 */
		self.doClearFilter = function () {
			if (self.isDataFiltered()) {
				self.prodSubBrandId = self.NO_FILTER_BY_PARAMETER;
				self.prodSubBrandName = self.NO_FILTER_BY_PARAMETER;
				self.refreshProductSubBrandTable();
			}
		};

		/**
		 * Check whether input data filter or not.
		 *
		 * @returns {boolean}
		 */
		self.isDataFiltered = function () {
			return self.prodSubBrandId || self.prodSubBrandName;
		};

		/**
		 * Export product sub brand to csv file.
		 */
		self.exportProductSubBrand = function () {
			if (!self.totalRecordCount) return;
			self.isDownloading = true;

			downloadService.export(self.generateExportUrl(), self.createExportFileName(), self.WAIT_TIME, self.exportResponse);
		};

		/**
		 * Generate the export url.
		 *
		 * @type {string}
		 */
		self.generateExportUrl = function () {
			var exportUrl = urlBase + '/pm/codeTable/productSubBrand/exportSubBrandToCSV?';
			exportUrl += 'prodSubBrandId=' + self.prodSubBrandId;
			exportUrl += '&prodSubBrandName=' + self.prodSubBrandName;
			exportUrl += '&totalPages=' + self.totalPages;
			return exportUrl;
		};

		/**
		 * Generate the file name to export.
		 *
		 * @returns {string}
		 */
		self.createExportFileName = function () {
			var fileName = 'Sub-Brand_';
			var d = new Date();
			fileName += d.getFullYear();
			fileName += ('0' + (d.getMonth() + 1)).slice(-2);
			fileName += ('0' + d.getDate()).slice(-2);
			fileName += ('0' + d.getHours()).slice(-2);
			fileName += ('0' + d.getMinutes()).slice(-2);
			fileName += ('0' + d.getSeconds()).slice(-2);
			fileName += '.csv';
			return fileName;
		};

		/**
		 * Load product sub brand data response success.
		 *
		 * @param results the results to load.
		 */
		self.productSubBrandResponseSuccess = function (results) {
			self.isWaitingForResponse = false;
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.dataResolvingParams.data = [];
				self.productSubBrands = [];
				self.error = self.NO_RECORDS_FOUND;
			} else {
				self.error = null;
				self.productSubBrands = results.data;
				self.defer.resolve(results.data);
			}
		};

		/**
		 * Handle when export to csv has error.
		 */
		self.exportResponse = function () {
			self.isDownloading = false;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the back end.
		 */
		self.fetchError = function (error) {
			self.isWaitingForResponse = false;
			self.success = null;
			self.error = self.getErrorMessage(error);
		};

		/**
		 * Returns error message.
		 *
		 * @param error
		 * @returns {string}
		 */
		self.getErrorMessage = function (error) {
			if (error && error.data) {
				if (error.data.message) {
					return error.data.message;
				} else {
					return error.data.error;
				}
			} else {
				return self.UNKNOWN_ERROR;
			}
		};
	}
})();
