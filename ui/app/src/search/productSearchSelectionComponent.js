/*
 * productSearchSelectionComponent.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */


'use strict';

(function() {
	angular.module('productMaintenanceUiApp').component('productSearchSelection', {

		require: {

		},
		bindings: {
			searchCriteria: '<',
			onUpdate: '&'
		},

		templateUrl: 'src/search/productSearchSelection.html',

		controller: ProductSearchSelectionController
	});

	ProductSearchSelectionController.$inject = ['HomeApi', 'ngTableParams'];

	function ProductSearchSelectionController(homeApi, ngTableParams) {

		var self = this;

		self.selectedProducts = [];

		self.error = null;

		/**
		 * This flag states if all the rows in the related products table has been selected
		 * @type {boolean}
		 */
		self.allProductsChecked=false;

		/**
		 * Whether or not the controller is waiting for data.
		 * @type {boolean}
		 */
		self.isWaiting = false;

		/**
		 * Whether or not user searched by productId
		 * @type {boolean}
		 */
		self.searchedByProductId = false;

		/**
		 * Whether or this is the first search done on a query.
		 * @type {boolean} True if it has and false otherwise.
		 */
		self.firstSearch = true;

		/**
		 * The parameters that define the table showing the report.
		 * @type {ngTableParams}
		 */
		self.tableParams = null;

		/**
		 * The total number of records in the report.
		 * @type {int}
		 */
		self.totalRecordCount = null;

		/**
		 * The total number of pages in the report.
		 * @type {null}
		 */
		self.totalPages = null;

		/**
		 * The number of records to show on the report.
		 * --lowered from 100 to speed up search (m314029)
		 * @type {number}
		 */
		self.PAGE_SIZE = 10;

		/**
		 * If the product details page is being viewed.
		 * @type {boolean}
		 */
		self.viewingProductDetails = false;

		/**
		 * Whether or not the user is doing an MRT search.
		 * @type {boolean}
		 */
		self.mrtSearch = false;
		/**
		 * Holds the list of exclude productids.
		 * @type {null}
		 */
		self.excludedProducts = [];
		/**
		 * Initialize the controller.
		 */
		self.$onInit = function(){
			//self.buildTable();
		};

		self.$onChanges = function() {
			self.firstSearch = true;
			self.buildTable();
		};

		/**
		 * Constructs the table that shows the report.
		 */
		self.buildTable = function()
		{
			self.tableParams = new ngTableParams(
				{
					// set defaults for ng-table
					page: 1,
					count: self.PAGE_SIZE,
					sorting: {PRODUCT_ID: "asc"}
				}, {

					// hide page size
					counts: [],

					defaultSort: 'asc',

					/**
					 * Called by ngTable to load data.
					 *
					 * @param $defer The object that will be waiting for data.
					 * @param params The parameters from the table helping the function determine what data to get.
					 */
					getData: function ($defer, params) {

						self.isWaiting = true;
						self.viewingProductDetails = false;
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
							params.page(1);
							self.firstSearch = false;
						} else {
							self.includeCounts = false;
						}

						self.doComplexSearch(params.page() - 1);

					}
				}
			);
		};

		/**
		 * Callback for when the user uses the complex search function.
		 */
		self.doComplexSearch = function(page) {
			self.searchCriteria.useSession = false;
			var search = self.searchCriteria;
			search.page = page;
			search.pageSize = self.PAGE_SIZE;
			search.firstSearch = self.includeCounts;
			homeApi.search(search, self.loadData, self.fetchError);
		};

		// *****************************************************************************
		// Callbacks
		// *****************************************************************************

		/**
		 * Callback for a successful call to get data from the backend. It requires the class level defer
		 * and dataResolvingParams object is set.
		 *
		 * @param results The data returned by the backend.
		 */
		self.loadData = function(results){
			self.error = null;

			self.isWaiting = false;

			// If this was the fist page, it includes record count and total pages.
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.totalPages = results.pageCount;
			}

			if (results.data.length === 0) {
				self.error = "No records found.";
			} else {
				self.data = results.data;

				self.resultMessage = self.getResultMessage(results.data.length, results.page);

				// Loop through the data and set the selected in case the user is moving between pages for mass update
				for (var i = 0; i < self.data.length; i++) {
					if (self.allProductsChecked) {
						self.data[i].isChecked = true;
						self.deSelectProduct(self.data[i]);
					}
				}

				// Return the data back to the ngTable.
				self.dataResolvingParams.total(self.totalRecordCount);
				self.defer.resolve(results.data);
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
		 * Generates the message that shows how many records and pages there are and what page the user is currently
		 * on.
		 *
		 * @param dataLength The number of records there are.
		 * @param currentPage The current page showing.
		 * @returns {string} The message.
		 */
		self.getResultMessage = function(dataLength, currentPage){
			return "Result " + (self.PAGE_SIZE * currentPage + 1) +
				" - " + (self.PAGE_SIZE * currentPage + dataLength) + " of " + self.totalRecordCount;
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
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		self.fetchError = function(error) {
			self.isWaiting = false;
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

		self.selectProducts = function() {
			for (var i = 0; i < self.data.length; i++) {
				if (self.data[i].isChecked === true) {
					self.selectedProducts.push(self.data[i]);
				}
			}

			self.searchSelectionResultsVisible = false;
			self.data = [];
			self.searchCriteria.excludedProducts = self.excludedProducts;
			// call parent component with selected products
			self.onUpdate({selectedProducts: self.selectedProducts});
		};

		self.toggleAllCheckedProducts = function () {
			self.excludedProducts = [];
			if (self.allProductsChecked === true) {
				self.checkAllProductsRows();
			} else if (self.allProductsChecked === false) {
				self.uncheckAllProductsRows();
			}
			self.searchCriteria.isSelectAll = self.allProductsChecked;
		};

		/**
		 * This method will check all rows in products table
		 */
		self.checkAllProductsRows= function () {
			self.allProductsChecked = true;

			if(self.data !== null){
				for(var index=0; index<self.data.length; index++){
					self.data[index].isChecked = true;
				}
			}
		};

		/**
		 * This method will uncheck all rows in products table
		 */
		self.uncheckAllProductsRows= function () {
			self.allProductsChecked = false;

			if(self.data !== null){
				for(var index=0; index<self.data.length; index++){
					self.data[index].isChecked = false;
				}
			}
			self.selectedProducts = [];
		};

		self.atLeastOneProductChecked = function() {
			if (self.allProductsChecked === true) {
				return true;
			}

			if (typeof self.data === "undefined" || self.data === null) {
				return false;
			}

			for(var index=0; index<self.data.length; index++){
				if (self.data[index].isChecked === true) {
					return true;
				}
			}

			return false;
		}
		/**
		 * Toggle check on product.
		 * @param product the product object.
		 */
		self.toggleCheckedProduct = function(product){
			if(self.excludedProducts == null || self.excludedProducts == undefined){
				self.excludedProducts = [];
			}
			if (self.allProductsChecked) {//checking if "Select All" is checked?
				!product.isChecked ? self.excludedProducts.push(product.prodId)
					: _.remove(self.excludedProducts, function (prodId) {
						return prodId == product.prodId;
					});
			}else{
				if(!product.isChecked && self.selectedProducts !=undefined && self.selectedProducts != null){
					// Deselect product.
					_.remove(self.selectedProducts, function (prod) {
						return prod.prodId == product.prodId;
					});
				}
			}
		}
		/**
		 * Deselect the product that it is existing in exclude products.
		 * @param product the product object.
		 */
		self.deSelectProduct = function(product){
			if(self.excludedProducts == null || self.excludedProducts == undefined){
				return;
			}
			if(self.excludedProducts.indexOf(product.prodId) !=-1){
				product.isChecked = false;
			}
		}
	}
})();
