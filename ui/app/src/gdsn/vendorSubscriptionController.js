/*
 * vendorSubscriptionController.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

'use strict';

(function() {

	angular.module('productMaintenanceUiApp').controller('VendorSubscriptionController', vendorSubscriptionController);

	vendorSubscriptionController.$inject = ['GdsnApi','ngTableParams', '$scope'];

	function vendorSubscriptionController(gdsnApi, ngTableParams, $scope) {

		var self = this;

		self.PAGE_SIZE = 15;
		self.WAIT_TIME = 120;
		self.GLN = "GLN";
		self.VENDOR_NAME = "vendor name";
		self.subscriptionStatusList = ["ADD", "DELETE", "MODIFY"];

		/**
		 * Text the user is searching for.
		 *
		 * @type {string}
		 */
		self.searchText = "";

		// State related
		/**
		 * The type of search the user wants to do.
		 *
		 * @type {string}
		 */
		self.selectionType = self.GLN;

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
		self.findAll = true;
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

		// Related to the popup to add a vendor
		/**
		 * The GLN of the vendor the user is adding.
		 *
		 * @type {string}
		 */
		self.newVendorGln = "";

		/**
		 * The name of the vendor the user is adding.
		 *
		 * @type {string}
		 */
		self.newVendorName = "";

		/**
		 * The status of the subscription the user is adding.
		 *
		 * @type {string}
		 */
		self.newSubscriptionStatus = "";

		/**
		 * Whether or not a new subscription should include RECEIVED CICs.
		 *
		 * @type {boolean}
		 */
		self.newCicReceived = true;

		/**
		 * Whether or not a new subscription should include REVIEW CICs.
		 *
		 * @type {boolean}
		 */
		self.newCicReview = true;

		/**
		 * Whether or not a new subscription should include SYNCHRONIZED CICs.
		 *
		 * @type {boolean}
		 */
		self.newCicSynchronized = true;

		/**
		 * When typing in a GLN, if an existing one is found, this gets set to true.
		 *
		 * @type {boolean}
		 */
		self.newGlnFound = false;
		/**
		 * Reset the searchText when user switch the radio button.
		 */
		self.resetData = function(){
			self.searchText = null;
		};

		/**
		 * Called by the UI to add a vendor subscription.
		 */
		self.addVendor = function() {
			// The object sent back maps to a VendorSubscription on the back end.
			gdsnApi.saveVendorSubscription(
				{vendorGln: self.newVendorGln, vendorName: self.newVendorName,
					subscriptionStatus: self.newSubscriptionStatus, sendReceivedCic: self.newCicReceived,
					sendReviewCic: self.newCicReview, sendSynchronizedCic: self.newCicSynchronized},
				self.addSuccess, self.addError
			);
		};

		/**
		 * Checks whether or not there is enough information to successfully send a vendor to the backend.
		 *
		 * @returns {boolean}
		 */
		self.isValidVendor = function() {
			return self.newVendorGln != "" && self.newVendorName != "" && self.newSubscriptionStatus != ""
		};

		/**
		 * Callback for when a vendor add is successful.
		 *
		 * @param results The response from the back end.
		 */
		self.addSuccess = function(results)  {
			self.success = results.message;
			self.resetNewVendorData();
		};

		/**
		 * Callback for when a vendor add fails.
		 *
		 * @param results The response from the back end.
		 */
		self.addError = function(results) {
			self.error = results.data.message;
			self.resetNewVendorData();
		};

		/**
		 * Resets all the values related to a new vendor to the defaults.
		 */
		self.resetNewVendorData = function() {
			self.newVendorGln = "";
			self.newVendorName = "";
			self.newSubscriptionStatus = "";
			self.newCicReceived = true;
			self.newCicReview = true;
			self.newCicSynchronized = true;
			self.newGlnFound = false;
		};

		/**
		 * Initiates a new search.
		 *
		 * @param findAll Should this search return all vendor subscriptions?
		 */
		self.newSearch = function(findAll) {
			self.error = null;
			self.success = null;
			self.findAll = findAll;
			self.firstSearch = true;
			if (self.tableParams == null) {
				self.buildTable();
			} else {
				self.tableParams.reload();
			}
		};

		/**
		 * Resets the search to be empty.
		 */
		self.clearSearch = function() {
			self.searchText = "";
		};

		/**
		 * Returns the placeholder text to put in the search box when the user hasn't entered anything.
		 *
		 * @returns {string}
		 */
		self.getTextPlaceHolder = function() {
			return "Please enter a " + self.selectionType + " to search for.";
		};

		/**
		 * Remove the Leading Zeros of searchText
		 */
		self.removeLeadingZeros = function(text){
		    return text.replace(/^0+/, '');
		};

		/**
		 * Initiates a call to get the list of vendor subscription recrods.
		 *
		 * @param includeCounts Whether or not to include getting record counts.
		 * @param page The page of data to ask for.
		 */
		self.fetchData = function(includeCounts, page) {
			if (self.findAll) {
				gdsnApi.queryVendorSubscriptions({
					page: page,
					pageSize: self.PAGE_SIZE,
					includeCounts: includeCounts
				}, self.loadData, self.fetchError);
			}
			else {
				if (self.selectionType == self.GLN) {
					gdsnApi.queryVendorSubscriptionsByGln({
						gln: self.removeLeadingZeros(self.searchText),
						page: page, pageSize: self.PAGE_SIZE,
						includeCounts: includeCounts
					}, self.loadData, self.fetchError);
				} else {
					gdsnApi.queryVendorSubscriptionsByVendorName({
						vendorName: self.searchText,
						page: page, pageSize: self.PAGE_SIZE,
						includeCounts: includeCounts
					}, self.loadData, self.fetchError);
				}
			}
		};

		self.addSearchGln = function() {
			self.newGlnFound = false;
			gdsnApi.queryVendorSubscriptionsByGln({page: 0, pageSize: 1, includeCounts: false, gln: self.newVendorGln},
			self.foundGln, self.fetchError);
		};

		self.foundGln = function(results){
			if (results.data.length == 1) {
				self.newVendorName = results.data[0].vendorName;
				self.newSubscriptionStatus = "DELETE";
				self.newCicReview = results.data[0].sendReviewCic;
				self.newCicReceived = results.data[0].sendReceivedCic;
				self.newCicSynchronized = results.data[0].sendSynchronizedCic;
				self.newGlnFound = true;
			}
		};

		/**
		 * Callback for when data is successfully returned from the backend.
		 *
		 * @param results The data returned from the backend.
		 */
		self.loadData = function(results) {
			self.isWaiting = false;
			self.data = results.data;
			self.startRecord = results.page * self.PAGE_SIZE;
			self.recordsVisible = results.data.length;
			self.defer.resolve(self.data);

			if (results.complete) {
				self.totalPages = results.pageCount;
				self.totalRecords = results.recordCount;
				self.dataResolvingParams.total(self.totalRecords);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.error = "No subscription found";
			}else{
				self.error = null;
			}
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
	}

})();
