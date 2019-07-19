/*
 * scaleMaintenanceCheckStatus.js
 *
 *  Copyright (c) 2018 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */
'use strict';

/**
 * Controller to support the scale maintenance status check of ePlum transactions.
 *
 * @author m314029
 * @since 2.18.3
 */
(function() {
	angular.module('productMaintenanceUiApp').controller('ScaleMaintenanceStatusCheckController', scaleMaintenanceStatusCheckController);

	scaleMaintenanceStatusCheckController.$inject = ['$stateParams', 'ngTableParams', 'ScaleMaintenanceApi', '$scope', '$interval', 'DownloadService', 'urlBase'];

	/**
	 * Creates the controller for the scale maintenance loads.
	 */
	function scaleMaintenanceStatusCheckController($stateParams, ngTableParams, scaleMaintenanceApi, $scope, $interval, downloadService, urlBase){

		var self = this;
		/**
		 * Whether or not the controller is waiting for data
		 * @type {boolean}
		 */
		self.isWaiting = false;
		self.isTransmitWaiting = false;
		self.isRetailWaiting = false;
		/**
		 * selected tracking id
		 * @type {number}
		 */
		self.selectedTransactionId = null;
		/**
		 * selected store id
		 * @type {number}
		 */
		self.selectedStoreId = null;
		/**
		 * Whether this is a first search or not.
		 *
		 * @type {boolean}
		 */
		var firstSearch = true;
		var firstTransmitSearch = true;
		var firstRetailSearch = true;
		/**
		 * Table params for transactions.
		 *
		 * @type {null}
		 */
		self.transactionTableParams = null;
		self.autoRefreshData = false;

		/**
		 * Table params for retails.
		 *
		 * @type {null}
		 */
		self.retailTableParams = null;
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
		 * Currently selected scale maintenance object.
         * @type {null}
         */
        self.selectedScaleMaintenance = null;

        /**
		 * Binary determining the visibility of store info display.
         * @type {boolean}
         */
        self.isDisplayingStoreUpcInfo = false;

		self.columns = [
			{
				title: 'Request ID',
				field: 'transactionId',
				visible: true},
			{
				title: 'Maintenance Type',
				field: 'updtDescription',
				visible: true},
			{
				title: 'Date & Time of Request',
				field: 'createTime',
				visible: true},
			{
				title: 'Status',
				field: 'status',
				visible: true},
			{
				title: 'Transmit status',
				field: 'result',
				visible: true},
			{
				title: 'User',
				field: 'userId',
				visible: true}];

		self.transmitColumns = [
			{
				title: 'Store',
				field: 'key.store',
				visible: true},
			{
				title: 'EPlum Batch ID',
				field: 'ePlumBatchId',
				visible: true},
			{
				title: 'Status',
				field: 'status',
				visible: true},
			{
				title: 'Authorization status',
				field: 'result',
				visible: true},
			{
				title: 'EPlum Response Message',
				field: 'responseMessage',
				visible: true}];

		self.retailColumns = [
			{
				title: 'UPC',
				field: 'upc',
				visible: true},
			{
				title: 'Authorized',
				field: 'authorizedSwitch',
				visible: true},
			{
				title: 'Retail',
				field: 'retailAmount',
				visible: true},
			{
				title: 'Error Message',
				field: 'error',
				visible: true}];

		const PAGE_SIZE = 10;
		const FIRST_PAGE = 1;

		/**
		 * Component ngOnInit lifecycle hook. This lifecycle is executed every time the component is initialized
		 * (or re-initialized).
		 */
		this.$onInit = function () {
			self.isWaiting = true;
			if($stateParams && $stateParams.transactionId){
				self.selectedTransactionId = $stateParams.transactionId;
			}
			self.transactionTableParams = buildTable();
			self.transmitTableParams = buildTransmitTable();
			self.retailTableParams = buildRetailTable();
		};

		/**
		 * Constructs the ng-table.
		 */
		function buildTable() {
			return new ngTableParams(
				{
					page: FIRST_PAGE,
					count: PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.data = null;
						self.isWaiting = true;
						self.defer = $defer;
						self.dataResolvingParams = params;
						self.includeCounts = false;
						if (firstSearch) {
							self.includeCounts = true;
							firstSearch = false;
						}
						getTrackingData(params.page()-1);
					}
				}
			)
		}
		/**
		 * Constructs the ng-table.
		 */
		function buildTransmitTable() {
			return new ngTableParams(
				{
					page: FIRST_PAGE,
					count: PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.transmitData = null;
						self.isTransmitWaiting = true;
						self.transmitDefer = $defer;
						self.transmitDataResolvingParams = params;
						self.transmitIncludeCounts = false;
						if (firstTransmitSearch) {
							self.transmitIncludeCounts = true;
							firstTransmitSearch = false;
						}
						getTransmitData(params.page()-1);
					}
				}
			)
		}

		/**
		 * Constructs the ng-table.
		 */
		function buildRetailTable() {
			return new ngTableParams(
				{
					page: FIRST_PAGE,
					count: PAGE_SIZE
				}, {
					counts: [],
					getData: function ($defer, params) {
						self.retailData = null;
						self.isRetailWaiting = true;
						self.retailDefer = $defer;
						self.retailDataResolvingParams = params;
						self.retailIncludeCounts = false;
						if (firstRetailSearch) {
							self.retailIncludeCounts = true;
							firstRetailSearch = false;
						}
						getRetailData(params.page()-1); // make one for retail
					}
				}
			)
		}

		/**
		 * Get tracking info from database with page number
		 * @param page
		 */
		function getTrackingData(page) {
			if(self.selectedTransactionId){
				scaleMaintenanceApi.checkStatusFindOneTransaction({
					transactionId: self.selectedTransactionId
				},{}).$promise.then(loadSingleData).catch(function(error) {
					fetchError(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
			} else {
				scaleMaintenanceApi.checkStatusFindAllTransactions({
					includeCount: self.includeCounts,
					page: page,
					pageSize: PAGE_SIZE
				}).$promise.then(loadData).catch(function(error) {
					fetchError(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
			}
		}

		/**
		 * Get tracking info from database with page number
		 * @param page
		 */
		function getTransmitData(page) {
			if(self.selectedTransactionId){
				scaleMaintenanceApi.checkStatusFindAllTransmits({
					transactionId: self.selectedTransactionId,
					includeCount: self.transmitIncludeCounts,
					page: page,
					pageSize: PAGE_SIZE
				}).$promise.then(loadTransmitData).catch(function(error) {
					fetchError(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
			}
		}

		/**
		 * Builds error message object, then calls fetch error function.
		 *
		 * @param message Error message to display to user.
		 */
		function buildErrorWithMessage (message) {
			fetchError({data: {message: message}});
		}

		/**
		 * Callback for a successful call to get list tracking data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		function loadData (results) {
			self.isWaiting = false;
			self.error = null;
			firstSearch = false;

			// If this was the first page, it includes record count and total pages .
			if (results.complete) {
				self.totalRecordCount = results.recordCount;
				self.dataResolvingParams.total(self.totalRecordCount);
			}
			if (results.data.length === 0) {
				self.data = null;
				self.dataResolvingParams.data = [];
				buildErrorWithMessage("No records found.");
			} else {
				self.error = null;
				self.data = results.data;
				self.defer.resolve(results.data);
			}
		}

		/**
		 * Callback for a successful call to get list transmit data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		function loadTransmitData (results) {
			self.isTransmitWaiting = false;
			self.error = null;
			firstTransmitSearch = false;

			// If this was the first page, it includes record count and total pages .
			if (results.complete) {
				self.totalTransmitRecordCount = results.recordCount;
				self.transmitDataResolvingParams.total(self.totalTransmitRecordCount);
			}
			if (results.data.length === 0) {
				self.transmitData = null;
				self.transmitDataResolvingParams.data = [];
				buildErrorWithMessage("No records found.");
			} else {
				self.error = null;
				self.transmitData = results.data;
				self.transmitDefer.resolve(results.data);
			}
		}

		/**
		 * Callback for a successful call to get single tracking data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		function loadSingleData (results) {
			self.isWaiting = false;
			self.error = null;

			self.totalRecordCount = 1;
			self.dataResolvingParams.total(1);

			self.data = [];
			self.data.push(results);
			self.defer.resolve(results.data);
		}

		/**
		 * User selected tracking id.
		 **/
		self.selectTrackingId = function(data){
			self.selectedTransactionId = data.transactionId;
			firstTransmitSearch = true;
			self.transmitTableParams.page(1);
			self.transmitTableParams.reload();

			self.selectedStoreId = null;
			firstRetailSearch = true;
			self.retailData = null;
		};

		/**
		 * Callback for when the backend returns an error.
		 *
		 * @param error The error from the backend.
		 */
		function fetchError(error) {
			self.isWaiting = false;
			self.isTransmitWaiting = false;
			if (error && error.data) {
				if(error.data.message) {
					setError(error.data.message);
				} else {
					setError(error.data.error);
				}
			}
			else {
				setError("An unknown error occurred.");
			}
		}

		/**
		 * Sets the controller's error message.
		 *
		 * @param error The error message.
		 */
		function setError(error) {
			self.error = error;
		}

		/**
		 * When auto refresh checkbox changes value, start 'auto' check for status if self.autoRefreshData is true; else
		 * stop 'auto' check.
		 */
		self.checkAutoRefresh = function(){
			if(self.autoRefreshData){
				$scope.start();
			}else{
				$scope.stop();
			}
		};

		/**
		 * Stops the interval that is periodically checking status.
		 */
		$scope.stop = function() {
			$interval.cancel(self.promise);
		};

		/**
		 * Starts the interval that checks status on scale maintenance automatically every 10 seconds.
		 */
		$scope.start = function() {
			// stops any running interval to avoid two intervals running at the same time
			$scope.stop();

			// store the interval promise
			self.promise=$interval(self.refreshData,10000);
		};

		/**
		 * Manually refresh the data.
		 */
		self.refreshData = function(){
			self.transactionTableParams.page(self.transactionTableParams.page());
			self.transactionTableParams.reload();
			self.transmitTableParams.page(self.transmitTableParams.page());
			self.transmitTableParams.reload();
			self.retailTableParams.page(self.retailTableParams.page());
			self.retailTableParams.reload()
		};

		/**
		 * Manually refresh the data.
		 */
		self.clearResult = function(){
			self.selectedTransactionId = null;
			firstSearch = true;
			self.transactionTableParams.page(1);
			self.transactionTableParams.reload();
			firstTransmitSearch = true;
			self.transmitData = [];
			firstRetailSearch = true;
			self.selectedStoreId = null;
			self.retailData = [];
		};

		/**
		 * User selected tracking id.
		 **/
		self.selectStoreId = function(data){
			self.selectedStoreId = data.key.store;
			firstRetailSearch = true;
			self.retailTableParams.page(1);
			self.retailTableParams.reload()
		};

		/**
		 * Get tracking info from database with page number
		 * @param page
		 */
		function getRetailData(page) {
			if(self.selectedStoreId){
				scaleMaintenanceApi.findAllScaleMaintenanceByStoreAndTransactionId({
					transactionId: self.selectedTransactionId,
					store: self.selectedStoreId,
					includeCount: self.transmitIncludeCounts,
					page: page,
					pageSize: PAGE_SIZE
				}).$promise.then(loadRetailData).catch(function(error) {
					fetchError(error);
				}).finally(function(){
					PMCommons.displayLoading(self, false);
				});
			}
		}

		/**
		 * Callback for a successful call to get list retail data from the backend.
		 *
		 * @param results The data returned by the backend.
		 */
		function loadRetailData (results) {
			self.isRetailWaiting = false;
			self.error = null;
			firstRetailSearch = false;

			// If this was the first page, it includes record count and total pages .
			if (results.complete) {
				self.totalRetailRecordCount = results.recordCount;
				self.retailDataResolvingParams.total(self.totalRetailRecordCount);
			}
			if (results.data.length === 0) {
				self.retailData = null;
				self.retailDataResolvingParams.data = [];
				buildErrorWithMessage("No records found.");
			} else {
				self.error = null;
				self.retailData = results.data;
				self.retailDefer.resolve(results.data);
			}
		}

		self.selectScaleMaintenance = function(scaleMaintenance) {
			self.selectedScaleMaintenance=scaleMaintenance;
			self.isDisplayingStoreUpcInfo = true;
		};

		self.onReturn = function() {
			self.selectedScaleMaintenance = null;
			self.isDisplayingStoreUpcInfo = false;
		};

		/**
		 * Initiates a download of all the records.
		 */
		self.export = function() {
			var encodedUri = urlBase + '/pm/scaleMaintenance/checkStatus/exportAllRetailsByStore?store=' +
				encodeURI(self.selectedStoreId) + "&totalRecordCount=" + self.totalRetailRecordCount+ "&transactionId="
				+ self.selectedTransactionId;
			var date = new Date();
			if(encodedUri !== self.EMPTY_STRING) {
				self.downloading = true;
				downloadService.export(encodedUri, 'SCALE_INFO_' +
					self.selectedStoreId + '_' + (date.getMonth()+1) + date.getDate() + date.getFullYear() + '.csv', self.WAIT_TIME,
					function () {
						self.downloading = false;
					});
			}
		};

	}
})();
