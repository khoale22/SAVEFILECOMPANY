/*
 *   productBrandComponent.js
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
	angular.module('productMaintenanceUiApp').component('productBrand', {
		scope: '=',
		// Inline template which is bind to message variable in the component controller
		templateUrl: 'src/codeTable/productBrand/productBrand.html',
		// The controller that handles our component logic
		controller: ProductBrandController
	});

	ProductBrandController.$inject = ['$rootScope', '$scope', 'ProductBrandApi', 'ngTableParams'];

	/**
	 * Product Brand component's controller definition.
	 *
	 * @param $scope scope of the case pack info component.
	 * @param productBrandApi the api of product brands.
	 * @param ngTableParams the table display product brands.
	 * @constructor
	 */
	function ProductBrandController($rootScope, $scope, productBrandApi, ngTableParams) {
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
		 * Flag for waiting response from back end.
		 *
		 * @type {boolean}
		 */
		self.isWaitingForResponse = false;

		/**
		 * Check box for show own brand only.
		 *
		 * @type {boolean}
		 */
		self.ownBrand = false;

		/**
		 * The list of product brands information.
		 *
		 * @type {Array}
		 */
		self.productBrands = [];

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
		 *  Holds the selected brand when user select on brand select box.
		 *
		 * @type {String}.
		 */
		self.productBrand = '';

		/**
		 * Holds the selected brand tier when user select on brand tier select box.
		 *
		 * @type {String}
		 */
		self.brandTier = '';

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
		 * Constructs the table that shows the product brands.
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

						// Calls to the backend to get the data.
						if (!self.productBrand && !self.brandTier) {
							self.findAllProductBrands(params.page() - 1);
						} else {
							self.filterProductBrands(params.page() - 1);
						}
					}
				}
			);
		};

		/**
		 * Find all the list of product brands information.
		 *
		 * @param page the page number.
		 */
		self.findAllProductBrands = function (page) {
			productBrandApi.findAllProductBrands({
					page: page,
					pageSize: self.PAGE_SIZE,
					ownBrand: self.ownBrand,
					includeCount: self.includeCount
				},
				self.productBrandResponseSuccess,
				self.fetchError);
		};

		/**
		 * Filter the list of product brands by parameters.
		 *
		 * @param page the page number.
		 */
		self.filterProductBrands = function (page) {
			productBrandApi.filterProductBrands({
					page: page,
					pageSize: self.PAGE_SIZE,
					productBrand: self.productBrand,
					brandTier: self.brandTier,
					ownBrand: self.ownBrand,
					includeCount: self.includeCount
				},
				self.productBrandResponseSuccess,
				self.fetchError);
		};

		/**
		 * Handle event when click show own brand only checkbox, or filter by product brand, or brand tier.
		 */
		self.refreshProductBrandTable = function () {
			self.firstSearch = true;
			self.tableParams = self.buildTable();
		};

		/**
		 * Clear search criteria and show own brand only checkbox.
		 */
		self.doClearFilter = function () {
			if (self.isDataFiltered()) {
				self.productBrand = self.NO_FILTER_BY_PARAMETER;
				self.brandTier = self.NO_FILTER_BY_PARAMETER;
				self.ownBrand = false;
				self.refreshProductBrandTable();
			}
		};

		/**
		 * Check whether input data filter or not.
		 *
		 * @returns {boolean}
		 */
		self.isDataFiltered = function () {
			return self.productBrand || self.brandTier || self.ownBrand;
		};

		/**
		 * Load product brand data response success.
		 *
		 * @param results the results to load.
		 */
		self.productBrandResponseSuccess = function (results) {
			self.isWaitingForResponse = false;
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.dataResolvingParams.data = [];
				self.productBrands = [];
				self.error = self.NO_RECORDS_FOUND;
			} else {
				self.error = null;
				self.productBrands = results.data;
				self.defer.resolve(results.data);
			}
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
