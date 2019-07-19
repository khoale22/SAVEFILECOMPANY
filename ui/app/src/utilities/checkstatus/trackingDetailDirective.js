/*
 * trackingDetailPanel.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 *
 */

/**
 * Directive to support the check status by tracking id.
 *
 * @author vn87351
 * @since 2.14.0
 */
'use strict';

(function() {
	var app = angular.module('productMaintenanceUiApp');
	app.directive('trackingDetailPanel', trackingDetailPanel);
	trackingDetailPanel.$inject = ['checkstatusApi','ngTableParams','DownloadService','urlBase'];

	function trackingDetailPanel(checkstatusApi, ngTableParams,downloadService,urlBase) {
		return {
			templateUrl: 'src/utilities/checkstatus/trackingdetail.html',
			scope: {
				selectedItem: '='
			},
			replace:true,
			controllerAs: 'trackingDetailDirective',
			controller: function ($scope) {
				$scope.actionUrl =urlBase+'/pm/batchUpload/exportToCsv?trackingId=';
				var self = this;
				self.PAGE_SIZE = 10;

				/**
				 * Tracks whether or not the user is waiting for data from the back end.
				 * @type {boolean}
				 */
				self.isWaiting = false;
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
				 * init directive
				 */
				self.firstSearch=true;
				self.init = function () {
				//	$scope.actionUrl=urlBase+'/pm/batchUpload/exportToCsv?trackingId='+self.selectedItem.trackingId;
				};

				/**
				 *If there is an error this will display the error
				 * @param error
				 */
				self.fetchError = function (error) {
					self.isWaiting = false;
					self.data = null;
					if (error && error.data) {
						if (error.data.message != null && error.data.message != "") {
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
				 * Sets the controller's error message.
				 *
				 * @param error The error message.
				 */
				self.setError = function(error) {
					self.modifyMessage = "";
					self.modifyError = null;
					self.error = error;
				};
				/**
				 * Constructs the ng-table.
				 */
				self.buildTable = function () {
					return new ngTableParams(
						{
							page: 1,
							count: self.PAGE_SIZE
						}, {
							counts: [],
							getData: function ($defer, params) {
								self.data = null;
								self.isWaiting = true;
								self.defer = $defer;
								self.dataResolvingParams = params;
								self.includeCounts = false;
								if (self.firstSearch) {
									self.includeCounts = true;
									self.firstSearch = false;
								}
								self.getTrackingDetail(params.page()-1);
							}
						}
					)
				};
				/**
				 * table params for tracking status detail
				 */
				//self.tableParams = self.buildTable();
				/**
				 * call backend get tracking detail
				 * @param page
				 */
				self.getTrackingDetail = function(page){
					checkstatusApi.getTrackingDetail({page: page,
						pageSize: self.PAGE_SIZE,includeCounts: self.includeCounts, trackingId:self.selectedItem.requestId},self.loadData,self.fetchError);
				};
				/**
				 * Callback for a successful call to get tracking data from the backend.
				 *
				 * @param results The data returned by the backend.
				 */
				self.loadData = function (results) {
					self.isWaiting = false;
					self.error = null;
					if(!self.searchAfterHandle){
						self.success = null;
					} else {
						self.success = self.messageWaiting;
					}

					// If this was the first page, it includes record count and total pages .
					if (results.complete) {
						self.totalRecordCount = results.recordCount;
						self.dataResolvingParams.total(self.totalRecordCount);
					}

					if (results.length === 0) {
						self.isWaiting = false;
						self.data = null;
						self.dataResolvingParams.data = [];
						if(!self.searchAfterHandle){
							self.error = "No records found.";
						}
					} else {
						self.error = null;
						self.data = results.data;
						self.defer.resolve(results.data);
					}
					if(self.searchAfterHandle){
						self.searchAfterHandle = false;
					}
				};

				/**
				 *If there are no errors build this method will initiate the table construction
				 * @param results
				 */
				self.loadTable = function (results) {
					//	self.buildTable();
					self.isWaiting = false;
				};

				/**
				 * watcher for selected tracking
				 */
				$scope.$watch('selectedItem', function(value){
					self.firstSearch=true;
					self.selectedItem = $scope.selectedItem;
					self.data = null;
					if(self.dataResolvingParams!=null){
						self.dataResolvingParams.data = [];
					}
					if(self.tableParams == null){
						self.tableParams = self.buildTable();
					}else{
						self.tableParams.page(1);
						self.tableParams.reload();
					}
				});
				/**
				 * Max time to wait for csv download.
				 * @type {number}
				 */
				self.WAIT_TIME = 1200;
				/**
				 * Initiates a download of all the records.
				 */
				self.export = function() {
					self.downloading = true;
					downloadService.export(self.generateExportUrl(), 'trackingDetail.csv', self.WAIT_TIME,
						function() { self.downloading = false; });
				};

				/**
				 * Generates the URL to ask for the export.
				 * @returns {string} The URL to ask for the export.
				 */
				self.generateExportUrl = function() {
					return urlBase + '/pm/batchUpload/exportToCsv?trackingId=' + self.selectedItem.requestId;
				};
			}
		}
	}
})();
